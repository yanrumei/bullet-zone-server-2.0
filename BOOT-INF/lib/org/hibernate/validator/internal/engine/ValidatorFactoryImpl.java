/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.spi.ConfigurationState;
/*     */ import org.hibernate.validator.HibernateValidatorContext;
/*     */ import org.hibernate.validator.HibernateValidatorFactory;
/*     */ import org.hibernate.validator.cfg.ConstraintMapping;
/*     */ import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
/*     */ import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;
/*     */ import org.hibernate.validator.internal.engine.time.DefaultTimeProvider;
/*     */ import org.hibernate.validator.internal.metadata.BeanMetaDataManager;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
/*     */ import org.hibernate.validator.internal.metadata.provider.ProgrammaticMetaDataProvider;
/*     */ import org.hibernate.validator.internal.metadata.provider.XmlMetaDataProvider;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.StringHelper;
/*     */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
/*     */ import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;
/*     */ import org.hibernate.validator.spi.cfg.ConstraintMappingContributor.ConstraintMappingBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidatorFactoryImpl
/*     */   implements HibernateValidatorFactory
/*     */ {
/*  64 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final MessageInterpolator messageInterpolator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TraversableResolver traversableResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ParameterNameProvider parameterNameProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TimeProvider timeProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintValidatorManager constraintValidatorManager;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<DefaultConstraintMapping> constraintMappings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintHelper constraintHelper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TypeResolutionHelper typeResolutionHelper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ExecutableHelper executableHelper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean failFast;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final MethodValidationConfiguration methodValidationConfiguration;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private XmlMetaDataProvider xmlMetaDataProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<ParameterNameProvider, BeanMetaDataManager> beanMetaDataManagerMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValidatorFactoryImpl(ConfigurationState configurationState)
/*     */   {
/* 142 */     ClassLoader externalClassLoader = getExternalClassLoader(configurationState);
/*     */     
/* 144 */     this.messageInterpolator = configurationState.getMessageInterpolator();
/* 145 */     this.traversableResolver = configurationState.getTraversableResolver();
/* 146 */     this.parameterNameProvider = configurationState.getParameterNameProvider();
/* 147 */     this.timeProvider = getTimeProvider(configurationState, externalClassLoader);
/* 148 */     this.beanMetaDataManagerMap = Collections.synchronizedMap(new IdentityHashMap());
/* 149 */     this.constraintHelper = new ConstraintHelper();
/* 150 */     this.typeResolutionHelper = new TypeResolutionHelper();
/* 151 */     this.executableHelper = new ExecutableHelper(this.typeResolutionHelper);
/*     */     
/*     */ 
/*     */ 
/* 155 */     if (configurationState.getMappingStreams().isEmpty()) {
/* 156 */       this.xmlMetaDataProvider = null;
/*     */     }
/*     */     else
/*     */     {
/* 160 */       this.xmlMetaDataProvider = new XmlMetaDataProvider(this.constraintHelper, this.parameterNameProvider, configurationState.getMappingStreams(), externalClassLoader);
/*     */     }
/*     */     
/*     */ 
/* 164 */     this.constraintMappings = Collections.unmodifiableSet(
/* 165 */       getConstraintMappings(configurationState, externalClassLoader));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */     Map<String, String> properties = configurationState.getProperties();
/*     */     
/* 173 */     boolean tmpFailFast = false;
/* 174 */     boolean tmpAllowOverridingMethodAlterParameterConstraint = false;
/* 175 */     boolean tmpAllowMultipleCascadedValidationOnReturnValues = false;
/* 176 */     boolean tmpAllowParallelMethodsDefineParameterConstraints = false;
/*     */     
/* 178 */     List<ValidatedValueUnwrapper<?>> tmpValidatedValueHandlers = CollectionHelper.newArrayList(5);
/*     */     
/* 180 */     if ((configurationState instanceof ConfigurationImpl)) {
/* 181 */       ConfigurationImpl hibernateSpecificConfig = (ConfigurationImpl)configurationState;
/*     */       
/*     */ 
/* 184 */       tmpFailFast = hibernateSpecificConfig.getFailFast();
/*     */       
/*     */ 
/*     */ 
/* 188 */       tmpAllowOverridingMethodAlterParameterConstraint = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowOverridingMethodAlterParameterConstraint();
/*     */       
/*     */ 
/* 191 */       tmpAllowMultipleCascadedValidationOnReturnValues = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowMultipleCascadedValidationOnReturnValues();
/*     */       
/*     */ 
/* 194 */       tmpAllowParallelMethodsDefineParameterConstraints = hibernateSpecificConfig.getMethodValidationConfiguration().isAllowParallelMethodsDefineParameterConstraints();
/*     */       
/* 196 */       tmpValidatedValueHandlers.addAll(hibernateSpecificConfig.getValidatedValueHandlers());
/*     */     }
/*     */     
/* 199 */     registerCustomConstraintValidators(this.constraintMappings, this.constraintHelper);
/*     */     
/* 201 */     tmpValidatedValueHandlers.addAll(
/* 202 */       getPropertyConfiguredValidatedValueHandlers(properties, externalClassLoader));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 207 */     this.validatedValueHandlers = Collections.unmodifiableList(tmpValidatedValueHandlers);
/*     */     
/* 209 */     tmpFailFast = checkPropertiesForBoolean(properties, "hibernate.validator.fail_fast", tmpFailFast);
/* 210 */     this.failFast = tmpFailFast;
/*     */     
/* 212 */     this.methodValidationConfiguration = new MethodValidationConfiguration();
/*     */     
/* 214 */     tmpAllowOverridingMethodAlterParameterConstraint = checkPropertiesForBoolean(properties, "hibernate.validator.allow_parameter_constraint_override", tmpAllowOverridingMethodAlterParameterConstraint);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 219 */     this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(tmpAllowOverridingMethodAlterParameterConstraint);
/*     */     
/*     */ 
/*     */ 
/* 223 */     tmpAllowMultipleCascadedValidationOnReturnValues = checkPropertiesForBoolean(properties, "hibernate.validator.allow_multiple_cascaded_validation_on_result", tmpAllowMultipleCascadedValidationOnReturnValues);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 228 */     this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(tmpAllowMultipleCascadedValidationOnReturnValues);
/*     */     
/*     */ 
/*     */ 
/* 232 */     tmpAllowParallelMethodsDefineParameterConstraints = checkPropertiesForBoolean(properties, "hibernate.validator.allow_parallel_method_parameter_constraint", tmpAllowParallelMethodsDefineParameterConstraints);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(tmpAllowParallelMethodsDefineParameterConstraints);
/*     */     
/*     */ 
/*     */ 
/* 241 */     this.constraintValidatorManager = new ConstraintValidatorManager(configurationState.getConstraintValidatorFactory());
/*     */   }
/*     */   
/*     */   private static ClassLoader getExternalClassLoader(ConfigurationState configurationState) {
/* 245 */     return (configurationState instanceof ConfigurationImpl) ? ((ConfigurationImpl)configurationState).getExternalClassLoader() : null;
/*     */   }
/*     */   
/*     */   private static Set<DefaultConstraintMapping> getConstraintMappings(ConfigurationState configurationState, ClassLoader externalClassLoader) {
/* 249 */     Set<DefaultConstraintMapping> constraintMappings = CollectionHelper.newHashSet();
/*     */     ConstraintMappingContributor serviceLoaderBasedContributor;
/* 251 */     if ((configurationState instanceof ConfigurationImpl)) {
/* 252 */       ConfigurationImpl hibernateConfiguration = (ConfigurationImpl)configurationState;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */       constraintMappings.addAll(hibernateConfiguration.getProgrammaticMappings());
/*     */       
/*     */ 
/*     */ 
/* 262 */       serviceLoaderBasedContributor = hibernateConfiguration.getServiceLoaderBasedConstraintMappingContributor();
/* 263 */       DefaultConstraintMappingBuilder builder = new DefaultConstraintMappingBuilder(constraintMappings);
/* 264 */       serviceLoaderBasedContributor.createConstraintMappings(builder);
/*     */     }
/*     */     
/*     */ 
/* 268 */     List<ConstraintMappingContributor> contributors = getPropertyConfiguredConstraintMappingContributors(configurationState.getProperties(), externalClassLoader);
/*     */     
/*     */ 
/* 271 */     for (ConstraintMappingContributor contributor : contributors) {
/* 272 */       DefaultConstraintMappingBuilder builder = new DefaultConstraintMappingBuilder(constraintMappings);
/* 273 */       contributor.createConstraintMappings(builder);
/*     */     }
/*     */     
/* 276 */     return constraintMappings;
/*     */   }
/*     */   
/*     */   private static TimeProvider getTimeProvider(ConfigurationState configurationState, ClassLoader externalClassLoader) {
/* 280 */     TimeProvider timeProvider = null;
/*     */     
/*     */ 
/* 283 */     if ((configurationState instanceof ConfigurationImpl)) {
/* 284 */       ConfigurationImpl hvConfig = (ConfigurationImpl)configurationState;
/* 285 */       timeProvider = hvConfig.getTimeProvider();
/*     */     }
/*     */     
/*     */ 
/* 289 */     if (timeProvider == null)
/*     */     {
/* 291 */       String timeProviderClassName = (String)configurationState.getProperties().get("hibernate.validator.time_provider");
/*     */       
/* 293 */       if (timeProviderClassName != null)
/*     */       {
/* 295 */         Class<? extends TimeProvider> handlerType = (Class)run(
/*     */         
/* 297 */           LoadClass.action(timeProviderClassName, externalClassLoader));
/*     */         
/* 299 */         timeProvider = (TimeProvider)run(NewInstance.action(handlerType, "time provider class"));
/*     */       }
/*     */     }
/*     */     
/* 303 */     return timeProvider != null ? timeProvider : DefaultTimeProvider.getInstance();
/*     */   }
/*     */   
/*     */   public Validator getValidator()
/*     */   {
/* 308 */     return createValidator(this.constraintValidatorManager
/* 309 */       .getDefaultConstraintValidatorFactory(), this.messageInterpolator, this.traversableResolver, this.parameterNameProvider, this.failFast, this.validatedValueHandlers, this.timeProvider, this.methodValidationConfiguration);
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
/*     */   public MessageInterpolator getMessageInterpolator()
/*     */   {
/* 322 */     return this.messageInterpolator;
/*     */   }
/*     */   
/*     */   public TraversableResolver getTraversableResolver()
/*     */   {
/* 327 */     return this.traversableResolver;
/*     */   }
/*     */   
/*     */   public ConstraintValidatorFactory getConstraintValidatorFactory()
/*     */   {
/* 332 */     return this.constraintValidatorManager.getDefaultConstraintValidatorFactory();
/*     */   }
/*     */   
/*     */   public ParameterNameProvider getParameterNameProvider()
/*     */   {
/* 337 */     return this.parameterNameProvider;
/*     */   }
/*     */   
/*     */   public boolean isFailFast() {
/* 341 */     return this.failFast;
/*     */   }
/*     */   
/*     */   public List<ValidatedValueUnwrapper<?>> getValidatedValueHandlers() {
/* 345 */     return this.validatedValueHandlers;
/*     */   }
/*     */   
/*     */   TimeProvider getTimeProvider() {
/* 349 */     return this.timeProvider;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T unwrap(Class<T> type)
/*     */   {
/* 355 */     if (type.isAssignableFrom(HibernateValidatorFactory.class)) {
/* 356 */       return (T)type.cast(this);
/*     */     }
/* 358 */     throw log.getTypeNotSupportedForUnwrappingException(type);
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext usingContext()
/*     */   {
/* 363 */     return new ValidatorContextImpl(this);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 368 */     this.constraintValidatorManager.clear();
/* 369 */     for (BeanMetaDataManager beanMetaDataManager : this.beanMetaDataManagerMap.values()) {
/* 370 */       beanMetaDataManager.clear();
/*     */     }
/*     */     
/*     */ 
/* 374 */     this.xmlMetaDataProvider = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Validator createValidator(ConstraintValidatorFactory constraintValidatorFactory, MessageInterpolator messageInterpolator, TraversableResolver traversableResolver, ParameterNameProvider parameterNameProvider, boolean failFast, List<ValidatedValueUnwrapper<?>> validatedValueHandlers, TimeProvider timeProvider, MethodValidationConfiguration methodValidationConfiguration)
/*     */   {
/*     */     BeanMetaDataManager beanMetaDataManager;
/*     */     
/*     */ 
/*     */ 
/* 387 */     if (!this.beanMetaDataManagerMap.containsKey(parameterNameProvider))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 392 */       BeanMetaDataManager beanMetaDataManager = new BeanMetaDataManager(this.constraintHelper, this.executableHelper, parameterNameProvider, buildDataProviders(parameterNameProvider), methodValidationConfiguration);
/*     */       
/*     */ 
/* 395 */       this.beanMetaDataManagerMap.put(parameterNameProvider, beanMetaDataManager);
/*     */     }
/*     */     else {
/* 398 */       beanMetaDataManager = (BeanMetaDataManager)this.beanMetaDataManagerMap.get(parameterNameProvider);
/*     */     }
/*     */     
/* 401 */     return new ValidatorImpl(constraintValidatorFactory, messageInterpolator, traversableResolver, beanMetaDataManager, parameterNameProvider, timeProvider, this.typeResolutionHelper, validatedValueHandlers, this.constraintValidatorManager, failFast);
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
/*     */   private List<MetaDataProvider> buildDataProviders(ParameterNameProvider parameterNameProvider)
/*     */   {
/* 416 */     List<MetaDataProvider> metaDataProviders = CollectionHelper.newArrayList();
/* 417 */     if (this.xmlMetaDataProvider != null) {
/* 418 */       metaDataProviders.add(this.xmlMetaDataProvider);
/*     */     }
/*     */     
/* 421 */     if (!this.constraintMappings.isEmpty()) {
/* 422 */       metaDataProviders.add(new ProgrammaticMetaDataProvider(this.constraintHelper, parameterNameProvider, this.constraintMappings));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */     return metaDataProviders;
/*     */   }
/*     */   
/*     */   private boolean checkPropertiesForBoolean(Map<String, String> properties, String propertyKey, boolean programmaticValue) {
/* 434 */     boolean value = programmaticValue;
/* 435 */     String propertyStringValue = (String)properties.get(propertyKey);
/* 436 */     if (propertyStringValue != null) {
/* 437 */       boolean configurationValue = Boolean.valueOf(propertyStringValue).booleanValue();
/*     */       
/* 439 */       if ((programmaticValue) && (!configurationValue)) {
/* 440 */         throw log.getInconsistentFailFastConfigurationException();
/*     */       }
/* 442 */       value = configurationValue;
/*     */     }
/* 444 */     return value;
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
/*     */   private static List<ValidatedValueUnwrapper<?>> getPropertyConfiguredValidatedValueHandlers(Map<String, String> properties, ClassLoader externalClassLoader)
/*     */   {
/* 457 */     String propertyValue = (String)properties.get("hibernate.validator.validated_value_handlers");
/*     */     
/* 459 */     if ((propertyValue == null) || (propertyValue.isEmpty())) {
/* 460 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 463 */     String[] handlerNames = propertyValue.split(",");
/* 464 */     List<ValidatedValueUnwrapper<?>> handlers = CollectionHelper.newArrayList(handlerNames.length);
/*     */     
/* 466 */     for (String handlerName : handlerNames)
/*     */     {
/*     */ 
/* 469 */       Class<? extends ValidatedValueUnwrapper<?>> handlerType = (Class)run(LoadClass.action(handlerName, externalClassLoader));
/* 470 */       handlers.add(run(NewInstance.action(handlerType, "validated value handler class")));
/*     */     }
/*     */     
/* 473 */     return handlers;
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
/*     */   private static List<ConstraintMappingContributor> getPropertyConfiguredConstraintMappingContributors(Map<String, String> properties, ClassLoader externalClassLoader)
/*     */   {
/* 489 */     String deprecatedPropertyValue = (String)properties.get("hibernate.validator.constraint_mapping_contributor");
/* 490 */     String propertyValue = (String)properties.get("hibernate.validator.constraint_mapping_contributors");
/*     */     
/* 492 */     if ((StringHelper.isNullOrEmptyString(deprecatedPropertyValue)) && (StringHelper.isNullOrEmptyString(propertyValue))) {
/* 493 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 496 */     StringBuilder assembledPropertyValue = new StringBuilder();
/* 497 */     if (!StringHelper.isNullOrEmptyString(deprecatedPropertyValue)) {
/* 498 */       assembledPropertyValue.append(deprecatedPropertyValue);
/*     */     }
/* 500 */     if (!StringHelper.isNullOrEmptyString(propertyValue)) {
/* 501 */       if (assembledPropertyValue.length() > 0) {
/* 502 */         assembledPropertyValue.append(",");
/*     */       }
/* 504 */       assembledPropertyValue.append(propertyValue);
/*     */     }
/*     */     
/* 507 */     String[] contributorNames = assembledPropertyValue.toString().split(",");
/* 508 */     List<ConstraintMappingContributor> contributors = CollectionHelper.newArrayList(contributorNames.length);
/*     */     
/* 510 */     for (String contributorName : contributorNames)
/*     */     {
/* 512 */       Class<? extends ConstraintMappingContributor> contributorType = (Class)run(
/* 513 */         LoadClass.action(contributorName, externalClassLoader));
/* 514 */       contributors.add(run(NewInstance.action(contributorType, "constraint mapping contributor class")));
/*     */     }
/*     */     
/* 517 */     return contributors;
/*     */   }
/*     */   
/*     */   private static void registerCustomConstraintValidators(Set<DefaultConstraintMapping> constraintMappings, ConstraintHelper constraintHelper)
/*     */   {
/* 522 */     Set<Class<?>> definedConstraints = CollectionHelper.newHashSet();
/* 523 */     for (DefaultConstraintMapping constraintMapping : constraintMappings) {
/* 524 */       for (ConstraintDefinitionContribution<?> contribution : constraintMapping.getConstraintDefinitionContributions()) {
/* 525 */         processConstraintDefinitionContribution(contribution, constraintHelper, definedConstraints);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static <A extends Annotation> void processConstraintDefinitionContribution(ConstraintDefinitionContribution<A> constraintDefinitionContribution, ConstraintHelper constraintHelper, Set<Class<?>> definedConstraints)
/*     */   {
/* 533 */     Class<A> constraintType = constraintDefinitionContribution.getConstraintType();
/* 534 */     if (definedConstraints.contains(constraintType)) {
/* 535 */       throw log.getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(constraintType.getName());
/*     */     }
/* 537 */     definedConstraints.add(constraintType);
/* 538 */     constraintHelper.putValidatorClasses(constraintType, constraintDefinitionContribution
/*     */     
/* 540 */       .getConstraintValidators(), constraintDefinitionContribution
/* 541 */       .includeExisting());
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
/* 552 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */ 
/*     */   private static class DefaultConstraintMappingBuilder
/*     */     implements ConstraintMappingContributor.ConstraintMappingBuilder
/*     */   {
/*     */     private final Set<DefaultConstraintMapping> mappings;
/*     */     
/*     */ 
/*     */     public DefaultConstraintMappingBuilder(Set<DefaultConstraintMapping> mappings)
/*     */     {
/* 564 */       this.mappings = mappings;
/*     */     }
/*     */     
/*     */     public ConstraintMapping addConstraintMapping()
/*     */     {
/* 569 */       DefaultConstraintMapping mapping = new DefaultConstraintMapping();
/* 570 */       this.mappings.add(mapping);
/* 571 */       return mapping;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ValidatorFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */