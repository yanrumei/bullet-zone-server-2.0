/*     */ package org.springframework.boot.autoconfigure.mail;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @ConfigurationProperties(prefix="spring.mail")
/*     */ public class MailProperties
/*     */ {
/*  36 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String host;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer port;
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
/*  61 */   private String protocol = "smtp";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private Charset defaultEncoding = DEFAULT_CHARSET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private Map<String, String> properties = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private String jndiName;
/*     */   
/*     */ 
/*     */   private boolean testConnection;
/*     */   
/*     */ 
/*     */ 
/*     */   public String getHost()
/*     */   {
/*  84 */     return this.host;
/*     */   }
/*     */   
/*     */   public void setHost(String host) {
/*  88 */     this.host = host;
/*     */   }
/*     */   
/*     */   public Integer getPort() {
/*  92 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(Integer port) {
/*  96 */     this.port = port;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 100 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 104 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 108 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 112 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getProtocol() {
/* 116 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public void setProtocol(String protocol) {
/* 120 */     this.protocol = protocol;
/*     */   }
/*     */   
/*     */   public Charset getDefaultEncoding() {
/* 124 */     return this.defaultEncoding;
/*     */   }
/*     */   
/*     */   public void setDefaultEncoding(Charset defaultEncoding) {
/* 128 */     this.defaultEncoding = defaultEncoding;
/*     */   }
/*     */   
/*     */   public Map<String, String> getProperties() {
/* 132 */     return this.properties;
/*     */   }
/*     */   
/*     */   public void setJndiName(String jndiName) {
/* 136 */     this.jndiName = jndiName;
/*     */   }
/*     */   
/*     */   public String getJndiName() {
/* 140 */     return this.jndiName;
/*     */   }
/*     */   
/*     */   public boolean isTestConnection() {
/* 144 */     return this.testConnection;
/*     */   }
/*     */   
/*     */   public void setTestConnection(boolean testConnection) {
/* 148 */     this.testConnection = testConnection;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mail\MailProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */