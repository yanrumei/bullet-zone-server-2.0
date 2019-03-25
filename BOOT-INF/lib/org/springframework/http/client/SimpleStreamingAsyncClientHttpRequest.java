/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ final class SimpleStreamingAsyncClientHttpRequest
/*     */   extends AbstractAsyncClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final int chunkSize;
/*     */   private OutputStream body;
/*     */   private final boolean outputStreaming;
/*     */   private final AsyncListenableTaskExecutor taskExecutor;
/*     */   
/*     */   SimpleStreamingAsyncClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming, AsyncListenableTaskExecutor taskExecutor)
/*     */   {
/*  57 */     this.connection = connection;
/*  58 */     this.chunkSize = chunkSize;
/*  59 */     this.outputStreaming = outputStreaming;
/*  60 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpMethod getMethod()
/*     */   {
/*  66 */     return HttpMethod.resolve(this.connection.getRequestMethod());
/*     */   }
/*     */   
/*     */   public URI getURI()
/*     */   {
/*     */     try {
/*  72 */       return this.connection.getURL().toURI();
/*     */     }
/*     */     catch (URISyntaxException ex)
/*     */     {
/*  76 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException
/*     */   {
/*  82 */     if (this.body == null) {
/*  83 */       if (this.outputStreaming) {
/*  84 */         int contentLength = (int)headers.getContentLength();
/*  85 */         if (contentLength >= 0) {
/*  86 */           this.connection.setFixedLengthStreamingMode(contentLength);
/*     */         }
/*     */         else {
/*  89 */           this.connection.setChunkedStreamingMode(this.chunkSize);
/*     */         }
/*     */       }
/*  92 */       SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/*  93 */       this.connection.connect();
/*  94 */       this.body = this.connection.getOutputStream();
/*     */     }
/*  96 */     return StreamUtils.nonClosing(this.body);
/*     */   }
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(final HttpHeaders headers) throws IOException
/*     */   {
/* 101 */     this.taskExecutor.submitListenable(new Callable()
/*     */     {
/*     */       public ClientHttpResponse call() throws Exception {
/*     */         try {
/* 105 */           if (SimpleStreamingAsyncClientHttpRequest.this.body != null) {
/* 106 */             SimpleStreamingAsyncClientHttpRequest.this.body.close();
/*     */           }
/*     */           else {
/* 109 */             SimpleBufferingClientHttpRequest.addHeaders(SimpleStreamingAsyncClientHttpRequest.this.connection, headers);
/* 110 */             SimpleStreamingAsyncClientHttpRequest.this.connection.connect();
/*     */             
/* 112 */             SimpleStreamingAsyncClientHttpRequest.this.connection.getResponseCode();
/*     */           }
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */         
/*     */ 
/* 118 */         return new SimpleClientHttpResponse(SimpleStreamingAsyncClientHttpRequest.this.connection);
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\SimpleStreamingAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */