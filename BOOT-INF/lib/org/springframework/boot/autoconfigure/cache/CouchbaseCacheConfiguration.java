/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import com.couchbase.client.java.Bucket;
/*    */ import com.couchbase.client.spring.cache.CacheBuilder;
/*    */ import com.couchbase.client.spring.cache.CouchbaseCacheManager;
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
/*    */ import org.springframework.cache.CacheManager;
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
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @ConditionalOnClass({Bucket.class, CouchbaseCacheManager.class})
/*    */ @ConditionalOnMissingBean({CacheManager.class})
/*    */ @ConditionalOnSingleCandidate(Bucket.class)
/*    */ @Conditional({CacheCondition.class})
/*    */ public class CouchbaseCacheConfiguration
/*    */ {
/*    */   private final CacheProperties cacheProperties;
/*    */   private final CacheManagerCustomizers customizers;
/*    */   private final Bucket bucket;
/*    */   
/*    */   public CouchbaseCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, Bucket bucket)
/*    */   {
/* 54 */     this.cacheProperties = cacheProperties;
/* 55 */     this.customizers = customizers;
/* 56 */     this.bucket = bucket;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public CouchbaseCacheManager cacheManager() {
/* 61 */     List<String> cacheNames = this.cacheProperties.getCacheNames();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 66 */     CouchbaseCacheManager cacheManager = new CouchbaseCacheManager(CacheBuilder.newInstance(this.bucket).withExpiration(this.cacheProperties.getCouchbase().getExpirationSeconds()), (String[])cacheNames.toArray(new String[cacheNames.size()]));
/* 67 */     return (CouchbaseCacheManager)this.customizers.customize(cacheManager);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CouchbaseCacheConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */