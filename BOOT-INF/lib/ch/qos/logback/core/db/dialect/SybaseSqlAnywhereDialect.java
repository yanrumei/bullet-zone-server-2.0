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
/*    */ 
/*    */ 
/*    */ public class SybaseSqlAnywhereDialect
/*    */   implements SQLDialect
/*    */ {
/*    */   public static final String SELECT_CURRVAL = "SELECT @@identity id";
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
/*    */   public String getSelectInsertId()
/*    */   {
/* 30 */     return "SELECT @@identity id";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\dialect\SybaseSqlAnywhereDialect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */