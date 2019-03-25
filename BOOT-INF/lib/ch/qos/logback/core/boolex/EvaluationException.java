/*    */ package ch.qos.logback.core.boolex;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EvaluationException
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
/*    */   public EvaluationException(String msg)
/*    */   {
/* 26 */     super(msg);
/*    */   }
/*    */   
/*    */   public EvaluationException(String msg, Throwable cause) {
/* 30 */     super(msg, cause);
/*    */   }
/*    */   
/*    */   public EvaluationException(Throwable cause) {
/* 34 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\boolex\EvaluationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */