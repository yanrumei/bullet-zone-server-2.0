/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.cache.concurrent.ConcurrentMapCache;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.CachingResourceResolver;
/*     */ import org.springframework.web.servlet.resource.CachingResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.ContentVersionStrategy;
/*     */ import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.FixedVersionStrategy;
/*     */ import org.springframework.web.servlet.resource.PathResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProvider;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
/*     */ import org.springframework.web.servlet.resource.VersionResourceResolver;
/*     */ import org.springframework.web.servlet.resource.WebJarsResourceResolver;
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
/*     */ class ResourcesBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String RESOURCE_CHAIN_CACHE = "spring-resource-chain-cache";
/*     */   private static final String VERSION_RESOLVER_ELEMENT = "version-resolver";
/*     */   private static final String VERSION_STRATEGY_ELEMENT = "version-strategy";
/*     */   private static final String FIXED_VERSION_STRATEGY_ELEMENT = "fixed-version-strategy";
/*     */   private static final String CONTENT_VERSION_STRATEGY_ELEMENT = "content-version-strategy";
/*     */   private static final String RESOURCE_URL_PROVIDER = "mvcResourceUrlProvider";
/*  83 */   private static final boolean isWebJarsAssetLocatorPresent = ClassUtils.isPresent("org.webjars.WebJarAssetLocator", ResourcesBeanDefinitionParser.class
/*  84 */     .getClassLoader());
/*     */   
/*     */ 
/*     */   public BeanDefinition parse(Element element, ParserContext context)
/*     */   {
/*  89 */     Object source = context.extractSource(element);
/*     */     
/*  91 */     registerUrlProvider(context, source);
/*     */     
/*  93 */     RuntimeBeanReference pathMatcherRef = MvcNamespaceUtils.registerPathMatcher(null, context, source);
/*  94 */     RuntimeBeanReference pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(null, context, source);
/*     */     
/*  96 */     String resourceHandlerName = registerResourceHandler(context, element, pathHelperRef, source);
/*  97 */     if (resourceHandlerName == null) {
/*  98 */       return null;
/*     */     }
/*     */     
/* 101 */     Map<String, String> urlMap = new ManagedMap();
/* 102 */     String resourceRequestPath = element.getAttribute("mapping");
/* 103 */     if (!StringUtils.hasText(resourceRequestPath)) {
/* 104 */       context.getReaderContext().error("The 'mapping' attribute is required.", context.extractSource(element));
/* 105 */       return null;
/*     */     }
/* 107 */     urlMap.put(resourceRequestPath, resourceHandlerName);
/*     */     
/* 109 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
/* 110 */     handlerMappingDef.setSource(source);
/* 111 */     handlerMappingDef.setRole(2);
/* 112 */     handlerMappingDef.getPropertyValues().add("urlMap", urlMap);
/* 113 */     handlerMappingDef.getPropertyValues().add("pathMatcher", pathMatcherRef).add("urlPathHelper", pathHelperRef);
/*     */     
/* 115 */     String orderValue = element.getAttribute("order");
/*     */     
/* 117 */     Object order = StringUtils.hasText(orderValue) ? orderValue : Integer.valueOf(2147483646);
/* 118 */     handlerMappingDef.getPropertyValues().add("order", order);
/*     */     
/* 120 */     RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
/* 121 */     handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
/*     */     
/* 123 */     String beanName = context.getReaderContext().generateBeanName(handlerMappingDef);
/* 124 */     context.getRegistry().registerBeanDefinition(beanName, handlerMappingDef);
/* 125 */     context.registerComponent(new BeanComponentDefinition(handlerMappingDef, beanName));
/*     */     
/*     */ 
/*     */ 
/* 129 */     MvcNamespaceUtils.registerDefaultComponents(context, source);
/*     */     
/* 131 */     return null;
/*     */   }
/*     */   
/*     */   private void registerUrlProvider(ParserContext context, Object source) {
/* 135 */     if (!context.getRegistry().containsBeanDefinition("mvcResourceUrlProvider")) {
/* 136 */       RootBeanDefinition urlProvider = new RootBeanDefinition(ResourceUrlProvider.class);
/* 137 */       urlProvider.setSource(source);
/* 138 */       urlProvider.setRole(2);
/* 139 */       context.getRegistry().registerBeanDefinition("mvcResourceUrlProvider", urlProvider);
/* 140 */       context.registerComponent(new BeanComponentDefinition(urlProvider, "mvcResourceUrlProvider"));
/*     */       
/* 142 */       RootBeanDefinition interceptor = new RootBeanDefinition(ResourceUrlProviderExposingInterceptor.class);
/* 143 */       interceptor.setSource(source);
/* 144 */       interceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, urlProvider);
/*     */       
/* 146 */       RootBeanDefinition mappedInterceptor = new RootBeanDefinition(MappedInterceptor.class);
/* 147 */       mappedInterceptor.setSource(source);
/* 148 */       mappedInterceptor.setRole(2);
/* 149 */       mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(0, null);
/* 150 */       mappedInterceptor.getConstructorArgumentValues().addIndexedArgumentValue(1, interceptor);
/* 151 */       String mappedInterceptorName = context.getReaderContext().registerWithGeneratedName(mappedInterceptor);
/* 152 */       context.registerComponent(new BeanComponentDefinition(mappedInterceptor, mappedInterceptorName));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String registerResourceHandler(ParserContext context, Element element, RuntimeBeanReference pathHelperRef, Object source)
/*     */   {
/* 159 */     String locationAttr = element.getAttribute("location");
/* 160 */     if (!StringUtils.hasText(locationAttr)) {
/* 161 */       context.getReaderContext().error("The 'location' attribute is required.", context.extractSource(element));
/* 162 */       return null;
/*     */     }
/*     */     
/* 165 */     RootBeanDefinition resourceHandlerDef = new RootBeanDefinition(ResourceHttpRequestHandler.class);
/* 166 */     resourceHandlerDef.setSource(source);
/* 167 */     resourceHandlerDef.setRole(2);
/*     */     
/* 169 */     MutablePropertyValues values = resourceHandlerDef.getPropertyValues();
/* 170 */     values.add("urlPathHelper", pathHelperRef);
/* 171 */     values.add("locationValues", StringUtils.commaDelimitedListToStringArray(locationAttr));
/*     */     
/* 173 */     String cacheSeconds = element.getAttribute("cache-period");
/* 174 */     if (StringUtils.hasText(cacheSeconds)) {
/* 175 */       values.add("cacheSeconds", cacheSeconds);
/*     */     }
/*     */     
/* 178 */     Element cacheControlElement = DomUtils.getChildElementByTagName(element, "cache-control");
/* 179 */     if (cacheControlElement != null) {
/* 180 */       CacheControl cacheControl = parseCacheControl(cacheControlElement);
/* 181 */       values.add("cacheControl", cacheControl);
/*     */     }
/*     */     
/* 184 */     Element resourceChainElement = DomUtils.getChildElementByTagName(element, "resource-chain");
/* 185 */     if (resourceChainElement != null) {
/* 186 */       parseResourceChain(resourceHandlerDef, context, resourceChainElement, source);
/*     */     }
/*     */     
/* 189 */     Object manager = MvcNamespaceUtils.getContentNegotiationManager(context);
/* 190 */     if (manager != null) {
/* 191 */       values.add("contentNegotiationManager", manager);
/*     */     }
/*     */     
/* 194 */     String beanName = context.getReaderContext().generateBeanName(resourceHandlerDef);
/* 195 */     context.getRegistry().registerBeanDefinition(beanName, resourceHandlerDef);
/* 196 */     context.registerComponent(new BeanComponentDefinition(resourceHandlerDef, beanName));
/* 197 */     return beanName;
/*     */   }
/*     */   
/*     */   private CacheControl parseCacheControl(Element element)
/*     */   {
/* 202 */     CacheControl cacheControl = CacheControl.empty();
/* 203 */     if ("true".equals(element.getAttribute("no-cache"))) {
/* 204 */       cacheControl = CacheControl.noCache();
/*     */     }
/* 206 */     else if ("true".equals(element.getAttribute("no-store"))) {
/* 207 */       cacheControl = CacheControl.noStore();
/*     */     }
/* 209 */     else if (element.hasAttribute("max-age")) {
/* 210 */       cacheControl = CacheControl.maxAge(Long.parseLong(element.getAttribute("max-age")), TimeUnit.SECONDS);
/*     */     }
/* 212 */     if ("true".equals(element.getAttribute("must-revalidate"))) {
/* 213 */       cacheControl = cacheControl.mustRevalidate();
/*     */     }
/* 215 */     if ("true".equals(element.getAttribute("no-transform"))) {
/* 216 */       cacheControl = cacheControl.noTransform();
/*     */     }
/* 218 */     if ("true".equals(element.getAttribute("cache-public"))) {
/* 219 */       cacheControl = cacheControl.cachePublic();
/*     */     }
/* 221 */     if ("true".equals(element.getAttribute("cache-private"))) {
/* 222 */       cacheControl = cacheControl.cachePrivate();
/*     */     }
/* 224 */     if ("true".equals(element.getAttribute("proxy-revalidate"))) {
/* 225 */       cacheControl = cacheControl.proxyRevalidate();
/*     */     }
/* 227 */     if (element.hasAttribute("s-maxage")) {
/* 228 */       cacheControl = cacheControl.sMaxAge(Long.parseLong(element.getAttribute("s-maxage")), TimeUnit.SECONDS);
/*     */     }
/* 230 */     if (element.hasAttribute("stale-while-revalidate")) {
/* 231 */       cacheControl = cacheControl.staleWhileRevalidate(
/* 232 */         Long.parseLong(element.getAttribute("stale-while-revalidate")), TimeUnit.SECONDS);
/*     */     }
/* 234 */     if (element.hasAttribute("stale-if-error")) {
/* 235 */       cacheControl = cacheControl.staleIfError(
/* 236 */         Long.parseLong(element.getAttribute("stale-if-error")), TimeUnit.SECONDS);
/*     */     }
/* 238 */     return cacheControl;
/*     */   }
/*     */   
/*     */ 
/*     */   private void parseResourceChain(RootBeanDefinition resourceHandlerDef, ParserContext context, Element element, Object source)
/*     */   {
/* 244 */     String autoRegistration = element.getAttribute("auto-registration");
/* 245 */     boolean isAutoRegistration = (!StringUtils.hasText(autoRegistration)) || (!"false".equals(autoRegistration));
/*     */     
/* 247 */     ManagedList<? super Object> resourceResolvers = new ManagedList();
/* 248 */     resourceResolvers.setSource(source);
/* 249 */     ManagedList<? super Object> resourceTransformers = new ManagedList();
/* 250 */     resourceTransformers.setSource(source);
/*     */     
/* 252 */     parseResourceCache(resourceResolvers, resourceTransformers, element, source);
/* 253 */     parseResourceResolversTransformers(isAutoRegistration, resourceResolvers, resourceTransformers, context, element, source);
/*     */     
/*     */ 
/* 256 */     if (!resourceResolvers.isEmpty()) {
/* 257 */       resourceHandlerDef.getPropertyValues().add("resourceResolvers", resourceResolvers);
/*     */     }
/* 259 */     if (!resourceTransformers.isEmpty()) {
/* 260 */       resourceHandlerDef.getPropertyValues().add("resourceTransformers", resourceTransformers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void parseResourceCache(ManagedList<? super Object> resourceResolvers, ManagedList<? super Object> resourceTransformers, Element element, Object source)
/*     */   {
/* 267 */     String resourceCache = element.getAttribute("resource-cache");
/* 268 */     if ("true".equals(resourceCache)) {
/* 269 */       ConstructorArgumentValues cargs = new ConstructorArgumentValues();
/*     */       
/* 271 */       RootBeanDefinition cachingResolverDef = new RootBeanDefinition(CachingResourceResolver.class);
/* 272 */       cachingResolverDef.setSource(source);
/* 273 */       cachingResolverDef.setRole(2);
/* 274 */       cachingResolverDef.setConstructorArgumentValues(cargs);
/*     */       
/* 276 */       RootBeanDefinition cachingTransformerDef = new RootBeanDefinition(CachingResourceTransformer.class);
/* 277 */       cachingTransformerDef.setSource(source);
/* 278 */       cachingTransformerDef.setRole(2);
/* 279 */       cachingTransformerDef.setConstructorArgumentValues(cargs);
/*     */       
/* 281 */       String cacheManagerName = element.getAttribute("cache-manager");
/* 282 */       String cacheName = element.getAttribute("cache-name");
/* 283 */       if ((StringUtils.hasText(cacheManagerName)) && (StringUtils.hasText(cacheName))) {
/* 284 */         RuntimeBeanReference cacheManagerRef = new RuntimeBeanReference(cacheManagerName);
/* 285 */         cargs.addIndexedArgumentValue(0, cacheManagerRef);
/* 286 */         cargs.addIndexedArgumentValue(1, cacheName);
/*     */       }
/*     */       else {
/* 289 */         ConstructorArgumentValues cacheCavs = new ConstructorArgumentValues();
/* 290 */         cacheCavs.addIndexedArgumentValue(0, "spring-resource-chain-cache");
/* 291 */         RootBeanDefinition cacheDef = new RootBeanDefinition(ConcurrentMapCache.class);
/* 292 */         cacheDef.setSource(source);
/* 293 */         cacheDef.setRole(2);
/* 294 */         cacheDef.setConstructorArgumentValues(cacheCavs);
/* 295 */         cargs.addIndexedArgumentValue(0, cacheDef);
/*     */       }
/* 297 */       resourceResolvers.add(cachingResolverDef);
/* 298 */       resourceTransformers.add(cachingTransformerDef);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void parseResourceResolversTransformers(boolean isAutoRegistration, ManagedList<? super Object> resourceResolvers, ManagedList<? super Object> resourceTransformers, ParserContext context, Element element, Object source)
/*     */   {
/* 306 */     Element resolversElement = DomUtils.getChildElementByTagName(element, "resolvers");
/* 307 */     Iterator localIterator; if (resolversElement != null) {
/* 308 */       for (localIterator = DomUtils.getChildElements(resolversElement).iterator(); localIterator.hasNext();) { beanElement = (Element)localIterator.next();
/* 309 */         if ("version-resolver".equals(beanElement.getLocalName())) {
/* 310 */           RootBeanDefinition versionResolverDef = parseVersionResolver(context, beanElement, source);
/* 311 */           versionResolverDef.setSource(source);
/* 312 */           resourceResolvers.add(versionResolverDef);
/* 313 */           if (isAutoRegistration) {
/* 314 */             RootBeanDefinition cssLinkTransformerDef = new RootBeanDefinition(CssLinkResourceTransformer.class);
/* 315 */             cssLinkTransformerDef.setSource(source);
/* 316 */             cssLinkTransformerDef.setRole(2);
/* 317 */             resourceTransformers.add(cssLinkTransformerDef);
/*     */           }
/*     */         }
/*     */         else {
/* 321 */           Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 322 */           resourceResolvers.add(object);
/*     */         }
/*     */       }
/*     */     }
/*     */     Element beanElement;
/* 327 */     if (isAutoRegistration) {
/* 328 */       if (isWebJarsAssetLocatorPresent) {
/* 329 */         RootBeanDefinition webJarsResolverDef = new RootBeanDefinition(WebJarsResourceResolver.class);
/* 330 */         webJarsResolverDef.setSource(source);
/* 331 */         webJarsResolverDef.setRole(2);
/* 332 */         resourceResolvers.add(webJarsResolverDef);
/*     */       }
/* 334 */       RootBeanDefinition pathResolverDef = new RootBeanDefinition(PathResourceResolver.class);
/* 335 */       pathResolverDef.setSource(source);
/* 336 */       pathResolverDef.setRole(2);
/* 337 */       resourceResolvers.add(pathResolverDef);
/*     */     }
/*     */     
/* 340 */     Element transformersElement = DomUtils.getChildElementByTagName(element, "transformers");
/* 341 */     if (transformersElement != null) {
/* 342 */       for (Element beanElement : DomUtils.getChildElementsByTagName(transformersElement, new String[] { "bean", "ref" })) {
/* 343 */         Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 344 */         resourceTransformers.add(object);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private RootBeanDefinition parseVersionResolver(ParserContext context, Element element, Object source) {
/* 350 */     ManagedMap<String, ? super Object> strategyMap = new ManagedMap();
/* 351 */     strategyMap.setSource(source);
/* 352 */     RootBeanDefinition versionResolverDef = new RootBeanDefinition(VersionResourceResolver.class);
/* 353 */     versionResolverDef.setSource(source);
/* 354 */     versionResolverDef.setRole(2);
/* 355 */     versionResolverDef.getPropertyValues().addPropertyValue("strategyMap", strategyMap);
/*     */     
/* 357 */     for (Element beanElement : DomUtils.getChildElements(element)) {
/* 358 */       String[] patterns = StringUtils.commaDelimitedListToStringArray(beanElement.getAttribute("patterns"));
/* 359 */       Object strategy = null;
/* 360 */       if ("fixed-version-strategy".equals(beanElement.getLocalName())) {
/* 361 */         ConstructorArgumentValues cargs = new ConstructorArgumentValues();
/* 362 */         cargs.addIndexedArgumentValue(0, beanElement.getAttribute("version"));
/* 363 */         strategyDef = new RootBeanDefinition(FixedVersionStrategy.class);
/* 364 */         strategyDef.setSource(source);
/* 365 */         strategyDef.setRole(2);
/* 366 */         strategyDef.setConstructorArgumentValues(cargs);
/* 367 */         strategy = strategyDef;
/*     */       }
/* 369 */       else if ("content-version-strategy".equals(beanElement.getLocalName())) {
/* 370 */         RootBeanDefinition strategyDef = new RootBeanDefinition(ContentVersionStrategy.class);
/* 371 */         strategyDef.setSource(source);
/* 372 */         strategyDef.setRole(2);
/* 373 */         strategy = strategyDef;
/*     */       }
/* 375 */       else if ("version-strategy".equals(beanElement.getLocalName())) {
/* 376 */         childElement = (Element)DomUtils.getChildElementsByTagName(beanElement, new String[] { "bean", "ref" }).get(0);
/* 377 */         strategy = context.getDelegate().parsePropertySubElement(childElement, null);
/*     */       }
/* 379 */       Element childElement = patterns;RootBeanDefinition strategyDef = childElement.length; for (RootBeanDefinition localRootBeanDefinition1 = 0; localRootBeanDefinition1 < strategyDef; localRootBeanDefinition1++) { String pattern = childElement[localRootBeanDefinition1];
/* 380 */         strategyMap.put(pattern, strategy);
/*     */       }
/*     */     }
/*     */     
/* 384 */     return versionResolverDef;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\ResourcesBeanDefinitionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */