/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RestCsrfPreventionFilter
/*     */   extends CsrfPreventionFilterBase
/*     */ {
/*     */   private static enum MethodType
/*     */   {
/*  78 */     NON_MODIFYING_METHOD,  MODIFYING_METHOD;
/*     */     
/*     */     private MethodType() {} }
/*     */   
/*  82 */   private static final Pattern NON_MODIFYING_METHODS_PATTERN = Pattern.compile("GET|HEAD|OPTIONS");
/*     */   
/*  84 */   private Set<String> pathsAcceptingParams = new HashSet();
/*     */   
/*  86 */   private String pathsDelimiter = ",";
/*     */   
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  92 */     if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse))) {
/*  93 */       MethodType mType = MethodType.MODIFYING_METHOD;
/*  94 */       String method = ((HttpServletRequest)request).getMethod();
/*  95 */       if ((method != null) && (NON_MODIFYING_METHODS_PATTERN.matcher(method).matches())) {
/*  96 */         mType = MethodType.NON_MODIFYING_METHOD;
/*     */       }
/*     */       RestCsrfPreventionStrategy strategy;
/*     */       RestCsrfPreventionStrategy strategy;
/* 100 */       switch (mType) {
/*     */       case NON_MODIFYING_METHOD: 
/* 102 */         strategy = new FetchRequest(null);
/* 103 */         break;
/*     */       default: 
/* 105 */         strategy = new StateChangingRequest(null);
/*     */       }
/*     */       
/*     */       
/* 109 */       if (!strategy.apply((HttpServletRequest)request, (HttpServletResponse)response)) {
/* 110 */         return;
/*     */       }
/*     */     }
/* 113 */     chain.doFilter(request, response);
/*     */   }
/*     */   
/*     */   private static abstract class RestCsrfPreventionStrategy
/*     */   {
/*     */     abstract boolean apply(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws IOException;
/*     */     
/*     */     protected String extractNonceFromRequestHeader(HttpServletRequest request, String key)
/*     */     {
/* 122 */       return request.getHeader(key);
/*     */     }
/*     */     
/*     */     protected String[] extractNonceFromRequestParams(HttpServletRequest request, String key) {
/* 126 */       return request.getParameterValues(key);
/*     */     }
/*     */     
/*     */     protected void storeNonceToResponse(HttpServletResponse response, String key, String value) {
/* 130 */       response.setHeader(key, value);
/*     */     }
/*     */     
/*     */     protected String extractNonceFromSession(HttpSession session, String key) {
/* 134 */       return session == null ? null : (String)session.getAttribute(key);
/*     */     }
/*     */     
/*     */ 
/* 138 */     protected void storeNonceToSession(HttpSession session, String key, Object value) { session.setAttribute(key, value); }
/*     */   }
/*     */   
/*     */   private class StateChangingRequest extends RestCsrfPreventionFilter.RestCsrfPreventionStrategy {
/* 142 */     private StateChangingRequest() { super(); }
/*     */     
/*     */     public boolean apply(HttpServletRequest request, HttpServletResponse response)
/*     */       throws IOException
/*     */     {
/* 147 */       if (isValidStateChangingRequest(
/* 148 */         extractNonceFromRequest(request), 
/* 149 */         extractNonceFromSession(request.getSession(false), "org.apache.catalina.filters.CSRF_REST_NONCE")))
/*     */       {
/* 151 */         return true;
/*     */       }
/*     */       
/* 154 */       storeNonceToResponse(response, "X-CSRF-Token", "Required");
/*     */       
/* 156 */       response.sendError(RestCsrfPreventionFilter.this.getDenyStatus(), FilterBase.sm
/* 157 */         .getString("restCsrfPreventionFilter.invalidNonce"));
/* 158 */       return false;
/*     */     }
/*     */     
/*     */     private boolean isValidStateChangingRequest(String reqNonce, String sessionNonce) {
/* 162 */       return (reqNonce != null) && (sessionNonce != null) && 
/* 163 */         (Objects.equals(reqNonce, sessionNonce));
/*     */     }
/*     */     
/*     */     private String extractNonceFromRequest(HttpServletRequest request) {
/* 167 */       String nonceFromRequest = extractNonceFromRequestHeader(request, "X-CSRF-Token");
/*     */       
/* 169 */       if (((nonceFromRequest == null) || (Objects.equals("", nonceFromRequest))) && 
/* 170 */         (!RestCsrfPreventionFilter.this.getPathsAcceptingParams().isEmpty()) && 
/* 171 */         (RestCsrfPreventionFilter.this.getPathsAcceptingParams().contains(RestCsrfPreventionFilter.this.getRequestedPath(request)))) {
/* 172 */         nonceFromRequest = extractNonceFromRequestParams(request);
/*     */       }
/* 174 */       return nonceFromRequest;
/*     */     }
/*     */     
/*     */     private String extractNonceFromRequestParams(HttpServletRequest request) {
/* 178 */       String[] params = extractNonceFromRequestParams(request, "X-CSRF-Token");
/*     */       
/* 180 */       if ((params != null) && (params.length > 0)) {
/* 181 */         String nonce = params[0];
/* 182 */         for (String param : params) {
/* 183 */           if (!Objects.equals(param, nonce)) {
/* 184 */             return null;
/*     */           }
/*     */         }
/* 187 */         return nonce;
/*     */       }
/* 189 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 193 */   private class FetchRequest extends RestCsrfPreventionFilter.RestCsrfPreventionStrategy { private FetchRequest() { super(); }
/*     */     
/*     */ 
/*     */     public boolean apply(HttpServletRequest request, HttpServletResponse response)
/*     */     {
/* 198 */       if ("Fetch".equalsIgnoreCase(extractNonceFromRequestHeader(request, "X-CSRF-Token")))
/*     */       {
/* 200 */         String nonceFromSessionStr = extractNonceFromSession(request.getSession(false), "org.apache.catalina.filters.CSRF_REST_NONCE");
/*     */         
/* 202 */         if (nonceFromSessionStr == null) {
/* 203 */           nonceFromSessionStr = RestCsrfPreventionFilter.this.generateNonce();
/* 204 */           storeNonceToSession((HttpSession)Objects.requireNonNull(request.getSession(true)), "org.apache.catalina.filters.CSRF_REST_NONCE", nonceFromSessionStr);
/*     */         }
/*     */         
/* 207 */         storeNonceToResponse(response, "X-CSRF-Token", nonceFromSessionStr);
/*     */       }
/*     */       
/* 210 */       return true;
/*     */     }
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
/*     */   public void setPathsAcceptingParams(String pathsList)
/*     */   {
/* 228 */     if (pathsList != null) {
/* 229 */       for (String element : pathsList.split(this.pathsDelimiter)) {
/* 230 */         this.pathsAcceptingParams.add(element.trim());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getPathsAcceptingParams() {
/* 236 */     return this.pathsAcceptingParams;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\RestCsrfPreventionFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */