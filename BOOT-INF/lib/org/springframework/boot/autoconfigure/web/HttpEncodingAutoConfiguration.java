/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.filter.CharacterEncodingFilter;
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
/*    */ @EnableConfigurationProperties({HttpEncodingProperties.class})
/*    */ @ConditionalOnWebApplication
/*    */ @ConditionalOnClass({CharacterEncodingFilter.class})
/*    */ @ConditionalOnProperty(prefix="spring.http.encoding", value={"enabled"}, matchIfMissing=true)
/*    */ public class HttpEncodingAutoConfiguration
/*    */ {
/*    */   private final HttpEncodingProperties properties;
/*    */   
/*    */   public HttpEncodingAutoConfiguration(HttpEncodingProperties properties)
/*    */   {
/* 52 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({CharacterEncodingFilter.class})
/*    */   public CharacterEncodingFilter characterEncodingFilter() {
/* 58 */     CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
/* 59 */     filter.setEncoding(this.properties.getCharset().name());
/* 60 */     filter.setForceRequestEncoding(this.properties.shouldForce(HttpEncodingProperties.Type.REQUEST));
/* 61 */     filter.setForceResponseEncoding(this.properties.shouldForce(HttpEncodingProperties.Type.RESPONSE));
/* 62 */     return filter;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public LocaleCharsetMappingsCustomizer localeCharsetMappingsCustomizer() {
/* 67 */     return new LocaleCharsetMappingsCustomizer(this.properties);
/*    */   }
/*    */   
/*    */   private static class LocaleCharsetMappingsCustomizer implements EmbeddedServletContainerCustomizer, Ordered
/*    */   {
/*    */     private final HttpEncodingProperties properties;
/*    */     
/*    */     LocaleCharsetMappingsCustomizer(HttpEncodingProperties properties)
/*    */     {
/* 76 */       this.properties = properties;
/*    */     }
/*    */     
/*    */     public void customize(ConfigurableEmbeddedServletContainer container)
/*    */     {
/* 81 */       if (this.properties.getMapping() != null) {
/* 82 */         container.setLocaleCharsetMappings(this.properties.getMapping());
/*    */       }
/*    */     }
/*    */     
/*    */     public int getOrder()
/*    */     {
/* 88 */       return 0;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\HttpEncodingAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */