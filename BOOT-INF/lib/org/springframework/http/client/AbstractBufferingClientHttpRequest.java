/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ abstract class AbstractBufferingClientHttpRequest
/*    */   extends AbstractClientHttpRequest
/*    */ {
/* 34 */   private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);
/*    */   
/*    */   protected OutputStream getBodyInternal(HttpHeaders headers)
/*    */     throws IOException
/*    */   {
/* 39 */     return this.bufferedOutput;
/*    */   }
/*    */   
/*    */   protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException
/*    */   {
/* 44 */     byte[] bytes = this.bufferedOutput.toByteArray();
/* 45 */     if (headers.getContentLength() < 0L) {
/* 46 */       headers.setContentLength(bytes.length);
/*    */     }
/* 48 */     ClientHttpResponse result = executeInternal(headers, bytes);
/* 49 */     this.bufferedOutput = null;
/* 50 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract ClientHttpResponse executeInternal(HttpHeaders paramHttpHeaders, byte[] paramArrayOfByte)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AbstractBufferingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */