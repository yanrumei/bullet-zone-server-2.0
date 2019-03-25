/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
/*    */ import org.springframework.cache.interceptor.CacheInterceptor;
/*    */ import org.springframework.cache.interceptor.CacheOperationSource;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Role;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
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
/*    */ public class ProxyCachingConfiguration
/*    */   extends AbstractCachingConfiguration
/*    */ {
/*    */   @Bean(name={"org.springframework.cache.config.internalCacheAdvisor"})
/*    */   @Role(2)
/*    */   public BeanFactoryCacheOperationSourceAdvisor cacheAdvisor()
/*    */   {
/* 43 */     BeanFactoryCacheOperationSourceAdvisor advisor = new BeanFactoryCacheOperationSourceAdvisor();
/*    */     
/* 45 */     advisor.setCacheOperationSource(cacheOperationSource());
/* 46 */     advisor.setAdvice(cacheInterceptor());
/* 47 */     advisor.setOrder(((Integer)this.enableCaching.getNumber("order")).intValue());
/* 48 */     return advisor;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheOperationSource cacheOperationSource() {
/* 54 */     return new AnnotationCacheOperationSource();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @Role(2)
/*    */   public CacheInterceptor cacheInterceptor() {
/* 60 */     CacheInterceptor interceptor = new CacheInterceptor();
/* 61 */     interceptor.setCacheOperationSources(new CacheOperationSource[] { cacheOperationSource() });
/* 62 */     if (this.cacheResolver != null) {
/* 63 */       interceptor.setCacheResolver(this.cacheResolver);
/*    */     }
/* 65 */     else if (this.cacheManager != null) {
/* 66 */       interceptor.setCacheManager(this.cacheManager);
/*    */     }
/* 68 */     if (this.keyGenerator != null) {
/* 69 */       interceptor.setKeyGenerator(this.keyGenerator);
/*    */     }
/* 71 */     if (this.errorHandler != null) {
/* 72 */       interceptor.setErrorHandler(this.errorHandler);
/*    */     }
/* 74 */     return interceptor;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\ProxyCachingConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */