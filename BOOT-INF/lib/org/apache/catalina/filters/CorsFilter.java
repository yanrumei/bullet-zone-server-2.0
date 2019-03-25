/*      */ package org.apache.catalina.filters;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import javax.servlet.Filter;
/*      */ import javax.servlet.FilterChain;
/*      */ import javax.servlet.FilterConfig;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
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
/*      */ public class CorsFilter
/*      */   implements Filter
/*      */ {
/*   81 */   private static final Log log = LogFactory.getLog(CorsFilter.class);
/*   82 */   private static final StringManager sm = StringManager.getManager(CorsFilter.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   89 */   private final Collection<String> allowedOrigins = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean anyOriginAllowed;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  100 */   private final Collection<String> allowedHttpMethods = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */   private final Collection<String> allowedHttpHeaders = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  113 */   private final Collection<String> exposedHeaders = new HashSet();
/*      */   
/*      */   private boolean supportsCredentials;
/*      */   
/*      */   private long preflightMaxAge;
/*      */   private boolean decorateRequest;
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
/*      */   public static final String RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
/*      */   public static final String REQUEST_HEADER_VARY = "Vary";
/*      */   public static final String REQUEST_HEADER_ORIGIN = "Origin";
/*      */   public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
/*      */   public static final String REQUEST_HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
/*      */   public static final String HTTP_REQUEST_ATTRIBUTE_PREFIX = "cors.";
/*      */   public static final String HTTP_REQUEST_ATTRIBUTE_ORIGIN = "cors.request.origin";
/*      */   public static final String HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST = "cors.isCorsRequest";
/*      */   public static final String HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE = "cors.request.type";
/*      */   public static final String HTTP_REQUEST_ATTRIBUTE_REQUEST_HEADERS = "cors.request.headers";
/*      */   
/*      */   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
/*      */     throws IOException, ServletException
/*      */   {
/*  138 */     if ((!(servletRequest instanceof HttpServletRequest)) || (!(servletResponse instanceof HttpServletResponse)))
/*      */     {
/*  140 */       throw new ServletException(sm.getString("corsFilter.onlyHttp"));
/*      */     }
/*      */     
/*      */ 
/*  144 */     HttpServletRequest request = (HttpServletRequest)servletRequest;
/*  145 */     HttpServletResponse response = (HttpServletResponse)servletResponse;
/*      */     
/*      */ 
/*  148 */     CORSRequestType requestType = checkRequestType(request);
/*      */     
/*      */ 
/*  151 */     if (this.decorateRequest) {
/*  152 */       decorateCORSProperties(request, requestType);
/*      */     }
/*  154 */     switch (requestType)
/*      */     {
/*      */     case SIMPLE: 
/*  157 */       handleSimpleCORS(request, response, filterChain);
/*  158 */       break;
/*      */     
/*      */     case ACTUAL: 
/*  161 */       handleSimpleCORS(request, response, filterChain);
/*  162 */       break;
/*      */     
/*      */     case PRE_FLIGHT: 
/*  165 */       handlePreflightCORS(request, response, filterChain);
/*  166 */       break;
/*      */     
/*      */     case NOT_CORS: 
/*  169 */       handleNonCORS(request, response, filterChain);
/*  170 */       break;
/*      */     
/*      */     default: 
/*  173 */       handleInvalidCORS(request, response, filterChain);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   public void init(FilterConfig filterConfig)
/*      */     throws ServletException
/*      */   {
/*  181 */     parseAndStore(
/*  182 */       getInitParameter(filterConfig, "cors.allowed.origins", "*"), 
/*      */       
/*  184 */       getInitParameter(filterConfig, "cors.allowed.methods", "GET,POST,HEAD,OPTIONS"), 
/*      */       
/*  186 */       getInitParameter(filterConfig, "cors.allowed.headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers"), 
/*      */       
/*  188 */       getInitParameter(filterConfig, "cors.exposed.headers", ""), 
/*      */       
/*  190 */       getInitParameter(filterConfig, "cors.support.credentials", "true"), 
/*      */       
/*  192 */       getInitParameter(filterConfig, "cors.preflight.maxage", "1800"), 
/*      */       
/*  194 */       getInitParameter(filterConfig, "cors.request.decorate", "true"));
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
/*      */   private String getInitParameter(FilterConfig filterConfig, String name, String defaultValue)
/*      */   {
/*  216 */     if (filterConfig == null) {
/*  217 */       return defaultValue;
/*      */     }
/*      */     
/*  220 */     String value = filterConfig.getInitParameter(name);
/*  221 */     if (value != null) {
/*  222 */       return value;
/*      */     }
/*      */     
/*  225 */     return defaultValue;
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
/*      */   protected void handleSimpleCORS(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*      */     throws IOException, ServletException
/*      */   {
/*  244 */     CORSRequestType requestType = checkRequestType(request);
/*  245 */     if ((requestType != CORSRequestType.SIMPLE) && (requestType != CORSRequestType.ACTUAL))
/*      */     {
/*      */ 
/*  248 */       throw new IllegalArgumentException(sm.getString("corsFilter.wrongType2", new Object[] { CORSRequestType.SIMPLE, CORSRequestType.ACTUAL }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  254 */     String origin = request.getHeader("Origin");
/*  255 */     String method = request.getMethod();
/*      */     
/*      */ 
/*  258 */     if (!isOriginAllowed(origin)) {
/*  259 */       handleInvalidCORS(request, response, filterChain);
/*  260 */       return;
/*      */     }
/*      */     
/*  263 */     if (!this.allowedHttpMethods.contains(method)) {
/*  264 */       handleInvalidCORS(request, response, filterChain);
/*  265 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  270 */     if ((this.anyOriginAllowed) && (!this.supportsCredentials))
/*      */     {
/*      */ 
/*      */ 
/*  274 */       response.addHeader("Access-Control-Allow-Origin", "*");
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  281 */       response.addHeader("Access-Control-Allow-Origin", origin);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  290 */     if (this.supportsCredentials) {
/*  291 */       response.addHeader("Access-Control-Allow-Credentials", "true");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  300 */     if ((this.exposedHeaders != null) && (this.exposedHeaders.size() > 0)) {
/*  301 */       String exposedHeadersString = join(this.exposedHeaders, ",");
/*  302 */       response.addHeader("Access-Control-Expose-Headers", exposedHeadersString);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  308 */     response.addHeader("Vary", "Origin");
/*      */     
/*      */ 
/*      */ 
/*  312 */     filterChain.doFilter(request, response);
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
/*      */   protected void handlePreflightCORS(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*      */     throws IOException, ServletException
/*      */   {
/*  329 */     CORSRequestType requestType = checkRequestType(request);
/*  330 */     if (requestType != CORSRequestType.PRE_FLIGHT) {
/*  331 */       throw new IllegalArgumentException(sm.getString("corsFilter.wrongType1", new Object[] {CORSRequestType.PRE_FLIGHT
/*  332 */         .name().toLowerCase(Locale.ENGLISH) }));
/*      */     }
/*      */     
/*      */ 
/*  336 */     String origin = request.getHeader("Origin");
/*      */     
/*      */ 
/*  339 */     if (!isOriginAllowed(origin)) {
/*  340 */       handleInvalidCORS(request, response, filterChain);
/*  341 */       return;
/*      */     }
/*      */     
/*      */ 
/*  345 */     String accessControlRequestMethod = request.getHeader("Access-Control-Request-Method");
/*      */     
/*  347 */     if (accessControlRequestMethod == null) {
/*  348 */       handleInvalidCORS(request, response, filterChain);
/*  349 */       return;
/*      */     }
/*  351 */     accessControlRequestMethod = accessControlRequestMethod.trim();
/*      */     
/*      */ 
/*      */ 
/*  355 */     String accessControlRequestHeadersHeader = request.getHeader("Access-Control-Request-Headers");
/*      */     
/*  357 */     List<String> accessControlRequestHeaders = new LinkedList();
/*  358 */     String[] headers; if ((accessControlRequestHeadersHeader != null) && 
/*  359 */       (!accessControlRequestHeadersHeader.trim().isEmpty())) {
/*  360 */       headers = accessControlRequestHeadersHeader.trim().split(",");
/*      */       
/*  362 */       for (String header : headers) {
/*  363 */         accessControlRequestHeaders.add(header.trim().toLowerCase(Locale.ENGLISH));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  368 */     if (!this.allowedHttpMethods.contains(accessControlRequestMethod)) {
/*  369 */       handleInvalidCORS(request, response, filterChain);
/*  370 */       return;
/*      */     }
/*      */     
/*      */ 
/*  374 */     if (!accessControlRequestHeaders.isEmpty()) {
/*  375 */       for (String header : accessControlRequestHeaders) {
/*  376 */         if (!this.allowedHttpHeaders.contains(header)) {
/*  377 */           handleInvalidCORS(request, response, filterChain);
/*  378 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  384 */     if (this.supportsCredentials) {
/*  385 */       response.addHeader("Access-Control-Allow-Origin", origin);
/*      */       
/*      */ 
/*  388 */       response.addHeader("Access-Control-Allow-Credentials", "true");
/*      */ 
/*      */ 
/*      */     }
/*  392 */     else if (this.anyOriginAllowed) {
/*  393 */       response.addHeader("Access-Control-Allow-Origin", "*");
/*      */     }
/*      */     else
/*      */     {
/*  397 */       response.addHeader("Access-Control-Allow-Origin", origin);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  404 */     if (this.preflightMaxAge > 0L) {
/*  405 */       response.addHeader("Access-Control-Max-Age", 
/*      */       
/*  407 */         String.valueOf(this.preflightMaxAge));
/*      */     }
/*      */     
/*      */ 
/*  411 */     response.addHeader("Access-Control-Allow-Methods", accessControlRequestMethod);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  416 */     if ((this.allowedHttpHeaders != null) && (!this.allowedHttpHeaders.isEmpty())) {
/*  417 */       response.addHeader("Access-Control-Allow-Headers", 
/*      */       
/*  419 */         join(this.allowedHttpHeaders, ","));
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
/*      */   private void handleNonCORS(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*      */     throws IOException, ServletException
/*      */   {
/*  441 */     filterChain.doFilter(request, response);
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
/*      */   private void handleInvalidCORS(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*      */   {
/*  454 */     String origin = request.getHeader("Origin");
/*  455 */     String method = request.getMethod();
/*  456 */     String accessControlRequestHeaders = request.getHeader("Access-Control-Request-Headers");
/*      */     
/*      */ 
/*  459 */     response.setContentType("text/plain");
/*  460 */     response.setStatus(403);
/*  461 */     response.resetBuffer();
/*      */     
/*  463 */     if (log.isDebugEnabled())
/*      */     {
/*  465 */       StringBuilder message = new StringBuilder("Invalid CORS request; Origin=");
/*      */       
/*  467 */       message.append(origin);
/*  468 */       message.append(";Method=");
/*  469 */       message.append(method);
/*  470 */       if (accessControlRequestHeaders != null) {
/*  471 */         message.append(";Access-Control-Request-Headers=");
/*  472 */         message.append(accessControlRequestHeaders);
/*      */       }
/*  474 */       log.debug(message.toString());
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
/*      */   public void destroy() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static void decorateCORSProperties(HttpServletRequest request, CORSRequestType corsRequestType)
/*      */   {
/*  505 */     if (request == null)
/*      */     {
/*  507 */       throw new IllegalArgumentException(sm.getString("corsFilter.nullRequest"));
/*      */     }
/*      */     
/*  510 */     if (corsRequestType == null)
/*      */     {
/*  512 */       throw new IllegalArgumentException(sm.getString("corsFilter.nullRequestType"));
/*      */     }
/*      */     
/*  515 */     switch (corsRequestType) {
/*      */     case SIMPLE: 
/*  517 */       request.setAttribute("cors.isCorsRequest", Boolean.TRUE);
/*      */       
/*      */ 
/*  520 */       request.setAttribute("cors.request.origin", request
/*  521 */         .getHeader("Origin"));
/*  522 */       request.setAttribute("cors.request.type", corsRequestType
/*      */       
/*  524 */         .name().toLowerCase(Locale.ENGLISH));
/*  525 */       break;
/*      */     case ACTUAL: 
/*  527 */       request.setAttribute("cors.isCorsRequest", Boolean.TRUE);
/*      */       
/*      */ 
/*  530 */       request.setAttribute("cors.request.origin", request
/*  531 */         .getHeader("Origin"));
/*  532 */       request.setAttribute("cors.request.type", corsRequestType
/*      */       
/*  534 */         .name().toLowerCase(Locale.ENGLISH));
/*  535 */       break;
/*      */     case PRE_FLIGHT: 
/*  537 */       request.setAttribute("cors.isCorsRequest", Boolean.TRUE);
/*      */       
/*      */ 
/*  540 */       request.setAttribute("cors.request.origin", request
/*  541 */         .getHeader("Origin"));
/*  542 */       request.setAttribute("cors.request.type", corsRequestType
/*      */       
/*  544 */         .name().toLowerCase(Locale.ENGLISH));
/*  545 */       String headers = request.getHeader("Access-Control-Request-Headers");
/*      */       
/*  547 */       if (headers == null) {
/*  548 */         headers = "";
/*      */       }
/*  550 */       request.setAttribute("cors.request.headers", headers);
/*      */       
/*  552 */       break;
/*      */     case NOT_CORS: 
/*  554 */       request.setAttribute("cors.isCorsRequest", Boolean.FALSE);
/*      */       
/*      */ 
/*  557 */       break;
/*      */     }
/*      */     
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
/*      */   protected static String join(Collection<String> elements, String joinSeparator)
/*      */   {
/*  576 */     String separator = ",";
/*  577 */     if (elements == null) {
/*  578 */       return null;
/*      */     }
/*  580 */     if (joinSeparator != null) {
/*  581 */       separator = joinSeparator;
/*      */     }
/*  583 */     StringBuilder buffer = new StringBuilder();
/*  584 */     boolean isFirst = true;
/*  585 */     for (String element : elements) {
/*  586 */       if (!isFirst) {
/*  587 */         buffer.append(separator);
/*      */       } else {
/*  589 */         isFirst = false;
/*      */       }
/*      */       
/*  592 */       if (element != null) {
/*  593 */         buffer.append(element);
/*      */       }
/*      */     }
/*      */     
/*  597 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CORSRequestType checkRequestType(HttpServletRequest request)
/*      */   {
/*  608 */     CORSRequestType requestType = CORSRequestType.INVALID_CORS;
/*  609 */     if (request == null)
/*      */     {
/*  611 */       throw new IllegalArgumentException(sm.getString("corsFilter.nullRequest"));
/*      */     }
/*  613 */     String originHeader = request.getHeader("Origin");
/*      */     
/*  615 */     if (originHeader != null) {
/*  616 */       if (originHeader.isEmpty()) {
/*  617 */         requestType = CORSRequestType.INVALID_CORS;
/*  618 */       } else if (!isValidOrigin(originHeader)) {
/*  619 */         requestType = CORSRequestType.INVALID_CORS;
/*  620 */       } else { if (isLocalOrigin(request, originHeader)) {
/*  621 */           return CORSRequestType.NOT_CORS;
/*      */         }
/*  623 */         String method = request.getMethod();
/*  624 */         if (method != null) {
/*  625 */           if ("OPTIONS".equals(method))
/*      */           {
/*  627 */             String accessControlRequestMethodHeader = request.getHeader("Access-Control-Request-Method");
/*      */             
/*  629 */             if ((accessControlRequestMethodHeader != null) && 
/*  630 */               (!accessControlRequestMethodHeader.isEmpty())) {
/*  631 */               requestType = CORSRequestType.PRE_FLIGHT;
/*  632 */             } else if ((accessControlRequestMethodHeader != null) && 
/*  633 */               (accessControlRequestMethodHeader.isEmpty())) {
/*  634 */               requestType = CORSRequestType.INVALID_CORS;
/*      */             } else {
/*  636 */               requestType = CORSRequestType.ACTUAL;
/*      */             }
/*  638 */           } else if (("GET".equals(method)) || ("HEAD".equals(method))) {
/*  639 */             requestType = CORSRequestType.SIMPLE;
/*  640 */           } else if ("POST".equals(method)) {
/*  641 */             String mediaType = getMediaType(request.getContentType());
/*  642 */             if (mediaType != null)
/*      */             {
/*  644 */               if (SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES.contains(mediaType)) {
/*  645 */                 requestType = CORSRequestType.SIMPLE;
/*      */               } else {
/*  647 */                 requestType = CORSRequestType.ACTUAL;
/*      */               }
/*      */             }
/*      */           } else {
/*  651 */             requestType = CORSRequestType.ACTUAL;
/*      */           }
/*      */         }
/*      */       }
/*      */     } else {
/*  656 */       requestType = CORSRequestType.NOT_CORS;
/*      */     }
/*      */     
/*  659 */     return requestType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean isLocalOrigin(HttpServletRequest request, String origin)
/*      */   {
/*  666 */     StringBuilder target = new StringBuilder();
/*  667 */     String scheme = request.getScheme();
/*  668 */     if (scheme == null) {
/*  669 */       return false;
/*      */     }
/*  671 */     scheme = scheme.toLowerCase(Locale.ENGLISH);
/*      */     
/*  673 */     target.append(scheme);
/*  674 */     target.append("://");
/*      */     
/*  676 */     String host = request.getServerName();
/*  677 */     if (host == null) {
/*  678 */       return false;
/*      */     }
/*  680 */     target.append(host);
/*      */     
/*  682 */     int port = request.getServerPort();
/*  683 */     if ((("http".equals(scheme)) && (port != 80)) || (
/*  684 */       ("https".equals(scheme)) && (port != 443))) {
/*  685 */       target.append(':');
/*  686 */       target.append(port);
/*      */     }
/*      */     
/*  689 */     return origin.equalsIgnoreCase(target.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getMediaType(String contentType)
/*      */   {
/*  698 */     if (contentType == null) {
/*  699 */       return null;
/*      */     }
/*  701 */     String result = contentType.toLowerCase(Locale.ENGLISH);
/*  702 */     int firstSemiColonIndex = result.indexOf(';');
/*  703 */     if (firstSemiColonIndex > -1) {
/*  704 */       result = result.substring(0, firstSemiColonIndex);
/*      */     }
/*  706 */     result = result.trim();
/*  707 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isOriginAllowed(String origin)
/*      */   {
/*  719 */     if (this.anyOriginAllowed) {
/*  720 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  725 */     return this.allowedOrigins.contains(origin);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseAndStore(String allowedOrigins, String allowedHttpMethods, String allowedHttpHeaders, String exposedHeaders, String supportsCredentials, String preflightMaxAge, String decorateRequest)
/*      */     throws ServletException
/*      */   {
/*  755 */     if (allowedOrigins.trim().equals("*")) {
/*  756 */       this.anyOriginAllowed = true;
/*      */     } else {
/*  758 */       this.anyOriginAllowed = false;
/*  759 */       Set<String> setAllowedOrigins = parseStringToSet(allowedOrigins);
/*  760 */       this.allowedOrigins.clear();
/*  761 */       this.allowedOrigins.addAll(setAllowedOrigins);
/*      */     }
/*      */     
/*  764 */     Set<String> setAllowedHttpMethods = parseStringToSet(allowedHttpMethods);
/*  765 */     this.allowedHttpMethods.clear();
/*  766 */     this.allowedHttpMethods.addAll(setAllowedHttpMethods);
/*      */     
/*  768 */     Set<String> setAllowedHttpHeaders = parseStringToSet(allowedHttpHeaders);
/*  769 */     Set<String> lowerCaseHeaders = new HashSet();
/*  770 */     for (String header : setAllowedHttpHeaders) {
/*  771 */       String lowerCase = header.toLowerCase(Locale.ENGLISH);
/*  772 */       lowerCaseHeaders.add(lowerCase);
/*      */     }
/*  774 */     this.allowedHttpHeaders.clear();
/*  775 */     this.allowedHttpHeaders.addAll(lowerCaseHeaders);
/*      */     
/*  777 */     Object setExposedHeaders = parseStringToSet(exposedHeaders);
/*  778 */     this.exposedHeaders.clear();
/*  779 */     this.exposedHeaders.addAll((Collection)setExposedHeaders);
/*      */     
/*      */ 
/*  782 */     this.supportsCredentials = Boolean.parseBoolean(supportsCredentials);
/*      */     try
/*      */     {
/*  785 */       if (!preflightMaxAge.isEmpty()) {
/*  786 */         this.preflightMaxAge = Long.parseLong(preflightMaxAge);
/*      */       } else {
/*  788 */         this.preflightMaxAge = 0L;
/*      */       }
/*      */     }
/*      */     catch (NumberFormatException e) {
/*  792 */       throw new ServletException(sm.getString("corsFilter.invalidPreflightMaxAge"), e);
/*      */     }
/*      */     
/*      */ 
/*  796 */     this.decorateRequest = Boolean.parseBoolean(decorateRequest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Set<String> parseStringToSet(String data)
/*      */   {
/*      */     String[] splits;
/*      */     
/*      */ 
/*      */     String[] splits;
/*      */     
/*      */ 
/*  809 */     if ((data != null) && (data.length() > 0)) {
/*  810 */       splits = data.split(",");
/*      */     } else {
/*  812 */       splits = new String[0];
/*      */     }
/*      */     
/*  815 */     Set<String> set = new HashSet();
/*  816 */     if (splits.length > 0) {
/*  817 */       for (String split : splits) {
/*  818 */         set.add(split.trim());
/*      */       }
/*      */     }
/*      */     
/*  822 */     return set;
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
/*      */   protected static boolean isValidOrigin(String origin)
/*      */   {
/*  840 */     if (origin.contains("%")) {
/*  841 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  845 */     if ("null".equals(origin)) {
/*  846 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  852 */     if (origin.startsWith("file://")) {
/*  853 */       return true;
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  858 */       originURI = new URI(origin);
/*      */     } catch (URISyntaxException e) { URI originURI;
/*  860 */       return false;
/*      */     }
/*      */     URI originURI;
/*  863 */     return originURI.getScheme() != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnyOriginAllowed()
/*      */   {
/*  874 */     return this.anyOriginAllowed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<String> getExposedHeaders()
/*      */   {
/*  884 */     return this.exposedHeaders;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSupportsCredentials()
/*      */   {
/*  895 */     return this.supportsCredentials;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getPreflightMaxAge()
/*      */   {
/*  905 */     return this.preflightMaxAge;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<String> getAllowedOrigins()
/*      */   {
/*  916 */     return this.allowedOrigins;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<String> getAllowedHttpMethods()
/*      */   {
/*  926 */     return this.allowedHttpMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<String> getAllowedHttpHeaders()
/*      */   {
/*  936 */     return this.allowedHttpHeaders;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static enum CORSRequestType
/*      */   {
/* 1056 */     SIMPLE, 
/*      */     
/*      */ 
/*      */ 
/* 1060 */     ACTUAL, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1065 */     PRE_FLIGHT, 
/*      */     
/*      */ 
/*      */ 
/* 1069 */     NOT_CORS, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1074 */     INVALID_CORS;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private CORSRequestType() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1085 */   public static final Collection<String> SIMPLE_HTTP_REQUEST_CONTENT_TYPE_VALUES = new HashSet(
/* 1086 */     Arrays.asList(new String[] { "application/x-www-form-urlencoded", "multipart/form-data", "text/plain" }));
/*      */   public static final String DEFAULT_ALLOWED_ORIGINS = "*";
/*      */   public static final String DEFAULT_ALLOWED_HTTP_METHODS = "GET,POST,HEAD,OPTIONS";
/*      */   public static final String DEFAULT_PREFLIGHT_MAXAGE = "1800";
/*      */   public static final String DEFAULT_SUPPORTS_CREDENTIALS = "true";
/*      */   public static final String DEFAULT_ALLOWED_HTTP_HEADERS = "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers";
/*      */   public static final String DEFAULT_EXPOSED_HEADERS = "";
/*      */   public static final String DEFAULT_DECORATE_REQUEST = "true";
/*      */   public static final String PARAM_CORS_ALLOWED_ORIGINS = "cors.allowed.origins";
/*      */   public static final String PARAM_CORS_SUPPORT_CREDENTIALS = "cors.support.credentials";
/*      */   public static final String PARAM_CORS_EXPOSED_HEADERS = "cors.exposed.headers";
/*      */   public static final String PARAM_CORS_ALLOWED_HEADERS = "cors.allowed.headers";
/*      */   public static final String PARAM_CORS_ALLOWED_METHODS = "cors.allowed.methods";
/*      */   public static final String PARAM_CORS_PREFLIGHT_MAXAGE = "cors.preflight.maxage";
/*      */   public static final String PARAM_CORS_REQUEST_DECORATE = "cors.request.decorate";
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\CorsFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */