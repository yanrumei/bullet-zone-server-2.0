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
/*    */ public class HSQLDBDialect
/*    */   implements SQLDialect
/*    */ {
/*    */   public static final String SELECT_CURRVAL = "CALL IDENTITY()";
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
/* 25 */     return "CALL IDENTITY()";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\dialect\HSQLDBDialect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */