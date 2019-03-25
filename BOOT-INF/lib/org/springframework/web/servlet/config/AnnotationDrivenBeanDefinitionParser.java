/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanReference;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.format.support.FormattingConversionServiceFactoryBean;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
/*     */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*     */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
/*     */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnnotationDrivenBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/* 152 */   public static final String HANDLER_MAPPING_BEAN_NAME = RequestMappingHandlerMapping.class.getName();
/*     */   
/* 154 */   public static final String HANDLER_ADAPTER_BEAN_NAME = RequestMappingHandlerAdapter.class.getName();
/*     */   
/*     */ 
/*     */   public static final String CONTENT_NEGOTIATION_MANAGER_BEAN_NAME = "mvcContentNegotiationManager";
/*     */   
/* 159 */   private static final boolean javaxValidationPresent = ClassUtils.isPresent("javax.validation.Validator", AnnotationDrivenBeanDefinitionParser.class
/* 160 */     .getClassLoader());
/*     */   
/*     */ 
/* 163 */   private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", AnnotationDrivenBeanDefinitionParser.class
/* 164 */     .getClassLoader());
/*     */   
/*     */ 
/* 167 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", AnnotationDrivenBeanDefinitionParser.class
/* 168 */     .getClassLoader());
/*     */   
/*     */   static {
/* 171 */     if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AnnotationDrivenBeanDefinitionParser.class
/* 172 */       .getClassLoader())) {} }
/* 173 */   private static final boolean jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AnnotationDrivenBeanDefinitionParser.class
/* 174 */     .getClassLoader());
/*     */   
/*     */ 
/* 177 */   private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", AnnotationDrivenBeanDefinitionParser.class
/* 178 */     .getClassLoader());
/*     */   
/*     */ 
/* 181 */   private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", AnnotationDrivenBeanDefinitionParser.class
/* 182 */     .getClassLoader());
/*     */   
/*     */ 
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext)
/*     */   {
/* 187 */     Object source = parserContext.extractSource(element);
/* 188 */     XmlReaderContext readerContext = parserContext.getReaderContext();
/*     */     
/* 190 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/* 191 */     parserContext.pushContainingComponent(compDefinition);
/*     */     
/* 193 */     RuntimeBeanReference contentNegotiationManager = getContentNegotiationManager(element, source, parserContext);
/*     */     
/* 195 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(RequestMappingHandlerMapping.class);
/* 196 */     handlerMappingDef.setSource(source);
/* 197 */     handlerMappingDef.setRole(2);
/* 198 */     handlerMappingDef.getPropertyValues().add("order", Integer.valueOf(0));
/* 199 */     handlerMappingDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/*     */     
/* 201 */     if (element.hasAttribute("enable-matrix-variables")) {
/* 202 */       Boolean enableMatrixVariables = Boolean.valueOf(element.getAttribute("enable-matrix-variables"));
/* 203 */       handlerMappingDef.getPropertyValues().add("removeSemicolonContent", Boolean.valueOf(!enableMatrixVariables.booleanValue()));
/*     */     }
/* 205 */     else if (element.hasAttribute("enableMatrixVariables")) {
/* 206 */       Boolean enableMatrixVariables = Boolean.valueOf(element.getAttribute("enableMatrixVariables"));
/* 207 */       handlerMappingDef.getPropertyValues().add("removeSemicolonContent", Boolean.valueOf(!enableMatrixVariables.booleanValue()));
/*     */     }
/*     */     
/* 210 */     configurePathMatchingProperties(handlerMappingDef, element, parserContext);
/* 211 */     readerContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_BEAN_NAME, handlerMappingDef);
/*     */     
/* 213 */     RuntimeBeanReference corsConfigurationsRef = MvcNamespaceUtils.registerCorsConfigurations(null, parserContext, source);
/* 214 */     handlerMappingDef.getPropertyValues().add("corsConfigurations", corsConfigurationsRef);
/*     */     
/* 216 */     RuntimeBeanReference conversionService = getConversionService(element, source, parserContext);
/* 217 */     RuntimeBeanReference validator = getValidator(element, source, parserContext);
/* 218 */     RuntimeBeanReference messageCodesResolver = getMessageCodesResolver(element);
/*     */     
/* 220 */     RootBeanDefinition bindingDef = new RootBeanDefinition(ConfigurableWebBindingInitializer.class);
/* 221 */     bindingDef.setSource(source);
/* 222 */     bindingDef.setRole(2);
/* 223 */     bindingDef.getPropertyValues().add("conversionService", conversionService);
/* 224 */     bindingDef.getPropertyValues().add("validator", validator);
/* 225 */     bindingDef.getPropertyValues().add("messageCodesResolver", messageCodesResolver);
/*     */     
/* 227 */     ManagedList<?> messageConverters = getMessageConverters(element, source, parserContext);
/* 228 */     ManagedList<?> argumentResolvers = getArgumentResolvers(element, parserContext);
/* 229 */     ManagedList<?> returnValueHandlers = getReturnValueHandlers(element, parserContext);
/* 230 */     String asyncTimeout = getAsyncTimeout(element);
/* 231 */     RuntimeBeanReference asyncExecutor = getAsyncExecutor(element);
/* 232 */     ManagedList<?> callableInterceptors = getCallableInterceptors(element, source, parserContext);
/* 233 */     ManagedList<?> deferredResultInterceptors = getDeferredResultInterceptors(element, source, parserContext);
/*     */     
/* 235 */     RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(RequestMappingHandlerAdapter.class);
/* 236 */     handlerAdapterDef.setSource(source);
/* 237 */     handlerAdapterDef.setRole(2);
/* 238 */     handlerAdapterDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/* 239 */     handlerAdapterDef.getPropertyValues().add("webBindingInitializer", bindingDef);
/* 240 */     handlerAdapterDef.getPropertyValues().add("messageConverters", messageConverters);
/* 241 */     addRequestBodyAdvice(handlerAdapterDef);
/* 242 */     addResponseBodyAdvice(handlerAdapterDef);
/*     */     
/* 244 */     if (element.hasAttribute("ignore-default-model-on-redirect")) {
/* 245 */       Boolean ignoreDefaultModel = Boolean.valueOf(element.getAttribute("ignore-default-model-on-redirect"));
/* 246 */       handlerAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", ignoreDefaultModel);
/*     */     }
/* 248 */     else if (element.hasAttribute("ignoreDefaultModelOnRedirect"))
/*     */     {
/* 250 */       Boolean ignoreDefaultModel = Boolean.valueOf(element.getAttribute("ignoreDefaultModelOnRedirect"));
/* 251 */       handlerAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", ignoreDefaultModel);
/*     */     }
/*     */     
/* 254 */     if (argumentResolvers != null) {
/* 255 */       handlerAdapterDef.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
/*     */     }
/* 257 */     if (returnValueHandlers != null) {
/* 258 */       handlerAdapterDef.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
/*     */     }
/* 260 */     if (asyncTimeout != null) {
/* 261 */       handlerAdapterDef.getPropertyValues().add("asyncRequestTimeout", asyncTimeout);
/*     */     }
/* 263 */     if (asyncExecutor != null) {
/* 264 */       handlerAdapterDef.getPropertyValues().add("taskExecutor", asyncExecutor);
/*     */     }
/*     */     
/* 267 */     handlerAdapterDef.getPropertyValues().add("callableInterceptors", callableInterceptors);
/* 268 */     handlerAdapterDef.getPropertyValues().add("deferredResultInterceptors", deferredResultInterceptors);
/* 269 */     readerContext.getRegistry().registerBeanDefinition(HANDLER_ADAPTER_BEAN_NAME, handlerAdapterDef);
/*     */     
/* 271 */     String uriCompContribName = "mvcUriComponentsContributor";
/* 272 */     RootBeanDefinition uriCompContribDef = new RootBeanDefinition(CompositeUriComponentsContributorFactoryBean.class);
/* 273 */     uriCompContribDef.setSource(source);
/* 274 */     uriCompContribDef.getPropertyValues().addPropertyValue("handlerAdapter", handlerAdapterDef);
/* 275 */     uriCompContribDef.getPropertyValues().addPropertyValue("conversionService", conversionService);
/* 276 */     readerContext.getRegistry().registerBeanDefinition(uriCompContribName, uriCompContribDef);
/*     */     
/* 278 */     RootBeanDefinition csInterceptorDef = new RootBeanDefinition(ConversionServiceExposingInterceptor.class);
/* 279 */     csInterceptorDef.setSource(source);
/* 280 */     csInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, conversionService);
/* 281 */     RootBeanDefinition mappedCsInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/* 282 */     mappedCsInterceptorDef.setSource(source);
/* 283 */     mappedCsInterceptorDef.setRole(2);
/* 284 */     mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, null);
/* 285 */     mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, csInterceptorDef);
/* 286 */     String mappedInterceptorName = readerContext.registerWithGeneratedName(mappedCsInterceptorDef);
/*     */     
/* 288 */     RootBeanDefinition exceptionHandlerExceptionResolver = new RootBeanDefinition(ExceptionHandlerExceptionResolver.class);
/* 289 */     exceptionHandlerExceptionResolver.setSource(source);
/* 290 */     exceptionHandlerExceptionResolver.setRole(2);
/* 291 */     exceptionHandlerExceptionResolver.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/* 292 */     exceptionHandlerExceptionResolver.getPropertyValues().add("messageConverters", messageConverters);
/* 293 */     exceptionHandlerExceptionResolver.getPropertyValues().add("order", Integer.valueOf(0));
/* 294 */     addResponseBodyAdvice(exceptionHandlerExceptionResolver);
/*     */     
/* 296 */     if (argumentResolvers != null) {
/* 297 */       exceptionHandlerExceptionResolver.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
/*     */     }
/* 299 */     if (returnValueHandlers != null) {
/* 300 */       exceptionHandlerExceptionResolver.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
/*     */     }
/*     */     
/* 303 */     String methodExceptionResolverName = readerContext.registerWithGeneratedName(exceptionHandlerExceptionResolver);
/*     */     
/* 305 */     RootBeanDefinition responseStatusExceptionResolver = new RootBeanDefinition(ResponseStatusExceptionResolver.class);
/* 306 */     responseStatusExceptionResolver.setSource(source);
/* 307 */     responseStatusExceptionResolver.setRole(2);
/* 308 */     responseStatusExceptionResolver.getPropertyValues().add("order", Integer.valueOf(1));
/*     */     
/* 310 */     String responseStatusExceptionResolverName = readerContext.registerWithGeneratedName(responseStatusExceptionResolver);
/*     */     
/* 312 */     RootBeanDefinition defaultExceptionResolver = new RootBeanDefinition(DefaultHandlerExceptionResolver.class);
/* 313 */     defaultExceptionResolver.setSource(source);
/* 314 */     defaultExceptionResolver.setRole(2);
/* 315 */     defaultExceptionResolver.getPropertyValues().add("order", Integer.valueOf(2));
/*     */     
/* 317 */     String defaultExceptionResolverName = readerContext.registerWithGeneratedName(defaultExceptionResolver);
/*     */     
/* 319 */     parserContext.registerComponent(new BeanComponentDefinition(handlerMappingDef, HANDLER_MAPPING_BEAN_NAME));
/* 320 */     parserContext.registerComponent(new BeanComponentDefinition(handlerAdapterDef, HANDLER_ADAPTER_BEAN_NAME));
/* 321 */     parserContext.registerComponent(new BeanComponentDefinition(uriCompContribDef, uriCompContribName));
/* 322 */     parserContext.registerComponent(new BeanComponentDefinition(exceptionHandlerExceptionResolver, methodExceptionResolverName));
/* 323 */     parserContext.registerComponent(new BeanComponentDefinition(responseStatusExceptionResolver, responseStatusExceptionResolverName));
/* 324 */     parserContext.registerComponent(new BeanComponentDefinition(defaultExceptionResolver, defaultExceptionResolverName));
/* 325 */     parserContext.registerComponent(new BeanComponentDefinition(mappedCsInterceptorDef, mappedInterceptorName));
/*     */     
/*     */ 
/* 328 */     MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
/*     */     
/* 330 */     parserContext.popAndRegisterContainingComponent();
/*     */     
/* 332 */     return null;
/*     */   }
/*     */   
/*     */   protected void addRequestBodyAdvice(RootBeanDefinition beanDef) {
/* 336 */     if (jackson2Present) {
/* 337 */       beanDef.getPropertyValues().add("requestBodyAdvice", new RootBeanDefinition(JsonViewRequestBodyAdvice.class));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addResponseBodyAdvice(RootBeanDefinition beanDef)
/*     */   {
/* 343 */     if (jackson2Present) {
/* 344 */       beanDef.getPropertyValues().add("responseBodyAdvice", new RootBeanDefinition(JsonViewResponseBodyAdvice.class));
/*     */     }
/*     */   }
/*     */   
/*     */   private RuntimeBeanReference getConversionService(Element element, Object source, ParserContext parserContext) {
/*     */     RuntimeBeanReference conversionServiceRef;
/*     */     RuntimeBeanReference conversionServiceRef;
/* 351 */     if (element.hasAttribute("conversion-service")) {
/* 352 */       conversionServiceRef = new RuntimeBeanReference(element.getAttribute("conversion-service"));
/*     */     }
/*     */     else {
/* 355 */       RootBeanDefinition conversionDef = new RootBeanDefinition(FormattingConversionServiceFactoryBean.class);
/* 356 */       conversionDef.setSource(source);
/* 357 */       conversionDef.setRole(2);
/* 358 */       String conversionName = parserContext.getReaderContext().registerWithGeneratedName(conversionDef);
/* 359 */       parserContext.registerComponent(new BeanComponentDefinition(conversionDef, conversionName));
/* 360 */       conversionServiceRef = new RuntimeBeanReference(conversionName);
/*     */     }
/* 362 */     return conversionServiceRef;
/*     */   }
/*     */   
/*     */   private RuntimeBeanReference getValidator(Element element, Object source, ParserContext parserContext) {
/* 366 */     if (element.hasAttribute("validator")) {
/* 367 */       return new RuntimeBeanReference(element.getAttribute("validator"));
/*     */     }
/* 369 */     if (javaxValidationPresent) {
/* 370 */       RootBeanDefinition validatorDef = new RootBeanDefinition("org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean");
/*     */       
/* 372 */       validatorDef.setSource(source);
/* 373 */       validatorDef.setRole(2);
/* 374 */       String validatorName = parserContext.getReaderContext().registerWithGeneratedName(validatorDef);
/* 375 */       parserContext.registerComponent(new BeanComponentDefinition(validatorDef, validatorName));
/* 376 */       return new RuntimeBeanReference(validatorName);
/*     */     }
/*     */     
/* 379 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private RuntimeBeanReference getContentNegotiationManager(Element element, Object source, ParserContext parserContext)
/*     */   {
/*     */     RuntimeBeanReference beanRef;
/*     */     RuntimeBeanReference beanRef;
/* 387 */     if (element.hasAttribute("content-negotiation-manager")) {
/* 388 */       String name = element.getAttribute("content-negotiation-manager");
/* 389 */       beanRef = new RuntimeBeanReference(name);
/*     */     }
/*     */     else {
/* 392 */       RootBeanDefinition factoryBeanDef = new RootBeanDefinition(ContentNegotiationManagerFactoryBean.class);
/* 393 */       factoryBeanDef.setSource(source);
/* 394 */       factoryBeanDef.setRole(2);
/* 395 */       factoryBeanDef.getPropertyValues().add("mediaTypes", getDefaultMediaTypes());
/*     */       
/* 397 */       String name = "mvcContentNegotiationManager";
/* 398 */       parserContext.getReaderContext().getRegistry().registerBeanDefinition(name, factoryBeanDef);
/* 399 */       parserContext.registerComponent(new BeanComponentDefinition(factoryBeanDef, name));
/* 400 */       beanRef = new RuntimeBeanReference(name);
/*     */     }
/* 402 */     return beanRef;
/*     */   }
/*     */   
/*     */ 
/*     */   private void configurePathMatchingProperties(RootBeanDefinition handlerMappingDef, Element element, ParserContext parserContext)
/*     */   {
/* 408 */     Element pathMatchingElement = DomUtils.getChildElementByTagName(element, "path-matching");
/* 409 */     if (pathMatchingElement != null) {
/* 410 */       Object source = parserContext.extractSource(element);
/* 411 */       if (pathMatchingElement.hasAttribute("suffix-pattern")) {
/* 412 */         Boolean useSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("suffix-pattern"));
/* 413 */         handlerMappingDef.getPropertyValues().add("useSuffixPatternMatch", useSuffixPatternMatch);
/*     */       }
/* 415 */       if (pathMatchingElement.hasAttribute("trailing-slash")) {
/* 416 */         Boolean useTrailingSlashMatch = Boolean.valueOf(pathMatchingElement.getAttribute("trailing-slash"));
/* 417 */         handlerMappingDef.getPropertyValues().add("useTrailingSlashMatch", useTrailingSlashMatch);
/*     */       }
/* 419 */       if (pathMatchingElement.hasAttribute("registered-suffixes-only")) {
/* 420 */         Boolean useRegisteredSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("registered-suffixes-only"));
/* 421 */         handlerMappingDef.getPropertyValues().add("useRegisteredSuffixPatternMatch", useRegisteredSuffixPatternMatch);
/*     */       }
/* 423 */       RuntimeBeanReference pathHelperRef = null;
/* 424 */       if (pathMatchingElement.hasAttribute("path-helper")) {
/* 425 */         pathHelperRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-helper"));
/*     */       }
/* 427 */       pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(pathHelperRef, parserContext, source);
/* 428 */       handlerMappingDef.getPropertyValues().add("urlPathHelper", pathHelperRef);
/*     */       
/* 430 */       RuntimeBeanReference pathMatcherRef = null;
/* 431 */       if (pathMatchingElement.hasAttribute("path-matcher")) {
/* 432 */         pathMatcherRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-matcher"));
/*     */       }
/* 434 */       pathMatcherRef = MvcNamespaceUtils.registerPathMatcher(pathMatcherRef, parserContext, source);
/* 435 */       handlerMappingDef.getPropertyValues().add("pathMatcher", pathMatcherRef);
/*     */     }
/*     */   }
/*     */   
/*     */   private Properties getDefaultMediaTypes() {
/* 440 */     Properties props = new Properties();
/* 441 */     if (romePresent) {
/* 442 */       props.put("atom", "application/atom+xml");
/* 443 */       props.put("rss", "application/rss+xml");
/*     */     }
/* 445 */     if ((jaxb2Present) || (jackson2XmlPresent)) {
/* 446 */       props.put("xml", "application/xml");
/*     */     }
/* 448 */     if ((jackson2Present) || (gsonPresent)) {
/* 449 */       props.put("json", "application/json");
/*     */     }
/* 451 */     return props;
/*     */   }
/*     */   
/*     */   private RuntimeBeanReference getMessageCodesResolver(Element element) {
/* 455 */     if (element.hasAttribute("message-codes-resolver")) {
/* 456 */       return new RuntimeBeanReference(element.getAttribute("message-codes-resolver"));
/*     */     }
/*     */     
/* 459 */     return null;
/*     */   }
/*     */   
/*     */   private String getAsyncTimeout(Element element)
/*     */   {
/* 464 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 465 */     return asyncElement != null ? asyncElement.getAttribute("default-timeout") : null;
/*     */   }
/*     */   
/*     */   private RuntimeBeanReference getAsyncExecutor(Element element) {
/* 469 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 470 */     if ((asyncElement != null) && 
/* 471 */       (asyncElement.hasAttribute("task-executor"))) {
/* 472 */       return new RuntimeBeanReference(asyncElement.getAttribute("task-executor"));
/*     */     }
/*     */     
/* 475 */     return null;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getCallableInterceptors(Element element, Object source, ParserContext parserContext) {
/* 479 */     ManagedList<? super Object> interceptors = new ManagedList();
/* 480 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 481 */     if (asyncElement != null) {
/* 482 */       Element interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "callable-interceptors");
/* 483 */       if (interceptorsElement != null) {
/* 484 */         interceptors.setSource(source);
/* 485 */         for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
/* 486 */           BeanDefinitionHolder beanDef = parserContext.getDelegate().parseBeanDefinitionElement(converter);
/* 487 */           beanDef = parserContext.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef);
/* 488 */           interceptors.add(beanDef);
/*     */         }
/*     */       }
/*     */     }
/* 492 */     return interceptors;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getDeferredResultInterceptors(Element element, Object source, ParserContext parserContext) {
/* 496 */     ManagedList<? super Object> interceptors = new ManagedList();
/* 497 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 498 */     if (asyncElement != null) {
/* 499 */       Element interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "deferred-result-interceptors");
/* 500 */       if (interceptorsElement != null) {
/* 501 */         interceptors.setSource(source);
/* 502 */         for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
/* 503 */           BeanDefinitionHolder beanDef = parserContext.getDelegate().parseBeanDefinitionElement(converter);
/* 504 */           beanDef = parserContext.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef);
/* 505 */           interceptors.add(beanDef);
/*     */         }
/*     */       }
/*     */     }
/* 509 */     return interceptors;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getArgumentResolvers(Element element, ParserContext parserContext) {
/* 513 */     Element resolversElement = DomUtils.getChildElementByTagName(element, "argument-resolvers");
/* 514 */     if (resolversElement != null) {
/* 515 */       ManagedList<Object> resolvers = extractBeanSubElements(resolversElement, parserContext);
/* 516 */       return wrapLegacyResolvers(resolvers, parserContext);
/*     */     }
/* 518 */     return null;
/*     */   }
/*     */   
/*     */   private ManagedList<Object> wrapLegacyResolvers(List<Object> list, ParserContext context) {
/* 522 */     ManagedList<Object> result = new ManagedList();
/* 523 */     for (Object object : list) {
/* 524 */       if ((object instanceof BeanDefinitionHolder)) {
/* 525 */         BeanDefinitionHolder beanDef = (BeanDefinitionHolder)object;
/* 526 */         String className = beanDef.getBeanDefinition().getBeanClassName();
/* 527 */         Class<?> clazz = ClassUtils.resolveClassName(className, context.getReaderContext().getBeanClassLoader());
/* 528 */         if (WebArgumentResolver.class.isAssignableFrom(clazz)) {
/* 529 */           RootBeanDefinition adapter = new RootBeanDefinition(ServletWebArgumentResolverAdapter.class);
/* 530 */           adapter.getConstructorArgumentValues().addIndexedArgumentValue(0, beanDef);
/* 531 */           result.add(new BeanDefinitionHolder(adapter, beanDef.getBeanName() + "Adapter"));
/* 532 */           continue;
/*     */         }
/*     */       }
/* 535 */       result.add(object);
/*     */     }
/* 537 */     return result;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getReturnValueHandlers(Element element, ParserContext parserContext) {
/* 541 */     Element handlers = DomUtils.getChildElementByTagName(element, "return-value-handlers");
/* 542 */     return handlers != null ? extractBeanSubElements(handlers, parserContext) : null;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getMessageConverters(Element element, Object source, ParserContext parserContext) {
/* 546 */     Element convertersElement = DomUtils.getChildElementByTagName(element, "message-converters");
/* 547 */     ManagedList<? super Object> messageConverters = new ManagedList();
/* 548 */     if (convertersElement != null) {
/* 549 */       messageConverters.setSource(source);
/* 550 */       for (Element beanElement : DomUtils.getChildElementsByTagName(convertersElement, new String[] { "bean", "ref" })) {
/* 551 */         Object object = parserContext.getDelegate().parsePropertySubElement(beanElement, null);
/* 552 */         messageConverters.add(object);
/*     */       }
/*     */     }
/*     */     
/* 556 */     if ((convertersElement == null) || (Boolean.valueOf(convertersElement.getAttribute("register-defaults")).booleanValue())) {
/* 557 */       messageConverters.setSource(source);
/* 558 */       messageConverters.add(createConverterDefinition(ByteArrayHttpMessageConverter.class, source));
/*     */       
/* 560 */       RootBeanDefinition stringConverterDef = createConverterDefinition(StringHttpMessageConverter.class, source);
/* 561 */       stringConverterDef.getPropertyValues().add("writeAcceptCharset", Boolean.valueOf(false));
/* 562 */       messageConverters.add(stringConverterDef);
/*     */       
/* 564 */       messageConverters.add(createConverterDefinition(ResourceHttpMessageConverter.class, source));
/* 565 */       messageConverters.add(createConverterDefinition(SourceHttpMessageConverter.class, source));
/* 566 */       messageConverters.add(createConverterDefinition(AllEncompassingFormHttpMessageConverter.class, source));
/*     */       
/* 568 */       if (romePresent) {
/* 569 */         messageConverters.add(createConverterDefinition(AtomFeedHttpMessageConverter.class, source));
/* 570 */         messageConverters.add(createConverterDefinition(RssChannelHttpMessageConverter.class, source));
/*     */       }
/*     */       
/* 573 */       if (jackson2XmlPresent) {
/* 574 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(MappingJackson2XmlHttpMessageConverter.class, source);
/* 575 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 576 */         jacksonFactoryDef.getPropertyValues().add("createXmlMapper", Boolean.valueOf(true));
/* 577 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 578 */         messageConverters.add(jacksonConverterDef);
/*     */       }
/* 580 */       else if (jaxb2Present) {
/* 581 */         messageConverters.add(createConverterDefinition(Jaxb2RootElementHttpMessageConverter.class, source));
/*     */       }
/*     */       
/* 584 */       if (jackson2Present) {
/* 585 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(MappingJackson2HttpMessageConverter.class, source);
/* 586 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 587 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 588 */         messageConverters.add(jacksonConverterDef);
/*     */       }
/* 590 */       else if (gsonPresent) {
/* 591 */         messageConverters.add(createConverterDefinition(GsonHttpMessageConverter.class, source));
/*     */       }
/*     */     }
/* 594 */     return messageConverters;
/*     */   }
/*     */   
/*     */   private GenericBeanDefinition createObjectMapperFactoryDefinition(Object source) {
/* 598 */     GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 599 */     beanDefinition.setBeanClass(Jackson2ObjectMapperFactoryBean.class);
/* 600 */     beanDefinition.setSource(source);
/* 601 */     beanDefinition.setRole(2);
/* 602 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition createConverterDefinition(Class<?> converterClass, Object source) {
/* 606 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(converterClass);
/* 607 */     beanDefinition.setSource(source);
/* 608 */     beanDefinition.setRole(2);
/* 609 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   private ManagedList<Object> extractBeanSubElements(Element parentElement, ParserContext parserContext)
/*     */   {
/* 614 */     ManagedList<Object> list = new ManagedList();
/* 615 */     list.setSource(parserContext.extractSource(parentElement));
/* 616 */     for (Element beanElement : DomUtils.getChildElementsByTagName(parentElement, new String[] { "bean", "ref" })) {
/* 617 */       Object object = parserContext.getDelegate().parsePropertySubElement(beanElement, null);
/* 618 */       list.add(object);
/*     */     }
/* 620 */     return list;
/*     */   }
/*     */   
/*     */   private ManagedList<BeanReference> extractBeanRefSubElements(Element parentElement, ParserContext parserContext) {
/* 624 */     ManagedList<BeanReference> list = new ManagedList();
/* 625 */     list.setSource(parserContext.extractSource(parentElement));
/* 626 */     for (Element refElement : DomUtils.getChildElementsByTagName(parentElement, "ref"))
/*     */     {
/* 628 */       if (StringUtils.hasText("bean")) {
/* 629 */         BeanReference reference = new RuntimeBeanReference(refElement.getAttribute("bean"), false);
/* 630 */         list.add(reference);
/*     */       }
/* 632 */       else if (StringUtils.hasText("parent")) {
/* 633 */         BeanReference reference = new RuntimeBeanReference(refElement.getAttribute("parent"), true);
/* 634 */         list.add(reference);
/*     */       }
/*     */       else {
/* 637 */         parserContext.getReaderContext().error("'bean' or 'parent' attribute is required for <ref> element", parserContext
/* 638 */           .extractSource(parentElement));
/*     */       }
/*     */     }
/* 641 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class CompositeUriComponentsContributorFactoryBean
/*     */     implements FactoryBean<CompositeUriComponentsContributor>, InitializingBean
/*     */   {
/*     */     private RequestMappingHandlerAdapter handlerAdapter;
/*     */     
/*     */ 
/*     */     private ConversionService conversionService;
/*     */     
/*     */ 
/*     */     private CompositeUriComponentsContributor uriComponentsContributor;
/*     */     
/*     */ 
/*     */     public void setHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter)
/*     */     {
/* 660 */       this.handlerAdapter = handlerAdapter;
/*     */     }
/*     */     
/*     */     public void setConversionService(ConversionService conversionService) {
/* 664 */       this.conversionService = conversionService;
/*     */     }
/*     */     
/*     */ 
/*     */     public void afterPropertiesSet()
/*     */     {
/* 670 */       this.uriComponentsContributor = new CompositeUriComponentsContributor(this.handlerAdapter.getArgumentResolvers(), this.conversionService);
/*     */     }
/*     */     
/*     */     public CompositeUriComponentsContributor getObject() throws Exception
/*     */     {
/* 675 */       return this.uriComponentsContributor;
/*     */     }
/*     */     
/*     */     public Class<?> getObjectType()
/*     */     {
/* 680 */       return CompositeUriComponentsContributor.class;
/*     */     }
/*     */     
/*     */     public boolean isSingleton()
/*     */     {
/* 685 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\AnnotationDrivenBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */