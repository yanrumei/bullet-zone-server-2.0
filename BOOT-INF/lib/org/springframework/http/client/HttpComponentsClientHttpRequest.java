/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
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
/*     */ final class HttpComponentsClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final HttpClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   
/*     */   HttpComponentsClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context)
/*     */   {
/*  59 */     this.httpClient = client;
/*  60 */     this.httpRequest = request;
/*  61 */     this.httpContext = context;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpMethod getMethod()
/*     */   {
/*  67 */     return HttpMethod.resolve(this.httpRequest.getMethod());
/*     */   }
/*     */   
/*     */   public URI getURI()
/*     */   {
/*  72 */     return this.httpRequest.getURI();
/*     */   }
/*     */   
/*     */   HttpContext getHttpContext() {
/*  76 */     return this.httpContext;
/*     */   }
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
/*     */     throws IOException
/*     */   {
/*  82 */     addHeaders(this.httpRequest, headers);
/*     */     
/*  84 */     if ((this.httpRequest instanceof HttpEntityEnclosingRequest)) {
/*  85 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  86 */       HttpEntity requestEntity = new ByteArrayEntity(bufferedOutput);
/*  87 */       entityEnclosingRequest.setEntity(requestEntity);
/*     */     }
/*  89 */     HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
/*  90 */     return new HttpComponentsClientHttpResponse(httpResponse);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void addHeaders(HttpUriRequest httpRequest, HttpHeaders headers)
/*     */   {
/* 100 */     for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/* 101 */       headerName = (String)entry.getKey();
/* 102 */       if ("Cookie".equalsIgnoreCase(headerName)) {
/* 103 */         headerValue = StringUtils.collectionToDelimitedString((Collection)entry.getValue(), "; ");
/* 104 */         httpRequest.addHeader(headerName, headerValue);
/*     */       }
/* 106 */       else if ((!"Content-Length".equalsIgnoreCase(headerName)) && 
/* 107 */         (!"Transfer-Encoding".equalsIgnoreCase(headerName))) {
/* 108 */         for (String headerValue : (List)entry.getValue()) {
/* 109 */           httpRequest.addHeader(headerName, headerValue);
/*     */         }
/*     */       }
/*     */     }
/*     */     String headerName;
/*     */     String headerValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\HttpComponentsClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */