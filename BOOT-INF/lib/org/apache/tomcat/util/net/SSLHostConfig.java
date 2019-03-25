/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.security.KeyStore;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.management.ObjectName;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
/*     */ import org.apache.tomcat.util.net.openssl.OpenSSLConf;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
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
/*     */ public class SSLHostConfig
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  50 */   private static final Log log = LogFactory.getLog(SSLHostConfig.class);
/*  51 */   private static final StringManager sm = StringManager.getManager(SSLHostConfig.class);
/*     */   
/*     */   private static final String DEFAULT_CIPHERS = "HIGH:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!MD5:!kRSA";
/*     */   
/*     */   protected static final String DEFAULT_SSL_HOST_NAME = "_default_";
/*  56 */   protected static final Set<String> SSL_PROTO_ALL_SET = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  62 */     SSL_PROTO_ALL_SET.add("SSLv2Hello");
/*  63 */     SSL_PROTO_ALL_SET.add("TLSv1");
/*  64 */     SSL_PROTO_ALL_SET.add("TLSv1.1");
/*  65 */     SSL_PROTO_ALL_SET.add("TLSv1.2");
/*     */   }
/*     */   
/*  68 */   private Type configType = null;
/*  69 */   private Type currentConfigType = null;
/*  70 */   private Map<Type, Set<String>> configuredProperties = new HashMap();
/*     */   
/*  72 */   private String hostName = "_default_";
/*     */   
/*  74 */   private transient Long openSslConfContext = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */ 
/*  78 */   private transient Long openSslContext = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */   private String[] enabledCiphers;
/*     */   
/*     */   private String[] enabledProtocols;
/*     */   
/*     */   private ObjectName oname;
/*     */   
/*  87 */   private SSLHostConfigCertificate defaultCertificate = null;
/*  88 */   private Set<SSLHostConfigCertificate> certificates = new HashSet(4);
/*     */   
/*     */   private String certificateRevocationListFile;
/*  91 */   private CertificateVerification certificateVerification = CertificateVerification.NONE;
/*  92 */   private int certificateVerificationDepth = 10;
/*     */   
/*  94 */   private boolean certificateVerificationDepthConfigured = false;
/*     */   private String ciphers;
/*  96 */   private LinkedHashSet<Cipher> cipherList = null;
/*  97 */   private List<String> jsseCipherNames = null;
/*  98 */   private String honorCipherOrder = null;
/*  99 */   private Set<String> protocols = new HashSet();
/*     */   
/* 101 */   private String keyManagerAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
/* 102 */   private boolean revocationEnabled = false;
/* 103 */   private int sessionCacheSize = 0;
/* 104 */   private int sessionTimeout = 86400;
/* 105 */   private String sslProtocol = "TLS";
/*     */   private String trustManagerClassName;
/* 107 */   private String truststoreAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
/* 108 */   private String truststoreFile = System.getProperty("javax.net.ssl.trustStore");
/* 109 */   private String truststorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
/* 110 */   private String truststoreProvider = System.getProperty("javax.net.ssl.trustStoreProvider");
/* 111 */   private String truststoreType = System.getProperty("javax.net.ssl.trustStoreType");
/* 112 */   private transient KeyStore truststore = null;
/*     */   
/*     */   private String certificateRevocationListPath;
/*     */   private String caCertificateFile;
/*     */   private String caCertificatePath;
/* 117 */   private boolean disableCompression = true;
/* 118 */   private boolean disableSessionTickets = false;
/* 119 */   private boolean insecureRenegotiation = false;
/* 120 */   private OpenSSLConf openSslConf = null;
/*     */   
/*     */   public SSLHostConfig()
/*     */   {
/* 124 */     setProtocols("all");
/*     */   }
/*     */   
/*     */   public Long getOpenSslConfContext()
/*     */   {
/* 129 */     return this.openSslConfContext;
/*     */   }
/*     */   
/*     */   public void setOpenSslConfContext(Long openSslConfContext)
/*     */   {
/* 134 */     this.openSslConfContext = openSslConfContext;
/*     */   }
/*     */   
/*     */   public Long getOpenSslContext()
/*     */   {
/* 139 */     return this.openSslContext;
/*     */   }
/*     */   
/*     */   public void setOpenSslContext(Long openSslContext)
/*     */   {
/* 144 */     this.openSslContext = openSslContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 150 */   public String getConfigType() { return this.configType.name(); }
/*     */   
/*     */   public void setConfigType(Type configType) {
/* 153 */     this.configType = configType;
/* 154 */     if (configType == Type.EITHER) {
/* 155 */       if (this.configuredProperties.remove(Type.JSSE) == null) {
/* 156 */         this.configuredProperties.remove(Type.OPENSSL);
/*     */       }
/*     */     } else {
/* 159 */       this.configuredProperties.remove(configType);
/*     */     }
/* 161 */     for (Iterator localIterator1 = this.configuredProperties.entrySet().iterator(); localIterator1.hasNext();) { entry = (Map.Entry)localIterator1.next();
/* 162 */       for (String property : (Set)entry.getValue()) {
/* 163 */         log.warn(sm.getString("sslHostConfig.mismatch", new Object[] { property, 
/* 164 */           getHostName(), entry.getKey(), configType }));
/*     */       }
/*     */     }
/*     */     Map.Entry<Type, Set<String>> entry;
/*     */   }
/*     */   
/*     */   void setProperty(String name, Type configType) {
/* 171 */     if (this.configType == null) {
/* 172 */       Set<String> properties = (Set)this.configuredProperties.get(configType);
/* 173 */       if (properties == null) {
/* 174 */         properties = new HashSet();
/* 175 */         this.configuredProperties.put(configType, properties);
/*     */       }
/* 177 */       properties.add(name);
/* 178 */     } else if (this.configType == Type.EITHER) {
/* 179 */       if (this.currentConfigType == null) {
/* 180 */         this.currentConfigType = configType;
/* 181 */       } else if (this.currentConfigType != configType) {
/* 182 */         log.warn(sm.getString("sslHostConfig.mismatch", new Object[] { name, 
/* 183 */           getHostName(), configType, this.currentConfigType }));
/*     */       }
/*     */     }
/* 186 */     else if (configType != this.configType) {
/* 187 */       log.warn(sm.getString("sslHostConfig.mismatch", new Object[] { name, 
/* 188 */         getHostName(), configType, this.configType }));
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
/*     */   public String[] getEnabledProtocols()
/*     */   {
/* 202 */     return this.enabledProtocols;
/*     */   }
/*     */   
/*     */   public void setEnabledProtocols(String[] enabledProtocols)
/*     */   {
/* 207 */     this.enabledProtocols = enabledProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getEnabledCiphers()
/*     */   {
/* 217 */     return this.enabledCiphers;
/*     */   }
/*     */   
/*     */   public void setEnabledCiphers(String[] enabledCiphers)
/*     */   {
/* 222 */     this.enabledCiphers = enabledCiphers;
/*     */   }
/*     */   
/*     */   public ObjectName getObjectName()
/*     */   {
/* 227 */     return this.oname;
/*     */   }
/*     */   
/*     */   public void setObjectName(ObjectName oname)
/*     */   {
/* 232 */     this.oname = oname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void registerDefaultCertificate()
/*     */   {
/* 239 */     if (this.defaultCertificate == null) {
/* 240 */       this.defaultCertificate = new SSLHostConfigCertificate(this, SSLHostConfigCertificate.Type.UNDEFINED);
/*     */       
/* 242 */       this.certificates.add(this.defaultCertificate);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addCertificate(SSLHostConfigCertificate certificate)
/*     */   {
/* 250 */     if (this.certificates.size() == 0) {
/* 251 */       this.certificates.add(certificate);
/* 252 */       return;
/*     */     }
/*     */     
/* 255 */     if (((this.certificates.size() == 1) && 
/* 256 */       (((SSLHostConfigCertificate)this.certificates.iterator().next()).getType() == SSLHostConfigCertificate.Type.UNDEFINED)) || 
/* 257 */       (certificate.getType() == SSLHostConfigCertificate.Type.UNDEFINED))
/*     */     {
/* 259 */       throw new IllegalArgumentException(sm.getString("sslHostConfig.certificate.notype"));
/*     */     }
/*     */     
/* 262 */     this.certificates.add(certificate);
/*     */   }
/*     */   
/*     */   public OpenSSLConf getOpenSslConf()
/*     */   {
/* 267 */     return this.openSslConf;
/*     */   }
/*     */   
/*     */   public void setOpenSslConf(OpenSSLConf conf)
/*     */   {
/* 272 */     if (conf == null)
/* 273 */       throw new IllegalArgumentException(sm.getString("sslHostConfig.opensslconf.null"));
/* 274 */     if (this.openSslConf != null) {
/* 275 */       throw new IllegalArgumentException(sm.getString("sslHostConfig.opensslconf.alreadySet"));
/*     */     }
/* 277 */     setProperty("<OpenSSLConf>", Type.OPENSSL);
/* 278 */     this.openSslConf = conf;
/*     */   }
/*     */   
/*     */   public Set<SSLHostConfigCertificate> getCertificates()
/*     */   {
/* 283 */     return getCertificates(false);
/*     */   }
/*     */   
/*     */   public Set<SSLHostConfigCertificate> getCertificates(boolean createDefaultIfEmpty)
/*     */   {
/* 288 */     if ((this.certificates.size() == 0) && (createDefaultIfEmpty)) {
/* 289 */       registerDefaultCertificate();
/*     */     }
/* 291 */     return this.certificates;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCertificateKeyPassword()
/*     */   {
/* 301 */     registerDefaultCertificate();
/* 302 */     return this.defaultCertificate.getCertificateKeyPassword();
/*     */   }
/*     */   
/* 305 */   public void setCertificateKeyPassword(String certificateKeyPassword) { registerDefaultCertificate();
/* 306 */     this.defaultCertificate.setCertificateKeyPassword(certificateKeyPassword);
/*     */   }
/*     */   
/*     */   public void setCertificateRevocationListFile(String certificateRevocationListFile)
/*     */   {
/* 311 */     this.certificateRevocationListFile = certificateRevocationListFile;
/*     */   }
/*     */   
/*     */   public String getCertificateRevocationListFile()
/*     */   {
/* 316 */     return this.certificateRevocationListFile;
/*     */   }
/*     */   
/*     */   public void setCertificateVerification(String certificateVerification)
/*     */   {
/*     */     try
/*     */     {
/* 323 */       this.certificateVerification = CertificateVerification.fromString(certificateVerification);
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 327 */       this.certificateVerification = CertificateVerification.REQUIRED;
/* 328 */       throw iae;
/*     */     }
/*     */   }
/*     */   
/*     */   public CertificateVerification getCertificateVerification()
/*     */   {
/* 334 */     return this.certificateVerification;
/*     */   }
/*     */   
/*     */   public void setCertificateVerificationDepth(int certificateVerificationDepth)
/*     */   {
/* 339 */     this.certificateVerificationDepth = certificateVerificationDepth;
/* 340 */     this.certificateVerificationDepthConfigured = true;
/*     */   }
/*     */   
/*     */   public int getCertificateVerificationDepth()
/*     */   {
/* 345 */     return this.certificateVerificationDepth;
/*     */   }
/*     */   
/*     */   public boolean isCertificateVerificationDepthConfigured()
/*     */   {
/* 350 */     return this.certificateVerificationDepthConfigured;
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
/*     */   public void setCiphers(String ciphersList)
/*     */   {
/* 363 */     if ((ciphersList != null) && (!ciphersList.contains(":"))) {
/* 364 */       StringBuilder sb = new StringBuilder();
/*     */       
/*     */ 
/* 367 */       String[] ciphers = ciphersList.split(",");
/* 368 */       for (String cipher : ciphers) {
/* 369 */         String trimmed = cipher.trim();
/* 370 */         if (trimmed.length() > 0) {
/* 371 */           String openSSLName = OpenSSLCipherConfigurationParser.jsseToOpenSSL(trimmed);
/* 372 */           if (openSSLName == null)
/*     */           {
/* 374 */             openSSLName = trimmed;
/*     */           }
/* 376 */           if (sb.length() > 0) {
/* 377 */             sb.append(':');
/*     */           }
/* 379 */           sb.append(openSSLName);
/*     */         }
/*     */       }
/* 382 */       this.ciphers = sb.toString();
/*     */     } else {
/* 384 */       this.ciphers = ciphersList;
/*     */     }
/* 386 */     this.cipherList = null;
/* 387 */     this.jsseCipherNames = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCiphers()
/*     */   {
/* 395 */     if (this.ciphers == null) {
/* 396 */       if ((!JreCompat.isJre8Available()) && (Type.JSSE.equals(this.configType))) {
/* 397 */         this.ciphers = "HIGH:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!MD5:!kRSA:!DHE";
/*     */       } else {
/* 399 */         this.ciphers = "HIGH:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!MD5:!kRSA";
/*     */       }
/*     */     }
/*     */     
/* 403 */     return this.ciphers;
/*     */   }
/*     */   
/*     */   public LinkedHashSet<Cipher> getCipherList()
/*     */   {
/* 408 */     if (this.cipherList == null) {
/* 409 */       this.cipherList = OpenSSLCipherConfigurationParser.parse(getCiphers());
/*     */     }
/* 411 */     return this.cipherList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getJsseCipherNames()
/*     */   {
/* 423 */     if (this.jsseCipherNames == null) {
/* 424 */       this.jsseCipherNames = OpenSSLCipherConfigurationParser.convertForJSSE(getCipherList());
/*     */     }
/* 426 */     return this.jsseCipherNames;
/*     */   }
/*     */   
/*     */   public void setHonorCipherOrder(String honorCipherOrder)
/*     */   {
/* 431 */     this.honorCipherOrder = honorCipherOrder;
/*     */   }
/*     */   
/*     */   public String getHonorCipherOrder()
/*     */   {
/* 436 */     return this.honorCipherOrder;
/*     */   }
/*     */   
/*     */   public void setHostName(String hostName)
/*     */   {
/* 441 */     this.hostName = hostName;
/*     */   }
/*     */   
/*     */   public String getHostName()
/*     */   {
/* 446 */     return this.hostName;
/*     */   }
/*     */   
/*     */   public void setProtocols(String input)
/*     */   {
/* 451 */     this.protocols.clear();
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
/* 464 */     for (String value : input.split("(?=[-+,])")) {
/* 465 */       String trimmed = value.trim();
/*     */       
/* 467 */       if (trimmed.length() > 1) {
/* 468 */         if (trimmed.charAt(0) == '+') {
/* 469 */           trimmed = trimmed.substring(1).trim();
/* 470 */           if (trimmed.equalsIgnoreCase("all")) {
/* 471 */             this.protocols.addAll(SSL_PROTO_ALL_SET);
/*     */           } else {
/* 473 */             this.protocols.add(trimmed);
/*     */           }
/* 475 */         } else if (trimmed.charAt(0) == '-') {
/* 476 */           trimmed = trimmed.substring(1).trim();
/* 477 */           if (trimmed.equalsIgnoreCase("all")) {
/* 478 */             this.protocols.removeAll(SSL_PROTO_ALL_SET);
/*     */           } else {
/* 480 */             this.protocols.remove(trimmed);
/*     */           }
/*     */         } else {
/* 483 */           if (trimmed.charAt(0) == ',') {
/* 484 */             trimmed = trimmed.substring(1).trim();
/*     */           }
/* 486 */           if (!this.protocols.isEmpty()) {
/* 487 */             log.warn(sm.getString("sslHostConfig.prefix_missing", new Object[] { trimmed, 
/* 488 */               getHostName() }));
/*     */           }
/* 490 */           if (trimmed.equalsIgnoreCase("all")) {
/* 491 */             this.protocols.addAll(SSL_PROTO_ALL_SET);
/*     */           } else {
/* 493 */             this.protocols.add(trimmed);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getProtocols()
/*     */   {
/* 502 */     return this.protocols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCertificateKeyAlias()
/*     */   {
/* 512 */     registerDefaultCertificate();
/* 513 */     return this.defaultCertificate.getCertificateKeyAlias();
/*     */   }
/*     */   
/* 516 */   public void setCertificateKeyAlias(String certificateKeyAlias) { registerDefaultCertificate();
/* 517 */     this.defaultCertificate.setCertificateKeyAlias(certificateKeyAlias);
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreFile()
/*     */   {
/* 522 */     registerDefaultCertificate();
/* 523 */     return this.defaultCertificate.getCertificateKeystoreFile();
/*     */   }
/*     */   
/* 526 */   public void setCertificateKeystoreFile(String certificateKeystoreFile) { registerDefaultCertificate();
/* 527 */     this.defaultCertificate.setCertificateKeystoreFile(certificateKeystoreFile);
/*     */   }
/*     */   
/*     */   public String getCertificateKeystorePassword()
/*     */   {
/* 532 */     registerDefaultCertificate();
/* 533 */     return this.defaultCertificate.getCertificateKeystorePassword();
/*     */   }
/*     */   
/* 536 */   public void setCertificateKeystorePassword(String certificateKeystorePassword) { registerDefaultCertificate();
/* 537 */     this.defaultCertificate.setCertificateKeystorePassword(certificateKeystorePassword);
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreProvider()
/*     */   {
/* 542 */     registerDefaultCertificate();
/* 543 */     return this.defaultCertificate.getCertificateKeystoreProvider();
/*     */   }
/*     */   
/* 546 */   public void setCertificateKeystoreProvider(String certificateKeystoreProvider) { registerDefaultCertificate();
/* 547 */     this.defaultCertificate.setCertificateKeystoreProvider(certificateKeystoreProvider);
/*     */   }
/*     */   
/*     */   public String getCertificateKeystoreType()
/*     */   {
/* 552 */     registerDefaultCertificate();
/* 553 */     return this.defaultCertificate.getCertificateKeystoreType();
/*     */   }
/*     */   
/* 556 */   public void setCertificateKeystoreType(String certificateKeystoreType) { registerDefaultCertificate();
/* 557 */     this.defaultCertificate.setCertificateKeystoreType(certificateKeystoreType);
/*     */   }
/*     */   
/*     */   public void setKeyManagerAlgorithm(String keyManagerAlgorithm)
/*     */   {
/* 562 */     setProperty("keyManagerAlgorithm", Type.JSSE);
/* 563 */     this.keyManagerAlgorithm = keyManagerAlgorithm;
/*     */   }
/*     */   
/*     */   public String getKeyManagerAlgorithm()
/*     */   {
/* 568 */     return this.keyManagerAlgorithm;
/*     */   }
/*     */   
/*     */   public void setRevocationEnabled(boolean revocationEnabled)
/*     */   {
/* 573 */     setProperty("revocationEnabled", Type.JSSE);
/* 574 */     this.revocationEnabled = revocationEnabled;
/*     */   }
/*     */   
/*     */   public boolean getRevocationEnabled()
/*     */   {
/* 579 */     return this.revocationEnabled;
/*     */   }
/*     */   
/*     */   public void setSessionCacheSize(int sessionCacheSize)
/*     */   {
/* 584 */     setProperty("sessionCacheSize", Type.JSSE);
/* 585 */     this.sessionCacheSize = sessionCacheSize;
/*     */   }
/*     */   
/*     */   public int getSessionCacheSize()
/*     */   {
/* 590 */     return this.sessionCacheSize;
/*     */   }
/*     */   
/*     */   public void setSessionTimeout(int sessionTimeout)
/*     */   {
/* 595 */     setProperty("sessionTimeout", Type.JSSE);
/* 596 */     this.sessionTimeout = sessionTimeout;
/*     */   }
/*     */   
/*     */   public int getSessionTimeout()
/*     */   {
/* 601 */     return this.sessionTimeout;
/*     */   }
/*     */   
/*     */   public void setSslProtocol(String sslProtocol)
/*     */   {
/* 606 */     setProperty("sslProtocol", Type.JSSE);
/* 607 */     this.sslProtocol = sslProtocol;
/*     */   }
/*     */   
/*     */   public String getSslProtocol()
/*     */   {
/* 612 */     return this.sslProtocol;
/*     */   }
/*     */   
/*     */   public void setTrustManagerClassName(String trustManagerClassName)
/*     */   {
/* 617 */     setProperty("trustManagerClassName", Type.JSSE);
/* 618 */     this.trustManagerClassName = trustManagerClassName;
/*     */   }
/*     */   
/*     */   public String getTrustManagerClassName()
/*     */   {
/* 623 */     return this.trustManagerClassName;
/*     */   }
/*     */   
/*     */   public void setTruststoreAlgorithm(String truststoreAlgorithm)
/*     */   {
/* 628 */     setProperty("truststoreAlgorithm", Type.JSSE);
/* 629 */     this.truststoreAlgorithm = truststoreAlgorithm;
/*     */   }
/*     */   
/*     */   public String getTruststoreAlgorithm()
/*     */   {
/* 634 */     return this.truststoreAlgorithm;
/*     */   }
/*     */   
/*     */   public void setTruststoreFile(String truststoreFile)
/*     */   {
/* 639 */     setProperty("truststoreFile", Type.JSSE);
/* 640 */     this.truststoreFile = truststoreFile;
/*     */   }
/*     */   
/*     */   public String getTruststoreFile()
/*     */   {
/* 645 */     return this.truststoreFile;
/*     */   }
/*     */   
/*     */   public void setTruststorePassword(String truststorePassword)
/*     */   {
/* 650 */     setProperty("truststorePassword", Type.JSSE);
/* 651 */     this.truststorePassword = truststorePassword;
/*     */   }
/*     */   
/*     */   public String getTruststorePassword()
/*     */   {
/* 656 */     return this.truststorePassword;
/*     */   }
/*     */   
/*     */   public void setTruststoreProvider(String truststoreProvider)
/*     */   {
/* 661 */     setProperty("truststoreProvider", Type.JSSE);
/* 662 */     this.truststoreProvider = truststoreProvider;
/*     */   }
/*     */   
/*     */   public String getTruststoreProvider()
/*     */   {
/* 667 */     if (this.truststoreProvider == null) {
/* 668 */       Set<SSLHostConfigCertificate> certificates = getCertificates();
/* 669 */       if (certificates.size() == 1) {
/* 670 */         return ((SSLHostConfigCertificate)certificates.iterator().next()).getCertificateKeystoreProvider();
/*     */       }
/* 672 */       return SSLHostConfigCertificate.DEFAULT_KEYSTORE_PROVIDER;
/*     */     }
/* 674 */     return this.truststoreProvider;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTruststoreType(String truststoreType)
/*     */   {
/* 680 */     setProperty("truststoreType", Type.JSSE);
/* 681 */     this.truststoreType = truststoreType;
/*     */   }
/*     */   
/*     */   public String getTruststoreType()
/*     */   {
/* 686 */     if (this.truststoreType == null) {
/* 687 */       Set<SSLHostConfigCertificate> certificates = getCertificates();
/* 688 */       if (certificates.size() == 1) {
/* 689 */         String keystoreType = ((SSLHostConfigCertificate)certificates.iterator().next()).getCertificateKeystoreType();
/*     */         
/*     */ 
/* 692 */         if (!"PKCS12".equalsIgnoreCase(keystoreType)) {
/* 693 */           return keystoreType;
/*     */         }
/*     */       }
/* 696 */       return SSLHostConfigCertificate.DEFAULT_KEYSTORE_TYPE;
/*     */     }
/* 698 */     return this.truststoreType;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTrustStore(KeyStore truststore)
/*     */   {
/* 704 */     this.truststore = truststore;
/*     */   }
/*     */   
/*     */   public KeyStore getTruststore() throws IOException
/*     */   {
/* 709 */     KeyStore result = this.truststore;
/* 710 */     if ((result == null) && 
/* 711 */       (this.truststoreFile != null)) {
/*     */       try {
/* 713 */         result = SSLUtilBase.getStore(getTruststoreType(), getTruststoreProvider(), 
/* 714 */           getTruststoreFile(), getTruststorePassword());
/*     */       } catch (IOException ioe) {
/* 716 */         Throwable cause = ioe.getCause();
/* 717 */         if ((cause instanceof UnrecoverableKeyException))
/*     */         {
/* 719 */           log.warn(sm.getString("jsse.invalid_truststore_password"), cause);
/*     */           
/*     */ 
/* 722 */           result = SSLUtilBase.getStore(getTruststoreType(), getTruststoreProvider(), 
/* 723 */             getTruststoreFile(), null);
/*     */         }
/*     */         else {
/* 726 */           throw ioe;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 731 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCertificateChainFile()
/*     */   {
/* 741 */     registerDefaultCertificate();
/* 742 */     return this.defaultCertificate.getCertificateChainFile();
/*     */   }
/*     */   
/* 745 */   public void setCertificateChainFile(String certificateChainFile) { registerDefaultCertificate();
/* 746 */     this.defaultCertificate.setCertificateChainFile(certificateChainFile);
/*     */   }
/*     */   
/*     */   public String getCertificateFile()
/*     */   {
/* 751 */     registerDefaultCertificate();
/* 752 */     return this.defaultCertificate.getCertificateFile();
/*     */   }
/*     */   
/* 755 */   public void setCertificateFile(String certificateFile) { registerDefaultCertificate();
/* 756 */     this.defaultCertificate.setCertificateFile(certificateFile);
/*     */   }
/*     */   
/*     */   public String getCertificateKeyFile()
/*     */   {
/* 761 */     registerDefaultCertificate();
/* 762 */     return this.defaultCertificate.getCertificateKeyFile();
/*     */   }
/*     */   
/* 765 */   public void setCertificateKeyFile(String certificateKeyFile) { registerDefaultCertificate();
/* 766 */     this.defaultCertificate.setCertificateKeyFile(certificateKeyFile);
/*     */   }
/*     */   
/*     */   public void setCertificateRevocationListPath(String certificateRevocationListPath)
/*     */   {
/* 771 */     setProperty("certificateRevocationListPath", Type.OPENSSL);
/* 772 */     this.certificateRevocationListPath = certificateRevocationListPath;
/*     */   }
/*     */   
/*     */   public String getCertificateRevocationListPath()
/*     */   {
/* 777 */     return this.certificateRevocationListPath;
/*     */   }
/*     */   
/*     */   public void setCaCertificateFile(String caCertificateFile)
/*     */   {
/* 782 */     setProperty("caCertificateFile", Type.OPENSSL);
/* 783 */     this.caCertificateFile = caCertificateFile;
/*     */   }
/*     */   
/*     */   public String getCaCertificateFile()
/*     */   {
/* 788 */     return this.caCertificateFile;
/*     */   }
/*     */   
/*     */   public void setCaCertificatePath(String caCertificatePath)
/*     */   {
/* 793 */     setProperty("caCertificatePath", Type.OPENSSL);
/* 794 */     this.caCertificatePath = caCertificatePath;
/*     */   }
/*     */   
/*     */   public String getCaCertificatePath()
/*     */   {
/* 799 */     return this.caCertificatePath;
/*     */   }
/*     */   
/*     */   public void setDisableCompression(boolean disableCompression)
/*     */   {
/* 804 */     setProperty("disableCompression", Type.OPENSSL);
/* 805 */     this.disableCompression = disableCompression;
/*     */   }
/*     */   
/*     */   public boolean getDisableCompression()
/*     */   {
/* 810 */     return this.disableCompression;
/*     */   }
/*     */   
/*     */   public void setDisableSessionTickets(boolean disableSessionTickets)
/*     */   {
/* 815 */     setProperty("disableSessionTickets", Type.OPENSSL);
/* 816 */     this.disableSessionTickets = disableSessionTickets;
/*     */   }
/*     */   
/*     */   public boolean getDisableSessionTickets()
/*     */   {
/* 821 */     return this.disableSessionTickets;
/*     */   }
/*     */   
/*     */   public void setInsecureRenegotiation(boolean insecureRenegotiation)
/*     */   {
/* 826 */     setProperty("insecureRenegotiation", Type.OPENSSL);
/* 827 */     this.insecureRenegotiation = insecureRenegotiation;
/*     */   }
/*     */   
/*     */   public boolean getInsecureRenegotiation()
/*     */   {
/* 832 */     return this.insecureRenegotiation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String adjustRelativePath(String path)
/*     */   {
/* 841 */     if ((path == null) || (path.length() == 0)) {
/* 842 */       return path;
/*     */     }
/* 844 */     String newPath = path;
/* 845 */     File f = new File(newPath);
/* 846 */     if (!f.isAbsolute()) {
/* 847 */       newPath = System.getProperty("catalina.base") + File.separator + newPath;
/* 848 */       f = new File(newPath);
/*     */     }
/* 850 */     if (!f.exists())
/*     */     {
/* 852 */       log.warn("configured file:[" + newPath + "] does not exist.");
/*     */     }
/* 854 */     return newPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Type
/*     */   {
/* 861 */     JSSE, 
/* 862 */     OPENSSL, 
/* 863 */     EITHER;
/*     */     
/*     */     private Type() {}
/*     */   }
/*     */   
/* 868 */   public static enum CertificateVerification { NONE, 
/* 869 */     OPTIONAL_NO_CA, 
/* 870 */     OPTIONAL, 
/* 871 */     REQUIRED;
/*     */     
/*     */     private CertificateVerification() {}
/* 874 */     public static CertificateVerification fromString(String value) { if (("true".equalsIgnoreCase(value)) || 
/* 875 */         ("yes".equalsIgnoreCase(value)) || 
/* 876 */         ("require".equalsIgnoreCase(value)) || 
/* 877 */         ("required".equalsIgnoreCase(value)))
/* 878 */         return REQUIRED;
/* 879 */       if (("optional".equalsIgnoreCase(value)) || 
/* 880 */         ("want".equalsIgnoreCase(value)))
/* 881 */         return OPTIONAL;
/* 882 */       if (("optionalNoCA".equalsIgnoreCase(value)) || 
/* 883 */         ("optional_no_ca".equalsIgnoreCase(value)))
/* 884 */         return OPTIONAL_NO_CA;
/* 885 */       if (("false".equalsIgnoreCase(value)) || 
/* 886 */         ("no".equalsIgnoreCase(value)) || 
/* 887 */         ("none".equalsIgnoreCase(value))) {
/* 888 */         return NONE;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 894 */       throw new IllegalArgumentException(SSLHostConfig.sm.getString("sslHostConfig.certificateVerificationInvalid", new Object[] { value }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SSLHostConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */