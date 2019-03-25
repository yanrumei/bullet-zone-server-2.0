/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.w3c.dom.Element;
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
/*    */ class SpringConfiguredBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_CONFIGURER_ASPECT_BEAN_NAME = "org.springframework.context.config.internalBeanConfigurerAspect";
/*    */   private static final String BEAN_CONFIGURER_ASPECT_CLASS_NAME = "org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect";
/*    */   
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*    */   {
/* 55 */     if (!parserContext.getRegistry().containsBeanDefinition("org.springframework.context.config.internalBeanConfigurerAspect")) {
/* 56 */       RootBeanDefinition def = new RootBeanDefinition();
/* 57 */       def.setBeanClassName("org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect");
/* 58 */       def.setFactoryMethodName("aspectOf");
/* 59 */       def.setRole(2);
/* 60 */       def.setSource(parserContext.extractSource(element));
/* 61 */       parserContext.registerBeanComponent(new BeanComponentDefinition(def, "org.springframework.context.config.internalBeanConfigurerAspect"));
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\config\SpringConfiguredBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */