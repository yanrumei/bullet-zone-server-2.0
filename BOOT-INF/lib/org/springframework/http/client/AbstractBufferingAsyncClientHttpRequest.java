/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractBufferingAsyncClientHttpRequest
/*    */   extends AbstractAsyncClientHttpRequest
/*    */ {
/* 35 */   private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);
/*    */   
/*    */   protected OutputStream getBodyInternal(HttpHeaders headers)
/*    */     throws IOException
/*    */   {
/* 40 */     return this.bufferedOutput;
/*    */   }
/*    */   
/*    */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers) throws IOException
/*    */   {
/* 45 */     byte[] bytes = this.bufferedOutput.toByteArray();
/* 46 */     if (headers.getContentLength() < 0L) {
/* 47 */       headers.setContentLength(bytes.length);
/*    */     }
/* 49 */     ListenableFuture<ClientHttpResponse> result = executeInternal(headers, bytes);
/* 50 */     this.bufferedOutput = null;
/* 51 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders paramHttpHeaders, byte[] paramArrayOfByte)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AbstractBufferingAsyncClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */