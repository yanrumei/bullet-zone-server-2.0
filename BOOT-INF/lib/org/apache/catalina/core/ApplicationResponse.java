/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.ServletResponseWrapper;
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
/*     */ class ApplicationResponse
/*     */   extends ServletResponseWrapper
/*     */ {
/*     */   public ApplicationResponse(ServletResponse response, boolean included)
/*     */   {
/*  52 */     super(response);
/*  53 */     setIncluded(included);
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
/*  65 */   protected boolean included = false;
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
/*     */   public void reset()
/*     */   {
/*  81 */     if ((!this.included) || (getResponse().isCommitted())) {
/*  82 */       getResponse().reset();
/*     */     }
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
/*     */   public void setContentLength(int len)
/*     */   {
/*  96 */     if (!this.included) {
/*  97 */       getResponse().setContentLength(len);
/*     */     }
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
/*     */   public void setContentLengthLong(long len)
/*     */   {
/* 111 */     if (!this.included) {
/* 112 */       getResponse().setContentLengthLong(len);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String type)
/*     */   {
/* 125 */     if (!this.included) {
/* 126 */       getResponse().setContentType(type);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale loc)
/*     */   {
/* 138 */     if (!this.included) {
/* 139 */       getResponse().setLocale(loc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBufferSize(int size)
/*     */   {
/* 150 */     if (!this.included) {
/* 151 */       getResponse().setBufferSize(size);
/*     */     }
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
/*     */   public void setResponse(ServletResponse response)
/*     */   {
/* 166 */     super.setResponse(response);
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
/*     */   void setIncluded(boolean included)
/*     */   {
/* 180 */     this.included = included;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */