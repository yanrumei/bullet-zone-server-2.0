/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
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
/*    */ public class MvcNamespaceHandler
/*    */   extends NamespaceHandlerSupport
/*    */ {
/*    */   public void init()
/*    */   {
/* 34 */     registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
/* 35 */     registerBeanDefinitionParser("default-servlet-handler", new DefaultServletHandlerBeanDefinitionParser());
/* 36 */     registerBeanDefinitionParser("interceptors", new InterceptorsBeanDefinitionParser());
/* 37 */     registerBeanDefinitionParser("resources", new ResourcesBeanDefinitionParser());
/* 38 */     registerBeanDefinitionParser("view-controller", new ViewControllerBeanDefinitionParser());
/* 39 */     registerBeanDefinitionParser("redirect-view-controller", new ViewControllerBeanDefinitionParser());
/* 40 */     registerBeanDefinitionParser("status-controller", new ViewControllerBeanDefinitionParser());
/* 41 */     registerBeanDefinitionParser("view-resolvers", new ViewResolversBeanDefinitionParser());
/* 42 */     registerBeanDefinitionParser("tiles-configurer", new TilesConfigurerBeanDefinitionParser());
/* 43 */     registerBeanDefinitionParser("freemarker-configurer", new FreeMarkerConfigurerBeanDefinitionParser());
/* 44 */     registerBeanDefinitionParser("velocity-configurer", new VelocityConfigurerBeanDefinitionParser());
/* 45 */     registerBeanDefinitionParser("groovy-configurer", new GroovyMarkupConfigurerBeanDefinitionParser());
/* 46 */     registerBeanDefinitionParser("script-template-configurer", new ScriptTemplateConfigurerBeanDefinitionParser());
/* 47 */     registerBeanDefinitionParser("cors", new CorsBeanDefinitionParser());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\MvcNamespaceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */