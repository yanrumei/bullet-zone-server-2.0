/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.AccessLog;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public final class JDBCAccessLogValve
/*     */   extends ValveBase
/*     */   implements AccessLog
/*     */ {
/*     */   public JDBCAccessLogValve()
/*     */   {
/* 140 */     super(true);
/* 141 */     this.driverName = null;
/* 142 */     this.connectionURL = null;
/* 143 */     this.tableName = "access";
/* 144 */     this.remoteHostField = "remoteHost";
/* 145 */     this.userField = "userName";
/* 146 */     this.timestampField = "timestamp";
/* 147 */     this.virtualHostField = "virtualHost";
/* 148 */     this.methodField = "method";
/* 149 */     this.queryField = "query";
/* 150 */     this.statusField = "status";
/* 151 */     this.bytesField = "bytes";
/* 152 */     this.refererField = "referer";
/* 153 */     this.userAgentField = "userAgent";
/* 154 */     this.pattern = "common";
/* 155 */     this.resolveHosts = false;
/* 156 */     this.conn = null;
/* 157 */     this.ps = null;
/* 158 */     this.currentTimeMillis = new Date().getTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */   boolean useLongContentLength = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 173 */   String connectionName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */   String connectionPassword = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 184 */   Driver driver = null;
/*     */   
/*     */   private String driverName;
/*     */   
/*     */   private String connectionURL;
/*     */   
/*     */   private String tableName;
/*     */   
/*     */   private String remoteHostField;
/*     */   
/*     */   private String userField;
/*     */   
/*     */   private String timestampField;
/*     */   
/*     */   private String virtualHostField;
/*     */   
/*     */   private String methodField;
/*     */   
/*     */   private String queryField;
/*     */   
/*     */   private String statusField;
/*     */   
/*     */   private String bytesField;
/*     */   
/*     */   private String refererField;
/*     */   
/*     */   private String userAgentField;
/*     */   private String pattern;
/*     */   private boolean resolveHosts;
/*     */   private Connection conn;
/*     */   private PreparedStatement ps;
/*     */   private long currentTimeMillis;
/* 216 */   boolean requestAttributesEnabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequestAttributesEnabled(boolean requestAttributesEnabled)
/*     */   {
/* 227 */     this.requestAttributesEnabled = requestAttributesEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getRequestAttributesEnabled()
/*     */   {
/* 235 */     return this.requestAttributesEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionName()
/*     */   {
/* 242 */     return this.connectionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionName(String connectionName)
/*     */   {
/* 251 */     this.connectionName = connectionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDriverName(String driverName)
/*     */   {
/* 260 */     this.driverName = driverName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getConnectionPassword()
/*     */   {
/* 267 */     return this.connectionPassword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionPassword(String connectionPassword)
/*     */   {
/* 276 */     this.connectionPassword = connectionPassword;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionURL(String connectionURL)
/*     */   {
/* 285 */     this.connectionURL = connectionURL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTableName(String tableName)
/*     */   {
/* 295 */     this.tableName = tableName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoteHostField(String remoteHostField)
/*     */   {
/* 305 */     this.remoteHostField = remoteHostField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserField(String userField)
/*     */   {
/* 315 */     this.userField = userField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestampField(String timestampField)
/*     */   {
/* 325 */     this.timestampField = timestampField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVirtualHostField(String virtualHostField)
/*     */   {
/* 336 */     this.virtualHostField = virtualHostField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMethodField(String methodField)
/*     */   {
/* 346 */     this.methodField = methodField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setQueryField(String queryField)
/*     */   {
/* 357 */     this.queryField = queryField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStatusField(String statusField)
/*     */   {
/* 367 */     this.statusField = statusField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytesField(String bytesField)
/*     */   {
/* 377 */     this.bytesField = bytesField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRefererField(String refererField)
/*     */   {
/* 387 */     this.refererField = refererField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserAgentField(String userAgentField)
/*     */   {
/* 397 */     this.userAgentField = userAgentField;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPattern(String pattern)
/*     */   {
/* 410 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResolveHosts(String resolveHosts)
/*     */   {
/* 421 */     this.resolveHosts = Boolean.parseBoolean(resolveHosts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUseLongContentLength()
/*     */   {
/* 429 */     return this.useLongContentLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUseLongContentLength(boolean useLongContentLength)
/*     */   {
/* 436 */     this.useLongContentLength = useLongContentLength;
/*     */   }
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
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 455 */     getNext().invoke(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */   public void log(Request request, Response response, long time)
/*     */   {
/* 461 */     if (!getState().isAvailable()) {
/* 462 */       return;
/*     */     }
/*     */     
/* 465 */     String EMPTY = "";
/*     */     String remoteHost;
/*     */     String remoteHost;
/* 468 */     if (this.resolveHosts) { String remoteHost;
/* 469 */       if (this.requestAttributesEnabled) {
/* 470 */         Object host = request.getAttribute("org.apache.catalina.AccessLog.RemoteHost");
/* 471 */         String remoteHost; if (host == null) {
/* 472 */           remoteHost = request.getRemoteHost();
/*     */         } else {
/* 474 */           remoteHost = (String)host;
/*     */         }
/*     */       } else {
/* 477 */         remoteHost = request.getRemoteHost();
/*     */       }
/*     */     } else { String remoteHost;
/* 480 */       if (this.requestAttributesEnabled) {
/* 481 */         Object addr = request.getAttribute("org.apache.catalina.AccessLog.RemoteAddr");
/* 482 */         String remoteHost; if (addr == null) {
/* 483 */           remoteHost = request.getRemoteAddr();
/*     */         } else {
/* 485 */           remoteHost = (String)addr;
/*     */         }
/*     */       } else {
/* 488 */         remoteHost = request.getRemoteAddr();
/*     */       }
/*     */     }
/* 491 */     String user = request.getRemoteUser();
/* 492 */     String query = request.getRequestURI();
/*     */     
/* 494 */     long bytes = response.getBytesWritten(true);
/* 495 */     if (bytes < 0L) {
/* 496 */       bytes = 0L;
/*     */     }
/* 498 */     int status = response.getStatus();
/* 499 */     String virtualHost = "";
/* 500 */     String method = "";
/* 501 */     String referer = "";
/* 502 */     String userAgent = "";
/* 503 */     String logPattern = this.pattern;
/* 504 */     if (logPattern.equals("combined")) {
/* 505 */       virtualHost = request.getServerName();
/* 506 */       method = request.getMethod();
/* 507 */       referer = request.getHeader("referer");
/* 508 */       userAgent = request.getHeader("user-agent");
/*     */     }
/* 510 */     synchronized (this) {
/* 511 */       int numberOfTries = 2;
/* 512 */       while (numberOfTries > 0) {
/*     */         try {
/* 514 */           open();
/*     */           
/* 516 */           this.ps.setString(1, remoteHost);
/* 517 */           this.ps.setString(2, user);
/* 518 */           this.ps.setTimestamp(3, new Timestamp(getCurrentTimeMillis()));
/* 519 */           this.ps.setString(4, query);
/* 520 */           this.ps.setInt(5, status);
/*     */           
/* 522 */           if (this.useLongContentLength) {
/* 523 */             this.ps.setLong(6, bytes);
/*     */           } else {
/* 525 */             if (bytes > 2147483647L) {
/* 526 */               bytes = -1L;
/*     */             }
/* 528 */             this.ps.setInt(6, (int)bytes);
/*     */           }
/* 530 */           if (logPattern.equals("combined")) {
/* 531 */             this.ps.setString(7, virtualHost);
/* 532 */             this.ps.setString(8, method);
/* 533 */             this.ps.setString(9, referer);
/* 534 */             this.ps.setString(10, userAgent);
/*     */           }
/* 536 */           this.ps.executeUpdate();
/* 537 */           return;
/*     */         }
/*     */         catch (SQLException e) {
/* 540 */           this.container.getLogger().error(sm.getString("jdbcAccessLogValve.exception"), e);
/*     */           
/*     */ 
/* 543 */           if (this.conn != null) {
/* 544 */             close();
/*     */           }
/*     */         }
/* 547 */         numberOfTries--;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void open()
/*     */     throws SQLException
/*     */   {
/* 563 */     if (this.conn != null) {
/* 564 */       return;
/*     */     }
/*     */     
/*     */ 
/* 568 */     if (this.driver == null) {
/*     */       try {
/* 570 */         Class<?> clazz = Class.forName(this.driverName);
/* 571 */         this.driver = ((Driver)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */       } catch (Throwable e) {
/* 573 */         ExceptionUtils.handleThrowable(e);
/* 574 */         throw new SQLException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 579 */     Properties props = new Properties();
/* 580 */     if (this.connectionName != null) {
/* 581 */       props.put("user", this.connectionName);
/*     */     }
/* 583 */     if (this.connectionPassword != null) {
/* 584 */       props.put("password", this.connectionPassword);
/*     */     }
/* 586 */     this.conn = this.driver.connect(this.connectionURL, props);
/* 587 */     this.conn.setAutoCommit(true);
/* 588 */     String logPattern = this.pattern;
/* 589 */     if (logPattern.equals("common"))
/*     */     {
/* 591 */       this.ps = this.conn.prepareStatement("INSERT INTO " + this.tableName + " (" + this.remoteHostField + ", " + this.userField + ", " + this.timestampField + ", " + this.queryField + ", " + this.statusField + ", " + this.bytesField + ") VALUES(?, ?, ?, ?, ?, ?)");
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 596 */     else if (logPattern.equals("combined"))
/*     */     {
/* 598 */       this.ps = this.conn.prepareStatement("INSERT INTO " + this.tableName + " (" + this.remoteHostField + ", " + this.userField + ", " + this.timestampField + ", " + this.queryField + ", " + this.statusField + ", " + this.bytesField + ", " + this.virtualHostField + ", " + this.methodField + ", " + this.refererField + ", " + this.userAgentField + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
/*     */     }
/*     */   }
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
/*     */   protected void close()
/*     */   {
/* 614 */     if (this.conn == null) {
/* 615 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 620 */       this.ps.close();
/*     */     } catch (Throwable f) {
/* 622 */       ExceptionUtils.handleThrowable(f);
/*     */     }
/* 624 */     this.ps = null;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 630 */       this.conn.close();
/*     */     } catch (SQLException e) {
/* 632 */       this.container.getLogger().error(sm.getString("jdbcAccessLogValve.close"), e);
/*     */     } finally {
/* 634 */       this.conn = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/*     */     try
/*     */     {
/* 651 */       open();
/*     */     } catch (SQLException e) {
/* 653 */       throw new LifecycleException(e);
/*     */     }
/*     */     
/* 656 */     setState(LifecycleState.STARTING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 670 */     setState(LifecycleState.STOPPING);
/*     */     
/* 672 */     close();
/*     */   }
/*     */   
/*     */   public long getCurrentTimeMillis()
/*     */   {
/* 677 */     long systime = System.currentTimeMillis();
/* 678 */     if (systime - this.currentTimeMillis > 1000L) {
/* 679 */       this.currentTimeMillis = new Date(systime).getTime();
/*     */     }
/* 681 */     return this.currentTimeMillis;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\JDBCAccessLogValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */