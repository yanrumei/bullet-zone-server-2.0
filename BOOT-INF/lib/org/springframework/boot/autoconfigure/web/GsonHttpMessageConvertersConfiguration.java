/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
/*    */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
/*    */ @ConditionalOnClass({Gson.class})
/*    */ class GsonHttpMessageConvertersConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnBean({Gson.class})
/*    */   @Conditional({GsonHttpMessageConvertersConfiguration.PreferGsonOrMissingJacksonCondition.class})
/*    */   protected static class GsonHttpMessageConverterConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean
/*    */     public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson)
/*    */     {
/* 50 */       GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
/* 51 */       converter.setGson(gson);
/* 52 */       return converter;
/*    */     }
/*    */   }
/*    */   
/*    */   private static class PreferGsonOrMissingJacksonCondition extends AnyNestedCondition
/*    */   {
/*    */     PreferGsonOrMissingJacksonCondition()
/*    */     {
/* 60 */       super();
/*    */     }
/*    */     
/*    */     @ConditionalOnMissingBean({MappingJackson2HttpMessageConverter.class})
/*    */     static class JacksonMissing {}
/*    */     
/*    */     @ConditionalOnProperty(name={"spring.http.converters.preferred-json-mapper"}, havingValue="gson", matchIfMissing=false)
/*    */     static class GsonPreferred {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\GsonHttpMessageConvertersConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */