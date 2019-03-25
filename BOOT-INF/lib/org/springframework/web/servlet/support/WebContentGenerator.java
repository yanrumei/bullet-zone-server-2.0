/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.HttpSessionRequiredException;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebContentGenerator
/*     */   extends WebApplicationObjectSupport
/*     */ {
/*     */   public static final String METHOD_GET = "GET";
/*     */   public static final String METHOD_HEAD = "HEAD";
/*     */   public static final String METHOD_POST = "POST";
/*     */   private static final String HEADER_PRAGMA = "Pragma";
/*     */   private static final String HEADER_EXPIRES = "Expires";
/*     */   protected static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*  85 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeaders", new Class[] { String.class });
/*     */   
/*     */ 
/*     */   private Set<String> supportedMethods;
/*     */   
/*     */ 
/*     */   private String allowHeader;
/*     */   
/*  93 */   private boolean requireSession = false;
/*     */   
/*     */   private CacheControl cacheControl;
/*     */   
/*  97 */   private int cacheSeconds = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   private String[] varyByRequestHeaders;
/*     */   
/*     */ 
/*     */ 
/* 105 */   private boolean useExpiresHeader = false;
/*     */   
/*     */ 
/* 108 */   private boolean useCacheControlHeader = true;
/*     */   
/*     */ 
/* 111 */   private boolean useCacheControlNoStore = true;
/*     */   
/* 113 */   private boolean alwaysMustRevalidate = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebContentGenerator()
/*     */   {
/* 121 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebContentGenerator(boolean restrictDefaultSupportedMethods)
/*     */   {
/* 131 */     if (restrictDefaultSupportedMethods) {
/* 132 */       this.supportedMethods = new LinkedHashSet(4);
/* 133 */       this.supportedMethods.add("GET");
/* 134 */       this.supportedMethods.add("HEAD");
/* 135 */       this.supportedMethods.add("POST");
/*     */     }
/* 137 */     initAllowHeader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebContentGenerator(String... supportedMethods)
/*     */   {
/* 145 */     setSupportedMethods(supportedMethods);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setSupportedMethods(String... methods)
/*     */   {
/* 155 */     if (!ObjectUtils.isEmpty(methods)) {
/* 156 */       this.supportedMethods = new LinkedHashSet(Arrays.asList(methods));
/*     */     }
/*     */     else {
/* 159 */       this.supportedMethods = null;
/*     */     }
/* 161 */     initAllowHeader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String[] getSupportedMethods()
/*     */   {
/* 168 */     return StringUtils.toStringArray(this.supportedMethods);
/*     */   }
/*     */   
/*     */   private void initAllowHeader() {
/*     */     Collection<String> allowedMethods;
/* 173 */     if (this.supportedMethods == null) {
/* 174 */       Collection<String> allowedMethods = new ArrayList(HttpMethod.values().length - 1);
/* 175 */       for (HttpMethod method : HttpMethod.values()) {
/* 176 */         if (!HttpMethod.TRACE.equals(method))
/* 177 */           allowedMethods.add(method.name());
/*     */       }
/*     */     } else {
/*     */       Collection<String> allowedMethods;
/* 181 */       if (this.supportedMethods.contains(HttpMethod.OPTIONS.name())) {
/* 182 */         allowedMethods = this.supportedMethods;
/*     */       }
/*     */       else {
/* 185 */         allowedMethods = new ArrayList(this.supportedMethods);
/* 186 */         allowedMethods.add(HttpMethod.OPTIONS.name());
/*     */       }
/*     */     }
/* 189 */     this.allowHeader = StringUtils.collectionToCommaDelimitedString(allowedMethods);
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
/*     */   protected String getAllowHeader()
/*     */   {
/* 202 */     return this.allowHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void setRequireSession(boolean requireSession)
/*     */   {
/* 209 */     this.requireSession = requireSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean isRequireSession()
/*     */   {
/* 216 */     return this.requireSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setCacheControl(CacheControl cacheControl)
/*     */   {
/* 225 */     this.cacheControl = cacheControl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final CacheControl getCacheControl()
/*     */   {
/* 234 */     return this.cacheControl;
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
/*     */   public final void setCacheSeconds(int seconds)
/*     */   {
/* 250 */     this.cacheSeconds = seconds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int getCacheSeconds()
/*     */   {
/* 257 */     return this.cacheSeconds;
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
/*     */   public final void setVaryByRequestHeaders(String... varyByRequestHeaders)
/*     */   {
/* 272 */     this.varyByRequestHeaders = varyByRequestHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String[] getVaryByRequestHeaders()
/*     */   {
/* 280 */     return this.varyByRequestHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setUseExpiresHeader(boolean useExpiresHeader)
/*     */   {
/* 293 */     this.useExpiresHeader = useExpiresHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isUseExpiresHeader()
/*     */   {
/* 302 */     return this.useExpiresHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setUseCacheControlHeader(boolean useCacheControlHeader)
/*     */   {
/* 314 */     this.useCacheControlHeader = useCacheControlHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isUseCacheControlHeader()
/*     */   {
/* 323 */     return this.useCacheControlHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setUseCacheControlNoStore(boolean useCacheControlNoStore)
/*     */   {
/* 333 */     this.useCacheControlNoStore = useCacheControlNoStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isUseCacheControlNoStore()
/*     */   {
/* 342 */     return this.useCacheControlNoStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void setAlwaysMustRevalidate(boolean mustRevalidate)
/*     */   {
/* 355 */     this.alwaysMustRevalidate = mustRevalidate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean isAlwaysMustRevalidate()
/*     */   {
/* 364 */     return this.alwaysMustRevalidate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void checkRequest(HttpServletRequest request)
/*     */     throws ServletException
/*     */   {
/* 376 */     String method = request.getMethod();
/* 377 */     if ((this.supportedMethods != null) && (!this.supportedMethods.contains(method))) {
/* 378 */       throw new HttpRequestMethodNotSupportedException(method, this.supportedMethods);
/*     */     }
/*     */     
/*     */ 
/* 382 */     if ((this.requireSession) && (request.getSession(false) == null)) {
/* 383 */       throw new HttpSessionRequiredException("Pre-existing session required but none found");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void prepareResponse(HttpServletResponse response)
/*     */   {
/* 394 */     if (this.cacheControl != null) {
/* 395 */       applyCacheControl(response, this.cacheControl);
/*     */     }
/*     */     else {
/* 398 */       applyCacheSeconds(response, this.cacheSeconds);
/*     */     }
/* 400 */     if ((servlet3Present) && (this.varyByRequestHeaders != null)) {
/* 401 */       for (String value : getVaryRequestHeadersToAdd(response)) {
/* 402 */         response.addHeader("Vary", value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void applyCacheControl(HttpServletResponse response, CacheControl cacheControl)
/*     */   {
/* 414 */     String ccValue = cacheControl.getHeaderValue();
/* 415 */     if (ccValue != null)
/*     */     {
/* 417 */       response.setHeader("Cache-Control", ccValue);
/*     */       
/* 419 */       if (response.containsHeader("Pragma"))
/*     */       {
/* 421 */         response.setHeader("Pragma", "");
/*     */       }
/* 423 */       if (response.containsHeader("Expires"))
/*     */       {
/* 425 */         response.setHeader("Expires", "");
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
/*     */   protected final void applyCacheSeconds(HttpServletResponse response, int cacheSeconds)
/*     */   {
/* 441 */     if ((this.useExpiresHeader) || (!this.useCacheControlHeader))
/*     */     {
/* 443 */       if (cacheSeconds > 0) {
/* 444 */         cacheForSeconds(response, cacheSeconds);
/*     */       }
/* 446 */       else if (cacheSeconds == 0) {
/* 447 */         preventCaching(response);
/*     */       }
/*     */     }
/*     */     else {
/*     */       CacheControl cControl;
/* 452 */       if (cacheSeconds > 0) {
/* 453 */         CacheControl cControl = CacheControl.maxAge(cacheSeconds, TimeUnit.SECONDS);
/* 454 */         if (this.alwaysMustRevalidate)
/* 455 */           cControl = cControl.mustRevalidate();
/*     */       } else {
/*     */         CacheControl cControl;
/* 458 */         if (cacheSeconds == 0) {
/* 459 */           cControl = this.useCacheControlNoStore ? CacheControl.noStore() : CacheControl.noCache();
/*     */         }
/*     */         else
/* 462 */           cControl = CacheControl.empty();
/*     */       }
/* 464 */       applyCacheControl(response, cControl);
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
/*     */   @Deprecated
/*     */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, boolean lastModified)
/*     */     throws ServletException
/*     */   {
/* 479 */     checkRequest(request);
/* 480 */     prepareResponse(response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, int cacheSeconds, boolean lastModified)
/*     */     throws ServletException
/*     */   {
/* 494 */     checkRequest(request);
/* 495 */     applyCacheSeconds(response, cacheSeconds);
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
/*     */   @Deprecated
/*     */   protected final void applyCacheSeconds(HttpServletResponse response, int cacheSeconds, boolean mustRevalidate)
/*     */   {
/* 513 */     if (cacheSeconds > 0) {
/* 514 */       cacheForSeconds(response, cacheSeconds, mustRevalidate);
/*     */     }
/* 516 */     else if (cacheSeconds == 0) {
/* 517 */       preventCaching(response);
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
/*     */   @Deprecated
/*     */   protected final void cacheForSeconds(HttpServletResponse response, int seconds)
/*     */   {
/* 531 */     cacheForSeconds(response, seconds, false);
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
/*     */   @Deprecated
/*     */   protected final void cacheForSeconds(HttpServletResponse response, int seconds, boolean mustRevalidate)
/*     */   {
/* 547 */     if (this.useExpiresHeader)
/*     */     {
/* 549 */       response.setDateHeader("Expires", System.currentTimeMillis() + seconds * 1000L);
/*     */     }
/* 551 */     else if (response.containsHeader("Expires"))
/*     */     {
/* 553 */       response.setHeader("Expires", "");
/*     */     }
/*     */     
/* 556 */     if (this.useCacheControlHeader)
/*     */     {
/* 558 */       String headerValue = "max-age=" + seconds;
/* 559 */       if ((mustRevalidate) || (this.alwaysMustRevalidate)) {
/* 560 */         headerValue = headerValue + ", must-revalidate";
/*     */       }
/* 562 */       response.setHeader("Cache-Control", headerValue);
/*     */     }
/*     */     
/* 565 */     if (response.containsHeader("Pragma"))
/*     */     {
/* 567 */       response.setHeader("Pragma", "");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected final void preventCaching(HttpServletResponse response)
/*     */   {
/* 579 */     response.setHeader("Pragma", "no-cache");
/*     */     
/* 581 */     if (this.useExpiresHeader)
/*     */     {
/* 583 */       response.setDateHeader("Expires", 1L);
/*     */     }
/*     */     
/* 586 */     if (this.useCacheControlHeader)
/*     */     {
/*     */ 
/* 589 */       response.setHeader("Cache-Control", "no-cache");
/* 590 */       if (this.useCacheControlNoStore) {
/* 591 */         response.addHeader("Cache-Control", "no-store");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Collection<String> getVaryRequestHeadersToAdd(HttpServletResponse response)
/*     */   {
/* 598 */     if (!response.containsHeader("Vary")) {
/* 599 */       return Arrays.asList(getVaryByRequestHeaders());
/*     */     }
/* 601 */     Collection<String> result = new ArrayList(getVaryByRequestHeaders().length);
/* 602 */     Collections.addAll(result, getVaryByRequestHeaders());
/* 603 */     for (String header : response.getHeaders("Vary")) {
/* 604 */       for (String existing : StringUtils.tokenizeToStringArray(header, ",")) {
/* 605 */         if ("*".equals(existing)) {
/* 606 */           return Collections.emptyList();
/*     */         }
/* 608 */         for (String value : getVaryByRequestHeaders()) {
/* 609 */           if (value.equalsIgnoreCase(existing)) {
/* 610 */             result.remove(value);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 615 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\WebContentGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */