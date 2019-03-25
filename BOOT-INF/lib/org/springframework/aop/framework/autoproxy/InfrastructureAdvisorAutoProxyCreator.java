/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
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
/*    */ public class InfrastructureAdvisorAutoProxyCreator
/*    */   extends AbstractAdvisorAutoProxyCreator
/*    */ {
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*    */   {
/* 37 */     super.initBeanFactory(beanFactory);
/* 38 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */   protected boolean isEligibleAdvisorBean(String beanName)
/*    */   {
/* 43 */     return (this.beanFactory.containsBeanDefinition(beanName)) && 
/* 44 */       (this.beanFactory.getBeanDefinition(beanName).getRole() == 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\InfrastructureAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */