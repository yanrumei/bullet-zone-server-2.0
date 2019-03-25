/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.util.xml.DomUtils;
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
/*    */ public class FreeMarkerConfigurerBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcFreeMarkerConfigurer";
/*    */   
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 44 */     return "org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer";
/*    */   }
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*    */   {
/* 49 */     return "mvcFreeMarkerConfigurer";
/*    */   }
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*    */   {
/* 54 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "template-loader-path");
/* 55 */     if (!childElements.isEmpty()) {
/* 56 */       List<String> locations = new ArrayList(childElements.size());
/* 57 */       for (Element childElement : childElements) {
/* 58 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 60 */       if (locations.isEmpty()) {
/* 61 */         locations.add("/WEB-INF/");
/*    */       }
/* 63 */       builder.addPropertyValue("templateLoaderPaths", locations.toArray(new String[locations.size()]));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\FreeMarkerConfigurerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */