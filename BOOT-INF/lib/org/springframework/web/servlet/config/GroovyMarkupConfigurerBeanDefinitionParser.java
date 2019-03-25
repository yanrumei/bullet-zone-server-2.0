/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
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
/*    */ public class GroovyMarkupConfigurerBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcGroovyMarkupConfigurer";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*    */   {
/* 39 */     return "mvcGroovyMarkupConfigurer";
/*    */   }
/*    */   
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 44 */     return "org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer";
/*    */   }
/*    */   
/*    */   protected boolean isEligibleAttribute(String name)
/*    */   {
/* 49 */     return (name.equals("auto-indent")) || (name.equals("cache-templates")) || (name.equals("resource-loader-path"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\GroovyMarkupConfigurerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */