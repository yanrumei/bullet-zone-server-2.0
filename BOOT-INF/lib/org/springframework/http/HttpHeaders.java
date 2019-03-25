/*      */ package org.springframework.http;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.net.URI;
/*      */ import java.nio.charset.Charset;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpHeaders
/*      */   implements MultiValueMap<String, String>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -8578554704772377436L;
/*      */   public static final String ACCEPT = "Accept";
/*      */   public static final String ACCEPT_CHARSET = "Accept-Charset";
/*      */   public static final String ACCEPT_ENCODING = "Accept-Encoding";
/*      */   public static final String ACCEPT_LANGUAGE = "Accept-Language";
/*      */   public static final String ACCEPT_RANGES = "Accept-Ranges";
/*      */   public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
/*      */   public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
/*      */   public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
/*      */   public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
/*      */   public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
/*      */   public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
/*      */   public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
/*      */   public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
/*      */   public static final String AGE = "Age";
/*      */   public static final String ALLOW = "Allow";
/*      */   public static final String AUTHORIZATION = "Authorization";
/*      */   public static final String CACHE_CONTROL = "Cache-Control";
/*      */   public static final String CONNECTION = "Connection";
/*      */   public static final String CONTENT_ENCODING = "Content-Encoding";
/*      */   public static final String CONTENT_DISPOSITION = "Content-Disposition";
/*      */   public static final String CONTENT_LANGUAGE = "Content-Language";
/*      */   public static final String CONTENT_LENGTH = "Content-Length";
/*      */   public static final String CONTENT_LOCATION = "Content-Location";
/*      */   public static final String CONTENT_RANGE = "Content-Range";
/*      */   public static final String CONTENT_TYPE = "Content-Type";
/*      */   public static final String COOKIE = "Cookie";
/*      */   public static final String DATE = "Date";
/*      */   public static final String ETAG = "ETag";
/*      */   public static final String EXPECT = "Expect";
/*      */   public static final String EXPIRES = "Expires";
/*      */   public static final String FROM = "From";
/*      */   public static final String HOST = "Host";
/*      */   public static final String IF_MATCH = "If-Match";
/*      */   public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
/*      */   public static final String IF_NONE_MATCH = "If-None-Match";
/*      */   public static final String IF_RANGE = "If-Range";
/*      */   public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
/*      */   public static final String LAST_MODIFIED = "Last-Modified";
/*      */   public static final String LINK = "Link";
/*      */   public static final String LOCATION = "Location";
/*      */   public static final String MAX_FORWARDS = "Max-Forwards";
/*      */   public static final String ORIGIN = "Origin";
/*      */   public static final String PRAGMA = "Pragma";
/*      */   public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
/*      */   public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
/*      */   public static final String RANGE = "Range";
/*      */   public static final String REFERER = "Referer";
/*      */   public static final String RETRY_AFTER = "Retry-After";
/*      */   public static final String SERVER = "Server";
/*      */   public static final String SET_COOKIE = "Set-Cookie";
/*      */   public static final String SET_COOKIE2 = "Set-Cookie2";
/*      */   public static final String TE = "TE";
/*      */   public static final String TRAILER = "Trailer";
/*      */   public static final String TRANSFER_ENCODING = "Transfer-Encoding";
/*      */   public static final String UPGRADE = "Upgrade";
/*      */   public static final String USER_AGENT = "User-Agent";
/*      */   public static final String VARY = "Vary";
/*      */   public static final String VIA = "Via";
/*      */   public static final String WARNING = "Warning";
/*      */   public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
/*  374 */   private static final String[] DATE_FORMATS = { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  384 */   private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
/*      */   
/*  386 */   private static TimeZone GMT = TimeZone.getTimeZone("GMT");
/*      */   
/*      */ 
/*      */ 
/*      */   private final Map<String, List<String>> headers;
/*      */   
/*      */ 
/*      */ 
/*      */   public HttpHeaders()
/*      */   {
/*  396 */     this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private HttpHeaders(Map<String, List<String>> headers, boolean readOnly)
/*      */   {
/*  403 */     Assert.notNull(headers, "'headers' must not be null");
/*  404 */     if (readOnly)
/*      */     {
/*  406 */       Map<String, List<String>> map = new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
/*  407 */       for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/*  408 */         List<String> values = Collections.unmodifiableList((List)entry.getValue());
/*  409 */         map.put(entry.getKey(), values);
/*      */       }
/*  411 */       this.headers = Collections.unmodifiableMap(map);
/*      */     }
/*      */     else {
/*  414 */       this.headers = headers;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAccept(List<MediaType> acceptableMediaTypes)
/*      */   {
/*  424 */     set("Accept", MediaType.toString(acceptableMediaTypes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<MediaType> getAccept()
/*      */   {
/*  433 */     return MediaType.parseMediaTypes(get("Accept"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlAllowCredentials(boolean allowCredentials)
/*      */   {
/*  440 */     set("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getAccessControlAllowCredentials()
/*      */   {
/*  447 */     return Boolean.parseBoolean(getFirst("Access-Control-Allow-Credentials"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlAllowHeaders(List<String> allowedHeaders)
/*      */   {
/*  454 */     set("Access-Control-Allow-Headers", toCommaDelimitedString(allowedHeaders));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getAccessControlAllowHeaders()
/*      */   {
/*  461 */     return getValuesAsList("Access-Control-Allow-Headers");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods)
/*      */   {
/*  468 */     set("Access-Control-Allow-Methods", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<HttpMethod> getAccessControlAllowMethods()
/*      */   {
/*  475 */     List<HttpMethod> result = new ArrayList();
/*  476 */     String value = getFirst("Access-Control-Allow-Methods");
/*  477 */     if (value != null) {
/*  478 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  479 */       for (String token : tokens) {
/*  480 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  481 */         if (resolved != null) {
/*  482 */           result.add(resolved);
/*      */         }
/*      */       }
/*      */     }
/*  486 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlAllowOrigin(String allowedOrigin)
/*      */   {
/*  493 */     set("Access-Control-Allow-Origin", allowedOrigin);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getAccessControlAllowOrigin()
/*      */   {
/*  500 */     return getFieldValues("Access-Control-Allow-Origin");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlExposeHeaders(List<String> exposedHeaders)
/*      */   {
/*  507 */     set("Access-Control-Expose-Headers", toCommaDelimitedString(exposedHeaders));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getAccessControlExposeHeaders()
/*      */   {
/*  514 */     return getValuesAsList("Access-Control-Expose-Headers");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlMaxAge(long maxAge)
/*      */   {
/*  521 */     set("Access-Control-Max-Age", Long.toString(maxAge));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getAccessControlMaxAge()
/*      */   {
/*  529 */     String value = getFirst("Access-Control-Max-Age");
/*  530 */     return value != null ? Long.parseLong(value) : -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlRequestHeaders(List<String> requestHeaders)
/*      */   {
/*  537 */     set("Access-Control-Request-Headers", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getAccessControlRequestHeaders()
/*      */   {
/*  544 */     return getValuesAsList("Access-Control-Request-Headers");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setAccessControlRequestMethod(HttpMethod requestMethod)
/*      */   {
/*  551 */     set("Access-Control-Request-Method", requestMethod.name());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HttpMethod getAccessControlRequestMethod()
/*      */   {
/*  558 */     return HttpMethod.resolve(getFirst("Access-Control-Request-Method"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAcceptCharset(List<Charset> acceptableCharsets)
/*      */   {
/*  566 */     StringBuilder builder = new StringBuilder();
/*  567 */     for (Iterator<Charset> iterator = acceptableCharsets.iterator(); iterator.hasNext();) {
/*  568 */       Charset charset = (Charset)iterator.next();
/*  569 */       builder.append(charset.name().toLowerCase(Locale.ENGLISH));
/*  570 */       if (iterator.hasNext()) {
/*  571 */         builder.append(", ");
/*      */       }
/*      */     }
/*  574 */     set("Accept-Charset", builder.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Charset> getAcceptCharset()
/*      */   {
/*  582 */     String value = getFirst("Accept-Charset");
/*  583 */     if (value != null) {
/*  584 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  585 */       List<Charset> result = new ArrayList(tokens.length);
/*  586 */       for (String token : tokens) {
/*  587 */         int paramIdx = token.indexOf(';');
/*      */         String charsetName;
/*  589 */         String charsetName; if (paramIdx == -1) {
/*  590 */           charsetName = token;
/*      */         }
/*      */         else {
/*  593 */           charsetName = token.substring(0, paramIdx);
/*      */         }
/*  595 */         if (!charsetName.equals("*")) {
/*  596 */           result.add(Charset.forName(charsetName));
/*      */         }
/*      */       }
/*  599 */       return result;
/*      */     }
/*      */     
/*  602 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllow(Set<HttpMethod> allowedMethods)
/*      */   {
/*  611 */     set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<HttpMethod> getAllow()
/*      */   {
/*  620 */     String value = getFirst("Allow");
/*  621 */     if (!StringUtils.isEmpty(value)) {
/*  622 */       String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/*  623 */       List<HttpMethod> result = new ArrayList(tokens.length);
/*  624 */       for (String token : tokens) {
/*  625 */         HttpMethod resolved = HttpMethod.resolve(token);
/*  626 */         if (resolved != null) {
/*  627 */           result.add(resolved);
/*      */         }
/*      */       }
/*  630 */       return EnumSet.copyOf(result);
/*      */     }
/*      */     
/*  633 */     return EnumSet.noneOf(HttpMethod.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCacheControl(String cacheControl)
/*      */   {
/*  641 */     set("Cache-Control", cacheControl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getCacheControl()
/*      */   {
/*  648 */     return getFieldValues("Cache-Control");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setConnection(String connection)
/*      */   {
/*  655 */     set("Connection", connection);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setConnection(List<String> connection)
/*      */   {
/*  662 */     set("Connection", toCommaDelimitedString(connection));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getConnection()
/*      */   {
/*  669 */     return getValuesAsList("Connection");
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
/*      */   public void setContentDispositionFormData(String name, String filename)
/*      */   {
/*  683 */     Assert.notNull(name, "'name' must not be null");
/*  684 */     StringBuilder builder = new StringBuilder("form-data; name=\"");
/*  685 */     builder.append(name).append('"');
/*  686 */     if (filename != null) {
/*  687 */       builder.append("; filename=\"");
/*  688 */       builder.append(filename).append('"');
/*      */     }
/*  690 */     set("Content-Disposition", builder.toString());
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
/*      */   public void setContentDispositionFormData(String name, String filename, Charset charset)
/*      */   {
/*  709 */     Assert.notNull(name, "'name' must not be null");
/*  710 */     StringBuilder builder = new StringBuilder("form-data; name=\"");
/*  711 */     builder.append(name).append('"');
/*  712 */     if (filename != null) {
/*  713 */       if ((charset == null) || (charset.name().equals("US-ASCII"))) {
/*  714 */         builder.append("; filename=\"");
/*  715 */         builder.append(filename).append('"');
/*      */       }
/*      */       else {
/*  718 */         builder.append("; filename*=");
/*  719 */         builder.append(encodeHeaderFieldParam(filename, charset));
/*      */       }
/*      */     }
/*  722 */     set("Content-Disposition", builder.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContentLength(long contentLength)
/*      */   {
/*  730 */     set("Content-Length", Long.toString(contentLength));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getContentLength()
/*      */   {
/*  739 */     String value = getFirst("Content-Length");
/*  740 */     return value != null ? Long.parseLong(value) : -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContentType(MediaType mediaType)
/*      */   {
/*  748 */     Assert.isTrue(!mediaType.isWildcardType(), "'Content-Type' cannot contain wildcard type '*'");
/*  749 */     Assert.isTrue(!mediaType.isWildcardSubtype(), "'Content-Type' cannot contain wildcard subtype '*'");
/*  750 */     set("Content-Type", mediaType.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MediaType getContentType()
/*      */   {
/*  759 */     String value = getFirst("Content-Type");
/*  760 */     return StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDate(long date)
/*      */   {
/*  770 */     setDate("Date", date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getDate()
/*      */   {
/*  781 */     return getFirstDate("Date");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setETag(String etag)
/*      */   {
/*  788 */     if (etag != null) {
/*  789 */       Assert.isTrue((etag.startsWith("\"")) || (etag.startsWith("W/")), "Invalid ETag: does not start with W/ or \"");
/*      */       
/*  791 */       Assert.isTrue(etag.endsWith("\""), "Invalid ETag: does not end with \"");
/*      */     }
/*  793 */     set("ETag", etag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getETag()
/*      */   {
/*  800 */     return getFirst("ETag");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExpires(long expires)
/*      */   {
/*  810 */     setDate("Expires", expires);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getExpires()
/*      */   {
/*  820 */     return getFirstDate("Expires", false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIfMatch(String ifMatch)
/*      */   {
/*  828 */     set("If-Match", ifMatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIfMatch(List<String> ifMatchList)
/*      */   {
/*  836 */     set("If-Match", toCommaDelimitedString(ifMatchList));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getIfMatch()
/*      */   {
/*  844 */     return getETagValuesAsList("If-Match");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIfModifiedSince(long ifModifiedSince)
/*      */   {
/*  853 */     setDate("If-Modified-Since", ifModifiedSince);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIfModifiedSince()
/*      */   {
/*  862 */     return getFirstDate("If-Modified-Since", false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setIfNoneMatch(String ifNoneMatch)
/*      */   {
/*  869 */     set("If-None-Match", ifNoneMatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setIfNoneMatch(List<String> ifNoneMatchList)
/*      */   {
/*  876 */     set("If-None-Match", toCommaDelimitedString(ifNoneMatchList));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public List<String> getIfNoneMatch()
/*      */   {
/*  883 */     return getETagValuesAsList("If-None-Match");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIfUnmodifiedSince(long ifUnmodifiedSince)
/*      */   {
/*  893 */     setDate("If-Unmodified-Since", ifUnmodifiedSince);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIfUnmodifiedSince()
/*      */   {
/*  903 */     return getFirstDate("If-Unmodified-Since", false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLastModified(long lastModified)
/*      */   {
/*  913 */     setDate("Last-Modified", lastModified);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLastModified()
/*      */   {
/*  923 */     return getFirstDate("Last-Modified", false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocation(URI location)
/*      */   {
/*  931 */     set("Location", location.toASCIIString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public URI getLocation()
/*      */   {
/*  940 */     String value = getFirst("Location");
/*  941 */     return value != null ? URI.create(value) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setOrigin(String origin)
/*      */   {
/*  948 */     set("Origin", origin);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getOrigin()
/*      */   {
/*  955 */     return getFirst("Origin");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setPragma(String pragma)
/*      */   {
/*  962 */     set("Pragma", pragma);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getPragma()
/*      */   {
/*  969 */     return getFirst("Pragma");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setRange(List<HttpRange> ranges)
/*      */   {
/*  976 */     String value = HttpRange.toString(ranges);
/*  977 */     set("Range", value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<HttpRange> getRange()
/*      */   {
/*  985 */     String value = getFirst("Range");
/*  986 */     return HttpRange.parseRanges(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setUpgrade(String upgrade)
/*      */   {
/*  993 */     set("Upgrade", upgrade);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getUpgrade()
/*      */   {
/* 1000 */     return getFirst("Upgrade");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setVary(List<String> requestHeaders)
/*      */   {
/* 1011 */     set("Vary", toCommaDelimitedString(requestHeaders));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getVary()
/*      */   {
/* 1019 */     return getValuesAsList("Vary");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDate(String headerName, long date)
/*      */   {
/* 1029 */     SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
/* 1030 */     dateFormat.setTimeZone(GMT);
/* 1031 */     set(headerName, dateFormat.format(new Date(date)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getFirstDate(String headerName)
/*      */   {
/* 1043 */     return getFirstDate(headerName, true);
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
/*      */   private long getFirstDate(String headerName, boolean rejectInvalid)
/*      */   {
/* 1058 */     String headerValue = getFirst(headerName);
/* 1059 */     if (headerValue == null)
/*      */     {
/* 1061 */       return -1L;
/*      */     }
/* 1063 */     if (headerValue.length() >= 3)
/*      */     {
/*      */ 
/* 1066 */       for (String dateFormat : DATE_FORMATS) {
/* 1067 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
/* 1068 */         simpleDateFormat.setTimeZone(GMT);
/*      */         try {
/* 1070 */           return simpleDateFormat.parse(headerValue).getTime();
/*      */         }
/*      */         catch (ParseException localParseException) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1077 */     if (rejectInvalid) {
/* 1078 */       throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
/*      */     }
/*      */     
/* 1081 */     return -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getValuesAsList(String headerName)
/*      */   {
/* 1092 */     List<String> values = get(headerName);
/* 1093 */     if (values != null) {
/* 1094 */       List<String> result = new ArrayList();
/* 1095 */       for (String value : values) {
/* 1096 */         if (value != null) {
/* 1097 */           String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
/* 1098 */           for (String token : tokens) {
/* 1099 */             result.add(token);
/*      */           }
/*      */         }
/*      */       }
/* 1103 */       return result;
/*      */     }
/* 1105 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<String> getETagValuesAsList(String headerName)
/*      */   {
/* 1115 */     List<String> values = get(headerName);
/* 1116 */     if (values != null) {
/* 1117 */       List<String> result = new ArrayList();
/* 1118 */       for (String value : values) {
/* 1119 */         if (value != null) {
/* 1120 */           Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);
/* 1121 */           while (matcher.find()) {
/* 1122 */             if ("*".equals(matcher.group())) {
/* 1123 */               result.add(matcher.group());
/*      */             }
/*      */             else {
/* 1126 */               result.add(matcher.group(1));
/*      */             }
/*      */           }
/* 1129 */           if (result.isEmpty()) {
/* 1130 */             throw new IllegalArgumentException("Could not parse header '" + headerName + "' with value '" + value + "'");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1135 */       return result;
/*      */     }
/* 1137 */     return Collections.emptyList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getFieldValues(String headerName)
/*      */   {
/* 1147 */     List<String> headerValues = get(headerName);
/* 1148 */     return headerValues != null ? toCommaDelimitedString(headerValues) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String toCommaDelimitedString(List<String> headerValues)
/*      */   {
/* 1157 */     StringBuilder builder = new StringBuilder();
/* 1158 */     for (Iterator<String> it = headerValues.iterator(); it.hasNext();) {
/* 1159 */       String val = (String)it.next();
/* 1160 */       builder.append(val);
/* 1161 */       if (it.hasNext()) {
/* 1162 */         builder.append(", ");
/*      */       }
/*      */     }
/* 1165 */     return builder.toString();
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
/*      */   public String getFirst(String headerName)
/*      */   {
/* 1178 */     List<String> headerValues = (List)this.headers.get(headerName);
/* 1179 */     return headerValues != null ? (String)headerValues.get(0) : null;
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
/*      */   public void add(String headerName, String headerValue)
/*      */   {
/* 1192 */     List<String> headerValues = (List)this.headers.get(headerName);
/* 1193 */     if (headerValues == null) {
/* 1194 */       headerValues = new LinkedList();
/* 1195 */       this.headers.put(headerName, headerValues);
/*      */     }
/* 1197 */     headerValues.add(headerValue);
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
/*      */   public void set(String headerName, String headerValue)
/*      */   {
/* 1210 */     List<String> headerValues = new LinkedList();
/* 1211 */     headerValues.add(headerValue);
/* 1212 */     this.headers.put(headerName, headerValues);
/*      */   }
/*      */   
/*      */   public void setAll(Map<String, String> values)
/*      */   {
/* 1217 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 1218 */       set((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/*      */   }
/*      */   
/*      */   public Map<String, String> toSingleValueMap()
/*      */   {
/* 1224 */     LinkedHashMap<String, String> singleValueMap = new LinkedHashMap(this.headers.size());
/* 1225 */     for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/* 1226 */       singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0));
/*      */     }
/* 1228 */     return singleValueMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/* 1236 */     return this.headers.size();
/*      */   }
/*      */   
/*      */   public boolean isEmpty()
/*      */   {
/* 1241 */     return this.headers.isEmpty();
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object key)
/*      */   {
/* 1246 */     return this.headers.containsKey(key);
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object value)
/*      */   {
/* 1251 */     return this.headers.containsValue(value);
/*      */   }
/*      */   
/*      */   public List<String> get(Object key)
/*      */   {
/* 1256 */     return (List)this.headers.get(key);
/*      */   }
/*      */   
/*      */   public List<String> put(String key, List<String> value)
/*      */   {
/* 1261 */     return (List)this.headers.put(key, value);
/*      */   }
/*      */   
/*      */   public List<String> remove(Object key)
/*      */   {
/* 1266 */     return (List)this.headers.remove(key);
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends String, ? extends List<String>> map)
/*      */   {
/* 1271 */     this.headers.putAll(map);
/*      */   }
/*      */   
/*      */   public void clear()
/*      */   {
/* 1276 */     this.headers.clear();
/*      */   }
/*      */   
/*      */   public Set<String> keySet()
/*      */   {
/* 1281 */     return this.headers.keySet();
/*      */   }
/*      */   
/*      */   public Collection<List<String>> values()
/*      */   {
/* 1286 */     return this.headers.values();
/*      */   }
/*      */   
/*      */   public Set<Map.Entry<String, List<String>>> entrySet()
/*      */   {
/* 1291 */     return this.headers.entrySet();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object other)
/*      */   {
/* 1297 */     if (this == other) {
/* 1298 */       return true;
/*      */     }
/* 1300 */     if (!(other instanceof HttpHeaders)) {
/* 1301 */       return false;
/*      */     }
/* 1303 */     HttpHeaders otherHeaders = (HttpHeaders)other;
/* 1304 */     return this.headers.equals(otherHeaders.headers);
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1309 */     return this.headers.hashCode();
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 1314 */     return this.headers.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers)
/*      */   {
/* 1322 */     return new HttpHeaders(headers, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String encodeHeaderFieldParam(String input, Charset charset)
/*      */   {
/* 1333 */     Assert.notNull(input, "Input String should not be null");
/* 1334 */     Assert.notNull(charset, "Charset should not be null");
/* 1335 */     if (charset.name().equals("US-ASCII")) {
/* 1336 */       return input;
/*      */     }
/* 1338 */     Assert.isTrue((charset.name().equals("UTF-8")) || (charset.name().equals("ISO-8859-1")), "Charset should be UTF-8 or ISO-8859-1");
/*      */     
/* 1340 */     byte[] source = input.getBytes(charset);
/* 1341 */     int len = source.length;
/* 1342 */     StringBuilder sb = new StringBuilder(len << 1);
/* 1343 */     sb.append(charset.name());
/* 1344 */     sb.append("''");
/* 1345 */     for (byte b : source) {
/* 1346 */       if (isRFC5987AttrChar(b)) {
/* 1347 */         sb.append((char)b);
/*      */       }
/*      */       else {
/* 1350 */         sb.append('%');
/* 1351 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 1352 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 1353 */         sb.append(hex1);
/* 1354 */         sb.append(hex2);
/*      */       }
/*      */     }
/* 1357 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private static boolean isRFC5987AttrChar(byte c) {
/* 1361 */     return ((c >= 48) && (c <= 57)) || ((c >= 97) && (c <= 122)) || ((c >= 65) && (c <= 90)) || (c == 33) || (c == 35) || (c == 36) || (c == 38) || (c == 43) || (c == 45) || (c == 46) || (c == 94) || (c == 95) || (c == 96) || (c == 124) || (c == 126);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\HttpHeaders.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */