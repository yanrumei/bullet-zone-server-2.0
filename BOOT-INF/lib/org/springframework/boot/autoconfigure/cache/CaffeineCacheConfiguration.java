/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import com.github.benmanes.caffeine.cache.CacheLoader;
/*    */ import com.github.benmanes.caffeine.cache.Caffeine;
/*    */ import com.github.benmanes.caffeine.cache.CaffeineSpec;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.caffeine.CaffeineCacheManager;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ class CaffeineCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizers;
/*    */   private final Caffeine<Object, Object> caffeine;
/*    */   private final CaffeineSpec caffeineSpec;
/*    */   private final CacheLoader<Object, Object> cacheLoader;
/*    */   
/*    */   CaffeineCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, ObjectProvider<Caffeine<Object, Object>> caffeine, ObjectProvider<CaffeineSpec> caffeineSpec, ObjectProvider<CacheLoader<Object, Object>> cacheLoader)
/*    */   {
/* 63 */     this.cacheProperties = cacheProperties;
/* 64 */     this.customizers = customizers;
/* 65 */     this.caffeine = ((Caffeine)caffeine.getIfAvailable());
/* 66 */     this.caffeineSpec = ((CaffeineSpec)caffeineSpec.getIfAvailable());
/* 67 */     this.cacheLoader = ((CacheLoader)cacheLoader.getIfAvailable());
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public CaffeineCacheManager cacheManager() {
/* 72 */     CaffeineCacheManager cacheManager = createCacheManager();
/* 73 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/* 74 */     if (!CollectionUtils.isEmpty(cacheNames)) {
/* 75 */       cacheManager.setCacheNames(cacheNames);
/*    */     }
/* 77 */     return (CaffeineCacheManager)this.customizers.customize(cacheManager);
/*    */   }
/*    */   
/*    */   private CaffeineCacheManager createCacheManager() {
/* 81 */     CaffeineCacheManager cacheManager = new CaffeineCacheManager();
/* 82 */     setCacheBuilder(cacheManager);
/* 83 */     if (this.cacheLoader != null) {
/* 84 */       cacheManager.setCacheLoader(this.cacheLoader);
/*    */     }
/* 86 */     return cacheManager;
/*    */   }
/*    */   
/*    */   private void setCacheBuilder(CaffeineCacheManager cacheManager) {
/* 90 */     String specification = this.cacheProperties.getCaffeine().getSpec();
/* 91 */     if (StringUtils.hasText(specification)) {
/* 92 */       cacheManager.setCacheSpecification(specification);
/*    */     }
/* 94 */     else if (this.caffeineSpec != null) {
/* 95 */       cacheManager.setCaffeineSpec(this.caffeineSpec);
/*    */     }
/* 97 */     else if (this.caffeine != null) {
/* 98 */       cacheManager.setCaffeine(this.caffeine);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CaffeineCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */