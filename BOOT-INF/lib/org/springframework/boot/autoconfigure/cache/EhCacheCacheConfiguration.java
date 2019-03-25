/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import net.sf.ehcache.Cache;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ResourceCondition;
/*    */ import org.springframework.cache.ehcache.EhCacheCacheManager;
/*    */ import org.springframework.cache.ehcache.EhCacheManagerUtils;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ @ConditionalOnClass({Cache.class, EhCacheCacheManager.class})
/*    */ @ConditionalOnMissingBean({org.springframework.cache.CacheManager.class})
/*    */ @Conditional({CacheCondition.class, ConfigAvailableCondition.class})
/*    */ class EhCacheCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizers;
/*    */   
/*    */   EhCacheCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers)
/*    */   {
/* 53 */     this.cacheProperties = cacheProperties;
/* 54 */     this.customizers = customizers;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager ehCacheCacheManager) {
/* 59 */     return (EhCacheCacheManager)this.customizers.customize(new EhCacheCacheManager(ehCacheCacheManager));
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public net.sf.ehcache.CacheManager ehCacheCacheManager()
/*    */   {
/* 66 */     Resource location = this.cacheProperties.resolveConfigLocation(this.cacheProperties.getEhcache().getConfig());
/* 67 */     if (location != null) {
/* 68 */       return EhCacheManagerUtils.buildCacheManager(location);
/*    */     }
/* 70 */     return EhCacheManagerUtils.buildCacheManager();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static class ConfigAvailableCondition
/*    */     extends ResourceCondition
/*    */   {
/*    */     ConfigAvailableCondition()
/*    */     {
/* 81 */       super("spring.cache.ehcache", "config", new String[] { "classpath:/ehcache.xml" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\EhCacheCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */