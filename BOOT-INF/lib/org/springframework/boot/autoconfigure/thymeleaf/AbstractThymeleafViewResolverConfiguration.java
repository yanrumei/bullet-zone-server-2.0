/*    */ package org.springframework.boot.autoconfigure.thymeleaf;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.LinkedHashMap;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.util.MimeType;
/*    */ import org.thymeleaf.spring4.SpringTemplateEngine;
/*    */ import org.thymeleaf.spring4.view.ThymeleafViewResolver;
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
/*    */ abstract class AbstractThymeleafViewResolverConfiguration
/*    */ {
/*    */   private final ThymeleafProperties properties;
/*    */   private final SpringTemplateEngine templateEngine;
/*    */   
/*    */   protected AbstractThymeleafViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine)
/*    */   {
/* 43 */     this.properties = properties;
/* 44 */     this.templateEngine = templateEngine;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean(name={"thymeleafViewResolver"})
/*    */   @ConditionalOnProperty(name={"spring.thymeleaf.enabled"}, matchIfMissing=true)
/*    */   public ThymeleafViewResolver thymeleafViewResolver() {
/* 51 */     ThymeleafViewResolver resolver = new ThymeleafViewResolver();
/* 52 */     configureTemplateEngine(resolver, this.templateEngine);
/* 53 */     resolver.setCharacterEncoding(this.properties.getEncoding().name());
/* 54 */     resolver.setContentType(appendCharset(this.properties.getContentType(), resolver
/* 55 */       .getCharacterEncoding()));
/* 56 */     resolver.setExcludedViewNames(this.properties.getExcludedViewNames());
/* 57 */     resolver.setViewNames(this.properties.getViewNames());
/*    */     
/*    */ 
/* 60 */     resolver.setOrder(2147483642);
/* 61 */     resolver.setCache(this.properties.isCache());
/* 62 */     return resolver;
/*    */   }
/*    */   
/*    */   protected abstract void configureTemplateEngine(ThymeleafViewResolver paramThymeleafViewResolver, SpringTemplateEngine paramSpringTemplateEngine);
/*    */   
/*    */   private String appendCharset(MimeType type, String charset)
/*    */   {
/* 69 */     if (type.getCharset() != null) {
/* 70 */       return type.toString();
/*    */     }
/* 72 */     LinkedHashMap<String, String> parameters = new LinkedHashMap();
/* 73 */     parameters.put("charset", charset);
/* 74 */     parameters.putAll(type.getParameters());
/* 75 */     return new MimeType(type, parameters).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\thymeleaf\AbstractThymeleafViewResolverConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */