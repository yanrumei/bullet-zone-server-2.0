/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import okhttp3.Headers;
/*    */ import okhttp3.Response;
/*    */ import okhttp3.ResponseBody;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
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
/*    */ class OkHttp3ClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final Response response;
/*    */   private volatile HttpHeaders headers;
/*    */   
/*    */   public OkHttp3ClientHttpResponse(Response response)
/*    */   {
/* 45 */     Assert.notNull(response, "Response must not be null");
/* 46 */     this.response = response;
/*    */   }
/*    */   
/*    */ 
/*    */   public int getRawStatusCode()
/*    */   {
/* 52 */     return this.response.code();
/*    */   }
/*    */   
/*    */   public String getStatusText()
/*    */   {
/* 57 */     return this.response.message();
/*    */   }
/*    */   
/*    */   public InputStream getBody() throws IOException
/*    */   {
/* 62 */     ResponseBody body = this.response.body();
/* 63 */     return body != null ? body.byteStream() : StreamUtils.emptyInput();
/*    */   }
/*    */   
/*    */   public HttpHeaders getHeaders()
/*    */   {
/* 68 */     HttpHeaders headers = this.headers;
/* 69 */     if (headers == null) {
/* 70 */       headers = new HttpHeaders();
/* 71 */       for (Iterator localIterator1 = this.response.headers().names().iterator(); localIterator1.hasNext();) { headerName = (String)localIterator1.next();
/* 72 */         for (String headerValue : this.response.headers(headerName))
/* 73 */           headers.add(headerName, headerValue);
/*    */       }
/*    */       String headerName;
/* 76 */       this.headers = headers;
/*    */     }
/* 78 */     return headers;
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 83 */     ResponseBody body = this.response.body();
/* 84 */     if (body != null) {
/* 85 */       body.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\OkHttp3ClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */