/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.aop.config.AopConfigUtils;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ class AspectJAutoProxyRegistrar
/*    */   implements ImportBeanDefinitionRegistrar
/*    */ {
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*    */   {
/* 45 */     AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
/*    */     
/*    */ 
/* 48 */     AnnotationAttributes enableAspectJAutoProxy = AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
/* 49 */     if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
/* 50 */       AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/*    */     }
/* 52 */     if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
/* 53 */       AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\AspectJAutoProxyRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */