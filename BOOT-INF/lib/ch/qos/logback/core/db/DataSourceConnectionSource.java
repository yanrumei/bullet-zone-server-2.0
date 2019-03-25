/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.DataSource;
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
/*    */ 
/*    */ public class DataSourceConnectionSource
/*    */   extends ConnectionSourceBase
/*    */ {
/*    */   private DataSource dataSource;
/*    */   
/*    */   public void start()
/*    */   {
/* 41 */     if (this.dataSource == null) {
/* 42 */       addWarn("WARNING: No data source specified");
/*    */     } else {
/* 44 */       discoverConnectionProperties();
/* 45 */       if ((!supportsGetGeneratedKeys()) && (getSQLDialectCode() == SQLDialectCode.UNKNOWN_DIALECT)) {
/* 46 */         addWarn("Connection does not support GetGeneratedKey method and could not discover the dialect.");
/*    */       }
/*    */     }
/* 49 */     super.start();
/*    */   }
/*    */   
/*    */ 
/*    */   public Connection getConnection()
/*    */     throws SQLException
/*    */   {
/* 56 */     if (this.dataSource == null) {
/* 57 */       addError("WARNING: No data source specified");
/* 58 */       return null;
/*    */     }
/*    */     
/* 61 */     if (getUser() == null) {
/* 62 */       return this.dataSource.getConnection();
/*    */     }
/* 64 */     return this.dataSource.getConnection(getUser(), getPassword());
/*    */   }
/*    */   
/*    */   public DataSource getDataSource()
/*    */   {
/* 69 */     return this.dataSource;
/*    */   }
/*    */   
/*    */   public void setDataSource(DataSource dataSource) {
/* 73 */     this.dataSource = dataSource;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\DataSourceConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */