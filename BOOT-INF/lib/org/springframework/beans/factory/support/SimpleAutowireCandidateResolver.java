/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.DependencyDescriptor;
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
/*    */ public class SimpleAutowireCandidateResolver
/*    */   implements AutowireCandidateResolver
/*    */ {
/*    */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor)
/*    */   {
/* 34 */     return bdHolder.getBeanDefinition().isAutowireCandidate();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isRequired(DependencyDescriptor descriptor)
/*    */   {
/* 47 */     return descriptor.isRequired();
/*    */   }
/*    */   
/*    */   public Object getSuggestedValue(DependencyDescriptor descriptor)
/*    */   {
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName)
/*    */   {
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\SimpleAutowireCandidateResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */