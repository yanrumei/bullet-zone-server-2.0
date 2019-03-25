/*     */ package org.springframework.boot.jta.narayana;
/*     */ 
/*     */ import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;
/*     */ import java.sql.SQLException;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.sql.XADataSource;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DataSourceXAResourceRecoveryHelper
/*     */   implements XAResourceRecoveryHelper, XAResource
/*     */ {
/*  43 */   private static final XAResource[] NO_XA_RESOURCES = new XAResource[0];
/*     */   
/*     */ 
/*  46 */   private static final Log logger = LogFactory.getLog(DataSourceXAResourceRecoveryHelper.class);
/*     */   
/*     */ 
/*     */   private final XADataSource xaDataSource;
/*     */   
/*     */ 
/*     */   private final String user;
/*     */   
/*     */   private final String password;
/*     */   
/*     */   private XAConnection xaConnection;
/*     */   
/*     */   private XAResource delegate;
/*     */   
/*     */ 
/*     */   public DataSourceXAResourceRecoveryHelper(XADataSource xaDataSource)
/*     */   {
/*  63 */     this(xaDataSource, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DataSourceXAResourceRecoveryHelper(XADataSource xaDataSource, String user, String password)
/*     */   {
/*  74 */     Assert.notNull(xaDataSource, "XADataSource must not be null");
/*  75 */     this.xaDataSource = xaDataSource;
/*  76 */     this.user = user;
/*  77 */     this.password = password;
/*     */   }
/*     */   
/*     */   public boolean initialise(String properties)
/*     */   {
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public XAResource[] getXAResources()
/*     */   {
/*  87 */     if (connect()) {
/*  88 */       return new XAResource[] { this };
/*     */     }
/*  90 */     return NO_XA_RESOURCES;
/*     */   }
/*     */   
/*     */   private boolean connect() {
/*  94 */     if (this.delegate == null) {
/*     */       try {
/*  96 */         this.xaConnection = getXaConnection();
/*  97 */         this.delegate = this.xaConnection.getXAResource();
/*     */       }
/*     */       catch (SQLException ex) {
/* 100 */         logger.warn("Failed to create connection", ex);
/* 101 */         return false;
/*     */       }
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   private XAConnection getXaConnection() throws SQLException {
/* 108 */     if ((this.user == null) && (this.password == null)) {
/* 109 */       return this.xaDataSource.getXAConnection();
/*     */     }
/* 111 */     return this.xaDataSource.getXAConnection(this.user, this.password);
/*     */   }
/*     */   
/*     */   public Xid[] recover(int flag) throws XAException
/*     */   {
/*     */     try {
/* 117 */       return getDelegate(true).recover(flag);
/*     */     }
/*     */     finally {
/* 120 */       if (flag == 8388608) {
/* 121 */         disconnect();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void disconnect() throws XAException {
/*     */     try {
/* 128 */       this.xaConnection.close();
/*     */     }
/*     */     catch (SQLException e) {
/* 131 */       logger.warn("Failed to close connection", e);
/*     */     }
/*     */     finally {
/* 134 */       this.xaConnection = null;
/* 135 */       this.delegate = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void start(Xid xid, int flags) throws XAException
/*     */   {
/* 141 */     getDelegate(true).start(xid, flags);
/*     */   }
/*     */   
/*     */   public void end(Xid xid, int flags) throws XAException
/*     */   {
/* 146 */     getDelegate(true).end(xid, flags);
/*     */   }
/*     */   
/*     */   public int prepare(Xid xid) throws XAException
/*     */   {
/* 151 */     return getDelegate(true).prepare(xid);
/*     */   }
/*     */   
/*     */   public void commit(Xid xid, boolean onePhase) throws XAException
/*     */   {
/* 156 */     getDelegate(true).commit(xid, onePhase);
/*     */   }
/*     */   
/*     */   public void rollback(Xid xid) throws XAException
/*     */   {
/* 161 */     getDelegate(true).rollback(xid);
/*     */   }
/*     */   
/*     */   public boolean isSameRM(XAResource xaResource) throws XAException
/*     */   {
/* 166 */     return getDelegate(true).isSameRM(xaResource);
/*     */   }
/*     */   
/*     */   public void forget(Xid xid) throws XAException
/*     */   {
/* 171 */     getDelegate(true).forget(xid);
/*     */   }
/*     */   
/*     */   public int getTransactionTimeout() throws XAException
/*     */   {
/* 176 */     return getDelegate(true).getTransactionTimeout();
/*     */   }
/*     */   
/*     */   public boolean setTransactionTimeout(int seconds) throws XAException
/*     */   {
/* 181 */     return getDelegate(true).setTransactionTimeout(seconds);
/*     */   }
/*     */   
/*     */   private XAResource getDelegate(boolean required) {
/* 185 */     Assert.state((this.delegate != null) || (!required), "Connection has not been opened");
/*     */     
/* 187 */     return this.delegate;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\DataSourceXAResourceRecoveryHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */