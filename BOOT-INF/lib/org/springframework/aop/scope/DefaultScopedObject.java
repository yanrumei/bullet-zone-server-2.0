/*    */ package org.springframework.aop.scope;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultScopedObject
/*    */   implements ScopedObject, Serializable
/*    */ {
/*    */   private final ConfigurableBeanFactory beanFactory;
/*    */   private final String targetBeanName;
/*    */   
/*    */   public DefaultScopedObject(ConfigurableBeanFactory beanFactory, String targetBeanName)
/*    */   {
/* 51 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 52 */     Assert.hasText(targetBeanName, "'targetBeanName' must not be empty");
/* 53 */     this.beanFactory = beanFactory;
/* 54 */     this.targetBeanName = targetBeanName;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getTargetObject()
/*    */   {
/* 60 */     return this.beanFactory.getBean(this.targetBeanName);
/*    */   }
/*    */   
/*    */   public void removeFromScope()
/*    */   {
/* 65 */     this.beanFactory.destroyScopedBean(this.targetBeanName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\scope\DefaultScopedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */