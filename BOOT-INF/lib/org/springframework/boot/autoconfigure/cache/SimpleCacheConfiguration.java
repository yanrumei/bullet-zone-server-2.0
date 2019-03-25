/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
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
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ class SimpleCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizerInvoker;
/*    */   
/*    */   SimpleCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizerInvoker)
/*    */   {
/* 45 */     this.cacheProperties = cacheProperties;
/* 46 */     this.customizerInvoker = customizerInvoker;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public ConcurrentMapCacheManager cacheManager() {
/* 51 */     ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
/* 52 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/* 53 */     if (!cacheNames.isEmpty()) {
/* 54 */       cacheManager.setCacheNames(cacheNames);
/*    */     }
/* 56 */     return (ConcurrentMapCacheManager)this.customizerInvoker.customize(cacheManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\SimpleCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */