/*     */ package org.apache.tomcat.util.http.fileupload.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.tomcat.util.http.fileupload.UploadContext;
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
/*     */ public class ServletRequestContext
/*     */   implements UploadContext
/*     */ {
/*     */   private final HttpServletRequest request;
/*     */   
/*     */   public ServletRequestContext(HttpServletRequest request)
/*     */   {
/*  51 */     this.request = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  63 */     return this.request.getCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/*  73 */     return this.request.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long contentLength()
/*     */   {
/*     */     long size;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  86 */       size = Long.parseLong(this.request.getHeader("Content-length"));
/*     */     } catch (NumberFormatException e) { long size;
/*  88 */       size = this.request.getContentLength();
/*     */     }
/*  90 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 102 */     return this.request.getInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return String.format("ContentLength=%s, ContentType=%s", new Object[] {
/* 113 */       Long.valueOf(contentLength()), 
/* 114 */       getContentType() });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\servlet\ServletRequestContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */