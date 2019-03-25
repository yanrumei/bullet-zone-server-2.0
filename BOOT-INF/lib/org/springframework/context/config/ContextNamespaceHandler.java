/*    */ package org.springframework.context.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*    */ import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
/*    */ import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
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
/*    */ public class ContextNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init()
/*    */   {
/* 35 */     registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
/* 36 */     registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
/* 37 */     registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
/* 38 */     registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
/* 39 */     registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
/* 40 */     registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
/* 41 */     registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
/* 42 */     registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\config\ContextNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */