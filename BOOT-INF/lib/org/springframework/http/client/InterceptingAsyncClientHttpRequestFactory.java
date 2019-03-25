/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InterceptingAsyncClientHttpRequestFactory
/*    */   implements AsyncClientHttpRequestFactory
/*    */ {
/*    */   private AsyncClientHttpRequestFactory delegate;
/*    */   private List<AsyncClientHttpRequestInterceptor> interceptors;
/*    */   
/*    */   public InterceptingAsyncClientHttpRequestFactory(AsyncClientHttpRequestFactory delegate, List<AsyncClientHttpRequestInterceptor> interceptors)
/*    */   {
/* 49 */     this.delegate = delegate;
/* 50 */     this.interceptors = (interceptors != null ? interceptors : Collections.emptyList());
/*    */   }
/*    */   
/*    */ 
/*    */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod method)
/*    */   {
/* 56 */     return new InterceptingAsyncClientHttpRequest(this.delegate, this.interceptors, uri, method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\InterceptingAsyncClientHttpRequestFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */