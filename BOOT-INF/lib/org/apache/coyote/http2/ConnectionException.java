/*    */ package org.apache.coyote.http2;
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
/*    */ public class ConnectionException
/*    */   extends Http2Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   ConnectionException(String msg, Http2Error error)
/*    */   {
/* 27 */     super(msg, error);
/*    */   }
/*    */   
/*    */   ConnectionException(String msg, Http2Error error, Throwable cause)
/*    */   {
/* 32 */     super(msg, error, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\ConnectionException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */