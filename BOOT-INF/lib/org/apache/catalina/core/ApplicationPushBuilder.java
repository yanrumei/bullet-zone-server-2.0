/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.SessionTrackingMode;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.servlet4preview.http.PushBuilder;
/*     */ import org.apache.catalina.util.SessionConfig;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
/*     */ import org.apache.tomcat.util.http.CookieProcessor;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.http.parser.HttpParser;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class ApplicationPushBuilder
/*     */   implements PushBuilder
/*     */ {
/*  47 */   private static final StringManager sm = StringManager.getManager(ApplicationPushBuilder.class);
/*  48 */   private static final Set<String> DISALLOWED_METHODS = new HashSet();
/*     */   private final HttpServletRequest baseRequest;
/*     */   
/*  51 */   static { DISALLOWED_METHODS.add("POST");
/*  52 */     DISALLOWED_METHODS.add("PUT");
/*  53 */     DISALLOWED_METHODS.add("DELETE");
/*  54 */     DISALLOWED_METHODS.add("CONNECT");
/*  55 */     DISALLOWED_METHODS.add("OPTIONS");
/*  56 */     DISALLOWED_METHODS.add("TRACE");
/*     */   }
/*     */   
/*     */ 
/*     */   private final org.apache.catalina.connector.Request catalinaRequest;
/*     */   
/*     */   private final org.apache.coyote.Request coyoteRequest;
/*     */   private final String sessionCookieName;
/*     */   private final String sessionPathParameterName;
/*     */   private final boolean addSessionCookie;
/*     */   private final boolean addSessionPathParameter;
/*  67 */   private final Map<String, List<String>> headers = new CaseInsensitiveKeyMap();
/*  68 */   private final List<Cookie> cookies = new ArrayList();
/*  69 */   private String method = "GET";
/*     */   
/*     */   private String path;
/*     */   private String queryString;
/*     */   private String sessionId;
/*     */   
/*     */   public ApplicationPushBuilder(org.apache.catalina.connector.Request catalinaRequest, HttpServletRequest request)
/*     */   {
/*  77 */     this.baseRequest = request;
/*  78 */     this.catalinaRequest = catalinaRequest;
/*  79 */     this.coyoteRequest = catalinaRequest.getCoyoteRequest();
/*     */     
/*     */ 
/*  82 */     Enumeration<String> headerNames = request.getHeaderNames();
/*  83 */     while (headerNames.hasMoreElements()) {
/*  84 */       String headerName = (String)headerNames.nextElement();
/*  85 */       List<String> values = new ArrayList();
/*  86 */       this.headers.put(headerName, values);
/*  87 */       Enumeration<String> headerValues = request.getHeaders(headerName);
/*  88 */       while (headerValues.hasMoreElements()) {
/*  89 */         values.add(headerValues.nextElement());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  94 */     this.headers.remove("if-match");
/*  95 */     this.headers.remove("if-none-match");
/*  96 */     this.headers.remove("if-modified-since");
/*  97 */     this.headers.remove("if-unmodified-since");
/*  98 */     this.headers.remove("if-range");
/*  99 */     this.headers.remove("range");
/* 100 */     this.headers.remove("expect");
/* 101 */     this.headers.remove("authorization");
/* 102 */     this.headers.remove("referer");
/*     */     
/* 104 */     this.headers.remove("cookie");
/*     */     
/*     */ 
/* 107 */     StringBuffer referer = request.getRequestURL();
/* 108 */     if (request.getQueryString() != null) {
/* 109 */       referer.append('?');
/* 110 */       referer.append(request.getQueryString());
/*     */     }
/*     */     
/* 113 */     addHeader("referer", referer.toString());
/*     */     
/*     */ 
/* 116 */     Context context = catalinaRequest.getContext();
/* 117 */     this.sessionCookieName = SessionConfig.getSessionCookieName(context);
/* 118 */     this.sessionPathParameterName = SessionConfig.getSessionUriParamName(context);
/*     */     
/* 120 */     HttpSession session = request.getSession(false);
/* 121 */     if (session != null) {
/* 122 */       this.sessionId = session.getId();
/*     */     }
/* 124 */     if (this.sessionId == null)
/* 125 */       this.sessionId = request.getRequestedSessionId();
/*     */     Set<SessionTrackingMode> sessionTrackingModes;
/* 127 */     if ((!request.isRequestedSessionIdFromCookie()) && (!request.isRequestedSessionIdFromURL()) && (this.sessionId != null))
/*     */     {
/*     */ 
/* 130 */       sessionTrackingModes = request.getServletContext().getEffectiveSessionTrackingModes();
/* 131 */       this.addSessionCookie = sessionTrackingModes.contains(SessionTrackingMode.COOKIE);
/* 132 */       this.addSessionPathParameter = sessionTrackingModes.contains(SessionTrackingMode.URL);
/*     */     } else {
/* 134 */       this.addSessionCookie = request.isRequestedSessionIdFromCookie();
/* 135 */       this.addSessionPathParameter = request.isRequestedSessionIdFromURL();
/*     */     }
/*     */     
/*     */ 
/* 139 */     if (request.getCookies() != null) {
/* 140 */       for (Cookie requestCookie : request.getCookies()) {
/* 141 */         this.cookies.add(requestCookie);
/*     */       }
/*     */     }
/* 144 */     for (Cookie responseCookie : catalinaRequest.getResponse().getCookies()) {
/* 145 */       if (responseCookie.getMaxAge() < 0)
/*     */       {
/*     */ 
/* 148 */         Object cookieIterator = this.cookies.iterator();
/* 149 */         while (((Iterator)cookieIterator).hasNext()) {
/* 150 */           Cookie cookie = (Cookie)((Iterator)cookieIterator).next();
/* 151 */           if (cookie.getName().equals(responseCookie.getName())) {
/* 152 */             ((Iterator)cookieIterator).remove();
/*     */           }
/*     */         }
/*     */       } else {
/* 156 */         this.cookies.add(new Cookie(responseCookie.getName(), responseCookie.getValue()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ApplicationPushBuilder path(String path)
/*     */   {
/* 163 */     if (path.startsWith("/")) {
/* 164 */       this.path = path;
/*     */     } else {
/* 166 */       String contextPath = this.baseRequest.getContextPath();
/* 167 */       int len = contextPath.length() + path.length() + 1;
/* 168 */       StringBuilder sb = new StringBuilder(len);
/* 169 */       sb.append(contextPath);
/* 170 */       sb.append('/');
/* 171 */       sb.append(path);
/* 172 */       this.path = sb.toString();
/*     */     }
/* 174 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 180 */     return this.path;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder method(String method)
/*     */   {
/* 186 */     String upperMethod = method.trim().toUpperCase();
/* 187 */     if (DISALLOWED_METHODS.contains(upperMethod))
/*     */     {
/* 189 */       throw new IllegalArgumentException(sm.getString("applicationPushBuilder.methodInvalid", new Object[] { upperMethod }));
/*     */     }
/*     */     
/* 192 */     for (char c : upperMethod.toCharArray()) {
/* 193 */       if (!HttpParser.isToken(c))
/*     */       {
/* 195 */         throw new IllegalArgumentException(sm.getString("applicationPushBuilder.methodNotToken", new Object[] { upperMethod }));
/*     */       }
/*     */     }
/* 198 */     this.method = method;
/* 199 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 205 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder queryString(String queryString)
/*     */   {
/* 211 */     this.queryString = queryString;
/* 212 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getQueryString()
/*     */   {
/* 218 */     return this.queryString;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder sessionId(String sessionId)
/*     */   {
/* 224 */     this.sessionId = sessionId;
/* 225 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSessionId()
/*     */   {
/* 231 */     return this.sessionId;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder addHeader(String name, String value)
/*     */   {
/* 237 */     List<String> values = (List)this.headers.get(name);
/* 238 */     if (values == null) {
/* 239 */       values = new ArrayList();
/* 240 */       this.headers.put(name, values);
/*     */     }
/* 242 */     values.add(value);
/*     */     
/* 244 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder setHeader(String name, String value)
/*     */   {
/* 250 */     List<String> values = (List)this.headers.get(name);
/* 251 */     if (values == null) {
/* 252 */       values = new ArrayList();
/* 253 */       this.headers.put(name, values);
/*     */     } else {
/* 255 */       values.clear();
/*     */     }
/* 257 */     values.add(value);
/*     */     
/* 259 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplicationPushBuilder removeHeader(String name)
/*     */   {
/* 265 */     this.headers.remove(name);
/*     */     
/* 267 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> getHeaderNames()
/*     */   {
/* 273 */     return Collections.unmodifiableSet(this.headers.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHeader(String name)
/*     */   {
/* 279 */     List<String> values = (List)this.headers.get(name);
/* 280 */     if (values == null) {
/* 281 */       return null;
/*     */     }
/* 283 */     return (String)values.get(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void push()
/*     */   {
/* 290 */     if (this.path == null) {
/* 291 */       throw new IllegalStateException(sm.getString("pushBuilder.noPath"));
/*     */     }
/*     */     
/* 294 */     org.apache.coyote.Request pushTarget = new org.apache.coyote.Request();
/*     */     
/* 296 */     pushTarget.method().setString(this.method);
/*     */     
/* 298 */     pushTarget.serverName().setString(this.baseRequest.getServerName());
/* 299 */     pushTarget.setServerPort(this.baseRequest.getServerPort());
/* 300 */     pushTarget.scheme().setString(this.baseRequest.getScheme());
/*     */     
/*     */ 
/* 303 */     for (Iterator localIterator1 = this.headers.entrySet().iterator(); localIterator1.hasNext();) { header = (Map.Entry)localIterator1.next();
/* 304 */       for (String value : (List)header.getValue()) {
/* 305 */         pushTarget.getMimeHeaders().addValue((String)header.getKey()).setString(value);
/*     */       }
/*     */     }
/*     */     
/*     */     Map.Entry<String, List<String>> header;
/* 310 */     int queryIndex = this.path.indexOf('?');
/*     */     
/* 312 */     String pushQueryString = null;
/* 313 */     String pushPath; if (queryIndex > -1) {
/* 314 */       String pushPath = this.path.substring(0, queryIndex);
/* 315 */       if (queryIndex + 1 < this.path.length()) {
/* 316 */         pushQueryString = this.path.substring(queryIndex + 1);
/*     */       }
/*     */     } else {
/* 319 */       pushPath = this.path;
/*     */     }
/*     */     
/*     */ 
/* 323 */     if (this.sessionId != null) {
/* 324 */       if (this.addSessionPathParameter) {
/* 325 */         pushPath = pushPath + ";" + this.sessionPathParameterName + "=" + this.sessionId;
/* 326 */         pushTarget.addPathParameter(this.sessionPathParameterName, this.sessionId);
/*     */       }
/* 328 */       if (this.addSessionCookie) {
/* 329 */         this.cookies.add(new Cookie(this.sessionCookieName, this.sessionId));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 334 */     pushTarget.requestURI().setString(pushPath);
/* 335 */     pushTarget.decodedURI().setString(decode(pushPath, this.catalinaRequest
/* 336 */       .getConnector().getURICharset()));
/*     */     
/*     */ 
/* 339 */     if ((pushQueryString == null) && (this.queryString != null)) {
/* 340 */       pushTarget.queryString().setString(this.queryString);
/* 341 */     } else if ((pushQueryString != null) && (this.queryString == null)) {
/* 342 */       pushTarget.queryString().setString(pushQueryString);
/* 343 */     } else if ((pushQueryString != null) && (this.queryString != null)) {
/* 344 */       pushTarget.queryString().setString(pushQueryString + "&" + this.queryString);
/*     */     }
/*     */     
/*     */ 
/* 348 */     setHeader("cookie", generateCookieHeader(this.cookies, this.catalinaRequest
/* 349 */       .getContext().getCookieProcessor()));
/*     */     
/* 351 */     this.coyoteRequest.action(ActionCode.PUSH_REQUEST, pushTarget);
/*     */     
/*     */ 
/* 354 */     pushTarget = null;
/* 355 */     this.path = null;
/* 356 */     this.headers.remove("if-none-match");
/* 357 */     this.headers.remove("if-modified-since");
/*     */   }
/*     */   
/*     */ 
/*     */   static String decode(String input, Charset charset)
/*     */   {
/* 363 */     int start = input.indexOf('%');
/* 364 */     int end = 0;
/*     */     
/*     */ 
/* 367 */     if (start == -1) {
/* 368 */       return input;
/*     */     }
/*     */     
/* 371 */     StringBuilder result = new StringBuilder(input.length());
/* 372 */     while (start != -1)
/*     */     {
/*     */ 
/* 375 */       result.append(input.substring(end, start));
/*     */       
/* 377 */       end = start + 3;
/* 378 */       while ((end < input.length()) && (input.charAt(end) == '%')) {
/* 379 */         end += 3;
/*     */       }
/* 381 */       result.append(decodePercentSequence(input.substring(start, end), charset));
/* 382 */       start = input.indexOf('%', end);
/*     */     }
/*     */     
/* 385 */     result.append(input.substring(end));
/*     */     
/* 387 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static String decodePercentSequence(String sequence, Charset charset)
/*     */   {
/* 392 */     byte[] bytes = new byte[sequence.length() / 3];
/* 393 */     for (int i = 0; i < bytes.length; i += 3)
/*     */     {
/* 395 */       bytes[i] = ((byte)((HexUtils.getDec(sequence.charAt(1 + 3 * i)) << 4) + HexUtils.getDec(sequence.charAt(2 + 3 * i))));
/*     */     }
/*     */     
/* 398 */     return new String(bytes, charset);
/*     */   }
/*     */   
/*     */   private static String generateCookieHeader(List<Cookie> cookies, CookieProcessor cookieProcessor)
/*     */   {
/* 403 */     StringBuilder result = new StringBuilder();
/* 404 */     boolean first = true;
/* 405 */     for (Cookie cookie : cookies) {
/* 406 */       if (first) {
/* 407 */         first = false;
/*     */       } else {
/* 409 */         result.append(';');
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */       result.append(cookieProcessor.generateHeader(cookie));
/*     */     }
/* 418 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationPushBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */