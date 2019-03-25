/*    */ package org.springframework.boot.autoconfigure.jms.artemis;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Set;
/*    */ import org.apache.activemq.artemis.api.core.TransportConfiguration;
/*    */ import org.apache.activemq.artemis.core.config.Configuration;
/*    */ import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
/*    */ import org.apache.activemq.artemis.core.remoting.impl.invm.InVMAcceptorFactory;
/*    */ import org.apache.activemq.artemis.core.server.JournalType;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ class ArtemisEmbeddedConfigurationFactory
/*    */ {
/* 39 */   private static final Log logger = LogFactory.getLog(ArtemisEmbeddedConfigurationFactory.class);
/*    */   private final ArtemisProperties.Embedded properties;
/*    */   
/*    */   ArtemisEmbeddedConfigurationFactory(ArtemisProperties properties)
/*    */   {
/* 44 */     this.properties = properties.getEmbedded();
/*    */   }
/*    */   
/*    */   public Configuration createConfiguration() {
/* 48 */     ConfigurationImpl configuration = new ConfigurationImpl();
/* 49 */     configuration.setSecurityEnabled(false);
/* 50 */     configuration.setPersistenceEnabled(this.properties.isPersistent());
/* 51 */     String dataDir = getDataDir();
/* 52 */     configuration.setJournalDirectory(dataDir + "/journal");
/* 53 */     if (this.properties.isPersistent()) {
/* 54 */       configuration.setJournalType(JournalType.NIO);
/* 55 */       configuration.setLargeMessagesDirectory(dataDir + "/largemessages");
/* 56 */       configuration.setBindingsDirectory(dataDir + "/bindings");
/* 57 */       configuration.setPagingDirectory(dataDir + "/paging");
/*    */     }
/*    */     
/*    */ 
/* 61 */     TransportConfiguration transportConfiguration = new TransportConfiguration(InVMAcceptorFactory.class.getName(), this.properties.generateTransportParameters());
/* 62 */     configuration.getAcceptorConfigurations().add(transportConfiguration);
/* 63 */     if (this.properties.isDefaultClusterPassword()) {
/* 64 */       logger.debug("Using default Artemis cluster password: " + this.properties
/* 65 */         .getClusterPassword());
/*    */     }
/* 67 */     configuration.setClusterPassword(this.properties.getClusterPassword());
/* 68 */     return configuration;
/*    */   }
/*    */   
/*    */   private String getDataDir() {
/* 72 */     if (this.properties.getDataDirectory() != null) {
/* 73 */       return this.properties.getDataDirectory();
/*    */     }
/* 75 */     String tempDirectory = System.getProperty("java.io.tmpdir");
/* 76 */     return new File(tempDirectory, "artemis-data").getAbsolutePath();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisEmbeddedConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */