/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
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
/*     */ class ApplicationHttpResponse
/*     */   extends HttpServletResponseWrapper
/*     */ {
/*     */   public ApplicationHttpResponse(HttpServletResponse response, boolean included)
/*     */   {
/*  55 */     super(response);
/*  56 */     setIncluded(included);
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
/*  68 */   protected boolean included = false;
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
/*  83 */     if ((!this.included) || (getResponse().isCommitted())) {
/*  84 */       getResponse().reset();
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
/*  98 */     if (!this.included) {
/*  99 */       getResponse().setContentLength(len);
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
/* 113 */     if (!this.included) {
/* 114 */       getResponse().setContentLengthLong(len);
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
/* 127 */     if (!this.included) {
/* 128 */       getResponse().setContentType(type);
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
/*     */   public void setLocale(Locale loc)
/*     */   {
/* 141 */     if (!this.included) {
/* 142 */       getResponse().setLocale(loc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBufferSize(int size)
/*     */   {
/* 154 */     if (!this.included) {
/* 155 */       getResponse().setBufferSize(size);
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
/*     */   public void addCookie(Cookie cookie)
/*     */   {
/* 170 */     if (!this.included) {
/* 171 */       ((HttpServletResponse)getResponse()).addCookie(cookie);
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
/*     */   public void addDateHeader(String name, long value)
/*     */   {
/* 185 */     if (!this.included) {
/* 186 */       ((HttpServletResponse)getResponse()).addDateHeader(name, value);
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
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 200 */     if (!this.included) {
/* 201 */       ((HttpServletResponse)getResponse()).addHeader(name, value);
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
/*     */   public void addIntHeader(String name, int value)
/*     */   {
/* 215 */     if (!this.included) {
/* 216 */       ((HttpServletResponse)getResponse()).addIntHeader(name, value);
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
/*     */   public void sendError(int sc)
/*     */     throws IOException
/*     */   {
/* 231 */     if (!this.included) {
/* 232 */       ((HttpServletResponse)getResponse()).sendError(sc);
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
/*     */   public void sendError(int sc, String msg)
/*     */     throws IOException
/*     */   {
/* 248 */     if (!this.included) {
/* 249 */       ((HttpServletResponse)getResponse()).sendError(sc, msg);
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
/*     */   public void sendRedirect(String location)
/*     */     throws IOException
/*     */   {
/* 264 */     if (!this.included) {
/* 265 */       ((HttpServletResponse)getResponse()).sendRedirect(location);
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
/*     */   public void setDateHeader(String name, long value)
/*     */   {
/* 279 */     if (!this.included) {
/* 280 */       ((HttpServletResponse)getResponse()).setDateHeader(name, value);
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
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 294 */     if (!this.included) {
/* 295 */       ((HttpServletResponse)getResponse()).setHeader(name, value);
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
/*     */   public void setIntHeader(String name, int value)
/*     */   {
/* 309 */     if (!this.included) {
/* 310 */       ((HttpServletResponse)getResponse()).setIntHeader(name, value);
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
/*     */   public void setStatus(int sc)
/*     */   {
/* 323 */     if (!this.included) {
/* 324 */       ((HttpServletResponse)getResponse()).setStatus(sc);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setStatus(int sc, String msg)
/*     */   {
/* 343 */     if (!this.included) {
/* 344 */       ((HttpServletResponse)getResponse()).setStatus(sc, msg);
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
/*     */   void setIncluded(boolean included)
/*     */   {
/* 358 */     this.included = included;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setResponse(HttpServletResponse response)
/*     */   {
/* 370 */     super.setResponse(response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationHttpResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */