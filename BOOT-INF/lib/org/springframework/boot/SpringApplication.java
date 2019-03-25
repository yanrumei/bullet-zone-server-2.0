/*      */ package org.springframework.boot;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.security.AccessControlException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*      */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*      */ import org.springframework.boot.diagnostics.FailureAnalyzers;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextInitializer;
/*      */ import org.springframework.context.ApplicationListener;
/*      */ import org.springframework.context.ConfigurableApplicationContext;
/*      */ import org.springframework.context.support.AbstractApplicationContext;
/*      */ import org.springframework.context.support.GenericApplicationContext;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.env.CompositePropertySource;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.core.env.MapPropertySource;
/*      */ import org.springframework.core.env.MutablePropertySources;
/*      */ import org.springframework.core.env.PropertySource;
/*      */ import org.springframework.core.env.SimpleCommandLinePropertySource;
/*      */ import org.springframework.core.env.StandardEnvironment;
/*      */ import org.springframework.core.io.DefaultResourceLoader;
/*      */ import org.springframework.core.io.ResourceLoader;
/*      */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StopWatch;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.context.WebApplicationContext;
/*      */ import org.springframework.web.context.support.StandardServletEnvironment;
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
/*      */ public class SpringApplication
/*      */ {
/*      */   public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context.annotation.AnnotationConfigApplicationContext";
/*      */   public static final String DEFAULT_WEB_CONTEXT_CLASS = "org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext";
/*  164 */   private static final String[] WEB_ENVIRONMENT_CLASSES = { "javax.servlet.Servlet", "org.springframework.web.context.ConfigurableWebApplicationContext" };
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String BANNER_LOCATION_PROPERTY_VALUE = "banner.txt";
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String BANNER_LOCATION_PROPERTY = "banner.location";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String SYSTEM_PROPERTY_JAVA_AWT_HEADLESS = "java.awt.headless";
/*      */   
/*      */ 
/*  179 */   private static final Log logger = LogFactory.getLog(SpringApplication.class);
/*      */   
/*  181 */   private final Set<Object> sources = new LinkedHashSet();
/*      */   
/*      */   private Class<?> mainApplicationClass;
/*      */   
/*  185 */   private Banner.Mode bannerMode = Banner.Mode.CONSOLE;
/*      */   
/*  187 */   private boolean logStartupInfo = true;
/*      */   
/*  189 */   private boolean addCommandLineProperties = true;
/*      */   
/*      */   private Banner banner;
/*      */   
/*      */   private ResourceLoader resourceLoader;
/*      */   
/*      */   private BeanNameGenerator beanNameGenerator;
/*      */   
/*      */   private ConfigurableEnvironment environment;
/*      */   
/*      */   private Class<? extends ConfigurableApplicationContext> applicationContextClass;
/*      */   
/*      */   private boolean webEnvironment;
/*      */   
/*  203 */   private boolean headless = true;
/*      */   
/*  205 */   private boolean registerShutdownHook = true;
/*      */   
/*      */   private List<ApplicationContextInitializer<?>> initializers;
/*      */   
/*      */   private List<ApplicationListener<?>> listeners;
/*      */   
/*      */   private Map<String, Object> defaultProperties;
/*      */   
/*  213 */   private Set<String> additionalProfiles = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SpringApplication(Object... sources)
/*      */   {
/*  225 */     initialize(sources);
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
/*      */   public SpringApplication(ResourceLoader resourceLoader, Object... sources)
/*      */   {
/*  239 */     this.resourceLoader = resourceLoader;
/*  240 */     initialize(sources);
/*      */   }
/*      */   
/*      */   private void initialize(Object[] sources)
/*      */   {
/*  245 */     if ((sources != null) && (sources.length > 0)) {
/*  246 */       this.sources.addAll(Arrays.asList(sources));
/*      */     }
/*  248 */     this.webEnvironment = deduceWebEnvironment();
/*  249 */     setInitializers(getSpringFactoriesInstances(ApplicationContextInitializer.class));
/*      */     
/*  251 */     setListeners(getSpringFactoriesInstances(ApplicationListener.class));
/*  252 */     this.mainApplicationClass = deduceMainApplicationClass();
/*      */   }
/*      */   
/*      */   private boolean deduceWebEnvironment() {
/*  256 */     for (String className : WEB_ENVIRONMENT_CLASSES) {
/*  257 */       if (!ClassUtils.isPresent(className, null)) {
/*  258 */         return false;
/*      */       }
/*      */     }
/*  261 */     return true;
/*      */   }
/*      */   
/*      */   private Class<?> deduceMainApplicationClass() {
/*      */     try {
/*  266 */       StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
/*  267 */       for (StackTraceElement stackTraceElement : stackTrace) {
/*  268 */         if ("main".equals(stackTraceElement.getMethodName())) {
/*  269 */           return Class.forName(stackTraceElement.getClassName());
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {}
/*      */     
/*      */ 
/*  276 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ConfigurableApplicationContext run(String... args)
/*      */   {
/*  286 */     StopWatch stopWatch = new StopWatch();
/*  287 */     stopWatch.start();
/*  288 */     ConfigurableApplicationContext context = null;
/*  289 */     FailureAnalyzers analyzers = null;
/*  290 */     configureHeadlessProperty();
/*  291 */     SpringApplicationRunListeners listeners = getRunListeners(args);
/*  292 */     listeners.starting();
/*      */     try {
/*  294 */       ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
/*      */       
/*  296 */       ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
/*      */       
/*  298 */       Banner printedBanner = printBanner(environment);
/*  299 */       context = createApplicationContext();
/*  300 */       analyzers = new FailureAnalyzers(context);
/*  301 */       prepareContext(context, environment, listeners, applicationArguments, printedBanner);
/*      */       
/*  303 */       refreshContext(context);
/*  304 */       afterRefresh(context, applicationArguments);
/*  305 */       listeners.finished(context, null);
/*  306 */       stopWatch.stop();
/*  307 */       if (this.logStartupInfo)
/*      */       {
/*  309 */         new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
/*      */       }
/*  311 */       return context;
/*      */     }
/*      */     catch (Throwable ex) {
/*  314 */       handleRunFailure(context, listeners, analyzers, ex);
/*  315 */       throw new IllegalStateException(ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments)
/*      */   {
/*  323 */     ConfigurableEnvironment environment = getOrCreateEnvironment();
/*  324 */     configureEnvironment(environment, applicationArguments.getSourceArgs());
/*  325 */     listeners.environmentPrepared(environment);
/*  326 */     if (!this.webEnvironment)
/*      */     {
/*  328 */       environment = new EnvironmentConverter(getClassLoader()).convertToStandardEnvironmentIfNecessary(environment);
/*      */     }
/*  330 */     return environment;
/*      */   }
/*      */   
/*      */ 
/*      */   private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment, SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner)
/*      */   {
/*  336 */     context.setEnvironment(environment);
/*  337 */     postProcessApplicationContext(context);
/*  338 */     applyInitializers(context);
/*  339 */     listeners.contextPrepared(context);
/*  340 */     if (this.logStartupInfo) {
/*  341 */       logStartupInfo(context.getParent() == null);
/*  342 */       logStartupProfileInfo(context);
/*      */     }
/*      */     
/*      */ 
/*  346 */     context.getBeanFactory().registerSingleton("springApplicationArguments", applicationArguments);
/*      */     
/*  348 */     if (printedBanner != null) {
/*  349 */       context.getBeanFactory().registerSingleton("springBootBanner", printedBanner);
/*      */     }
/*      */     
/*      */ 
/*  353 */     Set<Object> sources = getSources();
/*  354 */     Assert.notEmpty(sources, "Sources must not be empty");
/*  355 */     load(context, sources.toArray(new Object[sources.size()]));
/*  356 */     listeners.contextLoaded(context);
/*      */   }
/*      */   
/*      */   private void refreshContext(ConfigurableApplicationContext context) {
/*  360 */     refresh(context);
/*  361 */     if (this.registerShutdownHook) {
/*      */       try {
/*  363 */         context.registerShutdownHook();
/*      */       }
/*      */       catch (AccessControlException localAccessControlException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void configureHeadlessProperty()
/*      */   {
/*  372 */     System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", 
/*  373 */       Boolean.toString(this.headless)));
/*      */   }
/*      */   
/*      */   private SpringApplicationRunListeners getRunListeners(String[] args) {
/*  377 */     Class<?>[] types = { SpringApplication.class, String[].class };
/*  378 */     return new SpringApplicationRunListeners(logger, getSpringFactoriesInstances(SpringApplicationRunListener.class, types, new Object[] { this, args }));
/*      */   }
/*      */   
/*      */   private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type)
/*      */   {
/*  383 */     return getSpringFactoriesInstances(type, new Class[0], new Object[0]);
/*      */   }
/*      */   
/*      */   private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args)
/*      */   {
/*  388 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*      */     
/*      */ 
/*  391 */     Set<String> names = new LinkedHashSet(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
/*  392 */     List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
/*      */     
/*  394 */     AnnotationAwareOrderComparator.sort(instances);
/*  395 */     return instances;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, ClassLoader classLoader, Object[] args, Set<String> names)
/*      */   {
/*  402 */     List<T> instances = new ArrayList(names.size());
/*  403 */     for (String name : names) {
/*      */       try {
/*  405 */         Class<?> instanceClass = ClassUtils.forName(name, classLoader);
/*  406 */         Assert.isAssignable(type, instanceClass);
/*      */         
/*  408 */         Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
/*  409 */         T instance = BeanUtils.instantiateClass(constructor, args);
/*  410 */         instances.add(instance);
/*      */       }
/*      */       catch (Throwable ex) {
/*  413 */         throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
/*      */       }
/*      */     }
/*      */     
/*  417 */     return instances;
/*      */   }
/*      */   
/*      */   private ConfigurableEnvironment getOrCreateEnvironment() {
/*  421 */     if (this.environment != null) {
/*  422 */       return this.environment;
/*      */     }
/*  424 */     if (this.webEnvironment) {
/*  425 */       return new StandardServletEnvironment();
/*      */     }
/*  427 */     return new StandardEnvironment();
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
/*      */   protected void configureEnvironment(ConfigurableEnvironment environment, String[] args)
/*      */   {
/*  443 */     configurePropertySources(environment, args);
/*  444 */     configureProfiles(environment, args);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void configurePropertySources(ConfigurableEnvironment environment, String[] args)
/*      */   {
/*  456 */     MutablePropertySources sources = environment.getPropertySources();
/*  457 */     if ((this.defaultProperties != null) && (!this.defaultProperties.isEmpty())) {
/*  458 */       sources.addLast(new MapPropertySource("defaultProperties", this.defaultProperties));
/*      */     }
/*      */     
/*  461 */     if ((this.addCommandLineProperties) && (args.length > 0)) {
/*  462 */       String name = "commandLineArgs";
/*  463 */       if (sources.contains(name)) {
/*  464 */         PropertySource<?> source = sources.get(name);
/*  465 */         CompositePropertySource composite = new CompositePropertySource(name);
/*  466 */         composite.addPropertySource(new SimpleCommandLinePropertySource(name + "-" + args
/*  467 */           .hashCode(), args));
/*  468 */         composite.addPropertySource(source);
/*  469 */         sources.replace(name, composite);
/*      */       }
/*      */       else {
/*  472 */         sources.addFirst(new SimpleCommandLinePropertySource(args));
/*      */       }
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
/*      */   protected void configureProfiles(ConfigurableEnvironment environment, String[] args)
/*      */   {
/*  487 */     environment.getActiveProfiles();
/*      */     
/*  489 */     Set<String> profiles = new LinkedHashSet(this.additionalProfiles);
/*  490 */     profiles.addAll(Arrays.asList(environment.getActiveProfiles()));
/*  491 */     environment.setActiveProfiles((String[])profiles.toArray(new String[profiles.size()]));
/*      */   }
/*      */   
/*      */   private Banner printBanner(ConfigurableEnvironment environment) {
/*  495 */     if (this.bannerMode == Banner.Mode.OFF) {
/*  496 */       return null;
/*      */     }
/*      */     
/*  499 */     ResourceLoader resourceLoader = this.resourceLoader != null ? this.resourceLoader : new DefaultResourceLoader(getClassLoader());
/*  500 */     SpringApplicationBannerPrinter bannerPrinter = new SpringApplicationBannerPrinter(resourceLoader, this.banner);
/*      */     
/*  502 */     if (this.bannerMode == Banner.Mode.LOG) {
/*  503 */       return bannerPrinter.print(environment, this.mainApplicationClass, logger);
/*      */     }
/*  505 */     return bannerPrinter.print(environment, this.mainApplicationClass, System.out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ConfigurableApplicationContext createApplicationContext()
/*      */   {
/*  516 */     Class<?> contextClass = this.applicationContextClass;
/*  517 */     if (contextClass == null) {
/*      */       try {
/*  519 */         contextClass = Class.forName(this.webEnvironment ? "org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext" : "org.springframework.context.annotation.AnnotationConfigApplicationContext");
/*      */       }
/*      */       catch (ClassNotFoundException ex)
/*      */       {
/*  523 */         throw new IllegalStateException("Unable create a default ApplicationContext, please specify an ApplicationContextClass", ex);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  529 */     return (ConfigurableApplicationContext)BeanUtils.instantiate(contextClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void postProcessApplicationContext(ConfigurableApplicationContext context)
/*      */   {
/*  538 */     if (this.beanNameGenerator != null) {
/*  539 */       context.getBeanFactory().registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", this.beanNameGenerator);
/*      */     }
/*      */     
/*      */ 
/*  543 */     if (this.resourceLoader != null) {
/*  544 */       if ((context instanceof GenericApplicationContext))
/*      */       {
/*  546 */         ((GenericApplicationContext)context).setResourceLoader(this.resourceLoader);
/*      */       }
/*  548 */       if ((context instanceof DefaultResourceLoader))
/*      */       {
/*  550 */         ((DefaultResourceLoader)context).setClassLoader(this.resourceLoader.getClassLoader());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void applyInitializers(ConfigurableApplicationContext context)
/*      */   {
/*  563 */     for (ApplicationContextInitializer initializer : getInitializers()) {
/*  564 */       Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(initializer
/*  565 */         .getClass(), ApplicationContextInitializer.class);
/*  566 */       Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
/*  567 */       initializer.initialize(context);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void logStartupInfo(boolean isRoot)
/*      */   {
/*  577 */     if (isRoot)
/*      */     {
/*  579 */       new StartupInfoLogger(this.mainApplicationClass).logStarting(getApplicationLog());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void logStartupProfileInfo(ConfigurableApplicationContext context)
/*      */   {
/*  588 */     Log log = getApplicationLog();
/*  589 */     if (log.isInfoEnabled()) {
/*  590 */       String[] activeProfiles = context.getEnvironment().getActiveProfiles();
/*  591 */       if (ObjectUtils.isEmpty(activeProfiles)) {
/*  592 */         String[] defaultProfiles = context.getEnvironment().getDefaultProfiles();
/*  593 */         log.info("No active profile set, falling back to default profiles: " + 
/*  594 */           StringUtils.arrayToCommaDelimitedString(defaultProfiles));
/*      */       }
/*      */       else {
/*  597 */         log.info("The following profiles are active: " + 
/*  598 */           StringUtils.arrayToCommaDelimitedString(activeProfiles));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Log getApplicationLog()
/*      */   {
/*  608 */     if (this.mainApplicationClass == null) {
/*  609 */       return logger;
/*      */     }
/*  611 */     return LogFactory.getLog(this.mainApplicationClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void load(ApplicationContext context, Object[] sources)
/*      */   {
/*  620 */     if (logger.isDebugEnabled()) {
/*  621 */       logger.debug("Loading source " + 
/*  622 */         StringUtils.arrayToCommaDelimitedString(sources));
/*      */     }
/*  624 */     BeanDefinitionLoader loader = createBeanDefinitionLoader(
/*  625 */       getBeanDefinitionRegistry(context), sources);
/*  626 */     if (this.beanNameGenerator != null) {
/*  627 */       loader.setBeanNameGenerator(this.beanNameGenerator);
/*      */     }
/*  629 */     if (this.resourceLoader != null) {
/*  630 */       loader.setResourceLoader(this.resourceLoader);
/*      */     }
/*  632 */     if (this.environment != null) {
/*  633 */       loader.setEnvironment(this.environment);
/*      */     }
/*  635 */     loader.load();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ResourceLoader getResourceLoader()
/*      */   {
/*  644 */     return this.resourceLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ClassLoader getClassLoader()
/*      */   {
/*  654 */     if (this.resourceLoader != null) {
/*  655 */       return this.resourceLoader.getClassLoader();
/*      */     }
/*  657 */     return ClassUtils.getDefaultClassLoader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context)
/*      */   {
/*  666 */     if ((context instanceof BeanDefinitionRegistry)) {
/*  667 */       return (BeanDefinitionRegistry)context;
/*      */     }
/*  669 */     if ((context instanceof AbstractApplicationContext)) {
/*  670 */       return 
/*  671 */         (BeanDefinitionRegistry)((AbstractApplicationContext)context).getBeanFactory();
/*      */     }
/*  673 */     throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDefinitionLoader createBeanDefinitionLoader(BeanDefinitionRegistry registry, Object[] sources)
/*      */   {
/*  684 */     return new BeanDefinitionLoader(registry, sources);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void refresh(ApplicationContext applicationContext)
/*      */   {
/*  692 */     Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
/*  693 */     ((AbstractApplicationContext)applicationContext).refresh();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void afterRefresh(ConfigurableApplicationContext context, ApplicationArguments args)
/*      */   {
/*  703 */     callRunners(context, args);
/*      */   }
/*      */   
/*      */   private void callRunners(ApplicationContext context, ApplicationArguments args) {
/*  707 */     List<Object> runners = new ArrayList();
/*  708 */     runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
/*  709 */     runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
/*  710 */     AnnotationAwareOrderComparator.sort(runners);
/*  711 */     for (Object runner : new LinkedHashSet(runners)) {
/*  712 */       if ((runner instanceof ApplicationRunner)) {
/*  713 */         callRunner((ApplicationRunner)runner, args);
/*      */       }
/*  715 */       if ((runner instanceof CommandLineRunner)) {
/*  716 */         callRunner((CommandLineRunner)runner, args);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void callRunner(ApplicationRunner runner, ApplicationArguments args) {
/*      */     try {
/*  723 */       runner.run(args);
/*      */     }
/*      */     catch (Exception ex) {
/*  726 */       throw new IllegalStateException("Failed to execute ApplicationRunner", ex);
/*      */     }
/*      */   }
/*      */   
/*      */   private void callRunner(CommandLineRunner runner, ApplicationArguments args) {
/*      */     try {
/*  732 */       runner.run(args.getSourceArgs());
/*      */     }
/*      */     catch (Exception ex) {
/*  735 */       throw new IllegalStateException("Failed to execute CommandLineRunner", ex);
/*      */     }
/*      */   }
/*      */   
/*      */   private void handleRunFailure(ConfigurableApplicationContext context, SpringApplicationRunListeners listeners, FailureAnalyzers analyzers, Throwable exception)
/*      */   {
/*      */     try
/*      */     {
/*      */       try {
/*  744 */         handleExitCode(context, exception);
/*  745 */         listeners.finished(context, exception);
/*      */       }
/*      */       finally {
/*  748 */         reportFailure(analyzers, exception);
/*  749 */         if (context != null) {
/*  750 */           context.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception ex) {
/*  755 */       logger.warn("Unable to close ApplicationContext", ex);
/*      */     }
/*  757 */     ReflectionUtils.rethrowRuntimeException(exception);
/*      */   }
/*      */   
/*      */   private void reportFailure(FailureAnalyzers analyzers, Throwable failure) {
/*      */     try {
/*  762 */       if ((analyzers != null) && (analyzers.analyzeAndReport(failure))) {
/*  763 */         registerLoggedException(failure);
/*  764 */         return;
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */     
/*      */ 
/*  770 */     if (logger.isErrorEnabled()) {
/*  771 */       logger.error("Application startup failed", failure);
/*  772 */       registerLoggedException(failure);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void registerLoggedException(Throwable exception)
/*      */   {
/*  782 */     SpringBootExceptionHandler handler = getSpringBootExceptionHandler();
/*  783 */     if (handler != null) {
/*  784 */       handler.registerLoggedException(exception);
/*      */     }
/*      */   }
/*      */   
/*      */   private void handleExitCode(ConfigurableApplicationContext context, Throwable exception)
/*      */   {
/*  790 */     int exitCode = getExitCodeFromException(context, exception);
/*  791 */     if (exitCode != 0) {
/*  792 */       if (context != null) {
/*  793 */         context.publishEvent(new ExitCodeEvent(context, exitCode));
/*      */       }
/*  795 */       SpringBootExceptionHandler handler = getSpringBootExceptionHandler();
/*  796 */       if (handler != null) {
/*  797 */         handler.registerExitCode(exitCode);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private int getExitCodeFromException(ConfigurableApplicationContext context, Throwable exception)
/*      */   {
/*  804 */     int exitCode = getExitCodeFromMappedException(context, exception);
/*  805 */     if (exitCode == 0) {
/*  806 */       exitCode = getExitCodeFromExitCodeGeneratorException(exception);
/*      */     }
/*  808 */     return exitCode;
/*      */   }
/*      */   
/*      */   private int getExitCodeFromMappedException(ConfigurableApplicationContext context, Throwable exception)
/*      */   {
/*  813 */     if ((context == null) || (!context.isActive())) {
/*  814 */       return 0;
/*      */     }
/*  816 */     ExitCodeGenerators generators = new ExitCodeGenerators();
/*      */     
/*  818 */     Collection<ExitCodeExceptionMapper> beans = context.getBeansOfType(ExitCodeExceptionMapper.class).values();
/*  819 */     generators.addAll(exception, beans);
/*  820 */     return generators.getExitCode();
/*      */   }
/*      */   
/*      */   private int getExitCodeFromExitCodeGeneratorException(Throwable exception) {
/*  824 */     if (exception == null) {
/*  825 */       return 0;
/*      */     }
/*  827 */     if ((exception instanceof ExitCodeGenerator)) {
/*  828 */       return ((ExitCodeGenerator)exception).getExitCode();
/*      */     }
/*  830 */     return getExitCodeFromExitCodeGeneratorException(exception.getCause());
/*      */   }
/*      */   
/*      */   SpringBootExceptionHandler getSpringBootExceptionHandler() {
/*  834 */     if (isMainThread(Thread.currentThread())) {
/*  835 */       return SpringBootExceptionHandler.forCurrentThread();
/*      */     }
/*  837 */     return null;
/*      */   }
/*      */   
/*      */   private boolean isMainThread(Thread currentThread) {
/*  841 */     return (("main".equals(currentThread.getName())) || 
/*  842 */       ("restartedMain".equals(currentThread.getName()))) && 
/*  843 */       ("main".equals(currentThread.getThreadGroup().getName()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getMainApplicationClass()
/*      */   {
/*  851 */     return this.mainApplicationClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMainApplicationClass(Class<?> mainApplicationClass)
/*      */   {
/*  861 */     this.mainApplicationClass = mainApplicationClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isWebEnvironment()
/*      */   {
/*  870 */     return this.webEnvironment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWebEnvironment(boolean webEnvironment)
/*      */   {
/*  879 */     this.webEnvironment = webEnvironment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setHeadless(boolean headless)
/*      */   {
/*  888 */     this.headless = headless;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRegisterShutdownHook(boolean registerShutdownHook)
/*      */   {
/*  898 */     this.registerShutdownHook = registerShutdownHook;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBanner(Banner banner)
/*      */   {
/*  907 */     this.banner = banner;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBannerMode(Banner.Mode bannerMode)
/*      */   {
/*  916 */     this.bannerMode = bannerMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLogStartupInfo(boolean logStartupInfo)
/*      */   {
/*  925 */     this.logStartupInfo = logStartupInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAddCommandLineProperties(boolean addCommandLineProperties)
/*      */   {
/*  934 */     this.addCommandLineProperties = addCommandLineProperties;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultProperties(Map<String, Object> defaultProperties)
/*      */   {
/*  943 */     this.defaultProperties = defaultProperties;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultProperties(Properties defaultProperties)
/*      */   {
/*  951 */     this.defaultProperties = new HashMap();
/*  952 */     for (Object key : Collections.list(defaultProperties.propertyNames())) {
/*  953 */       this.defaultProperties.put((String)key, defaultProperties.get(key));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAdditionalProfiles(String... profiles)
/*      */   {
/*  963 */     this.additionalProfiles = new LinkedHashSet(Arrays.asList(profiles));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*      */   {
/*  971 */     this.beanNameGenerator = beanNameGenerator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnvironment(ConfigurableEnvironment environment)
/*      */   {
/*  980 */     this.environment = environment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Object> getSources()
/*      */   {
/*  990 */     return this.sources;
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
/*      */   public void setSources(Set<Object> sources)
/*      */   {
/* 1005 */     Assert.notNull(sources, "Sources must not be null");
/* 1006 */     this.sources.addAll(sources);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setResourceLoader(ResourceLoader resourceLoader)
/*      */   {
/* 1014 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 1015 */     this.resourceLoader = resourceLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setApplicationContextClass(Class<? extends ConfigurableApplicationContext> applicationContextClass)
/*      */   {
/* 1026 */     this.applicationContextClass = applicationContextClass;
/* 1027 */     if (!isWebApplicationContext(applicationContextClass)) {
/* 1028 */       this.webEnvironment = false;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isWebApplicationContext(Class<?> applicationContextClass) {
/*      */     try {
/* 1034 */       return WebApplicationContext.class.isAssignableFrom(applicationContextClass);
/*      */     }
/*      */     catch (NoClassDefFoundError ex) {}
/* 1037 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInitializers(Collection<? extends ApplicationContextInitializer<?>> initializers)
/*      */   {
/* 1048 */     this.initializers = new ArrayList();
/* 1049 */     this.initializers.addAll(initializers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addInitializers(ApplicationContextInitializer<?>... initializers)
/*      */   {
/* 1058 */     this.initializers.addAll(Arrays.asList(initializers));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<ApplicationContextInitializer<?>> getInitializers()
/*      */   {
/* 1067 */     return asUnmodifiableOrderedSet(this.initializers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setListeners(Collection<? extends ApplicationListener<?>> listeners)
/*      */   {
/* 1076 */     this.listeners = new ArrayList();
/* 1077 */     this.listeners.addAll(listeners);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addListeners(ApplicationListener<?>... listeners)
/*      */   {
/* 1086 */     this.listeners.addAll(Arrays.asList(listeners));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<ApplicationListener<?>> getListeners()
/*      */   {
/* 1096 */     return asUnmodifiableOrderedSet(this.listeners);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ConfigurableApplicationContext run(Object source, String... args)
/*      */   {
/* 1107 */     return run(new Object[] { source }, args);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ConfigurableApplicationContext run(Object[] sources, String[] args)
/*      */   {
/* 1118 */     return new SpringApplication(sources).run(args);
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
/*      */   public static void main(String[] args)
/*      */     throws Exception
/*      */   {
/* 1134 */     run(new Object[0], args);
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
/*      */   public static int exit(ApplicationContext context, ExitCodeGenerator... exitCodeGenerators)
/*      */   {
/* 1150 */     Assert.notNull(context, "Context must not be null");
/* 1151 */     int exitCode = 0;
/*      */     try {
/*      */       try {
/* 1154 */         ExitCodeGenerators generators = new ExitCodeGenerators();
/*      */         
/* 1156 */         Collection<ExitCodeGenerator> beans = context.getBeansOfType(ExitCodeGenerator.class).values();
/* 1157 */         generators.addAll(exitCodeGenerators);
/* 1158 */         generators.addAll(beans);
/* 1159 */         exitCode = generators.getExitCode();
/* 1160 */         if (exitCode != 0) {
/* 1161 */           context.publishEvent(new ExitCodeEvent(context, exitCode));
/*      */         }
/*      */       }
/*      */       finally {
/* 1165 */         close(context);
/*      */       }
/*      */     }
/*      */     catch (Exception ex) {
/* 1169 */       ex.printStackTrace();
/* 1170 */       exitCode = exitCode == 0 ? 1 : exitCode;
/*      */     }
/* 1172 */     return exitCode;
/*      */   }
/*      */   
/*      */   private static void close(ApplicationContext context) {
/* 1176 */     if ((context instanceof ConfigurableApplicationContext)) {
/* 1177 */       ConfigurableApplicationContext closable = (ConfigurableApplicationContext)context;
/* 1178 */       closable.close();
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Set<E> asUnmodifiableOrderedSet(Collection<E> elements) {
/* 1183 */     List<E> list = new ArrayList();
/* 1184 */     list.addAll(elements);
/* 1185 */     Collections.sort(list, AnnotationAwareOrderComparator.INSTANCE);
/* 1186 */     return new LinkedHashSet(list);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */