/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
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
/*     */ public class DelegatingWebMvcConfiguration
/*     */   extends WebMvcConfigurationSupport
/*     */ {
/*  44 */   private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
/*     */   
/*     */   @Autowired(required=false)
/*     */   public void setConfigurers(List<WebMvcConfigurer> configurers)
/*     */   {
/*  49 */     if (!CollectionUtils.isEmpty(configurers)) {
/*  50 */       this.configurers.addWebMvcConfigurers(configurers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void configurePathMatch(PathMatchConfigurer configurer)
/*     */   {
/*  57 */     this.configurers.configurePathMatch(configurer);
/*     */   }
/*     */   
/*     */   protected void configureContentNegotiation(ContentNegotiationConfigurer configurer)
/*     */   {
/*  62 */     this.configurers.configureContentNegotiation(configurer);
/*     */   }
/*     */   
/*     */   protected void configureAsyncSupport(AsyncSupportConfigurer configurer)
/*     */   {
/*  67 */     this.configurers.configureAsyncSupport(configurer);
/*     */   }
/*     */   
/*     */   protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
/*     */   {
/*  72 */     this.configurers.configureDefaultServletHandling(configurer);
/*     */   }
/*     */   
/*     */   protected void addFormatters(FormatterRegistry registry)
/*     */   {
/*  77 */     this.configurers.addFormatters(registry);
/*     */   }
/*     */   
/*     */   protected void addInterceptors(InterceptorRegistry registry)
/*     */   {
/*  82 */     this.configurers.addInterceptors(registry);
/*     */   }
/*     */   
/*     */   protected void addResourceHandlers(ResourceHandlerRegistry registry)
/*     */   {
/*  87 */     this.configurers.addResourceHandlers(registry);
/*     */   }
/*     */   
/*     */   protected void addCorsMappings(CorsRegistry registry)
/*     */   {
/*  92 */     this.configurers.addCorsMappings(registry);
/*     */   }
/*     */   
/*     */   protected void addViewControllers(ViewControllerRegistry registry)
/*     */   {
/*  97 */     this.configurers.addViewControllers(registry);
/*     */   }
/*     */   
/*     */   protected void configureViewResolvers(ViewResolverRegistry registry)
/*     */   {
/* 102 */     this.configurers.configureViewResolvers(registry);
/*     */   }
/*     */   
/*     */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 107 */     this.configurers.addArgumentResolvers(argumentResolvers);
/*     */   }
/*     */   
/*     */   protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 112 */     this.configurers.addReturnValueHandlers(returnValueHandlers);
/*     */   }
/*     */   
/*     */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 117 */     this.configurers.configureMessageConverters(converters);
/*     */   }
/*     */   
/*     */   protected void extendMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 122 */     this.configurers.extendMessageConverters(converters);
/*     */   }
/*     */   
/*     */   protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */   {
/* 127 */     this.configurers.configureHandlerExceptionResolvers(exceptionResolvers);
/*     */   }
/*     */   
/*     */   protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */   {
/* 132 */     this.configurers.extendHandlerExceptionResolvers(exceptionResolvers);
/*     */   }
/*     */   
/*     */   protected Validator getValidator()
/*     */   {
/* 137 */     return this.configurers.getValidator();
/*     */   }
/*     */   
/*     */   protected MessageCodesResolver getMessageCodesResolver()
/*     */   {
/* 142 */     return this.configurers.getMessageCodesResolver();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\DelegatingWebMvcConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */