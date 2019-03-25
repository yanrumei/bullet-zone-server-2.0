/*     */ package org.springframework.aop.framework.adapter;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.AfterAdvice;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrowsAdviceInterceptor
/*     */   implements MethodInterceptor, AfterAdvice
/*     */ {
/*     */   private static final String AFTER_THROWING = "afterThrowing";
/*  58 */   private static final Log logger = LogFactory.getLog(ThrowsAdviceInterceptor.class);
/*     */   
/*     */ 
/*     */   private final Object throwsAdvice;
/*     */   
/*     */ 
/*  64 */   private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThrowsAdviceInterceptor(Object throwsAdvice)
/*     */   {
/*  74 */     Assert.notNull(throwsAdvice, "Advice must not be null");
/*  75 */     this.throwsAdvice = throwsAdvice;
/*     */     
/*  77 */     Method[] methods = throwsAdvice.getClass().getMethods();
/*  78 */     for (Method method : methods) {
/*  79 */       if ((method.getName().equals("afterThrowing")) && 
/*  80 */         ((method.getParameterTypes().length == 1) || (method.getParameterTypes().length == 4)) && 
/*  81 */         (Throwable.class.isAssignableFrom(method.getParameterTypes()[(method.getParameterTypes().length - 1)])))
/*     */       {
/*     */ 
/*  84 */         this.exceptionHandlerMap.put(method.getParameterTypes()[(method.getParameterTypes().length - 1)], method);
/*  85 */         if (logger.isDebugEnabled()) {
/*  86 */           logger.debug("Found exception handler method: " + method);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  91 */     if (this.exceptionHandlerMap.isEmpty())
/*     */     {
/*  93 */       throw new IllegalArgumentException("At least one handler method must be found in class [" + throwsAdvice.getClass() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */   public int getHandlerMethodCount() {
/*  98 */     return this.exceptionHandlerMap.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method getExceptionHandler(Throwable exception)
/*     */   {
/* 107 */     Class<?> exceptionClass = exception.getClass();
/* 108 */     if (logger.isTraceEnabled()) {
/* 109 */       logger.trace("Trying to find handler for exception of type [" + exceptionClass.getName() + "]");
/*     */     }
/* 111 */     Method handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/* 112 */     while ((handler == null) && (exceptionClass != Throwable.class)) {
/* 113 */       exceptionClass = exceptionClass.getSuperclass();
/* 114 */       handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/*     */     }
/* 116 */     if ((handler != null) && (logger.isDebugEnabled())) {
/* 117 */       logger.debug("Found handler for exception of type [" + exceptionClass.getName() + "]: " + handler);
/*     */     }
/* 119 */     return handler;
/*     */   }
/*     */   
/*     */   public Object invoke(MethodInvocation mi) throws Throwable
/*     */   {
/*     */     try {
/* 125 */       return mi.proceed();
/*     */     }
/*     */     catch (Throwable ex) {
/* 128 */       Method handlerMethod = getExceptionHandler(ex);
/* 129 */       if (handlerMethod != null) {
/* 130 */         invokeHandlerMethod(mi, ex, handlerMethod);
/*     */       }
/* 132 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */   private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable { Object[] handlerArgs;
/*     */     Object[] handlerArgs;
/* 138 */     if (method.getParameterTypes().length == 1) {
/* 139 */       handlerArgs = new Object[] { ex };
/*     */     }
/*     */     else {
/* 142 */       handlerArgs = new Object[] { mi.getMethod(), mi.getArguments(), mi.getThis(), ex };
/*     */     }
/*     */     try {
/* 145 */       method.invoke(this.throwsAdvice, handlerArgs);
/*     */     }
/*     */     catch (InvocationTargetException targetEx) {
/* 148 */       throw targetEx.getTargetException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\adapter\ThrowsAdviceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */