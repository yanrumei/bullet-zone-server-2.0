/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.Validator;
/*     */ import org.hibernate.validator.HibernateValidatorContext;
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
/*     */ public class ValidatorContextImpl
/*     */   implements HibernateValidatorContext
/*     */ {
/*     */   private final ValidatorFactoryImpl validatorFactory;
/*     */   private MessageInterpolator messageInterpolator;
/*     */   private TraversableResolver traversableResolver;
/*     */   private ConstraintValidatorFactory constraintValidatorFactory;
/*     */   private ParameterNameProvider parameterNameProvider;
/*     */   private boolean failFast;
/*     */   private final List<ValidatedValueUnwrapper<?>> validatedValueHandlers;
/*     */   private TimeProvider timeProvider;
/*  40 */   private final MethodValidationConfiguration methodValidationConfiguration = new MethodValidationConfiguration();
/*     */   
/*     */   public ValidatorContextImpl(ValidatorFactoryImpl validatorFactory)
/*     */   {
/*  44 */     this.validatorFactory = validatorFactory;
/*  45 */     this.messageInterpolator = validatorFactory.getMessageInterpolator();
/*  46 */     this.traversableResolver = validatorFactory.getTraversableResolver();
/*  47 */     this.constraintValidatorFactory = validatorFactory.getConstraintValidatorFactory();
/*  48 */     this.parameterNameProvider = validatorFactory.getParameterNameProvider();
/*  49 */     this.failFast = validatorFactory.isFailFast();
/*     */     
/*  51 */     this.validatedValueHandlers = new ArrayList(validatorFactory.getValidatedValueHandlers());
/*     */     
/*  53 */     this.timeProvider = validatorFactory.getTimeProvider();
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext messageInterpolator(MessageInterpolator messageInterpolator)
/*     */   {
/*  58 */     if (messageInterpolator == null) {
/*  59 */       this.messageInterpolator = this.validatorFactory.getMessageInterpolator();
/*     */     }
/*     */     else {
/*  62 */       this.messageInterpolator = messageInterpolator;
/*     */     }
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext traversableResolver(TraversableResolver traversableResolver)
/*     */   {
/*  69 */     if (traversableResolver == null) {
/*  70 */       this.traversableResolver = this.validatorFactory.getTraversableResolver();
/*     */     }
/*     */     else {
/*  73 */       this.traversableResolver = traversableResolver;
/*     */     }
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext constraintValidatorFactory(ConstraintValidatorFactory factory)
/*     */   {
/*  80 */     if (factory == null) {
/*  81 */       this.constraintValidatorFactory = this.validatorFactory.getConstraintValidatorFactory();
/*     */     }
/*     */     else {
/*  84 */       this.constraintValidatorFactory = factory;
/*     */     }
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext parameterNameProvider(ParameterNameProvider parameterNameProvider)
/*     */   {
/*  91 */     if (parameterNameProvider == null) {
/*  92 */       this.parameterNameProvider = this.validatorFactory.getParameterNameProvider();
/*     */     }
/*     */     else {
/*  95 */       this.parameterNameProvider = parameterNameProvider;
/*     */     }
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext failFast(boolean failFast)
/*     */   {
/* 102 */     this.failFast = failFast;
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext addValidationValueHandler(ValidatedValueUnwrapper<?> handler)
/*     */   {
/* 108 */     this.validatedValueHandlers.add(handler);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext timeProvider(TimeProvider timeProvider)
/*     */   {
/* 114 */     if (timeProvider == null) {
/* 115 */       this.timeProvider = this.validatorFactory.getTimeProvider();
/*     */     }
/*     */     else {
/* 118 */       this.timeProvider = timeProvider;
/*     */     }
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext allowOverridingMethodAlterParameterConstraint(boolean allow)
/*     */   {
/* 125 */     this.methodValidationConfiguration.allowOverridingMethodAlterParameterConstraint(allow);
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext allowMultipleCascadedValidationOnReturnValues(boolean allow)
/*     */   {
/* 131 */     this.methodValidationConfiguration.allowMultipleCascadedValidationOnReturnValues(allow);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public HibernateValidatorContext allowParallelMethodsDefineParameterConstraints(boolean allow)
/*     */   {
/* 137 */     this.methodValidationConfiguration.allowParallelMethodsDefineParameterConstraints(allow);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public Validator getValidator()
/*     */   {
/* 143 */     return this.validatorFactory.createValidator(this.constraintValidatorFactory, this.messageInterpolator, this.traversableResolver, this.parameterNameProvider, this.failFast, this.validatedValueHandlers, this.timeProvider, this.methodValidationConfiguration);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ValidatorContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */