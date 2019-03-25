/*    */ package org.springframework.scripting.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.scripting.support.ScriptFactoryPostProcessor;
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
/*    */ public abstract class LangNamespaceUtils
/*    */ {
/*    */   private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME = "org.springframework.scripting.config.scriptFactoryPostProcessor";
/*    */   
/*    */   public static BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry)
/*    */   {
/* 47 */     BeanDefinition beanDefinition = null;
/* 48 */     if (registry.containsBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor")) {
/* 49 */       beanDefinition = registry.getBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor");
/*    */     }
/*    */     else {
/* 52 */       beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
/* 53 */       registry.registerBeanDefinition("org.springframework.scripting.config.scriptFactoryPostProcessor", beanDefinition);
/*    */     }
/* 55 */     return beanDefinition;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scripting\config\LangNamespaceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */