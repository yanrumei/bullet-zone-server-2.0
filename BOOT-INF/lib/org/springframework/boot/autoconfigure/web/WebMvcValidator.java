/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.boot.validation.MessageInterpolatorFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.SmartValidator;
/*     */ import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
/*     */ import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class WebMvcValidator
/*     */   implements SmartValidator, ApplicationContextAware, InitializingBean, DisposableBean
/*     */ {
/*     */   private final SpringValidatorAdapter target;
/*     */   private final boolean existingBean;
/*     */   
/*     */   WebMvcValidator(SpringValidatorAdapter target, boolean existingBean)
/*     */   {
/*  49 */     this.target = target;
/*  50 */     this.existingBean = existingBean;
/*     */   }
/*     */   
/*     */   SpringValidatorAdapter getTarget() {
/*  54 */     return this.target;
/*     */   }
/*     */   
/*     */   public boolean supports(Class<?> clazz)
/*     */   {
/*  59 */     return this.target.supports(clazz);
/*     */   }
/*     */   
/*     */   public void validate(Object target, Errors errors)
/*     */   {
/*  64 */     this.target.validate(target, errors);
/*     */   }
/*     */   
/*     */   public void validate(Object target, Errors errors, Object... validationHints)
/*     */   {
/*  69 */     this.target.validate(target, errors, validationHints);
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */     throws BeansException
/*     */   {
/*  75 */     if ((!this.existingBean) && ((this.target instanceof ApplicationContextAware)))
/*     */     {
/*  77 */       ((ApplicationContextAware)this.target).setApplicationContext(applicationContext);
/*     */     }
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  83 */     if ((!this.existingBean) && ((this.target instanceof InitializingBean))) {
/*  84 */       ((InitializingBean)this.target).afterPropertiesSet();
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroy() throws Exception
/*     */   {
/*  90 */     if ((!this.existingBean) && ((this.target instanceof DisposableBean))) {
/*  91 */       ((DisposableBean)this.target).destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   public static org.springframework.validation.Validator get(ApplicationContext applicationContext, org.springframework.validation.Validator validator)
/*     */   {
/*  97 */     if (validator != null) {
/*  98 */       return wrap(validator, false);
/*     */     }
/* 100 */     return getExistingOrCreate(applicationContext);
/*     */   }
/*     */   
/*     */   private static org.springframework.validation.Validator getExistingOrCreate(ApplicationContext applicationContext) {
/* 104 */     org.springframework.validation.Validator existing = getExisting(applicationContext);
/* 105 */     if (existing != null) {
/* 106 */       return wrap(existing, true);
/*     */     }
/* 108 */     return create();
/*     */   }
/*     */   
/*     */   private static org.springframework.validation.Validator getExisting(ApplicationContext applicationContext)
/*     */   {
/*     */     try {
/* 114 */       javax.validation.Validator validator = (javax.validation.Validator)applicationContext.getBean(javax.validation.Validator.class);
/* 115 */       if ((validator instanceof org.springframework.validation.Validator)) {
/* 116 */         return (org.springframework.validation.Validator)validator;
/*     */       }
/* 118 */       return new SpringValidatorAdapter(validator);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {}
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   private static org.springframework.validation.Validator create()
/*     */   {
/* 126 */     OptionalValidatorFactoryBean validator = new OptionalValidatorFactoryBean();
/* 127 */     validator.setMessageInterpolator(new MessageInterpolatorFactory().getObject());
/* 128 */     return wrap(validator, false);
/*     */   }
/*     */   
/*     */   private static org.springframework.validation.Validator wrap(org.springframework.validation.Validator validator, boolean existingBean) {
/* 132 */     if ((validator instanceof javax.validation.Validator)) {
/* 133 */       if ((validator instanceof SpringValidatorAdapter)) {
/* 134 */         return new WebMvcValidator((SpringValidatorAdapter)validator, existingBean);
/*     */       }
/*     */       
/* 137 */       return new WebMvcValidator(new SpringValidatorAdapter((javax.validation.Validator)validator), existingBean);
/*     */     }
/*     */     
/*     */ 
/* 141 */     return validator;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebMvcValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */