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
/*    */ 
/*    */ public class TilesConfigurerBeanDefinitionParser
/*    */   extends AbstractSingleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcTilesConfigurer";
/*    */   
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 45 */     return "org.springframework.web.servlet.view.tiles3.TilesConfigurer";
/*    */   }
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*    */   {
/* 50 */     return "mvcTilesConfigurer";
/*    */   }
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*    */   {
/* 55 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "definitions");
/* 56 */     if (!childElements.isEmpty()) {
/* 57 */       List<String> locations = new ArrayList(childElements.size());
/* 58 */       for (Element childElement : childElements) {
/* 59 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 61 */       builder.addPropertyValue("definitions", locations.toArray(new String[locations.size()]));
/*    */     }
/* 63 */     if (element.hasAttribute("check-refresh")) {
/* 64 */       builder.addPropertyValue("checkRefresh", element.getAttribute("check-refresh"));
/*    */     }
/* 66 */     if (element.hasAttribute("validate-definitions")) {
/* 67 */       builder.addPropertyValue("validateDefinitions", element.getAttribute("validate-definitions"));
/*    */     }
/* 69 */     if (element.hasAttribute("definitions-factory")) {
/* 70 */       builder.addPropertyValue("definitionsFactoryClass", element.getAttribute("definitions-factory"));
/*    */     }
/* 72 */     if (element.hasAttribute("preparer-factory")) {
/* 73 */       builder.addPropertyValue("preparerFactoryClass", element.getAttribute("preparer-factory"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\TilesConfigurerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */