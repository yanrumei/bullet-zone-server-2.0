/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AsynchronousSocketChannel;
/*      */ import java.nio.channels.CompletionHandler;
/*      */ import java.nio.channels.WritePendingException;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLEngineResult;
/*      */ import javax.net.ssl.SSLEngineResult.HandshakeStatus;
/*      */ import javax.net.ssl.SSLEngineResult.Status;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.ByteBufferUtils;
/*      */ import org.apache.tomcat.util.compat.JreCompat;
/*      */ import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
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
/*      */ public class SecureNio2Channel
/*      */   extends Nio2Channel
/*      */ {
/*   51 */   private static final Log log = LogFactory.getLog(SecureNio2Channel.class);
/*   52 */   private static final StringManager sm = StringManager.getManager(SecureNio2Channel.class);
/*      */   
/*      */   private static final int DEFAULT_NET_BUFFER_SIZE = 16921;
/*      */   
/*      */   protected ByteBuffer netInBuffer;
/*      */   
/*      */   protected ByteBuffer netOutBuffer;
/*      */   
/*      */   protected SSLEngine sslEngine;
/*      */   
/*      */   protected final Nio2Endpoint endpoint;
/*      */   
/*   64 */   protected boolean sniComplete = false;
/*      */   
/*      */   private volatile boolean handshakeComplete;
/*      */   
/*      */   private volatile SSLEngineResult.HandshakeStatus handshakeStatus;
/*   69 */   private volatile boolean unwrapBeforeRead = false;
/*      */   
/*      */   protected boolean closed;
/*      */   protected boolean closing;
/*      */   private final CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> handshakeReadCompletionHandler;
/*      */   private final CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> handshakeWriteCompletionHandler;
/*      */   
/*      */   public SecureNio2Channel(SocketBufferHandler bufHandler, Nio2Endpoint endpoint)
/*      */   {
/*   78 */     super(bufHandler);
/*   79 */     this.endpoint = endpoint;
/*   80 */     if (endpoint.getSocketProperties().getDirectSslBuffer()) {
/*   81 */       this.netInBuffer = ByteBuffer.allocateDirect(16921);
/*   82 */       this.netOutBuffer = ByteBuffer.allocateDirect(16921);
/*      */     } else {
/*   84 */       this.netInBuffer = ByteBuffer.allocate(16921);
/*   85 */       this.netOutBuffer = ByteBuffer.allocate(16921);
/*      */     }
/*   87 */     this.handshakeReadCompletionHandler = new HandshakeReadCompletionHandler(null);
/*   88 */     this.handshakeWriteCompletionHandler = new HandshakeWriteCompletionHandler(null);
/*      */   }
/*      */   
/*      */   private class HandshakeReadCompletionHandler implements CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>>
/*      */   {
/*      */     private HandshakeReadCompletionHandler() {}
/*      */     
/*      */     public void completed(Integer result, SocketWrapperBase<Nio2Channel> attachment) {
/*   96 */       if (result.intValue() < 0) {
/*   97 */         failed(new EOFException(), attachment);
/*      */       } else {
/*   99 */         SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.OPEN_READ, false);
/*      */       }
/*      */     }
/*      */     
/*      */     public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment) {
/*  104 */       SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.ERROR, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private class HandshakeWriteCompletionHandler implements CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>>
/*      */   {
/*      */     private HandshakeWriteCompletionHandler() {}
/*      */     
/*      */     public void completed(Integer result, SocketWrapperBase<Nio2Channel> attachment) {
/*  113 */       if (result.intValue() < 0) {
/*  114 */         failed(new EOFException(), attachment);
/*      */       } else {
/*  116 */         SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.OPEN_WRITE, false);
/*      */       }
/*      */     }
/*      */     
/*      */     public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment) {
/*  121 */       SecureNio2Channel.this.endpoint.processSocket(attachment, SocketEvent.ERROR, false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void reset(AsynchronousSocketChannel channel, SocketWrapperBase<Nio2Channel> socket)
/*      */     throws IOException
/*      */   {
/*  129 */     super.reset(channel, socket);
/*  130 */     this.sslEngine = null;
/*  131 */     this.sniComplete = false;
/*  132 */     this.handshakeComplete = false;
/*  133 */     this.closed = false;
/*  134 */     this.closing = false;
/*  135 */     this.netInBuffer.clear();
/*      */   }
/*      */   
/*      */   public void free()
/*      */   {
/*  140 */     super.free();
/*  141 */     if (this.endpoint.getSocketProperties().getDirectSslBuffer()) {
/*  142 */       ByteBufferUtils.cleanDirectBuffer(this.netInBuffer);
/*  143 */       ByteBufferUtils.cleanDirectBuffer(this.netOutBuffer);
/*      */     }
/*      */   }
/*      */   
/*      */   private class FutureFlush implements Future<Boolean> {
/*      */     private Future<Integer> integer;
/*      */     
/*  150 */     protected FutureFlush() { this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer); }
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning)
/*      */     {
/*  154 */       return this.integer.cancel(mayInterruptIfRunning);
/*      */     }
/*      */     
/*      */     public boolean isCancelled() {
/*  158 */       return this.integer.isCancelled();
/*      */     }
/*      */     
/*      */     public boolean isDone() {
/*  162 */       return this.integer.isDone();
/*      */     }
/*      */     
/*      */     public Boolean get() throws InterruptedException, ExecutionException
/*      */     {
/*  167 */       return Boolean.valueOf(((Integer)this.integer.get()).intValue() >= 0);
/*      */     }
/*      */     
/*      */     public Boolean get(long timeout, TimeUnit unit)
/*      */       throws InterruptedException, ExecutionException, TimeoutException
/*      */     {
/*  173 */       return Boolean.valueOf(((Integer)this.integer.get(timeout, unit)).intValue() >= 0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Future<Boolean> flush()
/*      */   {
/*  185 */     return new FutureFlush();
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
/*      */   public int handshake()
/*      */     throws IOException
/*      */   {
/*  205 */     return handshakeInternal(true);
/*      */   }
/*      */   
/*      */   protected int handshakeInternal(boolean async) throws IOException {
/*  209 */     if (this.handshakeComplete) {
/*  210 */       return 0;
/*      */     }
/*      */     
/*  213 */     if (!this.sniComplete) {
/*  214 */       int sniResult = processSNI();
/*  215 */       if (sniResult == 0) {
/*  216 */         this.sniComplete = true;
/*      */       } else {
/*  218 */         return sniResult;
/*      */       }
/*      */     }
/*      */     
/*  222 */     SSLEngineResult handshake = null;
/*      */     
/*  224 */     while (!this.handshakeComplete) {
/*  225 */       switch (this.handshakeStatus)
/*      */       {
/*      */       case NOT_HANDSHAKING: 
/*  228 */         throw new IOException(sm.getString("channel.nio.ssl.notHandshaking"));
/*      */       
/*      */       case FINISHED: 
/*  231 */         if (this.endpoint.hasNegotiableProtocols()) {
/*  232 */           if ((this.sslEngine instanceof SSLUtil.ProtocolInfo)) {
/*  233 */             this.socket.setNegotiatedProtocol(((SSLUtil.ProtocolInfo)this.sslEngine)
/*  234 */               .getNegotiatedProtocol());
/*  235 */           } else if (JreCompat.isJre9Available()) {
/*  236 */             this.socket.setNegotiatedProtocol(
/*  237 */               JreCompat.getInstance().getApplicationProtocol(this.sslEngine));
/*      */           }
/*      */         }
/*      */         
/*  241 */         this.handshakeComplete = (!this.netOutBuffer.hasRemaining());
/*      */         
/*  243 */         if (this.handshakeComplete) {
/*  244 */           return 0;
/*      */         }
/*  246 */         if (async) {
/*  247 */           this.sc.write(this.netOutBuffer, this.socket, this.handshakeWriteCompletionHandler);
/*      */         } else {
/*      */           try {
/*  250 */             this.sc.write(this.netOutBuffer).get(this.endpoint.getConnectionTimeout(), TimeUnit.MILLISECONDS);
/*      */           } catch (InterruptedException|ExecutionException|TimeoutException e) {
/*  252 */             throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
/*      */           }
/*      */         }
/*  255 */         return 1;
/*      */       
/*      */ 
/*      */       case NEED_WRAP: 
/*      */         try
/*      */         {
/*  261 */           handshake = handshakeWrap();
/*      */         } catch (SSLException e) {
/*  263 */           if (log.isDebugEnabled()) {
/*  264 */             log.debug(sm.getString("channel.nio.ssl.wrapException"), e);
/*      */           }
/*  266 */           handshake = handshakeWrap();
/*      */         }
/*  268 */         if (handshake.getStatus() == SSLEngineResult.Status.OK) {
/*  269 */           if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK)
/*  270 */             this.handshakeStatus = tasks();
/*  271 */         } else { if (handshake.getStatus() == SSLEngineResult.Status.CLOSED) {
/*  272 */             return -1;
/*      */           }
/*      */           
/*  275 */           throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringWrap", new Object[] { handshake.getStatus() }));
/*      */         }
/*  277 */         if ((this.handshakeStatus != SSLEngineResult.HandshakeStatus.NEED_UNWRAP) || (this.netOutBuffer.remaining() > 0))
/*      */         {
/*  279 */           if (async) {
/*  280 */             this.sc.write(this.netOutBuffer, this.socket, this.handshakeWriteCompletionHandler);
/*      */           } else {
/*      */             try {
/*  283 */               this.sc.write(this.netOutBuffer).get(this.endpoint.getConnectionTimeout(), TimeUnit.MILLISECONDS);
/*      */             } catch (InterruptedException|ExecutionException|TimeoutException e) {
/*  285 */               throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
/*      */             }
/*      */           }
/*  288 */           return 1;
/*      */         }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       case NEED_UNWRAP: 
/*  296 */         handshake = handshakeUnwrap();
/*  297 */         if (handshake.getStatus() == SSLEngineResult.Status.OK) {
/*  298 */           if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_TASK)
/*  299 */             this.handshakeStatus = tasks();
/*  300 */         } else { if (handshake.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/*  301 */             if (this.netInBuffer.position() == this.netInBuffer.limit())
/*      */             {
/*  303 */               this.netInBuffer.clear();
/*      */             }
/*      */             
/*  306 */             if (async) {
/*  307 */               this.sc.read(this.netInBuffer, this.socket, this.handshakeReadCompletionHandler);
/*      */             } else {
/*      */               try
/*      */               {
/*  311 */                 int read = ((Integer)this.sc.read(this.netInBuffer).get(this.endpoint.getConnectionTimeout(), TimeUnit.MILLISECONDS)).intValue();
/*  312 */                 if (read == -1) {
/*  313 */                   throw new EOFException();
/*      */                 }
/*      */               } catch (InterruptedException|ExecutionException|TimeoutException e) {
/*  316 */                 throw new IOException(sm.getString("channel.nio.ssl.handshakeError"));
/*      */               }
/*      */             }
/*  319 */             return 1;
/*      */           }
/*  321 */           throw new IOException(sm.getString("channel.nio.ssl.unexpectedStatusDuringUnwrap", new Object[] { this.handshakeStatus }));
/*      */         }
/*      */         
/*      */         break;
/*      */       case NEED_TASK: 
/*  326 */         this.handshakeStatus = tasks();
/*  327 */         break;
/*      */       default: 
/*  329 */         throw new IllegalStateException(sm.getString("channel.nio.ssl.invalidStatus", new Object[] { this.handshakeStatus }));
/*      */       }
/*      */       
/*      */     }
/*  333 */     return this.handshakeComplete ? 0 : handshakeInternal(async);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int processSNI()
/*      */     throws IOException
/*      */   {
/*  346 */     if (this.netInBuffer.position() == 0) {
/*  347 */       this.sc.read(this.netInBuffer, this.socket, this.handshakeReadCompletionHandler);
/*  348 */       return 1;
/*      */     }
/*      */     
/*  351 */     TLSClientHelloExtractor extractor = new TLSClientHelloExtractor(this.netInBuffer);
/*      */     
/*  353 */     if ((extractor.getResult() == TLSClientHelloExtractor.ExtractorResult.UNDERFLOW) && 
/*  354 */       (this.netInBuffer.capacity() < this.endpoint.getSniParseLimit()))
/*      */     {
/*      */ 
/*  357 */       int newLimit = Math.min(this.netInBuffer.capacity() * 2, this.endpoint.getSniParseLimit());
/*  358 */       log.info(sm.getString("channel.nio.ssl.expandNetInBuffer", new Object[] {
/*  359 */         Integer.toString(newLimit) }));
/*      */       
/*  361 */       this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, newLimit);
/*  362 */       this.sc.read(this.netInBuffer, this.socket, this.handshakeReadCompletionHandler);
/*  363 */       return 1;
/*      */     }
/*      */     
/*  366 */     String hostName = null;
/*  367 */     List<Cipher> clientRequestedCiphers = null;
/*  368 */     List<String> clientRequestedApplicationProtocols = null;
/*  369 */     switch (extractor.getResult()) {
/*      */     case COMPLETE: 
/*  371 */       hostName = extractor.getSNIValue();
/*      */       
/*  373 */       clientRequestedApplicationProtocols = extractor.getClientRequestedApplicationProtocols();
/*      */     
/*      */     case NOT_PRESENT: 
/*  376 */       clientRequestedCiphers = extractor.getClientRequestedCiphers();
/*  377 */       break;
/*      */     case NEED_READ: 
/*  379 */       this.sc.read(this.netInBuffer, this.socket, this.handshakeReadCompletionHandler);
/*  380 */       return 1;
/*      */     
/*      */     case UNDERFLOW: 
/*  383 */       if (log.isDebugEnabled()) {
/*  384 */         log.debug(sm.getString("channel.nio.ssl.sniDefault"));
/*      */       }
/*  386 */       hostName = this.endpoint.getDefaultSSLHostConfigName();
/*  387 */       clientRequestedCiphers = Collections.emptyList();
/*  388 */       break;
/*      */     case NON_SECURE: 
/*  390 */       this.netOutBuffer.clear();
/*  391 */       this.netOutBuffer.put(TLSClientHelloExtractor.USE_TLS_RESPONSE);
/*  392 */       this.netOutBuffer.flip();
/*  393 */       flush();
/*  394 */       throw new IOException(sm.getString("channel.nio.ssl.foundHttp"));
/*      */     }
/*      */     
/*  397 */     if (log.isDebugEnabled()) {
/*  398 */       log.debug(sm.getString("channel.nio.ssl.sniHostName", new Object[] { hostName }));
/*      */     }
/*      */     
/*  401 */     this.sslEngine = this.endpoint.createSSLEngine(hostName, clientRequestedCiphers, clientRequestedApplicationProtocols);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  406 */     getBufHandler().expand(this.sslEngine.getSession().getApplicationBufferSize());
/*  407 */     if (this.netOutBuffer.capacity() < this.sslEngine.getSession().getApplicationBufferSize())
/*      */     {
/*  409 */       log.info(sm.getString("channel.nio.ssl.expandNetOutBuffer", new Object[] {
/*  410 */         Integer.toString(this.sslEngine.getSession().getApplicationBufferSize()) }));
/*      */     }
/*  412 */     this.netInBuffer = ByteBufferUtils.expand(this.netInBuffer, this.sslEngine.getSession().getPacketBufferSize());
/*  413 */     this.netOutBuffer = ByteBufferUtils.expand(this.netOutBuffer, this.sslEngine.getSession().getPacketBufferSize());
/*      */     
/*      */ 
/*  416 */     this.netOutBuffer.position(0);
/*  417 */     this.netOutBuffer.limit(0);
/*      */     
/*      */ 
/*  420 */     this.sslEngine.beginHandshake();
/*  421 */     this.handshakeStatus = this.sslEngine.getHandshakeStatus();
/*      */     
/*  423 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rehandshake()
/*      */     throws IOException
/*      */   {
/*  436 */     if ((this.netInBuffer.position() > 0) && (this.netInBuffer.position() < this.netInBuffer.limit())) throw new IOException(sm.getString("channel.nio.ssl.netInputNotEmpty"));
/*  437 */     if ((this.netOutBuffer.position() > 0) && (this.netOutBuffer.position() < this.netOutBuffer.limit())) throw new IOException(sm.getString("channel.nio.ssl.netOutputNotEmpty"));
/*  438 */     if (!getBufHandler().isReadBufferEmpty()) throw new IOException(sm.getString("channel.nio.ssl.appInputNotEmpty"));
/*  439 */     if (!getBufHandler().isWriteBufferEmpty()) { throw new IOException(sm.getString("channel.nio.ssl.appOutputNotEmpty"));
/*      */     }
/*  441 */     this.netOutBuffer.position(0);
/*  442 */     this.netOutBuffer.limit(0);
/*  443 */     this.netInBuffer.position(0);
/*  444 */     this.netInBuffer.limit(0);
/*  445 */     getBufHandler().reset();
/*      */     
/*  447 */     this.handshakeComplete = false;
/*      */     
/*  449 */     this.sslEngine.beginHandshake();
/*  450 */     this.handshakeStatus = this.sslEngine.getHandshakeStatus();
/*      */     
/*  452 */     boolean handshaking = true;
/*      */     try {
/*  454 */       while (handshaking) {
/*  455 */         int hsStatus = handshakeInternal(false);
/*  456 */         switch (hsStatus) {
/*  457 */         case -1:  throw new EOFException(sm.getString("channel.nio.ssl.eofDuringHandshake"));
/*  458 */         case 0:  handshaking = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException x) {
/*  463 */       closeSilently();
/*  464 */       throw x;
/*      */     } catch (Exception cx) {
/*  466 */       closeSilently();
/*  467 */       IOException x = new IOException(cx);
/*  468 */       throw x;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SSLEngineResult.HandshakeStatus tasks()
/*      */   {
/*  478 */     Runnable r = null;
/*  479 */     while ((r = this.sslEngine.getDelegatedTask()) != null) {
/*  480 */       r.run();
/*      */     }
/*  482 */     return this.sslEngine.getHandshakeStatus();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SSLEngineResult handshakeWrap()
/*      */     throws IOException
/*      */   {
/*  493 */     this.netOutBuffer.clear();
/*      */     
/*  495 */     getBufHandler().configureWriteBufferForRead();
/*  496 */     SSLEngineResult result = this.sslEngine.wrap(getBufHandler().getWriteBuffer(), this.netOutBuffer);
/*      */     
/*  498 */     this.netOutBuffer.flip();
/*      */     
/*  500 */     this.handshakeStatus = result.getHandshakeStatus();
/*  501 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SSLEngineResult handshakeUnwrap()
/*      */     throws IOException
/*      */   {
/*  511 */     boolean cont = false;
/*      */     SSLEngineResult result;
/*      */     do
/*      */     {
/*  515 */       this.netInBuffer.flip();
/*      */       
/*  517 */       getBufHandler().configureReadBufferForWrite();
/*  518 */       result = this.sslEngine.unwrap(this.netInBuffer, getBufHandler().getReadBuffer());
/*      */       
/*  520 */       this.netInBuffer.compact();
/*      */       
/*  522 */       this.handshakeStatus = result.getHandshakeStatus();
/*  523 */       if ((result.getStatus() == SSLEngineResult.Status.OK) && 
/*  524 */         (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK))
/*      */       {
/*  526 */         this.handshakeStatus = tasks();
/*      */       }
/*      */       
/*  529 */       cont = (result.getStatus() == SSLEngineResult.Status.OK) && (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);
/*      */     }
/*  531 */     while (cont);
/*  532 */     return result;
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
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  548 */     if (this.closing) return;
/*  549 */     this.closing = true;
/*  550 */     this.sslEngine.closeOutbound();
/*      */     try
/*      */     {
/*  553 */       if (!((Boolean)flush().get(this.endpoint.getConnectionTimeout(), TimeUnit.MILLISECONDS)).booleanValue()) {
/*  554 */         throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
/*      */       }
/*      */     } catch (InterruptedException|ExecutionException|TimeoutException e) {
/*  557 */       throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"), e);
/*      */     } catch (WritePendingException e) {
/*  559 */       throw new IOException(sm.getString("channel.nio.ssl.pendingWriteDuringClose"), e);
/*      */     }
/*      */     
/*  562 */     this.netOutBuffer.clear();
/*      */     
/*  564 */     SSLEngineResult handshake = this.sslEngine.wrap(getEmptyBuf(), this.netOutBuffer);
/*      */     
/*  566 */     if (handshake.getStatus() != SSLEngineResult.Status.CLOSED) {
/*  567 */       throw new IOException(sm.getString("channel.nio.ssl.invalidCloseState"));
/*      */     }
/*      */     
/*  570 */     this.netOutBuffer.flip();
/*      */     try
/*      */     {
/*  573 */       if (!((Boolean)flush().get(this.endpoint.getConnectionTimeout(), TimeUnit.MILLISECONDS)).booleanValue()) {
/*  574 */         throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"));
/*      */       }
/*      */     } catch (InterruptedException|ExecutionException|TimeoutException e) {
/*  577 */       throw new IOException(sm.getString("channel.nio.ssl.remainingDataDuringClose"), e);
/*      */     } catch (WritePendingException e) {
/*  579 */       throw new IOException(sm.getString("channel.nio.ssl.pendingWriteDuringClose"), e);
/*      */     }
/*      */     
/*      */ 
/*  583 */     this.closed = ((!this.netOutBuffer.hasRemaining()) && (handshake.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NEED_WRAP));
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void close(boolean force)
/*      */     throws IOException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 156	org/apache/tomcat/util/net/SecureNio2Channel:close	()V
/*      */     //   4: iload_1
/*      */     //   5: ifne +10 -> 15
/*      */     //   8: aload_0
/*      */     //   9: getfield 22	org/apache/tomcat/util/net/SecureNio2Channel:closed	Z
/*      */     //   12: ifeq +44 -> 56
/*      */     //   15: aload_0
/*      */     //   16: iconst_1
/*      */     //   17: putfield 22	org/apache/tomcat/util/net/SecureNio2Channel:closed	Z
/*      */     //   20: aload_0
/*      */     //   21: getfield 47	org/apache/tomcat/util/net/SecureNio2Channel:sc	Ljava/nio/channels/AsynchronousSocketChannel;
/*      */     //   24: invokevirtual 157	java/nio/channels/AsynchronousSocketChannel:close	()V
/*      */     //   27: goto +29 -> 56
/*      */     //   30: astore_2
/*      */     //   31: iload_1
/*      */     //   32: ifne +10 -> 42
/*      */     //   35: aload_0
/*      */     //   36: getfield 22	org/apache/tomcat/util/net/SecureNio2Channel:closed	Z
/*      */     //   39: ifeq +15 -> 54
/*      */     //   42: aload_0
/*      */     //   43: iconst_1
/*      */     //   44: putfield 22	org/apache/tomcat/util/net/SecureNio2Channel:closed	Z
/*      */     //   47: aload_0
/*      */     //   48: getfield 47	org/apache/tomcat/util/net/SecureNio2Channel:sc	Ljava/nio/channels/AsynchronousSocketChannel;
/*      */     //   51: invokevirtual 157	java/nio/channels/AsynchronousSocketChannel:close	()V
/*      */     //   54: aload_2
/*      */     //   55: athrow
/*      */     //   56: return
/*      */     // Line number table:
/*      */     //   Java source line #590	-> byte code offset #0
/*      */     //   Java source line #592	-> byte code offset #4
/*      */     //   Java source line #593	-> byte code offset #15
/*      */     //   Java source line #594	-> byte code offset #20
/*      */     //   Java source line #592	-> byte code offset #30
/*      */     //   Java source line #593	-> byte code offset #42
/*      */     //   Java source line #594	-> byte code offset #47
/*      */     //   Java source line #597	-> byte code offset #56
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	57	0	this	SecureNio2Channel
/*      */     //   0	57	1	force	boolean
/*      */     //   30	25	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	4	30	finally
/*      */   }
/*      */   
/*      */   private void closeSilently()
/*      */   {
/*      */     try
/*      */     {
/*  602 */       close(true);
/*      */     }
/*      */     catch (IOException ioe)
/*      */     {
/*  606 */       log.debug(sm.getString("channel.nio.ssl.closeSilentError"), ioe);
/*      */     }
/*      */   }
/*      */   
/*      */   private class FutureRead implements Future<Integer> {
/*      */     private ByteBuffer dst;
/*      */     private Future<Integer> integer;
/*      */     
/*      */     private FutureRead(ByteBuffer dst) {
/*  615 */       this.dst = dst;
/*  616 */       if ((SecureNio2Channel.this.unwrapBeforeRead) || (SecureNio2Channel.this.netInBuffer.position() > 0)) {
/*  617 */         this.integer = null;
/*      */       } else {
/*  619 */         this.integer = SecureNio2Channel.this.sc.read(SecureNio2Channel.this.netInBuffer);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  624 */       return this.integer == null ? false : this.integer.cancel(mayInterruptIfRunning);
/*      */     }
/*      */     
/*      */     public boolean isCancelled() {
/*  628 */       return this.integer == null ? false : this.integer.isCancelled();
/*      */     }
/*      */     
/*      */     public boolean isDone() {
/*  632 */       return this.integer == null ? true : this.integer.isDone();
/*      */     }
/*      */     
/*      */     public Integer get() throws InterruptedException, ExecutionException {
/*      */       try {
/*  637 */         return this.integer == null ? unwrap(SecureNio2Channel.this.netInBuffer.position(), -1L, TimeUnit.MILLISECONDS) : unwrap(((Integer)this.integer.get()).intValue(), -1L, TimeUnit.MILLISECONDS);
/*      */       }
/*      */       catch (TimeoutException e) {
/*  640 */         throw new ExecutionException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public Integer get(long timeout, TimeUnit unit)
/*      */       throws InterruptedException, ExecutionException, TimeoutException
/*      */     {
/*  647 */       return this.integer == null ? unwrap(SecureNio2Channel.this.netInBuffer.position(), timeout, unit) : unwrap(((Integer)this.integer.get(timeout, unit)).intValue(), timeout, unit);
/*      */     }
/*      */     
/*      */     private Integer unwrap(int nRead, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException, InterruptedException {
/*  651 */       if ((SecureNio2Channel.this.closing) || (SecureNio2Channel.this.closed)) {
/*  652 */         return Integer.valueOf(-1);
/*      */       }
/*  654 */       if (nRead < 0) {
/*  655 */         return Integer.valueOf(-1);
/*      */       }
/*  657 */       int read = 0;
/*      */       
/*      */ 
/*      */       do
/*      */       {
/*  662 */         SecureNio2Channel.this.netInBuffer.flip();
/*      */         try
/*      */         {
/*  665 */           unwrap = SecureNio2Channel.this.sslEngine.unwrap(SecureNio2Channel.this.netInBuffer, this.dst);
/*      */         } catch (SSLException e) { SSLEngineResult unwrap;
/*  667 */           throw new ExecutionException(e);
/*      */         }
/*      */         SSLEngineResult unwrap;
/*  670 */         SecureNio2Channel.this.netInBuffer.compact();
/*  671 */         if ((unwrap.getStatus() == SSLEngineResult.Status.OK) || (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW))
/*      */         {
/*  673 */           read += unwrap.bytesProduced();
/*      */           
/*  675 */           if (unwrap.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/*  676 */             SecureNio2Channel.this.tasks();
/*      */           }
/*      */           
/*  679 */           if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/*  680 */             if (read != 0) break;
/*  681 */             this.integer = SecureNio2Channel.this.sc.read(SecureNio2Channel.this.netInBuffer);
/*  682 */             if (timeout > 0L) {
/*  683 */               return unwrap(((Integer)this.integer.get(timeout, unit)).intValue(), timeout, unit);
/*      */             }
/*  685 */             return unwrap(((Integer)this.integer.get()).intValue(), -1L, TimeUnit.MILLISECONDS);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  691 */         else if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/*  692 */           if (read > 0) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  700 */           if (this.dst == SecureNio2Channel.this.getBufHandler().getReadBuffer())
/*      */           {
/*      */ 
/*  703 */             SecureNio2Channel.this.getBufHandler().expand(SecureNio2Channel.this.sslEngine.getSession().getApplicationBufferSize());
/*  704 */             this.dst = SecureNio2Channel.this.getBufHandler().getReadBuffer();
/*  705 */           } else if (this.dst == SecureNio2Channel.this.getAppReadBufHandler().getByteBuffer())
/*      */           {
/*  707 */             SecureNio2Channel.this.getAppReadBufHandler().expand(SecureNio2Channel.this.sslEngine.getSession().getApplicationBufferSize());
/*  708 */             this.dst = SecureNio2Channel.this.getAppReadBufHandler().getByteBuffer();
/*      */           }
/*      */           else
/*      */           {
/*  712 */             throw new ExecutionException(new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.unwrapFailResize", new Object[] { unwrap.getStatus() })));
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  717 */           throw new ExecutionException(new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.unwrapFail", new Object[] { unwrap.getStatus() })));
/*      */         }
/*  719 */       } while (SecureNio2Channel.this.netInBuffer.position() != 0);
/*  720 */       if (!this.dst.hasRemaining()) {
/*  721 */         SecureNio2Channel.this.unwrapBeforeRead = true;
/*      */       } else {
/*  723 */         SecureNio2Channel.this.unwrapBeforeRead = false;
/*      */       }
/*  725 */       return Integer.valueOf(read);
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
/*      */   public Future<Integer> read(ByteBuffer dst)
/*      */   {
/*  738 */     if (!this.handshakeComplete) {
/*  739 */       throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
/*      */     }
/*  741 */     return new FutureRead(dst, null);
/*      */   }
/*      */   
/*      */   private class FutureWrite implements Future<Integer> {
/*      */     private final ByteBuffer src;
/*  746 */     private Future<Integer> integer = null;
/*  747 */     private int written = 0;
/*  748 */     private Throwable t = null;
/*      */     
/*  750 */     private FutureWrite(ByteBuffer src) { this.src = src;
/*      */       
/*  752 */       if ((SecureNio2Channel.this.closing) || (SecureNio2Channel.this.closed)) {
/*  753 */         this.t = new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.closing"));
/*      */       } else {
/*  755 */         wrap();
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  760 */       return this.integer.cancel(mayInterruptIfRunning);
/*      */     }
/*      */     
/*      */     public boolean isCancelled() {
/*  764 */       return this.integer.isCancelled();
/*      */     }
/*      */     
/*      */     public boolean isDone() {
/*  768 */       return this.integer.isDone();
/*      */     }
/*      */     
/*      */     public Integer get() throws InterruptedException, ExecutionException {
/*  772 */       if (this.t != null) {
/*  773 */         throw new ExecutionException(this.t);
/*      */       }
/*  775 */       if ((((Integer)this.integer.get()).intValue() > 0) && (this.written == 0)) {
/*  776 */         wrap();
/*  777 */         return get(); }
/*  778 */       if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
/*  779 */         this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
/*  780 */         return get();
/*      */       }
/*  782 */       return Integer.valueOf(this.written);
/*      */     }
/*      */     
/*      */ 
/*      */     public Integer get(long timeout, TimeUnit unit)
/*      */       throws InterruptedException, ExecutionException, TimeoutException
/*      */     {
/*  789 */       if (this.t != null) {
/*  790 */         throw new ExecutionException(this.t);
/*      */       }
/*  792 */       if ((((Integer)this.integer.get(timeout, unit)).intValue() > 0) && (this.written == 0)) {
/*  793 */         wrap();
/*  794 */         return get(timeout, unit); }
/*  795 */       if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
/*  796 */         this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
/*  797 */         return get(timeout, unit);
/*      */       }
/*  799 */       return Integer.valueOf(this.written);
/*      */     }
/*      */     
/*      */     protected void wrap() {
/*      */       try {
/*  804 */         if (!SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
/*  805 */           SecureNio2Channel.this.netOutBuffer.clear();
/*  806 */           SSLEngineResult result = SecureNio2Channel.this.sslEngine.wrap(this.src, SecureNio2Channel.this.netOutBuffer);
/*  807 */           this.written = result.bytesConsumed();
/*  808 */           SecureNio2Channel.this.netOutBuffer.flip();
/*  809 */           if (result.getStatus() == SSLEngineResult.Status.OK) {
/*  810 */             if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK)
/*  811 */               SecureNio2Channel.this.tasks();
/*      */           } else {
/*  813 */             this.t = new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.wrapFail", new Object[] { result.getStatus() }));
/*      */           }
/*      */         }
/*  816 */         this.integer = SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer);
/*      */       } catch (SSLException e) {
/*  818 */         this.t = e;
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
/*      */   public Future<Integer> write(ByteBuffer src)
/*      */   {
/*  831 */     return new FutureWrite(src, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <A> void read(final ByteBuffer dst, final long timeout, TimeUnit unit, final A attachment, final CompletionHandler<Integer, ? super A> handler)
/*      */   {
/*  839 */     if ((this.closing) || (this.closed)) {
/*  840 */       handler.completed(Integer.valueOf(-1), attachment);
/*  841 */       return;
/*      */     }
/*  843 */     if (!this.handshakeComplete) {
/*  844 */       throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
/*      */     }
/*  846 */     CompletionHandler<Integer, A> readCompletionHandler = new CompletionHandler()
/*      */     {
/*      */       public void completed(Integer nBytes, A attach) {
/*  849 */         if (nBytes.intValue() < 0) {
/*  850 */           failed(new EOFException(), attach);
/*      */         } else {
/*      */           try {
/*  853 */             ByteBuffer dst2 = dst;
/*      */             
/*  855 */             int read = 0;
/*      */             
/*      */ 
/*      */             do
/*      */             {
/*  860 */               SecureNio2Channel.this.netInBuffer.flip();
/*      */               
/*  862 */               SSLEngineResult unwrap = SecureNio2Channel.this.sslEngine.unwrap(SecureNio2Channel.this.netInBuffer, dst2);
/*      */               
/*  864 */               SecureNio2Channel.this.netInBuffer.compact();
/*  865 */               if ((unwrap.getStatus() == SSLEngineResult.Status.OK) || (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW))
/*      */               {
/*  867 */                 read += unwrap.bytesProduced();
/*      */                 
/*  869 */                 if (unwrap.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/*  870 */                   SecureNio2Channel.this.tasks();
/*      */                 }
/*  872 */                 if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/*  873 */                   if (read != 0) break;
/*  874 */                   SecureNio2Channel.this.sc.read(SecureNio2Channel.this.netInBuffer, timeout, attachment, handler, this);
/*      */ 
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*  880 */               else if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
/*  881 */                 if (read > 0) {
/*      */                   break;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  889 */                 if (dst2 == SecureNio2Channel.this.getBufHandler().getReadBuffer())
/*      */                 {
/*  891 */                   SecureNio2Channel.this.getBufHandler().expand(SecureNio2Channel.this.sslEngine
/*  892 */                     .getSession().getApplicationBufferSize());
/*  893 */                   dst2 = SecureNio2Channel.this.getBufHandler().getReadBuffer();
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/*  898 */                   throw new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.unwrapFailResize", new Object[] { unwrap.getStatus() }));
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/*  903 */                 throw new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.unwrapFail", new Object[] { unwrap.getStatus() }));
/*      */               }
/*      */               
/*  906 */             } while (SecureNio2Channel.this.netInBuffer.position() != 0);
/*  907 */             if (!dst2.hasRemaining()) {
/*  908 */               SecureNio2Channel.this.unwrapBeforeRead = true;
/*      */             } else {
/*  910 */               SecureNio2Channel.this.unwrapBeforeRead = false;
/*      */             }
/*      */             
/*  913 */             this.val$handler.completed(Integer.valueOf(read), attach);
/*      */           } catch (Exception e) {
/*  915 */             failed(e, attach);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, A attach) {
/*  921 */         this.val$handler.failed(exc, attach);
/*      */       }
/*      */     };
/*  924 */     if ((this.unwrapBeforeRead) || (this.netInBuffer.position() > 0)) {
/*  925 */       readCompletionHandler.completed(Integer.valueOf(this.netInBuffer.position()), attachment);
/*      */     } else {
/*  927 */       this.sc.read(this.netInBuffer, timeout, unit, attachment, readCompletionHandler);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <A> void read(final ByteBuffer[] dsts, final int offset, final int length, final long timeout, TimeUnit unit, final A attachment, final CompletionHandler<Long, ? super A> handler)
/*      */   {
/*  935 */     if ((offset < 0) || (dsts == null) || (offset + length > dsts.length)) {
/*  936 */       throw new IllegalArgumentException();
/*      */     }
/*  938 */     if ((this.closing) || (this.closed)) {
/*  939 */       handler.completed(Long.valueOf(-1L), attachment);
/*  940 */       return;
/*      */     }
/*  942 */     if (!this.handshakeComplete) {
/*  943 */       throw new IllegalStateException(sm.getString("channel.nio.ssl.incompleteHandshake"));
/*      */     }
/*  945 */     CompletionHandler<Integer, A> readCompletionHandler = new CompletionHandler()
/*      */     {
/*      */       public void completed(Integer nBytes, A attach) {
/*  948 */         if (nBytes.intValue() < 0) {
/*  949 */           failed(new EOFException(), attach);
/*      */         } else {
/*      */           try
/*      */           {
/*  953 */             long read = 0L;
/*      */             
/*      */ 
/*      */             do
/*      */             {
/*  958 */               SecureNio2Channel.this.netInBuffer.flip();
/*      */               
/*  960 */               SSLEngineResult unwrap = SecureNio2Channel.this.sslEngine.unwrap(SecureNio2Channel.this.netInBuffer, dsts, offset, length);
/*      */               
/*  962 */               SecureNio2Channel.this.netInBuffer.compact();
/*  963 */               if ((unwrap.getStatus() == SSLEngineResult.Status.OK) || (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW))
/*      */               {
/*  965 */                 read += unwrap.bytesProduced();
/*      */                 
/*  967 */                 if (unwrap.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/*  968 */                   SecureNio2Channel.this.tasks();
/*      */                 }
/*  970 */                 if (unwrap.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
/*  971 */                   if (read != 0L) break;
/*  972 */                   SecureNio2Channel.this.sc.read(SecureNio2Channel.this.netInBuffer, timeout, attachment, handler, this);
/*      */                 }
/*      */                 
/*      */               }
/*      */               else
/*      */               {
/*  978 */                 if ((unwrap.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) && (read > 0L)) {
/*      */                   break;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  986 */                 throw new IOException(SecureNio2Channel.sm.getString("channel.nio.ssl.unwrapFail", new Object[] { unwrap.getStatus() }));
/*      */               }
/*  988 */             } while (SecureNio2Channel.this.netInBuffer.position() != 0);
/*  989 */             int capacity = 0;
/*  990 */             int endOffset = offset + length;
/*  991 */             for (int i = offset; i < endOffset; i++) {
/*  992 */               capacity += dsts[i].remaining();
/*      */             }
/*  994 */             if (capacity == 0) {
/*  995 */               SecureNio2Channel.this.unwrapBeforeRead = true;
/*      */             } else {
/*  997 */               SecureNio2Channel.this.unwrapBeforeRead = false;
/*      */             }
/*      */             
/* 1000 */             this.val$handler.completed(Long.valueOf(read), attach);
/*      */           } catch (Exception e) {
/* 1002 */             failed(e, attach);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, A attach) {
/* 1008 */         this.val$handler.failed(exc, attach);
/*      */       }
/*      */     };
/* 1011 */     if ((this.unwrapBeforeRead) || (this.netInBuffer.position() > 0)) {
/* 1012 */       readCompletionHandler.completed(Integer.valueOf(this.netInBuffer.position()), attachment);
/*      */     } else {
/* 1014 */       this.sc.read(this.netInBuffer, timeout, unit, attachment, readCompletionHandler);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <A> void write(final ByteBuffer src, final long timeout, TimeUnit unit, final A attachment, final CompletionHandler<Integer, ? super A> handler)
/*      */   {
/* 1022 */     if ((this.closing) || (this.closed)) {
/* 1023 */       handler.failed(new IOException(sm.getString("channel.nio.ssl.closing")), attachment);
/* 1024 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1028 */       this.netOutBuffer.clear();
/*      */       
/* 1030 */       SSLEngineResult result = this.sslEngine.wrap(src, this.netOutBuffer);
/* 1031 */       final int written = result.bytesConsumed();
/* 1032 */       this.netOutBuffer.flip();
/* 1033 */       if (result.getStatus() == SSLEngineResult.Status.OK) {
/* 1034 */         if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 1035 */           tasks();
/*      */         }
/*      */         
/* 1038 */         this.sc.write(this.netOutBuffer, timeout, unit, attachment, new CompletionHandler()
/*      */         {
/*      */           public void completed(Integer nBytes, A attach)
/*      */           {
/* 1042 */             if (nBytes.intValue() < 0) {
/* 1043 */               failed(new EOFException(), attach);
/* 1044 */             } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
/* 1045 */               SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer, timeout, attachment, written, this);
/* 1046 */             } else if (src == 0)
/*      */             {
/* 1048 */               SecureNio2Channel.this.write(handler, timeout, attachment, written, this.val$handler);
/*      */             }
/*      */             else
/*      */             {
/* 1052 */               this.val$handler.completed(Integer.valueOf(src), attach);
/*      */             }
/*      */           }
/*      */           
/*      */           public void failed(Throwable exc, A attach) {
/* 1057 */             this.val$handler.failed(exc, attach);
/*      */           }
/*      */         });
/*      */       } else {
/* 1061 */         throw new IOException(sm.getString("channel.nio.ssl.wrapFail", new Object[] { result.getStatus() }));
/*      */       }
/*      */     } catch (Exception e) {
/* 1064 */       handler.failed(e, attachment);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <A> void write(final ByteBuffer[] srcs, final int offset, final int length, final long timeout, TimeUnit unit, final A attachment, final CompletionHandler<Long, ? super A> handler)
/*      */   {
/* 1072 */     if ((offset < 0) || (length < 0) || (offset > srcs.length - length)) {
/* 1073 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/* 1076 */     if ((this.closing) || (this.closed)) {
/* 1077 */       handler.failed(new IOException(sm.getString("channel.nio.ssl.closing")), attachment);
/* 1078 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1082 */       this.netOutBuffer.clear();
/*      */       
/* 1084 */       SSLEngineResult result = this.sslEngine.wrap(srcs, offset, length, this.netOutBuffer);
/* 1085 */       final int written = result.bytesConsumed();
/* 1086 */       this.netOutBuffer.flip();
/* 1087 */       if (result.getStatus() == SSLEngineResult.Status.OK) {
/* 1088 */         if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {
/* 1089 */           tasks();
/*      */         }
/*      */         
/* 1092 */         this.sc.write(this.netOutBuffer, timeout, unit, attachment, new CompletionHandler()
/*      */         {
/*      */           public void completed(Integer nBytes, A attach) {
/* 1095 */             if (nBytes.intValue() < 0) {
/* 1096 */               failed(new EOFException(), attach);
/* 1097 */             } else if (SecureNio2Channel.this.netOutBuffer.hasRemaining()) {
/* 1098 */               SecureNio2Channel.this.sc.write(SecureNio2Channel.this.netOutBuffer, timeout, attachment, written, this);
/* 1099 */             } else if (srcs == 0)
/*      */             {
/* 1101 */               SecureNio2Channel.this.write(offset, length, handler, timeout, attachment, written, this.val$handler);
/*      */             }
/*      */             else
/*      */             {
/* 1105 */               this.val$handler.completed(Long.valueOf(srcs), attach);
/*      */             }
/*      */           }
/*      */           
/*      */           public void failed(Throwable exc, A attach) {
/* 1110 */             this.val$handler.failed(exc, attach);
/*      */           }
/*      */         });
/*      */       } else {
/* 1114 */         throw new IOException(sm.getString("channel.nio.ssl.wrapFail", new Object[] { result.getStatus() }));
/*      */       }
/*      */     } catch (Exception e) {
/* 1117 */       handler.failed(e, attachment);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isHandshakeComplete()
/*      */   {
/* 1123 */     return this.handshakeComplete;
/*      */   }
/*      */   
/*      */   public boolean isClosing()
/*      */   {
/* 1128 */     return this.closing;
/*      */   }
/*      */   
/*      */   public SSLEngine getSslEngine() {
/* 1132 */     return this.sslEngine;
/*      */   }
/*      */   
/*      */   public ByteBuffer getEmptyBuf() {
/* 1136 */     return emptyBuf;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SecureNio2Channel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */