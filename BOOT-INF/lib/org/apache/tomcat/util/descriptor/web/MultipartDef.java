/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class MultipartDef
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String location;
/*     */   private String maxFileSize;
/*     */   private String maxRequestSize;
/*     */   private String fileSizeThreshold;
/*     */   
/*     */   public String getLocation()
/*     */   {
/*  35 */     return this.location;
/*     */   }
/*     */   
/*     */   public void setLocation(String location) {
/*  39 */     this.location = location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getMaxFileSize()
/*     */   {
/*  46 */     return this.maxFileSize;
/*     */   }
/*     */   
/*     */   public void setMaxFileSize(String maxFileSize) {
/*  50 */     this.maxFileSize = maxFileSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getMaxRequestSize()
/*     */   {
/*  57 */     return this.maxRequestSize;
/*     */   }
/*     */   
/*     */   public void setMaxRequestSize(String maxRequestSize) {
/*  61 */     this.maxRequestSize = maxRequestSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFileSizeThreshold()
/*     */   {
/*  68 */     return this.fileSizeThreshold;
/*     */   }
/*     */   
/*     */   public void setFileSizeThreshold(String fileSizeThreshold) {
/*  72 */     this.fileSizeThreshold = fileSizeThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  80 */     int prime = 31;
/*  81 */     int result = 1;
/*     */     
/*     */ 
/*     */ 
/*  85 */     result = 31 * result + (this.fileSizeThreshold == null ? 0 : this.fileSizeThreshold.hashCode());
/*     */     
/*  87 */     result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
/*     */     
/*  89 */     result = 31 * result + (this.maxFileSize == null ? 0 : this.maxFileSize.hashCode());
/*     */     
/*  91 */     result = 31 * result + (this.maxRequestSize == null ? 0 : this.maxRequestSize.hashCode());
/*  92 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  97 */     if (this == obj) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (obj == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     if (!(obj instanceof MultipartDef)) {
/* 104 */       return false;
/*     */     }
/* 106 */     MultipartDef other = (MultipartDef)obj;
/* 107 */     if (this.fileSizeThreshold == null) {
/* 108 */       if (other.fileSizeThreshold != null) {
/* 109 */         return false;
/*     */       }
/* 111 */     } else if (!this.fileSizeThreshold.equals(other.fileSizeThreshold)) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (this.location == null) {
/* 115 */       if (other.location != null) {
/* 116 */         return false;
/*     */       }
/* 118 */     } else if (!this.location.equals(other.location)) {
/* 119 */       return false;
/*     */     }
/* 121 */     if (this.maxFileSize == null) {
/* 122 */       if (other.maxFileSize != null) {
/* 123 */         return false;
/*     */       }
/* 125 */     } else if (!this.maxFileSize.equals(other.maxFileSize)) {
/* 126 */       return false;
/*     */     }
/* 128 */     if (this.maxRequestSize == null) {
/* 129 */       if (other.maxRequestSize != null) {
/* 130 */         return false;
/*     */       }
/* 132 */     } else if (!this.maxRequestSize.equals(other.maxRequestSize)) {
/* 133 */       return false;
/*     */     }
/* 135 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\MultipartDef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */