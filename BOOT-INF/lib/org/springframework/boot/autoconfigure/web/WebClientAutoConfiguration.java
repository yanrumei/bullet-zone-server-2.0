/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.web.client.RestTemplateBuilder;
/*    */ import org.springframework.boot.web.client.RestTemplateCustomizer;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.web.client.RestTemplate;
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
/*    */ @Configuration
/*    */ @AutoConfigureAfter({HttpMessageConvertersAutoConfiguration.class})
/*    */ public class WebClientAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnClass({RestTemplate.class})
/*    */   public static class RestTemplateConfiguration
/*    */   {
/*    */     private final ObjectProvider<HttpMessageConverters> messageConverters;
/*    */     private final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers;
/*    */     
/*    */     public RestTemplateConfiguration(ObjectProvider<HttpMessageConverters> messageConverters, ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers)
/*    */     {
/* 57 */       this.messageConverters = messageConverters;
/* 58 */       this.restTemplateCustomizers = restTemplateCustomizers;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/*    */     public RestTemplateBuilder restTemplateBuilder() {
/* 64 */       RestTemplateBuilder builder = new RestTemplateBuilder(new RestTemplateCustomizer[0]);
/* 65 */       HttpMessageConverters converters = (HttpMessageConverters)this.messageConverters.getIfUnique();
/* 66 */       if (converters != null) {
/* 67 */         builder = builder.messageConverters(converters.getConverters());
/*    */       }
/*    */       
/* 70 */       List<RestTemplateCustomizer> customizers = (List)this.restTemplateCustomizers.getIfAvailable();
/* 71 */       if (!CollectionUtils.isEmpty(customizers)) {
/* 72 */         customizers = new ArrayList(customizers);
/* 73 */         AnnotationAwareOrderComparator.sort(customizers);
/* 74 */         builder = builder.customizers(customizers);
/*    */       }
/* 76 */       return builder;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebClientAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */