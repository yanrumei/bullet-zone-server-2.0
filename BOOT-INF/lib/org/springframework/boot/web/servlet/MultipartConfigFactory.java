/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import javax.servlet.MultipartConfigElement;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class MultipartConfigFactory
/*     */ {
/*     */   private String location;
/*  40 */   private long maxFileSize = -1L;
/*     */   
/*  42 */   private long maxRequestSize = -1L;
/*     */   
/*  44 */   private int fileSizeThreshold = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocation(String location)
/*     */   {
/*  51 */     this.location = location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxFileSize(long maxFileSize)
/*     */   {
/*  60 */     this.maxFileSize = maxFileSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxFileSize(String maxFileSize)
/*     */   {
/*  70 */     this.maxFileSize = parseSize(maxFileSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxRequestSize(long maxRequestSize)
/*     */   {
/*  79 */     this.maxRequestSize = maxRequestSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxRequestSize(String maxRequestSize)
/*     */   {
/*  89 */     this.maxRequestSize = parseSize(maxRequestSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFileSizeThreshold(int fileSizeThreshold)
/*     */   {
/*  98 */     this.fileSizeThreshold = fileSizeThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFileSizeThreshold(String fileSizeThreshold)
/*     */   {
/* 108 */     this.fileSizeThreshold = ((int)parseSize(fileSizeThreshold));
/*     */   }
/*     */   
/*     */   private long parseSize(String size) {
/* 112 */     Assert.hasLength(size, "Size must not be empty");
/* 113 */     size = size.toUpperCase();
/* 114 */     if (size.endsWith("KB")) {
/* 115 */       return Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L;
/*     */     }
/* 117 */     if (size.endsWith("MB")) {
/* 118 */       return Long.valueOf(size.substring(0, size.length() - 2)).longValue() * 1024L * 1024L;
/*     */     }
/* 120 */     return Long.valueOf(size).longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultipartConfigElement createMultipartConfig()
/*     */   {
/* 128 */     return new MultipartConfigElement(this.location, this.maxFileSize, this.maxRequestSize, this.fileSizeThreshold);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\MultipartConfigFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */