/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.StringHttpMessageConverter;
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
/*    */ @Configuration
/*    */ @ConditionalOnClass({HttpMessageConverter.class})
/*    */ @AutoConfigureAfter({GsonAutoConfiguration.class, JacksonAutoConfiguration.class})
/*    */ @Import({JacksonHttpMessageConvertersConfiguration.class, GsonHttpMessageConvertersConfiguration.class})
/*    */ public class HttpMessageConvertersAutoConfiguration
/*    */ {
/*    */   static final String PREFERRED_MAPPER_PROPERTY = "spring.http.converters.preferred-json-mapper";
/*    */   private final List<HttpMessageConverter<?>> converters;
/*    */   
/*    */   public HttpMessageConvertersAutoConfiguration(ObjectProvider<List<HttpMessageConverter<?>>> convertersProvider)
/*    */   {
/* 61 */     this.converters = ((List)convertersProvider.getIfAvailable());
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public HttpMessageConverters messageConverters() {
/* 67 */     return new HttpMessageConverters(this.converters == null ? 
/* 68 */       Collections.emptyList() : this.converters);
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @ConditionalOnClass({StringHttpMessageConverter.class})
/*    */   @EnableConfigurationProperties({HttpEncodingProperties.class})
/*    */   protected static class StringHttpMessageConverterConfiguration
/*    */   {
/*    */     private final HttpEncodingProperties encodingProperties;
/*    */     
/*    */     protected StringHttpMessageConverterConfiguration(HttpEncodingProperties encodingProperties)
/*    */     {
/* 80 */       this.encodingProperties = encodingProperties;
/*    */     }
/*    */     
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/*    */     public StringHttpMessageConverter stringHttpMessageConverter()
/*    */     {
/* 87 */       StringHttpMessageConverter converter = new StringHttpMessageConverter(this.encodingProperties.getCharset());
/* 88 */       converter.setWriteAcceptCharset(false);
/* 89 */       return converter;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\HttpMessageConvertersAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */