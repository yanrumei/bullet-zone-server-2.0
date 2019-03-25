/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Principal;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequestWrapper;
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
/*     */ public class HttpServletRequestWrapper
/*     */   extends ServletRequestWrapper
/*     */   implements HttpServletRequest
/*     */ {
/*     */   public HttpServletRequestWrapper(HttpServletRequest request)
/*     */   {
/*  47 */     super(request);
/*     */   }
/*     */   
/*     */   private HttpServletRequest _getHttpServletRequest() {
/*  51 */     return (HttpServletRequest)super.getRequest();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAuthType()
/*     */   {
/*  60 */     return _getHttpServletRequest().getAuthType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Cookie[] getCookies()
/*     */   {
/*  69 */     return _getHttpServletRequest().getCookies();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getDateHeader(String name)
/*     */   {
/*  78 */     return _getHttpServletRequest().getDateHeader(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHeader(String name)
/*     */   {
/*  87 */     return _getHttpServletRequest().getHeader(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getHeaders(String name)
/*     */   {
/*  96 */     return _getHttpServletRequest().getHeaders(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getHeaderNames()
/*     */   {
/* 105 */     return _getHttpServletRequest().getHeaderNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntHeader(String name)
/*     */   {
/* 114 */     return _getHttpServletRequest().getIntHeader(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 123 */     return _getHttpServletRequest().getMethod();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathInfo()
/*     */   {
/* 132 */     return _getHttpServletRequest().getPathInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathTranslated()
/*     */   {
/* 141 */     return _getHttpServletRequest().getPathTranslated();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextPath()
/*     */   {
/* 150 */     return _getHttpServletRequest().getContextPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getQueryString()
/*     */   {
/* 159 */     return _getHttpServletRequest().getQueryString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemoteUser()
/*     */   {
/* 168 */     return _getHttpServletRequest().getRemoteUser();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUserInRole(String role)
/*     */   {
/* 177 */     return _getHttpServletRequest().isUserInRole(role);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 186 */     return _getHttpServletRequest().getUserPrincipal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRequestedSessionId()
/*     */   {
/* 195 */     return _getHttpServletRequest().getRequestedSessionId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRequestURI()
/*     */   {
/* 204 */     return _getHttpServletRequest().getRequestURI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuffer getRequestURL()
/*     */   {
/* 213 */     return _getHttpServletRequest().getRequestURL();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServletPath()
/*     */   {
/* 222 */     return _getHttpServletRequest().getServletPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpSession getSession(boolean create)
/*     */   {
/* 231 */     return _getHttpServletRequest().getSession(create);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpSession getSession()
/*     */   {
/* 240 */     return _getHttpServletRequest().getSession();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String changeSessionId()
/*     */   {
/* 249 */     return _getHttpServletRequest().changeSessionId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequestedSessionIdValid()
/*     */   {
/* 258 */     return _getHttpServletRequest().isRequestedSessionIdValid();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequestedSessionIdFromCookie()
/*     */   {
/* 267 */     return _getHttpServletRequest().isRequestedSessionIdFromCookie();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequestedSessionIdFromURL()
/*     */   {
/* 276 */     return _getHttpServletRequest().isRequestedSessionIdFromURL();
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
/*     */   public boolean isRequestedSessionIdFromUrl()
/*     */   {
/* 289 */     return _getHttpServletRequest().isRequestedSessionIdFromUrl();
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
/*     */   public boolean authenticate(HttpServletResponse response)
/*     */     throws IOException, ServletException
/*     */   {
/* 304 */     return _getHttpServletRequest().authenticate(response);
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
/*     */   public void login(String username, String password)
/*     */     throws ServletException
/*     */   {
/* 318 */     _getHttpServletRequest().login(username, password);
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
/*     */   public void logout()
/*     */     throws ServletException
/*     */   {
/* 332 */     _getHttpServletRequest().logout();
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
/*     */   public Collection<Part> getParts()
/*     */     throws IOException, ServletException
/*     */   {
/* 347 */     return _getHttpServletRequest().getParts();
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
/*     */   public Part getPart(String name)
/*     */     throws IOException, ServletException
/*     */   {
/* 362 */     return _getHttpServletRequest().getPart(name);
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
/*     */   public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
/*     */     throws IOException, ServletException
/*     */   {
/* 376 */     return _getHttpServletRequest().upgrade(httpUpgradeHandlerClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\http\HttpServletRequestWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */