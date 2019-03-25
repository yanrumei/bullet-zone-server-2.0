/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterRegistration;
/*     */ import javax.servlet.FilterRegistration.Dynamic;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import javax.servlet.SessionCookieConfig;
/*     */ import javax.servlet.SessionTrackingMode;
/*     */ import javax.servlet.descriptor.JspConfigDescriptor;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApplicationContextFacade
/*     */   implements org.apache.catalina.servlet4preview.ServletContext
/*     */ {
/*     */   private final Map<String, Class<?>[]> classCache;
/*     */   private final Map<String, Method> objectCache;
/*     */   private final ApplicationContext context;
/*     */   
/*     */   public ApplicationContextFacade(ApplicationContext context)
/*     */   {
/*  87 */     this.context = context;
/*     */     
/*  89 */     this.classCache = new HashMap();
/*  90 */     this.objectCache = new ConcurrentHashMap();
/*  91 */     initClassCache();
/*     */   }
/*     */   
/*     */   private void initClassCache()
/*     */   {
/*  96 */     Class<?>[] clazz = { String.class };
/*  97 */     this.classCache.put("getContext", clazz);
/*  98 */     this.classCache.put("getMimeType", clazz);
/*  99 */     this.classCache.put("getResourcePaths", clazz);
/* 100 */     this.classCache.put("getResource", clazz);
/* 101 */     this.classCache.put("getResourceAsStream", clazz);
/* 102 */     this.classCache.put("getRequestDispatcher", clazz);
/* 103 */     this.classCache.put("getNamedDispatcher", clazz);
/* 104 */     this.classCache.put("getServlet", clazz);
/* 105 */     this.classCache.put("setInitParameter", new Class[] { String.class, String.class });
/* 106 */     this.classCache.put("createServlet", new Class[] { Class.class });
/* 107 */     this.classCache.put("addServlet", new Class[] { String.class, String.class });
/* 108 */     this.classCache.put("createFilter", new Class[] { Class.class });
/* 109 */     this.classCache.put("addFilter", new Class[] { String.class, String.class });
/* 110 */     this.classCache.put("createListener", new Class[] { Class.class });
/* 111 */     this.classCache.put("addListener", clazz);
/* 112 */     this.classCache.put("getFilterRegistration", clazz);
/* 113 */     this.classCache.put("getServletRegistration", clazz);
/* 114 */     this.classCache.put("getInitParameter", clazz);
/* 115 */     this.classCache.put("setAttribute", new Class[] { String.class, Object.class });
/* 116 */     this.classCache.put("removeAttribute", clazz);
/* 117 */     this.classCache.put("getRealPath", clazz);
/* 118 */     this.classCache.put("getAttribute", clazz);
/* 119 */     this.classCache.put("log", clazz);
/* 120 */     this.classCache.put("setSessionTrackingModes", new Class[] { Set.class });
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
/*     */   public javax.servlet.ServletContext getContext(String uripath)
/*     */   {
/* 138 */     javax.servlet.ServletContext theContext = null;
/* 139 */     if (SecurityUtil.isPackageProtectionEnabled())
/*     */     {
/* 141 */       theContext = (javax.servlet.ServletContext)doPrivileged("getContext", new Object[] { uripath });
/*     */     } else {
/* 143 */       theContext = this.context.getContext(uripath);
/*     */     }
/* 145 */     if ((theContext != null) && ((theContext instanceof ApplicationContext)))
/*     */     {
/* 147 */       theContext = ((ApplicationContext)theContext).getFacade();
/*     */     }
/* 149 */     return theContext;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMajorVersion()
/*     */   {
/* 155 */     return this.context.getMajorVersion();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMinorVersion()
/*     */   {
/* 161 */     return this.context.getMinorVersion();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMimeType(String file)
/*     */   {
/* 167 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 168 */       return (String)doPrivileged("getMimeType", new Object[] { file });
/*     */     }
/* 170 */     return this.context.getMimeType(file);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<String> getResourcePaths(String path)
/*     */   {
/* 177 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 178 */       return (Set)doPrivileged("getResourcePaths", new Object[] { path });
/*     */     }
/*     */     
/* 181 */     return this.context.getResourcePaths(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public URL getResource(String path)
/*     */     throws MalformedURLException
/*     */   {
/* 189 */     if (Globals.IS_SECURITY_ENABLED) {
/*     */       try {
/* 191 */         return (URL)invokeMethod(this.context, "getResource", new Object[] { path });
/*     */       }
/*     */       catch (Throwable t) {
/* 194 */         ExceptionUtils.handleThrowable(t);
/* 195 */         if ((t instanceof MalformedURLException)) {
/* 196 */           throw ((MalformedURLException)t);
/*     */         }
/* 198 */         return null;
/*     */       }
/*     */     }
/* 201 */     return this.context.getResource(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InputStream getResourceAsStream(String path)
/*     */   {
/* 208 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 209 */       return (InputStream)doPrivileged("getResourceAsStream", new Object[] { path });
/*     */     }
/*     */     
/* 212 */     return this.context.getResourceAsStream(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RequestDispatcher getRequestDispatcher(String path)
/*     */   {
/* 219 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 220 */       return (RequestDispatcher)doPrivileged("getRequestDispatcher", new Object[] { path });
/*     */     }
/*     */     
/* 223 */     return this.context.getRequestDispatcher(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RequestDispatcher getNamedDispatcher(String name)
/*     */   {
/* 230 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 231 */       return (RequestDispatcher)doPrivileged("getNamedDispatcher", new Object[] { name });
/*     */     }
/*     */     
/* 234 */     return this.context.getNamedDispatcher(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Servlet getServlet(String name)
/*     */     throws ServletException
/*     */   {
/* 246 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 248 */         return (Servlet)invokeMethod(this.context, "getServlet", new Object[] { name });
/*     */       }
/*     */       catch (Throwable t) {
/* 251 */         ExceptionUtils.handleThrowable(t);
/* 252 */         if ((t instanceof ServletException)) {
/* 253 */           throw ((ServletException)t);
/*     */         }
/* 255 */         return null;
/*     */       }
/*     */     }
/* 258 */     return this.context.getServlet(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Enumeration<Servlet> getServlets()
/*     */   {
/* 270 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 271 */       return (Enumeration)doPrivileged("getServlets", null);
/*     */     }
/* 273 */     return this.context.getServlets();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Enumeration<String> getServletNames()
/*     */   {
/* 285 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 286 */       return (Enumeration)doPrivileged("getServletNames", null);
/*     */     }
/* 288 */     return this.context.getServletNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void log(String msg)
/*     */   {
/* 295 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 296 */       doPrivileged("log", new Object[] { msg });
/*     */     } else {
/* 298 */       this.context.log(msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void log(Exception exception, String msg)
/*     */   {
/* 310 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 311 */       doPrivileged("log", new Class[] { Exception.class, String.class }, new Object[] { exception, msg });
/*     */     }
/*     */     else {
/* 314 */       this.context.log(exception, msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void log(String message, Throwable throwable)
/*     */   {
/* 321 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 322 */       doPrivileged("log", new Class[] { String.class, Throwable.class }, new Object[] { message, throwable });
/*     */     }
/*     */     else {
/* 325 */       this.context.log(message, throwable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getRealPath(String path)
/*     */   {
/* 332 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 333 */       return (String)doPrivileged("getRealPath", new Object[] { path });
/*     */     }
/* 335 */     return this.context.getRealPath(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getServerInfo()
/*     */   {
/* 342 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 343 */       return (String)doPrivileged("getServerInfo", null);
/*     */     }
/* 345 */     return this.context.getServerInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getInitParameter(String name)
/*     */   {
/* 352 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 353 */       return (String)doPrivileged("getInitParameter", new Object[] { name });
/*     */     }
/*     */     
/* 356 */     return this.context.getInitParameter(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getInitParameterNames()
/*     */   {
/* 364 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 365 */       return (Enumeration)doPrivileged("getInitParameterNames", null);
/*     */     }
/*     */     
/* 368 */     return this.context.getInitParameterNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */   {
/* 375 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 376 */       return doPrivileged("getAttribute", new Object[] { name });
/*     */     }
/* 378 */     return this.context.getAttribute(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<String> getAttributeNames()
/*     */   {
/* 386 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 387 */       return (Enumeration)doPrivileged("getAttributeNames", null);
/*     */     }
/*     */     
/* 390 */     return this.context.getAttributeNames();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAttribute(String name, Object object)
/*     */   {
/* 397 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 398 */       doPrivileged("setAttribute", new Object[] { name, object });
/*     */     } else {
/* 400 */       this.context.setAttribute(name, object);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeAttribute(String name)
/*     */   {
/* 407 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 408 */       doPrivileged("removeAttribute", new Object[] { name });
/*     */     } else {
/* 410 */       this.context.removeAttribute(name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getServletContextName()
/*     */   {
/* 417 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 418 */       return (String)doPrivileged("getServletContextName", null);
/*     */     }
/* 420 */     return this.context.getServletContextName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getContextPath()
/*     */   {
/* 427 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 428 */       return (String)doPrivileged("getContextPath", null);
/*     */     }
/* 430 */     return this.context.getContextPath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterRegistration.Dynamic addFilter(String filterName, String className)
/*     */   {
/* 438 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 439 */       return (FilterRegistration.Dynamic)doPrivileged("addFilter", new Object[] { filterName, className });
/*     */     }
/*     */     
/* 442 */     return this.context.addFilter(filterName, className);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterRegistration.Dynamic addFilter(String filterName, Filter filter)
/*     */   {
/* 450 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 451 */       return (FilterRegistration.Dynamic)doPrivileged("addFilter", new Class[] { String.class, Filter.class }, new Object[] { filterName, filter });
/*     */     }
/*     */     
/*     */ 
/* 455 */     return this.context.addFilter(filterName, filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
/*     */   {
/* 463 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 464 */       return (FilterRegistration.Dynamic)doPrivileged("addFilter", new Class[] { String.class, Class.class }, new Object[] { filterName, filterClass });
/*     */     }
/*     */     
/*     */ 
/* 468 */     return this.context.addFilter(filterName, filterClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T extends Filter> T createFilter(Class<T> c)
/*     */     throws ServletException
/*     */   {
/* 476 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 478 */         return (Filter)invokeMethod(this.context, "createFilter", new Object[] { c });
/*     */       }
/*     */       catch (Throwable t) {
/* 481 */         ExceptionUtils.handleThrowable(t);
/* 482 */         if ((t instanceof ServletException)) {
/* 483 */           throw ((ServletException)t);
/*     */         }
/* 485 */         return null;
/*     */       }
/*     */     }
/* 488 */     return this.context.createFilter(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FilterRegistration getFilterRegistration(String filterName)
/*     */   {
/* 495 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 496 */       return (FilterRegistration)doPrivileged("getFilterRegistration", new Object[] { filterName });
/*     */     }
/*     */     
/* 499 */     return this.context.getFilterRegistration(filterName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistration.Dynamic addServlet(String servletName, String className)
/*     */   {
/* 507 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 508 */       return (ServletRegistration.Dynamic)doPrivileged("addServlet", new Object[] { servletName, className });
/*     */     }
/*     */     
/* 511 */     return this.context.addServlet(servletName, className);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet)
/*     */   {
/* 519 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 520 */       return (ServletRegistration.Dynamic)doPrivileged("addServlet", new Class[] { String.class, Servlet.class }, new Object[] { servletName, servlet });
/*     */     }
/*     */     
/*     */ 
/* 524 */     return this.context.addServlet(servletName, servlet);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass)
/*     */   {
/* 532 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 533 */       return (ServletRegistration.Dynamic)doPrivileged("addServlet", new Class[] { String.class, Class.class }, new Object[] { servletName, servletClass });
/*     */     }
/*     */     
/*     */ 
/* 537 */     return this.context.addServlet(servletName, servletClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletRegistration.Dynamic addJspFile(String jspName, String jspFile)
/*     */   {
/* 544 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 545 */       return (ServletRegistration.Dynamic)doPrivileged("addJspFile", new Object[] { jspName, jspFile });
/*     */     }
/*     */     
/* 548 */     return this.context.addJspFile(jspName, jspFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends Servlet> T createServlet(Class<T> c)
/*     */     throws ServletException
/*     */   {
/* 557 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 559 */         return (Servlet)invokeMethod(this.context, "createServlet", new Object[] { c });
/*     */       }
/*     */       catch (Throwable t) {
/* 562 */         ExceptionUtils.handleThrowable(t);
/* 563 */         if ((t instanceof ServletException)) {
/* 564 */           throw ((ServletException)t);
/*     */         }
/* 566 */         return null;
/*     */       }
/*     */     }
/* 569 */     return this.context.createServlet(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletRegistration getServletRegistration(String servletName)
/*     */   {
/* 576 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 577 */       return (ServletRegistration)doPrivileged("getServletRegistration", new Object[] { servletName });
/*     */     }
/*     */     
/* 580 */     return this.context.getServletRegistration(servletName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
/*     */   {
/* 588 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 589 */       return 
/* 590 */         (Set)doPrivileged("getDefaultSessionTrackingModes", null);
/*     */     }
/* 592 */     return this.context.getDefaultSessionTrackingModes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
/*     */   {
/* 599 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 600 */       return 
/* 601 */         (Set)doPrivileged("getEffectiveSessionTrackingModes", null);
/*     */     }
/* 603 */     return this.context.getEffectiveSessionTrackingModes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SessionCookieConfig getSessionCookieConfig()
/*     */   {
/* 610 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 611 */       return 
/* 612 */         (SessionCookieConfig)doPrivileged("getSessionCookieConfig", null);
/*     */     }
/* 614 */     return this.context.getSessionCookieConfig();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
/*     */   {
/* 622 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 623 */       doPrivileged("setSessionTrackingModes", new Object[] { sessionTrackingModes });
/*     */     }
/*     */     else {
/* 626 */       this.context.setSessionTrackingModes(sessionTrackingModes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean setInitParameter(String name, String value)
/*     */   {
/* 633 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 634 */       return 
/* 635 */         ((Boolean)doPrivileged("setInitParameter", new Object[] { name, value })).booleanValue();
/*     */     }
/* 637 */     return this.context.setInitParameter(name, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addListener(Class<? extends EventListener> listenerClass)
/*     */   {
/* 644 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 645 */       doPrivileged("addListener", new Class[] { Class.class }, new Object[] { listenerClass });
/*     */     }
/*     */     else
/*     */     {
/* 649 */       this.context.addListener(listenerClass);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addListener(String className)
/*     */   {
/* 656 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 657 */       doPrivileged("addListener", new Object[] { className });
/*     */     }
/*     */     else {
/* 660 */       this.context.addListener(className);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public <T extends EventListener> void addListener(T t)
/*     */   {
/* 667 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 668 */       doPrivileged("addListener", new Class[] { EventListener.class }, new Object[] { t });
/*     */     }
/*     */     else
/*     */     {
/* 672 */       this.context.addListener(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T extends EventListener> T createListener(Class<T> c)
/*     */     throws ServletException
/*     */   {
/* 681 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 683 */         return (EventListener)invokeMethod(this.context, "createListener", new Object[] { c });
/*     */       }
/*     */       catch (Throwable t) {
/* 686 */         ExceptionUtils.handleThrowable(t);
/* 687 */         if ((t instanceof ServletException)) {
/* 688 */           throw ((ServletException)t);
/*     */         }
/* 690 */         return null;
/*     */       }
/*     */     }
/* 693 */     return this.context.createListener(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void declareRoles(String... roleNames)
/*     */   {
/* 700 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 701 */       doPrivileged("declareRoles", new Object[] { roleNames });
/*     */     } else {
/* 703 */       this.context.declareRoles(roleNames);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 710 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 711 */       return (ClassLoader)doPrivileged("getClassLoader", null);
/*     */     }
/* 713 */     return this.context.getClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getEffectiveMajorVersion()
/*     */   {
/* 720 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 721 */       return 
/* 722 */         ((Integer)doPrivileged("getEffectiveMajorVersion", null)).intValue();
/*     */     }
/* 724 */     return this.context.getEffectiveMajorVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getEffectiveMinorVersion()
/*     */   {
/* 731 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 732 */       return 
/* 733 */         ((Integer)doPrivileged("getEffectiveMinorVersion", null)).intValue();
/*     */     }
/* 735 */     return this.context.getEffectiveMinorVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ? extends FilterRegistration> getFilterRegistrations()
/*     */   {
/* 743 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 744 */       return (Map)doPrivileged("getFilterRegistrations", null);
/*     */     }
/*     */     
/* 747 */     return this.context.getFilterRegistrations();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JspConfigDescriptor getJspConfigDescriptor()
/*     */   {
/* 754 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 755 */       return (JspConfigDescriptor)doPrivileged("getJspConfigDescriptor", null);
/*     */     }
/*     */     
/* 758 */     return this.context.getJspConfigDescriptor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ? extends ServletRegistration> getServletRegistrations()
/*     */   {
/* 766 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 767 */       return (Map)doPrivileged("getServletRegistrations", null);
/*     */     }
/*     */     
/* 770 */     return this.context.getServletRegistrations();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getVirtualServerName()
/*     */   {
/* 777 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 778 */       return (String)doPrivileged("getVirtualServerName", null);
/*     */     }
/* 780 */     return this.context.getVirtualServerName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSessionTimeout()
/*     */   {
/* 787 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 788 */       return ((Integer)doPrivileged("getSessionTimeout", null)).intValue();
/*     */     }
/* 790 */     return this.context.getSessionTimeout();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSessionTimeout(int sessionTimeout)
/*     */   {
/* 797 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 798 */       doPrivileged("setSessionTimeout", new Object[] { Integer.valueOf(sessionTimeout) });
/*     */     } else {
/* 800 */       this.context.setSessionTimeout(sessionTimeout);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getRequestCharacterEncoding()
/*     */   {
/* 807 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 808 */       return (String)doPrivileged("getRequestCharacterEncoding", null);
/*     */     }
/* 810 */     return this.context.getRequestCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRequestCharacterEncoding(String encoding)
/*     */   {
/* 817 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 818 */       doPrivileged("setRequestCharacterEncoding", new Object[] { encoding });
/*     */     } else {
/* 820 */       this.context.setRequestCharacterEncoding(encoding);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getResponseCharacterEncoding()
/*     */   {
/* 827 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 828 */       return (String)doPrivileged("getResponseCharacterEncoding", null);
/*     */     }
/* 830 */     return this.context.getResponseCharacterEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setResponseCharacterEncoding(String encoding)
/*     */   {
/* 837 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 838 */       doPrivileged("setResponseCharacterEncoding", new Object[] { encoding });
/*     */     } else {
/* 840 */       this.context.setResponseCharacterEncoding(encoding);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object doPrivileged(String methodName, Object[] params)
/*     */   {
/*     */     try
/*     */     {
/* 853 */       return invokeMethod(this.context, methodName, params);
/*     */     } catch (Throwable t) {
/* 855 */       ExceptionUtils.handleThrowable(t);
/* 856 */       throw new RuntimeException(t.getMessage(), t);
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
/*     */   private Object invokeMethod(ApplicationContext appContext, String methodName, Object[] params)
/*     */     throws Throwable
/*     */   {
/*     */     try
/*     */     {
/* 875 */       Method method = (Method)this.objectCache.get(methodName);
/* 876 */       if (method == null)
/*     */       {
/* 878 */         method = appContext.getClass().getMethod(methodName, (Class[])this.classCache.get(methodName));
/* 879 */         this.objectCache.put(methodName, method);
/*     */       }
/*     */       
/* 882 */       return executeMethod(method, appContext, params);
/*     */     } catch (Exception ex) { Object localObject1;
/* 884 */       handleException(ex);
/* 885 */       return null;
/*     */     } finally {
/* 887 */       params = null;
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
/*     */   private Object doPrivileged(String methodName, Class<?>[] clazz, Object[] params)
/*     */   {
/*     */     try
/*     */     {
/* 903 */       Method method = this.context.getClass().getMethod(methodName, clazz);
/* 904 */       return executeMethod(method, this.context, params);
/*     */     } catch (Exception ex) {
/*     */       try {
/* 907 */         handleException(ex);
/*     */       } catch (Throwable t) {
/* 909 */         ExceptionUtils.handleThrowable(t);
/* 910 */         throw new RuntimeException(t.getMessage());
/*     */       }
/* 912 */       return null;
/*     */     } finally {
/* 914 */       params = null;
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
/*     */   private Object executeMethod(Method method, ApplicationContext context, Object[] params)
/*     */     throws PrivilegedActionException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 933 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/* 934 */       return AccessController.doPrivileged(new PrivilegedExecuteMethod(method, context, params));
/*     */     }
/*     */     
/* 937 */     return method.invoke(context, params);
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
/*     */   private void handleException(Exception ex)
/*     */     throws Throwable
/*     */   {
/* 952 */     if ((ex instanceof PrivilegedActionException)) {
/* 953 */       ex = ((PrivilegedActionException)ex).getException();
/*     */     }
/*     */     Throwable realException;
/* 956 */     if ((ex instanceof InvocationTargetException)) {
/* 957 */       Throwable realException = ex.getCause();
/* 958 */       if (realException == null) {
/* 959 */         realException = ex;
/*     */       }
/*     */     } else {
/* 962 */       realException = ex;
/*     */     }
/*     */     
/* 965 */     throw realException;
/*     */   }
/*     */   
/*     */   private static class PrivilegedExecuteMethod implements PrivilegedExceptionAction<Object>
/*     */   {
/*     */     private final Method method;
/*     */     private final ApplicationContext context;
/*     */     private final Object[] params;
/*     */     
/*     */     public PrivilegedExecuteMethod(Method method, ApplicationContext context, Object[] params)
/*     */     {
/* 976 */       this.method = method;
/* 977 */       this.context = context;
/* 978 */       this.params = params;
/*     */     }
/*     */     
/*     */     public Object run() throws Exception
/*     */     {
/* 983 */       return this.method.invoke(this.context, this.params);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationContextFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */