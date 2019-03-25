/*    */ package org.springframework.jmx.access;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import javax.management.MBeanServerConnection;
/*    */ import javax.management.remote.JMXConnector;
/*    */ import javax.management.remote.JMXConnectorFactory;
/*    */ import javax.management.remote.JMXServiceURL;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*    */ import org.springframework.jmx.support.JmxUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConnectorDelegate
/*    */ {
/* 40 */   private static final Log logger = LogFactory.getLog(ConnectorDelegate.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private JMXConnector connector;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MBeanServerConnection connect(JMXServiceURL serviceUrl, Map<String, ?> environment, String agentId)
/*    */     throws MBeanServerNotFoundException
/*    */   {
/* 55 */     if (serviceUrl != null) {
/* 56 */       if (logger.isDebugEnabled()) {
/* 57 */         logger.debug("Connecting to remote MBeanServer at URL [" + serviceUrl + "]");
/*    */       }
/*    */       try {
/* 60 */         this.connector = JMXConnectorFactory.connect(serviceUrl, environment);
/* 61 */         return this.connector.getMBeanServerConnection();
/*    */       }
/*    */       catch (IOException ex) {
/* 64 */         throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer [" + serviceUrl + "]", ex);
/*    */       }
/*    */     }
/*    */     
/* 68 */     logger.debug("Attempting to locate local MBeanServer");
/* 69 */     return JmxUtils.locateMBeanServer(agentId);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void close()
/*    */   {
/* 77 */     if (this.connector != null) {
/*    */       try {
/* 79 */         this.connector.close();
/*    */       }
/*    */       catch (IOException ex) {
/* 82 */         logger.debug("Could not close JMX connector", ex);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\access\ConnectorDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */