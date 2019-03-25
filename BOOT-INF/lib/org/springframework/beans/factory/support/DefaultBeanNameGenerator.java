/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
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
/*    */ public class DefaultBeanNameGenerator
/*    */   implements BeanNameGenerator
/*    */ {
/*    */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
/*    */   {
/* 32 */     return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\DefaultBeanNameGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */