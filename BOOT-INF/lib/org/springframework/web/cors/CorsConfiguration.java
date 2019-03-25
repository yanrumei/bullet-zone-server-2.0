/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CorsConfiguration
/*     */ {
/*     */   public static final String ALL = "*";
/*     */   private static final List<HttpMethod> DEFAULT_METHODS;
/*     */   private List<String> allowedOrigins;
/*     */   private List<String> allowedMethods;
/*     */   
/*     */   static
/*     */   {
/*  60 */     List<HttpMethod> rawMethods = new ArrayList(2);
/*  61 */     rawMethods.add(HttpMethod.GET);
/*  62 */     rawMethods.add(HttpMethod.HEAD);
/*  63 */     DEFAULT_METHODS = Collections.unmodifiableList(rawMethods);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private List<HttpMethod> resolvedMethods = DEFAULT_METHODS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> allowedHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */   private List<String> exposedHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean allowCredentials;
/*     */   
/*     */ 
/*     */ 
/*     */   private Long maxAge;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsConfiguration(CorsConfiguration other)
/*     */   {
/*  95 */     this.allowedOrigins = other.allowedOrigins;
/*  96 */     this.allowedMethods = other.allowedMethods;
/*  97 */     this.resolvedMethods = other.resolvedMethods;
/*  98 */     this.allowedHeaders = other.allowedHeaders;
/*  99 */     this.exposedHeaders = other.exposedHeaders;
/* 100 */     this.allowCredentials = other.allowCredentials;
/* 101 */     this.maxAge = other.maxAge;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowedOrigins(List<String> allowedOrigins)
/*     */   {
/* 111 */     this.allowedOrigins = (allowedOrigins != null ? new ArrayList(allowedOrigins) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getAllowedOrigins()
/*     */   {
/* 120 */     return this.allowedOrigins;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAllowedOrigin(String origin)
/*     */   {
/* 127 */     if (this.allowedOrigins == null) {
/* 128 */       this.allowedOrigins = new ArrayList(4);
/*     */     }
/* 130 */     this.allowedOrigins.add(origin);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowedMethods(List<String> allowedMethods)
/*     */   {
/* 141 */     this.allowedMethods = (allowedMethods != null ? new ArrayList(allowedMethods) : null);
/* 142 */     if (!CollectionUtils.isEmpty(allowedMethods)) {
/* 143 */       this.resolvedMethods = new ArrayList(allowedMethods.size());
/* 144 */       for (String method : allowedMethods) {
/* 145 */         if ("*".equals(method)) {
/* 146 */           this.resolvedMethods = null;
/* 147 */           break;
/*     */         }
/* 149 */         this.resolvedMethods.add(HttpMethod.resolve(method));
/*     */       }
/*     */     }
/*     */     else {
/* 153 */       this.resolvedMethods = DEFAULT_METHODS;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getAllowedMethods()
/*     */   {
/* 165 */     return this.allowedMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAllowedMethod(HttpMethod method)
/*     */   {
/* 172 */     if (method != null) {
/* 173 */       addAllowedMethod(method.name());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAllowedMethod(String method)
/*     */   {
/* 181 */     if (StringUtils.hasText(method)) {
/* 182 */       if (this.allowedMethods == null) {
/* 183 */         this.allowedMethods = new ArrayList(4);
/* 184 */         this.resolvedMethods = new ArrayList(4);
/*     */       }
/* 186 */       this.allowedMethods.add(method);
/* 187 */       if ("*".equals(method)) {
/* 188 */         this.resolvedMethods = null;
/*     */       }
/* 190 */       else if (this.resolvedMethods != null) {
/* 191 */         this.resolvedMethods.add(HttpMethod.resolve(method));
/*     */       }
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
/*     */ 
/*     */   public void setAllowedHeaders(List<String> allowedHeaders)
/*     */   {
/* 207 */     this.allowedHeaders = (allowedHeaders != null ? new ArrayList(allowedHeaders) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getAllowedHeaders()
/*     */   {
/* 216 */     return this.allowedHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAllowedHeader(String allowedHeader)
/*     */   {
/* 223 */     if (this.allowedHeaders == null) {
/* 224 */       this.allowedHeaders = new ArrayList(4);
/*     */     }
/* 226 */     this.allowedHeaders.add(allowedHeader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposedHeaders(List<String> exposedHeaders)
/*     */   {
/* 238 */     if ((exposedHeaders != null) && (exposedHeaders.contains("*"))) {
/* 239 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 241 */     this.exposedHeaders = (exposedHeaders != null ? new ArrayList(exposedHeaders) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getExposedHeaders()
/*     */   {
/* 250 */     return this.exposedHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addExposedHeader(String exposedHeader)
/*     */   {
/* 258 */     if ("*".equals(exposedHeader)) {
/* 259 */       throw new IllegalArgumentException("'*' is not a valid exposed header value");
/*     */     }
/* 261 */     if (this.exposedHeaders == null) {
/* 262 */       this.exposedHeaders = new ArrayList(4);
/*     */     }
/* 264 */     this.exposedHeaders.add(exposedHeader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowCredentials(Boolean allowCredentials)
/*     */   {
/* 272 */     this.allowCredentials = allowCredentials;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getAllowCredentials()
/*     */   {
/* 280 */     return this.allowCredentials;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxAge(Long maxAge)
/*     */   {
/* 289 */     this.maxAge = maxAge;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Long getMaxAge()
/*     */   {
/* 297 */     return this.maxAge;
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
/*     */   public CorsConfiguration applyPermitDefaultValues()
/*     */   {
/* 320 */     if (this.allowedOrigins == null) {
/* 321 */       addAllowedOrigin("*");
/*     */     }
/* 323 */     if (this.allowedMethods == null) {
/* 324 */       setAllowedMethods(Arrays.asList(new String[] {HttpMethod.GET
/* 325 */         .name(), HttpMethod.HEAD.name(), HttpMethod.POST.name() }));
/*     */     }
/* 327 */     if (this.allowedHeaders == null) {
/* 328 */       addAllowedHeader("*");
/*     */     }
/* 330 */     if (this.allowCredentials == null) {
/* 331 */       setAllowCredentials(Boolean.valueOf(true));
/*     */     }
/* 333 */     if (this.maxAge == null) {
/* 334 */       setMaxAge(Long.valueOf(1800L));
/*     */     }
/* 336 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsConfiguration combine(CorsConfiguration other)
/*     */   {
/* 347 */     if (other == null) {
/* 348 */       return this;
/*     */     }
/* 350 */     CorsConfiguration config = new CorsConfiguration(this);
/* 351 */     config.setAllowedOrigins(combine(getAllowedOrigins(), other.getAllowedOrigins()));
/* 352 */     config.setAllowedMethods(combine(getAllowedMethods(), other.getAllowedMethods()));
/* 353 */     config.setAllowedHeaders(combine(getAllowedHeaders(), other.getAllowedHeaders()));
/* 354 */     config.setExposedHeaders(combine(getExposedHeaders(), other.getExposedHeaders()));
/* 355 */     Boolean allowCredentials = other.getAllowCredentials();
/* 356 */     if (allowCredentials != null) {
/* 357 */       config.setAllowCredentials(allowCredentials);
/*     */     }
/* 359 */     Long maxAge = other.getMaxAge();
/* 360 */     if (maxAge != null) {
/* 361 */       config.setMaxAge(maxAge);
/*     */     }
/* 363 */     return config;
/*     */   }
/*     */   
/*     */   private List<String> combine(List<String> source, List<String> other) {
/* 367 */     if ((other == null) || (other.contains("*"))) {
/* 368 */       return source;
/*     */     }
/* 370 */     if ((source == null) || (source.contains("*"))) {
/* 371 */       return other;
/*     */     }
/* 373 */     Set<String> combined = new LinkedHashSet(source);
/* 374 */     combined.addAll(other);
/* 375 */     return new ArrayList(combined);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String checkOrigin(String requestOrigin)
/*     */   {
/* 385 */     if (!StringUtils.hasText(requestOrigin)) {
/* 386 */       return null;
/*     */     }
/* 388 */     if (ObjectUtils.isEmpty(this.allowedOrigins)) {
/* 389 */       return null;
/*     */     }
/*     */     
/* 392 */     if (this.allowedOrigins.contains("*")) {
/* 393 */       if (this.allowCredentials != Boolean.TRUE) {
/* 394 */         return "*";
/*     */       }
/*     */       
/* 397 */       return requestOrigin;
/*     */     }
/*     */     
/* 400 */     for (String allowedOrigin : this.allowedOrigins) {
/* 401 */       if (requestOrigin.equalsIgnoreCase(allowedOrigin)) {
/* 402 */         return requestOrigin;
/*     */       }
/*     */     }
/*     */     
/* 406 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HttpMethod> checkHttpMethod(HttpMethod requestMethod)
/*     */   {
/* 418 */     if (requestMethod == null) {
/* 419 */       return null;
/*     */     }
/* 421 */     if (this.resolvedMethods == null) {
/* 422 */       return Collections.singletonList(requestMethod);
/*     */     }
/* 424 */     return this.resolvedMethods.contains(requestMethod) ? this.resolvedMethods : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> checkHeaders(List<String> requestHeaders)
/*     */   {
/* 436 */     if (requestHeaders == null) {
/* 437 */       return null;
/*     */     }
/* 439 */     if (requestHeaders.isEmpty()) {
/* 440 */       return Collections.emptyList();
/*     */     }
/* 442 */     if (ObjectUtils.isEmpty(this.allowedHeaders)) {
/* 443 */       return null;
/*     */     }
/*     */     
/* 446 */     boolean allowAnyHeader = this.allowedHeaders.contains("*");
/* 447 */     List<String> result = new ArrayList(requestHeaders.size());
/* 448 */     for (Iterator localIterator1 = requestHeaders.iterator(); localIterator1.hasNext();) { requestHeader = (String)localIterator1.next();
/* 449 */       if (StringUtils.hasText(requestHeader)) {
/* 450 */         requestHeader = requestHeader.trim();
/* 451 */         if (allowAnyHeader) {
/* 452 */           result.add(requestHeader);
/*     */         }
/*     */         else {
/* 455 */           for (String allowedHeader : this.allowedHeaders)
/* 456 */             if (requestHeader.equalsIgnoreCase(allowedHeader)) {
/* 457 */               result.add(requestHeader);
/* 458 */               break;
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/*     */     String requestHeader;
/* 464 */     return result.isEmpty() ? null : result;
/*     */   }
/*     */   
/*     */   public CorsConfiguration() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\cors\CorsConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */