/*      */ package org.apache.catalina.connector;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.AccessController;
/*      */ import java.security.Principal;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import javax.servlet.AsyncContext;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletInputStream;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import javax.servlet.http.HttpUpgradeHandler;
/*      */ import javax.servlet.http.Part;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.security.SecurityUtil;
/*      */ import org.apache.catalina.servlet4preview.http.PushBuilder;
/*      */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RequestFacade
/*      */   implements org.apache.catalina.servlet4preview.http.HttpServletRequest
/*      */ {
/*      */   private final class GetAttributePrivilegedAction
/*      */     implements PrivilegedAction<Enumeration<String>>
/*      */   {
/*      */     private GetAttributePrivilegedAction() {}
/*      */     
/*      */     public Enumeration<String> run()
/*      */     {
/*   67 */       return RequestFacade.this.request.getAttributeNames();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetParameterMapPrivilegedAction implements PrivilegedAction<Map<String, String[]>>
/*      */   {
/*      */     private GetParameterMapPrivilegedAction() {}
/*      */     
/*      */     public Map<String, String[]> run()
/*      */     {
/*   77 */       return RequestFacade.this.request.getParameterMap();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetRequestDispatcherPrivilegedAction
/*      */     implements PrivilegedAction<RequestDispatcher>
/*      */   {
/*      */     private final String path;
/*      */     
/*      */     public GetRequestDispatcherPrivilegedAction(String path)
/*      */     {
/*   88 */       this.path = path;
/*      */     }
/*      */     
/*      */     public RequestDispatcher run()
/*      */     {
/*   93 */       return RequestFacade.this.request.getRequestDispatcher(this.path);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetParameterPrivilegedAction
/*      */     implements PrivilegedAction<String>
/*      */   {
/*      */     public String name;
/*      */     
/*      */     public GetParameterPrivilegedAction(String name)
/*      */     {
/*  104 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String run()
/*      */     {
/*  109 */       return RequestFacade.this.request.getParameter(this.name);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetParameterNamesPrivilegedAction implements PrivilegedAction<Enumeration<String>>
/*      */   {
/*      */     private GetParameterNamesPrivilegedAction() {}
/*      */     
/*      */     public Enumeration<String> run()
/*      */     {
/*  119 */       return RequestFacade.this.request.getParameterNames();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetParameterValuePrivilegedAction
/*      */     implements PrivilegedAction<String[]>
/*      */   {
/*      */     public String name;
/*      */     
/*      */     public GetParameterValuePrivilegedAction(String name)
/*      */     {
/*  130 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String[] run()
/*      */     {
/*  135 */       return RequestFacade.this.request.getParameterValues(this.name);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetCookiesPrivilegedAction implements PrivilegedAction<Cookie[]>
/*      */   {
/*      */     private GetCookiesPrivilegedAction() {}
/*      */     
/*      */     public Cookie[] run()
/*      */     {
/*  145 */       return RequestFacade.this.request.getCookies();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetCharacterEncodingPrivilegedAction implements PrivilegedAction<String>
/*      */   {
/*      */     private GetCharacterEncodingPrivilegedAction() {}
/*      */     
/*      */     public String run()
/*      */     {
/*  155 */       return RequestFacade.this.request.getCharacterEncoding();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetHeadersPrivilegedAction
/*      */     implements PrivilegedAction<Enumeration<String>>
/*      */   {
/*      */     private final String name;
/*      */     
/*      */     public GetHeadersPrivilegedAction(String name)
/*      */     {
/*  166 */       this.name = name;
/*      */     }
/*      */     
/*      */     public Enumeration<String> run()
/*      */     {
/*  171 */       return RequestFacade.this.request.getHeaders(this.name);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetHeaderNamesPrivilegedAction implements PrivilegedAction<Enumeration<String>>
/*      */   {
/*      */     private GetHeaderNamesPrivilegedAction() {}
/*      */     
/*      */     public Enumeration<String> run()
/*      */     {
/*  181 */       return RequestFacade.this.request.getHeaderNames();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetLocalePrivilegedAction implements PrivilegedAction<Locale>
/*      */   {
/*      */     private GetLocalePrivilegedAction() {}
/*      */     
/*      */     public Locale run()
/*      */     {
/*  191 */       return RequestFacade.this.request.getLocale();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetLocalesPrivilegedAction implements PrivilegedAction<Enumeration<Locale>>
/*      */   {
/*      */     private GetLocalesPrivilegedAction() {}
/*      */     
/*      */     public Enumeration<Locale> run()
/*      */     {
/*  201 */       return RequestFacade.this.request.getLocales();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class GetSessionPrivilegedAction implements PrivilegedAction<HttpSession>
/*      */   {
/*      */     private final boolean create;
/*      */     
/*      */     public GetSessionPrivilegedAction(boolean create)
/*      */     {
/*  211 */       this.create = create;
/*      */     }
/*      */     
/*      */     public HttpSession run()
/*      */     {
/*  216 */       return RequestFacade.this.request.getSession(this.create);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RequestFacade(Request request)
/*      */   {
/*  230 */     this.request = request;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  241 */   protected Request request = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  247 */   protected static final StringManager sm = StringManager.getManager(RequestFacade.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */   {
/*  257 */     this.request = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/*  267 */     throw new CloneNotSupportedException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */   {
/*  277 */     if (this.request == null)
/*      */     {
/*  279 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  282 */     return this.request.getAttribute(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getAttributeNames()
/*      */   {
/*  289 */     if (this.request == null)
/*      */     {
/*  291 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  294 */     if (Globals.IS_SECURITY_ENABLED) {
/*  295 */       return (Enumeration)AccessController.doPrivileged(new GetAttributePrivilegedAction(null));
/*      */     }
/*      */     
/*  298 */     return this.request.getAttributeNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCharacterEncoding()
/*      */   {
/*  306 */     if (this.request == null)
/*      */     {
/*  308 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  311 */     if (Globals.IS_SECURITY_ENABLED) {
/*  312 */       return (String)AccessController.doPrivileged(new GetCharacterEncodingPrivilegedAction(null));
/*      */     }
/*      */     
/*  315 */     return this.request.getCharacterEncoding();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterEncoding(String env)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  324 */     if (this.request == null)
/*      */     {
/*  326 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  329 */     this.request.setCharacterEncoding(env);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getContentLength()
/*      */   {
/*  336 */     if (this.request == null)
/*      */     {
/*  338 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  341 */     return this.request.getContentLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getContentType()
/*      */   {
/*  348 */     if (this.request == null)
/*      */     {
/*  350 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  353 */     return this.request.getContentType();
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletInputStream getInputStream()
/*      */     throws IOException
/*      */   {
/*  360 */     if (this.request == null)
/*      */     {
/*  362 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  365 */     return this.request.getInputStream();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getParameter(String name)
/*      */   {
/*  372 */     if (this.request == null)
/*      */     {
/*  374 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  377 */     if (Globals.IS_SECURITY_ENABLED) {
/*  378 */       return (String)AccessController.doPrivileged(new GetParameterPrivilegedAction(name));
/*      */     }
/*      */     
/*  381 */     return this.request.getParameter(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getParameterNames()
/*      */   {
/*  389 */     if (this.request == null)
/*      */     {
/*  391 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  394 */     if (Globals.IS_SECURITY_ENABLED) {
/*  395 */       return (Enumeration)AccessController.doPrivileged(new GetParameterNamesPrivilegedAction(null));
/*      */     }
/*      */     
/*  398 */     return this.request.getParameterNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getParameterValues(String name)
/*      */   {
/*  406 */     if (this.request == null)
/*      */     {
/*  408 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  411 */     String[] ret = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  417 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*  418 */       ret = (String[])AccessController.doPrivileged(new GetParameterValuePrivilegedAction(name));
/*      */       
/*  420 */       if (ret != null) {
/*  421 */         ret = (String[])ret.clone();
/*      */       }
/*      */     } else {
/*  424 */       ret = this.request.getParameterValues(name);
/*      */     }
/*      */     
/*  427 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Map<String, String[]> getParameterMap()
/*      */   {
/*  434 */     if (this.request == null)
/*      */     {
/*  436 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  439 */     if (Globals.IS_SECURITY_ENABLED) {
/*  440 */       return (Map)AccessController.doPrivileged(new GetParameterMapPrivilegedAction(null));
/*      */     }
/*      */     
/*  443 */     return this.request.getParameterMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/*  451 */     if (this.request == null)
/*      */     {
/*  453 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  456 */     return this.request.getProtocol();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getScheme()
/*      */   {
/*  463 */     if (this.request == null)
/*      */     {
/*  465 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  468 */     return this.request.getScheme();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getServerName()
/*      */   {
/*  475 */     if (this.request == null)
/*      */     {
/*  477 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  480 */     return this.request.getServerName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getServerPort()
/*      */   {
/*  487 */     if (this.request == null)
/*      */     {
/*  489 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  492 */     return this.request.getServerPort();
/*      */   }
/*      */   
/*      */ 
/*      */   public BufferedReader getReader()
/*      */     throws IOException
/*      */   {
/*  499 */     if (this.request == null)
/*      */     {
/*  501 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  504 */     return this.request.getReader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRemoteAddr()
/*      */   {
/*  511 */     if (this.request == null)
/*      */     {
/*  513 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  516 */     return this.request.getRemoteAddr();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRemoteHost()
/*      */   {
/*  523 */     if (this.request == null)
/*      */     {
/*  525 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  528 */     return this.request.getRemoteHost();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAttribute(String name, Object o)
/*      */   {
/*  535 */     if (this.request == null)
/*      */     {
/*  537 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  540 */     this.request.setAttribute(name, o);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void removeAttribute(String name)
/*      */   {
/*  547 */     if (this.request == null)
/*      */     {
/*  549 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  552 */     this.request.removeAttribute(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  559 */     if (this.request == null)
/*      */     {
/*  561 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  564 */     if (Globals.IS_SECURITY_ENABLED) {
/*  565 */       return (Locale)AccessController.doPrivileged(new GetLocalePrivilegedAction(null));
/*      */     }
/*      */     
/*  568 */     return this.request.getLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<Locale> getLocales()
/*      */   {
/*  576 */     if (this.request == null)
/*      */     {
/*  578 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  581 */     if (Globals.IS_SECURITY_ENABLED) {
/*  582 */       return (Enumeration)AccessController.doPrivileged(new GetLocalesPrivilegedAction(null));
/*      */     }
/*      */     
/*  585 */     return this.request.getLocales();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSecure()
/*      */   {
/*  593 */     if (this.request == null)
/*      */     {
/*  595 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  598 */     return this.request.isSecure();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public RequestDispatcher getRequestDispatcher(String path)
/*      */   {
/*  605 */     if (this.request == null)
/*      */     {
/*  607 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  610 */     if (Globals.IS_SECURITY_ENABLED) {
/*  611 */       return (RequestDispatcher)AccessController.doPrivileged(new GetRequestDispatcherPrivilegedAction(path));
/*      */     }
/*      */     
/*  614 */     return this.request.getRequestDispatcher(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRealPath(String path)
/*      */   {
/*  621 */     if (this.request == null)
/*      */     {
/*  623 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  626 */     return this.request.getRealPath(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getAuthType()
/*      */   {
/*  633 */     if (this.request == null)
/*      */     {
/*  635 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  638 */     return this.request.getAuthType();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Cookie[] getCookies()
/*      */   {
/*  645 */     if (this.request == null)
/*      */     {
/*  647 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  650 */     Cookie[] ret = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  656 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*  657 */       ret = (Cookie[])AccessController.doPrivileged(new GetCookiesPrivilegedAction(null));
/*      */       
/*  659 */       if (ret != null) {
/*  660 */         ret = (Cookie[])ret.clone();
/*      */       }
/*      */     } else {
/*  663 */       ret = this.request.getCookies();
/*      */     }
/*      */     
/*  666 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getDateHeader(String name)
/*      */   {
/*  673 */     if (this.request == null)
/*      */     {
/*  675 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  678 */     return this.request.getDateHeader(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getHeader(String name)
/*      */   {
/*  685 */     if (this.request == null)
/*      */     {
/*  687 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  690 */     return this.request.getHeader(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getHeaders(String name)
/*      */   {
/*  697 */     if (this.request == null)
/*      */     {
/*  699 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  702 */     if (Globals.IS_SECURITY_ENABLED) {
/*  703 */       return (Enumeration)AccessController.doPrivileged(new GetHeadersPrivilegedAction(name));
/*      */     }
/*      */     
/*  706 */     return this.request.getHeaders(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getHeaderNames()
/*      */   {
/*  714 */     if (this.request == null)
/*      */     {
/*  716 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  719 */     if (Globals.IS_SECURITY_ENABLED) {
/*  720 */       return (Enumeration)AccessController.doPrivileged(new GetHeaderNamesPrivilegedAction(null));
/*      */     }
/*      */     
/*  723 */     return this.request.getHeaderNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getIntHeader(String name)
/*      */   {
/*  731 */     if (this.request == null)
/*      */     {
/*  733 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  736 */     return this.request.getIntHeader(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getMethod()
/*      */   {
/*  743 */     if (this.request == null)
/*      */     {
/*  745 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  748 */     return this.request.getMethod();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getPathInfo()
/*      */   {
/*  755 */     if (this.request == null)
/*      */     {
/*  757 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  760 */     return this.request.getPathInfo();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getPathTranslated()
/*      */   {
/*  767 */     if (this.request == null)
/*      */     {
/*  769 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  772 */     return this.request.getPathTranslated();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getContextPath()
/*      */   {
/*  779 */     if (this.request == null)
/*      */     {
/*  781 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  784 */     return this.request.getContextPath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/*  791 */     if (this.request == null)
/*      */     {
/*  793 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  796 */     return this.request.getQueryString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRemoteUser()
/*      */   {
/*  803 */     if (this.request == null)
/*      */     {
/*  805 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  808 */     return this.request.getRemoteUser();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isUserInRole(String role)
/*      */   {
/*  815 */     if (this.request == null)
/*      */     {
/*  817 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  820 */     return this.request.isUserInRole(role);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Principal getUserPrincipal()
/*      */   {
/*  827 */     if (this.request == null)
/*      */     {
/*  829 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  832 */     return this.request.getUserPrincipal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRequestedSessionId()
/*      */   {
/*  839 */     if (this.request == null)
/*      */     {
/*  841 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  844 */     return this.request.getRequestedSessionId();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRequestURI()
/*      */   {
/*  851 */     if (this.request == null)
/*      */     {
/*  853 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  856 */     return this.request.getRequestURI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public StringBuffer getRequestURL()
/*      */   {
/*  863 */     if (this.request == null)
/*      */     {
/*  865 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  868 */     return this.request.getRequestURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getServletPath()
/*      */   {
/*  875 */     if (this.request == null)
/*      */     {
/*  877 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  880 */     return this.request.getServletPath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HttpSession getSession(boolean create)
/*      */   {
/*  887 */     if (this.request == null)
/*      */     {
/*  889 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  892 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*  893 */       return 
/*  894 */         (HttpSession)AccessController.doPrivileged(new GetSessionPrivilegedAction(create));
/*      */     }
/*  896 */     return this.request.getSession(create);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HttpSession getSession()
/*      */   {
/*  903 */     if (this.request == null)
/*      */     {
/*  905 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  908 */     return getSession(true);
/*      */   }
/*      */   
/*      */ 
/*      */   public String changeSessionId()
/*      */   {
/*  914 */     if (this.request == null)
/*      */     {
/*  916 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  919 */     return this.request.changeSessionId();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isRequestedSessionIdValid()
/*      */   {
/*  925 */     if (this.request == null)
/*      */     {
/*  927 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  930 */     return this.request.isRequestedSessionIdValid();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdFromCookie()
/*      */   {
/*  937 */     if (this.request == null)
/*      */     {
/*  939 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  942 */     return this.request.isRequestedSessionIdFromCookie();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdFromURL()
/*      */   {
/*  949 */     if (this.request == null)
/*      */     {
/*  951 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  954 */     return this.request.isRequestedSessionIdFromURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isRequestedSessionIdFromUrl()
/*      */   {
/*  961 */     if (this.request == null)
/*      */     {
/*  963 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  966 */     return this.request.isRequestedSessionIdFromURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getLocalAddr()
/*      */   {
/*  973 */     if (this.request == null)
/*      */     {
/*  975 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  978 */     return this.request.getLocalAddr();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getLocalName()
/*      */   {
/*  985 */     if (this.request == null)
/*      */     {
/*  987 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/*  990 */     return this.request.getLocalName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getLocalPort()
/*      */   {
/*  997 */     if (this.request == null)
/*      */     {
/*  999 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/* 1002 */     return this.request.getLocalPort();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getRemotePort()
/*      */   {
/* 1009 */     if (this.request == null)
/*      */     {
/* 1011 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/* 1014 */     return this.request.getRemotePort();
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletContext getServletContext()
/*      */   {
/* 1020 */     if (this.request == null)
/*      */     {
/* 1022 */       throw new IllegalStateException(sm.getString("requestFacade.nullRequest"));
/*      */     }
/*      */     
/* 1025 */     return this.request.getServletContext();
/*      */   }
/*      */   
/*      */   public AsyncContext startAsync()
/*      */     throws IllegalStateException
/*      */   {
/* 1031 */     return this.request.startAsync();
/*      */   }
/*      */   
/*      */ 
/*      */   public AsyncContext startAsync(ServletRequest request, ServletResponse response)
/*      */     throws IllegalStateException
/*      */   {
/* 1038 */     return this.request.startAsync(request, response);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isAsyncStarted()
/*      */   {
/* 1044 */     return this.request.isAsyncStarted();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isAsyncSupported()
/*      */   {
/* 1050 */     return this.request.isAsyncSupported();
/*      */   }
/*      */   
/*      */ 
/*      */   public AsyncContext getAsyncContext()
/*      */   {
/* 1056 */     return this.request.getAsyncContext();
/*      */   }
/*      */   
/*      */   public DispatcherType getDispatcherType()
/*      */   {
/* 1061 */     return this.request.getDispatcherType();
/*      */   }
/*      */   
/*      */   public boolean authenticate(HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/* 1067 */     return this.request.authenticate(response);
/*      */   }
/*      */   
/*      */   public void login(String username, String password)
/*      */     throws ServletException
/*      */   {
/* 1073 */     this.request.login(username, password);
/*      */   }
/*      */   
/*      */   public void logout() throws ServletException
/*      */   {
/* 1078 */     this.request.logout();
/*      */   }
/*      */   
/*      */   public Collection<Part> getParts()
/*      */     throws IllegalStateException, IOException, ServletException
/*      */   {
/* 1084 */     return this.request.getParts();
/*      */   }
/*      */   
/*      */   public Part getPart(String name)
/*      */     throws IllegalStateException, IOException, ServletException
/*      */   {
/* 1090 */     return this.request.getPart(name);
/*      */   }
/*      */   
/*      */   public boolean getAllowTrace() {
/* 1094 */     return this.request.getConnector().getAllowTrace();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getContentLengthLong()
/*      */   {
/* 1104 */     return this.request.getContentLengthLong();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass)
/*      */     throws IOException, ServletException
/*      */   {
/* 1115 */     return this.request.upgrade(httpUpgradeHandlerClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletMapping getServletMapping()
/*      */   {
/* 1127 */     return this.request.getServletMapping();
/*      */   }
/*      */   
/*      */   public PushBuilder newPushBuilder(javax.servlet.http.HttpServletRequest request)
/*      */   {
/* 1132 */     return this.request.newPushBuilder(request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PushBuilder newPushBuilder()
/*      */   {
/* 1144 */     return this.request.newPushBuilder();
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\RequestFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */