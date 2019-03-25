/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class UrlPathHelper
/*     */ {
/*     */   private static final String WEBSPHERE_URI_ATTRIBUTE = "com.ibm.websphere.servlet.uri_non_decoded";
/*  57 */   private static final Log logger = LogFactory.getLog(UrlPathHelper.class);
/*     */   
/*     */ 
/*     */   static volatile Boolean websphereComplianceFlag;
/*     */   
/*  62 */   private boolean alwaysUseFullPath = false;
/*     */   
/*  64 */   private boolean urlDecode = true;
/*     */   
/*  66 */   private boolean removeSemicolonContent = true;
/*     */   
/*  68 */   private String defaultEncoding = "ISO-8859-1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*     */   {
/*  78 */     this.alwaysUseFullPath = alwaysUseFullPath;
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
/*     */   public void setUrlDecode(boolean urlDecode)
/*     */   {
/*  96 */     this.urlDecode = urlDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUrlDecode()
/*     */   {
/* 104 */     return this.urlDecode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent)
/*     */   {
/* 112 */     this.removeSemicolonContent = removeSemicolonContent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean shouldRemoveSemicolonContent()
/*     */   {
/* 119 */     return this.removeSemicolonContent;
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
/*     */   public void setDefaultEncoding(String defaultEncoding)
/*     */   {
/* 136 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getDefaultEncoding()
/*     */   {
/* 143 */     return this.defaultEncoding;
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
/*     */   public String getLookupPathForRequest(HttpServletRequest request)
/*     */   {
/* 158 */     if (this.alwaysUseFullPath) {
/* 159 */       return getPathWithinApplication(request);
/*     */     }
/*     */     
/* 162 */     String rest = getPathWithinServletMapping(request);
/* 163 */     if (!"".equals(rest)) {
/* 164 */       return rest;
/*     */     }
/*     */     
/* 167 */     return getPathWithinApplication(request);
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
/*     */   public String getPathWithinServletMapping(HttpServletRequest request)
/*     */   {
/* 185 */     String pathWithinApp = getPathWithinApplication(request);
/* 186 */     String servletPath = getServletPath(request);
/* 187 */     String sanitizedPathWithinApp = getSanitizedPath(pathWithinApp);
/*     */     
/*     */     String path;
/*     */     String path;
/* 191 */     if (servletPath.contains(sanitizedPathWithinApp)) {
/* 192 */       path = getRemainingPath(sanitizedPathWithinApp, servletPath, false);
/*     */     }
/*     */     else {
/* 195 */       path = getRemainingPath(pathWithinApp, servletPath, false);
/*     */     }
/*     */     
/* 198 */     if (path != null)
/*     */     {
/* 200 */       return path;
/*     */     }
/*     */     
/*     */ 
/* 204 */     String pathInfo = request.getPathInfo();
/* 205 */     if (pathInfo != null)
/*     */     {
/*     */ 
/* 208 */       return pathInfo;
/*     */     }
/* 210 */     if (!this.urlDecode)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 215 */       path = getRemainingPath(decodeInternal(request, pathWithinApp), servletPath, false);
/* 216 */       if (path != null) {
/* 217 */         return pathWithinApp;
/*     */       }
/*     */     }
/*     */     
/* 221 */     return servletPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathWithinApplication(HttpServletRequest request)
/*     */   {
/* 232 */     String contextPath = getContextPath(request);
/* 233 */     String requestUri = getRequestUri(request);
/* 234 */     String path = getRemainingPath(requestUri, contextPath, true);
/* 235 */     if (path != null)
/*     */     {
/* 237 */       return StringUtils.hasText(path) ? path : "/";
/*     */     }
/*     */     
/* 240 */     return requestUri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getRemainingPath(String requestUri, String mapping, boolean ignoreCase)
/*     */   {
/* 251 */     int index1 = 0;
/* 252 */     for (int index2 = 0; 
/* 253 */         (index1 < requestUri.length()) && (index2 < mapping.length()); index2++) {
/* 254 */       char c1 = requestUri.charAt(index1);
/* 255 */       char c2 = mapping.charAt(index2);
/* 256 */       if (c1 == ';') {
/* 257 */         index1 = requestUri.indexOf('/', index1);
/* 258 */         if (index1 == -1) {
/* 259 */           return null;
/*     */         }
/* 261 */         c1 = requestUri.charAt(index1);
/*     */       }
/* 263 */       if (c1 != c2)
/*     */       {
/*     */ 
/* 266 */         if ((!ignoreCase) || (Character.toLowerCase(c1) != Character.toLowerCase(c2)))
/*     */         {
/*     */ 
/* 269 */           return null;
/*     */         }
/*     */       }
/* 253 */       index1++;
/*     */     }
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
/* 271 */     if (index2 != mapping.length()) {
/* 272 */       return null;
/*     */     }
/* 274 */     if (index1 == requestUri.length()) {
/* 275 */       return "";
/*     */     }
/* 277 */     if (requestUri.charAt(index1) == ';') {
/* 278 */       index1 = requestUri.indexOf('/', index1);
/*     */     }
/* 280 */     return index1 != -1 ? requestUri.substring(index1) : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getSanitizedPath(String path)
/*     */   {
/* 290 */     String sanitized = path;
/*     */     for (;;) {
/* 292 */       int index = sanitized.indexOf("//");
/* 293 */       if (index < 0) {
/*     */         break;
/*     */       }
/*     */       
/* 297 */       sanitized = sanitized.substring(0, index) + sanitized.substring(index + 1);
/*     */     }
/*     */     
/* 300 */     return sanitized;
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
/*     */   public String getRequestUri(HttpServletRequest request)
/*     */   {
/* 315 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/* 316 */     if (uri == null) {
/* 317 */       uri = request.getRequestURI();
/*     */     }
/* 319 */     return decodeAndCleanUriString(request, uri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextPath(HttpServletRequest request)
/*     */   {
/* 331 */     String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
/* 332 */     if (contextPath == null) {
/* 333 */       contextPath = request.getContextPath();
/*     */     }
/* 335 */     if ("/".equals(contextPath))
/*     */     {
/* 337 */       contextPath = "";
/*     */     }
/* 339 */     return decodeRequestString(request, contextPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServletPath(HttpServletRequest request)
/*     */   {
/* 351 */     String servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
/* 352 */     if (servletPath == null) {
/* 353 */       servletPath = request.getServletPath();
/*     */     }
/* 355 */     if ((servletPath.length() > 1) && (servletPath.endsWith("/")) && (shouldRemoveTrailingServletPathSlash(request)))
/*     */     {
/*     */ 
/*     */ 
/* 359 */       servletPath = servletPath.substring(0, servletPath.length() - 1);
/*     */     }
/* 361 */     return servletPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginatingRequestUri(HttpServletRequest request)
/*     */   {
/* 370 */     String uri = (String)request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded");
/* 371 */     if (uri == null) {
/* 372 */       uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
/* 373 */       if (uri == null) {
/* 374 */         uri = request.getRequestURI();
/*     */       }
/*     */     }
/* 377 */     return decodeAndCleanUriString(request, uri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginatingContextPath(HttpServletRequest request)
/*     */   {
/* 389 */     String contextPath = (String)request.getAttribute("javax.servlet.forward.context_path");
/* 390 */     if (contextPath == null) {
/* 391 */       contextPath = request.getContextPath();
/*     */     }
/* 393 */     return decodeRequestString(request, contextPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginatingServletPath(HttpServletRequest request)
/*     */   {
/* 403 */     String servletPath = (String)request.getAttribute("javax.servlet.forward.servlet_path");
/* 404 */     if (servletPath == null) {
/* 405 */       servletPath = request.getServletPath();
/*     */     }
/* 407 */     return servletPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginatingQueryString(HttpServletRequest request)
/*     */   {
/* 417 */     if ((request.getAttribute("javax.servlet.forward.request_uri") != null) || 
/* 418 */       (request.getAttribute("javax.servlet.error.request_uri") != null)) {
/* 419 */       return (String)request.getAttribute("javax.servlet.forward.query_string");
/*     */     }
/*     */     
/* 422 */     return request.getQueryString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String decodeAndCleanUriString(HttpServletRequest request, String uri)
/*     */   {
/* 430 */     uri = removeSemicolonContent(uri);
/* 431 */     uri = decodeRequestString(request, uri);
/* 432 */     uri = getSanitizedPath(uri);
/* 433 */     return uri;
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
/*     */   public String decodeRequestString(HttpServletRequest request, String source)
/*     */   {
/* 449 */     if ((this.urlDecode) && (source != null)) {
/* 450 */       return decodeInternal(request, source);
/*     */     }
/* 452 */     return source;
/*     */   }
/*     */   
/*     */   private String decodeInternal(HttpServletRequest request, String source)
/*     */   {
/* 457 */     String enc = determineEncoding(request);
/*     */     try {
/* 459 */       return UriUtils.decode(source, enc);
/*     */     }
/*     */     catch (UnsupportedEncodingException ex) {
/* 462 */       if (logger.isWarnEnabled())
/* 463 */         logger.warn("Could not decode request string [" + source + "] with encoding '" + enc + "': falling back to platform default encoding; exception message: " + ex
/* 464 */           .getMessage());
/*     */     }
/* 466 */     return URLDecoder.decode(source);
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
/*     */   protected String determineEncoding(HttpServletRequest request)
/*     */   {
/* 481 */     String enc = request.getCharacterEncoding();
/* 482 */     if (enc == null) {
/* 483 */       enc = getDefaultEncoding();
/*     */     }
/* 485 */     return enc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String removeSemicolonContent(String requestUri)
/*     */   {
/* 496 */     return this.removeSemicolonContent ? 
/* 497 */       removeSemicolonContentInternal(requestUri) : removeJsessionid(requestUri);
/*     */   }
/*     */   
/*     */   private String removeSemicolonContentInternal(String requestUri) {
/* 501 */     int semicolonIndex = requestUri.indexOf(';');
/* 502 */     while (semicolonIndex != -1) {
/* 503 */       int slashIndex = requestUri.indexOf('/', semicolonIndex);
/* 504 */       String start = requestUri.substring(0, semicolonIndex);
/* 505 */       requestUri = slashIndex != -1 ? start + requestUri.substring(slashIndex) : start;
/* 506 */       semicolonIndex = requestUri.indexOf(';', semicolonIndex);
/*     */     }
/* 508 */     return requestUri;
/*     */   }
/*     */   
/*     */   private String removeJsessionid(String requestUri) {
/* 512 */     int startIndex = requestUri.toLowerCase().indexOf(";jsessionid=");
/* 513 */     if (startIndex != -1) {
/* 514 */       int endIndex = requestUri.indexOf(';', startIndex + 12);
/* 515 */       String start = requestUri.substring(0, startIndex);
/* 516 */       requestUri = endIndex != -1 ? start + requestUri.substring(endIndex) : start;
/*     */     }
/* 518 */     return requestUri;
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
/*     */   public Map<String, String> decodePathVariables(HttpServletRequest request, Map<String, String> vars)
/*     */   {
/* 533 */     if (this.urlDecode) {
/* 534 */       return vars;
/*     */     }
/*     */     
/* 537 */     Map<String, String> decodedVars = new LinkedHashMap(vars.size());
/* 538 */     for (Map.Entry<String, String> entry : vars.entrySet()) {
/* 539 */       decodedVars.put(entry.getKey(), decodeInternal(request, (String)entry.getValue()));
/*     */     }
/* 541 */     return decodedVars;
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
/*     */   public MultiValueMap<String, String> decodeMatrixVariables(HttpServletRequest request, MultiValueMap<String, String> vars)
/*     */   {
/* 557 */     if (this.urlDecode) {
/* 558 */       return vars;
/*     */     }
/*     */     
/* 561 */     MultiValueMap<String, String> decodedVars = new LinkedMultiValueMap(vars.size());
/* 562 */     for (Iterator localIterator1 = vars.keySet().iterator(); localIterator1.hasNext();) { key = (String)localIterator1.next();
/* 563 */       for (String value : (List)vars.get(key))
/* 564 */         decodedVars.add(key, decodeInternal(request, value));
/*     */     }
/*     */     String key;
/* 567 */     return decodedVars;
/*     */   }
/*     */   
/*     */   private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request)
/*     */   {
/* 572 */     if (request.getAttribute("com.ibm.websphere.servlet.uri_non_decoded") == null)
/*     */     {
/*     */ 
/*     */ 
/* 576 */       return false;
/*     */     }
/* 578 */     if (websphereComplianceFlag == null) {
/* 579 */       ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
/* 580 */       String className = "com.ibm.ws.webcontainer.WebContainer";
/* 581 */       String methodName = "getWebContainerProperties";
/* 582 */       String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
/* 583 */       boolean flag = false;
/*     */       try {
/* 585 */         Class<?> cl = classLoader.loadClass(className);
/* 586 */         Properties prop = (Properties)cl.getMethod(methodName, new Class[0]).invoke(null, new Object[0]);
/* 587 */         flag = Boolean.parseBoolean(prop.getProperty(propName));
/*     */       }
/*     */       catch (Throwable ex) {
/* 590 */         if (logger.isDebugEnabled()) {
/* 591 */           logger.debug("Could not introspect WebSphere web container properties: " + ex);
/*     */         }
/*     */       }
/* 594 */       websphereComplianceFlag = Boolean.valueOf(flag);
/*     */     }
/*     */     
/*     */ 
/* 598 */     return !websphereComplianceFlag.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\UrlPathHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */