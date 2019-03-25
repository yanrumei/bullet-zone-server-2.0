/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ public class HttpServletResponseWrapper
/*     */   extends ServletResponseWrapper
/*     */   implements HttpServletResponse
/*     */ {
/*     */   public HttpServletResponseWrapper(HttpServletResponse response)
/*     */   {
/*  45 */     super(response);
/*     */   }
/*     */   
/*     */   private HttpServletResponse _getHttpServletResponse() {
/*  49 */     return (HttpServletResponse)super.getResponse();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCookie(Cookie cookie)
/*     */   {
/*  58 */     _getHttpServletResponse().addCookie(cookie);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsHeader(String name)
/*     */   {
/*  67 */     return _getHttpServletResponse().containsHeader(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encodeURL(String url)
/*     */   {
/*  76 */     return _getHttpServletResponse().encodeURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encodeRedirectURL(String url)
/*     */   {
/*  85 */     return _getHttpServletResponse().encodeRedirectURL(url);
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
/*     */   public String encodeUrl(String url)
/*     */   {
/*  98 */     return _getHttpServletResponse().encodeUrl(url);
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
/*     */   public String encodeRedirectUrl(String url)
/*     */   {
/* 111 */     return _getHttpServletResponse().encodeRedirectUrl(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendError(int sc, String msg)
/*     */     throws IOException
/*     */   {
/* 120 */     _getHttpServletResponse().sendError(sc, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendError(int sc)
/*     */     throws IOException
/*     */   {
/* 129 */     _getHttpServletResponse().sendError(sc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendRedirect(String location)
/*     */     throws IOException
/*     */   {
/* 138 */     _getHttpServletResponse().sendRedirect(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateHeader(String name, long date)
/*     */   {
/* 147 */     _getHttpServletResponse().setDateHeader(name, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDateHeader(String name, long date)
/*     */   {
/* 156 */     _getHttpServletResponse().addDateHeader(name, date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 165 */     _getHttpServletResponse().setHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/* 174 */     _getHttpServletResponse().addHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIntHeader(String name, int value)
/*     */   {
/* 183 */     _getHttpServletResponse().setIntHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addIntHeader(String name, int value)
/*     */   {
/* 192 */     _getHttpServletResponse().addIntHeader(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatus(int sc)
/*     */   {
/* 201 */     _getHttpServletResponse().setStatus(sc);
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
/*     */   public void setStatus(int sc, String sm)
/*     */   {
/* 214 */     _getHttpServletResponse().setStatus(sc, sm);
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
/*     */   public int getStatus()
/*     */   {
/* 228 */     return _getHttpServletResponse().getStatus();
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
/*     */   public String getHeader(String name)
/*     */   {
/* 242 */     return _getHttpServletResponse().getHeader(name);
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
/*     */   public Collection<String> getHeaders(String name)
/*     */   {
/* 256 */     return _getHttpServletResponse().getHeaders(name);
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
/*     */   public Collection<String> getHeaderNames()
/*     */   {
/* 270 */     return _getHttpServletResponse().getHeaderNames();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpServletResponseWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */