/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.InterceptingAsyncClientHttpRequestFactory;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public abstract class InterceptingAsyncHttpAccessor
/*    */   extends AsyncHttpAccessor
/*    */ {
/* 37 */   private List<AsyncClientHttpRequestInterceptor> interceptors = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setInterceptors(List<AsyncClientHttpRequestInterceptor> interceptors)
/*    */   {
/* 46 */     this.interceptors = interceptors;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<AsyncClientHttpRequestInterceptor> getInterceptors()
/*    */   {
/* 53 */     return this.interceptors;
/*    */   }
/*    */   
/*    */ 
/*    */   public AsyncClientHttpRequestFactory getAsyncRequestFactory()
/*    */   {
/* 59 */     AsyncClientHttpRequestFactory delegate = super.getAsyncRequestFactory();
/* 60 */     if (!CollectionUtils.isEmpty(getInterceptors())) {
/* 61 */       return new InterceptingAsyncClientHttpRequestFactory(delegate, getInterceptors());
/*    */     }
/*    */     
/* 64 */     return delegate;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\support\InterceptingAsyncHttpAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */