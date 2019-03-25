/*     */ package javax.servlet;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletRequestWrapper
/*     */   implements ServletRequest
/*     */ {
/*     */   private ServletRequest request;
/*     */   
/*     */   public ServletRequestWrapper(ServletRequest request)
/*     */   {
/*  45 */     if (request == null) {
/*  46 */       throw new IllegalArgumentException("Request cannot be null");
/*     */     }
/*  48 */     this.request = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRequest getRequest()
/*     */   {
/*  56 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequest(ServletRequest request)
/*     */   {
/*  66 */     if (request == null) {
/*  67 */       throw new IllegalArgumentException("Request cannot be null");
/*     */     }
/*  69 */     this.request = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */   {
/*  78 */     return this.request.getAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getAttributeNames()
/*     */   {
/*  87 */     return this.request.getAttributeNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  96 */     return this.request.getCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterEncoding(String enc)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 106 */     this.request.setCharacterEncoding(enc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getContentLength()
/*     */   {
/* 115 */     return this.request.getContentLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getContentLengthLong()
/*     */   {
/* 126 */     return this.request.getContentLengthLong();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 135 */     return this.request.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletInputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 144 */     return this.request.getInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 153 */     return this.request.getParameter(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String[]> getParameterMap()
/*     */   {
/* 162 */     return this.request.getParameterMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getParameterNames()
/*     */   {
/* 171 */     return this.request.getParameterNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getParameterValues(String name)
/*     */   {
/* 180 */     return this.request.getParameterValues(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 189 */     return this.request.getProtocol();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getScheme()
/*     */   {
/* 198 */     return this.request.getScheme();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerName()
/*     */   {
/* 207 */     return this.request.getServerName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getServerPort()
/*     */   {
/* 216 */     return this.request.getServerPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedReader getReader()
/*     */     throws IOException
/*     */   {
/* 225 */     return this.request.getReader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemoteAddr()
/*     */   {
/* 234 */     return this.request.getRemoteAddr();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemoteHost()
/*     */   {
/* 243 */     return this.request.getRemoteHost();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttribute(String name, Object o)
/*     */   {
/* 252 */     this.request.setAttribute(name, o);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeAttribute(String name)
/*     */   {
/* 261 */     this.request.removeAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 270 */     return this.request.getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<Locale> getLocales()
/*     */   {
/* 279 */     return this.request.getLocales();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 288 */     return this.request.isSecure();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestDispatcher getRequestDispatcher(String path)
/*     */   {
/* 297 */     return this.request.getRequestDispatcher(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public String getRealPath(String path)
/*     */   {
/* 310 */     return this.request.getRealPath(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRemotePort()
/*     */   {
/* 321 */     return this.request.getRemotePort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalName()
/*     */   {
/* 332 */     return this.request.getLocalName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalAddr()
/*     */   {
/* 343 */     return this.request.getLocalAddr();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLocalPort()
/*     */   {
/* 354 */     return this.request.getLocalPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletContext getServletContext()
/*     */   {
/* 365 */     return this.request.getServletContext();
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
/*     */   public AsyncContext startAsync()
/*     */     throws IllegalStateException
/*     */   {
/* 379 */     return this.request.startAsync();
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
/*     */ 
/*     */   public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
/*     */     throws IllegalStateException
/*     */   {
/* 398 */     return this.request.startAsync(servletRequest, servletResponse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAsyncStarted()
/*     */   {
/* 409 */     return this.request.isAsyncStarted();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAsyncSupported()
/*     */   {
/* 420 */     return this.request.isAsyncSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AsyncContext getAsyncContext()
/*     */   {
/* 431 */     return this.request.getAsyncContext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWrapperFor(ServletRequest wrapped)
/*     */   {
/* 443 */     if (this.request == wrapped) {
/* 444 */       return true;
/*     */     }
/* 446 */     if ((this.request instanceof ServletRequestWrapper)) {
/* 447 */       return ((ServletRequestWrapper)this.request).isWrapperFor(wrapped);
/*     */     }
/* 449 */     return false;
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
/* 462 */     if (wrappedType.isAssignableFrom(this.request.getClass())) {
/* 463 */       return true;
/*     */     }
/* 465 */     if ((this.request instanceof ServletRequestWrapper)) {
/* 466 */       return ((ServletRequestWrapper)this.request).isWrapperFor(wrappedType);
/*     */     }
/* 468 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DispatcherType getDispatcherType()
/*     */   {
/* 479 */     return this.request.getDispatcherType();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletRequestWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */