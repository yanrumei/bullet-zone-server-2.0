/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
/*     */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.NameValueExpression;
/*     */ import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RequestMappingInfoHandlerMapping
/*     */   extends AbstractHandlerMethodMapping<RequestMappingInfo>
/*     */ {
/*     */   private static final Method HTTP_OPTIONS_HANDLE_METHOD;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  64 */       HTTP_OPTIONS_HANDLE_METHOD = HttpOptionsHandler.class.getMethod("handle", new Class[0]);
/*     */     }
/*     */     catch (NoSuchMethodException ex)
/*     */     {
/*  68 */       throw new IllegalStateException("Failed to retrieve internal handler method for HTTP OPTIONS", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected RequestMappingInfoHandlerMapping()
/*     */   {
/*  74 */     setHandlerMethodMappingNamingStrategy(new RequestMappingInfoHandlerMethodMappingNamingStrategy());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Set<String> getMappingPathPatterns(RequestMappingInfo info)
/*     */   {
/*  83 */     return info.getPatternsCondition().getPatterns();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request)
/*     */   {
/*  94 */     return info.getMatchingCondition(request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Comparator<RequestMappingInfo> getMappingComparator(final HttpServletRequest request)
/*     */   {
/* 102 */     new Comparator()
/*     */     {
/*     */       public int compare(RequestMappingInfo info1, RequestMappingInfo info2) {
/* 105 */         return info1.compareTo(info2, request);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request)
/*     */   {
/* 118 */     super.handleMatch(info, lookupPath, request);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     Set<String> patterns = info.getPatternsCondition().getPatterns();
/* 125 */     Map<String, String> decodedUriVariables; String bestPattern; Map<String, String> uriVariables; Map<String, String> decodedUriVariables; if (patterns.isEmpty()) {
/* 126 */       String bestPattern = lookupPath;
/* 127 */       Map<String, String> uriVariables = Collections.emptyMap();
/* 128 */       decodedUriVariables = Collections.emptyMap();
/*     */     }
/*     */     else {
/* 131 */       bestPattern = (String)patterns.iterator().next();
/* 132 */       uriVariables = getPathMatcher().extractUriTemplateVariables(bestPattern, lookupPath);
/* 133 */       decodedUriVariables = getUrlPathHelper().decodePathVariables(request, uriVariables);
/*     */     }
/*     */     
/* 136 */     request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
/* 137 */     request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodedUriVariables);
/*     */     
/* 139 */     if (isMatrixVariableContentAvailable()) {
/* 140 */       Map<String, MultiValueMap<String, String>> matrixVars = extractMatrixVariables(request, uriVariables);
/* 141 */       request.setAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, matrixVars);
/*     */     }
/*     */     
/* 144 */     if (!info.getProducesCondition().getProducibleMediaTypes().isEmpty()) {
/* 145 */       Set<MediaType> mediaTypes = info.getProducesCondition().getProducibleMediaTypes();
/* 146 */       request.setAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMatrixVariableContentAvailable() {
/* 151 */     return !getUrlPathHelper().shouldRemoveSemicolonContent();
/*     */   }
/*     */   
/*     */ 
/*     */   private Map<String, MultiValueMap<String, String>> extractMatrixVariables(HttpServletRequest request, Map<String, String> uriVariables)
/*     */   {
/* 157 */     Map<String, MultiValueMap<String, String>> result = new LinkedHashMap();
/* 158 */     for (Map.Entry<String, String> uriVar : uriVariables.entrySet()) {
/* 159 */       String uriVarValue = (String)uriVar.getValue();
/*     */       
/* 161 */       int equalsIndex = uriVarValue.indexOf('=');
/* 162 */       if (equalsIndex != -1)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */         int semicolonIndex = uriVarValue.indexOf(';');
/* 169 */         String matrixVariables; String matrixVariables; if ((semicolonIndex == -1) || (semicolonIndex == 0) || (equalsIndex < semicolonIndex)) {
/* 170 */           matrixVariables = uriVarValue;
/*     */         }
/*     */         else {
/* 173 */           matrixVariables = uriVarValue.substring(semicolonIndex + 1);
/* 174 */           uriVariables.put(uriVar.getKey(), uriVarValue.substring(0, semicolonIndex));
/*     */         }
/*     */         
/* 177 */         MultiValueMap<String, String> vars = WebUtils.parseMatrixVariables(matrixVariables);
/* 178 */         result.put(uriVar.getKey(), getUrlPathHelper().decodeMatrixVariables(request, vars));
/*     */       } }
/* 180 */     return result;
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
/*     */   protected HandlerMethod handleNoMatch(Set<RequestMappingInfo> infos, String lookupPath, HttpServletRequest request)
/*     */     throws ServletException
/*     */   {
/* 195 */     PartialMatchHelper helper = new PartialMatchHelper(infos, request);
/*     */     
/* 197 */     if (helper.isEmpty()) {
/* 198 */       return null;
/*     */     }
/*     */     
/* 201 */     if (helper.hasMethodsMismatch()) {
/* 202 */       Set<String> methods = helper.getAllowedMethods();
/* 203 */       if (HttpMethod.OPTIONS.matches(request.getMethod())) {
/* 204 */         HttpOptionsHandler handler = new HttpOptionsHandler(methods);
/* 205 */         return new HandlerMethod(handler, HTTP_OPTIONS_HANDLE_METHOD);
/*     */       }
/* 207 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), methods);
/*     */     }
/*     */     
/* 210 */     if (helper.hasConsumesMismatch()) {
/* 211 */       Set<MediaType> mediaTypes = helper.getConsumableMediaTypes();
/* 212 */       MediaType contentType = null;
/* 213 */       if (StringUtils.hasLength(request.getContentType())) {
/*     */         try {
/* 215 */           contentType = MediaType.parseMediaType(request.getContentType());
/*     */         }
/*     */         catch (InvalidMediaTypeException ex) {
/* 218 */           throw new HttpMediaTypeNotSupportedException(ex.getMessage());
/*     */         }
/*     */       }
/* 221 */       throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList(mediaTypes));
/*     */     }
/*     */     
/* 224 */     if (helper.hasProducesMismatch()) {
/* 225 */       Set<MediaType> mediaTypes = helper.getProducibleMediaTypes();
/* 226 */       throw new HttpMediaTypeNotAcceptableException(new ArrayList(mediaTypes));
/*     */     }
/*     */     
/* 229 */     if (helper.hasParamsMismatch()) {
/* 230 */       List<String[]> conditions = helper.getParamConditions();
/* 231 */       throw new UnsatisfiedServletRequestParameterException(conditions, request.getParameterMap());
/*     */     }
/*     */     
/* 234 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class PartialMatchHelper
/*     */   {
/* 243 */     private final List<PartialMatch> partialMatches = new ArrayList();
/*     */     
/*     */     public PartialMatchHelper(Set<RequestMappingInfo> infos, HttpServletRequest request) {
/* 246 */       for (RequestMappingInfo info : infos) {
/* 247 */         if (info.getPatternsCondition().getMatchingCondition(request) != null) {
/* 248 */           this.partialMatches.add(new PartialMatch(info, request));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 257 */       return this.partialMatches.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasMethodsMismatch()
/*     */     {
/* 264 */       for (PartialMatch match : this.partialMatches) {
/* 265 */         if (match.hasMethodsMatch()) {
/* 266 */           return false;
/*     */         }
/*     */       }
/* 269 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasConsumesMismatch()
/*     */     {
/* 276 */       for (PartialMatch match : this.partialMatches) {
/* 277 */         if (match.hasConsumesMatch()) {
/* 278 */           return false;
/*     */         }
/*     */       }
/* 281 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasProducesMismatch()
/*     */     {
/* 288 */       for (PartialMatch match : this.partialMatches) {
/* 289 */         if (match.hasProducesMatch()) {
/* 290 */           return false;
/*     */         }
/*     */       }
/* 293 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasParamsMismatch()
/*     */     {
/* 300 */       for (PartialMatch match : this.partialMatches) {
/* 301 */         if (match.hasParamsMatch()) {
/* 302 */           return false;
/*     */         }
/*     */       }
/* 305 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Set<String> getAllowedMethods()
/*     */     {
/* 312 */       Set<String> result = new LinkedHashSet();
/* 313 */       for (PartialMatch match : this.partialMatches) {
/* 314 */         for (RequestMethod method : match.getInfo().getMethodsCondition().getMethods()) {
/* 315 */           result.add(method.name());
/*     */         }
/*     */       }
/* 318 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<MediaType> getConsumableMediaTypes()
/*     */     {
/* 326 */       Set<MediaType> result = new LinkedHashSet();
/* 327 */       for (PartialMatch match : this.partialMatches) {
/* 328 */         if (match.hasMethodsMatch()) {
/* 329 */           result.addAll(match.getInfo().getConsumesCondition().getConsumableMediaTypes());
/*     */         }
/*     */       }
/* 332 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<MediaType> getProducibleMediaTypes()
/*     */     {
/* 340 */       Set<MediaType> result = new LinkedHashSet();
/* 341 */       for (PartialMatch match : this.partialMatches) {
/* 342 */         if (match.hasConsumesMatch()) {
/* 343 */           result.addAll(match.getInfo().getProducesCondition().getProducibleMediaTypes());
/*     */         }
/*     */       }
/* 346 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<String[]> getParamConditions()
/*     */     {
/* 354 */       List<String[]> result = new ArrayList();
/* 355 */       for (PartialMatch match : this.partialMatches) {
/* 356 */         if (match.hasProducesMatch()) {
/* 357 */           Set<NameValueExpression<String>> set = match.getInfo().getParamsCondition().getExpressions();
/* 358 */           if (!CollectionUtils.isEmpty(set)) {
/* 359 */             int i = 0;
/* 360 */             String[] array = new String[set.size()];
/* 361 */             for (NameValueExpression<String> expression : set) {
/* 362 */               array[(i++)] = expression.toString();
/*     */             }
/* 364 */             result.add(array);
/*     */           }
/*     */         }
/*     */       }
/* 368 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private static class PartialMatch
/*     */     {
/*     */       private final RequestMappingInfo info;
/*     */       
/*     */ 
/*     */       private final boolean methodsMatch;
/*     */       
/*     */ 
/*     */       private final boolean consumesMatch;
/*     */       
/*     */ 
/*     */       private final boolean producesMatch;
/*     */       
/*     */ 
/*     */       private final boolean paramsMatch;
/*     */       
/*     */ 
/*     */       public PartialMatch(RequestMappingInfo info, HttpServletRequest request)
/*     */       {
/* 392 */         this.info = info;
/* 393 */         this.methodsMatch = (info.getMethodsCondition().getMatchingCondition(request) != null);
/* 394 */         this.consumesMatch = (info.getConsumesCondition().getMatchingCondition(request) != null);
/* 395 */         this.producesMatch = (info.getProducesCondition().getMatchingCondition(request) != null);
/* 396 */         this.paramsMatch = (info.getParamsCondition().getMatchingCondition(request) != null);
/*     */       }
/*     */       
/*     */       public RequestMappingInfo getInfo() {
/* 400 */         return this.info;
/*     */       }
/*     */       
/*     */       public boolean hasMethodsMatch() {
/* 404 */         return this.methodsMatch;
/*     */       }
/*     */       
/*     */       public boolean hasConsumesMatch() {
/* 408 */         return (hasMethodsMatch()) && (this.consumesMatch);
/*     */       }
/*     */       
/*     */       public boolean hasProducesMatch() {
/* 412 */         return (hasConsumesMatch()) && (this.producesMatch);
/*     */       }
/*     */       
/*     */       public boolean hasParamsMatch() {
/* 416 */         return (hasProducesMatch()) && (this.paramsMatch);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 421 */         return this.info.toString();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class HttpOptionsHandler
/*     */   {
/* 432 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public HttpOptionsHandler(Set<String> declaredMethods) {
/* 435 */       this.headers.setAllow(initAllowedHttpMethods(declaredMethods));
/*     */     }
/*     */     
/*     */     private static Set<HttpMethod> initAllowedHttpMethods(Set<String> declaredMethods) {
/* 439 */       Set<HttpMethod> result = new LinkedHashSet(declaredMethods.size());
/* 440 */       boolean hasHead; if (declaredMethods.isEmpty()) {
/* 441 */         for (HttpMethod method : HttpMethod.values()) {
/* 442 */           if (!HttpMethod.TRACE.equals(method)) {
/* 443 */             result.add(method);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 448 */         hasHead = declaredMethods.contains("HEAD");
/* 449 */         for (String method : declaredMethods) {
/* 450 */           result.add(HttpMethod.valueOf(method));
/* 451 */           if ((!hasHead) && ("GET".equals(method))) {
/* 452 */             result.add(HttpMethod.HEAD);
/*     */           }
/*     */         }
/*     */       }
/* 456 */       return result;
/*     */     }
/*     */     
/*     */     public HttpHeaders handle() {
/* 460 */       return this.headers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\RequestMappingInfoHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */