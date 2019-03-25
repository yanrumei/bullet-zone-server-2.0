/*    */ package org.springframework.boot.autoconfigure.orm.jpa;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.sql.DataSource;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.jdbc.DatabaseDriver;
/*    */ import org.springframework.jdbc.support.JdbcUtils;
/*    */ import org.springframework.jdbc.support.MetaDataAccessException;
/*    */ import org.springframework.orm.jpa.vendor.Database;
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
/*    */ final class DatabaseLookup
/*    */ {
/* 41 */   private static final Log logger = LogFactory.getLog(DatabaseLookup.class);
/*    */   private static final Map<DatabaseDriver, Database> LOOKUP;
/*    */   
/*    */   static
/*    */   {
/* 46 */     Map<DatabaseDriver, Database> map = new HashMap();
/* 47 */     map.put(DatabaseDriver.DERBY, Database.DERBY);
/* 48 */     map.put(DatabaseDriver.H2, Database.H2);
/* 49 */     map.put(DatabaseDriver.HSQLDB, Database.HSQL);
/* 50 */     map.put(DatabaseDriver.MYSQL, Database.MYSQL);
/* 51 */     map.put(DatabaseDriver.ORACLE, Database.ORACLE);
/* 52 */     map.put(DatabaseDriver.POSTGRESQL, Database.POSTGRESQL);
/* 53 */     map.put(DatabaseDriver.SQLSERVER, Database.SQL_SERVER);
/* 54 */     map.put(DatabaseDriver.DB2, Database.DB2);
/* 55 */     map.put(DatabaseDriver.INFORMIX, Database.INFORMIX);
/* 56 */     LOOKUP = Collections.unmodifiableMap(map);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Database getDatabase(DataSource dataSource)
/*    */   {
/* 68 */     if (dataSource == null) {
/* 69 */       return Database.DEFAULT;
/*    */     }
/*    */     try {
/* 72 */       String url = (String)JdbcUtils.extractDatabaseMetaData(dataSource, "getURL");
/* 73 */       DatabaseDriver driver = DatabaseDriver.fromJdbcUrl(url);
/* 74 */       Database database = (Database)LOOKUP.get(driver);
/* 75 */       if (database != null) {
/* 76 */         return database;
/*    */       }
/*    */     }
/*    */     catch (MetaDataAccessException ex) {
/* 80 */       logger.warn("Unable to determine jdbc url from datasource", ex);
/*    */     }
/* 82 */     return Database.DEFAULT;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\DatabaseLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */