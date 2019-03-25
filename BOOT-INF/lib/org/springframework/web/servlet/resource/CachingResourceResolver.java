/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachingResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   public static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX = "resolvedResource:";
/*     */   public static final String RESOLVED_URL_PATH_CACHE_KEY_PREFIX = "resolvedUrlPath:";
/*     */   private final Cache cache;
/*     */   
/*     */   public CachingResourceResolver(CacheManager cacheManager, String cacheName)
/*     */   {
/*  47 */     this(cacheManager.getCache(cacheName));
/*     */   }
/*     */   
/*     */   public CachingResourceResolver(Cache cache) {
/*  51 */     Assert.notNull(cache, "Cache is required");
/*  52 */     this.cache = cache;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Cache getCache()
/*     */   {
/*  60 */     return this.cache;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/*  68 */     String key = computeKey(request, requestPath);
/*  69 */     Resource resource = (Resource)this.cache.get(key, Resource.class);
/*     */     
/*  71 */     if (resource != null) {
/*  72 */       if (this.logger.isTraceEnabled()) {
/*  73 */         this.logger.trace("Found match: " + resource);
/*     */       }
/*  75 */       return resource;
/*     */     }
/*     */     
/*  78 */     resource = chain.resolveResource(request, requestPath, locations);
/*  79 */     if (resource != null) {
/*  80 */       if (this.logger.isTraceEnabled()) {
/*  81 */         this.logger.trace("Putting resolved resource in cache: " + resource);
/*     */       }
/*  83 */       this.cache.put(key, resource);
/*     */     }
/*     */     
/*  86 */     return resource;
/*     */   }
/*     */   
/*     */   protected String computeKey(HttpServletRequest request, String requestPath) {
/*  90 */     StringBuilder key = new StringBuilder("resolvedResource:");
/*  91 */     key.append(requestPath);
/*  92 */     if (request != null) {
/*  93 */       String encoding = request.getHeader("Accept-Encoding");
/*  94 */       if ((encoding != null) && (encoding.contains("gzip"))) {
/*  95 */         key.append("+encoding=gzip");
/*     */       }
/*     */     }
/*  98 */     return key.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/* 105 */     String key = "resolvedUrlPath:" + resourceUrlPath;
/* 106 */     String resolvedUrlPath = (String)this.cache.get(key, String.class);
/*     */     
/* 108 */     if (resolvedUrlPath != null) {
/* 109 */       if (this.logger.isTraceEnabled()) {
/* 110 */         this.logger.trace("Found match: \"" + resolvedUrlPath + "\"");
/*     */       }
/* 112 */       return resolvedUrlPath;
/*     */     }
/*     */     
/* 115 */     resolvedUrlPath = chain.resolveUrlPath(resourceUrlPath, locations);
/* 116 */     if (resolvedUrlPath != null) {
/* 117 */       if (this.logger.isTraceEnabled()) {
/* 118 */         this.logger.trace("Putting resolved resource URL path in cache: \"" + resolvedUrlPath + "\"");
/*     */       }
/* 120 */       this.cache.put(key, resolvedUrlPath);
/*     */     }
/*     */     
/* 123 */     return resolvedUrlPath;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\CachingResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */