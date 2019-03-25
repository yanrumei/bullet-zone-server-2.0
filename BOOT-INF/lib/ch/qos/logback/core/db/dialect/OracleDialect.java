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
/*    */ public class OracleDialect
/*    */   implements SQLDialect
/*    */ {
/*    */   public static final String SELECT_CURRVAL = "SELECT logging_event_id_seq.currval from dual";
/*    */   
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
/* 26 */     return "SELECT logging_event_id_seq.currval from dual";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\dialect\OracleDialect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */