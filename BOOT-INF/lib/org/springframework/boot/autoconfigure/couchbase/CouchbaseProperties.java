/*     */ package org.springframework.boot.autoconfigure.couchbase;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
/*     */ @ConfigurationProperties(prefix="spring.couchbase")
/*     */ public class CouchbaseProperties
/*     */ {
/*     */   private List<String> bootstrapHosts;
/*  40 */   private final Bucket bucket = new Bucket();
/*     */   
/*  42 */   private final Env env = new Env();
/*     */   
/*     */   public List<String> getBootstrapHosts() {
/*  45 */     return this.bootstrapHosts;
/*     */   }
/*     */   
/*     */   public void setBootstrapHosts(List<String> bootstrapHosts) {
/*  49 */     this.bootstrapHosts = bootstrapHosts;
/*     */   }
/*     */   
/*     */   public Bucket getBucket() {
/*  53 */     return this.bucket;
/*     */   }
/*     */   
/*     */   public Env getEnv() {
/*  57 */     return this.env;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Bucket
/*     */   {
/*  65 */     private String name = "default";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     private String password = "";
/*     */     
/*     */     public String getName() {
/*  73 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/*  77 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getPassword() {
/*  81 */       return this.password;
/*     */     }
/*     */     
/*     */     public void setPassword(String password) {
/*  85 */       this.password = password;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Env
/*     */   {
/*     */     @NestedConfigurationProperty
/*  92 */     private final CouchbaseProperties.Endpoints endpoints = new CouchbaseProperties.Endpoints();
/*     */     
/*     */     @NestedConfigurationProperty
/*  95 */     private final CouchbaseProperties.Ssl ssl = new CouchbaseProperties.Ssl();
/*     */     
/*     */     @NestedConfigurationProperty
/*  98 */     private final CouchbaseProperties.Timeouts timeouts = new CouchbaseProperties.Timeouts();
/*     */     
/*     */     public CouchbaseProperties.Endpoints getEndpoints()
/*     */     {
/* 102 */       return this.endpoints;
/*     */     }
/*     */     
/*     */     public CouchbaseProperties.Ssl getSsl() {
/* 106 */       return this.ssl;
/*     */     }
/*     */     
/*     */     public CouchbaseProperties.Timeouts getTimeouts() {
/* 110 */       return this.timeouts;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Endpoints
/*     */   {
/* 120 */     private int keyValue = 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 125 */     private int query = 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 130 */     private int view = 1;
/*     */     
/*     */     public int getKeyValue() {
/* 133 */       return this.keyValue;
/*     */     }
/*     */     
/*     */     public void setKeyValue(int keyValue) {
/* 137 */       this.keyValue = keyValue;
/*     */     }
/*     */     
/*     */     public int getQuery() {
/* 141 */       return this.query;
/*     */     }
/*     */     
/*     */     public void setQuery(int query) {
/* 145 */       this.query = query;
/*     */     }
/*     */     
/*     */     public int getView() {
/* 149 */       return this.view;
/*     */     }
/*     */     
/*     */     public void setView(int view) {
/* 153 */       this.view = view;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Ssl
/*     */   {
/*     */     private Boolean enabled;
/*     */     
/*     */ 
/*     */ 
/*     */     private String keyStore;
/*     */     
/*     */ 
/*     */ 
/*     */     private String keyStorePassword;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Boolean getEnabled()
/*     */     {
/* 177 */       return Boolean.valueOf(this.enabled != null ? this.enabled.booleanValue() : 
/* 178 */         StringUtils.hasText(this.keyStore));
/*     */     }
/*     */     
/*     */     public void setEnabled(Boolean enabled) {
/* 182 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public String getKeyStore() {
/* 186 */       return this.keyStore;
/*     */     }
/*     */     
/*     */     public void setKeyStore(String keyStore) {
/* 190 */       this.keyStore = keyStore;
/*     */     }
/*     */     
/*     */     public String getKeyStorePassword() {
/* 194 */       return this.keyStorePassword;
/*     */     }
/*     */     
/*     */     public void setKeyStorePassword(String keyStorePassword) {
/* 198 */       this.keyStorePassword = keyStorePassword;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Timeouts
/*     */   {
/* 208 */     private long connect = 5000L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 213 */     private long keyValue = 2500L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 218 */     private long query = 7500L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 223 */     private int socketConnect = 1000;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 228 */     private long view = 7500L;
/*     */     
/*     */     public long getConnect() {
/* 231 */       return this.connect;
/*     */     }
/*     */     
/*     */     public void setConnect(long connect) {
/* 235 */       this.connect = connect;
/*     */     }
/*     */     
/*     */     public long getKeyValue() {
/* 239 */       return this.keyValue;
/*     */     }
/*     */     
/*     */     public void setKeyValue(long keyValue) {
/* 243 */       this.keyValue = keyValue;
/*     */     }
/*     */     
/*     */     public long getQuery() {
/* 247 */       return this.query;
/*     */     }
/*     */     
/*     */     public void setQuery(long query) {
/* 251 */       this.query = query;
/*     */     }
/*     */     
/*     */     public int getSocketConnect() {
/* 255 */       return this.socketConnect;
/*     */     }
/*     */     
/*     */     public void setSocketConnect(int socketConnect) {
/* 259 */       this.socketConnect = socketConnect;
/*     */     }
/*     */     
/*     */     public long getView() {
/* 263 */       return this.view;
/*     */     }
/*     */     
/*     */     public void setView(long view) {
/* 267 */       this.view = view;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\couchbase\CouchbaseProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */