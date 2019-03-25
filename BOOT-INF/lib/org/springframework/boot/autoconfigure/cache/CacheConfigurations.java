/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ final class CacheConfigurations
/*    */ {
/*    */   private static final Map<CacheType, Class<?>> MAPPINGS;
/*    */   
/*    */   static
/*    */   {
/* 36 */     Map<CacheType, Class<?>> mappings = new HashMap();
/* 37 */     mappings.put(CacheType.GENERIC, GenericCacheConfiguration.class);
/* 38 */     mappings.put(CacheType.EHCACHE, EhCacheCacheConfiguration.class);
/* 39 */     mappings.put(CacheType.HAZELCAST, HazelcastCacheConfiguration.class);
/* 40 */     mappings.put(CacheType.INFINISPAN, InfinispanCacheConfiguration.class);
/* 41 */     mappings.put(CacheType.JCACHE, JCacheCacheConfiguration.class);
/* 42 */     mappings.put(CacheType.COUCHBASE, CouchbaseCacheConfiguration.class);
/* 43 */     mappings.put(CacheType.REDIS, RedisCacheConfiguration.class);
/* 44 */     mappings.put(CacheType.CAFFEINE, CaffeineCacheConfiguration.class);
/* 45 */     addGuavaMapping(mappings);
/* 46 */     mappings.put(CacheType.SIMPLE, SimpleCacheConfiguration.class);
/* 47 */     mappings.put(CacheType.NONE, NoOpCacheConfiguration.class);
/* 48 */     MAPPINGS = Collections.unmodifiableMap(mappings);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   private static void addGuavaMapping(Map<CacheType, Class<?>> mappings) {
/* 53 */     mappings.put(CacheType.GUAVA, GuavaCacheConfiguration.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static String getConfigurationClass(CacheType cacheType)
/*    */   {
/* 60 */     Class<?> configurationClass = (Class)MAPPINGS.get(cacheType);
/* 61 */     Assert.state(configurationClass != null, "Unknown cache type " + cacheType);
/* 62 */     return configurationClass.getName();
/*    */   }
/*    */   
/*    */   public static CacheType getType(String configurationClassName) {
/* 66 */     for (Map.Entry<CacheType, Class<?>> entry : MAPPINGS.entrySet()) {
/* 67 */       if (((Class)entry.getValue()).getName().equals(configurationClassName)) {
/* 68 */         return (CacheType)entry.getKey();
/*    */       }
/*    */     }
/* 71 */     throw new IllegalStateException("Unknown configuration class " + configurationClassName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheConfigurations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */