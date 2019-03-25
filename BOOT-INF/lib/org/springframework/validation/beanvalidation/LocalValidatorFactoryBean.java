/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.validation.Configuration;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.ValidationProviderResolver;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorContext;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.bootstrap.GenericBootstrap;
/*     */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*     */ import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalValidatorFactoryBean
/*     */   extends SpringValidatorAdapter
/*     */   implements ValidatorFactory, ApplicationContextAware, InitializingBean, DisposableBean
/*     */ {
/*  94 */   private static final Method closeMethod = ClassUtils.getMethodIfAvailable(ValidatorFactory.class, "close", new Class[0]);
/*     */   
/*     */ 
/*     */   private Class providerClass;
/*     */   
/*     */ 
/*     */   private ValidationProviderResolver validationProviderResolver;
/*     */   
/*     */   private MessageInterpolator messageInterpolator;
/*     */   
/*     */   private TraversableResolver traversableResolver;
/*     */   
/*     */   private ConstraintValidatorFactory constraintValidatorFactory;
/*     */   
/* 108 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*     */   
/*     */   private Resource[] mappingLocations;
/*     */   
/* 112 */   private final Map<String, String> validationPropertyMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */ 
/*     */ 
/*     */   private ValidatorFactory validatorFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProviderClass(Class providerClass)
/*     */   {
/* 127 */     this.providerClass = providerClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidationProviderResolver(ValidationProviderResolver validationProviderResolver)
/*     */   {
/* 136 */     this.validationProviderResolver = validationProviderResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageInterpolator(MessageInterpolator messageInterpolator)
/*     */   {
/* 144 */     this.messageInterpolator = messageInterpolator;
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
/*     */   public void setValidationMessageSource(MessageSource messageSource)
/*     */   {
/* 162 */     this.messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(messageSource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTraversableResolver(TraversableResolver traversableResolver)
/*     */   {
/* 170 */     this.traversableResolver = traversableResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory)
/*     */   {
/* 179 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 188 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMappingLocations(Resource... mappingLocations)
/*     */   {
/* 195 */     this.mappingLocations = mappingLocations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidationProperties(Properties jpaProperties)
/*     */   {
/* 205 */     CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.validationPropertyMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValidationPropertyMap(Map<String, String> validationProperties)
/*     */   {
/* 214 */     if (validationProperties != null) {
/* 215 */       this.validationPropertyMap.putAll(validationProperties);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getValidationPropertyMap()
/*     */   {
/* 225 */     return this.validationPropertyMap;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 230 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/*     */     Configuration<?> configuration;
/*     */     Configuration<?> configuration;
/* 238 */     if (this.providerClass != null) {
/* 239 */       ProviderSpecificBootstrap bootstrap = Validation.byProvider(this.providerClass);
/* 240 */       if (this.validationProviderResolver != null) {
/* 241 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 243 */       configuration = bootstrap.configure();
/*     */     }
/*     */     else {
/* 246 */       GenericBootstrap bootstrap = Validation.byDefaultProvider();
/* 247 */       if (this.validationProviderResolver != null) {
/* 248 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 250 */       configuration = bootstrap.configure();
/*     */     }
/*     */     
/*     */ 
/* 254 */     if (this.applicationContext != null) {
/*     */       try {
/* 256 */         Method eclMethod = configuration.getClass().getMethod("externalClassLoader", new Class[] { ClassLoader.class });
/* 257 */         ReflectionUtils.invokeMethod(eclMethod, configuration, new Object[] { this.applicationContext.getClassLoader() });
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 264 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 265 */     if (targetInterpolator == null) {
/* 266 */       targetInterpolator = configuration.getDefaultMessageInterpolator();
/*     */     }
/* 268 */     configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/*     */     
/* 270 */     if (this.traversableResolver != null) {
/* 271 */       configuration.traversableResolver(this.traversableResolver);
/*     */     }
/*     */     
/* 274 */     ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
/* 275 */     if ((targetConstraintValidatorFactory == null) && (this.applicationContext != null))
/*     */     {
/* 277 */       targetConstraintValidatorFactory = new SpringConstraintValidatorFactory(this.applicationContext.getAutowireCapableBeanFactory());
/*     */     }
/* 279 */     if (targetConstraintValidatorFactory != null) {
/* 280 */       configuration.constraintValidatorFactory(targetConstraintValidatorFactory);
/*     */     }
/*     */     
/* 283 */     if (this.parameterNameDiscoverer != null) {
/* 284 */       configureParameterNameProviderIfPossible(configuration);
/*     */     }
/*     */     
/* 287 */     if (this.mappingLocations != null) {
/* 288 */       for (Resource location : this.mappingLocations) {
/*     */         try {
/* 290 */           configuration.addMapping(location.getInputStream());
/*     */         }
/*     */         catch (IOException ex) {
/* 293 */           throw new IllegalStateException("Cannot read mapping resource: " + location);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 298 */     for (??? = this.validationPropertyMap.entrySet().iterator(); ((Iterator)???).hasNext();) { Object entry = (Map.Entry)((Iterator)???).next();
/* 299 */       configuration.addProperty((String)((Map.Entry)entry).getKey(), (String)((Map.Entry)entry).getValue());
/*     */     }
/*     */     
/*     */ 
/* 303 */     postProcessConfiguration(configuration);
/*     */     
/* 305 */     this.validatorFactory = configuration.buildValidatorFactory();
/* 306 */     setTargetValidator(this.validatorFactory.getValidator());
/*     */   }
/*     */   
/*     */   private void configureParameterNameProviderIfPossible(Configuration<?> configuration)
/*     */   {
/*     */     try {
/* 312 */       Class<?> parameterNameProviderClass = ClassUtils.forName("javax.validation.ParameterNameProvider", getClass().getClassLoader());
/*     */       
/* 314 */       Method parameterNameProviderMethod = Configuration.class.getMethod("parameterNameProvider", new Class[] { parameterNameProviderClass });
/* 315 */       final Object defaultProvider = ReflectionUtils.invokeMethod(Configuration.class
/* 316 */         .getMethod("getDefaultParameterNameProvider", new Class[0]), configuration);
/* 317 */       final ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 318 */       Object parameterNameProvider = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { parameterNameProviderClass }, new InvocationHandler()
/*     */       {
/*     */         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */         {
/* 322 */           if (method.getName().equals("getParameterNames")) {
/* 323 */             String[] result = null;
/* 324 */             if ((args[0] instanceof Constructor)) {
/* 325 */               result = discoverer.getParameterNames((Constructor)args[0]);
/*     */             }
/* 327 */             else if ((args[0] instanceof Method)) {
/* 328 */               result = discoverer.getParameterNames((Method)args[0]);
/*     */             }
/* 330 */             if (result != null) {
/* 331 */               return Arrays.asList(result);
/*     */             }
/*     */             try
/*     */             {
/* 335 */               return method.invoke(defaultProvider, args);
/*     */             }
/*     */             catch (InvocationTargetException ex) {
/* 338 */               throw ex.getTargetException();
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */           try
/*     */           {
/* 345 */             return method.invoke(this, args);
/*     */           }
/*     */           catch (InvocationTargetException ex) {
/* 348 */             throw ex.getTargetException();
/*     */           }
/*     */           
/*     */         }
/* 352 */       });
/* 353 */       ReflectionUtils.invokeMethod(parameterNameProviderMethod, configuration, new Object[] { parameterNameProvider });
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postProcessConfiguration(Configuration<?> configuration) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Validator getValidator()
/*     */   {
/* 374 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 375 */     return this.validatorFactory.getValidator();
/*     */   }
/*     */   
/*     */   public ValidatorContext usingContext()
/*     */   {
/* 380 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 381 */     return this.validatorFactory.usingContext();
/*     */   }
/*     */   
/*     */   public MessageInterpolator getMessageInterpolator()
/*     */   {
/* 386 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 387 */     return this.validatorFactory.getMessageInterpolator();
/*     */   }
/*     */   
/*     */   public TraversableResolver getTraversableResolver()
/*     */   {
/* 392 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 393 */     return this.validatorFactory.getTraversableResolver();
/*     */   }
/*     */   
/*     */   public ConstraintValidatorFactory getConstraintValidatorFactory()
/*     */   {
/* 398 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 399 */     return this.validatorFactory.getConstraintValidatorFactory();
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T unwrap(Class<T> type)
/*     */   {
/* 405 */     if ((type == null) || (!ValidatorFactory.class.isAssignableFrom(type))) {
/*     */       try {
/* 407 */         return (T)super.unwrap(type);
/*     */       }
/*     */       catch (ValidationException localValidationException1) {}
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 414 */       return (T)this.validatorFactory.unwrap(type);
/*     */     }
/*     */     catch (ValidationException ex)
/*     */     {
/* 418 */       if (ValidatorFactory.class == type) {
/* 419 */         return this.validatorFactory;
/*     */       }
/* 421 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 427 */     if ((closeMethod != null) && (this.validatorFactory != null)) {
/* 428 */       ReflectionUtils.invokeMethod(closeMethod, this.validatorFactory);
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */   {
/* 434 */     close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class HibernateValidatorDelegate
/*     */   {
/*     */     public static MessageInterpolator buildMessageInterpolator(MessageSource messageSource)
/*     */     {
/* 444 */       return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\LocalValidatorFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */