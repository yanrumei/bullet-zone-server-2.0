/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public class ServletWebRequest
/*     */   extends ServletRequestAttributes
/*     */   implements NativeWebRequest
/*     */ {
/*     */   private static final String ETAG = "ETag";
/*     */   private static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*     */   private static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*     */   private static final String IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String LAST_MODIFIED = "Last-Modified";
/*  63 */   private static final List<String> SAFE_METHODS = Arrays.asList(new String[] { "GET", "HEAD" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   private static final String[] DATE_FORMATS = { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private static TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*  85 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", new Class[] { String.class });
/*     */   
/*  87 */   private boolean notModified = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletWebRequest(HttpServletRequest request)
/*     */   {
/*  95 */     super(request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletWebRequest(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 104 */     super(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getNativeRequest()
/*     */   {
/* 110 */     return getRequest();
/*     */   }
/*     */   
/*     */   public Object getNativeResponse()
/*     */   {
/* 115 */     return getResponse();
/*     */   }
/*     */   
/*     */   public <T> T getNativeRequest(Class<T> requiredType)
/*     */   {
/* 120 */     return (T)WebUtils.getNativeRequest(getRequest(), requiredType);
/*     */   }
/*     */   
/*     */   public <T> T getNativeResponse(Class<T> requiredType)
/*     */   {
/* 125 */     return (T)WebUtils.getNativeResponse(getResponse(), requiredType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMethod getHttpMethod()
/*     */   {
/* 133 */     return HttpMethod.resolve(getRequest().getMethod());
/*     */   }
/*     */   
/*     */   public String getHeader(String headerName)
/*     */   {
/* 138 */     return getRequest().getHeader(headerName);
/*     */   }
/*     */   
/*     */   public String[] getHeaderValues(String headerName)
/*     */   {
/* 143 */     String[] headerValues = StringUtils.toStringArray(getRequest().getHeaders(headerName));
/* 144 */     return !ObjectUtils.isEmpty(headerValues) ? headerValues : null;
/*     */   }
/*     */   
/*     */   public Iterator<String> getHeaderNames()
/*     */   {
/* 149 */     return CollectionUtils.toIterator(getRequest().getHeaderNames());
/*     */   }
/*     */   
/*     */   public String getParameter(String paramName)
/*     */   {
/* 154 */     return getRequest().getParameter(paramName);
/*     */   }
/*     */   
/*     */   public String[] getParameterValues(String paramName)
/*     */   {
/* 159 */     return getRequest().getParameterValues(paramName);
/*     */   }
/*     */   
/*     */   public Iterator<String> getParameterNames()
/*     */   {
/* 164 */     return CollectionUtils.toIterator(getRequest().getParameterNames());
/*     */   }
/*     */   
/*     */   public Map<String, String[]> getParameterMap()
/*     */   {
/* 169 */     return getRequest().getParameterMap();
/*     */   }
/*     */   
/*     */   public Locale getLocale()
/*     */   {
/* 174 */     return getRequest().getLocale();
/*     */   }
/*     */   
/*     */   public String getContextPath()
/*     */   {
/* 179 */     return getRequest().getContextPath();
/*     */   }
/*     */   
/*     */   public String getRemoteUser()
/*     */   {
/* 184 */     return getRequest().getRemoteUser();
/*     */   }
/*     */   
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 189 */     return getRequest().getUserPrincipal();
/*     */   }
/*     */   
/*     */   public boolean isUserInRole(String role)
/*     */   {
/* 194 */     return getRequest().isUserInRole(role);
/*     */   }
/*     */   
/*     */   public boolean isSecure()
/*     */   {
/* 199 */     return getRequest().isSecure();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean checkNotModified(long lastModifiedTimestamp)
/*     */   {
/* 205 */     return checkNotModified(null, lastModifiedTimestamp);
/*     */   }
/*     */   
/*     */   public boolean checkNotModified(String etag)
/*     */   {
/* 210 */     return checkNotModified(etag, -1L);
/*     */   }
/*     */   
/*     */   public boolean checkNotModified(String etag, long lastModifiedTimestamp)
/*     */   {
/* 215 */     HttpServletResponse response = getResponse();
/* 216 */     if ((this.notModified) || (!isStatusOK(response))) {
/* 217 */       return this.notModified;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 223 */     if (validateIfUnmodifiedSince(lastModifiedTimestamp)) {
/* 224 */       if (this.notModified) {
/* 225 */         response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
/*     */       }
/* 227 */       return this.notModified;
/*     */     }
/*     */     
/* 230 */     boolean validated = validateIfNoneMatch(etag);
/* 231 */     if (!validated) {
/* 232 */       validateIfModifiedSince(lastModifiedTimestamp);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 237 */     boolean isHttpGetOrHead = SAFE_METHODS.contains(getRequest().getMethod());
/* 238 */     if (this.notModified) {
/* 239 */       response.setStatus(isHttpGetOrHead ? HttpStatus.NOT_MODIFIED
/* 240 */         .value() : HttpStatus.PRECONDITION_FAILED.value());
/*     */     }
/* 242 */     if (isHttpGetOrHead) {
/* 243 */       if ((lastModifiedTimestamp > 0L) && (isHeaderAbsent(response, "Last-Modified"))) {
/* 244 */         response.setDateHeader("Last-Modified", lastModifiedTimestamp);
/*     */       }
/* 246 */       if ((StringUtils.hasLength(etag)) && (isHeaderAbsent(response, "ETag"))) {
/* 247 */         response.setHeader("ETag", padEtagIfNecessary(etag));
/*     */       }
/*     */     }
/*     */     
/* 251 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private boolean isStatusOK(HttpServletResponse response) {
/* 255 */     if ((response == null) || (!servlet3Present))
/*     */     {
/* 257 */       return true;
/*     */     }
/* 259 */     return response.getStatus() == 200;
/*     */   }
/*     */   
/*     */   private boolean isHeaderAbsent(HttpServletResponse response, String header) {
/* 263 */     if ((response == null) || (!servlet3Present))
/*     */     {
/* 265 */       return true;
/*     */     }
/* 267 */     return response.getHeader(header) == null;
/*     */   }
/*     */   
/*     */   private boolean validateIfUnmodifiedSince(long lastModifiedTimestamp) {
/* 271 */     if (lastModifiedTimestamp < 0L) {
/* 272 */       return false;
/*     */     }
/* 274 */     long ifUnmodifiedSince = parseDateHeader("If-Unmodified-Since");
/* 275 */     if (ifUnmodifiedSince == -1L) {
/* 276 */       return false;
/*     */     }
/*     */     
/* 279 */     this.notModified = (ifUnmodifiedSince < lastModifiedTimestamp / 1000L * 1000L);
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   private boolean validateIfNoneMatch(String etag) {
/* 284 */     if (!StringUtils.hasLength(etag)) {
/* 285 */       return false;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 290 */       ifNoneMatch = getRequest().getHeaders("If-None-Match");
/*     */     } catch (IllegalArgumentException ex) {
/*     */       Enumeration<String> ifNoneMatch;
/* 293 */       return false; }
/*     */     Enumeration<String> ifNoneMatch;
/* 295 */     if (!ifNoneMatch.hasMoreElements()) {
/* 296 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 300 */     etag = padEtagIfNecessary(etag);
/* 301 */     while (ifNoneMatch.hasMoreElements()) {
/* 302 */       String clientETags = (String)ifNoneMatch.nextElement();
/* 303 */       Matcher etagMatcher = ETAG_HEADER_VALUE_PATTERN.matcher(clientETags);
/*     */       
/* 305 */       while (etagMatcher.find()) {
/* 306 */         if ((StringUtils.hasLength(etagMatcher.group())) && 
/* 307 */           (etag.replaceFirst("^W/", "").equals(etagMatcher.group(3)))) {
/* 308 */           this.notModified = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 314 */     return true;
/*     */   }
/*     */   
/*     */   private String padEtagIfNecessary(String etag) {
/* 318 */     if (!StringUtils.hasLength(etag)) {
/* 319 */       return etag;
/*     */     }
/* 321 */     if (((etag.startsWith("\"")) || (etag.startsWith("W/\""))) && (etag.endsWith("\""))) {
/* 322 */       return etag;
/*     */     }
/* 324 */     return "\"" + etag + "\"";
/*     */   }
/*     */   
/*     */   private boolean validateIfModifiedSince(long lastModifiedTimestamp) {
/* 328 */     if (lastModifiedTimestamp < 0L) {
/* 329 */       return false;
/*     */     }
/* 331 */     long ifModifiedSince = parseDateHeader("If-Modified-Since");
/* 332 */     if (ifModifiedSince == -1L) {
/* 333 */       return false;
/*     */     }
/*     */     
/* 336 */     this.notModified = (ifModifiedSince >= lastModifiedTimestamp / 1000L * 1000L);
/* 337 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isNotModified() {
/* 341 */     return this.notModified;
/*     */   }
/*     */   
/*     */   private long parseDateHeader(String headerName) {
/* 345 */     long dateValue = -1L;
/*     */     try {
/* 347 */       dateValue = getRequest().getDateHeader(headerName);
/*     */     }
/*     */     catch (IllegalArgumentException ex) {
/* 350 */       String headerValue = getHeader(headerName);
/*     */       
/* 352 */       int separatorIndex = headerValue.indexOf(';');
/* 353 */       if (separatorIndex != -1) {
/* 354 */         String datePart = headerValue.substring(0, separatorIndex);
/* 355 */         dateValue = parseDateValue(datePart);
/*     */       }
/*     */     }
/* 358 */     return dateValue;
/*     */   }
/*     */   
/*     */   private long parseDateValue(String headerValue) {
/* 362 */     if (headerValue == null)
/*     */     {
/* 364 */       return -1L;
/*     */     }
/* 366 */     if (headerValue.length() >= 3)
/*     */     {
/*     */ 
/* 369 */       for (String dateFormat : DATE_FORMATS) {
/* 370 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 371 */         simpleDateFormat.setTimeZone(GMT);
/*     */         try {
/* 373 */           return simpleDateFormat.parse(headerValue).getTime();
/*     */         }
/*     */         catch (ParseException localParseException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 380 */     return -1L;
/*     */   }
/*     */   
/*     */   public String getDescription(boolean includeClientInfo)
/*     */   {
/* 385 */     HttpServletRequest request = getRequest();
/* 386 */     StringBuilder sb = new StringBuilder();
/* 387 */     sb.append("uri=").append(request.getRequestURI());
/* 388 */     if (includeClientInfo) {
/* 389 */       String client = request.getRemoteAddr();
/* 390 */       if (StringUtils.hasLength(client)) {
/* 391 */         sb.append(";client=").append(client);
/*     */       }
/* 393 */       HttpSession session = request.getSession(false);
/* 394 */       if (session != null) {
/* 395 */         sb.append(";session=").append(session.getId());
/*     */       }
/* 397 */       String user = request.getRemoteUser();
/* 398 */       if (StringUtils.hasLength(user)) {
/* 399 */         sb.append(";user=").append(user);
/*     */       }
/*     */     }
/* 402 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 408 */     return "ServletWebRequest: " + getDescription(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\ServletWebRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */