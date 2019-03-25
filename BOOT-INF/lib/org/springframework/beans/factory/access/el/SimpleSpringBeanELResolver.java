/*    */ package org.springframework.beans.factory.access.el;
/*    */ 
/*    */ import javax.el.ELContext;
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class SimpleSpringBeanELResolver
/*    */   extends SpringBeanELResolver
/*    */ {
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public SimpleSpringBeanELResolver(BeanFactory beanFactory)
/*    */   {
/* 41 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 42 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */   protected BeanFactory getBeanFactory(ELContext elContext)
/*    */   {
/* 47 */     return this.beanFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\access\el\SimpleSpringBeanELResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */