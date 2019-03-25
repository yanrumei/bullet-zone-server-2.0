/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.AfterAdvice;
/*    */ import org.springframework.aop.AfterReturningAdvice;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AfterReturningAdviceInterceptor
/*    */   implements MethodInterceptor, AfterAdvice, Serializable
/*    */ {
/*    */   private final AfterReturningAdvice advice;
/*    */   
/*    */   public AfterReturningAdviceInterceptor(AfterReturningAdvice advice)
/*    */   {
/* 46 */     Assert.notNull(advice, "Advice must not be null");
/* 47 */     this.advice = advice;
/*    */   }
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable
/*    */   {
/* 52 */     Object retVal = mi.proceed();
/* 53 */     this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
/* 54 */     return retVal;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\adapter\AfterReturningAdviceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */