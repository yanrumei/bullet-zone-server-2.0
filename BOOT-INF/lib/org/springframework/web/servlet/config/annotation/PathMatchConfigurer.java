/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathMatchConfigurer
/*     */ {
/*     */   private Boolean suffixPatternMatch;
/*     */   private Boolean trailingSlashMatch;
/*     */   private Boolean registeredSuffixPatternMatch;
/*     */   private UrlPathHelper urlPathHelper;
/*     */   private PathMatcher pathMatcher;
/*     */   
/*     */   public PathMatchConfigurer setUseSuffixPatternMatch(Boolean suffixPatternMatch)
/*     */   {
/*  58 */     this.suffixPatternMatch = suffixPatternMatch;
/*  59 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMatchConfigurer setUseTrailingSlashMatch(Boolean trailingSlashMatch)
/*     */   {
/*  68 */     this.trailingSlashMatch = trailingSlashMatch;
/*  69 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMatchConfigurer setUseRegisteredSuffixPatternMatch(Boolean registeredSuffixPatternMatch)
/*     */   {
/*  82 */     this.registeredSuffixPatternMatch = registeredSuffixPatternMatch;
/*  83 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMatchConfigurer setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/*  93 */     this.urlPathHelper = urlPathHelper;
/*  94 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMatchConfigurer setPathMatcher(PathMatcher pathMatcher)
/*     */   {
/* 103 */     this.pathMatcher = pathMatcher;
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public Boolean isUseSuffixPatternMatch()
/*     */   {
/* 109 */     return this.suffixPatternMatch;
/*     */   }
/*     */   
/*     */   public Boolean isUseTrailingSlashMatch() {
/* 113 */     return this.trailingSlashMatch;
/*     */   }
/*     */   
/*     */   public Boolean isUseRegisteredSuffixPatternMatch() {
/* 117 */     return this.registeredSuffixPatternMatch;
/*     */   }
/*     */   
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 121 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/* 125 */     return this.pathMatcher;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\PathMatchConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */