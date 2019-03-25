/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Conditional;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.redis.cache.RedisCacheManager;
/*    */ import org.springframework.data.redis.core.RedisTemplate;
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
/*    */ @AutoConfigureAfter({RedisAutoConfiguration.class})
/*    */ @ConditionalOnBean({RedisTemplate.class})
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ class RedisCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizerInvoker;
/*    */   
/*    */   RedisCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizerInvoker)
/*    */   {
/* 51 */     this.cacheProperties = cacheProperties;
/* 52 */     this.customizerInvoker = customizerInvoker;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public RedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
/* 57 */     RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
/* 58 */     cacheManager.setUsePrefix(true);
/* 59 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/* 60 */     if (!cacheNames.isEmpty()) {
/* 61 */       cacheManager.setCacheNames(cacheNames);
/*    */     }
/* 63 */     return (RedisCacheManager)this.customizerInvoker.customize(cacheManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\RedisCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */