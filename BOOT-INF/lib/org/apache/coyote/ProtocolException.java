/*    */ package org.apache.coyote;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProtocolException
/*    */   extends RuntimeException
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
/*    */   public ProtocolException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ProtocolException(String message, Throwable cause)
/*    */   {
/* 32 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ProtocolException(String message) {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public ProtocolException(Throwable cause) {
/* 40 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ProtocolException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */