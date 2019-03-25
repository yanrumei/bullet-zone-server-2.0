/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLDecoder;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import javax.naming.NamingException;
/*      */ import javax.servlet.Filter;
/*      */ import javax.servlet.FilterRegistration;
/*      */ import javax.servlet.FilterRegistration.Dynamic;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.Servlet;
/*      */ import javax.servlet.ServletContextAttributeEvent;
/*      */ import javax.servlet.ServletContextAttributeListener;
/*      */ import javax.servlet.ServletContextListener;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRegistration;
/*      */ import javax.servlet.ServletRegistration.Dynamic;
/*      */ import javax.servlet.ServletRequestAttributeListener;
/*      */ import javax.servlet.ServletRequestListener;
/*      */ import javax.servlet.SessionCookieConfig;
/*      */ import javax.servlet.SessionTrackingMode;
/*      */ import javax.servlet.descriptor.JspConfigDescriptor;
/*      */ import javax.servlet.http.HttpSessionAttributeListener;
/*      */ import javax.servlet.http.HttpSessionIdListener;
/*      */ import javax.servlet.http.HttpSessionListener;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.Wrapper;
/*      */ import org.apache.catalina.connector.Connector;
/*      */ import org.apache.catalina.mapper.Mapper;
/*      */ import org.apache.catalina.mapper.MappingData;
/*      */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*      */ import org.apache.catalina.util.ServerInfo;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.InstanceManager;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.CharChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.descriptor.web.FilterDef;
/*      */ import org.apache.tomcat.util.http.RequestUtil;
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
/*      */ 
/*      */ 
/*      */ public class ApplicationContext
/*      */   implements org.apache.catalina.servlet4preview.ServletContext
/*      */ {
/*   99 */   protected static final boolean STRICT_SERVLET_COMPLIANCE = Globals.STRICT_SERVLET_COMPLIANCE;
/*      */   
/*  101 */   static { String requireSlash = System.getProperty("org.apache.catalina.core.ApplicationContext.GET_RESOURCE_REQUIRE_SLASH");
/*      */     
/*  103 */     if (requireSlash == null) {
/*  104 */       GET_RESOURCE_REQUIRE_SLASH = STRICT_SERVLET_COMPLIANCE;
/*      */     } else {
/*  106 */       GET_RESOURCE_REQUIRE_SLASH = Boolean.parseBoolean(requireSlash);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final boolean GET_RESOURCE_REQUIRE_SLASH;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ApplicationContext(StandardContext context)
/*      */   {
/*  121 */     this.context = context;
/*  122 */     this.service = ((Engine)context.getParent().getParent()).getService();
/*  123 */     this.sessionCookieConfig = new ApplicationSessionCookieConfig(context);
/*      */     
/*      */ 
/*  126 */     populateSessionTrackingModes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  136 */   protected Map<String, Object> attributes = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */   private final Map<String, String> readOnlyAttributes = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final StandardContext context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Service service;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */   private static final List<String> emptyString = Collections.emptyList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  166 */   private static final List<Servlet> emptyServlet = Collections.emptyList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  172 */   private final javax.servlet.ServletContext facade = new ApplicationContextFacade(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  178 */   private final ConcurrentMap<String, String> parameters = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */   private final ThreadLocal<DispatchData> dispatchData = new ThreadLocal();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private SessionCookieConfig sessionCookieConfig;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  202 */   private Set<SessionTrackingMode> sessionTrackingModes = null;
/*  203 */   private Set<SessionTrackingMode> defaultSessionTrackingModes = null;
/*  204 */   private Set<SessionTrackingMode> supportedSessionTrackingModes = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  211 */   private boolean newServletContextListenerAllowed = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */   {
/*  218 */     return this.attributes.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */   public Enumeration<String> getAttributeNames()
/*      */   {
/*  224 */     Set<String> names = new HashSet();
/*  225 */     names.addAll(this.attributes.keySet());
/*  226 */     return Collections.enumeration(names);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public javax.servlet.ServletContext getContext(String uri)
/*      */   {
/*  234 */     if ((uri == null) || (!uri.startsWith("/"))) {
/*  235 */       return null;
/*      */     }
/*      */     
/*  238 */     Context child = null;
/*      */     try
/*      */     {
/*  241 */       Container host = this.context.getParent();
/*  242 */       child = (Context)host.findChild(uri);
/*      */       
/*      */ 
/*  245 */       if ((child != null) && (!child.getState().isAvailable())) {
/*  246 */         child = null;
/*      */       }
/*      */       
/*      */ 
/*  250 */       if (child == null) {
/*  251 */         int i = uri.indexOf("##");
/*  252 */         if (i > -1) {
/*  253 */           uri = uri.substring(0, i);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  259 */         MessageBytes hostMB = MessageBytes.newInstance();
/*  260 */         hostMB.setString(host.getName());
/*      */         
/*  262 */         MessageBytes pathMB = MessageBytes.newInstance();
/*  263 */         pathMB.setString(uri);
/*      */         
/*  265 */         MappingData mappingData = new MappingData();
/*  266 */         ((Engine)host.getParent()).getService().getMapper().map(hostMB, pathMB, null, mappingData);
/*  267 */         child = mappingData.context;
/*      */       }
/*      */     } catch (Throwable t) {
/*  270 */       ExceptionUtils.handleThrowable(t);
/*  271 */       return null;
/*      */     }
/*      */     
/*  274 */     if (child == null) {
/*  275 */       return null;
/*      */     }
/*      */     
/*  278 */     if (this.context.getCrossContext())
/*      */     {
/*  280 */       return child.getServletContext(); }
/*  281 */     if (child == this.context)
/*      */     {
/*  283 */       return this.context.getServletContext();
/*      */     }
/*      */     
/*  286 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getContextPath()
/*      */   {
/*  293 */     return this.context.getPath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getInitParameter(String name)
/*      */   {
/*  301 */     if (("org.apache.jasper.XML_VALIDATE_TLD".equals(name)) && 
/*  302 */       (this.context.getTldValidation())) {
/*  303 */       return "true";
/*      */     }
/*  305 */     if (("org.apache.jasper.XML_BLOCK_EXTERNAL".equals(name)) && 
/*  306 */       (!this.context.getXmlBlockExternal()))
/*      */     {
/*  308 */       return "false";
/*      */     }
/*      */     
/*  311 */     return (String)this.parameters.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */   public Enumeration<String> getInitParameterNames()
/*      */   {
/*  317 */     Set<String> names = new HashSet();
/*  318 */     names.addAll(this.parameters.keySet());
/*      */     
/*      */ 
/*  321 */     if (this.context.getTldValidation()) {
/*  322 */       names.add("org.apache.jasper.XML_VALIDATE_TLD");
/*      */     }
/*  324 */     if (!this.context.getXmlBlockExternal()) {
/*  325 */       names.add("org.apache.jasper.XML_BLOCK_EXTERNAL");
/*      */     }
/*  327 */     return Collections.enumeration(names);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getMajorVersion()
/*      */   {
/*  333 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getMinorVersion()
/*      */   {
/*  339 */     return 1;
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
/*      */   public String getMimeType(String file)
/*      */   {
/*  352 */     if (file == null)
/*  353 */       return null;
/*  354 */     int period = file.lastIndexOf('.');
/*  355 */     if (period < 0)
/*  356 */       return null;
/*  357 */     String extension = file.substring(period + 1);
/*  358 */     if (extension.length() < 1)
/*  359 */       return null;
/*  360 */     return this.context.findMimeMapping(extension);
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
/*      */ 
/*      */ 
/*      */   public RequestDispatcher getNamedDispatcher(String name)
/*      */   {
/*  375 */     if (name == null) {
/*  376 */       return null;
/*      */     }
/*      */     
/*  379 */     Wrapper wrapper = (Wrapper)this.context.findChild(name);
/*  380 */     if (wrapper == null) {
/*  381 */       return null;
/*      */     }
/*  383 */     return new ApplicationDispatcher(wrapper, null, null, null, null, null, name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRealPath(String path)
/*      */   {
/*  390 */     String validatedPath = validateResourcePath(path, true);
/*  391 */     return this.context.getRealPath(validatedPath);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public RequestDispatcher getRequestDispatcher(String path)
/*      */   {
/*  399 */     if (path == null) {
/*  400 */       return null;
/*      */     }
/*  402 */     if (!path.startsWith("/"))
/*      */     {
/*  404 */       throw new IllegalArgumentException(sm.getString("applicationContext.requestDispatcher.iae", new Object[] { path }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  412 */     int pos = path.indexOf('?');
/*  413 */     String queryString; String uri; String queryString; if (pos >= 0) {
/*  414 */       String uri = path.substring(0, pos);
/*  415 */       queryString = path.substring(pos + 1);
/*      */     } else {
/*  417 */       uri = path;
/*  418 */       queryString = null;
/*      */     }
/*      */     
/*  421 */     String normalizedPath = RequestUtil.normalize(uri);
/*  422 */     if (normalizedPath == null) {
/*  423 */       return null;
/*      */     }
/*      */     
/*  426 */     if (getContext().getDispatchersUseEncodedPaths())
/*      */     {
/*      */       try
/*      */       {
/*  430 */         decodedPath = URLDecoder.decode(normalizedPath, "UTF-8");
/*      */       } catch (UnsupportedEncodingException e) {
/*      */         String decodedPath;
/*  433 */         return null;
/*      */       }
/*      */       
/*      */       String decodedPath;
/*  437 */       normalizedPath = RequestUtil.normalize(decodedPath);
/*  438 */       if (!decodedPath.equals(normalizedPath)) {
/*  439 */         getContext().getLogger().warn(sm
/*  440 */           .getString("applicationContext.illegalDispatchPath", new Object[] { path }), new IllegalArgumentException());
/*      */         
/*  442 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  446 */       uri = URLEncoder.DEFAULT.encode(getContextPath(), StandardCharsets.UTF_8) + uri;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  452 */       uri = URLEncoder.DEFAULT.encode(getContextPath() + uri, StandardCharsets.UTF_8);
/*      */     }
/*      */     
/*  455 */     pos = normalizedPath.length();
/*      */     
/*      */ 
/*  458 */     DispatchData dd = (DispatchData)this.dispatchData.get();
/*  459 */     if (dd == null) {
/*  460 */       dd = new DispatchData();
/*  461 */       this.dispatchData.set(dd);
/*      */     }
/*      */     
/*  464 */     MessageBytes uriMB = dd.uriMB;
/*  465 */     uriMB.recycle();
/*      */     
/*      */ 
/*  468 */     MappingData mappingData = dd.mappingData;
/*      */     
/*      */ 
/*  471 */     CharChunk uriCC = uriMB.getCharChunk();
/*      */     try {
/*  473 */       uriCC.append(this.context.getPath(), 0, this.context.getPath().length());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  478 */       int semicolon = normalizedPath.indexOf(';');
/*  479 */       if ((pos >= 0) && (semicolon > pos)) {
/*  480 */         semicolon = -1;
/*      */       }
/*  482 */       uriCC.append(normalizedPath, 0, semicolon > 0 ? semicolon : pos);
/*  483 */       this.service.getMapper().map(this.context, uriMB, mappingData);
/*  484 */       if (mappingData.wrapper == null) {
/*  485 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  492 */       if (semicolon > 0) {
/*  493 */         uriCC.append(normalizedPath, semicolon, pos - semicolon);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  497 */       log(sm.getString("applicationContext.mapping.error"), e);
/*  498 */       return null;
/*      */     }
/*      */     
/*  501 */     Wrapper wrapper = mappingData.wrapper;
/*  502 */     String wrapperPath = mappingData.wrapperPath.toString();
/*  503 */     String pathInfo = mappingData.pathInfo.toString();
/*  504 */     ServletMapping mapping = new ApplicationMapping(mappingData).getServletMapping();
/*      */     
/*  506 */     mappingData.recycle();
/*      */     
/*      */ 
/*  509 */     return new ApplicationDispatcher(wrapper, uri, wrapperPath, pathInfo, queryString, mapping, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public URL getResource(String path)
/*      */     throws MalformedURLException
/*      */   {
/*  517 */     String validatedPath = validateResourcePath(path, false);
/*      */     
/*  519 */     if (validatedPath == null)
/*      */     {
/*  521 */       throw new MalformedURLException(sm.getString("applicationContext.requestDispatcher.iae", new Object[] { path }));
/*      */     }
/*      */     
/*  524 */     WebResourceRoot resources = this.context.getResources();
/*  525 */     if (resources != null) {
/*  526 */       return resources.getResource(validatedPath).getURL();
/*      */     }
/*      */     
/*  529 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public InputStream getResourceAsStream(String path)
/*      */   {
/*  536 */     String validatedPath = validateResourcePath(path, false);
/*      */     
/*  538 */     if (validatedPath == null) {
/*  539 */       return null;
/*      */     }
/*      */     
/*  542 */     WebResourceRoot resources = this.context.getResources();
/*  543 */     if (resources != null) {
/*  544 */       return resources.getResource(validatedPath).getInputStream();
/*      */     }
/*      */     
/*  547 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String validateResourcePath(String path, boolean allowEmptyPath)
/*      */   {
/*  556 */     if (path == null) {
/*  557 */       return null;
/*      */     }
/*      */     
/*  560 */     if ((path.length() == 0) && (allowEmptyPath)) {
/*  561 */       return path;
/*      */     }
/*      */     
/*  564 */     if (!path.startsWith("/")) {
/*  565 */       if (GET_RESOURCE_REQUIRE_SLASH) {
/*  566 */         return null;
/*      */       }
/*  568 */       return "/" + path;
/*      */     }
/*      */     
/*      */ 
/*  572 */     return path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getResourcePaths(String path)
/*      */   {
/*  580 */     if (path == null) {
/*  581 */       return null;
/*      */     }
/*  583 */     if (!path.startsWith("/"))
/*      */     {
/*  585 */       throw new IllegalArgumentException(sm.getString("applicationContext.resourcePaths.iae", new Object[] { path }));
/*      */     }
/*      */     
/*  588 */     WebResourceRoot resources = this.context.getResources();
/*  589 */     if (resources != null) {
/*  590 */       return resources.listWebAppPaths(path);
/*      */     }
/*      */     
/*  593 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getServerInfo()
/*      */   {
/*  599 */     return ServerInfo.getServerInfo();
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Servlet getServlet(String name)
/*      */   {
/*  606 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public String getServletContextName()
/*      */   {
/*  612 */     return this.context.getDisplayName();
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Enumeration<String> getServletNames()
/*      */   {
/*  619 */     return Collections.enumeration(emptyString);
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Enumeration<Servlet> getServlets()
/*      */   {
/*  626 */     return Collections.enumeration(emptyServlet);
/*      */   }
/*      */   
/*      */ 
/*      */   public void log(String message)
/*      */   {
/*  632 */     this.context.getLogger().info(message);
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public void log(Exception exception, String message)
/*      */   {
/*  639 */     this.context.getLogger().error(message, exception);
/*      */   }
/*      */   
/*      */ 
/*      */   public void log(String message, Throwable throwable)
/*      */   {
/*  645 */     this.context.getLogger().error(message, throwable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void removeAttribute(String name)
/*      */   {
/*  652 */     Object value = null;
/*      */     
/*      */ 
/*      */ 
/*  656 */     if (this.readOnlyAttributes.containsKey(name)) {
/*  657 */       return;
/*      */     }
/*  659 */     value = this.attributes.remove(name);
/*  660 */     if (value == null) {
/*  661 */       return;
/*      */     }
/*      */     
/*      */ 
/*  665 */     Object[] listeners = this.context.getApplicationEventListeners();
/*  666 */     if ((listeners == null) || (listeners.length == 0)) {
/*  667 */       return;
/*      */     }
/*  669 */     ServletContextAttributeEvent event = new ServletContextAttributeEvent(this.context.getServletContext(), name, value);
/*      */     
/*  671 */     for (int i = 0; i < listeners.length; i++) {
/*  672 */       if ((listeners[i] instanceof ServletContextAttributeListener))
/*      */       {
/*  674 */         ServletContextAttributeListener listener = (ServletContextAttributeListener)listeners[i];
/*      */         try
/*      */         {
/*  677 */           this.context.fireContainerEvent("beforeContextAttributeRemoved", listener);
/*      */           
/*  679 */           listener.attributeRemoved(event);
/*  680 */           this.context.fireContainerEvent("afterContextAttributeRemoved", listener);
/*      */         }
/*      */         catch (Throwable t) {
/*  683 */           ExceptionUtils.handleThrowable(t);
/*  684 */           this.context.fireContainerEvent("afterContextAttributeRemoved", listener);
/*      */           
/*      */ 
/*  687 */           log(sm.getString("applicationContext.attributeEvent"), t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/*  696 */     if (name == null)
/*      */     {
/*  698 */       throw new NullPointerException(sm.getString("applicationContext.setAttribute.namenull"));
/*      */     }
/*      */     
/*      */ 
/*  702 */     if (value == null) {
/*  703 */       removeAttribute(name);
/*  704 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  709 */     if (this.readOnlyAttributes.containsKey(name)) {
/*  710 */       return;
/*      */     }
/*  712 */     Object oldValue = this.attributes.put(name, value);
/*  713 */     boolean replaced = oldValue != null;
/*      */     
/*      */ 
/*  716 */     Object[] listeners = this.context.getApplicationEventListeners();
/*  717 */     if ((listeners == null) || (listeners.length == 0))
/*  718 */       return;
/*  719 */     ServletContextAttributeEvent event = null;
/*  720 */     if (replaced)
/*      */     {
/*  722 */       event = new ServletContextAttributeEvent(this.context.getServletContext(), name, oldValue);
/*      */     }
/*      */     else
/*      */     {
/*  726 */       event = new ServletContextAttributeEvent(this.context.getServletContext(), name, value);
/*      */     }
/*      */     
/*  729 */     for (int i = 0; i < listeners.length; i++) {
/*  730 */       if ((listeners[i] instanceof ServletContextAttributeListener))
/*      */       {
/*  732 */         ServletContextAttributeListener listener = (ServletContextAttributeListener)listeners[i];
/*      */         try
/*      */         {
/*  735 */           if (replaced)
/*      */           {
/*  737 */             this.context.fireContainerEvent("beforeContextAttributeReplaced", listener);
/*  738 */             listener.attributeReplaced(event);
/*  739 */             this.context.fireContainerEvent("afterContextAttributeReplaced", listener);
/*      */           }
/*      */           else {
/*  742 */             this.context.fireContainerEvent("beforeContextAttributeAdded", listener);
/*      */             
/*  744 */             listener.attributeAdded(event);
/*  745 */             this.context.fireContainerEvent("afterContextAttributeAdded", listener);
/*      */           }
/*      */         }
/*      */         catch (Throwable t) {
/*  749 */           ExceptionUtils.handleThrowable(t);
/*  750 */           if (replaced) {
/*  751 */             this.context.fireContainerEvent("afterContextAttributeReplaced", listener);
/*      */           }
/*      */           else {
/*  754 */             this.context.fireContainerEvent("afterContextAttributeAdded", listener);
/*      */           }
/*      */           
/*  757 */           log(sm.getString("applicationContext.attributeEvent"), t);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public FilterRegistration.Dynamic addFilter(String filterName, String className)
/*      */   {
/*  765 */     return addFilter(filterName, className, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public FilterRegistration.Dynamic addFilter(String filterName, Filter filter)
/*      */   {
/*  771 */     return addFilter(filterName, null, filter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
/*      */   {
/*  778 */     return addFilter(filterName, filterClass.getName(), null);
/*      */   }
/*      */   
/*      */ 
/*      */   private FilterRegistration.Dynamic addFilter(String filterName, String filterClass, Filter filter)
/*      */     throws IllegalStateException
/*      */   {
/*  785 */     if ((filterName == null) || (filterName.equals(""))) {
/*  786 */       throw new IllegalArgumentException(sm.getString("applicationContext.invalidFilterName", new Object[] { filterName }));
/*      */     }
/*      */     
/*      */ 
/*  790 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/*      */ 
/*  793 */       throw new IllegalStateException(sm.getString("applicationContext.addFilter.ise", new Object[] {
/*  794 */         getContextPath() }));
/*      */     }
/*      */     
/*  797 */     FilterDef filterDef = this.context.findFilterDef(filterName);
/*      */     
/*      */ 
/*      */ 
/*  801 */     if (filterDef == null) {
/*  802 */       filterDef = new FilterDef();
/*  803 */       filterDef.setFilterName(filterName);
/*  804 */       this.context.addFilterDef(filterDef);
/*      */     }
/*  806 */     else if ((filterDef.getFilterName() != null) && 
/*  807 */       (filterDef.getFilterClass() != null)) {
/*  808 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  812 */     if (filter == null) {
/*  813 */       filterDef.setFilterClass(filterClass);
/*      */     } else {
/*  815 */       filterDef.setFilterClass(filter.getClass().getName());
/*  816 */       filterDef.setFilter(filter);
/*      */     }
/*      */     
/*  819 */     return new ApplicationFilterRegistration(filterDef, this.context);
/*      */   }
/*      */   
/*      */   public <T extends Filter> T createFilter(Class<T> c)
/*      */     throws ServletException
/*      */   {
/*      */     try
/*      */     {
/*  827 */       return (Filter)this.context.getInstanceManager().newInstance(c.getName());
/*      */     }
/*      */     catch (InvocationTargetException e) {
/*  830 */       ExceptionUtils.handleThrowable(e.getCause());
/*  831 */       throw new ServletException(e);
/*      */     }
/*      */     catch (IllegalAccessException|NamingException|InstantiationException|ClassNotFoundException|NoSuchMethodException e) {
/*  834 */       throw new ServletException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public FilterRegistration getFilterRegistration(String filterName)
/*      */   {
/*  841 */     FilterDef filterDef = this.context.findFilterDef(filterName);
/*  842 */     if (filterDef == null) {
/*  843 */       return null;
/*      */     }
/*  845 */     return new ApplicationFilterRegistration(filterDef, this.context);
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletRegistration.Dynamic addServlet(String servletName, String className)
/*      */   {
/*  851 */     return addServlet(servletName, className, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet)
/*      */   {
/*  857 */     return addServlet(servletName, null, servlet, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass)
/*      */   {
/*  864 */     return addServlet(servletName, servletClass.getName(), null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletRegistration.Dynamic addJspFile(String jspName, String jspFile)
/*      */   {
/*  872 */     if ((jspFile == null) || (!jspFile.startsWith("/")))
/*      */     {
/*  874 */       throw new IllegalArgumentException(sm.getString("applicationContext.addJspFile.iae", new Object[] { jspFile }));
/*      */     }
/*      */     
/*  877 */     String jspServletClassName = null;
/*  878 */     Map<String, String> jspFileInitParams = new HashMap();
/*      */     
/*  880 */     Wrapper jspServlet = (Wrapper)this.context.findChild("jsp");
/*      */     
/*  882 */     if (jspServlet == null)
/*      */     {
/*      */ 
/*  885 */       jspServletClassName = "org.apache.jasper.servlet.JspServlet";
/*      */     }
/*      */     else
/*      */     {
/*  889 */       jspServletClassName = jspServlet.getServletClass();
/*      */       
/*  891 */       String[] params = jspServlet.findInitParameters();
/*  892 */       for (String param : params) {
/*  893 */         jspFileInitParams.put(param, jspServlet.findInitParameter(param));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  898 */     jspFileInitParams.put("jspFile", jspFile);
/*      */     
/*  900 */     return addServlet(jspName, jspServletClassName, null, jspFileInitParams);
/*      */   }
/*      */   
/*      */ 
/*      */   private ServletRegistration.Dynamic addServlet(String servletName, String servletClass, Servlet servlet, Map<String, String> initParams)
/*      */     throws IllegalStateException
/*      */   {
/*  907 */     if ((servletName == null) || (servletName.equals(""))) {
/*  908 */       throw new IllegalArgumentException(sm.getString("applicationContext.invalidServletName", new Object[] { servletName }));
/*      */     }
/*      */     
/*      */ 
/*  912 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/*      */ 
/*  915 */       throw new IllegalStateException(sm.getString("applicationContext.addServlet.ise", new Object[] {
/*  916 */         getContextPath() }));
/*      */     }
/*      */     
/*  919 */     Wrapper wrapper = (Wrapper)this.context.findChild(servletName);
/*      */     
/*      */ 
/*      */ 
/*  923 */     if (wrapper == null) {
/*  924 */       wrapper = this.context.createWrapper();
/*  925 */       wrapper.setName(servletName);
/*  926 */       this.context.addChild(wrapper);
/*      */     }
/*  928 */     else if ((wrapper.getName() != null) && 
/*  929 */       (wrapper.getServletClass() != null)) {
/*  930 */       if (wrapper.isOverridable()) {
/*  931 */         wrapper.setOverridable(false);
/*      */       } else {
/*  933 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  938 */     if (servlet == null) {
/*  939 */       wrapper.setServletClass(servletClass);
/*      */     } else {
/*  941 */       wrapper.setServletClass(servlet.getClass().getName());
/*  942 */       wrapper.setServlet(servlet);
/*      */     }
/*      */     
/*  945 */     if (initParams != null) {
/*  946 */       for (Map.Entry<String, String> initParam : initParams.entrySet()) {
/*  947 */         wrapper.addInitParameter((String)initParam.getKey(), (String)initParam.getValue());
/*      */       }
/*      */     }
/*      */     
/*  951 */     return this.context.dynamicServletAdded(wrapper);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T extends Servlet> T createServlet(Class<T> c)
/*      */     throws ServletException
/*      */   {
/*      */     try
/*      */     {
/*  960 */       T servlet = (Servlet)this.context.getInstanceManager().newInstance(c.getName());
/*  961 */       this.context.dynamicServletCreated(servlet);
/*  962 */       return servlet;
/*      */     } catch (InvocationTargetException e) {
/*  964 */       ExceptionUtils.handleThrowable(e.getCause());
/*  965 */       throw new ServletException(e);
/*      */     }
/*      */     catch (IllegalAccessException|NamingException|InstantiationException|ClassNotFoundException|NoSuchMethodException e) {
/*  968 */       throw new ServletException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ServletRegistration getServletRegistration(String servletName)
/*      */   {
/*  975 */     Wrapper wrapper = (Wrapper)this.context.findChild(servletName);
/*  976 */     if (wrapper == null) {
/*  977 */       return null;
/*      */     }
/*      */     
/*  980 */     return new ApplicationServletRegistration(wrapper, this.context);
/*      */   }
/*      */   
/*      */ 
/*      */   public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
/*      */   {
/*  986 */     return this.defaultSessionTrackingModes;
/*      */   }
/*      */   
/*      */ 
/*      */   private void populateSessionTrackingModes()
/*      */   {
/*  992 */     this.defaultSessionTrackingModes = EnumSet.of(SessionTrackingMode.URL);
/*  993 */     this.supportedSessionTrackingModes = EnumSet.of(SessionTrackingMode.URL);
/*      */     
/*  995 */     if (this.context.getCookies()) {
/*  996 */       this.defaultSessionTrackingModes.add(SessionTrackingMode.COOKIE);
/*  997 */       this.supportedSessionTrackingModes.add(SessionTrackingMode.COOKIE);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1002 */     Service s = ((Engine)this.context.getParent().getParent()).getService();
/* 1003 */     Connector[] connectors = s.findConnectors();
/*      */     
/* 1005 */     for (Connector connector : connectors) {
/* 1006 */       if (Boolean.TRUE.equals(connector.getAttribute("SSLEnabled"))) {
/* 1007 */         this.supportedSessionTrackingModes.add(SessionTrackingMode.SSL);
/* 1008 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
/*      */   {
/* 1016 */     if (this.sessionTrackingModes != null) {
/* 1017 */       return this.sessionTrackingModes;
/*      */     }
/* 1019 */     return this.defaultSessionTrackingModes;
/*      */   }
/*      */   
/*      */ 
/*      */   public SessionCookieConfig getSessionCookieConfig()
/*      */   {
/* 1025 */     return this.sessionCookieConfig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
/*      */   {
/* 1032 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1034 */       throw new IllegalStateException(sm.getString("applicationContext.setSessionTracking.ise", new Object[] {
/* 1035 */         getContextPath() }));
/*      */     }
/*      */     
/*      */ 
/* 1039 */     for (SessionTrackingMode sessionTrackingMode : sessionTrackingModes) {
/* 1040 */       if (!this.supportedSessionTrackingModes.contains(sessionTrackingMode)) {
/* 1041 */         throw new IllegalArgumentException(sm.getString("applicationContext.setSessionTracking.iae.invalid", new Object[] {sessionTrackingMode
/*      */         
/* 1043 */           .toString(), getContextPath() }));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1048 */     if ((sessionTrackingModes.contains(SessionTrackingMode.SSL)) && 
/* 1049 */       (sessionTrackingModes.size() > 1)) {
/* 1050 */       throw new IllegalArgumentException(sm.getString("applicationContext.setSessionTracking.iae.ssl", new Object[] {
/*      */       
/* 1052 */         getContextPath() }));
/*      */     }
/*      */     
/*      */ 
/* 1056 */     this.sessionTrackingModes = sessionTrackingModes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean setInitParameter(String name, String value)
/*      */   {
/* 1063 */     if (name == null)
/*      */     {
/* 1065 */       throw new NullPointerException(sm.getString("applicationContext.setAttribute.namenull"));
/*      */     }
/* 1067 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1069 */       throw new IllegalStateException(sm.getString("applicationContext.setInitParam.ise", new Object[] {
/* 1070 */         getContextPath() }));
/*      */     }
/*      */     
/* 1073 */     return this.parameters.putIfAbsent(name, value) == null;
/*      */   }
/*      */   
/*      */ 
/*      */   public void addListener(Class<? extends EventListener> listenerClass)
/*      */   {
/*      */     try
/*      */     {
/* 1081 */       listener = createListener(listenerClass);
/*      */     } catch (ServletException e) { EventListener listener;
/* 1083 */       throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.init", new Object[] {listenerClass
/*      */       
/* 1085 */         .getName() }), e); }
/*      */     EventListener listener;
/* 1087 */     addListener(listener);
/*      */   }
/*      */   
/*      */ 
/*      */   public void addListener(String className)
/*      */   {
/*      */     try
/*      */     {
/* 1095 */       if (this.context.getInstanceManager() != null) {
/* 1096 */         Object obj = this.context.getInstanceManager().newInstance(className);
/*      */         
/* 1098 */         if (!(obj instanceof EventListener)) {
/* 1099 */           throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.wrongType", new Object[] { className }));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1104 */         EventListener listener = (EventListener)obj;
/* 1105 */         addListener(listener);
/*      */       }
/*      */     } catch (InvocationTargetException e) {
/* 1108 */       ExceptionUtils.handleThrowable(e.getCause());
/* 1109 */       throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.cnfe", new Object[] { className }), e);
/*      */ 
/*      */     }
/*      */     catch (IllegalAccessException|NamingException|InstantiationException|ClassNotFoundException|NoSuchMethodException e)
/*      */     {
/* 1114 */       throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.cnfe", new Object[] { className }), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends EventListener> void addListener(T t)
/*      */   {
/* 1124 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1126 */       throw new IllegalStateException(sm.getString("applicationContext.addListener.ise", new Object[] {
/* 1127 */         getContextPath() }));
/*      */     }
/*      */     
/* 1130 */     boolean match = false;
/* 1131 */     if (((t instanceof ServletContextAttributeListener)) || ((t instanceof ServletRequestListener)) || ((t instanceof ServletRequestAttributeListener)) || ((t instanceof HttpSessionIdListener)) || ((t instanceof HttpSessionAttributeListener)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1136 */       this.context.addApplicationEventListener(t);
/* 1137 */       match = true;
/*      */     }
/*      */     
/* 1140 */     if (((t instanceof HttpSessionListener)) || (((t instanceof ServletContextListener)) && (this.newServletContextListenerAllowed)))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1145 */       this.context.addApplicationLifecycleListener(t);
/* 1146 */       match = true;
/*      */     }
/*      */     
/* 1149 */     if (match) { return;
/*      */     }
/* 1151 */     if ((t instanceof ServletContextListener)) {
/* 1152 */       throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.sclNotAllowed", new Object[] {t
/*      */       
/* 1154 */         .getClass().getName() }));
/*      */     }
/* 1156 */     throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.wrongType", new Object[] {t
/*      */     
/* 1158 */       .getClass().getName() }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends EventListener> T createListener(Class<T> c)
/*      */     throws ServletException
/*      */   {
/*      */     try
/*      */     {
/* 1169 */       T listener = (EventListener)this.context.getInstanceManager().newInstance(c);
/* 1170 */       if (((listener instanceof ServletContextListener)) || ((listener instanceof ServletContextAttributeListener)) || ((listener instanceof ServletRequestListener)) || ((listener instanceof ServletRequestAttributeListener)) || ((listener instanceof HttpSessionListener)) || ((listener instanceof HttpSessionIdListener)) || ((listener instanceof HttpSessionAttributeListener)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1177 */         return listener;
/*      */       }
/* 1179 */       throw new IllegalArgumentException(sm.getString("applicationContext.addListener.iae.wrongType", new Object[] {listener
/*      */       
/* 1181 */         .getClass().getName() }));
/*      */     } catch (InvocationTargetException e) {
/* 1183 */       ExceptionUtils.handleThrowable(e.getCause());
/* 1184 */       throw new ServletException(e);
/*      */     }
/*      */     catch (IllegalAccessException|NamingException|InstantiationException|NoSuchMethodException e) {
/* 1187 */       throw new ServletException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void declareRoles(String... roleNames)
/*      */   {
/* 1195 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/*      */ 
/* 1198 */       throw new IllegalStateException(sm.getString("applicationContext.addRole.ise", new Object[] {
/* 1199 */         getContextPath() }));
/*      */     }
/*      */     
/* 1202 */     if (roleNames == null)
/*      */     {
/* 1204 */       throw new IllegalArgumentException(sm.getString("applicationContext.roles.iae", new Object[] {
/* 1205 */         getContextPath() }));
/*      */     }
/*      */     
/* 1208 */     for (String role : roleNames) {
/* 1209 */       if ((role == null) || ("".equals(role)))
/*      */       {
/* 1211 */         throw new IllegalArgumentException(sm.getString("applicationContext.role.iae", new Object[] {
/* 1212 */           getContextPath() }));
/*      */       }
/* 1214 */       this.context.addSecurityRole(role);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ClassLoader getClassLoader()
/*      */   {
/* 1221 */     ClassLoader result = this.context.getLoader().getClassLoader();
/* 1222 */     if (Globals.IS_SECURITY_ENABLED) {
/* 1223 */       ClassLoader tccl = Thread.currentThread().getContextClassLoader();
/* 1224 */       ClassLoader parent = result;
/* 1225 */       while ((parent != null) && 
/* 1226 */         (parent != tccl))
/*      */       {
/*      */ 
/* 1229 */         parent = parent.getParent();
/*      */       }
/* 1231 */       if (parent == null) {
/* 1232 */         System.getSecurityManager().checkPermission(new RuntimePermission("getClassLoader"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1237 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getEffectiveMajorVersion()
/*      */   {
/* 1243 */     return this.context.getEffectiveMajorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getEffectiveMinorVersion()
/*      */   {
/* 1249 */     return this.context.getEffectiveMinorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<String, ? extends FilterRegistration> getFilterRegistrations()
/*      */   {
/* 1255 */     Map<String, ApplicationFilterRegistration> result = new HashMap();
/*      */     
/* 1257 */     FilterDef[] filterDefs = this.context.findFilterDefs();
/* 1258 */     for (FilterDef filterDef : filterDefs) {
/* 1259 */       result.put(filterDef.getFilterName(), new ApplicationFilterRegistration(filterDef, this.context));
/*      */     }
/*      */     
/*      */ 
/* 1263 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public JspConfigDescriptor getJspConfigDescriptor()
/*      */   {
/* 1269 */     return this.context.getJspConfigDescriptor();
/*      */   }
/*      */   
/*      */ 
/*      */   public Map<String, ? extends ServletRegistration> getServletRegistrations()
/*      */   {
/* 1275 */     Map<String, ApplicationServletRegistration> result = new HashMap();
/*      */     
/* 1277 */     Container[] wrappers = this.context.findChildren();
/* 1278 */     for (Container wrapper : wrappers) {
/* 1279 */       result.put(((Wrapper)wrapper).getName(), new ApplicationServletRegistration((Wrapper)wrapper, this.context));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1284 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getVirtualServerName()
/*      */   {
/* 1291 */     Container host = this.context.getParent();
/* 1292 */     Container engine = host.getParent();
/* 1293 */     return engine.getName() + "/" + host.getName();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getSessionTimeout()
/*      */   {
/* 1299 */     return this.context.getSessionTimeout();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSessionTimeout(int sessionTimeout)
/*      */   {
/* 1305 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1307 */       throw new IllegalStateException(sm.getString("applicationContext.setSessionTimeout.ise", new Object[] {
/* 1308 */         getContextPath() }));
/*      */     }
/*      */     
/* 1311 */     this.context.setSessionTimeout(sessionTimeout);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getRequestCharacterEncoding()
/*      */   {
/* 1317 */     return this.context.getRequestCharacterEncoding();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setRequestCharacterEncoding(String encoding)
/*      */   {
/* 1323 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1325 */       throw new IllegalStateException(sm.getString("applicationContext.setRequestEncoding.ise", new Object[] {
/* 1326 */         getContextPath() }));
/*      */     }
/*      */     
/* 1329 */     this.context.setRequestCharacterEncoding(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getResponseCharacterEncoding()
/*      */   {
/* 1335 */     return this.context.getResponseCharacterEncoding();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setResponseCharacterEncoding(String encoding)
/*      */   {
/* 1341 */     if (!this.context.getState().equals(LifecycleState.STARTING_PREP))
/*      */     {
/* 1343 */       throw new IllegalStateException(sm.getString("applicationContext.setResponseEncoding.ise", new Object[] {
/* 1344 */         getContextPath() }));
/*      */     }
/*      */     
/* 1347 */     this.context.setResponseCharacterEncoding(encoding);
/*      */   }
/*      */   
/*      */ 
/*      */   protected StandardContext getContext()
/*      */   {
/* 1353 */     return this.context;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void clearAttributes()
/*      */   {
/* 1362 */     ArrayList<String> list = new ArrayList();
/* 1363 */     Iterator<String> iter = this.attributes.keySet().iterator();
/* 1364 */     while (iter.hasNext()) {
/* 1365 */       list.add(iter.next());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1370 */     Iterator<String> keys = list.iterator();
/* 1371 */     while (keys.hasNext()) {
/* 1372 */       String key = (String)keys.next();
/* 1373 */       removeAttribute(key);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected javax.servlet.ServletContext getFacade()
/*      */   {
/* 1384 */     return this.facade;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setAttributeReadOnly(String name)
/*      */   {
/* 1394 */     if (this.attributes.containsKey(name)) {
/* 1395 */       this.readOnlyAttributes.put(name, name);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setNewServletContextListenerAllowed(boolean allowed)
/*      */   {
/* 1401 */     this.newServletContextListenerAllowed = allowed;
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class DispatchData
/*      */   {
/*      */     public MessageBytes uriMB;
/*      */     
/*      */     public MappingData mappingData;
/*      */     
/*      */ 
/*      */     public DispatchData()
/*      */     {
/* 1414 */       this.uriMB = MessageBytes.newInstance();
/* 1415 */       CharChunk uriCC = this.uriMB.getCharChunk();
/* 1416 */       uriCC.setLimit(-1);
/* 1417 */       this.mappingData = new MappingData();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */