/*     */ package org.springframework.boot.autoconfigure.jms.activemq;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @ConfigurationProperties(prefix="spring.activemq")
/*     */ public class ActiveMQProperties
/*     */ {
/*     */   private String brokerUrl;
/*  44 */   private boolean inMemory = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String user;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private int closeTimeout = 15000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private boolean nonBlockingRedelivery = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private int sendTimeout = 0;
/*     */   
/*  74 */   private Pool pool = new Pool();
/*     */   
/*  76 */   private Packages packages = new Packages();
/*     */   
/*     */   public String getBrokerUrl() {
/*  79 */     return this.brokerUrl;
/*     */   }
/*     */   
/*     */   public void setBrokerUrl(String brokerUrl) {
/*  83 */     this.brokerUrl = brokerUrl;
/*     */   }
/*     */   
/*     */   public boolean isInMemory() {
/*  87 */     return this.inMemory;
/*     */   }
/*     */   
/*     */   public void setInMemory(boolean inMemory) {
/*  91 */     this.inMemory = inMemory;
/*     */   }
/*     */   
/*     */   public String getUser() {
/*  95 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/*  99 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 103 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 107 */     this.password = password;
/*     */   }
/*     */   
/*     */   public int getCloseTimeout() {
/* 111 */     return this.closeTimeout;
/*     */   }
/*     */   
/*     */   public void setCloseTimeout(int closeTimeout) {
/* 115 */     this.closeTimeout = closeTimeout;
/*     */   }
/*     */   
/*     */   public boolean isNonBlockingRedelivery() {
/* 119 */     return this.nonBlockingRedelivery;
/*     */   }
/*     */   
/*     */   public void setNonBlockingRedelivery(boolean nonBlockingRedelivery) {
/* 123 */     this.nonBlockingRedelivery = nonBlockingRedelivery;
/*     */   }
/*     */   
/*     */   public int getSendTimeout() {
/* 127 */     return this.sendTimeout;
/*     */   }
/*     */   
/*     */   public void setSendTimeout(int sendTimeout) {
/* 131 */     this.sendTimeout = sendTimeout;
/*     */   }
/*     */   
/*     */   public Pool getPool() {
/* 135 */     return this.pool;
/*     */   }
/*     */   
/*     */   public void setPool(Pool pool) {
/* 139 */     this.pool = pool;
/*     */   }
/*     */   
/*     */   public Packages getPackages() {
/* 143 */     return this.packages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Pool
/*     */   {
/*     */     private boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 158 */     private boolean blockIfFull = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     private long blockIfFullTimeout = -1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     private boolean createConnectionOnStartup = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 174 */     private long expiryTimeout = 0L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 179 */     private int idleTimeout = 30000;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 184 */     private int maxConnections = 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 189 */     private int maximumActiveSessionPerConnection = 500;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 194 */     private boolean reconnectOnException = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */     private long timeBetweenExpirationCheck = -1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */     private boolean useAnonymousProducers = true;
/*     */     
/*     */     public boolean isEnabled() {
/* 209 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 213 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public boolean isBlockIfFull() {
/* 217 */       return this.blockIfFull;
/*     */     }
/*     */     
/*     */     public void setBlockIfFull(boolean blockIfFull) {
/* 221 */       this.blockIfFull = blockIfFull;
/*     */     }
/*     */     
/*     */     public long getBlockIfFullTimeout() {
/* 225 */       return this.blockIfFullTimeout;
/*     */     }
/*     */     
/*     */     public void setBlockIfFullTimeout(long blockIfFullTimeout) {
/* 229 */       this.blockIfFullTimeout = blockIfFullTimeout;
/*     */     }
/*     */     
/*     */     public boolean isCreateConnectionOnStartup() {
/* 233 */       return this.createConnectionOnStartup;
/*     */     }
/*     */     
/*     */     public void setCreateConnectionOnStartup(boolean createConnectionOnStartup) {
/* 237 */       this.createConnectionOnStartup = createConnectionOnStartup;
/*     */     }
/*     */     
/*     */     public long getExpiryTimeout() {
/* 241 */       return this.expiryTimeout;
/*     */     }
/*     */     
/*     */     public void setExpiryTimeout(long expiryTimeout) {
/* 245 */       this.expiryTimeout = expiryTimeout;
/*     */     }
/*     */     
/*     */     public int getIdleTimeout() {
/* 249 */       return this.idleTimeout;
/*     */     }
/*     */     
/*     */     public void setIdleTimeout(int idleTimeout) {
/* 253 */       this.idleTimeout = idleTimeout;
/*     */     }
/*     */     
/*     */     public int getMaxConnections() {
/* 257 */       return this.maxConnections;
/*     */     }
/*     */     
/*     */     public void setMaxConnections(int maxConnections) {
/* 261 */       this.maxConnections = maxConnections;
/*     */     }
/*     */     
/*     */     public int getMaximumActiveSessionPerConnection() {
/* 265 */       return this.maximumActiveSessionPerConnection;
/*     */     }
/*     */     
/*     */     public void setMaximumActiveSessionPerConnection(int maximumActiveSessionPerConnection)
/*     */     {
/* 270 */       this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
/*     */     }
/*     */     
/*     */     public boolean isReconnectOnException() {
/* 274 */       return this.reconnectOnException;
/*     */     }
/*     */     
/*     */     public void setReconnectOnException(boolean reconnectOnException) {
/* 278 */       this.reconnectOnException = reconnectOnException;
/*     */     }
/*     */     
/*     */     public long getTimeBetweenExpirationCheck() {
/* 282 */       return this.timeBetweenExpirationCheck;
/*     */     }
/*     */     
/*     */     public void setTimeBetweenExpirationCheck(long timeBetweenExpirationCheck) {
/* 286 */       this.timeBetweenExpirationCheck = timeBetweenExpirationCheck;
/*     */     }
/*     */     
/*     */     public boolean isUseAnonymousProducers() {
/* 290 */       return this.useAnonymousProducers;
/*     */     }
/*     */     
/*     */     public void setUseAnonymousProducers(boolean useAnonymousProducers) {
/* 294 */       this.useAnonymousProducers = useAnonymousProducers;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Packages
/*     */   {
/*     */     private Boolean trustAll;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 310 */     private List<String> trusted = new ArrayList();
/*     */     
/*     */     public Boolean getTrustAll() {
/* 313 */       return this.trustAll;
/*     */     }
/*     */     
/*     */     public void setTrustAll(Boolean trustAll) {
/* 317 */       this.trustAll = trustAll;
/*     */     }
/*     */     
/*     */     public List<String> getTrusted() {
/* 321 */       return this.trusted;
/*     */     }
/*     */     
/*     */     public void setTrusted(List<String> trusted) {
/* 325 */       this.trusted = trusted;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\activemq\ActiveMQProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */