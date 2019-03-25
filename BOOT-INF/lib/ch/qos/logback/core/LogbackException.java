/*    */ package ch.qos.logback.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogbackException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -799956346239073266L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LogbackException(String msg)
/*    */   {
/* 21 */     super(msg);
/*    */   }
/*    */   
/*    */   public LogbackException(String msg, Throwable nested) {
/* 25 */     super(msg, nested);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\LogbackException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */