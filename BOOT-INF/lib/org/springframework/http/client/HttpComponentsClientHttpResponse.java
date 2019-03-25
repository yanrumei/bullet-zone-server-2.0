/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.StatusLine;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class HttpComponentsClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpResponse httpResponse;
/*    */   private HttpHeaders headers;
/*    */   
/*    */   HttpComponentsClientHttpResponse(HttpResponse httpResponse)
/*    */   {
/* 50 */     this.httpResponse = httpResponse;
/*    */   }
/*    */   
/*    */   public int getRawStatusCode()
/*    */     throws IOException
/*    */   {
/* 56 */     return this.httpResponse.getStatusLine().getStatusCode();
/*    */   }
/*    */   
/*    */   public String getStatusText() throws IOException
/*    */   {
/* 61 */     return this.httpResponse.getStatusLine().getReasonPhrase();
/*    */   }
/*    */   
/*    */   public HttpHeaders getHeaders()
/*    */   {
/* 66 */     if (this.headers == null) {
/* 67 */       this.headers = new HttpHeaders();
/* 68 */       for (Header header : this.httpResponse.getAllHeaders()) {
/* 69 */         this.headers.add(header.getName(), header.getValue());
/*    */       }
/*    */     }
/* 72 */     return this.headers;
/*    */   }
/*    */   
/*    */   public InputStream getBody() throws IOException
/*    */   {
/* 77 */     HttpEntity entity = this.httpResponse.getEntity();
/* 78 */     return entity != null ? entity.getContent() : StreamUtils.emptyInput();
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void close()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 2	org/springframework/http/client/HttpComponentsClientHttpResponse:httpResponse	Lorg/apache/http/HttpResponse;
/*    */     //   4: invokeinterface 13 1 0
/*    */     //   9: invokestatic 16	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
/*    */     //   12: aload_0
/*    */     //   13: getfield 2	org/springframework/http/client/HttpComponentsClientHttpResponse:httpResponse	Lorg/apache/http/HttpResponse;
/*    */     //   16: instanceof 17
/*    */     //   19: ifeq +43 -> 62
/*    */     //   22: aload_0
/*    */     //   23: getfield 2	org/springframework/http/client/HttpComponentsClientHttpResponse:httpResponse	Lorg/apache/http/HttpResponse;
/*    */     //   26: checkcast 17	java/io/Closeable
/*    */     //   29: invokeinterface 18 1 0
/*    */     //   34: goto +28 -> 62
/*    */     //   37: astore_1
/*    */     //   38: aload_0
/*    */     //   39: getfield 2	org/springframework/http/client/HttpComponentsClientHttpResponse:httpResponse	Lorg/apache/http/HttpResponse;
/*    */     //   42: instanceof 17
/*    */     //   45: ifeq +15 -> 60
/*    */     //   48: aload_0
/*    */     //   49: getfield 2	org/springframework/http/client/HttpComponentsClientHttpResponse:httpResponse	Lorg/apache/http/HttpResponse;
/*    */     //   52: checkcast 17	java/io/Closeable
/*    */     //   55: invokeinterface 18 1 0
/*    */     //   60: aload_1
/*    */     //   61: athrow
/*    */     //   62: goto +4 -> 66
/*    */     //   65: astore_1
/*    */     //   66: return
/*    */     // Line number table:
/*    */     //   Java source line #87	-> byte code offset #0
/*    */     //   Java source line #90	-> byte code offset #12
/*    */     //   Java source line #91	-> byte code offset #22
/*    */     //   Java source line #90	-> byte code offset #37
/*    */     //   Java source line #91	-> byte code offset #48
/*    */     //   Java source line #97	-> byte code offset #62
/*    */     //   Java source line #95	-> byte code offset #65
/*    */     //   Java source line #98	-> byte code offset #66
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	67	0	this	HttpComponentsClientHttpResponse
/*    */     //   37	24	1	localObject	Object
/*    */     //   65	1	1	localIOException	IOException
/*    */     //   65	1	1	localIOException1	IOException
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	12	37	finally
/*    */     //   0	62	65	java/io/IOException
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\HttpComponentsClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */