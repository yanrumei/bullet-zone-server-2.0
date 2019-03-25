/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.NetworkChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
/*     */ import org.apache.tomcat.util.net.openssl.OpenSSLImplementation;
/*     */ import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
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
/*     */ public abstract class AbstractJsseEndpoint<S>
/*     */   extends AbstractEndpoint<S>
/*     */ {
/*  40 */   private String sslImplementationName = null;
/*  41 */   private int sniParseLimit = 65536;
/*     */   
/*  43 */   private SSLImplementation sslImplementation = null;
/*     */   
/*     */   public String getSslImplementationName() {
/*  46 */     return this.sslImplementationName;
/*     */   }
/*     */   
/*     */   public void setSslImplementationName(String s)
/*     */   {
/*  51 */     this.sslImplementationName = s;
/*     */   }
/*     */   
/*     */   public SSLImplementation getSslImplementation()
/*     */   {
/*  56 */     return this.sslImplementation;
/*     */   }
/*     */   
/*     */   public int getSniParseLimit()
/*     */   {
/*  61 */     return this.sniParseLimit;
/*     */   }
/*     */   
/*     */   public void setSniParseLimit(int sniParseLimit)
/*     */   {
/*  66 */     this.sniParseLimit = sniParseLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SSLHostConfig.Type getSslConfigType()
/*     */   {
/*  73 */     if (OpenSSLImplementation.class.getName().equals(this.sslImplementationName)) {
/*  74 */       return SSLHostConfig.Type.EITHER;
/*     */     }
/*  76 */     return SSLHostConfig.Type.JSSE;
/*     */   }
/*     */   
/*     */   protected void initialiseSsl()
/*     */     throws Exception
/*     */   {
/*  82 */     if (isSSLEnabled()) {
/*  83 */       this.sslImplementation = SSLImplementation.getInstance(getSslImplementationName());
/*     */       
/*  85 */       for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/*  86 */         sslHostConfig.setConfigType(getSslConfigType());
/*  87 */         createSSLContext(sslHostConfig);
/*     */       }
/*     */       
/*     */ 
/*  91 */       if (this.sslHostConfigs.get(getDefaultSSLHostConfigName()) == null) {
/*  92 */         throw new IllegalArgumentException(sm.getString("endpoint.noSslHostConfig", new Object[] {
/*  93 */           getDefaultSSLHostConfigName(), getName() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void createSSLContext(SSLHostConfig sslHostConfig)
/*     */     throws IllegalArgumentException
/*     */   {
/* 102 */     boolean firstCertificate = true;
/* 103 */     for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
/* 104 */       SSLUtil sslUtil = this.sslImplementation.getSSLUtil(certificate);
/* 105 */       if (firstCertificate) {
/* 106 */         firstCertificate = false;
/* 107 */         sslHostConfig.setEnabledProtocols(sslUtil.getEnabledProtocols());
/* 108 */         sslHostConfig.setEnabledCiphers(sslUtil.getEnabledCiphers());
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 113 */         SSLContext sslContext = sslUtil.createSSLContext(this.negotiableProtocols);
/* 114 */         sslContext.init(sslUtil.getKeyManagers(), sslUtil.getTrustManagers(), null);
/*     */       } catch (Exception e) {
/* 116 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */       SSLContext sslContext;
/* 119 */       SSLSessionContext sessionContext = sslContext.getServerSessionContext();
/* 120 */       if (sessionContext != null) {
/* 121 */         sslUtil.configureSessionContext(sessionContext);
/*     */       }
/* 123 */       certificate.setSslContext(sslContext);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void destroySsl() throws Exception
/*     */   {
/* 129 */     if (isSSLEnabled()) {
/* 130 */       for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/* 131 */         releaseSSLContext(sslHostConfig);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void releaseSSLContext(SSLHostConfig sslHostConfig)
/*     */   {
/* 139 */     for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
/* 140 */       if (certificate.getSslContext() != null) {
/* 141 */         SSLContext sslContext = certificate.getSslContext();
/* 142 */         if (sslContext != null) {
/* 143 */           sslContext.destroy();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected SSLEngine createSSLEngine(String sniHostName, List<Cipher> clientRequestedCiphers, List<String> clientRequestedApplicationProtocols)
/*     */   {
/* 152 */     SSLHostConfig sslHostConfig = getSSLHostConfig(sniHostName);
/*     */     
/* 154 */     SSLHostConfigCertificate certificate = selectCertificate(sslHostConfig, clientRequestedCiphers);
/*     */     
/* 156 */     SSLContext sslContext = certificate.getSslContext();
/* 157 */     if (sslContext == null)
/*     */     {
/* 159 */       throw new IllegalStateException(sm.getString("endpoint.jsse.noSslContext", new Object[] { sniHostName }));
/*     */     }
/*     */     
/* 162 */     SSLEngine engine = sslContext.createSSLEngine();
/* 163 */     switch (sslHostConfig.getCertificateVerification()) {
/*     */     case NONE: 
/* 165 */       engine.setNeedClientAuth(false);
/* 166 */       engine.setWantClientAuth(false);
/* 167 */       break;
/*     */     case OPTIONAL: 
/*     */     case OPTIONAL_NO_CA: 
/* 170 */       engine.setWantClientAuth(true);
/* 171 */       break;
/*     */     case REQUIRED: 
/* 173 */       engine.setNeedClientAuth(true);
/*     */     }
/*     */     
/* 176 */     engine.setUseClientMode(false);
/* 177 */     engine.setEnabledCipherSuites(sslHostConfig.getEnabledCiphers());
/* 178 */     engine.setEnabledProtocols(sslHostConfig.getEnabledProtocols());
/*     */     
/* 180 */     String honorCipherOrderStr = sslHostConfig.getHonorCipherOrder();
/* 181 */     if (honorCipherOrderStr != null) {
/* 182 */       boolean honorCipherOrder = Boolean.parseBoolean(honorCipherOrderStr);
/* 183 */       JreCompat.getInstance().setUseServerCipherSuitesOrder(engine, honorCipherOrder);
/*     */     }
/*     */     
/* 186 */     if ((JreCompat.isJre9Available()) && (clientRequestedApplicationProtocols != null) && 
/* 187 */       (clientRequestedApplicationProtocols.size() > 0) && 
/* 188 */       (this.negotiableProtocols.size() > 0)) {
/* 189 */       SSLParameters sslParameters = engine.getSSLParameters();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 194 */       List<String> commonProtocols = new ArrayList();
/* 195 */       commonProtocols.addAll(this.negotiableProtocols);
/* 196 */       commonProtocols.retainAll(clientRequestedApplicationProtocols);
/* 197 */       if (commonProtocols.size() > 0) {
/* 198 */         String[] commonProtocolsArray = (String[])commonProtocols.toArray(new String[commonProtocols.size()]);
/* 199 */         JreCompat.getInstance().setApplicationProtocols(sslParameters, commonProtocolsArray);
/*     */       }
/*     */       
/*     */ 
/* 203 */       engine.setSSLParameters(sslParameters);
/*     */     }
/*     */     
/* 206 */     return engine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private SSLHostConfigCertificate selectCertificate(SSLHostConfig sslHostConfig, List<Cipher> clientCiphers)
/*     */   {
/* 213 */     Set<SSLHostConfigCertificate> certificates = sslHostConfig.getCertificates(true);
/* 214 */     if (certificates.size() == 1) {
/* 215 */       return (SSLHostConfigCertificate)certificates.iterator().next();
/*     */     }
/*     */     
/* 218 */     LinkedHashSet<Cipher> serverCiphers = sslHostConfig.getCipherList();
/*     */     
/* 220 */     List<Cipher> candidateCiphers = new ArrayList();
/* 221 */     if (Boolean.parseBoolean(sslHostConfig.getHonorCipherOrder())) {
/* 222 */       candidateCiphers.addAll(serverCiphers);
/* 223 */       candidateCiphers.retainAll(clientCiphers);
/*     */     } else {
/* 225 */       candidateCiphers.addAll(clientCiphers);
/* 226 */       candidateCiphers.retainAll(serverCiphers);
/*     */     }
/*     */     
/* 229 */     Iterator<Cipher> candidateIter = candidateCiphers.iterator();
/* 230 */     Cipher candidate; while (candidateIter.hasNext()) {
/* 231 */       candidate = (Cipher)candidateIter.next();
/* 232 */       for (SSLHostConfigCertificate certificate : certificates) {
/* 233 */         if (certificate.getType().isCompatibleWith(candidate.getAu())) {
/* 234 */           return certificate;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 241 */     return (SSLHostConfigCertificate)certificates.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAlpnSupported()
/*     */   {
/* 248 */     if (!isSSLEnabled()) {
/* 249 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 255 */       sslImplementation = SSLImplementation.getInstance(getSslImplementationName());
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/*     */       SSLImplementation sslImplementation;
/* 259 */       return false; }
/*     */     SSLImplementation sslImplementation;
/* 261 */     return sslImplementation.isAlpnSupported();
/*     */   }
/*     */   
/*     */   public void init()
/*     */     throws Exception
/*     */   {
/* 267 */     testServerCipherSuitesOrderSupport();
/* 268 */     super.init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void testServerCipherSuitesOrderSupport()
/*     */   {
/* 275 */     if ((!JreCompat.isJre8Available()) && 
/* 276 */       (!OpenSSLImplementation.class.getName().equals(getSslImplementationName()))) {
/* 277 */       for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/* 278 */         if (sslHostConfig.getHonorCipherOrder() != null)
/*     */         {
/* 280 */           throw new UnsupportedOperationException(sm.getString("endpoint.jsse.cannotHonorServerCipherOrder"));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void unbind()
/*     */     throws Exception
/*     */   {
/* 289 */     for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/* 290 */       for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
/* 291 */         certificate.setSslContext(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract NetworkChannel getServerSocket();
/*     */   
/*     */   protected final InetSocketAddress getLocalAddress()
/*     */     throws IOException
/*     */   {
/* 302 */     NetworkChannel serverSock = getServerSocket();
/* 303 */     if (serverSock == null) {
/* 304 */       return null;
/*     */     }
/* 306 */     SocketAddress sa = serverSock.getLocalAddress();
/* 307 */     if ((sa instanceof InetSocketAddress)) {
/* 308 */       return (InetSocketAddress)sa;
/*     */     }
/* 310 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\AbstractJsseEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */