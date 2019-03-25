/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.annotation.CrossOrigin;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.handler.MatchableHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.RequestMatchResult;
/*     */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfo.Builder;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfo.BuilderConfiguration;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
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
/*     */ public class RequestMappingHandlerMapping
/*     */   extends RequestMappingInfoHandlerMapping
/*     */   implements MatchableHandlerMapping, EmbeddedValueResolverAware
/*     */ {
/*  58 */   private boolean useSuffixPatternMatch = true;
/*     */   
/*  60 */   private boolean useRegisteredSuffixPatternMatch = false;
/*     */   
/*  62 */   private boolean useTrailingSlashMatch = true;
/*     */   
/*  64 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*     */   
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*  68 */   private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch)
/*     */   {
/*  79 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseRegisteredSuffixPatternMatch(boolean useRegisteredSuffixPatternMatch)
/*     */   {
/*  90 */     this.useRegisteredSuffixPatternMatch = useRegisteredSuffixPatternMatch;
/*  91 */     this.useSuffixPatternMatch = ((useRegisteredSuffixPatternMatch) || (this.useSuffixPatternMatch));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch)
/*     */   {
/* 100 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 108 */     Assert.notNull(contentNegotiationManager, "ContentNegotiationManager must not be null");
/* 109 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*     */   {
/* 114 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */   {
/* 119 */     this.config = new RequestMappingInfo.BuilderConfiguration();
/* 120 */     this.config.setUrlPathHelper(getUrlPathHelper());
/* 121 */     this.config.setPathMatcher(getPathMatcher());
/* 122 */     this.config.setSuffixPatternMatch(this.useSuffixPatternMatch);
/* 123 */     this.config.setTrailingSlashMatch(this.useTrailingSlashMatch);
/* 124 */     this.config.setRegisteredSuffixPatternMatch(this.useRegisteredSuffixPatternMatch);
/* 125 */     this.config.setContentNegotiationManager(getContentNegotiationManager());
/*     */     
/* 127 */     super.afterPropertiesSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean useSuffixPatternMatch()
/*     */   {
/* 135 */     return this.useSuffixPatternMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean useRegisteredSuffixPatternMatch()
/*     */   {
/* 142 */     return this.useRegisteredSuffixPatternMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean useTrailingSlashMatch()
/*     */   {
/* 149 */     return this.useTrailingSlashMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ContentNegotiationManager getContentNegotiationManager()
/*     */   {
/* 156 */     return this.contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<String> getFileExtensions()
/*     */   {
/* 163 */     return this.config.getFileExtensions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isHandler(Class<?> beanType)
/*     */   {
/* 173 */     return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class)) || 
/* 174 */       (AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
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
/*     */   protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType)
/*     */   {
/* 187 */     RequestMappingInfo info = createRequestMappingInfo(method);
/* 188 */     if (info != null) {
/* 189 */       RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
/* 190 */       if (typeInfo != null) {
/* 191 */         info = typeInfo.combine(info);
/*     */       }
/*     */     }
/* 194 */     return info;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element)
/*     */   {
/* 205 */     RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
/*     */     
/* 207 */     RequestCondition<?> condition = (element instanceof Class) ? getCustomTypeCondition((Class)element) : getCustomMethodCondition((Method)element);
/* 208 */     return requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null;
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
/*     */   protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType)
/*     */   {
/* 223 */     return null;
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
/*     */   protected RequestCondition<?> getCustomMethodCondition(Method method)
/*     */   {
/* 238 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition)
/*     */   {
/* 250 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */       RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(requestMapping.path())).methods(requestMapping.method()).params(requestMapping.params()).headers(requestMapping.headers()).consumes(requestMapping.consumes()).produces(requestMapping.produces()).mappingName(requestMapping.name()).customCondition(customCondition).options(this.config).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] resolveEmbeddedValuesInPatterns(String[] patterns)
/*     */   {
/* 268 */     if (this.embeddedValueResolver == null) {
/* 269 */       return patterns;
/*     */     }
/*     */     
/* 272 */     String[] resolvedPatterns = new String[patterns.length];
/* 273 */     for (int i = 0; i < patterns.length; i++) {
/* 274 */       resolvedPatterns[i] = this.embeddedValueResolver.resolveStringValue(patterns[i]);
/*     */     }
/* 276 */     return resolvedPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */   public RequestMatchResult match(HttpServletRequest request, String pattern)
/*     */   {
/* 282 */     RequestMappingInfo info = RequestMappingInfo.paths(new String[] { pattern }).options(this.config).build();
/* 283 */     RequestMappingInfo matchingInfo = info.getMatchingCondition(request);
/* 284 */     if (matchingInfo == null) {
/* 285 */       return null;
/*     */     }
/* 287 */     Set<String> patterns = matchingInfo.getPatternsCondition().getPatterns();
/* 288 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 289 */     return new RequestMatchResult((String)patterns.iterator().next(), lookupPath, getPathMatcher());
/*     */   }
/*     */   
/*     */   protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo)
/*     */   {
/* 294 */     HandlerMethod handlerMethod = createHandlerMethod(handler, method);
/* 295 */     CrossOrigin typeAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), CrossOrigin.class);
/* 296 */     CrossOrigin methodAnnotation = (CrossOrigin)AnnotatedElementUtils.findMergedAnnotation(method, CrossOrigin.class);
/*     */     
/* 298 */     if ((typeAnnotation == null) && (methodAnnotation == null)) {
/* 299 */       return null;
/*     */     }
/*     */     
/* 302 */     CorsConfiguration config = new CorsConfiguration();
/* 303 */     updateCorsConfig(config, typeAnnotation);
/* 304 */     updateCorsConfig(config, methodAnnotation);
/*     */     
/* 306 */     if (CollectionUtils.isEmpty(config.getAllowedMethods())) {
/* 307 */       for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
/* 308 */         config.addAllowedMethod(allowedMethod.name());
/*     */       }
/*     */     }
/* 311 */     return config.applyPermitDefaultValues();
/*     */   }
/*     */   
/*     */   private void updateCorsConfig(CorsConfiguration config, CrossOrigin annotation) {
/* 315 */     if (annotation == null) {
/* 316 */       return;
/*     */     }
/* 318 */     for (String origin : annotation.origins()) {
/* 319 */       config.addAllowedOrigin(resolveCorsAnnotationValue(origin));
/*     */     }
/* 321 */     for (RequestMethod method : annotation.methods()) {
/* 322 */       config.addAllowedMethod(method.name());
/*     */     }
/* 324 */     for (String header : annotation.allowedHeaders()) {
/* 325 */       config.addAllowedHeader(resolveCorsAnnotationValue(header));
/*     */     }
/* 327 */     for (String header : annotation.exposedHeaders()) {
/* 328 */       config.addExposedHeader(resolveCorsAnnotationValue(header));
/*     */     }
/*     */     
/* 331 */     String allowCredentials = resolveCorsAnnotationValue(annotation.allowCredentials());
/* 332 */     if ("true".equalsIgnoreCase(allowCredentials)) {
/* 333 */       config.setAllowCredentials(Boolean.valueOf(true));
/*     */     }
/* 335 */     else if ("false".equalsIgnoreCase(allowCredentials)) {
/* 336 */       config.setAllowCredentials(Boolean.valueOf(false));
/*     */     }
/* 338 */     else if (!allowCredentials.isEmpty()) {
/* 339 */       throw new IllegalStateException("@CrossOrigin's allowCredentials value must be \"true\", \"false\", or an empty string (\"\"): current value is [" + allowCredentials + "]");
/*     */     }
/*     */     
/*     */ 
/* 343 */     if ((annotation.maxAge() >= 0L) && (config.getMaxAge() == null)) {
/* 344 */       config.setMaxAge(Long.valueOf(annotation.maxAge()));
/*     */     }
/*     */   }
/*     */   
/*     */   private String resolveCorsAnnotationValue(String value) {
/* 349 */     return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestMappingHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */