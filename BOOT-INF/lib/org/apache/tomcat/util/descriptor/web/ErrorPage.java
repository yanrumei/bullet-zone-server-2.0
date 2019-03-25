/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
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
/*     */ public class ErrorPage
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private int errorCode = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private String exceptionType = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private String location = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getErrorCode()
/*     */   {
/*  64 */     return this.errorCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorCode(int errorCode)
/*     */   {
/*  76 */     this.errorCode = errorCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setErrorCode(String errorCode)
/*     */   {
/*     */     try
/*     */     {
/*  89 */       this.errorCode = Integer.parseInt(errorCode);
/*     */     } catch (NumberFormatException nfe) {
/*  91 */       throw new IllegalArgumentException(nfe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExceptionType()
/*     */   {
/* 101 */     return this.exceptionType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExceptionType(String exceptionType)
/*     */   {
/* 113 */     this.exceptionType = exceptionType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 123 */     return this.location;
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
/*     */ 
/*     */ 
/*     */   public void setLocation(String location)
/*     */   {
/* 138 */     this.location = UDecoder.URLDecode(location);
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
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     StringBuilder sb = new StringBuilder("ErrorPage[");
/* 153 */     if (this.exceptionType == null) {
/* 154 */       sb.append("errorCode=");
/* 155 */       sb.append(this.errorCode);
/*     */     } else {
/* 157 */       sb.append("exceptionType=");
/* 158 */       sb.append(this.exceptionType);
/*     */     }
/* 160 */     sb.append(", location=");
/* 161 */     sb.append(this.location);
/* 162 */     sb.append("]");
/* 163 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 168 */     if (this.exceptionType == null) {
/* 169 */       return Integer.toString(this.errorCode);
/*     */     }
/* 171 */     return this.exceptionType;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ErrorPage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */