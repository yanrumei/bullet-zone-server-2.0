/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import okhttp3.Cache;
/*     */ import okhttp3.Dispatcher;
/*     */ import okhttp3.MediaType;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.OkHttpClient.Builder;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Request.Builder;
/*     */ import okhttp3.RequestBody;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class OkHttp3ClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private OkHttpClient client;
/*     */   private final boolean defaultClient;
/*     */   
/*     */   public OkHttp3ClientHttpRequestFactory()
/*     */   {
/*  57 */     this.client = new OkHttpClient();
/*  58 */     this.defaultClient = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OkHttp3ClientHttpRequestFactory(OkHttpClient client)
/*     */   {
/*  66 */     Assert.notNull(client, "OkHttpClient must not be null");
/*  67 */     this.client = client;
/*  68 */     this.defaultClient = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadTimeout(int readTimeout)
/*     */   {
/*  80 */     this.client = this.client.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWriteTimeout(int writeTimeout)
/*     */   {
/*  91 */     this.client = this.client.newBuilder().writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(int connectTimeout)
/*     */   {
/* 102 */     this.client = this.client.newBuilder().connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).build();
/*     */   }
/*     */   
/*     */ 
/*     */   public ClientHttpRequest createRequest(URI uri, org.springframework.http.HttpMethod httpMethod)
/*     */   {
/* 108 */     return new OkHttp3ClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, org.springframework.http.HttpMethod httpMethod)
/*     */   {
/* 113 */     return new OkHttp3AsyncClientHttpRequest(this.client, uri, httpMethod);
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */     throws IOException
/*     */   {
/* 119 */     if (this.defaultClient)
/*     */     {
/* 121 */       if (this.client.cache() != null) {
/* 122 */         this.client.cache().close();
/*     */       }
/* 124 */       this.client.dispatcher().executorService().shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static Request buildRequest(HttpHeaders headers, byte[] content, URI uri, org.springframework.http.HttpMethod method)
/*     */     throws MalformedURLException
/*     */   {
/* 132 */     MediaType contentType = getContentType(headers);
/*     */     
/*     */ 
/* 135 */     RequestBody body = (content.length > 0) || (okhttp3.internal.http.HttpMethod.requiresRequestBody(method.name())) ? RequestBody.create(contentType, content) : null;
/*     */     
/* 137 */     Request.Builder builder = new Request.Builder().url(uri.toURL()).method(method.name(), body);
/* 138 */     for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/* 139 */       headerName = (String)entry.getKey();
/* 140 */       for (String headerValue : (List)entry.getValue())
/* 141 */         builder.addHeader(headerName, headerValue);
/*     */     }
/*     */     String headerName;
/* 144 */     return builder.build();
/*     */   }
/*     */   
/*     */   private static MediaType getContentType(HttpHeaders headers) {
/* 148 */     String rawContentType = headers.getFirst("Content-Type");
/* 149 */     return StringUtils.hasText(rawContentType) ? MediaType.parse(rawContentType) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\OkHttp3ClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */