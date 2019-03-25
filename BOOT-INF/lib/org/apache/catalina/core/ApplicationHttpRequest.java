/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.RequestDispatcher;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletRequestWrapper;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.catalina.connector.RequestFacade;
/*      */ import org.apache.catalina.servlet4preview.http.HttpServletRequestWrapper;
/*      */ import org.apache.catalina.servlet4preview.http.PushBuilder;
/*      */ import org.apache.catalina.servlet4preview.http.ServletMapping;
/*      */ import org.apache.catalina.util.ParameterMap;
/*      */ import org.apache.tomcat.util.buf.B2CConverter;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.http.Parameters;
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
/*      */ class ApplicationHttpRequest
/*      */   extends HttpServletRequestWrapper
/*      */ {
/*   78 */   protected static final String[] specials = { "javax.servlet.include.request_uri", "javax.servlet.include.context_path", "javax.servlet.include.servlet_path", "javax.servlet.include.path_info", "javax.servlet.include.query_string", "javax.servlet.include.mapping", "javax.servlet.forward.request_uri", "javax.servlet.forward.context_path", "javax.servlet.forward.servlet_path", "javax.servlet.forward.path_info", "javax.servlet.forward.query_string", "javax.servlet.forward.mapping" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int SPECIALS_FIRST_FORWARD_INDEX = 6;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Context context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ApplicationHttpRequest(javax.servlet.http.HttpServletRequest request, Context context, boolean crossContext)
/*      */   {
/*  109 */     super(request);
/*  110 */     this.context = context;
/*  111 */     this.crossContext = crossContext;
/*  112 */     setRequest(request);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  129 */   protected String contextPath = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean crossContext;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */   protected DispatcherType dispatcherType = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  149 */   protected Map<String, String[]> parameters = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  155 */   private boolean parsedParams = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   protected String pathInfo = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  167 */   private String queryParamString = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected String queryString = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected Object requestDispatcherPath = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   protected String requestURI = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */   protected String servletPath = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   private ServletMapping mapping = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  203 */   protected Session session = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  209 */   protected final Object[] specialAttributes = new Object[specials.length];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServletContext getServletContext()
/*      */   {
/*  216 */     if (this.context == null) {
/*  217 */       return null;
/*      */     }
/*  219 */     return this.context.getServletContext();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(String name)
/*      */   {
/*  231 */     if (name.equals("org.apache.catalina.core.DISPATCHER_TYPE"))
/*  232 */       return this.dispatcherType;
/*  233 */     if (name.equals("org.apache.catalina.core.DISPATCHER_REQUEST_PATH")) {
/*  234 */       if (this.requestDispatcherPath != null) {
/*  235 */         return this.requestDispatcherPath.toString();
/*      */       }
/*  237 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  241 */     int pos = getSpecial(name);
/*  242 */     if (pos == -1) {
/*  243 */       return getRequest().getAttribute(name);
/*      */     }
/*  245 */     if ((this.specialAttributes[pos] == null) && (this.specialAttributes[6] == null) && (pos >= 6))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  251 */       return getRequest().getAttribute(name);
/*      */     }
/*  253 */     return this.specialAttributes[pos];
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
/*      */   public Enumeration<String> getAttributeNames()
/*      */   {
/*  266 */     return new AttributeNamesEnumerator();
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
/*      */   public void removeAttribute(String name)
/*      */   {
/*  279 */     if (!removeSpecial(name)) {
/*  280 */       getRequest().removeAttribute(name);
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
/*      */ 
/*      */   public void setAttribute(String name, Object value)
/*      */   {
/*  295 */     if (name.equals("org.apache.catalina.core.DISPATCHER_TYPE")) {
/*  296 */       this.dispatcherType = ((DispatcherType)value);
/*  297 */       return; }
/*  298 */     if (name.equals("org.apache.catalina.core.DISPATCHER_REQUEST_PATH")) {
/*  299 */       this.requestDispatcherPath = value;
/*  300 */       return;
/*      */     }
/*      */     
/*  303 */     if (!setSpecial(name, value)) {
/*  304 */       getRequest().setAttribute(name, value);
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
/*      */ 
/*      */   public RequestDispatcher getRequestDispatcher(String path)
/*      */   {
/*  319 */     if (this.context == null) {
/*  320 */       return null;
/*      */     }
/*      */     
/*  323 */     if (path == null)
/*  324 */       return null;
/*  325 */     if (path.startsWith("/")) {
/*  326 */       return this.context.getServletContext().getRequestDispatcher(path);
/*      */     }
/*      */     
/*      */ 
/*  330 */     String servletPath = (String)getAttribute("javax.servlet.include.servlet_path");
/*  331 */     if (servletPath == null) {
/*  332 */       servletPath = getServletPath();
/*      */     }
/*      */     
/*  335 */     String pathInfo = getPathInfo();
/*  336 */     String requestPath = null;
/*      */     
/*  338 */     if (pathInfo == null) {
/*  339 */       requestPath = servletPath;
/*      */     } else {
/*  341 */       requestPath = servletPath + pathInfo;
/*      */     }
/*      */     
/*  344 */     int pos = requestPath.lastIndexOf('/');
/*  345 */     String relative = null;
/*  346 */     if (pos >= 0) {
/*  347 */       relative = requestPath.substring(0, pos + 1) + path;
/*      */     } else {
/*  349 */       relative = requestPath + path;
/*      */     }
/*      */     
/*  352 */     return this.context.getServletContext().getRequestDispatcher(relative);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispatcherType getDispatcherType()
/*      */   {
/*  363 */     return this.dispatcherType;
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
/*      */   public String getContextPath()
/*      */   {
/*  377 */     return this.contextPath;
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
/*      */   public String getParameter(String name)
/*      */   {
/*  390 */     parseParameters();
/*      */     
/*  392 */     String[] value = (String[])this.parameters.get(name);
/*  393 */     if (value == null) {
/*  394 */       return null;
/*      */     }
/*  396 */     return value[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Map<String, String[]> getParameterMap()
/*      */   {
/*  408 */     parseParameters();
/*  409 */     return this.parameters;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<String> getParameterNames()
/*      */   {
/*  421 */     parseParameters();
/*  422 */     return Collections.enumeration(this.parameters.keySet());
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
/*      */   public String[] getParameterValues(String name)
/*      */   {
/*  435 */     parseParameters();
/*  436 */     return (String[])this.parameters.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPathInfo()
/*      */   {
/*  447 */     return this.pathInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPathTranslated()
/*      */   {
/*  458 */     if ((getPathInfo() == null) || (getServletContext() == null)) {
/*  459 */       return null;
/*      */     }
/*      */     
/*  462 */     return getServletContext().getRealPath(getPathInfo());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getQueryString()
/*      */   {
/*  473 */     return this.queryString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getRequestURI()
/*      */   {
/*  485 */     return this.requestURI;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer getRequestURL()
/*      */   {
/*  497 */     StringBuffer url = new StringBuffer();
/*  498 */     String scheme = getScheme();
/*  499 */     int port = getServerPort();
/*  500 */     if (port < 0) {
/*  501 */       port = 80;
/*      */     }
/*  503 */     url.append(scheme);
/*  504 */     url.append("://");
/*  505 */     url.append(getServerName());
/*  506 */     if (((scheme.equals("http")) && (port != 80)) || (
/*  507 */       (scheme.equals("https")) && (port != 443))) {
/*  508 */       url.append(':');
/*  509 */       url.append(port);
/*      */     }
/*  511 */     url.append(getRequestURI());
/*      */     
/*  513 */     return url;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getServletPath()
/*      */   {
/*  525 */     return this.servletPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ServletMapping getServletMapping()
/*      */   {
/*  532 */     return this.mapping;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpSession getSession()
/*      */   {
/*  542 */     return getSession(true);
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
/*      */   public HttpSession getSession(boolean create)
/*      */   {
/*  555 */     if (this.crossContext)
/*      */     {
/*      */ 
/*  558 */       if (this.context == null) {
/*  559 */         return null;
/*      */       }
/*      */       
/*  562 */       if ((this.session != null) && (this.session.isValid())) {
/*  563 */         return this.session.getSession();
/*      */       }
/*      */       
/*  566 */       HttpSession other = super.getSession(false);
/*  567 */       if ((create) && (other == null))
/*      */       {
/*      */ 
/*      */ 
/*  571 */         other = super.getSession(true);
/*      */       }
/*  573 */       if (other != null) {
/*  574 */         Session localSession = null;
/*      */         try
/*      */         {
/*  577 */           localSession = this.context.getManager().findSession(other.getId());
/*  578 */           if ((localSession != null) && (!localSession.isValid())) {
/*  579 */             localSession = null;
/*      */           }
/*      */         }
/*      */         catch (IOException localIOException) {}
/*      */         
/*  584 */         if ((localSession == null) && (create))
/*      */         {
/*  586 */           localSession = this.context.getManager().createSession(other.getId());
/*      */         }
/*  588 */         if (localSession != null) {
/*  589 */           localSession.access();
/*  590 */           this.session = localSession;
/*  591 */           return this.session.getSession();
/*      */         }
/*      */       }
/*  594 */       return null;
/*      */     }
/*      */     
/*  597 */     return super.getSession(create);
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
/*      */ 
/*      */   public boolean isRequestedSessionIdValid()
/*      */   {
/*  613 */     if (this.crossContext)
/*      */     {
/*  615 */       String requestedSessionId = getRequestedSessionId();
/*  616 */       if (requestedSessionId == null)
/*  617 */         return false;
/*  618 */       if (this.context == null)
/*  619 */         return false;
/*  620 */       Manager manager = this.context.getManager();
/*  621 */       if (manager == null)
/*  622 */         return false;
/*  623 */       Session session = null;
/*      */       try {
/*  625 */         session = manager.findSession(requestedSessionId);
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */       
/*  629 */       if ((session != null) && (session.isValid())) {
/*  630 */         return true;
/*      */       }
/*  632 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  636 */     return super.isRequestedSessionIdValid();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public PushBuilder newPushBuilder()
/*      */   {
/*  643 */     ServletRequest current = getRequest();
/*  644 */     while ((current instanceof ServletRequestWrapper)) {
/*  645 */       current = ((ServletRequestWrapper)current).getRequest();
/*      */     }
/*  647 */     if ((current instanceof RequestFacade)) {
/*  648 */       return ((RequestFacade)current).newPushBuilder(this);
/*      */     }
/*  650 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void recycle()
/*      */   {
/*  661 */     if (this.session != null) {
/*  662 */       this.session.endAccess();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setContextPath(String contextPath)
/*      */   {
/*  674 */     this.contextPath = contextPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setPathInfo(String pathInfo)
/*      */   {
/*  686 */     this.pathInfo = pathInfo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setQueryString(String queryString)
/*      */   {
/*  698 */     this.queryString = queryString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setRequest(javax.servlet.http.HttpServletRequest request)
/*      */   {
/*  710 */     super.setRequest(request);
/*      */     
/*      */ 
/*  713 */     this.dispatcherType = ((DispatcherType)request.getAttribute("org.apache.catalina.core.DISPATCHER_TYPE"));
/*  714 */     this.requestDispatcherPath = request.getAttribute("org.apache.catalina.core.DISPATCHER_REQUEST_PATH");
/*      */     
/*      */ 
/*  717 */     this.contextPath = request.getContextPath();
/*  718 */     this.pathInfo = request.getPathInfo();
/*  719 */     this.queryString = request.getQueryString();
/*  720 */     this.requestURI = request.getRequestURI();
/*  721 */     this.servletPath = request.getServletPath();
/*  722 */     if ((request instanceof org.apache.catalina.servlet4preview.http.HttpServletRequest)) {
/*  723 */       this.mapping = ((org.apache.catalina.servlet4preview.http.HttpServletRequest)request).getServletMapping();
/*      */     } else {
/*  725 */       this.mapping = new ApplicationMapping(null).getServletMapping();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setRequestURI(String requestURI)
/*      */   {
/*  737 */     this.requestURI = requestURI;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setServletPath(String servletPath)
/*      */   {
/*  749 */     this.servletPath = servletPath;
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
/*      */   void parseParameters()
/*      */   {
/*  762 */     if (this.parsedParams) {
/*  763 */       return;
/*      */     }
/*      */     
/*  766 */     this.parameters = new ParameterMap();
/*  767 */     this.parameters.putAll(getRequest().getParameterMap());
/*  768 */     mergeParameters();
/*  769 */     ((ParameterMap)this.parameters).setLocked(true);
/*  770 */     this.parsedParams = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setQueryParams(String queryString)
/*      */   {
/*  781 */     this.queryParamString = queryString;
/*      */   }
/*      */   
/*      */   void setMapping(ServletMapping mapping)
/*      */   {
/*  786 */     this.mapping = mapping;
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
/*      */   protected boolean isSpecial(String name)
/*      */   {
/*  800 */     for (int i = 0; i < specials.length; i++) {
/*  801 */       if (specials[i].equals(name))
/*  802 */         return true;
/*      */     }
/*  804 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getSpecial(String name)
/*      */   {
/*  816 */     for (int i = 0; i < specials.length; i++) {
/*  817 */       if (specials[i].equals(name)) {
/*  818 */         return i;
/*      */       }
/*      */     }
/*  821 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean setSpecial(String name, Object value)
/*      */   {
/*  831 */     for (int i = 0; i < specials.length; i++) {
/*  832 */       if (specials[i].equals(name)) {
/*  833 */         this.specialAttributes[i] = value;
/*  834 */         return true;
/*      */       }
/*      */     }
/*  837 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean removeSpecial(String name)
/*      */   {
/*  847 */     for (int i = 0; i < specials.length; i++) {
/*  848 */       if (specials[i].equals(name)) {
/*  849 */         this.specialAttributes[i] = null;
/*  850 */         return true;
/*      */       }
/*      */     }
/*  853 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String[] mergeValues(String[] values1, String[] values2)
/*      */   {
/*  865 */     ArrayList<Object> results = new ArrayList();
/*      */     
/*  867 */     if (values1 != null)
/*      */     {
/*      */ 
/*  870 */       for (String value : values1) {
/*  871 */         results.add(value);
/*      */       }
/*      */     }
/*      */     
/*  875 */     if (values2 != null)
/*      */     {
/*      */ 
/*  878 */       for (String value : values2) {
/*  879 */         results.add(value);
/*      */       }
/*      */     }
/*      */     
/*  883 */     String[] values = new String[results.size()];
/*  884 */     return (String[])results.toArray(values);
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
/*      */   private void mergeParameters()
/*      */   {
/*  899 */     if ((this.queryParamString == null) || (this.queryParamString.length() < 1)) {
/*  900 */       return;
/*      */     }
/*      */     
/*  903 */     Parameters paramParser = new Parameters();
/*  904 */     MessageBytes queryMB = MessageBytes.newInstance();
/*  905 */     queryMB.setString(this.queryParamString);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  912 */     String encoding = getCharacterEncoding();
/*  913 */     Charset charset = null;
/*  914 */     if (encoding != null) {
/*      */       try {
/*  916 */         charset = B2CConverter.getCharset(encoding);
/*  917 */         queryMB.setCharset(charset);
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {
/*  920 */         charset = StandardCharsets.ISO_8859_1;
/*      */       }
/*      */     }
/*      */     
/*  924 */     paramParser.setQuery(queryMB);
/*  925 */     paramParser.setQueryStringCharset(charset);
/*  926 */     paramParser.handleQueryParameters();
/*      */     
/*      */ 
/*  929 */     Enumeration<String> dispParamNames = paramParser.getParameterNames();
/*  930 */     while (dispParamNames.hasMoreElements()) {
/*  931 */       String dispParamName = (String)dispParamNames.nextElement();
/*  932 */       String[] dispParamValues = paramParser.getParameterValues(dispParamName);
/*  933 */       String[] originalValues = (String[])this.parameters.get(dispParamName);
/*  934 */       if (originalValues == null) {
/*  935 */         this.parameters.put(dispParamName, dispParamValues);
/*      */       }
/*      */       else {
/*  938 */         this.parameters.put(dispParamName, mergeValues(dispParamValues, originalValues));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class AttributeNamesEnumerator
/*      */     implements Enumeration<String>
/*      */   {
/*  952 */     protected int pos = -1;
/*      */     protected final int last;
/*      */     protected final Enumeration<String> parentEnumeration;
/*  955 */     protected String next = null;
/*      */     
/*      */     public AttributeNamesEnumerator() {
/*  958 */       int last = -1;
/*  959 */       this.parentEnumeration = ApplicationHttpRequest.this.getRequest().getAttributeNames();
/*  960 */       for (int i = ApplicationHttpRequest.this.specialAttributes.length - 1; i >= 0; i--) {
/*  961 */         if (ApplicationHttpRequest.this.getAttribute(ApplicationHttpRequest.specials[i]) != null) {
/*  962 */           last = i;
/*  963 */           break;
/*      */         }
/*      */       }
/*  966 */       this.last = last;
/*      */     }
/*      */     
/*      */     public boolean hasMoreElements()
/*      */     {
/*  971 */       return (this.pos != this.last) || (this.next != null) || 
/*  972 */         ((this.next = findNext()) != null);
/*      */     }
/*      */     
/*      */     public String nextElement()
/*      */     {
/*  977 */       if (this.pos != this.last) {
/*  978 */         for (int i = this.pos + 1; i <= this.last; i++) {
/*  979 */           if (ApplicationHttpRequest.this.getAttribute(ApplicationHttpRequest.specials[i]) != null) {
/*  980 */             this.pos = i;
/*  981 */             return ApplicationHttpRequest.specials[i];
/*      */           }
/*      */         }
/*      */       }
/*  985 */       String result = this.next;
/*  986 */       if (this.next != null) {
/*  987 */         this.next = findNext();
/*      */       } else {
/*  989 */         throw new NoSuchElementException();
/*      */       }
/*  991 */       return result;
/*      */     }
/*      */     
/*      */     protected String findNext() {
/*  995 */       String result = null;
/*  996 */       while ((result == null) && (this.parentEnumeration.hasMoreElements())) {
/*  997 */         String current = (String)this.parentEnumeration.nextElement();
/*  998 */         if (!ApplicationHttpRequest.this.isSpecial(current)) {
/*  999 */           result = current;
/*      */         }
/*      */       }
/* 1002 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationHttpRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */