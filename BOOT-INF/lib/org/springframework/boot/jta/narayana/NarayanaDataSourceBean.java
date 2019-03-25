/*     */ package org.springframework.boot.jta.narayana;
/*     */ 
/*     */ import com.arjuna.ats.internal.jdbc.ConnectionManager;
/*     */ import com.arjuna.ats.jdbc.TransactionalDriver;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.XADataSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class NarayanaDataSourceBean
/*     */   implements DataSource
/*     */ {
/*     */   private final XADataSource xaDataSource;
/*     */   
/*     */   public NarayanaDataSourceBean(XADataSource xaDataSource)
/*     */   {
/*  51 */     Assert.notNull(xaDataSource, "XADataSource must not be null");
/*  52 */     this.xaDataSource = xaDataSource;
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException
/*     */   {
/*  57 */     Properties properties = new Properties();
/*  58 */     properties.put(TransactionalDriver.XADataSource, this.xaDataSource);
/*  59 */     return ConnectionManager.create(null, properties);
/*     */   }
/*     */   
/*     */   public Connection getConnection(String username, String password)
/*     */     throws SQLException
/*     */   {
/*  65 */     Properties properties = new Properties();
/*  66 */     properties.put(TransactionalDriver.XADataSource, this.xaDataSource);
/*  67 */     properties.put("user", username);
/*  68 */     properties.put("password", password);
/*  69 */     return ConnectionManager.create(null, properties);
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException
/*     */   {
/*  74 */     return this.xaDataSource.getLogWriter();
/*     */   }
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException
/*     */   {
/*  79 */     this.xaDataSource.setLogWriter(out);
/*     */   }
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException
/*     */   {
/*  84 */     this.xaDataSource.setLoginTimeout(seconds);
/*     */   }
/*     */   
/*     */   public int getLoginTimeout() throws SQLException
/*     */   {
/*  89 */     return this.xaDataSource.getLoginTimeout();
/*     */   }
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException
/*     */   {
/*  94 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public <T> T unwrap(Class<T> iface)
/*     */     throws SQLException
/*     */   {
/* 100 */     if (isWrapperFor(iface)) {
/* 101 */       return this;
/*     */     }
/* 103 */     if (ClassUtils.isAssignableValue(iface, this.xaDataSource)) {
/* 104 */       return this.xaDataSource;
/*     */     }
/* 106 */     throw new SQLException(getClass() + " is not a wrapper for " + iface);
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException
/*     */   {
/* 111 */     return iface.isAssignableFrom(getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaDataSourceBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */