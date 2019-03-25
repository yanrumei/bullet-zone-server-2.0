/*     */ package org.springframework.web.context;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextLoader
/*     */ {
/*     */   public static final String CONTEXT_ID_PARAM = "contextId";
/*     */   public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
/*     */   public static final String CONTEXT_CLASS_PARAM = "contextClass";
/*     */   public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";
/*     */   public static final String GLOBAL_INITIALIZER_CLASSES_PARAM = "globalInitializerClasses";
/*     */   public static final String LOCATOR_FACTORY_SELECTOR_PARAM = "locatorFactorySelector";
/*     */   public static final String LOCATOR_FACTORY_KEY_PARAM = "parentContextKey";
/*     */   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
/*     */   private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
/*     */   private static final Properties defaultStrategies;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 172 */       ClassPathResource resource = new ClassPathResource("ContextLoader.properties", ContextLoader.class);
/* 173 */       defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
/*     */     }
/*     */     catch (IOException ex) {
/* 176 */       throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */   private static final Map<ClassLoader, WebApplicationContext> currentContextPerThread = new ConcurrentHashMap(1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static volatile WebApplicationContext currentContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private WebApplicationContext context;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BeanFactoryReference parentContextRef;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 206 */   private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList();
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
/*     */   public ContextLoader(WebApplicationContext context)
/*     */   {
/* 262 */     this.context = context;
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
/*     */   public void setContextInitializers(ApplicationContextInitializer<?>... initializers)
/*     */   {
/* 275 */     if (initializers != null) {
/* 276 */       for (ApplicationContextInitializer<?> initializer : initializers) {
/* 277 */         this.contextInitializers.add(initializer);
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebApplicationContext initWebApplicationContext(ServletContext servletContext)
/*     */   {
/* 295 */     if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
/* 296 */       throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 301 */     Log logger = LogFactory.getLog(ContextLoader.class);
/* 302 */     servletContext.log("Initializing Spring root WebApplicationContext");
/* 303 */     if (logger.isInfoEnabled()) {
/* 304 */       logger.info("Root WebApplicationContext: initialization started");
/*     */     }
/* 306 */     long startTime = System.currentTimeMillis();
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 311 */       if (this.context == null) {
/* 312 */         this.context = createWebApplicationContext(servletContext);
/*     */       }
/* 314 */       if ((this.context instanceof ConfigurableWebApplicationContext)) {
/* 315 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
/* 316 */         if (!cwac.isActive())
/*     */         {
/*     */ 
/* 319 */           if (cwac.getParent() == null)
/*     */           {
/*     */ 
/* 322 */             ApplicationContext parent = loadParentContext(servletContext);
/* 323 */             cwac.setParent(parent);
/*     */           }
/* 325 */           configureAndRefreshWebApplicationContext(cwac, servletContext);
/*     */         }
/*     */       }
/* 328 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
/*     */       
/* 330 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 331 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 332 */         currentContext = this.context;
/*     */       }
/* 334 */       else if (ccl != null) {
/* 335 */         currentContextPerThread.put(ccl, this.context);
/*     */       }
/*     */       
/* 338 */       if (logger.isDebugEnabled()) {
/* 339 */         logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
/*     */       }
/*     */       
/* 342 */       if (logger.isInfoEnabled()) {
/* 343 */         long elapsedTime = System.currentTimeMillis() - startTime;
/* 344 */         logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
/*     */       }
/*     */       
/* 347 */       return this.context;
/*     */     }
/*     */     catch (RuntimeException ex) {
/* 350 */       logger.error("Context initialization failed", ex);
/* 351 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/* 352 */       throw ex;
/*     */     }
/*     */     catch (Error err) {
/* 355 */       logger.error("Context initialization failed", err);
/* 356 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, err);
/* 357 */       throw err;
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected WebApplicationContext createWebApplicationContext(ServletContext sc)
/*     */   {
/* 374 */     Class<?> contextClass = determineContextClass(sc);
/* 375 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass))
/*     */     {
/* 377 */       throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
/*     */     }
/* 379 */     return (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> determineContextClass(ServletContext servletContext)
/*     */   {
/* 391 */     String contextClassName = servletContext.getInitParameter("contextClass");
/* 392 */     if (contextClassName != null) {
/*     */       try {
/* 394 */         return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
/*     */       }
/*     */       catch (ClassNotFoundException ex) {
/* 397 */         throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 402 */     contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
/*     */     try {
/* 404 */       return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 407 */       throw new ApplicationContextException("Failed to load default context class [" + contextClassName + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc)
/*     */   {
/* 414 */     if (ObjectUtils.identityToString(wac).equals(wac.getId()))
/*     */     {
/*     */ 
/* 417 */       String idParam = sc.getInitParameter("contextId");
/* 418 */       if (idParam != null) {
/* 419 */         wac.setId(idParam);
/*     */       }
/*     */       else
/*     */       {
/* 423 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 424 */           ObjectUtils.getDisplayString(sc.getContextPath()));
/*     */       }
/*     */     }
/*     */     
/* 428 */     wac.setServletContext(sc);
/* 429 */     String configLocationParam = sc.getInitParameter("contextConfigLocation");
/* 430 */     if (configLocationParam != null) {
/* 431 */       wac.setConfigLocation(configLocationParam);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 437 */     ConfigurableEnvironment env = wac.getEnvironment();
/* 438 */     if ((env instanceof ConfigurableWebEnvironment)) {
/* 439 */       ((ConfigurableWebEnvironment)env).initPropertySources(sc, null);
/*     */     }
/*     */     
/* 442 */     customizeContext(sc, wac);
/* 443 */     wac.refresh();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac)
/*     */   {
/* 465 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> initializerClasses = determineContextInitializerClasses(sc);
/*     */     
/* 467 */     for (Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass : initializerClasses)
/*     */     {
/* 469 */       Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/* 470 */       if ((initializerContextClass != null) && (!initializerContextClass.isInstance(wac))) {
/* 471 */         throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this context loader: [%s]", new Object[] {initializerClass
/*     */         
/*     */ 
/* 474 */           .getName(), initializerContextClass.getName(), wac
/* 475 */           .getClass().getName() }));
/*     */       }
/* 477 */       this.contextInitializers.add(BeanUtils.instantiateClass(initializerClass));
/*     */     }
/*     */     
/* 480 */     AnnotationAwareOrderComparator.sort(this.contextInitializers);
/* 481 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
/* 482 */       initializer.initialize(wac);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(ServletContext servletContext)
/*     */   {
/* 495 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes = new ArrayList();
/*     */     
/*     */ 
/* 498 */     String globalClassNames = servletContext.getInitParameter("globalInitializerClasses");
/* 499 */     String str1; String className; if (globalClassNames != null) {
/* 500 */       String[] arrayOfString1 = StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n");int i = arrayOfString1.length; for (str1 = 0; str1 < i; str1++) { className = arrayOfString1[str1];
/* 501 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 505 */     String localClassNames = servletContext.getInitParameter("contextInitializerClasses");
/* 506 */     if (localClassNames != null) {
/* 507 */       String[] arrayOfString2 = StringUtils.tokenizeToStringArray(localClassNames, ",; \t\n");str1 = arrayOfString2.length; for (className = 0; className < str1; className++) { String className = arrayOfString2[className];
/* 508 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 512 */     return classes;
/*     */   }
/*     */   
/*     */   private Class<ApplicationContextInitializer<ConfigurableApplicationContext>> loadInitializerClass(String className)
/*     */   {
/*     */     try {
/* 518 */       Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/* 519 */       if (!ApplicationContextInitializer.class.isAssignableFrom(clazz)) {
/* 520 */         throw new ApplicationContextException("Initializer class does not implement ApplicationContextInitializer interface: " + clazz);
/*     */       }
/*     */       
/* 523 */       return clazz;
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/* 526 */       throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
/*     */     }
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
/*     */   protected ApplicationContext loadParentContext(ServletContext servletContext)
/*     */   {
/* 551 */     ApplicationContext parentContext = null;
/* 552 */     String locatorFactorySelector = servletContext.getInitParameter("locatorFactorySelector");
/* 553 */     String parentContextKey = servletContext.getInitParameter("parentContextKey");
/*     */     
/* 555 */     if (parentContextKey != null)
/*     */     {
/* 557 */       BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locatorFactorySelector);
/* 558 */       Log logger = LogFactory.getLog(ContextLoader.class);
/* 559 */       if (logger.isDebugEnabled()) {
/* 560 */         logger.debug("Getting parent context definition: using parent context key of '" + parentContextKey + "' with BeanFactoryLocator");
/*     */       }
/*     */       
/* 563 */       this.parentContextRef = locator.useBeanFactory(parentContextKey);
/* 564 */       parentContext = (ApplicationContext)this.parentContextRef.getFactory();
/*     */     }
/*     */     
/* 567 */     return parentContext;
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
/*     */   public static WebApplicationContext getCurrentWebApplicationContext()
/*     */   {
/* 611 */     ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 612 */     if (ccl != null) {
/* 613 */       WebApplicationContext ccpt = (WebApplicationContext)currentContextPerThread.get(ccl);
/* 614 */       if (ccpt != null) {
/* 615 */         return ccpt;
/*     */       }
/*     */     }
/* 618 */     return currentContext;
/*     */   }
/*     */   
/*     */   public ContextLoader() {}
/*     */   
/*     */   /* Error */
/*     */   public void closeWebApplicationContext(ServletContext servletContext)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 115
/*     */     //   3: invokeinterface 15 2 0
/*     */     //   8: aload_0
/*     */     //   9: getfield 5	org/springframework/web/context/ContextLoader:context	Lorg/springframework/web/context/WebApplicationContext;
/*     */     //   12: instanceof 21
/*     */     //   15: ifeq +15 -> 30
/*     */     //   18: aload_0
/*     */     //   19: getfield 5	org/springframework/web/context/ContextLoader:context	Lorg/springframework/web/context/WebApplicationContext;
/*     */     //   22: checkcast 21	org/springframework/web/context/ConfigurableWebApplicationContext
/*     */     //   25: invokeinterface 116 1 0
/*     */     //   30: invokestatic 28	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   33: invokevirtual 29	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   36: astore_2
/*     */     //   37: aload_2
/*     */     //   38: ldc 12
/*     */     //   40: invokevirtual 30	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   43: if_acmpne +10 -> 53
/*     */     //   46: aconst_null
/*     */     //   47: putstatic 31	org/springframework/web/context/ContextLoader:currentContext	Lorg/springframework/web/context/WebApplicationContext;
/*     */     //   50: goto +17 -> 67
/*     */     //   53: aload_2
/*     */     //   54: ifnull +13 -> 67
/*     */     //   57: getstatic 32	org/springframework/web/context/ContextLoader:currentContextPerThread	Ljava/util/Map;
/*     */     //   60: aload_2
/*     */     //   61: invokeinterface 117 2 0
/*     */     //   66: pop
/*     */     //   67: aload_1
/*     */     //   68: getstatic 7	org/springframework/web/context/WebApplicationContext:ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE	Ljava/lang/String;
/*     */     //   71: invokeinterface 118 2 0
/*     */     //   76: aload_0
/*     */     //   77: getfield 112	org/springframework/web/context/ContextLoader:parentContextRef	Lorg/springframework/beans/factory/access/BeanFactoryReference;
/*     */     //   80: ifnull +12 -> 92
/*     */     //   83: aload_0
/*     */     //   84: getfield 112	org/springframework/web/context/ContextLoader:parentContextRef	Lorg/springframework/beans/factory/access/BeanFactoryReference;
/*     */     //   87: invokeinterface 119 1 0
/*     */     //   92: goto +72 -> 164
/*     */     //   95: astore_3
/*     */     //   96: invokestatic 28	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   99: invokevirtual 29	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   102: astore 4
/*     */     //   104: aload 4
/*     */     //   106: ldc 12
/*     */     //   108: invokevirtual 30	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   111: if_acmpne +10 -> 121
/*     */     //   114: aconst_null
/*     */     //   115: putstatic 31	org/springframework/web/context/ContextLoader:currentContext	Lorg/springframework/web/context/WebApplicationContext;
/*     */     //   118: goto +19 -> 137
/*     */     //   121: aload 4
/*     */     //   123: ifnull +14 -> 137
/*     */     //   126: getstatic 32	org/springframework/web/context/ContextLoader:currentContextPerThread	Ljava/util/Map;
/*     */     //   129: aload 4
/*     */     //   131: invokeinterface 117 2 0
/*     */     //   136: pop
/*     */     //   137: aload_1
/*     */     //   138: getstatic 7	org/springframework/web/context/WebApplicationContext:ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE	Ljava/lang/String;
/*     */     //   141: invokeinterface 118 2 0
/*     */     //   146: aload_0
/*     */     //   147: getfield 112	org/springframework/web/context/ContextLoader:parentContextRef	Lorg/springframework/beans/factory/access/BeanFactoryReference;
/*     */     //   150: ifnull +12 -> 162
/*     */     //   153: aload_0
/*     */     //   154: getfield 112	org/springframework/web/context/ContextLoader:parentContextRef	Lorg/springframework/beans/factory/access/BeanFactoryReference;
/*     */     //   157: invokeinterface 119 1 0
/*     */     //   162: aload_3
/*     */     //   163: athrow
/*     */     //   164: return
/*     */     // Line number table:
/*     */     //   Java source line #580	-> byte code offset #0
/*     */     //   Java source line #582	-> byte code offset #8
/*     */     //   Java source line #583	-> byte code offset #18
/*     */     //   Java source line #587	-> byte code offset #30
/*     */     //   Java source line #588	-> byte code offset #37
/*     */     //   Java source line #589	-> byte code offset #46
/*     */     //   Java source line #591	-> byte code offset #53
/*     */     //   Java source line #592	-> byte code offset #57
/*     */     //   Java source line #594	-> byte code offset #67
/*     */     //   Java source line #595	-> byte code offset #76
/*     */     //   Java source line #596	-> byte code offset #83
/*     */     //   Java source line #598	-> byte code offset #92
/*     */     //   Java source line #587	-> byte code offset #95
/*     */     //   Java source line #588	-> byte code offset #104
/*     */     //   Java source line #589	-> byte code offset #114
/*     */     //   Java source line #591	-> byte code offset #121
/*     */     //   Java source line #592	-> byte code offset #126
/*     */     //   Java source line #594	-> byte code offset #137
/*     */     //   Java source line #595	-> byte code offset #146
/*     */     //   Java source line #596	-> byte code offset #153
/*     */     //   Java source line #598	-> byte code offset #162
/*     */     //   Java source line #599	-> byte code offset #164
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	165	0	this	ContextLoader
/*     */     //   0	165	1	servletContext	ServletContext
/*     */     //   36	25	2	ccl	ClassLoader
/*     */     //   95	68	3	localObject	Object
/*     */     //   102	28	4	ccl	ClassLoader
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	30	95	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\ContextLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */