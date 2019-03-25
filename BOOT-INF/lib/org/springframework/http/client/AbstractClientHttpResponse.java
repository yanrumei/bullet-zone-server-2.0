/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public abstract class AbstractClientHttpResponse
/*    */   implements ClientHttpResponse
/*    */ {
/*    */   public HttpStatus getStatusCode()
/*    */     throws IOException
/*    */   {
/* 33 */     return HttpStatus.valueOf(getRawStatusCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\client\AbstractClientHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */