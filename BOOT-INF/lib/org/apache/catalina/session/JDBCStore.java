/*      */ package org.apache.catalina.session;
/*      */ 
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Driver;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Properties;
/*      */ import javax.naming.InitialContext;
/*      */ import javax.naming.NamingException;
/*      */ import javax.sql.DataSource;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Globals;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.Manager;
/*      */ import org.apache.catalina.Session;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JDBCStore
/*      */   extends StoreBase
/*      */ {
/*   61 */   private String name = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final String storeName = "JDBCStore";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static final String threadName = "JDBCStore";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   76 */   protected String connectionName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   82 */   protected String connectionPassword = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   87 */   protected String connectionURL = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   92 */   private Connection dbConnection = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   97 */   protected Driver driver = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  102 */   protected String driverName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  107 */   protected String dataSourceName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  112 */   private boolean localDataSource = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  117 */   protected DataSource dataSource = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  125 */   protected String sessionTable = "tomcat$sessions";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  130 */   protected String sessionAppCol = "app";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  135 */   protected String sessionIdCol = "id";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  140 */   protected String sessionDataCol = "data";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  145 */   protected String sessionValidCol = "valid";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  150 */   protected String sessionMaxInactiveCol = "maxinactive";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  155 */   protected String sessionLastAccessedCol = "lastaccess";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  163 */   protected PreparedStatement preparedSizeSql = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  168 */   protected PreparedStatement preparedSaveSql = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  173 */   protected PreparedStatement preparedClearSql = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  178 */   protected PreparedStatement preparedRemoveSql = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected PreparedStatement preparedLoadSql = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  192 */     if (this.name == null) {
/*  193 */       Container container = this.manager.getContext();
/*  194 */       String contextName = container.getName();
/*  195 */       if (!contextName.startsWith("/")) {
/*  196 */         contextName = "/" + contextName;
/*      */       }
/*  198 */       String hostName = "";
/*  199 */       String engineName = "";
/*      */       
/*  201 */       if (container.getParent() != null) {
/*  202 */         Container host = container.getParent();
/*  203 */         hostName = host.getName();
/*  204 */         if (host.getParent() != null) {
/*  205 */           engineName = host.getParent().getName();
/*      */         }
/*      */       }
/*  208 */       this.name = ("/" + engineName + "/" + hostName + contextName);
/*      */     }
/*  210 */     return this.name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getThreadName()
/*      */   {
/*  217 */     return "JDBCStore";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStoreName()
/*      */   {
/*  225 */     return "JDBCStore";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDriverName(String driverName)
/*      */   {
/*  234 */     String oldDriverName = this.driverName;
/*  235 */     this.driverName = driverName;
/*  236 */     this.support.firePropertyChange("driverName", oldDriverName, this.driverName);
/*      */     
/*      */ 
/*  239 */     this.driverName = driverName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getDriverName()
/*      */   {
/*  246 */     return this.driverName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getConnectionName()
/*      */   {
/*  253 */     return this.connectionName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionName(String connectionName)
/*      */   {
/*  262 */     this.connectionName = connectionName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getConnectionPassword()
/*      */   {
/*  269 */     return this.connectionPassword;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionPassword(String connectionPassword)
/*      */   {
/*  278 */     this.connectionPassword = connectionPassword;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectionURL(String connectionURL)
/*      */   {
/*  287 */     String oldConnString = this.connectionURL;
/*  288 */     this.connectionURL = connectionURL;
/*  289 */     this.support.firePropertyChange("connectionURL", oldConnString, this.connectionURL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getConnectionURL()
/*      */   {
/*  298 */     return this.connectionURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionTable(String sessionTable)
/*      */   {
/*  307 */     String oldSessionTable = this.sessionTable;
/*  308 */     this.sessionTable = sessionTable;
/*  309 */     this.support.firePropertyChange("sessionTable", oldSessionTable, this.sessionTable);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionTable()
/*      */   {
/*  318 */     return this.sessionTable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionAppCol(String sessionAppCol)
/*      */   {
/*  327 */     String oldSessionAppCol = this.sessionAppCol;
/*  328 */     this.sessionAppCol = sessionAppCol;
/*  329 */     this.support.firePropertyChange("sessionAppCol", oldSessionAppCol, this.sessionAppCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionAppCol()
/*      */   {
/*  338 */     return this.sessionAppCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionIdCol(String sessionIdCol)
/*      */   {
/*  347 */     String oldSessionIdCol = this.sessionIdCol;
/*  348 */     this.sessionIdCol = sessionIdCol;
/*  349 */     this.support.firePropertyChange("sessionIdCol", oldSessionIdCol, this.sessionIdCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionIdCol()
/*      */   {
/*  358 */     return this.sessionIdCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionDataCol(String sessionDataCol)
/*      */   {
/*  367 */     String oldSessionDataCol = this.sessionDataCol;
/*  368 */     this.sessionDataCol = sessionDataCol;
/*  369 */     this.support.firePropertyChange("sessionDataCol", oldSessionDataCol, this.sessionDataCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionDataCol()
/*      */   {
/*  378 */     return this.sessionDataCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionValidCol(String sessionValidCol)
/*      */   {
/*  387 */     String oldSessionValidCol = this.sessionValidCol;
/*  388 */     this.sessionValidCol = sessionValidCol;
/*  389 */     this.support.firePropertyChange("sessionValidCol", oldSessionValidCol, this.sessionValidCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionValidCol()
/*      */   {
/*  398 */     return this.sessionValidCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionMaxInactiveCol(String sessionMaxInactiveCol)
/*      */   {
/*  407 */     String oldSessionMaxInactiveCol = this.sessionMaxInactiveCol;
/*  408 */     this.sessionMaxInactiveCol = sessionMaxInactiveCol;
/*  409 */     this.support.firePropertyChange("sessionMaxInactiveCol", oldSessionMaxInactiveCol, this.sessionMaxInactiveCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionMaxInactiveCol()
/*      */   {
/*  418 */     return this.sessionMaxInactiveCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionLastAccessedCol(String sessionLastAccessedCol)
/*      */   {
/*  427 */     String oldSessionLastAccessedCol = this.sessionLastAccessedCol;
/*  428 */     this.sessionLastAccessedCol = sessionLastAccessedCol;
/*  429 */     this.support.firePropertyChange("sessionLastAccessedCol", oldSessionLastAccessedCol, this.sessionLastAccessedCol);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSessionLastAccessedCol()
/*      */   {
/*  438 */     return this.sessionLastAccessedCol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDataSourceName(String dataSourceName)
/*      */   {
/*  447 */     if ((dataSourceName == null) || ("".equals(dataSourceName.trim()))) {
/*  448 */       this.manager.getContext().getLogger().warn(sm
/*  449 */         .getString(getStoreName() + ".missingDataSourceName"));
/*  450 */       return;
/*      */     }
/*  452 */     this.dataSourceName = dataSourceName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getDataSourceName()
/*      */   {
/*  459 */     return this.dataSourceName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getLocalDataSource()
/*      */   {
/*  466 */     return this.localDataSource;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocalDataSource(boolean localDataSource)
/*      */   {
/*  476 */     this.localDataSource = localDataSource;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String[] expiredKeys()
/*      */     throws IOException
/*      */   {
/*  484 */     return keys(true);
/*      */   }
/*      */   
/*      */   public String[] keys() throws IOException
/*      */   {
/*  489 */     return keys(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String[] keys(boolean expiredOnly)
/*      */     throws IOException
/*      */   {
/*  504 */     String[] keys = null;
/*  505 */     synchronized (this) {
/*  506 */       int numberOfTries = 2;
/*  507 */       while (numberOfTries > 0)
/*      */       {
/*  509 */         Connection _conn = getConnection();
/*  510 */         if (_conn == null) {
/*  511 */           return new String[0];
/*      */         }
/*      */         try
/*      */         {
/*  515 */           String keysSql = "SELECT " + this.sessionIdCol + " FROM " + this.sessionTable + " WHERE " + this.sessionAppCol + " = ?";
/*      */           
/*  517 */           if (expiredOnly) {
/*  518 */             keysSql = keysSql + " AND (" + this.sessionLastAccessedCol + " + " + this.sessionMaxInactiveCol + " * 1000 < ?)";
/*      */           }
/*      */           
/*  521 */           PreparedStatement preparedKeysSql = _conn.prepareStatement(keysSql);Throwable localThrowable6 = null;
/*  522 */           try { preparedKeysSql.setString(1, getName());
/*  523 */             if (expiredOnly) {
/*  524 */               preparedKeysSql.setLong(2, System.currentTimeMillis());
/*      */             }
/*  526 */             ResultSet rst = preparedKeysSql.executeQuery();Throwable localThrowable7 = null;
/*  527 */             try { ArrayList<String> tmpkeys = new ArrayList();
/*  528 */               if (rst != null) {
/*  529 */                 while (rst.next()) {
/*  530 */                   tmpkeys.add(rst.getString(1));
/*      */                 }
/*      */               }
/*  533 */               keys = (String[])tmpkeys.toArray(new String[tmpkeys.size()]);
/*      */               
/*  535 */               numberOfTries = 0;
/*      */             }
/*      */             catch (Throwable localThrowable1)
/*      */             {
/*  526 */               localThrowable7 = localThrowable1;throw localThrowable1;
/*      */             }
/*      */             finally {}
/*      */           }
/*      */           catch (Throwable localThrowable4)
/*      */           {
/*  521 */             localThrowable6 = localThrowable4;throw localThrowable4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  537 */             if (preparedKeysSql != null) if (localThrowable6 != null) try { preparedKeysSql.close(); } catch (Throwable localThrowable5) {} else preparedKeysSql.close();
/*      */           }
/*  539 */         } catch (SQLException e) { this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  540 */           keys = new String[0];
/*      */           
/*  542 */           if (this.dbConnection != null)
/*  543 */             close(this.dbConnection);
/*      */         } finally {
/*  545 */           release(_conn);
/*      */         }
/*  547 */         numberOfTries--;
/*      */       }
/*      */     }
/*  550 */     return keys;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSize()
/*      */     throws IOException
/*      */   {
/*  564 */     int size = 0;
/*      */     
/*  566 */     synchronized (this) {
/*  567 */       int numberOfTries = 2;
/*  568 */       while (numberOfTries > 0) {
/*  569 */         Connection _conn = getConnection();
/*      */         
/*  571 */         if (_conn == null) {
/*  572 */           return size;
/*      */         }
/*      */         try
/*      */         {
/*  576 */           if (this.preparedSizeSql == null) {
/*  577 */             String sizeSql = "SELECT COUNT(" + this.sessionIdCol + ") FROM " + this.sessionTable + " WHERE " + this.sessionAppCol + " = ?";
/*      */             
/*      */ 
/*  580 */             this.preparedSizeSql = _conn.prepareStatement(sizeSql);
/*      */           }
/*      */           
/*  583 */           this.preparedSizeSql.setString(1, getName());
/*  584 */           ResultSet rst = this.preparedSizeSql.executeQuery();Throwable localThrowable3 = null;
/*  585 */           try { if (rst.next()) {
/*  586 */               size = rst.getInt(1);
/*      */             }
/*      */             
/*  589 */             numberOfTries = 0;
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/*  584 */             localThrowable3 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*  590 */             if (rst != null) if (localThrowable3 != null) try { rst.close(); } catch (Throwable localThrowable2) {} else rst.close();
/*      */           }
/*  592 */         } catch (SQLException e) { this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  593 */           if (this.dbConnection != null)
/*  594 */             close(this.dbConnection);
/*      */         } finally {
/*  596 */           release(_conn);
/*      */         }
/*  598 */         numberOfTries--;
/*      */       }
/*      */     }
/*  601 */     return size;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Session load(String id)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/*  615 */     StandardSession _session = null;
/*  616 */     org.apache.catalina.Context context = getManager().getContext();
/*  617 */     Log contextLog = context.getLogger();
/*      */     
/*  619 */     synchronized (this) {
/*  620 */       int numberOfTries = 2;
/*  621 */       while (numberOfTries > 0) {
/*  622 */         Connection _conn = getConnection();
/*  623 */         if (_conn == null) {
/*  624 */           return null;
/*      */         }
/*      */         
/*  627 */         ClassLoader oldThreadContextCL = context.bind(Globals.IS_SECURITY_ENABLED, null);
/*      */         try
/*      */         {
/*  630 */           if (this.preparedLoadSql == null) {
/*  631 */             String loadSql = "SELECT " + this.sessionIdCol + ", " + this.sessionDataCol + " FROM " + this.sessionTable + " WHERE " + this.sessionIdCol + " = ? AND " + this.sessionAppCol + " = ?";
/*      */             
/*      */ 
/*      */ 
/*  635 */             this.preparedLoadSql = _conn.prepareStatement(loadSql);
/*      */           }
/*      */           
/*  638 */           this.preparedLoadSql.setString(1, id);
/*  639 */           this.preparedLoadSql.setString(2, getName());
/*  640 */           ResultSet rst = this.preparedLoadSql.executeQuery();Throwable localThrowable6 = null;
/*  641 */           try { if (rst.next())
/*      */             {
/*  643 */               ObjectInputStream ois = getObjectInputStream(rst.getBinaryStream(2));Throwable localThrowable7 = null;
/*  644 */               try { if (contextLog.isDebugEnabled()) {
/*  645 */                   contextLog.debug(sm.getString(
/*  646 */                     getStoreName() + ".loading", new Object[] { id, this.sessionTable }));
/*      */                 }
/*      */                 
/*  649 */                 _session = (StandardSession)this.manager.createEmptySession();
/*  650 */                 _session.readObjectData(ois);
/*  651 */                 _session.setManager(this.manager);
/*      */               }
/*      */               catch (Throwable localThrowable1)
/*      */               {
/*  642 */                 localThrowable7 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*      */               finally
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*  652 */                 if (ois != null) if (localThrowable7 != null) try { ois.close(); } catch (Throwable localThrowable2) { localThrowable7.addSuppressed(localThrowable2); } else ois.close();
/*  653 */               } } else if (context.getLogger().isDebugEnabled()) {
/*  654 */               contextLog.debug(getStoreName() + ": No persisted data object found");
/*      */             }
/*      */             
/*  657 */             numberOfTries = 0;
/*      */           }
/*      */           catch (Throwable localThrowable4)
/*      */           {
/*  640 */             localThrowable6 = localThrowable4;throw localThrowable4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  658 */             if (rst != null) if (localThrowable6 != null) try { rst.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else rst.close();
/*      */           }
/*  660 */         } catch (SQLException e) { contextLog.error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  661 */           if (this.dbConnection != null)
/*  662 */             close(this.dbConnection);
/*      */         } finally {
/*  664 */           context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
/*  665 */           release(_conn);
/*      */         }
/*  667 */         numberOfTries--;
/*      */       }
/*      */     }
/*      */     
/*  671 */     return _session;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void remove(String id)
/*      */     throws IOException
/*      */   {
/*  686 */     synchronized (this) {
/*  687 */       int numberOfTries = 2;
/*  688 */       while (numberOfTries > 0) {
/*  689 */         Connection _conn = getConnection();
/*      */         
/*  691 */         if (_conn == null) {
/*  692 */           return;
/*      */         }
/*      */         try
/*      */         {
/*  696 */           remove(id, _conn);
/*      */           
/*  698 */           numberOfTries = 0;
/*      */         } catch (SQLException e) {
/*  700 */           this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  701 */           if (this.dbConnection != null)
/*  702 */             close(this.dbConnection);
/*      */         } finally {
/*  704 */           release(_conn);
/*      */         }
/*  706 */         numberOfTries--;
/*      */       }
/*      */     }
/*      */     
/*  710 */     if (this.manager.getContext().getLogger().isDebugEnabled()) {
/*  711 */       this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".removing", new Object[] { id, this.sessionTable }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void remove(String id, Connection _conn)
/*      */     throws SQLException
/*      */   {
/*  725 */     if (this.preparedRemoveSql == null) {
/*  726 */       String removeSql = "DELETE FROM " + this.sessionTable + " WHERE " + this.sessionIdCol + " = ?  AND " + this.sessionAppCol + " = ?";
/*      */       
/*      */ 
/*  729 */       this.preparedRemoveSql = _conn.prepareStatement(removeSql);
/*      */     }
/*      */     
/*  732 */     this.preparedRemoveSql.setString(1, id);
/*  733 */     this.preparedRemoveSql.setString(2, getName());
/*  734 */     this.preparedRemoveSql.execute();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */     throws IOException
/*      */   {
/*  745 */     synchronized (this) {
/*  746 */       int numberOfTries = 2;
/*  747 */       while (numberOfTries > 0) {
/*  748 */         Connection _conn = getConnection();
/*  749 */         if (_conn == null) {
/*  750 */           return;
/*      */         }
/*      */         try
/*      */         {
/*  754 */           if (this.preparedClearSql == null) {
/*  755 */             String clearSql = "DELETE FROM " + this.sessionTable + " WHERE " + this.sessionAppCol + " = ?";
/*      */             
/*  757 */             this.preparedClearSql = _conn.prepareStatement(clearSql);
/*      */           }
/*      */           
/*  760 */           this.preparedClearSql.setString(1, getName());
/*  761 */           this.preparedClearSql.execute();
/*      */           
/*  763 */           numberOfTries = 0;
/*      */         } catch (SQLException e) {
/*  765 */           this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  766 */           if (this.dbConnection != null)
/*  767 */             close(this.dbConnection);
/*      */         } finally {
/*  769 */           release(_conn);
/*      */         }
/*  771 */         numberOfTries--;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void save(Session session)
/*      */     throws IOException
/*      */   {
/*  784 */     ByteArrayOutputStream bos = null;
/*      */     
/*  786 */     synchronized (this) {
/*  787 */       int numberOfTries = 2;
/*  788 */       while (numberOfTries > 0) {
/*  789 */         Connection _conn = getConnection();
/*  790 */         if (_conn == null) {
/*  791 */           return;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/*  798 */           remove(session.getIdInternal(), _conn);
/*      */           
/*  800 */           bos = new ByteArrayOutputStream();
/*  801 */           ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));Throwable localThrowable9 = null;
/*      */           try {
/*  803 */             ((StandardSession)session).writeObjectData(oos);
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/*  801 */             localThrowable9 = localThrowable1;throw localThrowable1;
/*      */           }
/*      */           finally {
/*  804 */             if (oos != null) if (localThrowable9 != null) try { oos.close(); } catch (Throwable localThrowable2) {} else oos.close(); }
/*  805 */           byte[] obs = bos.toByteArray();
/*  806 */           int size = obs.length;
/*  807 */           ByteArrayInputStream bis = new ByteArrayInputStream(obs, 0, size);Throwable localThrowable10 = null;
/*  808 */           try { InputStream in = new BufferedInputStream(bis, size);Throwable localThrowable11 = null;
/*  809 */             try { if (this.preparedSaveSql == null) {
/*  810 */                 String saveSql = "INSERT INTO " + this.sessionTable + " (" + this.sessionIdCol + ", " + this.sessionAppCol + ", " + this.sessionDataCol + ", " + this.sessionValidCol + ", " + this.sessionMaxInactiveCol + ", " + this.sessionLastAccessedCol + ") VALUES (?, ?, ?, ?, ?, ?)";
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  816 */                 this.preparedSaveSql = _conn.prepareStatement(saveSql);
/*      */               }
/*      */               
/*  819 */               this.preparedSaveSql.setString(1, session.getIdInternal());
/*  820 */               this.preparedSaveSql.setString(2, getName());
/*  821 */               this.preparedSaveSql.setBinaryStream(3, in, size);
/*  822 */               this.preparedSaveSql.setString(4, session.isValid() ? "1" : "0");
/*  823 */               this.preparedSaveSql.setInt(5, session.getMaxInactiveInterval());
/*  824 */               this.preparedSaveSql.setLong(6, session.getLastAccessedTime());
/*  825 */               this.preparedSaveSql.execute();
/*      */               
/*  827 */               numberOfTries = 0;
/*      */             }
/*      */             catch (Throwable localThrowable4)
/*      */             {
/*  807 */               localThrowable11 = localThrowable4;throw localThrowable4; } finally {} } catch (Throwable localThrowable7) { localThrowable10 = localThrowable7;throw localThrowable7;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*      */           finally
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  828 */             if (bis != null) if (localThrowable10 != null) try { bis.close(); } catch (Throwable localThrowable8) {} else bis.close();
/*      */           }
/*  830 */         } catch (SQLException e) { this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".SQLException", new Object[] { e }));
/*  831 */           if (this.dbConnection != null) {
/*  832 */             close(this.dbConnection);
/*      */           }
/*      */         }
/*      */         catch (IOException localIOException) {}finally {
/*  836 */           release(_conn);
/*      */         }
/*  838 */         numberOfTries--;
/*      */       }
/*      */     }
/*      */     
/*  842 */     if (this.manager.getContext().getLogger().isDebugEnabled()) {
/*  843 */       this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".saving", new Object[] {session
/*  844 */         .getIdInternal(), this.sessionTable }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Connection getConnection()
/*      */   {
/*  859 */     Connection conn = null;
/*      */     try {
/*  861 */       conn = open();
/*  862 */       if ((conn == null) || (conn.isClosed())) {
/*  863 */         this.manager.getContext().getLogger().info(sm.getString(getStoreName() + ".checkConnectionDBClosed"));
/*  864 */         conn = open();
/*  865 */         if ((conn == null) || (conn.isClosed())) {
/*  866 */           this.manager.getContext().getLogger().info(sm.getString(getStoreName() + ".checkConnectionDBReOpenFail"));
/*      */         }
/*      */       }
/*      */     } catch (SQLException ex) {
/*  870 */       this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".checkConnectionSQLException", new Object[] {ex
/*  871 */         .toString() }));
/*      */     }
/*      */     
/*  874 */     return conn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Connection open()
/*      */     throws SQLException
/*      */   {
/*  888 */     if (this.dbConnection != null) {
/*  889 */       return this.dbConnection;
/*      */     }
/*  891 */     if ((this.dataSourceName != null) && (this.dataSource == null)) {
/*  892 */       org.apache.catalina.Context context = getManager().getContext();
/*  893 */       ClassLoader oldThreadContextCL = null;
/*  894 */       if (this.localDataSource) {
/*  895 */         oldThreadContextCL = context.bind(Globals.IS_SECURITY_ENABLED, null);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  900 */         javax.naming.Context initCtx = new InitialContext();
/*  901 */         javax.naming.Context envCtx = (javax.naming.Context)initCtx.lookup("java:comp/env");
/*  902 */         this.dataSource = ((DataSource)envCtx.lookup(this.dataSourceName));
/*      */       } catch (NamingException e) {
/*  904 */         context.getLogger().error(sm
/*  905 */           .getString(getStoreName() + ".wrongDataSource", new Object[] { this.dataSourceName }), e);
/*      */       }
/*      */       finally {
/*  908 */         if (this.localDataSource) {
/*  909 */           context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  914 */     if (this.dataSource != null) {
/*  915 */       return this.dataSource.getConnection();
/*      */     }
/*      */     
/*      */ 
/*  919 */     if (this.driver == null) {
/*      */       try {
/*  921 */         Class<?> clazz = Class.forName(this.driverName);
/*  922 */         this.driver = ((Driver)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*      */       } catch (ReflectiveOperationException e) {
/*  924 */         this.manager.getContext().getLogger().error(sm
/*  925 */           .getString(getStoreName() + ".checkConnectionClassNotFoundException", new Object[] {e
/*  926 */           .toString() }));
/*  927 */         throw new SQLException(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  932 */     Properties props = new Properties();
/*  933 */     if (this.connectionName != null)
/*  934 */       props.put("user", this.connectionName);
/*  935 */     if (this.connectionPassword != null)
/*  936 */       props.put("password", this.connectionPassword);
/*  937 */     this.dbConnection = this.driver.connect(this.connectionURL, props);
/*  938 */     this.dbConnection.setAutoCommit(true);
/*  939 */     return this.dbConnection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void close(Connection dbConnection)
/*      */   {
/*  951 */     if (dbConnection == null) {
/*  952 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  956 */       this.preparedSizeSql.close();
/*      */     } catch (Throwable f) {
/*  958 */       ExceptionUtils.handleThrowable(f);
/*      */     }
/*  960 */     this.preparedSizeSql = null;
/*      */     try
/*      */     {
/*  963 */       this.preparedSaveSql.close();
/*      */     } catch (Throwable f) {
/*  965 */       ExceptionUtils.handleThrowable(f);
/*      */     }
/*  967 */     this.preparedSaveSql = null;
/*      */     try
/*      */     {
/*  970 */       this.preparedClearSql.close();
/*      */     } catch (Throwable f) {
/*  972 */       ExceptionUtils.handleThrowable(f);
/*      */     }
/*      */     try
/*      */     {
/*  976 */       this.preparedRemoveSql.close();
/*      */     } catch (Throwable f) {
/*  978 */       ExceptionUtils.handleThrowable(f);
/*      */     }
/*  980 */     this.preparedRemoveSql = null;
/*      */     try
/*      */     {
/*  983 */       this.preparedLoadSql.close();
/*      */     } catch (Throwable f) {
/*  985 */       ExceptionUtils.handleThrowable(f);
/*      */     }
/*  987 */     this.preparedLoadSql = null;
/*      */     
/*      */     try
/*      */     {
/*  991 */       if (!dbConnection.getAutoCommit()) {
/*  992 */         dbConnection.commit();
/*      */       }
/*      */     } catch (SQLException e) {
/*  995 */       this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".commitSQLException"), e);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1000 */       dbConnection.close();
/*      */     } catch (SQLException e) {
/* 1002 */       this.manager.getContext().getLogger().error(sm.getString(getStoreName() + ".close", new Object[] { e.toString() }));
/*      */     } finally {
/* 1004 */       this.dbConnection = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void release(Connection conn)
/*      */   {
/* 1016 */     if (this.dataSource != null) {
/* 1017 */       close(conn);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void startInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1031 */     if (this.dataSourceName == null)
/*      */     {
/* 1033 */       this.dbConnection = getConnection();
/*      */     }
/*      */     
/* 1036 */     super.startInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1049 */     super.stopInternal();
/*      */     
/*      */ 
/* 1052 */     if (this.dbConnection != null) {
/*      */       try {
/* 1054 */         this.dbConnection.commit();
/*      */       }
/*      */       catch (SQLException localSQLException) {}
/*      */       
/* 1058 */       close(this.dbConnection);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\JDBCStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */