/*      */ package org.apache.catalina.servlets;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Stack;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.connector.RequestFacade;
/*      */ import org.apache.catalina.util.ConcurrentDateFormat;
/*      */ import org.apache.catalina.util.DOMWriter;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.catalina.util.XMLWriter;
/*      */ import org.apache.tomcat.util.buf.UDecoder;
/*      */ import org.apache.tomcat.util.http.FastHttpDateFormat;
/*      */ import org.apache.tomcat.util.http.RequestUtil;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.ConcurrentMessageDigest;
/*      */ import org.apache.tomcat.util.security.MD5Encoder;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WebdavServlet
/*      */   extends DefaultServlet
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*  132 */   private static final URLEncoder URL_ENCODER_XML = (URLEncoder)URLEncoder.DEFAULT.clone();
/*      */   private static final String METHOD_PROPFIND = "PROPFIND";
/*      */   
/*      */   static {
/*  136 */     URL_ENCODER_XML.removeSafeCharacter('&');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String METHOD_PROPPATCH = "PROPPATCH";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String METHOD_MKCOL = "MKCOL";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String METHOD_COPY = "COPY";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String METHOD_MOVE = "MOVE";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String METHOD_LOCK = "LOCK";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String METHOD_UNLOCK = "UNLOCK";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int FIND_BY_PROPERTY = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int FIND_ALL_PROP = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int FIND_PROPERTY_NAMES = 2;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int LOCK_CREATION = 0;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int LOCK_REFRESH = 1;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int DEFAULT_TIMEOUT = 3600;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int MAX_TIMEOUT = 604800;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final String DEFAULT_NAMESPACE = "DAV:";
/*      */   
/*      */ 
/*      */ 
/*  199 */   protected static final ConcurrentDateFormat creationDateFormat = new ConcurrentDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US, 
/*      */   
/*  201 */     TimeZone.getTimeZone("GMT"));
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */   private final Hashtable<String, LockInfo> resourceLocks = new Hashtable();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  223 */   private final Hashtable<String, Vector<String>> lockNullResources = new Hashtable();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */   private final Vector<LockInfo> collectionLocks = new Vector();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   private String secret = "catalina";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  246 */   private int maxDepth = 3;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  253 */   private boolean allowSpecialPaths = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void init()
/*      */     throws ServletException
/*      */   {
/*  266 */     super.init();
/*      */     
/*  268 */     if (getServletConfig().getInitParameter("secret") != null) {
/*  269 */       this.secret = getServletConfig().getInitParameter("secret");
/*      */     }
/*  271 */     if (getServletConfig().getInitParameter("maxDepth") != null) {
/*  272 */       this.maxDepth = Integer.parseInt(
/*  273 */         getServletConfig().getInitParameter("maxDepth"));
/*      */     }
/*  275 */     if (getServletConfig().getInitParameter("allowSpecialPaths") != null) {
/*  276 */       this.allowSpecialPaths = Boolean.parseBoolean(
/*  277 */         getServletConfig().getInitParameter("allowSpecialPaths"));
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
/*      */   protected DocumentBuilder getDocumentBuilder()
/*      */     throws ServletException
/*      */   {
/*  292 */     DocumentBuilder documentBuilder = null;
/*  293 */     DocumentBuilderFactory documentBuilderFactory = null;
/*      */     try {
/*  295 */       documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  296 */       documentBuilderFactory.setNamespaceAware(true);
/*  297 */       documentBuilderFactory.setExpandEntityReferences(false);
/*  298 */       documentBuilder = documentBuilderFactory.newDocumentBuilder();
/*  299 */       documentBuilder.setEntityResolver(new WebdavResolver(
/*  300 */         getServletContext()));
/*      */     }
/*      */     catch (ParserConfigurationException e) {
/*  303 */       throw new ServletException(sm.getString("webdavservlet.jaxpfailed"));
/*      */     }
/*  305 */     return documentBuilder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void service(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  316 */     String path = getRelativePath(req);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  321 */     if (req.getDispatcherType() == DispatcherType.ERROR) {
/*  322 */       doGet(req, resp);
/*  323 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  331 */     if (isSpecialPath(path)) {
/*  332 */       resp.sendError(404);
/*  333 */       return;
/*      */     }
/*      */     
/*  336 */     String method = req.getMethod();
/*      */     
/*  338 */     if (this.debug > 0) {
/*  339 */       log("[" + method + "] " + path);
/*      */     }
/*      */     
/*  342 */     if (method.equals("PROPFIND")) {
/*  343 */       doPropfind(req, resp);
/*  344 */     } else if (method.equals("PROPPATCH")) {
/*  345 */       doProppatch(req, resp);
/*  346 */     } else if (method.equals("MKCOL")) {
/*  347 */       doMkcol(req, resp);
/*  348 */     } else if (method.equals("COPY")) {
/*  349 */       doCopy(req, resp);
/*  350 */     } else if (method.equals("MOVE")) {
/*  351 */       doMove(req, resp);
/*  352 */     } else if (method.equals("LOCK")) {
/*  353 */       doLock(req, resp);
/*  354 */     } else if (method.equals("UNLOCK")) {
/*  355 */       doUnlock(req, resp);
/*      */     }
/*      */     else {
/*  358 */       super.service(req, resp);
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
/*      */   private final boolean isSpecialPath(String path)
/*      */   {
/*  371 */     return (!this.allowSpecialPaths) && (
/*  372 */       (path.toUpperCase(Locale.ENGLISH).startsWith("/WEB-INF")) || 
/*  373 */       (path.toUpperCase(Locale.ENGLISH).startsWith("/META-INF")));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/*  383 */     if (!super.checkIfHeaders(request, response, resource)) {
/*  384 */       return false;
/*      */     }
/*      */     
/*  387 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String rewriteUrl(String path)
/*      */   {
/*  399 */     return URL_ENCODER_XML.encode(path, StandardCharsets.UTF_8);
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
/*      */   protected String getRelativePath(HttpServletRequest request)
/*      */   {
/*  413 */     return getRelativePath(request, false);
/*      */   }
/*      */   
/*      */   protected String getRelativePath(HttpServletRequest request, boolean allowEmptyPath)
/*      */   {
/*      */     String pathInfo;
/*      */     String pathInfo;
/*  420 */     if (request.getAttribute("javax.servlet.include.request_uri") != null)
/*      */     {
/*  422 */       pathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
/*      */     } else {
/*  424 */       pathInfo = request.getPathInfo();
/*      */     }
/*      */     
/*  427 */     StringBuilder result = new StringBuilder();
/*  428 */     if (pathInfo != null) {
/*  429 */       result.append(pathInfo);
/*      */     }
/*  431 */     if (result.length() == 0) {
/*  432 */       result.append('/');
/*      */     }
/*      */     
/*  435 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getPathPrefix(HttpServletRequest request)
/*      */   {
/*  445 */     String contextPath = request.getContextPath();
/*  446 */     if (request.getServletPath() != null) {
/*  447 */       contextPath = contextPath + request.getServletPath();
/*      */     }
/*  449 */     return contextPath;
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
/*      */   protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  465 */     resp.addHeader("DAV", "1,2");
/*      */     
/*  467 */     StringBuilder methodsAllowed = determineMethodsAllowed(req);
/*      */     
/*  469 */     resp.addHeader("Allow", methodsAllowed.toString());
/*  470 */     resp.addHeader("MS-Author-Via", "DAV");
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
/*      */   protected void doPropfind(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  484 */     if (!this.listings)
/*      */     {
/*  486 */       StringBuilder methodsAllowed = determineMethodsAllowed(req);
/*      */       
/*  488 */       resp.addHeader("Allow", methodsAllowed.toString());
/*  489 */       resp.sendError(405);
/*  490 */       return;
/*      */     }
/*      */     
/*  493 */     String path = getRelativePath(req);
/*  494 */     if ((path.length() > 1) && (path.endsWith("/"))) {
/*  495 */       path = path.substring(0, path.length() - 1);
/*      */     }
/*      */     
/*  498 */     Vector<String> properties = null;
/*      */     
/*  500 */     int depth = this.maxDepth;
/*      */     
/*  502 */     int type = 1;
/*      */     
/*  504 */     String depthStr = req.getHeader("Depth");
/*      */     
/*  506 */     if (depthStr == null) {
/*  507 */       depth = this.maxDepth;
/*      */     }
/*  509 */     else if (depthStr.equals("0")) {
/*  510 */       depth = 0;
/*  511 */     } else if (depthStr.equals("1")) {
/*  512 */       depth = 1;
/*  513 */     } else if (depthStr.equals("infinity")) {
/*  514 */       depth = this.maxDepth;
/*      */     }
/*      */     
/*      */ 
/*  518 */     Node propNode = null;
/*      */     
/*  520 */     if (req.getContentLengthLong() > 0L) {
/*  521 */       DocumentBuilder documentBuilder = getDocumentBuilder();
/*      */       
/*      */       try
/*      */       {
/*  525 */         Document document = documentBuilder.parse(new InputSource(req.getInputStream()));
/*      */         
/*      */ 
/*  528 */         Element rootElement = document.getDocumentElement();
/*  529 */         NodeList childList = rootElement.getChildNodes();
/*      */         
/*  531 */         for (int i = 0; i < childList.getLength(); i++) {
/*  532 */           Node currentNode = childList.item(i);
/*  533 */           switch (currentNode.getNodeType()) {
/*      */           case 3: 
/*      */             break;
/*      */           case 1: 
/*  537 */             if (currentNode.getNodeName().endsWith("prop")) {
/*  538 */               type = 0;
/*  539 */               propNode = currentNode;
/*      */             }
/*  541 */             if (currentNode.getNodeName().endsWith("propname")) {
/*  542 */               type = 2;
/*      */             }
/*  544 */             if (currentNode.getNodeName().endsWith("allprop")) {
/*  545 */               type = 1;
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (SAXException e) {
/*  552 */         resp.sendError(400);
/*  553 */         return;
/*      */       }
/*      */       catch (IOException e) {
/*  556 */         resp.sendError(400);
/*  557 */         return;
/*      */       }
/*      */     }
/*      */     
/*  561 */     if (type == 0) {
/*  562 */       properties = new Vector();
/*      */       
/*      */ 
/*  565 */       NodeList childList = propNode.getChildNodes();
/*      */       
/*  567 */       for (int i = 0; i < childList.getLength(); i++) {
/*  568 */         Node currentNode = childList.item(i);
/*  569 */         switch (currentNode.getNodeType()) {
/*      */         case 3: 
/*      */           break;
/*      */         case 1: 
/*  573 */           String nodeName = currentNode.getNodeName();
/*  574 */           String propertyName = null;
/*  575 */           if (nodeName.indexOf(':') != -1)
/*      */           {
/*  577 */             propertyName = nodeName.substring(nodeName.indexOf(':') + 1);
/*      */           } else {
/*  579 */             propertyName = nodeName;
/*      */           }
/*      */           
/*  582 */           properties.addElement(propertyName);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  589 */     WebResource resource = this.resources.getResource(path);
/*      */     XMLWriter generatedXML;
/*  591 */     if (!resource.exists()) {
/*  592 */       int slash = path.lastIndexOf('/');
/*  593 */       if (slash != -1) {
/*  594 */         String parentPath = path.substring(0, slash);
/*      */         
/*  596 */         Vector<String> currentLockNullResources = (Vector)this.lockNullResources.get(parentPath);
/*  597 */         if (currentLockNullResources != null)
/*      */         {
/*  599 */           Enumeration<String> lockNullResourcesList = currentLockNullResources.elements();
/*  600 */           while (lockNullResourcesList.hasMoreElements())
/*      */           {
/*  602 */             String lockNullPath = (String)lockNullResourcesList.nextElement();
/*  603 */             if (lockNullPath.equals(path)) {
/*  604 */               resp.setStatus(207);
/*  605 */               resp.setContentType("text/xml; charset=UTF-8");
/*      */               
/*      */ 
/*  608 */               generatedXML = new XMLWriter(resp.getWriter());
/*  609 */               generatedXML.writeXMLHeader();
/*  610 */               generatedXML.writeElement("D", "DAV:", "multistatus", 0);
/*      */               
/*      */ 
/*  613 */               parseLockNullProperties(req, generatedXML, lockNullPath, type, properties);
/*      */               
/*  615 */               generatedXML.writeElement("D", "multistatus", 1);
/*      */               
/*  617 */               generatedXML.sendData();
/*  618 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  625 */     if (!resource.exists()) {
/*  626 */       resp.sendError(404, path);
/*  627 */       return;
/*      */     }
/*      */     
/*  630 */     resp.setStatus(207);
/*      */     
/*  632 */     resp.setContentType("text/xml; charset=UTF-8");
/*      */     
/*      */ 
/*  635 */     XMLWriter generatedXML = new XMLWriter(resp.getWriter());
/*  636 */     generatedXML.writeXMLHeader();
/*      */     
/*  638 */     generatedXML.writeElement("D", "DAV:", "multistatus", 0);
/*      */     
/*      */ 
/*  641 */     if (depth == 0) {
/*  642 */       parseProperties(req, generatedXML, path, type, properties);
/*      */     }
/*      */     else
/*      */     {
/*  646 */       Stack<String> stack = new Stack();
/*  647 */       stack.push(path);
/*      */       
/*      */ 
/*  650 */       Stack<String> stackBelow = new Stack();
/*      */       
/*  652 */       while ((!stack.isEmpty()) && (depth >= 0))
/*      */       {
/*  654 */         String currentPath = (String)stack.pop();
/*  655 */         parseProperties(req, generatedXML, currentPath, type, properties);
/*      */         
/*      */ 
/*  658 */         resource = this.resources.getResource(currentPath);
/*      */         
/*  660 */         if ((resource.isDirectory()) && (depth > 0))
/*      */         {
/*  662 */           String[] entries = this.resources.list(currentPath);
/*  663 */           for (String entry : entries) {
/*  664 */             String newPath = currentPath;
/*  665 */             if (!newPath.endsWith("/"))
/*  666 */               newPath = newPath + "/";
/*  667 */             newPath = newPath + entry;
/*  668 */             stackBelow.push(newPath);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  673 */           String lockPath = currentPath;
/*  674 */           if (lockPath.endsWith("/"))
/*      */           {
/*  676 */             lockPath = lockPath.substring(0, lockPath.length() - 1);
/*      */           }
/*  678 */           Object currentLockNullResources = (Vector)this.lockNullResources.get(lockPath);
/*  679 */           if (currentLockNullResources != null)
/*      */           {
/*  681 */             Object lockNullResourcesList = ((Vector)currentLockNullResources).elements();
/*  682 */             while (((Enumeration)lockNullResourcesList).hasMoreElements())
/*      */             {
/*  684 */               String lockNullPath = (String)((Enumeration)lockNullResourcesList).nextElement();
/*      */               
/*  686 */               parseLockNullProperties(req, generatedXML, lockNullPath, type, properties);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  693 */         if (stack.isEmpty()) {
/*  694 */           depth--;
/*  695 */           stack = stackBelow;
/*  696 */           stackBelow = new Stack();
/*      */         }
/*      */         
/*  699 */         generatedXML.sendData();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  704 */     generatedXML.writeElement("D", "multistatus", 1);
/*      */     
/*  706 */     generatedXML.sendData();
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
/*      */   protected void doProppatch(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/*  720 */     if (this.readOnly) {
/*  721 */       resp.sendError(403);
/*  722 */       return;
/*      */     }
/*      */     
/*  725 */     if (isLocked(req)) {
/*  726 */       resp.sendError(423);
/*  727 */       return;
/*      */     }
/*      */     
/*  730 */     resp.sendError(501);
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
/*      */   protected void doMkcol(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  745 */     if (this.readOnly) {
/*  746 */       resp.sendError(403);
/*  747 */       return;
/*      */     }
/*      */     
/*  750 */     if (isLocked(req)) {
/*  751 */       resp.sendError(423);
/*  752 */       return;
/*      */     }
/*      */     
/*  755 */     String path = getRelativePath(req);
/*      */     
/*  757 */     WebResource resource = this.resources.getResource(path);
/*      */     
/*      */ 
/*      */ 
/*  761 */     if (resource.exists())
/*      */     {
/*  763 */       StringBuilder methodsAllowed = determineMethodsAllowed(req);
/*      */       
/*  765 */       resp.addHeader("Allow", methodsAllowed.toString());
/*      */       
/*  767 */       resp.sendError(405);
/*  768 */       return;
/*      */     }
/*      */     
/*  771 */     if (req.getContentLengthLong() > 0L) {
/*  772 */       DocumentBuilder documentBuilder = getDocumentBuilder();
/*      */       try
/*      */       {
/*  775 */         documentBuilder.parse(new InputSource(req.getInputStream()));
/*      */         
/*  777 */         resp.sendError(501);
/*  778 */         return;
/*      */       }
/*      */       catch (SAXException saxe)
/*      */       {
/*  782 */         resp.sendError(415);
/*  783 */         return;
/*      */       }
/*      */     }
/*      */     
/*  787 */     if (this.resources.mkdir(path)) {
/*  788 */       resp.setStatus(201);
/*      */       
/*  790 */       this.lockNullResources.remove(path);
/*      */     } else {
/*  792 */       resp.sendError(409, 
/*      */       
/*  794 */         WebdavStatus.getStatusText(409));
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
/*      */   protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  810 */     if (this.readOnly) {
/*  811 */       resp.sendError(403);
/*  812 */       return;
/*      */     }
/*      */     
/*  815 */     if (isLocked(req)) {
/*  816 */       resp.sendError(423);
/*  817 */       return;
/*      */     }
/*      */     
/*  820 */     deleteResource(req, resp);
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
/*      */   protected void doPut(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  838 */     if (isLocked(req)) {
/*  839 */       resp.sendError(423);
/*  840 */       return;
/*      */     }
/*      */     
/*  843 */     super.doPut(req, resp);
/*      */     
/*  845 */     String path = getRelativePath(req);
/*      */     
/*      */ 
/*  848 */     this.lockNullResources.remove(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doCopy(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/*  861 */     if (this.readOnly) {
/*  862 */       resp.sendError(403);
/*  863 */       return;
/*      */     }
/*      */     
/*  866 */     copyResource(req, resp);
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
/*      */   protected void doMove(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/*  880 */     if (this.readOnly) {
/*  881 */       resp.sendError(403);
/*  882 */       return;
/*      */     }
/*      */     
/*  885 */     if (isLocked(req)) {
/*  886 */       resp.sendError(423);
/*  887 */       return;
/*      */     }
/*      */     
/*  890 */     String path = getRelativePath(req);
/*      */     
/*  892 */     if (copyResource(req, resp)) {
/*  893 */       deleteResource(path, req, resp, false);
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
/*      */   protected void doLock(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  909 */     if (this.readOnly) {
/*  910 */       resp.sendError(403);
/*  911 */       return;
/*      */     }
/*      */     
/*  914 */     if (isLocked(req)) {
/*  915 */       resp.sendError(423);
/*  916 */       return;
/*      */     }
/*      */     
/*  919 */     LockInfo lock = new LockInfo(null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  925 */     String depthStr = req.getHeader("Depth");
/*      */     
/*  927 */     if (depthStr == null) {
/*  928 */       lock.depth = this.maxDepth;
/*      */     }
/*  930 */     else if (depthStr.equals("0")) {
/*  931 */       lock.depth = 0;
/*      */     } else {
/*  933 */       lock.depth = this.maxDepth;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  939 */     int lockDuration = 3600;
/*  940 */     String lockDurationStr = req.getHeader("Timeout");
/*  941 */     if (lockDurationStr == null) {
/*  942 */       lockDuration = 3600;
/*      */     } else {
/*  944 */       int commaPos = lockDurationStr.indexOf(',');
/*      */       
/*  946 */       if (commaPos != -1) {
/*  947 */         lockDurationStr = lockDurationStr.substring(0, commaPos);
/*      */       }
/*  949 */       if (lockDurationStr.startsWith("Second-")) {
/*  950 */         lockDuration = Integer.parseInt(lockDurationStr.substring(7));
/*      */       }
/*  952 */       else if (lockDurationStr.equalsIgnoreCase("infinity")) {
/*  953 */         lockDuration = 604800;
/*      */       } else {
/*      */         try {
/*  956 */           lockDuration = Integer.parseInt(lockDurationStr);
/*      */         } catch (NumberFormatException e) {
/*  958 */           lockDuration = 604800;
/*      */         }
/*      */       }
/*      */       
/*  962 */       if (lockDuration == 0) {
/*  963 */         lockDuration = 3600;
/*      */       }
/*  965 */       if (lockDuration > 604800) {
/*  966 */         lockDuration = 604800;
/*      */       }
/*      */     }
/*  969 */     lock.expiresAt = (System.currentTimeMillis() + lockDuration * 1000);
/*      */     
/*  971 */     int lockRequestType = 0;
/*      */     
/*  973 */     Node lockInfoNode = null;
/*      */     
/*  975 */     DocumentBuilder documentBuilder = getDocumentBuilder();
/*      */     try
/*      */     {
/*  978 */       Document document = documentBuilder.parse(new InputSource(req
/*  979 */         .getInputStream()));
/*      */       
/*      */ 
/*  982 */       Element rootElement = document.getDocumentElement();
/*  983 */       lockInfoNode = rootElement;
/*      */     } catch (IOException e) {
/*  985 */       lockRequestType = 1;
/*      */     } catch (SAXException e) {
/*  987 */       lockRequestType = 1;
/*      */     }
/*      */     
/*  990 */     if (lockInfoNode != null)
/*      */     {
/*      */ 
/*      */ 
/*  994 */       NodeList childList = lockInfoNode.getChildNodes();
/*  995 */       StringWriter strWriter = null;
/*  996 */       DOMWriter domWriter = null;
/*      */       
/*  998 */       Node lockScopeNode = null;
/*  999 */       Node lockTypeNode = null;
/* 1000 */       Node lockOwnerNode = null;
/*      */       
/* 1002 */       for (int i = 0; i < childList.getLength(); i++) {
/* 1003 */         Node currentNode = childList.item(i);
/* 1004 */         switch (currentNode.getNodeType()) {
/*      */         case 3: 
/*      */           break;
/*      */         case 1: 
/* 1008 */           String nodeName = currentNode.getNodeName();
/* 1009 */           if (nodeName.endsWith("lockscope")) {
/* 1010 */             lockScopeNode = currentNode;
/*      */           }
/* 1012 */           if (nodeName.endsWith("locktype")) {
/* 1013 */             lockTypeNode = currentNode;
/*      */           }
/* 1015 */           if (nodeName.endsWith("owner")) {
/* 1016 */             lockOwnerNode = currentNode;
/*      */           }
/*      */           break;
/*      */         }
/*      */         
/*      */       }
/* 1022 */       if (lockScopeNode != null)
/*      */       {
/* 1024 */         childList = lockScopeNode.getChildNodes();
/* 1025 */         for (int i = 0; i < childList.getLength(); i++) {
/* 1026 */           Node currentNode = childList.item(i);
/* 1027 */           switch (currentNode.getNodeType()) {
/*      */           case 3: 
/*      */             break;
/*      */           case 1: 
/* 1031 */             String tempScope = currentNode.getNodeName();
/* 1032 */             if (tempScope.indexOf(':') != -1)
/*      */             {
/* 1034 */               lock.scope = tempScope.substring(tempScope.indexOf(':') + 1);
/*      */             } else {
/* 1036 */               lock.scope = tempScope;
/*      */             }
/*      */             break;
/*      */           }
/*      */           
/*      */         }
/* 1042 */         if (lock.scope == null)
/*      */         {
/* 1044 */           resp.setStatus(400);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1049 */         resp.setStatus(400);
/*      */       }
/*      */       
/* 1052 */       if (lockTypeNode != null)
/*      */       {
/* 1054 */         childList = lockTypeNode.getChildNodes();
/* 1055 */         for (int i = 0; i < childList.getLength(); i++) {
/* 1056 */           Node currentNode = childList.item(i);
/* 1057 */           switch (currentNode.getNodeType()) {
/*      */           case 3: 
/*      */             break;
/*      */           case 1: 
/* 1061 */             String tempType = currentNode.getNodeName();
/* 1062 */             if (tempType.indexOf(':') != -1)
/*      */             {
/* 1064 */               lock.type = tempType.substring(tempType.indexOf(':') + 1);
/*      */             } else {
/* 1066 */               lock.type = tempType;
/*      */             }
/*      */             break;
/*      */           }
/*      */           
/*      */         }
/* 1072 */         if (lock.type == null)
/*      */         {
/* 1074 */           resp.setStatus(400);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1079 */         resp.setStatus(400);
/*      */       }
/*      */       
/* 1082 */       if (lockOwnerNode != null)
/*      */       {
/* 1084 */         childList = lockOwnerNode.getChildNodes();
/* 1085 */         for (int i = 0; i < childList.getLength(); i++) {
/* 1086 */           Node currentNode = childList.item(i);
/* 1087 */           switch (currentNode.getNodeType()) {
/*      */           case 3: 
/* 1089 */             lock.owner += currentNode.getNodeValue();
/* 1090 */             break;
/*      */           case 1: 
/* 1092 */             strWriter = new StringWriter();
/* 1093 */             domWriter = new DOMWriter(strWriter);
/* 1094 */             domWriter.print(currentNode);
/* 1095 */             lock.owner += strWriter.toString();
/*      */           }
/*      */           
/*      */         }
/*      */         
/* 1100 */         if (lock.owner == null)
/*      */         {
/* 1102 */           resp.setStatus(400);
/*      */         }
/*      */       }
/*      */       else {
/* 1106 */         lock.owner = "";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1111 */     String path = getRelativePath(req);
/*      */     
/* 1113 */     lock.path = path;
/*      */     
/* 1115 */     WebResource resource = this.resources.getResource(path);
/*      */     
/* 1117 */     Enumeration<LockInfo> locksList = null;
/*      */     
/* 1119 */     if (lockRequestType == 0)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1125 */       String lockTokenStr = req.getServletPath() + "-" + lock.type + "-" + lock.scope + "-" + req.getUserPrincipal() + "-" + lock.depth + "-" + lock.owner + "-" + lock.tokens + "-" + lock.expiresAt + "-" + System.currentTimeMillis() + "-" + this.secret;
/*      */       
/* 1127 */       String lockToken = MD5Encoder.encode(ConcurrentMessageDigest.digestMD5(new byte[][] {lockTokenStr
/* 1128 */         .getBytes(StandardCharsets.ISO_8859_1) }));
/*      */       
/* 1130 */       if ((resource.isDirectory()) && (lock.depth == this.maxDepth))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1136 */         Vector<String> lockPaths = new Vector();
/* 1137 */         locksList = this.collectionLocks.elements();
/* 1138 */         while (locksList.hasMoreElements()) {
/* 1139 */           LockInfo currentLock = (LockInfo)locksList.nextElement();
/* 1140 */           if (currentLock.hasExpired()) {
/* 1141 */             this.resourceLocks.remove(currentLock.path);
/*      */ 
/*      */           }
/* 1144 */           else if ((currentLock.path.startsWith(lock.path)) && (
/* 1145 */             (currentLock.isExclusive()) || 
/* 1146 */             (lock.isExclusive())))
/*      */           {
/* 1148 */             lockPaths.addElement(currentLock.path);
/*      */           }
/*      */         }
/* 1151 */         locksList = this.resourceLocks.elements();
/* 1152 */         while (locksList.hasMoreElements()) {
/* 1153 */           LockInfo currentLock = (LockInfo)locksList.nextElement();
/* 1154 */           if (currentLock.hasExpired()) {
/* 1155 */             this.resourceLocks.remove(currentLock.path);
/*      */ 
/*      */           }
/* 1158 */           else if ((currentLock.path.startsWith(lock.path)) && (
/* 1159 */             (currentLock.isExclusive()) || 
/* 1160 */             (lock.isExclusive())))
/*      */           {
/* 1162 */             lockPaths.addElement(currentLock.path);
/*      */           }
/*      */         }
/*      */         
/* 1166 */         if (!lockPaths.isEmpty())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1171 */           Enumeration<String> lockPathsList = lockPaths.elements();
/*      */           
/* 1173 */           resp.setStatus(409);
/*      */           
/* 1175 */           XMLWriter generatedXML = new XMLWriter();
/* 1176 */           generatedXML.writeXMLHeader();
/*      */           
/* 1178 */           generatedXML.writeElement("D", "DAV:", "multistatus", 0);
/*      */           
/*      */ 
/* 1181 */           while (lockPathsList.hasMoreElements()) {
/* 1182 */             generatedXML.writeElement("D", "response", 0);
/*      */             
/* 1184 */             generatedXML.writeElement("D", "href", 0);
/*      */             
/* 1186 */             generatedXML.writeText((String)lockPathsList.nextElement());
/* 1187 */             generatedXML.writeElement("D", "href", 1);
/*      */             
/* 1189 */             generatedXML.writeElement("D", "status", 0);
/*      */             
/* 1191 */             generatedXML
/* 1192 */               .writeText("HTTP/1.1 423 " + 
/*      */               
/* 1194 */               WebdavStatus.getStatusText(423));
/* 1195 */             generatedXML.writeElement("D", "status", 1);
/*      */             
/*      */ 
/* 1198 */             generatedXML.writeElement("D", "response", 1);
/*      */           }
/*      */           
/*      */ 
/* 1202 */           generatedXML.writeElement("D", "multistatus", 1);
/*      */           
/*      */ 
/* 1205 */           Writer writer = resp.getWriter();
/* 1206 */           writer.write(generatedXML.toString());
/* 1207 */           writer.close();
/*      */           
/* 1209 */           return;
/*      */         }
/*      */         
/*      */ 
/* 1213 */         boolean addLock = true;
/*      */         
/*      */ 
/* 1216 */         locksList = this.collectionLocks.elements();
/* 1217 */         while (locksList.hasMoreElements())
/*      */         {
/* 1219 */           LockInfo currentLock = (LockInfo)locksList.nextElement();
/* 1220 */           if (currentLock.path.equals(lock.path))
/*      */           {
/* 1222 */             if (currentLock.isExclusive()) {
/* 1223 */               resp.sendError(423);
/* 1224 */               return;
/*      */             }
/* 1226 */             if (lock.isExclusive()) {
/* 1227 */               resp.sendError(423);
/* 1228 */               return;
/*      */             }
/*      */             
/*      */ 
/* 1232 */             currentLock.tokens.addElement(lockToken);
/* 1233 */             lock = currentLock;
/* 1234 */             addLock = false;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1240 */         if (addLock) {
/* 1241 */           lock.tokens.addElement(lockToken);
/* 1242 */           this.collectionLocks.addElement(lock);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1250 */         LockInfo presentLock = (LockInfo)this.resourceLocks.get(lock.path);
/* 1251 */         if (presentLock != null)
/*      */         {
/* 1253 */           if ((presentLock.isExclusive()) || (lock.isExclusive()))
/*      */           {
/*      */ 
/* 1256 */             resp.sendError(412);
/* 1257 */             return;
/*      */           }
/* 1259 */           presentLock.tokens.addElement(lockToken);
/* 1260 */           lock = presentLock;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1265 */           lock.tokens.addElement(lockToken);
/* 1266 */           this.resourceLocks.put(lock.path, lock);
/*      */           
/*      */ 
/* 1269 */           if (!resource.exists())
/*      */           {
/*      */ 
/* 1272 */             int slash = lock.path.lastIndexOf('/');
/* 1273 */             String parentPath = lock.path.substring(0, slash);
/*      */             
/*      */ 
/* 1276 */             Vector<String> lockNulls = (Vector)this.lockNullResources.get(parentPath);
/* 1277 */             if (lockNulls == null) {
/* 1278 */               lockNulls = new Vector();
/* 1279 */               this.lockNullResources.put(parentPath, lockNulls);
/*      */             }
/*      */             
/* 1282 */             lockNulls.addElement(lock.path);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1287 */           resp.addHeader("Lock-Token", "<opaquelocktoken:" + lockToken + ">");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1295 */     if (lockRequestType == 1)
/*      */     {
/* 1297 */       String ifHeader = req.getHeader("If");
/* 1298 */       if (ifHeader == null) {
/* 1299 */         ifHeader = "";
/*      */       }
/*      */       
/*      */ 
/* 1303 */       LockInfo toRenew = (LockInfo)this.resourceLocks.get(path);
/* 1304 */       Enumeration<String> tokenList = null;
/*      */       
/* 1306 */       if (toRenew != null)
/*      */       {
/* 1308 */         tokenList = toRenew.tokens.elements();
/* 1309 */         while (tokenList.hasMoreElements()) {
/* 1310 */           String token = (String)tokenList.nextElement();
/* 1311 */           if (ifHeader.indexOf(token) != -1) {
/* 1312 */             toRenew.expiresAt = lock.expiresAt;
/* 1313 */             lock = toRenew;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1321 */       Enumeration<LockInfo> collectionLocksList = this.collectionLocks.elements();
/* 1322 */       while (collectionLocksList.hasMoreElements()) {
/* 1323 */         toRenew = (LockInfo)collectionLocksList.nextElement();
/* 1324 */         if (path.equals(toRenew.path))
/*      */         {
/* 1326 */           tokenList = toRenew.tokens.elements();
/* 1327 */           while (tokenList.hasMoreElements()) {
/* 1328 */             String token = (String)tokenList.nextElement();
/* 1329 */             if (ifHeader.indexOf(token) != -1) {
/* 1330 */               toRenew.expiresAt = lock.expiresAt;
/* 1331 */               lock = toRenew;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1342 */     XMLWriter generatedXML = new XMLWriter();
/* 1343 */     generatedXML.writeXMLHeader();
/* 1344 */     generatedXML.writeElement("D", "DAV:", "prop", 0);
/*      */     
/*      */ 
/* 1347 */     generatedXML.writeElement("D", "lockdiscovery", 0);
/*      */     
/* 1349 */     lock.toXML(generatedXML);
/*      */     
/* 1351 */     generatedXML.writeElement("D", "lockdiscovery", 1);
/*      */     
/* 1353 */     generatedXML.writeElement("D", "prop", 1);
/*      */     
/* 1355 */     resp.setStatus(200);
/* 1356 */     resp.setContentType("text/xml; charset=UTF-8");
/* 1357 */     Writer writer = resp.getWriter();
/* 1358 */     writer.write(generatedXML.toString());
/* 1359 */     writer.close();
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
/*      */   protected void doUnlock(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/* 1373 */     if (this.readOnly) {
/* 1374 */       resp.sendError(403);
/* 1375 */       return;
/*      */     }
/*      */     
/* 1378 */     if (isLocked(req)) {
/* 1379 */       resp.sendError(423);
/* 1380 */       return;
/*      */     }
/*      */     
/* 1383 */     String path = getRelativePath(req);
/*      */     
/* 1385 */     String lockTokenHeader = req.getHeader("Lock-Token");
/* 1386 */     if (lockTokenHeader == null) {
/* 1387 */       lockTokenHeader = "";
/*      */     }
/*      */     
/*      */ 
/* 1391 */     LockInfo lock = (LockInfo)this.resourceLocks.get(path);
/* 1392 */     Enumeration<String> tokenList = null;
/* 1393 */     if (lock != null)
/*      */     {
/*      */ 
/*      */ 
/* 1397 */       tokenList = lock.tokens.elements();
/* 1398 */       while (tokenList.hasMoreElements()) {
/* 1399 */         String token = (String)tokenList.nextElement();
/* 1400 */         if (lockTokenHeader.indexOf(token) != -1) {
/* 1401 */           lock.tokens.removeElement(token);
/*      */         }
/*      */       }
/*      */       
/* 1405 */       if (lock.tokens.isEmpty()) {
/* 1406 */         this.resourceLocks.remove(path);
/*      */         
/* 1408 */         this.lockNullResources.remove(path);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1415 */     Enumeration<LockInfo> collectionLocksList = this.collectionLocks.elements();
/* 1416 */     while (collectionLocksList.hasMoreElements()) {
/* 1417 */       lock = (LockInfo)collectionLocksList.nextElement();
/* 1418 */       if (path.equals(lock.path))
/*      */       {
/* 1420 */         tokenList = lock.tokens.elements();
/* 1421 */         while (tokenList.hasMoreElements()) {
/* 1422 */           String token = (String)tokenList.nextElement();
/* 1423 */           if (lockTokenHeader.indexOf(token) != -1) {
/* 1424 */             lock.tokens.removeElement(token);
/* 1425 */             break;
/*      */           }
/*      */         }
/*      */         
/* 1429 */         if (lock.tokens.isEmpty()) {
/* 1430 */           this.collectionLocks.removeElement(lock);
/*      */           
/* 1432 */           this.lockNullResources.remove(path);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1438 */     resp.setStatus(204);
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
/*      */   private boolean isLocked(HttpServletRequest req)
/*      */   {
/* 1456 */     String path = getRelativePath(req);
/*      */     
/* 1458 */     String ifHeader = req.getHeader("If");
/* 1459 */     if (ifHeader == null) {
/* 1460 */       ifHeader = "";
/*      */     }
/* 1462 */     String lockTokenHeader = req.getHeader("Lock-Token");
/* 1463 */     if (lockTokenHeader == null) {
/* 1464 */       lockTokenHeader = "";
/*      */     }
/* 1466 */     return isLocked(path, ifHeader + lockTokenHeader);
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
/*      */   private boolean isLocked(String path, String ifHeader)
/*      */   {
/* 1484 */     LockInfo lock = (LockInfo)this.resourceLocks.get(path);
/* 1485 */     Enumeration<String> tokenList = null;
/* 1486 */     if ((lock != null) && (lock.hasExpired())) {
/* 1487 */       this.resourceLocks.remove(path);
/* 1488 */     } else if (lock != null)
/*      */     {
/*      */ 
/*      */ 
/* 1492 */       tokenList = lock.tokens.elements();
/* 1493 */       boolean tokenMatch = false;
/* 1494 */       while (tokenList.hasMoreElements()) {
/* 1495 */         String token = (String)tokenList.nextElement();
/* 1496 */         if (ifHeader.indexOf(token) != -1) {
/* 1497 */           tokenMatch = true;
/* 1498 */           break;
/*      */         }
/*      */       }
/* 1501 */       if (!tokenMatch) {
/* 1502 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1508 */     Enumeration<LockInfo> collectionLocksList = this.collectionLocks.elements();
/* 1509 */     while (collectionLocksList.hasMoreElements()) {
/* 1510 */       lock = (LockInfo)collectionLocksList.nextElement();
/* 1511 */       if (lock.hasExpired()) {
/* 1512 */         this.collectionLocks.removeElement(lock);
/* 1513 */       } else if (path.startsWith(lock.path))
/*      */       {
/* 1515 */         tokenList = lock.tokens.elements();
/* 1516 */         boolean tokenMatch = false;
/* 1517 */         while (tokenList.hasMoreElements()) {
/* 1518 */           String token = (String)tokenList.nextElement();
/* 1519 */           if (ifHeader.indexOf(token) != -1) {
/* 1520 */             tokenMatch = true;
/* 1521 */             break;
/*      */           }
/*      */         }
/* 1524 */         if (!tokenMatch) {
/* 1525 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1530 */     return false;
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
/*      */   private boolean copyResource(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/* 1549 */     String destinationPath = req.getHeader("Destination");
/*      */     
/* 1551 */     if (destinationPath == null) {
/* 1552 */       resp.sendError(400);
/* 1553 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1557 */     destinationPath = UDecoder.URLDecode(destinationPath, StandardCharsets.UTF_8);
/*      */     
/* 1559 */     int protocolIndex = destinationPath.indexOf("://");
/* 1560 */     if (protocolIndex >= 0)
/*      */     {
/*      */ 
/*      */ 
/* 1564 */       int firstSeparator = destinationPath.indexOf('/', protocolIndex + 4);
/* 1565 */       if (firstSeparator < 0) {
/* 1566 */         destinationPath = "/";
/*      */       } else {
/* 1568 */         destinationPath = destinationPath.substring(firstSeparator);
/*      */       }
/*      */     } else {
/* 1571 */       String hostName = req.getServerName();
/* 1572 */       if ((hostName != null) && (destinationPath.startsWith(hostName))) {
/* 1573 */         destinationPath = destinationPath.substring(hostName.length());
/*      */       }
/*      */       
/* 1576 */       int portIndex = destinationPath.indexOf(':');
/* 1577 */       if (portIndex >= 0) {
/* 1578 */         destinationPath = destinationPath.substring(portIndex);
/*      */       }
/*      */       
/* 1581 */       if (destinationPath.startsWith(":")) {
/* 1582 */         int firstSeparator = destinationPath.indexOf('/');
/* 1583 */         if (firstSeparator < 0) {
/* 1584 */           destinationPath = "/";
/*      */         }
/*      */         else {
/* 1587 */           destinationPath = destinationPath.substring(firstSeparator);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1593 */     destinationPath = RequestUtil.normalize(destinationPath);
/*      */     
/* 1595 */     String contextPath = req.getContextPath();
/* 1596 */     if ((contextPath != null) && 
/* 1597 */       (destinationPath.startsWith(contextPath))) {
/* 1598 */       destinationPath = destinationPath.substring(contextPath.length());
/*      */     }
/*      */     
/* 1601 */     String pathInfo = req.getPathInfo();
/* 1602 */     if (pathInfo != null) {
/* 1603 */       String servletPath = req.getServletPath();
/* 1604 */       if ((servletPath != null) && 
/* 1605 */         (destinationPath.startsWith(servletPath)))
/*      */       {
/* 1607 */         destinationPath = destinationPath.substring(servletPath.length());
/*      */       }
/*      */     }
/*      */     
/* 1611 */     if (this.debug > 0) {
/* 1612 */       log("Dest path :" + destinationPath);
/*      */     }
/*      */     
/* 1615 */     if (isSpecialPath(destinationPath)) {
/* 1616 */       resp.sendError(403);
/* 1617 */       return false;
/*      */     }
/*      */     
/* 1620 */     String path = getRelativePath(req);
/*      */     
/* 1622 */     if (destinationPath.equals(path)) {
/* 1623 */       resp.sendError(403);
/* 1624 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1629 */     boolean overwrite = true;
/* 1630 */     String overwriteHeader = req.getHeader("Overwrite");
/*      */     
/* 1632 */     if (overwriteHeader != null) {
/* 1633 */       if (overwriteHeader.equalsIgnoreCase("T")) {
/* 1634 */         overwrite = true;
/*      */       } else {
/* 1636 */         overwrite = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1642 */     WebResource destination = this.resources.getResource(destinationPath);
/*      */     
/* 1644 */     if (overwrite)
/*      */     {
/* 1646 */       if (destination.exists()) {
/* 1647 */         if (!deleteResource(destinationPath, req, resp, true)) {
/* 1648 */           return false;
/*      */         }
/*      */       } else {
/* 1651 */         resp.setStatus(201);
/*      */       }
/*      */       
/*      */     }
/* 1655 */     else if (destination.exists()) {
/* 1656 */       resp.sendError(412);
/* 1657 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1663 */     Hashtable<String, Integer> errorList = new Hashtable();
/*      */     
/* 1665 */     boolean result = copyResource(errorList, path, destinationPath);
/*      */     
/* 1667 */     if ((!result) || (!errorList.isEmpty())) {
/* 1668 */       if (errorList.size() == 1) {
/* 1669 */         resp.sendError(((Integer)errorList.elements().nextElement()).intValue());
/*      */       } else {
/* 1671 */         sendReport(req, resp, errorList);
/*      */       }
/* 1673 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1677 */     if (destination.exists()) {
/* 1678 */       resp.setStatus(204);
/*      */     } else {
/* 1680 */       resp.setStatus(201);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1685 */     this.lockNullResources.remove(destinationPath);
/*      */     
/* 1687 */     return true;
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
/*      */   private boolean copyResource(Hashtable<String, Integer> errorList, String source, String dest)
/*      */   {
/* 1703 */     if (this.debug > 1) {
/* 1704 */       log("Copy: " + source + " To: " + dest);
/*      */     }
/* 1706 */     WebResource sourceResource = this.resources.getResource(source);
/*      */     
/* 1708 */     if (sourceResource.isDirectory()) {
/* 1709 */       if (!this.resources.mkdir(dest)) {
/* 1710 */         WebResource destResource = this.resources.getResource(dest);
/* 1711 */         if (!destResource.isDirectory()) {
/* 1712 */           errorList.put(dest, Integer.valueOf(409));
/* 1713 */           return false;
/*      */         }
/*      */       }
/*      */       
/* 1717 */       String[] entries = this.resources.list(source);
/* 1718 */       for (String entry : entries) {
/* 1719 */         String childDest = dest;
/* 1720 */         if (!childDest.equals("/")) {
/* 1721 */           childDest = childDest + "/";
/*      */         }
/* 1723 */         childDest = childDest + entry;
/* 1724 */         String childSrc = source;
/* 1725 */         if (!childSrc.equals("/")) {
/* 1726 */           childSrc = childSrc + "/";
/*      */         }
/* 1728 */         childSrc = childSrc + entry;
/* 1729 */         copyResource(errorList, childSrc, childDest);
/*      */       }
/* 1731 */     } else if (sourceResource.isFile()) {
/* 1732 */       WebResource destResource = this.resources.getResource(dest);
/* 1733 */       Object parent; WebResource parentResource; if ((!destResource.exists()) && (!destResource.getWebappPath().endsWith("/"))) {
/* 1734 */         int lastSlash = destResource.getWebappPath().lastIndexOf('/');
/* 1735 */         if (lastSlash > 0) {
/* 1736 */           parent = destResource.getWebappPath().substring(0, lastSlash);
/* 1737 */           parentResource = this.resources.getResource((String)parent);
/* 1738 */           if (!parentResource.isDirectory()) {
/* 1739 */             errorList.put(source, Integer.valueOf(409));
/* 1740 */             return false;
/*      */           }
/*      */         }
/*      */       }
/* 1744 */       try { InputStream is = sourceResource.getInputStream();parent = null;
/* 1745 */         try { if (!this.resources.write(dest, is, false))
/*      */           {
/* 1747 */             errorList.put(source, 
/* 1748 */               Integer.valueOf(500));
/* 1749 */             return false;
/*      */           }
/*      */         }
/*      */         catch (Throwable localThrowable5)
/*      */         {
/* 1744 */           parent = localThrowable5;throw localThrowable5;
/*      */ 
/*      */ 
/*      */         }
/*      */         finally
/*      */         {
/*      */ 
/* 1751 */           if (is != null) if (parent != null) try { is.close(); } catch (Throwable localThrowable3) { ((Throwable)parent).addSuppressed(localThrowable3); } else is.close();
/* 1752 */         } } catch (IOException e) { log(sm.getString("webdavservlet.inputstreamclosefail", new Object[] { source }), e);
/*      */       }
/*      */     } else {
/* 1755 */       errorList.put(source, 
/* 1756 */         Integer.valueOf(500));
/* 1757 */       return false;
/*      */     }
/* 1759 */     return true;
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
/*      */   private boolean deleteResource(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws IOException
/*      */   {
/* 1775 */     String path = getRelativePath(req);
/*      */     
/* 1777 */     return deleteResource(path, req, resp, true);
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
/*      */   private boolean deleteResource(String path, HttpServletRequest req, HttpServletResponse resp, boolean setStatus)
/*      */     throws IOException
/*      */   {
/* 1797 */     String ifHeader = req.getHeader("If");
/* 1798 */     if (ifHeader == null) {
/* 1799 */       ifHeader = "";
/*      */     }
/* 1801 */     String lockTokenHeader = req.getHeader("Lock-Token");
/* 1802 */     if (lockTokenHeader == null) {
/* 1803 */       lockTokenHeader = "";
/*      */     }
/* 1805 */     if (isLocked(path, ifHeader + lockTokenHeader)) {
/* 1806 */       resp.sendError(423);
/* 1807 */       return false;
/*      */     }
/*      */     
/* 1810 */     WebResource resource = this.resources.getResource(path);
/*      */     
/* 1812 */     if (!resource.exists()) {
/* 1813 */       resp.sendError(404);
/* 1814 */       return false;
/*      */     }
/*      */     
/* 1817 */     if (!resource.isDirectory()) {
/* 1818 */       if (!resource.delete()) {
/* 1819 */         resp.sendError(500);
/* 1820 */         return false;
/*      */       }
/*      */     }
/*      */     else {
/* 1824 */       Hashtable<String, Integer> errorList = new Hashtable();
/*      */       
/* 1826 */       deleteCollection(req, path, errorList);
/* 1827 */       if (!resource.delete()) {
/* 1828 */         errorList.put(path, 
/* 1829 */           Integer.valueOf(500));
/*      */       }
/*      */       
/* 1832 */       if (!errorList.isEmpty()) {
/* 1833 */         sendReport(req, resp, errorList);
/* 1834 */         return false;
/*      */       }
/*      */     }
/* 1837 */     if (setStatus) {
/* 1838 */       resp.setStatus(204);
/*      */     }
/* 1840 */     return true;
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
/*      */   private void deleteCollection(HttpServletRequest req, String path, Hashtable<String, Integer> errorList)
/*      */   {
/* 1854 */     if (this.debug > 1) {
/* 1855 */       log("Delete:" + path);
/*      */     }
/*      */     
/* 1858 */     if (isSpecialPath(path)) {
/* 1859 */       errorList.put(path, Integer.valueOf(403));
/* 1860 */       return;
/*      */     }
/*      */     
/* 1863 */     String ifHeader = req.getHeader("If");
/* 1864 */     if (ifHeader == null) {
/* 1865 */       ifHeader = "";
/*      */     }
/* 1867 */     String lockTokenHeader = req.getHeader("Lock-Token");
/* 1868 */     if (lockTokenHeader == null) {
/* 1869 */       lockTokenHeader = "";
/*      */     }
/* 1871 */     String[] entries = this.resources.list(path);
/*      */     
/* 1873 */     for (String entry : entries) {
/* 1874 */       String childName = path;
/* 1875 */       if (!childName.equals("/"))
/* 1876 */         childName = childName + "/";
/* 1877 */       childName = childName + entry;
/*      */       
/* 1879 */       if (isLocked(childName, ifHeader + lockTokenHeader))
/*      */       {
/* 1881 */         errorList.put(childName, Integer.valueOf(423));
/*      */       }
/*      */       else {
/* 1884 */         WebResource childResource = this.resources.getResource(childName);
/* 1885 */         if (childResource.isDirectory()) {
/* 1886 */           deleteCollection(req, childName, errorList);
/*      */         }
/*      */         
/* 1889 */         if ((!childResource.delete()) && 
/* 1890 */           (!childResource.isDirectory()))
/*      */         {
/*      */ 
/* 1893 */           errorList.put(childName, Integer.valueOf(500));
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void sendReport(HttpServletRequest req, HttpServletResponse resp, Hashtable<String, Integer> errorList)
/*      */     throws IOException
/*      */   {
/* 1915 */     resp.setStatus(207);
/*      */     
/* 1917 */     String absoluteUri = req.getRequestURI();
/* 1918 */     String relativePath = getRelativePath(req);
/*      */     
/* 1920 */     XMLWriter generatedXML = new XMLWriter();
/* 1921 */     generatedXML.writeXMLHeader();
/*      */     
/* 1923 */     generatedXML.writeElement("D", "DAV:", "multistatus", 0);
/*      */     
/*      */ 
/* 1926 */     Enumeration<String> pathList = errorList.keys();
/* 1927 */     while (pathList.hasMoreElements())
/*      */     {
/* 1929 */       String errorPath = (String)pathList.nextElement();
/* 1930 */       int errorCode = ((Integer)errorList.get(errorPath)).intValue();
/*      */       
/* 1932 */       generatedXML.writeElement("D", "response", 0);
/*      */       
/* 1934 */       generatedXML.writeElement("D", "href", 0);
/* 1935 */       String toAppend = errorPath.substring(relativePath.length());
/* 1936 */       if (!toAppend.startsWith("/"))
/* 1937 */         toAppend = "/" + toAppend;
/* 1938 */       generatedXML.writeText(absoluteUri + toAppend);
/* 1939 */       generatedXML.writeElement("D", "href", 1);
/* 1940 */       generatedXML.writeElement("D", "status", 0);
/* 1941 */       generatedXML.writeText("HTTP/1.1 " + errorCode + " " + 
/* 1942 */         WebdavStatus.getStatusText(errorCode));
/* 1943 */       generatedXML.writeElement("D", "status", 1);
/*      */       
/* 1945 */       generatedXML.writeElement("D", "response", 1);
/*      */     }
/*      */     
/*      */ 
/* 1949 */     generatedXML.writeElement("D", "multistatus", 1);
/*      */     
/* 1951 */     Writer writer = resp.getWriter();
/* 1952 */     writer.write(generatedXML.toString());
/* 1953 */     writer.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseProperties(HttpServletRequest req, XMLWriter generatedXML, String path, int type, Vector<String> propertiesVector)
/*      */   {
/* 1975 */     if (isSpecialPath(path)) {
/* 1976 */       return;
/*      */     }
/* 1978 */     WebResource resource = this.resources.getResource(path);
/* 1979 */     if (!resource.exists())
/*      */     {
/*      */ 
/* 1982 */       return;
/*      */     }
/*      */     
/* 1985 */     String href = req.getContextPath() + req.getServletPath();
/* 1986 */     if ((href.endsWith("/")) && (path.startsWith("/"))) {
/* 1987 */       href = href + path.substring(1);
/*      */     } else
/* 1989 */       href = href + path;
/* 1990 */     if ((resource.isDirectory()) && (!href.endsWith("/"))) {
/* 1991 */       href = href + "/";
/*      */     }
/* 1993 */     String rewrittenUrl = rewriteUrl(href);
/*      */     
/* 1995 */     generatePropFindResponse(generatedXML, rewrittenUrl, path, type, propertiesVector, resource
/* 1996 */       .isFile(), false, resource.getCreation(), resource.getLastModified(), resource
/* 1997 */       .getContentLength(), getServletContext().getMimeType(resource.getName()), resource
/* 1998 */       .getETag());
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
/*      */ 
/*      */   private void parseLockNullProperties(HttpServletRequest req, XMLWriter generatedXML, String path, int type, Vector<String> propertiesVector)
/*      */   {
/* 2018 */     if (isSpecialPath(path)) {
/* 2019 */       return;
/*      */     }
/*      */     
/* 2022 */     LockInfo lock = (LockInfo)this.resourceLocks.get(path);
/*      */     
/* 2024 */     if (lock == null) {
/* 2025 */       return;
/*      */     }
/* 2027 */     String absoluteUri = req.getRequestURI();
/* 2028 */     String relativePath = getRelativePath(req);
/* 2029 */     String toAppend = path.substring(relativePath.length());
/* 2030 */     if (!toAppend.startsWith("/")) {
/* 2031 */       toAppend = "/" + toAppend;
/*      */     }
/* 2033 */     String rewrittenUrl = rewriteUrl(RequestUtil.normalize(absoluteUri + toAppend));
/*      */     
/*      */ 
/* 2036 */     generatePropFindResponse(generatedXML, rewrittenUrl, path, type, propertiesVector, true, true, lock.creationDate
/* 2037 */       .getTime(), lock.creationDate.getTime(), 0L, "", "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void generatePropFindResponse(XMLWriter generatedXML, String rewrittenUrl, String path, int propFindType, Vector<String> propertiesVector, boolean isFile, boolean isLockNull, long created, long lastModified, long contentLength, String contentType, String eTag)
/*      */   {
/* 2047 */     generatedXML.writeElement("D", "response", 0);
/*      */     
/* 2049 */     String status = "HTTP/1.1 200 " + WebdavStatus.getStatusText(200);
/*      */     
/*      */ 
/* 2052 */     generatedXML.writeElement("D", "href", 0);
/* 2053 */     generatedXML.writeText(rewrittenUrl);
/* 2054 */     generatedXML.writeElement("D", "href", 1);
/*      */     
/* 2056 */     String resourceName = path;
/* 2057 */     int lastSlash = path.lastIndexOf('/');
/* 2058 */     if (lastSlash != -1) {
/* 2059 */       resourceName = resourceName.substring(lastSlash + 1);
/*      */     }
/* 2061 */     switch (propFindType)
/*      */     {
/*      */ 
/*      */     case 1: 
/* 2065 */       generatedXML.writeElement("D", "propstat", 0);
/* 2066 */       generatedXML.writeElement("D", "prop", 0);
/*      */       
/* 2068 */       generatedXML.writeProperty("D", "creationdate", getISOCreationDate(created));
/* 2069 */       generatedXML.writeElement("D", "displayname", 0);
/* 2070 */       generatedXML.writeData(resourceName);
/* 2071 */       generatedXML.writeElement("D", "displayname", 1);
/* 2072 */       if (isFile) {
/* 2073 */         generatedXML.writeProperty("D", "getlastmodified", 
/* 2074 */           FastHttpDateFormat.formatDate(lastModified, null));
/* 2075 */         generatedXML.writeProperty("D", "getcontentlength", Long.toString(contentLength));
/* 2076 */         if (contentType != null) {
/* 2077 */           generatedXML.writeProperty("D", "getcontenttype", contentType);
/*      */         }
/* 2079 */         generatedXML.writeProperty("D", "getetag", eTag);
/* 2080 */         if (isLockNull) {
/* 2081 */           generatedXML.writeElement("D", "resourcetype", 0);
/* 2082 */           generatedXML.writeElement("D", "lock-null", 2);
/* 2083 */           generatedXML.writeElement("D", "resourcetype", 1);
/*      */         } else {
/* 2085 */           generatedXML.writeElement("D", "resourcetype", 2);
/*      */         }
/*      */       } else {
/* 2088 */         generatedXML.writeElement("D", "resourcetype", 0);
/* 2089 */         generatedXML.writeElement("D", "collection", 2);
/* 2090 */         generatedXML.writeElement("D", "resourcetype", 1);
/*      */       }
/*      */       
/* 2093 */       generatedXML.writeProperty("D", "source", "");
/*      */       
/* 2095 */       String supportedLocks = "<D:lockentry><D:lockscope><D:exclusive/></D:lockscope><D:locktype><D:write/></D:locktype></D:lockentry><D:lockentry><D:lockscope><D:shared/></D:lockscope><D:locktype><D:write/></D:locktype></D:lockentry>";
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2102 */       generatedXML.writeElement("D", "supportedlock", 0);
/* 2103 */       generatedXML.writeText(supportedLocks);
/* 2104 */       generatedXML.writeElement("D", "supportedlock", 1);
/*      */       
/* 2106 */       generateLockDiscovery(path, generatedXML);
/*      */       
/* 2108 */       generatedXML.writeElement("D", "prop", 1);
/* 2109 */       generatedXML.writeElement("D", "status", 0);
/* 2110 */       generatedXML.writeText(status);
/* 2111 */       generatedXML.writeElement("D", "status", 1);
/* 2112 */       generatedXML.writeElement("D", "propstat", 1);
/*      */       
/* 2114 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 2118 */       generatedXML.writeElement("D", "propstat", 0);
/* 2119 */       generatedXML.writeElement("D", "prop", 0);
/*      */       
/* 2121 */       generatedXML.writeElement("D", "creationdate", 2);
/* 2122 */       generatedXML.writeElement("D", "displayname", 2);
/* 2123 */       if (isFile) {
/* 2124 */         generatedXML.writeElement("D", "getcontentlanguage", 2);
/* 2125 */         generatedXML.writeElement("D", "getcontentlength", 2);
/* 2126 */         generatedXML.writeElement("D", "getcontenttype", 2);
/* 2127 */         generatedXML.writeElement("D", "getetag", 2);
/* 2128 */         generatedXML.writeElement("D", "getlastmodified", 2);
/*      */       }
/* 2130 */       generatedXML.writeElement("D", "resourcetype", 2);
/* 2131 */       generatedXML.writeElement("D", "source", 2);
/* 2132 */       generatedXML.writeElement("D", "lockdiscovery", 2);
/*      */       
/* 2134 */       generatedXML.writeElement("D", "prop", 1);
/* 2135 */       generatedXML.writeElement("D", "status", 0);
/* 2136 */       generatedXML.writeText(status);
/* 2137 */       generatedXML.writeElement("D", "status", 1);
/* 2138 */       generatedXML.writeElement("D", "propstat", 1);
/*      */       
/* 2140 */       break;
/*      */     
/*      */ 
/*      */     case 0: 
/* 2144 */       Vector<String> propertiesNotFound = new Vector();
/*      */       
/*      */ 
/*      */ 
/* 2148 */       generatedXML.writeElement("D", "propstat", 0);
/* 2149 */       generatedXML.writeElement("D", "prop", 0);
/*      */       
/* 2151 */       Enumeration<String> properties = propertiesVector.elements();
/*      */       
/* 2153 */       while (properties.hasMoreElements())
/*      */       {
/* 2155 */         String property = (String)properties.nextElement();
/*      */         
/* 2157 */         if (property.equals("creationdate")) {
/* 2158 */           generatedXML.writeProperty("D", "creationdate", getISOCreationDate(created));
/* 2159 */         } else if (property.equals("displayname")) {
/* 2160 */           generatedXML.writeElement("D", "displayname", 0);
/* 2161 */           generatedXML.writeData(resourceName);
/* 2162 */           generatedXML.writeElement("D", "displayname", 1);
/* 2163 */         } else if (property.equals("getcontentlanguage")) {
/* 2164 */           if (isFile) {
/* 2165 */             generatedXML.writeElement("D", "getcontentlanguage", 2);
/*      */           }
/*      */           else {
/* 2168 */             propertiesNotFound.addElement(property);
/*      */           }
/* 2170 */         } else if (property.equals("getcontentlength")) {
/* 2171 */           if (isFile) {
/* 2172 */             generatedXML.writeProperty("D", "getcontentlength", 
/* 2173 */               Long.toString(contentLength));
/*      */           } else {
/* 2175 */             propertiesNotFound.addElement(property);
/*      */           }
/* 2177 */         } else if (property.equals("getcontenttype")) {
/* 2178 */           if (isFile) {
/* 2179 */             generatedXML.writeProperty("D", "getcontenttype", contentType);
/*      */           } else {
/* 2181 */             propertiesNotFound.addElement(property);
/*      */           }
/* 2183 */         } else if (property.equals("getetag")) {
/* 2184 */           if (isFile) {
/* 2185 */             generatedXML.writeProperty("D", "getetag", eTag);
/*      */           } else {
/* 2187 */             propertiesNotFound.addElement(property);
/*      */           }
/* 2189 */         } else if (property.equals("getlastmodified")) {
/* 2190 */           if (isFile) {
/* 2191 */             generatedXML.writeProperty("D", "getlastmodified", 
/* 2192 */               FastHttpDateFormat.formatDate(lastModified, null));
/*      */           } else {
/* 2194 */             propertiesNotFound.addElement(property);
/*      */           }
/* 2196 */         } else if (property.equals("resourcetype")) {
/* 2197 */           if (isFile) {
/* 2198 */             if (isLockNull) {
/* 2199 */               generatedXML.writeElement("D", "resourcetype", 0);
/* 2200 */               generatedXML.writeElement("D", "lock-null", 2);
/* 2201 */               generatedXML.writeElement("D", "resourcetype", 1);
/*      */             } else {
/* 2203 */               generatedXML.writeElement("D", "resourcetype", 2);
/*      */             }
/*      */           } else {
/* 2206 */             generatedXML.writeElement("D", "resourcetype", 0);
/* 2207 */             generatedXML.writeElement("D", "collection", 2);
/* 2208 */             generatedXML.writeElement("D", "resourcetype", 1);
/*      */           }
/* 2210 */         } else if (property.equals("source")) {
/* 2211 */           generatedXML.writeProperty("D", "source", "");
/* 2212 */         } else if (property.equals("supportedlock")) {
/* 2213 */           String supportedLocks = "<D:lockentry><D:lockscope><D:exclusive/></D:lockscope><D:locktype><D:write/></D:locktype></D:lockentry><D:lockentry><D:lockscope><D:shared/></D:lockscope><D:locktype><D:write/></D:locktype></D:lockentry>";
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2220 */           generatedXML.writeElement("D", "supportedlock", 0);
/* 2221 */           generatedXML.writeText(supportedLocks);
/* 2222 */           generatedXML.writeElement("D", "supportedlock", 1);
/* 2223 */         } else if (property.equals("lockdiscovery")) {
/* 2224 */           if (!generateLockDiscovery(path, generatedXML))
/* 2225 */             propertiesNotFound.addElement(property);
/*      */         } else {
/* 2227 */           propertiesNotFound.addElement(property);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2232 */       generatedXML.writeElement("D", "prop", 1);
/* 2233 */       generatedXML.writeElement("D", "status", 0);
/* 2234 */       generatedXML.writeText(status);
/* 2235 */       generatedXML.writeElement("D", "status", 1);
/* 2236 */       generatedXML.writeElement("D", "propstat", 1);
/*      */       
/* 2238 */       Enumeration<String> propertiesNotFoundList = propertiesNotFound.elements();
/*      */       
/* 2240 */       if (propertiesNotFoundList.hasMoreElements())
/*      */       {
/*      */ 
/* 2243 */         status = "HTTP/1.1 404 " + WebdavStatus.getStatusText(404);
/*      */         
/* 2245 */         generatedXML.writeElement("D", "propstat", 0);
/* 2246 */         generatedXML.writeElement("D", "prop", 0);
/*      */         
/* 2248 */         while (propertiesNotFoundList.hasMoreElements()) {
/* 2249 */           generatedXML.writeElement("D", (String)propertiesNotFoundList.nextElement(), 2);
/*      */         }
/*      */         
/*      */ 
/* 2253 */         generatedXML.writeElement("D", "prop", 1);
/* 2254 */         generatedXML.writeElement("D", "status", 0);
/* 2255 */         generatedXML.writeText(status);
/* 2256 */         generatedXML.writeElement("D", "status", 1);
/* 2257 */         generatedXML.writeElement("D", "propstat", 1);
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     }
/*      */     
/*      */     
/* 2265 */     generatedXML.writeElement("D", "response", 1);
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
/*      */   private boolean generateLockDiscovery(String path, XMLWriter generatedXML)
/*      */   {
/* 2279 */     LockInfo resourceLock = (LockInfo)this.resourceLocks.get(path);
/* 2280 */     Enumeration<LockInfo> collectionLocksList = this.collectionLocks.elements();
/*      */     
/* 2282 */     boolean wroteStart = false;
/*      */     
/* 2284 */     if (resourceLock != null) {
/* 2285 */       wroteStart = true;
/* 2286 */       generatedXML.writeElement("D", "lockdiscovery", 0);
/* 2287 */       resourceLock.toXML(generatedXML);
/*      */     }
/*      */     
/* 2290 */     while (collectionLocksList.hasMoreElements()) {
/* 2291 */       LockInfo currentLock = (LockInfo)collectionLocksList.nextElement();
/* 2292 */       if (path.startsWith(currentLock.path)) {
/* 2293 */         if (!wroteStart) {
/* 2294 */           wroteStart = true;
/* 2295 */           generatedXML.writeElement("D", "lockdiscovery", 0);
/*      */         }
/*      */         
/* 2298 */         currentLock.toXML(generatedXML);
/*      */       }
/*      */     }
/*      */     
/* 2302 */     if (wroteStart) {
/* 2303 */       generatedXML.writeElement("D", "lockdiscovery", 1);
/*      */     } else {
/* 2305 */       return false;
/*      */     }
/*      */     
/* 2308 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getISOCreationDate(long creationDate)
/*      */   {
/* 2318 */     return creationDateFormat.format(new Date(creationDate));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private StringBuilder determineMethodsAllowed(HttpServletRequest req)
/*      */   {
/* 2328 */     StringBuilder methodsAllowed = new StringBuilder();
/*      */     
/* 2330 */     WebResource resource = this.resources.getResource(getRelativePath(req));
/*      */     
/* 2332 */     if (!resource.exists()) {
/* 2333 */       methodsAllowed.append("OPTIONS, MKCOL, PUT, LOCK");
/* 2334 */       return methodsAllowed;
/*      */     }
/*      */     
/* 2337 */     methodsAllowed.append("OPTIONS, GET, HEAD, POST, DELETE");
/*      */     
/* 2339 */     if (((req instanceof RequestFacade)) && 
/* 2340 */       (((RequestFacade)req).getAllowTrace())) {
/* 2341 */       methodsAllowed.append(", TRACE");
/*      */     }
/* 2343 */     methodsAllowed.append(", PROPPATCH, COPY, MOVE, LOCK, UNLOCK");
/*      */     
/* 2345 */     if (this.listings) {
/* 2346 */       methodsAllowed.append(", PROPFIND");
/*      */     }
/*      */     
/* 2349 */     if (resource.isFile()) {
/* 2350 */       methodsAllowed.append(", PUT");
/*      */     }
/*      */     
/* 2353 */     return methodsAllowed;
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
/*      */   private class LockInfo
/*      */   {
/* 2368 */     String path = "/";
/* 2369 */     String type = "write";
/* 2370 */     String scope = "exclusive";
/* 2371 */     int depth = 0;
/* 2372 */     String owner = "";
/* 2373 */     Vector<String> tokens = new Vector();
/* 2374 */     long expiresAt = 0L;
/* 2375 */     Date creationDate = new Date();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private LockInfo() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2387 */       StringBuilder result = new StringBuilder("Type:");
/* 2388 */       result.append(this.type);
/* 2389 */       result.append("\nScope:");
/* 2390 */       result.append(this.scope);
/* 2391 */       result.append("\nDepth:");
/* 2392 */       result.append(this.depth);
/* 2393 */       result.append("\nOwner:");
/* 2394 */       result.append(this.owner);
/* 2395 */       result.append("\nExpiration:");
/* 2396 */       result.append(FastHttpDateFormat.formatDate(this.expiresAt, null));
/* 2397 */       Enumeration<String> tokensList = this.tokens.elements();
/* 2398 */       while (tokensList.hasMoreElements()) {
/* 2399 */         result.append("\nToken:");
/* 2400 */         result.append((String)tokensList.nextElement());
/*      */       }
/* 2402 */       result.append("\n");
/* 2403 */       return result.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasExpired()
/*      */     {
/* 2411 */       return System.currentTimeMillis() > this.expiresAt;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isExclusive()
/*      */     {
/* 2419 */       return this.scope.equals("exclusive");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void toXML(XMLWriter generatedXML)
/*      */     {
/* 2431 */       generatedXML.writeElement("D", "activelock", 0);
/*      */       
/* 2433 */       generatedXML.writeElement("D", "locktype", 0);
/* 2434 */       generatedXML.writeElement("D", this.type, 2);
/* 2435 */       generatedXML.writeElement("D", "locktype", 1);
/*      */       
/* 2437 */       generatedXML.writeElement("D", "lockscope", 0);
/* 2438 */       generatedXML.writeElement("D", this.scope, 2);
/* 2439 */       generatedXML.writeElement("D", "lockscope", 1);
/*      */       
/* 2441 */       generatedXML.writeElement("D", "depth", 0);
/* 2442 */       if (this.depth == WebdavServlet.this.maxDepth) {
/* 2443 */         generatedXML.writeText("Infinity");
/*      */       } else {
/* 2445 */         generatedXML.writeText("0");
/*      */       }
/* 2447 */       generatedXML.writeElement("D", "depth", 1);
/*      */       
/* 2449 */       generatedXML.writeElement("D", "owner", 0);
/* 2450 */       generatedXML.writeText(this.owner);
/* 2451 */       generatedXML.writeElement("D", "owner", 1);
/*      */       
/* 2453 */       generatedXML.writeElement("D", "timeout", 0);
/* 2454 */       long timeout = (this.expiresAt - System.currentTimeMillis()) / 1000L;
/* 2455 */       generatedXML.writeText("Second-" + timeout);
/* 2456 */       generatedXML.writeElement("D", "timeout", 1);
/*      */       
/* 2458 */       generatedXML.writeElement("D", "locktoken", 0);
/* 2459 */       Enumeration<String> tokensList = this.tokens.elements();
/* 2460 */       while (tokensList.hasMoreElements()) {
/* 2461 */         generatedXML.writeElement("D", "href", 0);
/* 2462 */         generatedXML.writeText("opaquelocktoken:" + 
/* 2463 */           (String)tokensList.nextElement());
/* 2464 */         generatedXML.writeElement("D", "href", 1);
/*      */       }
/* 2466 */       generatedXML.writeElement("D", "locktoken", 1);
/*      */       
/* 2468 */       generatedXML.writeElement("D", "activelock", 1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class WebdavResolver
/*      */     implements EntityResolver
/*      */   {
/*      */     private ServletContext context;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public WebdavResolver(ServletContext theContext)
/*      */     {
/* 2487 */       this.context = theContext;
/*      */     }
/*      */     
/*      */     public InputSource resolveEntity(String publicId, String systemId)
/*      */     {
/* 2492 */       this.context.log(DefaultServlet.sm.getString("webdavservlet.enternalEntityIgnored", new Object[] { publicId, systemId }));
/*      */       
/* 2494 */       return new InputSource(new StringReader("Ignored external entity"));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlets\WebdavServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */