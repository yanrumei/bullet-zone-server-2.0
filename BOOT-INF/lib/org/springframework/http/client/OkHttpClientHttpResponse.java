/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import com.squareup.okhttp.Headers;
/*    */ import com.squareup.okhttp.Response;
/*    */ import com.squareup.okhttp.ResponseBody;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ class OkHttpClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final Response response;
/*    */   private HttpHeaders headers;
/*    */   
/*    */   public OkHttpClientHttpResponse(Response response)
/*    */   {
/* 43 */     Assert.notNull(response, "Response must not be null");
/* 44 */     this.response = response;
/*    */   }
/*    */   
/*    */ 
/*    */   public int getRawStatusCode()
/*    */   {
/* 50 */     return this.response.code();
/*    */   }
/*    */   
/*    */   public String getStatusText()
/*    */   {
/* 55 */     return this.response.message();
/*    */   }
/*    */   
/*    */   public InputStream getBody() throws IOException
/*    */   {
/* 60 */     return this.response.body().byteStream();
/*    */   }
/*    */   
/*    */   public HttpHeaders getHeaders()
/*    */   {
/* 65 */     if (this.headers == null) {
/* 66 */       HttpHeaders headers = new HttpHeaders();
/* 67 */       for (Iterator localIterator1 = this.response.headers().names().iterator(); localIterator1.hasNext();) { headerName = (String)localIterator1.next();
/* 68 */         for (String headerValue : this.response.headers(headerName))
/* 69 */           headers.add(headerName, headerValue);
/*    */       }
/*    */       String headerName;
/* 72 */       this.headers = headers;
/*    */     }
/* 74 */     return this.headers;
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/*    */     try {
/* 80 */       this.response.body().close();
/*    */     }
/*    */     catch (IOException localIOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\OkHttpClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */