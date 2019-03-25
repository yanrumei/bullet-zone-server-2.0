/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
/*     */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
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
/*     */ public final class RequestMappingInfo
/*     */   implements RequestCondition<RequestMappingInfo>
/*     */ {
/*     */   private final String name;
/*     */   private final PatternsRequestCondition patternsCondition;
/*     */   private final RequestMethodsRequestCondition methodsCondition;
/*     */   private final ParamsRequestCondition paramsCondition;
/*     */   private final HeadersRequestCondition headersCondition;
/*     */   private final ConsumesRequestCondition consumesCondition;
/*     */   private final ProducesRequestCondition producesCondition;
/*     */   private final RequestConditionHolder customConditionHolder;
/*     */   
/*     */   public RequestMappingInfo(String name, PatternsRequestCondition patterns, RequestMethodsRequestCondition methods, ParamsRequestCondition params, HeadersRequestCondition headers, ConsumesRequestCondition consumes, ProducesRequestCondition produces, RequestCondition<?> custom)
/*     */   {
/*  76 */     this.name = (StringUtils.hasText(name) ? name : null);
/*  77 */     this.patternsCondition = (patterns != null ? patterns : new PatternsRequestCondition(new String[0]));
/*  78 */     this.methodsCondition = (methods != null ? methods : new RequestMethodsRequestCondition(new RequestMethod[0]));
/*  79 */     this.paramsCondition = (params != null ? params : new ParamsRequestCondition(new String[0]));
/*  80 */     this.headersCondition = (headers != null ? headers : new HeadersRequestCondition(new String[0]));
/*  81 */     this.consumesCondition = (consumes != null ? consumes : new ConsumesRequestCondition(new String[0]));
/*  82 */     this.producesCondition = (produces != null ? produces : new ProducesRequestCondition(new String[0]));
/*  83 */     this.customConditionHolder = new RequestConditionHolder(custom);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestMappingInfo(PatternsRequestCondition patterns, RequestMethodsRequestCondition methods, ParamsRequestCondition params, HeadersRequestCondition headers, ConsumesRequestCondition consumes, ProducesRequestCondition produces, RequestCondition<?> custom)
/*     */   {
/*  93 */     this(null, patterns, methods, params, headers, consumes, produces, custom);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RequestMappingInfo(RequestMappingInfo info, RequestCondition<?> customRequestCondition)
/*     */   {
/* 100 */     this(info.name, info.patternsCondition, info.methodsCondition, info.paramsCondition, info.headersCondition, info.consumesCondition, info.producesCondition, customRequestCondition);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 109 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PatternsRequestCondition getPatternsCondition()
/*     */   {
/* 117 */     return this.patternsCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestMethodsRequestCondition getMethodsCondition()
/*     */   {
/* 125 */     return this.methodsCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParamsRequestCondition getParamsCondition()
/*     */   {
/* 133 */     return this.paramsCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadersRequestCondition getHeadersCondition()
/*     */   {
/* 141 */     return this.headersCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConsumesRequestCondition getConsumesCondition()
/*     */   {
/* 149 */     return this.consumesCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProducesRequestCondition getProducesCondition()
/*     */   {
/* 157 */     return this.producesCondition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RequestCondition<?> getCustomCondition()
/*     */   {
/* 164 */     return this.customConditionHolder.getCondition();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestMappingInfo combine(RequestMappingInfo other)
/*     */   {
/* 175 */     String name = combineNames(other);
/* 176 */     PatternsRequestCondition patterns = this.patternsCondition.combine(other.patternsCondition);
/* 177 */     RequestMethodsRequestCondition methods = this.methodsCondition.combine(other.methodsCondition);
/* 178 */     ParamsRequestCondition params = this.paramsCondition.combine(other.paramsCondition);
/* 179 */     HeadersRequestCondition headers = this.headersCondition.combine(other.headersCondition);
/* 180 */     ConsumesRequestCondition consumes = this.consumesCondition.combine(other.consumesCondition);
/* 181 */     ProducesRequestCondition produces = this.producesCondition.combine(other.producesCondition);
/* 182 */     RequestConditionHolder custom = this.customConditionHolder.combine(other.customConditionHolder);
/*     */     
/* 184 */     return new RequestMappingInfo(name, patterns, methods, params, headers, consumes, produces, custom
/* 185 */       .getCondition());
/*     */   }
/*     */   
/*     */   private String combineNames(RequestMappingInfo other) {
/* 189 */     if ((this.name != null) && (other.name != null)) {
/* 190 */       String separator = "#";
/* 191 */       return this.name + separator + other.name;
/*     */     }
/* 193 */     if (this.name != null) {
/* 194 */       return this.name;
/*     */     }
/*     */     
/* 197 */     return other.name;
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
/*     */   public RequestMappingInfo getMatchingCondition(HttpServletRequest request)
/*     */   {
/* 210 */     RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
/* 211 */     ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
/* 212 */     HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
/* 213 */     ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
/* 214 */     ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
/*     */     
/* 216 */     if ((methods == null) || (params == null) || (headers == null) || (consumes == null) || (produces == null)) {
/* 217 */       return null;
/*     */     }
/*     */     
/* 220 */     PatternsRequestCondition patterns = this.patternsCondition.getMatchingCondition(request);
/* 221 */     if (patterns == null) {
/* 222 */       return null;
/*     */     }
/*     */     
/* 225 */     RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
/* 226 */     if (custom == null) {
/* 227 */       return null;
/*     */     }
/*     */     
/* 230 */     return new RequestMappingInfo(this.name, patterns, methods, params, headers, consumes, produces, custom
/* 231 */       .getCondition());
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
/*     */   public int compareTo(RequestMappingInfo other, HttpServletRequest request)
/*     */   {
/* 244 */     if (HttpMethod.HEAD.matches(request.getMethod())) {
/* 245 */       int result = this.methodsCondition.compareTo(other.getMethodsCondition(), request);
/* 246 */       if (result != 0) {
/* 247 */         return result;
/*     */       }
/*     */     }
/* 250 */     int result = this.patternsCondition.compareTo(other.getPatternsCondition(), request);
/* 251 */     if (result != 0) {
/* 252 */       return result;
/*     */     }
/* 254 */     result = this.paramsCondition.compareTo(other.getParamsCondition(), request);
/* 255 */     if (result != 0) {
/* 256 */       return result;
/*     */     }
/* 258 */     result = this.headersCondition.compareTo(other.getHeadersCondition(), request);
/* 259 */     if (result != 0) {
/* 260 */       return result;
/*     */     }
/* 262 */     result = this.consumesCondition.compareTo(other.getConsumesCondition(), request);
/* 263 */     if (result != 0) {
/* 264 */       return result;
/*     */     }
/* 266 */     result = this.producesCondition.compareTo(other.getProducesCondition(), request);
/* 267 */     if (result != 0) {
/* 268 */       return result;
/*     */     }
/*     */     
/* 271 */     result = this.methodsCondition.compareTo(other.getMethodsCondition(), request);
/* 272 */     if (result != 0) {
/* 273 */       return result;
/*     */     }
/* 275 */     result = this.customConditionHolder.compareTo(other.customConditionHolder, request);
/* 276 */     if (result != 0) {
/* 277 */       return result;
/*     */     }
/* 279 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 284 */     if (this == other) {
/* 285 */       return true;
/*     */     }
/* 287 */     if (!(other instanceof RequestMappingInfo)) {
/* 288 */       return false;
/*     */     }
/* 290 */     RequestMappingInfo otherInfo = (RequestMappingInfo)other;
/* 291 */     return (this.patternsCondition.equals(otherInfo.patternsCondition)) && 
/* 292 */       (this.methodsCondition.equals(otherInfo.methodsCondition)) && 
/* 293 */       (this.paramsCondition.equals(otherInfo.paramsCondition)) && 
/* 294 */       (this.headersCondition.equals(otherInfo.headersCondition)) && 
/* 295 */       (this.consumesCondition.equals(otherInfo.consumesCondition)) && 
/* 296 */       (this.producesCondition.equals(otherInfo.producesCondition)) && 
/* 297 */       (this.customConditionHolder.equals(otherInfo.customConditionHolder));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 302 */     return 
/*     */     
/*     */ 
/* 305 */       this.patternsCondition.hashCode() * 31 + this.methodsCondition.hashCode() + this.paramsCondition.hashCode() + this.headersCondition.hashCode() + this.consumesCondition.hashCode() + this.producesCondition.hashCode() + this.customConditionHolder.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 310 */     StringBuilder builder = new StringBuilder("{");
/* 311 */     builder.append(this.patternsCondition);
/* 312 */     if (!this.methodsCondition.isEmpty()) {
/* 313 */       builder.append(",methods=").append(this.methodsCondition);
/*     */     }
/* 315 */     if (!this.paramsCondition.isEmpty()) {
/* 316 */       builder.append(",params=").append(this.paramsCondition);
/*     */     }
/* 318 */     if (!this.headersCondition.isEmpty()) {
/* 319 */       builder.append(",headers=").append(this.headersCondition);
/*     */     }
/* 321 */     if (!this.consumesCondition.isEmpty()) {
/* 322 */       builder.append(",consumes=").append(this.consumesCondition);
/*     */     }
/* 324 */     if (!this.producesCondition.isEmpty()) {
/* 325 */       builder.append(",produces=").append(this.producesCondition);
/*     */     }
/* 327 */     if (!this.customConditionHolder.isEmpty()) {
/* 328 */       builder.append(",custom=").append(this.customConditionHolder);
/*     */     }
/* 330 */     builder.append('}');
/* 331 */     return builder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder paths(String... paths)
/*     */   {
/* 341 */     return new DefaultBuilder(paths);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract interface Builder
/*     */   {
/*     */     public abstract Builder paths(String... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Builder methods(RequestMethod... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Builder params(String... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Builder headers(String... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Builder consumes(String... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Builder produces(String... paramVarArgs);
/*     */     
/*     */ 
/*     */ 
/*     */     public abstract Builder mappingName(String paramString);
/*     */     
/*     */ 
/*     */ 
/*     */     public abstract Builder customCondition(RequestCondition<?> paramRequestCondition);
/*     */     
/*     */ 
/*     */ 
/*     */     public abstract Builder options(RequestMappingInfo.BuilderConfiguration paramBuilderConfiguration);
/*     */     
/*     */ 
/*     */ 
/*     */     public abstract RequestMappingInfo build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class DefaultBuilder
/*     */     implements RequestMappingInfo.Builder
/*     */   {
/*     */     private String[] paths;
/*     */     
/*     */ 
/*     */     private RequestMethod[] methods;
/*     */     
/*     */ 
/*     */     private String[] params;
/*     */     
/*     */ 
/*     */     private String[] headers;
/*     */     
/*     */ 
/*     */     private String[] consumes;
/*     */     
/*     */ 
/*     */     private String[] produces;
/*     */     
/*     */ 
/*     */     private String mappingName;
/*     */     
/*     */ 
/*     */     private RequestCondition<?> customCondition;
/*     */     
/*     */ 
/* 422 */     private RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
/*     */     
/*     */     public DefaultBuilder(String... paths) {
/* 425 */       this.paths = paths;
/*     */     }
/*     */     
/*     */     public RequestMappingInfo.Builder paths(String... paths)
/*     */     {
/* 430 */       this.paths = paths;
/* 431 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder methods(RequestMethod... methods)
/*     */     {
/* 436 */       this.methods = methods;
/* 437 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder params(String... params)
/*     */     {
/* 442 */       this.params = params;
/* 443 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder headers(String... headers)
/*     */     {
/* 448 */       this.headers = headers;
/* 449 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder consumes(String... consumes)
/*     */     {
/* 454 */       this.consumes = consumes;
/* 455 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder produces(String... produces)
/*     */     {
/* 460 */       this.produces = produces;
/* 461 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder mappingName(String name)
/*     */     {
/* 466 */       this.mappingName = name;
/* 467 */       return this;
/*     */     }
/*     */     
/*     */     public DefaultBuilder customCondition(RequestCondition<?> condition)
/*     */     {
/* 472 */       this.customCondition = condition;
/* 473 */       return this;
/*     */     }
/*     */     
/*     */     public RequestMappingInfo.Builder options(RequestMappingInfo.BuilderConfiguration options)
/*     */     {
/* 478 */       this.options = options;
/* 479 */       return this;
/*     */     }
/*     */     
/*     */     public RequestMappingInfo build()
/*     */     {
/* 484 */       ContentNegotiationManager manager = this.options.getContentNegotiationManager();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 489 */       PatternsRequestCondition patternsCondition = new PatternsRequestCondition(this.paths, this.options.getUrlPathHelper(), this.options.getPathMatcher(), this.options.useSuffixPatternMatch(), this.options.useTrailingSlashMatch(), this.options.getFileExtensions());
/*     */       
/* 491 */       return new RequestMappingInfo(this.mappingName, patternsCondition, new RequestMethodsRequestCondition(this.methods), new ParamsRequestCondition(this.params), new HeadersRequestCondition(this.headers), new ConsumesRequestCondition(this.consumes, this.headers), new ProducesRequestCondition(this.produces, this.headers, manager), this.customCondition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class BuilderConfiguration
/*     */   {
/*     */     private UrlPathHelper urlPathHelper;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private PathMatcher pathMatcher;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 515 */     private boolean trailingSlashMatch = true;
/*     */     
/* 517 */     private boolean suffixPatternMatch = true;
/*     */     
/* 519 */     private boolean registeredSuffixPatternMatch = false;
/*     */     
/*     */ 
/*     */     private ContentNegotiationManager contentNegotiationManager;
/*     */     
/*     */ 
/*     */     @Deprecated
/*     */     public void setPathHelper(UrlPathHelper pathHelper)
/*     */     {
/* 528 */       this.urlPathHelper = pathHelper;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */     {
/* 537 */       this.urlPathHelper = urlPathHelper;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public UrlPathHelper getUrlPathHelper()
/*     */     {
/* 544 */       return this.urlPathHelper;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setPathMatcher(PathMatcher pathMatcher)
/*     */     {
/* 552 */       this.pathMatcher = pathMatcher;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public PathMatcher getPathMatcher()
/*     */     {
/* 559 */       return this.pathMatcher;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setTrailingSlashMatch(boolean trailingSlashMatch)
/*     */     {
/* 567 */       this.trailingSlashMatch = trailingSlashMatch;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean useTrailingSlashMatch()
/*     */     {
/* 574 */       return this.trailingSlashMatch;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setSuffixPatternMatch(boolean suffixPatternMatch)
/*     */     {
/* 583 */       this.suffixPatternMatch = suffixPatternMatch;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean useSuffixPatternMatch()
/*     */     {
/* 590 */       return this.suffixPatternMatch;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setRegisteredSuffixPatternMatch(boolean registeredSuffixPatternMatch)
/*     */     {
/* 601 */       this.registeredSuffixPatternMatch = registeredSuffixPatternMatch;
/* 602 */       this.suffixPatternMatch = ((registeredSuffixPatternMatch) || (this.suffixPatternMatch));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean useRegisteredSuffixPatternMatch()
/*     */     {
/* 610 */       return this.registeredSuffixPatternMatch;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<String> getFileExtensions()
/*     */     {
/* 619 */       if ((useRegisteredSuffixPatternMatch()) && (this.contentNegotiationManager != null)) {
/* 620 */         return this.contentNegotiationManager.getAllFileExtensions();
/*     */       }
/* 622 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */     {
/* 630 */       this.contentNegotiationManager = contentNegotiationManager;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContentNegotiationManager getContentNegotiationManager()
/*     */     {
/* 638 */       return this.contentNegotiationManager;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\RequestMappingInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */