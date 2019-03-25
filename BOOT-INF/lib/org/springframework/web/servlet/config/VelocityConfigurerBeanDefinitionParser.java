/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
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
/*    */ public class VelocityConfigurerBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcVelocityConfigurer";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*    */   {
/* 40 */     return "mvcVelocityConfigurer";
/*    */   }
/*    */   
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 45 */     return "org.springframework.web.servlet.view.velocity.VelocityConfigurer";
/*    */   }
/*    */   
/*    */   protected boolean isEligibleAttribute(String attributeName)
/*    */   {
/* 50 */     return attributeName.equals("resource-loader-path");
/*    */   }
/*    */   
/*    */   protected void postProcess(BeanDefinitionBuilder builder, Element element)
/*    */   {
/* 55 */     if (!builder.getBeanDefinition().hasAttribute("resourceLoaderPath")) {
/* 56 */       builder.getBeanDefinition().setAttribute("resourceLoaderPath", "/WEB-INF/");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\VelocityConfigurerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */