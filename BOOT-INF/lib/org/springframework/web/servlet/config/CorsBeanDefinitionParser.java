/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.util.xml.DomUtils;
/*    */ import org.springframework.web.cors.CorsConfiguration;
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
/*    */ public class CorsBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*    */   {
/* 47 */     Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap();
/* 48 */     List<Element> mappings = DomUtils.getChildElementsByTagName(element, "mapping");
/*    */     CorsConfiguration config;
/* 50 */     if (mappings.isEmpty()) {
/* 51 */       config = new CorsConfiguration().applyPermitDefaultValues();
/* 52 */       corsConfigurations.put("/**", config);
/*    */     }
/*    */     else {
/* 55 */       for (Element mapping : mappings) {
/* 56 */         CorsConfiguration config = new CorsConfiguration();
/* 57 */         if (mapping.hasAttribute("allowed-origins")) {
/* 58 */           String[] allowedOrigins = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-origins"), ",");
/* 59 */           config.setAllowedOrigins(Arrays.asList(allowedOrigins));
/*    */         }
/* 61 */         if (mapping.hasAttribute("allowed-methods")) {
/* 62 */           String[] allowedMethods = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-methods"), ",");
/* 63 */           config.setAllowedMethods(Arrays.asList(allowedMethods));
/*    */         }
/* 65 */         if (mapping.hasAttribute("allowed-headers")) {
/* 66 */           String[] allowedHeaders = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-headers"), ",");
/* 67 */           config.setAllowedHeaders(Arrays.asList(allowedHeaders));
/*    */         }
/* 69 */         if (mapping.hasAttribute("exposed-headers")) {
/* 70 */           String[] exposedHeaders = StringUtils.tokenizeToStringArray(mapping.getAttribute("exposed-headers"), ",");
/* 71 */           config.setExposedHeaders(Arrays.asList(exposedHeaders));
/*    */         }
/* 73 */         if (mapping.hasAttribute("allow-credentials")) {
/* 74 */           config.setAllowCredentials(Boolean.valueOf(Boolean.parseBoolean(mapping.getAttribute("allow-credentials"))));
/*    */         }
/* 76 */         if (mapping.hasAttribute("max-age")) {
/* 77 */           config.setMaxAge(Long.valueOf(Long.parseLong(mapping.getAttribute("max-age"))));
/*    */         }
/* 79 */         corsConfigurations.put(mapping.getAttribute("path"), config.applyPermitDefaultValues());
/*    */       }
/*    */     }
/*    */     
/* 83 */     MvcNamespaceUtils.registerCorsConfigurations(corsConfigurations, parserContext, parserContext.extractSource(element));
/* 84 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\CorsBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */