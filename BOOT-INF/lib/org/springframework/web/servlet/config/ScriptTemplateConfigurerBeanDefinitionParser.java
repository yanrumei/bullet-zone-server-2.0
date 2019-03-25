/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
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
/*    */ public class ScriptTemplateConfigurerBeanDefinitionParser
/*    */   extends AbstractSimpleBeanDefinitionParser
/*    */ {
/*    */   public static final String BEAN_NAME = "mvcScriptTemplateConfigurer";
/*    */   
/*    */   protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
/*    */   {
/* 45 */     return "mvcScriptTemplateConfigurer";
/*    */   }
/*    */   
/*    */   protected String getBeanClassName(Element element)
/*    */   {
/* 50 */     return "org.springframework.web.servlet.view.script.ScriptTemplateConfigurer";
/*    */   }
/*    */   
/*    */   protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
/*    */   {
/* 55 */     List<Element> childElements = DomUtils.getChildElementsByTagName(element, "script");
/* 56 */     if (!childElements.isEmpty()) {
/* 57 */       List<String> locations = new ArrayList(childElements.size());
/* 58 */       for (Element childElement : childElements) {
/* 59 */         locations.add(childElement.getAttribute("location"));
/*    */       }
/* 61 */       builder.addPropertyValue("scripts", locations.toArray(new String[locations.size()]));
/*    */     }
/* 63 */     builder.addPropertyValue("engineName", element.getAttribute("engine-name"));
/* 64 */     if (element.hasAttribute("render-object")) {
/* 65 */       builder.addPropertyValue("renderObject", element.getAttribute("render-object"));
/*    */     }
/* 67 */     if (element.hasAttribute("render-function")) {
/* 68 */       builder.addPropertyValue("renderFunction", element.getAttribute("render-function"));
/*    */     }
/* 70 */     if (element.hasAttribute("content-type")) {
/* 71 */       builder.addPropertyValue("contentType", element.getAttribute("content-type"));
/*    */     }
/* 73 */     if (element.hasAttribute("charset")) {
/* 74 */       builder.addPropertyValue("charset", Charset.forName(element.getAttribute("charset")));
/*    */     }
/* 76 */     if (element.hasAttribute("resource-loader-path")) {
/* 77 */       builder.addPropertyValue("resourceLoaderPath", element.getAttribute("resource-loader-path"));
/*    */     }
/* 79 */     if (element.hasAttribute("shared-engine")) {
/* 80 */       builder.addPropertyValue("sharedEngine", element.getAttribute("shared-engine"));
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean isEligibleAttribute(String name)
/*    */   {
/* 86 */     return (name.equals("engine-name")) || (name.equals("scripts")) || (name.equals("render-object")) || 
/* 87 */       (name.equals("render-function")) || (name.equals("content-type")) || 
/* 88 */       (name.equals("charset")) || (name.equals("resource-loader-path"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\ScriptTemplateConfigurerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */