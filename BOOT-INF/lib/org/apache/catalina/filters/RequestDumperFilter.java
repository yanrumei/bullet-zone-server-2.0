/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class RequestDumperFilter
/*     */   implements Filter
/*     */ {
/*     */   private static final String NON_HTTP_REQ_MSG = "Not available. Non-http request.";
/*     */   private static final String NON_HTTP_RES_MSG = "Not available. Non-http response.";
/*  58 */   private static final ThreadLocal<Timestamp> timestamp = new ThreadLocal()
/*     */   {
/*     */     protected RequestDumperFilter.Timestamp initialValue()
/*     */     {
/*  62 */       return new RequestDumperFilter.Timestamp(null);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Log log = LogFactory.getLog(RequestDumperFilter.class);
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
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  88 */     HttpServletRequest hRequest = null;
/*  89 */     HttpServletResponse hResponse = null;
/*     */     
/*  91 */     if ((request instanceof HttpServletRequest)) {
/*  92 */       hRequest = (HttpServletRequest)request;
/*     */     }
/*  94 */     if ((response instanceof HttpServletResponse)) {
/*  95 */       hResponse = (HttpServletResponse)response;
/*     */     }
/*     */     
/*     */ 
/*  99 */     doLog("START TIME        ", getTimestamp());
/*     */     
/* 101 */     if (hRequest == null) {
/* 102 */       doLog("        requestURI", "Not available. Non-http request.");
/* 103 */       doLog("          authType", "Not available. Non-http request.");
/*     */     } else {
/* 105 */       doLog("        requestURI", hRequest.getRequestURI());
/* 106 */       doLog("          authType", hRequest.getAuthType());
/*     */     }
/*     */     
/* 109 */     doLog(" characterEncoding", request.getCharacterEncoding());
/* 110 */     doLog("     contentLength", 
/* 111 */       Long.toString(request.getContentLengthLong()));
/* 112 */     doLog("       contentType", request.getContentType());
/*     */     
/* 114 */     if (hRequest == null) {
/* 115 */       doLog("       contextPath", "Not available. Non-http request.");
/* 116 */       doLog("            cookie", "Not available. Non-http request.");
/* 117 */       doLog("            header", "Not available. Non-http request.");
/*     */     } else {
/* 119 */       doLog("       contextPath", hRequest.getContextPath());
/* 120 */       Cookie[] cookies = hRequest.getCookies();
/* 121 */       if (cookies != null) {
/* 122 */         for (int i = 0; i < cookies.length; i++) {
/* 123 */           doLog("            cookie", cookies[i].getName() + "=" + cookies[i]
/* 124 */             .getValue());
/*     */         }
/*     */       }
/* 127 */       Enumeration<String> hnames = hRequest.getHeaderNames();
/* 128 */       while (hnames.hasMoreElements()) {
/* 129 */         String hname = (String)hnames.nextElement();
/* 130 */         Enumeration<String> hvalues = hRequest.getHeaders(hname);
/* 131 */         while (hvalues.hasMoreElements()) {
/* 132 */           String hvalue = (String)hvalues.nextElement();
/* 133 */           doLog("            header", hname + "=" + hvalue);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 138 */     doLog("            locale", request.getLocale().toString());
/*     */     
/* 140 */     if (hRequest == null) {
/* 141 */       doLog("            method", "Not available. Non-http request.");
/*     */     } else {
/* 143 */       doLog("            method", hRequest.getMethod());
/*     */     }
/*     */     
/* 146 */     Enumeration<String> pnames = request.getParameterNames();
/* 147 */     String[] pvalues; while (pnames.hasMoreElements()) {
/* 148 */       String pname = (String)pnames.nextElement();
/* 149 */       pvalues = request.getParameterValues(pname);
/* 150 */       StringBuilder result = new StringBuilder(pname);
/* 151 */       result.append('=');
/* 152 */       for (int i = 0; i < pvalues.length; i++) {
/* 153 */         if (i > 0) {
/* 154 */           result.append(", ");
/*     */         }
/* 156 */         result.append(pvalues[i]);
/*     */       }
/* 158 */       doLog("         parameter", result.toString());
/*     */     }
/*     */     
/* 161 */     if (hRequest == null) {
/* 162 */       doLog("          pathInfo", "Not available. Non-http request.");
/*     */     } else {
/* 164 */       doLog("          pathInfo", hRequest.getPathInfo());
/*     */     }
/*     */     
/* 167 */     doLog("          protocol", request.getProtocol());
/*     */     
/* 169 */     if (hRequest == null) {
/* 170 */       doLog("       queryString", "Not available. Non-http request.");
/*     */     } else {
/* 172 */       doLog("       queryString", hRequest.getQueryString());
/*     */     }
/*     */     
/* 175 */     doLog("        remoteAddr", request.getRemoteAddr());
/* 176 */     doLog("        remoteHost", request.getRemoteHost());
/*     */     
/* 178 */     if (hRequest == null) {
/* 179 */       doLog("        remoteUser", "Not available. Non-http request.");
/* 180 */       doLog("requestedSessionId", "Not available. Non-http request.");
/*     */     } else {
/* 182 */       doLog("        remoteUser", hRequest.getRemoteUser());
/* 183 */       doLog("requestedSessionId", hRequest.getRequestedSessionId());
/*     */     }
/*     */     
/* 186 */     doLog("            scheme", request.getScheme());
/* 187 */     doLog("        serverName", request.getServerName());
/* 188 */     doLog("        serverPort", 
/* 189 */       Integer.toString(request.getServerPort()));
/*     */     
/* 191 */     if (hRequest == null) {
/* 192 */       doLog("       servletPath", "Not available. Non-http request.");
/*     */     } else {
/* 194 */       doLog("       servletPath", hRequest.getServletPath());
/*     */     }
/*     */     
/* 197 */     doLog("          isSecure", 
/* 198 */       Boolean.valueOf(request.isSecure()).toString());
/* 199 */     doLog("------------------", "--------------------------------------------");
/*     */     
/*     */ 
/*     */ 
/* 203 */     chain.doFilter(request, response);
/*     */     
/*     */ 
/* 206 */     doLog("------------------", "--------------------------------------------");
/*     */     
/* 208 */     if (hRequest == null) {
/* 209 */       doLog("          authType", "Not available. Non-http request.");
/*     */     } else {
/* 211 */       doLog("          authType", hRequest.getAuthType());
/*     */     }
/*     */     
/* 214 */     doLog("       contentType", response.getContentType());
/*     */     
/* 216 */     if (hResponse == null) {
/* 217 */       doLog("            header", "Not available. Non-http response.");
/*     */     } else {
/* 219 */       Iterable<String> rhnames = hResponse.getHeaderNames();
/* 220 */       for (pvalues = rhnames.iterator(); pvalues.hasNext();) { rhname = (String)pvalues.next();
/* 221 */         Iterable<String> rhvalues = hResponse.getHeaders(rhname);
/* 222 */         for (String rhvalue : rhvalues) {
/* 223 */           doLog("            header", rhname + "=" + rhvalue);
/*     */         }
/*     */       }
/*     */     }
/*     */     String rhname;
/* 228 */     if (hRequest == null) {
/* 229 */       doLog("        remoteUser", "Not available. Non-http request.");
/*     */     } else {
/* 231 */       doLog("        remoteUser", hRequest.getRemoteUser());
/*     */     }
/*     */     
/* 234 */     if (hResponse == null) {
/* 235 */       doLog("        remoteUser", "Not available. Non-http response.");
/*     */     } else {
/* 237 */       doLog("            status", 
/* 238 */         Integer.toString(hResponse.getStatus()));
/*     */     }
/*     */     
/* 241 */     doLog("END TIME          ", getTimestamp());
/* 242 */     doLog("==================", "============================================");
/*     */   }
/*     */   
/*     */   private void doLog(String attribute, String value)
/*     */   {
/* 247 */     StringBuilder sb = new StringBuilder(80);
/* 248 */     sb.append(Thread.currentThread().getName());
/* 249 */     sb.append(' ');
/* 250 */     sb.append(attribute);
/* 251 */     sb.append('=');
/* 252 */     sb.append(value);
/* 253 */     log.info(sb.toString());
/*     */   }
/*     */   
/*     */   private String getTimestamp() {
/* 257 */     Timestamp ts = (Timestamp)timestamp.get();
/* 258 */     long currentTime = System.currentTimeMillis();
/*     */     
/* 260 */     if (ts.date.getTime() + 999L < currentTime) {
/* 261 */       ts.date.setTime(currentTime - currentTime % 1000L);
/* 262 */       ts.update();
/*     */     }
/* 264 */     return ts.dateString;
/*     */   }
/*     */   
/*     */ 
/*     */   public void init(FilterConfig filterConfig)
/*     */     throws ServletException
/*     */   {}
/*     */   
/*     */ 
/*     */   public void destroy() {}
/*     */   
/*     */ 
/*     */   private static final class Timestamp
/*     */   {
/* 278 */     private final Date date = new Date(0L);
/* 279 */     private final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
/*     */     
/* 281 */     private String dateString = this.format.format(this.date);
/*     */     
/* 283 */     private void update() { this.dateString = this.format.format(this.date); }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\RequestDumperFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */