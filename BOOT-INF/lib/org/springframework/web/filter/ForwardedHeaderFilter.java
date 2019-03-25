/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class ForwardedHeaderFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  67 */   private static final Set<String> FORWARDED_HEADER_NAMES = Collections.newSetFromMap(new LinkedCaseInsensitiveMap(5, Locale.ENGLISH));
/*     */   private final UrlPathHelper pathHelper;
/*     */   
/*  70 */   static { FORWARDED_HEADER_NAMES.add("Forwarded");
/*  71 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Host");
/*  72 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Port");
/*  73 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Proto");
/*  74 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Prefix");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ForwardedHeaderFilter()
/*     */   {
/*  86 */     this.pathHelper = new UrlPathHelper();
/*  87 */     this.pathHelper.setUrlDecode(false);
/*  88 */     this.pathHelper.setRemoveSemicolonContent(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoveOnly(boolean removeOnly)
/*     */   {
/*  99 */     this.removeOnly = removeOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean removeOnly;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean relativeRedirects;
/*     */   
/*     */ 
/*     */   public void setRelativeRedirects(boolean relativeRedirects)
/*     */   {
/* 114 */     this.relativeRedirects = relativeRedirects;
/*     */   }
/*     */   
/*     */   protected boolean shouldNotFilter(HttpServletRequest request)
/*     */     throws ServletException
/*     */   {
/* 120 */     Enumeration<String> names = request.getHeaderNames();
/* 121 */     while (names.hasMoreElements()) {
/* 122 */       String name = (String)names.nextElement();
/* 123 */       if (FORWARDED_HEADER_NAMES.contains(name)) {
/* 124 */         return false;
/*     */       }
/*     */     }
/* 127 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch()
/*     */   {
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldNotFilterErrorDispatch()
/*     */   {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*     */     throws ServletException, IOException
/*     */   {
/* 144 */     if (this.removeOnly) {
/* 145 */       ForwardedHeaderRemovingRequest theRequest = new ForwardedHeaderRemovingRequest(request);
/* 146 */       filterChain.doFilter(theRequest, response);
/*     */     }
/*     */     else {
/* 149 */       HttpServletRequest theRequest = new ForwardedHeaderExtractingRequest(request, this.pathHelper);
/*     */       
/* 151 */       HttpServletResponse theResponse = this.relativeRedirects ? RelativeRedirectResponseWrapper.wrapIfNecessary(response, HttpStatus.SEE_OTHER) : new ForwardedHeaderExtractingResponse(response, theRequest);
/*     */       
/* 153 */       filterChain.doFilter(theRequest, theResponse);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ForwardedHeaderRemovingRequest
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private final Map<String, List<String>> headers;
/*     */     
/*     */ 
/*     */     public ForwardedHeaderRemovingRequest(HttpServletRequest request)
/*     */     {
/* 166 */       super();
/* 167 */       this.headers = initHeaders(request);
/*     */     }
/*     */     
/*     */     private static Map<String, List<String>> initHeaders(HttpServletRequest request) {
/* 171 */       Map<String, List<String>> headers = new LinkedCaseInsensitiveMap(Locale.ENGLISH);
/* 172 */       Enumeration<String> names = request.getHeaderNames();
/* 173 */       while (names.hasMoreElements()) {
/* 174 */         String name = (String)names.nextElement();
/* 175 */         if (!ForwardedHeaderFilter.FORWARDED_HEADER_NAMES.contains(name)) {
/* 176 */           headers.put(name, Collections.list(request.getHeaders(name)));
/*     */         }
/*     */       }
/* 179 */       return headers;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String getHeader(String name)
/*     */     {
/* 186 */       List<String> value = (List)this.headers.get(name);
/* 187 */       return CollectionUtils.isEmpty(value) ? null : (String)value.get(0);
/*     */     }
/*     */     
/*     */     public Enumeration<String> getHeaders(String name)
/*     */     {
/* 192 */       List<String> value = (List)this.headers.get(name);
/* 193 */       return Collections.enumeration(value != null ? value : Collections.emptySet());
/*     */     }
/*     */     
/*     */     public Enumeration<String> getHeaderNames()
/*     */     {
/* 198 */       return Collections.enumeration(this.headers.keySet());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ForwardedHeaderExtractingRequest
/*     */     extends ForwardedHeaderFilter.ForwardedHeaderRemovingRequest
/*     */   {
/*     */     private final String scheme;
/*     */     
/*     */     private final boolean secure;
/*     */     
/*     */     private final String host;
/*     */     
/*     */     private final int port;
/*     */     
/*     */     private final String contextPath;
/*     */     
/*     */     private final String requestUri;
/*     */     
/*     */     private final String requestUrl;
/*     */     
/*     */ 
/*     */     public ForwardedHeaderExtractingRequest(HttpServletRequest request, UrlPathHelper pathHelper)
/*     */     {
/* 223 */       super();
/*     */       
/* 225 */       HttpRequest httpRequest = new ServletServerHttpRequest(request);
/* 226 */       UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();
/* 227 */       int port = uriComponents.getPort();
/*     */       
/* 229 */       this.scheme = uriComponents.getScheme();
/* 230 */       this.secure = "https".equals(this.scheme);
/* 231 */       this.host = uriComponents.getHost();
/* 232 */       this.port = (port == -1 ? 80 : this.secure ? 443 : port);
/*     */       
/* 234 */       String prefix = getForwardedPrefix(request);
/* 235 */       this.contextPath = (prefix != null ? prefix : request.getContextPath());
/* 236 */       this.requestUri = (this.contextPath + pathHelper.getPathWithinApplication(request));
/* 237 */       this.requestUrl = (this.scheme + "://" + this.host + (port == -1 ? "" : new StringBuilder().append(":").append(port).toString()) + this.requestUri);
/*     */     }
/*     */     
/*     */     private static String getForwardedPrefix(HttpServletRequest request) {
/* 241 */       String prefix = null;
/* 242 */       Enumeration<String> names = request.getHeaderNames();
/* 243 */       while (names.hasMoreElements()) {
/* 244 */         String name = (String)names.nextElement();
/* 245 */         if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
/* 246 */           prefix = request.getHeader(name);
/*     */         }
/*     */       }
/* 249 */       if (prefix != null) {
/* 250 */         while (prefix.endsWith("/")) {
/* 251 */           prefix = prefix.substring(0, prefix.length() - 1);
/*     */         }
/*     */       }
/* 254 */       return prefix;
/*     */     }
/*     */     
/*     */     public String getScheme()
/*     */     {
/* 259 */       return this.scheme;
/*     */     }
/*     */     
/*     */     public String getServerName()
/*     */     {
/* 264 */       return this.host;
/*     */     }
/*     */     
/*     */     public int getServerPort()
/*     */     {
/* 269 */       return this.port;
/*     */     }
/*     */     
/*     */     public boolean isSecure()
/*     */     {
/* 274 */       return this.secure;
/*     */     }
/*     */     
/*     */     public String getContextPath()
/*     */     {
/* 279 */       return this.contextPath;
/*     */     }
/*     */     
/*     */     public String getRequestURI()
/*     */     {
/* 284 */       return this.requestUri;
/*     */     }
/*     */     
/*     */     public StringBuffer getRequestURL()
/*     */     {
/* 289 */       return new StringBuffer(this.requestUrl);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ForwardedHeaderExtractingResponse
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private static final String FOLDER_SEPARATOR = "/";
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     public ForwardedHeaderExtractingResponse(HttpServletResponse response, HttpServletRequest request)
/*     */     {
/* 301 */       super();
/* 302 */       this.request = request;
/*     */     }
/*     */     
/*     */     public void sendRedirect(String location) throws IOException
/*     */     {
/* 307 */       UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(location);
/*     */       
/*     */ 
/* 310 */       if (builder.build().getScheme() != null) {
/* 311 */         super.sendRedirect(location);
/* 312 */         return;
/*     */       }
/*     */       
/*     */ 
/* 316 */       if (location.startsWith("//")) {
/* 317 */         String scheme = this.request.getScheme();
/* 318 */         super.sendRedirect(builder.scheme(scheme).toUriString());
/* 319 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 324 */       String path = location.startsWith("/") ? location : StringUtils.applyRelativePath(this.request.getRequestURI(), location);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 329 */       String result = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(this.request)).replacePath(path).build().normalize().toUriString();
/*     */       
/* 331 */       super.sendRedirect(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\ForwardedHeaderFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */