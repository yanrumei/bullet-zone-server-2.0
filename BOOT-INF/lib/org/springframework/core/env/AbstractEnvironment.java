/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractEnvironment
/*     */   implements ConfigurableEnvironment
/*     */ {
/*     */   public static final String IGNORE_GETENV_PROPERTY_NAME = "spring.getenv.ignore";
/*     */   public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
/*     */   public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
/*     */   protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
/* 102 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 104 */   private final Set<String> activeProfiles = new LinkedHashSet();
/*     */   
/* 106 */   private final Set<String> defaultProfiles = new LinkedHashSet(getReservedDefaultProfiles());
/*     */   
/* 108 */   private final MutablePropertySources propertySources = new MutablePropertySources(this.logger);
/*     */   
/* 110 */   private final ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(this.propertySources);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractEnvironment()
/*     */   {
/* 122 */     customizePropertySources(this.propertySources);
/* 123 */     if (this.logger.isDebugEnabled()) {
/* 124 */       this.logger.debug("Initialized " + getClass().getSimpleName() + " with PropertySources " + this.propertySources);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void customizePropertySources(MutablePropertySources propertySources) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Set<String> getReservedDefaultProfiles()
/*     */   {
/* 215 */     return Collections.singleton("default");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getActiveProfiles()
/*     */   {
/* 225 */     return StringUtils.toStringArray(doGetActiveProfiles());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Set<String> doGetActiveProfiles()
/*     */   {
/* 237 */     synchronized (this.activeProfiles) {
/* 238 */       if (this.activeProfiles.isEmpty()) {
/* 239 */         String profiles = getProperty("spring.profiles.active");
/* 240 */         if (StringUtils.hasText(profiles)) {
/* 241 */           setActiveProfiles(StringUtils.commaDelimitedListToStringArray(
/* 242 */             StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       }
/* 245 */       return this.activeProfiles;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setActiveProfiles(String... profiles)
/*     */   {
/* 251 */     Assert.notNull(profiles, "Profile array must not be null");
/* 252 */     synchronized (this.activeProfiles) {
/* 253 */       this.activeProfiles.clear();
/* 254 */       for (String profile : profiles) {
/* 255 */         validateProfile(profile);
/* 256 */         this.activeProfiles.add(profile);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addActiveProfile(String profile)
/*     */   {
/* 263 */     if (this.logger.isDebugEnabled()) {
/* 264 */       this.logger.debug("Activating profile '" + profile + "'");
/*     */     }
/* 266 */     validateProfile(profile);
/* 267 */     doGetActiveProfiles();
/* 268 */     synchronized (this.activeProfiles) {
/* 269 */       this.activeProfiles.add(profile);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getDefaultProfiles()
/*     */   {
/* 276 */     return StringUtils.toStringArray(doGetDefaultProfiles());
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
/*     */   protected Set<String> doGetDefaultProfiles()
/*     */   {
/* 292 */     synchronized (this.defaultProfiles) {
/* 293 */       if (this.defaultProfiles.equals(getReservedDefaultProfiles())) {
/* 294 */         String profiles = getProperty("spring.profiles.default");
/* 295 */         if (StringUtils.hasText(profiles)) {
/* 296 */           setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(
/* 297 */             StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       }
/* 300 */       return this.defaultProfiles;
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
/*     */   public void setDefaultProfiles(String... profiles)
/*     */   {
/* 314 */     Assert.notNull(profiles, "Profile array must not be null");
/* 315 */     synchronized (this.defaultProfiles) {
/* 316 */       this.defaultProfiles.clear();
/* 317 */       for (String profile : profiles) {
/* 318 */         validateProfile(profile);
/* 319 */         this.defaultProfiles.add(profile);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean acceptsProfiles(String... profiles)
/*     */   {
/* 326 */     Assert.notEmpty(profiles, "Must specify at least one profile");
/* 327 */     for (String profile : profiles) {
/* 328 */       if ((StringUtils.hasLength(profile)) && (profile.charAt(0) == '!')) {
/* 329 */         if (!isProfileActive(profile.substring(1))) {
/* 330 */           return true;
/*     */         }
/*     */       }
/* 333 */       else if (isProfileActive(profile)) {
/* 334 */         return true;
/*     */       }
/*     */     }
/* 337 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isProfileActive(String profile)
/*     */   {
/* 346 */     validateProfile(profile);
/* 347 */     Set<String> currentActiveProfiles = doGetActiveProfiles();
/* 348 */     return (currentActiveProfiles.contains(profile)) || (
/* 349 */       (currentActiveProfiles.isEmpty()) && (doGetDefaultProfiles().contains(profile)));
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
/*     */   protected void validateProfile(String profile)
/*     */   {
/* 363 */     if (!StringUtils.hasText(profile)) {
/* 364 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must contain text");
/*     */     }
/* 366 */     if (profile.charAt(0) == '!') {
/* 367 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must not begin with ! operator");
/*     */     }
/*     */   }
/*     */   
/*     */   public MutablePropertySources getPropertySources()
/*     */   {
/* 373 */     return this.propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Object> getSystemEnvironment()
/*     */   {
/* 379 */     if (suppressGetenvAccess()) {
/* 380 */       return Collections.emptyMap();
/*     */     }
/*     */     try {
/* 383 */       return System.getenv();
/*     */     }
/*     */     catch (AccessControlException ex) {}
/* 386 */     new ReadOnlySystemAttributesMap()
/*     */     {
/*     */       protected String getSystemAttribute(String attributeName) {
/*     */         try {
/* 390 */           return System.getenv(attributeName);
/*     */         }
/*     */         catch (AccessControlException ex) {
/* 393 */           if (AbstractEnvironment.this.logger.isInfoEnabled())
/* 394 */             AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system environment variable '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 395 */               .getMessage());
/*     */         }
/* 397 */         return null;
/*     */       }
/*     */     };
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
/*     */   protected boolean suppressGetenvAccess()
/*     */   {
/* 416 */     return SpringProperties.getFlag("spring.getenv.ignore");
/*     */   }
/*     */   
/*     */   public Map<String, Object> getSystemProperties()
/*     */   {
/*     */     try
/*     */     {
/* 423 */       return System.getProperties();
/*     */     }
/*     */     catch (AccessControlException ex) {}
/* 426 */     new ReadOnlySystemAttributesMap()
/*     */     {
/*     */       protected String getSystemAttribute(String attributeName) {
/*     */         try {
/* 430 */           return System.getProperty(attributeName);
/*     */         }
/*     */         catch (AccessControlException ex) {
/* 433 */           if (AbstractEnvironment.this.logger.isInfoEnabled())
/* 434 */             AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system property '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 435 */               .getMessage());
/*     */         }
/* 437 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void merge(ConfigurableEnvironment parent)
/*     */   {
/* 446 */     for (PropertySource<?> ps : parent.getPropertySources()) {
/* 447 */       if (!this.propertySources.contains(ps.getName())) {
/* 448 */         this.propertySources.addLast(ps);
/*     */       }
/*     */     }
/* 451 */     String[] parentActiveProfiles = parent.getActiveProfiles();
/* 452 */     String str1; String profile; if (!ObjectUtils.isEmpty(parentActiveProfiles)) {
/* 453 */       synchronized (this.activeProfiles) {
/* 454 */         String[] arrayOfString1 = parentActiveProfiles;int i = arrayOfString1.length; for (str1 = 0; str1 < i; str1++) { profile = arrayOfString1[str1];
/* 455 */           this.activeProfiles.add(profile);
/*     */         }
/*     */       }
/*     */     }
/* 459 */     String[] parentDefaultProfiles = parent.getDefaultProfiles();
/* 460 */     if (!ObjectUtils.isEmpty(parentDefaultProfiles)) {
/* 461 */       synchronized (this.defaultProfiles) {
/* 462 */         this.defaultProfiles.remove("default");
/* 463 */         String[] arrayOfString2 = parentDefaultProfiles;str1 = arrayOfString2.length; for (profile = 0; profile < str1; profile++) { String profile = arrayOfString2[profile];
/* 464 */           this.defaultProfiles.add(profile);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConfigurableConversionService getConversionService()
/*     */   {
/* 477 */     return this.propertyResolver.getConversionService();
/*     */   }
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService)
/*     */   {
/* 482 */     this.propertyResolver.setConversionService(conversionService);
/*     */   }
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix)
/*     */   {
/* 487 */     this.propertyResolver.setPlaceholderPrefix(placeholderPrefix);
/*     */   }
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix)
/*     */   {
/* 492 */     this.propertyResolver.setPlaceholderSuffix(placeholderSuffix);
/*     */   }
/*     */   
/*     */   public void setValueSeparator(String valueSeparator)
/*     */   {
/* 497 */     this.propertyResolver.setValueSeparator(valueSeparator);
/*     */   }
/*     */   
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders)
/*     */   {
/* 502 */     this.propertyResolver.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
/*     */   }
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties)
/*     */   {
/* 507 */     this.propertyResolver.setRequiredProperties(requiredProperties);
/*     */   }
/*     */   
/*     */   public void validateRequiredProperties() throws MissingRequiredPropertiesException
/*     */   {
/* 512 */     this.propertyResolver.validateRequiredProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsProperty(String key)
/*     */   {
/* 522 */     return this.propertyResolver.containsProperty(key);
/*     */   }
/*     */   
/*     */   public String getProperty(String key)
/*     */   {
/* 527 */     return this.propertyResolver.getProperty(key);
/*     */   }
/*     */   
/*     */   public String getProperty(String key, String defaultValue)
/*     */   {
/* 532 */     return this.propertyResolver.getProperty(key, defaultValue);
/*     */   }
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType)
/*     */   {
/* 537 */     return (T)this.propertyResolver.getProperty(key, targetType);
/*     */   }
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue)
/*     */   {
/* 542 */     return (T)this.propertyResolver.getProperty(key, targetType, defaultValue);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType)
/*     */   {
/* 548 */     return this.propertyResolver.getPropertyAsClass(key, targetType);
/*     */   }
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException
/*     */   {
/* 553 */     return this.propertyResolver.getRequiredProperty(key);
/*     */   }
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException
/*     */   {
/* 558 */     return (T)this.propertyResolver.getRequiredProperty(key, targetType);
/*     */   }
/*     */   
/*     */   public String resolvePlaceholders(String text)
/*     */   {
/* 563 */     return this.propertyResolver.resolvePlaceholders(text);
/*     */   }
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException
/*     */   {
/* 568 */     return this.propertyResolver.resolveRequiredPlaceholders(text);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 574 */     return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles + ", defaultProfiles=" + this.defaultProfiles + ", propertySources=" + this.propertySources + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\AbstractEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */