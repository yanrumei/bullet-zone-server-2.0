/*      */ package org.apache.catalina.servlets;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URLDecoder;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServlet;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.apache.catalina.util.IOTools;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
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
/*      */ public final class CGIServlet
/*      */   extends HttpServlet
/*      */ {
/*  238 */   private static final Log log = LogFactory.getLog(CGIServlet.class);
/*  239 */   private static final StringManager sm = StringManager.getManager(CGIServlet.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  251 */   private String cgiPathPrefix = null;
/*      */   
/*      */ 
/*  254 */   private String cgiExecutable = "perl";
/*      */   
/*      */ 
/*  257 */   private List<String> cgiExecutableArgs = null;
/*      */   
/*      */ 
/*      */ 
/*  261 */   private String parameterEncoding = System.getProperty("file.encoding", "UTF-8");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  267 */   private long stderrTimeout = 2000L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */   private Pattern envHttpHeadersPattern = Pattern.compile("ACCEPT[-0-9A-Z]*|CACHE-CONTROL|COOKIE|HOST|IF-[-0-9A-Z]*|REFERER|USER-AGENT");
/*      */   
/*      */ 
/*      */ 
/*  280 */   private static final Object expandFileLock = new Object();
/*      */   
/*      */ 
/*  283 */   private final Hashtable<String, String> shellEnv = new Hashtable();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  290 */   private boolean enableCmdLineArguments = true;
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
/*      */   public void init(ServletConfig config)
/*      */     throws ServletException
/*      */   {
/*  310 */     super.init(config);
/*      */     
/*      */ 
/*  313 */     this.cgiPathPrefix = getServletConfig().getInitParameter("cgiPathPrefix");
/*      */     
/*  315 */     boolean passShellEnvironment = Boolean.parseBoolean(getServletConfig().getInitParameter("passShellEnvironment"));
/*      */     
/*  317 */     if (passShellEnvironment) {
/*  318 */       this.shellEnv.putAll(System.getenv());
/*      */     }
/*      */     
/*  321 */     Enumeration<String> e = config.getInitParameterNames();
/*  322 */     while (e.hasMoreElements()) {
/*  323 */       String initParamName = (String)e.nextElement();
/*  324 */       if (initParamName.startsWith("environment-variable-")) {
/*  325 */         if (initParamName.length() == 21) {
/*  326 */           throw new ServletException(sm.getString("cgiServlet.emptyEnvVarName"));
/*      */         }
/*  328 */         this.shellEnv.put(initParamName.substring(21), config.getInitParameter(initParamName));
/*      */       }
/*      */     }
/*      */     
/*  332 */     if (getServletConfig().getInitParameter("executable") != null) {
/*  333 */       this.cgiExecutable = getServletConfig().getInitParameter("executable");
/*      */     }
/*      */     
/*  336 */     if (getServletConfig().getInitParameter("executable-arg-1") != null) {
/*  337 */       List<String> args = new ArrayList();
/*  338 */       for (int i = 1;; i++) {
/*  339 */         String arg = getServletConfig().getInitParameter("executable-arg-" + i);
/*      */         
/*  341 */         if (arg == null) {
/*      */           break;
/*      */         }
/*  344 */         args.add(arg);
/*      */       }
/*  346 */       this.cgiExecutableArgs = args;
/*      */     }
/*      */     
/*  349 */     if (getServletConfig().getInitParameter("parameterEncoding") != null) {
/*  350 */       this.parameterEncoding = getServletConfig().getInitParameter("parameterEncoding");
/*      */     }
/*      */     
/*  353 */     if (getServletConfig().getInitParameter("stderrTimeout") != null) {
/*  354 */       this.stderrTimeout = Long.parseLong(getServletConfig().getInitParameter("stderrTimeout"));
/*      */     }
/*      */     
/*      */ 
/*  358 */     if (getServletConfig().getInitParameter("envHttpHeaders") != null)
/*      */     {
/*  360 */       this.envHttpHeadersPattern = Pattern.compile(getServletConfig().getInitParameter("envHttpHeaders"));
/*      */     }
/*      */     
/*  363 */     if (getServletConfig().getInitParameter("enableCmdLineArguments") != null)
/*      */     {
/*  365 */       this.enableCmdLineArguments = Boolean.parseBoolean(config.getInitParameter("enableCmdLineArguments"));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private void printServletEnvironment(HttpServletRequest req)
/*      */     throws IOException
/*      */   {
/*  384 */     log.trace("ServletRequest Properties");
/*  385 */     Enumeration<String> attrs = req.getAttributeNames();
/*  386 */     while (attrs.hasMoreElements()) {
/*  387 */       String attr = (String)attrs.nextElement();
/*  388 */       log.trace("Request Attribute: " + attr + ": [ " + req.getAttribute(attr) + "]");
/*      */     }
/*  390 */     log.trace("Character Encoding: [" + req.getCharacterEncoding() + "]");
/*  391 */     log.trace("Content Length: [" + req.getContentLengthLong() + "]");
/*  392 */     log.trace("Content Type: [" + req.getContentType() + "]");
/*  393 */     Enumeration<Locale> locales = req.getLocales();
/*  394 */     while (locales.hasMoreElements()) {
/*  395 */       Locale locale = (Locale)locales.nextElement();
/*  396 */       log.trace("Locale: [" + locale + "]");
/*      */     }
/*  398 */     Enumeration<String> params = req.getParameterNames();
/*  399 */     while (params.hasMoreElements()) {
/*  400 */       String param = (String)params.nextElement();
/*  401 */       for (String value : req.getParameterValues(param)) {
/*  402 */         log.trace("Request Parameter: " + param + ":  [" + value + "]");
/*      */       }
/*      */     }
/*  405 */     log.trace("Protocol: [" + req.getProtocol() + "]");
/*  406 */     log.trace("Remote Address: [" + req.getRemoteAddr() + "]");
/*  407 */     log.trace("Remote Host: [" + req.getRemoteHost() + "]");
/*  408 */     log.trace("Scheme: [" + req.getScheme() + "]");
/*  409 */     log.trace("Secure: [" + req.isSecure() + "]");
/*  410 */     log.trace("Server Name: [" + req.getServerName() + "]");
/*  411 */     log.trace("Server Port: [" + req.getServerPort() + "]");
/*      */     
/*      */ 
/*  414 */     log.trace("HttpServletRequest Properties");
/*  415 */     log.trace("Auth Type: [" + req.getAuthType() + "]");
/*  416 */     log.trace("Context Path: [" + req.getContextPath() + "]");
/*  417 */     Cookie[] cookies = req.getCookies();
/*  418 */     if (cookies != null) {
/*  419 */       for (Cookie cookie : cookies) {
/*  420 */         log.trace("Cookie: " + cookie.getName() + ": [" + cookie.getValue() + "]");
/*      */       }
/*      */     }
/*  423 */     Object headers = req.getHeaderNames();
/*  424 */     while (((Enumeration)headers).hasMoreElements()) {
/*  425 */       String header = (String)((Enumeration)headers).nextElement();
/*  426 */       log.trace("HTTP Header: " + header + ": [" + req.getHeader(header) + "]");
/*      */     }
/*  428 */     log.trace("Method: [" + req.getMethod() + "]");
/*  429 */     log.trace("Path Info: [" + req.getPathInfo() + "]");
/*  430 */     log.trace("Path Translated: [" + req.getPathTranslated() + "]");
/*  431 */     log.trace("Query String: [" + req.getQueryString() + "]");
/*  432 */     log.trace("Remote User: [" + req.getRemoteUser() + "]");
/*  433 */     log.trace("Requested Session ID: [" + req.getRequestedSessionId() + "]");
/*  434 */     log.trace("Requested Session ID From Cookie: [" + req
/*  435 */       .isRequestedSessionIdFromCookie() + "]");
/*  436 */     log.trace("Requested Session ID From URL: [" + req.isRequestedSessionIdFromURL() + "]");
/*  437 */     log.trace("Requested Session ID Valid: [" + req.isRequestedSessionIdValid() + "]");
/*  438 */     log.trace("Request URI: [" + req.getRequestURI() + "]");
/*  439 */     log.trace("Servlet Path: [" + req.getServletPath() + "]");
/*  440 */     log.trace("User Principal: [" + req.getUserPrincipal() + "]");
/*      */     
/*      */ 
/*  443 */     HttpSession session = req.getSession(false);
/*  444 */     if (session != null)
/*      */     {
/*      */ 
/*  447 */       log.trace("HttpSession Properties");
/*  448 */       log.trace("ID: [" + session.getId() + "]");
/*  449 */       log.trace("Creation Time: [" + new Date(session.getCreationTime()) + "]");
/*  450 */       log.trace("Last Accessed Time: [" + new Date(session.getLastAccessedTime()) + "]");
/*  451 */       log.trace("Max Inactive Interval: [" + session.getMaxInactiveInterval() + "]");
/*      */       
/*      */ 
/*  454 */       attrs = session.getAttributeNames();
/*  455 */       while (attrs.hasMoreElements()) {
/*  456 */         String attr = (String)attrs.nextElement();
/*  457 */         log.trace("Session Attribute: " + attr + ": [" + session.getAttribute(attr) + "]");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  462 */     log.trace("ServletConfig Properties");
/*  463 */     log.trace("Servlet Name: [" + getServletConfig().getServletName() + "]");
/*      */     
/*      */ 
/*  466 */     params = getServletConfig().getInitParameterNames();
/*  467 */     while (params.hasMoreElements()) {
/*  468 */       String param = (String)params.nextElement();
/*  469 */       String value = getServletConfig().getInitParameter(param);
/*  470 */       log.trace("Servlet Init Param: " + param + ": [" + value + "]");
/*      */     }
/*      */     
/*      */ 
/*  474 */     log.trace("ServletContext Properties");
/*  475 */     log.trace("Major Version: [" + getServletContext().getMajorVersion() + "]");
/*  476 */     log.trace("Minor Version: [" + getServletContext().getMinorVersion() + "]");
/*  477 */     log.trace("Real Path for '/': [" + getServletContext().getRealPath("/") + "]");
/*  478 */     log.trace("Server Info: [" + getServletContext().getServerInfo() + "]");
/*      */     
/*      */ 
/*  481 */     log.trace("ServletContext Initialization Parameters");
/*  482 */     params = getServletContext().getInitParameterNames();
/*  483 */     while (params.hasMoreElements()) {
/*  484 */       String param = (String)params.nextElement();
/*  485 */       String value = getServletContext().getInitParameter(param);
/*  486 */       log.trace("Servlet Context Init Param: " + param + ": [" + value + "]");
/*      */     }
/*      */     
/*      */ 
/*  490 */     log.trace("ServletContext Attributes");
/*  491 */     attrs = getServletContext().getAttributeNames();
/*  492 */     while (attrs.hasMoreElements()) {
/*  493 */       String attr = (String)attrs.nextElement();
/*  494 */       log.trace("Servlet Context Attribute: " + attr + ": [" + 
/*  495 */         getServletContext().getAttribute(attr) + "]");
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
/*      */ 
/*      */ 
/*      */   protected void doPost(HttpServletRequest req, HttpServletResponse res)
/*      */     throws IOException, ServletException
/*      */   {
/*  513 */     doGet(req, res);
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
/*      */   protected void doGet(HttpServletRequest req, HttpServletResponse res)
/*      */     throws ServletException, IOException
/*      */   {
/*  530 */     CGIEnvironment cgiEnv = new CGIEnvironment(req, getServletContext());
/*      */     
/*  532 */     if (cgiEnv.isValid())
/*      */     {
/*      */ 
/*      */ 
/*  536 */       CGIRunner cgi = new CGIRunner(cgiEnv.getCommand(), cgiEnv.getEnvironment(), cgiEnv.getWorkingDirectory(), cgiEnv.getParameters());
/*      */       
/*  538 */       if ("POST".equals(req.getMethod())) {
/*  539 */         cgi.setInput(req.getInputStream());
/*      */       }
/*  541 */       cgi.setResponse(res);
/*  542 */       cgi.run();
/*      */     } else {
/*  544 */       res.sendError(404);
/*      */     }
/*      */     
/*  547 */     if (log.isTraceEnabled()) {
/*  548 */       String[] cgiEnvLines = cgiEnv.toString().split(System.lineSeparator());
/*  549 */       for (String cgiEnvLine : cgiEnvLines) {
/*  550 */         log.trace(cgiEnvLine);
/*      */       }
/*      */       
/*  553 */       printServletEnvironment(req);
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
/*      */   private boolean setStatus(HttpServletResponse response, int status)
/*      */     throws IOException
/*      */   {
/*  567 */     if (status >= 400) {
/*  568 */       response.sendError(status);
/*  569 */       return true;
/*      */     }
/*  571 */     response.setStatus(status);
/*  572 */     return false;
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
/*      */   protected class CGIEnvironment
/*      */   {
/*  585 */     private ServletContext context = null;
/*      */     
/*      */ 
/*  588 */     private String contextPath = null;
/*      */     
/*      */ 
/*  591 */     private String servletPath = null;
/*      */     
/*      */ 
/*  594 */     private String pathInfo = null;
/*      */     
/*      */ 
/*  597 */     private String webAppRootDir = null;
/*      */     
/*      */ 
/*  600 */     private File tmpDir = null;
/*      */     
/*      */ 
/*  603 */     private Hashtable<String, String> env = null;
/*      */     
/*      */ 
/*  606 */     private String command = null;
/*      */     
/*      */ 
/*      */     private final File workingDirectory;
/*      */     
/*      */ 
/*  612 */     private final ArrayList<String> cmdLineParameters = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean valid;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected CGIEnvironment(HttpServletRequest req, ServletContext context)
/*      */       throws IOException
/*      */     {
/*  630 */       setupFromContext(context);
/*  631 */       setupFromRequest(req);
/*      */       
/*  633 */       this.valid = setCGIEnvironment(req);
/*      */       
/*  635 */       if (this.valid) {
/*  636 */         this.workingDirectory = new File(this.command.substring(0, this.command
/*  637 */           .lastIndexOf(File.separator)));
/*      */       } else {
/*  639 */         this.workingDirectory = null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setupFromContext(ServletContext context)
/*      */     {
/*  651 */       this.context = context;
/*  652 */       this.webAppRootDir = context.getRealPath("/");
/*  653 */       this.tmpDir = ((File)context.getAttribute("javax.servlet.context.tempdir"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setupFromRequest(HttpServletRequest req)
/*      */       throws UnsupportedEncodingException
/*      */     {
/*  667 */       boolean isIncluded = false;
/*      */       
/*      */ 
/*  670 */       if (req.getAttribute("javax.servlet.include.request_uri") != null)
/*      */       {
/*  672 */         isIncluded = true;
/*      */       }
/*  674 */       if (isIncluded) {
/*  675 */         this.contextPath = ((String)req.getAttribute("javax.servlet.include.context_path"));
/*      */         
/*  677 */         this.servletPath = ((String)req.getAttribute("javax.servlet.include.servlet_path"));
/*      */         
/*  679 */         this.pathInfo = ((String)req.getAttribute("javax.servlet.include.path_info"));
/*      */       }
/*      */       else {
/*  682 */         this.contextPath = req.getContextPath();
/*  683 */         this.servletPath = req.getServletPath();
/*  684 */         this.pathInfo = req.getPathInfo();
/*      */       }
/*      */       
/*      */ 
/*  688 */       if (this.pathInfo == null) {
/*  689 */         this.pathInfo = this.servletPath;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  696 */       if ((CGIServlet.this.enableCmdLineArguments) && ((req.getMethod().equals("GET")) || 
/*  697 */         (req.getMethod().equals("POST")) || (req.getMethod().equals("HEAD")))) { String qs;
/*      */         String qs;
/*  699 */         if (isIncluded) {
/*  700 */           qs = (String)req.getAttribute("javax.servlet.include.query_string");
/*      */         }
/*      */         else {
/*  703 */           qs = req.getQueryString();
/*      */         }
/*  705 */         if ((qs != null) && (qs.indexOf('=') == -1)) {
/*  706 */           StringTokenizer qsTokens = new StringTokenizer(qs, "+");
/*  707 */           while (qsTokens.hasMoreTokens()) {
/*  708 */             this.cmdLineParameters.add(URLDecoder.decode(qsTokens.nextToken(), 
/*  709 */               CGIServlet.this.parameterEncoding));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
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
/*      */     protected String[] findCGI(String pathInfo, String webAppRootDir, String contextPath, String servletPath, String cgiPathPrefix)
/*      */     {
/*  782 */       String path = null;
/*  783 */       String name = null;
/*  784 */       String scriptname = null;
/*      */       
/*  786 */       if ((webAppRootDir != null) && 
/*  787 */         (webAppRootDir.lastIndexOf(File.separator) == webAppRootDir.length() - 1))
/*      */       {
/*  789 */         webAppRootDir = webAppRootDir.substring(0, webAppRootDir.length() - 1);
/*      */       }
/*      */       
/*  792 */       if (cgiPathPrefix != null) {
/*  793 */         webAppRootDir = webAppRootDir + File.separator + cgiPathPrefix;
/*      */       }
/*      */       
/*  796 */       if (CGIServlet.log.isDebugEnabled()) {
/*  797 */         CGIServlet.log.debug(CGIServlet.sm.getString("cgiServlet.find.path", new Object[] { pathInfo, webAppRootDir }));
/*      */       }
/*      */       
/*  800 */       File currentLocation = new File(webAppRootDir);
/*  801 */       StringTokenizer dirWalker = new StringTokenizer(pathInfo, "/");
/*  802 */       if (CGIServlet.log.isDebugEnabled()) {
/*  803 */         CGIServlet.log.debug(CGIServlet.sm.getString("cgiServlet.find.location", new Object[] {currentLocation
/*  804 */           .getAbsolutePath() }));
/*      */       }
/*  806 */       StringBuilder cginameBuilder = new StringBuilder();
/*  807 */       while ((!currentLocation.isFile()) && (dirWalker.hasMoreElements())) {
/*  808 */         String nextElement = (String)dirWalker.nextElement();
/*  809 */         currentLocation = new File(currentLocation, nextElement);
/*  810 */         cginameBuilder.append('/').append(nextElement);
/*  811 */         if (CGIServlet.log.isDebugEnabled()) {
/*  812 */           CGIServlet.log.debug(CGIServlet.sm.getString("cgiServlet.find.location", new Object[] {currentLocation
/*  813 */             .getAbsolutePath() }));
/*      */         }
/*      */       }
/*  816 */       String cginame = cginameBuilder.toString();
/*  817 */       if (!currentLocation.isFile()) {
/*  818 */         return new String[] { null, null, null, null };
/*      */       }
/*      */       
/*  821 */       path = currentLocation.getAbsolutePath();
/*  822 */       name = currentLocation.getName();
/*      */       
/*  824 */       if (servletPath.startsWith(cginame)) {
/*  825 */         scriptname = contextPath + cginame;
/*      */       } else {
/*  827 */         scriptname = contextPath + servletPath + cginame;
/*      */       }
/*      */       
/*  830 */       if (CGIServlet.log.isDebugEnabled()) {
/*  831 */         CGIServlet.log.debug(CGIServlet.sm.getString("cgiServlet.find.found", new Object[] { name, path, scriptname, cginame }));
/*      */       }
/*  833 */       return new String[] { path, scriptname, cginame, name };
/*      */     }
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
/*      */     protected boolean setCGIEnvironment(HttpServletRequest req)
/*      */       throws IOException
/*      */     {
/*  855 */       Hashtable<String, String> envp = new Hashtable();
/*      */       
/*      */ 
/*  858 */       envp.putAll(CGIServlet.this.shellEnv);
/*      */       
/*      */ 
/*  861 */       String sPathInfoOrig = null;
/*  862 */       String sPathInfoCGI = null;
/*  863 */       String sPathTranslatedCGI = null;
/*  864 */       String sCGIFullPath = null;
/*  865 */       String sCGIScriptName = null;
/*  866 */       String sCGIFullName = null;
/*  867 */       String sCGIName = null;
/*      */       
/*      */ 
/*      */ 
/*  871 */       sPathInfoOrig = this.pathInfo;
/*  872 */       sPathInfoOrig = sPathInfoOrig == null ? "" : sPathInfoOrig;
/*      */       
/*  874 */       if (this.webAppRootDir == null)
/*      */       {
/*  876 */         this.webAppRootDir = this.tmpDir.toString();
/*  877 */         expandCGIScript();
/*      */       }
/*      */       
/*  880 */       String[] sCGINames = findCGI(sPathInfoOrig, this.webAppRootDir, this.contextPath, this.servletPath, 
/*      */       
/*      */ 
/*      */ 
/*  884 */         CGIServlet.this.cgiPathPrefix);
/*      */       
/*  886 */       sCGIFullPath = sCGINames[0];
/*  887 */       sCGIScriptName = sCGINames[1];
/*  888 */       sCGIFullName = sCGINames[2];
/*  889 */       sCGIName = sCGINames[3];
/*      */       
/*  891 */       if ((sCGIFullPath == null) || (sCGIScriptName == null) || (sCGIFullName == null) || (sCGIName == null))
/*      */       {
/*      */ 
/*      */ 
/*  895 */         return false;
/*      */       }
/*      */       
/*  898 */       envp.put("SERVER_SOFTWARE", "TOMCAT");
/*      */       
/*  900 */       envp.put("SERVER_NAME", nullsToBlanks(req.getServerName()));
/*      */       
/*  902 */       envp.put("GATEWAY_INTERFACE", "CGI/1.1");
/*      */       
/*  904 */       envp.put("SERVER_PROTOCOL", nullsToBlanks(req.getProtocol()));
/*      */       
/*  906 */       int port = req.getServerPort();
/*      */       
/*  908 */       Integer iPort = port == 0 ? Integer.valueOf(-1) : Integer.valueOf(port);
/*  909 */       envp.put("SERVER_PORT", iPort.toString());
/*      */       
/*  911 */       envp.put("REQUEST_METHOD", nullsToBlanks(req.getMethod()));
/*      */       
/*  913 */       envp.put("REQUEST_URI", nullsToBlanks(req.getRequestURI()));
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
/*  927 */       if ((this.pathInfo == null) || 
/*  928 */         (this.pathInfo.substring(sCGIFullName.length()).length() <= 0)) {
/*  929 */         sPathInfoCGI = "";
/*      */       } else {
/*  931 */         sPathInfoCGI = this.pathInfo.substring(sCGIFullName.length());
/*      */       }
/*  933 */       envp.put("PATH_INFO", sPathInfoCGI);
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
/*  955 */       if (!"".equals(sPathInfoCGI)) {
/*  956 */         sPathTranslatedCGI = this.context.getRealPath(sPathInfoCGI);
/*      */       }
/*  958 */       if ((sPathTranslatedCGI != null) && (!"".equals(sPathTranslatedCGI)))
/*      */       {
/*      */ 
/*  961 */         envp.put("PATH_TRANSLATED", nullsToBlanks(sPathTranslatedCGI));
/*      */       }
/*      */       
/*      */ 
/*  965 */       envp.put("SCRIPT_NAME", nullsToBlanks(sCGIScriptName));
/*      */       
/*  967 */       envp.put("QUERY_STRING", nullsToBlanks(req.getQueryString()));
/*      */       
/*  969 */       envp.put("REMOTE_HOST", nullsToBlanks(req.getRemoteHost()));
/*      */       
/*  971 */       envp.put("REMOTE_ADDR", nullsToBlanks(req.getRemoteAddr()));
/*      */       
/*  973 */       envp.put("AUTH_TYPE", nullsToBlanks(req.getAuthType()));
/*      */       
/*  975 */       envp.put("REMOTE_USER", nullsToBlanks(req.getRemoteUser()));
/*      */       
/*  977 */       envp.put("REMOTE_IDENT", "");
/*      */       
/*  979 */       envp.put("CONTENT_TYPE", nullsToBlanks(req.getContentType()));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  986 */       long contentLength = req.getContentLengthLong();
/*      */       
/*  988 */       String sContentLength = contentLength <= 0L ? "" : Long.toString(contentLength);
/*  989 */       envp.put("CONTENT_LENGTH", sContentLength);
/*      */       
/*      */ 
/*  992 */       Enumeration<String> headers = req.getHeaderNames();
/*  993 */       String header = null;
/*  994 */       while (headers.hasMoreElements()) {
/*  995 */         header = null;
/*  996 */         header = ((String)headers.nextElement()).toUpperCase(Locale.ENGLISH);
/*      */         
/*      */ 
/*      */ 
/* 1000 */         if (CGIServlet.this.envHttpHeadersPattern.matcher(header).matches()) {
/* 1001 */           envp.put("HTTP_" + header.replace('-', '_'), req.getHeader(header));
/*      */         }
/*      */       }
/*      */       
/* 1005 */       File fCGIFullPath = new File(sCGIFullPath);
/* 1006 */       this.command = fCGIFullPath.getCanonicalPath();
/*      */       
/* 1008 */       envp.put("X_TOMCAT_SCRIPT_PATH", this.command);
/*      */       
/* 1010 */       envp.put("SCRIPT_FILENAME", this.command);
/*      */       
/* 1012 */       this.env = envp;
/*      */       
/* 1014 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void expandCGIScript()
/*      */     {
/* 1023 */       StringBuilder srcPath = new StringBuilder();
/* 1024 */       StringBuilder destPath = new StringBuilder();
/* 1025 */       InputStream is = null;
/*      */       
/*      */ 
/* 1028 */       if (CGIServlet.this.cgiPathPrefix == null) {
/* 1029 */         srcPath.append(this.pathInfo);
/* 1030 */         is = this.context.getResourceAsStream(srcPath.toString());
/* 1031 */         destPath.append(this.tmpDir);
/* 1032 */         destPath.append(this.pathInfo);
/*      */       }
/*      */       else {
/* 1035 */         srcPath.append(CGIServlet.this.cgiPathPrefix);
/* 1036 */         StringTokenizer pathWalker = new StringTokenizer(this.pathInfo, "/");
/*      */         
/*      */ 
/* 1039 */         while ((pathWalker.hasMoreElements()) && (is == null)) {
/* 1040 */           srcPath.append("/");
/* 1041 */           srcPath.append(pathWalker.nextElement());
/* 1042 */           is = this.context.getResourceAsStream(srcPath.toString());
/*      */         }
/* 1044 */         destPath.append(this.tmpDir);
/* 1045 */         destPath.append("/");
/* 1046 */         destPath.append(srcPath);
/*      */       }
/*      */       
/* 1049 */       if (is == null)
/*      */       {
/* 1051 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.expandNotFound", new Object[] { srcPath }));
/* 1052 */         return;
/*      */       }
/*      */       
/* 1055 */       File f = new File(destPath.toString());
/* 1056 */       if (f.exists()) {
/*      */         try {
/* 1058 */           is.close();
/*      */         } catch (IOException e) {
/* 1060 */           CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.expandCloseFail", new Object[] { srcPath }), e);
/*      */         }
/*      */         
/* 1063 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1067 */       File dir = f.getParentFile();
/* 1068 */       if ((!dir.mkdirs()) && (!dir.isDirectory())) {
/* 1069 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.expandCreateDirFail", new Object[] { dir.getAbsolutePath() }));
/* 1070 */         return;
/*      */       }
/*      */       try
/*      */       {
/* 1074 */         synchronized (CGIServlet.expandFileLock)
/*      */         {
/* 1076 */           if (f.exists()) {
/* 1077 */             return;
/*      */           }
/*      */           
/*      */ 
/* 1081 */           if (!f.createNewFile()) {
/* 1082 */             return;
/*      */           }
/*      */           try
/*      */           {
/* 1086 */             Files.copy(is, f.toPath(), new CopyOption[0]);
/*      */           } finally {
/* 1088 */             is.close();
/*      */           }
/*      */           
/* 1091 */           if (CGIServlet.log.isDebugEnabled()) {
/* 1092 */             CGIServlet.log.debug(CGIServlet.sm.getString("cgiServlet.expandOk", new Object[] { srcPath, destPath }));
/*      */           }
/*      */         }
/*      */       } catch (IOException ioe) {
/* 1096 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.expandFail", new Object[] { srcPath, destPath }), ioe);
/*      */         
/* 1098 */         if ((f.exists()) && 
/* 1099 */           (!f.delete())) {
/* 1100 */           CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.expandDeleteFail", new Object[] { f.getAbsolutePath() }));
/*      */         }
/*      */       }
/*      */     }
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
/*      */     public String toString()
/*      */     {
/* 1116 */       StringBuilder sb = new StringBuilder();
/*      */       
/* 1118 */       sb.append("CGIEnvironment Info:");
/* 1119 */       sb.append(System.lineSeparator());
/*      */       
/* 1121 */       if (isValid()) {
/* 1122 */         sb.append("Validity: [true]");
/* 1123 */         sb.append(System.lineSeparator());
/*      */         
/* 1125 */         sb.append("Environment values:");
/* 1126 */         sb.append(System.lineSeparator());
/* 1127 */         for (Map.Entry<String, String> entry : this.env.entrySet()) {
/* 1128 */           sb.append("  ");
/* 1129 */           sb.append((String)entry.getKey());
/* 1130 */           sb.append(": [");
/* 1131 */           sb.append(blanksToString((String)entry.getValue(), "will be set to blank"));
/* 1132 */           sb.append("]");
/* 1133 */           sb.append(System.lineSeparator());
/*      */         }
/*      */         
/* 1136 */         sb.append("Derived Command :[");
/* 1137 */         sb.append(nullsToBlanks(this.command));
/* 1138 */         sb.append("]");
/* 1139 */         sb.append(System.lineSeparator());
/*      */         
/*      */ 
/* 1142 */         sb.append("Working Directory: [");
/* 1143 */         if (this.workingDirectory != null) {
/* 1144 */           sb.append(this.workingDirectory.toString());
/*      */         }
/* 1146 */         sb.append("]");
/* 1147 */         sb.append(System.lineSeparator());
/*      */         
/* 1149 */         sb.append("Command Line Params:");
/* 1150 */         sb.append(System.lineSeparator());
/* 1151 */         for (String param : this.cmdLineParameters) {
/* 1152 */           sb.append("  [");
/* 1153 */           sb.append(param);
/* 1154 */           sb.append("]");
/* 1155 */           sb.append(System.lineSeparator());
/*      */         }
/*      */       } else {
/* 1158 */         sb.append("Validity: [false]");
/* 1159 */         sb.append(System.lineSeparator());
/* 1160 */         sb.append("CGI script not found or not specified.");
/* 1161 */         sb.append(System.lineSeparator());
/* 1162 */         sb.append("Check the HttpServletRequest pathInfo property to see if it is what ");
/* 1163 */         sb.append(System.lineSeparator());
/* 1164 */         sb.append("you meant it to be. You must specify an existant and executable file ");
/* 1165 */         sb.append(System.lineSeparator());
/* 1166 */         sb.append("as part of the path-info.");
/* 1167 */         sb.append(System.lineSeparator());
/*      */       }
/*      */       
/* 1170 */       return sb.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String getCommand()
/*      */     {
/* 1181 */       return this.command;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected File getWorkingDirectory()
/*      */     {
/* 1192 */       return this.workingDirectory;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Hashtable<String, String> getEnvironment()
/*      */     {
/* 1203 */       return this.env;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected ArrayList<String> getParameters()
/*      */     {
/* 1214 */       return this.cmdLineParameters;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean isValid()
/*      */     {
/* 1226 */       return this.valid;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String nullsToBlanks(String s)
/*      */     {
/* 1238 */       return nullsToString(s, "");
/*      */     }
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
/*      */     protected String nullsToString(String couldBeNull, String subForNulls)
/*      */     {
/* 1252 */       return couldBeNull == null ? subForNulls : couldBeNull;
/*      */     }
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
/*      */     protected String blanksToString(String couldBeBlank, String subForBlanks)
/*      */     {
/* 1266 */       return ("".equals(couldBeBlank)) || (couldBeBlank == null) ? subForBlanks : couldBeBlank;
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
/*      */   protected class CGIRunner
/*      */   {
/*      */     private final String command;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final Hashtable<String, String> env;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final File wd;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final ArrayList<String> params;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1312 */     private InputStream stdin = null;
/*      */     
/*      */ 
/* 1315 */     private HttpServletResponse response = null;
/*      */     
/*      */ 
/* 1318 */     private boolean readyToRun = false;
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
/*      */     protected CGIRunner(Hashtable<String, String> command, File env, ArrayList<String> wd)
/*      */     {
/* 1337 */       this.command = command;
/* 1338 */       this.env = env;
/* 1339 */       this.wd = wd;
/* 1340 */       this.params = params;
/* 1341 */       updateReadyStatus();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void updateReadyStatus()
/*      */     {
/* 1349 */       if ((this.command != null) && (this.env != null) && (this.wd != null) && (this.params != null) && (this.response != null))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1354 */         this.readyToRun = true;
/*      */       } else {
/* 1356 */         this.readyToRun = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean isReady()
/*      */     {
/* 1368 */       return this.readyToRun;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setResponse(HttpServletResponse response)
/*      */     {
/* 1380 */       this.response = response;
/* 1381 */       updateReadyStatus();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setInput(InputStream stdin)
/*      */     {
/* 1392 */       this.stdin = stdin;
/* 1393 */       updateReadyStatus();
/*      */     }
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
/*      */     protected String[] hashToStringArray(Hashtable<String, ?> h)
/*      */       throws NullPointerException
/*      */     {
/* 1411 */       Vector<String> v = new Vector();
/* 1412 */       Enumeration<String> e = h.keys();
/* 1413 */       while (e.hasMoreElements()) {
/* 1414 */         String k = (String)e.nextElement();
/* 1415 */         v.add(k + "=" + h.get(k).toString());
/*      */       }
/* 1417 */       String[] strArr = new String[v.size()];
/* 1418 */       v.copyInto(strArr);
/* 1419 */       return strArr;
/*      */     }
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
/*      */     protected void run()
/*      */       throws IOException
/*      */     {
/* 1481 */       if (!isReady()) {
/* 1482 */         throw new IOException(getClass().getName() + ": not ready to run.");
/*      */       }
/*      */       
/* 1485 */       if (CGIServlet.log.isDebugEnabled()) {
/* 1486 */         CGIServlet.log.debug("envp: [" + this.env + "], command: [" + this.command + "]");
/*      */       }
/*      */       
/* 1489 */       if ((this.command.indexOf(File.separator + "." + File.separator) >= 0) || 
/* 1490 */         (this.command.indexOf(File.separator + "..") >= 0) || 
/* 1491 */         (this.command.indexOf(".." + File.separator) >= 0)) {
/* 1492 */         throw new IOException(getClass().getName() + "Illegal Character in CGI command path ('.' or '..') detected.  Not running CGI [" + this.command + "].");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1502 */       Runtime rt = null;
/* 1503 */       BufferedReader cgiHeaderReader = null;
/* 1504 */       InputStream cgiOutput = null;
/* 1505 */       BufferedReader commandsStdErr = null;
/* 1506 */       Thread errReaderThread = null;
/* 1507 */       BufferedOutputStream commandsStdIn = null;
/* 1508 */       Process proc = null;
/* 1509 */       int bufRead = -1;
/*      */       
/* 1511 */       List<String> cmdAndArgs = new ArrayList();
/* 1512 */       if (CGIServlet.this.cgiExecutable.length() != 0) {
/* 1513 */         cmdAndArgs.add(CGIServlet.this.cgiExecutable);
/*      */       }
/* 1515 */       if (CGIServlet.this.cgiExecutableArgs != null) {
/* 1516 */         cmdAndArgs.addAll(CGIServlet.this.cgiExecutableArgs);
/*      */       }
/* 1518 */       cmdAndArgs.add(this.command);
/* 1519 */       cmdAndArgs.addAll(this.params);
/*      */       try
/*      */       {
/* 1522 */         rt = Runtime.getRuntime();
/* 1523 */         proc = rt.exec(
/* 1524 */           (String[])cmdAndArgs.toArray(new String[cmdAndArgs.size()]), 
/* 1525 */           hashToStringArray(this.env), this.wd);
/*      */         
/* 1527 */         String sContentLength = (String)this.env.get("CONTENT_LENGTH");
/*      */         
/* 1529 */         if (!"".equals(sContentLength)) {
/* 1530 */           commandsStdIn = new BufferedOutputStream(proc.getOutputStream());
/* 1531 */           IOTools.flow(this.stdin, commandsStdIn);
/* 1532 */           commandsStdIn.flush();
/* 1533 */           commandsStdIn.close();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1541 */         boolean isRunning = true;
/*      */         
/* 1543 */         commandsStdErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
/* 1544 */         final BufferedReader stdErrRdr = commandsStdErr;
/*      */         
/* 1546 */         errReaderThread = new Thread()
/*      */         {
/*      */           public void run() {
/* 1549 */             CGIServlet.CGIRunner.this.sendToLog(stdErrRdr);
/*      */           }
/* 1551 */         };
/* 1552 */         errReaderThread.start();
/*      */         
/*      */ 
/* 1555 */         InputStream cgiHeaderStream = new CGIServlet.HTTPHeaderInputStream(proc.getInputStream());
/* 1556 */         cgiHeaderReader = new BufferedReader(new InputStreamReader(cgiHeaderStream));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1563 */         boolean skipBody = false;
/*      */         
/* 1565 */         while (isRunning) {
/*      */           try
/*      */           {
/* 1568 */             String line = null;
/* 1569 */             while (((line = cgiHeaderReader.readLine()) != null) && (!"".equals(line))) {
/* 1570 */               if (CGIServlet.log.isTraceEnabled()) {
/* 1571 */                 CGIServlet.log.trace("addHeader(\"" + line + "\")");
/*      */               }
/* 1573 */               if (line.startsWith("HTTP")) {
/* 1574 */                 skipBody = CGIServlet.this.setStatus(this.response, getSCFromHttpStatusLine(line));
/* 1575 */               } else if (line.indexOf(':') >= 0)
/*      */               {
/* 1577 */                 String header = line.substring(0, line.indexOf(':')).trim();
/*      */                 
/* 1579 */                 String value = line.substring(line.indexOf(':') + 1).trim();
/* 1580 */                 if (header.equalsIgnoreCase("status")) {
/* 1581 */                   skipBody = CGIServlet.this.setStatus(this.response, getSCFromCGIStatusHeader(value));
/*      */                 } else {
/* 1583 */                   this.response.addHeader(header, value);
/*      */                 }
/*      */               } else {
/* 1586 */                 CGIServlet.log.info(CGIServlet.sm.getString("cgiServlet.runBadHeader", new Object[] { line }));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1591 */             byte[] bBuf = new byte['ࠀ'];
/*      */             
/* 1593 */             OutputStream out = this.response.getOutputStream();
/* 1594 */             cgiOutput = proc.getInputStream();
/*      */             try
/*      */             {
/* 1597 */               while ((!skipBody) && ((bufRead = cgiOutput.read(bBuf)) != -1)) {
/* 1598 */                 if (CGIServlet.log.isTraceEnabled()) {
/* 1599 */                   CGIServlet.log.trace("output " + bufRead + " bytes of data");
/*      */                 }
/* 1601 */                 out.write(bBuf, 0, bufRead);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1607 */               while ((bufRead != -1) && 
/* 1608 */                 ((bufRead = cgiOutput.read(bBuf)) != -1)) {}
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1614 */               proc.exitValue();
/*      */             }
/*      */             finally
/*      */             {
/* 1607 */               if (bufRead != -1) {
/* 1608 */                 while ((bufRead = cgiOutput.read(bBuf)) != -1) {}
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1616 */             isRunning = false;
/*      */           }
/*      */           catch (IllegalThreadStateException e) {
/*      */             try {
/* 1620 */               Thread.sleep(500L);
/*      */             }
/*      */             catch (InterruptedException localInterruptedException1) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1628 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runFail"), e);
/* 1629 */         throw e;
/*      */       }
/*      */       finally {
/* 1632 */         if (cgiHeaderReader != null) {
/*      */           try {
/* 1634 */             cgiHeaderReader.close();
/*      */           } catch (IOException ioe) {
/* 1636 */             CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runHeaderReaderFail"), ioe);
/*      */           }
/*      */         }
/*      */         
/* 1640 */         if (cgiOutput != null) {
/*      */           try {
/* 1642 */             cgiOutput.close();
/*      */           } catch (IOException ioe) {
/* 1644 */             CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runOutputStreamFail"), ioe);
/*      */           }
/*      */         }
/*      */         
/* 1648 */         if (errReaderThread != null)
/*      */           try {
/* 1650 */             errReaderThread.join(CGIServlet.this.stderrTimeout);
/*      */           } catch (InterruptedException e) {
/* 1652 */             CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runReaderInterrupt"));
/*      */           }
/* 1654 */         if (proc != null) {
/* 1655 */           proc.destroy();
/* 1656 */           proc = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int getSCFromHttpStatusLine(String line)
/*      */     {
/* 1669 */       int statusStart = line.indexOf(' ') + 1;
/*      */       
/* 1671 */       if ((statusStart < 1) || (line.length() < statusStart + 3))
/*      */       {
/* 1673 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runInvalidStatus", new Object[] { line }));
/* 1674 */         return 500;
/*      */       }
/*      */       
/* 1677 */       String status = line.substring(statusStart, statusStart + 3);
/*      */       
/*      */       try
/*      */       {
/* 1681 */         statusCode = Integer.parseInt(status);
/*      */       } catch (NumberFormatException nfe) {
/*      */         int statusCode;
/* 1684 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runInvalidStatus", new Object[] { status }));
/* 1685 */         return 500;
/*      */       }
/*      */       int statusCode;
/* 1688 */       return statusCode;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int getSCFromCGIStatusHeader(String value)
/*      */     {
/* 1700 */       if (value.length() < 3)
/*      */       {
/* 1702 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runInvalidStatus", new Object[] { value }));
/* 1703 */         return 500;
/*      */       }
/*      */       
/* 1706 */       String status = value.substring(0, 3);
/*      */       
/*      */       try
/*      */       {
/* 1710 */         statusCode = Integer.parseInt(status);
/*      */       } catch (NumberFormatException nfe) {
/*      */         int statusCode;
/* 1713 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runInvalidStatus", new Object[] { status }));
/* 1714 */         return 500;
/*      */       }
/*      */       int statusCode;
/* 1717 */       return statusCode;
/*      */     }
/*      */     
/*      */     private void sendToLog(BufferedReader rdr) {
/* 1721 */       String line = null;
/* 1722 */       int lineCount = 0;
/*      */       try {
/* 1724 */         while ((line = rdr.readLine()) != null) {
/* 1725 */           CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runStdErr", new Object[] { line }));
/* 1726 */           lineCount++;
/*      */         }
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1732 */           rdr.close();
/*      */         } catch (IOException e) {
/* 1734 */           CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runStdErrFail"), e);
/*      */         }
/*      */         
/* 1737 */         if (lineCount <= 0) {
/*      */           return;
/*      */         }
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/* 1729 */         CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runStdErrFail"), e);
/*      */       } finally {
/*      */         try {
/* 1732 */           rdr.close();
/*      */         } catch (IOException e) {
/* 1734 */           CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runStdErrFail"), e);
/*      */         }
/*      */       }
/*      */       
/* 1738 */       CGIServlet.log.warn(CGIServlet.sm.getString("cgiServlet.runStdErrCount", new Object[] { Integer.valueOf(lineCount) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static class HTTPHeaderInputStream
/*      */     extends InputStream
/*      */   {
/*      */     private static final int STATE_CHARACTER = 0;
/*      */     
/*      */     private static final int STATE_FIRST_CR = 1;
/*      */     
/*      */     private static final int STATE_FIRST_LF = 2;
/*      */     
/*      */     private static final int STATE_SECOND_CR = 3;
/*      */     private static final int STATE_HEADER_END = 4;
/*      */     private final InputStream input;
/*      */     private int state;
/*      */     
/*      */     HTTPHeaderInputStream(InputStream theInput)
/*      */     {
/* 1759 */       this.input = theInput;
/* 1760 */       this.state = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public int read()
/*      */       throws IOException
/*      */     {
/* 1768 */       if (this.state == 4) {
/* 1769 */         return -1;
/*      */       }
/*      */       
/* 1772 */       int i = this.input.read();
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
/* 1790 */       if (i == 10)
/*      */       {
/* 1792 */         switch (this.state) {
/*      */         case 0: 
/* 1794 */           this.state = 2;
/* 1795 */           break;
/*      */         case 1: 
/* 1797 */           this.state = 2;
/* 1798 */           break;
/*      */         case 2: 
/*      */         case 3: 
/* 1801 */           this.state = 4;
/*      */         
/*      */         }
/*      */         
/* 1805 */       } else if (i == 13)
/*      */       {
/* 1807 */         switch (this.state) {
/*      */         case 0: 
/* 1809 */           this.state = 1;
/* 1810 */           break;
/*      */         case 1: 
/* 1812 */           this.state = 4;
/* 1813 */           break;
/*      */         case 2: 
/* 1815 */           this.state = 3;
/*      */         
/*      */         }
/*      */         
/*      */       } else {
/* 1820 */         this.state = 0;
/*      */       }
/*      */       
/* 1823 */       return i;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlets\CGIServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */