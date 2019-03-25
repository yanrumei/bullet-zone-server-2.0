/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.support.SimpleCacheManager;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
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
/*    */ @Configuration
/*    */ @ConditionalOnBean({Cache.class})
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ class GenericCacheConfiguration
/*    */ {
/*    */   private final CacheManagerCustomizers customizers;
/*    */   
/*    */   GenericCacheConfiguration(CacheManagerCustomizers customizers)
/*    */   {
/* 46 */     this.customizers = customizers;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public SimpleCacheManager cacheManager(Collection<Cache> caches) {
/* 51 */     SimpleCacheManager cacheManager = new SimpleCacheManager();
/* 52 */     cacheManager.setCaches(caches);
/* 53 */     return (SimpleCacheManager)this.customizers.customize(cacheManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\GenericCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */