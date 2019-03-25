/*    */ package ch.qos.logback.core.status;
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
/*    */ public class InfoStatus
/*    */   extends StatusBase
/*    */ {
/*    */   public InfoStatus(String msg, Object origin)
/*    */   {
/* 18 */     super(0, msg, origin);
/*    */   }
/*    */   
/*    */   public InfoStatus(String msg, Object origin, Throwable t) {
/* 22 */     super(0, msg, origin, t);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\status\InfoStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */