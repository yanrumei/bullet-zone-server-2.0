/*     */ package org.springframework.boot.context.embedded;
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
/*     */ public class Ssl
/*     */ {
/*  31 */   private boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ClientAuth clientAuth;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] ciphers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] enabledProtocols;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyAlias;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyPassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyStore;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyStorePassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyStoreType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String keyStoreProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String trustStore;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String trustStorePassword;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String trustStoreType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String trustStoreProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private String protocol = "TLS";
/*     */   
/*     */   public boolean isEnabled() {
/* 105 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 109 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public ClientAuth getClientAuth() {
/* 113 */     return this.clientAuth;
/*     */   }
/*     */   
/*     */   public void setClientAuth(ClientAuth clientAuth) {
/* 117 */     this.clientAuth = clientAuth;
/*     */   }
/*     */   
/*     */   public String[] getCiphers() {
/* 121 */     return this.ciphers;
/*     */   }
/*     */   
/*     */   public void setCiphers(String[] ciphers) {
/* 125 */     this.ciphers = ciphers;
/*     */   }
/*     */   
/*     */   public String getKeyAlias() {
/* 129 */     return this.keyAlias;
/*     */   }
/*     */   
/*     */   public void setKeyAlias(String keyAlias) {
/* 133 */     this.keyAlias = keyAlias;
/*     */   }
/*     */   
/*     */   public String getKeyPassword() {
/* 137 */     return this.keyPassword;
/*     */   }
/*     */   
/*     */   public void setKeyPassword(String keyPassword) {
/* 141 */     this.keyPassword = keyPassword;
/*     */   }
/*     */   
/*     */   public String getKeyStore() {
/* 145 */     return this.keyStore;
/*     */   }
/*     */   
/*     */   public void setKeyStore(String keyStore) {
/* 149 */     this.keyStore = keyStore;
/*     */   }
/*     */   
/*     */   public String getKeyStorePassword() {
/* 153 */     return this.keyStorePassword;
/*     */   }
/*     */   
/*     */   public void setKeyStorePassword(String keyStorePassword) {
/* 157 */     this.keyStorePassword = keyStorePassword;
/*     */   }
/*     */   
/*     */   public String getKeyStoreType() {
/* 161 */     return this.keyStoreType;
/*     */   }
/*     */   
/*     */   public void setKeyStoreType(String keyStoreType) {
/* 165 */     this.keyStoreType = keyStoreType;
/*     */   }
/*     */   
/*     */   public String getKeyStoreProvider() {
/* 169 */     return this.keyStoreProvider;
/*     */   }
/*     */   
/*     */   public void setKeyStoreProvider(String keyStoreProvider) {
/* 173 */     this.keyStoreProvider = keyStoreProvider;
/*     */   }
/*     */   
/*     */   public String[] getEnabledProtocols() {
/* 177 */     return this.enabledProtocols;
/*     */   }
/*     */   
/*     */   public void setEnabledProtocols(String[] enabledProtocols) {
/* 181 */     this.enabledProtocols = enabledProtocols;
/*     */   }
/*     */   
/*     */   public String getTrustStore() {
/* 185 */     return this.trustStore;
/*     */   }
/*     */   
/*     */   public void setTrustStore(String trustStore) {
/* 189 */     this.trustStore = trustStore;
/*     */   }
/*     */   
/*     */   public String getTrustStorePassword() {
/* 193 */     return this.trustStorePassword;
/*     */   }
/*     */   
/*     */   public void setTrustStorePassword(String trustStorePassword) {
/* 197 */     this.trustStorePassword = trustStorePassword;
/*     */   }
/*     */   
/*     */   public String getTrustStoreType() {
/* 201 */     return this.trustStoreType;
/*     */   }
/*     */   
/*     */   public void setTrustStoreType(String trustStoreType) {
/* 205 */     this.trustStoreType = trustStoreType;
/*     */   }
/*     */   
/*     */   public String getTrustStoreProvider() {
/* 209 */     return this.trustStoreProvider;
/*     */   }
/*     */   
/*     */   public void setTrustStoreProvider(String trustStoreProvider) {
/* 213 */     this.trustStoreProvider = trustStoreProvider;
/*     */   }
/*     */   
/*     */   public String getProtocol() {
/* 217 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public void setProtocol(String protocol) {
/* 221 */     this.protocol = protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ClientAuth
/*     */   {
/* 232 */     WANT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     NEED;
/*     */     
/*     */     private ClientAuth() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\Ssl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */