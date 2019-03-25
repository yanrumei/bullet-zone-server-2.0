/*    */ package org.apache.coyote.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Http2Exception
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Http2Error error;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   Http2Exception(String msg, Http2Error error)
/*    */   {
/* 27 */     super(msg);
/* 28 */     this.error = error;
/*    */   }
/*    */   
/*    */   Http2Exception(String msg, Http2Error error, Throwable cause)
/*    */   {
/* 33 */     super(msg, cause);
/* 34 */     this.error = error;
/*    */   }
/*    */   
/*    */   Http2Error getError()
/*    */   {
/* 39 */     return this.error;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2Exception.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */