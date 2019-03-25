/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ public class CompositeCacheOperationSource
/*    */   implements CacheOperationSource, Serializable
/*    */ {
/*    */   private final CacheOperationSource[] cacheOperationSources;
/*    */   
/*    */   public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources)
/*    */   {
/* 44 */     Assert.notEmpty(cacheOperationSources, "cacheOperationSources array must not be empty");
/* 45 */     this.cacheOperationSources = cacheOperationSources;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final CacheOperationSource[] getCacheOperationSources()
/*    */   {
/* 53 */     return this.cacheOperationSources;
/*    */   }
/*    */   
/*    */   public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass)
/*    */   {
/* 58 */     Collection<CacheOperation> ops = null;
/*    */     
/* 60 */     for (CacheOperationSource source : this.cacheOperationSources) {
/* 61 */       Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
/* 62 */       if (cacheOperations != null) {
/* 63 */         if (ops == null) {
/* 64 */           ops = new ArrayList();
/*    */         }
/*    */         
/* 67 */         ops.addAll(cacheOperations);
/*    */       }
/*    */     }
/* 70 */     return ops;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CompositeCacheOperationSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */