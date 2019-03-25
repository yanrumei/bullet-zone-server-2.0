/*     */ package org.springframework.boot.autoconfigure.mobile;
/*     */ 
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.mobile.devicedelegatingviewresolver")
/*     */ public class DeviceDelegatingViewResolverProperties
/*     */ {
/*     */   private boolean enableFallback;
/*  38 */   private String normalPrefix = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private String normalSuffix = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private String mobilePrefix = "mobile/";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private String mobileSuffix = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private String tabletPrefix = "tablet/";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private String tabletSuffix = "";
/*     */   
/*     */   public void setEnableFallback(boolean enableFallback) {
/*  66 */     this.enableFallback = enableFallback;
/*     */   }
/*     */   
/*     */   public boolean isEnableFallback() {
/*  70 */     return this.enableFallback;
/*     */   }
/*     */   
/*     */   public String getNormalPrefix() {
/*  74 */     return this.normalPrefix;
/*     */   }
/*     */   
/*     */   public void setNormalPrefix(String normalPrefix) {
/*  78 */     this.normalPrefix = normalPrefix;
/*     */   }
/*     */   
/*     */   public String getNormalSuffix() {
/*  82 */     return this.normalSuffix;
/*     */   }
/*     */   
/*     */   public void setNormalSuffix(String normalSuffix) {
/*  86 */     this.normalSuffix = normalSuffix;
/*     */   }
/*     */   
/*     */   public String getMobilePrefix() {
/*  90 */     return this.mobilePrefix;
/*     */   }
/*     */   
/*     */   public void setMobilePrefix(String mobilePrefix) {
/*  94 */     this.mobilePrefix = mobilePrefix;
/*     */   }
/*     */   
/*     */   public String getMobileSuffix() {
/*  98 */     return this.mobileSuffix;
/*     */   }
/*     */   
/*     */   public void setMobileSuffix(String mobileSuffix) {
/* 102 */     this.mobileSuffix = mobileSuffix;
/*     */   }
/*     */   
/*     */   public String getTabletPrefix() {
/* 106 */     return this.tabletPrefix;
/*     */   }
/*     */   
/*     */   public void setTabletPrefix(String tabletPrefix) {
/* 110 */     this.tabletPrefix = tabletPrefix;
/*     */   }
/*     */   
/*     */   public String getTabletSuffix() {
/* 114 */     return this.tabletSuffix;
/*     */   }
/*     */   
/*     */   public void setTabletSuffix(String tabletSuffix) {
/* 118 */     this.tabletSuffix = tabletSuffix;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mobile\DeviceDelegatingViewResolverProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */