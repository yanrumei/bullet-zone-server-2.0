/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.support.NoOpCacheManager;
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
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @Conditional({CacheCondition.class})
/*    */ class NoOpCacheConfiguration
/*    */ {
/*    */   @Bean
/*    */   public NoOpCacheManager cacheManager()
/*    */   {
/* 39 */     return new NoOpCacheManager();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\NoOpCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */