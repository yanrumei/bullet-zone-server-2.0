/*    */ package ch.qos.logback.core.db.dialect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PostgreSQLDialect
/*    */   implements SQLDialect
/*    */ {
/*    */   public static final String SELECT_CURRVAL = "SELECT currval('logging_event_id_seq')";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSelectInsertId()
/*    */   {
/* 25 */     return "SELECT currval('logging_event_id_seq')";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\dialect\PostgreSQLDialect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */