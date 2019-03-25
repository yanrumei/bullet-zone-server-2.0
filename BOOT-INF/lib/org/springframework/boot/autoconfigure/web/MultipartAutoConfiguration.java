/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import javax.servlet.MultipartConfigElement;
/*    */ import javax.servlet.Servlet;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.web.multipart.MultipartResolver;
/*    */ import org.springframework.web.multipart.support.StandardServletMultipartResolver;
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
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class})
/*    */ @ConditionalOnProperty(prefix="spring.http.multipart", name={"enabled"}, matchIfMissing=true)
/*    */ @EnableConfigurationProperties({MultipartProperties.class})
/*    */ public class MultipartAutoConfiguration
/*    */ {
/*    */   private final MultipartProperties multipartProperties;
/*    */   
/*    */   public MultipartAutoConfiguration(MultipartProperties multipartProperties)
/*    */   {
/* 58 */     this.multipartProperties = multipartProperties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public MultipartConfigElement multipartConfigElement() {
/* 64 */     return this.multipartProperties.createMultipartConfig();
/*    */   }
/*    */   
/*    */   @Bean(name={"multipartResolver"})
/*    */   @ConditionalOnMissingBean({MultipartResolver.class})
/*    */   public StandardServletMultipartResolver multipartResolver() {
/* 70 */     StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
/* 71 */     multipartResolver.setResolveLazily(this.multipartProperties.isResolveLazily());
/* 72 */     return multipartResolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\MultipartAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */