/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.jni.Address;
/*      */ import org.apache.tomcat.jni.Error;
/*      */ import org.apache.tomcat.jni.File;
/*      */ import org.apache.tomcat.jni.Library;
/*      */ import org.apache.tomcat.jni.OS;
/*      */ import org.apache.tomcat.jni.Poll;
/*      */ import org.apache.tomcat.jni.Pool;
/*      */ import org.apache.tomcat.jni.SSL;
/*      */ import org.apache.tomcat.jni.SSLConf;
/*      */ import org.apache.tomcat.jni.SSLContext;
/*      */ import org.apache.tomcat.jni.SSLContext.SNICallBack;
/*      */ import org.apache.tomcat.jni.SSLSocket;
/*      */ import org.apache.tomcat.jni.Sockaddr;
/*      */ import org.apache.tomcat.jni.Socket;
/*      */ import org.apache.tomcat.jni.Status;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.ByteBufferUtils;
/*      */ import org.apache.tomcat.util.collections.SynchronizedStack;
/*      */ import org.apache.tomcat.util.net.openssl.OpenSSLConf;
/*      */ import org.apache.tomcat.util.net.openssl.OpenSSLEngine;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AprEndpoint
/*      */   extends AbstractEndpoint<Long>
/*      */   implements SSLContext.SNICallBack
/*      */ {
/*   82 */   private static final Log log = LogFactory.getLog(AprEndpoint.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   89 */   protected long rootPool = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   95 */   protected long serverSock = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */   protected long serverSockPool = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  107 */   protected long sslContext = 0L;
/*      */   
/*      */ 
/*  110 */   private final Map<Long, AprSocketWrapper> connections = new ConcurrentHashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AprEndpoint()
/*      */   {
/*  118 */     setMaxConnections(8192);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   protected boolean deferAccept = true;
/*  128 */   public void setDeferAccept(boolean deferAccept) { this.deferAccept = deferAccept; }
/*      */   
/*  130 */   public boolean getDeferAccept() { return this.deferAccept; }
/*      */   
/*      */ 
/*  133 */   private boolean ipv6v6only = false;
/*  134 */   public void setIpv6v6only(boolean ipv6v6only) { this.ipv6v6only = ipv6v6only; }
/*  135 */   public boolean getIpv6v6only() { return this.ipv6v6only; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  141 */   protected int sendfileSize = 1024;
/*  142 */   public void setSendfileSize(int sendfileSize) { this.sendfileSize = sendfileSize; }
/*  143 */   public int getSendfileSize() { return this.sendfileSize; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */   protected int pollTime = 2000;
/*  151 */   public int getPollTime() { return this.pollTime; }
/*  152 */   public void setPollTime(int pollTime) { if (pollTime > 0) { this.pollTime = pollTime;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  162 */   private boolean useSendFileSet = false;
/*      */   
/*      */   public void setUseSendfile(boolean useSendfile) {
/*  165 */     this.useSendFileSet = true;
/*  166 */     super.setUseSendfile(useSendfile);
/*      */   }
/*      */   
/*      */ 
/*      */   private void setUseSendfileInternal(boolean useSendfile)
/*      */   {
/*  172 */     super.setUseSendfile(useSendfile);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   protected Poller poller = null;
/*      */   
/*  181 */   public Poller getPoller() { return this.poller; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  188 */   protected Sendfile sendfile = null;
/*      */   
/*  190 */   public Sendfile getSendfile() { return this.sendfile; }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SSLHostConfig.Type getSslConfigType()
/*      */   {
/*  196 */     return SSLHostConfig.Type.OPENSSL;
/*      */   }
/*      */   
/*      */   public InetSocketAddress getLocalAddress()
/*      */     throws IOException
/*      */   {
/*  202 */     long s = this.serverSock;
/*  203 */     if (s == 0L) {
/*  204 */       return null;
/*      */     }
/*      */     try
/*      */     {
/*  208 */       sa = Address.get(0, s);
/*      */     } catch (IOException ioe) {
/*      */       long sa;
/*  211 */       throw ioe;
/*      */     }
/*      */     catch (Exception e) {
/*  214 */       throw new IOException(e); }
/*      */     long sa;
/*  216 */     Sockaddr addr = Address.getInfo(sa);
/*  217 */     if (addr.hostname == null)
/*      */     {
/*  219 */       if (addr.family == 2) {
/*  220 */         return new InetSocketAddress("::", addr.port);
/*      */       }
/*  222 */       return new InetSocketAddress("0.0.0.0", addr.port);
/*      */     }
/*      */     
/*  225 */     return new InetSocketAddress(addr.hostname, addr.port);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxConnections(int maxConnections)
/*      */   {
/*  238 */     if (maxConnections == -1) {
/*  239 */       log.warn(sm.getString("endpoint.apr.maxConnections.unlimited", new Object[] {
/*  240 */         Integer.valueOf(getMaxConnections()) }));
/*  241 */       return;
/*      */     }
/*  243 */     if (this.running) {
/*  244 */       log.warn(sm.getString("endpoint.apr.maxConnections.running", new Object[] {
/*  245 */         Integer.valueOf(getMaxConnections()) }));
/*  246 */       return;
/*      */     }
/*  248 */     super.setMaxConnections(maxConnections);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getKeepAliveCount()
/*      */   {
/*  260 */     if (this.poller == null) {
/*  261 */       return 0;
/*      */     }
/*      */     
/*  264 */     return this.poller.getConnectionCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSendfileCount()
/*      */   {
/*  274 */     if (this.sendfile == null) {
/*  275 */       return 0;
/*      */     }
/*      */     
/*  278 */     return this.sendfile.getSendfileCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void bind()
/*      */     throws Exception
/*      */   {
/*      */     try
/*      */     {
/*  293 */       this.rootPool = Pool.create(0L);
/*      */     } catch (UnsatisfiedLinkError e) {
/*  295 */       throw new Exception(sm.getString("endpoint.init.notavail"));
/*      */     }
/*      */     
/*      */ 
/*  299 */     this.serverSockPool = Pool.create(this.rootPool);
/*      */     
/*  301 */     String addressStr = null;
/*  302 */     if (getAddress() != null) {
/*  303 */       addressStr = getAddress().getHostAddress();
/*      */     }
/*  305 */     int family = 1;
/*  306 */     if (Library.APR_HAVE_IPV6) {
/*  307 */       if (addressStr == null) {
/*  308 */         if (!OS.IS_BSD) {
/*  309 */           family = 0;
/*      */         }
/*  311 */       } else if (addressStr.indexOf(':') >= 0) {
/*  312 */         family = 0;
/*      */       }
/*      */     }
/*      */     
/*  316 */     long inetAddress = Address.info(addressStr, family, 
/*  317 */       getPort(), 0, this.rootPool);
/*      */     
/*  319 */     this.serverSock = Socket.create(Address.getInfo(inetAddress).family, 0, 6, this.rootPool);
/*      */     
/*      */ 
/*  322 */     if (OS.IS_UNIX) {
/*  323 */       Socket.optSet(this.serverSock, 16, 1);
/*      */     }
/*  325 */     if (Library.APR_HAVE_IPV6) {
/*  326 */       if (getIpv6v6only()) {
/*  327 */         Socket.optSet(this.serverSock, 16384, 1);
/*      */       } else {
/*  329 */         Socket.optSet(this.serverSock, 16384, 0);
/*      */       }
/*      */     }
/*      */     
/*  333 */     Socket.optSet(this.serverSock, 2, 1);
/*      */     
/*  335 */     int ret = Socket.bind(this.serverSock, inetAddress);
/*  336 */     if (ret != 0) {
/*  337 */       throw new Exception(sm.getString("endpoint.init.bind", new Object[] { "" + ret, Error.strerror(ret) }));
/*      */     }
/*      */     
/*  340 */     ret = Socket.listen(this.serverSock, getAcceptCount());
/*  341 */     if (ret != 0) {
/*  342 */       throw new Exception(sm.getString("endpoint.init.listen", new Object[] { "" + ret, Error.strerror(ret) }));
/*      */     }
/*  344 */     if ((OS.IS_WIN32) || (OS.IS_WIN64))
/*      */     {
/*  346 */       Socket.optSet(this.serverSock, 16, 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  351 */     if (!this.useSendFileSet) {
/*  352 */       setUseSendfileInternal(Library.APR_HAS_SENDFILE);
/*  353 */     } else if ((getUseSendfile()) && (!Library.APR_HAS_SENDFILE)) {
/*  354 */       setUseSendfileInternal(false);
/*      */     }
/*      */     
/*      */ 
/*  358 */     if (this.acceptorThreadCount == 0)
/*      */     {
/*  360 */       this.acceptorThreadCount = 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  366 */     if ((this.deferAccept) && 
/*  367 */       (Socket.optSet(this.serverSock, 32768, 1) == 70023)) {
/*  368 */       this.deferAccept = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  373 */     if (isSSLEnabled()) {
/*  374 */       for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/*  375 */         sslHostConfig.setConfigType(getSslConfigType());
/*  376 */         createSSLContext(sslHostConfig);
/*      */       }
/*  378 */       SSLHostConfig defaultSSLHostConfig = (SSLHostConfig)this.sslHostConfigs.get(getDefaultSSLHostConfigName());
/*  379 */       if (defaultSSLHostConfig == null) {
/*  380 */         throw new IllegalArgumentException(sm.getString("endpoint.noSslHostConfig", new Object[] {
/*  381 */           getDefaultSSLHostConfigName(), getName() }));
/*      */       }
/*  383 */       Long defaultSSLContext = defaultSSLHostConfig.getOpenSslContext();
/*  384 */       this.sslContext = defaultSSLContext.longValue();
/*  385 */       SSLContext.registerDefault(defaultSSLContext, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void createSSLContext(SSLHostConfig sslHostConfig)
/*      */     throws Exception
/*      */   {
/*  393 */     Set<SSLHostConfigCertificate> certificates = sslHostConfig.getCertificates(true);
/*  394 */     boolean firstCertificate = true;
/*  395 */     for (Iterator localIterator1 = certificates.iterator(); localIterator1.hasNext();) { certificate = (SSLHostConfigCertificate)localIterator1.next();
/*  396 */       if (SSLHostConfig.adjustRelativePath(certificate.getCertificateFile()) == null)
/*      */       {
/*  398 */         throw new Exception(sm.getString("endpoint.apr.noSslCertFile"));
/*      */       }
/*  400 */       if (firstCertificate)
/*      */       {
/*      */ 
/*  403 */         firstCertificate = false;
/*      */         
/*  405 */         enabledProtocols = SSLUtilBase.getEnabled("protocols", log, true, sslHostConfig
/*  406 */           .getProtocols(), OpenSSLEngine.IMPLEMENTED_PROTOCOLS_SET);
/*      */         
/*  408 */         sslHostConfig.setEnabledProtocols(
/*  409 */           (String[])enabledProtocols.toArray(new String[enabledProtocols.size()]));
/*      */         
/*  411 */         enabledCiphers = SSLUtilBase.getEnabled("ciphers", log, false, sslHostConfig
/*  412 */           .getJsseCipherNames(), OpenSSLEngine.AVAILABLE_CIPHER_SUITES);
/*      */         
/*  414 */         sslHostConfig.setEnabledCiphers(
/*  415 */           (String[])enabledCiphers.toArray(new String[enabledCiphers.size()])); } }
/*      */     SSLHostConfigCertificate certificate;
/*      */     List<String> enabledProtocols;
/*  418 */     List<String> enabledCiphers; if (certificates.size() > 2)
/*      */     {
/*  420 */       throw new Exception(sm.getString("endpoint.apr.tooManyCertFiles"));
/*      */     }
/*      */     
/*      */ 
/*  424 */     int value = 0;
/*  425 */     if (sslHostConfig.getProtocols().size() == 0)
/*      */     {
/*  427 */       value = 28;
/*      */     } else {
/*  429 */       certificate = sslHostConfig.getEnabledProtocols();enabledProtocols = certificate.length; for (enabledCiphers = 0; enabledCiphers < enabledProtocols; enabledCiphers++) { String protocol = certificate[enabledCiphers];
/*  430 */         if (!"SSLv2Hello".equalsIgnoreCase(protocol))
/*      */         {
/*  432 */           if ("SSLv2".equalsIgnoreCase(protocol)) {
/*  433 */             value |= 0x1;
/*  434 */           } else if ("SSLv3".equalsIgnoreCase(protocol)) {
/*  435 */             value |= 0x2;
/*  436 */           } else if ("TLSv1".equalsIgnoreCase(protocol)) {
/*  437 */             value |= 0x4;
/*  438 */           } else if ("TLSv1.1".equalsIgnoreCase(protocol)) {
/*  439 */             value |= 0x8;
/*  440 */           } else if ("TLSv1.2".equalsIgnoreCase(protocol)) {
/*  441 */             value |= 0x10;
/*      */           }
/*      */           else
/*      */           {
/*  445 */             throw new Exception(sm.getString("endpoint.apr.invalidSslProtocol", new Object[] { protocol }));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  452 */     long ctx = 0L;
/*      */     try {
/*  454 */       ctx = SSLContext.make(this.rootPool, value, 1);
/*      */ 
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*      */ 
/*  460 */       throw new Exception(sm.getString("endpoint.apr.failSslContextMake"), e);
/*      */     }
/*      */     
/*  463 */     if (sslHostConfig.getInsecureRenegotiation()) {
/*  464 */       SSLContext.setOptions(ctx, 262144);
/*      */     } else {
/*  466 */       SSLContext.clearOptions(ctx, 262144);
/*      */     }
/*      */     
/*      */ 
/*  470 */     String honorCipherOrderStr = sslHostConfig.getHonorCipherOrder();
/*  471 */     if (honorCipherOrderStr != null) {
/*  472 */       boolean honorCipherOrder = Boolean.valueOf(honorCipherOrderStr).booleanValue();
/*  473 */       if (honorCipherOrder) {
/*  474 */         SSLContext.setOptions(ctx, 4194304);
/*      */       } else {
/*  476 */         SSLContext.clearOptions(ctx, 4194304);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  481 */     if (sslHostConfig.getDisableCompression()) {
/*  482 */       SSLContext.setOptions(ctx, 131072);
/*      */     } else {
/*  484 */       SSLContext.clearOptions(ctx, 131072);
/*      */     }
/*      */     
/*      */ 
/*  488 */     if (sslHostConfig.getDisableSessionTickets()) {
/*  489 */       SSLContext.setOptions(ctx, 16384);
/*      */     } else {
/*  491 */       SSLContext.clearOptions(ctx, 16384);
/*      */     }
/*      */     
/*      */ 
/*  495 */     SSLContext.setCipherSuite(ctx, sslHostConfig.getCiphers());
/*      */     
/*      */ 
/*      */ 
/*  499 */     int idx = 0;
/*  500 */     for (SSLHostConfigCertificate certificate : sslHostConfig.getCertificates(true)) {
/*  501 */       SSLContext.setCertificate(ctx, 
/*  502 */         SSLHostConfig.adjustRelativePath(certificate.getCertificateFile()), 
/*  503 */         SSLHostConfig.adjustRelativePath(certificate.getCertificateKeyFile()), certificate
/*  504 */         .getCertificateKeyPassword(), idx++);
/*      */       
/*  506 */       SSLContext.setCertificateChainFile(ctx, 
/*  507 */         SSLHostConfig.adjustRelativePath(certificate.getCertificateChainFile()), false);
/*      */     }
/*      */     
/*  510 */     SSLContext.setCACertificate(ctx, 
/*  511 */       SSLHostConfig.adjustRelativePath(sslHostConfig.getCaCertificateFile()), 
/*  512 */       SSLHostConfig.adjustRelativePath(sslHostConfig.getCaCertificatePath()));
/*      */     
/*  514 */     SSLContext.setCARevocation(ctx, 
/*  515 */       SSLHostConfig.adjustRelativePath(sslHostConfig
/*  516 */       .getCertificateRevocationListFile()), 
/*  517 */       SSLHostConfig.adjustRelativePath(sslHostConfig
/*  518 */       .getCertificateRevocationListPath()));
/*      */     
/*  520 */     switch (sslHostConfig.getCertificateVerification()) {
/*      */     case NONE: 
/*  522 */       value = 0;
/*  523 */       break;
/*      */     case OPTIONAL: 
/*  525 */       value = 1;
/*  526 */       break;
/*      */     case OPTIONAL_NO_CA: 
/*  528 */       value = 3;
/*  529 */       break;
/*      */     case REQUIRED: 
/*  531 */       value = 2;
/*      */     }
/*      */     
/*  534 */     SSLContext.setVerify(ctx, value, sslHostConfig.getCertificateVerificationDepth());
/*      */     
/*  536 */     if (getUseSendfile()) {
/*  537 */       setUseSendfileInternal(false);
/*  538 */       if (this.useSendFileSet) {
/*  539 */         log.warn(sm.getString("endpoint.apr.noSendfileWithSSL"));
/*      */       }
/*      */     }
/*      */     
/*  543 */     if (this.negotiableProtocols.size() > 0) {
/*  544 */       Object protocols = new ArrayList();
/*  545 */       ((ArrayList)protocols).addAll(this.negotiableProtocols);
/*  546 */       ((ArrayList)protocols).add("http/1.1");
/*  547 */       String[] protocolsArray = (String[])((ArrayList)protocols).toArray(new String[0]);
/*  548 */       SSLContext.setAlpnProtos(ctx, protocolsArray, 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  554 */     SSLContext.setSessionIdContext(ctx, SSLContext.DEFAULT_SESSION_ID_CONTEXT);
/*      */     
/*      */ 
/*  557 */     OpenSSLConf openSslConf = sslHostConfig.getOpenSslConf();
/*  558 */     long cctx; if (openSslConf != null)
/*      */     {
/*      */       try {
/*  561 */         if (log.isDebugEnabled())
/*  562 */           log.debug(sm.getString("endpoint.apr.makeConf"));
/*  563 */         cctx = SSLConf.make(this.rootPool, 58);
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/*      */         long cctx;
/*      */         
/*  569 */         throw new Exception(sm.getString("endpoint.apr.errMakeConf"), e); }
/*      */       long cctx;
/*  571 */       if (cctx != 0L)
/*      */       {
/*  573 */         if (log.isDebugEnabled())
/*  574 */           log.debug(sm.getString("endpoint.apr.checkConf"));
/*      */         try {
/*  576 */           if (!openSslConf.check(cctx)) {
/*  577 */             log.error(sm.getString("endpoint.apr.errCheckConf"));
/*  578 */             throw new Exception(sm.getString("endpoint.apr.errCheckConf"));
/*      */           }
/*      */         } catch (Exception e) {
/*  581 */           throw new Exception(sm.getString("endpoint.apr.errCheckConf"), e);
/*      */         }
/*      */         
/*  584 */         if (log.isDebugEnabled())
/*  585 */           log.debug(sm.getString("endpoint.apr.applyConf"));
/*      */         try {
/*  587 */           if (!openSslConf.apply(cctx, ctx)) {
/*  588 */             log.error(sm.getString("endpoint.apr.errApplyConf"));
/*  589 */             throw new Exception(sm.getString("endpoint.apr.errApplyConf"));
/*      */           }
/*      */         } catch (Exception e) {
/*  592 */           throw new Exception(sm.getString("endpoint.apr.errApplyConf"), e);
/*      */         }
/*      */         
/*  595 */         int opts = SSLContext.getOptions(ctx);
/*  596 */         List<String> enabled = new ArrayList();
/*      */         
/*      */ 
/*  599 */         enabled.add("SSLv2Hello");
/*  600 */         if ((opts & 0x4000000) == 0) {
/*  601 */           enabled.add("TLSv1");
/*      */         }
/*  603 */         if ((opts & 0x10000000) == 0) {
/*  604 */           enabled.add("TLSv1.1");
/*      */         }
/*  606 */         if ((opts & 0x8000000) == 0) {
/*  607 */           enabled.add("TLSv1.2");
/*      */         }
/*  609 */         if ((opts & 0x1000000) == 0) {
/*  610 */           enabled.add("SSLv2");
/*      */         }
/*  612 */         if ((opts & 0x2000000) == 0) {
/*  613 */           enabled.add("SSLv3");
/*      */         }
/*  615 */         sslHostConfig.setEnabledProtocols(
/*  616 */           (String[])enabled.toArray(new String[enabled.size()]));
/*      */         
/*  618 */         sslHostConfig.setEnabledCiphers(SSLContext.getCiphers(ctx));
/*      */       }
/*      */     } else {
/*  621 */       cctx = 0L;
/*      */     }
/*      */     
/*  624 */     sslHostConfig.setOpenSslConfContext(Long.valueOf(cctx));
/*  625 */     sslHostConfig.setOpenSslContext(Long.valueOf(ctx));
/*      */   }
/*      */   
/*      */ 
/*      */   protected void releaseSSLContext(SSLHostConfig sslHostConfig)
/*      */   {
/*  631 */     Long ctx = sslHostConfig.getOpenSslContext();
/*  632 */     if (ctx != null) {
/*  633 */       SSLContext.free(ctx.longValue());
/*  634 */       sslHostConfig.setOpenSslContext(null);
/*      */     }
/*  636 */     Long cctx = sslHostConfig.getOpenSslConfContext();
/*  637 */     if (cctx != null) {
/*  638 */       SSLConf.free(cctx.longValue());
/*  639 */       sslHostConfig.setOpenSslConfContext(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public long getSslContext(String sniHostName)
/*      */   {
/*  646 */     SSLHostConfig sslHostConfig = getSSLHostConfig(sniHostName);
/*  647 */     Long ctx = sslHostConfig.getOpenSslContext();
/*  648 */     if (ctx != null) {
/*  649 */       return ctx.longValue();
/*      */     }
/*      */     
/*  652 */     return 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAlpnSupported()
/*      */   {
/*  662 */     return isSSLEnabled();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startInternal()
/*      */     throws Exception
/*      */   {
/*  672 */     if (!this.running) {
/*  673 */       this.running = true;
/*  674 */       this.paused = false;
/*      */       
/*      */ 
/*  677 */       this.processorCache = new SynchronizedStack(128, this.socketProperties.getProcessorCache());
/*      */       
/*      */ 
/*  680 */       if (getExecutor() == null) {
/*  681 */         createExecutor();
/*      */       }
/*      */       
/*  684 */       initializeConnectionLatch();
/*      */       
/*      */ 
/*  687 */       this.poller = new Poller();
/*  688 */       this.poller.init();
/*  689 */       Thread pollerThread = new Thread(this.poller, getName() + "-Poller");
/*  690 */       pollerThread.setPriority(this.threadPriority);
/*  691 */       pollerThread.setDaemon(true);
/*  692 */       pollerThread.start();
/*      */       
/*      */ 
/*  695 */       if (getUseSendfile()) {
/*  696 */         this.sendfile = new Sendfile();
/*  697 */         this.sendfile.init();
/*      */         
/*  699 */         Thread sendfileThread = new Thread(this.sendfile, getName() + "-Sendfile");
/*  700 */         sendfileThread.setPriority(this.threadPriority);
/*  701 */         sendfileThread.setDaemon(true);
/*  702 */         sendfileThread.start();
/*      */       }
/*      */       
/*  705 */       startAcceptorThreads();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stopInternal()
/*      */   {
/*  715 */     releaseConnectionLatch();
/*  716 */     if (!this.paused) {
/*  717 */       pause();
/*      */     }
/*  719 */     if (this.running) {
/*  720 */       this.running = false;
/*  721 */       this.poller.stop();
/*  722 */       for (Object localObject = this.connections.values().iterator(); ((Iterator)localObject).hasNext();) { socketWrapper = (SocketWrapperBase)((Iterator)localObject).next();
/*      */         try {
/*  724 */           socketWrapper.close();
/*      */         }
/*      */         catch (IOException localIOException) {}
/*      */       }
/*      */       
/*  729 */       localObject = this.acceptors;SocketWrapperBase<Long> socketWrapper = localObject.length; for (localIOException = 0; localIOException < socketWrapper; localIOException++) { AbstractEndpoint.Acceptor acceptor = localObject[localIOException];
/*  730 */         long waitLeft = 10000L;
/*  731 */         while ((waitLeft > 0L) && 
/*  732 */           (acceptor.getState() != AbstractEndpoint.Acceptor.AcceptorState.ENDED) && (this.serverSock != 0L))
/*      */         {
/*      */           try {
/*  735 */             Thread.sleep(50L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */           
/*  739 */           waitLeft -= 50L;
/*      */         }
/*  741 */         if (waitLeft == 0L) {
/*  742 */           log.warn(sm.getString("endpoint.warn.unlockAcceptorFailed", new Object[] {acceptor
/*  743 */             .getThreadName() }));
/*      */           
/*      */ 
/*  746 */           if (this.serverSock != 0L) {
/*  747 */             Socket.shutdown(this.serverSock, 0);
/*  748 */             this.serverSock = 0L;
/*      */           }
/*      */         }
/*      */       }
/*      */       try {
/*  753 */         this.poller.destroy();
/*      */       }
/*      */       catch (Exception localException2) {}
/*      */       
/*  757 */       this.poller = null;
/*  758 */       this.connections.clear();
/*  759 */       if (getUseSendfile()) {
/*      */         try {
/*  761 */           this.sendfile.destroy();
/*      */         }
/*      */         catch (Exception localException3) {}
/*      */         
/*  765 */         this.sendfile = null;
/*      */       }
/*  767 */       this.processorCache.clear();
/*      */     }
/*  769 */     shutdownExecutor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unbind()
/*      */     throws Exception
/*      */   {
/*  778 */     if (this.running) {
/*  779 */       stop();
/*      */     }
/*      */     
/*      */ 
/*  783 */     if (this.serverSockPool != 0L) {
/*  784 */       Pool.destroy(this.serverSockPool);
/*  785 */       this.serverSockPool = 0L;
/*      */     }
/*      */     
/*      */ 
/*  789 */     if (this.serverSock != 0L) {
/*  790 */       Socket.close(this.serverSock);
/*  791 */       this.serverSock = 0L;
/*      */     }
/*      */     
/*  794 */     if (this.sslContext != 0L) {
/*  795 */       Long ctx = Long.valueOf(this.sslContext);
/*  796 */       SSLContext.unregisterDefault(ctx);
/*  797 */       for (SSLHostConfig sslHostConfig : this.sslHostConfigs.values()) {
/*  798 */         sslHostConfig.setOpenSslContext(null);
/*      */       }
/*  800 */       this.sslContext = 0L;
/*      */     }
/*      */     
/*      */ 
/*  804 */     if (this.rootPool != 0L) {
/*  805 */       Pool.destroy(this.rootPool);
/*  806 */       this.rootPool = 0L;
/*      */     }
/*      */     
/*  809 */     getHandler().recycle();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AbstractEndpoint.Acceptor createAcceptor()
/*      */   {
/*  817 */     return new Acceptor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean setSocketOptions(SocketWrapperBase<Long> socketWrapper)
/*      */   {
/*  829 */     long socket = ((Long)socketWrapper.getSocket()).longValue();
/*      */     
/*  831 */     int step = 1;
/*      */     
/*      */     try
/*      */     {
/*  835 */       if ((this.socketProperties.getSoLingerOn()) && (this.socketProperties.getSoLingerTime() >= 0))
/*  836 */         Socket.optSet(socket, 1, this.socketProperties.getSoLingerTime());
/*  837 */       if (this.socketProperties.getTcpNoDelay())
/*  838 */         Socket.optSet(socket, 512, this.socketProperties.getTcpNoDelay() ? 1 : 0);
/*  839 */       Socket.timeoutSet(socket, this.socketProperties.getSoTimeout() * 1000);
/*      */       
/*      */ 
/*  842 */       step = 2;
/*  843 */       if (this.sslContext != 0L) {
/*  844 */         SSLSocket.attach(this.sslContext, socket);
/*  845 */         if (SSLSocket.handshake(socket) != 0) {
/*  846 */           if (log.isDebugEnabled()) {
/*  847 */             log.debug(sm.getString("endpoint.err.handshake") + ": " + SSL.getLastError());
/*      */           }
/*  849 */           return false;
/*      */         }
/*      */         
/*  852 */         if (this.negotiableProtocols.size() > 0) {
/*  853 */           byte[] negotiated = new byte['Ā'];
/*  854 */           int len = SSLSocket.getALPN(socket, negotiated);
/*  855 */           String negotiatedProtocol = new String(negotiated, 0, len, StandardCharsets.UTF_8);
/*      */           
/*  857 */           if (negotiatedProtocol.length() > 0) {
/*  858 */             socketWrapper.setNegotiatedProtocol(negotiatedProtocol);
/*  859 */             if (log.isDebugEnabled()) {
/*  860 */               log.debug(sm.getString("endpoint.alpn.negotiated", new Object[] { negotiatedProtocol }));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Throwable t) {
/*  866 */       ExceptionUtils.handleThrowable(t);
/*  867 */       if (log.isDebugEnabled()) {
/*  868 */         if (step == 2) {
/*  869 */           log.debug(sm.getString("endpoint.err.handshake"), t);
/*      */         } else {
/*  871 */           log.debug(sm.getString("endpoint.err.unexpected"), t);
/*      */         }
/*      */       }
/*      */       
/*  875 */       return false;
/*      */     }
/*  877 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long allocatePoller(int size, long pool, int timeout)
/*      */   {
/*      */     try
/*      */     {
/*  890 */       return Poll.create(size, pool, 0, timeout * 1000);
/*      */     } catch (Error e) {
/*  892 */       if (Status.APR_STATUS_IS_EINVAL(e.getError())) {
/*  893 */         log.info(sm.getString("endpoint.poll.limitedpollsize", new Object[] { "" + size }));
/*  894 */         return 0L;
/*      */       }
/*  896 */       log.error(sm.getString("endpoint.poll.initfail"), e); }
/*  897 */     return -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean processSocketWithOptions(long socket)
/*      */   {
/*      */     try
/*      */     {
/*  913 */       if (this.running) {
/*  914 */         if (log.isDebugEnabled()) {
/*  915 */           log.debug(sm.getString("endpoint.debug.socket", new Object[] {
/*  916 */             Long.valueOf(socket) }));
/*      */         }
/*  918 */         AprSocketWrapper wrapper = new AprSocketWrapper(Long.valueOf(socket), this);
/*  919 */         wrapper.setKeepAliveLeft(getMaxKeepAliveRequests());
/*  920 */         wrapper.setSecure(isSSLEnabled());
/*  921 */         wrapper.setReadTimeout(getConnectionTimeout());
/*  922 */         wrapper.setWriteTimeout(getConnectionTimeout());
/*  923 */         this.connections.put(Long.valueOf(socket), wrapper);
/*  924 */         getExecutor().execute(new SocketWithOptionsProcessor(wrapper));
/*      */       }
/*      */     } catch (RejectedExecutionException x) {
/*  927 */       log.warn("Socket processing request was rejected for:" + socket, x);
/*  928 */       return false;
/*      */     } catch (Throwable t) {
/*  930 */       ExceptionUtils.handleThrowable(t);
/*      */       
/*      */ 
/*  933 */       log.error(sm.getString("endpoint.process.fail"), t);
/*  934 */       return false;
/*      */     }
/*  936 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean processSocket(long socket, SocketEvent event)
/*      */   {
/*  951 */     SocketWrapperBase<Long> socketWrapper = (SocketWrapperBase)this.connections.get(Long.valueOf(socket));
/*  952 */     return processSocket(socketWrapper, event, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SocketProcessorBase<Long> createSocketProcessor(SocketWrapperBase<Long> socketWrapper, SocketEvent event)
/*      */   {
/*  959 */     return new SocketProcessor(socketWrapper, event);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void closeSocket(long socket)
/*      */   {
/*  966 */     SocketWrapperBase<Long> wrapper = (SocketWrapperBase)this.connections.remove(Long.valueOf(socket));
/*  967 */     if (wrapper != null)
/*      */     {
/*  969 */       ((AprSocketWrapper)wrapper).close();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void destroySocket(long socket)
/*      */   {
/*  979 */     this.connections.remove(Long.valueOf(socket));
/*  980 */     if (log.isDebugEnabled()) {
/*  981 */       String msg = sm.getString("endpoint.debug.destroySocket", new Object[] {
/*  982 */         Long.valueOf(socket) });
/*  983 */       if (log.isTraceEnabled()) {
/*  984 */         log.trace(msg, new Exception());
/*      */       } else {
/*  986 */         log.debug(msg);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  993 */     if (socket != 0L) {
/*  994 */       Socket.destroy(socket);
/*  995 */       countDownConnection();
/*      */     }
/*      */   }
/*      */   
/*      */   protected Log getLog()
/*      */   {
/* 1001 */     return log;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class Acceptor
/*      */     extends AbstractEndpoint.Acceptor
/*      */   {
/* 1011 */     private final Log log = LogFactory.getLog(Acceptor.class);
/*      */     
/*      */     protected Acceptor() {}
/*      */     
/*      */     public void run() {
/* 1016 */       int errorDelay = 0;
/*      */       
/*      */ 
/* 1019 */       while (AprEndpoint.this.running)
/*      */       {
/*      */ 
/* 1022 */         while ((AprEndpoint.this.paused) && (AprEndpoint.this.running)) {
/* 1023 */           this.state = AbstractEndpoint.Acceptor.AcceptorState.PAUSED;
/*      */           try {
/* 1025 */             Thread.sleep(50L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */         
/*      */ 
/* 1031 */         if (!AprEndpoint.this.running) {
/*      */           break;
/*      */         }
/* 1034 */         this.state = AbstractEndpoint.Acceptor.AcceptorState.RUNNING;
/*      */         
/*      */         try
/*      */         {
/* 1038 */           AprEndpoint.this.countUpOrAwaitConnection();
/*      */           
/* 1040 */           long socket = 0L;
/*      */           
/*      */           try
/*      */           {
/* 1044 */             socket = Socket.accept(AprEndpoint.this.serverSock);
/* 1045 */             if (this.log.isDebugEnabled()) {
/* 1046 */               long sa = Address.get(1, socket);
/* 1047 */               Sockaddr addr = Address.getInfo(sa);
/* 1048 */               this.log.debug(AbstractEndpoint.sm.getString("endpoint.apr.remoteport", new Object[] {
/* 1049 */                 Long.valueOf(socket), 
/* 1050 */                 Long.valueOf(addr.port) }));
/*      */             }
/*      */           }
/*      */           catch (Exception e) {
/* 1054 */             AprEndpoint.this.countDownConnection();
/* 1055 */             if (AprEndpoint.this.running)
/*      */             {
/* 1057 */               errorDelay = AprEndpoint.this.handleExceptionWithDelay(errorDelay);
/*      */               
/* 1059 */               throw e;
/*      */             }
/* 1061 */             break;
/*      */           }
/*      */           
/*      */ 
/* 1065 */           errorDelay = 0;
/*      */           
/* 1067 */           if ((AprEndpoint.this.running) && (!AprEndpoint.this.paused))
/*      */           {
/* 1069 */             if (!AprEndpoint.this.processSocketWithOptions(socket))
/*      */             {
/* 1071 */               AprEndpoint.this.closeSocket(socket);
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           else {
/* 1077 */             AprEndpoint.this.destroySocket(socket);
/*      */           }
/*      */         } catch (Throwable t) {
/* 1080 */           ExceptionUtils.handleThrowable(t);
/* 1081 */           String msg = AbstractEndpoint.sm.getString("endpoint.accept.fail");
/* 1082 */           if ((t instanceof Error)) {
/* 1083 */             Error e = (Error)t;
/* 1084 */             if (e.getError() == 233)
/*      */             {
/*      */ 
/*      */ 
/* 1088 */               this.log.warn(msg, t);
/*      */             } else {
/* 1090 */               this.log.error(msg, t);
/*      */             }
/*      */           } else {
/* 1093 */             this.log.error(msg, t);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1098 */       this.state = AbstractEndpoint.Acceptor.AcceptorState.ENDED;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SocketInfo
/*      */   {
/*      */     public long socket;
/*      */     public long timeout;
/*      */     public int flags;
/*      */     
/*      */     public boolean read()
/*      */     {
/* 1110 */       return (this.flags & 0x1) == 1;
/*      */     }
/*      */     
/* 1113 */     public boolean write() { return (this.flags & 0x4) == 4; }
/*      */     
/*      */     public static int merge(int flag1, int flag2) {
/* 1116 */       return flag1 & 0x1 | flag2 & 0x1 | flag1 & 0x4 | flag2 & 0x4;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1121 */       StringBuilder sb = new StringBuilder();
/* 1122 */       sb.append("Socket: [");
/* 1123 */       sb.append(this.socket);
/* 1124 */       sb.append("], timeout: [");
/* 1125 */       sb.append(this.timeout);
/* 1126 */       sb.append("], flags: [");
/* 1127 */       sb.append(this.flags);
/* 1128 */       return sb.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class SocketTimeouts
/*      */   {
/*      */     protected int size;
/*      */     
/*      */     protected long[] sockets;
/*      */     
/*      */     protected long[] timeouts;
/* 1140 */     protected int pos = 0;
/*      */     
/*      */     public SocketTimeouts(int size) {
/* 1143 */       this.size = 0;
/* 1144 */       this.sockets = new long[size];
/* 1145 */       this.timeouts = new long[size];
/*      */     }
/*      */     
/*      */     public void add(long socket, long timeout) {
/* 1149 */       this.sockets[this.size] = socket;
/* 1150 */       this.timeouts[this.size] = timeout;
/* 1151 */       this.size += 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public long remove(long socket)
/*      */     {
/* 1163 */       long result = 0L;
/* 1164 */       for (int i = 0; i < this.size; i++) {
/* 1165 */         if (this.sockets[i] == socket) {
/* 1166 */           result = this.timeouts[i];
/* 1167 */           this.sockets[i] = this.sockets[(this.size - 1)];
/* 1168 */           this.timeouts[i] = this.timeouts[(this.size - 1)];
/* 1169 */           this.size -= 1;
/* 1170 */           break;
/*      */         }
/*      */       }
/* 1173 */       return result;
/*      */     }
/*      */     
/*      */     public long check(long date) {
/* 1177 */       while (this.pos < this.size) {
/* 1178 */         if (date >= this.timeouts[this.pos]) {
/* 1179 */           long result = this.sockets[this.pos];
/* 1180 */           this.sockets[this.pos] = this.sockets[(this.size - 1)];
/* 1181 */           this.timeouts[this.pos] = this.timeouts[(this.size - 1)];
/* 1182 */           this.size -= 1;
/* 1183 */           return result;
/*      */         }
/* 1185 */         this.pos += 1;
/*      */       }
/* 1187 */       this.pos = 0;
/* 1188 */       return 0L;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class SocketList
/*      */   {
/*      */     protected volatile int size;
/*      */     
/*      */     protected int pos;
/*      */     
/*      */     protected long[] sockets;
/*      */     
/*      */     protected long[] timeouts;
/*      */     
/*      */     protected int[] flags;
/* 1204 */     protected AprEndpoint.SocketInfo info = new AprEndpoint.SocketInfo();
/*      */     
/*      */     public SocketList(int size) {
/* 1207 */       this.size = 0;
/* 1208 */       this.pos = 0;
/* 1209 */       this.sockets = new long[size];
/* 1210 */       this.timeouts = new long[size];
/* 1211 */       this.flags = new int[size];
/*      */     }
/*      */     
/*      */     public int size() {
/* 1215 */       return this.size;
/*      */     }
/*      */     
/*      */     public AprEndpoint.SocketInfo get() {
/* 1219 */       if (this.pos == this.size) {
/* 1220 */         return null;
/*      */       }
/* 1222 */       this.info.socket = this.sockets[this.pos];
/* 1223 */       this.info.timeout = this.timeouts[this.pos];
/* 1224 */       this.info.flags = this.flags[this.pos];
/* 1225 */       this.pos += 1;
/* 1226 */       return this.info;
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/* 1231 */       this.size = 0;
/* 1232 */       this.pos = 0;
/*      */     }
/*      */     
/*      */     public boolean add(long socket, long timeout, int flag) {
/* 1236 */       if (this.size == this.sockets.length) {
/* 1237 */         return false;
/*      */       }
/* 1239 */       for (int i = 0; i < this.size; i++) {
/* 1240 */         if (this.sockets[i] == socket) {
/* 1241 */           this.flags[i] = AprEndpoint.SocketInfo.merge(this.flags[i], flag);
/* 1242 */           return true;
/*      */         }
/*      */       }
/* 1245 */       this.sockets[this.size] = socket;
/* 1246 */       this.timeouts[this.size] = timeout;
/* 1247 */       this.flags[this.size] = flag;
/* 1248 */       this.size += 1;
/* 1249 */       return true;
/*      */     }
/*      */     
/*      */     public boolean remove(long socket)
/*      */     {
/* 1254 */       for (int i = 0; i < this.size; i++) {
/* 1255 */         if (this.sockets[i] == socket) {
/* 1256 */           this.sockets[i] = this.sockets[(this.size - 1)];
/* 1257 */           this.timeouts[i] = this.timeouts[(this.size - 1)];
/* 1258 */           this.flags[this.size] = this.flags[(this.size - 1)];
/* 1259 */           this.size -= 1;
/* 1260 */           return true;
/*      */         }
/*      */       }
/* 1263 */       return false;
/*      */     }
/*      */     
/*      */     public void duplicate(SocketList copy) {
/* 1267 */       copy.size = this.size;
/* 1268 */       copy.pos = this.pos;
/* 1269 */       System.arraycopy(this.sockets, 0, copy.sockets, 0, this.size);
/* 1270 */       System.arraycopy(this.timeouts, 0, copy.timeouts, 0, this.size);
/* 1271 */       System.arraycopy(this.flags, 0, copy.flags, 0, this.size);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class Poller
/*      */     implements Runnable
/*      */   {
/* 1283 */     private long[] pollers = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1288 */     private int actualPollerSize = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1293 */     private int[] pollerSpace = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int pollerCount;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int pollerTime;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private int nextPollerTime;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1315 */     private long pool = 0L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private long[] desc;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1325 */     private AprEndpoint.SocketList addList = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1331 */     private AprEndpoint.SocketList closeList = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1337 */     private AprEndpoint.SocketTimeouts timeouts = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1344 */     private long lastMaintain = System.currentTimeMillis();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Poller() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1357 */     private AtomicInteger connectionCount = new AtomicInteger(0);
/* 1358 */     public int getConnectionCount() { return this.connectionCount.get(); }
/*      */     
/*      */ 
/* 1361 */     private volatile boolean pollerRunning = true;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected synchronized void init()
/*      */     {
/* 1369 */       this.pool = Pool.create(AprEndpoint.this.serverSockPool);
/*      */       
/*      */ 
/* 1372 */       int defaultPollerSize = AprEndpoint.this.getMaxConnections();
/*      */       
/* 1374 */       if (((OS.IS_WIN32) || (OS.IS_WIN64)) && (defaultPollerSize > 1024))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1379 */         this.actualPollerSize = 1024;
/*      */       } else {
/* 1381 */         this.actualPollerSize = defaultPollerSize;
/*      */       }
/*      */       
/* 1384 */       this.timeouts = new AprEndpoint.SocketTimeouts(defaultPollerSize);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1389 */       long pollset = AprEndpoint.this.allocatePoller(this.actualPollerSize, this.pool, -1);
/* 1390 */       if ((pollset == 0L) && (this.actualPollerSize > 1024)) {
/* 1391 */         this.actualPollerSize = 1024;
/* 1392 */         pollset = AprEndpoint.this.allocatePoller(this.actualPollerSize, this.pool, -1);
/*      */       }
/* 1394 */       if (pollset == 0L) {
/* 1395 */         this.actualPollerSize = 62;
/* 1396 */         pollset = AprEndpoint.this.allocatePoller(this.actualPollerSize, this.pool, -1);
/*      */       }
/*      */       
/* 1399 */       this.pollerCount = (defaultPollerSize / this.actualPollerSize);
/* 1400 */       this.pollerTime = (AprEndpoint.this.pollTime / this.pollerCount);
/* 1401 */       this.nextPollerTime = this.pollerTime;
/*      */       
/* 1403 */       this.pollers = new long[this.pollerCount];
/* 1404 */       this.pollers[0] = pollset;
/* 1405 */       for (int i = 1; i < this.pollerCount; i++) {
/* 1406 */         this.pollers[i] = AprEndpoint.this.allocatePoller(this.actualPollerSize, this.pool, -1);
/*      */       }
/*      */       
/* 1409 */       this.pollerSpace = new int[this.pollerCount];
/* 1410 */       for (int i = 0; i < this.pollerCount; i++) {
/* 1411 */         this.pollerSpace[i] = this.actualPollerSize;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1423 */       this.desc = new long[this.actualPollerSize * 4];
/* 1424 */       this.connectionCount.set(0);
/* 1425 */       this.addList = new AprEndpoint.SocketList(defaultPollerSize);
/* 1426 */       this.closeList = new AprEndpoint.SocketList(defaultPollerSize);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected synchronized void stop()
/*      */     {
/* 1435 */       this.pollerRunning = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected synchronized void destroy()
/*      */     {
/*      */       try
/*      */       {
/* 1447 */         notify();
/* 1448 */         wait(this.pollerCount * AprEndpoint.this.pollTime / 1000);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */       
/*      */ 
/* 1453 */       AprEndpoint.SocketInfo info = this.closeList.get();
/* 1454 */       while (info != null)
/*      */       {
/* 1456 */         this.addList.remove(info.socket);
/*      */         
/* 1458 */         removeFromPoller(info.socket);
/*      */         
/*      */ 
/* 1461 */         AprEndpoint.this.destroySocket(info.socket);
/* 1462 */         info = this.closeList.get();
/*      */       }
/* 1464 */       this.closeList.clear();
/*      */       
/* 1466 */       info = this.addList.get();
/* 1467 */       while (info != null)
/*      */       {
/* 1469 */         removeFromPoller(info.socket);
/*      */         
/*      */ 
/* 1472 */         AprEndpoint.this.destroySocket(info.socket);
/* 1473 */         info = this.addList.get();
/*      */       }
/* 1475 */       this.addList.clear();
/*      */       
/* 1477 */       for (int i = 0; i < this.pollerCount; i++) {
/* 1478 */         int rv = Poll.pollset(this.pollers[i], this.desc);
/* 1479 */         if (rv > 0) {
/* 1480 */           for (int n = 0; n < rv; n++) {
/* 1481 */             AprEndpoint.this.destroySocket(this.desc[(n * 2 + 1)]);
/*      */           }
/*      */         }
/*      */       }
/* 1485 */       Pool.destroy(this.pool);
/* 1486 */       this.connectionCount.set(0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void add(long socket, long timeout, int flags)
/*      */     {
/* 1505 */       if (AprEndpoint.log.isDebugEnabled()) {
/* 1506 */         String msg = AbstractEndpoint.sm.getString("endpoint.debug.pollerAdd", new Object[] {
/* 1507 */           Long.valueOf(socket), Long.valueOf(timeout), 
/* 1508 */           Integer.valueOf(flags) });
/* 1509 */         if (AprEndpoint.log.isTraceEnabled()) {
/* 1510 */           AprEndpoint.log.trace(msg, new Exception());
/*      */         } else {
/* 1512 */           AprEndpoint.log.debug(msg);
/*      */         }
/*      */       }
/* 1515 */       if (timeout <= 0L)
/*      */       {
/* 1517 */         timeout = 2147483647L;
/*      */       }
/* 1519 */       synchronized (this)
/*      */       {
/*      */ 
/* 1522 */         if (this.addList.add(socket, timeout, flags)) {
/* 1523 */           notify();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean addToPoller(long socket, int events)
/*      */     {
/* 1534 */       int rv = -1;
/* 1535 */       for (int i = 0; i < this.pollers.length; i++) {
/* 1536 */         if (this.pollerSpace[i] > 0) {
/* 1537 */           rv = Poll.add(this.pollers[i], socket, events);
/* 1538 */           if (rv == 0) {
/* 1539 */             this.pollerSpace[i] -= 1;
/* 1540 */             this.connectionCount.incrementAndGet();
/* 1541 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 1545 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private synchronized void close(long socket)
/*      */     {
/* 1555 */       this.closeList.add(socket, 0L, 0);
/* 1556 */       notify();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void removeFromPoller(long socket)
/*      */     {
/* 1565 */       if (AprEndpoint.log.isDebugEnabled()) {
/* 1566 */         AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.pollerRemove", new Object[] {
/* 1567 */           Long.valueOf(socket) }));
/*      */       }
/* 1569 */       int rv = -1;
/* 1570 */       for (int i = 0; i < this.pollers.length; i++) {
/* 1571 */         if (this.pollerSpace[i] < this.actualPollerSize) {
/* 1572 */           rv = Poll.remove(this.pollers[i], socket);
/* 1573 */           if (rv != 70015) {
/* 1574 */             this.pollerSpace[i] += 1;
/* 1575 */             this.connectionCount.decrementAndGet();
/* 1576 */             if (!AprEndpoint.log.isDebugEnabled()) break;
/* 1577 */             AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.pollerRemoved", new Object[] {
/* 1578 */               Long.valueOf(socket) })); break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1584 */       this.timeouts.remove(socket);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private synchronized void maintain()
/*      */     {
/* 1591 */       long date = System.currentTimeMillis();
/*      */       
/*      */ 
/* 1594 */       if (date - this.lastMaintain < 1000L) {
/* 1595 */         return;
/*      */       }
/* 1597 */       this.lastMaintain = date;
/*      */       
/* 1599 */       long socket = this.timeouts.check(date);
/* 1600 */       while (socket != 0L) {
/* 1601 */         if (AprEndpoint.log.isDebugEnabled()) {
/* 1602 */           AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.socketTimeout", new Object[] {
/* 1603 */             Long.valueOf(socket) }));
/*      */         }
/* 1605 */         SocketWrapperBase<Long> socketWrapper = (SocketWrapperBase)AprEndpoint.this.connections.get(Long.valueOf(socket));
/* 1606 */         socketWrapper.setError(new SocketTimeoutException());
/* 1607 */         AprEndpoint.this.processSocket(socketWrapper, SocketEvent.ERROR, true);
/* 1608 */         socket = this.timeouts.check(date);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1618 */       StringBuffer buf = new StringBuffer();
/* 1619 */       buf.append("Poller");
/* 1620 */       long[] res = new long[this.actualPollerSize * 2];
/* 1621 */       for (int i = 0; i < this.pollers.length; i++) {
/* 1622 */         int count = Poll.pollset(this.pollers[i], res);
/* 1623 */         buf.append(" [ ");
/* 1624 */         for (int j = 0; j < count; j++) {
/* 1625 */           buf.append(this.desc[(2 * j + 1)]).append(" ");
/*      */         }
/* 1627 */         buf.append("]");
/*      */       }
/* 1629 */       return buf.toString();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/* 1640 */       AprEndpoint.SocketList localAddList = new AprEndpoint.SocketList(AprEndpoint.this.getMaxConnections());
/* 1641 */       AprEndpoint.SocketList localCloseList = new AprEndpoint.SocketList(AprEndpoint.this.getMaxConnections());
/*      */       
/*      */ 
/* 1644 */       while (this.pollerRunning)
/*      */       {
/*      */ 
/* 1647 */         while ((this.pollerRunning) && (this.connectionCount.get() < 1) && 
/* 1648 */           (this.addList.size() < 1) && (this.closeList.size() < 1)) {
/*      */           try {
/* 1650 */             if ((AprEndpoint.this.getConnectionTimeout() > 0) && (this.pollerRunning)) {
/* 1651 */               maintain();
/*      */             }
/* 1653 */             synchronized (this)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1660 */               if ((this.addList.size() < 1) && (this.closeList.size() < 1)) {
/* 1661 */                 wait(10000L);
/*      */               }
/*      */             }
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}catch (Throwable t)
/*      */           {
/* 1667 */             ExceptionUtils.handleThrowable(t);
/* 1668 */             AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.timeout.err"));
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1673 */         if (!this.pollerRunning) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1680 */           synchronized (this) {
/* 1681 */             if (this.closeList.size() > 0)
/*      */             {
/*      */ 
/* 1684 */               this.closeList.duplicate(localCloseList);
/* 1685 */               this.closeList.clear();
/*      */             } else {
/* 1687 */               localCloseList.clear();
/*      */             }
/*      */           }
/* 1690 */           synchronized (this) {
/* 1691 */             if (this.addList.size() > 0)
/*      */             {
/*      */ 
/* 1694 */               this.addList.duplicate(localAddList);
/* 1695 */               this.addList.clear();
/*      */             } else {
/* 1697 */               localAddList.clear();
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1702 */           if (localCloseList.size() > 0) {
/* 1703 */             AprEndpoint.SocketInfo info = localCloseList.get();
/* 1704 */             while (info != null) {
/* 1705 */               localAddList.remove(info.socket);
/* 1706 */               removeFromPoller(info.socket);
/* 1707 */               AprEndpoint.this.destroySocket(info.socket);
/* 1708 */               info = localCloseList.get();
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1713 */           if (localAddList.size() > 0) {
/* 1714 */             AprEndpoint.SocketInfo info = localAddList.get();
/* 1715 */             while (info != null) {
/* 1716 */               if (AprEndpoint.log.isDebugEnabled()) {
/* 1717 */                 AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.pollerAddDo", new Object[] {
/*      */                 
/* 1719 */                   Long.valueOf(info.socket) }));
/*      */               }
/* 1721 */               this.timeouts.remove(info.socket);
/* 1722 */               AprEndpoint.AprSocketWrapper wrapper = (AprEndpoint.AprSocketWrapper)AprEndpoint.this.connections.get(
/* 1723 */                 Long.valueOf(info.socket));
/* 1724 */               if (wrapper != null)
/*      */               {
/*      */ 
/* 1727 */                 if ((info.read()) || (info.write())) {
/* 1728 */                   wrapper.pollerFlags = 
/*      */                   
/* 1730 */                     (wrapper.pollerFlags | (info.read() ? 1 : 0) | (info.write() ? 4 : 0));
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1736 */                   removeFromPoller(info.socket);
/* 1737 */                   if (!addToPoller(info.socket, wrapper.pollerFlags)) {
/* 1738 */                     AprEndpoint.this.closeSocket(info.socket);
/*      */                   } else {
/* 1740 */                     this.timeouts.add(info.socket, 
/* 1741 */                       System.currentTimeMillis() + info.timeout);
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/* 1746 */                   AprEndpoint.this.closeSocket(info.socket);
/* 1747 */                   AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.apr.pollAddInvalid", new Object[] { info }));
/*      */                 }
/*      */                 
/* 1750 */                 info = localAddList.get();
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 1755 */           for (int i = 0; i < this.pollers.length; i++)
/*      */           {
/*      */ 
/* 1758 */             boolean reset = false;
/*      */             
/* 1760 */             int rv = 0;
/*      */             
/* 1762 */             if (this.pollerSpace[i] < this.actualPollerSize) {
/* 1763 */               rv = Poll.poll(this.pollers[i], this.nextPollerTime, this.desc, true);
/*      */               
/* 1765 */               this.nextPollerTime = this.pollerTime;
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/* 1774 */               this.nextPollerTime += this.pollerTime;
/*      */             }
/* 1776 */             if (rv > 0) {
/* 1777 */               rv = mergeDescriptors(this.desc, rv);
/* 1778 */               this.pollerSpace[i] += rv;
/* 1779 */               this.connectionCount.addAndGet(-rv);
/* 1780 */               for (int n = 0; n < rv; n++) {
/* 1781 */                 if (AprEndpoint.this.getLog().isDebugEnabled()) {
/* 1782 */                   AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.pollerProcess", new Object[] {
/*      */                   
/* 1784 */                     Long.valueOf(this.desc[(n * 2 + 1)]), 
/* 1785 */                     Long.valueOf(this.desc[(n * 2)]) }));
/*      */                 }
/* 1787 */                 long timeout = this.timeouts.remove(this.desc[(n * 2 + 1)]);
/* 1788 */                 AprEndpoint.AprSocketWrapper wrapper = (AprEndpoint.AprSocketWrapper)AprEndpoint.this.connections.get(
/* 1789 */                   Long.valueOf(this.desc[(n * 2 + 1)]));
/* 1790 */                 if (wrapper != null)
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1796 */                   wrapper.pollerFlags = (wrapper.pollerFlags & ((int)this.desc[(n * 2)] ^ 0xFFFFFFFF));
/*      */                   
/* 1798 */                   if (((this.desc[(n * 2)] & 0x20) == 32L) || ((this.desc[(n * 2)] & 0x10) == 16L) || ((this.desc[(n * 2)] & 0x40) == 64L))
/*      */                   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1810 */                     if ((this.desc[(n * 2)] & 1L) == 1L)
/*      */                     {
/* 1812 */                       if (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_READ))
/*      */                       {
/* 1814 */                         AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                       }
/* 1816 */                     } else if ((this.desc[(n * 2)] & 0x4) == 4L)
/*      */                     {
/* 1818 */                       if (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_WRITE))
/*      */                       {
/* 1820 */                         AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                       }
/* 1822 */                     } else if ((wrapper.pollerFlags & 0x1) == 1)
/*      */                     {
/*      */ 
/* 1825 */                       if (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_READ))
/*      */                       {
/* 1827 */                         AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                       }
/* 1829 */                     } else if ((wrapper.pollerFlags & 0x4) == 4)
/*      */                     {
/*      */ 
/* 1832 */                       if (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_WRITE))
/*      */                       {
/* 1834 */                         AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                       }
/*      */                     }
/*      */                     else {
/* 1838 */                       AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                     }
/* 1840 */                   } else if (((this.desc[(n * 2)] & 1L) == 1L) || ((this.desc[(n * 2)] & 0x4) == 4L))
/*      */                   {
/* 1842 */                     boolean error = false;
/* 1843 */                     if (((this.desc[(n * 2)] & 1L) == 1L) && 
/* 1844 */                       (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_READ))) {
/* 1845 */                       error = true;
/*      */                       
/* 1847 */                       AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                     }
/* 1849 */                     if ((!error) && ((this.desc[(n * 2)] & 0x4) == 4L))
/*      */                     {
/* 1851 */                       if (!AprEndpoint.this.processSocket(this.desc[(n * 2 + 1)], SocketEvent.OPEN_WRITE))
/*      */                       {
/* 1853 */                         error = true;
/* 1854 */                         AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                       } }
/* 1856 */                     if ((!error) && (wrapper.pollerFlags != 0))
/*      */                     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1865 */                       if (timeout > 0L) {
/* 1866 */                         timeout -= System.currentTimeMillis();
/*      */                       }
/*      */                       
/*      */ 
/* 1870 */                       if (timeout <= 0L) {
/* 1871 */                         timeout = 1L;
/*      */                       }
/*      */                       
/*      */ 
/* 1875 */                       if (timeout > 2147483647L) {
/* 1876 */                         timeout = 2147483647L;
/*      */                       }
/* 1878 */                       add(this.desc[(n * 2 + 1)], (int)timeout, wrapper.pollerFlags);
/*      */                     }
/*      */                   }
/*      */                   else {
/* 1882 */                     AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.apr.pollUnknownEvent", new Object[] {
/*      */                     
/* 1884 */                       Long.valueOf(this.desc[(n * 2)]) }));
/*      */                     
/* 1886 */                     AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */                   }
/*      */                 }
/* 1889 */               } } else if (rv < 0) {
/* 1890 */               int errn = -rv;
/*      */               
/* 1892 */               if ((errn != 120001) && (errn != 120003)) {
/* 1893 */                 if (errn > 120000) {
/* 1894 */                   errn -= 120000;
/*      */                 }
/* 1896 */                 AprEndpoint.this.getLog().error(AbstractEndpoint.sm.getString("endpoint.apr.pollError", new Object[] {
/*      */                 
/* 1898 */                   Integer.valueOf(errn), 
/* 1899 */                   Error.strerror(errn) }));
/*      */                 
/* 1901 */                 reset = true;
/*      */               }
/*      */             }
/*      */             
/* 1905 */             if ((reset) && (this.pollerRunning))
/*      */             {
/* 1907 */               int count = Poll.pollset(this.pollers[i], this.desc);
/* 1908 */               long newPoller = AprEndpoint.this.allocatePoller(this.actualPollerSize, this.pool, -1);
/*      */               
/* 1910 */               this.pollerSpace[i] = this.actualPollerSize;
/* 1911 */               this.connectionCount.addAndGet(-count);
/* 1912 */               Poll.destroy(this.pollers[i]);
/* 1913 */               this.pollers[i] = newPoller;
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Throwable t) {
/* 1918 */           ExceptionUtils.handleThrowable(t);
/* 1919 */           AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.poll.error"), t);
/*      */         }
/*      */         try
/*      */         {
/* 1923 */           if ((AprEndpoint.this.getConnectionTimeout() > 0) && (this.pollerRunning))
/*      */           {
/*      */ 
/* 1926 */             maintain();
/*      */           }
/*      */         } catch (Throwable t) {
/* 1929 */           ExceptionUtils.handleThrowable(t);
/* 1930 */           AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.timeout.err"), t);
/*      */         }
/*      */       }
/*      */       
/* 1934 */       synchronized (this) {
/* 1935 */         notifyAll();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int mergeDescriptors(long[] desc, int startCount)
/*      */     {
/* 1951 */       HashMap<Long, Long> merged = new HashMap(startCount);
/* 1952 */       Long old; for (int n = 0; n < startCount; n++) {
/* 1953 */         old = (Long)merged.put(Long.valueOf(desc[(2 * n + 1)]), Long.valueOf(desc[(2 * n)]));
/* 1954 */         if (old != null)
/*      */         {
/* 1956 */           merged.put(Long.valueOf(desc[(2 * n + 1)]), 
/* 1957 */             Long.valueOf(desc[(2 * n)] | old.longValue()));
/* 1958 */           if (AprEndpoint.log.isDebugEnabled()) {
/* 1959 */             AprEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.apr.pollMergeEvents", new Object[] {
/* 1960 */               Long.valueOf(desc[(2 * n + 1)]), Long.valueOf(desc[(2 * n)]), old }));
/*      */           }
/*      */         }
/*      */       }
/* 1964 */       int i = 0;
/* 1965 */       for (Map.Entry<Long, Long> entry : merged.entrySet()) {
/* 1966 */         desc[(i++)] = ((Long)entry.getValue()).longValue();
/* 1967 */         desc[(i++)] = ((Long)entry.getKey()).longValue();
/*      */       }
/* 1969 */       return merged.size();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class SendfileData
/*      */     extends SendfileDataBase
/*      */   {
/*      */     protected long fd;
/*      */     
/*      */     protected long fdpool;
/*      */     
/*      */     protected long socket;
/*      */     
/*      */ 
/*      */     public SendfileData(String filename, long pos, long length)
/*      */     {
/* 1987 */       super(pos, length);
/*      */     }
/*      */   }
/*      */   
/*      */   public class Sendfile
/*      */     implements Runnable
/*      */   {
/*      */     public Sendfile() {}
/*      */     
/* 1996 */     protected long sendfilePollset = 0L;
/* 1997 */     protected long pool = 0L;
/*      */     protected long[] desc;
/*      */     
/*      */     protected HashMap<Long, AprEndpoint.SendfileData> sendfileData;
/*      */     protected int sendfileCount;
/* 2002 */     public int getSendfileCount() { return this.sendfileCount; }
/*      */     
/*      */ 
/*      */     protected ArrayList<AprEndpoint.SendfileData> addS;
/* 2006 */     private volatile boolean sendfileRunning = true;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void init()
/*      */     {
/* 2014 */       this.pool = Pool.create(AprEndpoint.this.serverSockPool);
/* 2015 */       int size = AprEndpoint.this.sendfileSize;
/* 2016 */       if (size <= 0) {
/* 2017 */         size = (OS.IS_WIN32) || (OS.IS_WIN64) ? 1024 : 16384;
/*      */       }
/* 2019 */       this.sendfilePollset = AprEndpoint.this.allocatePoller(size, this.pool, AprEndpoint.this.getConnectionTimeout());
/* 2020 */       if ((this.sendfilePollset == 0L) && (size > 1024)) {
/* 2021 */         size = 1024;
/* 2022 */         this.sendfilePollset = AprEndpoint.this.allocatePoller(size, this.pool, AprEndpoint.this.getConnectionTimeout());
/*      */       }
/* 2024 */       if (this.sendfilePollset == 0L) {
/* 2025 */         size = 62;
/* 2026 */         this.sendfilePollset = AprEndpoint.this.allocatePoller(size, this.pool, AprEndpoint.this.getConnectionTimeout());
/*      */       }
/* 2028 */       this.desc = new long[size * 2];
/* 2029 */       this.sendfileData = new HashMap(size);
/* 2030 */       this.addS = new ArrayList();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected void destroy()
/*      */     {
/* 2037 */       this.sendfileRunning = false;
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 2042 */         synchronized (this) {
/* 2043 */           notify();
/* 2044 */           wait(AprEndpoint.this.pollTime / 1000);
/*      */         }
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */       
/*      */ 
/* 2050 */       for (int i = this.addS.size() - 1; i >= 0; i--) {
/* 2051 */         AprEndpoint.SendfileData data = (AprEndpoint.SendfileData)this.addS.get(i);
/* 2052 */         AprEndpoint.this.closeSocket(data.socket);
/*      */       }
/*      */       
/* 2055 */       int rv = Poll.pollset(this.sendfilePollset, this.desc);
/* 2056 */       if (rv > 0) {
/* 2057 */         for (int n = 0; n < rv; n++) {
/* 2058 */           AprEndpoint.this.closeSocket(this.desc[(n * 2 + 1)]);
/*      */         }
/*      */       }
/* 2061 */       Pool.destroy(this.pool);
/* 2062 */       this.sendfileData.clear();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SendfileState add(AprEndpoint.SendfileData data)
/*      */     {
/*      */       try
/*      */       {
/* 2078 */         data.fdpool = Socket.pool(data.socket);
/*      */         
/* 2080 */         data.fd = File.open(data.fileName, 4129, 0, data.fdpool);
/*      */         
/*      */ 
/*      */ 
/* 2084 */         Socket.timeoutSet(data.socket, 0L);
/* 2085 */         while (this.sendfileRunning) {
/* 2086 */           long nw = Socket.sendfilen(data.socket, data.fd, data.pos, data.length, 0);
/*      */           
/* 2088 */           if (nw < 0L) {
/* 2089 */             if (-nw == 120002L) break;
/* 2090 */             Pool.destroy(data.fdpool);
/* 2091 */             data.socket = 0L;
/* 2092 */             return SendfileState.ERROR;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2098 */           data.pos += nw;
/* 2099 */           data.length -= nw;
/* 2100 */           if (data.length == 0L)
/*      */           {
/* 2102 */             Pool.destroy(data.fdpool);
/*      */             
/* 2104 */             Socket.timeoutSet(data.socket, AprEndpoint.this.getConnectionTimeout() * 1000);
/* 2105 */             return SendfileState.DONE;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/* 2110 */         AprEndpoint.log.warn(AbstractEndpoint.sm.getString("endpoint.sendfile.error"), e);
/* 2111 */         return SendfileState.ERROR;
/*      */       }
/*      */       
/*      */ 
/* 2115 */       synchronized (this) {
/* 2116 */         this.addS.add(data);
/* 2117 */         notify();
/*      */       }
/* 2119 */       return SendfileState.PENDING;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void remove(AprEndpoint.SendfileData data)
/*      */     {
/* 2128 */       int rv = Poll.remove(this.sendfilePollset, data.socket);
/* 2129 */       if (rv == 0) {
/* 2130 */         this.sendfileCount -= 1;
/*      */       }
/* 2132 */       this.sendfileData.remove(Long.valueOf(data.socket));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/* 2142 */       long maintainTime = 0L;
/*      */       
/* 2144 */       while (this.sendfileRunning)
/*      */       {
/*      */ 
/* 2147 */         while ((this.sendfileRunning) && (AprEndpoint.this.paused)) {
/*      */           try {
/* 2149 */             Thread.sleep(1000L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */         
/*      */ 
/* 2155 */         while ((this.sendfileRunning) && (this.sendfileCount < 1) && (this.addS.size() < 1))
/*      */         {
/* 2157 */           maintainTime = 0L;
/*      */           try {
/* 2159 */             synchronized (this) {
/* 2160 */               wait();
/*      */             }
/*      */           }
/*      */           catch (InterruptedException localInterruptedException1) {}
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2168 */         if (!this.sendfileRunning) {
/*      */           break;
/*      */         }
/*      */         
/*      */         try
/*      */         {
/* 2174 */           if (this.addS.size() > 0) {
/* 2175 */             synchronized (this) {
/* 2176 */               for (int i = this.addS.size() - 1; i >= 0; i--) {
/* 2177 */                 AprEndpoint.SendfileData data = (AprEndpoint.SendfileData)this.addS.get(i);
/* 2178 */                 int rv = Poll.add(this.sendfilePollset, data.socket, 4);
/* 2179 */                 if (rv == 0) {
/* 2180 */                   this.sendfileData.put(Long.valueOf(data.socket), data);
/* 2181 */                   this.sendfileCount += 1;
/*      */                 } else {
/* 2183 */                   AprEndpoint.this.getLog().warn(AbstractEndpoint.sm.getString("endpoint.sendfile.addfail", new Object[] {
/*      */                   
/* 2185 */                     Integer.valueOf(rv), 
/* 2186 */                     Error.strerror(rv) }));
/*      */                   
/* 2188 */                   AprEndpoint.this.closeSocket(data.socket);
/*      */                 }
/*      */               }
/* 2191 */               this.addS.clear();
/*      */             }
/*      */           }
/*      */           
/* 2195 */           maintainTime += AprEndpoint.this.pollTime;
/*      */           
/* 2197 */           int rv = Poll.poll(this.sendfilePollset, AprEndpoint.this.pollTime, this.desc, false);
/* 2198 */           if (rv > 0) {
/* 2199 */             for (int n = 0; n < rv; n++)
/*      */             {
/*      */ 
/* 2202 */               AprEndpoint.SendfileData state = (AprEndpoint.SendfileData)this.sendfileData.get(Long.valueOf(this.desc[(n * 2 + 1)]));
/*      */               
/* 2204 */               if (((this.desc[(n * 2)] & 0x20) == 32L) || ((this.desc[(n * 2)] & 0x10) == 16L))
/*      */               {
/*      */ 
/* 2207 */                 remove(state);
/*      */                 
/*      */ 
/* 2210 */                 AprEndpoint.this.closeSocket(state.socket);
/*      */               }
/*      */               else
/*      */               {
/* 2214 */                 long nw = Socket.sendfilen(state.socket, state.fd, state.pos, state.length, 0);
/*      */                 
/*      */ 
/* 2217 */                 if (nw < 0L)
/*      */                 {
/* 2219 */                   remove(state);
/*      */                   
/*      */ 
/* 2222 */                   AprEndpoint.this.closeSocket(state.socket);
/*      */                 }
/*      */                 else
/*      */                 {
/* 2226 */                   state.pos += nw;
/* 2227 */                   state.length -= nw;
/* 2228 */                   if (state.length == 0L) {
/* 2229 */                     remove(state);
/* 2230 */                     switch (AprEndpoint.1.$SwitchMap$org$apache$tomcat$util$net$SendfileKeepAliveState[state.keepAliveState.ordinal()])
/*      */                     {
/*      */ 
/*      */                     case 1: 
/* 2234 */                       AprEndpoint.this.closeSocket(state.socket);
/* 2235 */                       break;
/*      */                     
/*      */ 
/*      */                     case 2: 
/* 2239 */                       Pool.destroy(state.fdpool);
/* 2240 */                       Socket.timeoutSet(state.socket, AprEndpoint.this.getConnectionTimeout() * 1000);
/*      */                       
/* 2242 */                       if (!AprEndpoint.this.processSocket(state.socket, SocketEvent.OPEN_READ)) {
/* 2243 */                         AprEndpoint.this.closeSocket(state.socket);
/*      */                       }
/*      */                       
/*      */ 
/*      */                       break;
/*      */                     case 3: 
/* 2249 */                       Pool.destroy(state.fdpool);
/* 2250 */                       Socket.timeoutSet(state.socket, AprEndpoint.this.getConnectionTimeout() * 1000);
/*      */                       
/*      */ 
/* 2253 */                       AprEndpoint.Poller.access$500(AprEndpoint.this.getPoller(), state.socket, AprEndpoint.this.getKeepAliveTimeout(), 1);
/*      */                     }
/*      */                     
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/* 2260 */           } else if (rv < 0) {
/* 2261 */             int errn = -rv;
/*      */             
/* 2263 */             if ((errn != 120001) && (errn != 120003)) {
/* 2264 */               if (errn > 120000) {
/* 2265 */                 errn -= 120000;
/*      */               }
/* 2267 */               AprEndpoint.this.getLog().error(AbstractEndpoint.sm.getString("endpoint.apr.pollError", new Object[] {
/*      */               
/* 2269 */                 Integer.valueOf(errn), 
/* 2270 */                 Error.strerror(errn) }));
/*      */               
/* 2272 */               synchronized (this) {
/* 2273 */                 destroy();
/* 2274 */                 init();
/*      */               }
/* 2276 */               continue;
/*      */             }
/*      */           }
/*      */           
/* 2280 */           if ((AprEndpoint.this.getConnectionTimeout() > 0) && (maintainTime > 1000000L) && (this.sendfileRunning))
/*      */           {
/* 2282 */             rv = Poll.maintain(this.sendfilePollset, this.desc, false);
/* 2283 */             maintainTime = 0L;
/* 2284 */             if (rv > 0) {
/* 2285 */               for (int n = 0; n < rv; n++)
/*      */               {
/* 2287 */                 AprEndpoint.SendfileData state = (AprEndpoint.SendfileData)this.sendfileData.get(Long.valueOf(this.desc[n]));
/*      */                 
/* 2289 */                 remove(state);
/*      */                 
/*      */ 
/* 2292 */                 AprEndpoint.this.closeSocket(state.socket);
/*      */               }
/*      */             }
/*      */           }
/*      */         } catch (Throwable t) {
/* 2297 */           ExceptionUtils.handleThrowable(t);
/* 2298 */           AprEndpoint.this.getLog().error(AbstractEndpoint.sm.getString("endpoint.poll.error"), t);
/*      */         }
/*      */       }
/*      */       
/* 2302 */       synchronized (this) {
/* 2303 */         notifyAll();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class SocketWithOptionsProcessor
/*      */     implements Runnable
/*      */   {
/* 2322 */     protected SocketWrapperBase<Long> socket = null;
/*      */     
/*      */     public SocketWithOptionsProcessor()
/*      */     {
/* 2326 */       this.socket = socket;
/*      */     }
/*      */     
/*      */ 
/*      */     public void run()
/*      */     {
/* 2332 */       synchronized (this.socket) {
/* 2333 */         if (!AprEndpoint.this.deferAccept) {
/* 2334 */           if (AprEndpoint.this.setSocketOptions(this.socket)) {
/* 2335 */             AprEndpoint.Poller.access$500(AprEndpoint.this.getPoller(), ((Long)this.socket.getSocket()).longValue(), AprEndpoint.this
/* 2336 */               .getConnectionTimeout(), 1);
/*      */           }
/*      */           else {
/* 2339 */             AprEndpoint.this.closeSocket(((Long)this.socket.getSocket()).longValue());
/* 2340 */             this.socket = null;
/*      */           }
/*      */         }
/*      */         else {
/* 2344 */           if (!AprEndpoint.this.setSocketOptions(this.socket))
/*      */           {
/* 2346 */             AprEndpoint.this.closeSocket(((Long)this.socket.getSocket()).longValue());
/* 2347 */             this.socket = null;
/* 2348 */             return;
/*      */           }
/*      */           
/* 2351 */           AbstractEndpoint.Handler.SocketState state = AprEndpoint.this.getHandler().process(this.socket, SocketEvent.OPEN_READ);
/*      */           
/* 2353 */           if (state == AbstractEndpoint.Handler.SocketState.CLOSED)
/*      */           {
/* 2355 */             AprEndpoint.this.closeSocket(((Long)this.socket.getSocket()).longValue());
/* 2356 */             this.socket = null;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class SocketProcessor
/*      */     extends SocketProcessorBase<Long>
/*      */   {
/*      */     public SocketProcessor(SocketEvent socketWrapper)
/*      */     {
/* 2374 */       super(event);
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     protected void doRun()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   4: invokevirtual 3	org/apache/tomcat/util/net/AprEndpoint:getHandler	()Lorg/apache/tomcat/util/net/AbstractEndpoint$Handler;
/*      */       //   7: aload_0
/*      */       //   8: getfield 4	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:socketWrapper	Lorg/apache/tomcat/util/net/SocketWrapperBase;
/*      */       //   11: aload_0
/*      */       //   12: getfield 5	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:event	Lorg/apache/tomcat/util/net/SocketEvent;
/*      */       //   15: invokeinterface 6 3 0
/*      */       //   20: astore_1
/*      */       //   21: aload_1
/*      */       //   22: getstatic 7	org/apache/tomcat/util/net/AbstractEndpoint$Handler$SocketState:CLOSED	Lorg/apache/tomcat/util/net/AbstractEndpoint$Handler$SocketState;
/*      */       //   25: if_acmpne +23 -> 48
/*      */       //   28: aload_0
/*      */       //   29: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   32: aload_0
/*      */       //   33: getfield 4	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:socketWrapper	Lorg/apache/tomcat/util/net/SocketWrapperBase;
/*      */       //   36: invokevirtual 8	org/apache/tomcat/util/net/SocketWrapperBase:getSocket	()Ljava/lang/Object;
/*      */       //   39: checkcast 9	java/lang/Long
/*      */       //   42: invokevirtual 10	java/lang/Long:longValue	()J
/*      */       //   45: invokestatic 11	org/apache/tomcat/util/net/AprEndpoint:access$000	(Lorg/apache/tomcat/util/net/AprEndpoint;J)V
/*      */       //   48: aload_0
/*      */       //   49: aconst_null
/*      */       //   50: putfield 4	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:socketWrapper	Lorg/apache/tomcat/util/net/SocketWrapperBase;
/*      */       //   53: aload_0
/*      */       //   54: aconst_null
/*      */       //   55: putfield 5	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:event	Lorg/apache/tomcat/util/net/SocketEvent;
/*      */       //   58: aload_0
/*      */       //   59: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   62: getfield 12	org/apache/tomcat/util/net/AprEndpoint:running	Z
/*      */       //   65: ifeq +73 -> 138
/*      */       //   68: aload_0
/*      */       //   69: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   72: getfield 13	org/apache/tomcat/util/net/AprEndpoint:paused	Z
/*      */       //   75: ifne +63 -> 138
/*      */       //   78: aload_0
/*      */       //   79: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   82: getfield 14	org/apache/tomcat/util/net/AprEndpoint:processorCache	Lorg/apache/tomcat/util/collections/SynchronizedStack;
/*      */       //   85: aload_0
/*      */       //   86: invokevirtual 15	org/apache/tomcat/util/collections/SynchronizedStack:push	(Ljava/lang/Object;)Z
/*      */       //   89: pop
/*      */       //   90: goto +48 -> 138
/*      */       //   93: astore_2
/*      */       //   94: aload_0
/*      */       //   95: aconst_null
/*      */       //   96: putfield 4	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:socketWrapper	Lorg/apache/tomcat/util/net/SocketWrapperBase;
/*      */       //   99: aload_0
/*      */       //   100: aconst_null
/*      */       //   101: putfield 5	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:event	Lorg/apache/tomcat/util/net/SocketEvent;
/*      */       //   104: aload_0
/*      */       //   105: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   108: getfield 12	org/apache/tomcat/util/net/AprEndpoint:running	Z
/*      */       //   111: ifeq +25 -> 136
/*      */       //   114: aload_0
/*      */       //   115: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   118: getfield 13	org/apache/tomcat/util/net/AprEndpoint:paused	Z
/*      */       //   121: ifne +15 -> 136
/*      */       //   124: aload_0
/*      */       //   125: getfield 1	org/apache/tomcat/util/net/AprEndpoint$SocketProcessor:this$0	Lorg/apache/tomcat/util/net/AprEndpoint;
/*      */       //   128: getfield 14	org/apache/tomcat/util/net/AprEndpoint:processorCache	Lorg/apache/tomcat/util/collections/SynchronizedStack;
/*      */       //   131: aload_0
/*      */       //   132: invokevirtual 15	org/apache/tomcat/util/collections/SynchronizedStack:push	(Ljava/lang/Object;)Z
/*      */       //   135: pop
/*      */       //   136: aload_2
/*      */       //   137: athrow
/*      */       //   138: return
/*      */       // Line number table:
/*      */       //   Java source line #2381	-> byte code offset #0
/*      */       //   Java source line #2382	-> byte code offset #21
/*      */       //   Java source line #2384	-> byte code offset #28
/*      */       //   Java source line #2387	-> byte code offset #48
/*      */       //   Java source line #2388	-> byte code offset #53
/*      */       //   Java source line #2390	-> byte code offset #58
/*      */       //   Java source line #2391	-> byte code offset #78
/*      */       //   Java source line #2387	-> byte code offset #93
/*      */       //   Java source line #2388	-> byte code offset #99
/*      */       //   Java source line #2390	-> byte code offset #104
/*      */       //   Java source line #2391	-> byte code offset #124
/*      */       //   Java source line #2394	-> byte code offset #138
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	139	0	this	SocketProcessor
/*      */       //   20	2	1	state	AbstractEndpoint.Handler.SocketState
/*      */       //   93	44	2	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   0	48	93	finally
/*      */     }
/*      */   }
/*      */   
/*      */   public static class AprSocketWrapper
/*      */     extends SocketWrapperBase<Long>
/*      */   {
/*      */     private static final int SSL_OUTPUT_BUFFER_SIZE = 8192;
/*      */     private final ByteBuffer sslOutputBuffer;
/* 2404 */     private final Object closedLock = new Object();
/* 2405 */     private volatile boolean closed = false;
/*      */     
/*      */ 
/* 2408 */     private int pollerFlags = 0;
/*      */     
/*      */     public AprSocketWrapper(Long socket, AprEndpoint endpoint)
/*      */     {
/* 2412 */       super(endpoint);
/*      */       
/*      */ 
/*      */ 
/* 2416 */       if (endpoint.isSSLEnabled()) {
/* 2417 */         this.sslOutputBuffer = ByteBuffer.allocateDirect(8192);
/* 2418 */         this.sslOutputBuffer.position(8192);
/*      */       } else {
/* 2420 */         this.sslOutputBuffer = null;
/*      */       }
/*      */       
/* 2423 */       this.socketBufferHandler = new SocketBufferHandler(9000, 9000, true);
/*      */     }
/*      */     
/*      */     public int read(boolean block, byte[] b, int off, int len)
/*      */       throws IOException
/*      */     {
/* 2429 */       int nRead = populateReadBuffer(b, off, len);
/* 2430 */       if (nRead > 0) {
/* 2431 */         return nRead;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2442 */       nRead = fillReadBuffer(block);
/*      */       
/*      */ 
/*      */ 
/* 2446 */       if (nRead > 0) {
/* 2447 */         this.socketBufferHandler.configureReadBufferForRead();
/* 2448 */         nRead = Math.min(nRead, len);
/* 2449 */         this.socketBufferHandler.getReadBuffer().get(b, off, nRead);
/*      */       }
/* 2451 */       return nRead;
/*      */     }
/*      */     
/*      */     public int read(boolean block, ByteBuffer to)
/*      */       throws IOException
/*      */     {
/* 2457 */       int nRead = populateReadBuffer(to);
/* 2458 */       if (nRead > 0) {
/* 2459 */         return nRead;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2470 */       int limit = this.socketBufferHandler.getReadBuffer().capacity();
/* 2471 */       if ((to.isDirect()) && (to.remaining() >= limit)) {
/* 2472 */         to.limit(to.position() + limit);
/* 2473 */         nRead = fillReadBuffer(block, to);
/*      */       }
/*      */       else {
/* 2476 */         nRead = fillReadBuffer(block);
/*      */         
/*      */ 
/*      */ 
/* 2480 */         if (nRead > 0) {
/* 2481 */           nRead = populateReadBuffer(to);
/*      */         }
/*      */       }
/* 2484 */       return nRead;
/*      */     }
/*      */     
/*      */     private int fillReadBuffer(boolean block) throws IOException
/*      */     {
/* 2489 */       this.socketBufferHandler.configureReadBufferForWrite();
/* 2490 */       return fillReadBuffer(block, this.socketBufferHandler.getReadBuffer());
/*      */     }
/*      */     
/*      */     private int fillReadBuffer(boolean block, ByteBuffer to) throws IOException
/*      */     {
/* 2495 */       if (this.closed) {
/* 2496 */         throw new IOException(sm.getString("socket.apr.closed", new Object[] { getSocket() }));
/*      */       }
/*      */       
/* 2499 */       Lock readLock = getBlockingStatusReadLock();
/* 2500 */       ReentrantReadWriteLock.WriteLock writeLock = getBlockingStatusWriteLock();
/*      */       
/* 2502 */       boolean readDone = false;
/* 2503 */       int result = 0;
/* 2504 */       readLock.lock();
/*      */       try {
/* 2506 */         if (getBlockingStatus() == block) {
/* 2507 */           if (block) {
/* 2508 */             Socket.timeoutSet(((Long)getSocket()).longValue(), getReadTimeout() * 1000L);
/*      */           }
/* 2510 */           result = Socket.recvb(((Long)getSocket()).longValue(), to, to.position(), to
/* 2511 */             .remaining());
/* 2512 */           readDone = true;
/*      */         }
/*      */       } finally {
/* 2515 */         readLock.unlock();
/*      */       }
/*      */       
/* 2518 */       if (!readDone) {
/* 2519 */         writeLock.lock();
/*      */         try
/*      */         {
/* 2522 */           setBlockingStatus(block);
/* 2523 */           if (block) {
/* 2524 */             Socket.timeoutSet(((Long)getSocket()).longValue(), getReadTimeout() * 1000L);
/*      */           } else {
/* 2526 */             Socket.timeoutSet(((Long)getSocket()).longValue(), 0L);
/*      */           }
/*      */           
/* 2529 */           readLock.lock();
/*      */           try {
/* 2531 */             writeLock.unlock();
/* 2532 */             result = Socket.recvb(((Long)getSocket()).longValue(), to, to.position(), to
/* 2533 */               .remaining());
/*      */           } finally {
/* 2535 */             readLock.unlock();
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 2540 */           if (writeLock.isHeldByCurrentThread()) {
/* 2541 */             writeLock.unlock();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2546 */       if (result > 0) {
/* 2547 */         to.position(to.position() + result);
/* 2548 */         return result; }
/* 2549 */       if ((result == 0) || (-result == 120002))
/* 2550 */         return 0;
/* 2551 */       if ((-result == 120005) || (-result == 120001)) {
/* 2552 */         if (block) {
/* 2553 */           throw new SocketTimeoutException(sm.getString("iib.readtimeout"));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2561 */         return 0;
/*      */       }
/* 2563 */       if (-result == 70014)
/* 2564 */         return -1;
/* 2565 */       if (((OS.IS_WIN32) || (OS.IS_WIN64)) && (-result == 730053))
/*      */       {
/*      */ 
/* 2568 */         throw new EOFException(sm.getString("socket.apr.clientAbort"));
/*      */       }
/* 2570 */       throw new IOException(sm.getString("socket.apr.read.error", new Object[] {
/* 2571 */         Integer.valueOf(-result), getSocket(), this }));
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isReadyForRead()
/*      */       throws IOException
/*      */     {
/* 2578 */       this.socketBufferHandler.configureReadBufferForRead();
/*      */       
/* 2580 */       if (this.socketBufferHandler.getReadBuffer().remaining() > 0) {
/* 2581 */         return true;
/*      */       }
/*      */       
/* 2584 */       int read = fillReadBuffer(false);
/*      */       
/* 2586 */       boolean isReady = (this.socketBufferHandler.getReadBuffer().position() > 0) || (read == -1);
/* 2587 */       return isReady;
/*      */     }
/*      */     
/*      */ 
/*      */     public void close()
/*      */     {
/* 2593 */       getEndpoint().getHandler().release(this);
/* 2594 */       synchronized (this.closedLock)
/*      */       {
/*      */ 
/* 2597 */         if (this.closed) {
/* 2598 */           return;
/*      */         }
/* 2600 */         this.closed = true;
/* 2601 */         if (this.sslOutputBuffer != null) {
/* 2602 */           ByteBufferUtils.cleanDirectBuffer(this.sslOutputBuffer);
/*      */         }
/* 2604 */         AprEndpoint.Poller.access$600(((AprEndpoint)getEndpoint()).getPoller(), ((Long)getSocket()).longValue());
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     public boolean isClosed()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 5	org/apache/tomcat/util/net/AprEndpoint$AprSocketWrapper:closedLock	Ljava/lang/Object;
/*      */       //   4: dup
/*      */       //   5: astore_1
/*      */       //   6: monitorenter
/*      */       //   7: aload_0
/*      */       //   8: getfield 6	org/apache/tomcat/util/net/AprEndpoint$AprSocketWrapper:closed	Z
/*      */       //   11: aload_1
/*      */       //   12: monitorexit
/*      */       //   13: ireturn
/*      */       //   14: astore_2
/*      */       //   15: aload_1
/*      */       //   16: monitorexit
/*      */       //   17: aload_2
/*      */       //   18: athrow
/*      */       // Line number table:
/*      */       //   Java source line #2611	-> byte code offset #0
/*      */       //   Java source line #2612	-> byte code offset #7
/*      */       //   Java source line #2613	-> byte code offset #14
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	19	0	this	AprSocketWrapper
/*      */       //   5	11	1	Ljava/lang/Object;	Object
/*      */       //   14	4	2	localObject1	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	13	14	finally
/*      */       //   14	17	14	finally
/*      */     }
/*      */     
/*      */     protected void writeByteBufferBlocking(ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 2619 */       if (from.isDirect()) {
/* 2620 */         super.writeByteBufferBlocking(from);
/*      */       }
/*      */       else {
/* 2623 */         ByteBuffer writeBuffer = this.socketBufferHandler.getWriteBuffer();
/* 2624 */         int limit = writeBuffer.capacity();
/* 2625 */         while (from.remaining() >= limit) {
/* 2626 */           this.socketBufferHandler.configureWriteBufferForWrite();
/* 2627 */           transfer(from, writeBuffer);
/* 2628 */           doWrite(true);
/*      */         }
/*      */         
/* 2631 */         if (from.remaining() > 0) {
/* 2632 */           this.socketBufferHandler.configureWriteBufferForWrite();
/* 2633 */           transfer(from, writeBuffer);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected boolean writeByteBufferNonBlocking(ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 2641 */       if (from.isDirect()) {
/* 2642 */         return super.writeByteBufferNonBlocking(from);
/*      */       }
/*      */       
/* 2645 */       ByteBuffer writeBuffer = this.socketBufferHandler.getWriteBuffer();
/* 2646 */       int limit = writeBuffer.capacity();
/* 2647 */       while (from.remaining() >= limit) {
/* 2648 */         this.socketBufferHandler.configureWriteBufferForWrite();
/* 2649 */         transfer(from, writeBuffer);
/* 2650 */         int newPosition = writeBuffer.position() + limit;
/* 2651 */         doWrite(false);
/* 2652 */         if (writeBuffer.position() != newPosition)
/*      */         {
/*      */ 
/*      */ 
/* 2656 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 2660 */       if (from.remaining() > 0) {
/* 2661 */         this.socketBufferHandler.configureWriteBufferForWrite();
/* 2662 */         transfer(from, writeBuffer);
/*      */       }
/*      */       
/* 2665 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void doWrite(boolean block, ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 2672 */       if (this.closed) {
/* 2673 */         throw new IOException(sm.getString("socket.apr.closed", new Object[] { getSocket() }));
/*      */       }
/*      */       
/* 2676 */       Lock readLock = getBlockingStatusReadLock();
/* 2677 */       ReentrantReadWriteLock.WriteLock writeLock = getBlockingStatusWriteLock();
/*      */       
/* 2679 */       readLock.lock();
/*      */       try {
/* 2681 */         if (getBlockingStatus() == block) {
/* 2682 */           if (block) {
/* 2683 */             Socket.timeoutSet(((Long)getSocket()).longValue(), getWriteTimeout() * 1000L);
/*      */           }
/* 2685 */           doWriteInternal(from);
/* 2686 */           return;
/*      */         }
/*      */       } finally {
/* 2689 */         readLock.unlock();
/*      */       }
/*      */       
/* 2692 */       writeLock.lock();
/*      */       try
/*      */       {
/* 2695 */         setBlockingStatus(block);
/* 2696 */         if (block) {
/* 2697 */           Socket.timeoutSet(((Long)getSocket()).longValue(), getWriteTimeout() * 1000L);
/*      */         } else {
/* 2699 */           Socket.timeoutSet(((Long)getSocket()).longValue(), 0L);
/*      */         }
/*      */         
/*      */ 
/* 2703 */         readLock.lock();
/*      */         try {
/* 2705 */           writeLock.unlock();
/* 2706 */           doWriteInternal(from);
/*      */         } finally {
/* 2708 */           readLock.unlock();
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 2713 */         if (writeLock.isHeldByCurrentThread()) {
/* 2714 */           writeLock.unlock();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void doWriteInternal(ByteBuffer from) throws IOException
/*      */     {
/*      */       int thisTime;
/*      */       do
/*      */       {
/* 2724 */         thisTime = 0;
/* 2725 */         if (getEndpoint().isSSLEnabled()) {
/* 2726 */           if (this.sslOutputBuffer.remaining() == 0)
/*      */           {
/* 2728 */             this.sslOutputBuffer.clear();
/* 2729 */             transfer(from, this.sslOutputBuffer);
/* 2730 */             this.sslOutputBuffer.flip();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2736 */           thisTime = Socket.sendb(((Long)getSocket()).longValue(), this.sslOutputBuffer, this.sslOutputBuffer
/* 2737 */             .position(), this.sslOutputBuffer.limit());
/* 2738 */           if (thisTime > 0) {
/* 2739 */             this.sslOutputBuffer.position(this.sslOutputBuffer.position() + thisTime);
/*      */           }
/*      */         } else {
/* 2742 */           thisTime = Socket.sendb(((Long)getSocket()).longValue(), from, from.position(), from
/* 2743 */             .remaining());
/* 2744 */           if (thisTime > 0) {
/* 2745 */             from.position(from.position() + thisTime);
/*      */           }
/*      */         }
/* 2748 */         if (Status.APR_STATUS_IS_EAGAIN(-thisTime)) {
/* 2749 */           thisTime = 0;
/* 2750 */         } else { if (-thisTime == 70014)
/* 2751 */             throw new EOFException(sm.getString("socket.apr.clientAbort"));
/* 2752 */           if (((OS.IS_WIN32) || (OS.IS_WIN64)) && (-thisTime == 730053))
/*      */           {
/*      */ 
/* 2755 */             throw new EOFException(sm.getString("socket.apr.clientAbort")); }
/* 2756 */           if (thisTime < 0)
/* 2757 */             throw new IOException(sm.getString("socket.apr.write.error", new Object[] {
/* 2758 */               Integer.valueOf(-thisTime), getSocket(), this }));
/*      */         }
/* 2760 */       } while (((thisTime > 0) || (getBlockingStatus())) && (from.hasRemaining()));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void registerReadInterest()
/*      */     {
/* 2772 */       synchronized (this.closedLock) {
/* 2773 */         if (this.closed) {
/* 2774 */           return;
/*      */         }
/* 2776 */         AprEndpoint.Poller p = ((AprEndpoint)getEndpoint()).getPoller();
/* 2777 */         if (p != null) {
/* 2778 */           AprEndpoint.Poller.access$500(p, ((Long)getSocket()).longValue(), getReadTimeout(), 1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void registerWriteInterest()
/*      */     {
/* 2787 */       synchronized (this.closedLock) {
/* 2788 */         if (this.closed) {
/* 2789 */           return;
/*      */         }
/* 2791 */         AprEndpoint.Poller.access$500(((AprEndpoint)getEndpoint()).getPoller(), 
/* 2792 */           ((Long)getSocket()).longValue(), getWriteTimeout(), 4);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileDataBase createSendfileData(String filename, long pos, long length)
/*      */     {
/* 2799 */       return new AprEndpoint.SendfileData(filename, pos, length);
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileState processSendfile(SendfileDataBase sendfileData)
/*      */     {
/* 2805 */       ((AprEndpoint.SendfileData)sendfileData).socket = ((Long)getSocket()).longValue();
/* 2806 */       return ((AprEndpoint)getEndpoint()).getSendfile().add((AprEndpoint.SendfileData)sendfileData);
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteAddr()
/*      */     {
/* 2812 */       if (this.closed) {
/* 2813 */         return;
/*      */       }
/*      */       try {
/* 2816 */         long socket = ((Long)getSocket()).longValue();
/* 2817 */         long sa = Address.get(1, socket);
/* 2818 */         this.remoteAddr = Address.getip(sa);
/*      */       } catch (Exception e) {
/* 2820 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noRemoteAddr", new Object[] { getSocket() }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteHost()
/*      */     {
/* 2827 */       if (this.closed) {
/* 2828 */         return;
/*      */       }
/*      */       try {
/* 2831 */         long socket = ((Long)getSocket()).longValue();
/* 2832 */         long sa = Address.get(1, socket);
/* 2833 */         this.remoteHost = Address.getnameinfo(sa, 0);
/* 2834 */         if (this.remoteAddr == null) {
/* 2835 */           this.remoteAddr = Address.getip(sa);
/*      */         }
/*      */       } catch (Exception e) {
/* 2838 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noRemoteHost", new Object[] { getSocket() }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemotePort()
/*      */     {
/* 2845 */       if (this.closed) {
/* 2846 */         return;
/*      */       }
/*      */       try {
/* 2849 */         long socket = ((Long)getSocket()).longValue();
/* 2850 */         long sa = Address.get(1, socket);
/* 2851 */         Sockaddr addr = Address.getInfo(sa);
/* 2852 */         this.remotePort = addr.port;
/*      */       } catch (Exception e) {
/* 2854 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noRemotePort", new Object[] { getSocket() }), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalName()
/*      */     {
/* 2861 */       if (this.closed) {
/* 2862 */         return;
/*      */       }
/*      */       try {
/* 2865 */         long socket = ((Long)getSocket()).longValue();
/* 2866 */         long sa = Address.get(0, socket);
/* 2867 */         this.localName = Address.getnameinfo(sa, 0);
/*      */       } catch (Exception e) {
/* 2869 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noLocalName"), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalAddr()
/*      */     {
/* 2876 */       if (this.closed) {
/* 2877 */         return;
/*      */       }
/*      */       try {
/* 2880 */         long socket = ((Long)getSocket()).longValue();
/* 2881 */         long sa = Address.get(0, socket);
/* 2882 */         this.localAddr = Address.getip(sa);
/*      */       } catch (Exception e) {
/* 2884 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noLocalAddr"), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalPort()
/*      */     {
/* 2891 */       if (this.closed) {
/* 2892 */         return;
/*      */       }
/*      */       try {
/* 2895 */         long socket = ((Long)getSocket()).longValue();
/* 2896 */         long sa = Address.get(0, socket);
/* 2897 */         Sockaddr addr = Address.getInfo(sa);
/* 2898 */         this.localPort = addr.port;
/*      */       } catch (Exception e) {
/* 2900 */         AprEndpoint.log.warn(sm.getString("endpoint.warn.noLocalPort"), e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public SSLSupport getSslSupport(String clientCertProvider)
/*      */     {
/* 2907 */       if (getEndpoint().isSSLEnabled()) {
/* 2908 */         return new AprSSLSupport(this, clientCertProvider);
/*      */       }
/* 2910 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public void doClientAuth(SSLSupport sslSupport)
/*      */       throws IOException
/*      */     {
/* 2917 */       long socket = ((Long)getSocket()).longValue();
/*      */       try
/*      */       {
/* 2920 */         SSLSocket.setVerify(socket, 2, -1);
/* 2921 */         SSLSocket.renegotiate(socket);
/*      */       } catch (Throwable t) {
/* 2923 */         ExceptionUtils.handleThrowable(t);
/* 2924 */         throw new IOException(sm.getString("socket.sslreneg"), t);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setAppReadBufHandler(ApplicationBufferHandler handler) {}
/*      */     
/*      */ 
/*      */     String getSSLInfoS(int id)
/*      */     {
/* 2935 */       synchronized (this.closedLock) {
/* 2936 */         if (this.closed) {
/* 2937 */           return null;
/*      */         }
/*      */         try {
/* 2940 */           return SSLSocket.getInfoS(((Long)getSocket()).longValue(), id);
/*      */         } catch (Exception e) {
/* 2942 */           throw new IllegalStateException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     int getSSLInfoI(int id) {
/* 2948 */       synchronized (this.closedLock) {
/* 2949 */         if (this.closed) {
/* 2950 */           return 0;
/*      */         }
/*      */         try {
/* 2953 */           return SSLSocket.getInfoI(((Long)getSocket()).longValue(), id);
/*      */         } catch (Exception e) {
/* 2955 */           throw new IllegalStateException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     byte[] getSSLInfoB(int id) {
/* 2961 */       synchronized (this.closedLock) {
/* 2962 */         if (this.closed) {
/* 2963 */           return null;
/*      */         }
/*      */         try {
/* 2966 */           return SSLSocket.getInfoB(((Long)getSocket()).longValue(), id);
/*      */         } catch (Exception e) {
/* 2968 */           throw new IllegalStateException(e);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\AprEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */