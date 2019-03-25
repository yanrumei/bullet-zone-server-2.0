/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXConnectorServerFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import javax.management.remote.MBeanServerForwarder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.JmxException;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectorServerFactoryBean
/*     */   extends MBeanRegistrationSupport
/*     */   implements FactoryBean<JMXConnectorServer>, InitializingBean, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_SERVICE_URL = "service:jmx:jmxmp://localhost:9875";
/*  62 */   private String serviceUrl = "service:jmx:jmxmp://localhost:9875";
/*     */   
/*  64 */   private Map<String, Object> environment = new HashMap();
/*     */   
/*     */   private MBeanServerForwarder forwarder;
/*     */   
/*     */   private ObjectName objectName;
/*     */   
/*  70 */   private boolean threaded = false;
/*     */   
/*  72 */   private boolean daemon = false;
/*     */   
/*     */ 
/*     */   private JMXConnectorServer connectorServer;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setServiceUrl(String serviceUrl)
/*     */   {
/*  81 */     this.serviceUrl = serviceUrl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(Properties environment)
/*     */   {
/*  89 */     CollectionUtils.mergePropertiesIntoMap(environment, this.environment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironmentMap(Map<String, ?> environment)
/*     */   {
/*  97 */     if (environment != null) {
/*  98 */       this.environment.putAll(environment);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setForwarder(MBeanServerForwarder forwarder)
/*     */   {
/* 106 */     this.forwarder = forwarder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectName(Object objectName)
/*     */     throws MalformedObjectNameException
/*     */   {
/* 116 */     this.objectName = ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setThreaded(boolean threaded)
/*     */   {
/* 123 */     this.threaded = threaded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDaemon(boolean daemon)
/*     */   {
/* 131 */     this.daemon = daemon;
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
/*     */   public void afterPropertiesSet()
/*     */     throws JMException, IOException
/*     */   {
/* 146 */     if (this.server == null) {
/* 147 */       this.server = JmxUtils.locateMBeanServer();
/*     */     }
/*     */     
/*     */ 
/* 151 */     JMXServiceURL url = new JMXServiceURL(this.serviceUrl);
/*     */     
/*     */ 
/* 154 */     this.connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, this.environment, this.server);
/*     */     
/*     */ 
/* 157 */     if (this.forwarder != null) {
/* 158 */       this.connectorServer.setMBeanServerForwarder(this.forwarder);
/*     */     }
/*     */     
/*     */ 
/* 162 */     if (this.objectName != null) {
/* 163 */       doRegister(this.connectorServer, this.objectName);
/*     */     }
/*     */     try
/*     */     {
/* 167 */       if (this.threaded)
/*     */       {
/* 169 */         Thread connectorThread = new Thread()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 173 */               ConnectorServerFactoryBean.this.connectorServer.start();
/*     */             }
/*     */             catch (IOException ex) {
/* 176 */               throw new JmxException("Could not start JMX connector server after delay", ex);
/*     */             }
/*     */             
/*     */           }
/* 180 */         };
/* 181 */         connectorThread.setName("JMX Connector Thread [" + this.serviceUrl + "]");
/* 182 */         connectorThread.setDaemon(this.daemon);
/* 183 */         connectorThread.start();
/*     */       }
/*     */       else
/*     */       {
/* 187 */         this.connectorServer.start();
/*     */       }
/*     */       
/* 190 */       if (this.logger.isInfoEnabled()) {
/* 191 */         this.logger.info("JMX connector server started: " + this.connectorServer);
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 197 */       unregisterBeans();
/* 198 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public JMXConnectorServer getObject()
/*     */   {
/* 205 */     return this.connectorServer;
/*     */   }
/*     */   
/*     */   public Class<? extends JMXConnectorServer> getObjectType()
/*     */   {
/* 210 */     return this.connectorServer != null ? this.connectorServer.getClass() : JMXConnectorServer.class;
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void destroy()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 35	org/springframework/jmx/support/ConnectorServerFactoryBean:logger	Lorg/apache/commons/logging/Log;
/*     */     //   4: invokeinterface 36 1 0
/*     */     //   9: ifeq +34 -> 43
/*     */     //   12: aload_0
/*     */     //   13: getfield 35	org/springframework/jmx/support/ConnectorServerFactoryBean:logger	Lorg/apache/commons/logging/Log;
/*     */     //   16: new 25	java/lang/StringBuilder
/*     */     //   19: dup
/*     */     //   20: invokespecial 26	java/lang/StringBuilder:<init>	()V
/*     */     //   23: ldc 44
/*     */     //   25: invokevirtual 28	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   28: aload_0
/*     */     //   29: getfield 1	org/springframework/jmx/support/ConnectorServerFactoryBean:connectorServer	Ljavax/management/remote/JMXConnectorServer;
/*     */     //   32: invokevirtual 38	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   35: invokevirtual 30	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   38: invokeinterface 39 2 0
/*     */     //   43: aload_0
/*     */     //   44: getfield 1	org/springframework/jmx/support/ConnectorServerFactoryBean:connectorServer	Ljavax/management/remote/JMXConnectorServer;
/*     */     //   47: invokevirtual 45	javax/management/remote/JMXConnectorServer:stop	()V
/*     */     //   50: aload_0
/*     */     //   51: invokevirtual 41	org/springframework/jmx/support/ConnectorServerFactoryBean:unregisterBeans	()V
/*     */     //   54: goto +10 -> 64
/*     */     //   57: astore_1
/*     */     //   58: aload_0
/*     */     //   59: invokevirtual 41	org/springframework/jmx/support/ConnectorServerFactoryBean:unregisterBeans	()V
/*     */     //   62: aload_1
/*     */     //   63: athrow
/*     */     //   64: return
/*     */     // Line number table:
/*     */     //   Java source line #226	-> byte code offset #0
/*     */     //   Java source line #227	-> byte code offset #12
/*     */     //   Java source line #230	-> byte code offset #43
/*     */     //   Java source line #233	-> byte code offset #50
/*     */     //   Java source line #234	-> byte code offset #54
/*     */     //   Java source line #233	-> byte code offset #57
/*     */     //   Java source line #235	-> byte code offset #64
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	65	0	this	ConnectorServerFactoryBean
/*     */     //   57	6	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   43	50	57	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\ConnectorServerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */