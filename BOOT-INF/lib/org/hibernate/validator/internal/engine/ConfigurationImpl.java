/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.BootstrapConfiguration;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.ValidationProviderResolver;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.spi.BootstrapState;
/*     */ import javax.validation.spi.ConfigurationState;
/*     */ import javax.validation.spi.ValidationProvider;
/*     */ import org.hibernate.validator.HibernateValidatorConfiguration;
/*     */ import org.hibernate.validator.cfg.ConstraintMapping;
/*     */ import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
/*     */ import org.hibernate.validator.internal.engine.resolver.DefaultTraversableResolver;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.OptionalValueUnwrapper;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*     */ import org.hibernate.validator.internal.util.Version;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.SetContextClassLoader;
/*     */ import org.hibernate.validator.internal.xml.ValidationBootstrapParameters;
/*     */ import org.hibernate.validator.internal.xml.ValidationXmlParser;
/*     */ import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
/*     */ import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
/*     */ import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;
/*     */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
/*     */ import org.hibernate.validator.spi.time.TimeProvider;
/*     */ import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationImpl
/*     */   implements HibernateValidatorConfiguration, ConfigurationState
/*     */ {
/*     */   private static final String JFX_UNWRAPPER_CLASS = "org.hibernate.validator.internal.engine.valuehandling.JavaFXPropertyValueUnwrapper";
/*     */   
/*     */   static
/*     */   {
/*  71 */     Version.touch();
/*     */   }
/*     */   
/*  74 */   private static final Log log = LoggerFactory.make();
/*     */   
/*     */   private final ResourceBundleLocator defaultResourceBundleLocator;
/*     */   
/*     */   private MessageInterpolator defaultMessageInterpolator;
/*     */   
/*     */   private MessageInterpolator messageInterpolator;
/*     */   
/*     */   private final TraversableResolver defaultTraversableResolver;
/*     */   
/*     */   private final ConstraintValidatorFactory defaultConstraintValidatorFactory;
/*     */   
/*     */   private final ParameterNameProvider defaultParameterNameProvider;
/*     */   
/*     */   private final ConstraintMappingContributor serviceLoaderBasedConstraintMappingContributor;
/*     */   private ValidationProviderResolver providerResolver;
/*     */   private final ValidationBootstrapParameters validationBootstrapParameters;
/*  91 */   private boolean ignoreXmlConfiguration = false;
/*  92 */   private final Set<InputStream> configurationStreams = CollectionHelper.newHashSet();
/*     */   
/*     */   private BootstrapConfiguration bootstrapConfiguration;
/*     */   
/*  96 */   private final Set<DefaultConstraintMapping> programmaticMappings = CollectionHelper.newHashSet();
/*     */   private boolean failFast;
/*  98 */   private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers = CollectionHelper.newArrayList();
/*     */   private ClassLoader externalClassLoader;
/*     */   private TimeProvider timeProvider;
/* 101 */   private final MethodValidationConfiguration methodValidationConfiguration = new MethodValidationConfiguration();
/*     */   
/*     */   public ConfigurationImpl(BootstrapState state) {
/* 104 */     this();
/* 105 */     if (state.getValidationProviderResolver() == null) {
/* 106 */       this.providerResolver = state.getDefaultValidationProviderResolver();
/*     */     }
/*     */     else {
/* 109 */       this.providerResolver = state.getValidationProviderResolver();
/*     */     }
/*     */   }
/*     */   
/*     */   public ConfigurationImpl(ValidationProvider<?> provider) {
/* 114 */     this();
/* 115 */     if (provider == null) {
/* 116 */       throw log.getInconsistentConfigurationException();
/*     */     }
/* 118 */     this.providerResolver = null;
/* 119 */     this.validationBootstrapParameters.setProvider(provider);
/*     */   }
/*     */   
/*     */   private ConfigurationImpl() {
/* 123 */     this.validationBootstrapParameters = new ValidationBootstrapParameters();
/* 124 */     TypeResolutionHelper typeResolutionHelper = new TypeResolutionHelper();
/* 125 */     if (isJavaFxInClasspath()) {
/* 126 */       this.validatedValueHandlers.add(createJavaFXUnwrapperClass(typeResolutionHelper));
/*     */     }
/* 128 */     if (Version.getJavaRelease() >= 8) {
/* 129 */       this.validatedValueHandlers.add(new OptionalValueUnwrapper(typeResolutionHelper));
/*     */     }
/* 131 */     this.defaultResourceBundleLocator = new PlatformResourceBundleLocator("ValidationMessages");
/*     */     
/*     */ 
/* 134 */     this.defaultTraversableResolver = new DefaultTraversableResolver();
/* 135 */     this.defaultConstraintValidatorFactory = new ConstraintValidatorFactoryImpl();
/* 136 */     this.defaultParameterNameProvider = new DefaultParameterNameProvider();
/* 137 */     this.serviceLoaderBasedConstraintMappingContributor = new ServiceLoaderBasedConstraintMappingContributor(typeResolutionHelper);
/*     */   }
/*     */   
/*     */   private ValidatedValueUnwrapper<?> createJavaFXUnwrapperClass(TypeResolutionHelper typeResolutionHelper)
/*     */   {
/*     */     try
/*     */     {
/* 144 */       Class<?> jfxUnwrapperClass = (Class)run(LoadClass.action("org.hibernate.validator.internal.engine.valuehandling.JavaFXPropertyValueUnwrapper", getClass().getClassLoader()));
/* 145 */       return 
/* 146 */         (ValidatedValueUnwrapper)jfxUnwrapperClass.getConstructor(new Class[] { TypeResolutionHelper.class }).newInstance(new Object[] { typeResolutionHelper });
/*     */     }
/*     */     catch (Exception e) {
/* 149 */       throw log.validatedValueUnwrapperCannotBeCreated("org.hibernate.validator.internal.engine.valuehandling.JavaFXPropertyValueUnwrapper", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final HibernateValidatorConfiguration ignoreXmlConfiguration()
/*     */   {
/* 155 */     this.ignoreXmlConfiguration = true;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public final ConfigurationImpl messageInterpolator(MessageInterpolator interpolator)
/*     */   {
/* 161 */     if ((log.isDebugEnabled()) && 
/* 162 */       (interpolator != null)) {
/* 163 */       log.debug("Setting custom MessageInterpolator of type " + interpolator.getClass().getName());
/*     */     }
/*     */     
/* 166 */     this.validationBootstrapParameters.setMessageInterpolator(interpolator);
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public final ConfigurationImpl traversableResolver(TraversableResolver resolver)
/*     */   {
/* 172 */     if ((log.isDebugEnabled()) && 
/* 173 */       (resolver != null)) {
/* 174 */       log.debug("Setting custom TraversableResolver of type " + resolver.getClass().getName());
/*     */     }
/*     */     
/* 177 */     this.validationBootstrapParameters.setTraversableResolver(resolver);
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public final ConfigurationImpl constraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory)
/*     */   {
/* 183 */     if ((log.isDebugEnabled()) && 
/* 184 */       (constraintValidatorFactory != null)) {
/* 185 */       log.debug("Setting custom ConstraintValidatorFactory of type " + constraintValidatorFactory
/* 186 */         .getClass()
/* 187 */         .getName());
/*     */     }
/*     */     
/*     */ 
/* 191 */     this.validationBootstrapParameters.setConstraintValidatorFactory(constraintValidatorFactory);
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration parameterNameProvider(ParameterNameProvider parameterNameProvider)
/*     */   {
/* 197 */     if ((log.isDebugEnabled()) && 
/* 198 */       (parameterNameProvider != null)) {
/* 199 */       log.debug("Setting custom ParameterNameProvider of type " + parameterNameProvider
/* 200 */         .getClass()
/* 201 */         .getName());
/*     */     }
/*     */     
/*     */ 
/* 205 */     this.validationBootstrapParameters.setParameterNameProvider(parameterNameProvider);
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   public final HibernateValidatorConfiguration addMapping(InputStream stream)
/*     */   {
/* 211 */     Contracts.assertNotNull(stream, Messages.MESSAGES.inputStreamCannotBeNull());
/*     */     
/* 213 */     this.validationBootstrapParameters.addMapping(stream.markSupported() ? stream : new BufferedInputStream(stream));
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public final HibernateValidatorConfiguration failFast(boolean failFast)
/*     */   {
/* 219 */     this.failFast = failFast;
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration allowOverridingMethodAlterParameterConstraint(boolean allow)
/*     */   {
/* 225 */     this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(allow);
/* 226 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAllowOverridingMethodAlterParameterConstraint() {
/* 230 */     return this.methodValidationConfiguration.isAllowOverridingMethodAlterParameterConstraint();
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration allowMultipleCascadedValidationOnReturnValues(boolean allow)
/*     */   {
/* 235 */     this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(allow);
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAllowMultipleCascadedValidationOnReturnValues() {
/* 240 */     return this.methodValidationConfiguration.isAllowMultipleCascadedValidationOnReturnValues();
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration allowParallelMethodsDefineParameterConstraints(boolean allow)
/*     */   {
/* 245 */     this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(allow);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAllowParallelMethodsDefineParameterConstraints() {
/* 250 */     return this.methodValidationConfiguration.isAllowParallelMethodsDefineParameterConstraints();
/*     */   }
/*     */   
/*     */   public MethodValidationConfiguration getMethodValidationConfiguration() {
/* 254 */     return this.methodValidationConfiguration;
/*     */   }
/*     */   
/*     */   public final DefaultConstraintMapping createConstraintMapping()
/*     */   {
/* 259 */     return new DefaultConstraintMapping();
/*     */   }
/*     */   
/*     */   public final HibernateValidatorConfiguration addMapping(ConstraintMapping mapping)
/*     */   {
/* 264 */     Contracts.assertNotNull(mapping, Messages.MESSAGES.parameterMustNotBeNull("mapping"));
/*     */     
/* 266 */     this.programmaticMappings.add((DefaultConstraintMapping)mapping);
/* 267 */     return this;
/*     */   }
/*     */   
/*     */   public final HibernateValidatorConfiguration addProperty(String name, String value)
/*     */   {
/* 272 */     if (value != null) {
/* 273 */       this.validationBootstrapParameters.addConfigProperty(name, value);
/*     */     }
/* 275 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public HibernateValidatorConfiguration addValidatedValueHandler(ValidatedValueUnwrapper<?> handler)
/*     */   {
/* 281 */     Contracts.assertNotNull(handler, Messages.MESSAGES.parameterMustNotBeNull("handler"));
/* 282 */     this.validatedValueHandlers.add(handler);
/*     */     
/* 284 */     return this;
/*     */   }
/*     */   
/*     */   public final ConstraintMappingContributor getServiceLoaderBasedConstraintMappingContributor() {
/* 288 */     return this.serviceLoaderBasedConstraintMappingContributor;
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration externalClassLoader(ClassLoader externalClassLoader)
/*     */   {
/* 293 */     Contracts.assertNotNull(externalClassLoader, Messages.MESSAGES.parameterMustNotBeNull("externalClassLoader"));
/* 294 */     this.externalClassLoader = externalClassLoader;
/*     */     
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorConfiguration timeProvider(TimeProvider timeProvider)
/*     */   {
/* 301 */     Contracts.assertNotNull(timeProvider, Messages.MESSAGES.parameterMustNotBeNull("timeProvider"));
/* 302 */     this.timeProvider = timeProvider;
/*     */     
/* 304 */     return this;
/*     */   }
/*     */   
/*     */   public final ValidatorFactory buildValidatorFactory()
/*     */   {
/* 309 */     parseValidationXml();
/* 310 */     ValidatorFactory factory = null;
/*     */     try {
/* 312 */       if (isSpecificProvider()) {
/* 313 */         factory = this.validationBootstrapParameters.getProvider().buildValidatorFactory(this);
/*     */       }
/*     */       else {
/* 316 */         providerClass = this.validationBootstrapParameters.getProviderClass();
/* 317 */         if (providerClass != null) {
/* 318 */           for (ValidationProvider<?> provider : this.providerResolver.getValidationProviders()) {
/* 319 */             if (providerClass.isAssignableFrom(provider.getClass())) {
/* 320 */               factory = provider.buildValidatorFactory(this);
/* 321 */               break;
/*     */             }
/*     */           }
/* 324 */           if (factory == null) {
/* 325 */             throw log.getUnableToFindProviderException(providerClass);
/*     */           }
/*     */         }
/*     */         else {
/* 329 */           Object providers = this.providerResolver.getValidationProviders();
/* 330 */           assert (((List)providers).size() != 0);
/* 331 */           factory = ((ValidationProvider)((List)providers).get(0)).buildValidatorFactory(this);
/*     */         }
/*     */       }
/*     */     } finally {
/*     */       Class<? extends ValidationProvider<?>> providerClass;
/*     */       InputStream in;
/* 337 */       for (InputStream in : this.configurationStreams) {
/*     */         try {
/* 339 */           in.close();
/*     */         }
/*     */         catch (IOException io) {
/* 342 */           log.unableToCloseInputStream();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 347 */     return factory;
/*     */   }
/*     */   
/*     */   public final boolean isIgnoreXmlConfiguration()
/*     */   {
/* 352 */     return this.ignoreXmlConfiguration;
/*     */   }
/*     */   
/*     */   public final MessageInterpolator getMessageInterpolator()
/*     */   {
/* 357 */     if (this.messageInterpolator == null)
/*     */     {
/* 359 */       MessageInterpolator interpolator = this.validationBootstrapParameters.getMessageInterpolator();
/* 360 */       if (interpolator != null) {
/* 361 */         this.messageInterpolator = interpolator;
/*     */       }
/*     */       else {
/* 364 */         this.messageInterpolator = getDefaultMessageInterpolatorConfiguredWithClassLoader();
/*     */       }
/*     */     }
/*     */     
/* 368 */     return this.messageInterpolator;
/*     */   }
/*     */   
/*     */   public final Set<InputStream> getMappingStreams()
/*     */   {
/* 373 */     return this.validationBootstrapParameters.getMappings();
/*     */   }
/*     */   
/*     */   public final boolean getFailFast() {
/* 377 */     return this.failFast;
/*     */   }
/*     */   
/*     */   public final ConstraintValidatorFactory getConstraintValidatorFactory()
/*     */   {
/* 382 */     return this.validationBootstrapParameters.getConstraintValidatorFactory();
/*     */   }
/*     */   
/*     */   public final TraversableResolver getTraversableResolver()
/*     */   {
/* 387 */     return this.validationBootstrapParameters.getTraversableResolver();
/*     */   }
/*     */   
/*     */   public BootstrapConfiguration getBootstrapConfiguration()
/*     */   {
/* 392 */     if (this.bootstrapConfiguration == null) {
/* 393 */       this.bootstrapConfiguration = new ValidationXmlParser(this.externalClassLoader).parseValidationXml();
/*     */     }
/* 395 */     return this.bootstrapConfiguration;
/*     */   }
/*     */   
/*     */   public ParameterNameProvider getParameterNameProvider()
/*     */   {
/* 400 */     return this.validationBootstrapParameters.getParameterNameProvider();
/*     */   }
/*     */   
/*     */   public List<ValidatedValueUnwrapper<?>> getValidatedValueHandlers() {
/* 404 */     return this.validatedValueHandlers;
/*     */   }
/*     */   
/*     */   public TimeProvider getTimeProvider() {
/* 408 */     return this.timeProvider;
/*     */   }
/*     */   
/*     */   public final Map<String, String> getProperties()
/*     */   {
/* 413 */     return this.validationBootstrapParameters.getConfigProperties();
/*     */   }
/*     */   
/*     */   public ClassLoader getExternalClassLoader() {
/* 417 */     return this.externalClassLoader;
/*     */   }
/*     */   
/*     */   public final MessageInterpolator getDefaultMessageInterpolator()
/*     */   {
/* 422 */     if (this.defaultMessageInterpolator == null) {
/* 423 */       this.defaultMessageInterpolator = new ResourceBundleMessageInterpolator(this.defaultResourceBundleLocator);
/*     */     }
/*     */     
/* 426 */     return this.defaultMessageInterpolator;
/*     */   }
/*     */   
/*     */   public final TraversableResolver getDefaultTraversableResolver()
/*     */   {
/* 431 */     return this.defaultTraversableResolver;
/*     */   }
/*     */   
/*     */   public final ConstraintValidatorFactory getDefaultConstraintValidatorFactory()
/*     */   {
/* 436 */     return this.defaultConstraintValidatorFactory;
/*     */   }
/*     */   
/*     */   public final ResourceBundleLocator getDefaultResourceBundleLocator()
/*     */   {
/* 441 */     return this.defaultResourceBundleLocator;
/*     */   }
/*     */   
/*     */   public ParameterNameProvider getDefaultParameterNameProvider()
/*     */   {
/* 446 */     return this.defaultParameterNameProvider;
/*     */   }
/*     */   
/*     */   public final Set<DefaultConstraintMapping> getProgrammaticMappings() {
/* 450 */     return this.programmaticMappings;
/*     */   }
/*     */   
/*     */   private boolean isSpecificProvider() {
/* 454 */     return this.validationBootstrapParameters.getProvider() != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void parseValidationXml()
/*     */   {
/* 461 */     if (this.ignoreXmlConfiguration) {
/* 462 */       log.ignoringXmlConfiguration();
/*     */       
/* 464 */       if (this.validationBootstrapParameters.getTraversableResolver() == null) {
/* 465 */         this.validationBootstrapParameters.setTraversableResolver(this.defaultTraversableResolver);
/*     */       }
/* 467 */       if (this.validationBootstrapParameters.getConstraintValidatorFactory() == null) {
/* 468 */         this.validationBootstrapParameters.setConstraintValidatorFactory(this.defaultConstraintValidatorFactory);
/*     */       }
/* 470 */       if (this.validationBootstrapParameters.getParameterNameProvider() == null) {
/* 471 */         this.validationBootstrapParameters.setParameterNameProvider(this.defaultParameterNameProvider);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 476 */       ValidationBootstrapParameters xmlParameters = new ValidationBootstrapParameters(getBootstrapConfiguration(), this.externalClassLoader);
/*     */       
/* 478 */       applyXmlSettings(xmlParameters);
/*     */     }
/*     */   }
/*     */   
/*     */   private void applyXmlSettings(ValidationBootstrapParameters xmlParameters) {
/* 483 */     this.validationBootstrapParameters.setProviderClass(xmlParameters.getProviderClass());
/*     */     
/* 485 */     if ((this.validationBootstrapParameters.getMessageInterpolator() == null) && 
/* 486 */       (xmlParameters.getMessageInterpolator() != null)) {
/* 487 */       this.validationBootstrapParameters.setMessageInterpolator(xmlParameters.getMessageInterpolator());
/*     */     }
/*     */     
/*     */ 
/* 491 */     if (this.validationBootstrapParameters.getTraversableResolver() == null) {
/* 492 */       if (xmlParameters.getTraversableResolver() != null) {
/* 493 */         this.validationBootstrapParameters.setTraversableResolver(xmlParameters.getTraversableResolver());
/*     */       }
/*     */       else {
/* 496 */         this.validationBootstrapParameters.setTraversableResolver(this.defaultTraversableResolver);
/*     */       }
/*     */     }
/*     */     
/* 500 */     if (this.validationBootstrapParameters.getConstraintValidatorFactory() == null) {
/* 501 */       if (xmlParameters.getConstraintValidatorFactory() != null) {
/* 502 */         this.validationBootstrapParameters.setConstraintValidatorFactory(xmlParameters
/* 503 */           .getConstraintValidatorFactory());
/*     */       }
/*     */       else
/*     */       {
/* 507 */         this.validationBootstrapParameters.setConstraintValidatorFactory(this.defaultConstraintValidatorFactory);
/*     */       }
/*     */     }
/*     */     
/* 511 */     if (this.validationBootstrapParameters.getParameterNameProvider() == null) {
/* 512 */       if (xmlParameters.getParameterNameProvider() != null) {
/* 513 */         this.validationBootstrapParameters.setParameterNameProvider(xmlParameters.getParameterNameProvider());
/*     */       }
/*     */       else {
/* 516 */         this.validationBootstrapParameters.setParameterNameProvider(this.defaultParameterNameProvider);
/*     */       }
/*     */     }
/*     */     
/* 520 */     this.validationBootstrapParameters.addAllMappings(xmlParameters.getMappings());
/* 521 */     this.configurationStreams.addAll(xmlParameters.getMappings());
/*     */     
/* 523 */     for (Map.Entry<String, String> entry : xmlParameters.getConfigProperties().entrySet()) {
/* 524 */       if (this.validationBootstrapParameters.getConfigProperties().get(entry.getKey()) == null) {
/* 525 */         this.validationBootstrapParameters.addConfigProperty((String)entry.getKey(), (String)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isJavaFxInClasspath() {
/* 531 */     return isClassPresent("javafx.application.Application", false);
/*     */   }
/*     */   
/*     */   private boolean isClassPresent(String className, boolean fallbackOnTCCL) {
/*     */     try {
/* 536 */       run(LoadClass.action(className, getClass().getClassLoader(), fallbackOnTCCL));
/* 537 */       return true;
/*     */     }
/*     */     catch (ValidationException e) {}
/* 540 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MessageInterpolator getDefaultMessageInterpolatorConfiguredWithClassLoader()
/*     */   {
/* 548 */     if (this.externalClassLoader != null) {
/* 549 */       PlatformResourceBundleLocator userResourceBundleLocator = new PlatformResourceBundleLocator("ValidationMessages", this.externalClassLoader);
/*     */       
/*     */ 
/*     */ 
/* 553 */       PlatformResourceBundleLocator contributorResourceBundleLocator = new PlatformResourceBundleLocator("ContributorValidationMessages", this.externalClassLoader, true);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 561 */       ClassLoader originalContextClassLoader = (ClassLoader)run(GetClassLoader.fromContext());
/*     */       try
/*     */       {
/* 564 */         run(SetContextClassLoader.action(this.externalClassLoader));
/* 565 */         return new ResourceBundleMessageInterpolator(userResourceBundleLocator, contributorResourceBundleLocator);
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 571 */         run(SetContextClassLoader.action(originalContextClassLoader));
/*     */       }
/*     */     }
/*     */     
/* 575 */     return getDefaultMessageInterpolator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 586 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ConfigurationImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */