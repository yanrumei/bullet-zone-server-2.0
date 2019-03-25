/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.web.filter.OrderedHiddenHttpMethodFilter;
/*     */ import org.springframework.boot.web.filter.OrderedHttpPutFormContentFilter;
/*     */ import org.springframework.boot.web.filter.OrderedRequestContextFilter;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.context.annotation.Primary;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatter;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.DefaultMessageCodesResolver;
/*     */ import org.springframework.validation.MessageCodesResolver;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.ContentNegotiationStrategy;
/*     */ import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.RequestContextListener;
/*     */ import org.springframework.web.filter.HiddenHttpMethodFilter;
/*     */ import org.springframework.web.filter.HttpPutFormContentFilter;
/*     */ import org.springframework.web.filter.RequestContextFilter;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
/*     */ import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
/*     */ import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
/*     */ import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
/*     */ import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
/*     */ import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
/*     */ import org.springframework.web.servlet.i18n.FixedLocaleResolver;
/*     */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
/*     */ import org.springframework.web.servlet.resource.GzipResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*     */ import org.springframework.web.servlet.resource.ResourceResolver;
/*     */ import org.springframework.web.servlet.resource.VersionResourceResolver;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class})
/*     */ @ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
/*     */ @AutoConfigureOrder(-2147483638)
/*     */ @AutoConfigureAfter({DispatcherServletAutoConfiguration.class, ValidationAutoConfiguration.class})
/*     */ public class WebMvcAutoConfiguration
/*     */ {
/*     */   public static final String DEFAULT_PREFIX = "";
/*     */   public static final String DEFAULT_SUFFIX = "";
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({HiddenHttpMethodFilter.class})
/*     */   public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter()
/*     */   {
/* 142 */     return new OrderedHiddenHttpMethodFilter();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({HttpPutFormContentFilter.class})
/*     */   @ConditionalOnProperty(prefix="spring.mvc.formcontent.putfilter", name={"enabled"}, matchIfMissing=true)
/*     */   public OrderedHttpPutFormContentFilter httpPutFormContentFilter() {
/* 149 */     return new OrderedHttpPutFormContentFilter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @Import({WebMvcAutoConfiguration.EnableWebMvcConfiguration.class})
/*     */   @EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
/*     */   public static class WebMvcAutoConfigurationAdapter
/*     */     extends WebMvcConfigurerAdapter
/*     */   {
/* 160 */     private static final Log logger = LogFactory.getLog(WebMvcConfigurerAdapter.class);
/*     */     
/*     */ 
/*     */     private final ResourceProperties resourceProperties;
/*     */     
/*     */     private final WebMvcProperties mvcProperties;
/*     */     
/*     */     private final ListableBeanFactory beanFactory;
/*     */     
/*     */     private final HttpMessageConverters messageConverters;
/*     */     
/*     */     final WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer resourceHandlerRegistrationCustomizer;
/*     */     
/*     */ 
/*     */     public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties, ListableBeanFactory beanFactory, @Lazy HttpMessageConverters messageConverters, ObjectProvider<WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider)
/*     */     {
/* 176 */       this.resourceProperties = resourceProperties;
/* 177 */       this.mvcProperties = mvcProperties;
/* 178 */       this.beanFactory = beanFactory;
/* 179 */       this.messageConverters = messageConverters;
/*     */       
/* 181 */       this.resourceHandlerRegistrationCustomizer = ((WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer)resourceHandlerRegistrationCustomizerProvider.getIfAvailable());
/*     */     }
/*     */     
/*     */     public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
/*     */     {
/* 186 */       converters.addAll(this.messageConverters.getConverters());
/*     */     }
/*     */     
/*     */     public void configureAsyncSupport(AsyncSupportConfigurer configurer)
/*     */     {
/* 191 */       Long timeout = this.mvcProperties.getAsync().getRequestTimeout();
/* 192 */       if (timeout != null) {
/* 193 */         configurer.setDefaultTimeout(timeout.longValue());
/*     */       }
/*     */     }
/*     */     
/*     */     public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
/*     */     {
/* 199 */       Map<String, MediaType> mediaTypes = this.mvcProperties.getMediaTypes();
/* 200 */       for (Map.Entry<String, MediaType> mediaType : mediaTypes.entrySet()) {
/* 201 */         configurer.mediaType((String)mediaType.getKey(), (MediaType)mediaType.getValue());
/*     */       }
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public InternalResourceViewResolver defaultViewResolver() {
/* 208 */       InternalResourceViewResolver resolver = new InternalResourceViewResolver();
/* 209 */       resolver.setPrefix(this.mvcProperties.getView().getPrefix());
/* 210 */       resolver.setSuffix(this.mvcProperties.getView().getSuffix());
/* 211 */       return resolver;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnBean({View.class})
/*     */     @ConditionalOnMissingBean
/*     */     public BeanNameViewResolver beanNameViewResolver() {
/* 218 */       BeanNameViewResolver resolver = new BeanNameViewResolver();
/* 219 */       resolver.setOrder(2147483637);
/* 220 */       return resolver;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnBean({ViewResolver.class})
/*     */     @ConditionalOnMissingBean(name={"viewResolver"}, value={ContentNegotiatingViewResolver.class})
/*     */     public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
/* 227 */       ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
/* 228 */       resolver.setContentNegotiationManager(
/* 229 */         (ContentNegotiationManager)beanFactory.getBean(ContentNegotiationManager.class));
/*     */       
/*     */ 
/* 232 */       resolver.setOrder(Integer.MIN_VALUE);
/* 233 */       return resolver;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnProperty(prefix="spring.mvc", name={"locale"})
/*     */     public LocaleResolver localeResolver()
/*     */     {
/* 241 */       if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
/* 242 */         return new FixedLocaleResolver(this.mvcProperties.getLocale());
/*     */       }
/* 244 */       AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
/* 245 */       localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
/* 246 */       return localeResolver;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnProperty(prefix="spring.mvc", name={"date-format"})
/*     */     public Formatter<Date> dateFormatter() {
/* 252 */       return new DateFormatter(this.mvcProperties.getDateFormat());
/*     */     }
/*     */     
/*     */     public MessageCodesResolver getMessageCodesResolver()
/*     */     {
/* 257 */       if (this.mvcProperties.getMessageCodesResolverFormat() != null) {
/* 258 */         DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
/* 259 */         resolver.setMessageCodeFormatter(this.mvcProperties
/* 260 */           .getMessageCodesResolverFormat());
/* 261 */         return resolver;
/*     */       }
/* 263 */       return null;
/*     */     }
/*     */     
/*     */     public void addFormatters(FormatterRegistry registry)
/*     */     {
/* 268 */       for (Converter<?, ?> converter : getBeansOfType(Converter.class)) {
/* 269 */         registry.addConverter(converter);
/*     */       }
/* 271 */       for (GenericConverter converter : getBeansOfType(GenericConverter.class)) {
/* 272 */         registry.addConverter(converter);
/*     */       }
/* 274 */       for (Formatter<?> formatter : getBeansOfType(Formatter.class)) {
/* 275 */         registry.addFormatter(formatter);
/*     */       }
/*     */     }
/*     */     
/*     */     private <T> Collection<T> getBeansOfType(Class<T> type) {
/* 280 */       return this.beanFactory.getBeansOfType(type).values();
/*     */     }
/*     */     
/*     */     public void addResourceHandlers(ResourceHandlerRegistry registry)
/*     */     {
/* 285 */       if (!this.resourceProperties.isAddMappings()) {
/* 286 */         logger.debug("Default resource handling disabled");
/* 287 */         return;
/*     */       }
/* 289 */       Integer cachePeriod = this.resourceProperties.getCachePeriod();
/* 290 */       if (!registry.hasMappingForPattern("/webjars/**")) {
/* 291 */         customizeResourceHandlerRegistration(registry
/* 292 */           .addResourceHandler(new String[] { "/webjars/**" })
/* 293 */           .addResourceLocations(new String[] { "classpath:/META-INF/resources/webjars/" })
/*     */           
/* 295 */           .setCachePeriod(cachePeriod));
/*     */       }
/* 297 */       String staticPathPattern = this.mvcProperties.getStaticPathPattern();
/* 298 */       if (!registry.hasMappingForPattern(staticPathPattern)) {
/* 299 */         customizeResourceHandlerRegistration(registry
/* 300 */           .addResourceHandler(new String[] { staticPathPattern })
/* 301 */           .addResourceLocations(this.resourceProperties
/* 302 */           .getStaticLocations())
/* 303 */           .setCachePeriod(cachePeriod));
/*     */       }
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public WebMvcAutoConfiguration.WelcomePageHandlerMapping welcomePageHandlerMapping(ResourceProperties resourceProperties)
/*     */     {
/* 310 */       return new WebMvcAutoConfiguration.WelcomePageHandlerMapping(resourceProperties.getWelcomePage(), this.mvcProperties
/* 311 */         .getStaticPathPattern(), null);
/*     */     }
/*     */     
/*     */     private void customizeResourceHandlerRegistration(ResourceHandlerRegistration registration)
/*     */     {
/* 316 */       if (this.resourceHandlerRegistrationCustomizer != null) {
/* 317 */         this.resourceHandlerRegistrationCustomizer.customize(registration);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({RequestContextListener.class, RequestContextFilter.class})
/*     */     public static RequestContextFilter requestContextFilter()
/*     */     {
/* 326 */       return new OrderedRequestContextFilter();
/*     */     }
/*     */     
/*     */     @Configuration
/*     */     @ConditionalOnProperty(value={"spring.mvc.favicon.enabled"}, matchIfMissing=true)
/*     */     public static class FaviconConfiguration
/*     */     {
/*     */       private final ResourceProperties resourceProperties;
/*     */       
/*     */       public FaviconConfiguration(ResourceProperties resourceProperties) {
/* 336 */         this.resourceProperties = resourceProperties;
/*     */       }
/*     */       
/*     */       @Bean
/*     */       public SimpleUrlHandlerMapping faviconHandlerMapping() {
/* 341 */         SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
/* 342 */         mapping.setOrder(-2147483647);
/* 343 */         mapping.setUrlMap(Collections.singletonMap("**/favicon.ico", 
/* 344 */           faviconRequestHandler()));
/* 345 */         return mapping;
/*     */       }
/*     */       
/*     */       @Bean
/*     */       public ResourceHttpRequestHandler faviconRequestHandler() {
/* 350 */         ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
/* 351 */         requestHandler
/* 352 */           .setLocations(this.resourceProperties.getFaviconLocations());
/* 353 */         return requestHandler;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   public static class EnableWebMvcConfiguration
/*     */     extends DelegatingWebMvcConfiguration
/*     */   {
/*     */     private final WebMvcProperties mvcProperties;
/*     */     
/*     */ 
/*     */     private final ListableBeanFactory beanFactory;
/*     */     
/*     */ 
/*     */     private final WebMvcRegistrations mvcRegistrations;
/*     */     
/*     */ 
/*     */ 
/*     */     public EnableWebMvcConfiguration(ObjectProvider<WebMvcProperties> mvcPropertiesProvider, ObjectProvider<WebMvcRegistrations> mvcRegistrationsProvider, ListableBeanFactory beanFactory)
/*     */     {
/* 376 */       this.mvcProperties = ((WebMvcProperties)mvcPropertiesProvider.getIfAvailable());
/* 377 */       this.mvcRegistrations = ((WebMvcRegistrations)mvcRegistrationsProvider.getIfUnique());
/* 378 */       this.beanFactory = beanFactory;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public RequestMappingHandlerAdapter requestMappingHandlerAdapter()
/*     */     {
/* 384 */       RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
/* 385 */       adapter.setIgnoreDefaultModelOnRedirect(this.mvcProperties == null ? true : this.mvcProperties
/* 386 */         .isIgnoreDefaultModelOnRedirect());
/* 387 */       return adapter;
/*     */     }
/*     */     
/*     */     protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter()
/*     */     {
/* 392 */       if ((this.mvcRegistrations != null) && 
/* 393 */         (this.mvcRegistrations.getRequestMappingHandlerAdapter() != null)) {
/* 394 */         return this.mvcRegistrations.getRequestMappingHandlerAdapter();
/*     */       }
/* 396 */       return super.createRequestMappingHandlerAdapter();
/*     */     }
/*     */     
/*     */ 
/*     */     @Bean
/*     */     @Primary
/*     */     public RequestMappingHandlerMapping requestMappingHandlerMapping()
/*     */     {
/* 404 */       return super.requestMappingHandlerMapping();
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public Validator mvcValidator()
/*     */     {
/* 410 */       if (!ClassUtils.isPresent("javax.validation.Validator", 
/* 411 */         getClass().getClassLoader())) {
/* 412 */         return super.mvcValidator();
/*     */       }
/* 414 */       return WebMvcValidator.get(getApplicationContext(), getValidator());
/*     */     }
/*     */     
/*     */     protected RequestMappingHandlerMapping createRequestMappingHandlerMapping()
/*     */     {
/* 419 */       if ((this.mvcRegistrations != null) && 
/* 420 */         (this.mvcRegistrations.getRequestMappingHandlerMapping() != null)) {
/* 421 */         return this.mvcRegistrations.getRequestMappingHandlerMapping();
/*     */       }
/* 423 */       return super.createRequestMappingHandlerMapping();
/*     */     }
/*     */     
/*     */     protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer()
/*     */     {
/*     */       try {
/* 429 */         return (ConfigurableWebBindingInitializer)this.beanFactory.getBean(ConfigurableWebBindingInitializer.class);
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException ex) {}
/* 432 */       return super.getConfigurableWebBindingInitializer();
/*     */     }
/*     */     
/*     */ 
/*     */     protected ExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver()
/*     */     {
/* 438 */       if ((this.mvcRegistrations != null) && 
/* 439 */         (this.mvcRegistrations.getExceptionHandlerExceptionResolver() != null)) {
/* 440 */         return this.mvcRegistrations.getExceptionHandlerExceptionResolver();
/*     */       }
/* 442 */       return super.createExceptionHandlerExceptionResolver();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers)
/*     */     {
/* 448 */       super.configureHandlerExceptionResolvers(exceptionResolvers);
/* 449 */       if (exceptionResolvers.isEmpty()) {
/* 450 */         addDefaultHandlerExceptionResolvers(exceptionResolvers);
/*     */       }
/* 452 */       if (this.mvcProperties.isLogResolvedException()) {
/* 453 */         for (HandlerExceptionResolver resolver : exceptionResolvers) {
/* 454 */           if ((resolver instanceof AbstractHandlerExceptionResolver))
/*     */           {
/* 456 */             ((AbstractHandlerExceptionResolver)resolver).setWarnLogCategory(resolver.getClass().getName());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public ContentNegotiationManager mvcContentNegotiationManager()
/*     */     {
/* 465 */       ContentNegotiationManager manager = super.mvcContentNegotiationManager();
/* 466 */       List<ContentNegotiationStrategy> strategies = manager.getStrategies();
/* 467 */       ListIterator<ContentNegotiationStrategy> iterator = strategies.listIterator();
/* 468 */       while (iterator.hasNext()) {
/* 469 */         ContentNegotiationStrategy strategy = (ContentNegotiationStrategy)iterator.next();
/* 470 */         if ((strategy instanceof PathExtensionContentNegotiationStrategy)) {
/* 471 */           iterator.set(new WebMvcAutoConfiguration.OptionalPathExtensionContentNegotiationStrategy(strategy));
/*     */         }
/*     */       }
/*     */       
/* 475 */       return manager;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnEnabledResourceChain
/*     */   static class ResourceChainCustomizerConfiguration
/*     */   {
/*     */     @Bean
/*     */     public WebMvcAutoConfiguration.ResourceChainResourceHandlerRegistrationCustomizer resourceHandlerRegistrationCustomizer()
/*     */     {
/* 486 */       return new WebMvcAutoConfiguration.ResourceChainResourceHandlerRegistrationCustomizer(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static abstract interface ResourceHandlerRegistrationCustomizer
/*     */   {
/*     */     public abstract void customize(ResourceHandlerRegistration paramResourceHandlerRegistration);
/*     */   }
/*     */   
/*     */   private static class ResourceChainResourceHandlerRegistrationCustomizer
/*     */     implements WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer
/*     */   {
/*     */     @Autowired
/* 500 */     private ResourceProperties resourceProperties = new ResourceProperties();
/*     */     
/*     */ 
/*     */     public void customize(ResourceHandlerRegistration registration)
/*     */     {
/* 505 */       ResourceProperties.Chain properties = this.resourceProperties.getChain();
/* 506 */       configureResourceChain(properties, registration
/* 507 */         .resourceChain(properties.isCache()));
/*     */     }
/*     */     
/*     */     private void configureResourceChain(ResourceProperties.Chain properties, ResourceChainRegistration chain)
/*     */     {
/* 512 */       ResourceProperties.Strategy strategy = properties.getStrategy();
/* 513 */       if ((strategy.getFixed().isEnabled()) || (strategy.getContent().isEnabled())) {
/* 514 */         chain.addResolver(getVersionResourceResolver(strategy));
/*     */       }
/* 516 */       if (properties.isGzipped()) {
/* 517 */         chain.addResolver(new GzipResourceResolver());
/*     */       }
/* 519 */       if (properties.isHtmlApplicationCache()) {
/* 520 */         chain.addTransformer(new AppCacheManifestTransformer());
/*     */       }
/*     */     }
/*     */     
/*     */     private ResourceResolver getVersionResourceResolver(ResourceProperties.Strategy properties)
/*     */     {
/* 526 */       VersionResourceResolver resolver = new VersionResourceResolver();
/* 527 */       if (properties.getFixed().isEnabled()) {
/* 528 */         String version = properties.getFixed().getVersion();
/* 529 */         String[] paths = properties.getFixed().getPaths();
/* 530 */         resolver.addFixedVersionStrategy(version, paths);
/*     */       }
/* 532 */       if (properties.getContent().isEnabled()) {
/* 533 */         String[] paths = properties.getContent().getPaths();
/* 534 */         resolver.addContentVersionStrategy(paths);
/*     */       }
/* 536 */       return resolver;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class WelcomePageHandlerMapping
/*     */     extends AbstractUrlHandlerMapping
/*     */   {
/* 544 */     private static final Log logger = LogFactory.getLog(WelcomePageHandlerMapping.class);
/*     */     
/*     */     private WelcomePageHandlerMapping(Resource welcomePage, String staticPathPattern)
/*     */     {
/* 548 */       if ((welcomePage != null) && ("/**".equals(staticPathPattern))) {
/* 549 */         logger.info("Adding welcome page: " + welcomePage);
/* 550 */         ParameterizableViewController controller = new ParameterizableViewController();
/* 551 */         controller.setViewName("forward:index.html");
/* 552 */         setRootHandler(controller);
/* 553 */         setOrder(0);
/*     */       }
/*     */     }
/*     */     
/*     */     public Object getHandlerInternal(HttpServletRequest request) throws Exception
/*     */     {
/* 559 */       for (MediaType mediaType : getAcceptedMediaTypes(request)) {
/* 560 */         if (mediaType.includes(MediaType.TEXT_HTML)) {
/* 561 */           return super.getHandlerInternal(request);
/*     */         }
/*     */       }
/* 564 */       return null;
/*     */     }
/*     */     
/*     */     private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
/* 568 */       String acceptHeader = request.getHeader("Accept");
/* 569 */       return MediaType.parseMediaTypes(
/* 570 */         StringUtils.hasText(acceptHeader) ? acceptHeader : "*/*");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class OptionalPathExtensionContentNegotiationStrategy
/*     */     implements ContentNegotiationStrategy
/*     */   {
/* 582 */     private static final String SKIP_ATTRIBUTE = PathExtensionContentNegotiationStrategy.class
/* 583 */       .getName() + ".SKIP";
/*     */     
/*     */     private final ContentNegotiationStrategy delegate;
/*     */     
/*     */     OptionalPathExtensionContentNegotiationStrategy(ContentNegotiationStrategy delegate)
/*     */     {
/* 589 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest)
/*     */       throws HttpMediaTypeNotAcceptableException
/*     */     {
/* 595 */       Object skip = webRequest.getAttribute(SKIP_ATTRIBUTE, 0);
/*     */       
/* 597 */       if ((skip != null) && (Boolean.parseBoolean(skip.toString()))) {
/* 598 */         return Collections.emptyList();
/*     */       }
/* 600 */       return this.delegate.resolveMediaTypes(webRequest);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebMvcAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */