/*     */ package org.springframework.boot.web.servlet;
/*     */ 
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ErrorPage
/*     */ {
/*     */   private final HttpStatus status;
/*     */   private final Class<? extends Throwable> exception;
/*     */   private final String path;
/*     */   
/*     */   public ErrorPage(String path)
/*     */   {
/*  38 */     this.status = null;
/*  39 */     this.exception = null;
/*  40 */     this.path = path;
/*     */   }
/*     */   
/*     */   public ErrorPage(HttpStatus status, String path) {
/*  44 */     this.status = status;
/*  45 */     this.exception = null;
/*  46 */     this.path = path;
/*     */   }
/*     */   
/*     */   public ErrorPage(Class<? extends Throwable> exception, String path) {
/*  50 */     this.status = null;
/*  51 */     this.exception = exception;
/*  52 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/*  62 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<? extends Throwable> getException()
/*     */   {
/*  70 */     return this.exception;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpStatus getStatus()
/*     */   {
/*  79 */     return this.status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatusCode()
/*     */   {
/*  87 */     return this.status == null ? 0 : this.status.value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExceptionName()
/*     */   {
/*  95 */     return this.exception == null ? null : this.exception.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isGlobal()
/*     */   {
/* 104 */     return (this.status == null) && (this.exception == null);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 109 */     int prime = 31;
/* 110 */     int result = 1;
/* 111 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getExceptionName());
/* 112 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.path);
/* 113 */     result = 31 * result + getStatusCode();
/* 114 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 119 */     if (this == obj) {
/* 120 */       return true;
/*     */     }
/* 122 */     if (obj == null) {
/* 123 */       return false;
/*     */     }
/* 125 */     if ((obj instanceof ErrorPage)) {
/* 126 */       ErrorPage other = (ErrorPage)obj;
/* 127 */       boolean rtn = true;
/* 128 */       rtn = (rtn) && (ObjectUtils.nullSafeEquals(getExceptionName(), other
/* 129 */         .getExceptionName()));
/* 130 */       rtn = (rtn) && (ObjectUtils.nullSafeEquals(this.path, other.path));
/* 131 */       rtn = (rtn) && (this.status == other.status);
/* 132 */       return rtn;
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ErrorPage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */