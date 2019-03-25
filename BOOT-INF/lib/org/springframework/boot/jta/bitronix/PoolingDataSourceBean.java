/*     */ package org.springframework.boot.jta.bitronix;
/*     */ 
/*     */ import bitronix.tm.resource.common.ResourceBean;
/*     */ import bitronix.tm.resource.common.XAStatefulHolder;
/*     */ import bitronix.tm.resource.jdbc.PoolingDataSource;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.sql.XADataSource;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.lang.UsesJava7;
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
/*     */ @ConfigurationProperties(prefix="spring.jta.bitronix.datasource")
/*     */ public class PoolingDataSourceBean
/*     */   extends PoolingDataSource
/*     */   implements BeanNameAware, InitializingBean
/*     */ {
/*  53 */   private static final ThreadLocal<PoolingDataSourceBean> source = new ThreadLocal();
/*     */   
/*     */   private XADataSource dataSource;
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   public PoolingDataSourceBean()
/*     */   {
/*  61 */     setMaxPoolSize(10);
/*  62 */     setAllowLocalTransactions(true);
/*  63 */     setEnableJdbc4ConnectionTest(true);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized void init()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 2	org/springframework/boot/jta/bitronix/PoolingDataSourceBean:source	Ljava/lang/ThreadLocal;
/*     */     //   3: aload_0
/*     */     //   4: invokevirtual 7	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   7: aload_0
/*     */     //   8: invokespecial 8	bitronix/tm/resource/jdbc/PoolingDataSource:init	()V
/*     */     //   11: getstatic 2	org/springframework/boot/jta/bitronix/PoolingDataSourceBean:source	Ljava/lang/ThreadLocal;
/*     */     //   14: invokevirtual 9	java/lang/ThreadLocal:remove	()V
/*     */     //   17: goto +12 -> 29
/*     */     //   20: astore_1
/*     */     //   21: getstatic 2	org/springframework/boot/jta/bitronix/PoolingDataSourceBean:source	Ljava/lang/ThreadLocal;
/*     */     //   24: invokevirtual 9	java/lang/ThreadLocal:remove	()V
/*     */     //   27: aload_1
/*     */     //   28: athrow
/*     */     //   29: return
/*     */     // Line number table:
/*     */     //   Java source line #68	-> byte code offset #0
/*     */     //   Java source line #70	-> byte code offset #7
/*     */     //   Java source line #73	-> byte code offset #11
/*     */     //   Java source line #74	-> byte code offset #17
/*     */     //   Java source line #73	-> byte code offset #20
/*     */     //   Java source line #75	-> byte code offset #29
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	30	0	this	PoolingDataSourceBean
/*     */     //   20	8	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	11	20	finally
/*     */   }
/*     */   
/*     */   public void setBeanName(String name)
/*     */   {
/*  79 */     this.beanName = name;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  84 */     if (!StringUtils.hasLength(getUniqueName())) {
/*  85 */       setUniqueName(this.beanName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDataSource(XADataSource dataSource)
/*     */   {
/*  95 */     this.dataSource = dataSource;
/*  96 */     setClassName(DirectXADataSource.class.getName());
/*  97 */     setDriverProperties(new Properties());
/*     */   }
/*     */   
/*     */   protected final XADataSource getDataSource() {
/* 101 */     return this.dataSource;
/*     */   }
/*     */   
/*     */   public XAStatefulHolder createPooledConnection(Object xaFactory, ResourceBean bean)
/*     */     throws Exception
/*     */   {
/* 107 */     if ((xaFactory instanceof DirectXADataSource)) {
/* 108 */       xaFactory = ((DirectXADataSource)xaFactory).getDataSource();
/*     */     }
/* 110 */     return super.createPooledConnection(xaFactory, bean);
/*     */   }
/*     */   
/*     */   @UsesJava7
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException
/*     */   {
/*     */     try {
/* 117 */       return getParentLogger();
/*     */     }
/*     */     catch (Exception ex) {}
/*     */     
/* 121 */     return Logger.getLogger("global");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class DirectXADataSource
/*     */     implements XADataSource
/*     */   {
/*     */     private final XADataSource dataSource;
/*     */     
/*     */ 
/*     */ 
/*     */     public DirectXADataSource()
/*     */     {
/* 136 */       this.dataSource = ((PoolingDataSourceBean)PoolingDataSourceBean.source.get()).dataSource;
/*     */     }
/*     */     
/*     */     public PrintWriter getLogWriter() throws SQLException
/*     */     {
/* 141 */       return this.dataSource.getLogWriter();
/*     */     }
/*     */     
/*     */     public XAConnection getXAConnection() throws SQLException
/*     */     {
/* 146 */       return this.dataSource.getXAConnection();
/*     */     }
/*     */     
/*     */     public XAConnection getXAConnection(String user, String password)
/*     */       throws SQLException
/*     */     {
/* 152 */       return this.dataSource.getXAConnection(user, password);
/*     */     }
/*     */     
/*     */     public void setLogWriter(PrintWriter out) throws SQLException
/*     */     {
/* 157 */       this.dataSource.setLogWriter(out);
/*     */     }
/*     */     
/*     */     public void setLoginTimeout(int seconds) throws SQLException
/*     */     {
/* 162 */       this.dataSource.setLoginTimeout(seconds);
/*     */     }
/*     */     
/*     */     public int getLoginTimeout() throws SQLException
/*     */     {
/* 167 */       return this.dataSource.getLoginTimeout();
/*     */     }
/*     */     
/*     */     @UsesJava7
/*     */     public Logger getParentLogger() throws SQLFeatureNotSupportedException
/*     */     {
/* 173 */       return this.dataSource.getParentLogger();
/*     */     }
/*     */     
/*     */     public XADataSource getDataSource() {
/* 177 */       return this.dataSource;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\bitronix\PoolingDataSourceBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */