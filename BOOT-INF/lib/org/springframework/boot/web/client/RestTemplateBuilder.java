/*     */ package org.springframework.boot.web.client;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.client.AbstractClientHttpRequestFactoryWrapper;
/*     */ import org.springframework.http.client.ClientHttpRequestFactory;
/*     */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*     */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
/*     */ import org.springframework.http.client.support.BasicAuthorizationInterceptor;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.client.ResponseErrorHandler;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ import org.springframework.web.util.UriTemplateHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RestTemplateBuilder
/*     */ {
/*     */   private static final Map<String, String> REQUEST_FACTORY_CANDIDATES;
/*     */   private final boolean detectRequestFactory;
/*     */   private final String rootUri;
/*     */   private final Set<HttpMessageConverter<?>> messageConverters;
/*     */   private final ClientHttpRequestFactory requestFactory;
/*     */   private final UriTemplateHandler uriTemplateHandler;
/*     */   private final ResponseErrorHandler errorHandler;
/*     */   private final BasicAuthorizationInterceptor basicAuthorization;
/*     */   private final Set<RestTemplateCustomizer> restTemplateCustomizers;
/*     */   private final Set<RequestFactoryCustomizer> requestFactoryCustomizers;
/*     */   private final Set<ClientHttpRequestInterceptor> interceptors;
/*     */   
/*     */   static
/*     */   {
/*  69 */     Map<String, String> candidates = new LinkedHashMap();
/*  70 */     candidates.put("org.apache.http.client.HttpClient", "org.springframework.http.client.HttpComponentsClientHttpRequestFactory");
/*     */     
/*  72 */     candidates.put("okhttp3.OkHttpClient", "org.springframework.http.client.OkHttp3ClientHttpRequestFactory");
/*     */     
/*  74 */     candidates.put("com.squareup.okhttp.OkHttpClient", "org.springframework.http.client.OkHttpClientHttpRequestFactory");
/*     */     
/*  76 */     candidates.put("io.netty.channel.EventLoopGroup", "org.springframework.http.client.Netty4ClientHttpRequestFactory");
/*     */     
/*  78 */     REQUEST_FACTORY_CANDIDATES = Collections.unmodifiableMap(candidates);
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
/*     */   public RestTemplateBuilder(RestTemplateCustomizer... customizers)
/*     */   {
/* 107 */     Assert.notNull(customizers, "Customizers must not be null");
/* 108 */     this.detectRequestFactory = true;
/* 109 */     this.rootUri = null;
/* 110 */     this.messageConverters = null;
/* 111 */     this.requestFactory = null;
/* 112 */     this.uriTemplateHandler = null;
/* 113 */     this.errorHandler = null;
/* 114 */     this.basicAuthorization = null;
/* 115 */     this.restTemplateCustomizers = Collections.unmodifiableSet(new LinkedHashSet(
/* 116 */       Arrays.asList(customizers)));
/* 117 */     this.requestFactoryCustomizers = Collections.emptySet();
/* 118 */     this.interceptors = Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RestTemplateBuilder(boolean detectRequestFactory, String rootUri, Set<HttpMessageConverter<?>> messageConverters, ClientHttpRequestFactory requestFactory, UriTemplateHandler uriTemplateHandler, ResponseErrorHandler errorHandler, BasicAuthorizationInterceptor basicAuthorization, Set<RestTemplateCustomizer> restTemplateCustomizers, Set<RequestFactoryCustomizer> requestFactoryCustomizers, Set<ClientHttpRequestInterceptor> interceptors)
/*     */   {
/* 130 */     this.detectRequestFactory = detectRequestFactory;
/* 131 */     this.rootUri = rootUri;
/* 132 */     this.messageConverters = messageConverters;
/* 133 */     this.requestFactory = requestFactory;
/* 134 */     this.uriTemplateHandler = uriTemplateHandler;
/* 135 */     this.errorHandler = errorHandler;
/* 136 */     this.basicAuthorization = basicAuthorization;
/* 137 */     this.restTemplateCustomizers = restTemplateCustomizers;
/* 138 */     this.requestFactoryCustomizers = requestFactoryCustomizers;
/* 139 */     this.interceptors = interceptors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplateBuilder detectRequestFactory(boolean detectRequestFactory)
/*     */   {
/* 150 */     return new RestTemplateBuilder(detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder rootUri(String rootUri)
/*     */   {
/* 163 */     return new RestTemplateBuilder(this.detectRequestFactory, rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder messageConverters(HttpMessageConverter<?>... messageConverters)
/*     */   {
/* 179 */     Assert.notNull(messageConverters, "MessageConverters must not be null");
/* 180 */     return messageConverters(Arrays.asList(messageConverters));
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
/*     */   public RestTemplateBuilder messageConverters(Collection<? extends HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 193 */     Assert.notNull(messageConverters, "MessageConverters must not be null");
/* 194 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, 
/* 195 */       Collections.unmodifiableSet(new LinkedHashSet(messageConverters)), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder additionalMessageConverters(HttpMessageConverter<?>... messageConverters)
/*     */   {
/* 211 */     Assert.notNull(messageConverters, "MessageConverters must not be null");
/* 212 */     return additionalMessageConverters(Arrays.asList(messageConverters));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplateBuilder additionalMessageConverters(Collection<? extends HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 224 */     Assert.notNull(messageConverters, "MessageConverters must not be null");
/* 225 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, 
/* 226 */       append(this.messageConverters, messageConverters), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder defaultMessageConverters()
/*     */   {
/* 240 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, 
/* 241 */       Collections.unmodifiableSet(new LinkedHashSet(new RestTemplate()
/* 242 */       .getMessageConverters())), this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder interceptors(ClientHttpRequestInterceptor... interceptors)
/*     */   {
/* 259 */     Assert.notNull(interceptors, "interceptors must not be null");
/* 260 */     return interceptors(Arrays.asList(interceptors));
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
/*     */   public RestTemplateBuilder interceptors(Collection<ClientHttpRequestInterceptor> interceptors)
/*     */   {
/* 274 */     Assert.notNull(interceptors, "interceptors must not be null");
/* 275 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, 
/*     */     
/*     */ 
/* 278 */       Collections.unmodifiableSet(new LinkedHashSet(interceptors)));
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
/*     */   public RestTemplateBuilder additionalInterceptors(ClientHttpRequestInterceptor... interceptors)
/*     */   {
/* 292 */     Assert.notNull(interceptors, "interceptors must not be null");
/* 293 */     return additionalInterceptors(Arrays.asList(interceptors));
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
/*     */   public RestTemplateBuilder additionalInterceptors(Collection<? extends ClientHttpRequestInterceptor> interceptors)
/*     */   {
/* 306 */     Assert.notNull(interceptors, "interceptors must not be null");
/* 307 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, 
/*     */     
/*     */ 
/* 310 */       append(this.interceptors, interceptors));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplateBuilder requestFactory(Class<? extends ClientHttpRequestFactory> requestFactory)
/*     */   {
/* 321 */     Assert.notNull(requestFactory, "RequestFactory must not be null");
/* 322 */     return requestFactory(createRequestFactory(requestFactory));
/*     */   }
/*     */   
/*     */   private ClientHttpRequestFactory createRequestFactory(Class<? extends ClientHttpRequestFactory> requestFactory)
/*     */   {
/*     */     try {
/* 328 */       Constructor<?> constructor = requestFactory.getDeclaredConstructor(new Class[0]);
/* 329 */       constructor.setAccessible(true);
/* 330 */       return (ClientHttpRequestFactory)constructor.newInstance(new Object[0]);
/*     */     }
/*     */     catch (Exception ex) {
/* 333 */       throw new IllegalStateException(ex);
/*     */     }
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
/*     */   public RestTemplateBuilder requestFactory(ClientHttpRequestFactory requestFactory)
/*     */   {
/* 347 */     Assert.notNull(requestFactory, "RequestFactory must not be null");
/* 348 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder uriTemplateHandler(UriTemplateHandler uriTemplateHandler)
/*     */   {
/* 361 */     Assert.notNull(uriTemplateHandler, "UriTemplateHandler must not be null");
/* 362 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder errorHandler(ResponseErrorHandler errorHandler)
/*     */   {
/* 375 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/* 376 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, errorHandler, this.basicAuthorization, this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder basicAuthorization(String username, String password)
/*     */   {
/* 390 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, new BasicAuthorizationInterceptor(username, password), this.restTemplateCustomizers, this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder customizers(RestTemplateCustomizer... restTemplateCustomizers)
/*     */   {
/* 408 */     Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
/*     */     
/* 410 */     return customizers(Arrays.asList(restTemplateCustomizers));
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
/*     */   public RestTemplateBuilder customizers(Collection<? extends RestTemplateCustomizer> restTemplateCustomizers)
/*     */   {
/* 424 */     Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
/*     */     
/* 426 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, 
/*     */     
/*     */ 
/* 429 */       Collections.unmodifiableSet(new LinkedHashSet(restTemplateCustomizers)), this.requestFactoryCustomizers, this.interceptors);
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
/*     */   public RestTemplateBuilder additionalCustomizers(RestTemplateCustomizer... restTemplateCustomizers)
/*     */   {
/* 444 */     Assert.notNull(restTemplateCustomizers, "RestTemplateCustomizers must not be null");
/*     */     
/* 446 */     return additionalCustomizers(Arrays.asList(restTemplateCustomizers));
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
/*     */   public RestTemplateBuilder additionalCustomizers(Collection<? extends RestTemplateCustomizer> customizers)
/*     */   {
/* 459 */     Assert.notNull(customizers, "RestTemplateCustomizers must not be null");
/* 460 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, 
/*     */     
/*     */ 
/* 463 */       append(this.restTemplateCustomizers, customizers), this.requestFactoryCustomizers, this.interceptors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplateBuilder setConnectTimeout(int connectTimeout)
/*     */   {
/* 474 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, 
/*     */     
/*     */ 
/* 477 */       append(this.requestFactoryCustomizers, new ConnectTimeoutRequestFactoryCustomizer(connectTimeout)), this.interceptors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplateBuilder setReadTimeout(int readTimeout)
/*     */   {
/* 489 */     return new RestTemplateBuilder(this.detectRequestFactory, this.rootUri, this.messageConverters, this.requestFactory, this.uriTemplateHandler, this.errorHandler, this.basicAuthorization, this.restTemplateCustomizers, 
/*     */     
/*     */ 
/* 492 */       append(this.requestFactoryCustomizers, new ReadTimeoutRequestFactoryCustomizer(readTimeout)), this.interceptors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RestTemplate build()
/*     */   {
/* 504 */     return build(RestTemplate.class);
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
/*     */   public <T extends RestTemplate> T build(Class<T> restTemplateClass)
/*     */   {
/* 518 */     return configure((RestTemplate)BeanUtils.instantiate(restTemplateClass));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends RestTemplate> T configure(T restTemplate)
/*     */   {
/* 530 */     configureRequestFactory(restTemplate);
/* 531 */     if (!CollectionUtils.isEmpty(this.messageConverters)) {
/* 532 */       restTemplate.setMessageConverters(new ArrayList(this.messageConverters));
/*     */     }
/*     */     
/* 535 */     if (this.uriTemplateHandler != null) {
/* 536 */       restTemplate.setUriTemplateHandler(this.uriTemplateHandler);
/*     */     }
/* 538 */     if (this.errorHandler != null) {
/* 539 */       restTemplate.setErrorHandler(this.errorHandler);
/*     */     }
/* 541 */     if (this.rootUri != null) {
/* 542 */       RootUriTemplateHandler.addTo(restTemplate, this.rootUri);
/*     */     }
/* 544 */     if (this.basicAuthorization != null) {
/* 545 */       restTemplate.getInterceptors().add(this.basicAuthorization);
/*     */     }
/* 547 */     if (!CollectionUtils.isEmpty(this.restTemplateCustomizers)) {
/* 548 */       for (RestTemplateCustomizer customizer : this.restTemplateCustomizers) {
/* 549 */         customizer.customize(restTemplate);
/*     */       }
/*     */     }
/* 552 */     restTemplate.getInterceptors().addAll(this.interceptors);
/* 553 */     return restTemplate;
/*     */   }
/*     */   
/*     */   private void configureRequestFactory(RestTemplate restTemplate) {
/* 557 */     ClientHttpRequestFactory requestFactory = null;
/* 558 */     if (this.requestFactory != null) {
/* 559 */       requestFactory = this.requestFactory;
/*     */     }
/* 561 */     else if (this.detectRequestFactory) {
/* 562 */       requestFactory = detectRequestFactory();
/*     */     }
/* 564 */     if (requestFactory != null) {
/* 565 */       ClientHttpRequestFactory unwrappedRequestFactory = unwrapRequestFactoryIfNecessary(requestFactory);
/*     */       
/* 567 */       for (RequestFactoryCustomizer customizer : this.requestFactoryCustomizers) {
/* 568 */         customizer.customize(unwrappedRequestFactory);
/*     */       }
/* 570 */       restTemplate.setRequestFactory(requestFactory);
/*     */     }
/*     */   }
/*     */   
/*     */   private ClientHttpRequestFactory unwrapRequestFactoryIfNecessary(ClientHttpRequestFactory requestFactory)
/*     */   {
/* 576 */     if (!(requestFactory instanceof AbstractClientHttpRequestFactoryWrapper)) {
/* 577 */       return requestFactory;
/*     */     }
/* 579 */     ClientHttpRequestFactory unwrappedRequestFactory = requestFactory;
/* 580 */     Field field = ReflectionUtils.findField(AbstractClientHttpRequestFactoryWrapper.class, "requestFactory");
/*     */     
/* 582 */     ReflectionUtils.makeAccessible(field);
/*     */     do
/*     */     {
/* 585 */       unwrappedRequestFactory = (ClientHttpRequestFactory)ReflectionUtils.getField(field, unwrappedRequestFactory);
/*     */     }
/* 587 */     while ((unwrappedRequestFactory instanceof AbstractClientHttpRequestFactoryWrapper));
/* 588 */     return unwrappedRequestFactory;
/*     */   }
/*     */   
/*     */   private ClientHttpRequestFactory detectRequestFactory() {
/* 592 */     for (Map.Entry<String, String> candidate : REQUEST_FACTORY_CANDIDATES
/* 593 */       .entrySet()) {
/* 594 */       ClassLoader classLoader = getClass().getClassLoader();
/* 595 */       if (ClassUtils.isPresent((String)candidate.getKey(), classLoader)) {
/* 596 */         Class<?> factoryClass = ClassUtils.resolveClassName((String)candidate.getValue(), classLoader);
/*     */         
/*     */ 
/* 599 */         ClientHttpRequestFactory requestFactory = (ClientHttpRequestFactory)BeanUtils.instantiate(factoryClass);
/* 600 */         initializeIfNecessary(requestFactory);
/* 601 */         return requestFactory;
/*     */       }
/*     */     }
/* 604 */     return new SimpleClientHttpRequestFactory();
/*     */   }
/*     */   
/*     */   private void initializeIfNecessary(ClientHttpRequestFactory requestFactory) {
/* 608 */     if ((requestFactory instanceof InitializingBean)) {
/*     */       try {
/* 610 */         ((InitializingBean)requestFactory).afterPropertiesSet();
/*     */       }
/*     */       catch (Exception ex) {
/* 613 */         throw new IllegalStateException("Failed to initialize request factory " + requestFactory, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private <T> Set<T> append(Set<T> set, T addition)
/*     */   {
/* 621 */     Set<T> result = new LinkedHashSet(set == null ? Collections.emptySet() : set);
/* 622 */     result.add(addition);
/* 623 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */   private <T> Set<T> append(Set<T> set, Collection<? extends T> additions)
/*     */   {
/* 628 */     Set<T> result = new LinkedHashSet(set == null ? Collections.emptySet() : set);
/* 629 */     result.addAll(additions);
/* 630 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static abstract interface RequestFactoryCustomizer
/*     */   {
/*     */     public abstract void customize(ClientHttpRequestFactory paramClientHttpRequestFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static abstract class TimeoutRequestFactoryCustomizer
/*     */     implements RestTemplateBuilder.RequestFactoryCustomizer
/*     */   {
/*     */     private final int timeout;
/*     */     
/*     */ 
/*     */     private final String methodName;
/*     */     
/*     */ 
/*     */     TimeoutRequestFactoryCustomizer(int timeout, String methodName)
/*     */     {
/* 653 */       this.timeout = timeout;
/* 654 */       this.methodName = methodName;
/*     */     }
/*     */     
/*     */     public void customize(ClientHttpRequestFactory factory)
/*     */     {
/* 659 */       ReflectionUtils.invokeMethod(findMethod(factory), factory, new Object[] { Integer.valueOf(this.timeout) });
/*     */     }
/*     */     
/*     */     private Method findMethod(ClientHttpRequestFactory factory) {
/* 663 */       Method method = ReflectionUtils.findMethod(factory.getClass(), this.methodName, new Class[] { Integer.TYPE });
/*     */       
/* 665 */       if (method != null) {
/* 666 */         return method;
/*     */       }
/* 668 */       throw new IllegalStateException("Request factory " + factory.getClass() + " does not have a " + this.methodName + "(int) method");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ReadTimeoutRequestFactoryCustomizer
/*     */     extends RestTemplateBuilder.TimeoutRequestFactoryCustomizer
/*     */   {
/*     */     ReadTimeoutRequestFactoryCustomizer(int readTimeout)
/*     */     {
/* 681 */       super("setReadTimeout");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConnectTimeoutRequestFactoryCustomizer
/*     */     extends RestTemplateBuilder.TimeoutRequestFactoryCustomizer
/*     */   {
/*     */     ConnectTimeoutRequestFactoryCustomizer(int connectTimeout)
/*     */     {
/* 693 */       super("setConnectTimeout");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\client\RestTemplateBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */