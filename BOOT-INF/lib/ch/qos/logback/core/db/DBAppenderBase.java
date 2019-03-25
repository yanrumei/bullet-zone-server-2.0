/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import ch.qos.logback.core.UnsynchronizedAppenderBase;
/*     */ import ch.qos.logback.core.db.dialect.DBUtil;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialect;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
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
/*     */ public abstract class DBAppenderBase<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */ {
/*     */   protected ConnectionSource connectionSource;
/*  37 */   protected boolean cnxSupportsGetGeneratedKeys = false;
/*  38 */   protected boolean cnxSupportsBatchUpdates = false;
/*     */   
/*     */   protected SQLDialect sqlDialect;
/*     */   
/*     */   protected abstract Method getGeneratedKeysMethod();
/*     */   
/*     */   protected abstract String getInsertSQL();
/*     */   
/*     */   public void start()
/*     */   {
/*  48 */     if (this.connectionSource == null) {
/*  49 */       throw new IllegalStateException("DBAppender cannot function without a connection source");
/*     */     }
/*     */     
/*  52 */     this.sqlDialect = DBUtil.getDialectFromCode(this.connectionSource.getSQLDialectCode());
/*  53 */     if (getGeneratedKeysMethod() != null) {
/*  54 */       this.cnxSupportsGetGeneratedKeys = this.connectionSource.supportsGetGeneratedKeys();
/*     */     } else {
/*  56 */       this.cnxSupportsGetGeneratedKeys = false;
/*     */     }
/*  58 */     this.cnxSupportsBatchUpdates = this.connectionSource.supportsBatchUpdates();
/*  59 */     if ((!this.cnxSupportsGetGeneratedKeys) && (this.sqlDialect == null)) {
/*  60 */       throw new IllegalStateException("DBAppender cannot function if the JDBC driver does not support getGeneratedKeys method *and* without a specific SQL dialect");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  65 */     super.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConnectionSource getConnectionSource()
/*     */   {
/*  72 */     return this.connectionSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionSource(ConnectionSource connectionSource)
/*     */   {
/*  80 */     this.connectionSource = connectionSource;
/*     */   }
/*     */   
/*     */   public void append(E eventObject)
/*     */   {
/*  85 */     Connection connection = null;
/*  86 */     PreparedStatement insertStatement = null;
/*     */     try {
/*  88 */       connection = this.connectionSource.getConnection();
/*  89 */       connection.setAutoCommit(false);
/*     */       
/*  91 */       if (this.cnxSupportsGetGeneratedKeys) {
/*  92 */         String EVENT_ID_COL_NAME = "EVENT_ID";
/*     */         
/*  94 */         if (this.connectionSource.getSQLDialectCode() == SQLDialectCode.POSTGRES_DIALECT) {
/*  95 */           EVENT_ID_COL_NAME = EVENT_ID_COL_NAME.toLowerCase();
/*     */         }
/*  97 */         insertStatement = connection.prepareStatement(getInsertSQL(), new String[] { EVENT_ID_COL_NAME });
/*     */       } else {
/*  99 */         insertStatement = connection.prepareStatement(getInsertSQL());
/*     */       }
/*     */       
/*     */       long eventId;
/*     */       
/* 104 */       synchronized (this) {
/* 105 */         subAppend(eventObject, connection, insertStatement);
/* 106 */         eventId = selectEventId(insertStatement, connection);
/*     */       }
/* 108 */       secondarySubAppend(eventObject, connection, eventId);
/*     */       
/* 110 */       connection.commit();
/*     */     } catch (Throwable sqle) {
/* 112 */       addError("problem appending event", sqle);
/*     */     } finally {
/* 114 */       DBHelper.closeStatement(insertStatement);
/* 115 */       DBHelper.closeConnection(connection);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void subAppend(E paramE, Connection paramConnection, PreparedStatement paramPreparedStatement) throws Throwable;
/*     */   
/*     */   protected abstract void secondarySubAppend(E paramE, Connection paramConnection, long paramLong) throws Throwable;
/*     */   
/*     */   protected long selectEventId(PreparedStatement insertStatement, Connection connection) throws SQLException, InvocationTargetException {
/* 124 */     ResultSet rs = null;
/* 125 */     Statement idStatement = null;
/*     */     try {
/* 127 */       boolean gotGeneratedKeys = false;
/* 128 */       if (this.cnxSupportsGetGeneratedKeys) {
/*     */         try {
/* 130 */           rs = (ResultSet)getGeneratedKeysMethod().invoke(insertStatement, (Object[])null);
/* 131 */           gotGeneratedKeys = true;
/*     */         } catch (InvocationTargetException ex) {
/* 133 */           Throwable target = ex.getTargetException();
/* 134 */           if ((target instanceof SQLException)) {
/* 135 */             throw ((SQLException)target);
/*     */           }
/* 137 */           throw ex;
/*     */         } catch (IllegalAccessException ex) {
/* 139 */           addWarn("IllegalAccessException invoking PreparedStatement.getGeneratedKeys", ex);
/*     */         }
/*     */       }
/*     */       
/* 143 */       if (!gotGeneratedKeys) {
/* 144 */         idStatement = connection.createStatement();
/* 145 */         idStatement.setMaxRows(1);
/* 146 */         String selectInsertIdStr = this.sqlDialect.getSelectInsertId();
/* 147 */         rs = idStatement.executeQuery(selectInsertIdStr);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 152 */       rs.next();
/* 153 */       long eventId = rs.getLong(1);
/* 154 */       return eventId;
/*     */     } finally {
/* 156 */       if (rs != null) {
/*     */         try {
/* 158 */           rs.close();
/*     */         }
/*     */         catch (SQLException e) {}
/*     */       }
/* 162 */       DBHelper.closeStatement(idStatement);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 168 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\db\DBAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */