/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.web.servlet.MultipartConfigFactory;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @ConfigurationProperties(prefix="spring.http.multipart", ignoreUnknownFields=false)
/*     */ public class MultipartProperties
/*     */ {
/*  52 */   private boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String location;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private String maxFileSize = "1MB";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private String maxRequestSize = "10MB";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private String fileSizeThreshold = "0";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private boolean resolveLazily = false;
/*     */   
/*     */   public boolean getEnabled() {
/*  84 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/*  88 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  92 */     return this.location;
/*     */   }
/*     */   
/*     */   public void setLocation(String location) {
/*  96 */     this.location = location;
/*     */   }
/*     */   
/*     */   public String getMaxFileSize() {
/* 100 */     return this.maxFileSize;
/*     */   }
/*     */   
/*     */   public void setMaxFileSize(String maxFileSize) {
/* 104 */     this.maxFileSize = maxFileSize;
/*     */   }
/*     */   
/*     */   public String getMaxRequestSize() {
/* 108 */     return this.maxRequestSize;
/*     */   }
/*     */   
/*     */   public void setMaxRequestSize(String maxRequestSize) {
/* 112 */     this.maxRequestSize = maxRequestSize;
/*     */   }
/*     */   
/*     */   public String getFileSizeThreshold() {
/* 116 */     return this.fileSizeThreshold;
/*     */   }
/*     */   
/*     */   public void setFileSizeThreshold(String fileSizeThreshold) {
/* 120 */     this.fileSizeThreshold = fileSizeThreshold;
/*     */   }
/*     */   
/*     */   public boolean isResolveLazily() {
/* 124 */     return this.resolveLazily;
/*     */   }
/*     */   
/*     */   public void setResolveLazily(boolean resolveLazily) {
/* 128 */     this.resolveLazily = resolveLazily;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipartConfigElement createMultipartConfig()
/*     */   {
/* 136 */     MultipartConfigFactory factory = new MultipartConfigFactory();
/* 137 */     if (StringUtils.hasText(this.fileSizeThreshold)) {
/* 138 */       factory.setFileSizeThreshold(this.fileSizeThreshold);
/*     */     }
/* 140 */     if (StringUtils.hasText(this.location)) {
/* 141 */       factory.setLocation(this.location);
/*     */     }
/* 143 */     if (StringUtils.hasText(this.maxRequestSize)) {
/* 144 */       factory.setMaxRequestSize(this.maxRequestSize);
/*     */     }
/* 146 */     if (StringUtils.hasText(this.maxFileSize)) {
/* 147 */       factory.setMaxFileSize(this.maxFileSize);
/*     */     }
/* 149 */     return factory.createMultipartConfig();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\MultipartProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */