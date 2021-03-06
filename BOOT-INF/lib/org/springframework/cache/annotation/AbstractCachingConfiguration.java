/*    */ package org.springframework.cache.annotation;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.cache.interceptor.CacheErrorHandler;
/*    */ import org.springframework.cache.interceptor.CacheResolver;
/*    */ import org.springframework.cache.interceptor.KeyGenerator;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.ImportAware;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public abstract class AbstractCachingConfiguration
/*    */   implements ImportAware
/*    */ {
/*    */   protected AnnotationAttributes enableCaching;
/*    */   protected CacheManager cacheManager;
/*    */   protected CacheResolver cacheResolver;
/*    */   protected KeyGenerator keyGenerator;
/*    */   protected CacheErrorHandler errorHandler;
/*    */   
/*    */   public void setImportMetadata(AnnotationMetadata importMetadata)
/*    */   {
/* 57 */     this.enableCaching = AnnotationAttributes.fromMap(importMetadata
/* 58 */       .getAnnotationAttributes(EnableCaching.class.getName(), false));
/* 59 */     if (this.enableCaching == null)
/*    */     {
/* 61 */       throw new IllegalArgumentException("@EnableCaching is not present on importing class " + importMetadata.getClassName());
/*    */     }
/*    */   }
/*    */   
/*    */   @Autowired(required=false)
/*    */   void setConfigurers(Collection<CachingConfigurer> configurers) {
/* 67 */     if (CollectionUtils.isEmpty(configurers)) {
/* 68 */       return;
/*    */     }
/* 70 */     if (configurers.size() > 1) {
/* 71 */       throw new IllegalStateException(configurers.size() + " implementations of CachingConfigurer were found when only 1 was expected. Refactor the configuration such that CachingConfigurer is implemented only once or not at all.");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 76 */     CachingConfigurer configurer = (CachingConfigurer)configurers.iterator().next();
/* 77 */     useCachingConfigurer(configurer);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void useCachingConfigurer(CachingConfigurer config)
/*    */   {
/* 84 */     this.cacheManager = config.cacheManager();
/* 85 */     this.cacheResolver = config.cacheResolver();
/* 86 */     this.keyGenerator = config.keyGenerator();
/* 87 */     this.errorHandler = config.errorHandler();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\AbstractCachingConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */