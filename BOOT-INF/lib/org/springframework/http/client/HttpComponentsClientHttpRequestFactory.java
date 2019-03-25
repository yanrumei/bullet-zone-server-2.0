/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class HttpComponentsClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private static Class<?> abstractHttpClientClass;
/*     */   private HttpClient httpClient;
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  67 */       abstractHttpClientClass = ClassUtils.forName("org.apache.http.impl.client.AbstractHttpClient", HttpComponentsClientHttpRequestFactory.class
/*  68 */         .getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private boolean bufferRequestBody = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsClientHttpRequestFactory()
/*     */   {
/*  88 */     this.httpClient = HttpClients.createSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsClientHttpRequestFactory(HttpClient httpClient)
/*     */   {
/*  97 */     setHttpClient(httpClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHttpClient(HttpClient httpClient)
/*     */   {
/* 106 */     Assert.notNull(httpClient, "HttpClient must not be null");
/* 107 */     this.httpClient = httpClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpClient getHttpClient()
/*     */   {
/* 115 */     return this.httpClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(int timeout)
/*     */   {
/* 127 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/* 128 */     this.requestConfig = requestConfigBuilder().setConnectTimeout(timeout).build();
/* 129 */     setLegacyConnectionTimeout(getHttpClient(), timeout);
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
/*     */ 
/*     */ 
/*     */   private void setLegacyConnectionTimeout(HttpClient client, int timeout)
/*     */   {
/* 148 */     if ((abstractHttpClientClass != null) && (abstractHttpClientClass.isInstance(client))) {
/* 149 */       client.getParams().setIntParameter("http.connection.timeout", timeout);
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
/*     */   public void setConnectionRequestTimeout(int connectionRequestTimeout)
/*     */   {
/* 163 */     this.requestConfig = requestConfigBuilder().setConnectionRequestTimeout(connectionRequestTimeout).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadTimeout(int timeout)
/*     */   {
/* 175 */     Assert.isTrue(timeout >= 0, "Timeout must be a non-negative value");
/* 176 */     this.requestConfig = requestConfigBuilder().setSocketTimeout(timeout).build();
/* 177 */     setLegacySocketTimeout(getHttpClient(), timeout);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setLegacySocketTimeout(HttpClient client, int timeout)
/*     */   {
/* 189 */     if ((abstractHttpClientClass != null) && (abstractHttpClientClass.isInstance(client))) {
/* 190 */       client.getParams().setIntParameter("http.socket.timeout", timeout);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBufferRequestBody(boolean bufferRequestBody)
/*     */   {
/* 200 */     this.bufferRequestBody = bufferRequestBody;
/*     */   }
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/*     */     throws IOException
/*     */   {
/* 206 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 207 */     postProcessHttpRequest(httpRequest);
/* 208 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 209 */     if (context == null) {
/* 210 */       context = HttpClientContext.create();
/*     */     }
/*     */     
/*     */ 
/* 214 */     if (context.getAttribute("http.request-config") == null)
/*     */     {
/* 216 */       RequestConfig config = null;
/* 217 */       if ((httpRequest instanceof Configurable)) {
/* 218 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 220 */       if (config == null) {
/* 221 */         config = createRequestConfig(getHttpClient());
/*     */       }
/* 223 */       if (config != null) {
/* 224 */         context.setAttribute("http.request-config", config);
/*     */       }
/*     */     }
/*     */     
/* 228 */     if (this.bufferRequestBody) {
/* 229 */       return new HttpComponentsClientHttpRequest(getHttpClient(), httpRequest, context);
/*     */     }
/*     */     
/* 232 */     return new HttpComponentsStreamingClientHttpRequest(getHttpClient(), httpRequest, context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RequestConfig.Builder requestConfigBuilder()
/*     */   {
/* 242 */     return this.requestConfig != null ? RequestConfig.copy(this.requestConfig) : RequestConfig.custom();
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
/*     */   protected RequestConfig createRequestConfig(Object client)
/*     */   {
/* 257 */     if ((client instanceof Configurable)) {
/* 258 */       RequestConfig clientRequestConfig = ((Configurable)client).getConfig();
/* 259 */       return mergeRequestConfig(clientRequestConfig);
/*     */     }
/* 261 */     return this.requestConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RequestConfig mergeRequestConfig(RequestConfig clientConfig)
/*     */   {
/* 272 */     if (this.requestConfig == null) {
/* 273 */       return clientConfig;
/*     */     }
/*     */     
/* 276 */     RequestConfig.Builder builder = RequestConfig.copy(clientConfig);
/* 277 */     int connectTimeout = this.requestConfig.getConnectTimeout();
/* 278 */     if (connectTimeout >= 0) {
/* 279 */       builder.setConnectTimeout(connectTimeout);
/*     */     }
/* 281 */     int connectionRequestTimeout = this.requestConfig.getConnectionRequestTimeout();
/* 282 */     if (connectionRequestTimeout >= 0) {
/* 283 */       builder.setConnectionRequestTimeout(connectionRequestTimeout);
/*     */     }
/* 285 */     int socketTimeout = this.requestConfig.getSocketTimeout();
/* 286 */     if (socketTimeout >= 0) {
/* 287 */       builder.setSocketTimeout(socketTimeout);
/*     */     }
/* 289 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri)
/*     */   {
/* 299 */     switch (httpMethod) {
/*     */     case GET: 
/* 301 */       return new HttpGet(uri);
/*     */     case HEAD: 
/* 303 */       return new HttpHead(uri);
/*     */     case POST: 
/* 305 */       return new HttpPost(uri);
/*     */     case PUT: 
/* 307 */       return new HttpPut(uri);
/*     */     case PATCH: 
/* 309 */       return new HttpPatch(uri);
/*     */     case DELETE: 
/* 311 */       return new HttpDelete(uri);
/*     */     case OPTIONS: 
/* 313 */       return new HttpOptions(uri);
/*     */     case TRACE: 
/* 315 */       return new HttpTrace(uri);
/*     */     }
/* 317 */     throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
/*     */   {
/* 338 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws Exception
/*     */   {
/* 349 */     HttpClient httpClient = getHttpClient();
/* 350 */     if ((httpClient instanceof Closeable)) {
/* 351 */       ((Closeable)httpClient).close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postProcessHttpRequest(HttpUriRequest request) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class HttpDelete
/*     */     extends HttpEntityEnclosingRequestBase
/*     */   {
/*     */     public HttpDelete(URI uri)
/*     */     {
/* 368 */       setURI(uri);
/*     */     }
/*     */     
/*     */     public String getMethod()
/*     */     {
/* 373 */       return "DELETE";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\HttpComponentsClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */