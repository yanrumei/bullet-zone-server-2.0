/*     */ package org.springframework.boot.autoconfigure.cache;
/*     */ 
/*     */ import com.hazelcast.core.Hazelcast;
/*     */ import com.hazelcast.core.HazelcastInstance;
/*     */ import com.hazelcast.spring.cache.HazelcastCacheManager;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastConfigResourceCondition;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastInstanceFactory;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ abstract class HazelcastInstanceConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnSingleCandidate(HazelcastInstance.class)
/*     */   static class Existing
/*     */   {
/*     */     private final CacheProperties cacheProperties;
/*     */     private final CacheManagerCustomizers customizers;
/*     */     
/*     */     Existing(CacheProperties cacheProperties, CacheManagerCustomizers customizers)
/*     */     {
/*  52 */       this.cacheProperties = cacheProperties;
/*  53 */       this.customizers = customizers;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public HazelcastCacheManager cacheManager(HazelcastInstance existingHazelcastInstance)
/*     */       throws IOException
/*     */     {
/*  60 */       Resource config = this.cacheProperties.getHazelcast().getConfig();
/*  61 */       Resource location = this.cacheProperties.resolveConfigLocation(config);
/*  62 */       if (location != null)
/*     */       {
/*  64 */         HazelcastInstance cacheHazelcastInstance = new HazelcastInstanceFactory(location).getHazelcastInstance();
/*  65 */         return new HazelcastInstanceConfiguration.CloseableHazelcastCacheManager(cacheHazelcastInstance);
/*     */       }
/*  67 */       HazelcastCacheManager cacheManager = new HazelcastCacheManager(existingHazelcastInstance);
/*     */       
/*  69 */       return (HazelcastCacheManager)this.customizers.customize(cacheManager);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({HazelcastInstance.class})
/*     */   @Conditional({HazelcastInstanceConfiguration.ConfigAvailableCondition.class})
/*     */   static class Specific
/*     */   {
/*     */     private final CacheProperties cacheProperties;
/*     */     private final CacheManagerCustomizers customizers;
/*     */     
/*     */     Specific(CacheProperties cacheProperties, CacheManagerCustomizers customizers)
/*     */     {
/*  84 */       this.cacheProperties = cacheProperties;
/*  85 */       this.customizers = customizers;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public HazelcastInstance hazelcastInstance() throws IOException
/*     */     {
/*  91 */       Resource config = this.cacheProperties.getHazelcast().getConfig();
/*  92 */       Resource location = this.cacheProperties.resolveConfigLocation(config);
/*  93 */       if (location != null) {
/*  94 */         return new HazelcastInstanceFactory(location).getHazelcastInstance();
/*     */       }
/*  96 */       return Hazelcast.newHazelcastInstance();
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public HazelcastCacheManager cacheManager() throws IOException
/*     */     {
/* 102 */       HazelcastCacheManager cacheManager = new HazelcastCacheManager(hazelcastInstance());
/* 103 */       return (HazelcastCacheManager)this.customizers.customize(cacheManager);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class ConfigAvailableCondition
/*     */     extends HazelcastConfigResourceCondition
/*     */   {
/*     */     ConfigAvailableCondition()
/*     */     {
/* 115 */       super("config");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CloseableHazelcastCacheManager
/*     */     extends HazelcastCacheManager implements Closeable
/*     */   {
/*     */     private final HazelcastInstance hazelcastInstance;
/*     */     
/*     */     CloseableHazelcastCacheManager(HazelcastInstance hazelcastInstance)
/*     */     {
/* 126 */       super();
/* 127 */       this.hazelcastInstance = hazelcastInstance;
/*     */     }
/*     */     
/*     */     public void close() throws IOException
/*     */     {
/* 132 */       this.hazelcastInstance.shutdown();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\HazelcastInstanceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */