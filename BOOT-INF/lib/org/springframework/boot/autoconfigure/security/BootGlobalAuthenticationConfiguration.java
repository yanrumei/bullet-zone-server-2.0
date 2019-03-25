/*    */ package org.springframework.boot.autoconfigure.security;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
/*    */ import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
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
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({GlobalAuthenticationConfigurerAdapter.class})
/*    */ public class BootGlobalAuthenticationConfiguration
/*    */ {
/*    */   @Bean
/*    */   public static BootGlobalAuthenticationConfigurationAdapter bootGlobalAuthenticationConfigurationAdapter(ApplicationContext context)
/*    */   {
/* 57 */     return new BootGlobalAuthenticationConfigurationAdapter(context);
/*    */   }
/*    */   
/*    */ 
/*    */   private static class BootGlobalAuthenticationConfigurationAdapter
/*    */     extends GlobalAuthenticationConfigurerAdapter
/*    */   {
/* 64 */     private static final Log logger = LogFactory.getLog(BootGlobalAuthenticationConfiguration.class);
/*    */     private final ApplicationContext context;
/*    */     
/*    */     BootGlobalAuthenticationConfigurationAdapter(ApplicationContext context)
/*    */     {
/* 69 */       this.context = context;
/*    */     }
/*    */     
/*    */ 
/*    */     public void init(AuthenticationManagerBuilder auth)
/*    */     {
/* 75 */       Map<String, Object> beansWithAnnotation = this.context.getBeansWithAnnotation(EnableAutoConfiguration.class);
/* 76 */       if (logger.isDebugEnabled()) {
/* 77 */         logger.debug("Eagerly initializing " + beansWithAnnotation);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\BootGlobalAuthenticationConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */