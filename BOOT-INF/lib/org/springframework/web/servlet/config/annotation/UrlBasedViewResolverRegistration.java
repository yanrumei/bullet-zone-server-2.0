/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
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
/*     */ public class UrlBasedViewResolverRegistration
/*     */ {
/*     */   protected final UrlBasedViewResolver viewResolver;
/*     */   
/*     */   public UrlBasedViewResolverRegistration(UrlBasedViewResolver viewResolver)
/*     */   {
/*  36 */     this.viewResolver = viewResolver;
/*     */   }
/*     */   
/*     */   protected UrlBasedViewResolver getViewResolver()
/*     */   {
/*  41 */     return this.viewResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration prefix(String prefix)
/*     */   {
/*  49 */     this.viewResolver.setPrefix(prefix);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration suffix(String suffix)
/*     */   {
/*  58 */     this.viewResolver.setSuffix(suffix);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration viewClass(Class<?> viewClass)
/*     */   {
/*  67 */     this.viewResolver.setViewClass(viewClass);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration viewNames(String... viewNames)
/*     */   {
/*  78 */     this.viewResolver.setViewNames(viewNames);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration attributes(Map<String, ?> attributes)
/*     */   {
/*  89 */     this.viewResolver.setAttributesMap(attributes);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration cacheLimit(int cacheLimit)
/*     */   {
/*  99 */     this.viewResolver.setCacheLimit(cacheLimit);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration cache(boolean cache)
/*     */   {
/* 112 */     this.viewResolver.setCache(cache);
/* 113 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\UrlBasedViewResolverRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */