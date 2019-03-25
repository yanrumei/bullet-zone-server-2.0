/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.MethodBeforeAdvice;
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
/*    */ public class MethodBeforeAdviceInterceptor
/*    */   implements MethodInterceptor, Serializable
/*    */ {
/*    */   private MethodBeforeAdvice advice;
/*    */   
/*    */   public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice)
/*    */   {
/* 45 */     Assert.notNull(advice, "Advice must not be null");
/* 46 */     this.advice = advice;
/*    */   }
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable
/*    */   {
/* 51 */     this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
/* 52 */     return mi.proceed();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\adapter\MethodBeforeAdviceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */