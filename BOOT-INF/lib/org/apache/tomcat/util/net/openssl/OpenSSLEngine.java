/*      */ package org.apache.tomcat.util.net.openssl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ReadOnlyBufferException;
/*      */ import java.security.Principal;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLPeerUnverifiedException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSessionBindingEvent;
/*      */ import javax.net.ssl.SSLSessionBindingListener;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ import javax.security.cert.CertificateException;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.jni.Buffer;
/*      */ import org.apache.tomcat.jni.Pool;
/*      */ import org.apache.tomcat.jni.SSL;
/*      */ import org.apache.tomcat.jni.SSLContext;
/*      */ import org.apache.tomcat.util.buf.ByteBufferUtils;
/*      */ import org.apache.tomcat.util.net.SSLUtil.ProtocolInfo;
/*      */ import org.apache.tomcat.util.net.openssl.ciphers.OpenSSLCipherConfigurationParser;
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
/*      */ public final class OpenSSLEngine
/*      */   extends SSLEngine
/*      */   implements SSLUtil.ProtocolInfo
/*      */ {
/*   61 */   private static final Log logger = LogFactory.getLog(OpenSSLEngine.class);
/*   62 */   private static final StringManager sm = StringManager.getManager(OpenSSLEngine.class);
/*      */   
/*   64 */   private static final Certificate[] EMPTY_CERTIFICATES = new Certificate[0];
/*      */   public static final Set<String> AVAILABLE_CIPHER_SUITES;
/*      */   private static final int MAX_PLAINTEXT_LENGTH = 16384;
/*      */   
/*      */   static {
/*   69 */     Set<String> availableCipherSuites = new LinkedHashSet(128);
/*   70 */     long aprPool = Pool.create(0L);
/*      */     try {
/*   72 */       long sslCtx = SSLContext.make(aprPool, 28, 1);
/*      */       try {
/*   74 */         SSLContext.setOptions(sslCtx, 4095);
/*   75 */         SSLContext.setCipherSuite(sslCtx, "ALL");
/*   76 */         long ssl = SSL.newSSL(sslCtx, true);
/*      */         try {
/*   78 */           for (String c : SSL.getCiphers(ssl))
/*      */           {
/*   80 */             if ((c != null) && (c.length() != 0) && (!availableCipherSuites.contains(c)))
/*      */             {
/*      */ 
/*   83 */               availableCipherSuites.add(OpenSSLCipherConfigurationParser.openSSLToJsse(c));
/*      */             }
/*      */           }
/*      */         }
/*      */         finally {}
/*      */       } finally {
/*   89 */         SSLContext.free(sslCtx);
/*      */       }
/*      */     } catch (Exception e) {
/*   92 */       logger.warn(sm.getString("engine.ciphersFailure"), e);
/*      */     } finally {
/*   94 */       Pool.destroy(aprPool);
/*      */     }
/*   96 */     AVAILABLE_CIPHER_SUITES = Collections.unmodifiableSet(availableCipherSuites);
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int MAX_COMPRESSED_LENGTH = 17408;
/*      */   
/*      */   private static final int MAX_CIPHERTEXT_LENGTH = 18432;
/*      */   
/*      */   static final int VERIFY_DEPTH = 10;
/*      */   
/*  106 */   private static final String[] IMPLEMENTED_PROTOCOLS = { "SSLv2Hello", "SSLv2", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  115 */   public static final Set<String> IMPLEMENTED_PROTOCOLS_SET = Collections.unmodifiableSet(new HashSet(Arrays.asList(IMPLEMENTED_PROTOCOLS)));
/*      */   
/*      */   static final int MAX_ENCRYPTED_PACKET_LENGTH = 18713;
/*      */   static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 2329;
/*      */   private static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
/*      */   
/*      */   static enum ClientAuthMode
/*      */   {
/*  123 */     NONE, 
/*  124 */     OPTIONAL, 
/*  125 */     REQUIRE;
/*      */     
/*      */     private ClientAuthMode() {}
/*      */   }
/*      */   
/*  130 */   private static final long EMPTY_ADDR = Buffer.address(ByteBuffer.allocate(0));
/*      */   
/*      */   private long ssl;
/*      */   
/*      */   private long networkBIO;
/*      */   
/*      */   private int accepted;
/*      */   
/*      */   private boolean handshakeFinished;
/*      */   
/*      */   private int currentHandshake;
/*      */   
/*      */   private boolean receivedShutdown;
/*      */   
/*      */   private volatile boolean destroyed;
/*      */   
/*      */   private volatile String cipher;
/*      */   
/*      */   private volatile String applicationProtocol;
/*      */   
/*      */   private volatile Certificate[] peerCerts;
/*      */   
/*      */   @Deprecated
/*      */   private volatile javax.security.cert.X509Certificate[] x509PeerCerts;
/*  154 */   private volatile ClientAuthMode clientAuth = ClientAuthMode.NONE;
/*      */   
/*      */   private boolean isInboundDone;
/*      */   
/*      */   private boolean isOutboundDone;
/*      */   private boolean engineClosed;
/*  160 */   private boolean sendHandshakeError = false;
/*      */   
/*      */   private final boolean clientMode;
/*      */   
/*      */   private final String fallbackApplicationProtocol;
/*      */   private final OpenSSLSessionContext sessionContext;
/*      */   private final boolean alpn;
/*      */   private final boolean initialized;
/*  168 */   private String selectedProtocol = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final OpenSSLSession session;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   OpenSSLEngine(long sslCtx, String fallbackApplicationProtocol, boolean clientMode, OpenSSLSessionContext sessionContext, boolean alpn)
/*      */   {
/*  187 */     this(sslCtx, fallbackApplicationProtocol, clientMode, sessionContext, alpn, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   OpenSSLEngine(long sslCtx, String fallbackApplicationProtocol, boolean clientMode, OpenSSLSessionContext sessionContext, boolean alpn, boolean initialized)
/*      */   {
/*  208 */     if (sslCtx == 0L) {
/*  209 */       throw new IllegalArgumentException(sm.getString("engine.noSSLContext"));
/*      */     }
/*  211 */     this.session = new OpenSSLSession(null);
/*  212 */     this.destroyed = true;
/*  213 */     this.ssl = SSL.newSSL(sslCtx, !clientMode);
/*  214 */     this.networkBIO = SSL.makeNetworkBIO(this.ssl);
/*  215 */     this.destroyed = false;
/*  216 */     this.fallbackApplicationProtocol = fallbackApplicationProtocol;
/*  217 */     this.clientMode = clientMode;
/*  218 */     this.sessionContext = sessionContext;
/*  219 */     this.alpn = alpn;
/*  220 */     this.initialized = initialized;
/*      */   }
/*      */   
/*      */   public String getNegotiatedProtocol()
/*      */   {
/*  225 */     return this.selectedProtocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void shutdown()
/*      */   {
/*  232 */     if (!this.destroyed) {
/*  233 */       this.destroyed = true;
/*  234 */       SSL.freeBIO(this.networkBIO);
/*  235 */       SSL.freeSSL(this.ssl);
/*  236 */       this.ssl = (this.networkBIO = 0L);
/*      */       
/*      */ 
/*  239 */       this.isInboundDone = (this.isOutboundDone = this.engineClosed = 1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int writePlaintextData(ByteBuffer src)
/*      */   {
/*  249 */     int pos = src.position();
/*  250 */     int limit = src.limit();
/*  251 */     int len = Math.min(limit - pos, 16384);
/*      */     
/*      */ 
/*  254 */     if (src.isDirect()) {
/*  255 */       long addr = Buffer.address(src) + pos;
/*  256 */       int sslWrote = SSL.writeToSSL(this.ssl, addr, len);
/*  257 */       if (sslWrote >= 0) {
/*  258 */         src.position(pos + sslWrote);
/*  259 */         return sslWrote;
/*      */       }
/*      */     } else {
/*  262 */       ByteBuffer buf = ByteBuffer.allocateDirect(len);
/*      */       try {
/*  264 */         long addr = memoryAddress(buf);
/*      */         
/*  266 */         src.limit(pos + len);
/*      */         
/*  268 */         buf.put(src);
/*  269 */         src.limit(limit);
/*      */         
/*  271 */         int sslWrote = SSL.writeToSSL(this.ssl, addr, len);
/*  272 */         if (sslWrote >= 0) {
/*  273 */           src.position(pos + sslWrote);
/*  274 */           return sslWrote;
/*      */         }
/*  276 */         src.position(pos);
/*      */       }
/*      */       finally {
/*  279 */         buf.clear();
/*  280 */         ByteBufferUtils.cleanDirectBuffer(buf);
/*      */       }
/*      */     }
/*      */     
/*      */     int sslWrote;
/*  285 */     throw new IllegalStateException(sm.getString("engine.writeToSSLFailed", new Object[] {Integer.toString(sslWrote) }));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int writeEncryptedData(ByteBuffer src)
/*      */   {
/*  292 */     int pos = src.position();
/*  293 */     int len = src.remaining();
/*  294 */     if (src.isDirect()) {
/*  295 */       long addr = Buffer.address(src) + pos;
/*  296 */       int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
/*  297 */       if (netWrote >= 0) {
/*  298 */         src.position(pos + netWrote);
/*  299 */         return netWrote;
/*      */       }
/*      */     } else {
/*  302 */       ByteBuffer buf = ByteBuffer.allocateDirect(len);
/*      */       try {
/*  304 */         long addr = memoryAddress(buf);
/*      */         
/*  306 */         buf.put(src);
/*      */         
/*  308 */         int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
/*  309 */         if (netWrote >= 0) {
/*  310 */           src.position(pos + netWrote);
/*  311 */           return netWrote;
/*      */         }
/*  313 */         src.position(pos);
/*      */       }
/*      */       finally {
/*  316 */         buf.clear();
/*  317 */         ByteBufferUtils.cleanDirectBuffer(buf);
/*      */       }
/*      */     }
/*      */     
/*  321 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int readPlaintextData(ByteBuffer dst)
/*      */   {
/*  328 */     if (dst.isDirect()) {
/*  329 */       int pos = dst.position();
/*  330 */       long addr = Buffer.address(dst) + pos;
/*  331 */       int len = dst.limit() - pos;
/*  332 */       int sslRead = SSL.readFromSSL(this.ssl, addr, len);
/*  333 */       if (sslRead > 0) {
/*  334 */         dst.position(pos + sslRead);
/*  335 */         return sslRead;
/*      */       }
/*      */     } else {
/*  338 */       int pos = dst.position();
/*  339 */       int limit = dst.limit();
/*  340 */       int len = Math.min(18713, limit - pos);
/*  341 */       ByteBuffer buf = ByteBuffer.allocateDirect(len);
/*      */       try {
/*  343 */         long addr = memoryAddress(buf);
/*      */         
/*  345 */         int sslRead = SSL.readFromSSL(this.ssl, addr, len);
/*  346 */         if (sslRead > 0) {
/*  347 */           buf.limit(sslRead);
/*  348 */           dst.limit(pos + sslRead);
/*  349 */           dst.put(buf);
/*  350 */           dst.limit(limit);
/*  351 */           return sslRead;
/*      */         }
/*      */       } finally {
/*  354 */         buf.clear();
/*  355 */         ByteBufferUtils.cleanDirectBuffer(buf);
/*      */       }
/*      */     }
/*      */     
/*  359 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int readEncryptedData(ByteBuffer dst, int pending)
/*      */   {
/*  366 */     if ((dst.isDirect()) && (dst.remaining() >= pending)) {
/*  367 */       int pos = dst.position();
/*  368 */       long addr = Buffer.address(dst) + pos;
/*  369 */       int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
/*  370 */       if (bioRead > 0) {
/*  371 */         dst.position(pos + bioRead);
/*  372 */         return bioRead;
/*      */       }
/*      */     } else {
/*  375 */       ByteBuffer buf = ByteBuffer.allocateDirect(pending);
/*      */       try {
/*  377 */         long addr = memoryAddress(buf);
/*      */         
/*  379 */         int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
/*  380 */         if (bioRead > 0) {
/*  381 */           buf.limit(bioRead);
/*  382 */           int oldLimit = dst.limit();
/*  383 */           dst.limit(dst.position() + bioRead);
/*  384 */           dst.put(buf);
/*  385 */           dst.limit(oldLimit);
/*  386 */           return bioRead;
/*      */         }
/*      */       } finally {
/*  389 */         buf.clear();
/*  390 */         ByteBufferUtils.cleanDirectBuffer(buf);
/*      */       }
/*      */     }
/*      */     
/*  394 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int length, ByteBuffer dst)
/*      */     throws SSLException
/*      */   {
/*  401 */     if (this.destroyed) {
/*  402 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */     }
/*      */     
/*      */ 
/*  406 */     if ((srcs == null) || (dst == null)) {
/*  407 */       throw new IllegalArgumentException(sm.getString("engine.nullBuffer"));
/*      */     }
/*  409 */     if ((offset >= srcs.length) || (offset + length > srcs.length)) {
/*  410 */       throw new IndexOutOfBoundsException(sm.getString("engine.invalidBufferArray", new Object[] {
/*  411 */         Integer.toString(offset), Integer.toString(length), 
/*  412 */         Integer.toString(srcs.length) }));
/*      */     }
/*  414 */     if (dst.isReadOnly()) {
/*  415 */       throw new ReadOnlyBufferException();
/*      */     }
/*      */     
/*      */ 
/*  419 */     if (this.accepted == 0) {
/*  420 */       beginHandshakeImplicitly();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  425 */     SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
/*      */     
/*  427 */     if (((!this.handshakeFinished) || (this.engineClosed)) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)) {
/*  428 */       return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
/*      */     }
/*      */     
/*  431 */     int bytesProduced = 0;
/*      */     
/*      */ 
/*      */ 
/*  435 */     int pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
/*  436 */     if (pendingNet > 0)
/*      */     {
/*  438 */       int capacity = dst.remaining();
/*  439 */       if (capacity < pendingNet) {
/*  440 */         return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus, 0, 0);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  445 */         bytesProduced = readEncryptedData(dst, pendingNet);
/*      */       } catch (Exception e) {
/*  447 */         throw new SSLException(e);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  453 */       if (this.isOutboundDone) {
/*  454 */         shutdown();
/*      */       }
/*      */       
/*  457 */       return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), 0, bytesProduced);
/*      */     }
/*      */     
/*      */ 
/*  461 */     int bytesConsumed = 0;
/*  462 */     int endOffset = offset + length;
/*  463 */     for (int i = offset; i < endOffset; i++) {
/*  464 */       ByteBuffer src = srcs[i];
/*  465 */       if (src == null) {
/*  466 */         throw new IllegalArgumentException(sm.getString("engine.nullBufferInArray"));
/*      */       }
/*  468 */       while (src.hasRemaining())
/*      */       {
/*      */         try
/*      */         {
/*  472 */           bytesConsumed += writePlaintextData(src);
/*      */         } catch (Exception e) {
/*  474 */           throw new SSLException(e);
/*      */         }
/*      */         
/*      */ 
/*  478 */         pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
/*  479 */         if (pendingNet > 0)
/*      */         {
/*  481 */           int capacity = dst.remaining();
/*  482 */           if (capacity < pendingNet) {
/*  483 */             return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, 
/*  484 */               getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */           }
/*      */           
/*      */           try
/*      */           {
/*  489 */             bytesProduced += readEncryptedData(dst, pendingNet);
/*      */           } catch (Exception e) {
/*  491 */             throw new SSLException(e);
/*      */           }
/*      */           
/*  494 */           return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */         }
/*      */       }
/*      */     }
/*  498 */     return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
/*      */   }
/*      */   
/*      */   public synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length)
/*      */     throws SSLException
/*      */   {
/*  504 */     if (this.destroyed) {
/*  505 */       return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
/*      */     }
/*      */     
/*      */ 
/*  509 */     if ((src == null) || (dsts == null)) {
/*  510 */       throw new IllegalArgumentException(sm.getString("engine.nullBuffer"));
/*      */     }
/*  512 */     if ((offset >= dsts.length) || (offset + length > dsts.length)) {
/*  513 */       throw new IndexOutOfBoundsException(sm.getString("engine.invalidBufferArray", new Object[] {
/*  514 */         Integer.toString(offset), Integer.toString(length), 
/*  515 */         Integer.toString(dsts.length) }));
/*      */     }
/*  517 */     int capacity = 0;
/*  518 */     int endOffset = offset + length;
/*  519 */     for (int i = offset; i < endOffset; i++) {
/*  520 */       ByteBuffer dst = dsts[i];
/*  521 */       if (dst == null) {
/*  522 */         throw new IllegalArgumentException(sm.getString("engine.nullBufferInArray"));
/*      */       }
/*  524 */       if (dst.isReadOnly()) {
/*  525 */         throw new ReadOnlyBufferException();
/*      */       }
/*  527 */       capacity += dst.remaining();
/*      */     }
/*      */     
/*      */ 
/*  531 */     if (this.accepted == 0) {
/*  532 */       beginHandshakeImplicitly();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  537 */     SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
/*  538 */     if (((!this.handshakeFinished) || (this.engineClosed)) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP)) {
/*  539 */       return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
/*      */     }
/*      */     
/*  542 */     int len = src.remaining();
/*      */     
/*      */ 
/*  545 */     if (len > 18713) {
/*  546 */       this.isInboundDone = true;
/*  547 */       this.isOutboundDone = true;
/*  548 */       this.engineClosed = true;
/*  549 */       shutdown();
/*  550 */       throw new SSLException(sm.getString("engine.oversizedPacket"));
/*      */     }
/*      */     
/*      */ 
/*  554 */     int written = -1;
/*      */     try {
/*  556 */       written = writeEncryptedData(src);
/*      */     } catch (Exception e) {
/*  558 */       throw new SSLException(e);
/*      */     }
/*      */     
/*  561 */     if (written < 0) {
/*  562 */       written = 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  568 */     int pendingApp = pendingReadableBytesInSSL();
/*  569 */     if (!this.handshakeFinished) {
/*  570 */       pendingApp = 0;
/*      */     }
/*  572 */     int bytesProduced = 0;
/*  573 */     int idx = offset;
/*      */     
/*  575 */     if (capacity < pendingApp) {
/*  576 */       return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), written, 0);
/*      */     }
/*      */     
/*  579 */     while (pendingApp > 0)
/*      */     {
/*  581 */       while (idx < endOffset) {
/*  582 */         ByteBuffer dst = dsts[idx];
/*  583 */         if (!dst.hasRemaining()) {
/*  584 */           idx++;
/*      */         }
/*      */         else
/*      */         {
/*  588 */           if (pendingApp <= 0) {
/*      */             break;
/*      */           }
/*      */           
/*      */           try
/*      */           {
/*  594 */             bytesRead = readPlaintextData(dst);
/*      */           } catch (Exception e) { int bytesRead;
/*  596 */             throw new SSLException(e);
/*      */           }
/*      */           int bytesRead;
/*  599 */           if (bytesRead == 0) {
/*      */             break;
/*      */           }
/*      */           
/*  603 */           bytesProduced += bytesRead;
/*  604 */           pendingApp -= bytesRead;
/*  605 */           capacity -= bytesRead;
/*      */           
/*  607 */           if (!dst.hasRemaining())
/*  608 */             idx++;
/*      */         }
/*      */       }
/*  611 */       if (capacity == 0)
/*      */         break;
/*  613 */       if (pendingApp == 0) {
/*  614 */         pendingApp = pendingReadableBytesInSSL();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  619 */     if ((!this.receivedShutdown) && ((SSL.getShutdown(this.ssl) & 0x2) == 2)) {
/*  620 */       this.receivedShutdown = true;
/*  621 */       closeOutbound();
/*  622 */       closeInbound();
/*      */     }
/*  624 */     if ((bytesProduced == 0) && (written == 0)) {
/*  625 */       return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, getHandshakeStatus(), 0, 0);
/*      */     }
/*  627 */     return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), written, bytesProduced);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int pendingReadableBytesInSSL()
/*      */     throws SSLException
/*      */   {
/*  636 */     int lastPrimingReadResult = SSL.readFromSSL(this.ssl, EMPTY_ADDR, 0);
/*      */     
/*      */ 
/*  639 */     if (lastPrimingReadResult <= 0) {
/*  640 */       checkLastError();
/*      */     }
/*  642 */     return SSL.pendingReadableBytesInSSL(this.ssl);
/*      */   }
/*      */   
/*      */ 
/*      */   public Runnable getDelegatedTask()
/*      */   {
/*  648 */     return null;
/*      */   }
/*      */   
/*      */   public synchronized void closeInbound() throws SSLException
/*      */   {
/*  653 */     if (this.isInboundDone) {
/*  654 */       return;
/*      */     }
/*      */     
/*  657 */     this.isInboundDone = true;
/*  658 */     this.engineClosed = true;
/*      */     
/*  660 */     shutdown();
/*      */     
/*  662 */     if ((this.accepted != 0) && (!this.receivedShutdown)) {
/*  663 */       throw new SSLException(sm.getString("engine.inboundClose"));
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized boolean isInboundDone()
/*      */   {
/*  669 */     return (this.isInboundDone) || (this.engineClosed);
/*      */   }
/*      */   
/*      */   public synchronized void closeOutbound()
/*      */   {
/*  674 */     if (this.isOutboundDone) {
/*  675 */       return;
/*      */     }
/*      */     
/*  678 */     this.isOutboundDone = true;
/*  679 */     this.engineClosed = true;
/*      */     
/*  681 */     if ((this.accepted != 0) && (!this.destroyed)) {
/*  682 */       int mode = SSL.getShutdown(this.ssl);
/*  683 */       if ((mode & 0x1) != 1) {
/*  684 */         SSL.shutdownSSL(this.ssl);
/*      */       }
/*      */     }
/*      */     else {
/*  688 */       shutdown();
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized boolean isOutboundDone()
/*      */   {
/*  694 */     return this.isOutboundDone;
/*      */   }
/*      */   
/*      */   public String[] getSupportedCipherSuites()
/*      */   {
/*  699 */     Set<String> availableCipherSuites = AVAILABLE_CIPHER_SUITES;
/*  700 */     return (String[])availableCipherSuites.toArray(new String[availableCipherSuites.size()]);
/*      */   }
/*      */   
/*      */   public synchronized String[] getEnabledCipherSuites()
/*      */   {
/*  705 */     if (this.destroyed) {
/*  706 */       return new String[0];
/*      */     }
/*  708 */     String[] enabled = SSL.getCiphers(this.ssl);
/*  709 */     if (enabled == null) {
/*  710 */       return new String[0];
/*      */     }
/*  712 */     for (int i = 0; i < enabled.length; i++) {
/*  713 */       String mapped = OpenSSLCipherConfigurationParser.openSSLToJsse(enabled[i]);
/*  714 */       if (mapped != null) {
/*  715 */         enabled[i] = mapped;
/*      */       }
/*      */     }
/*  718 */     return enabled;
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void setEnabledCipherSuites(String[] cipherSuites)
/*      */   {
/*  724 */     if (this.initialized) {
/*  725 */       return;
/*      */     }
/*  727 */     if (cipherSuites == null) {
/*  728 */       throw new IllegalArgumentException(sm.getString("engine.nullCipherSuite"));
/*      */     }
/*  730 */     if (this.destroyed) {
/*  731 */       return;
/*      */     }
/*  733 */     StringBuilder buf = new StringBuilder();
/*  734 */     for (String cipherSuite : cipherSuites) {
/*  735 */       if (cipherSuite == null) {
/*      */         break;
/*      */       }
/*  738 */       String converted = OpenSSLCipherConfigurationParser.jsseToOpenSSL(cipherSuite);
/*  739 */       if (!AVAILABLE_CIPHER_SUITES.contains(cipherSuite)) {
/*  740 */         logger.debug(sm.getString("engine.unsupportedCipher", new Object[] { cipherSuite, converted }));
/*      */       }
/*  742 */       if (converted != null) {
/*  743 */         cipherSuite = converted;
/*      */       }
/*      */       
/*  746 */       buf.append(cipherSuite);
/*  747 */       buf.append(':');
/*      */     }
/*      */     
/*  750 */     if (buf.length() == 0) {
/*  751 */       throw new IllegalArgumentException(sm.getString("engine.emptyCipherSuite"));
/*      */     }
/*  753 */     buf.setLength(buf.length() - 1);
/*      */     
/*  755 */     String cipherSuiteSpec = buf.toString();
/*      */     try {
/*  757 */       SSL.setCipherSuites(this.ssl, cipherSuiteSpec);
/*      */     } catch (Exception e) {
/*  759 */       throw new IllegalStateException(sm.getString("engine.failedCipherSuite", new Object[] { cipherSuiteSpec }), e);
/*      */     }
/*      */   }
/*      */   
/*      */   public String[] getSupportedProtocols()
/*      */   {
/*  765 */     return (String[])IMPLEMENTED_PROTOCOLS.clone();
/*      */   }
/*      */   
/*      */   public synchronized String[] getEnabledProtocols()
/*      */   {
/*  770 */     if (this.destroyed) {
/*  771 */       return new String[0];
/*      */     }
/*  773 */     List<String> enabled = new ArrayList();
/*      */     
/*  775 */     enabled.add("SSLv2Hello");
/*  776 */     int opts = SSL.getOptions(this.ssl);
/*  777 */     if ((opts & 0x4000000) == 0) {
/*  778 */       enabled.add("TLSv1");
/*      */     }
/*  780 */     if ((opts & 0x10000000) == 0) {
/*  781 */       enabled.add("TLSv1.1");
/*      */     }
/*  783 */     if ((opts & 0x8000000) == 0) {
/*  784 */       enabled.add("TLSv1.2");
/*      */     }
/*  786 */     if ((opts & 0x1000000) == 0) {
/*  787 */       enabled.add("SSLv2");
/*      */     }
/*  789 */     if ((opts & 0x2000000) == 0) {
/*  790 */       enabled.add("SSLv3");
/*      */     }
/*  792 */     int size = enabled.size();
/*  793 */     if (size == 0) {
/*  794 */       return new String[0];
/*      */     }
/*  796 */     return (String[])enabled.toArray(new String[size]);
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized void setEnabledProtocols(String[] protocols)
/*      */   {
/*  802 */     if (this.initialized) {
/*  803 */       return;
/*      */     }
/*  805 */     if (protocols == null)
/*      */     {
/*  807 */       throw new IllegalArgumentException();
/*      */     }
/*  809 */     if (this.destroyed) {
/*  810 */       return;
/*      */     }
/*  812 */     boolean sslv2 = false;
/*  813 */     boolean sslv3 = false;
/*  814 */     boolean tlsv1 = false;
/*  815 */     boolean tlsv1_1 = false;
/*  816 */     boolean tlsv1_2 = false;
/*  817 */     for (String p : protocols) {
/*  818 */       if (!IMPLEMENTED_PROTOCOLS_SET.contains(p)) {
/*  819 */         throw new IllegalArgumentException(sm.getString("engine.unsupportedProtocol", new Object[] { p }));
/*      */       }
/*  821 */       if (p.equals("SSLv2")) {
/*  822 */         sslv2 = true;
/*  823 */       } else if (p.equals("SSLv3")) {
/*  824 */         sslv3 = true;
/*  825 */       } else if (p.equals("TLSv1")) {
/*  826 */         tlsv1 = true;
/*  827 */       } else if (p.equals("TLSv1.1")) {
/*  828 */         tlsv1_1 = true;
/*  829 */       } else if (p.equals("TLSv1.2")) {
/*  830 */         tlsv1_2 = true;
/*      */       }
/*      */     }
/*      */     
/*  834 */     SSL.setOptions(this.ssl, 4095);
/*      */     
/*  836 */     if (!sslv2) {
/*  837 */       SSL.setOptions(this.ssl, 16777216);
/*      */     }
/*  839 */     if (!sslv3) {
/*  840 */       SSL.setOptions(this.ssl, 33554432);
/*      */     }
/*  842 */     if (!tlsv1) {
/*  843 */       SSL.setOptions(this.ssl, 67108864);
/*      */     }
/*  845 */     if (!tlsv1_1) {
/*  846 */       SSL.setOptions(this.ssl, 268435456);
/*      */     }
/*  848 */     if (!tlsv1_2) {
/*  849 */       SSL.setOptions(this.ssl, 134217728);
/*      */     }
/*      */   }
/*      */   
/*      */   public SSLSession getSession()
/*      */   {
/*  855 */     return this.session;
/*      */   }
/*      */   
/*      */   public synchronized void beginHandshake() throws SSLException
/*      */   {
/*  860 */     if ((this.engineClosed) || (this.destroyed)) {
/*  861 */       throw new SSLException(sm.getString("engine.engineClosed"));
/*      */     }
/*  863 */     switch (this.accepted) {
/*      */     case 0: 
/*  865 */       handshake();
/*  866 */       this.accepted = 2;
/*  867 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1: 
/*  875 */       this.accepted = 2;
/*  876 */       break;
/*      */     case 2: 
/*  878 */       renegotiate();
/*  879 */       break;
/*      */     default: 
/*  881 */       throw new Error();
/*      */     }
/*      */   }
/*      */   
/*      */   private void beginHandshakeImplicitly() throws SSLException {
/*  886 */     handshake();
/*  887 */     this.accepted = 1;
/*      */   }
/*      */   
/*      */   private void handshake() throws SSLException {
/*  891 */     this.currentHandshake = SSL.getHandshakeCount(this.ssl);
/*  892 */     int code = SSL.doHandshake(this.ssl);
/*  893 */     if (code <= 0) {
/*  894 */       checkLastError();
/*      */     } else {
/*  896 */       if (this.alpn) {
/*  897 */         this.selectedProtocol = SSL.getAlpnSelected(this.ssl);
/*  898 */         if (this.selectedProtocol == null) {
/*  899 */           this.selectedProtocol = SSL.getNextProtoNegotiated(this.ssl);
/*      */         }
/*      */       }
/*  902 */       this.session.lastAccessedTime = System.currentTimeMillis();
/*      */       
/*      */ 
/*  905 */       this.handshakeFinished = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized void renegotiate() throws SSLException {
/*  910 */     int code = SSL.renegotiate(this.ssl);
/*  911 */     if (code <= 0) {
/*  912 */       checkLastError();
/*      */     }
/*  914 */     this.handshakeFinished = false;
/*  915 */     this.peerCerts = null;
/*  916 */     this.x509PeerCerts = null;
/*  917 */     this.currentHandshake = SSL.getHandshakeCount(this.ssl);
/*  918 */     int code2 = SSL.doHandshake(this.ssl);
/*  919 */     if (code2 <= 0) {
/*  920 */       checkLastError();
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkLastError() throws SSLException {
/*  925 */     long error = SSL.getLastErrorNumber();
/*  926 */     if (error != 0L) {
/*  927 */       String err = SSL.getErrorString(error);
/*  928 */       if (logger.isDebugEnabled()) {
/*  929 */         logger.debug(sm.getString("engine.openSSLError", new Object[] { Long.toString(error), err }));
/*      */       }
/*      */       
/*  932 */       if (!this.handshakeFinished) {
/*  933 */         this.sendHandshakeError = true;
/*      */       } else {
/*  935 */         throw new SSLException(err);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static long memoryAddress(ByteBuffer buf) {
/*  941 */     return Buffer.address(buf);
/*      */   }
/*      */   
/*      */   private SSLEngineResult.Status getEngineStatus() {
/*  945 */     return this.engineClosed ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
/*      */   }
/*      */   
/*      */   public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus()
/*      */   {
/*  950 */     if ((this.accepted == 0) || (this.destroyed)) {
/*  951 */       return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */     }
/*      */     
/*      */ 
/*  955 */     if (!this.handshakeFinished)
/*      */     {
/*      */ 
/*  958 */       if ((this.sendHandshakeError) || (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0)) {
/*  959 */         if (this.sendHandshakeError)
/*      */         {
/*  961 */           this.sendHandshakeError = false;
/*  962 */           this.currentHandshake += 1;
/*      */         }
/*  964 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  969 */       int handshakeCount = SSL.getHandshakeCount(this.ssl);
/*  970 */       if (handshakeCount != this.currentHandshake) {
/*  971 */         if (this.alpn) {
/*  972 */           this.selectedProtocol = SSL.getAlpnSelected(this.ssl);
/*  973 */           if (this.selectedProtocol == null) {
/*  974 */             this.selectedProtocol = SSL.getNextProtoNegotiated(this.ssl);
/*      */           }
/*      */         }
/*  977 */         this.session.lastAccessedTime = System.currentTimeMillis();
/*  978 */         this.handshakeFinished = true;
/*  979 */         return SSLEngineResult.HandshakeStatus.FINISHED;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  984 */       return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */     }
/*      */     
/*      */ 
/*  988 */     if (this.engineClosed)
/*      */     {
/*  990 */       if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
/*  991 */         return SSLEngineResult.HandshakeStatus.NEED_WRAP;
/*      */       }
/*      */       
/*      */ 
/*  995 */       return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
/*      */     }
/*      */     
/*  998 */     return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
/*      */   }
/*      */   
/*      */   public void setUseClientMode(boolean clientMode)
/*      */   {
/* 1003 */     if (clientMode != this.clientMode) {
/* 1004 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getUseClientMode()
/*      */   {
/* 1010 */     return this.clientMode;
/*      */   }
/*      */   
/*      */   public void setNeedClientAuth(boolean b)
/*      */   {
/* 1015 */     setClientAuth(b ? ClientAuthMode.REQUIRE : ClientAuthMode.NONE);
/*      */   }
/*      */   
/*      */   public boolean getNeedClientAuth()
/*      */   {
/* 1020 */     return this.clientAuth == ClientAuthMode.REQUIRE;
/*      */   }
/*      */   
/*      */   public void setWantClientAuth(boolean b)
/*      */   {
/* 1025 */     setClientAuth(b ? ClientAuthMode.OPTIONAL : ClientAuthMode.NONE);
/*      */   }
/*      */   
/*      */   public boolean getWantClientAuth()
/*      */   {
/* 1030 */     return this.clientAuth == ClientAuthMode.OPTIONAL;
/*      */   }
/*      */   
/*      */   private void setClientAuth(ClientAuthMode mode) {
/* 1034 */     if (this.clientMode) {
/* 1035 */       return;
/*      */     }
/* 1037 */     synchronized (this) {
/* 1038 */       if (this.clientAuth == mode)
/*      */       {
/* 1040 */         return;
/*      */       }
/* 1042 */       switch (mode) {
/*      */       case NONE: 
/* 1044 */         SSL.setVerify(this.ssl, 0, 10);
/* 1045 */         break;
/*      */       case REQUIRE: 
/* 1047 */         SSL.setVerify(this.ssl, 2, 10);
/* 1048 */         break;
/*      */       case OPTIONAL: 
/* 1050 */         SSL.setVerify(this.ssl, 1, 10);
/*      */       }
/*      */       
/* 1053 */       this.clientAuth = mode;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setEnableSessionCreation(boolean b)
/*      */   {
/* 1059 */     if (b) {
/* 1060 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getEnableSessionCreation()
/*      */   {
/* 1066 */     return false;
/*      */   }
/*      */   
/*      */   protected void finalize() throws Throwable
/*      */   {
/* 1071 */     super.finalize();
/*      */     
/* 1073 */     shutdown();
/*      */   }
/*      */   
/*      */ 
/*      */   private class OpenSSLSession
/*      */     implements SSLSession
/*      */   {
/*      */     private Map<String, Object> values;
/*      */     
/* 1082 */     private long lastAccessedTime = -1L;
/*      */     
/*      */     private OpenSSLSession() {}
/*      */     
/*      */     public byte[] getId() { byte[] id;
/* 1087 */       synchronized (OpenSSLEngine.this) {
/* 1088 */         if (OpenSSLEngine.this.destroyed) {
/* 1089 */           throw new IllegalStateException(OpenSSLEngine.sm.getString("engine.noSession"));
/*      */         }
/* 1091 */         id = SSL.getSessionId(OpenSSLEngine.this.ssl);
/*      */       }
/*      */       byte[] id;
/* 1094 */       return id;
/*      */     }
/*      */     
/*      */     public SSLSessionContext getSessionContext()
/*      */     {
/* 1099 */       return OpenSSLEngine.this.sessionContext;
/*      */     }
/*      */     
/*      */ 
/*      */     public long getCreationTime()
/*      */     {
/* 1105 */       long creationTime = 0L;
/* 1106 */       synchronized (OpenSSLEngine.this) {
/* 1107 */         if (OpenSSLEngine.this.destroyed) {
/* 1108 */           throw new IllegalStateException(OpenSSLEngine.sm.getString("engine.noSession"));
/*      */         }
/* 1110 */         creationTime = SSL.getTime(OpenSSLEngine.this.ssl);
/*      */       }
/* 1112 */       return creationTime * 1000L;
/*      */     }
/*      */     
/*      */     public long getLastAccessedTime()
/*      */     {
/* 1117 */       return this.lastAccessedTime > 0L ? this.lastAccessedTime : getCreationTime();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void invalidate() {}
/*      */     
/*      */ 
/*      */     public boolean isValid()
/*      */     {
/* 1127 */       return false;
/*      */     }
/*      */     
/*      */     public void putValue(String name, Object value)
/*      */     {
/* 1132 */       if (name == null) {
/* 1133 */         throw new IllegalArgumentException(OpenSSLEngine.sm.getString("engine.nullName"));
/*      */       }
/* 1135 */       if (value == null) {
/* 1136 */         throw new IllegalArgumentException(OpenSSLEngine.sm.getString("engine.nullValue"));
/*      */       }
/* 1138 */       Map<String, Object> values = this.values;
/* 1139 */       if (values == null)
/*      */       {
/* 1141 */         values = this.values = new HashMap(2);
/*      */       }
/* 1143 */       Object old = values.put(name, value);
/* 1144 */       if ((value instanceof SSLSessionBindingListener)) {
/* 1145 */         ((SSLSessionBindingListener)value).valueBound(new SSLSessionBindingEvent(this, name));
/*      */       }
/* 1147 */       notifyUnbound(old, name);
/*      */     }
/*      */     
/*      */     public Object getValue(String name)
/*      */     {
/* 1152 */       if (name == null) {
/* 1153 */         throw new IllegalArgumentException(OpenSSLEngine.sm.getString("engine.nullName"));
/*      */       }
/* 1155 */       if (this.values == null) {
/* 1156 */         return null;
/*      */       }
/* 1158 */       return this.values.get(name);
/*      */     }
/*      */     
/*      */     public void removeValue(String name)
/*      */     {
/* 1163 */       if (name == null) {
/* 1164 */         throw new IllegalArgumentException(OpenSSLEngine.sm.getString("engine.nullName"));
/*      */       }
/* 1166 */       Map<String, Object> values = this.values;
/* 1167 */       if (values == null) {
/* 1168 */         return;
/*      */       }
/* 1170 */       Object old = values.remove(name);
/* 1171 */       notifyUnbound(old, name);
/*      */     }
/*      */     
/*      */     public String[] getValueNames()
/*      */     {
/* 1176 */       Map<String, Object> values = this.values;
/* 1177 */       if ((values == null) || (values.isEmpty())) {
/* 1178 */         return new String[0];
/*      */       }
/* 1180 */       return (String[])values.keySet().toArray(new String[values.size()]);
/*      */     }
/*      */     
/*      */     private void notifyUnbound(Object value, String name) {
/* 1184 */       if ((value instanceof SSLSessionBindingListener)) {
/* 1185 */         ((SSLSessionBindingListener)value).valueUnbound(new SSLSessionBindingEvent(this, name));
/*      */       }
/*      */     }
/*      */     
/*      */     public Certificate[] getPeerCertificates()
/*      */       throws SSLPeerUnverifiedException
/*      */     {
/* 1192 */       Certificate[] c = OpenSSLEngine.this.peerCerts;
/* 1193 */       if (c == null)
/*      */       {
/*      */         byte[] clientCert;
/* 1196 */         synchronized (OpenSSLEngine.this) {
/* 1197 */           if ((OpenSSLEngine.this.destroyed) || (SSL.isInInit(OpenSSLEngine.this.ssl) != 0)) {
/* 1198 */             throw new SSLPeerUnverifiedException(OpenSSLEngine.sm.getString("engine.unverifiedPeer"));
/*      */           }
/* 1200 */           byte[][] chain = SSL.getPeerCertChain(OpenSSLEngine.this.ssl);
/* 1201 */           byte[] clientCert; if (!OpenSSLEngine.this.clientMode)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1206 */             clientCert = SSL.getPeerCertificate(OpenSSLEngine.this.ssl);
/*      */           } else
/* 1208 */             clientCert = null; }
/*      */         byte[][] chain;
/*      */         byte[] clientCert;
/* 1211 */         if ((chain == null) && (clientCert == null)) {
/* 1212 */           return null;
/*      */         }
/* 1214 */         int len = 0;
/* 1215 */         if (chain != null) {
/* 1216 */           len += chain.length;
/*      */         }
/*      */         
/* 1219 */         int i = 0;
/*      */         Certificate[] certificates;
/* 1221 */         if (clientCert != null) {
/* 1222 */           len++;
/* 1223 */           Certificate[] certificates = new Certificate[len];
/* 1224 */           certificates[(i++)] = new OpenSSLX509Certificate(clientCert);
/*      */         } else {
/* 1226 */           certificates = new Certificate[len];
/*      */         }
/* 1228 */         if (chain != null) {
/* 1229 */           int a = 0;
/* 1230 */           for (; i < certificates.length; i++) {
/* 1231 */             certificates[i] = new OpenSSLX509Certificate(chain[(a++)]);
/*      */           }
/*      */         }
/* 1234 */         c = OpenSSLEngine.this.peerCerts = certificates;
/*      */       }
/* 1236 */       return c;
/*      */     }
/*      */     
/*      */ 
/*      */     public Certificate[] getLocalCertificates()
/*      */     {
/* 1242 */       return OpenSSLEngine.EMPTY_CERTIFICATES;
/*      */     }
/*      */     
/*      */ 
/*      */     @Deprecated
/*      */     public javax.security.cert.X509Certificate[] getPeerCertificateChain()
/*      */       throws SSLPeerUnverifiedException
/*      */     {
/* 1250 */       javax.security.cert.X509Certificate[] c = OpenSSLEngine.this.x509PeerCerts;
/* 1251 */       if (c == null) {
/*      */         byte[][] chain;
/* 1253 */         synchronized (OpenSSLEngine.this) {
/* 1254 */           if ((OpenSSLEngine.this.destroyed) || (SSL.isInInit(OpenSSLEngine.this.ssl) != 0)) {
/* 1255 */             throw new SSLPeerUnverifiedException(OpenSSLEngine.sm.getString("engine.unverifiedPeer"));
/*      */           }
/* 1257 */           chain = SSL.getPeerCertChain(OpenSSLEngine.this.ssl); }
/*      */         byte[][] chain;
/* 1259 */         if (chain == null) {
/* 1260 */           throw new SSLPeerUnverifiedException(OpenSSLEngine.sm.getString("engine.unverifiedPeer"));
/*      */         }
/* 1262 */         javax.security.cert.X509Certificate[] peerCerts = new javax.security.cert.X509Certificate[chain.length];
/*      */         
/* 1264 */         for (int i = 0; i < peerCerts.length; i++) {
/*      */           try {
/* 1266 */             peerCerts[i] = javax.security.cert.X509Certificate.getInstance(chain[i]);
/*      */           } catch (CertificateException e) {
/* 1268 */             throw new IllegalStateException(e);
/*      */           }
/*      */         }
/* 1271 */         c = OpenSSLEngine.this.x509PeerCerts = peerCerts;
/*      */       }
/* 1273 */       return c;
/*      */     }
/*      */     
/*      */     public Principal getPeerPrincipal() throws SSLPeerUnverifiedException
/*      */     {
/* 1278 */       Certificate[] peer = getPeerCertificates();
/* 1279 */       if ((peer == null) || (peer.length == 0)) {
/* 1280 */         return null;
/*      */       }
/* 1282 */       return principal(peer);
/*      */     }
/*      */     
/*      */     public Principal getLocalPrincipal()
/*      */     {
/* 1287 */       Certificate[] local = getLocalCertificates();
/* 1288 */       if ((local == null) || (local.length == 0)) {
/* 1289 */         return null;
/*      */       }
/* 1291 */       return principal(local);
/*      */     }
/*      */     
/*      */     private Principal principal(Certificate[] certs) {
/* 1295 */       return ((java.security.cert.X509Certificate)certs[0]).getIssuerX500Principal();
/*      */     }
/*      */     
/*      */     public String getCipherSuite()
/*      */     {
/* 1300 */       if (OpenSSLEngine.this.cipher == null) {
/*      */         String ciphers;
/* 1302 */         synchronized (OpenSSLEngine.this) {
/* 1303 */           if (!OpenSSLEngine.this.handshakeFinished) {
/* 1304 */             return "SSL_NULL_WITH_NULL_NULL";
/*      */           }
/* 1306 */           if (OpenSSLEngine.this.destroyed) {
/* 1307 */             return "SSL_NULL_WITH_NULL_NULL";
/*      */           }
/* 1309 */           ciphers = SSL.getCipherForSSL(OpenSSLEngine.this.ssl); }
/*      */         String ciphers;
/* 1311 */         String c = OpenSSLCipherConfigurationParser.openSSLToJsse(ciphers);
/* 1312 */         if (c != null) {
/* 1313 */           OpenSSLEngine.this.cipher = c;
/*      */         }
/*      */       }
/* 1316 */       return OpenSSLEngine.this.cipher;
/*      */     }
/*      */     
/*      */     public String getProtocol()
/*      */     {
/* 1321 */       String applicationProtocol = OpenSSLEngine.this.applicationProtocol;
/* 1322 */       if (applicationProtocol == null) {
/* 1323 */         synchronized (OpenSSLEngine.this) {
/* 1324 */           if (OpenSSLEngine.this.destroyed) {
/* 1325 */             throw new IllegalStateException(OpenSSLEngine.sm.getString("engine.noSession"));
/*      */           }
/* 1327 */           applicationProtocol = SSL.getNextProtoNegotiated(OpenSSLEngine.this.ssl);
/*      */         }
/* 1329 */         if (applicationProtocol == null) {
/* 1330 */           applicationProtocol = OpenSSLEngine.this.fallbackApplicationProtocol;
/*      */         }
/* 1332 */         if (applicationProtocol != null) {
/* 1333 */           OpenSSLEngine.this.applicationProtocol = applicationProtocol.replace(':', '_');
/*      */         } else {
/* 1335 */           OpenSSLEngine.this.applicationProtocol = (applicationProtocol = "");
/*      */         }
/*      */       }
/*      */       String version;
/* 1339 */       synchronized (OpenSSLEngine.this) {
/* 1340 */         if (OpenSSLEngine.this.destroyed) {
/* 1341 */           throw new IllegalStateException(OpenSSLEngine.sm.getString("engine.noSession"));
/*      */         }
/* 1343 */         version = SSL.getVersion(OpenSSLEngine.this.ssl); }
/*      */       String version;
/* 1345 */       if (applicationProtocol.isEmpty()) {
/* 1346 */         return version;
/*      */       }
/* 1348 */       return version + ':' + applicationProtocol;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public String getPeerHost()
/*      */     {
/* 1355 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public int getPeerPort()
/*      */     {
/* 1361 */       return 0;
/*      */     }
/*      */     
/*      */     public int getPacketBufferSize()
/*      */     {
/* 1366 */       return 18713;
/*      */     }
/*      */     
/*      */     public int getApplicationBufferSize()
/*      */     {
/* 1371 */       return 16384;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */