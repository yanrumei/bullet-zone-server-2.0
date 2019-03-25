/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintViolation;
/*     */ import javax.validation.ConstraintViolationException;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.hibernate.validator.HibernateValidator;
/*     */ import org.hibernate.validator.HibernateValidatorConfiguration;
/*     */ import org.hibernate.validator.method.MethodConstraintViolation;
/*     */ import org.hibernate.validator.method.MethodConstraintViolationException;
/*     */ import org.hibernate.validator.method.MethodValidator;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodValidationInterceptor
/*     */   implements MethodInterceptor
/*     */ {
/*     */   private static Method forExecutablesMethod;
/*     */   private static Method validateParametersMethod;
/*     */   private static Method validateReturnValueMethod;
/*     */   private volatile Validator validator;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  71 */       forExecutablesMethod = Validator.class.getMethod("forExecutables", new Class[0]);
/*  72 */       Class<?> executableValidatorClass = forExecutablesMethod.getReturnType();
/*  73 */       validateParametersMethod = executableValidatorClass.getMethod("validateParameters", new Class[] { Object.class, Method.class, Object[].class, Class[].class });
/*     */       
/*  75 */       validateReturnValueMethod = executableValidatorClass.getMethod("validateReturnValue", new Class[] { Object.class, Method.class, Object.class, Class[].class });
/*     */     }
/*     */     catch (Exception localException) {}
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
/*     */   public MethodValidationInterceptor()
/*     */   {
/*  91 */     this(forExecutablesMethod != null ? Validation.buildDefaultValidatorFactory() : 
/*  92 */       HibernateValidatorDelegate.buildValidatorFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodValidationInterceptor(ValidatorFactory validatorFactory)
/*     */   {
/* 100 */     this(validatorFactory.getValidator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodValidationInterceptor(Validator validator)
/*     */   {
/* 108 */     this.validator = validator;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object invoke(MethodInvocation invocation)
/*     */     throws Throwable
/*     */   {
/* 115 */     Class<?>[] groups = determineValidationGroups(invocation);
/*     */     
/* 117 */     if (forExecutablesMethod != null)
/*     */     {
/*     */       Object execVal;
/*     */       try {
/* 121 */         execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, this.validator);
/*     */       }
/*     */       catch (AbstractMethodError err) {
/*     */         Object execVal;
/* 125 */         Validator nativeValidator = (Validator)this.validator.unwrap(Validator.class);
/* 126 */         execVal = ReflectionUtils.invokeMethod(forExecutablesMethod, nativeValidator);
/*     */         
/* 128 */         this.validator = nativeValidator;
/*     */       }
/*     */       
/* 131 */       Method methodToValidate = invocation.getMethod();
/*     */       
/*     */       try
/*     */       {
/* 135 */         result = (Set)ReflectionUtils.invokeMethod(validateParametersMethod, execVal, new Object[] {invocation
/* 136 */           .getThis(), methodToValidate, invocation.getArguments(), groups });
/*     */       }
/*     */       catch (IllegalArgumentException ex)
/*     */       {
/*     */         Set<ConstraintViolation<?>> result;
/* 141 */         methodToValidate = BridgeMethodResolver.findBridgedMethod(
/* 142 */           ClassUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass()));
/* 143 */         result = (Set)ReflectionUtils.invokeMethod(validateParametersMethod, execVal, new Object[] {invocation
/* 144 */           .getThis(), methodToValidate, invocation.getArguments(), groups });
/*     */       }
/* 146 */       if (!result.isEmpty()) {
/* 147 */         throw new ConstraintViolationException(result);
/*     */       }
/*     */       
/* 150 */       Object returnValue = invocation.proceed();
/* 151 */       Set<ConstraintViolation<?>> result = (Set)ReflectionUtils.invokeMethod(validateReturnValueMethod, execVal, new Object[] {invocation
/* 152 */         .getThis(), methodToValidate, returnValue, groups });
/* 153 */       if (!result.isEmpty()) {
/* 154 */         throw new ConstraintViolationException(result);
/*     */       }
/* 156 */       return returnValue;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 161 */     return HibernateValidatorDelegate.invokeWithinValidation(invocation, this.validator, groups);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?>[] determineValidationGroups(MethodInvocation invocation)
/*     */   {
/* 173 */     Validated validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
/* 174 */     if (validatedAnn == null) {
/* 175 */       validatedAnn = (Validated)AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
/*     */     }
/* 177 */     return validatedAnn != null ? validatedAnn.value() : new Class[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class HibernateValidatorDelegate
/*     */   {
/*     */     public static ValidatorFactory buildValidatorFactory()
/*     */     {
/* 187 */       return ((HibernateValidatorConfiguration)Validation.byProvider(HibernateValidator.class).configure()).buildValidatorFactory();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static Object invokeWithinValidation(MethodInvocation invocation, Validator validator, Class<?>[] groups)
/*     */       throws Throwable
/*     */     {
/* 195 */       MethodValidator methodValidator = (MethodValidator)validator.unwrap(MethodValidator.class);
/*     */       
/* 197 */       Set<MethodConstraintViolation<Object>> result = methodValidator.validateAllParameters(invocation
/* 198 */         .getThis(), invocation.getMethod(), invocation.getArguments(), groups);
/* 199 */       if (!result.isEmpty()) {
/* 200 */         throw new MethodConstraintViolationException(result);
/*     */       }
/* 202 */       Object returnValue = invocation.proceed();
/* 203 */       result = methodValidator.validateReturnValue(invocation
/* 204 */         .getThis(), invocation.getMethod(), returnValue, groups);
/* 205 */       if (!result.isEmpty()) {
/* 206 */         throw new MethodConstraintViolationException(result);
/*     */       }
/* 208 */       return returnValue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\beanvalidation\MethodValidationInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */