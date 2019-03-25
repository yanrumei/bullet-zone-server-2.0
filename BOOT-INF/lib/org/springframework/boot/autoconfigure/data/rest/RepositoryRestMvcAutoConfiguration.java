/*    */ package org.springframework.boot.autoconfigure.data.rest;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
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
/*    */ @ConditionalOnWebApplication
/*    */ @ConditionalOnMissingBean({RepositoryRestMvcConfiguration.class})
/*    */ @ConditionalOnClass({RepositoryRestMvcConfiguration.class})
/*    */ @AutoConfigureAfter({HttpMessageConvertersAutoConfiguration.class, JacksonAutoConfiguration.class})
/*    */ @EnableConfigurationProperties({RepositoryRestProperties.class})
/*    */ @Import({RepositoryRestMvcConfiguration.class})
/*    */ public class RepositoryRestMvcAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   public SpringBootRepositoryRestConfigurer springBootRepositoryRestConfigurer()
/*    */   {
/* 60 */     return new SpringBootRepositoryRestConfigurer();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\rest\RepositoryRestMvcAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */