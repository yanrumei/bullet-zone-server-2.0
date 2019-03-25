/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ public abstract class AbstractUrlHandlerMapping
/*     */   extends AbstractHandlerMapping
/*     */   implements MatchableHandlerMapping
/*     */ {
/*     */   private Object rootHandler;
/*  56 */   private boolean useTrailingSlashMatch = false;
/*     */   
/*  58 */   private boolean lazyInitHandlers = false;
/*     */   
/*  60 */   private final Map<String, Object> handlerMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRootHandler(Object rootHandler)
/*     */   {
/*  69 */     this.rootHandler = rootHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getRootHandler()
/*     */   {
/*  77 */     return this.rootHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch)
/*     */   {
/*  86 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean useTrailingSlashMatch()
/*     */   {
/*  93 */     return this.useTrailingSlashMatch;
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
/*     */   public void setLazyInitHandlers(boolean lazyInitHandlers)
/*     */   {
/* 107 */     this.lazyInitHandlers = lazyInitHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getHandlerInternal(HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 117 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 118 */     Object handler = lookupHandler(lookupPath, request);
/* 119 */     if (handler == null)
/*     */     {
/*     */ 
/* 122 */       Object rawHandler = null;
/* 123 */       if ("/".equals(lookupPath)) {
/* 124 */         rawHandler = getRootHandler();
/*     */       }
/* 126 */       if (rawHandler == null) {
/* 127 */         rawHandler = getDefaultHandler();
/*     */       }
/* 129 */       if (rawHandler != null)
/*     */       {
/* 131 */         if ((rawHandler instanceof String)) {
/* 132 */           String handlerName = (String)rawHandler;
/* 133 */           rawHandler = getApplicationContext().getBean(handlerName);
/*     */         }
/* 135 */         validateHandler(rawHandler, request);
/* 136 */         handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, null);
/*     */       }
/*     */     }
/* 139 */     if ((handler != null) && (this.logger.isDebugEnabled())) {
/* 140 */       this.logger.debug("Mapping [" + lookupPath + "] to " + handler);
/*     */     }
/* 142 */     else if ((handler == null) && (this.logger.isTraceEnabled())) {
/* 143 */       this.logger.trace("No handler mapping found for [" + lookupPath + "]");
/*     */     }
/* 145 */     return handler;
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
/*     */   protected Object lookupHandler(String urlPath, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 163 */     Object handler = this.handlerMap.get(urlPath);
/* 164 */     if (handler != null)
/*     */     {
/* 166 */       if ((handler instanceof String)) {
/* 167 */         String handlerName = (String)handler;
/* 168 */         handler = getApplicationContext().getBean(handlerName);
/*     */       }
/* 170 */       validateHandler(handler, request);
/* 171 */       return buildPathExposingHandler(handler, urlPath, urlPath, null);
/*     */     }
/*     */     
/*     */ 
/* 175 */     List<String> matchingPatterns = new ArrayList();
/* 176 */     for (String registeredPattern : this.handlerMap.keySet()) {
/* 177 */       if (getPathMatcher().match(registeredPattern, urlPath)) {
/* 178 */         matchingPatterns.add(registeredPattern);
/*     */       }
/* 180 */       else if ((useTrailingSlashMatch()) && 
/* 181 */         (!registeredPattern.endsWith("/")) && (getPathMatcher().match(registeredPattern + "/", urlPath))) {
/* 182 */         matchingPatterns.add(registeredPattern + "/");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 187 */     String bestMatch = null;
/* 188 */     Comparator<String> patternComparator = getPathMatcher().getPatternComparator(urlPath);
/* 189 */     if (!matchingPatterns.isEmpty()) {
/* 190 */       Collections.sort(matchingPatterns, patternComparator);
/* 191 */       if (this.logger.isDebugEnabled()) {
/* 192 */         this.logger.debug("Matching patterns for request [" + urlPath + "] are " + matchingPatterns);
/*     */       }
/* 194 */       bestMatch = (String)matchingPatterns.get(0);
/*     */     }
/* 196 */     if (bestMatch != null) {
/* 197 */       handler = this.handlerMap.get(bestMatch);
/* 198 */       if (handler == null) {
/* 199 */         if (bestMatch.endsWith("/")) {
/* 200 */           handler = this.handlerMap.get(bestMatch.substring(0, bestMatch.length() - 1));
/*     */         }
/* 202 */         if (handler == null) {
/* 203 */           throw new IllegalStateException("Could not find handler for best pattern match [" + bestMatch + "]");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 208 */       if ((handler instanceof String)) {
/* 209 */         String handlerName = (String)handler;
/* 210 */         handler = getApplicationContext().getBean(handlerName);
/*     */       }
/* 212 */       validateHandler(handler, request);
/* 213 */       String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestMatch, urlPath);
/*     */       
/*     */ 
/*     */ 
/* 217 */       Map<String, String> uriTemplateVariables = new LinkedHashMap();
/* 218 */       for (String matchingPattern : matchingPatterns) {
/* 219 */         if (patternComparator.compare(bestMatch, matchingPattern) == 0) {
/* 220 */           Map<String, String> vars = getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath);
/* 221 */           Map<String, String> decodedVars = getUrlPathHelper().decodePathVariables(request, vars);
/* 222 */           uriTemplateVariables.putAll(decodedVars);
/*     */         }
/*     */       }
/* 225 */       if (this.logger.isDebugEnabled()) {
/* 226 */         this.logger.debug("URI Template variables for request [" + urlPath + "] are " + uriTemplateVariables);
/*     */       }
/* 228 */       return buildPathExposingHandler(handler, bestMatch, pathWithinMapping, uriTemplateVariables);
/*     */     }
/*     */     
/*     */ 
/* 232 */     return null;
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
/*     */   protected void validateHandler(Object handler, HttpServletRequest request)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object buildPathExposingHandler(Object rawHandler, String bestMatchingPattern, String pathWithinMapping, Map<String, String> uriTemplateVariables)
/*     */   {
/* 260 */     HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler);
/* 261 */     chain.addInterceptor(new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
/* 262 */     if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
/* 263 */       chain.addInterceptor(new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
/*     */     }
/* 265 */     return chain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposePathWithinMapping(String bestMatchingPattern, String pathWithinMapping, HttpServletRequest request)
/*     */   {
/* 275 */     request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestMatchingPattern);
/* 276 */     request.setAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeUriTemplateVariables(Map<String, String> uriTemplateVariables, HttpServletRequest request)
/*     */   {
/* 286 */     request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
/*     */   }
/*     */   
/*     */   public RequestMatchResult match(HttpServletRequest request, String pattern)
/*     */   {
/* 291 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 292 */     if (getPathMatcher().match(pattern, lookupPath)) {
/* 293 */       return new RequestMatchResult(pattern, lookupPath, getPathMatcher());
/*     */     }
/* 295 */     if ((useTrailingSlashMatch()) && 
/* 296 */       (!pattern.endsWith("/")) && (getPathMatcher().match(pattern + "/", lookupPath))) {
/* 297 */       return new RequestMatchResult(pattern + "/", lookupPath, getPathMatcher());
/*     */     }
/*     */     
/* 300 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerHandler(String[] urlPaths, String beanName)
/*     */     throws BeansException, IllegalStateException
/*     */   {
/* 311 */     Assert.notNull(urlPaths, "URL path array must not be null");
/* 312 */     for (String urlPath : urlPaths) {
/* 313 */       registerHandler(urlPath, beanName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void registerHandler(String urlPath, Object handler)
/*     */     throws BeansException, IllegalStateException
/*     */   {
/* 326 */     Assert.notNull(urlPath, "URL path must not be null");
/* 327 */     Assert.notNull(handler, "Handler object must not be null");
/* 328 */     Object resolvedHandler = handler;
/*     */     
/*     */ 
/* 331 */     if ((!this.lazyInitHandlers) && ((handler instanceof String))) {
/* 332 */       String handlerName = (String)handler;
/* 333 */       if (getApplicationContext().isSingleton(handlerName)) {
/* 334 */         resolvedHandler = getApplicationContext().getBean(handlerName);
/*     */       }
/*     */     }
/*     */     
/* 338 */     Object mappedHandler = this.handlerMap.get(urlPath);
/* 339 */     if (mappedHandler != null) {
/* 340 */       if (mappedHandler != resolvedHandler)
/*     */       {
/*     */ 
/* 343 */         throw new IllegalStateException("Cannot map " + getHandlerDescription(handler) + " to URL path [" + urlPath + "]: There is already " + getHandlerDescription(mappedHandler) + " mapped.");
/*     */       }
/*     */       
/*     */     }
/* 347 */     else if (urlPath.equals("/")) {
/* 348 */       if (this.logger.isInfoEnabled()) {
/* 349 */         this.logger.info("Root mapping to " + getHandlerDescription(handler));
/*     */       }
/* 351 */       setRootHandler(resolvedHandler);
/*     */     }
/* 353 */     else if (urlPath.equals("/*")) {
/* 354 */       if (this.logger.isInfoEnabled()) {
/* 355 */         this.logger.info("Default mapping to " + getHandlerDescription(handler));
/*     */       }
/* 357 */       setDefaultHandler(resolvedHandler);
/*     */     }
/*     */     else {
/* 360 */       this.handlerMap.put(urlPath, resolvedHandler);
/* 361 */       if (this.logger.isInfoEnabled()) {
/* 362 */         this.logger.info("Mapped URL path [" + urlPath + "] onto " + getHandlerDescription(handler));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String getHandlerDescription(Object handler)
/*     */   {
/* 369 */     return "handler " + ((handler instanceof String) ? "'" + handler + "'" : new StringBuilder().append("of type [").append(handler.getClass()).append("]").toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Map<String, Object> getHandlerMap()
/*     */   {
/* 380 */     return Collections.unmodifiableMap(this.handlerMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean supportsTypeLevelMappings()
/*     */   {
/* 387 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class PathExposingHandlerInterceptor
/*     */     extends HandlerInterceptorAdapter
/*     */   {
/*     */     private final String bestMatchingPattern;
/*     */     
/*     */ 
/*     */     private final String pathWithinMapping;
/*     */     
/*     */ 
/*     */     public PathExposingHandlerInterceptor(String bestMatchingPattern, String pathWithinMapping)
/*     */     {
/* 403 */       this.bestMatchingPattern = bestMatchingPattern;
/* 404 */       this.pathWithinMapping = pathWithinMapping;
/*     */     }
/*     */     
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     {
/* 409 */       AbstractUrlHandlerMapping.this.exposePathWithinMapping(this.bestMatchingPattern, this.pathWithinMapping, request);
/* 410 */       request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, Boolean.valueOf(AbstractUrlHandlerMapping.this.supportsTypeLevelMappings()));
/* 411 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class UriTemplateVariablesHandlerInterceptor
/*     */     extends HandlerInterceptorAdapter
/*     */   {
/*     */     private final Map<String, String> uriTemplateVariables;
/*     */     
/*     */ 
/*     */ 
/*     */     public UriTemplateVariablesHandlerInterceptor()
/*     */     {
/* 426 */       this.uriTemplateVariables = uriTemplateVariables;
/*     */     }
/*     */     
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     {
/* 431 */       AbstractUrlHandlerMapping.this.exposeUriTemplateVariables(this.uriTemplateVariables, request);
/* 432 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractUrlHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */