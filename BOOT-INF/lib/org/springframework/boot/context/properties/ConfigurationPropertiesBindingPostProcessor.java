/*     */ package org.springframework.boot.context.properties;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.boot.bind.PropertiesConfigurationFactory;
/*     */ import org.springframework.boot.validation.MessageInterpolatorFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationPropertiesBindingPostProcessor
/*     */   implements BeanPostProcessor, BeanFactoryAware, EnvironmentAware, ApplicationContextAware, InitializingBean, DisposableBean, ApplicationListener<ContextRefreshedEvent>, PriorityOrdered
/*     */ {
/*     */   public static final String VALIDATOR_BEAN_NAME = "configurationPropertiesValidator";
/*  86 */   private static final String[] VALIDATOR_CLASSES = { "javax.validation.Validator", "javax.validation.ValidatorFactory", "javax.validation.bootstrap.GenericBootstrap" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private static final Log logger = LogFactory.getLog(ConfigurationPropertiesBindingPostProcessor.class);
/*     */   
/*  93 */   private ConfigurationBeanFactoryMetaData beans = new ConfigurationBeanFactoryMetaData();
/*     */   
/*     */   private PropertySources propertySources;
/*     */   
/*     */   private Validator validator;
/*     */   
/*     */   private volatile Validator localValidator;
/*     */   
/*     */   private ConversionService conversionService;
/*     */   
/*     */   private DefaultConversionService defaultConversionService;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/* 107 */   private Environment environment = new StandardEnvironment();
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */   
/* 111 */   private List<Converter<?, ?>> converters = Collections.emptyList();
/*     */   
/* 113 */   private List<GenericConverter> genericConverters = Collections.emptyList();
/*     */   
/* 115 */   private int order = -2147483647;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Autowired(required=false)
/*     */   @ConfigurationPropertiesBinding
/*     */   public void setConverters(List<Converter<?, ?>> converters)
/*     */   {
/* 125 */     this.converters = converters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Autowired(required=false)
/*     */   @ConfigurationPropertiesBinding
/*     */   public void setGenericConverters(List<GenericConverter> converters)
/*     */   {
/* 136 */     this.genericConverters = converters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 144 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 153 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertySources(PropertySources propertySources)
/*     */   {
/* 161 */     this.propertySources = propertySources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidator(Validator validator)
/*     */   {
/* 169 */     this.validator = validator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConversionService(ConversionService conversionService)
/*     */   {
/* 177 */     this.conversionService = conversionService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanMetaDataStore(ConfigurationBeanFactoryMetaData beans)
/*     */   {
/* 185 */     this.beans = beans;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 190 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 195 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 200 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/* 205 */     if (this.propertySources == null) {
/* 206 */       this.propertySources = deducePropertySources();
/*     */     }
/* 208 */     if (this.validator == null) {
/* 209 */       this.validator = ((Validator)getOptionalBean("configurationPropertiesValidator", Validator.class));
/*     */     }
/* 211 */     if (this.conversionService == null) {
/* 212 */       this.conversionService = ((ConversionService)getOptionalBean("conversionService", ConversionService.class));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onApplicationEvent(ContextRefreshedEvent event)
/*     */   {
/* 220 */     freeLocalValidator();
/*     */   }
/*     */   
/*     */   public void destroy() throws Exception
/*     */   {
/* 225 */     freeLocalValidator();
/*     */   }
/*     */   
/*     */   private void freeLocalValidator() {
/*     */     try {
/* 230 */       Validator validator = this.localValidator;
/* 231 */       this.localValidator = null;
/* 232 */       if (validator != null) {
/* 233 */         ((DisposableBean)validator).destroy();
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 237 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private PropertySources deducePropertySources() {
/* 242 */     PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer();
/* 243 */     if (configurer != null)
/*     */     {
/* 245 */       return new FlatPropertySources(configurer.getAppliedPropertySources());
/*     */     }
/* 247 */     if ((this.environment instanceof ConfigurableEnvironment))
/*     */     {
/* 249 */       MutablePropertySources propertySources = ((ConfigurableEnvironment)this.environment).getPropertySources();
/* 250 */       return new FlatPropertySources(propertySources);
/*     */     }
/*     */     
/* 253 */     logger.warn("Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment");
/*     */     
/* 255 */     return new MutablePropertySources();
/*     */   }
/*     */   
/*     */   private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer()
/*     */   {
/* 260 */     if ((this.beanFactory instanceof ListableBeanFactory)) {
/* 261 */       ListableBeanFactory listableBeanFactory = (ListableBeanFactory)this.beanFactory;
/*     */       
/* 263 */       Map<String, PropertySourcesPlaceholderConfigurer> beans = listableBeanFactory.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
/*     */       
/* 265 */       if (beans.size() == 1) {
/* 266 */         return (PropertySourcesPlaceholderConfigurer)beans.values().iterator().next();
/*     */       }
/* 268 */       if ((beans.size() > 1) && (logger.isWarnEnabled())) {
/* 269 */         logger.warn("Multiple PropertySourcesPlaceholderConfigurer beans registered " + beans
/* 270 */           .keySet() + ", falling back to Environment");
/*     */       }
/*     */     }
/*     */     
/* 274 */     return null;
/*     */   }
/*     */   
/*     */   private <T> T getOptionalBean(String name, Class<T> type) {
/*     */     try {
/* 279 */       return (T)this.beanFactory.getBean(name, type);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/* 282 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/* 290 */     ConfigurationProperties annotation = (ConfigurationProperties)AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);
/* 291 */     if (annotation != null) {
/* 292 */       postProcessBeforeInitialization(bean, beanName, annotation);
/*     */     }
/* 294 */     annotation = (ConfigurationProperties)this.beans.findFactoryAnnotation(beanName, ConfigurationProperties.class);
/*     */     
/* 296 */     if (annotation != null) {
/* 297 */       postProcessBeforeInitialization(bean, beanName, annotation);
/*     */     }
/* 299 */     return bean;
/*     */   }
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/* 305 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */   private void postProcessBeforeInitialization(Object bean, String beanName, ConfigurationProperties annotation)
/*     */   {
/* 311 */     Object target = bean;
/* 312 */     PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory(target);
/*     */     
/* 314 */     factory.setPropertySources(this.propertySources);
/* 315 */     factory.setValidator(determineValidator(bean));
/*     */     
/*     */ 
/* 318 */     factory.setConversionService(this.conversionService == null ? 
/* 319 */       getDefaultConversionService() : this.conversionService);
/* 320 */     if (annotation != null) {
/* 321 */       factory.setIgnoreInvalidFields(annotation.ignoreInvalidFields());
/* 322 */       factory.setIgnoreUnknownFields(annotation.ignoreUnknownFields());
/* 323 */       factory.setExceptionIfInvalid(annotation.exceptionIfInvalid());
/* 324 */       factory.setIgnoreNestedProperties(annotation.ignoreNestedProperties());
/* 325 */       if (StringUtils.hasLength(annotation.prefix())) {
/* 326 */         factory.setTargetName(annotation.prefix());
/*     */       }
/*     */     }
/*     */     try {
/* 330 */       factory.bindPropertiesToTarget();
/*     */     }
/*     */     catch (Exception ex) {
/* 333 */       String targetClass = ClassUtils.getShortName(target.getClass());
/*     */       
/* 335 */       throw new BeanCreationException(beanName, "Could not bind properties to " + targetClass + " (" + getAnnotationDetails(annotation) + ")", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getAnnotationDetails(ConfigurationProperties annotation) {
/* 340 */     if (annotation == null) {
/* 341 */       return "";
/*     */     }
/* 343 */     StringBuilder details = new StringBuilder();
/* 344 */     details.append("prefix=").append(annotation.prefix());
/* 345 */     details.append(", ignoreInvalidFields=").append(annotation.ignoreInvalidFields());
/* 346 */     details.append(", ignoreUnknownFields=").append(annotation.ignoreUnknownFields());
/* 347 */     details.append(", ignoreNestedProperties=")
/* 348 */       .append(annotation.ignoreNestedProperties());
/* 349 */     return details.toString();
/*     */   }
/*     */   
/*     */   private Validator determineValidator(Object bean) {
/* 353 */     Validator validator = getValidator();
/* 354 */     boolean supportsBean = (validator != null) && (validator.supports(bean.getClass()));
/* 355 */     if (ClassUtils.isAssignable(Validator.class, bean.getClass())) {
/* 356 */       if (supportsBean) {
/* 357 */         return new ChainingValidator(new Validator[] { validator, (Validator)bean });
/*     */       }
/* 359 */       return (Validator)bean;
/*     */     }
/* 361 */     return supportsBean ? validator : null;
/*     */   }
/*     */   
/*     */   private Validator getValidator() {
/* 365 */     if (this.validator != null) {
/* 366 */       return this.validator;
/*     */     }
/* 368 */     if ((this.localValidator == null) && (isJsr303Present())) {
/* 369 */       this.localValidator = new ValidatedLocalValidatorFactoryBean(this.applicationContext);
/*     */     }
/*     */     
/* 372 */     return this.localValidator;
/*     */   }
/*     */   
/*     */   private boolean isJsr303Present() {
/* 376 */     for (String validatorClass : VALIDATOR_CLASSES) {
/* 377 */       if (!ClassUtils.isPresent(validatorClass, this.applicationContext
/* 378 */         .getClassLoader())) {
/* 379 */         return false;
/*     */       }
/*     */     }
/* 382 */     return true;
/*     */   }
/*     */   
/*     */   private ConversionService getDefaultConversionService() {
/* 386 */     if (this.defaultConversionService == null) {
/* 387 */       DefaultConversionService conversionService = new DefaultConversionService();
/* 388 */       this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
/* 389 */       for (Converter<?, ?> converter : this.converters) {
/* 390 */         conversionService.addConverter(converter);
/*     */       }
/* 392 */       for (GenericConverter genericConverter : this.genericConverters) {
/* 393 */         conversionService.addConverter(genericConverter);
/*     */       }
/* 395 */       this.defaultConversionService = conversionService;
/*     */     }
/* 397 */     return this.defaultConversionService;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ValidatedLocalValidatorFactoryBean
/*     */     extends LocalValidatorFactoryBean
/*     */   {
/* 408 */     private static final Log logger = LogFactory.getLog(ConfigurationPropertiesBindingPostProcessor.class);
/*     */     
/*     */     ValidatedLocalValidatorFactoryBean(ApplicationContext applicationContext) {
/* 411 */       setApplicationContext(applicationContext);
/* 412 */       setMessageInterpolator(new MessageInterpolatorFactory().getObject());
/* 413 */       afterPropertiesSet();
/*     */     }
/*     */     
/*     */     public boolean supports(Class<?> type)
/*     */     {
/* 418 */       if (!super.supports(type)) {
/* 419 */         return false;
/*     */       }
/* 421 */       if (AnnotatedElementUtils.hasAnnotation(type, Validated.class)) {
/* 422 */         return true;
/*     */       }
/* 424 */       if ((type.getPackage() != null) && 
/* 425 */         (type.getPackage().getName().startsWith("org.springframework.boot"))) {
/* 426 */         return false;
/*     */       }
/* 428 */       if (getConstraintsForClass(type).isBeanConstrained()) {
/* 429 */         logger.warn("The @ConfigurationProperties bean " + type + " contains validation constraints but had not been annotated with @Validated.");
/*     */       }
/*     */       
/*     */ 
/* 433 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ChainingValidator
/*     */     implements Validator
/*     */   {
/*     */     private Validator[] validators;
/*     */     
/*     */ 
/*     */     ChainingValidator(Validator... validators)
/*     */     {
/* 447 */       Assert.notNull(validators, "Validators must not be null");
/* 448 */       this.validators = validators;
/*     */     }
/*     */     
/*     */     public boolean supports(Class<?> clazz)
/*     */     {
/* 453 */       for (Validator validator : this.validators) {
/* 454 */         if (validator.supports(clazz)) {
/* 455 */           return true;
/*     */         }
/*     */       }
/* 458 */       return false;
/*     */     }
/*     */     
/*     */     public void validate(Object target, Errors errors)
/*     */     {
/* 463 */       for (Validator validator : this.validators) {
/* 464 */         if (validator.supports(target.getClass())) {
/* 465 */           validator.validate(target, errors);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class FlatPropertySources
/*     */     implements PropertySources
/*     */   {
/*     */     private PropertySources propertySources;
/*     */     
/*     */ 
/*     */     FlatPropertySources(PropertySources propertySources)
/*     */     {
/* 481 */       this.propertySources = propertySources;
/*     */     }
/*     */     
/*     */     public Iterator<PropertySource<?>> iterator()
/*     */     {
/* 486 */       MutablePropertySources result = getFlattened();
/* 487 */       return result.iterator();
/*     */     }
/*     */     
/*     */     public boolean contains(String name)
/*     */     {
/* 492 */       return get(name) != null;
/*     */     }
/*     */     
/*     */     public PropertySource<?> get(String name)
/*     */     {
/* 497 */       return getFlattened().get(name);
/*     */     }
/*     */     
/*     */     private MutablePropertySources getFlattened() {
/* 501 */       MutablePropertySources result = new MutablePropertySources();
/* 502 */       for (PropertySource<?> propertySource : this.propertySources) {
/* 503 */         flattenPropertySources(propertySource, result);
/*     */       }
/* 505 */       return result;
/*     */     }
/*     */     
/*     */     private void flattenPropertySources(PropertySource<?> propertySource, MutablePropertySources result)
/*     */     {
/* 510 */       Object source = propertySource.getSource();
/* 511 */       if ((source instanceof ConfigurableEnvironment)) {
/* 512 */         ConfigurableEnvironment environment = (ConfigurableEnvironment)source;
/* 513 */         for (PropertySource<?> childSource : environment.getPropertySources()) {
/* 514 */           flattenPropertySources(childSource, result);
/*     */         }
/*     */       }
/*     */       else {
/* 518 */         result.addLast(propertySource);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\properties\ConfigurationPropertiesBindingPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */