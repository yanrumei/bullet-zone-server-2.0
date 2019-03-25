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
/*    */ public class HpackException
/*    */   extends Exception
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
/*    */ 
/*    */   public HpackException(String message, Throwable cause)
/*    */   {
/* 28 */     super(message, cause);
/*    */   }
/*    */   
/* 31 */   public HpackException(String message) { super(message); }
/*    */   
/*    */   public HpackException() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\HpackException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */