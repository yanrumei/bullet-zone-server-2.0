/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.Assert;
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
/*    */ public abstract class AbstractClientHttpRequestFactoryWrapper
/*    */   implements ClientHttpRequestFactory
/*    */ {
/*    */   private final ClientHttpRequestFactory requestFactory;
/*    */   
/*    */   protected AbstractClientHttpRequestFactoryWrapper(ClientHttpRequestFactory requestFactory)
/*    */   {
/* 42 */     Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
/* 43 */     this.requestFactory = requestFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)
/*    */     throws IOException
/*    */   {
/* 54 */     return createRequest(uri, httpMethod, this.requestFactory);
/*    */   }
/*    */   
/*    */   protected abstract ClientHttpRequest createRequest(URI paramURI, HttpMethod paramHttpMethod, ClientHttpRequestFactory paramClientHttpRequestFactory)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AbstractClientHttpRequestFactoryWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */