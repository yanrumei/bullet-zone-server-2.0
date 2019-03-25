/*    */ package org.apache.coyote.http11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HeadersTooLargeException
/*    */   extends IllegalStateException
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
/*    */   public HeadersTooLargeException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HeadersTooLargeException(String message, Throwable cause)
/*    */   {
/* 32 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public HeadersTooLargeException(String s) {
/* 36 */     super(s);
/*    */   }
/*    */   
/*    */   public HeadersTooLargeException(Throwable cause) {
/* 40 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\HeadersTooLargeException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */