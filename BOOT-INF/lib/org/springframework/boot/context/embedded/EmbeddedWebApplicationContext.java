/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.Scope;
/*     */ import org.springframework.boot.web.servlet.ServletContextInitializer;
/*     */ import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.io.DefaultResourceLoader.ClassPathContextResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.ContextLoader;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*     */ import org.springframework.web.context.support.ServletContextResource;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmbeddedWebApplicationContext
/*     */   extends GenericWebApplicationContext
/*     */ {
/*  92 */   private static final Log logger = LogFactory.getLog(EmbeddedWebApplicationContext.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile EmbeddedServletContainer embeddedServletContainer;
/*     */   
/*     */ 
/*     */ 
/*     */   private ServletConfig servletConfig;
/*     */   
/*     */ 
/*     */ 
/*     */   private String namespace;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */   {
/* 114 */     beanFactory.addBeanPostProcessor(new WebApplicationContextServletContextAwareProcessor(this));
/*     */     
/* 116 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*     */   }
/*     */   
/*     */   public final void refresh() throws BeansException, IllegalStateException
/*     */   {
/*     */     try {
/* 122 */       super.refresh();
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 125 */       stopAndReleaseEmbeddedServletContainer();
/* 126 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onRefresh()
/*     */   {
/* 132 */     super.onRefresh();
/*     */     try {
/* 134 */       createEmbeddedServletContainer();
/*     */     }
/*     */     catch (Throwable ex) {
/* 137 */       throw new ApplicationContextException("Unable to start embedded container", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void finishRefresh()
/*     */   {
/* 144 */     super.finishRefresh();
/* 145 */     EmbeddedServletContainer localContainer = startEmbeddedServletContainer();
/* 146 */     if (localContainer != null) {
/* 147 */       publishEvent(new EmbeddedServletContainerInitializedEvent(this, localContainer));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onClose()
/*     */   {
/* 154 */     super.onClose();
/* 155 */     stopAndReleaseEmbeddedServletContainer();
/*     */   }
/*     */   
/*     */   private void createEmbeddedServletContainer() {
/* 159 */     EmbeddedServletContainer localContainer = this.embeddedServletContainer;
/* 160 */     ServletContext localServletContext = getServletContext();
/* 161 */     if ((localContainer == null) && (localServletContext == null)) {
/* 162 */       EmbeddedServletContainerFactory containerFactory = getEmbeddedServletContainerFactory();
/*     */       
/* 164 */       this.embeddedServletContainer = containerFactory.getEmbeddedServletContainer(new ServletContextInitializer[] {getSelfInitializer() });
/*     */     }
/* 166 */     else if (localServletContext != null) {
/*     */       try {
/* 168 */         getSelfInitializer().onStartup(localServletContext);
/*     */       }
/*     */       catch (ServletException ex) {
/* 171 */         throw new ApplicationContextException("Cannot initialize servlet context", ex);
/*     */       }
/*     */     }
/*     */     
/* 175 */     initPropertySources();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected EmbeddedServletContainerFactory getEmbeddedServletContainerFactory()
/*     */   {
/* 187 */     String[] beanNames = getBeanFactory().getBeanNamesForType(EmbeddedServletContainerFactory.class);
/* 188 */     if (beanNames.length == 0) {
/* 189 */       throw new ApplicationContextException("Unable to start EmbeddedWebApplicationContext due to missing EmbeddedServletContainerFactory bean.");
/*     */     }
/*     */     
/*     */ 
/* 193 */     if (beanNames.length > 1)
/*     */     {
/*     */ 
/*     */ 
/* 197 */       throw new ApplicationContextException("Unable to start EmbeddedWebApplicationContext due to multiple EmbeddedServletContainerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
/*     */     }
/* 199 */     return (EmbeddedServletContainerFactory)getBeanFactory().getBean(beanNames[0], EmbeddedServletContainerFactory.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ServletContextInitializer getSelfInitializer()
/*     */   {
/* 210 */     new ServletContextInitializer()
/*     */     {
/*     */       public void onStartup(ServletContext servletContext) throws ServletException {
/* 213 */         EmbeddedWebApplicationContext.this.selfInitialize(servletContext);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private void selfInitialize(ServletContext servletContext) throws ServletException {
/* 219 */     prepareEmbeddedWebApplicationContext(servletContext);
/* 220 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/* 221 */     ExistingWebApplicationScopes existingScopes = new ExistingWebApplicationScopes(beanFactory);
/*     */     
/* 223 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, 
/* 224 */       getServletContext());
/* 225 */     existingScopes.restore();
/* 226 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, 
/* 227 */       getServletContext());
/* 228 */     for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
/* 229 */       beans.onStartup(servletContext);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Collection<ServletContextInitializer> getServletContextInitializerBeans()
/*     */   {
/* 241 */     return new ServletContextInitializerBeans(getBeanFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void prepareEmbeddedWebApplicationContext(ServletContext servletContext)
/*     */   {
/* 252 */     Object rootContext = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/* 254 */     if (rootContext != null) {
/* 255 */       if (rootContext == this) {
/* 256 */         throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ServletContextInitializers!");
/*     */       }
/*     */       
/*     */ 
/* 260 */       return;
/*     */     }
/* 262 */     Log logger = LogFactory.getLog(ContextLoader.class);
/* 263 */     servletContext.log("Initializing Spring embedded WebApplicationContext");
/*     */     try {
/* 265 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this);
/*     */       
/* 267 */       if (logger.isDebugEnabled()) {
/* 268 */         logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 273 */       setServletContext(servletContext);
/* 274 */       if (logger.isInfoEnabled()) {
/* 275 */         long elapsedTime = System.currentTimeMillis() - getStartupDate();
/* 276 */         logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
/*     */       }
/*     */     }
/*     */     catch (RuntimeException ex)
/*     */     {
/* 281 */       logger.error("Context initialization failed", ex);
/* 282 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/*     */       
/* 284 */       throw ex;
/*     */     }
/*     */     catch (Error ex) {
/* 287 */       logger.error("Context initialization failed", ex);
/* 288 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/*     */       
/* 290 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   private EmbeddedServletContainer startEmbeddedServletContainer() {
/* 295 */     EmbeddedServletContainer localContainer = this.embeddedServletContainer;
/* 296 */     if (localContainer != null) {
/* 297 */       localContainer.start();
/*     */     }
/* 299 */     return localContainer;
/*     */   }
/*     */   
/*     */   private void stopAndReleaseEmbeddedServletContainer() {
/* 303 */     EmbeddedServletContainer localContainer = this.embeddedServletContainer;
/* 304 */     if (localContainer != null) {
/*     */       try {
/* 306 */         localContainer.stop();
/* 307 */         this.embeddedServletContainer = null;
/*     */       }
/*     */       catch (Exception ex) {
/* 310 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Resource getResourceByPath(String path)
/*     */   {
/* 317 */     if (getServletContext() == null) {
/* 318 */       return new DefaultResourceLoader.ClassPathContextResource(path, getClassLoader());
/*     */     }
/* 320 */     return new ServletContextResource(getServletContext(), path);
/*     */   }
/*     */   
/*     */   public void setNamespace(String namespace)
/*     */   {
/* 325 */     this.namespace = namespace;
/*     */   }
/*     */   
/*     */   public String getNamespace()
/*     */   {
/* 330 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig)
/*     */   {
/* 335 */     this.servletConfig = servletConfig;
/*     */   }
/*     */   
/*     */   public ServletConfig getServletConfig()
/*     */   {
/* 340 */     return this.servletConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmbeddedServletContainer getEmbeddedServletContainer()
/*     */   {
/* 349 */     return this.embeddedServletContainer;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ExistingWebApplicationScopes
/*     */   {
/*     */     private static final Set<String> SCOPES;
/*     */     
/*     */     private final ConfigurableListableBeanFactory beanFactory;
/*     */     
/*     */ 
/*     */     static
/*     */     {
/* 362 */       Set<String> scopes = new LinkedHashSet();
/* 363 */       scopes.add("request");
/* 364 */       scopes.add("session");
/* 365 */       scopes.add("globalSession");
/* 366 */       SCOPES = Collections.unmodifiableSet(scopes);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 371 */     private final Map<String, Scope> scopes = new HashMap();
/*     */     
/*     */     public ExistingWebApplicationScopes(ConfigurableListableBeanFactory beanFactory) {
/* 374 */       this.beanFactory = beanFactory;
/* 375 */       for (String scopeName : SCOPES) {
/* 376 */         Scope scope = beanFactory.getRegisteredScope(scopeName);
/* 377 */         if (scope != null) {
/* 378 */           this.scopes.put(scopeName, scope);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void restore() {
/* 384 */       for (Map.Entry<String, Scope> entry : this.scopes.entrySet()) {
/* 385 */         if (EmbeddedWebApplicationContext.logger.isInfoEnabled()) {
/* 386 */           EmbeddedWebApplicationContext.logger.info("Restoring user defined scope " + (String)entry.getKey());
/*     */         }
/* 388 */         this.beanFactory.registerScope((String)entry.getKey(), (Scope)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\EmbeddedWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */