/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BufferingClientHttpRequestFactory
/*    */   extends AbstractClientHttpRequestFactoryWrapper
/*    */ {
/*    */   public BufferingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory)
/*    */   {
/* 41 */     super(requestFactory);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory)
/*    */     throws IOException
/*    */   {
/* 49 */     ClientHttpRequest request = requestFactory.createRequest(uri, httpMethod);
/* 50 */     if (shouldBuffer(uri, httpMethod)) {
/* 51 */       return new BufferingClientHttpRequestWrapper(request);
/*    */     }
/*    */     
/* 54 */     return request;
/*    */   }
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
/*    */   protected boolean shouldBuffer(URI uri, HttpMethod httpMethod)
/*    */   {
/* 68 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\BufferingClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */