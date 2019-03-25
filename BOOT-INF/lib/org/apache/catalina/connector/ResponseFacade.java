/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseFacade
/*     */   implements HttpServletResponse
/*     */ {
/*     */   private final class SetContentTypePrivilegedAction
/*     */     implements PrivilegedAction<Void>
/*     */   {
/*     */     private final String contentType;
/*     */     
/*     */     public SetContentTypePrivilegedAction(String contentType)
/*     */     {
/*  55 */       this.contentType = contentType;
/*     */     }
/*     */     
/*     */     public Void run()
/*     */     {
/*  60 */       ResponseFacade.this.response.setContentType(this.contentType);
/*  61 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class DateHeaderPrivilegedAction implements PrivilegedAction<Void>
/*     */   {
/*     */     private final String name;
/*     */     private final long value;
/*     */     private final boolean add;
/*     */     
/*     */     DateHeaderPrivilegedAction(String name, long value, boolean add)
/*     */     {
/*  73 */       this.name = name;
/*  74 */       this.value = value;
/*  75 */       this.add = add;
/*     */     }
/*     */     
/*     */     public Void run()
/*     */     {
/*  80 */       if (this.add) {
/*  81 */         ResponseFacade.this.response.addDateHeader(this.name, this.value);
/*     */       } else {
/*  83 */         ResponseFacade.this.response.setDateHeader(this.name, this.value);
/*     */       }
/*  85 */       return null;
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
/*     */   public ResponseFacade(Response response)
/*     */   {
/*  99 */     this.response = response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   protected static final StringManager sm = StringManager.getManager(ResponseFacade.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   protected Response response = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 125 */     this.response = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 135 */     throw new CloneNotSupportedException();
/*     */   }
/*     */   
/*     */ 
/*     */   public void finish()
/*     */   {
/* 141 */     if (this.response == null)
/*     */     {
/* 143 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 146 */     this.response.setSuspended(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/* 152 */     if (this.response == null)
/*     */     {
/* 154 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 157 */     return this.response.isSuspended();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getContentWritten()
/*     */   {
/* 163 */     if (this.response == null)
/*     */     {
/* 165 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 168 */     return this.response.getContentWritten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 177 */     if (this.response == null)
/*     */     {
/* 179 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 182 */     return this.response.getCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletOutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 194 */     ServletOutputStream sos = this.response.getOutputStream();
/* 195 */     if (isFinished()) {
/* 196 */       this.response.setSuspended(true);
/*     */     }
/* 198 */     return sos;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PrintWriter getWriter()
/*     */     throws IOException
/*     */   {
/* 211 */     PrintWriter writer = this.response.getWriter();
/* 212 */     if (isFinished()) {
/* 213 */       this.response.setSuspended(true);
/*     */     }
/* 215 */     return writer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentLength(int len)
/*     */   {
/* 223 */     if (isCommitted()) {
/* 224 */       return;
/*     */     }
/*     */     
/* 227 */     this.response.setContentLength(len);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setContentLengthLong(long length)
/*     */   {
/* 233 */     if (isCommitted()) {
/* 234 */       return;
/*     */     }
/* 236 */     this.response.setContentLengthLong(length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setContentType(String type)
/*     */   {
/* 243 */     if (isCommitted()) {
/* 244 */       return;
/*     */     }
/*     */     
/* 247 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 248 */       AccessController.doPrivileged(new SetContentTypePrivilegedAction(type));
/*     */     } else {
/* 250 */       this.response.setContentType(type);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBufferSize(int size)
/*     */   {
/* 258 */     if (isCommitted())
/*     */     {
/* 260 */       throw new IllegalStateException(sm.getString("coyoteResponse.setBufferSize.ise"));
/*     */     }
/*     */     
/* 263 */     this.response.setBufferSize(size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/* 271 */     if (this.response == null)
/*     */     {
/* 273 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 276 */     return this.response.getBufferSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 284 */     if (isFinished())
/*     */     {
/*     */ 
/* 287 */       return;
/*     */     }
/*     */     
/* 290 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 292 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Void run()
/*     */             throws IOException
/*     */           {
/* 297 */             ResponseFacade.this.response.setAppCommitted(true);
/*     */             
/* 299 */             ResponseFacade.this.response.flushBuffer();
/* 300 */             return null;
/*     */           }
/*     */         });
/*     */       } catch (PrivilegedActionException e) {
/* 304 */         Exception ex = e.getException();
/* 305 */         if ((ex instanceof IOException)) {
/* 306 */           throw ((IOException)ex);
/*     */         }
/*     */       }
/*     */     } else {
/* 310 */       this.response.setAppCommitted(true);
/*     */       
/* 312 */       this.response.flushBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetBuffer()
/*     */   {
/* 321 */     if (isCommitted())
/*     */     {
/* 323 */       throw new IllegalStateException(sm.getString("coyoteResponse.resetBuffer.ise"));
/*     */     }
/*     */     
/* 326 */     this.response.resetBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCommitted()
/*     */   {
/* 334 */     if (this.response == null)
/*     */     {
/* 336 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 339 */     return this.response.isAppCommitted();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 346 */     if (isCommitted())
/*     */     {
/* 348 */       throw new IllegalStateException(sm.getString("coyoteResponse.reset.ise"));
/*     */     }
/*     */     
/* 351 */     this.response.reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale loc)
/*     */   {
/* 359 */     if (isCommitted()) {
/* 360 */       return;
/*     */     }
/*     */     
/* 363 */     this.response.setLocale(loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 370 */     if (this.response == null)
/*     */     {
/* 372 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 375 */     return this.response.getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCookie(Cookie cookie)
/*     */   {
/* 382 */     if (isCommitted()) {
/* 383 */       return;
/*     */     }
/*     */     
/* 386 */     this.response.addCookie(cookie);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsHeader(String name)
/*     */   {
/* 394 */     if (this.response == null)
/*     */     {
/* 396 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 399 */     return this.response.containsHeader(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String encodeURL(String url)
/*     */   {
/* 406 */     if (this.response == null)
/*     */     {
/* 408 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 411 */     return this.response.encodeURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String encodeRedirectURL(String url)
/*     */   {
/* 418 */     if (this.response == null)
/*     */     {
/* 420 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 423 */     return this.response.encodeRedirectURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String encodeUrl(String url)
/*     */   {
/* 430 */     if (this.response == null)
/*     */     {
/* 432 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 435 */     return this.response.encodeURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String encodeRedirectUrl(String url)
/*     */   {
/* 442 */     if (this.response == null)
/*     */     {
/* 444 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 447 */     return this.response.encodeRedirectURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sendError(int sc, String msg)
/*     */     throws IOException
/*     */   {
/* 455 */     if (isCommitted())
/*     */     {
/* 457 */       throw new IllegalStateException(sm.getString("coyoteResponse.sendError.ise"));
/*     */     }
/*     */     
/* 460 */     this.response.setAppCommitted(true);
/*     */     
/* 462 */     this.response.sendError(sc, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendError(int sc)
/*     */     throws IOException
/*     */   {
/* 471 */     if (isCommitted())
/*     */     {
/* 473 */       throw new IllegalStateException(sm.getString("coyoteResponse.sendError.ise"));
/*     */     }
/*     */     
/* 476 */     this.response.setAppCommitted(true);
/*     */     
/* 478 */     this.response.sendError(sc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendRedirect(String location)
/*     */     throws IOException
/*     */   {
/* 487 */     if (isCommitted())
/*     */     {
/* 489 */       throw new IllegalStateException(sm.getString("coyoteResponse.sendRedirect.ise"));
/*     */     }
/*     */     
/* 492 */     this.response.setAppCommitted(true);
/*     */     
/* 494 */     this.response.sendRedirect(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateHeader(String name, long date)
/*     */   {
/* 502 */     if (isCommitted()) {
/* 503 */       return;
/*     */     }
/*     */     
/* 506 */     if (Globals.IS_SECURITY_ENABLED) {
/* 507 */       AccessController.doPrivileged(new DateHeaderPrivilegedAction(name, date, false));
/*     */     }
/*     */     else {
/* 510 */       this.response.setDateHeader(name, date);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDateHeader(String name, long date)
/*     */   {
/* 519 */     if (isCommitted()) {
/* 520 */       return;
/*     */     }
/*     */     
/* 523 */     if (Globals.IS_SECURITY_ENABLED) {
/* 524 */       AccessController.doPrivileged(new DateHeaderPrivilegedAction(name, date, true));
/*     */     }
/*     */     else {
/* 527 */       this.response.addDateHeader(name, date);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 536 */     if (isCommitted()) {
/* 537 */       return;
/*     */     }
/*     */     
/* 540 */     this.response.setHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 548 */     if (isCommitted()) {
/* 549 */       return;
/*     */     }
/*     */     
/* 552 */     this.response.addHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIntHeader(String name, int value)
/*     */   {
/* 560 */     if (isCommitted()) {
/* 561 */       return;
/*     */     }
/*     */     
/* 564 */     this.response.setIntHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addIntHeader(String name, int value)
/*     */   {
/* 572 */     if (isCommitted()) {
/* 573 */       return;
/*     */     }
/*     */     
/* 576 */     this.response.addIntHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(int sc)
/*     */   {
/* 584 */     if (isCommitted()) {
/* 585 */       return;
/*     */     }
/*     */     
/* 588 */     this.response.setStatus(sc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(int sc, String sm)
/*     */   {
/* 596 */     if (isCommitted()) {
/* 597 */       return;
/*     */     }
/*     */     
/* 600 */     this.response.setStatus(sc, sm);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 607 */     if (this.response == null)
/*     */     {
/* 609 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 612 */     return this.response.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCharacterEncoding(String arg0)
/*     */   {
/* 619 */     if (this.response == null)
/*     */     {
/* 621 */       throw new IllegalStateException(sm.getString("responseFacade.nullResponse"));
/*     */     }
/*     */     
/* 624 */     this.response.setCharacterEncoding(arg0);
/*     */   }
/*     */   
/*     */   public int getStatus()
/*     */   {
/* 629 */     return this.response.getStatus();
/*     */   }
/*     */   
/*     */   public String getHeader(String name)
/*     */   {
/* 634 */     return this.response.getHeader(name);
/*     */   }
/*     */   
/*     */   public Collection<String> getHeaderNames()
/*     */   {
/* 639 */     return this.response.getHeaderNames();
/*     */   }
/*     */   
/*     */   public Collection<String> getHeaders(String name)
/*     */   {
/* 644 */     return this.response.getHeaders(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\ResponseFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */