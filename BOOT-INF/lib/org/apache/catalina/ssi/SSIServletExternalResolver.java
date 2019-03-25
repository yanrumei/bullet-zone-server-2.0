/*     */ package org.apache.catalina.ssi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.coyote.Constants;
/*     */ import org.apache.tomcat.util.buf.B2CConverter;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ import org.apache.tomcat.util.http.RequestUtil;
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
/*     */ public class SSIServletExternalResolver
/*     */   implements SSIExternalResolver
/*     */ {
/*  50 */   protected final String[] VARIABLE_NAMES = { "AUTH_TYPE", "CONTENT_LENGTH", "CONTENT_TYPE", "DOCUMENT_NAME", "DOCUMENT_URI", "GATEWAY_INTERFACE", "HTTP_ACCEPT", "HTTP_ACCEPT_ENCODING", "HTTP_ACCEPT_LANGUAGE", "HTTP_CONNECTION", "HTTP_HOST", "HTTP_REFERER", "HTTP_USER_AGENT", "PATH_INFO", "PATH_TRANSLATED", "QUERY_STRING", "QUERY_STRING_UNESCAPED", "REMOTE_ADDR", "REMOTE_HOST", "REMOTE_PORT", "REMOTE_USER", "REQUEST_METHOD", "REQUEST_URI", "SCRIPT_FILENAME", "SCRIPT_NAME", "SERVER_ADDR", "SERVER_NAME", "SERVER_PORT", "SERVER_PROTOCOL", "SERVER_SOFTWARE", "UNIQUE_ID" };
/*     */   
/*     */ 
/*     */   protected final ServletContext context;
/*     */   
/*     */ 
/*     */   protected final HttpServletRequest req;
/*     */   
/*     */ 
/*     */   protected final HttpServletResponse res;
/*     */   
/*     */   protected final boolean isVirtualWebappRelative;
/*     */   
/*     */   protected final int debug;
/*     */   
/*     */   protected final String inputEncoding;
/*     */   
/*     */ 
/*     */   public SSIServletExternalResolver(ServletContext context, HttpServletRequest req, HttpServletResponse res, boolean isVirtualWebappRelative, int debug, String inputEncoding)
/*     */   {
/*  70 */     this.context = context;
/*  71 */     this.req = req;
/*  72 */     this.res = res;
/*  73 */     this.isVirtualWebappRelative = isVirtualWebappRelative;
/*  74 */     this.debug = debug;
/*  75 */     this.inputEncoding = inputEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void log(String message, Throwable throwable)
/*     */   {
/*  84 */     if (throwable != null) {
/*  85 */       this.context.log(message, throwable);
/*     */     } else {
/*  87 */       this.context.log(message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addVariableNames(Collection<String> variableNames)
/*     */   {
/*  94 */     for (int i = 0; i < this.VARIABLE_NAMES.length; i++) {
/*  95 */       String variableName = this.VARIABLE_NAMES[i];
/*  96 */       String variableValue = getVariableValue(variableName);
/*  97 */       if (variableValue != null) {
/*  98 */         variableNames.add(variableName);
/*     */       }
/*     */     }
/* 101 */     Enumeration<String> e = this.req.getAttributeNames();
/* 102 */     while (e.hasMoreElements()) {
/* 103 */       String name = (String)e.nextElement();
/* 104 */       if (!isNameReserved(name)) {
/* 105 */         variableNames.add(name);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object getReqAttributeIgnoreCase(String targetName)
/*     */   {
/* 112 */     Object object = null;
/* 113 */     if (!isNameReserved(targetName)) {
/* 114 */       object = this.req.getAttribute(targetName);
/* 115 */       if (object == null) {
/* 116 */         Enumeration<String> e = this.req.getAttributeNames();
/* 117 */         while (e.hasMoreElements()) {
/* 118 */           String name = (String)e.nextElement();
/* 119 */           if ((targetName.equalsIgnoreCase(name)) && 
/* 120 */             (!isNameReserved(name))) {
/* 121 */             object = this.req.getAttribute(name);
/* 122 */             if (object != null) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 129 */     return object;
/*     */   }
/*     */   
/*     */   protected boolean isNameReserved(String name)
/*     */   {
/* 134 */     return (name.startsWith("java.")) || (name.startsWith("javax.")) || 
/* 135 */       (name.startsWith("sun."));
/*     */   }
/*     */   
/*     */ 
/*     */   public void setVariableValue(String name, String value)
/*     */   {
/* 141 */     if (!isNameReserved(name)) {
/* 142 */       this.req.setAttribute(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getVariableValue(String name)
/*     */   {
/* 149 */     String retVal = null;
/* 150 */     Object object = getReqAttributeIgnoreCase(name);
/* 151 */     if (object != null) {
/* 152 */       retVal = object.toString();
/*     */     } else {
/* 154 */       retVal = getCGIVariable(name);
/*     */     }
/* 156 */     return retVal;
/*     */   }
/*     */   
/*     */   protected String getCGIVariable(String name)
/*     */   {
/* 161 */     String retVal = null;
/* 162 */     String[] nameParts = name.toUpperCase(Locale.ENGLISH).split("_");
/* 163 */     int requiredParts = 2;
/* 164 */     if (nameParts.length == 1) {
/* 165 */       if (nameParts[0].equals("PATH")) {
/* 166 */         requiredParts = 1;
/*     */       }
/*     */     }
/* 169 */     else if (nameParts[0].equals("AUTH")) {
/* 170 */       if (nameParts[1].equals("TYPE")) {
/* 171 */         retVal = this.req.getAuthType();
/*     */       }
/* 173 */     } else if (nameParts[0].equals("CONTENT")) {
/* 174 */       if (nameParts[1].equals("LENGTH")) {
/* 175 */         long contentLength = this.req.getContentLengthLong();
/* 176 */         if (contentLength >= 0L) {
/* 177 */           retVal = Long.toString(contentLength);
/*     */         }
/* 179 */       } else if (nameParts[1].equals("TYPE")) {
/* 180 */         retVal = this.req.getContentType();
/*     */       }
/* 182 */     } else if (nameParts[0].equals("DOCUMENT")) {
/* 183 */       if (nameParts[1].equals("NAME")) {
/* 184 */         String requestURI = this.req.getRequestURI();
/* 185 */         retVal = requestURI.substring(requestURI.lastIndexOf('/') + 1);
/* 186 */       } else if (nameParts[1].equals("URI")) {
/* 187 */         retVal = this.req.getRequestURI();
/*     */       }
/* 189 */     } else if (name.equalsIgnoreCase("GATEWAY_INTERFACE")) {
/* 190 */       retVal = "CGI/1.1";
/* 191 */     } else if (nameParts[0].equals("HTTP")) {
/* 192 */       if (nameParts[1].equals("ACCEPT")) {
/* 193 */         String accept = null;
/* 194 */         if (nameParts.length == 2) {
/* 195 */           accept = "Accept";
/* 196 */         } else if (nameParts[2].equals("ENCODING")) {
/* 197 */           requiredParts = 3;
/* 198 */           accept = "Accept-Encoding";
/* 199 */         } else if (nameParts[2].equals("LANGUAGE")) {
/* 200 */           requiredParts = 3;
/* 201 */           accept = "Accept-Language";
/*     */         }
/* 203 */         if (accept != null) {
/* 204 */           Enumeration<String> acceptHeaders = this.req.getHeaders(accept);
/* 205 */           if ((acceptHeaders != null) && 
/* 206 */             (acceptHeaders.hasMoreElements()))
/*     */           {
/* 208 */             StringBuilder rv = new StringBuilder((String)acceptHeaders.nextElement());
/* 209 */             while (acceptHeaders.hasMoreElements()) {
/* 210 */               rv.append(", ");
/* 211 */               rv.append((String)acceptHeaders.nextElement());
/*     */             }
/* 213 */             retVal = rv.toString();
/*     */           }
/*     */         }
/*     */       }
/* 217 */       else if (nameParts[1].equals("CONNECTION")) {
/* 218 */         retVal = this.req.getHeader("Connection");
/*     */       }
/* 220 */       else if (nameParts[1].equals("HOST")) {
/* 221 */         retVal = this.req.getHeader("Host");
/*     */       }
/* 223 */       else if (nameParts[1].equals("REFERER")) {
/* 224 */         retVal = this.req.getHeader("Referer");
/*     */       }
/* 226 */       else if ((nameParts[1].equals("USER")) && 
/* 227 */         (nameParts.length == 3) && 
/* 228 */         (nameParts[2].equals("AGENT"))) {
/* 229 */         requiredParts = 3;
/* 230 */         retVal = this.req.getHeader("User-Agent");
/*     */       }
/*     */     }
/* 233 */     else if (nameParts[0].equals("PATH")) {
/* 234 */       if (nameParts[1].equals("INFO")) {
/* 235 */         retVal = this.req.getPathInfo();
/* 236 */       } else if (nameParts[1].equals("TRANSLATED")) {
/* 237 */         retVal = this.req.getPathTranslated();
/*     */       }
/* 239 */     } else if (nameParts[0].equals("QUERY")) {
/* 240 */       if (nameParts[1].equals("STRING")) {
/* 241 */         String queryString = this.req.getQueryString();
/* 242 */         if (nameParts.length == 2)
/*     */         {
/* 244 */           retVal = nullToEmptyString(queryString);
/* 245 */         } else if (nameParts[2].equals("UNESCAPED")) {
/* 246 */           requiredParts = 3;
/* 247 */           if (queryString != null) {
/* 248 */             Charset uriCharset = null;
/* 249 */             Charset requestCharset = null;
/* 250 */             boolean useBodyEncodingForURI = false;
/*     */             
/*     */ 
/*     */ 
/* 254 */             if ((this.req instanceof org.apache.catalina.connector.Request)) {
/*     */               try {
/* 256 */                 requestCharset = ((org.apache.catalina.connector.Request)this.req).getCoyoteRequest().getCharset();
/*     */               }
/*     */               catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
/*     */               
/* 260 */               Connector connector = ((org.apache.catalina.connector.Request)this.req).getConnector();
/* 261 */               uriCharset = connector.getURICharset();
/* 262 */               useBodyEncodingForURI = connector.getUseBodyEncodingForURI();
/*     */             }
/*     */             
/*     */             Charset queryStringCharset;
/*     */             
/*     */             Charset queryStringCharset;
/* 268 */             if ((useBodyEncodingForURI) && (requestCharset != null)) {
/* 269 */               queryStringCharset = requestCharset; } else { Charset queryStringCharset;
/* 270 */               if (uriCharset != null) {
/* 271 */                 queryStringCharset = uriCharset;
/*     */               }
/*     */               else {
/* 274 */                 queryStringCharset = Constants.DEFAULT_URI_CHARSET;
/*     */               }
/*     */             }
/* 277 */             retVal = UDecoder.URLDecode(queryString, queryStringCharset);
/*     */           }
/*     */         }
/*     */       }
/* 281 */     } else if (nameParts[0].equals("REMOTE")) {
/* 282 */       if (nameParts[1].equals("ADDR")) {
/* 283 */         retVal = this.req.getRemoteAddr();
/* 284 */       } else if (nameParts[1].equals("HOST")) {
/* 285 */         retVal = this.req.getRemoteHost();
/* 286 */       } else if (!nameParts[1].equals("IDENT"))
/*     */       {
/* 288 */         if (nameParts[1].equals("PORT")) {
/* 289 */           retVal = Integer.toString(this.req.getRemotePort());
/* 290 */         } else if (nameParts[1].equals("USER"))
/* 291 */           retVal = this.req.getRemoteUser();
/*     */       }
/* 293 */     } else if (nameParts[0].equals("REQUEST")) {
/* 294 */       if (nameParts[1].equals("METHOD")) {
/* 295 */         retVal = this.req.getMethod();
/*     */       }
/* 297 */       else if (nameParts[1].equals("URI"))
/*     */       {
/* 299 */         retVal = (String)this.req.getAttribute("javax.servlet.forward.request_uri");
/*     */         
/* 301 */         if (retVal == null) retVal = this.req.getRequestURI();
/*     */       }
/* 303 */     } else if (nameParts[0].equals("SCRIPT")) {
/* 304 */       String scriptName = this.req.getServletPath();
/* 305 */       if (nameParts[1].equals("FILENAME")) {
/* 306 */         retVal = this.context.getRealPath(scriptName);
/*     */       }
/* 308 */       else if (nameParts[1].equals("NAME")) {
/* 309 */         retVal = scriptName;
/*     */       }
/* 311 */     } else if (nameParts[0].equals("SERVER")) {
/* 312 */       if (nameParts[1].equals("ADDR")) {
/* 313 */         retVal = this.req.getLocalAddr();
/*     */       }
/* 315 */       if (nameParts[1].equals("NAME")) {
/* 316 */         retVal = this.req.getServerName();
/* 317 */       } else if (nameParts[1].equals("PORT")) {
/* 318 */         retVal = Integer.toString(this.req.getServerPort());
/* 319 */       } else if (nameParts[1].equals("PROTOCOL")) {
/* 320 */         retVal = this.req.getProtocol();
/* 321 */       } else if (nameParts[1].equals("SOFTWARE")) {
/* 322 */         StringBuilder rv = new StringBuilder(this.context.getServerInfo());
/* 323 */         rv.append(" ");
/* 324 */         rv.append(System.getProperty("java.vm.name"));
/* 325 */         rv.append("/");
/* 326 */         rv.append(System.getProperty("java.vm.version"));
/* 327 */         rv.append(" ");
/* 328 */         rv.append(System.getProperty("os.name"));
/* 329 */         retVal = rv.toString();
/*     */       }
/* 331 */     } else if (name.equalsIgnoreCase("UNIQUE_ID")) {
/* 332 */       retVal = this.req.getRequestedSessionId();
/*     */     }
/* 334 */     if (requiredParts != nameParts.length) return null;
/* 335 */     return retVal;
/*     */   }
/*     */   
/*     */   public Date getCurrentDate()
/*     */   {
/* 340 */     return new Date();
/*     */   }
/*     */   
/*     */   protected String nullToEmptyString(String string)
/*     */   {
/* 345 */     String retVal = string;
/* 346 */     if (retVal == null) {
/* 347 */       retVal = "";
/*     */     }
/* 349 */     return retVal;
/*     */   }
/*     */   
/*     */   protected String getPathWithoutFileName(String servletPath)
/*     */   {
/* 354 */     String retVal = null;
/* 355 */     int lastSlash = servletPath.lastIndexOf('/');
/* 356 */     if (lastSlash >= 0)
/*     */     {
/* 358 */       retVal = servletPath.substring(0, lastSlash + 1);
/*     */     }
/* 360 */     return retVal;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getPathWithoutContext(String contextPath, String servletPath)
/*     */   {
/* 366 */     if (servletPath.startsWith(contextPath)) {
/* 367 */       return servletPath.substring(contextPath.length());
/*     */     }
/* 369 */     return servletPath;
/*     */   }
/*     */   
/*     */   protected String getAbsolutePath(String path) throws IOException
/*     */   {
/* 374 */     String pathWithoutContext = SSIServletRequestUtil.getRelativePath(this.req);
/* 375 */     String prefix = getPathWithoutFileName(pathWithoutContext);
/* 376 */     if (prefix == null) {
/* 377 */       throw new IOException("Couldn't remove filename from path: " + pathWithoutContext);
/*     */     }
/*     */     
/* 380 */     String fullPath = prefix + path;
/* 381 */     String retVal = RequestUtil.normalize(fullPath);
/* 382 */     if (retVal == null) {
/* 383 */       throw new IOException("Normalization yielded null on path: " + fullPath);
/*     */     }
/*     */     
/* 386 */     return retVal;
/*     */   }
/*     */   
/*     */   protected ServletContextAndPath getServletContextAndPathFromNonVirtualPath(String nonVirtualPath)
/*     */     throws IOException
/*     */   {
/* 392 */     if ((nonVirtualPath.startsWith("/")) || (nonVirtualPath.startsWith("\\"))) {
/* 393 */       throw new IOException("A non-virtual path can't be absolute: " + nonVirtualPath);
/*     */     }
/*     */     
/* 396 */     if (nonVirtualPath.indexOf("../") >= 0) {
/* 397 */       throw new IOException("A non-virtual path can't contain '../' : " + nonVirtualPath);
/*     */     }
/*     */     
/* 400 */     String path = getAbsolutePath(nonVirtualPath);
/* 401 */     ServletContextAndPath csAndP = new ServletContextAndPath(this.context, path);
/*     */     
/* 403 */     return csAndP;
/*     */   }
/*     */   
/*     */ 
/*     */   protected ServletContextAndPath getServletContextAndPathFromVirtualPath(String virtualPath)
/*     */     throws IOException
/*     */   {
/* 410 */     if ((!virtualPath.startsWith("/")) && (!virtualPath.startsWith("\\"))) {
/* 411 */       return new ServletContextAndPath(this.context, 
/* 412 */         getAbsolutePath(virtualPath));
/*     */     }
/*     */     
/* 415 */     String normalized = RequestUtil.normalize(virtualPath);
/* 416 */     if (this.isVirtualWebappRelative) {
/* 417 */       return new ServletContextAndPath(this.context, normalized);
/*     */     }
/*     */     
/* 420 */     ServletContext normContext = this.context.getContext(normalized);
/* 421 */     if (normContext == null) {
/* 422 */       throw new IOException("Couldn't get context for path: " + normalized);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 429 */     if (!isRootContext(normContext)) {
/* 430 */       String noContext = getPathWithoutContext(normContext
/* 431 */         .getContextPath(), normalized);
/* 432 */       if (noContext == null) {
/* 433 */         throw new IOException("Couldn't remove context from path: " + normalized);
/*     */       }
/*     */       
/*     */ 
/* 437 */       return new ServletContextAndPath(normContext, noContext);
/*     */     }
/*     */     
/* 440 */     return new ServletContextAndPath(normContext, normalized);
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
/*     */   protected boolean isRootContext(ServletContext servletContext)
/*     */   {
/* 453 */     return servletContext == servletContext.getContext("/");
/*     */   }
/*     */   
/*     */   protected ServletContextAndPath getServletContextAndPath(String originalPath, boolean virtual)
/*     */     throws IOException
/*     */   {
/* 459 */     ServletContextAndPath csAndP = null;
/* 460 */     if (this.debug > 0) {
/* 461 */       log("SSIServletExternalResolver.getServletContextAndPath( " + originalPath + ", " + virtual + ")", null);
/*     */     }
/*     */     
/* 464 */     if (virtual) {
/* 465 */       csAndP = getServletContextAndPathFromVirtualPath(originalPath);
/*     */     } else {
/* 467 */       csAndP = getServletContextAndPathFromNonVirtualPath(originalPath);
/*     */     }
/* 469 */     return csAndP;
/*     */   }
/*     */   
/*     */   protected URLConnection getURLConnection(String originalPath, boolean virtual)
/*     */     throws IOException
/*     */   {
/* 475 */     ServletContextAndPath csAndP = getServletContextAndPath(originalPath, virtual);
/*     */     
/* 477 */     ServletContext context = csAndP.getServletContext();
/* 478 */     String path = csAndP.getPath();
/* 479 */     URL url = context.getResource(path);
/* 480 */     if (url == null) {
/* 481 */       throw new IOException("Context did not contain resource: " + path);
/*     */     }
/* 483 */     URLConnection urlConnection = url.openConnection();
/* 484 */     return urlConnection;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getFileLastModified(String path, boolean virtual)
/*     */     throws IOException
/*     */   {
/* 491 */     long lastModified = 0L;
/*     */     try {
/* 493 */       URLConnection urlConnection = getURLConnection(path, virtual);
/* 494 */       lastModified = urlConnection.getLastModified();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/* 498 */     return lastModified;
/*     */   }
/*     */   
/*     */   public long getFileSize(String path, boolean virtual)
/*     */     throws IOException
/*     */   {
/* 504 */     long fileSize = -1L;
/*     */     try {
/* 506 */       URLConnection urlConnection = getURLConnection(path, virtual);
/* 507 */       fileSize = urlConnection.getContentLengthLong();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/* 511 */     return fileSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFileText(String originalPath, boolean virtual)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 522 */       ServletContextAndPath csAndP = getServletContextAndPath(originalPath, virtual);
/*     */       
/* 524 */       ServletContext context = csAndP.getServletContext();
/* 525 */       String path = csAndP.getPath();
/* 526 */       RequestDispatcher rd = context.getRequestDispatcher(path);
/* 527 */       if (rd == null) {
/* 528 */         throw new IOException("Couldn't get request dispatcher for path: " + path);
/*     */       }
/*     */       
/* 531 */       ByteArrayServletOutputStream basos = new ByteArrayServletOutputStream();
/*     */       
/* 533 */       ResponseIncludeWrapper responseIncludeWrapper = new ResponseIncludeWrapper(context, this.req, this.res, basos);
/*     */       
/* 535 */       rd.include(this.req, responseIncludeWrapper);
/*     */       
/* 537 */       responseIncludeWrapper.flushOutputStreamOrWriter();
/* 538 */       byte[] bytes = basos.toByteArray();
/*     */       
/*     */       String retVal;
/*     */       String retVal;
/* 542 */       if (this.inputEncoding == null) {
/* 543 */         retVal = new String(bytes);
/*     */       }
/*     */       else {
/* 546 */         retVal = new String(bytes, B2CConverter.getCharset(this.inputEncoding));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 553 */       if ((retVal.equals("")) && (!this.req.getMethod().equalsIgnoreCase("HEAD"))) {
/* 554 */         throw new IOException("Couldn't find file: " + path);
/*     */       }
/* 556 */       return retVal;
/*     */     }
/*     */     catch (ServletException e) {
/* 559 */       throw new IOException("Couldn't include file: " + originalPath + " because of ServletException: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ServletContextAndPath
/*     */   {
/*     */     protected final ServletContext servletContext;
/*     */     protected final String path;
/*     */     
/*     */     public ServletContextAndPath(ServletContext servletContext, String path)
/*     */     {
/* 570 */       this.servletContext = servletContext;
/* 571 */       this.path = path;
/*     */     }
/*     */     
/*     */     public ServletContext getServletContext()
/*     */     {
/* 576 */       return this.servletContext;
/*     */     }
/*     */     
/*     */     public String getPath()
/*     */     {
/* 581 */       return this.path;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\ssi\SSIServletExternalResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */