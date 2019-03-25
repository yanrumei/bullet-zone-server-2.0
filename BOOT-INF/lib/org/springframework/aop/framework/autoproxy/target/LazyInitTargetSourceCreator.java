/*    */ package org.springframework.aop.framework.autoproxy.target;
/*    */ 
/*    */ import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
/*    */ import org.springframework.aop.target.LazyInitTargetSource;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LazyInitTargetSourceCreator
/*    */   extends AbstractBeanFactoryBasedTargetSourceCreator
/*    */ {
/*    */   protected boolean isPrototypeBased()
/*    */   {
/* 58 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName)
/*    */   {
/* 65 */     if ((getBeanFactory() instanceof ConfigurableListableBeanFactory))
/*    */     {
/* 67 */       BeanDefinition definition = ((ConfigurableListableBeanFactory)getBeanFactory()).getBeanDefinition(beanName);
/* 68 */       if (definition.isLazyInit()) {
/* 69 */         return new LazyInitTargetSource();
/*    */       }
/*    */     }
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\target\LazyInitTargetSourceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */