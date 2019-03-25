/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheBuilderSpec;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.guava.GuavaCacheManager;
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
/*    */ @ConditionalOnClass({CacheBuilder.class, GuavaCacheManager.class})
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ @Deprecated
/*    */ class GuavaCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizers;
/*    */   private final CacheBuilder<Object, Object> cacheBuilder;
/*    */   private final CacheBuilderSpec cacheBuilderSpec;
/*    */   private final CacheLoader<Object, Object> cacheLoader;
/*    */   
/*    */   GuavaCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, ObjectProvider<CacheBuilder<Object, Object>> cacheBuilder, ObjectProvider<CacheBuilderSpec> cacheBuilderSpec, ObjectProvider<CacheLoader<Object, Object>> cacheLoader)
/*    */   {
/* 64 */     this.cacheProperties = cacheProperties;
/* 65 */     this.customizers = customizers;
/* 66 */     this.cacheBuilder = ((CacheBuilder)cacheBuilder.getIfAvailable());
/* 67 */     this.cacheBuilderSpec = ((CacheBuilderSpec)cacheBuilderSpec.getIfAvailable());
/* 68 */     this.cacheLoader = ((CacheLoader)cacheLoader.getIfAvailable());
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public GuavaCacheManager cacheManager() {
/* 73 */     GuavaCacheManager cacheManager = createCacheManager();
/* 74 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/* 75 */     if (!CollectionUtils.isEmpty(cacheNames)) {
/* 76 */       cacheManager.setCacheNames(cacheNames);
/*    */     }
/* 78 */     return (GuavaCacheManager)this.customizers.customize(cacheManager);
/*    */   }
/*    */   
/*    */   private GuavaCacheManager createCacheManager() {
/* 82 */     GuavaCacheManager cacheManager = new GuavaCacheManager();
/* 83 */     setCacheBuilder(cacheManager);
/* 84 */     if (this.cacheLoader != null) {
/* 85 */       cacheManager.setCacheLoader(this.cacheLoader);
/*    */     }
/* 87 */     return cacheManager;
/*    */   }
/*    */   
/*    */   private void setCacheBuilder(GuavaCacheManager cacheManager) {
/* 91 */     String specification = this.cacheProperties.getGuava().getSpec();
/* 92 */     if (StringUtils.hasText(specification)) {
/* 93 */       cacheManager.setCacheSpecification(specification);
/*    */     }
/* 95 */     else if (this.cacheBuilderSpec != null) {
/* 96 */       cacheManager.setCacheBuilderSpec(this.cacheBuilderSpec);
/*    */     }
/* 98 */     else if (this.cacheBuilder != null) {
/* 99 */       cacheManager.setCacheBuilder(this.cacheBuilder);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\GuavaCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */