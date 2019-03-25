/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
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
/*    */ 
/*    */ 
/*    */ public class NoOpCacheManager
/*    */   implements CacheManager
/*    */ {
/* 43 */   private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap(16);
/*    */   
/* 45 */   private final Set<String> cacheNames = new LinkedHashSet(16);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Cache getCache(String name)
/*    */   {
/* 54 */     Cache cache = (Cache)this.caches.get(name);
/* 55 */     if (cache == null) {
/* 56 */       this.caches.putIfAbsent(name, new NoOpCache(name));
/* 57 */       synchronized (this.cacheNames) {
/* 58 */         this.cacheNames.add(name);
/*    */       }
/*    */     }
/*    */     
/* 62 */     return (Cache)this.caches.get(name);
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public java.util.Collection<String> getCacheNames()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 7	org/springframework/cache/support/NoOpCacheManager:cacheNames	Ljava/util/Set;
/*    */     //   4: dup
/*    */     //   5: astore_1
/*    */     //   6: monitorenter
/*    */     //   7: aload_0
/*    */     //   8: getfield 7	org/springframework/cache/support/NoOpCacheManager:cacheNames	Ljava/util/Set;
/*    */     //   11: invokestatic 14	java/util/Collections:unmodifiableSet	(Ljava/util/Set;)Ljava/util/Set;
/*    */     //   14: aload_1
/*    */     //   15: monitorexit
/*    */     //   16: areturn
/*    */     //   17: astore_2
/*    */     //   18: aload_1
/*    */     //   19: monitorexit
/*    */     //   20: aload_2
/*    */     //   21: athrow
/*    */     // Line number table:
/*    */     //   Java source line #70	-> byte code offset #0
/*    */     //   Java source line #71	-> byte code offset #7
/*    */     //   Java source line #72	-> byte code offset #17
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	22	0	this	NoOpCacheManager
/*    */     //   5	14	1	Ljava/lang/Object;	Object
/*    */     //   17	4	2	localObject1	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	16	17	finally
/*    */     //   17	20	17	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\support\NoOpCacheManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */