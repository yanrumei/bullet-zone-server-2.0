/*    */ package org.springframework.web.client;
/*    */ 
/*    */ import org.springframework.core.NestedRuntimeException;
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
/*    */ public class RestClientException
/*    */   extends NestedRuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -4084444984163796577L;
/*    */   
/*    */   public RestClientException(String msg)
/*    */   {
/* 38 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RestClientException(String msg, Throwable ex)
/*    */   {
/* 48 */     super(msg, ex);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\client\RestClientException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */