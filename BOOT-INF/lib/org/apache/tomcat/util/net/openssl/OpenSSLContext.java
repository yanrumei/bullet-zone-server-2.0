/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.jni.CertificateVerifier;
/*     */ import org.apache.tomcat.jni.Pool;
/*     */ import org.apache.tomcat.jni.SSLConf;
/*     */ import org.apache.tomcat.util.codec.binary.Base64;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*     */ import org.apache.tomcat.util.net.SSLHostConfigCertificate.Type;
/*     */ import org.apache.tomcat.util.net.jsse.JSSEKeyManager;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.OpenSSLCipherConfigurationParser;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class OpenSSLContext
/*     */   implements org.apache.tomcat.util.net.SSLContext
/*     */ {
/*  60 */   private static final Base64 BASE64_ENCODER = new Base64(64, new byte[] { 10 });
/*     */   
/*  62 */   private static final Log log = LogFactory.getLog(OpenSSLContext.class);
/*     */   
/*     */ 
/*  65 */   private static final StringManager netSm = StringManager.getManager(AbstractEndpoint.class);
/*  66 */   private static final StringManager sm = StringManager.getManager(OpenSSLContext.class);
/*     */   
/*     */   private static final String defaultProtocol = "TLS";
/*     */   
/*     */   private final SSLHostConfig sslHostConfig;
/*     */   
/*     */   private final SSLHostConfigCertificate certificate;
/*     */   
/*     */   private OpenSSLSessionContext sessionContext;
/*     */   private X509KeyManager x509KeyManager;
/*     */   private X509TrustManager x509TrustManager;
/*     */   private final List<String> negotiableProtocols;
/*  78 */   private List<String> jsseCipherNames = new ArrayList();
/*     */   private String enabledProtocol;
/*     */   
/*  81 */   public List<String> getJsseCipherNames() { return this.jsseCipherNames; }
/*     */   
/*     */ 
/*     */   private final long aprPool;
/*     */   public String getEnabledProtocol()
/*     */   {
/*  87 */     return this.enabledProtocol;
/*     */   }
/*     */   
/*     */   public void setEnabledProtocol(String protocol) {
/*  91 */     this.enabledProtocol = (protocol == null ? "TLS" : protocol);
/*     */   }
/*     */   
/*     */ 
/*  95 */   private final AtomicInteger aprPoolDestroyed = new AtomicInteger(0);
/*     */   
/*     */ 
/*     */   protected final long cctx;
/*     */   
/*     */   protected final long ctx;
/*     */   
/*     */   static final CertificateFactory X509_CERT_FACTORY;
/*     */   
/*     */   private static final String BEGIN_KEY = "-----BEGIN RSA PRIVATE KEY-----\n";
/*     */   
/* 106 */   private static final Object END_KEY = "\n-----END RSA PRIVATE KEY-----";
/* 107 */   private boolean initialized = false;
/*     */   
/*     */   static {
/*     */     try {
/* 111 */       X509_CERT_FACTORY = CertificateFactory.getInstance("X.509");
/*     */     } catch (CertificateException e) {
/* 113 */       throw new IllegalStateException(sm.getString("openssl.X509FactoryError"), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public OpenSSLContext(SSLHostConfigCertificate certificate, List<String> negotiableProtocols) throws SSLException
/*     */   {
/* 119 */     this.sslHostConfig = certificate.getSSLHostConfig();
/* 120 */     this.certificate = certificate;
/* 121 */     this.aprPool = Pool.create(0L);
/* 122 */     boolean success = false;
/*     */     try
/*     */     {
/* 125 */       OpenSSLConf openSslConf = this.sslHostConfig.getOpenSslConf();
/* 126 */       if (openSslConf != null) {
/*     */         try {
/* 128 */           if (log.isDebugEnabled())
/* 129 */             log.debug(sm.getString("openssl.makeConf"));
/* 130 */           this.cctx = SSLConf.make(this.aprPool, 58);
/*     */ 
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */ 
/* 136 */           throw new SSLException(sm.getString("openssl.errMakeConf"), e);
/*     */         }
/*     */       } else {
/* 139 */         this.cctx = 0L;
/*     */       }
/* 141 */       this.sslHostConfig.setOpenSslConfContext(Long.valueOf(this.cctx));
/*     */       
/*     */ 
/* 144 */       int value = 0;
/* 145 */       for (String protocol : this.sslHostConfig.getEnabledProtocols()) {
/* 146 */         if (!"SSLv2Hello".equalsIgnoreCase(protocol))
/*     */         {
/* 148 */           if ("SSLv2".equalsIgnoreCase(protocol)) {
/* 149 */             value |= 0x1;
/* 150 */           } else if ("SSLv3".equalsIgnoreCase(protocol)) {
/* 151 */             value |= 0x2;
/* 152 */           } else if ("TLSv1".equalsIgnoreCase(protocol)) {
/* 153 */             value |= 0x4;
/* 154 */           } else if ("TLSv1.1".equalsIgnoreCase(protocol)) {
/* 155 */             value |= 0x8;
/* 156 */           } else if ("TLSv1.2".equalsIgnoreCase(protocol)) {
/* 157 */             value |= 0x10;
/* 158 */           } else if ("all".equalsIgnoreCase(protocol)) {
/* 159 */             value |= 0x1C;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 164 */             throw new Exception(netSm.getString("endpoint.apr.invalidSslProtocol", new Object[] { protocol }));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 171 */         this.ctx = org.apache.tomcat.jni.SSLContext.make(this.aprPool, value, 1);
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/* 177 */         throw new Exception(netSm.getString("endpoint.apr.failSslContextMake"), e);
/*     */       }
/*     */       
/* 180 */       this.negotiableProtocols = negotiableProtocols;
/*     */       
/* 182 */       success = true;
/*     */     } catch (Exception e) {
/* 184 */       throw new SSLException(sm.getString("openssl.errorSSLCtxInit"), e);
/*     */     } finally {
/* 186 */       if (!success) {
/* 187 */         destroy();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void destroy()
/*     */   {
/* 195 */     if (this.aprPoolDestroyed.compareAndSet(0, 1)) {
/* 196 */       if (this.ctx != 0L) {
/* 197 */         org.apache.tomcat.jni.SSLContext.free(this.ctx);
/*     */       }
/* 199 */       if (this.cctx != 0L) {
/* 200 */         SSLConf.free(this.cctx);
/*     */       }
/* 202 */       if (this.aprPool != 0L) {
/* 203 */         Pool.destroy(this.aprPool);
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public synchronized void init(KeyManager[] kms, TrustManager[] tms, SecureRandom sr)
/*     */   {
/* 219 */     if (this.initialized) {
/* 220 */       log.warn(sm.getString("openssl.doubleInit"));
/* 221 */       return;
/*     */     }
/*     */     try {
/* 224 */       if (this.sslHostConfig.getInsecureRenegotiation()) {
/* 225 */         org.apache.tomcat.jni.SSLContext.setOptions(this.ctx, 262144);
/*     */       } else {
/* 227 */         org.apache.tomcat.jni.SSLContext.clearOptions(this.ctx, 262144);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 232 */       String honorCipherOrderStr = this.sslHostConfig.getHonorCipherOrder();
/* 233 */       if (honorCipherOrderStr != null) {
/* 234 */         if (Boolean.parseBoolean(honorCipherOrderStr)) {
/* 235 */           org.apache.tomcat.jni.SSLContext.setOptions(this.ctx, 4194304);
/*     */         } else {
/* 237 */           org.apache.tomcat.jni.SSLContext.clearOptions(this.ctx, 4194304);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 242 */       if (this.sslHostConfig.getDisableCompression()) {
/* 243 */         org.apache.tomcat.jni.SSLContext.setOptions(this.ctx, 131072);
/*     */       } else {
/* 245 */         org.apache.tomcat.jni.SSLContext.clearOptions(this.ctx, 131072);
/*     */       }
/*     */       
/*     */ 
/* 249 */       if (this.sslHostConfig.getDisableSessionTickets()) {
/* 250 */         org.apache.tomcat.jni.SSLContext.setOptions(this.ctx, 16384);
/*     */       } else {
/* 252 */         org.apache.tomcat.jni.SSLContext.clearOptions(this.ctx, 16384);
/*     */       }
/*     */       
/*     */ 
/* 256 */       if (this.sslHostConfig.getSessionCacheSize() > 0) {
/* 257 */         org.apache.tomcat.jni.SSLContext.setSessionCacheSize(this.ctx, this.sslHostConfig.getSessionCacheSize());
/*     */       }
/*     */       else {
/* 260 */         long sessionCacheSize = org.apache.tomcat.jni.SSLContext.setSessionCacheSize(this.ctx, 20480L);
/*     */         
/* 262 */         org.apache.tomcat.jni.SSLContext.setSessionCacheSize(this.ctx, sessionCacheSize);
/*     */       }
/*     */       
/*     */ 
/* 266 */       if (this.sslHostConfig.getSessionTimeout() > 0) {
/* 267 */         org.apache.tomcat.jni.SSLContext.setSessionCacheTimeout(this.ctx, this.sslHostConfig.getSessionTimeout());
/*     */       }
/*     */       else {
/* 270 */         long sessionTimeout = org.apache.tomcat.jni.SSLContext.setSessionCacheTimeout(this.ctx, 300L);
/*     */         
/* 272 */         org.apache.tomcat.jni.SSLContext.setSessionCacheTimeout(this.ctx, sessionTimeout);
/*     */       }
/*     */       
/*     */ 
/* 276 */       String opensslCipherConfig = this.sslHostConfig.getCiphers();
/* 277 */       this.jsseCipherNames = OpenSSLCipherConfigurationParser.parseExpression(opensslCipherConfig);
/* 278 */       org.apache.tomcat.jni.SSLContext.setCipherSuite(this.ctx, opensslCipherConfig);
/*     */       X509Certificate[] chain;
/* 280 */       PrivateKey key; StringBuilder sb; if (this.certificate.getCertificateFile() != null)
/*     */       {
/* 282 */         org.apache.tomcat.jni.SSLContext.setCertificate(this.ctx, 
/* 283 */           SSLHostConfig.adjustRelativePath(this.certificate.getCertificateFile()), 
/* 284 */           SSLHostConfig.adjustRelativePath(this.certificate.getCertificateKeyFile()), this.certificate
/* 285 */           .getCertificateKeyPassword(), 0);
/*     */         
/* 287 */         org.apache.tomcat.jni.SSLContext.setCertificateChainFile(this.ctx, 
/* 288 */           SSLHostConfig.adjustRelativePath(this.certificate.getCertificateChainFile()), false);
/*     */         
/* 290 */         org.apache.tomcat.jni.SSLContext.setCARevocation(this.ctx, 
/* 291 */           SSLHostConfig.adjustRelativePath(this.sslHostConfig
/* 292 */           .getCertificateRevocationListFile()), 
/* 293 */           SSLHostConfig.adjustRelativePath(this.sslHostConfig
/* 294 */           .getCertificateRevocationListPath()));
/*     */       } else {
/* 296 */         this.x509KeyManager = chooseKeyManager(kms);
/* 297 */         String alias = this.certificate.getCertificateKeyAlias();
/* 298 */         if (alias == null) {
/* 299 */           alias = "tomcat";
/*     */         }
/* 301 */         chain = this.x509KeyManager.getCertificateChain(alias);
/* 302 */         if (chain == null) {
/* 303 */           alias = findAlias(this.x509KeyManager, this.certificate);
/* 304 */           chain = this.x509KeyManager.getCertificateChain(alias);
/*     */         }
/* 306 */         key = this.x509KeyManager.getPrivateKey(alias);
/* 307 */         sb = new StringBuilder("-----BEGIN RSA PRIVATE KEY-----\n");
/* 308 */         String encoded = BASE64_ENCODER.encodeToString(key.getEncoded());
/* 309 */         if (encoded.endsWith("\n")) {
/* 310 */           encoded = encoded.substring(0, encoded.length() - 1);
/*     */         }
/* 312 */         sb.append(encoded);
/* 313 */         sb.append(END_KEY);
/* 314 */         org.apache.tomcat.jni.SSLContext.setCertificateRaw(this.ctx, chain[0].getEncoded(), sb.toString().getBytes(StandardCharsets.US_ASCII), 0);
/* 315 */         for (int i = 1; i < chain.length; i++) {
/* 316 */           org.apache.tomcat.jni.SSLContext.addChainCertificateRaw(this.ctx, chain[i].getEncoded());
/*     */         }
/*     */       }
/*     */       
/* 320 */       int value = 0;
/* 321 */       switch (this.sslHostConfig.getCertificateVerification()) {
/*     */       case NONE: 
/* 323 */         value = 0;
/* 324 */         break;
/*     */       case OPTIONAL: 
/* 326 */         value = 1;
/* 327 */         break;
/*     */       case OPTIONAL_NO_CA: 
/* 329 */         value = 3;
/* 330 */         break;
/*     */       case REQUIRED: 
/* 332 */         value = 2;
/*     */       }
/*     */       
/* 335 */       org.apache.tomcat.jni.SSLContext.setVerify(this.ctx, value, this.sslHostConfig.getCertificateVerificationDepth());
/*     */       
/* 337 */       if (tms != null)
/*     */       {
/* 339 */         this.x509TrustManager = chooseTrustManager(tms);
/* 340 */         org.apache.tomcat.jni.SSLContext.setCertVerifyCallback(this.ctx, new CertificateVerifier()
/*     */         {
/*     */           public boolean verify(long ssl, byte[][] chain, String auth) {
/* 343 */             X509Certificate[] peerCerts = OpenSSLContext.certificates(chain);
/*     */             try {
/* 345 */               OpenSSLContext.this.x509TrustManager.checkClientTrusted(peerCerts, auth);
/* 346 */               return true;
/*     */             } catch (Exception e) {
/* 348 */               OpenSSLContext.log.debug(OpenSSLContext.sm.getString("openssl.certificateVerificationFailed"), e);
/*     */             }
/* 350 */             return false;
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 356 */         });
/* 357 */         chain = this.x509TrustManager.getAcceptedIssuers();key = chain.length; for (sb = 0; sb < key; sb++) { X509Certificate caCert = chain[sb];
/* 358 */           org.apache.tomcat.jni.SSLContext.addClientCACertificateRaw(this.ctx, caCert.getEncoded());
/* 359 */           if (log.isDebugEnabled()) {
/* 360 */             log.debug(sm.getString("openssl.addedClientCaCert", new Object[] { caCert.toString() }));
/*     */           }
/*     */         }
/*     */       } else {
/* 364 */         org.apache.tomcat.jni.SSLContext.setCACertificate(this.ctx, 
/* 365 */           SSLHostConfig.adjustRelativePath(this.sslHostConfig.getCaCertificateFile()), 
/* 366 */           SSLHostConfig.adjustRelativePath(this.sslHostConfig.getCaCertificatePath()));
/*     */       }
/*     */       
/* 369 */       if ((this.negotiableProtocols != null) && (this.negotiableProtocols.size() > 0)) {
/* 370 */         ArrayList<String> protocols = new ArrayList();
/* 371 */         protocols.addAll(this.negotiableProtocols);
/* 372 */         protocols.add("http/1.1");
/* 373 */         String[] protocolsArray = (String[])protocols.toArray(new String[0]);
/* 374 */         org.apache.tomcat.jni.SSLContext.setAlpnProtos(this.ctx, protocolsArray, 0);
/* 375 */         org.apache.tomcat.jni.SSLContext.setNpnProtos(this.ctx, protocolsArray, 0);
/*     */       }
/*     */       
/*     */ 
/* 379 */       OpenSSLConf openSslConf = this.sslHostConfig.getOpenSslConf();
/* 380 */       if ((openSslConf != null) && (this.cctx != 0L))
/*     */       {
/* 382 */         if (log.isDebugEnabled())
/* 383 */           log.debug(sm.getString("openssl.checkConf"));
/*     */         try {
/* 385 */           if (!openSslConf.check(this.cctx)) {
/* 386 */             log.error(sm.getString("openssl.errCheckConf"));
/* 387 */             throw new Exception(sm.getString("openssl.errCheckConf"));
/*     */           }
/*     */         } catch (Exception e) {
/* 390 */           throw new Exception(sm.getString("openssl.errCheckConf"), e);
/*     */         }
/* 392 */         if (log.isDebugEnabled())
/* 393 */           log.debug(sm.getString("openssl.applyConf"));
/*     */         try {
/* 395 */           if (!openSslConf.apply(this.cctx, this.ctx)) {
/* 396 */             log.error(sm.getString("openssl.errApplyConf"));
/* 397 */             throw new SSLException(sm.getString("openssl.errApplyConf"));
/*     */           }
/*     */         } catch (Exception e) {
/* 400 */           throw new SSLException(sm.getString("openssl.errApplyConf"), e);
/*     */         }
/*     */         
/* 403 */         int opts = org.apache.tomcat.jni.SSLContext.getOptions(this.ctx);
/* 404 */         List<String> enabled = new ArrayList();
/*     */         
/*     */ 
/* 407 */         enabled.add("SSLv2Hello");
/* 408 */         if ((opts & 0x4000000) == 0) {
/* 409 */           enabled.add("TLSv1");
/*     */         }
/* 411 */         if ((opts & 0x10000000) == 0) {
/* 412 */           enabled.add("TLSv1.1");
/*     */         }
/* 414 */         if ((opts & 0x8000000) == 0) {
/* 415 */           enabled.add("TLSv1.2");
/*     */         }
/* 417 */         if ((opts & 0x1000000) == 0) {
/* 418 */           enabled.add("SSLv2");
/*     */         }
/* 420 */         if ((opts & 0x2000000) == 0) {
/* 421 */           enabled.add("SSLv3");
/*     */         }
/* 423 */         this.sslHostConfig.setEnabledProtocols(
/* 424 */           (String[])enabled.toArray(new String[enabled.size()]));
/*     */         
/* 426 */         this.sslHostConfig.setEnabledCiphers(org.apache.tomcat.jni.SSLContext.getCiphers(this.ctx));
/*     */       }
/*     */       
/* 429 */       this.sessionContext = new OpenSSLSessionContext(this);
/*     */       
/*     */ 
/*     */ 
/* 433 */       this.sessionContext.setSessionIdContext(org.apache.tomcat.jni.SSLContext.DEFAULT_SESSION_ID_CONTEXT);
/* 434 */       this.sslHostConfig.setOpenSslContext(Long.valueOf(this.ctx));
/* 435 */       this.initialized = true;
/*     */     } catch (Exception e) {
/* 437 */       log.warn(sm.getString("openssl.errorSSLCtxInit"), e);
/* 438 */       destroy();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String findAlias(X509KeyManager keyManager, SSLHostConfigCertificate certificate)
/*     */   {
/* 448 */     SSLHostConfigCertificate.Type type = certificate.getType();
/* 449 */     String result = null;
/*     */     
/* 451 */     List<SSLHostConfigCertificate.Type> candidateTypes = new ArrayList();
/* 452 */     if (SSLHostConfigCertificate.Type.UNDEFINED.equals(type))
/*     */     {
/* 454 */       candidateTypes.addAll(Arrays.asList(SSLHostConfigCertificate.Type.values()));
/* 455 */       candidateTypes.remove(SSLHostConfigCertificate.Type.UNDEFINED);
/*     */     }
/*     */     else {
/* 458 */       candidateTypes.add(type);
/*     */     }
/*     */     
/* 461 */     Iterator<SSLHostConfigCertificate.Type> iter = candidateTypes.iterator();
/* 462 */     while ((result == null) && (iter.hasNext())) {
/* 463 */       result = keyManager.chooseServerAlias(((SSLHostConfigCertificate.Type)iter.next()).toString(), null, null);
/*     */     }
/*     */     
/* 466 */     return result;
/*     */   }
/*     */   
/*     */   private static X509KeyManager chooseKeyManager(KeyManager[] managers) throws Exception {
/* 470 */     for (KeyManager manager : managers) {
/* 471 */       if ((manager instanceof JSSEKeyManager)) {
/* 472 */         return (JSSEKeyManager)manager;
/*     */       }
/*     */     }
/* 475 */     for (KeyManager manager : managers) {
/* 476 */       if ((manager instanceof X509KeyManager)) {
/* 477 */         return (X509KeyManager)manager;
/*     */       }
/*     */     }
/* 480 */     throw new IllegalStateException(sm.getString("openssl.keyManagerMissing"));
/*     */   }
/*     */   
/*     */   private static X509TrustManager chooseTrustManager(TrustManager[] managers) {
/* 484 */     for (TrustManager m : managers) {
/* 485 */       if ((m instanceof X509TrustManager)) {
/* 486 */         return (X509TrustManager)m;
/*     */       }
/*     */     }
/* 489 */     throw new IllegalStateException(sm.getString("openssl.trustManagerMissing"));
/*     */   }
/*     */   
/*     */   private static X509Certificate[] certificates(byte[][] chain) {
/* 493 */     X509Certificate[] peerCerts = new X509Certificate[chain.length];
/* 494 */     for (int i = 0; i < peerCerts.length; i++) {
/* 495 */       peerCerts[i] = new OpenSSLX509Certificate(chain[i]);
/*     */     }
/* 497 */     return peerCerts;
/*     */   }
/*     */   
/*     */   long getSSLContextID()
/*     */   {
/* 502 */     return this.ctx;
/*     */   }
/*     */   
/*     */ 
/*     */   public SSLSessionContext getServerSessionContext()
/*     */   {
/* 508 */     return this.sessionContext;
/*     */   }
/*     */   
/*     */   public SSLEngine createSSLEngine()
/*     */   {
/* 513 */     return new OpenSSLEngine(this.ctx, "TLS", false, this.sessionContext, (this.negotiableProtocols != null) && 
/* 514 */       (this.negotiableProtocols.size() > 0), this.initialized);
/*     */   }
/*     */   
/*     */   public SSLServerSocketFactory getServerSocketFactory()
/*     */   {
/* 519 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public SSLParameters getSupportedSSLParameters()
/*     */   {
/* 524 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public X509Certificate[] getCertificateChain(String alias)
/*     */   {
/* 529 */     X509Certificate[] chain = null;
/* 530 */     if (this.x509KeyManager != null) {
/* 531 */       if (alias == null) {
/* 532 */         alias = "tomcat";
/*     */       }
/* 534 */       chain = this.x509KeyManager.getCertificateChain(alias);
/* 535 */       if (chain == null) {
/* 536 */         alias = findAlias(this.x509KeyManager, this.certificate);
/* 537 */         chain = this.x509KeyManager.getCertificateChain(alias);
/*     */       }
/*     */     }
/*     */     
/* 541 */     return chain;
/*     */   }
/*     */   
/*     */   public X509Certificate[] getAcceptedIssuers()
/*     */   {
/* 546 */     X509Certificate[] acceptedCerts = null;
/* 547 */     if (this.x509TrustManager != null) {
/* 548 */       acceptedCerts = this.x509TrustManager.getAcceptedIssuers();
/*     */     }
/* 550 */     return acceptedCerts;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void finalize()
/*     */     throws java.lang.Throwable
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 55	org/apache/tomcat/util/net/openssl/OpenSSLContext:destroy	()V
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 198	java/lang/Object:finalize	()V
/*     */     //   8: goto +10 -> 18
/*     */     //   11: astore_1
/*     */     //   12: aload_0
/*     */     //   13: invokespecial 198	java/lang/Object:finalize	()V
/*     */     //   16: aload_1
/*     */     //   17: athrow
/*     */     //   18: return
/*     */     // Line number table:
/*     */     //   Java source line #567	-> byte code offset #0
/*     */     //   Java source line #569	-> byte code offset #4
/*     */     //   Java source line #570	-> byte code offset #8
/*     */     //   Java source line #569	-> byte code offset #11
/*     */     //   Java source line #571	-> byte code offset #18
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	OpenSSLContext
/*     */     //   11	6	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	11	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */