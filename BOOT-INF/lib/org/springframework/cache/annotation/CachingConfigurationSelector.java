/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.context.annotation.AdviceMode;
/*     */ import org.springframework.context.annotation.AdviceModeImportSelector;
/*     */ import org.springframework.context.annotation.AutoProxyRegistrar;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachingConfigurationSelector
/*     */   extends AdviceModeImportSelector<EnableCaching>
/*     */ {
/*     */   private static final String PROXY_JCACHE_CONFIGURATION_CLASS = "org.springframework.cache.jcache.config.ProxyJCacheConfiguration";
/*     */   private static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJCachingConfiguration";
/*     */   private static final String JCACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJJCacheConfiguration";
/*  52 */   private static final boolean jsr107Present = ClassUtils.isPresent("javax.cache.Cache", CachingConfigurationSelector.class
/*  53 */     .getClassLoader());
/*     */   
/*  55 */   private static final boolean jcacheImplPresent = ClassUtils.isPresent("org.springframework.cache.jcache.config.ProxyJCacheConfiguration", CachingConfigurationSelector.class
/*  56 */     .getClassLoader());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] selectImports(AdviceMode adviceMode)
/*     */   {
/*  66 */     switch (adviceMode) {
/*     */     case PROXY: 
/*  68 */       return getProxyImports();
/*     */     case ASPECTJ: 
/*  70 */       return getAspectJImports();
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] getProxyImports()
/*     */   {
/*  81 */     List<String> result = new ArrayList();
/*  82 */     result.add(AutoProxyRegistrar.class.getName());
/*  83 */     result.add(ProxyCachingConfiguration.class.getName());
/*  84 */     if ((jsr107Present) && (jcacheImplPresent)) {
/*  85 */       result.add("org.springframework.cache.jcache.config.ProxyJCacheConfiguration");
/*     */     }
/*  87 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] getAspectJImports()
/*     */   {
/*  95 */     List<String> result = new ArrayList();
/*  96 */     result.add("org.springframework.cache.aspectj.AspectJCachingConfiguration");
/*  97 */     if ((jsr107Present) && (jcacheImplPresent)) {
/*  98 */       result.add("org.springframework.cache.aspectj.AspectJJCacheConfiguration");
/*     */     }
/* 100 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\annotation\CachingConfigurationSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */