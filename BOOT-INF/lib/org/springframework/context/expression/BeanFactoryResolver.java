/*    */ package org.springframework.context.expression;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.BeanResolver;
/*    */ import org.springframework.expression.EvaluationContext;
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
/*    */ public class BeanFactoryResolver
/*    */   implements BeanResolver
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public BeanFactoryResolver(BeanFactory beanFactory)
/*    */   {
/* 38 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 39 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */   public Object resolve(EvaluationContext context, String beanName) throws AccessException
/*    */   {
/*    */     try {
/* 45 */       return this.beanFactory.getBean(beanName);
/*    */     }
/*    */     catch (BeansException ex) {
/* 48 */       throw new AccessException("Could not resolve bean reference against BeanFactory", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\expression\BeanFactoryResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */