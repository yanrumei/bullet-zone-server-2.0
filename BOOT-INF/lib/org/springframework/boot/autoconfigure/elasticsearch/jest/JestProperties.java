/*     */ package org.springframework.boot.autoconfigure.elasticsearch.jest;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ @ConfigurationProperties(prefix="spring.elasticsearch.jest")
/*     */ public class JestProperties
/*     */ {
/*  37 */   private List<String> uris = new ArrayList(
/*  38 */     Collections.singletonList("http://localhost:9200"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String username;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private boolean multiThreaded = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private int connectionTimeout = 3000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private int readTimeout = 3000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private final Proxy proxy = new Proxy();
/*     */   
/*     */   public List<String> getUris() {
/*  71 */     return this.uris;
/*     */   }
/*     */   
/*     */   public void setUris(List<String> uris) {
/*  75 */     this.uris = uris;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  79 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  83 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  87 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  91 */     this.password = password;
/*     */   }
/*     */   
/*     */   public boolean isMultiThreaded() {
/*  95 */     return this.multiThreaded;
/*     */   }
/*     */   
/*     */   public void setMultiThreaded(boolean multiThreaded) {
/*  99 */     this.multiThreaded = multiThreaded;
/*     */   }
/*     */   
/*     */   public int getConnectionTimeout() {
/* 103 */     return this.connectionTimeout;
/*     */   }
/*     */   
/*     */   public void setConnectionTimeout(int connectionTimeout) {
/* 107 */     this.connectionTimeout = connectionTimeout;
/*     */   }
/*     */   
/*     */   public int getReadTimeout() {
/* 111 */     return this.readTimeout;
/*     */   }
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/* 115 */     this.readTimeout = readTimeout;
/*     */   }
/*     */   
/*     */   public Proxy getProxy() {
/* 119 */     return this.proxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Proxy
/*     */   {
/*     */     private String host;
/*     */     
/*     */ 
/*     */     private Integer port;
/*     */     
/*     */ 
/*     */ 
/*     */     public String getHost()
/*     */     {
/* 135 */       return this.host;
/*     */     }
/*     */     
/*     */     public void setHost(String host) {
/* 139 */       this.host = host;
/*     */     }
/*     */     
/*     */     public Integer getPort() {
/* 143 */       return this.port;
/*     */     }
/*     */     
/*     */     public void setPort(Integer port) {
/* 147 */       this.port = port;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\elasticsearch\jest\JestProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */