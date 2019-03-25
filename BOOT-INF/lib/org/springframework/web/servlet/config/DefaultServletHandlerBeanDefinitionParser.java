/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.beans.factory.support.ManagedMap;
/*    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*    */ import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
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
/*    */ class DefaultServletHandlerBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*    */   {
/* 48 */     Object source = parserContext.extractSource(element);
/*    */     
/* 50 */     String defaultServletName = element.getAttribute("default-servlet-name");
/* 51 */     RootBeanDefinition defaultServletHandlerDef = new RootBeanDefinition(DefaultServletHttpRequestHandler.class);
/* 52 */     defaultServletHandlerDef.setSource(source);
/* 53 */     defaultServletHandlerDef.setRole(2);
/* 54 */     if (StringUtils.hasText(defaultServletName)) {
/* 55 */       defaultServletHandlerDef.getPropertyValues().add("defaultServletName", defaultServletName);
/*    */     }
/* 57 */     String defaultServletHandlerName = parserContext.getReaderContext().generateBeanName(defaultServletHandlerDef);
/* 58 */     parserContext.getRegistry().registerBeanDefinition(defaultServletHandlerName, defaultServletHandlerDef);
/* 59 */     parserContext.registerComponent(new BeanComponentDefinition(defaultServletHandlerDef, defaultServletHandlerName));
/*    */     
/* 61 */     Map<String, String> urlMap = new ManagedMap();
/* 62 */     urlMap.put("/**", defaultServletHandlerName);
/*    */     
/* 64 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 65 */     handlerMappingDef.setSource(source);
/* 66 */     handlerMappingDef.setRole(2);
/* 67 */     handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
/*    */     
/* 69 */     String handlerMappingBeanName = parserContext.getReaderContext().generateBeanName(handlerMappingDef);
/* 70 */     parserContext.getRegistry().registerBeanDefinition(handlerMappingBeanName, handlerMappingDef);
/* 71 */     parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, handlerMappingBeanName));
/*    */     
/*    */ 
/* 74 */     MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
/*    */     
/* 76 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\DefaultServletHandlerBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */