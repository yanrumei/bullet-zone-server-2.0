/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.StreamUtils;
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
/*    */ final class BufferingClientHttpRequestWrapper
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final ClientHttpRequest request;
/*    */   
/*    */   BufferingClientHttpRequestWrapper(ClientHttpRequest request)
/*    */   {
/* 38 */     this.request = request;
/*    */   }
/*    */   
/*    */ 
/*    */   public HttpMethod getMethod()
/*    */   {
/* 44 */     return this.request.getMethod();
/*    */   }
/*    */   
/*    */   public URI getURI()
/*    */   {
/* 49 */     return this.request.getURI();
/*    */   }
/*    */   
/*    */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException
/*    */   {
/* 54 */     this.request.getHeaders().putAll(headers);
/* 55 */     StreamUtils.copy(bufferedOutput, this.request.getBody());
/* 56 */     ClientHttpResponse response = this.request.execute();
/* 57 */     return new BufferingClientHttpResponseWrapper(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\BufferingClientHttpRequestWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */