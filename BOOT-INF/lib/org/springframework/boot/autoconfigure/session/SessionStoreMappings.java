/*    */ package org.springframework.boot.autoconfigure.session;
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
/*    */ final class SessionStoreMappings
/*    */ {
/*    */   private static final Map<StoreType, Class<?>> MAPPINGS;
/*    */   
/*    */   static
/*    */   {
/* 36 */     Map<StoreType, Class<?>> mappings = new HashMap();
/* 37 */     mappings.put(StoreType.REDIS, RedisSessionConfiguration.class);
/* 38 */     mappings.put(StoreType.MONGO, MongoSessionConfiguration.class);
/* 39 */     mappings.put(StoreType.JDBC, JdbcSessionConfiguration.class);
/* 40 */     mappings.put(StoreType.HAZELCAST, HazelcastSessionConfiguration.class);
/* 41 */     mappings.put(StoreType.HASH_MAP, HashMapSessionConfiguration.class);
/* 42 */     mappings.put(StoreType.NONE, NoOpSessionConfiguration.class);
/* 43 */     MAPPINGS = Collections.unmodifiableMap(mappings);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static String getConfigurationClass(StoreType sessionStoreType)
/*    */   {
/* 50 */     Class<?> configurationClass = (Class)MAPPINGS.get(sessionStoreType);
/* 51 */     Assert.state(configurationClass != null, "Unknown session store type " + sessionStoreType);
/*    */     
/* 53 */     return configurationClass.getName();
/*    */   }
/*    */   
/*    */   public static StoreType getType(String configurationClassName) {
/* 57 */     for (Map.Entry<StoreType, Class<?>> entry : MAPPINGS.entrySet()) {
/* 58 */       if (((Class)entry.getValue()).getName().equals(configurationClassName)) {
/* 59 */         return (StoreType)entry.getKey();
/*    */       }
/*    */     }
/* 62 */     throw new IllegalStateException("Unknown configuration class " + configurationClassName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\SessionStoreMappings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */