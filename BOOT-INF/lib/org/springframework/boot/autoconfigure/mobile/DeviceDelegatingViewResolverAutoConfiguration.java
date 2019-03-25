/*     */ package org.springframework.boot.autoconfigure.mobile;
/*     */ 
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mustache.web.MustacheViewResolver;
/*     */ import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
/*     */ import org.thymeleaf.spring4.view.ThymeleafViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnWebApplication
/*     */ @ConditionalOnClass({LiteDeviceDelegatingViewResolver.class})
/*     */ @ConditionalOnProperty(prefix="spring.mobile.devicedelegatingviewresolver", name={"enabled"}, havingValue="true")
/*     */ @EnableConfigurationProperties({DeviceDelegatingViewResolverProperties.class})
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class, FreeMarkerAutoConfiguration.class, GroovyTemplateAutoConfiguration.class, MustacheAutoConfiguration.class, ThymeleafAutoConfiguration.class})
/*     */ public class DeviceDelegatingViewResolverAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   protected static class LiteDeviceDelegatingViewResolverFactoryConfiguration
/*     */   {
/*     */     @Bean
/*     */     public DeviceDelegatingViewResolverFactory deviceDelegatingViewResolverFactory(DeviceDelegatingViewResolverProperties properties)
/*     */     {
/*  67 */       return new DeviceDelegatingViewResolverFactory(properties);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({FreeMarkerViewResolver.class})
/*     */   protected static class DeviceDelegatingFreeMarkerViewResolverConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnBean({FreeMarkerViewResolver.class})
/*     */     public LiteDeviceDelegatingViewResolver deviceDelegatingFreeMarkerViewResolver(DeviceDelegatingViewResolverFactory factory, FreeMarkerViewResolver viewResolver)
/*     */     {
/*  81 */       return factory.createViewResolver(viewResolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({GroovyMarkupViewResolver.class})
/*     */   protected static class DeviceDelegatingGroovyMarkupViewResolverConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnBean({GroovyMarkupViewResolver.class})
/*     */     public LiteDeviceDelegatingViewResolver deviceDelegatingGroovyMarkupViewResolver(DeviceDelegatingViewResolverFactory factory, GroovyMarkupViewResolver viewResolver)
/*     */     {
/*  95 */       return factory.createViewResolver(viewResolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({InternalResourceViewResolver.class})
/*     */   protected static class DeviceDelegatingJspViewResolverConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnBean({InternalResourceViewResolver.class})
/*     */     public LiteDeviceDelegatingViewResolver deviceDelegatingJspViewResolver(DeviceDelegatingViewResolverFactory factory, InternalResourceViewResolver viewResolver)
/*     */     {
/* 109 */       return factory.createViewResolver(viewResolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({MustacheViewResolver.class})
/*     */   protected static class DeviceDelegatingMustacheViewResolverConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnBean({MustacheViewResolver.class})
/*     */     public LiteDeviceDelegatingViewResolver deviceDelegatingMustacheViewResolver(DeviceDelegatingViewResolverFactory factory, MustacheViewResolver viewResolver)
/*     */     {
/* 123 */       return factory.createViewResolver(viewResolver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({ThymeleafViewResolver.class})
/*     */   protected static class DeviceDelegatingThymeleafViewResolverConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnBean({ThymeleafViewResolver.class})
/*     */     public LiteDeviceDelegatingViewResolver deviceDelegatingThymeleafViewResolver(DeviceDelegatingViewResolverFactory factory, ThymeleafViewResolver viewResolver)
/*     */     {
/* 137 */       return factory.createViewResolver(viewResolver);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mobile\DeviceDelegatingViewResolverAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */