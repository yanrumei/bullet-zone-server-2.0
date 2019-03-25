/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.HttpRequest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class InterceptingClientHttpRequest
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final ClientHttpRequestFactory requestFactory;
/*    */   private final List<ClientHttpRequestInterceptor> interceptors;
/*    */   private HttpMethod method;
/*    */   private URI uri;
/*    */   
/*    */   protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method)
/*    */   {
/* 50 */     this.requestFactory = requestFactory;
/* 51 */     this.interceptors = interceptors;
/* 52 */     this.method = method;
/* 53 */     this.uri = uri;
/*    */   }
/*    */   
/*    */ 
/*    */   public HttpMethod getMethod()
/*    */   {
/* 59 */     return this.method;
/*    */   }
/*    */   
/*    */   public URI getURI()
/*    */   {
/* 64 */     return this.uri;
/*    */   }
/*    */   
/*    */   protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException
/*    */   {
/* 69 */     InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
/* 70 */     return requestExecution.execute(this, bufferedOutput);
/*    */   }
/*    */   
/*    */   private class InterceptingRequestExecution implements ClientHttpRequestExecution
/*    */   {
/*    */     private final Iterator<ClientHttpRequestInterceptor> iterator;
/*    */     
/*    */     public InterceptingRequestExecution()
/*    */     {
/* 79 */       this.iterator = InterceptingClientHttpRequest.this.interceptors.iterator();
/*    */     }
/*    */     
/*    */     public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException
/*    */     {
/* 84 */       if (this.iterator.hasNext()) {
/* 85 */         ClientHttpRequestInterceptor nextInterceptor = (ClientHttpRequestInterceptor)this.iterator.next();
/* 86 */         return nextInterceptor.intercept(request, body, this);
/*    */       }
/*    */       
/* 89 */       ClientHttpRequest delegate = InterceptingClientHttpRequest.this.requestFactory.createRequest(request.getURI(), request.getMethod());
/* 90 */       for (Iterator localIterator1 = request.getHeaders().entrySet().iterator(); localIterator1.hasNext();) { entry = (Map.Entry)localIterator1.next();
/* 91 */         List<String> values = (List)entry.getValue();
/* 92 */         for (String value : values)
/* 93 */           delegate.getHeaders().add((String)entry.getKey(), value);
/*    */       }
/*    */       Map.Entry<String, List<String>> entry;
/* 96 */       if (body.length > 0) {
/* 97 */         StreamUtils.copy(body, delegate.getBody());
/*    */       }
/* 99 */       return delegate.execute();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\InterceptingClientHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */