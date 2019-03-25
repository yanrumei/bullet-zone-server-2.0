/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ class WebMvcConfigurerComposite
/*     */   implements WebMvcConfigurer
/*     */ {
/*  39 */   private final List<WebMvcConfigurer> delegates = new ArrayList();
/*     */   
/*     */   public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers)
/*     */   {
/*  43 */     if (!CollectionUtils.isEmpty(configurers)) {
/*  44 */       this.delegates.addAll(configurers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void configurePathMatch(PathMatchConfigurer configurer)
/*     */   {
/*  51 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  52 */       delegate.configurePathMatch(configurer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
/*     */   {
/*  58 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  59 */       delegate.configureContentNegotiation(configurer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureAsyncSupport(AsyncSupportConfigurer configurer)
/*     */   {
/*  65 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  66 */       delegate.configureAsyncSupport(configurer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
/*     */   {
/*  72 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  73 */       delegate.configureDefaultServletHandling(configurer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addFormatters(FormatterRegistry registry)
/*     */   {
/*  79 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  80 */       delegate.addFormatters(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addInterceptors(InterceptorRegistry registry)
/*     */   {
/*  86 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  87 */       delegate.addInterceptors(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addResourceHandlers(ResourceHandlerRegistry registry)
/*     */   {
/*  93 */     for (WebMvcConfigurer delegate : this.delegates) {
/*  94 */       delegate.addResourceHandlers(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCorsMappings(CorsRegistry registry)
/*     */   {
/* 100 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 101 */       delegate.addCorsMappings(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addViewControllers(ViewControllerRegistry registry)
/*     */   {
/* 107 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 108 */       delegate.addViewControllers(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureViewResolvers(ViewResolverRegistry registry)
/*     */   {
/* 114 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 115 */       delegate.configureViewResolvers(registry);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 121 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 122 */       delegate.addArgumentResolvers(argumentResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 128 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 129 */       delegate.addReturnValueHandlers(returnValueHandlers);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 135 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 136 */       delegate.configureMessageConverters(converters);
/*     */     }
/*     */   }
/*     */   
/*     */   public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */   {
/* 142 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 143 */       delegate.extendMessageConverters(converters);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */   {
/* 149 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 150 */       delegate.configureHandlerExceptionResolvers(exceptionResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */   public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */   {
/* 156 */     for (WebMvcConfigurer delegate : this.delegates) {
/* 157 */       delegate.extendHandlerExceptionResolvers(exceptionResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */   public Validator getValidator()
/*     */   {
/* 163 */     Validator selected = null;
/* 164 */     for (WebMvcConfigurer configurer : this.delegates) {
/* 165 */       Validator validator = configurer.getValidator();
/* 166 */       if (validator != null) {
/* 167 */         if (selected != null) {
/* 168 */           throw new IllegalStateException("No unique Validator found: {" + selected + ", " + validator + "}");
/*     */         }
/*     */         
/* 171 */         selected = validator;
/*     */       }
/*     */     }
/* 174 */     return selected;
/*     */   }
/*     */   
/*     */   public MessageCodesResolver getMessageCodesResolver()
/*     */   {
/* 179 */     MessageCodesResolver selected = null;
/* 180 */     for (WebMvcConfigurer configurer : this.delegates) {
/* 181 */       MessageCodesResolver messageCodesResolver = configurer.getMessageCodesResolver();
/* 182 */       if (messageCodesResolver != null) {
/* 183 */         if (selected != null) {
/* 184 */           throw new IllegalStateException("No unique MessageCodesResolver found: {" + selected + ", " + messageCodesResolver + "}");
/*     */         }
/*     */         
/* 187 */         selected = messageCodesResolver;
/*     */       }
/*     */     }
/* 190 */     return selected;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\WebMvcConfigurerComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */