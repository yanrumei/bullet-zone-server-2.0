/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachingResourceTransformer
/*    */   implements ResourceTransformer
/*    */ {
/* 41 */   private static final Log logger = LogFactory.getLog(CachingResourceTransformer.class);
/*    */   
/*    */   private final Cache cache;
/*    */   
/*    */   public CachingResourceTransformer(CacheManager cacheManager, String cacheName)
/*    */   {
/* 47 */     this(cacheManager.getCache(cacheName));
/*    */   }
/*    */   
/*    */   public CachingResourceTransformer(Cache cache) {
/* 51 */     Assert.notNull(cache, "Cache is required");
/* 52 */     this.cache = cache;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Cache getCache()
/*    */   {
/* 60 */     return this.cache;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
/*    */     throws IOException
/*    */   {
/* 68 */     Resource transformed = (Resource)this.cache.get(resource, Resource.class);
/* 69 */     if (transformed != null) {
/* 70 */       if (logger.isTraceEnabled()) {
/* 71 */         logger.trace("Found match: " + transformed);
/*    */       }
/* 73 */       return transformed;
/*    */     }
/*    */     
/* 76 */     transformed = transformerChain.transform(request, resource);
/*    */     
/* 78 */     if (logger.isTraceEnabled()) {
/* 79 */       logger.trace("Putting transformed resource in cache: " + transformed);
/*    */     }
/* 81 */     this.cache.put(resource, transformed);
/*    */     
/* 83 */     return transformed;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\CachingResourceTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */