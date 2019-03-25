/*     */ package org.springframework.boot.autoconfigure.cache;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import org.infinispan.configuration.cache.ConfigurationBuilder;
/*     */ import org.infinispan.manager.DefaultCacheManager;
/*     */ import org.infinispan.manager.EmbeddedCacheManager;
/*     */ import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @ConditionalOnClass({SpringEmbeddedCacheManager.class})
/*     */ @ConditionalOnMissingBean({CacheManager.class})
/*     */ @Conditional({CacheCondition.class})
/*     */ public class InfinispanCacheConfiguration
/*     */ {
/*     */   private final CacheProperties cacheProperties;
/*     */   private final CacheManagerCustomizers customizers;
/*     */   private final ConfigurationBuilder defaultConfigurationBuilder;
/*     */   
/*     */   public InfinispanCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, ObjectProvider<ConfigurationBuilder> defaultConfigurationBuilder)
/*     */   {
/*  60 */     this.cacheProperties = cacheProperties;
/*  61 */     this.customizers = customizers;
/*  62 */     this.defaultConfigurationBuilder = ((ConfigurationBuilder)defaultConfigurationBuilder.getIfAvailable());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public SpringEmbeddedCacheManager cacheManager(EmbeddedCacheManager embeddedCacheManager)
/*     */   {
/*  68 */     SpringEmbeddedCacheManager cacheManager = new SpringEmbeddedCacheManager(embeddedCacheManager);
/*     */     
/*  70 */     return (SpringEmbeddedCacheManager)this.customizers.customize(cacheManager);
/*     */   }
/*     */   
/*     */   @Bean(destroyMethod="stop")
/*     */   @ConditionalOnMissingBean
/*     */   public EmbeddedCacheManager infinispanCacheManager() throws IOException {
/*  76 */     EmbeddedCacheManager cacheManager = createEmbeddedCacheManager();
/*  77 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/*  78 */     if (!CollectionUtils.isEmpty(cacheNames)) {
/*  79 */       for (String cacheName : cacheNames) {
/*  80 */         cacheManager.defineConfiguration(cacheName, 
/*  81 */           getDefaultCacheConfiguration());
/*     */       }
/*     */     }
/*  84 */     return cacheManager;
/*     */   }
/*     */   
/*     */   private EmbeddedCacheManager createEmbeddedCacheManager() throws IOException
/*     */   {
/*  89 */     Resource location = this.cacheProperties.resolveConfigLocation(this.cacheProperties.getInfinispan().getConfig());
/*  90 */     if (location != null) {
/*  91 */       InputStream in = location.getInputStream();
/*     */       try {
/*  93 */         return new DefaultCacheManager(in);
/*     */       }
/*     */       finally {
/*  96 */         in.close();
/*     */       }
/*     */     }
/*  99 */     return new DefaultCacheManager();
/*     */   }
/*     */   
/*     */   private org.infinispan.configuration.cache.Configuration getDefaultCacheConfiguration() {
/* 103 */     if (this.defaultConfigurationBuilder != null) {
/* 104 */       return this.defaultConfigurationBuilder.build();
/*     */     }
/* 106 */     return new ConfigurationBuilder().build();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\InfinispanCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */