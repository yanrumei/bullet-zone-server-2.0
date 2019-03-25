/*     */ package org.springframework.boot.autoconfigure.sendgrid;
/*     */ 
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
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.sendgrid")
/*     */ public class SendGridProperties
/*     */ {
/*     */   private String username;
/*     */   private String password;
/*     */   private String apiKey;
/*     */   private Proxy proxy;
/*     */   
/*     */   public String getUsername()
/*     */   {
/*  51 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  55 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  59 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  63 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getApiKey() {
/*  67 */     return this.apiKey;
/*     */   }
/*     */   
/*     */   public void setApiKey(String apiKey) {
/*  71 */     this.apiKey = apiKey;
/*     */   }
/*     */   
/*     */   public Proxy getProxy() {
/*  75 */     return this.proxy;
/*     */   }
/*     */   
/*     */   public void setProxy(Proxy proxy) {
/*  79 */     this.proxy = proxy;
/*     */   }
/*     */   
/*     */   public boolean isProxyConfigured() {
/*  83 */     return (this.proxy != null) && (this.proxy.getHost() != null) && 
/*  84 */       (this.proxy.getPort() != null);
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
/* 100 */       return this.host;
/*     */     }
/*     */     
/*     */     public void setHost(String host) {
/* 104 */       this.host = host;
/*     */     }
/*     */     
/*     */     public Integer getPort() {
/* 108 */       return this.port;
/*     */     }
/*     */     
/*     */     public void setPort(Integer port) {
/* 112 */       this.port = port;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\sendgrid\SendGridProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */