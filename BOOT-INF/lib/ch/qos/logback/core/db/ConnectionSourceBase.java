/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import ch.qos.logback.core.db.dialect.DBUtil;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
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
/*     */ public abstract class ConnectionSourceBase
/*     */   extends ContextAwareBase
/*     */   implements ConnectionSource
/*     */ {
/*     */   private boolean started;
/*  31 */   private String user = null;
/*  32 */   private String password = null;
/*     */   
/*     */ 
/*  35 */   private SQLDialectCode dialectCode = SQLDialectCode.UNKNOWN_DIALECT;
/*  36 */   private boolean supportsGetGeneratedKeys = false;
/*  37 */   private boolean supportsBatchUpdates = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void discoverConnectionProperties()
/*     */   {
/*  44 */     Connection connection = null;
/*     */     try {
/*  46 */       connection = getConnection();
/*  47 */       if (connection == null) {
/*  48 */         addWarn("Could not get a connection");
/*     */       }
/*     */       else {
/*  51 */         DatabaseMetaData meta = connection.getMetaData();
/*  52 */         DBUtil util = new DBUtil();
/*  53 */         util.setContext(getContext());
/*  54 */         this.supportsGetGeneratedKeys = util.supportsGetGeneratedKeys(meta);
/*  55 */         this.supportsBatchUpdates = util.supportsBatchUpdates(meta);
/*  56 */         this.dialectCode = DBUtil.discoverSQLDialect(meta);
/*  57 */         addInfo("Driver name=" + meta.getDriverName());
/*  58 */         addInfo("Driver version=" + meta.getDriverVersion());
/*  59 */         addInfo("supportsGetGeneratedKeys=" + this.supportsGetGeneratedKeys);
/*     */       }
/*     */     } catch (SQLException se) {
/*  62 */       addWarn("Could not discover the dialect to use.", se);
/*     */     } finally {
/*  64 */       DBHelper.closeConnection(connection);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean supportsGetGeneratedKeys()
/*     */   {
/*  72 */     return this.supportsGetGeneratedKeys;
/*     */   }
/*     */   
/*     */   public final SQLDialectCode getSQLDialectCode() {
/*  76 */     return this.dialectCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String getPassword()
/*     */   {
/*  83 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setPassword(String password)
/*     */   {
/*  91 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String getUser()
/*     */   {
/*  98 */     return this.user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setUser(String username)
/*     */   {
/* 106 */     this.user = username;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean supportsBatchUpdates()
/*     */   {
/* 113 */     return this.supportsBatchUpdates;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 117 */     return this.started;
/*     */   }
/*     */   
/*     */   public void start() {
/* 121 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 125 */     this.started = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\ConnectionSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */