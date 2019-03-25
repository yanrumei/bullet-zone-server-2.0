/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.websocket.server.HandshakeRequest;
/*     */ import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
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
/*     */ public class WsHandshakeRequest
/*     */   implements HandshakeRequest
/*     */ {
/*     */   private final URI requestUri;
/*     */   private final Map<String, List<String>> parameterMap;
/*     */   private final String queryString;
/*     */   private final Principal userPrincipal;
/*     */   private final Map<String, List<String>> headers;
/*     */   private final Object httpSession;
/*     */   private volatile HttpServletRequest request;
/*     */   
/*     */   public WsHandshakeRequest(HttpServletRequest request, Map<String, String> pathParams)
/*     */   {
/*  52 */     this.request = request;
/*     */     
/*  54 */     this.queryString = request.getQueryString();
/*  55 */     this.userPrincipal = request.getUserPrincipal();
/*  56 */     this.httpSession = request.getSession(false);
/*     */     
/*     */ 
/*  59 */     StringBuilder sb = new StringBuilder(request.getRequestURI());
/*  60 */     if (this.queryString != null) {
/*  61 */       sb.append("?");
/*  62 */       sb.append(this.queryString);
/*     */     }
/*     */     try {
/*  65 */       this.requestUri = new URI(sb.toString());
/*     */     } catch (URISyntaxException e) {
/*  67 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */     
/*     */ 
/*  71 */     Map<String, String[]> originalParameters = request.getParameterMap();
/*     */     
/*  73 */     Map<String, List<String>> newParameters = new HashMap(originalParameters.size());
/*  74 */     for (Map.Entry<String, String[]> entry : originalParameters.entrySet()) {
/*  75 */       newParameters.put(entry.getKey(), 
/*  76 */         Collections.unmodifiableList(
/*  77 */         Arrays.asList((Object[])entry.getValue())));
/*     */     }
/*  79 */     for (Map.Entry<String, String> entry : pathParams.entrySet()) {
/*  80 */       newParameters.put(entry.getKey(), 
/*  81 */         Collections.unmodifiableList(
/*  82 */         Arrays.asList(new String[] {(String)entry.getValue() })));
/*     */     }
/*  84 */     this.parameterMap = Collections.unmodifiableMap(newParameters);
/*     */     
/*     */ 
/*  87 */     Object newHeaders = new CaseInsensitiveKeyMap();
/*     */     
/*  89 */     Enumeration<String> headerNames = request.getHeaderNames();
/*  90 */     while (headerNames.hasMoreElements()) {
/*  91 */       String headerName = (String)headerNames.nextElement();
/*     */       
/*  93 */       ((Map)newHeaders).put(headerName, Collections.unmodifiableList(
/*  94 */         Collections.list(request.getHeaders(headerName))));
/*     */     }
/*     */     
/*  97 */     this.headers = Collections.unmodifiableMap((Map)newHeaders);
/*     */   }
/*     */   
/*     */   public URI getRequestURI()
/*     */   {
/* 102 */     return this.requestUri;
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getParameterMap()
/*     */   {
/* 107 */     return this.parameterMap;
/*     */   }
/*     */   
/*     */   public String getQueryString()
/*     */   {
/* 112 */     return this.queryString;
/*     */   }
/*     */   
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 117 */     return this.userPrincipal;
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getHeaders()
/*     */   {
/* 122 */     return this.headers;
/*     */   }
/*     */   
/*     */   public boolean isUserInRole(String role)
/*     */   {
/* 127 */     if (this.request == null) {
/* 128 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 131 */     return this.request.isUserInRole(role);
/*     */   }
/*     */   
/*     */   public Object getHttpSession()
/*     */   {
/* 136 */     return this.httpSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void finished()
/*     */   {
/* 148 */     this.request = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsHandshakeRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */