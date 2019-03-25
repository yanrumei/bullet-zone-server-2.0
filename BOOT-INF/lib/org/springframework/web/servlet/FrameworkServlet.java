/*      */ package org.springframework.web.servlet;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.security.Principal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpServletResponseWrapper;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextAware;
/*      */ import org.springframework.context.ApplicationContextException;
/*      */ import org.springframework.context.ApplicationContextInitializer;
/*      */ import org.springframework.context.ApplicationListener;
/*      */ import org.springframework.context.ConfigurableApplicationContext;
/*      */ import org.springframework.context.event.ContextRefreshedEvent;
/*      */ import org.springframework.context.event.SourceFilteringListener;
/*      */ import org.springframework.context.i18n.LocaleContext;
/*      */ import org.springframework.context.i18n.LocaleContextHolder;
/*      */ import org.springframework.context.i18n.SimpleLocaleContext;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.http.HttpMethod;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*      */ import org.springframework.web.context.ConfigurableWebEnvironment;
/*      */ import org.springframework.web.context.WebApplicationContext;
/*      */ import org.springframework.web.context.request.NativeWebRequest;
/*      */ import org.springframework.web.context.request.RequestAttributes;
/*      */ import org.springframework.web.context.request.RequestContextHolder;
/*      */ import org.springframework.web.context.request.ServletRequestAttributes;
/*      */ import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;
/*      */ import org.springframework.web.context.request.async.WebAsyncManager;
/*      */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*      */ import org.springframework.web.context.support.ServletRequestHandledEvent;
/*      */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*      */ import org.springframework.web.context.support.XmlWebApplicationContext;
/*      */ import org.springframework.web.cors.CorsUtils;
/*      */ import org.springframework.web.util.NestedServletException;
/*      */ import org.springframework.web.util.WebUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class FrameworkServlet
/*      */   extends HttpServletBean
/*      */   implements ApplicationContextAware
/*      */ {
/*      */   public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
/*  150 */   public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  156 */   public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  167 */   private static final boolean responseGetStatusAvailable = ClassUtils.hasMethod(HttpServletResponse.class, "getStatus", new Class[0]);
/*      */   
/*      */ 
/*      */ 
/*      */   private String contextAttribute;
/*      */   
/*      */ 
/*  174 */   private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
/*      */   
/*      */ 
/*      */   private String contextId;
/*      */   
/*      */ 
/*      */   private String namespace;
/*      */   
/*      */ 
/*      */   private String contextConfigLocation;
/*      */   
/*      */ 
/*  186 */   private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */   private String contextInitializerClasses;
/*      */   
/*      */ 
/*  193 */   private boolean publishContext = true;
/*      */   
/*      */ 
/*  196 */   private boolean publishEvents = true;
/*      */   
/*      */ 
/*  199 */   private boolean threadContextInheritable = false;
/*      */   
/*      */ 
/*  202 */   private boolean dispatchOptionsRequest = false;
/*      */   
/*      */ 
/*  205 */   private boolean dispatchTraceRequest = false;
/*      */   
/*      */ 
/*      */   private WebApplicationContext webApplicationContext;
/*      */   
/*      */ 
/*  211 */   private boolean webApplicationContextInjected = false;
/*      */   
/*      */ 
/*  214 */   private boolean refreshEventReceived = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FrameworkServlet() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FrameworkServlet(WebApplicationContext webApplicationContext)
/*      */   {
/*  278 */     this.webApplicationContext = webApplicationContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextAttribute(String contextAttribute)
/*      */   {
/*  287 */     this.contextAttribute = contextAttribute;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getContextAttribute()
/*      */   {
/*  295 */     return this.contextAttribute;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextClass(Class<?> contextClass)
/*      */   {
/*  308 */     this.contextClass = contextClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Class<?> getContextClass()
/*      */   {
/*  315 */     return this.contextClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextId(String contextId)
/*      */   {
/*  323 */     this.contextId = contextId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getContextId()
/*      */   {
/*  330 */     return this.contextId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNamespace(String namespace)
/*      */   {
/*  338 */     this.namespace = namespace;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNamespace()
/*      */   {
/*  346 */     return getServletName() + "-servlet";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextConfigLocation(String contextConfigLocation)
/*      */   {
/*  355 */     this.contextConfigLocation = contextConfigLocation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getContextConfigLocation()
/*      */   {
/*  362 */     return this.contextConfigLocation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextInitializers(ApplicationContextInitializer<?>... initializers)
/*      */   {
/*  373 */     if (initializers != null) {
/*  374 */       for (ApplicationContextInitializer<?> initializer : initializers) {
/*  375 */         this.contextInitializers.add(initializer);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContextInitializerClasses(String contextInitializerClasses)
/*      */   {
/*  387 */     this.contextInitializerClasses = contextInitializerClasses;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPublishContext(boolean publishContext)
/*      */   {
/*  397 */     this.publishContext = publishContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPublishEvents(boolean publishEvents)
/*      */   {
/*  407 */     this.publishEvents = publishEvents;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setThreadContextInheritable(boolean threadContextInheritable)
/*      */   {
/*  423 */     this.threadContextInheritable = threadContextInheritable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDispatchOptionsRequest(boolean dispatchOptionsRequest)
/*      */   {
/*  443 */     this.dispatchOptionsRequest = dispatchOptionsRequest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDispatchTraceRequest(boolean dispatchTraceRequest)
/*      */   {
/*  460 */     this.dispatchTraceRequest = dispatchTraceRequest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setApplicationContext(ApplicationContext applicationContext)
/*      */   {
/*  474 */     if ((this.webApplicationContext == null) && ((applicationContext instanceof WebApplicationContext))) {
/*  475 */       this.webApplicationContext = ((WebApplicationContext)applicationContext);
/*  476 */       this.webApplicationContextInjected = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void initServletBean()
/*      */     throws ServletException
/*      */   {
/*  487 */     getServletContext().log("Initializing Spring FrameworkServlet '" + getServletName() + "'");
/*  488 */     if (this.logger.isInfoEnabled()) {
/*  489 */       this.logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
/*      */     }
/*  491 */     long startTime = System.currentTimeMillis();
/*      */     try
/*      */     {
/*  494 */       this.webApplicationContext = initWebApplicationContext();
/*  495 */       initFrameworkServlet();
/*      */     }
/*      */     catch (ServletException ex) {
/*  498 */       this.logger.error("Context initialization failed", ex);
/*  499 */       throw ex;
/*      */     }
/*      */     catch (RuntimeException ex) {
/*  502 */       this.logger.error("Context initialization failed", ex);
/*  503 */       throw ex;
/*      */     }
/*      */     
/*  506 */     if (this.logger.isInfoEnabled()) {
/*  507 */       long elapsedTime = System.currentTimeMillis() - startTime;
/*  508 */       this.logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " + elapsedTime + " ms");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebApplicationContext initWebApplicationContext()
/*      */   {
/*  524 */     WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/*  525 */     WebApplicationContext wac = null;
/*      */     
/*  527 */     if (this.webApplicationContext != null)
/*      */     {
/*  529 */       wac = this.webApplicationContext;
/*  530 */       if ((wac instanceof ConfigurableWebApplicationContext)) {
/*  531 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)wac;
/*  532 */         if (!cwac.isActive())
/*      */         {
/*      */ 
/*  535 */           if (cwac.getParent() == null)
/*      */           {
/*      */ 
/*  538 */             cwac.setParent(rootContext);
/*      */           }
/*  540 */           configureAndRefreshWebApplicationContext(cwac);
/*      */         }
/*      */       }
/*      */     }
/*  544 */     if (wac == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  549 */       wac = findWebApplicationContext();
/*      */     }
/*  551 */     if (wac == null)
/*      */     {
/*  553 */       wac = createWebApplicationContext(rootContext);
/*      */     }
/*      */     
/*  556 */     if (!this.refreshEventReceived)
/*      */     {
/*      */ 
/*      */ 
/*  560 */       onRefresh(wac);
/*      */     }
/*      */     
/*  563 */     if (this.publishContext)
/*      */     {
/*  565 */       String attrName = getServletContextAttributeName();
/*  566 */       getServletContext().setAttribute(attrName, wac);
/*  567 */       if (this.logger.isDebugEnabled()) {
/*  568 */         this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() + "' as ServletContext attribute with name [" + attrName + "]");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  573 */     return wac;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebApplicationContext findWebApplicationContext()
/*      */   {
/*  587 */     String attrName = getContextAttribute();
/*  588 */     if (attrName == null) {
/*  589 */       return null;
/*      */     }
/*      */     
/*  592 */     WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/*  593 */     if (wac == null) {
/*  594 */       throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
/*      */     }
/*  596 */     return wac;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebApplicationContext createWebApplicationContext(ApplicationContext parent)
/*      */   {
/*  615 */     Class<?> contextClass = getContextClass();
/*  616 */     if (this.logger.isDebugEnabled()) {
/*  617 */       this.logger.debug("Servlet with name '" + getServletName() + "' will try to create custom WebApplicationContext context of class '" + contextClass
/*      */       
/*  619 */         .getName() + "', using parent context [" + parent + "]");
/*      */     }
/*  621 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass))
/*      */     {
/*      */ 
/*  624 */       throw new ApplicationContextException("Fatal initialization error in servlet with name '" + getServletName() + "': custom WebApplicationContext class [" + contextClass.getName() + "] is not of type ConfigurableWebApplicationContext");
/*      */     }
/*      */     
/*      */ 
/*  628 */     ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
/*      */     
/*  630 */     wac.setEnvironment(getEnvironment());
/*  631 */     wac.setParent(parent);
/*  632 */     wac.setConfigLocation(getContextConfigLocation());
/*      */     
/*  634 */     configureAndRefreshWebApplicationContext(wac);
/*      */     
/*  636 */     return wac;
/*      */   }
/*      */   
/*      */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
/*  640 */     if (ObjectUtils.identityToString(wac).equals(wac.getId()))
/*      */     {
/*      */ 
/*  643 */       if (this.contextId != null) {
/*  644 */         wac.setId(this.contextId);
/*      */       }
/*      */       else
/*      */       {
/*  648 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/*  649 */           ObjectUtils.getDisplayString(getServletContext().getContextPath()) + '/' + getServletName());
/*      */       }
/*      */     }
/*      */     
/*  653 */     wac.setServletContext(getServletContext());
/*  654 */     wac.setServletConfig(getServletConfig());
/*  655 */     wac.setNamespace(getNamespace());
/*  656 */     wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener(null)));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  661 */     ConfigurableEnvironment env = wac.getEnvironment();
/*  662 */     if ((env instanceof ConfigurableWebEnvironment)) {
/*  663 */       ((ConfigurableWebEnvironment)env).initPropertySources(getServletContext(), getServletConfig());
/*      */     }
/*      */     
/*  666 */     postProcessWebApplicationContext(wac);
/*  667 */     applyInitializers(wac);
/*  668 */     wac.refresh();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected WebApplicationContext createWebApplicationContext(WebApplicationContext parent)
/*      */   {
/*  682 */     return createWebApplicationContext(parent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void applyInitializers(ConfigurableApplicationContext wac)
/*      */   {
/*  715 */     String globalClassNames = getServletContext().getInitParameter("globalInitializerClasses");
/*  716 */     if (globalClassNames != null) {
/*  717 */       for (String className : StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n")) {
/*  718 */         this.contextInitializers.add(loadInitializer(className, wac));
/*      */       }
/*      */     }
/*      */     
/*  722 */     if (this.contextInitializerClasses != null) {
/*  723 */       for (String className : StringUtils.tokenizeToStringArray(this.contextInitializerClasses, ",; \t\n")) {
/*  724 */         this.contextInitializers.add(loadInitializer(className, wac));
/*      */       }
/*      */     }
/*      */     
/*  728 */     AnnotationAwareOrderComparator.sort(this.contextInitializers);
/*  729 */     for (??? = this.contextInitializers.iterator(); ((Iterator)???).hasNext();) { Object initializer = (ApplicationContextInitializer)((Iterator)???).next();
/*  730 */       ((ApplicationContextInitializer)initializer).initialize(wac);
/*      */     }
/*      */   }
/*      */   
/*      */   private ApplicationContextInitializer<ConfigurableApplicationContext> loadInitializer(String className, ConfigurableApplicationContext wac)
/*      */   {
/*      */     try
/*      */     {
/*  738 */       Class<?> initializerClass = ClassUtils.forName(className, wac.getClassLoader());
/*      */       
/*  740 */       Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/*  741 */       if ((initializerContextClass != null) && (!initializerContextClass.isInstance(wac))) {
/*  742 */         throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this framework servlet: [%s]", new Object[] {initializerClass
/*      */         
/*      */ 
/*  745 */           .getName(), initializerContextClass.getName(), wac
/*  746 */           .getClass().getName() }));
/*      */       }
/*  748 */       return (ApplicationContextInitializer)BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/*  751 */       throw new ApplicationContextException(String.format("Could not load class [%s] specified via 'contextInitializerClasses' init-param", new Object[] { className }), ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletContextAttributeName()
/*      */   {
/*  764 */     return SERVLET_CONTEXT_PREFIX + getServletName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final WebApplicationContext getWebApplicationContext()
/*      */   {
/*  771 */     return this.webApplicationContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initFrameworkServlet()
/*      */     throws ServletException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void refresh()
/*      */   {
/*  791 */     WebApplicationContext wac = getWebApplicationContext();
/*  792 */     if (!(wac instanceof ConfigurableApplicationContext)) {
/*  793 */       throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
/*      */     }
/*  795 */     ((ConfigurableApplicationContext)wac).refresh();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onApplicationEvent(ContextRefreshedEvent event)
/*      */   {
/*  805 */     this.refreshEventReceived = true;
/*  806 */     onRefresh(event.getApplicationContext());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onRefresh(ApplicationContext context) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  826 */     getServletContext().log("Destroying Spring FrameworkServlet '" + getServletName() + "'");
/*      */     
/*  828 */     if (((this.webApplicationContext instanceof ConfigurableApplicationContext)) && (!this.webApplicationContextInjected)) {
/*  829 */       ((ConfigurableApplicationContext)this.webApplicationContext).close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  841 */     HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
/*  842 */     if ((HttpMethod.PATCH == httpMethod) || (httpMethod == null)) {
/*  843 */       processRequest(request, response);
/*      */     }
/*      */     else {
/*  846 */       super.service(request, response);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  861 */     processRequest(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void doPost(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  872 */     processRequest(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void doPut(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  883 */     processRequest(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  894 */     processRequest(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doOptions(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  907 */     if ((this.dispatchOptionsRequest) || (CorsUtils.isPreFlightRequest(request))) {
/*  908 */       processRequest(request, response);
/*  909 */       if (response.containsHeader("Allow"))
/*      */       {
/*  911 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  917 */     super.doOptions(request, new HttpServletResponseWrapper(response)
/*      */     {
/*      */       public void setHeader(String name, String value) {
/*  920 */         if ("Allow".equals(name)) {
/*  921 */           value = (StringUtils.hasLength(value) ? value + ", " : "") + HttpMethod.PATCH.name();
/*      */         }
/*  923 */         super.setHeader(name, value);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doTrace(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  937 */     if (this.dispatchTraceRequest) {
/*  938 */       processRequest(request, response);
/*  939 */       if ("message/http".equals(response.getContentType()))
/*      */       {
/*  941 */         return;
/*      */       }
/*      */     }
/*  944 */     super.doTrace(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
/*      */     throws ServletException, IOException
/*      */   {
/*  955 */     long startTime = System.currentTimeMillis();
/*  956 */     Throwable failureCause = null;
/*      */     
/*  958 */     LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
/*  959 */     LocaleContext localeContext = buildLocaleContext(request);
/*      */     
/*  961 */     RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
/*  962 */     ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
/*      */     
/*  964 */     WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
/*  965 */     asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor(null));
/*      */     
/*  967 */     initContextHolders(request, localeContext, requestAttributes);
/*      */     try
/*      */     {
/*  970 */       doService(request, response);
/*      */     }
/*      */     catch (ServletException ex) {
/*  973 */       failureCause = ex;
/*  974 */       throw ex;
/*      */     }
/*      */     catch (IOException ex) {
/*  977 */       failureCause = ex;
/*  978 */       throw ex;
/*      */     }
/*      */     catch (Throwable ex) {
/*  981 */       failureCause = ex;
/*  982 */       throw new NestedServletException("Request processing failed", ex);
/*      */     }
/*      */     finally
/*      */     {
/*  986 */       resetContextHolders(request, previousLocaleContext, previousAttributes);
/*  987 */       if (requestAttributes != null) {
/*  988 */         requestAttributes.requestCompleted();
/*      */       }
/*      */       
/*  991 */       if (this.logger.isDebugEnabled()) {
/*  992 */         if (failureCause != null) {
/*  993 */           this.logger.debug("Could not complete request", failureCause);
/*      */ 
/*      */         }
/*  996 */         else if (asyncManager.isConcurrentHandlingStarted()) {
/*  997 */           this.logger.debug("Leaving response open for concurrent processing");
/*      */         }
/*      */         else {
/* 1000 */           this.logger.debug("Successfully completed request");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1005 */       publishRequestHandledEvent(request, response, startTime, failureCause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected LocaleContext buildLocaleContext(HttpServletRequest request)
/*      */   {
/* 1017 */     return new SimpleLocaleContext(request.getLocale());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ServletRequestAttributes buildRequestAttributes(HttpServletRequest request, HttpServletResponse response, RequestAttributes previousAttributes)
/*      */   {
/* 1034 */     if ((previousAttributes == null) || ((previousAttributes instanceof ServletRequestAttributes))) {
/* 1035 */       return new ServletRequestAttributes(request, response);
/*      */     }
/*      */     
/* 1038 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initContextHolders(HttpServletRequest request, LocaleContext localeContext, RequestAttributes requestAttributes)
/*      */   {
/* 1045 */     if (localeContext != null) {
/* 1046 */       LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
/*      */     }
/* 1048 */     if (requestAttributes != null) {
/* 1049 */       RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
/*      */     }
/* 1051 */     if (this.logger.isTraceEnabled()) {
/* 1052 */       this.logger.trace("Bound request context to thread: " + request);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void resetContextHolders(HttpServletRequest request, LocaleContext prevLocaleContext, RequestAttributes previousAttributes)
/*      */   {
/* 1059 */     LocaleContextHolder.setLocaleContext(prevLocaleContext, this.threadContextInheritable);
/* 1060 */     RequestContextHolder.setRequestAttributes(previousAttributes, this.threadContextInheritable);
/* 1061 */     if (this.logger.isTraceEnabled()) {
/* 1062 */       this.logger.trace("Cleared thread-bound request context: " + request);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void publishRequestHandledEvent(HttpServletRequest request, HttpServletResponse response, long startTime, Throwable failureCause)
/*      */   {
/* 1069 */     if (this.publishEvents)
/*      */     {
/* 1071 */       long processingTime = System.currentTimeMillis() - startTime;
/* 1072 */       int statusCode = responseGetStatusAvailable ? response.getStatus() : -1;
/* 1073 */       this.webApplicationContext.publishEvent(new ServletRequestHandledEvent(this, request
/*      */       
/* 1075 */         .getRequestURI(), request.getRemoteAddr(), request
/* 1076 */         .getMethod(), getServletConfig().getServletName(), 
/* 1077 */         WebUtils.getSessionId(request), getUsernameForRequest(request), processingTime, failureCause, statusCode));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getUsernameForRequest(HttpServletRequest request)
/*      */   {
/* 1091 */     Principal userPrincipal = request.getUserPrincipal();
/* 1092 */     return userPrincipal != null ? userPrincipal.getName() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doService(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*      */     throws Exception;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class ContextRefreshListener
/*      */     implements ApplicationListener<ContextRefreshedEvent>
/*      */   {
/*      */     private ContextRefreshListener() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void onApplicationEvent(ContextRefreshedEvent event)
/*      */     {
/* 1121 */       FrameworkServlet.this.onApplicationEvent(event);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class RequestBindingInterceptor
/*      */     extends CallableProcessingInterceptorAdapter
/*      */   {
/*      */     private RequestBindingInterceptor() {}
/*      */     
/*      */ 
/*      */     public <T> void preProcess(NativeWebRequest webRequest, Callable<T> task)
/*      */     {
/* 1134 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 1135 */       if (request != null) {
/* 1136 */         HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 1137 */         FrameworkServlet.this.initContextHolders(request, FrameworkServlet.this.buildLocaleContext(request), FrameworkServlet.this
/* 1138 */           .buildRequestAttributes(request, response, null));
/*      */       }
/*      */     }
/*      */     
/*      */     public <T> void postProcess(NativeWebRequest webRequest, Callable<T> task, Object concurrentResult) {
/* 1143 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 1144 */       if (request != null) {
/* 1145 */         FrameworkServlet.this.resetContextHolders(request, null, null);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\FrameworkServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */