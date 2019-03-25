/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.interceptor.CacheErrorHandler;
/*    */ import org.springframework.cache.interceptor.CacheResolver;
/*    */ import org.springframework.cache.interceptor.KeyGenerator;
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
/*    */ public class CachingConfigurerSupport
/*    */   implements CachingConfigurer
/*    */ {
/*    */   public CacheManager cacheManager()
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public KeyGenerator keyGenerator()
/*    */   {
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public CacheResolver cacheResolver()
/*    */   {
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public CacheErrorHandler errorHandler()
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\CachingConfigurerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */