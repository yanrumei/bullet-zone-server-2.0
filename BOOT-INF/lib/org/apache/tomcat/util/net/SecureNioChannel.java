/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLEngineResult;
/*     */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*     */ import javax.net.ssl.SSLEngineResult.Status;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteBufferUtils;
/*     */ import org.apache.tomcat.util.compat.JreCompat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecureNioChannel
/*     */   extends NioChannel
/*     */ {
/*  48 */   private static final Log log = LogFactory.getLog(SecureNioChannel.class);
/*  49 */   private static final StringManager sm = StringManager.getManager(SecureNioChannel.class);
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_NET_BUFFER_SIZE = 16921;
/*     */   
/*     */   protected ByteBuffer netInBuffer;
/*     */   
/*     */   protected ByteBuffer netOutBuffer;
/*     */   
/*     */   protected SSLEngine sslEngine;
/*     */   
/*  60 */   protected boolean sniComplete = false;
/*     */   
/*  62 */   protected boolean handshakeComplete = false;
/*     */   
/*     */   protected SSLEngineResult.HandshakeStatus handshakeStatus;
/*  65 */   protected boolean closed = false;
/*  66 */   protected boolean closing = false;
/*     */   
/*     */   protected NioSelectorPool pool;
/*     */   private final NioEndpoint endpoint;
/*     */   
/*     */   public SecureNioChannel(SocketChannel channel, SocketBufferHandler bufHandler, NioSelectorPool pool, NioEndpoint endpoint)
/*     */   {
/*  73 */     super(channel, bufHandler);
/*     */     
/*     */ 
/*  76 */     if (endpoint.getSocketProperties().getDirectSslBuffer()) {
/*  77 */       this.netInBuffer = ByteBuffer.allocateDirect(16921);
/*  78 */       this.netOutBuffer = ByteBuffer.allocateDirect(16921);
/*     */     } else {
/*  80 */       this.netInBuffer = ByteBuffer.allocate(16921);
/*  81 */       this.netOutBuffer = ByteBuffer.allocate(16921);
/*     */     }
/*     */     
/*     */ 
/*  85 */     this.pool = pool;
/*  86 */     this.endpoint = endpoint;
/*     */   }
/*     */   
/*     */   public void reset() throws IOException
/*     */   {
/*  91 */     super.reset();
/*  92 */     this.sslEngine = null;
/*  93 */     this.sniComplete = false;
/*  94 */     this.handshakeComplete = false;
/*  95 */     this.closed = false;
/*  96 */     this.closing = false;
/*  97 */     this.netInBuffer.clear();
/*     */   }
/*     */   
/*     */   public void free()
/*     */   {
/* 102 */     super.free();
/* 103 */     if (this.endpoint.getSocketProperties().getDirectSslBuffer()) {
/* 104 */       ByteBufferUtils.cleanDirectBuffer(this.netInBuffer);
/* 105 */       ByteBufferUtils.cleanDirectBuffer(this.netOutBuffer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean flush(boolean block, Selector s, long timeout)
/*     */     throws IOException
/*     */   {
/* 127 */     if (!block) {
/* 128 */       flush(this.netOutBuffer);
/*     */     } else {
/* 130 */       this.pool.write(this.netOutBuffer, this, s, timeout, block);
/*     */     }
/* 132 */     return !this.netOutBuffer.hasRemaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean flush(ByteBuffer buf)
/*     */     throws IOException
/*     */   {
/* 142 */     int remaining = buf.remaining();
/* 143 */     if (remaining > 0) {
/* 144 */       int written = this.sc.write(buf);
/* 145 */       return written >= remaining;
/*     */     }
/* 147 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int handshake(boolean read, boolean write)
/*     */     throws IOException
/*     */   {
/* 170 */     if (this.handshakeComplete) {
/* 171 */       return 0;
/*     */     }
/*     */     
/* 174 */     if (!this.sniComplete) {
/* 175 */       int sniResult = processSNI();
/* 176 */       if (sniResult == 0) {
/* 177 */         this.sniComplete = true;
/*     */       } else {
/* 179 */         return sniResult;
/*     */       }
/*     */     }
/*     */     
/* 183 */     if (!flush(this.netOutBuffer)) { return 4;
/*     */     }
/* 185 */     SSLEngineResult handshake = null;
/*     */     
/* 187 */     while (!this.handshakeComplete) {
/* 188 */       switch (this.handshakeStatus)
/*     */       {
/*     */       case NOT_HANDSHAKING: 
/* 191 */         throw new IOException(sm.getString("channel.nio.ssl.notHandshaking"));
/*     */       
/*     */       case FINISHED: 
/* 194 */         if (this.endpoint.hasNegotiableProtocols()) {
/* 195 */           if ((this.sslEngine instanceof SSLUtil.ProtocolInfo)) {
/* 196 */             this.socketWrapper.setNegotiatedProtocol(((SSLUtil.ProtocolInfo)this.sslEngine)
/* 197 */               .getNegotiatedProtocol());
/* 198 */           } else if (JreCompat.isJre9Available()) {
/* 199 */             this.socketWrapper.setNegotiatedProtocol(
/* 200 */               JreCompat.getInstance().getApplicationProtocol(this.sslEngine));
/*     */           }
/*     */         }
/*     */         
/* 204 */         this.handshakeComplete = (!this.netOutBuffer.hasRemaining());
/*     */         
/* 206 */         return this.handshakeComplete ? 0 : 4;
/*     */       
/*     */       case NEED_WRAP: 
/*     */         try
/*     */         {
/* 211 */           handshake = handshakeWrap(write);
/*     */         } catch (SSLException e) {
/* 213 */           if (log.isDebugEnabled()) {
/* 214 */             log.debug(sm.getString("channel.nio.ssl.wrapException"), e);
/*     */           }
/* 216 */           handshake = handshakeWrap(write);
/*     */         }
/* 218 */         if (handshake.getStatus() == SSLEngineResult.Status.OK) {
/* 219 */           if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK)
/* 220 */             this.handshakeStatus = tasks();
/* 221 */         } else { if (handshake.getStatus() == SSLEngineResult.Status.CLOSED) {
/* 222 */             flush(this.netOutBuffer);
/* 223 */             return -1;
/*     */           }
/*     */           
/* 226 */           throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringWrap", new Object[] { handshake.getStatus() }));
/*     */         }
/* 228 */         if ((this.handshakeStatus != SSLEngineResult.HandshakeStatus.NEED_UNWRAP) || (!flush(this.netOutBuffer)))
/*     */         {
/* 230 */           return 4;
/*     */         }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case NEED_UNWRAP: 
/* 238 */         handshake = handshakeUnwrap(read);
/* 239 */         if (handshake.getStatus() == SSLEngineResult.Status.OK) {
/* 240 */           if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK)
/* 241 */             this.handshakeStatus = tasks();
/* 242 */         } else { if (handshake.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW)
/*     */           {
/* 244 */             return 1; }
/* 245 */           if (handshake.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 246 */             getBufHandler().configureReadBufferForWrite();
/*     */           } else {
/* 248 */             throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringWrap", new Object[] { this.handshakeStatus }));
/*     */           }
/*     */         }
/*     */         break;
/*     */       case NEED_TASK: 
/* 253 */         this.handshakeStatus = tasks();
/* 254 */         break;
/*     */       default: 
/* 256 */         throw new IllegalStateException(sm.getString("channel.nio.ssl.invalidStatus", new Object[] { this.handshakeStatus }));
/*     */       }
/*     */       
/*     */     }
/* 260 */     return 0;
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
/*     */   private int processSNI()
/*     */     throws IOException
/*     */   {
/* 277 */     int bytesRead = this.sc.read(this.netInBuffer);
/* 278 */     if (bytesRead == -1)
/*     */     {
/* 280 */       return -1;
/*     */     }
/* 282 */     TLSClientHelloExtractor extractor = new TLSClientHelloExtractor(this.netInBuffer);
/*     */     
/* 284 */     while ((extractor.getResult() == TLSClientHelloExtractor.ExtractorResult.UNDERFLOW) && 
/* 285 */       (this.netInBuffer.capacity() < this.endpoint.getSniParseLimit()))
/*     */     {
/*     */ 
/* 288 */       int newLimit = Math.min(this.netInBuffer.capacity() * 2, this.endpoint.getSniParseLimit());
/* 289 */       log.info(sm.getString("channel.nio.ssl.expandNetInBuffer", new Object[] {
/* 290 */         Integer.toString(newLimit) }));
/*     */       
/* 292 */       this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, newLimit);
/* 293 */       this.sc.read(this.netInBuffer);
/* 294 */       extractor = new TLSClientHelloExtractor(this.netInBuffer);
/*     */     }
/*     */     
/* 297 */     String hostName = null;
/* 298 */     List<Cipher> clientRequestedCiphers = null;
/* 299 */     List<String> clientRequestedApplicationProtocols = null;
/* 300 */     switch (extractor.getResult()) {
/*     */     case COMPLETE: 
/* 302 */       hostName = extractor.getSNIValue();
/*     */       
/* 304 */       clientRequestedApplicationProtocols = extractor.getClientRequestedApplicationProtocols();
/*     */     
/*     */     case NOT_PRESENT: 
/* 307 */       clientRequestedCiphers = extractor.getClientRequestedCiphers();
/* 308 */       break;
/*     */     case NEED_READ: 
/* 310 */       return 1;
/*     */     
/*     */     case UNDERFLOW: 
/* 313 */       if (log.isDebugEnabled()) {
/* 314 */         log.debug(sm.getString("channel.nio.ssl.sniDefault"));
/*     */       }
/* 316 */       hostName = this.endpoint.getDefaultSSLHostConfigName();
/* 317 */       clientRequestedCiphers = Collections.emptyList();
/* 318 */       break;
/*     */     case NON_SECURE: 
/* 320 */       this.netOutBuffer.clear();
/* 321 */       this.netOutBuffer.put(TLSClientHelloExtractor.USE_TLS_RESPONSE);
/* 322 */       this.netOutBuffer.flip();
/* 323 */       flushOutbound();
/* 324 */       throw new IOException(sm.getString("channel.nio.ssl.foundHttp"));
/*     */     }
/*     */     
/* 327 */     if (log.isDebugEnabled()) {
/* 328 */       log.debug(sm.getString("channel.nio.ssl.sniHostName", new Object[] { hostName }));
/*     */     }
/*     */     
/* 331 */     this.sslEngine = this.endpoint.createSSLEngine(hostName, clientRequestedCiphers, clientRequestedApplicationProtocols);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 336 */     getBufHandler().expand(this.sslEngine.getSession().getApplicationBufferSize());
/* 337 */     if (this.netOutBuffer.capacity() < this.sslEngine.getSession().getApplicationBufferSize())
/*     */     {
/* 339 */       log.info(sm.getString("channel.nio.ssl.expandNetOutBuffer", new Object[] {
/* 340 */         Integer.toString(this.sslEngine.getSession().getApplicationBufferSize()) }));
/*     */     }
/* 342 */     this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, this.sslEngine.getSession().getPacketBufferSize());
/* 343 */     this.netOutBuffer = ByteBufferUtils.expand(this.netOutBuffer, this.sslEngine.getSession().getPacketBufferSize());
/*     */     
/*     */ 
/* 346 */     this.netOutBuffer.position(0);
/* 347 */     this.netOutBuffer.limit(0);
/*     */     
/*     */ 
/* 350 */     this.sslEngine.beginHandshake();
/* 351 */     this.handshakeStatus = this.sslEngine.getHandshakeStatus();
/*     */     
/* 353 */     return 0;
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
/*     */   public void rehandshake(long timeout)
/*     */     throws IOException
/*     */   {
/* 368 */     if ((this.netInBuffer.position() > 0) && (this.netInBuffer.position() < this.netInBuffer.limit())) throw new IOException(sm.getString("channel.nio.ssl.netInputNotEmpty"));
/* 369 */     if ((this.netOutBuffer.position() > 0) && (this.netOutBuffer.position() < this.netOutBuffer.limit())) throw new IOException(sm.getString("channel.nio.ssl.netOutputNotEmpty"));
/* 370 */     if (!getBufHandler().isReadBufferEmpty()) throw new IOException(sm.getString("channel.nio.ssl.appInputNotEmpty"));
/* 371 */     if (!getBufHandler().isWriteBufferEmpty()) throw new IOException(sm.getString("channel.nio.ssl.appOutputNotEmpty"));
/* 372 */     this.handshakeComplete = false;
/* 373 */     boolean isReadable = false;
/* 374 */     boolean isWriteable = false;
/* 375 */     boolean handshaking = true;
/* 376 */     Selector selector = null;
/* 377 */     SelectionKey key = null;
/*     */     try {
/* 379 */       this.sslEngine.beginHandshake();
/* 380 */       this.handshakeStatus = this.sslEngine.getHandshakeStatus();
/* 381 */       while (handshaking) {
/* 382 */         int hsStatus = handshake(isReadable, isWriteable);
/* 383 */         switch (hsStatus) {
/* 384 */         case -1:  throw new EOFException(sm.getString("channel.nio.ssl.eofDuringHandshake"));
/* 385 */         case 0:  handshaking = false; break;
/*     */         default: 
/* 387 */           long now = System.currentTimeMillis();
/* 388 */           if (selector == null) {
/* 389 */             selector = Selector.open();
/* 390 */             key = getIOChannel().register(selector, hsStatus);
/*     */           } else {
/* 392 */             key.interestOps(hsStatus);
/*     */           }
/* 394 */           int keyCount = selector.select(timeout);
/* 395 */           if ((keyCount == 0) && (System.currentTimeMillis() - now >= timeout)) {
/* 396 */             throw new SocketTimeoutException(sm.getString("channel.nio.ssl.timeoutDuringHandshake"));
/*     */           }
/* 398 */           isReadable = key.isReadable();
/* 399 */           isWriteable = key.isWritable(); }
/*     */       }
/*     */       IOException x;
/*     */       return;
/*     */     } catch (IOException x) {
/* 404 */       closeSilently();
/* 405 */       throw x;
/*     */     } catch (Exception cx) {
/* 407 */       closeSilently();
/* 408 */       x = new IOException(cx);
/* 409 */       throw x;
/*     */     } finally {
/* 411 */       if (key != null) try { key.cancel(); } catch (Exception localException3) {}
/* 412 */       if (selector != null) { try { selector.close();
/*     */         }
/*     */         catch (Exception localException4) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SSLEngineResult.HandshakeStatus tasks()
/*     */   {
/* 423 */     Runnable r = null;
/* 424 */     while ((r = this.sslEngine.getDelegatedTask()) != null) {
/* 425 */       r.run();
/*     */     }
/* 427 */     return this.sslEngine.getHandshakeStatus();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SSLEngineResult handshakeWrap(boolean doWrite)
/*     */     throws IOException
/*     */   {
/* 439 */     this.netOutBuffer.clear();
/*     */     
/* 441 */     getBufHandler().configureWriteBufferForRead();
/* 442 */     SSLEngineResult result = this.sslEngine.wrap(getBufHandler().getWriteBuffer(), this.netOutBuffer);
/*     */     
/* 444 */     this.netOutBuffer.flip();
/*     */     
/* 446 */     this.handshakeStatus = result.getHandshakeStatus();
/*     */     
/* 448 */     if (doWrite) flush(this.netOutBuffer);
/* 449 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SSLEngineResult handshakeUnwrap(boolean doread)
/*     */     throws IOException
/*     */   {
/* 460 */     if (this.netInBuffer.position() == this.netInBuffer.limit())
/*     */     {
/* 462 */       this.netInBuffer.clear();
/*     */     }
/* 464 */     if (doread)
/*     */     {
/* 466 */       int read = this.sc.read(this.netInBuffer);
/* 467 */       if (read == -1) { throw new IOException(sm.getString("channel.nio.ssl.eofDuringHandshake"));
/*     */       }
/*     */     }
/* 470 */     boolean cont = false;
/*     */     SSLEngineResult result;
/*     */     do
/*     */     {
/* 474 */       this.netInBuffer.flip();
/*     */       
/* 476 */       getBufHandler().configureReadBufferForWrite();
/* 477 */       result = this.sslEngine.unwrap(this.netInBuffer, getBufHandler().getReadBuffer());
/*     */       
/* 479 */       this.netInBuffer.compact();
/*     */       
/* 481 */       this.handshakeStatus = result.getHandshakeStatus();
/* 482 */       if ((result.getStatus() == SSLEngineResult.Status.OK) && 
/* 483 */         (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK))
/*     */       {
/* 485 */         this.handshakeStatus = tasks();
/*     */       }
/*     */       
/* 488 */       cont = (result.getStatus() == SSLEngineResult.Status.OK) && (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
/*     */     }
/* 490 */     while (cont);
/* 491 */     return result;
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
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 508 */     if (this.closing) return;
/* 509 */     this.closing = true;
/* 510 */     this.sslEngine.closeOutbound();
/*     */     
/* 512 */     if (!flush(this.netOutBuffer)) {
/* 513 */       throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
/*     */     }
/*     */     
/* 516 */     this.netOutBuffer.clear();
/*     */     
/* 518 */     SSLEngineResult handshake = this.sslEngine.wrap(getEmptyBuf(), this.netOutBuffer);
/*     */     
/* 520 */     if (handshake.getStatus() != SSLEngineResult.Status.CLOSED) {
/* 521 */       throw new IOException(sm.getString("channel.nio.ssl.invalidCloseState"));
/*     */     }
/*     */     
/* 524 */     this.netOutBuffer.flip();
/*     */     
/* 526 */     flush(this.netOutBuffer);
/*     */     
/*     */ 
/* 529 */     this.closed = ((!this.netOutBuffer.hasRemaining()) && (handshake.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_WRAP));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void close(boolean force)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 146	org/apache/tomcat/util/net/SecureNioChannel:close	()V
/*     */     //   4: iload_1
/*     */     //   5: ifne +10 -> 15
/*     */     //   8: aload_0
/*     */     //   9: getfield 4	org/apache/tomcat/util/net/SecureNioChannel:closed	Z
/*     */     //   12: ifeq +64 -> 76
/*     */     //   15: aload_0
/*     */     //   16: iconst_1
/*     */     //   17: putfield 4	org/apache/tomcat/util/net/SecureNioChannel:closed	Z
/*     */     //   20: aload_0
/*     */     //   21: getfield 24	org/apache/tomcat/util/net/SecureNioChannel:sc	Ljava/nio/channels/SocketChannel;
/*     */     //   24: invokevirtual 147	java/nio/channels/SocketChannel:socket	()Ljava/net/Socket;
/*     */     //   27: invokevirtual 148	java/net/Socket:close	()V
/*     */     //   30: aload_0
/*     */     //   31: getfield 24	org/apache/tomcat/util/net/SecureNioChannel:sc	Ljava/nio/channels/SocketChannel;
/*     */     //   34: invokevirtual 149	java/nio/channels/SocketChannel:close	()V
/*     */     //   37: goto +39 -> 76
/*     */     //   40: astore_2
/*     */     //   41: iload_1
/*     */     //   42: ifne +10 -> 52
/*     */     //   45: aload_0
/*     */     //   46: getfield 4	org/apache/tomcat/util/net/SecureNioChannel:closed	Z
/*     */     //   49: ifeq +25 -> 74
/*     */     //   52: aload_0
/*     */     //   53: iconst_1
/*     */     //   54: putfield 4	org/apache/tomcat/util/net/SecureNioChannel:closed	Z
/*     */     //   57: aload_0
/*     */     //   58: getfield 24	org/apache/tomcat/util/net/SecureNioChannel:sc	Ljava/nio/channels/SocketChannel;
/*     */     //   61: invokevirtual 147	java/nio/channels/SocketChannel:socket	()Ljava/net/Socket;
/*     */     //   64: invokevirtual 148	java/net/Socket:close	()V
/*     */     //   67: aload_0
/*     */     //   68: getfield 24	org/apache/tomcat/util/net/SecureNioChannel:sc	Ljava/nio/channels/SocketChannel;
/*     */     //   71: invokevirtual 149	java/nio/channels/SocketChannel:close	()V
/*     */     //   74: aload_2
/*     */     //   75: athrow
/*     */     //   76: return
/*     */     // Line number table:
/*     */     //   Java source line #536	-> byte code offset #0
/*     */     //   Java source line #538	-> byte code offset #4
/*     */     //   Java source line #539	-> byte code offset #15
/*     */     //   Java source line #540	-> byte code offset #20
/*     */     //   Java source line #541	-> byte code offset #30
/*     */     //   Java source line #538	-> byte code offset #40
/*     */     //   Java source line #539	-> byte code offset #52
/*     */     //   Java source line #540	-> byte code offset #57
/*     */     //   Java source line #541	-> byte code offset #67
/*     */     //   Java source line #544	-> byte code offset #76
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	77	0	this	SecureNioChannel
/*     */     //   0	77	1	force	boolean
/*     */     //   40	35	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	4	40	finally
/*     */   }
/*     */   
/*     */   private void closeSilently()
/*     */   {
/*     */     try
/*     */     {
/* 549 */       close(true);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 553 */       log.debug(sm.getString("channel.nio.ssl.closeSilentError"), ioe);
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
/*     */ 
/*     */ 
/*     */   public int read(ByteBuffer dst)
/*     */     throws IOException
/*     */   {
/* 571 */     if ((dst != getBufHandler().getReadBuffer()) && ((getAppReadBufHandler() == null) || 
/* 572 */       (dst != getAppReadBufHandler().getByteBuffer()))) {
/* 573 */       throw new IllegalArgumentException(sm.getString("channel.nio.ssl.invalidBuffer"));
/*     */     }
/*     */     
/* 576 */     if ((this.closing) || (this.closed)) { return -1;
/*     */     }
/* 578 */     if (!this.handshakeComplete) { throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
/*     */     }
/*     */     
/* 581 */     int netread = this.sc.read(this.netInBuffer);
/*     */     
/* 583 */     if (netread == -1) { return -1;
/*     */     }
/*     */     
/* 586 */     int read = 0;
/*     */     
/*     */ 
/*     */     do
/*     */     {
/* 591 */       this.netInBuffer.flip();
/*     */       
/* 593 */       SSLEngineResult unwrap = this.sslEngine.unwrap(this.netInBuffer, dst);
/*     */       
/* 595 */       this.netInBuffer.compact();
/*     */       
/* 597 */       if ((unwrap.getStatus() == SSLEngineResult.Status.OK) || (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW))
/*     */       {
/* 599 */         read += unwrap.bytesProduced();
/*     */         
/* 601 */         if (unwrap.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 602 */           tasks();
/*     */         }
/*     */         
/* 605 */         if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/*     */           break;
/*     */         }
/* 608 */       } else if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/* 609 */         if (read > 0) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 617 */         if (dst == getBufHandler().getReadBuffer())
/*     */         {
/* 619 */           getBufHandler().expand(this.sslEngine.getSession().getApplicationBufferSize());
/* 620 */           dst = getBufHandler().getReadBuffer();
/* 621 */         } else if (dst == getAppReadBufHandler().getByteBuffer())
/*     */         {
/* 623 */           getAppReadBufHandler().expand(this.sslEngine.getSession().getApplicationBufferSize());
/* 624 */           dst = getAppReadBufHandler().getByteBuffer();
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 629 */           throw new IOException(sm.getString("channel.nio.ssl.unwrapFailResize", new Object[] {unwrap.getStatus() }));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 634 */         throw new IOException(sm.getString("channel.nio.ssl.unwrapFail", new Object[] { unwrap.getStatus() }));
/*     */       }
/* 636 */     } while (this.netInBuffer.position() != 0);
/* 637 */     return read;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int write(ByteBuffer src)
/*     */     throws IOException
/*     */   {
/* 649 */     checkInterruptStatus();
/* 650 */     if (src == this.netOutBuffer)
/*     */     {
/*     */ 
/* 653 */       int written = this.sc.write(src);
/* 654 */       return written;
/*     */     }
/*     */     
/* 657 */     if ((this.closing) || (this.closed)) {
/* 658 */       throw new IOException(sm.getString("channel.nio.ssl.closing"));
/*     */     }
/*     */     
/* 661 */     if (!flush(this.netOutBuffer))
/*     */     {
/* 663 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 667 */     this.netOutBuffer.clear();
/*     */     
/* 669 */     SSLEngineResult result = this.sslEngine.wrap(src, this.netOutBuffer);
/*     */     
/* 671 */     int written = result.bytesConsumed();
/* 672 */     this.netOutBuffer.flip();
/*     */     
/* 674 */     if (result.getStatus() == SSLEngineResult.Status.OK) {
/* 675 */       if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) tasks();
/*     */     } else {
/* 677 */       throw new IOException(sm.getString("channel.nio.ssl.wrapFail", new Object[] { result.getStatus() }));
/*     */     }
/*     */     
/*     */ 
/* 681 */     flush(this.netOutBuffer);
/*     */     
/* 683 */     return written;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getOutboundRemaining()
/*     */   {
/* 689 */     return this.netOutBuffer.remaining();
/*     */   }
/*     */   
/*     */   public boolean flushOutbound() throws IOException
/*     */   {
/* 694 */     int remaining = this.netOutBuffer.remaining();
/* 695 */     flush(this.netOutBuffer);
/* 696 */     int remaining2 = this.netOutBuffer.remaining();
/* 697 */     return remaining2 < remaining;
/*     */   }
/*     */   
/*     */   public boolean isHandshakeComplete()
/*     */   {
/* 702 */     return this.handshakeComplete;
/*     */   }
/*     */   
/*     */   public boolean isClosing()
/*     */   {
/* 707 */     return this.closing;
/*     */   }
/*     */   
/*     */   public SSLEngine getSslEngine() {
/* 711 */     return this.sslEngine;
/*     */   }
/*     */   
/*     */   public ByteBuffer getEmptyBuf() {
/* 715 */     return emptyBuf;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SecureNioChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */