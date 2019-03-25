/*    */ package org.springframework.boot.autoconfigure.cache;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ public class CacheManagerCustomizers
/*    */ {
/* 38 */   private static final Log logger = LogFactory.getLog(CacheManagerCustomizers.class);
/*    */   
/*    */ 
/*    */   private final List<CacheManagerCustomizer<?>> customizers;
/*    */   
/*    */ 
/*    */   public CacheManagerCustomizers(List<? extends CacheManagerCustomizer<?>> customizers)
/*    */   {
/* 46 */     this.customizers = (customizers != null ? new ArrayList(customizers) : Collections.emptyList());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public <T extends CacheManager> T customize(T cacheManager)
/*    */   {
/* 58 */     for (CacheManagerCustomizer<?> customizer : this.customizers)
/*    */     {
/*    */ 
/* 61 */       Class<?> generic = ResolvableType.forClass(CacheManagerCustomizer.class, customizer.getClass()).resolveGeneric(new int[0]);
/* 62 */       if (generic.isInstance(cacheManager)) {
/* 63 */         customize(cacheManager, customizer);
/*    */       }
/*    */     }
/* 66 */     return cacheManager;
/*    */   }
/*    */   
/*    */   private void customize(CacheManager cacheManager, CacheManagerCustomizer customizer)
/*    */   {
/*    */     try {
/* 72 */       customizer.customize(cacheManager);
/*    */ 
/*    */     }
/*    */     catch (ClassCastException ex)
/*    */     {
/* 77 */       if (logger.isDebugEnabled()) {
/* 78 */         logger.debug("Non-matching cache manager type for customizer: " + customizer, ex);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\cache\CacheManagerCustomizers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */