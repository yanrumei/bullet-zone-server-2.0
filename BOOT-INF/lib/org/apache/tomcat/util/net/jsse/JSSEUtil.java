/*     */ package org.apache.tomcat.util.net.jsse;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertPathParameters;
/*     */ import java.security.cert.CertStore;
/*     */ import java.security.cert.CertStoreParameters;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.CollectionCertStoreParameters;
/*     */ import java.security.cert.PKIXBuilderParameters;
/*     */ import java.security.cert.X509CertSelector;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.CertPathTrustManagerParameters;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.compat.JreVendor;
/*     */ import org.apache.tomcat.util.file.ConfigFileLoader;
/*     */ import org.apache.tomcat.util.net.SSLContext;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.apache.tomcat.util.net.SSLHostConfigCertificate;
/*     */ import org.apache.tomcat.util.net.SSLUtilBase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSSEUtil
/*     */   extends SSLUtilBase
/*     */ {
/*  80 */   private static final Log log = LogFactory.getLog(JSSEUtil.class);
/*  81 */   private static final StringManager sm = StringManager.getManager(JSSEUtil.class);
/*     */   private static final Set<String> implementedProtocols;
/*     */   private static final Set<String> implementedCiphers;
/*     */   private final SSLHostConfig sslHostConfig;
/*     */   
/*     */   static
/*     */   {
/*     */     try {
/*  89 */       SSLContext context = new JSSESSLContext("TLS");
/*  90 */       context.init(null, null, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException|KeyManagementException e)
/*     */     {
/*  94 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */     SSLContext context;
/*  97 */     String[] implementedProtocolsArray = context.getSupportedSSLParameters().getProtocols();
/*  98 */     implementedProtocols = new HashSet(implementedProtocolsArray.length);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     String[] arrayOfString1 = implementedProtocolsArray;int i = arrayOfString1.length; String protocol; for (String str1 = 0; str1 < i; str1++) { protocol = arrayOfString1[str1];
/* 106 */       String protocolUpper = protocol.toUpperCase(Locale.ENGLISH);
/* 107 */       if ((!"SSLV2HELLO".equals(protocolUpper)) && (!"SSLV3".equals(protocolUpper)) && 
/* 108 */         (protocolUpper.contains("SSL"))) {
/* 109 */         log.debug(sm.getString("jsse.excludeProtocol", new Object[] { protocol }));
/*     */       }
/*     */       else
/*     */       {
/* 113 */         implementedProtocols.add(protocol);
/*     */       }
/*     */     }
/* 116 */     if (implementedProtocols.size() == 0) {
/* 117 */       log.warn(sm.getString("jsse.noDefaultProtocols"));
/*     */     }
/*     */     
/* 120 */     String[] implementedCipherSuiteArray = context.getSupportedSSLParameters().getCipherSuites();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 125 */     if (JreVendor.IS_IBM_JVM) {
/* 126 */       implementedCiphers = new HashSet(implementedCipherSuiteArray.length * 2);
/* 127 */       String[] arrayOfString2 = implementedCipherSuiteArray;str1 = arrayOfString2.length; for (protocol = 0; protocol < str1; protocol++) { String name = arrayOfString2[protocol];
/* 128 */         implementedCiphers.add(name);
/* 129 */         if (name.startsWith("SSL")) {
/* 130 */           implementedCiphers.add("TLS" + name.substring(3));
/*     */         }
/*     */       }
/*     */     } else {
/* 134 */       implementedCiphers = new HashSet(implementedCipherSuiteArray.length);
/* 135 */       implementedCiphers.addAll(Arrays.asList(implementedCipherSuiteArray));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSSEUtil(SSLHostConfigCertificate certificate)
/*     */   {
/* 144 */     super(certificate);
/* 145 */     this.sslHostConfig = certificate.getSSLHostConfig();
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 151 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<String> getImplementedProtocols()
/*     */   {
/* 157 */     return implementedProtocols;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Set<String> getImplementedCiphers()
/*     */   {
/* 163 */     return implementedCiphers;
/*     */   }
/*     */   
/*     */   public SSLContext createSSLContext(List<String> negotiableProtocols)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 169 */     return new JSSESSLContext(this.sslHostConfig.getSslProtocol());
/*     */   }
/*     */   
/*     */   public KeyManager[] getKeyManagers()
/*     */     throws Exception
/*     */   {
/* 175 */     String keyAlias = this.certificate.getCertificateKeyAlias();
/* 176 */     String algorithm = this.sslHostConfig.getKeyManagerAlgorithm();
/* 177 */     String keyPass = this.certificate.getCertificateKeyPassword();
/*     */     
/*     */ 
/* 180 */     if (keyPass == null) {
/* 181 */       keyPass = this.certificate.getCertificateKeystorePassword();
/*     */     }
/*     */     
/* 184 */     KeyStore ks = this.certificate.getCertificateKeystore();
/* 185 */     KeyStore ksUsed = ks;
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
/* 199 */     char[] keyPassArray = keyPass.toCharArray();
/*     */     
/* 201 */     if (ks == null)
/*     */     {
/* 203 */       PEMFile privateKeyFile = new PEMFile(SSLHostConfig.adjustRelativePath(this.certificate.getCertificateKeyFile() != null ? this.certificate.getCertificateKeyFile() : this.certificate.getCertificateFile()), keyPass);
/*     */       
/* 205 */       PEMFile certificateFile = new PEMFile(SSLHostConfig.adjustRelativePath(this.certificate.getCertificateFile()));
/*     */       
/* 207 */       Collection<Certificate> chain = new ArrayList();
/* 208 */       chain.addAll(certificateFile.getCertificates());
/* 209 */       if (this.certificate.getCertificateChainFile() != null) {
/* 210 */         PEMFile certificateChainFile = new PEMFile(SSLHostConfig.adjustRelativePath(this.certificate.getCertificateChainFile()));
/* 211 */         chain.addAll(certificateChainFile.getCertificates());
/*     */       }
/*     */       
/* 214 */       if (keyAlias == null) {
/* 215 */         keyAlias = "tomcat";
/*     */       }
/*     */       
/*     */ 
/* 219 */       ksUsed = KeyStore.getInstance("JKS");
/* 220 */       ksUsed.load(null, null);
/* 221 */       ksUsed.setKeyEntry(keyAlias, privateKeyFile.getPrivateKey(), keyPass.toCharArray(), 
/* 222 */         (Certificate[])chain.toArray(new Certificate[chain.size()]));
/*     */     } else {
/* 224 */       if ((keyAlias != null) && (!ks.isKeyEntry(keyAlias)))
/* 225 */         throw new IOException(sm.getString("jsse.alias_no_key_entry", new Object[] { keyAlias }));
/* 226 */       if (keyAlias == null) {
/* 227 */         Enumeration<String> aliases = ks.aliases();
/* 228 */         if (!aliases.hasMoreElements()) {
/* 229 */           throw new IOException(sm.getString("jsse.noKeys"));
/*     */         }
/* 231 */         while ((aliases.hasMoreElements()) && (keyAlias == null)) {
/* 232 */           keyAlias = (String)aliases.nextElement();
/* 233 */           if (!ks.isKeyEntry(keyAlias)) {
/* 234 */             keyAlias = null;
/*     */           }
/*     */         }
/* 237 */         if (keyAlias == null) {
/* 238 */           throw new IOException(sm.getString("jsse.alias_no_key_entry", new Object[] { null }));
/*     */         }
/*     */       }
/*     */       
/* 242 */       Key k = ks.getKey(keyAlias, keyPassArray);
/* 243 */       if ((k != null) && ("PKCS#8".equalsIgnoreCase(k.getFormat())))
/*     */       {
/* 245 */         String provider = this.certificate.getCertificateKeystoreProvider();
/* 246 */         if (provider == null) {
/* 247 */           ksUsed = KeyStore.getInstance(this.certificate.getCertificateKeystoreType());
/*     */         } else {
/* 249 */           ksUsed = KeyStore.getInstance(this.certificate.getCertificateKeystoreType(), provider);
/*     */         }
/*     */         
/* 252 */         ksUsed.load(null, null);
/* 253 */         ksUsed.setKeyEntry(keyAlias, k, keyPassArray, ks.getCertificateChain(keyAlias));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 259 */     KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
/* 260 */     kmf.init(ksUsed, keyPassArray);
/*     */     
/* 262 */     KeyManager[] kms = kmf.getKeyManagers();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 267 */     if ((kms != null) && (ksUsed == ks)) {
/* 268 */       String alias = keyAlias;
/*     */       
/* 270 */       if ("JKS".equals(this.certificate.getCertificateKeystoreType())) {
/* 271 */         alias = alias.toLowerCase(Locale.ENGLISH);
/*     */       }
/* 273 */       for (int i = 0; i < kms.length; i++) {
/* 274 */         kms[i] = new JSSEKeyManager((X509KeyManager)kms[i], alias);
/*     */       }
/*     */     }
/*     */     
/* 278 */     return kms;
/*     */   }
/*     */   
/*     */ 
/*     */   public TrustManager[] getTrustManagers()
/*     */     throws Exception
/*     */   {
/* 285 */     String className = this.sslHostConfig.getTrustManagerClassName();
/* 286 */     if ((className != null) && (className.length() > 0)) {
/* 287 */       ClassLoader classLoader = getClass().getClassLoader();
/* 288 */       Class<?> clazz = classLoader.loadClass(className);
/* 289 */       if (!TrustManager.class.isAssignableFrom(clazz)) {
/* 290 */         throw new InstantiationException(sm.getString("jsse.invalidTrustManagerClassName", new Object[] { className }));
/*     */       }
/*     */       
/* 293 */       Object trustManagerObject = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 294 */       TrustManager trustManager = (TrustManager)trustManagerObject;
/* 295 */       return new TrustManager[] { trustManager };
/*     */     }
/*     */     
/* 298 */     TrustManager[] tms = null;
/*     */     
/* 300 */     KeyStore trustStore = this.sslHostConfig.getTruststore();
/* 301 */     if (trustStore != null) {
/* 302 */       checkTrustStoreEntries(trustStore);
/* 303 */       String algorithm = this.sslHostConfig.getTruststoreAlgorithm();
/* 304 */       String crlf = this.sslHostConfig.getCertificateRevocationListFile();
/* 305 */       boolean revocationEnabled = this.sslHostConfig.getRevocationEnabled();
/*     */       
/* 307 */       if ("PKIX".equalsIgnoreCase(algorithm)) {
/* 308 */         TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
/* 309 */         CertPathParameters params = getParameters(crlf, trustStore, revocationEnabled);
/* 310 */         ManagerFactoryParameters mfp = new CertPathTrustManagerParameters(params);
/* 311 */         tmf.init(mfp);
/* 312 */         tms = tmf.getTrustManagers();
/*     */       } else {
/* 314 */         TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
/* 315 */         tmf.init(trustStore);
/* 316 */         tms = tmf.getTrustManagers();
/* 317 */         if ((crlf != null) && (crlf.length() > 0)) {
/* 318 */           throw new CRLException(sm.getString("jsseUtil.noCrlSupport", new Object[] { algorithm }));
/*     */         }
/*     */         
/* 321 */         if (this.sslHostConfig.isCertificateVerificationDepthConfigured()) {
/* 322 */           log.warn(sm.getString("jsseUtil.noVerificationDepth", new Object[] { algorithm }));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 327 */     return tms;
/*     */   }
/*     */   
/*     */   private void checkTrustStoreEntries(KeyStore trustStore) throws Exception
/*     */   {
/* 332 */     Enumeration<String> aliases = trustStore.aliases();
/* 333 */     if (aliases != null) {
/* 334 */       Date now = new Date();
/* 335 */       while (aliases.hasMoreElements()) {
/* 336 */         String alias = (String)aliases.nextElement();
/* 337 */         if (trustStore.isCertificateEntry(alias)) {
/* 338 */           Certificate cert = trustStore.getCertificate(alias);
/* 339 */           if ((cert instanceof X509Certificate)) {
/*     */             try {
/* 341 */               ((X509Certificate)cert).checkValidity(now);
/*     */             } catch (CertificateExpiredException|CertificateNotYetValidException e) {
/* 343 */               String msg = sm.getString("jsseUtil.trustedCertNotValid", new Object[] { alias, ((X509Certificate)cert)
/* 344 */                 .getSubjectDN(), e.getMessage() });
/* 345 */               if (log.isDebugEnabled()) {
/* 346 */                 log.debug(msg, e);
/*     */               } else {
/* 348 */                 log.warn(msg);
/*     */               }
/*     */               
/*     */             }
/* 352 */           } else if (log.isDebugEnabled()) {
/* 353 */             log.debug(sm.getString("jsseUtil.trustedCertNotChecked", new Object[] { alias }));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void configureSessionContext(SSLSessionContext sslSessionContext)
/*     */   {
/* 364 */     sslSessionContext.setSessionCacheSize(this.sslHostConfig.getSessionCacheSize());
/* 365 */     sslSessionContext.setSessionTimeout(this.sslHostConfig.getSessionTimeout());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CertPathParameters getParameters(String crlf, KeyStore trustStore, boolean revocationEnabled)
/*     */     throws Exception
/*     */   {
/* 385 */     PKIXBuilderParameters xparams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
/*     */     
/* 387 */     if ((crlf != null) && (crlf.length() > 0)) {
/* 388 */       Collection<? extends CRL> crls = getCRLs(crlf);
/* 389 */       CertStoreParameters csp = new CollectionCertStoreParameters(crls);
/* 390 */       CertStore store = CertStore.getInstance("Collection", csp);
/* 391 */       xparams.addCertStore(store);
/* 392 */       xparams.setRevocationEnabled(true);
/*     */     } else {
/* 394 */       xparams.setRevocationEnabled(revocationEnabled);
/*     */     }
/* 396 */     xparams.setMaxPathLength(this.sslHostConfig.getCertificateVerificationDepth());
/* 397 */     return xparams;
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
/*     */   protected Collection<? extends CRL> getCRLs(String crlf)
/*     */     throws IOException, CRLException, CertificateException
/*     */   {
/* 412 */     Collection<? extends CRL> crls = null;
/*     */     try {
/* 414 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 415 */       InputStream is = ConfigFileLoader.getInputStream(crlf);Throwable localThrowable3 = null;
/* 416 */       try { crls = cf.generateCRLs(is);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 415 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */       } finally {
/* 417 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/*     */       }
/* 419 */     } catch (IOException iex) { throw iex;
/*     */     } catch (CRLException crle) {
/* 421 */       throw crle;
/*     */     } catch (CertificateException ce) {
/* 423 */       throw ce;
/*     */     }
/* 425 */     return crls;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\jsse\JSSEUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */