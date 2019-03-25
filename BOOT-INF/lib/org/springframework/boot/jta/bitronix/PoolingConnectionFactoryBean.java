/*     */ package org.springframework.boot.jta.bitronix;
/*     */ 
/*     */ import bitronix.tm.resource.common.ResourceBean;
/*     */ import bitronix.tm.resource.common.XAStatefulHolder;
/*     */ import bitronix.tm.resource.jms.PoolingConnectionFactory;
/*     */ import java.util.Properties;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.XAConnection;
/*     */ import javax.jms.XAConnectionFactory;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.jta.bitronix.connectionfactory")
/*     */ public class PoolingConnectionFactoryBean
/*     */   extends PoolingConnectionFactory
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*  49 */   private static final ThreadLocal<PoolingConnectionFactoryBean> source = new ThreadLocal();
/*     */   
/*     */   private String beanName;
/*     */   private XAConnectionFactory connectionFactory;
/*     */   
/*     */   public PoolingConnectionFactoryBean()
/*     */   {
/*  56 */     setMaxPoolSize(10);
/*  57 */     setTestConnections(true);
/*  58 */     setAutomaticEnlistingEnabled(true);
/*  59 */     setAllowLocalTransactions(true);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized void init()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 2	org/springframework/boot/jta/bitronix/PoolingConnectionFactoryBean:source	Ljava/lang/ThreadLocal;
/*     */     //   3: aload_0
/*     */     //   4: invokevirtual 8	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   7: aload_0
/*     */     //   8: invokespecial 9	bitronix/tm/resource/jms/PoolingConnectionFactory:init	()V
/*     */     //   11: getstatic 2	org/springframework/boot/jta/bitronix/PoolingConnectionFactoryBean:source	Ljava/lang/ThreadLocal;
/*     */     //   14: invokevirtual 10	java/lang/ThreadLocal:remove	()V
/*     */     //   17: goto +12 -> 29
/*     */     //   20: astore_1
/*     */     //   21: getstatic 2	org/springframework/boot/jta/bitronix/PoolingConnectionFactoryBean:source	Ljava/lang/ThreadLocal;
/*     */     //   24: invokevirtual 10	java/lang/ThreadLocal:remove	()V
/*     */     //   27: aload_1
/*     */     //   28: athrow
/*     */     //   29: return
/*     */     // Line number table:
/*     */     //   Java source line #64	-> byte code offset #0
/*     */     //   Java source line #66	-> byte code offset #7
/*     */     //   Java source line #69	-> byte code offset #11
/*     */     //   Java source line #70	-> byte code offset #17
/*     */     //   Java source line #69	-> byte code offset #20
/*     */     //   Java source line #71	-> byte code offset #29
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	30	0	this	PoolingConnectionFactoryBean
/*     */     //   20	8	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	11	20	finally
/*     */   }
/*     */   
/*     */   public void setBeanName(String name)
/*     */   {
/*  75 */     this.beanName = name;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  80 */     if (!StringUtils.hasLength(getUniqueName())) {
/*  81 */       setUniqueName(this.beanName);
/*     */     }
/*  83 */     init();
/*     */   }
/*     */   
/*     */   public void destroy() throws Exception
/*     */   {
/*  88 */     close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionFactory(XAConnectionFactory connectionFactory)
/*     */   {
/*  97 */     this.connectionFactory = connectionFactory;
/*  98 */     setClassName(DirectXAConnectionFactory.class.getName());
/*  99 */     setDriverProperties(new Properties());
/*     */   }
/*     */   
/*     */   protected final XAConnectionFactory getConnectionFactory() {
/* 103 */     return this.connectionFactory;
/*     */   }
/*     */   
/*     */   public XAStatefulHolder createPooledConnection(Object xaFactory, ResourceBean bean)
/*     */     throws Exception
/*     */   {
/* 109 */     if ((xaFactory instanceof DirectXAConnectionFactory)) {
/* 110 */       xaFactory = ((DirectXAConnectionFactory)xaFactory).getConnectionFactory();
/*     */     }
/* 112 */     return super.createPooledConnection(xaFactory, bean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class DirectXAConnectionFactory
/*     */     implements XAConnectionFactory
/*     */   {
/*     */     private final XAConnectionFactory connectionFactory;
/*     */     
/*     */ 
/*     */ 
/*     */     public DirectXAConnectionFactory()
/*     */     {
/* 126 */       this.connectionFactory = ((PoolingConnectionFactoryBean)PoolingConnectionFactoryBean.source.get()).connectionFactory;
/*     */     }
/*     */     
/*     */     public XAConnection createXAConnection() throws JMSException
/*     */     {
/* 131 */       return this.connectionFactory.createXAConnection();
/*     */     }
/*     */     
/*     */     public XAConnection createXAConnection(String userName, String password)
/*     */       throws JMSException
/*     */     {
/* 137 */       return this.connectionFactory.createXAConnection(userName, password);
/*     */     }
/*     */     
/*     */     public XAConnectionFactory getConnectionFactory() {
/* 141 */       return this.connectionFactory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\bitronix\PoolingConnectionFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */