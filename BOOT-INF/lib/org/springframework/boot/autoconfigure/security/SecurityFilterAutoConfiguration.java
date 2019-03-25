/*    */ package org.springframework.boot.autoconfigure.security;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.DispatcherType;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
/*    */ import org.springframework.boot.web.servlet.ServletRegistrationBean;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.security.config.http.SessionCreationPolicy;
/*    */ import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
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
/*    */ @EnableConfigurationProperties
/*    */ @ConditionalOnClass({AbstractSecurityWebApplicationInitializer.class, SessionCreationPolicy.class})
/*    */ @AutoConfigureAfter({SecurityAutoConfiguration.class})
/*    */ public class SecurityFilterAutoConfiguration
/*    */ {
/*    */   private static final String DEFAULT_FILTER_NAME = "springSecurityFilterChain";
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnBean(name={"springSecurityFilterChain"})
/*    */   public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(SecurityProperties securityProperties)
/*    */   {
/* 64 */     DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean("springSecurityFilterChain", new ServletRegistrationBean[0]);
/*    */     
/* 66 */     registration.setOrder(securityProperties.getFilterOrder());
/* 67 */     registration.setDispatcherTypes(getDispatcherTypes(securityProperties));
/* 68 */     return registration;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SecurityProperties securityProperties() {
/* 74 */     return new SecurityProperties();
/*    */   }
/*    */   
/*    */   private EnumSet<DispatcherType> getDispatcherTypes(SecurityProperties securityProperties)
/*    */   {
/* 79 */     if (securityProperties.getFilterDispatcherTypes() == null) {
/* 80 */       return null;
/*    */     }
/* 82 */     Set<DispatcherType> dispatcherTypes = new HashSet();
/* 83 */     for (String dispatcherType : securityProperties.getFilterDispatcherTypes()) {
/* 84 */       dispatcherTypes.add(DispatcherType.valueOf(dispatcherType));
/*    */     }
/* 86 */     return EnumSet.copyOf(dispatcherTypes);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\SecurityFilterAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */