/*     */ package org.apache.tomcat.util.net.jsse;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ class JSSESSLContext
/*     */   implements org.apache.tomcat.util.net.SSLContext
/*     */ {
/*     */   private javax.net.ssl.SSLContext context;
/*     */   private KeyManager[] kms;
/*     */   private TrustManager[] tms;
/*     */   
/*     */   JSSESSLContext(String protocol)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  45 */     this.context = javax.net.ssl.SSLContext.getInstance(protocol);
/*     */   }
/*     */   
/*     */   public void init(KeyManager[] kms, TrustManager[] tms, SecureRandom sr)
/*     */     throws KeyManagementException
/*     */   {
/*  51 */     this.kms = kms;
/*  52 */     this.tms = tms;
/*  53 */     this.context.init(kms, tms, sr);
/*     */   }
/*     */   
/*     */ 
/*     */   public void destroy() {}
/*     */   
/*     */ 
/*     */   public SSLSessionContext getServerSessionContext()
/*     */   {
/*  62 */     return this.context.getServerSessionContext();
/*     */   }
/*     */   
/*     */   public SSLEngine createSSLEngine()
/*     */   {
/*  67 */     return this.context.createSSLEngine();
/*     */   }
/*     */   
/*     */   public SSLServerSocketFactory getServerSocketFactory()
/*     */   {
/*  72 */     return this.context.getServerSocketFactory();
/*     */   }
/*     */   
/*     */   public SSLParameters getSupportedSSLParameters()
/*     */   {
/*  77 */     return this.context.getSupportedSSLParameters();
/*     */   }
/*     */   
/*     */   public X509Certificate[] getCertificateChain(String alias)
/*     */   {
/*  82 */     X509Certificate[] result = null;
/*  83 */     if (this.kms != null) {
/*  84 */       for (int i = 0; (i < this.kms.length) && (result == null); i++) {
/*  85 */         if ((this.kms[i] instanceof X509KeyManager)) {
/*  86 */           result = ((X509KeyManager)this.kms[i]).getCertificateChain(alias);
/*     */         }
/*     */       }
/*     */     }
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   public X509Certificate[] getAcceptedIssuers()
/*     */   {
/*  95 */     Set<X509Certificate> certs = new HashSet();
/*  96 */     if (this.tms != null) {
/*  97 */       for (TrustManager tm : this.tms) {
/*  98 */         if ((tm instanceof X509TrustManager)) {
/*  99 */           X509Certificate[] accepted = ((X509TrustManager)tm).getAcceptedIssuers();
/* 100 */           if (accepted != null) {
/* 101 */             for (X509Certificate c : accepted) {
/* 102 */               certs.add(c);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return (X509Certificate[])certs.toArray(new X509Certificate[certs.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\JSSESSLContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */