/*      */ package org.apache.catalina.servlets;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.ServletConfig;
/*      */ import javax.servlet.ServletContext;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletOutputStream;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.ServletResponseWrapper;
/*      */ import javax.servlet.UnavailableException;
/*      */ import javax.servlet.http.HttpServlet;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import javax.xml.transform.stream.StreamSource;
/*      */ import org.apache.catalina.Context;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.WebResource;
/*      */ import org.apache.catalina.WebResourceRoot;
/*      */ import org.apache.catalina.connector.RequestFacade;
/*      */ import org.apache.catalina.connector.ResponseFacade;
/*      */ import org.apache.catalina.util.ServerInfo;
/*      */ import org.apache.catalina.util.URLEncoder;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ import org.apache.tomcat.util.security.Escape;
/*      */ import org.apache.tomcat.util.security.PrivilegedGetTccl;
/*      */ import org.apache.tomcat.util.security.PrivilegedSetTccl;
/*      */ import org.w3c.dom.Document;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.EntityResolver2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultServlet
/*      */   extends HttpServlet
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*  134 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.servlets");
/*      */   
/*      */ 
/*      */   private static final DocumentBuilderFactory factory;
/*      */   
/*      */ 
/*      */   private static final SecureEntityResolver secureEntityResolver;
/*      */   
/*      */ 
/*  143 */   protected static final ArrayList<Range> FULL = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final String mimeSeparation = "CATALINA_MIME_BOUNDARY";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected static final String RESOURCES_JNDI_NAME = "java:/comp/Resources";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final int BUFFER_SIZE = 4096;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*  166 */     if (Globals.IS_SECURITY_ENABLED) {
/*  167 */       factory = DocumentBuilderFactory.newInstance();
/*  168 */       factory.setNamespaceAware(true);
/*  169 */       factory.setValidating(false);
/*  170 */       secureEntityResolver = new SecureEntityResolver(null);
/*      */     } else {
/*  172 */       factory = null;
/*  173 */       secureEntityResolver = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected int debug = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  188 */   protected int input = 2048;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  193 */   protected boolean listings = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  198 */   protected boolean readOnly = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CompressionFormat[] compressionFormats;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  208 */   protected int output = 2048;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  213 */   protected String localXsltFile = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  218 */   protected String contextXsltFile = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  223 */   protected String globalXsltFile = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  228 */   protected String readmeFile = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  233 */   protected transient WebResourceRoot resources = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  239 */   protected String fileEncoding = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  244 */   protected int sendfileSize = 49152;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  249 */   protected boolean useAcceptRanges = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  254 */   protected boolean showServerInfo = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  274 */     if (getServletConfig().getInitParameter("debug") != null) {
/*  275 */       this.debug = Integer.parseInt(getServletConfig().getInitParameter("debug"));
/*      */     }
/*  277 */     if (getServletConfig().getInitParameter("input") != null) {
/*  278 */       this.input = Integer.parseInt(getServletConfig().getInitParameter("input"));
/*      */     }
/*  280 */     if (getServletConfig().getInitParameter("output") != null) {
/*  281 */       this.output = Integer.parseInt(getServletConfig().getInitParameter("output"));
/*      */     }
/*  283 */     this.listings = Boolean.parseBoolean(getServletConfig().getInitParameter("listings"));
/*      */     
/*  285 */     if (getServletConfig().getInitParameter("readonly") != null) {
/*  286 */       this.readOnly = Boolean.parseBoolean(getServletConfig().getInitParameter("readonly"));
/*      */     }
/*  288 */     this.compressionFormats = parseCompressionFormats(
/*  289 */       getServletConfig().getInitParameter("precompressed"), 
/*  290 */       getServletConfig().getInitParameter("gzip"));
/*      */     
/*  292 */     if (getServletConfig().getInitParameter("sendfileSize") != null)
/*      */     {
/*  294 */       this.sendfileSize = (Integer.parseInt(getServletConfig().getInitParameter("sendfileSize")) * 1024);
/*      */     }
/*  296 */     this.fileEncoding = getServletConfig().getInitParameter("fileEncoding");
/*      */     
/*  298 */     this.globalXsltFile = getServletConfig().getInitParameter("globalXsltFile");
/*  299 */     this.contextXsltFile = getServletConfig().getInitParameter("contextXsltFile");
/*  300 */     this.localXsltFile = getServletConfig().getInitParameter("localXsltFile");
/*  301 */     this.readmeFile = getServletConfig().getInitParameter("readmeFile");
/*      */     
/*  303 */     if (getServletConfig().getInitParameter("useAcceptRanges") != null) {
/*  304 */       this.useAcceptRanges = Boolean.parseBoolean(getServletConfig().getInitParameter("useAcceptRanges"));
/*      */     }
/*      */     
/*  307 */     if (this.input < 256)
/*  308 */       this.input = 256;
/*  309 */     if (this.output < 256) {
/*  310 */       this.output = 256;
/*      */     }
/*  312 */     if (this.debug > 0) {
/*  313 */       log("DefaultServlet.init:  input buffer size=" + this.input + ", output buffer size=" + this.output);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  318 */     this.resources = ((WebResourceRoot)getServletContext().getAttribute("org.apache.catalina.resources"));
/*      */     
/*      */ 
/*  321 */     if (this.resources == null) {
/*  322 */       throw new UnavailableException("No resources");
/*      */     }
/*      */     
/*  325 */     if (getServletConfig().getInitParameter("showServerInfo") != null) {
/*  326 */       this.showServerInfo = Boolean.parseBoolean(getServletConfig().getInitParameter("showServerInfo"));
/*      */     }
/*      */   }
/*      */   
/*      */   private CompressionFormat[] parseCompressionFormats(String precompressed, String gzip) {
/*  331 */     List<CompressionFormat> ret = new ArrayList();
/*  332 */     if ((precompressed != null) && (precompressed.indexOf('=') > 0)) {
/*  333 */       for (String pair : precompressed.split(",")) {
/*  334 */         String[] setting = pair.split("=");
/*  335 */         String encoding = setting[0];
/*  336 */         String extension = setting[1];
/*  337 */         ret.add(new CompressionFormat(extension, encoding));
/*      */       }
/*  339 */     } else if (precompressed != null) {
/*  340 */       if (Boolean.parseBoolean(precompressed)) {
/*  341 */         ret.add(new CompressionFormat(".br", "br"));
/*  342 */         ret.add(new CompressionFormat(".gz", "gzip"));
/*      */       }
/*  344 */     } else if (Boolean.parseBoolean(gzip))
/*      */     {
/*  346 */       ret.add(new CompressionFormat(".gz", "gzip"));
/*      */     }
/*  348 */     return (CompressionFormat[])ret.toArray(new CompressionFormat[ret.size()]);
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
/*  362 */     return getRelativePath(request, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String getRelativePath(HttpServletRequest request, boolean allowEmptyPath)
/*      */   {
/*      */     String servletPath;
/*      */     
/*      */     String pathInfo;
/*      */     
/*      */     String servletPath;
/*      */     
/*  375 */     if (request.getAttribute("javax.servlet.include.request_uri") != null)
/*      */     {
/*  377 */       String pathInfo = (String)request.getAttribute("javax.servlet.include.path_info");
/*  378 */       servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
/*      */     } else {
/*  380 */       pathInfo = request.getPathInfo();
/*  381 */       servletPath = request.getServletPath();
/*      */     }
/*      */     
/*  384 */     StringBuilder result = new StringBuilder();
/*  385 */     if (servletPath.length() > 0) {
/*  386 */       result.append(servletPath);
/*      */     }
/*  388 */     if (pathInfo != null) {
/*  389 */       result.append(pathInfo);
/*      */     }
/*  391 */     if ((result.length() == 0) && (!allowEmptyPath)) {
/*  392 */       result.append('/');
/*      */     }
/*      */     
/*  395 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getPathPrefix(HttpServletRequest request)
/*      */   {
/*  407 */     return request.getContextPath();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void service(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  415 */     if (req.getDispatcherType() == DispatcherType.ERROR) {
/*  416 */       doGet(req, resp);
/*      */     } else {
/*  418 */       super.service(req, resp);
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
/*      */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  438 */     serveResource(request, response, true, this.fileEncoding);
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
/*      */   protected void doHead(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  458 */     boolean serveContent = DispatcherType.INCLUDE.equals(request.getDispatcherType());
/*  459 */     serveResource(request, response, serveContent, this.fileEncoding);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  486 */     StringBuilder allow = new StringBuilder();
/*      */     
/*  488 */     allow.append("GET, HEAD");
/*      */     
/*  490 */     allow.append(", POST");
/*      */     
/*  492 */     allow.append(", PUT");
/*      */     
/*  494 */     allow.append(", DELETE");
/*      */     
/*  496 */     if (((req instanceof RequestFacade)) && 
/*  497 */       (((RequestFacade)req).getAllowTrace())) {
/*  498 */       allow.append(", TRACE");
/*      */     }
/*      */     
/*  501 */     allow.append(", OPTIONS");
/*      */     
/*  503 */     resp.setHeader("Allow", allow.toString());
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
/*      */   protected void doPost(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException, ServletException
/*      */   {
/*  520 */     doGet(request, response);
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
/*      */   protected void doPut(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  537 */     if (this.readOnly) {
/*  538 */       resp.sendError(403);
/*  539 */       return;
/*      */     }
/*      */     
/*  542 */     String path = getRelativePath(req);
/*      */     
/*  544 */     WebResource resource = this.resources.getResource(path);
/*      */     
/*  546 */     Range range = parseContentRange(req, resp);
/*      */     
/*  548 */     InputStream resourceInputStream = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  555 */       if (range != null) {
/*  556 */         File contentFile = executePartialPut(req, range, path);
/*  557 */         resourceInputStream = new FileInputStream(contentFile);
/*      */       } else {
/*  559 */         resourceInputStream = req.getInputStream();
/*      */       }
/*      */       
/*  562 */       if (this.resources.write(path, resourceInputStream, true)) {
/*  563 */         if (resource.exists()) {
/*  564 */           resp.setStatus(204);
/*      */         } else {
/*  566 */           resp.setStatus(201);
/*      */         }
/*      */       } else
/*  569 */         resp.sendError(409);
/*      */       return;
/*      */     } finally {
/*  572 */       if (resourceInputStream != null) {
/*      */         try {
/*  574 */           resourceInputStream.close();
/*      */         }
/*      */         catch (IOException localIOException1) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected File executePartialPut(HttpServletRequest req, Range range, String path)
/*      */     throws IOException
/*      */   {
/*  601 */     File tempDir = (File)getServletContext().getAttribute("javax.servlet.context.tempdir");
/*      */     
/*  603 */     String convertedResourcePath = path.replace('/', '.');
/*  604 */     File contentFile = new File(tempDir, convertedResourcePath);
/*  605 */     if (contentFile.createNewFile())
/*      */     {
/*  607 */       contentFile.deleteOnExit();
/*      */     }
/*      */     
/*  610 */     RandomAccessFile randAccessContentFile = new RandomAccessFile(contentFile, "rw");Throwable localThrowable9 = null;
/*      */     try
/*      */     {
/*  613 */       WebResource oldResource = this.resources.getResource(path);
/*      */       
/*      */ 
/*  616 */       if (oldResource.isFile())
/*      */       {
/*  618 */         BufferedInputStream bufOldRevStream = new BufferedInputStream(oldResource.getInputStream(), 4096);Throwable localThrowable10 = null;
/*      */         
/*      */         try
/*      */         {
/*  622 */           copyBuffer = new byte['က'];
/*  623 */           int numBytesRead; while ((numBytesRead = bufOldRevStream.read(copyBuffer)) != -1) {
/*  624 */             randAccessContentFile.write(copyBuffer, 0, numBytesRead);
/*      */           }
/*      */         }
/*      */         catch (Throwable localThrowable1)
/*      */         {
/*  617 */           localThrowable10 = localThrowable1;throw localThrowable1;
/*      */         }
/*      */         finally {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  630 */       randAccessContentFile.setLength(range.length);
/*      */       
/*      */ 
/*  633 */       randAccessContentFile.seek(range.start);
/*      */       
/*  635 */       byte[] transferBuffer = new byte['က'];
/*      */       
/*  637 */       BufferedInputStream requestBufInStream = new BufferedInputStream(req.getInputStream(), 4096);byte[] copyBuffer = null;
/*      */       try { int numBytesRead;
/*  638 */         while ((numBytesRead = requestBufInStream.read(transferBuffer)) != -1) {
/*  639 */           randAccessContentFile.write(transferBuffer, 0, numBytesRead);
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable12)
/*      */       {
/*  636 */         copyBuffer = localThrowable12;throw localThrowable12;
/*      */       }
/*      */       finally {}
/*      */     }
/*      */     catch (Throwable localThrowable7)
/*      */     {
/*  610 */       localThrowable9 = localThrowable7;throw localThrowable7;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  642 */       if (randAccessContentFile != null) if (localThrowable9 != null) try { randAccessContentFile.close(); } catch (Throwable localThrowable8) { localThrowable9.addSuppressed(localThrowable8); } else randAccessContentFile.close();
/*      */     }
/*  644 */     return contentFile;
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
/*      */   protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
/*      */     throws ServletException, IOException
/*      */   {
/*  661 */     if (this.readOnly) {
/*  662 */       resp.sendError(403);
/*  663 */       return;
/*      */     }
/*      */     
/*  666 */     String path = getRelativePath(req);
/*      */     
/*  668 */     WebResource resource = this.resources.getResource(path);
/*      */     
/*  670 */     if (resource.exists()) {
/*  671 */       if (resource.delete()) {
/*  672 */         resp.setStatus(204);
/*      */       } else {
/*  674 */         resp.sendError(405);
/*      */       }
/*      */     } else {
/*  677 */       resp.sendError(404);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/*  700 */     return (checkIfMatch(request, response, resource)) && 
/*  701 */       (checkIfModifiedSince(request, response, resource)) && 
/*  702 */       (checkIfNoneMatch(request, response, resource)) && 
/*  703 */       (checkIfUnmodifiedSince(request, response, resource));
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
/*  715 */     return URLEncoder.DEFAULT.encode(path, StandardCharsets.UTF_8);
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
/*      */   protected void serveResource(HttpServletRequest request, HttpServletResponse response, boolean content, String encoding)
/*      */     throws IOException, ServletException
/*      */   {
/*  737 */     boolean serveContent = content;
/*      */     
/*      */ 
/*  740 */     String path = getRelativePath(request, true);
/*      */     
/*  742 */     if (this.debug > 0) {
/*  743 */       if (serveContent) {
/*  744 */         log("DefaultServlet.serveResource:  Serving resource '" + path + "' headers and data");
/*      */       }
/*      */       else {
/*  747 */         log("DefaultServlet.serveResource:  Serving resource '" + path + "' headers only");
/*      */       }
/*      */     }
/*      */     
/*  751 */     if (path.length() == 0)
/*      */     {
/*  753 */       doDirectoryRedirect(request, response);
/*  754 */       return;
/*      */     }
/*      */     
/*  757 */     WebResource resource = this.resources.getResource(path);
/*  758 */     boolean isError = DispatcherType.ERROR == request.getDispatcherType();
/*      */     
/*  760 */     if (!resource.exists())
/*      */     {
/*      */ 
/*  763 */       String requestUri = (String)request.getAttribute("javax.servlet.include.request_uri");
/*      */       
/*  765 */       if (requestUri == null) {
/*  766 */         requestUri = request.getRequestURI();
/*      */       }
/*      */       else
/*      */       {
/*  770 */         throw new FileNotFoundException(sm.getString("defaultServlet.missingResource", new Object[] { requestUri }));
/*      */       }
/*      */       
/*      */ 
/*  774 */       if (isError) {
/*  775 */         response.sendError(((Integer)request.getAttribute("javax.servlet.error.status_code"))
/*  776 */           .intValue());
/*      */       } else {
/*  778 */         response.sendError(404, requestUri);
/*      */       }
/*  780 */       return;
/*      */     }
/*      */     
/*  783 */     if (!resource.canRead())
/*      */     {
/*      */ 
/*  786 */       String requestUri = (String)request.getAttribute("javax.servlet.include.request_uri");
/*      */       
/*  788 */       if (requestUri == null) {
/*  789 */         requestUri = request.getRequestURI();
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  794 */         throw new FileNotFoundException(sm.getString("defaultServlet.missingResource", new Object[] { requestUri }));
/*      */       }
/*      */       
/*      */ 
/*  798 */       if (isError) {
/*  799 */         response.sendError(((Integer)request.getAttribute("javax.servlet.error.status_code"))
/*  800 */           .intValue());
/*      */       } else {
/*  802 */         response.sendError(403, requestUri);
/*      */       }
/*  804 */       return;
/*      */     }
/*      */     
/*  807 */     boolean included = false;
/*      */     
/*      */ 
/*  810 */     if (resource.isFile())
/*      */     {
/*  812 */       included = request.getAttribute("javax.servlet.include.context_path") != null;
/*      */       
/*  814 */       if ((!included) && (!isError) && (!checkIfHeaders(request, response, resource))) {
/*  815 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  820 */     String contentType = resource.getMimeType();
/*  821 */     if (contentType == null) {
/*  822 */       contentType = getServletContext().getMimeType(resource.getName());
/*  823 */       resource.setMimeType(contentType);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  829 */     String eTag = null;
/*  830 */     String lastModifiedHttp = null;
/*  831 */     if ((resource.isFile()) && (!isError)) {
/*  832 */       eTag = resource.getETag();
/*  833 */       lastModifiedHttp = resource.getLastModifiedHttp();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  838 */     boolean usingPrecompressedVersion = false;
/*  839 */     if ((this.compressionFormats.length > 0) && (!included) && (resource.isFile()) && 
/*  840 */       (!pathEndsWithCompressedExtension(path)))
/*      */     {
/*  842 */       List<PrecompressedResource> precompressedResources = getAvailablePrecompressedResources(path);
/*  843 */       if (!precompressedResources.isEmpty()) {
/*  844 */         Collection<String> varyHeaders = response.getHeaders("Vary");
/*  845 */         boolean addRequired = true;
/*  846 */         for (String varyHeader : varyHeaders) {
/*  847 */           if (("*".equals(varyHeader)) || 
/*  848 */             ("accept-encoding".equalsIgnoreCase(varyHeader))) {
/*  849 */             addRequired = false;
/*  850 */             break;
/*      */           }
/*      */         }
/*  853 */         if (addRequired) {
/*  854 */           response.addHeader("Vary", "accept-encoding");
/*      */         }
/*      */         
/*  857 */         PrecompressedResource bestResource = getBestPrecompressedResource(request, precompressedResources);
/*  858 */         if (bestResource != null) {
/*  859 */           response.addHeader("Content-Encoding", bestResource.format.encoding);
/*  860 */           resource = bestResource.resource;
/*  861 */           usingPrecompressedVersion = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  866 */     ArrayList<Range> ranges = null;
/*  867 */     long contentLength = -1L;
/*      */     
/*  869 */     if (resource.isDirectory()) {
/*  870 */       if (!path.endsWith("/")) {
/*  871 */         doDirectoryRedirect(request, response);
/*  872 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  877 */       if (!this.listings) {
/*  878 */         response.sendError(404, request
/*  879 */           .getRequestURI());
/*  880 */         return;
/*      */       }
/*  882 */       contentType = "text/html;charset=UTF-8";
/*      */     } else {
/*  884 */       if (!isError) {
/*  885 */         if (this.useAcceptRanges)
/*      */         {
/*  887 */           response.setHeader("Accept-Ranges", "bytes");
/*      */         }
/*      */         
/*      */ 
/*  891 */         ranges = parseRange(request, response, resource);
/*      */         
/*      */ 
/*  894 */         response.setHeader("ETag", eTag);
/*      */         
/*      */ 
/*  897 */         response.setHeader("Last-Modified", lastModifiedHttp);
/*      */       }
/*      */       
/*      */ 
/*  901 */       contentLength = resource.getContentLength();
/*      */       
/*      */ 
/*  904 */       if (contentLength == 0L) {
/*  905 */         serveContent = false;
/*      */       }
/*      */     }
/*      */     
/*  909 */     ServletOutputStream ostream = null;
/*  910 */     PrintWriter writer = null;
/*      */     
/*  912 */     if (serveContent) {
/*      */       try
/*      */       {
/*  915 */         ostream = response.getOutputStream();
/*      */       }
/*      */       catch (IllegalStateException e)
/*      */       {
/*  919 */         if ((!usingPrecompressedVersion) && ((contentType == null) || 
/*      */         
/*  921 */           (contentType.startsWith("text")) || 
/*  922 */           (contentType.endsWith("xml")) || 
/*  923 */           (contentType.contains("/javascript"))))
/*      */         {
/*  925 */           writer = response.getWriter();
/*      */           
/*  927 */           ranges = FULL;
/*      */         } else {
/*  929 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  937 */     ServletResponse r = response;
/*  938 */     long contentWritten = 0L;
/*  939 */     while ((r instanceof ServletResponseWrapper)) {
/*  940 */       r = ((ServletResponseWrapper)r).getResponse();
/*      */     }
/*  942 */     if ((r instanceof ResponseFacade)) {
/*  943 */       contentWritten = ((ResponseFacade)r).getContentWritten();
/*      */     }
/*  945 */     if (contentWritten > 0L) {
/*  946 */       ranges = FULL;
/*      */     }
/*      */     
/*  949 */     if ((resource.isDirectory()) || (isError) || (((ranges != null) && 
/*      */     
/*  951 */       (!ranges.isEmpty())) || (
/*  952 */       (request.getHeader("Range") == null) || (ranges == FULL))))
/*      */     {
/*      */ 
/*      */ 
/*  956 */       if (contentType != null) {
/*  957 */         if (this.debug > 0) {
/*  958 */           log("DefaultServlet.serveFile:  contentType='" + contentType + "'");
/*      */         }
/*  960 */         response.setContentType(contentType);
/*      */       }
/*  962 */       if ((resource.isFile()) && (contentLength >= 0L) && ((!serveContent) || (ostream != null)))
/*      */       {
/*  964 */         if (this.debug > 0) {
/*  965 */           log("DefaultServlet.serveFile:  contentLength=" + contentLength);
/*      */         }
/*      */         
/*      */ 
/*  969 */         if (contentWritten == 0L) {
/*  970 */           response.setContentLengthLong(contentLength);
/*      */         }
/*      */       }
/*      */       
/*  974 */       if (serveContent) {
/*      */         try {
/*  976 */           response.setBufferSize(this.output);
/*      */         }
/*      */         catch (IllegalStateException localIllegalStateException1) {}
/*      */         
/*  980 */         InputStream renderResult = null;
/*  981 */         if (ostream == null)
/*      */         {
/*      */ 
/*  984 */           if (resource.isDirectory()) {
/*  985 */             renderResult = render(getPathPrefix(request), resource, encoding);
/*      */           } else {
/*  987 */             renderResult = resource.getInputStream();
/*      */           }
/*  989 */           copy(renderResult, writer, encoding);
/*      */         }
/*      */         else {
/*  992 */           if (resource.isDirectory()) {
/*  993 */             renderResult = render(getPathPrefix(request), resource, encoding);
/*      */ 
/*      */           }
/*  996 */           else if (!checkSendfile(request, response, resource, contentLength, null))
/*      */           {
/*      */ 
/*      */ 
/* 1000 */             byte[] resourceBody = resource.getContent();
/* 1001 */             if (resourceBody == null)
/*      */             {
/*      */ 
/* 1004 */               renderResult = resource.getInputStream();
/*      */             }
/*      */             else {
/* 1007 */               ostream.write(resourceBody);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1013 */           if (renderResult != null) {
/* 1014 */             copy(renderResult, ostream);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1021 */       if ((ranges == null) || (ranges.isEmpty())) {
/* 1022 */         return;
/*      */       }
/*      */       
/*      */ 
/* 1026 */       response.setStatus(206);
/*      */       
/* 1028 */       if (ranges.size() == 1)
/*      */       {
/* 1030 */         Range range = (Range)ranges.get(0);
/* 1031 */         response.addHeader("Content-Range", "bytes " + range.start + "-" + range.end + "/" + range.length);
/*      */         
/*      */ 
/*      */ 
/* 1035 */         long length = range.end - range.start + 1L;
/* 1036 */         response.setContentLengthLong(length);
/*      */         
/* 1038 */         if (contentType != null) {
/* 1039 */           if (this.debug > 0) {
/* 1040 */             log("DefaultServlet.serveFile:  contentType='" + contentType + "'");
/*      */           }
/* 1042 */           response.setContentType(contentType);
/*      */         }
/*      */         
/* 1045 */         if (serveContent) {
/*      */           try {
/* 1047 */             response.setBufferSize(this.output);
/*      */           }
/*      */           catch (IllegalStateException localIllegalStateException2) {}
/*      */           
/* 1051 */           if (ostream != null) {
/* 1052 */             if (!checkSendfile(request, response, resource, range.end - range.start + 1L, range))
/*      */             {
/* 1054 */               copy(resource, ostream, range);
/*      */             }
/*      */           } else {
/* 1057 */             throw new IllegalStateException();
/*      */           }
/*      */         }
/*      */       } else {
/* 1061 */         response.setContentType("multipart/byteranges; boundary=CATALINA_MIME_BOUNDARY");
/*      */         
/* 1063 */         if (serveContent) {
/*      */           try {
/* 1065 */             response.setBufferSize(this.output);
/*      */           }
/*      */           catch (IllegalStateException localIllegalStateException3) {}
/*      */           
/* 1069 */           if (ostream != null) {
/* 1070 */             copy(resource, ostream, ranges.iterator(), contentType);
/*      */           }
/*      */           else {
/* 1073 */             throw new IllegalStateException();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean pathEndsWithCompressedExtension(String path) {
/* 1081 */     for (CompressionFormat format : this.compressionFormats) {
/* 1082 */       if (path.endsWith(format.extension)) {
/* 1083 */         return true;
/*      */       }
/*      */     }
/* 1086 */     return false;
/*      */   }
/*      */   
/*      */   private List<PrecompressedResource> getAvailablePrecompressedResources(String path) {
/* 1090 */     List<PrecompressedResource> ret = new ArrayList(this.compressionFormats.length);
/* 1091 */     for (CompressionFormat format : this.compressionFormats) {
/* 1092 */       WebResource precompressedResource = this.resources.getResource(path + format.extension);
/* 1093 */       if ((precompressedResource.exists()) && (precompressedResource.isFile())) {
/* 1094 */         ret.add(new PrecompressedResource(precompressedResource, format, null));
/*      */       }
/*      */     }
/* 1097 */     return ret;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private PrecompressedResource getBestPrecompressedResource(HttpServletRequest request, List<PrecompressedResource> precompressedResources)
/*      */   {
/* 1109 */     Enumeration<String> headers = request.getHeaders("Accept-Encoding");
/* 1110 */     PrecompressedResource bestResource = null;
/* 1111 */     double bestResourceQuality = 0.0D;
/* 1112 */     int bestResourcePreference = Integer.MAX_VALUE;
/* 1113 */     while (headers.hasMoreElements()) {
/* 1114 */       String header = (String)headers.nextElement();
/* 1115 */       for (String preference : header.split(",")) {
/* 1116 */         double quality = 1.0D;
/* 1117 */         int qualityIdx = preference.indexOf(';');
/* 1118 */         if (qualityIdx > 0) {
/* 1119 */           int equalsIdx = preference.indexOf('=', qualityIdx + 1);
/* 1120 */           if (equalsIdx != -1)
/*      */           {
/*      */ 
/* 1123 */             quality = Double.parseDouble(preference.substring(equalsIdx + 1).trim());
/*      */           }
/* 1125 */         } else if (quality >= bestResourceQuality) {
/* 1126 */           String encoding = preference;
/* 1127 */           if (qualityIdx > 0) {
/* 1128 */             encoding = encoding.substring(0, qualityIdx);
/*      */           }
/* 1130 */           encoding = encoding.trim();
/* 1131 */           if ("identity".equals(encoding)) {
/* 1132 */             bestResource = null;
/* 1133 */             bestResourceQuality = quality;
/* 1134 */             bestResourcePreference = Integer.MAX_VALUE;
/*      */ 
/*      */           }
/* 1137 */           else if ("*".equals(encoding)) {
/* 1138 */             bestResource = (PrecompressedResource)precompressedResources.get(0);
/* 1139 */             bestResourceQuality = quality;
/* 1140 */             bestResourcePreference = 0;
/*      */           }
/*      */           else {
/* 1143 */             for (int i = 0; i < precompressedResources.size(); i++) {
/* 1144 */               PrecompressedResource resource = (PrecompressedResource)precompressedResources.get(i);
/* 1145 */               if (encoding.equals(resource.format.encoding)) {
/* 1146 */                 if ((quality <= bestResourceQuality) && (i >= bestResourcePreference)) break;
/* 1147 */                 bestResource = resource;
/* 1148 */                 bestResourceQuality = quality;
/* 1149 */                 bestResourcePreference = i; break;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1157 */     return bestResource;
/*      */   }
/*      */   
/*      */   private void doDirectoryRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException
/*      */   {
/* 1162 */     StringBuilder location = new StringBuilder(request.getRequestURI());
/* 1163 */     location.append('/');
/* 1164 */     if (request.getQueryString() != null) {
/* 1165 */       location.append('?');
/* 1166 */       location.append(request.getQueryString());
/*      */     }
/* 1168 */     response.sendRedirect(response.encodeRedirectURL(location.toString()));
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
/*      */   protected Range parseContentRange(HttpServletRequest request, HttpServletResponse response)
/*      */     throws IOException
/*      */   {
/* 1184 */     String rangeHeader = request.getHeader("Content-Range");
/*      */     
/* 1186 */     if (rangeHeader == null) {
/* 1187 */       return null;
/*      */     }
/*      */     
/* 1190 */     if (!rangeHeader.startsWith("bytes")) {
/* 1191 */       response.sendError(400);
/* 1192 */       return null;
/*      */     }
/*      */     
/* 1195 */     rangeHeader = rangeHeader.substring(6).trim();
/*      */     
/* 1197 */     int dashPos = rangeHeader.indexOf('-');
/* 1198 */     int slashPos = rangeHeader.indexOf('/');
/*      */     
/* 1200 */     if (dashPos == -1) {
/* 1201 */       response.sendError(400);
/* 1202 */       return null;
/*      */     }
/*      */     
/* 1205 */     if (slashPos == -1) {
/* 1206 */       response.sendError(400);
/* 1207 */       return null;
/*      */     }
/*      */     
/* 1210 */     Range range = new Range();
/*      */     try
/*      */     {
/* 1213 */       range.start = Long.parseLong(rangeHeader.substring(0, dashPos));
/*      */       
/* 1215 */       range.end = Long.parseLong(rangeHeader.substring(dashPos + 1, slashPos));
/*      */       
/* 1217 */       range.length = Long.parseLong(rangeHeader.substring(slashPos + 1, rangeHeader.length()));
/*      */     } catch (NumberFormatException e) {
/* 1219 */       response.sendError(400);
/* 1220 */       return null;
/*      */     }
/*      */     
/* 1223 */     if (!range.validate()) {
/* 1224 */       response.sendError(400);
/* 1225 */       return null;
/*      */     }
/*      */     
/* 1228 */     return range;
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
/*      */   protected ArrayList<Range> parseRange(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/* 1247 */     String headerValue = request.getHeader("If-Range");
/*      */     
/* 1249 */     if (headerValue != null)
/*      */     {
/* 1251 */       long headerValueTime = -1L;
/*      */       try {
/* 1253 */         headerValueTime = request.getDateHeader("If-Range");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */       
/*      */ 
/* 1258 */       String eTag = resource.getETag();
/* 1259 */       long lastModified = resource.getLastModified();
/*      */       
/* 1261 */       if (headerValueTime == -1L)
/*      */       {
/*      */ 
/*      */ 
/* 1265 */         if (!eTag.equals(headerValue.trim())) {
/* 1266 */           return FULL;
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/* 1273 */       else if (lastModified > headerValueTime + 1000L) {
/* 1274 */         return FULL;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1280 */     long fileLength = resource.getContentLength();
/*      */     
/* 1282 */     if (fileLength == 0L) {
/* 1283 */       return null;
/*      */     }
/*      */     
/* 1286 */     String rangeHeader = request.getHeader("Range");
/*      */     
/* 1288 */     if (rangeHeader == null) {
/* 1289 */       return null;
/*      */     }
/*      */     
/* 1292 */     if (!rangeHeader.startsWith("bytes")) {
/* 1293 */       response.addHeader("Content-Range", "bytes */" + fileLength);
/* 1294 */       response
/* 1295 */         .sendError(416);
/* 1296 */       return null;
/*      */     }
/*      */     
/* 1299 */     rangeHeader = rangeHeader.substring(6);
/*      */     
/*      */ 
/*      */ 
/* 1303 */     ArrayList<Range> result = new ArrayList();
/* 1304 */     StringTokenizer commaTokenizer = new StringTokenizer(rangeHeader, ",");
/*      */     
/*      */ 
/* 1307 */     while (commaTokenizer.hasMoreTokens()) {
/* 1308 */       String rangeDefinition = commaTokenizer.nextToken().trim();
/*      */       
/* 1310 */       Range currentRange = new Range();
/* 1311 */       currentRange.length = fileLength;
/*      */       
/* 1313 */       int dashPos = rangeDefinition.indexOf('-');
/*      */       
/* 1315 */       if (dashPos == -1) {
/* 1316 */         response.addHeader("Content-Range", "bytes */" + fileLength);
/* 1317 */         response
/* 1318 */           .sendError(416);
/* 1319 */         return null;
/*      */       }
/*      */       
/* 1322 */       if (dashPos == 0) {
/*      */         try
/*      */         {
/* 1325 */           long offset = Long.parseLong(rangeDefinition);
/* 1326 */           currentRange.start = (fileLength + offset);
/* 1327 */           currentRange.end = (fileLength - 1L);
/*      */         } catch (NumberFormatException e) {
/* 1329 */           response.addHeader("Content-Range", "bytes */" + fileLength);
/*      */           
/* 1331 */           response
/* 1332 */             .sendError(416);
/*      */           
/* 1334 */           return null;
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*      */         try
/*      */         {
/* 1341 */           currentRange.start = Long.parseLong(rangeDefinition.substring(0, dashPos));
/* 1342 */           if (dashPos < rangeDefinition.length() - 1)
/*      */           {
/* 1344 */             currentRange.end = Long.parseLong(rangeDefinition
/* 1345 */               .substring(dashPos + 1, rangeDefinition.length()));
/*      */           } else
/* 1347 */             currentRange.end = (fileLength - 1L);
/*      */         } catch (NumberFormatException e) {
/* 1349 */           response.addHeader("Content-Range", "bytes */" + fileLength);
/*      */           
/* 1351 */           response
/* 1352 */             .sendError(416);
/*      */           
/* 1354 */           return null;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1359 */       if (!currentRange.validate()) {
/* 1360 */         response.addHeader("Content-Range", "bytes */" + fileLength);
/* 1361 */         response
/* 1362 */           .sendError(416);
/* 1363 */         return null;
/*      */       }
/*      */       
/* 1366 */       result.add(currentRange);
/*      */     }
/*      */     
/* 1369 */     return result;
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
/*      */   @Deprecated
/*      */   protected InputStream render(String contextPath, WebResource resource)
/*      */     throws IOException, ServletException
/*      */   {
/* 1389 */     return render(contextPath, resource, null);
/*      */   }
/*      */   
/*      */   protected InputStream render(String contextPath, WebResource resource, String encoding)
/*      */     throws IOException, ServletException
/*      */   {
/* 1395 */     Source xsltSource = findXsltSource(resource);
/*      */     
/* 1397 */     if (xsltSource == null) {
/* 1398 */       return renderHtml(contextPath, resource, encoding);
/*      */     }
/* 1400 */     return renderXml(contextPath, resource, xsltSource, encoding);
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
/*      */   @Deprecated
/*      */   protected InputStream renderXml(String contextPath, WebResource resource, Source xsltSource)
/*      */     throws IOException, ServletException
/*      */   {
/* 1422 */     return renderXml(contextPath, resource, xsltSource, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected InputStream renderXml(String contextPath, WebResource resource, Source xsltSource, String encoding)
/*      */     throws IOException, ServletException
/*      */   {
/* 1429 */     StringBuilder sb = new StringBuilder();
/*      */     
/* 1431 */     sb.append("<?xml version=\"1.0\"?>");
/* 1432 */     sb.append("<listing ");
/* 1433 */     sb.append(" contextPath='");
/* 1434 */     sb.append(contextPath);
/* 1435 */     sb.append("'");
/* 1436 */     sb.append(" directory='");
/* 1437 */     sb.append(resource.getName());
/* 1438 */     sb.append("' ");
/* 1439 */     sb.append(" hasParent='").append(!resource.getName().equals("/"));
/* 1440 */     sb.append("'>");
/*      */     
/* 1442 */     sb.append("<entries>");
/*      */     
/* 1444 */     String[] entries = this.resources.list(resource.getWebappPath());
/*      */     
/*      */ 
/* 1447 */     String rewrittenContextPath = rewriteUrl(contextPath);
/* 1448 */     String directoryWebappPath = resource.getWebappPath();
/*      */     
/* 1450 */     for (String entry : entries)
/*      */     {
/* 1452 */       if ((!entry.equalsIgnoreCase("WEB-INF")) && 
/* 1453 */         (!entry.equalsIgnoreCase("META-INF")) && 
/* 1454 */         (!entry.equalsIgnoreCase(this.localXsltFile)))
/*      */       {
/*      */ 
/* 1457 */         if (!(directoryWebappPath + entry).equals(this.contextXsltFile))
/*      */         {
/*      */ 
/*      */ 
/* 1461 */           WebResource childResource = this.resources.getResource(directoryWebappPath + entry);
/* 1462 */           if (childResource.exists())
/*      */           {
/*      */ 
/*      */ 
/* 1466 */             sb.append("<entry");
/* 1467 */             sb.append(" type='")
/* 1468 */               .append(childResource.isDirectory() ? "dir" : "file")
/* 1469 */               .append("'");
/* 1470 */             sb.append(" urlPath='")
/* 1471 */               .append(rewrittenContextPath)
/* 1472 */               .append(rewriteUrl(directoryWebappPath + entry))
/* 1473 */               .append(childResource.isDirectory() ? "/" : "")
/* 1474 */               .append("'");
/* 1475 */             if (childResource.isFile())
/*      */             {
/*      */ 
/* 1478 */               sb.append(" size='").append(renderSize(childResource.getContentLength())).append("'");
/*      */             }
/*      */             
/*      */ 
/* 1482 */             sb.append(" date='").append(childResource.getLastModifiedHttp()).append("'");
/*      */             
/* 1484 */             sb.append(">");
/* 1485 */             sb.append(Escape.htmlElementContent(entry));
/* 1486 */             if (childResource.isDirectory())
/* 1487 */               sb.append("/");
/* 1488 */             sb.append("</entry>");
/*      */           } } } }
/* 1490 */     sb.append("</entries>");
/*      */     
/* 1492 */     String readme = getReadme(resource, encoding);
/*      */     
/* 1494 */     if (readme != null) {
/* 1495 */       sb.append("<readme><![CDATA[");
/* 1496 */       sb.append(readme);
/* 1497 */       sb.append("]]></readme>");
/*      */     }
/*      */     
/* 1500 */     sb.append("</listing>");
/*      */     
/*      */     ClassLoader original;
/*      */     
/*      */     ClassLoader original;
/* 1505 */     if (Globals.IS_SECURITY_ENABLED) {
/* 1506 */       PrivilegedGetTccl pa = new PrivilegedGetTccl();
/* 1507 */       original = (ClassLoader)AccessController.doPrivileged(pa);
/*      */     } else {
/* 1509 */       original = Thread.currentThread().getContextClassLoader();
/*      */     }
/*      */     try {
/* 1512 */       if (Globals.IS_SECURITY_ENABLED)
/*      */       {
/* 1514 */         PrivilegedSetTccl pa = new PrivilegedSetTccl(DefaultServlet.class.getClassLoader());
/* 1515 */         AccessController.doPrivileged(pa);
/*      */       } else {
/* 1517 */         Thread.currentThread().setContextClassLoader(DefaultServlet.class
/* 1518 */           .getClassLoader());
/*      */       }
/*      */       
/* 1521 */       TransformerFactory tFactory = TransformerFactory.newInstance();
/* 1522 */       Source xmlSource = new StreamSource(new StringReader(sb.toString()));
/* 1523 */       Transformer transformer = tFactory.newTransformer(xsltSource);
/*      */       
/* 1525 */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 1526 */       OutputStreamWriter osWriter = new OutputStreamWriter(stream, "UTF8");
/* 1527 */       StreamResult out = new StreamResult(osWriter);
/* 1528 */       transformer.transform(xmlSource, out);
/* 1529 */       osWriter.flush();
/* 1530 */       PrivilegedSetTccl pa; return new ByteArrayInputStream(stream.toByteArray());
/*      */     } catch (TransformerException e) {
/* 1532 */       throw new ServletException("XSL transformer error", e);
/*      */     } finally {
/* 1534 */       if (Globals.IS_SECURITY_ENABLED) {
/* 1535 */         PrivilegedSetTccl pa = new PrivilegedSetTccl(original);
/* 1536 */         AccessController.doPrivileged(pa);
/*      */       } else {
/* 1538 */         Thread.currentThread().setContextClassLoader(original);
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
/*      */   @Deprecated
/*      */   protected InputStream renderHtml(String contextPath, WebResource resource)
/*      */     throws IOException
/*      */   {
/* 1559 */     return renderHtml(contextPath, resource, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected InputStream renderHtml(String contextPath, WebResource resource, String encoding)
/*      */     throws IOException
/*      */   {
/* 1566 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 1567 */     OutputStreamWriter osWriter = new OutputStreamWriter(stream, "UTF8");
/* 1568 */     PrintWriter writer = new PrintWriter(osWriter);
/*      */     
/* 1570 */     StringBuilder sb = new StringBuilder();
/*      */     
/* 1572 */     String[] entries = this.resources.list(resource.getWebappPath());
/*      */     
/*      */ 
/* 1575 */     String rewrittenContextPath = rewriteUrl(contextPath);
/* 1576 */     String directoryWebappPath = resource.getWebappPath();
/*      */     
/*      */ 
/* 1579 */     sb.append("<html>\r\n");
/* 1580 */     sb.append("<head>\r\n");
/* 1581 */     sb.append("<title>");
/* 1582 */     sb.append(sm.getString("directory.title", new Object[] { directoryWebappPath }));
/* 1583 */     sb.append("</title>\r\n");
/* 1584 */     sb.append("<STYLE><!--");
/* 1585 */     sb.append("h1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} h2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} h3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} body {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} b {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} p {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;} a {color:black;} a.name {color:black;} .line {height:1px;background-color:#525D76;border:none;}");
/* 1586 */     sb.append("--></STYLE> ");
/* 1587 */     sb.append("</head>\r\n");
/* 1588 */     sb.append("<body>");
/* 1589 */     sb.append("<h1>");
/* 1590 */     sb.append(sm.getString("directory.title", new Object[] { directoryWebappPath }));
/*      */     
/*      */ 
/* 1593 */     String parentDirectory = directoryWebappPath;
/* 1594 */     if (parentDirectory.endsWith("/"))
/*      */     {
/* 1596 */       parentDirectory = parentDirectory.substring(0, parentDirectory.length() - 1);
/*      */     }
/* 1598 */     int slash = parentDirectory.lastIndexOf('/');
/* 1599 */     if (slash >= 0) {
/* 1600 */       String parent = directoryWebappPath.substring(0, slash);
/* 1601 */       sb.append(" - <a href=\"");
/* 1602 */       sb.append(rewrittenContextPath);
/* 1603 */       if (parent.equals(""))
/* 1604 */         parent = "/";
/* 1605 */       sb.append(rewriteUrl(parent));
/* 1606 */       if (!parent.endsWith("/"))
/* 1607 */         sb.append("/");
/* 1608 */       sb.append("\">");
/* 1609 */       sb.append("<b>");
/* 1610 */       sb.append(sm.getString("directory.parent", new Object[] { parent }));
/* 1611 */       sb.append("</b>");
/* 1612 */       sb.append("</a>");
/*      */     }
/*      */     
/* 1615 */     sb.append("</h1>");
/* 1616 */     sb.append("<HR size=\"1\" noshade=\"noshade\">");
/*      */     
/* 1618 */     sb.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\">\r\n");
/*      */     
/*      */ 
/*      */ 
/* 1622 */     sb.append("<tr>\r\n");
/* 1623 */     sb.append("<td align=\"left\"><font size=\"+1\"><strong>");
/* 1624 */     sb.append(sm.getString("directory.filename"));
/* 1625 */     sb.append("</strong></font></td>\r\n");
/* 1626 */     sb.append("<td align=\"center\"><font size=\"+1\"><strong>");
/* 1627 */     sb.append(sm.getString("directory.size"));
/* 1628 */     sb.append("</strong></font></td>\r\n");
/* 1629 */     sb.append("<td align=\"right\"><font size=\"+1\"><strong>");
/* 1630 */     sb.append(sm.getString("directory.lastModified"));
/* 1631 */     sb.append("</strong></font></td>\r\n");
/* 1632 */     sb.append("</tr>");
/*      */     
/* 1634 */     boolean shade = false;
/* 1635 */     for (String entry : entries) {
/* 1636 */       if ((!entry.equalsIgnoreCase("WEB-INF")) && 
/* 1637 */         (!entry.equalsIgnoreCase("META-INF")))
/*      */       {
/*      */ 
/*      */ 
/* 1641 */         WebResource childResource = this.resources.getResource(directoryWebappPath + entry);
/* 1642 */         if (childResource.exists())
/*      */         {
/*      */ 
/*      */ 
/* 1646 */           sb.append("<tr");
/* 1647 */           if (shade)
/* 1648 */             sb.append(" bgcolor=\"#eeeeee\"");
/* 1649 */           sb.append(">\r\n");
/* 1650 */           shade = !shade;
/*      */           
/* 1652 */           sb.append("<td align=\"left\">&nbsp;&nbsp;\r\n");
/* 1653 */           sb.append("<a href=\"");
/* 1654 */           sb.append(rewrittenContextPath);
/* 1655 */           sb.append(rewriteUrl(directoryWebappPath + entry));
/* 1656 */           if (childResource.isDirectory())
/* 1657 */             sb.append("/");
/* 1658 */           sb.append("\"><tt>");
/* 1659 */           sb.append(Escape.htmlElementContent(entry));
/* 1660 */           if (childResource.isDirectory())
/* 1661 */             sb.append("/");
/* 1662 */           sb.append("</tt></a></td>\r\n");
/*      */           
/* 1664 */           sb.append("<td align=\"right\"><tt>");
/* 1665 */           if (childResource.isDirectory()) {
/* 1666 */             sb.append("&nbsp;");
/*      */           } else
/* 1668 */             sb.append(renderSize(childResource.getContentLength()));
/* 1669 */           sb.append("</tt></td>\r\n");
/*      */           
/* 1671 */           sb.append("<td align=\"right\"><tt>");
/* 1672 */           sb.append(childResource.getLastModifiedHttp());
/* 1673 */           sb.append("</tt></td>\r\n");
/*      */           
/* 1675 */           sb.append("</tr>\r\n");
/*      */         }
/*      */       }
/*      */     }
/* 1679 */     sb.append("</table>\r\n");
/*      */     
/* 1681 */     sb.append("<HR size=\"1\" noshade=\"noshade\">");
/*      */     
/* 1683 */     String readme = getReadme(resource, encoding);
/* 1684 */     if (readme != null) {
/* 1685 */       sb.append(readme);
/* 1686 */       sb.append("<HR size=\"1\" noshade=\"noshade\">");
/*      */     }
/*      */     
/* 1689 */     if (this.showServerInfo) {
/* 1690 */       sb.append("<h3>").append(ServerInfo.getServerInfo()).append("</h3>");
/*      */     }
/* 1692 */     sb.append("</body>\r\n");
/* 1693 */     sb.append("</html>\r\n");
/*      */     
/*      */ 
/* 1696 */     writer.write(sb.toString());
/* 1697 */     writer.flush();
/* 1698 */     return new ByteArrayInputStream(stream.toByteArray());
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
/*      */   protected String renderSize(long size)
/*      */   {
/* 1711 */     long leftSide = size / 1024L;
/* 1712 */     long rightSide = size % 1024L / 103L;
/* 1713 */     if ((leftSide == 0L) && (rightSide == 0L) && (size > 0L)) {
/* 1714 */       rightSide = 1L;
/*      */     }
/* 1716 */     return "" + leftSide + "." + rightSide + " kb";
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
/*      */   @Deprecated
/*      */   protected String getReadme(WebResource directory)
/*      */   {
/* 1730 */     return getReadme(directory, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getReadme(WebResource directory, String encoding)
/*      */   {
/* 1741 */     if (this.readmeFile != null) {
/* 1742 */       WebResource resource = this.resources.getResource(directory
/* 1743 */         .getWebappPath() + this.readmeFile);
/* 1744 */       StringWriter buffer; if (resource.isFile()) {
/* 1745 */         buffer = new StringWriter();
/* 1746 */         InputStreamReader reader = null;
/* 1747 */         try { InputStream is = resource.getInputStream();Throwable localThrowable3 = null;
/* 1748 */           try { if (encoding != null) {
/* 1749 */               reader = new InputStreamReader(is, encoding);
/*      */             } else {
/* 1751 */               reader = new InputStreamReader(is);
/*      */             }
/* 1753 */             copyRange(reader, new PrintWriter(buffer));
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/* 1747 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/* 1754 */             if (is != null) { if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { is.close();
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1764 */           return buffer.toString();
/*      */         }
/*      */         catch (IOException e)
/*      */         {
/* 1755 */           log("Failure to close reader", e);
/*      */         } finally {
/* 1757 */           if (reader != null) {
/*      */             try {
/* 1759 */               reader.close();
/*      */             }
/*      */             catch (IOException localIOException3) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1766 */       if (this.debug > 10) {
/* 1767 */         log("readme '" + this.readmeFile + "' not found");
/*      */       }
/* 1769 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1773 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Source findXsltSource(WebResource directory)
/*      */     throws IOException
/*      */   {
/* 1786 */     if (this.localXsltFile != null) {
/* 1787 */       WebResource resource = this.resources.getResource(directory
/* 1788 */         .getWebappPath() + this.localXsltFile);
/* 1789 */       if (resource.isFile()) {
/* 1790 */         InputStream is = resource.getInputStream();
/* 1791 */         if (is != null) {
/* 1792 */           if (Globals.IS_SECURITY_ENABLED) {
/* 1793 */             return secureXslt(is);
/*      */           }
/* 1795 */           return new StreamSource(is);
/*      */         }
/*      */       }
/*      */       
/* 1799 */       if (this.debug > 10) {
/* 1800 */         log("localXsltFile '" + this.localXsltFile + "' not found");
/*      */       }
/*      */     }
/*      */     
/* 1804 */     if (this.contextXsltFile != null)
/*      */     {
/* 1806 */       InputStream is = getServletContext().getResourceAsStream(this.contextXsltFile);
/* 1807 */       if (is != null) {
/* 1808 */         if (Globals.IS_SECURITY_ENABLED) {
/* 1809 */           return secureXslt(is);
/*      */         }
/* 1811 */         return new StreamSource(is);
/*      */       }
/*      */       
/*      */ 
/* 1815 */       if (this.debug > 10) {
/* 1816 */         log("contextXsltFile '" + this.contextXsltFile + "' not found");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1822 */     if (this.globalXsltFile != null) {
/* 1823 */       File f = validateGlobalXsltFile();
/* 1824 */       if (f != null) {
/* 1825 */         FileInputStream fis = new FileInputStream(f);Throwable localThrowable3 = null;
/* 1826 */         try { byte[] b = new byte[(int)f.length()];
/* 1827 */           fis.read(b);
/* 1828 */           return new StreamSource(new ByteArrayInputStream(b));
/*      */         }
/*      */         catch (Throwable localThrowable1)
/*      */         {
/* 1825 */           localThrowable3 = localThrowable1;throw localThrowable1;
/*      */         }
/*      */         finally
/*      */         {
/* 1829 */           if (fis != null) if (localThrowable3 != null) try { fis.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else fis.close();
/*      */         }
/*      */       }
/*      */     }
/* 1833 */     return null;
/*      */   }
/*      */   
/*      */   private File validateGlobalXsltFile()
/*      */   {
/* 1838 */     Context context = this.resources.getContext();
/*      */     
/* 1840 */     File baseConf = new File(context.getCatalinaBase(), "conf");
/* 1841 */     File result = validateGlobalXsltFile(baseConf);
/* 1842 */     if (result == null) {
/* 1843 */       File homeConf = new File(context.getCatalinaHome(), "conf");
/* 1844 */       if (!baseConf.equals(homeConf)) {
/* 1845 */         result = validateGlobalXsltFile(homeConf);
/*      */       }
/*      */     }
/*      */     
/* 1849 */     return result;
/*      */   }
/*      */   
/*      */   private File validateGlobalXsltFile(File base)
/*      */   {
/* 1854 */     File candidate = new File(this.globalXsltFile);
/* 1855 */     if (!candidate.isAbsolute()) {
/* 1856 */       candidate = new File(base, this.globalXsltFile);
/*      */     }
/*      */     
/* 1859 */     if (!candidate.isFile()) {
/* 1860 */       return null;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1865 */       if (!candidate.getCanonicalPath().startsWith(base.getCanonicalPath())) {
/* 1866 */         return null;
/*      */       }
/*      */     } catch (IOException ioe) {
/* 1869 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 1873 */     String nameLower = candidate.getName().toLowerCase(Locale.ENGLISH);
/* 1874 */     if ((!nameLower.endsWith(".xslt")) && (!nameLower.endsWith(".xsl"))) {
/* 1875 */       return null;
/*      */     }
/*      */     
/* 1878 */     return candidate;
/*      */   }
/*      */   
/*      */ 
/*      */   private Source secureXslt(InputStream is)
/*      */   {
/* 1884 */     result = null;
/*      */     try {
/* 1886 */       DocumentBuilder builder = factory.newDocumentBuilder();
/* 1887 */       builder.setEntityResolver(secureEntityResolver);
/* 1888 */       Document document = builder.parse(is);
/* 1889 */       return new DOMSource(document);
/*      */     } catch (ParserConfigurationException|SAXException|IOException e) {
/* 1891 */       if (this.debug > 0) {
/* 1892 */         log(e.getMessage(), e);
/*      */       }
/*      */     } finally {
/* 1895 */       if (is != null) {
/*      */         try {
/* 1897 */           is.close();
/*      */         }
/*      */         catch (IOException localIOException2) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean checkSendfile(HttpServletRequest request, HttpServletResponse response, WebResource resource, long length, Range range)
/*      */   {
/* 1925 */     if ((this.sendfileSize > 0) && (length > this.sendfileSize)) {
/*      */       String canonicalPath;
/* 1927 */       if ((Boolean.TRUE.equals(request.getAttribute("org.apache.tomcat.sendfile.support"))) && 
/* 1928 */         (request.getClass().getName().equals("org.apache.catalina.connector.RequestFacade")) && 
/* 1929 */         (response.getClass().getName().equals("org.apache.catalina.connector.ResponseFacade")) && 
/* 1930 */         (resource.isFile()) && 
/* 1931 */         ((canonicalPath = resource.getCanonicalPath()) != null))
/*      */       {
/* 1933 */         request.setAttribute("org.apache.tomcat.sendfile.filename", canonicalPath);
/* 1934 */         if (range == null) {
/* 1935 */           request.setAttribute("org.apache.tomcat.sendfile.start", Long.valueOf(0L));
/* 1936 */           request.setAttribute("org.apache.tomcat.sendfile.end", Long.valueOf(length));
/*      */         } else {
/* 1938 */           request.setAttribute("org.apache.tomcat.sendfile.start", Long.valueOf(range.start));
/* 1939 */           request.setAttribute("org.apache.tomcat.sendfile.end", Long.valueOf(range.end + 1L));
/*      */         }
/* 1941 */         return true;
/*      */       } }
/* 1943 */     return false;
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
/*      */   protected boolean checkIfMatch(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/* 1962 */     String eTag = resource.getETag();
/* 1963 */     String headerValue = request.getHeader("If-Match");
/* 1964 */     if ((headerValue != null) && 
/* 1965 */       (headerValue.indexOf('*') == -1))
/*      */     {
/* 1967 */       StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
/*      */       
/* 1969 */       boolean conditionSatisfied = false;
/*      */       
/* 1971 */       while ((!conditionSatisfied) && (commaTokenizer.hasMoreTokens())) {
/* 1972 */         String currentToken = commaTokenizer.nextToken();
/* 1973 */         if (currentToken.trim().equals(eTag)) {
/* 1974 */           conditionSatisfied = true;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1979 */       if (!conditionSatisfied)
/*      */       {
/* 1981 */         response.sendError(412);
/* 1982 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1987 */     return true;
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
/*      */   protected boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */   {
/*      */     try
/*      */     {
/* 2004 */       long headerValue = request.getDateHeader("If-Modified-Since");
/* 2005 */       long lastModified = resource.getLastModified();
/* 2006 */       if (headerValue != -1L)
/*      */       {
/*      */ 
/*      */ 
/* 2010 */         if ((request.getHeader("If-None-Match") == null) && (lastModified < headerValue + 1000L))
/*      */         {
/*      */ 
/*      */ 
/* 2014 */           response.setStatus(304);
/* 2015 */           response.setHeader("ETag", resource.getETag());
/*      */           
/* 2017 */           return false;
/*      */         }
/*      */       }
/*      */     } catch (IllegalArgumentException illegalArgument) {
/* 2021 */       return true;
/*      */     }
/* 2023 */     return true;
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
/*      */   protected boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/* 2042 */     String eTag = resource.getETag();
/* 2043 */     String headerValue = request.getHeader("If-None-Match");
/* 2044 */     if (headerValue != null)
/*      */     {
/* 2046 */       boolean conditionSatisfied = false;
/*      */       
/* 2048 */       if (!headerValue.equals("*"))
/*      */       {
/* 2050 */         StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
/*      */         
/*      */ 
/* 2053 */         while ((!conditionSatisfied) && (commaTokenizer.hasMoreTokens())) {
/* 2054 */           String currentToken = commaTokenizer.nextToken();
/* 2055 */           if (currentToken.trim().equals(eTag)) {
/* 2056 */             conditionSatisfied = true;
/*      */           }
/*      */         }
/*      */       } else {
/* 2060 */         conditionSatisfied = true;
/*      */       }
/*      */       
/* 2063 */       if (conditionSatisfied)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2069 */         if (("GET".equals(request.getMethod())) || 
/* 2070 */           ("HEAD".equals(request.getMethod()))) {
/* 2071 */           response.setStatus(304);
/* 2072 */           response.setHeader("ETag", eTag);
/*      */           
/* 2074 */           return false;
/*      */         }
/* 2076 */         response.sendError(412);
/* 2077 */         return false;
/*      */       }
/*      */     }
/* 2080 */     return true;
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
/*      */   protected boolean checkIfUnmodifiedSince(HttpServletRequest request, HttpServletResponse response, WebResource resource)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 2098 */       long lastModified = resource.getLastModified();
/* 2099 */       long headerValue = request.getDateHeader("If-Unmodified-Since");
/* 2100 */       if ((headerValue != -1L) && 
/* 2101 */         (lastModified >= headerValue + 1000L))
/*      */       {
/*      */ 
/* 2104 */         response.sendError(412);
/* 2105 */         return false;
/*      */       }
/*      */     }
/*      */     catch (IllegalArgumentException illegalArgument) {
/* 2109 */       return true;
/*      */     }
/* 2111 */     return true;
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
/*      */   @Deprecated
/*      */   protected void copy(WebResource resource, InputStream is, ServletOutputStream ostream)
/*      */     throws IOException
/*      */   {
/* 2133 */     copy(is, ostream);
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
/*      */   protected void copy(InputStream is, ServletOutputStream ostream)
/*      */     throws IOException
/*      */   {
/* 2149 */     IOException exception = null;
/* 2150 */     InputStream istream = new BufferedInputStream(is, this.input);
/*      */     
/*      */ 
/* 2153 */     exception = copyRange(istream, ostream);
/*      */     
/*      */ 
/* 2156 */     istream.close();
/*      */     
/*      */ 
/* 2159 */     if (exception != null) {
/* 2160 */       throw exception;
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
/*      */ 
/*      */   @Deprecated
/*      */   protected void copy(WebResource resource, InputStream is, PrintWriter writer, String encoding)
/*      */     throws IOException
/*      */   {
/* 2182 */     copy(is, writer, encoding);
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
/*      */   protected void copy(InputStream is, PrintWriter writer, String encoding)
/*      */     throws IOException
/*      */   {
/* 2198 */     IOException exception = null;
/*      */     Reader reader;
/*      */     Reader reader;
/* 2201 */     if (encoding == null) {
/* 2202 */       reader = new InputStreamReader(is);
/*      */     } else {
/* 2204 */       reader = new InputStreamReader(is, encoding);
/*      */     }
/*      */     
/*      */ 
/* 2208 */     exception = copyRange(reader, writer);
/*      */     
/*      */ 
/* 2211 */     reader.close();
/*      */     
/*      */ 
/* 2214 */     if (exception != null) {
/* 2215 */       throw exception;
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
/*      */   protected void copy(WebResource resource, ServletOutputStream ostream, Range range)
/*      */     throws IOException
/*      */   {
/* 2234 */     IOException exception = null;
/*      */     
/* 2236 */     InputStream resourceInputStream = resource.getInputStream();
/* 2237 */     InputStream istream = new BufferedInputStream(resourceInputStream, this.input);
/*      */     
/* 2239 */     exception = copyRange(istream, ostream, range.start, range.end);
/*      */     
/*      */ 
/* 2242 */     istream.close();
/*      */     
/*      */ 
/* 2245 */     if (exception != null) {
/* 2246 */       throw exception;
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
/*      */ 
/*      */   protected void copy(WebResource resource, ServletOutputStream ostream, Iterator<Range> ranges, String contentType)
/*      */     throws IOException
/*      */   {
/* 2267 */     IOException exception = null;
/*      */     
/* 2269 */     while ((exception == null) && (ranges.hasNext()))
/*      */     {
/* 2271 */       InputStream resourceInputStream = resource.getInputStream();
/* 2272 */       InputStream istream = new BufferedInputStream(resourceInputStream, this.input);Throwable localThrowable3 = null;
/*      */       try {
/* 2274 */         Range currentRange = (Range)ranges.next();
/*      */         
/*      */ 
/* 2277 */         ostream.println();
/* 2278 */         ostream.println("--CATALINA_MIME_BOUNDARY");
/* 2279 */         if (contentType != null)
/* 2280 */           ostream.println("Content-Type: " + contentType);
/* 2281 */         ostream.println("Content-Range: bytes " + currentRange.start + "-" + currentRange.end + "/" + currentRange.length);
/*      */         
/*      */ 
/* 2284 */         ostream.println();
/*      */         
/*      */ 
/* 2287 */         exception = copyRange(istream, ostream, currentRange.start, currentRange.end);
/*      */       }
/*      */       catch (Throwable localThrowable1)
/*      */       {
/* 2272 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2289 */         if (istream != null) if (localThrowable3 != null) try { istream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else istream.close();
/*      */       }
/*      */     }
/* 2292 */     ostream.println();
/* 2293 */     ostream.print("--CATALINA_MIME_BOUNDARY--");
/*      */     
/*      */ 
/* 2296 */     if (exception != null) {
/* 2297 */       throw exception;
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
/*      */   protected IOException copyRange(InputStream istream, ServletOutputStream ostream)
/*      */   {
/* 2315 */     exception = null;
/* 2316 */     byte[] buffer = new byte[this.input];
/* 2317 */     int len = buffer.length;
/*      */     try {
/*      */       for (;;) {
/* 2320 */         len = istream.read(buffer);
/* 2321 */         if (len == -1)
/*      */           break;
/* 2323 */         ostream.write(buffer, 0, len);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2330 */       return exception;
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 2325 */       exception = e;
/* 2326 */       len = -1;
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
/*      */ 
/*      */ 
/*      */   protected IOException copyRange(Reader reader, PrintWriter writer)
/*      */   {
/* 2347 */     exception = null;
/* 2348 */     char[] buffer = new char[this.input];
/* 2349 */     int len = buffer.length;
/*      */     try {
/*      */       for (;;) {
/* 2352 */         len = reader.read(buffer);
/* 2353 */         if (len == -1)
/*      */           break;
/* 2355 */         writer.write(buffer, 0, len);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2362 */       return exception;
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 2357 */       exception = e;
/* 2358 */       len = -1;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected IOException copyRange(InputStream istream, ServletOutputStream ostream, long start, long end)
/*      */   {
/* 2382 */     if (this.debug > 10) {
/* 2383 */       log("Serving bytes:" + start + "-" + end);
/*      */     }
/* 2385 */     long skipped = 0L;
/*      */     try {
/* 2387 */       skipped = istream.skip(start);
/*      */     } catch (IOException e) {
/* 2389 */       return e;
/*      */     }
/* 2391 */     if (skipped < start) {
/* 2392 */       return new IOException(sm.getString("defaultservlet.skipfail", new Object[] {
/* 2393 */         Long.valueOf(skipped), Long.valueOf(start) }));
/*      */     }
/*      */     
/* 2396 */     IOException exception = null;
/* 2397 */     long bytesToRead = end - start + 1L;
/*      */     
/* 2399 */     byte[] buffer = new byte[this.input];
/* 2400 */     int len = buffer.length;
/* 2401 */     while ((bytesToRead > 0L) && (len >= buffer.length)) {
/*      */       try {
/* 2403 */         len = istream.read(buffer);
/* 2404 */         if (bytesToRead >= len) {
/* 2405 */           ostream.write(buffer, 0, len);
/* 2406 */           bytesToRead -= len;
/*      */         } else {
/* 2408 */           ostream.write(buffer, 0, (int)bytesToRead);
/* 2409 */           bytesToRead = 0L;
/*      */         }
/*      */       } catch (IOException e) {
/* 2412 */         exception = e;
/* 2413 */         len = -1;
/*      */       }
/* 2415 */       if (len < buffer.length) {
/*      */         break;
/*      */       }
/*      */     }
/* 2419 */     return exception;
/*      */   }
/*      */   
/*      */ 
/*      */   public void destroy() {}
/*      */   
/*      */ 
/*      */   protected static class Range
/*      */   {
/*      */     public long start;
/*      */     
/*      */     public long end;
/*      */     
/*      */     public long length;
/*      */     
/*      */     public boolean validate()
/*      */     {
/* 2436 */       if (this.end >= this.length)
/* 2437 */         this.end = (this.length - 1L);
/* 2438 */       return (this.start >= 0L) && (this.end >= 0L) && (this.start <= this.end) && (this.length > 0L);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static class CompressionFormat implements Serializable {
/*      */     private static final long serialVersionUID = 1L;
/*      */     public final String extension;
/*      */     public final String encoding;
/*      */     
/*      */     public CompressionFormat(String extension, String encoding) {
/* 2448 */       this.extension = extension;
/* 2449 */       this.encoding = encoding;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PrecompressedResource {
/*      */     public final WebResource resource;
/*      */     public final DefaultServlet.CompressionFormat format;
/*      */     
/*      */     private PrecompressedResource(WebResource resource, DefaultServlet.CompressionFormat format) {
/* 2458 */       this.resource = resource;
/* 2459 */       this.format = format;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class SecureEntityResolver
/*      */     implements EntityResolver2
/*      */   {
/*      */     public InputSource resolveEntity(String publicId, String systemId)
/*      */       throws SAXException, IOException
/*      */     {
/* 2472 */       throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalEntity", new Object[] { publicId, systemId }));
/*      */     }
/*      */     
/*      */ 
/*      */     public InputSource getExternalSubset(String name, String baseURI)
/*      */       throws SAXException, IOException
/*      */     {
/* 2479 */       throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalSubset", new Object[] { name, baseURI }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
/*      */       throws SAXException, IOException
/*      */     {
/* 2487 */       throw new SAXException(DefaultServlet.sm.getString("defaultServlet.blockExternalEntity2", new Object[] { name, publicId, baseURI, systemId }));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlets\DefaultServlet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */