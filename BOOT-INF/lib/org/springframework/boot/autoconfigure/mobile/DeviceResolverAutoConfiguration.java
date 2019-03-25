/*    */ package org.springframework.boot.autoconfigure.mobile;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
/*    */ import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
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
/*    */ @Configuration
/*    */ @ConditionalOnClass({DeviceResolverHandlerInterceptor.class, DeviceHandlerMethodArgumentResolver.class})
/*    */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*    */ @ConditionalOnWebApplication
/*    */ public class DeviceResolverAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean({DeviceResolverHandlerInterceptor.class})
/*    */   public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor()
/*    */   {
/* 53 */     return new DeviceResolverHandlerInterceptor();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
/* 58 */     return new DeviceHandlerMethodArgumentResolver();
/*    */   }
/*    */   
/*    */ 
/*    */   @Configuration
/*    */   @Order(0)
/*    */   protected static class DeviceResolverMvcConfiguration
/*    */     extends WebMvcConfigurerAdapter
/*    */   {
/*    */     private DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor;
/*    */     
/*    */     private DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver;
/*    */     
/*    */     protected DeviceResolverMvcConfiguration(DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor, DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver)
/*    */     {
/* 73 */       this.deviceResolverHandlerInterceptor = deviceResolverHandlerInterceptor;
/* 74 */       this.deviceHandlerMethodArgumentResolver = deviceHandlerMethodArgumentResolver;
/*    */     }
/*    */     
/*    */     public void addInterceptors(InterceptorRegistry registry)
/*    */     {
/* 79 */       registry.addInterceptor(this.deviceResolverHandlerInterceptor);
/*    */     }
/*    */     
/*    */ 
/*    */     public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*    */     {
/* 85 */       argumentResolvers.add(this.deviceHandlerMethodArgumentResolver);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mobile\DeviceResolverAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */