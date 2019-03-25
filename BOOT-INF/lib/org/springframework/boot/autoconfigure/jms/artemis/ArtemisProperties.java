/*     */ package org.springframework.boot.autoconfigure.jms.artemis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @ConfigurationProperties(prefix="spring.artemis")
/*     */ public class ArtemisProperties
/*     */ {
/*     */   private ArtemisMode mode;
/*  46 */   private String host = "localhost";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private int port = 61616;
/*     */   
/*     */ 
/*     */ 
/*     */   private String user;
/*     */   
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*  63 */   private final Embedded embedded = new Embedded();
/*     */   
/*     */   public ArtemisMode getMode() {
/*  66 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void setMode(ArtemisMode mode) {
/*  70 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public String getHost() {
/*  74 */     return this.host;
/*     */   }
/*     */   
/*     */   public void setHost(String host) {
/*  78 */     this.host = host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  82 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/*  86 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getUser() {
/*  90 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/*  94 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  98 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 102 */     this.password = password;
/*     */   }
/*     */   
/*     */   public Embedded getEmbedded() {
/* 106 */     return this.embedded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Embedded
/*     */   {
/* 114 */     private static final AtomicInteger serverIdCounter = new AtomicInteger();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 119 */     private int serverId = serverIdCounter.getAndIncrement();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 124 */     private boolean enabled = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean persistent;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String dataDirectory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 139 */     private String[] queues = new String[0];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 144 */     private String[] topics = new String[0];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 149 */     private String clusterPassword = UUID.randomUUID().toString();
/*     */     
/* 151 */     private boolean defaultClusterPassword = true;
/*     */     
/*     */     public int getServerId() {
/* 154 */       return this.serverId;
/*     */     }
/*     */     
/*     */     public void setServerId(int serverId) {
/* 158 */       this.serverId = serverId;
/*     */     }
/*     */     
/*     */     public boolean isEnabled() {
/* 162 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 166 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public boolean isPersistent() {
/* 170 */       return this.persistent;
/*     */     }
/*     */     
/*     */     public void setPersistent(boolean persistent) {
/* 174 */       this.persistent = persistent;
/*     */     }
/*     */     
/*     */     public String getDataDirectory() {
/* 178 */       return this.dataDirectory;
/*     */     }
/*     */     
/*     */     public void setDataDirectory(String dataDirectory) {
/* 182 */       this.dataDirectory = dataDirectory;
/*     */     }
/*     */     
/*     */     public String[] getQueues() {
/* 186 */       return this.queues;
/*     */     }
/*     */     
/*     */     public void setQueues(String[] queues) {
/* 190 */       this.queues = queues;
/*     */     }
/*     */     
/*     */     public String[] getTopics() {
/* 194 */       return this.topics;
/*     */     }
/*     */     
/*     */     public void setTopics(String[] topics) {
/* 198 */       this.topics = topics;
/*     */     }
/*     */     
/*     */     public String getClusterPassword() {
/* 202 */       return this.clusterPassword;
/*     */     }
/*     */     
/*     */     public void setClusterPassword(String clusterPassword) {
/* 206 */       this.clusterPassword = clusterPassword;
/* 207 */       this.defaultClusterPassword = false;
/*     */     }
/*     */     
/*     */     public boolean isDefaultClusterPassword() {
/* 211 */       return this.defaultClusterPassword;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Map<String, Object> generateTransportParameters()
/*     */     {
/* 221 */       Map<String, Object> parameters = new HashMap();
/* 222 */       parameters.put("serverId", Integer.valueOf(getServerId()));
/* 223 */       return parameters;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jms\artemis\ArtemisProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */