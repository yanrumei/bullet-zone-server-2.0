/*     */ package org.apache.coyote.http11;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpUpgradeHandler;
/*     */ import org.apache.coyote.AbstractProtocol;
/*     */ import org.apache.coyote.AbstractProtocol.ConnectionHandler;
/*     */ import org.apache.coyote.CompressionConfig;
/*     */ import org.apache.coyote.Processor;
/*     */ import org.apache.coyote.UpgradeProtocol;
/*     */ import org.apache.coyote.UpgradeToken;
/*     */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*     */ import org.apache.coyote.http11.upgrade.UpgradeProcessorExternal;
/*     */ import org.apache.coyote.http11.upgrade.UpgradeProcessorInternal;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.buf.StringUtils;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig;
/*     */ import org.apache.tomcat.util.net.SSLHostConfig.CertificateVerification;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public abstract class AbstractHttp11Protocol<S>
/*     */   extends AbstractProtocol<S>
/*     */ {
/*  48 */   protected static final StringManager sm = StringManager.getManager(AbstractHttp11Protocol.class);
/*     */   
/*  50 */   private final CompressionConfig compressionConfig = new CompressionConfig();
/*     */   
/*     */   public AbstractHttp11Protocol(AbstractEndpoint<S> endpoint)
/*     */   {
/*  54 */     super(endpoint);
/*  55 */     setConnectionTimeout(60000);
/*  56 */     AbstractProtocol.ConnectionHandler<S> cHandler = new AbstractProtocol.ConnectionHandler(this);
/*  57 */     setHandler(cHandler);
/*  58 */     getEndpoint().setHandler(cHandler);
/*     */   }
/*     */   
/*     */   public void init()
/*     */     throws Exception
/*     */   {
/*  64 */     for (UpgradeProtocol upgradeProtocol : this.upgradeProtocols) {
/*  65 */       configureUpgradeProtocol(upgradeProtocol);
/*     */     }
/*     */     
/*  68 */     super.init();
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getProtocolName()
/*     */   {
/*  74 */     return "Http";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractEndpoint<S> getEndpoint()
/*     */   {
/*  85 */     return super.getEndpoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   private boolean allowHostHeaderMismatch = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAllowHostHeaderMismatch()
/*     */   {
/* 101 */     return this.allowHostHeaderMismatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowHostHeaderMismatch(boolean allowHostHeaderMismatch)
/*     */   {
/* 111 */     this.allowHostHeaderMismatch = allowHostHeaderMismatch;
/*     */   }
/*     */   
/*     */ 
/* 115 */   private boolean rejectIllegalHeaderName = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getRejectIllegalHeaderName()
/*     */   {
/* 124 */     return this.rejectIllegalHeaderName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRejectIllegalHeaderName(boolean rejectIllegalHeaderName)
/*     */   {
/* 135 */     this.rejectIllegalHeaderName = rejectIllegalHeaderName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */   private int maxSavePostSize = 4096;
/* 144 */   public int getMaxSavePostSize() { return this.maxSavePostSize; }
/* 145 */   public void setMaxSavePostSize(int valueI) { this.maxSavePostSize = valueI; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */   private int maxHttpHeaderSize = 8192;
/* 152 */   public int getMaxHttpHeaderSize() { return this.maxHttpHeaderSize; }
/* 153 */   public void setMaxHttpHeaderSize(int valueI) { this.maxHttpHeaderSize = valueI; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */   private int connectionUploadTimeout = 300000;
/* 161 */   public int getConnectionUploadTimeout() { return this.connectionUploadTimeout; }
/*     */   
/* 163 */   public void setConnectionUploadTimeout(int i) { this.connectionUploadTimeout = i; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */   private boolean disableUploadTimeout = true;
/* 172 */   public boolean getDisableUploadTimeout() { return this.disableUploadTimeout; }
/*     */   
/* 174 */   public void setDisableUploadTimeout(boolean isDisabled) { this.disableUploadTimeout = isDisabled; }
/*     */   
/*     */ 
/*     */   public String getCompression()
/*     */   {
/* 179 */     return this.compressionConfig.getCompression();
/*     */   }
/*     */   
/* 182 */   public void setCompression(String valueS) { this.compressionConfig.setCompression(valueS); }
/*     */   
/*     */ 
/*     */   public String getNoCompressionUserAgents()
/*     */   {
/* 187 */     return this.compressionConfig.getNoCompressionUserAgents();
/*     */   }
/*     */   
/* 190 */   public void setNoCompressionUserAgents(String valueS) { this.compressionConfig.setNoCompressionUserAgents(valueS); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getCompressableMimeType()
/*     */   {
/* 200 */     return getCompressibleMimeType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setCompressableMimeType(String valueS)
/*     */   {
/* 208 */     setCompressibleMimeType(valueS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String[] getCompressableMimeTypes()
/*     */   {
/* 216 */     return getCompressibleMimeTypes();
/*     */   }
/*     */   
/*     */   public String getCompressibleMimeType()
/*     */   {
/* 221 */     return this.compressionConfig.getCompressibleMimeType();
/*     */   }
/*     */   
/* 224 */   public void setCompressibleMimeType(String valueS) { this.compressionConfig.setCompressibleMimeType(valueS); }
/*     */   
/*     */   public String[] getCompressibleMimeTypes() {
/* 227 */     return this.compressionConfig.getCompressibleMimeTypes();
/*     */   }
/*     */   
/*     */   public int getCompressionMinSize()
/*     */   {
/* 232 */     return this.compressionConfig.getCompressionMinSize();
/*     */   }
/*     */   
/* 235 */   public void setCompressionMinSize(int valueI) { this.compressionConfig.setCompressionMinSize(valueI); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 243 */   private String restrictedUserAgents = null;
/* 244 */   public String getRestrictedUserAgents() { return this.restrictedUserAgents; }
/*     */   
/* 246 */   public void setRestrictedUserAgents(String valueS) { this.restrictedUserAgents = valueS; }
/*     */   
/*     */ 
/*     */ 
/*     */   private String server;
/*     */   
/*     */   public String getServer()
/*     */   {
/* 254 */     return this.server; }
/*     */   
/* 256 */   public void setServer(String server) { this.server = server; }
/*     */   
/*     */ 
/*     */ 
/* 260 */   private boolean serverRemoveAppProvidedValues = false;
/* 261 */   public boolean getServerRemoveAppProvidedValues() { return this.serverRemoveAppProvidedValues; }
/*     */   
/* 263 */   public void setServerRemoveAppProvidedValues(boolean serverRemoveAppProvidedValues) { this.serverRemoveAppProvidedValues = serverRemoveAppProvidedValues; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 270 */   private int maxTrailerSize = 8192;
/* 271 */   public int getMaxTrailerSize() { return this.maxTrailerSize; }
/*     */   
/* 273 */   public void setMaxTrailerSize(int maxTrailerSize) { this.maxTrailerSize = maxTrailerSize; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 280 */   private int maxExtensionSize = 8192;
/* 281 */   public int getMaxExtensionSize() { return this.maxExtensionSize; }
/*     */   
/* 283 */   public void setMaxExtensionSize(int maxExtensionSize) { this.maxExtensionSize = maxExtensionSize; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 290 */   private int maxSwallowSize = 2097152;
/* 291 */   public int getMaxSwallowSize() { return this.maxSwallowSize; }
/*     */   
/* 293 */   public void setMaxSwallowSize(int maxSwallowSize) { this.maxSwallowSize = maxSwallowSize; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean secure;
/*     */   
/*     */ 
/*     */   public boolean getSecure()
/*     */   {
/* 303 */     return this.secure; }
/*     */   
/* 305 */   public void setSecure(boolean b) { this.secure = b; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 314 */   private Set<String> allowedTrailerHeaders = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */   
/*     */   public void setAllowedTrailerHeaders(String commaSeparatedHeaders)
/*     */   {
/* 318 */     Set<String> toRemove = new HashSet();
/* 319 */     toRemove.addAll(this.allowedTrailerHeaders);
/* 320 */     if (commaSeparatedHeaders != null) {
/* 321 */       String[] headers = commaSeparatedHeaders.split(",");
/* 322 */       for (String header : headers) {
/* 323 */         String trimmedHeader = header.trim().toLowerCase(Locale.ENGLISH);
/* 324 */         if (toRemove.contains(trimmedHeader)) {
/* 325 */           toRemove.remove(trimmedHeader);
/*     */         } else {
/* 327 */           this.allowedTrailerHeaders.add(trimmedHeader);
/*     */         }
/*     */       }
/* 330 */       this.allowedTrailerHeaders.removeAll(toRemove);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAllowedTrailerHeaders()
/*     */   {
/* 336 */     List<String> copy = new ArrayList(this.allowedTrailerHeaders.size());
/* 337 */     copy.addAll(this.allowedTrailerHeaders);
/* 338 */     return StringUtils.join(copy);
/*     */   }
/*     */   
/* 341 */   public void addAllowedTrailerHeader(String header) { if (header != null)
/* 342 */       this.allowedTrailerHeaders.add(header.trim().toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */   
/*     */   public void removeAllowedTrailerHeader(String header) {
/* 346 */     if (header != null) {
/* 347 */       this.allowedTrailerHeaders.remove(header.trim().toLowerCase(Locale.ENGLISH));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 355 */   private final List<UpgradeProtocol> upgradeProtocols = new ArrayList();
/*     */   
/*     */   public void addUpgradeProtocol(UpgradeProtocol upgradeProtocol) {
/* 358 */     this.upgradeProtocols.add(upgradeProtocol);
/*     */   }
/*     */   
/*     */   public UpgradeProtocol[] findUpgradeProtocols() {
/* 362 */     return (UpgradeProtocol[])this.upgradeProtocols.toArray(new UpgradeProtocol[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 369 */   private final Map<String, UpgradeProtocol> httpUpgradeProtocols = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 374 */   private final Map<String, UpgradeProtocol> negotiatedProtocols = new HashMap();
/*     */   
/*     */   private void configureUpgradeProtocol(UpgradeProtocol upgradeProtocol) {
/* 377 */     String httpUpgradeName = upgradeProtocol.getHttpUpgradeName(getEndpoint().isSSLEnabled());
/* 378 */     boolean httpUpgradeConfigured = false;
/* 379 */     if ((httpUpgradeName != null) && (httpUpgradeName.length() > 0)) {
/* 380 */       this.httpUpgradeProtocols.put(httpUpgradeName, upgradeProtocol);
/* 381 */       httpUpgradeConfigured = true;
/* 382 */       getLog().info(sm.getString("abstractHttp11Protocol.httpUpgradeConfigured", new Object[] {
/* 383 */         getName(), httpUpgradeName }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 388 */     String alpnName = upgradeProtocol.getAlpnName();
/* 389 */     if ((alpnName != null) && (alpnName.length() > 0)) {
/* 390 */       if (getEndpoint().isAlpnSupported()) {
/* 391 */         this.negotiatedProtocols.put(alpnName, upgradeProtocol);
/* 392 */         getEndpoint().addNegotiatedProtocol(alpnName);
/* 393 */         getLog().info(sm.getString("abstractHttp11Protocol.alpnConfigured", new Object[] {
/* 394 */           getName(), alpnName }));
/*     */       }
/* 396 */       else if (!httpUpgradeConfigured)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 401 */         getLog().error(sm.getString("abstractHttp11Protocol.alpnWithNoAlpn", new Object[] {upgradeProtocol
/* 402 */           .getClass().getName(), alpnName, getName() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public UpgradeProtocol getNegotiatedProtocol(String negotiatedName)
/*     */   {
/* 409 */     return (UpgradeProtocol)this.negotiatedProtocols.get(negotiatedName);
/*     */   }
/*     */   
/*     */   public UpgradeProtocol getUpgradeProtocol(String upgradedName) {
/* 413 */     return (UpgradeProtocol)this.httpUpgradeProtocols.get(upgradedName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 420 */   public boolean isSSLEnabled() { return getEndpoint().isSSLEnabled(); }
/*     */   
/* 422 */   public void setSSLEnabled(boolean SSLEnabled) { getEndpoint().setSSLEnabled(SSLEnabled); }
/*     */   
/*     */ 
/*     */ 
/* 426 */   public boolean getUseSendfile() { return getEndpoint().getUseSendfile(); }
/* 427 */   public void setUseSendfile(boolean useSendfile) { getEndpoint().setUseSendfile(useSendfile); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxKeepAliveRequests()
/*     */   {
/* 436 */     return getEndpoint().getMaxKeepAliveRequests();
/*     */   }
/*     */   
/* 439 */   public void setMaxKeepAliveRequests(int mkar) { getEndpoint().setMaxKeepAliveRequests(mkar); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 447 */   public String getDefaultSSLHostConfigName() { return getEndpoint().getDefaultSSLHostConfigName(); }
/*     */   
/*     */   public void setDefaultSSLHostConfigName(String defaultSSLHostConfigName) {
/* 450 */     getEndpoint().setDefaultSSLHostConfigName(defaultSSLHostConfigName);
/* 451 */     if (this.defaultSSLHostConfig != null) {
/* 452 */       this.defaultSSLHostConfig.setHostName(defaultSSLHostConfigName);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addSslHostConfig(SSLHostConfig sslHostConfig)
/*     */   {
/* 459 */     getEndpoint().addSslHostConfig(sslHostConfig);
/*     */   }
/*     */   
/*     */   public SSLHostConfig[] findSslHostConfigs()
/*     */   {
/* 464 */     return getEndpoint().findSslHostConfigs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 470 */   private SSLHostConfig defaultSSLHostConfig = null;
/*     */   
/* 472 */   private void registerDefaultSSLHostConfig() { if (this.defaultSSLHostConfig == null) {
/* 473 */       for (SSLHostConfig sslHostConfig : findSslHostConfigs()) {
/* 474 */         if (getDefaultSSLHostConfigName().equals(sslHostConfig.getHostName())) {
/* 475 */           this.defaultSSLHostConfig = sslHostConfig;
/* 476 */           break;
/*     */         }
/*     */       }
/* 479 */       if (this.defaultSSLHostConfig == null) {
/* 480 */         this.defaultSSLHostConfig = new SSLHostConfig();
/* 481 */         this.defaultSSLHostConfig.setHostName(getDefaultSSLHostConfigName());
/* 482 */         getEndpoint().addSslHostConfig(this.defaultSSLHostConfig);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSslEnabledProtocols()
/*     */   {
/* 492 */     registerDefaultSSLHostConfig();
/* 493 */     return StringUtils.join(this.defaultSSLHostConfig.getEnabledProtocols());
/*     */   }
/*     */   
/* 496 */   public void setSslEnabledProtocols(String enabledProtocols) { registerDefaultSSLHostConfig();
/* 497 */     this.defaultSSLHostConfig.setProtocols(enabledProtocols);
/*     */   }
/*     */   
/* 500 */   public String getSSLProtocol() { registerDefaultSSLHostConfig();
/* 501 */     return StringUtils.join(this.defaultSSLHostConfig.getEnabledProtocols());
/*     */   }
/*     */   
/* 504 */   public void setSSLProtocol(String sslProtocol) { registerDefaultSSLHostConfig();
/* 505 */     this.defaultSSLHostConfig.setProtocols(sslProtocol);
/*     */   }
/*     */   
/*     */   public String getKeystoreFile()
/*     */   {
/* 510 */     registerDefaultSSLHostConfig();
/* 511 */     return this.defaultSSLHostConfig.getCertificateKeystoreFile();
/*     */   }
/*     */   
/* 514 */   public void setKeystoreFile(String keystoreFile) { registerDefaultSSLHostConfig();
/* 515 */     this.defaultSSLHostConfig.setCertificateKeystoreFile(keystoreFile);
/*     */   }
/*     */   
/* 518 */   public String getSSLCertificateChainFile() { registerDefaultSSLHostConfig();
/* 519 */     return this.defaultSSLHostConfig.getCertificateChainFile();
/*     */   }
/*     */   
/* 522 */   public void setSSLCertificateChainFile(String certificateChainFile) { registerDefaultSSLHostConfig();
/* 523 */     this.defaultSSLHostConfig.setCertificateChainFile(certificateChainFile);
/*     */   }
/*     */   
/* 526 */   public String getSSLCertificateFile() { registerDefaultSSLHostConfig();
/* 527 */     return this.defaultSSLHostConfig.getCertificateFile();
/*     */   }
/*     */   
/* 530 */   public void setSSLCertificateFile(String certificateFile) { registerDefaultSSLHostConfig();
/* 531 */     this.defaultSSLHostConfig.setCertificateFile(certificateFile);
/*     */   }
/*     */   
/* 534 */   public String getSSLCertificateKeyFile() { registerDefaultSSLHostConfig();
/* 535 */     return this.defaultSSLHostConfig.getCertificateKeyFile();
/*     */   }
/*     */   
/* 538 */   public void setSSLCertificateKeyFile(String certificateKeyFile) { registerDefaultSSLHostConfig();
/* 539 */     this.defaultSSLHostConfig.setCertificateKeyFile(certificateKeyFile);
/*     */   }
/*     */   
/*     */   public String getAlgorithm()
/*     */   {
/* 544 */     registerDefaultSSLHostConfig();
/* 545 */     return this.defaultSSLHostConfig.getKeyManagerAlgorithm();
/*     */   }
/*     */   
/* 548 */   public void setAlgorithm(String keyManagerAlgorithm) { registerDefaultSSLHostConfig();
/* 549 */     this.defaultSSLHostConfig.setKeyManagerAlgorithm(keyManagerAlgorithm);
/*     */   }
/*     */   
/*     */   public String getClientAuth()
/*     */   {
/* 554 */     registerDefaultSSLHostConfig();
/* 555 */     return this.defaultSSLHostConfig.getCertificateVerification().toString();
/*     */   }
/*     */   
/* 558 */   public void setClientAuth(String certificateVerification) { registerDefaultSSLHostConfig();
/* 559 */     this.defaultSSLHostConfig.setCertificateVerification(certificateVerification);
/*     */   }
/*     */   
/*     */   public String getSSLVerifyClient()
/*     */   {
/* 564 */     registerDefaultSSLHostConfig();
/* 565 */     return this.defaultSSLHostConfig.getCertificateVerification().toString();
/*     */   }
/*     */   
/* 568 */   public void setSSLVerifyClient(String certificateVerification) { registerDefaultSSLHostConfig();
/* 569 */     this.defaultSSLHostConfig.setCertificateVerification(certificateVerification);
/*     */   }
/*     */   
/*     */   public int getTrustMaxCertLength()
/*     */   {
/* 574 */     registerDefaultSSLHostConfig();
/* 575 */     return this.defaultSSLHostConfig.getCertificateVerificationDepth();
/*     */   }
/*     */   
/* 578 */   public void setTrustMaxCertLength(int certificateVerificationDepth) { registerDefaultSSLHostConfig();
/* 579 */     this.defaultSSLHostConfig.setCertificateVerificationDepth(certificateVerificationDepth);
/*     */   }
/*     */   
/* 582 */   public int getSSLVerifyDepth() { registerDefaultSSLHostConfig();
/* 583 */     return this.defaultSSLHostConfig.getCertificateVerificationDepth();
/*     */   }
/*     */   
/* 586 */   public void setSSLVerifyDepth(int certificateVerificationDepth) { registerDefaultSSLHostConfig();
/* 587 */     this.defaultSSLHostConfig.setCertificateVerificationDepth(certificateVerificationDepth);
/*     */   }
/*     */   
/*     */   public String getUseServerCipherSuitesOrder()
/*     */   {
/* 592 */     registerDefaultSSLHostConfig();
/* 593 */     return this.defaultSSLHostConfig.getHonorCipherOrder();
/*     */   }
/*     */   
/* 596 */   public void setUseServerCipherSuitesOrder(String honorCipherOrder) { registerDefaultSSLHostConfig();
/* 597 */     this.defaultSSLHostConfig.setHonorCipherOrder(honorCipherOrder);
/*     */   }
/*     */   
/* 600 */   public String getSSLHonorCipherOrder() { registerDefaultSSLHostConfig();
/* 601 */     return this.defaultSSLHostConfig.getHonorCipherOrder();
/*     */   }
/*     */   
/* 604 */   public void setSSLHonorCipherOrder(String honorCipherOrder) { registerDefaultSSLHostConfig();
/* 605 */     this.defaultSSLHostConfig.setHonorCipherOrder(honorCipherOrder);
/*     */   }
/*     */   
/*     */   public String getCiphers()
/*     */   {
/* 610 */     registerDefaultSSLHostConfig();
/* 611 */     return this.defaultSSLHostConfig.getCiphers();
/*     */   }
/*     */   
/* 614 */   public void setCiphers(String ciphers) { registerDefaultSSLHostConfig();
/* 615 */     this.defaultSSLHostConfig.setCiphers(ciphers);
/*     */   }
/*     */   
/* 618 */   public String getSSLCipherSuite() { registerDefaultSSLHostConfig();
/* 619 */     return this.defaultSSLHostConfig.getCiphers();
/*     */   }
/*     */   
/* 622 */   public void setSSLCipherSuite(String ciphers) { registerDefaultSSLHostConfig();
/* 623 */     this.defaultSSLHostConfig.setCiphers(ciphers);
/*     */   }
/*     */   
/*     */   public String getKeystorePass()
/*     */   {
/* 628 */     registerDefaultSSLHostConfig();
/* 629 */     return this.defaultSSLHostConfig.getCertificateKeystorePassword();
/*     */   }
/*     */   
/* 632 */   public void setKeystorePass(String certificateKeystorePassword) { registerDefaultSSLHostConfig();
/* 633 */     this.defaultSSLHostConfig.setCertificateKeystorePassword(certificateKeystorePassword);
/*     */   }
/*     */   
/*     */   public String getKeyPass()
/*     */   {
/* 638 */     registerDefaultSSLHostConfig();
/* 639 */     return this.defaultSSLHostConfig.getCertificateKeyPassword();
/*     */   }
/*     */   
/* 642 */   public void setKeyPass(String certificateKeyPassword) { registerDefaultSSLHostConfig();
/* 643 */     this.defaultSSLHostConfig.setCertificateKeyPassword(certificateKeyPassword);
/*     */   }
/*     */   
/* 646 */   public String getSSLPassword() { registerDefaultSSLHostConfig();
/* 647 */     return this.defaultSSLHostConfig.getCertificateKeyPassword();
/*     */   }
/*     */   
/* 650 */   public void setSSLPassword(String certificateKeyPassword) { registerDefaultSSLHostConfig();
/* 651 */     this.defaultSSLHostConfig.setCertificateKeyPassword(certificateKeyPassword);
/*     */   }
/*     */   
/*     */   public String getCrlFile()
/*     */   {
/* 656 */     registerDefaultSSLHostConfig();
/* 657 */     return this.defaultSSLHostConfig.getCertificateRevocationListFile();
/*     */   }
/*     */   
/* 660 */   public void setCrlFile(String certificateRevocationListFile) { registerDefaultSSLHostConfig();
/* 661 */     this.defaultSSLHostConfig.setCertificateRevocationListFile(certificateRevocationListFile);
/*     */   }
/*     */   
/* 664 */   public String getSSLCARevocationFile() { registerDefaultSSLHostConfig();
/* 665 */     return this.defaultSSLHostConfig.getCertificateRevocationListFile();
/*     */   }
/*     */   
/* 668 */   public void setSSLCARevocationFile(String certificateRevocationListFile) { registerDefaultSSLHostConfig();
/* 669 */     this.defaultSSLHostConfig.setCertificateRevocationListFile(certificateRevocationListFile);
/*     */   }
/*     */   
/* 672 */   public String getSSLCARevocationPath() { registerDefaultSSLHostConfig();
/* 673 */     return this.defaultSSLHostConfig.getCertificateRevocationListPath();
/*     */   }
/*     */   
/* 676 */   public void setSSLCARevocationPath(String certificateRevocationListPath) { registerDefaultSSLHostConfig();
/* 677 */     this.defaultSSLHostConfig.setCertificateRevocationListPath(certificateRevocationListPath);
/*     */   }
/*     */   
/*     */   public String getKeystoreType()
/*     */   {
/* 682 */     registerDefaultSSLHostConfig();
/* 683 */     return this.defaultSSLHostConfig.getCertificateKeystoreType();
/*     */   }
/*     */   
/* 686 */   public void setKeystoreType(String certificateKeystoreType) { registerDefaultSSLHostConfig();
/* 687 */     this.defaultSSLHostConfig.setCertificateKeystoreType(certificateKeystoreType);
/*     */   }
/*     */   
/*     */   public String getKeystoreProvider()
/*     */   {
/* 692 */     registerDefaultSSLHostConfig();
/* 693 */     return this.defaultSSLHostConfig.getCertificateKeystoreProvider();
/*     */   }
/*     */   
/* 696 */   public void setKeystoreProvider(String certificateKeystoreProvider) { registerDefaultSSLHostConfig();
/* 697 */     this.defaultSSLHostConfig.setCertificateKeystoreProvider(certificateKeystoreProvider);
/*     */   }
/*     */   
/*     */   public String getKeyAlias()
/*     */   {
/* 702 */     registerDefaultSSLHostConfig();
/* 703 */     return this.defaultSSLHostConfig.getCertificateKeyAlias();
/*     */   }
/*     */   
/* 706 */   public void setKeyAlias(String certificateKeyAlias) { registerDefaultSSLHostConfig();
/* 707 */     this.defaultSSLHostConfig.setCertificateKeyAlias(certificateKeyAlias);
/*     */   }
/*     */   
/*     */   public String getTruststoreAlgorithm()
/*     */   {
/* 712 */     registerDefaultSSLHostConfig();
/* 713 */     return this.defaultSSLHostConfig.getTruststoreAlgorithm();
/*     */   }
/*     */   
/* 716 */   public void setTruststoreAlgorithm(String truststoreAlgorithm) { registerDefaultSSLHostConfig();
/* 717 */     this.defaultSSLHostConfig.setTruststoreAlgorithm(truststoreAlgorithm);
/*     */   }
/*     */   
/*     */   public String getTruststoreFile()
/*     */   {
/* 722 */     registerDefaultSSLHostConfig();
/* 723 */     return this.defaultSSLHostConfig.getTruststoreFile();
/*     */   }
/*     */   
/* 726 */   public void setTruststoreFile(String truststoreFile) { registerDefaultSSLHostConfig();
/* 727 */     this.defaultSSLHostConfig.setTruststoreFile(truststoreFile);
/*     */   }
/*     */   
/*     */   public String getTruststorePass()
/*     */   {
/* 732 */     registerDefaultSSLHostConfig();
/* 733 */     return this.defaultSSLHostConfig.getTruststorePassword();
/*     */   }
/*     */   
/* 736 */   public void setTruststorePass(String truststorePassword) { registerDefaultSSLHostConfig();
/* 737 */     this.defaultSSLHostConfig.setTruststorePassword(truststorePassword);
/*     */   }
/*     */   
/*     */   public String getTruststoreType()
/*     */   {
/* 742 */     registerDefaultSSLHostConfig();
/* 743 */     return this.defaultSSLHostConfig.getTruststoreType();
/*     */   }
/*     */   
/* 746 */   public void setTruststoreType(String truststoreType) { registerDefaultSSLHostConfig();
/* 747 */     this.defaultSSLHostConfig.setTruststoreType(truststoreType);
/*     */   }
/*     */   
/*     */   public String getTruststoreProvider()
/*     */   {
/* 752 */     registerDefaultSSLHostConfig();
/* 753 */     return this.defaultSSLHostConfig.getTruststoreProvider();
/*     */   }
/*     */   
/* 756 */   public void setTruststoreProvider(String truststoreProvider) { registerDefaultSSLHostConfig();
/* 757 */     this.defaultSSLHostConfig.setTruststoreProvider(truststoreProvider);
/*     */   }
/*     */   
/*     */   public String getSslProtocol()
/*     */   {
/* 762 */     registerDefaultSSLHostConfig();
/* 763 */     return this.defaultSSLHostConfig.getSslProtocol();
/*     */   }
/*     */   
/* 766 */   public void setSslProtocol(String sslProtocol) { registerDefaultSSLHostConfig();
/* 767 */     this.defaultSSLHostConfig.setSslProtocol(sslProtocol);
/*     */   }
/*     */   
/*     */   public int getSessionCacheSize()
/*     */   {
/* 772 */     registerDefaultSSLHostConfig();
/* 773 */     return this.defaultSSLHostConfig.getSessionCacheSize();
/*     */   }
/*     */   
/* 776 */   public void setSessionCacheSize(int sessionCacheSize) { registerDefaultSSLHostConfig();
/* 777 */     this.defaultSSLHostConfig.setSessionCacheSize(sessionCacheSize);
/*     */   }
/*     */   
/*     */   public int getSessionTimeout()
/*     */   {
/* 782 */     registerDefaultSSLHostConfig();
/* 783 */     return this.defaultSSLHostConfig.getSessionTimeout();
/*     */   }
/*     */   
/* 786 */   public void setSessionTimeout(int sessionTimeout) { registerDefaultSSLHostConfig();
/* 787 */     this.defaultSSLHostConfig.setSessionTimeout(sessionTimeout);
/*     */   }
/*     */   
/*     */   public String getSSLCACertificatePath()
/*     */   {
/* 792 */     registerDefaultSSLHostConfig();
/* 793 */     return this.defaultSSLHostConfig.getCaCertificatePath();
/*     */   }
/*     */   
/* 796 */   public void setSSLCACertificatePath(String caCertificatePath) { registerDefaultSSLHostConfig();
/* 797 */     this.defaultSSLHostConfig.setCaCertificatePath(caCertificatePath);
/*     */   }
/*     */   
/*     */   public String getSSLCACertificateFile()
/*     */   {
/* 802 */     registerDefaultSSLHostConfig();
/* 803 */     return this.defaultSSLHostConfig.getCaCertificateFile();
/*     */   }
/*     */   
/* 806 */   public void setSSLCACertificateFile(String caCertificateFile) { registerDefaultSSLHostConfig();
/* 807 */     this.defaultSSLHostConfig.setCaCertificateFile(caCertificateFile);
/*     */   }
/*     */   
/*     */   public boolean getSSLDisableCompression()
/*     */   {
/* 812 */     registerDefaultSSLHostConfig();
/* 813 */     return this.defaultSSLHostConfig.getDisableCompression();
/*     */   }
/*     */   
/* 816 */   public void setSSLDisableCompression(boolean disableCompression) { registerDefaultSSLHostConfig();
/* 817 */     this.defaultSSLHostConfig.setDisableCompression(disableCompression);
/*     */   }
/*     */   
/*     */   public boolean getSSLDisableSessionTickets()
/*     */   {
/* 822 */     registerDefaultSSLHostConfig();
/* 823 */     return this.defaultSSLHostConfig.getDisableSessionTickets();
/*     */   }
/*     */   
/* 826 */   public void setSSLDisableSessionTickets(boolean disableSessionTickets) { registerDefaultSSLHostConfig();
/* 827 */     this.defaultSSLHostConfig.setDisableSessionTickets(disableSessionTickets);
/*     */   }
/*     */   
/*     */   public String getTrustManagerClassName()
/*     */   {
/* 832 */     registerDefaultSSLHostConfig();
/* 833 */     return this.defaultSSLHostConfig.getTrustManagerClassName();
/*     */   }
/*     */   
/* 836 */   public void setTrustManagerClassName(String trustManagerClassName) { registerDefaultSSLHostConfig();
/* 837 */     this.defaultSSLHostConfig.setTrustManagerClassName(trustManagerClassName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Processor createProcessor()
/*     */   {
/* 849 */     Http11Processor processor = new Http11Processor(getMaxHttpHeaderSize(), getAllowHostHeaderMismatch(), getRejectIllegalHeaderName(), getEndpoint(), getMaxTrailerSize(), this.allowedTrailerHeaders, getMaxExtensionSize(), getMaxSwallowSize(), this.httpUpgradeProtocols, getSendReasonPhrase());
/* 850 */     processor.setAdapter(getAdapter());
/* 851 */     processor.setMaxKeepAliveRequests(getMaxKeepAliveRequests());
/* 852 */     processor.setConnectionUploadTimeout(getConnectionUploadTimeout());
/* 853 */     processor.setDisableUploadTimeout(getDisableUploadTimeout());
/* 854 */     processor.setCompressionMinSize(getCompressionMinSize());
/* 855 */     processor.setCompression(getCompression());
/* 856 */     processor.setNoCompressionUserAgents(getNoCompressionUserAgents());
/* 857 */     processor.setCompressibleMimeTypes(getCompressibleMimeTypes());
/* 858 */     processor.setRestrictedUserAgents(getRestrictedUserAgents());
/* 859 */     processor.setMaxSavePostSize(getMaxSavePostSize());
/* 860 */     processor.setServer(getServer());
/* 861 */     processor.setServerRemoveAppProvidedValues(getServerRemoveAppProvidedValues());
/* 862 */     return processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Processor createUpgradeProcessor(SocketWrapperBase<?> socket, UpgradeToken upgradeToken)
/*     */   {
/* 870 */     HttpUpgradeHandler httpUpgradeHandler = upgradeToken.getHttpUpgradeHandler();
/* 871 */     if ((httpUpgradeHandler instanceof InternalHttpUpgradeHandler)) {
/* 872 */       return new UpgradeProcessorInternal(socket, upgradeToken);
/*     */     }
/* 874 */     return new UpgradeProcessorExternal(socket, upgradeToken);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\AbstractHttp11Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */