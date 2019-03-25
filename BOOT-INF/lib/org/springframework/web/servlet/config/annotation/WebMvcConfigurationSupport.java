/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.support.DefaultFormattingConversionService;
/*     */ import org.springframework.format.support.FormattingConversionService;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*     */ import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
/*     */ import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/*     */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProvider;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.ViewResolverComposite;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class WebMvcConfigurationSupport
/*     */   implements ApplicationContextAware, ServletContextAware
/*     */ {
/* 177 */   private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", WebMvcConfigurationSupport.class
/* 178 */     .getClassLoader());
/*     */   
/*     */ 
/* 181 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", WebMvcConfigurationSupport.class
/* 182 */     .getClassLoader());
/*     */   
/*     */   static {
/* 185 */     if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", WebMvcConfigurationSupport.class
/* 186 */       .getClassLoader())) {} }
/* 187 */   private static final boolean jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", WebMvcConfigurationSupport.class
/* 188 */     .getClassLoader());
/*     */   
/*     */ 
/* 191 */   private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", WebMvcConfigurationSupport.class
/* 192 */     .getClassLoader());
/*     */   
/*     */ 
/* 195 */   private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", WebMvcConfigurationSupport.class
/* 196 */     .getClassLoader());
/*     */   
/*     */ 
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */ 
/*     */   private ServletContext servletContext;
/*     */   
/*     */ 
/*     */   private List<Object> interceptors;
/*     */   
/*     */ 
/*     */   private PathMatchConfigurer pathMatchConfigurer;
/*     */   
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */   private List<HandlerMethodArgumentResolver> argumentResolvers;
/*     */   
/*     */   private List<HandlerMethodReturnValueHandler> returnValueHandlers;
/*     */   
/*     */   private List<HttpMessageConverter<?>> messageConverters;
/*     */   
/*     */   private Map<String, CorsConfiguration> corsConfigurations;
/*     */   
/*     */ 
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 223 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationContext getApplicationContext()
/*     */   {
/* 231 */     return this.applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 240 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletContext getServletContext()
/*     */   {
/* 248 */     return this.servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public RequestMappingHandlerMapping requestMappingHandlerMapping()
/*     */   {
/* 258 */     RequestMappingHandlerMapping mapping = createRequestMappingHandlerMapping();
/* 259 */     mapping.setOrder(0);
/* 260 */     mapping.setInterceptors(getInterceptors());
/* 261 */     mapping.setContentNegotiationManager(mvcContentNegotiationManager());
/* 262 */     mapping.setCorsConfigurations(getCorsConfigurations());
/*     */     
/* 264 */     PathMatchConfigurer configurer = getPathMatchConfigurer();
/* 265 */     if (configurer.isUseSuffixPatternMatch() != null) {
/* 266 */       mapping.setUseSuffixPatternMatch(configurer.isUseSuffixPatternMatch().booleanValue());
/*     */     }
/* 268 */     if (configurer.isUseRegisteredSuffixPatternMatch() != null) {
/* 269 */       mapping.setUseRegisteredSuffixPatternMatch(configurer.isUseRegisteredSuffixPatternMatch().booleanValue());
/*     */     }
/* 271 */     if (configurer.isUseTrailingSlashMatch() != null) {
/* 272 */       mapping.setUseTrailingSlashMatch(configurer.isUseTrailingSlashMatch().booleanValue());
/*     */     }
/* 274 */     UrlPathHelper pathHelper = configurer.getUrlPathHelper();
/* 275 */     if (pathHelper != null) {
/* 276 */       mapping.setUrlPathHelper(pathHelper);
/*     */     }
/* 278 */     PathMatcher pathMatcher = configurer.getPathMatcher();
/* 279 */     if (pathMatcher != null) {
/* 280 */       mapping.setPathMatcher(pathMatcher);
/*     */     }
/*     */     
/* 283 */     return mapping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestMappingHandlerMapping createRequestMappingHandlerMapping()
/*     */   {
/* 292 */     return new RequestMappingHandlerMapping();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object[] getInterceptors()
/*     */   {
/* 301 */     if (this.interceptors == null) {
/* 302 */       InterceptorRegistry registry = new InterceptorRegistry();
/* 303 */       addInterceptors(registry);
/* 304 */       registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
/* 305 */       registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
/* 306 */       this.interceptors = registry.getInterceptors();
/*     */     }
/* 308 */     return this.interceptors.toArray();
/*     */   }
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
/*     */   protected PathMatchConfigurer getPathMatchConfigurer()
/*     */   {
/* 325 */     if (this.pathMatchConfigurer == null) {
/* 326 */       this.pathMatchConfigurer = new PathMatchConfigurer();
/* 327 */       configurePathMatch(this.pathMatchConfigurer);
/*     */     }
/* 329 */     return this.pathMatchConfigurer;
/*     */   }
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
/*     */   @Bean
/*     */   public PathMatcher mvcPathMatcher()
/*     */   {
/* 349 */     PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
/* 350 */     return pathMatcher != null ? pathMatcher : new AntPathMatcher();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public UrlPathHelper mvcUrlPathHelper()
/*     */   {
/* 362 */     UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
/* 363 */     return pathHelper != null ? pathHelper : new UrlPathHelper();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public ContentNegotiationManager mvcContentNegotiationManager()
/*     */   {
/* 372 */     if (this.contentNegotiationManager == null) {
/* 373 */       ContentNegotiationConfigurer configurer = new ContentNegotiationConfigurer(this.servletContext);
/* 374 */       configurer.mediaTypes(getDefaultMediaTypes());
/* 375 */       configureContentNegotiation(configurer);
/* 376 */       this.contentNegotiationManager = configurer.buildContentNegotiationManager();
/*     */     }
/* 378 */     return this.contentNegotiationManager;
/*     */   }
/*     */   
/*     */   protected Map<String, MediaType> getDefaultMediaTypes() {
/* 382 */     Map<String, MediaType> map = new HashMap(4);
/* 383 */     if (romePresent) {
/* 384 */       map.put("atom", MediaType.APPLICATION_ATOM_XML);
/* 385 */       map.put("rss", MediaType.APPLICATION_RSS_XML);
/*     */     }
/* 387 */     if ((jaxb2Present) || (jackson2XmlPresent)) {
/* 388 */       map.put("xml", MediaType.APPLICATION_XML);
/*     */     }
/* 390 */     if ((jackson2Present) || (gsonPresent)) {
/* 391 */       map.put("json", MediaType.APPLICATION_JSON);
/*     */     }
/* 393 */     return map;
/*     */   }
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
/*     */   @Bean
/*     */   public HandlerMapping viewControllerHandlerMapping()
/*     */   {
/* 410 */     ViewControllerRegistry registry = new ViewControllerRegistry(this.applicationContext);
/* 411 */     addViewControllers(registry);
/*     */     
/* 413 */     AbstractHandlerMapping handlerMapping = registry.buildHandlerMapping();
/* 414 */     handlerMapping = handlerMapping != null ? handlerMapping : new EmptyHandlerMapping(null);
/* 415 */     handlerMapping.setPathMatcher(mvcPathMatcher());
/* 416 */     handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
/* 417 */     handlerMapping.setInterceptors(getInterceptors());
/* 418 */     handlerMapping.setCorsConfigurations(getCorsConfigurations());
/* 419 */     return handlerMapping;
/*     */   }
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
/*     */   @Bean
/*     */   public BeanNameUrlHandlerMapping beanNameHandlerMapping()
/*     */   {
/* 435 */     BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
/* 436 */     mapping.setOrder(2);
/* 437 */     mapping.setInterceptors(getInterceptors());
/* 438 */     mapping.setCorsConfigurations(getCorsConfigurations());
/* 439 */     return mapping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public HandlerMapping resourceHandlerMapping()
/*     */   {
/* 449 */     Assert.state(this.applicationContext != null, "No ApplicationContext set");
/* 450 */     Assert.state(this.servletContext != null, "No ServletContext set");
/*     */     
/*     */ 
/* 453 */     ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext, this.servletContext, mvcContentNegotiationManager(), mvcUrlPathHelper());
/* 454 */     addResourceHandlers(registry);
/*     */     
/* 456 */     AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
/* 457 */     if (handlerMapping != null) {
/* 458 */       handlerMapping.setPathMatcher(mvcPathMatcher());
/* 459 */       handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
/* 460 */       handlerMapping.setInterceptors(new Object[] { new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()) });
/* 461 */       handlerMapping.setCorsConfigurations(getCorsConfigurations());
/*     */     }
/*     */     else {
/* 464 */       handlerMapping = new EmptyHandlerMapping(null);
/*     */     }
/* 466 */     return handlerMapping;
/*     */   }
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
/*     */   @Bean
/*     */   public ResourceUrlProvider mvcResourceUrlProvider()
/*     */   {
/* 482 */     ResourceUrlProvider urlProvider = new ResourceUrlProvider();
/* 483 */     UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
/* 484 */     if (pathHelper != null) {
/* 485 */       urlProvider.setUrlPathHelper(pathHelper);
/*     */     }
/* 487 */     PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
/* 488 */     if (pathMatcher != null) {
/* 489 */       urlProvider.setPathMatcher(pathMatcher);
/*     */     }
/* 491 */     return urlProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public HandlerMapping defaultServletHandlerMapping()
/*     */   {
/* 501 */     DefaultServletHandlerConfigurer configurer = new DefaultServletHandlerConfigurer(this.servletContext);
/* 502 */     configureDefaultServletHandling(configurer);
/*     */     
/* 504 */     HandlerMapping handlerMapping = configurer.buildHandlerMapping();
/* 505 */     return handlerMapping != null ? handlerMapping : new EmptyHandlerMapping(null);
/*     */   }
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
/*     */   @Bean
/*     */   public RequestMappingHandlerAdapter requestMappingHandlerAdapter()
/*     */   {
/* 527 */     RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
/* 528 */     adapter.setContentNegotiationManager(mvcContentNegotiationManager());
/* 529 */     adapter.setMessageConverters(getMessageConverters());
/* 530 */     adapter.setWebBindingInitializer(getConfigurableWebBindingInitializer());
/* 531 */     adapter.setCustomArgumentResolvers(getArgumentResolvers());
/* 532 */     adapter.setCustomReturnValueHandlers(getReturnValueHandlers());
/*     */     
/* 534 */     if (jackson2Present) {
/* 535 */       adapter.setRequestBodyAdvice(
/* 536 */         Collections.singletonList(new JsonViewRequestBodyAdvice()));
/* 537 */       adapter.setResponseBodyAdvice(
/* 538 */         Collections.singletonList(new JsonViewResponseBodyAdvice()));
/*     */     }
/*     */     
/* 541 */     AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
/* 542 */     configureAsyncSupport(configurer);
/* 543 */     if (configurer.getTaskExecutor() != null) {
/* 544 */       adapter.setTaskExecutor(configurer.getTaskExecutor());
/*     */     }
/* 546 */     if (configurer.getTimeout() != null) {
/* 547 */       adapter.setAsyncRequestTimeout(configurer.getTimeout().longValue());
/*     */     }
/* 549 */     adapter.setCallableInterceptors(configurer.getCallableInterceptors());
/* 550 */     adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());
/*     */     
/* 552 */     return adapter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter()
/*     */   {
/* 561 */     return new RequestMappingHandlerAdapter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer()
/*     */   {
/* 569 */     ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
/* 570 */     initializer.setConversionService(mvcConversionService());
/* 571 */     initializer.setValidator(mvcValidator());
/* 572 */     initializer.setMessageCodesResolver(getMessageCodesResolver());
/* 573 */     return initializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MessageCodesResolver getMessageCodesResolver()
/*     */   {
/* 580 */     return null;
/*     */   }
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
/*     */   @Bean
/*     */   public FormattingConversionService mvcConversionService()
/*     */   {
/* 597 */     FormattingConversionService conversionService = new DefaultFormattingConversionService();
/* 598 */     addFormatters(conversionService);
/* 599 */     return conversionService;
/*     */   }
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
/*     */   @Bean
/*     */   public Validator mvcValidator()
/*     */   {
/* 618 */     Validator validator = getValidator();
/* 619 */     if (validator == null) {
/* 620 */       if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader()))
/*     */       {
/*     */         try {
/* 623 */           String className = "org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean";
/* 624 */           clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
/*     */         } catch (ClassNotFoundException ex) {
/*     */           Class<?> clazz;
/* 627 */           throw new BeanInitializationException("Could not find default validator class", ex);
/*     */         }
/*     */         catch (LinkageError ex) {
/* 630 */           throw new BeanInitializationException("Could not load default validator class", ex); }
/*     */         Class<?> clazz;
/* 632 */         validator = (Validator)BeanUtils.instantiateClass(clazz);
/*     */       }
/*     */       else {
/* 635 */         validator = new NoOpValidator(null);
/*     */       }
/*     */     }
/* 638 */     return validator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Validator getValidator()
/*     */   {
/* 645 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final List<HandlerMethodArgumentResolver> getArgumentResolvers()
/*     */   {
/* 656 */     if (this.argumentResolvers == null) {
/* 657 */       this.argumentResolvers = new ArrayList();
/* 658 */       addArgumentResolvers(this.argumentResolvers);
/*     */     }
/* 660 */     return this.argumentResolvers;
/*     */   }
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
/*     */   protected final List<HandlerMethodReturnValueHandler> getReturnValueHandlers()
/*     */   {
/* 685 */     if (this.returnValueHandlers == null) {
/* 686 */       this.returnValueHandlers = new ArrayList();
/* 687 */       addReturnValueHandlers(this.returnValueHandlers);
/*     */     }
/* 689 */     return this.returnValueHandlers;
/*     */   }
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
/*     */   protected final List<HttpMessageConverter<?>> getMessageConverters()
/*     */   {
/* 716 */     if (this.messageConverters == null) {
/* 717 */       this.messageConverters = new ArrayList();
/* 718 */       configureMessageConverters(this.messageConverters);
/* 719 */       if (this.messageConverters.isEmpty()) {
/* 720 */         addDefaultHttpMessageConverters(this.messageConverters);
/*     */       }
/* 722 */       extendMessageConverters(this.messageConverters);
/*     */     }
/* 724 */     return this.messageConverters;
/*     */   }
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
/*     */   protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 757 */     StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
/* 758 */     stringConverter.setWriteAcceptCharset(false);
/*     */     
/* 760 */     messageConverters.add(new ByteArrayHttpMessageConverter());
/* 761 */     messageConverters.add(stringConverter);
/* 762 */     messageConverters.add(new ResourceHttpMessageConverter());
/* 763 */     messageConverters.add(new SourceHttpMessageConverter());
/* 764 */     messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*     */     
/* 766 */     if (romePresent) {
/* 767 */       messageConverters.add(new AtomFeedHttpMessageConverter());
/* 768 */       messageConverters.add(new RssChannelHttpMessageConverter());
/*     */     }
/*     */     
/* 771 */     if (jackson2XmlPresent) {
/* 772 */       messageConverters.add(new MappingJackson2XmlHttpMessageConverter(
/* 773 */         Jackson2ObjectMapperBuilder.xml().applicationContext(this.applicationContext).build()));
/*     */     }
/* 775 */     else if (jaxb2Present) {
/* 776 */       messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/*     */     }
/*     */     
/* 779 */     if (jackson2Present) {
/* 780 */       messageConverters.add(new MappingJackson2HttpMessageConverter(
/* 781 */         Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build()));
/*     */     }
/* 783 */     else if (gsonPresent) {
/* 784 */       messageConverters.add(new GsonHttpMessageConverter());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public CompositeUriComponentsContributor mvcUriComponentsContributor()
/*     */   {
/* 795 */     return new CompositeUriComponentsContributor(
/* 796 */       requestMappingHandlerAdapter().getArgumentResolvers(), mvcConversionService());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public HttpRequestHandlerAdapter httpRequestHandlerAdapter()
/*     */   {
/* 805 */     return new HttpRequestHandlerAdapter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter()
/*     */   {
/* 814 */     return new SimpleControllerHandlerAdapter();
/*     */   }
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
/*     */   @Bean
/*     */   public HandlerExceptionResolver handlerExceptionResolver()
/*     */   {
/* 829 */     List<HandlerExceptionResolver> exceptionResolvers = new ArrayList();
/* 830 */     configureHandlerExceptionResolvers(exceptionResolvers);
/* 831 */     if (exceptionResolvers.isEmpty()) {
/* 832 */       addDefaultHandlerExceptionResolvers(exceptionResolvers);
/*     */     }
/* 834 */     extendHandlerExceptionResolvers(exceptionResolvers);
/* 835 */     HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
/* 836 */     composite.setOrder(0);
/* 837 */     composite.setExceptionResolvers(exceptionResolvers);
/* 838 */     return composite;
/*     */   }
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
/*     */   protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */   {
/* 877 */     ExceptionHandlerExceptionResolver exceptionHandlerResolver = createExceptionHandlerExceptionResolver();
/* 878 */     exceptionHandlerResolver.setContentNegotiationManager(mvcContentNegotiationManager());
/* 879 */     exceptionHandlerResolver.setMessageConverters(getMessageConverters());
/* 880 */     exceptionHandlerResolver.setCustomArgumentResolvers(getArgumentResolvers());
/* 881 */     exceptionHandlerResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
/* 882 */     if (jackson2Present) {
/* 883 */       exceptionHandlerResolver.setResponseBodyAdvice(
/* 884 */         Collections.singletonList(new JsonViewResponseBodyAdvice()));
/*     */     }
/* 886 */     exceptionHandlerResolver.setApplicationContext(this.applicationContext);
/* 887 */     exceptionHandlerResolver.afterPropertiesSet();
/* 888 */     exceptionResolvers.add(exceptionHandlerResolver);
/*     */     
/* 890 */     ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
/* 891 */     responseStatusResolver.setMessageSource(this.applicationContext);
/* 892 */     exceptionResolvers.add(responseStatusResolver);
/*     */     
/* 894 */     exceptionResolvers.add(new DefaultHandlerExceptionResolver());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver()
/*     */   {
/* 903 */     return new ExceptionHandlerExceptionResolver();
/*     */   }
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
/*     */   @Bean
/*     */   public ViewResolver mvcViewResolver()
/*     */   {
/* 921 */     ViewResolverRegistry registry = new ViewResolverRegistry(mvcContentNegotiationManager(), this.applicationContext);
/* 922 */     configureViewResolvers(registry);
/*     */     
/* 924 */     if (registry.getViewResolvers().isEmpty()) {
/* 925 */       String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, ViewResolver.class, true, false);
/*     */       
/* 927 */       if (names.length == 1) {
/* 928 */         registry.getViewResolvers().add(new InternalResourceViewResolver());
/*     */       }
/*     */     }
/*     */     
/* 932 */     ViewResolverComposite composite = new ViewResolverComposite();
/* 933 */     composite.setOrder(registry.getOrder());
/* 934 */     composite.setViewResolvers(registry.getViewResolvers());
/* 935 */     composite.setApplicationContext(this.applicationContext);
/* 936 */     composite.setServletContext(this.servletContext);
/* 937 */     return composite;
/*     */   }
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
/*     */   protected final Map<String, CorsConfiguration> getCorsConfigurations()
/*     */   {
/* 953 */     if (this.corsConfigurations == null) {
/* 954 */       CorsRegistry registry = new CorsRegistry();
/* 955 */       addCorsMappings(registry);
/* 956 */       this.corsConfigurations = registry.getCorsConfigurations();
/*     */     }
/* 958 */     return this.corsConfigurations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   @Lazy
/*     */   public HandlerMappingIntrospector mvcHandlerMappingIntrospector()
/*     */   {
/* 971 */     return new HandlerMappingIntrospector();
/*     */   }
/*     */   
/*     */   protected void addInterceptors(InterceptorRegistry registry) {}
/*     */   
/*     */   protected void configurePathMatch(PathMatchConfigurer configurer) {}
/*     */   
/*     */   private static final class EmptyHandlerMapping extends AbstractHandlerMapping {
/*     */     protected Object getHandlerInternal(HttpServletRequest request) {
/* 980 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}
/*     */   
/*     */   protected void addViewControllers(ViewControllerRegistry registry) {}
/*     */   
/*     */   private static final class NoOpValidator implements Validator {
/* 989 */     public boolean supports(Class<?> clazz) { return false; }
/*     */     
/*     */     public void validate(Object target, Errors errors) {}
/*     */   }
/*     */   
/*     */   protected void addResourceHandlers(ResourceHandlerRegistry registry) {}
/*     */   
/*     */   protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/*     */   
/*     */   protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {}
/*     */   
/*     */   protected void addFormatters(FormatterRegistry registry) {}
/*     */   
/*     */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}
/*     */   
/*     */   protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}
/*     */   
/*     */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*     */   
/*     */   protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/*     */   
/*     */   protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
/*     */   
/*     */   protected void configureViewResolvers(ViewResolverRegistry registry) {}
/*     */   
/*     */   protected void addCorsMappings(CorsRegistry registry) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\WebMvcConfigurationSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */