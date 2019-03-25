/*    */ package org.apache.coyote;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class CloseNowException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public CloseNowException() {}
/*    */   
/*    */   public CloseNowException(String message, Throwable cause)
/*    */   {
/* 39 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public CloseNowException(String message)
/*    */   {
/* 44 */     super(message);
/*    */   }
/*    */   
/*    */   public CloseNowException(Throwable cause)
/*    */   {
/* 49 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\CloseNowException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */