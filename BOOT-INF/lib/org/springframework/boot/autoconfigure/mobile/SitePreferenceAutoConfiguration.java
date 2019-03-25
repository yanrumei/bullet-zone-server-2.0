/*    */ package org.springframework.boot.autoconfigure.mobile;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
/*    */ import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
/*    */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
/*    */ @ConditionalOnClass({SitePreferenceHandlerInterceptor.class, SitePreferenceHandlerMethodArgumentResolver.class})
/*    */ @AutoConfigureAfter({DeviceResolverAutoConfiguration.class})
/*    */ @ConditionalOnProperty(prefix="spring.mobile.sitepreference", name={"enabled"}, havingValue="true", matchIfMissing=true)
/*    */ @ConditionalOnWebApplication
/*    */ public class SitePreferenceAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({SitePreferenceHandlerInterceptor.class})
/*    */   public SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor()
/*    */   {
/* 56 */     return new SitePreferenceHandlerInterceptor();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver() {
/* 61 */     return new SitePreferenceHandlerMethodArgumentResolver();
/*    */   }
/*    */   
/*    */ 
/*    */   @Configuration
/*    */   protected static class SitePreferenceMvcConfiguration
/*    */     extends WebMvcConfigurerAdapter
/*    */   {
/*    */     private final SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor;
/*    */     
/*    */     private final SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver;
/*    */     
/*    */     protected SitePreferenceMvcConfiguration(SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor, SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver)
/*    */     {
/* 75 */       this.sitePreferenceHandlerInterceptor = sitePreferenceHandlerInterceptor;
/* 76 */       this.sitePreferenceHandlerMethodArgumentResolver = sitePreferenceHandlerMethodArgumentResolver;
/*    */     }
/*    */     
/*    */     public void addInterceptors(InterceptorRegistry registry)
/*    */     {
/* 81 */       registry.addInterceptor(this.sitePreferenceHandlerInterceptor);
/*    */     }
/*    */     
/*    */ 
/*    */     public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*    */     {
/* 87 */       argumentResolvers.add(this.sitePreferenceHandlerMethodArgumentResolver);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mobile\SitePreferenceAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */