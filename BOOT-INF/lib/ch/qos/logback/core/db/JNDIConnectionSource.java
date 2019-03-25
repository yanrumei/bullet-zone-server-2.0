/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
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
/*     */ 
/*     */ public class JNDIConnectionSource
/*     */   extends ConnectionSourceBase
/*     */ {
/*  43 */   private String jndiLocation = null;
/*  44 */   private DataSource dataSource = null;
/*     */   
/*     */   public void start() {
/*  47 */     if (this.jndiLocation == null) {
/*  48 */       addError("No JNDI location specified for JNDIConnectionSource.");
/*     */     }
/*  50 */     discoverConnectionProperties();
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  54 */     Connection conn = null;
/*     */     try {
/*  56 */       if (this.dataSource == null) {
/*  57 */         this.dataSource = lookupDataSource();
/*     */       }
/*  59 */       if (getUser() != null) {
/*  60 */         addWarn("Ignoring property [user] with value [" + getUser() + "] for obtaining a connection from a DataSource.");
/*     */       }
/*  62 */       conn = this.dataSource.getConnection();
/*     */     } catch (NamingException ne) {
/*  64 */       addError("Error while getting data source", ne);
/*  65 */       throw new SQLException("NamingException while looking up DataSource: " + ne.getMessage());
/*     */     } catch (ClassCastException cce) {
/*  67 */       addError("ClassCastException while looking up DataSource.", cce);
/*  68 */       throw new SQLException("ClassCastException while looking up DataSource: " + cce.getMessage());
/*     */     }
/*     */     
/*  71 */     return conn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getJndiLocation()
/*     */   {
/*  80 */     return this.jndiLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJndiLocation(String jndiLocation)
/*     */   {
/*  90 */     this.jndiLocation = jndiLocation;
/*     */   }
/*     */   
/*     */   private DataSource lookupDataSource() throws NamingException, SQLException {
/*  94 */     addInfo("Looking up [" + this.jndiLocation + "] in JNDI");
/*     */     
/*  96 */     Context initialContext = new InitialContext();
/*  97 */     Object obj = initialContext.lookup(this.jndiLocation);
/*     */     
/*     */ 
/*     */ 
/* 101 */     DataSource ds = (DataSource)obj;
/*     */     
/* 103 */     if (ds == null) {
/* 104 */       throw new SQLException("Failed to obtain data source from JNDI location " + this.jndiLocation);
/*     */     }
/* 106 */     return ds;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\JNDIConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */