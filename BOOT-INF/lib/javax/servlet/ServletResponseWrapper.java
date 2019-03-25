/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
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
/*     */ public class ServletResponseWrapper
/*     */   implements ServletResponse
/*     */ {
/*     */   private ServletResponse response;
/*     */   
/*     */   public ServletResponseWrapper(ServletResponse response)
/*     */   {
/*  44 */     if (response == null) {
/*  45 */       throw new IllegalArgumentException("Response cannot be null");
/*     */     }
/*  47 */     this.response = response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletResponse getResponse()
/*     */   {
/*  56 */     return this.response;
/*     */   }
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
/*  68 */     if (response == null) {
/*  69 */       throw new IllegalArgumentException("Response cannot be null");
/*     */     }
/*  71 */     this.response = response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterEncoding(String charset)
/*     */   {
/*  82 */     this.response.setCharacterEncoding(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  91 */     return this.response.getCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletOutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 100 */     return this.response.getOutputStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PrintWriter getWriter()
/*     */     throws IOException
/*     */   {
/* 109 */     return this.response.getWriter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentLength(int len)
/*     */   {
/* 118 */     this.response.setContentLength(len);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentLengthLong(long length)
/*     */   {
/* 129 */     this.response.setContentLengthLong(length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String type)
/*     */   {
/* 138 */     this.response.setContentType(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 149 */     return this.response.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBufferSize(int size)
/*     */   {
/* 158 */     this.response.setBufferSize(size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/* 167 */     return this.response.getBufferSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 176 */     this.response.flushBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCommitted()
/*     */   {
/* 185 */     return this.response.isCommitted();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 194 */     this.response.reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetBuffer()
/*     */   {
/* 203 */     this.response.resetBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale loc)
/*     */   {
/* 212 */     this.response.setLocale(loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 221 */     return this.response.getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWrapperFor(ServletResponse wrapped)
/*     */   {
/* 233 */     if (this.response == wrapped) {
/* 234 */       return true;
/*     */     }
/* 236 */     if ((this.response instanceof ServletResponseWrapper)) {
/* 237 */       return ((ServletResponseWrapper)this.response).isWrapperFor(wrapped);
/*     */     }
/* 239 */     return false;
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
/*     */   public boolean isWrapperFor(Class<?> wrappedType)
/*     */   {
/* 252 */     if (wrappedType.isAssignableFrom(this.response.getClass())) {
/* 253 */       return true;
/*     */     }
/* 255 */     if ((this.response instanceof ServletResponseWrapper)) {
/* 256 */       return ((ServletResponseWrapper)this.response).isWrapperFor(wrappedType);
/*     */     }
/* 258 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletResponseWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */