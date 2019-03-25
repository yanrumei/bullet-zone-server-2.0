/*     */ package org.apache.tomcat.util.net.jsse;
/*     */ 
/*     */ import java.net.Socket;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509KeyManager;
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
/*     */ public final class JSSEKeyManager
/*     */   extends X509ExtendedKeyManager
/*     */ {
/*     */   private X509KeyManager delegate;
/*     */   private String serverKeyAlias;
/*     */   
/*     */   public JSSEKeyManager(X509KeyManager mgr, String serverKeyAlias)
/*     */   {
/*  51 */     this.delegate = mgr;
/*  52 */     this.serverKeyAlias = serverKeyAlias;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
/*     */   {
/*  63 */     if (this.serverKeyAlias != null) {
/*  64 */       return this.serverKeyAlias;
/*     */     }
/*     */     
/*  67 */     return this.delegate.chooseServerAlias(keyType, issuers, socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
/*     */   {
/*  79 */     if (this.serverKeyAlias != null) {
/*  80 */       return this.serverKeyAlias;
/*     */     }
/*     */     
/*  83 */     return super.chooseEngineServerAlias(keyType, issuers, engine);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
/*     */   {
/*  90 */     return this.delegate.chooseClientAlias(keyType, issuers, socket);
/*     */   }
/*     */   
/*     */ 
/*     */   public X509Certificate[] getCertificateChain(String alias)
/*     */   {
/*  96 */     return this.delegate.getCertificateChain(alias);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getClientAliases(String keyType, Principal[] issuers)
/*     */   {
/* 102 */     return this.delegate.getClientAliases(keyType, issuers);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getServerAliases(String keyType, Principal[] issuers)
/*     */   {
/* 108 */     return this.delegate.getServerAliases(keyType, issuers);
/*     */   }
/*     */   
/*     */ 
/*     */   public PrivateKey getPrivateKey(String alias)
/*     */   {
/* 114 */     return this.delegate.getPrivateKey(alias);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
/*     */   {
/* 121 */     return this.delegate.chooseClientAlias(keyType, issuers, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\JSSEKeyManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */