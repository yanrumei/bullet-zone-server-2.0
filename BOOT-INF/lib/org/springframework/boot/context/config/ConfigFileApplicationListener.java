/*     */ package org.springframework.boot.context.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.bind.PropertiesConfigurationFactory;
/*     */ import org.springframework.boot.bind.PropertySourcesPropertyValues;
/*     */ import org.springframework.boot.bind.RelaxedDataBinder;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*     */ import org.springframework.boot.context.event.ApplicationPreparedEvent;
/*     */ import org.springframework.boot.env.EnumerableCompositePropertySource;
/*     */ import org.springframework.boot.env.EnvironmentPostProcessor;
/*     */ import org.springframework.boot.env.PropertySourcesLoader;
/*     */ import org.springframework.boot.logging.DeferredLog;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.event.SmartApplicationListener;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.EnumerablePropertySource;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigFileApplicationListener
/*     */   implements EnvironmentPostProcessor, SmartApplicationListener, Ordered
/*     */ {
/*     */   private static final String DEFAULT_PROPERTIES = "defaultProperties";
/*     */   private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/";
/*     */   private static final String DEFAULT_NAMES = "application";
/*     */   public static final String ACTIVE_PROFILES_PROPERTY = "spring.profiles.active";
/*     */   public static final String INCLUDE_PROFILES_PROPERTY = "spring.profiles.include";
/*     */   public static final String CONFIG_NAME_PROPERTY = "spring.config.name";
/*     */   public static final String CONFIG_LOCATION_PROPERTY = "spring.config.location";
/*     */   public static final int DEFAULT_ORDER = -2147483638;
/*     */   public static final String APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME = "applicationConfigurationProperties";
/* 144 */   private final DeferredLog logger = new DeferredLog();
/*     */   
/*     */   private String searchLocations;
/*     */   
/*     */   private String names;
/*     */   
/* 150 */   private int order = -2147483638;
/*     */   
/* 152 */   private final ConversionService conversionService = new DefaultConversionService();
/*     */   
/*     */   public boolean supportsEventType(Class<? extends ApplicationEvent> eventType)
/*     */   {
/* 156 */     return (ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(eventType)) || 
/* 157 */       (ApplicationPreparedEvent.class.isAssignableFrom(eventType));
/*     */   }
/*     */   
/*     */   public boolean supportsSourceType(Class<?> aClass)
/*     */   {
/* 162 */     return true;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event)
/*     */   {
/* 167 */     if ((event instanceof ApplicationEnvironmentPreparedEvent)) {
/* 168 */       onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent)event);
/*     */     }
/*     */     
/* 171 */     if ((event instanceof ApplicationPreparedEvent)) {
/* 172 */       onApplicationPreparedEvent(event);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event)
/*     */   {
/* 178 */     List<EnvironmentPostProcessor> postProcessors = loadPostProcessors();
/* 179 */     postProcessors.add(this);
/* 180 */     AnnotationAwareOrderComparator.sort(postProcessors);
/* 181 */     for (EnvironmentPostProcessor postProcessor : postProcessors) {
/* 182 */       postProcessor.postProcessEnvironment(event.getEnvironment(), event
/* 183 */         .getSpringApplication());
/*     */     }
/*     */   }
/*     */   
/*     */   List<EnvironmentPostProcessor> loadPostProcessors() {
/* 188 */     return SpringFactoriesLoader.loadFactories(EnvironmentPostProcessor.class, 
/* 189 */       getClass().getClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */   public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
/*     */   {
/* 195 */     addPropertySources(environment, application.getResourceLoader());
/* 196 */     configureIgnoreBeanInfo(environment);
/* 197 */     bindToSpringApplication(environment, application);
/*     */   }
/*     */   
/*     */   private void configureIgnoreBeanInfo(ConfigurableEnvironment environment) {
/* 201 */     if (System.getProperty("spring.beaninfo.ignore") == null)
/*     */     {
/* 203 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.beaninfo.");
/*     */       
/* 205 */       Boolean ignore = (Boolean)resolver.getProperty("ignore", Boolean.class, Boolean.TRUE);
/* 206 */       System.setProperty("spring.beaninfo.ignore", ignore
/* 207 */         .toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private void onApplicationPreparedEvent(ApplicationEvent event) {
/* 212 */     this.logger.replayTo(ConfigFileApplicationListener.class);
/* 213 */     addPostProcessors(((ApplicationPreparedEvent)event).getApplicationContext());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader)
/*     */   {
/* 224 */     RandomValuePropertySource.addToEnvironment(environment);
/* 225 */     new Loader(environment, resourceLoader).load();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bindToSpringApplication(ConfigurableEnvironment environment, SpringApplication application)
/*     */   {
/* 235 */     PropertiesConfigurationFactory<SpringApplication> binder = new PropertiesConfigurationFactory(application);
/*     */     
/* 237 */     binder.setTargetName("spring.main");
/* 238 */     binder.setConversionService(this.conversionService);
/* 239 */     binder.setPropertySources(environment.getPropertySources());
/*     */     try {
/* 241 */       binder.bindPropertiesToTarget();
/*     */     }
/*     */     catch (BindException ex) {
/* 244 */       throw new IllegalStateException("Cannot bind to SpringApplication", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addPostProcessors(ConfigurableApplicationContext context)
/*     */   {
/* 253 */     context.addBeanFactoryPostProcessor(new PropertySourceOrderingPostProcessor(context));
/*     */   }
/*     */   
/*     */   public void setOrder(int order)
/*     */   {
/* 258 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 263 */     return this.order;
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
/*     */   public void setSearchLocations(String locations)
/*     */   {
/* 276 */     Assert.hasLength(locations, "Locations must not be empty");
/* 277 */     this.searchLocations = locations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSearchNames(String names)
/*     */   {
/* 286 */     Assert.hasLength(names, "Names must not be empty");
/* 287 */     this.names = names;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class PropertySourceOrderingPostProcessor
/*     */     implements BeanFactoryPostProcessor, Ordered
/*     */   {
/*     */     private ConfigurableApplicationContext context;
/*     */     
/*     */ 
/*     */     PropertySourceOrderingPostProcessor(ConfigurableApplicationContext context)
/*     */     {
/* 300 */       this.context = context;
/*     */     }
/*     */     
/*     */     public int getOrder()
/*     */     {
/* 305 */       return Integer.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {
/* 311 */       reorderSources(this.context.getEnvironment());
/*     */     }
/*     */     
/*     */     private void reorderSources(ConfigurableEnvironment environment)
/*     */     {
/* 316 */       ConfigFileApplicationListener.ConfigurationPropertySources.finishAndRelocate(environment.getPropertySources());
/*     */       
/* 318 */       PropertySource<?> defaultProperties = environment.getPropertySources().remove("defaultProperties");
/* 319 */       if (defaultProperties != null) {
/* 320 */         environment.getPropertySources().addLast(defaultProperties);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class Loader
/*     */   {
/* 331 */     private final Log logger = ConfigFileApplicationListener.this.logger;
/*     */     
/*     */     private final ConfigurableEnvironment environment;
/*     */     
/*     */     private final ResourceLoader resourceLoader;
/*     */     
/*     */     private PropertySourcesLoader propertiesLoader;
/*     */     
/*     */     private Queue<ConfigFileApplicationListener.Profile> profiles;
/*     */     
/*     */     private List<ConfigFileApplicationListener.Profile> processedProfiles;
/*     */     private boolean activatedProfiles;
/*     */     
/*     */     Loader(ConfigurableEnvironment environment, ResourceLoader resourceLoader)
/*     */     {
/* 346 */       this.environment = environment;
/* 347 */       this.resourceLoader = (resourceLoader == null ? new DefaultResourceLoader() : resourceLoader);
/*     */     }
/*     */     
/*     */     public void load()
/*     */     {
/* 352 */       this.propertiesLoader = new PropertySourcesLoader();
/* 353 */       this.activatedProfiles = false;
/* 354 */       this.profiles = Collections.asLifoQueue(new LinkedList());
/* 355 */       this.processedProfiles = new LinkedList();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 360 */       Set<ConfigFileApplicationListener.Profile> initialActiveProfiles = initializeActiveProfiles();
/* 361 */       this.profiles.addAll(getUnprocessedActiveProfiles(initialActiveProfiles));
/* 362 */       String defaultProfileName; if (this.profiles.isEmpty()) {
/* 363 */         for (defaultProfileName : this.environment.getDefaultProfiles()) {
/* 364 */           ConfigFileApplicationListener.Profile defaultProfile = new ConfigFileApplicationListener.Profile(defaultProfileName, true);
/* 365 */           if (!this.profiles.contains(defaultProfile)) {
/* 366 */             this.profiles.add(defaultProfile);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 374 */       this.profiles.add(null);
/*     */       
/* 376 */       while (!this.profiles.isEmpty()) {
/* 377 */         ConfigFileApplicationListener.Profile profile = (ConfigFileApplicationListener.Profile)this.profiles.poll();
/* 378 */         for (Iterator localIterator = getSearchLocations().iterator(); localIterator.hasNext();) { location = (String)localIterator.next();
/* 379 */           if (!location.endsWith("/"))
/*     */           {
/*     */ 
/* 382 */             load(location, null, profile);
/*     */           }
/*     */           else {
/* 385 */             for (String name : getSearchNames())
/* 386 */               load(location, name, profile);
/*     */           }
/*     */         }
/*     */         String location;
/* 390 */         this.processedProfiles.add(profile);
/*     */       }
/*     */       
/* 393 */       addConfigurationProperties(this.propertiesLoader.getPropertySources());
/*     */     }
/*     */     
/*     */     private Set<ConfigFileApplicationListener.Profile> initializeActiveProfiles() {
/* 397 */       if ((!this.environment.containsProperty("spring.profiles.active")) && 
/* 398 */         (!this.environment.containsProperty("spring.profiles.include"))) {
/* 399 */         return Collections.emptySet();
/*     */       }
/*     */       
/*     */ 
/* 403 */       ConfigFileApplicationListener.SpringProfiles springProfiles = bindSpringProfiles(this.environment
/* 404 */         .getPropertySources());
/*     */       
/* 406 */       Set<ConfigFileApplicationListener.Profile> activeProfiles = new LinkedHashSet(springProfiles.getActiveProfiles());
/* 407 */       activeProfiles.addAll(springProfiles.getIncludeProfiles());
/* 408 */       maybeActivateProfiles(activeProfiles);
/* 409 */       return activeProfiles;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private List<ConfigFileApplicationListener.Profile> getUnprocessedActiveProfiles(Set<ConfigFileApplicationListener.Profile> initialActiveProfiles)
/*     */     {
/* 426 */       List<ConfigFileApplicationListener.Profile> unprocessedActiveProfiles = new ArrayList();
/* 427 */       for (String profileName : this.environment.getActiveProfiles()) {
/* 428 */         ConfigFileApplicationListener.Profile profile = new ConfigFileApplicationListener.Profile(profileName);
/* 429 */         if (!initialActiveProfiles.contains(profile)) {
/* 430 */           unprocessedActiveProfiles.add(profile);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 435 */       Collections.reverse(unprocessedActiveProfiles);
/* 436 */       return unprocessedActiveProfiles;
/*     */     }
/*     */     
/*     */     private void load(String location, String name, ConfigFileApplicationListener.Profile profile) {
/* 440 */       String group = "profile=" + (profile == null ? "" : profile);
/* 441 */       if (!StringUtils.hasText(name))
/*     */       {
/* 443 */         loadIntoGroup(group, location, profile);
/*     */       }
/*     */       else
/*     */       {
/* 447 */         for (String ext : this.propertiesLoader.getAllFileExtensions()) {
/* 448 */           if (profile != null)
/*     */           {
/* 450 */             loadIntoGroup(group, location + name + "-" + profile + "." + ext, null);
/*     */             
/* 452 */             for (ConfigFileApplicationListener.Profile processedProfile : this.processedProfiles) {
/* 453 */               if (processedProfile != null) {
/* 454 */                 loadIntoGroup(group, location + name + "-" + processedProfile + "." + ext, profile);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 461 */             loadIntoGroup(group, location + name + "-" + profile + "." + ext, profile);
/*     */           }
/*     */           
/*     */ 
/* 465 */           loadIntoGroup(group, location + name + "." + ext, profile);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private PropertySource<?> loadIntoGroup(String identifier, String location, ConfigFileApplicationListener.Profile profile)
/*     */     {
/*     */       try {
/* 473 */         return doLoadIntoGroup(identifier, location, profile);
/*     */       }
/*     */       catch (Exception ex) {
/* 476 */         throw new IllegalStateException("Failed to load property source from location '" + location + "'", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private PropertySource<?> doLoadIntoGroup(String identifier, String location, ConfigFileApplicationListener.Profile profile)
/*     */       throws IOException
/*     */     {
/* 484 */       Resource resource = this.resourceLoader.getResource(location);
/* 485 */       PropertySource<?> propertySource = null;
/* 486 */       StringBuilder msg = new StringBuilder();
/* 487 */       if ((resource != null) && (resource.exists())) {
/* 488 */         String name = "applicationConfig: [" + location + "]";
/* 489 */         String group = "applicationConfig: [" + identifier + "]";
/* 490 */         propertySource = this.propertiesLoader.load(resource, group, name, profile == null ? null : profile
/* 491 */           .getName());
/* 492 */         if (propertySource != null) {
/* 493 */           msg.append("Loaded ");
/* 494 */           handleProfileProperties(propertySource);
/*     */         }
/*     */         else {
/* 497 */           msg.append("Skipped (empty) ");
/*     */         }
/*     */       }
/*     */       else {
/* 501 */         msg.append("Skipped ");
/*     */       }
/* 503 */       msg.append("config file ");
/* 504 */       msg.append(getResourceDescription(location, resource));
/* 505 */       if (profile != null) {
/* 506 */         msg.append(" for profile ").append(profile);
/*     */       }
/* 508 */       if ((resource == null) || (!resource.exists())) {
/* 509 */         msg.append(" resource not found");
/* 510 */         this.logger.trace(msg);
/*     */       }
/*     */       else {
/* 513 */         this.logger.debug(msg);
/*     */       }
/* 515 */       return propertySource;
/*     */     }
/*     */     
/*     */     private String getResourceDescription(String location, Resource resource) {
/* 519 */       String resourceDescription = "'" + location + "'";
/* 520 */       if (resource != null) {
/*     */         try {
/* 522 */           resourceDescription = String.format("'%s' (%s)", new Object[] {resource
/* 523 */             .getURI().toASCIIString(), location });
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */       }
/*     */       
/*     */ 
/* 529 */       return resourceDescription;
/*     */     }
/*     */     
/*     */     private void handleProfileProperties(PropertySource<?> propertySource) {
/* 533 */       ConfigFileApplicationListener.SpringProfiles springProfiles = bindSpringProfiles(propertySource);
/* 534 */       maybeActivateProfiles(springProfiles.getActiveProfiles());
/* 535 */       addProfiles(springProfiles.getIncludeProfiles());
/*     */     }
/*     */     
/*     */     private ConfigFileApplicationListener.SpringProfiles bindSpringProfiles(PropertySource<?> propertySource) {
/* 539 */       MutablePropertySources propertySources = new MutablePropertySources();
/* 540 */       propertySources.addFirst(propertySource);
/* 541 */       return bindSpringProfiles(propertySources);
/*     */     }
/*     */     
/*     */     private ConfigFileApplicationListener.SpringProfiles bindSpringProfiles(PropertySources propertySources) {
/* 545 */       ConfigFileApplicationListener.SpringProfiles springProfiles = new ConfigFileApplicationListener.SpringProfiles();
/* 546 */       RelaxedDataBinder dataBinder = new RelaxedDataBinder(springProfiles, "spring.profiles");
/*     */       
/* 548 */       dataBinder.bind(new PropertySourcesPropertyValues(propertySources, false));
/* 549 */       springProfiles.setActive(resolvePlaceholders(springProfiles.getActive()));
/* 550 */       springProfiles.setInclude(resolvePlaceholders(springProfiles.getInclude()));
/* 551 */       return springProfiles;
/*     */     }
/*     */     
/*     */     private List<String> resolvePlaceholders(List<String> values) {
/* 555 */       List<String> resolved = new ArrayList();
/* 556 */       for (String value : values) {
/* 557 */         resolved.add(this.environment.resolvePlaceholders(value));
/*     */       }
/* 559 */       return resolved;
/*     */     }
/*     */     
/*     */     private void maybeActivateProfiles(Set<ConfigFileApplicationListener.Profile> profiles) {
/* 563 */       if (this.activatedProfiles) {
/* 564 */         if (!profiles.isEmpty()) {
/* 565 */           this.logger.debug("Profiles already activated, '" + profiles + "' will not be applied");
/*     */         }
/*     */         
/* 568 */         return;
/*     */       }
/* 570 */       if (!profiles.isEmpty()) {
/* 571 */         addProfiles(profiles);
/* 572 */         this.logger.debug("Activated profiles " + 
/* 573 */           StringUtils.collectionToCommaDelimitedString(profiles));
/* 574 */         this.activatedProfiles = true;
/* 575 */         removeUnprocessedDefaultProfiles();
/*     */       }
/*     */     }
/*     */     
/*     */     private void removeUnprocessedDefaultProfiles() {
/* 580 */       Iterator<ConfigFileApplicationListener.Profile> iterator = this.profiles.iterator();
/* 581 */       while (iterator.hasNext()) {
/* 582 */         if (((ConfigFileApplicationListener.Profile)iterator.next()).isDefaultProfile()) {
/* 583 */           iterator.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void addProfiles(Set<ConfigFileApplicationListener.Profile> profiles) {
/* 589 */       for (ConfigFileApplicationListener.Profile profile : profiles) {
/* 590 */         this.profiles.add(profile);
/* 591 */         if (!environmentHasActiveProfile(profile.getName()))
/*     */         {
/*     */ 
/* 594 */           prependProfile(this.environment, profile);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean environmentHasActiveProfile(String profile) {
/* 600 */       for (String activeProfile : this.environment.getActiveProfiles()) {
/* 601 */         if (activeProfile.equals(profile)) {
/* 602 */           return true;
/*     */         }
/*     */       }
/* 605 */       return false;
/*     */     }
/*     */     
/*     */     private void prependProfile(ConfigurableEnvironment environment, ConfigFileApplicationListener.Profile profile)
/*     */     {
/* 610 */       Set<String> profiles = new LinkedHashSet();
/* 611 */       environment.getActiveProfiles();
/*     */       
/* 613 */       profiles.add(profile.getName());
/* 614 */       profiles.addAll(Arrays.asList(environment.getActiveProfiles()));
/* 615 */       environment.setActiveProfiles((String[])profiles.toArray(new String[profiles.size()]));
/*     */     }
/*     */     
/*     */     private Set<String> getSearchLocations() {
/* 619 */       Set<String> locations = new LinkedHashSet();
/*     */       
/* 621 */       if (this.environment.containsProperty("spring.config.location")) {
/* 622 */         for (String path : asResolvedSet(this.environment
/* 623 */           .getProperty("spring.config.location"), null)) {
/* 624 */           if (!path.contains("$")) {
/* 625 */             path = StringUtils.cleanPath(path);
/* 626 */             if (!ResourceUtils.isUrl(path)) {
/* 627 */               path = "file:" + path;
/*     */             }
/*     */           }
/* 630 */           locations.add(path);
/*     */         }
/*     */       }
/* 633 */       locations.addAll(
/* 634 */         asResolvedSet(ConfigFileApplicationListener.this.searchLocations, "classpath:/,classpath:/config/,file:./,file:./config/"));
/*     */       
/* 636 */       return locations;
/*     */     }
/*     */     
/*     */     private Set<String> getSearchNames() {
/* 640 */       if (this.environment.containsProperty("spring.config.name")) {
/* 641 */         return asResolvedSet(this.environment.getProperty("spring.config.name"), null);
/*     */       }
/*     */       
/* 644 */       return asResolvedSet(ConfigFileApplicationListener.this.names, "application");
/*     */     }
/*     */     
/*     */     private Set<String> asResolvedSet(String value, String fallback) {
/* 648 */       List<String> list = Arrays.asList(StringUtils.trimArrayElements(
/* 649 */         StringUtils.commaDelimitedListToStringArray(value != null ? this.environment
/* 650 */         .resolvePlaceholders(value) : fallback)));
/* 651 */       Collections.reverse(list);
/* 652 */       return new LinkedHashSet(list);
/*     */     }
/*     */     
/*     */     private void addConfigurationProperties(MutablePropertySources sources) {
/* 656 */       List<PropertySource<?>> reorderedSources = new ArrayList();
/* 657 */       for (PropertySource<?> item : sources) {
/* 658 */         reorderedSources.add(item);
/*     */       }
/* 660 */       addConfigurationProperties(new ConfigFileApplicationListener.ConfigurationPropertySources(reorderedSources));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void addConfigurationProperties(ConfigFileApplicationListener.ConfigurationPropertySources configurationSources)
/*     */     {
/* 667 */       MutablePropertySources existingSources = this.environment.getPropertySources();
/* 668 */       if (existingSources.contains("defaultProperties")) {
/* 669 */         existingSources.addBefore("defaultProperties", configurationSources);
/*     */       }
/*     */       else {
/* 672 */         existingSources.addLast(configurationSources);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Profile
/*     */   {
/*     */     private final String name;
/*     */     private final boolean defaultProfile;
/*     */     
/*     */     Profile(String name)
/*     */     {
/* 685 */       this(name, false);
/*     */     }
/*     */     
/*     */     Profile(String name, boolean defaultProfile) {
/* 689 */       Assert.notNull(name, "Name must not be null");
/* 690 */       this.name = name;
/* 691 */       this.defaultProfile = defaultProfile;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 695 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isDefaultProfile() {
/* 699 */       return this.defaultProfile;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 704 */       return this.name;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 709 */       return this.name.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 714 */       if (obj == this) {
/* 715 */         return true;
/*     */       }
/* 717 */       if ((obj == null) || (obj.getClass() != getClass())) {
/* 718 */         return false;
/*     */       }
/* 720 */       return ((Profile)obj).name.equals(this.name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class ConfigurationPropertySources
/*     */     extends EnumerablePropertySource<Collection<PropertySource<?>>>
/*     */   {
/*     */     private final Collection<PropertySource<?>> sources;
/*     */     
/*     */ 
/*     */     private final String[] names;
/*     */     
/*     */ 
/*     */     ConfigurationPropertySources(Collection<PropertySource<?>> sources)
/*     */     {
/* 737 */       super(sources);
/* 738 */       this.sources = sources;
/* 739 */       List<String> names = new ArrayList();
/* 740 */       for (PropertySource<?> source : sources) {
/* 741 */         if ((source instanceof EnumerablePropertySource)) {
/* 742 */           names.addAll(Arrays.asList(((EnumerablePropertySource)source)
/* 743 */             .getPropertyNames()));
/*     */         }
/*     */       }
/* 746 */       this.names = ((String[])names.toArray(new String[names.size()]));
/*     */     }
/*     */     
/*     */     public Object getProperty(String name)
/*     */     {
/* 751 */       for (PropertySource<?> propertySource : this.sources) {
/* 752 */         Object value = propertySource.getProperty(name);
/* 753 */         if (value != null) {
/* 754 */           return value;
/*     */         }
/*     */       }
/* 757 */       return null;
/*     */     }
/*     */     
/*     */     public static void finishAndRelocate(MutablePropertySources propertySources) {
/* 761 */       String name = "applicationConfigurationProperties";
/*     */       
/* 763 */       ConfigurationPropertySources removed = (ConfigurationPropertySources)propertySources.get(name);
/* 764 */       if (removed != null) {
/* 765 */         for (PropertySource<?> propertySource : removed.sources) {
/* 766 */           if ((propertySource instanceof EnumerableCompositePropertySource)) {
/* 767 */             EnumerableCompositePropertySource composite = (EnumerableCompositePropertySource)propertySource;
/* 768 */             for (PropertySource<?> nested : (Collection)composite.getSource()) {
/* 769 */               propertySources.addAfter(name, nested);
/* 770 */               name = nested.getName();
/*     */             }
/*     */           }
/*     */           else {
/* 774 */             propertySources.addAfter(name, propertySource);
/*     */           }
/*     */         }
/* 777 */         propertySources.remove("applicationConfigurationProperties");
/*     */       }
/*     */     }
/*     */     
/*     */     public String[] getPropertyNames()
/*     */     {
/* 783 */       return this.names;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class SpringProfiles
/*     */   {
/* 793 */     private List<String> active = new ArrayList();
/*     */     
/* 795 */     private List<String> include = new ArrayList();
/*     */     
/*     */     public List<String> getActive() {
/* 798 */       return this.active;
/*     */     }
/*     */     
/*     */     public void setActive(List<String> active) {
/* 802 */       this.active = active;
/*     */     }
/*     */     
/*     */     public List<String> getInclude() {
/* 806 */       return this.include;
/*     */     }
/*     */     
/*     */     public void setInclude(List<String> include) {
/* 810 */       this.include = include;
/*     */     }
/*     */     
/*     */     Set<ConfigFileApplicationListener.Profile> getActiveProfiles() {
/* 814 */       return asProfileSet(this.active);
/*     */     }
/*     */     
/*     */     Set<ConfigFileApplicationListener.Profile> getIncludeProfiles() {
/* 818 */       return asProfileSet(this.include);
/*     */     }
/*     */     
/*     */     private Set<ConfigFileApplicationListener.Profile> asProfileSet(List<String> profileNames) {
/* 822 */       List<ConfigFileApplicationListener.Profile> profiles = new ArrayList();
/* 823 */       for (String profileName : profileNames) {
/* 824 */         profiles.add(new ConfigFileApplicationListener.Profile(profileName));
/*     */       }
/* 826 */       Collections.reverse(profiles);
/* 827 */       return new LinkedHashSet(profiles);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\ConfigFileApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */