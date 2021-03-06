/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import org.springframework.beans.factory.NamedBean;
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
/*    */ public class NamedBeanHolder<T>
/*    */   implements NamedBean
/*    */ {
/*    */   private final String beanName;
/*    */   private final T beanInstance;
/*    */   
/*    */   public NamedBeanHolder(String beanName, T beanInstance)
/*    */   {
/* 42 */     Assert.notNull(beanName, "Bean name must not be null");
/* 43 */     this.beanName = beanName;
/* 44 */     this.beanInstance = beanInstance;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getBeanName()
/*    */   {
/* 53 */     return this.beanName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public T getBeanInstance()
/*    */   {
/* 60 */     return (T)this.beanInstance;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\NamedBeanHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */