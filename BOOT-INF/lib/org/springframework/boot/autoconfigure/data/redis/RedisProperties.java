/*     */ package org.springframework.boot.autoconfigure.data.redis;
/*     */ 
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
/*     */ @ConfigurationProperties(prefix="spring.redis")
/*     */ public class RedisProperties
/*     */ {
/*  37 */   private int database = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String url;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private String host = "localhost";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private int port = 6379;
/*     */   
/*     */ 
/*     */   private boolean ssl;
/*     */   
/*     */ 
/*     */   private int timeout;
/*     */   
/*     */ 
/*     */   private Pool pool;
/*     */   
/*     */ 
/*     */   private Sentinel sentinel;
/*     */   
/*     */   private Cluster cluster;
/*     */   
/*     */ 
/*     */   public int getDatabase()
/*     */   {
/*  76 */     return this.database;
/*     */   }
/*     */   
/*     */   public void setDatabase(int database) {
/*  80 */     this.database = database;
/*     */   }
/*     */   
/*     */   public String getUrl() {
/*  84 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/*  88 */     this.url = url;
/*     */   }
/*     */   
/*     */   public String getHost() {
/*  92 */     return this.host;
/*     */   }
/*     */   
/*     */   public void setHost(String host) {
/*  96 */     this.host = host;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 100 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 104 */     this.password = password;
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 108 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/* 112 */     this.port = port;
/*     */   }
/*     */   
/*     */   public boolean isSsl() {
/* 116 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public void setSsl(boolean ssl) {
/* 120 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */   public void setTimeout(int timeout) {
/* 124 */     this.timeout = timeout;
/*     */   }
/*     */   
/*     */   public int getTimeout() {
/* 128 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public Sentinel getSentinel() {
/* 132 */     return this.sentinel;
/*     */   }
/*     */   
/*     */   public void setSentinel(Sentinel sentinel) {
/* 136 */     this.sentinel = sentinel;
/*     */   }
/*     */   
/*     */   public Pool getPool() {
/* 140 */     return this.pool;
/*     */   }
/*     */   
/*     */   public void setPool(Pool pool) {
/* 144 */     this.pool = pool;
/*     */   }
/*     */   
/*     */   public Cluster getCluster() {
/* 148 */     return this.cluster;
/*     */   }
/*     */   
/*     */   public void setCluster(Cluster cluster) {
/* 152 */     this.cluster = cluster;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Pool
/*     */   {
/* 164 */     private int maxIdle = 8;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 170 */     private int minIdle = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */     private int maxActive = 8;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */     private int maxWait = -1;
/*     */     
/*     */     public int getMaxIdle() {
/* 186 */       return this.maxIdle;
/*     */     }
/*     */     
/*     */     public void setMaxIdle(int maxIdle) {
/* 190 */       this.maxIdle = maxIdle;
/*     */     }
/*     */     
/*     */     public int getMinIdle() {
/* 194 */       return this.minIdle;
/*     */     }
/*     */     
/*     */     public void setMinIdle(int minIdle) {
/* 198 */       this.minIdle = minIdle;
/*     */     }
/*     */     
/*     */     public int getMaxActive() {
/* 202 */       return this.maxActive;
/*     */     }
/*     */     
/*     */     public void setMaxActive(int maxActive) {
/* 206 */       this.maxActive = maxActive;
/*     */     }
/*     */     
/*     */     public int getMaxWait() {
/* 210 */       return this.maxWait;
/*     */     }
/*     */     
/*     */     public void setMaxWait(int maxWait) {
/* 214 */       this.maxWait = maxWait;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Cluster
/*     */   {
/*     */     private List<String> nodes;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Integer maxRedirects;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<String> getNodes()
/*     */     {
/* 237 */       return this.nodes;
/*     */     }
/*     */     
/*     */     public void setNodes(List<String> nodes) {
/* 241 */       this.nodes = nodes;
/*     */     }
/*     */     
/*     */     public Integer getMaxRedirects() {
/* 245 */       return this.maxRedirects;
/*     */     }
/*     */     
/*     */     public void setMaxRedirects(Integer maxRedirects) {
/* 249 */       this.maxRedirects = maxRedirects;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Sentinel
/*     */   {
/*     */     private String master;
/*     */     
/*     */ 
/*     */ 
/*     */     private String nodes;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMaster()
/*     */     {
/* 270 */       return this.master;
/*     */     }
/*     */     
/*     */     public void setMaster(String master) {
/* 274 */       this.master = master;
/*     */     }
/*     */     
/*     */     public String getNodes() {
/* 278 */       return this.nodes;
/*     */     }
/*     */     
/*     */     public void setNodes(String nodes) {
/* 282 */       this.nodes = nodes;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\redis\RedisProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */