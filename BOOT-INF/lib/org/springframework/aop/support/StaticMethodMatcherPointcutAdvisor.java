/*    */ package org.springframework.aop.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.aop.Advice;
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.PointcutAdvisor;
/*    */ import org.springframework.core.Ordered;
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
/*    */ public abstract class StaticMethodMatcherPointcutAdvisor
/*    */   extends StaticMethodMatcherPointcut
/*    */   implements PointcutAdvisor, Ordered, Serializable
/*    */ {
/* 39 */   private int order = Integer.MAX_VALUE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private Advice advice;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public StaticMethodMatcherPointcutAdvisor() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public StaticMethodMatcherPointcutAdvisor(Advice advice)
/*    */   {
/* 57 */     Assert.notNull(advice, "Advice must not be null");
/* 58 */     this.advice = advice;
/*    */   }
/*    */   
/*    */   public void setOrder(int order)
/*    */   {
/* 63 */     this.order = order;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 68 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setAdvice(Advice advice) {
/* 72 */     this.advice = advice;
/*    */   }
/*    */   
/*    */   public Advice getAdvice()
/*    */   {
/* 77 */     return this.advice;
/*    */   }
/*    */   
/*    */   public boolean isPerInstance()
/*    */   {
/* 82 */     return true;
/*    */   }
/*    */   
/*    */   public Pointcut getPointcut()
/*    */   {
/* 87 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\support\StaticMethodMatcherPointcutAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */