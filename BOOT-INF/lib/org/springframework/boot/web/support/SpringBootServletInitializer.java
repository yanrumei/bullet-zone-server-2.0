/*     */ package org.springframework.boot.web.support;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletContextEvent;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.builder.ParentContextApplicationContextInitializer;
/*     */ import org.springframework.boot.builder.SpringApplicationBuilder;
/*     */ import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.WebApplicationInitializer;
/*     */ import org.springframework.web.context.ContextLoaderListener;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.StandardServletEnvironment;
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
/*     */ public abstract class SpringBootServletInitializer
/*     */   implements WebApplicationInitializer
/*     */ {
/*     */   protected Log logger;
/*  70 */   private boolean registerErrorPageFilter = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setRegisterErrorPageFilter(boolean registerErrorPageFilter)
/*     */   {
/*  79 */     this.registerErrorPageFilter = registerErrorPageFilter;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onStartup(ServletContext servletContext)
/*     */     throws ServletException
/*     */   {
/*  86 */     this.logger = LogFactory.getLog(getClass());
/*  87 */     WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);
/*     */     
/*  89 */     if (rootAppContext != null) {
/*  90 */       servletContext.addListener(new ContextLoaderListener(rootAppContext)
/*     */       {
/*     */ 
/*     */         public void contextInitialized(ServletContextEvent event) {}
/*     */ 
/*     */       });
/*     */     }
/*     */     else {
/*  98 */       this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected WebApplicationContext createRootApplicationContext(ServletContext servletContext)
/*     */   {
/* 106 */     SpringApplicationBuilder builder = createSpringApplicationBuilder();
/* 107 */     StandardServletEnvironment environment = new StandardServletEnvironment();
/* 108 */     environment.initPropertySources(servletContext, null);
/* 109 */     builder.environment(environment);
/* 110 */     builder.main(getClass());
/* 111 */     ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
/* 112 */     if (parent != null) {
/* 113 */       this.logger.info("Root context already created (using as parent).");
/* 114 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
/*     */       
/* 116 */       builder.initializers(new ApplicationContextInitializer[] { new ParentContextApplicationContextInitializer(parent) });
/*     */     }
/* 118 */     builder.initializers(new ApplicationContextInitializer[] { new ServletContextApplicationContextInitializer(servletContext) });
/*     */     
/* 120 */     builder.contextClass(AnnotationConfigEmbeddedWebApplicationContext.class);
/* 121 */     builder = configure(builder);
/* 122 */     SpringApplication application = builder.build();
/* 123 */     if ((application.getSources().isEmpty()) && 
/* 124 */       (AnnotationUtils.findAnnotation(getClass(), Configuration.class) != null)) {
/* 125 */       application.getSources().add(getClass());
/*     */     }
/* 127 */     Assert.state(!application.getSources().isEmpty(), "No SpringApplication sources have been defined. Either override the configure method or add an @Configuration annotation");
/*     */     
/*     */ 
/*     */ 
/* 131 */     if (this.registerErrorPageFilter) {
/* 132 */       application.getSources().add(ErrorPageFilterConfiguration.class);
/*     */     }
/* 134 */     return run(application);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SpringApplicationBuilder createSpringApplicationBuilder()
/*     */   {
/* 145 */     return new SpringApplicationBuilder(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected WebApplicationContext run(SpringApplication application)
/*     */   {
/* 154 */     return (WebApplicationContext)application.run(new String[0]);
/*     */   }
/*     */   
/*     */   private ApplicationContext getExistingRootWebApplicationContext(ServletContext servletContext)
/*     */   {
/* 159 */     Object context = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/* 161 */     if ((context instanceof ApplicationContext)) {
/* 162 */       return (ApplicationContext)context;
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
/*     */   {
/* 177 */     return builder;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\support\SpringBootServletInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */