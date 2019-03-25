/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*     */ import org.apache.http.impl.nio.client.HttpAsyncClients;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class HttpComponentsAsyncClientHttpRequestFactory
/*     */   extends HttpComponentsClientHttpRequestFactory
/*     */   implements AsyncClientHttpRequestFactory, InitializingBean
/*     */ {
/*     */   private HttpAsyncClient asyncClient;
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory()
/*     */   {
/*  60 */     this.asyncClient = HttpAsyncClients.createSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpAsyncClient asyncClient)
/*     */   {
/*  71 */     setAsyncClient(asyncClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpAsyncClient asyncClient)
/*     */   {
/*  81 */     setAsyncClient(asyncClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpClient httpClient, HttpAsyncClient asyncClient)
/*     */   {
/*  92 */     super(httpClient);
/*  93 */     setAsyncClient(asyncClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpClient httpClient, CloseableHttpAsyncClient asyncClient)
/*     */   {
/* 105 */     super(httpClient);
/* 106 */     setAsyncClient(asyncClient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsyncClient(HttpAsyncClient asyncClient)
/*     */   {
/* 117 */     Assert.notNull(asyncClient, "HttpAsyncClient must not be null");
/* 118 */     this.asyncClient = asyncClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpAsyncClient getAsyncClient()
/*     */   {
/* 128 */     return this.asyncClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setHttpAsyncClient(CloseableHttpAsyncClient asyncClient)
/*     */   {
/* 138 */     this.asyncClient = asyncClient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public CloseableHttpAsyncClient getHttpAsyncClient()
/*     */   {
/* 148 */     Assert.state((this.asyncClient == null) || ((this.asyncClient instanceof CloseableHttpAsyncClient)), "No CloseableHttpAsyncClient - use getAsyncClient() instead");
/*     */     
/* 150 */     return (CloseableHttpAsyncClient)this.asyncClient;
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 156 */     startAsyncClient();
/*     */   }
/*     */   
/*     */   private void startAsyncClient() {
/* 160 */     HttpAsyncClient asyncClient = getAsyncClient();
/* 161 */     if ((asyncClient instanceof CloseableHttpAsyncClient)) {
/* 162 */       CloseableHttpAsyncClient closeableAsyncClient = (CloseableHttpAsyncClient)asyncClient;
/* 163 */       if (!closeableAsyncClient.isRunning()) {
/* 164 */         closeableAsyncClient.start();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException
/*     */   {
/* 171 */     startAsyncClient();
/*     */     
/* 173 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 174 */     postProcessHttpRequest(httpRequest);
/* 175 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 176 */     if (context == null) {
/* 177 */       context = HttpClientContext.create();
/*     */     }
/*     */     
/*     */ 
/* 181 */     if (context.getAttribute("http.request-config") == null)
/*     */     {
/* 183 */       RequestConfig config = null;
/* 184 */       if ((httpRequest instanceof Configurable)) {
/* 185 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 187 */       if (config == null) {
/* 188 */         config = createRequestConfig(getAsyncClient());
/*     */       }
/* 190 */       if (config != null) {
/* 191 */         context.setAttribute("http.request-config", config);
/*     */       }
/*     */     }
/*     */     
/* 195 */     return new HttpComponentsAsyncClientHttpRequest(getAsyncClient(), httpRequest, context);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void destroy()
/*     */     throws java.lang.Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 28	org/springframework/http/client/HttpComponentsClientHttpRequestFactory:destroy	()V
/*     */     //   4: aload_0
/*     */     //   5: invokevirtual 12	org/springframework/http/client/HttpComponentsAsyncClientHttpRequestFactory:getAsyncClient	()Lorg/apache/http/nio/client/HttpAsyncClient;
/*     */     //   8: astore_1
/*     */     //   9: aload_1
/*     */     //   10: instanceof 29
/*     */     //   13: ifeq +12 -> 25
/*     */     //   16: aload_1
/*     */     //   17: checkcast 29	java/io/Closeable
/*     */     //   20: invokeinterface 30 1 0
/*     */     //   25: goto +27 -> 52
/*     */     //   28: astore_2
/*     */     //   29: aload_0
/*     */     //   30: invokevirtual 12	org/springframework/http/client/HttpComponentsAsyncClientHttpRequestFactory:getAsyncClient	()Lorg/apache/http/nio/client/HttpAsyncClient;
/*     */     //   33: astore_3
/*     */     //   34: aload_3
/*     */     //   35: instanceof 29
/*     */     //   38: ifeq +12 -> 50
/*     */     //   41: aload_3
/*     */     //   42: checkcast 29	java/io/Closeable
/*     */     //   45: invokeinterface 30 1 0
/*     */     //   50: aload_2
/*     */     //   51: athrow
/*     */     //   52: return
/*     */     // Line number table:
/*     */     //   Java source line #201	-> byte code offset #0
/*     */     //   Java source line #204	-> byte code offset #4
/*     */     //   Java source line #205	-> byte code offset #9
/*     */     //   Java source line #206	-> byte code offset #16
/*     */     //   Java source line #208	-> byte code offset #25
/*     */     //   Java source line #204	-> byte code offset #28
/*     */     //   Java source line #205	-> byte code offset #34
/*     */     //   Java source line #206	-> byte code offset #41
/*     */     //   Java source line #208	-> byte code offset #50
/*     */     //   Java source line #209	-> byte code offset #52
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	53	0	this	HttpComponentsAsyncClientHttpRequestFactory
/*     */     //   8	9	1	asyncClient	HttpAsyncClient
/*     */     //   28	23	2	localObject	Object
/*     */     //   33	9	3	asyncClient	HttpAsyncClient
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	28	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\HttpComponentsAsyncClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */