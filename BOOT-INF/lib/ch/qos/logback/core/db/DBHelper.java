/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
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
/*    */ public class DBHelper
/*    */ {
/*    */   public static void closeConnection(Connection connection)
/*    */   {
/* 27 */     if (connection != null) {
/*    */       try {
/* 29 */         connection.close();
/*    */       }
/*    */       catch (SQLException sqle) {}
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public static void closeStatement(Statement statement)
/*    */   {
/* 38 */     if (statement != null) {
/*    */       try {
/* 40 */         statement.close();
/*    */       }
/*    */       catch (SQLException sqle) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\DBHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */