/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebMvcConfigurerAdapter
/*     */   implements WebMvcConfigurer
/*     */ {
/*     */   public void configurePathMatch(PathMatchConfigurer configurer) {}
/*     */   
/*     */   public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}
/*     */   
/*     */   public void configureAsyncSupport(AsyncSupportConfigurer configurer) {}
/*     */   
/*     */   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/*     */   
/*     */   public void addFormatters(FormatterRegistry registry) {}
/*     */   
/*     */   public void addInterceptors(InterceptorRegistry registry) {}
/*     */   
/*     */   public void addResourceHandlers(ResourceHandlerRegistry registry) {}
/*     */   
/*     */   public void addCorsMappings(CorsRegistry registry) {}
/*     */   
/*     */   public void addViewControllers(ViewControllerRegistry registry) {}
/*     */   
/*     */   public void configureViewResolvers(ViewResolverRegistry registry) {}
/*     */   
/*     */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}
/*     */   
/*     */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}
/*     */   
/*     */   public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/*     */   
/*     */   public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/*     */   
/*     */   public Validator getValidator()
/*     */   {
/* 172 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageCodesResolver getMessageCodesResolver()
/*     */   {
/* 181 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\WebMvcConfigurerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */