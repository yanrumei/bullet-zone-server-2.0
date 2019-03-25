/*     */ package org.springframework.boot.jdbc;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum DatabaseDriver
/*     */ {
/*  40 */   UNKNOWN(null, null), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   DERBY("Apache Derby", "org.apache.derby.jdbc.EmbeddedDriver", "org.apache.derby.jdbc.EmbeddedXADataSource", "SELECT 1 FROM SYSIBM.SYSDUMMY1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   H2("H2", "org.h2.Driver", "org.h2.jdbcx.JdbcDataSource", "SELECT 1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   HSQLDB("HSQL Database Engine", "org.hsqldb.jdbc.JDBCDriver", "org.hsqldb.jdbc.pool.JDBCXADataSource", "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   SQLITE("SQLite", "org.sqlite.JDBC"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   MYSQL("MySQL", "com.mysql.jdbc.Driver", "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource", "SELECT 1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   MARIADB("MySQL", "org.mariadb.jdbc.Driver", "org.mariadb.jdbc.MariaDbDataSource", "SELECT 1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   GAE(null, "com.google.appengine.api.rdbms.AppEngineDriver"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   ORACLE("Oracle", "oracle.jdbc.OracleDriver", "oracle.jdbc.xa.client.OracleXADataSource", "SELECT 'Hello' from DUAL"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   POSTGRESQL("PostgreSQL", "org.postgresql.Driver", "org.postgresql.xa.PGXADataSource", "SELECT 1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   JTDS(null, "net.sourceforge.jtds.jdbc.Driver"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 110 */   SQLSERVER("Microsoft SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "com.microsoft.sqlserver.jdbc.SQLServerXADataSource", "SELECT 1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   FIREBIRD("Firebird", "org.firebirdsql.jdbc.FBDriver", "org.firebirdsql.ds.FBXADataSource", "SELECT 1 FROM RDB$DATABASE"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */   DB2("DB2", "com.ibm.db2.jcc.DB2Driver", "com.ibm.db2.jcc.DB2XADataSource", "SELECT 1 FROM SYSIBM.SYSDUMMY1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   DB2_AS400("DB2 UDB for AS/400", "com.ibm.as400.access.AS400JDBCDriver", "com.ibm.as400.access.AS400JDBCXADataSource", "SELECT 1 FROM SYSIBM.SYSDUMMY1"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */   TERADATA("Teradata", "com.teradata.jdbc.TeraDriver"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 185 */   INFORMIX("Informix Dynamic Server", "com.informix.jdbc.IfxDriver", null, "select count(*) from systables");
/*     */   
/*     */ 
/*     */ 
/*     */   private final String productName;
/*     */   
/*     */ 
/*     */   private final String driverClassName;
/*     */   
/*     */ 
/*     */   private final String xaDataSourceClassName;
/*     */   
/*     */ 
/*     */   private final String validationQuery;
/*     */   
/*     */ 
/*     */ 
/*     */   private DatabaseDriver(String productName, String driverClassName)
/*     */   {
/* 204 */     this(productName, driverClassName, null);
/*     */   }
/*     */   
/*     */   private DatabaseDriver(String productName, String driverClassName, String xaDataSourceClassName)
/*     */   {
/* 209 */     this(productName, driverClassName, xaDataSourceClassName, null);
/*     */   }
/*     */   
/*     */   private DatabaseDriver(String productName, String driverClassName, String xaDataSourceClassName, String validationQuery)
/*     */   {
/* 214 */     this.productName = productName;
/* 215 */     this.driverClassName = driverClassName;
/* 216 */     this.xaDataSourceClassName = xaDataSourceClassName;
/* 217 */     this.validationQuery = validationQuery;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 225 */     return name().toLowerCase();
/*     */   }
/*     */   
/*     */   protected boolean matchProductName(String productName) {
/* 229 */     return (this.productName != null) && (this.productName.equalsIgnoreCase(productName));
/*     */   }
/*     */   
/*     */   protected Collection<String> getUrlPrefixes() {
/* 233 */     return Collections.singleton(name().toLowerCase());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDriverClassName()
/*     */   {
/* 241 */     return this.driverClassName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getXaDataSourceClassName()
/*     */   {
/* 249 */     return this.xaDataSourceClassName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getValidationQuery()
/*     */   {
/* 257 */     return this.validationQuery;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DatabaseDriver fromJdbcUrl(String url)
/*     */   {
/* 266 */     if (StringUtils.hasLength(url)) {
/* 267 */       Assert.isTrue(url.startsWith("jdbc"), "URL must start with 'jdbc'");
/* 268 */       String urlWithoutPrefix = url.substring("jdbc".length()).toLowerCase();
/* 269 */       DatabaseDriver driver; for (driver : values()) {
/* 270 */         for (String urlPrefix : driver.getUrlPrefixes()) {
/* 271 */           String prefix = ":" + urlPrefix + ":";
/* 272 */           if ((driver != UNKNOWN) && (urlWithoutPrefix.startsWith(prefix))) {
/* 273 */             return driver;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 278 */     return UNKNOWN;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DatabaseDriver fromProductName(String productName)
/*     */   {
/* 287 */     if (StringUtils.hasLength(productName)) {
/* 288 */       for (DatabaseDriver candidate : values()) {
/* 289 */         if (candidate.matchProductName(productName)) {
/* 290 */           return candidate;
/*     */         }
/*     */       }
/*     */     }
/* 294 */     return UNKNOWN;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jdbc\DatabaseDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */