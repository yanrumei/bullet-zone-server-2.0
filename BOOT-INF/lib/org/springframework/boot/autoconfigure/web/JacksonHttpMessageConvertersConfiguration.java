/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*    */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
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
/*    */ class JacksonHttpMessageConvertersConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnClass({ObjectMapper.class})
/*    */   @ConditionalOnBean({ObjectMapper.class})
/*    */   @ConditionalOnProperty(name={"spring.http.converters.preferred-json-mapper"}, havingValue="jackson", matchIfMissing=true)
/*    */   protected static class MappingJackson2HttpMessageConverterConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean(value={MappingJackson2HttpMessageConverter.class}, ignoredType={"org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter", "org.springframework.data.rest.webmvc.alps.AlpsJsonHttpMessageConverter"})
/*    */     public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper)
/*    */     {
/* 53 */       return new MappingJackson2HttpMessageConverter(objectMapper);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   @Configuration
/*    */   @ConditionalOnClass({XmlMapper.class})
/*    */   @ConditionalOnBean({Jackson2ObjectMapperBuilder.class})
/*    */   protected static class MappingJackson2XmlHttpMessageConverterConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/*    */     public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter(Jackson2ObjectMapperBuilder builder)
/*    */     {
/* 67 */       return new MappingJackson2XmlHttpMessageConverter(builder
/* 68 */         .createXmlMapper(true).build());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\JacksonHttpMessageConvertersConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */