/*     */ package org.springframework.boot.jta.atomikos;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="spring.jta.atomikos.properties")
/*     */ public class AtomikosProperties
/*     */ {
/*     */   private String service;
/*  45 */   private long maxTimeout = 300000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private long defaultJtaTimeout = 10000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private int maxActives = 50;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   private boolean enableLogging = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String transactionManagerUniqueName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private boolean serialJtaTransactions = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean forceShutdownOnVmExit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   private String logBaseName = "tmlog";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String logBaseDir;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private long checkpointInterval = 500L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean threadedTwoPhaseCommit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setService(String service)
/*     */   {
/* 110 */     this.service = service;
/*     */   }
/*     */   
/*     */   public String getService() {
/* 114 */     return this.service;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxTimeout(long maxTimeout)
/*     */   {
/* 125 */     this.maxTimeout = maxTimeout;
/*     */   }
/*     */   
/*     */   public long getMaxTimeout() {
/* 129 */     return this.maxTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultJtaTimeout(long defaultJtaTimeout)
/*     */   {
/* 138 */     this.defaultJtaTimeout = defaultJtaTimeout;
/*     */   }
/*     */   
/*     */   public long getDefaultJtaTimeout() {
/* 142 */     return this.defaultJtaTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxActives(int maxActives)
/*     */   {
/* 154 */     this.maxActives = maxActives;
/*     */   }
/*     */   
/*     */   public int getMaxActives() {
/* 158 */     return this.maxActives;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnableLogging(boolean enableLogging)
/*     */   {
/* 169 */     this.enableLogging = enableLogging;
/*     */   }
/*     */   
/*     */   public boolean isEnableLogging() {
/* 173 */     return this.enableLogging;
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
/*     */   public void setTransactionManagerUniqueName(String uniqueName)
/*     */   {
/* 188 */     this.transactionManagerUniqueName = uniqueName;
/*     */   }
/*     */   
/*     */   public String getTransactionManagerUniqueName() {
/* 192 */     return this.transactionManagerUniqueName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerialJtaTransactions(boolean serialJtaTransactions)
/*     */   {
/* 204 */     this.serialJtaTransactions = serialJtaTransactions;
/*     */   }
/*     */   
/*     */   public boolean isSerialJtaTransactions() {
/* 208 */     return this.serialJtaTransactions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForceShutdownOnVmExit(boolean forceShutdownOnVmExit)
/*     */   {
/* 217 */     this.forceShutdownOnVmExit = forceShutdownOnVmExit;
/*     */   }
/*     */   
/*     */   public boolean isForceShutdownOnVmExit() {
/* 221 */     return this.forceShutdownOnVmExit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogBaseName(String logBaseName)
/*     */   {
/* 232 */     this.logBaseName = logBaseName;
/*     */   }
/*     */   
/*     */   public String getLogBaseName() {
/* 236 */     return this.logBaseName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogBaseDir(String logBaseDir)
/*     */   {
/* 247 */     this.logBaseDir = logBaseDir;
/*     */   }
/*     */   
/*     */   public String getLogBaseDir() {
/* 251 */     return this.logBaseDir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckpointInterval(long checkpointInterval)
/*     */   {
/* 260 */     this.checkpointInterval = checkpointInterval;
/*     */   }
/*     */   
/*     */   public long getCheckpointInterval() {
/* 264 */     return this.checkpointInterval;
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
/*     */   public void setThreadedTwoPhaseCommit(boolean threadedTwoPhaseCommit)
/*     */   {
/* 277 */     this.threadedTwoPhaseCommit = threadedTwoPhaseCommit;
/*     */   }
/*     */   
/*     */   public boolean isThreadedTwoPhaseCommit() {
/* 281 */     return this.threadedTwoPhaseCommit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Properties asProperties()
/*     */   {
/* 290 */     Properties properties = new Properties();
/* 291 */     set(properties, "service", getService());
/* 292 */     set(properties, "max_timeout", Long.valueOf(getMaxTimeout()));
/* 293 */     set(properties, "default_jta_timeout", Long.valueOf(getDefaultJtaTimeout()));
/* 294 */     set(properties, "max_actives", Integer.valueOf(getMaxActives()));
/* 295 */     set(properties, "enable_logging", Boolean.valueOf(isEnableLogging()));
/* 296 */     set(properties, "tm_unique_name", getTransactionManagerUniqueName());
/* 297 */     set(properties, "serial_jta_transactions", Boolean.valueOf(isSerialJtaTransactions()));
/* 298 */     set(properties, "force_shutdown_on_vm_exit", Boolean.valueOf(isForceShutdownOnVmExit()));
/* 299 */     set(properties, "log_base_name", getLogBaseName());
/* 300 */     set(properties, "log_base_dir", getLogBaseDir());
/* 301 */     set(properties, "checkpoint_interval", Long.valueOf(getCheckpointInterval()));
/* 302 */     set(properties, "threaded_2pc", Boolean.valueOf(isThreadedTwoPhaseCommit()));
/* 303 */     return properties;
/*     */   }
/*     */   
/*     */   private void set(Properties properties, String key, Object value) {
/* 307 */     String id = "com.atomikos.icatch." + key;
/* 308 */     if ((value != null) && (!properties.containsKey(id))) {
/* 309 */       properties.setProperty(id, value.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\atomikos\AtomikosProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */