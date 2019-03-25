/*    */ package org.apache.commons.logging;
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
/*    */ public class LogConfigurationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8486587136871052495L;
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
/*    */   public LogConfigurationException() {}
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
/*    */   public LogConfigurationException(String message)
/*    */   {
/* 52 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LogConfigurationException(Throwable cause)
/*    */   {
/* 64 */     this(cause == null ? null : cause.toString(), cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LogConfigurationException(String message, Throwable cause)
/*    */   {
/* 77 */     super(message + " (Caused by " + cause + ")");
/* 78 */     this.cause = cause;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 84 */   protected Throwable cause = null;
/*    */   
/*    */ 
/*    */ 
/*    */   public Throwable getCause()
/*    */   {
/* 90 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jcl-over-slf4j-1.7.25.jar!\org\apache\commons\logging\LogConfigurationException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */