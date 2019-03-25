/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.SocketAddress;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AsynchronousChannelGroup;
/*      */ import java.nio.channels.AsynchronousCloseException;
/*      */ import java.nio.channels.AsynchronousServerSocketChannel;
/*      */ import java.nio.channels.AsynchronousSocketChannel;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.CompletionHandler;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.NetworkChannel;
/*      */ import java.nio.channels.ReadPendingException;
/*      */ import java.nio.channels.WritePendingException;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.LinkedBlockingDeque;
/*      */ import java.util.concurrent.Semaphore;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.ByteBufferHolder;
/*      */ import org.apache.tomcat.util.collections.SynchronizedStack;
/*      */ import org.apache.tomcat.util.net.jsse.JSSESupport;
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
/*      */ public class Nio2Endpoint
/*      */   extends AbstractJsseEndpoint<Nio2Channel>
/*      */ {
/*   66 */   private static final Log log = LogFactory.getLog(Nio2Endpoint.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   74 */   private AsynchronousServerSocketChannel serverSock = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   79 */   private static ThreadLocal<Boolean> inlineCompletion = new ThreadLocal();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   84 */   private AsynchronousChannelGroup threadGroup = null;
/*      */   
/*      */ 
/*      */ 
/*      */   private volatile boolean allClosed;
/*      */   
/*      */ 
/*      */   private SynchronizedStack<Nio2Channel> nioChannels;
/*      */   
/*      */ 
/*      */ 
/*      */   public Nio2Endpoint()
/*      */   {
/*   97 */     setMaxConnections(-1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSocketProperties(SocketProperties socketProperties)
/*      */   {
/*  104 */     this.socketProperties = socketProperties;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDeferAccept()
/*      */   {
/*  113 */     return false;
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
/*      */   public int getKeepAliveCount()
/*      */   {
/*  126 */     return -1;
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
/*      */   public void bind()
/*      */     throws Exception
/*      */   {
/*  140 */     if (getExecutor() == null) {
/*  141 */       createExecutor();
/*      */     }
/*  143 */     if ((getExecutor() instanceof ExecutorService)) {
/*  144 */       this.threadGroup = AsynchronousChannelGroup.withThreadPool((ExecutorService)getExecutor());
/*      */     }
/*      */     
/*  147 */     if (!this.internalExecutor) {
/*  148 */       log.warn(sm.getString("endpoint.nio2.exclusiveExecutor"));
/*      */     }
/*      */     
/*  151 */     this.serverSock = AsynchronousServerSocketChannel.open(this.threadGroup);
/*  152 */     this.socketProperties.setProperties(this.serverSock);
/*  153 */     InetSocketAddress addr = getAddress() != null ? new InetSocketAddress(getAddress(), getPort()) : new InetSocketAddress(getPort());
/*  154 */     this.serverSock.bind(addr, getAcceptCount());
/*      */     
/*      */ 
/*  157 */     if (this.acceptorThreadCount != 1)
/*      */     {
/*  159 */       this.acceptorThreadCount = 1;
/*      */     }
/*      */     
/*      */ 
/*  163 */     initialiseSsl();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startInternal()
/*      */     throws Exception
/*      */   {
/*  173 */     if (!this.running) {
/*  174 */       this.allClosed = false;
/*  175 */       this.running = true;
/*  176 */       this.paused = false;
/*      */       
/*      */ 
/*  179 */       this.processorCache = new SynchronizedStack(128, this.socketProperties.getProcessorCache());
/*      */       
/*  181 */       this.nioChannels = new SynchronizedStack(128, this.socketProperties.getBufferPool());
/*      */       
/*      */ 
/*  184 */       if (getExecutor() == null) {
/*  185 */         createExecutor();
/*      */       }
/*      */       
/*  188 */       initializeConnectionLatch();
/*  189 */       startAcceptorThreads();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stopInternal()
/*      */   {
/*  199 */     releaseConnectionLatch();
/*  200 */     if (!this.paused) {
/*  201 */       pause();
/*      */     }
/*  203 */     if (this.running) {
/*  204 */       this.running = false;
/*  205 */       unlockAccept();
/*      */       
/*      */ 
/*  208 */       getExecutor().execute(new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*      */           try {
/*  213 */             for (Nio2Channel channel : Nio2Endpoint.this.getHandler().getOpenSockets()) {
/*  214 */               Nio2Endpoint.this.closeSocket(channel.getSocket());
/*      */             }
/*      */           } catch (Throwable t) {
/*  217 */             ExceptionUtils.handleThrowable(t);
/*      */           } finally {
/*  219 */             Nio2Endpoint.this.allClosed = true;
/*      */           }
/*      */         }
/*  222 */       });
/*  223 */       this.nioChannels.clear();
/*  224 */       this.processorCache.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unbind()
/*      */     throws Exception
/*      */   {
/*  234 */     if (this.running) {
/*  235 */       stop();
/*      */     }
/*      */     
/*  238 */     this.serverSock.close();
/*  239 */     this.serverSock = null;
/*  240 */     destroySsl();
/*  241 */     super.unbind();
/*      */     
/*  243 */     shutdownExecutor();
/*  244 */     if (getHandler() != null) {
/*  245 */       getHandler().recycle();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void shutdownExecutor()
/*      */   {
/*  252 */     if ((this.threadGroup != null) && (this.internalExecutor)) {
/*      */       try {
/*  254 */         long timeout = getExecutorTerminationTimeoutMillis();
/*  255 */         while ((timeout > 0L) && (!this.allClosed)) {
/*  256 */           timeout -= 100L;
/*  257 */           Thread.sleep(100L);
/*      */         }
/*  259 */         this.threadGroup.shutdownNow();
/*  260 */         if (timeout > 0L) {
/*  261 */           this.threadGroup.awaitTermination(timeout, TimeUnit.MILLISECONDS);
/*      */         }
/*      */       } catch (IOException e) {
/*  264 */         getLog().warn(sm.getString("endpoint.warn.executorShutdown", new Object[] { getName() }), e);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */       
/*  268 */       if (!this.threadGroup.isTerminated()) {
/*  269 */         getLog().warn(sm.getString("endpoint.warn.executorShutdown", new Object[] { getName() }));
/*      */       }
/*  271 */       this.threadGroup = null;
/*      */     }
/*      */     
/*  274 */     super.shutdownExecutor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getWriteBufSize()
/*      */   {
/*  282 */     return this.socketProperties.getTxBufSize();
/*      */   }
/*      */   
/*      */   public int getReadBufSize() {
/*  286 */     return this.socketProperties.getRxBufSize();
/*      */   }
/*      */   
/*      */   protected AbstractEndpoint.Acceptor createAcceptor()
/*      */   {
/*  291 */     return new Acceptor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean setSocketOptions(AsynchronousSocketChannel socket)
/*      */   {
/*      */     try
/*      */     {
/*  303 */       this.socketProperties.setProperties(socket);
/*  304 */       Nio2Channel channel = (Nio2Channel)this.nioChannels.pop();
/*  305 */       if (channel == null)
/*      */       {
/*      */ 
/*      */ 
/*  309 */         SocketBufferHandler bufhandler = new SocketBufferHandler(this.socketProperties.getAppReadBufSize(), this.socketProperties.getAppWriteBufSize(), this.socketProperties.getDirectBuffer());
/*  310 */         if (isSSLEnabled()) {
/*  311 */           channel = new SecureNio2Channel(bufhandler, this);
/*      */         } else {
/*  313 */           channel = new Nio2Channel(bufhandler);
/*      */         }
/*      */       }
/*  316 */       Nio2SocketWrapper socketWrapper = new Nio2SocketWrapper(channel, this);
/*  317 */       channel.reset(socket, socketWrapper);
/*  318 */       socketWrapper.setReadTimeout(getSocketProperties().getSoTimeout());
/*  319 */       socketWrapper.setWriteTimeout(getSocketProperties().getSoTimeout());
/*  320 */       socketWrapper.setKeepAliveLeft(getMaxKeepAliveRequests());
/*  321 */       socketWrapper.setSecure(isSSLEnabled());
/*  322 */       socketWrapper.setReadTimeout(getConnectionTimeout());
/*  323 */       socketWrapper.setWriteTimeout(getConnectionTimeout());
/*      */       
/*  325 */       return processSocket(socketWrapper, SocketEvent.OPEN_READ, true);
/*      */     } catch (Throwable t) {
/*  327 */       ExceptionUtils.handleThrowable(t);
/*  328 */       log.error("", t);
/*      */     }
/*      */     
/*  331 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SocketProcessorBase<Nio2Channel> createSocketProcessor(SocketWrapperBase<Nio2Channel> socketWrapper, SocketEvent event)
/*      */   {
/*  338 */     return new SocketProcessor(socketWrapper, event);
/*      */   }
/*      */   
/*      */   public void closeSocket(SocketWrapperBase<Nio2Channel> socket)
/*      */   {
/*  343 */     if (log.isDebugEnabled()) {
/*  344 */       log.debug("Calling [" + this + "].closeSocket([" + socket + "],[" + socket.getSocket() + "])", new Exception());
/*      */     }
/*      */     
/*  347 */     if (socket == null) {
/*  348 */       return;
/*      */     }
/*      */     try {
/*  351 */       getHandler().release(socket);
/*      */     } catch (Throwable e) {
/*  353 */       ExceptionUtils.handleThrowable(e);
/*  354 */       if (log.isDebugEnabled()) log.error("", e);
/*      */     }
/*  356 */     Nio2SocketWrapper nio2Socket = (Nio2SocketWrapper)socket;
/*      */     try {
/*  358 */       synchronized ((Nio2Channel)socket.getSocket()) {
/*  359 */         if (!nio2Socket.closed) {
/*  360 */           nio2Socket.closed = true;
/*  361 */           countDownConnection();
/*      */         }
/*  363 */         if (((Nio2Channel)socket.getSocket()).isOpen()) {
/*  364 */           ((Nio2Channel)socket.getSocket()).close(true);
/*      */         }
/*      */       }
/*      */     } catch (Throwable e) {
/*  368 */       ExceptionUtils.handleThrowable(e);
/*  369 */       if (log.isDebugEnabled()) log.error("", e);
/*      */     }
/*      */     try {
/*  372 */       if ((nio2Socket.getSendfileData() != null) && 
/*  373 */         (nio2Socket.getSendfileData().fchannel != null) && 
/*  374 */         (nio2Socket.getSendfileData().fchannel.isOpen())) {
/*  375 */         nio2Socket.getSendfileData().fchannel.close();
/*      */       }
/*      */     } catch (Throwable e) {
/*  378 */       ExceptionUtils.handleThrowable(e);
/*  379 */       if (log.isDebugEnabled()) log.error("", e);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Log getLog()
/*      */   {
/*  385 */     return log;
/*      */   }
/*      */   
/*      */ 
/*      */   protected NetworkChannel getServerSocket()
/*      */   {
/*  391 */     return this.serverSock;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class Acceptor
/*      */     extends AbstractEndpoint.Acceptor
/*      */   {
/*      */     protected Acceptor() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void run()
/*      */     {
/*  406 */       int errorDelay = 0;
/*      */       
/*      */ 
/*  409 */       while (Nio2Endpoint.this.running)
/*      */       {
/*      */ 
/*  412 */         while ((Nio2Endpoint.this.paused) && (Nio2Endpoint.this.running)) {
/*  413 */           this.state = AbstractEndpoint.Acceptor.AcceptorState.PAUSED;
/*      */           try {
/*  415 */             Thread.sleep(50L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */         
/*      */ 
/*  421 */         if (!Nio2Endpoint.this.running) {
/*      */           break;
/*      */         }
/*  424 */         this.state = AbstractEndpoint.Acceptor.AcceptorState.RUNNING;
/*      */         
/*      */         try
/*      */         {
/*  428 */           Nio2Endpoint.this.countUpOrAwaitConnection();
/*      */           
/*  430 */           AsynchronousSocketChannel socket = null;
/*      */           
/*      */           try
/*      */           {
/*  434 */             socket = (AsynchronousSocketChannel)Nio2Endpoint.this.serverSock.accept().get();
/*      */           }
/*      */           catch (Exception e) {
/*  437 */             Nio2Endpoint.this.countDownConnection();
/*  438 */             if (Nio2Endpoint.this.running)
/*      */             {
/*  440 */               errorDelay = Nio2Endpoint.this.handleExceptionWithDelay(errorDelay);
/*      */               
/*  442 */               throw e;
/*      */             }
/*  444 */             break;
/*      */           }
/*      */           
/*      */ 
/*  448 */           errorDelay = 0;
/*      */           
/*      */ 
/*  451 */           if ((Nio2Endpoint.this.running) && (!Nio2Endpoint.this.paused))
/*      */           {
/*      */ 
/*  454 */             if (!Nio2Endpoint.this.setSocketOptions(socket)) {
/*  455 */               closeSocket(socket);
/*      */             }
/*      */           } else {
/*  458 */             closeSocket(socket);
/*      */           }
/*      */         } catch (Throwable t) {
/*  461 */           ExceptionUtils.handleThrowable(t);
/*  462 */           Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.accept.fail"), t);
/*      */         }
/*      */       }
/*  465 */       this.state = AbstractEndpoint.Acceptor.AcceptorState.ENDED;
/*      */     }
/*      */     
/*      */     private void closeSocket(AsynchronousSocketChannel socket)
/*      */     {
/*  470 */       Nio2Endpoint.this.countDownConnection();
/*      */       try {
/*  472 */         socket.close();
/*      */       } catch (IOException ioe) {
/*  474 */         if (Nio2Endpoint.log.isDebugEnabled()) {
/*  475 */           Nio2Endpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.close"), ioe);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Nio2SocketWrapper
/*      */     extends SocketWrapperBase<Nio2Channel>
/*      */   {
/*  484 */     private static final ThreadLocal<AtomicInteger> nestedWriteCompletionCount = new ThreadLocal()
/*      */     {
/*      */       protected AtomicInteger initialValue()
/*      */       {
/*  488 */         return new AtomicInteger(0);
/*      */       }
/*      */     };
/*      */     
/*  492 */     private Nio2Endpoint.SendfileData sendfileData = null;
/*      */     
/*      */     private final CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> readCompletionHandler;
/*  495 */     private final Semaphore readPending = new Semaphore(1);
/*  496 */     private boolean readInterest = false;
/*      */     
/*      */     private final CompletionHandler<Integer, ByteBuffer> writeCompletionHandler;
/*      */     private final CompletionHandler<Long, ByteBuffer[]> gatheringWriteCompletionHandler;
/*  500 */     private final Semaphore writePending = new Semaphore(1);
/*  501 */     private boolean writeInterest = false;
/*  502 */     private boolean writeNotify = false;
/*  503 */     private boolean closed = false;
/*      */     
/*  505 */     private CompletionHandler<Integer, SocketWrapperBase<Nio2Channel>> awaitBytesHandler = new CompletionHandler()
/*      */     {
/*      */ 
/*      */       public void completed(Integer nBytes, SocketWrapperBase<Nio2Channel> attachment)
/*      */       {
/*  510 */         if (nBytes.intValue() < 0) {
/*  511 */           failed(new ClosedChannelException(), attachment);
/*  512 */           return;
/*      */         }
/*  514 */         Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(attachment, SocketEvent.OPEN_READ, Nio2Endpoint.isInline());
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment)
/*      */       {
/*  519 */         Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(attachment, SocketEvent.DISCONNECT, true);
/*      */       }
/*      */     };
/*      */     
/*  523 */     private CompletionHandler<Integer, Nio2Endpoint.SendfileData> sendfileHandler = new CompletionHandler()
/*      */     {
/*      */ 
/*      */       public void completed(Integer nWrite, Nio2Endpoint.SendfileData attachment)
/*      */       {
/*  528 */         if (nWrite.intValue() < 0) {
/*  529 */           failed(new EOFException(), attachment);
/*  530 */           return;
/*      */         }
/*  532 */         attachment.pos += nWrite.intValue();
/*  533 */         ByteBuffer buffer = ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).getBufHandler().getWriteBuffer();
/*  534 */         if (!buffer.hasRemaining()) {
/*  535 */           if (attachment.length <= 0L)
/*      */           {
/*  537 */             Nio2Endpoint.Nio2SocketWrapper.this.setSendfileData(null);
/*      */             try {
/*  539 */               attachment.fchannel.close();
/*      */             }
/*      */             catch (IOException localIOException1) {}
/*      */             
/*  543 */             if (Nio2Endpoint.isInline()) {
/*  544 */               attachment.doneInline = true;
/*      */             } else {
/*  546 */               switch (Nio2Endpoint.2.$SwitchMap$org$apache$tomcat$util$net$SendfileKeepAliveState[attachment.keepAliveState.ordinal()]) {
/*      */               case 1: 
/*  548 */                 Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.DISCONNECT, false);
/*      */                 
/*  550 */                 break;
/*      */               
/*      */               case 2: 
/*  553 */                 Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.OPEN_READ, true);
/*      */                 
/*  555 */                 break;
/*      */               
/*      */               case 3: 
/*  558 */                 Nio2Endpoint.Nio2SocketWrapper.this.awaitBytes();
/*      */               }
/*      */               
/*      */             }
/*      */             
/*  563 */             return;
/*      */           }
/*  565 */           ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).getBufHandler().configureWriteBufferForWrite();
/*  566 */           int nRead = -1;
/*      */           try {
/*  568 */             nRead = attachment.fchannel.read(buffer);
/*      */           } catch (IOException e) {
/*  570 */             failed(e, attachment);
/*  571 */             return;
/*      */           }
/*  573 */           if (nRead > 0) {
/*  574 */             ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).getBufHandler().configureWriteBufferForRead();
/*  575 */             if (attachment.length < buffer.remaining()) {
/*  576 */               buffer.limit(buffer.limit() - buffer.remaining() + (int)attachment.length);
/*      */             }
/*  578 */             attachment.length -= nRead;
/*      */           } else {
/*  580 */             failed(new EOFException(), attachment);
/*  581 */             return;
/*      */           }
/*      */         }
/*      */         
/*  585 */         ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).write(buffer, Nio2Endpoint.Nio2SocketWrapper.this.getNio2WriteTimeout(), TimeUnit.MILLISECONDS, attachment, this);
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, Nio2Endpoint.SendfileData attachment)
/*      */       {
/*      */         try {
/*  591 */           attachment.fchannel.close();
/*      */         }
/*      */         catch (IOException localIOException) {}
/*      */         
/*  595 */         if (!Nio2Endpoint.isInline()) {
/*  596 */           Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.ERROR, false);
/*      */         } else {
/*  598 */           attachment.doneInline = true;
/*  599 */           attachment.error = true;
/*      */         }
/*      */       }
/*      */     };
/*      */     
/*      */     public Nio2SocketWrapper(Nio2Channel channel, final Nio2Endpoint endpoint) {
/*  605 */       super(endpoint);
/*  606 */       this.socketBufferHandler = channel.getBufHandler();
/*      */       
/*  608 */       this.readCompletionHandler = new CompletionHandler()
/*      */       {
/*      */         public void completed(Integer nBytes, SocketWrapperBase<Nio2Channel> attachment) {
/*  611 */           boolean notify = false;
/*  612 */           if (Nio2Endpoint.log.isDebugEnabled()) {
/*  613 */             Nio2Endpoint.log.debug("Socket: [" + attachment + "], Interest: [" + Nio2Endpoint.Nio2SocketWrapper.this.readInterest + "]");
/*      */           }
/*  615 */           synchronized (Nio2Endpoint.Nio2SocketWrapper.this.readCompletionHandler) {
/*  616 */             if (nBytes.intValue() < 0) {
/*  617 */               failed(new EOFException(), attachment);
/*      */             }
/*  619 */             else if ((Nio2Endpoint.Nio2SocketWrapper.this.readInterest) && (!Nio2Endpoint.isInline())) {
/*  620 */               Nio2Endpoint.Nio2SocketWrapper.this.readInterest = false;
/*  621 */               notify = true;
/*      */             }
/*      */             else
/*      */             {
/*  625 */               Nio2Endpoint.Nio2SocketWrapper.this.readPending.release();
/*      */             }
/*      */           }
/*      */           
/*  629 */           if (notify)
/*  630 */             Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(attachment, SocketEvent.OPEN_READ, false);
/*      */         }
/*      */         
/*      */         public void failed(Throwable exc, SocketWrapperBase<Nio2Channel> attachment) {
/*      */           IOException ioe;
/*      */           IOException ioe;
/*  636 */           if ((exc instanceof IOException)) {
/*  637 */             ioe = (IOException)exc;
/*      */           } else {
/*  639 */             ioe = new IOException(exc);
/*      */           }
/*  641 */           Nio2Endpoint.Nio2SocketWrapper.this.setError(ioe);
/*  642 */           if ((exc instanceof AsynchronousCloseException))
/*      */           {
/*      */ 
/*  645 */             Nio2Endpoint.Nio2SocketWrapper.this.readPending.release();
/*      */             
/*  647 */             return;
/*      */           }
/*  649 */           Nio2Endpoint.Nio2SocketWrapper.this.getEndpoint().processSocket(attachment, SocketEvent.ERROR, true);
/*      */         }
/*      */         
/*  652 */       };
/*  653 */       this.writeCompletionHandler = new CompletionHandler()
/*      */       {
/*      */         public void completed(Integer nBytes, ByteBuffer attachment) {
/*  656 */           Nio2Endpoint.Nio2SocketWrapper.this.writeNotify = false;
/*  657 */           synchronized (Nio2Endpoint.Nio2SocketWrapper.this.writeCompletionHandler) {
/*  658 */             if (nBytes.intValue() < 0) {
/*  659 */               failed(new EOFException(SocketWrapperBase.sm.getString("iob.failedwrite")), attachment);
/*  660 */             } else if (Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites.size() > 0) {
/*  661 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).incrementAndGet();
/*      */               
/*  663 */               ArrayList<ByteBuffer> arrayList = new ArrayList();
/*  664 */               if (attachment.hasRemaining()) {
/*  665 */                 arrayList.add(attachment);
/*      */               }
/*  667 */               for (ByteBufferHolder buffer : Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites) {
/*  668 */                 buffer.flip();
/*  669 */                 arrayList.add(buffer.getBuf());
/*      */               }
/*  671 */               Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites.clear();
/*  672 */               ByteBuffer[] array = (ByteBuffer[])arrayList.toArray(new ByteBuffer[arrayList.size()]);
/*  673 */               ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).write(array, 0, array.length, Nio2Endpoint.Nio2SocketWrapper.this
/*  674 */                 .getNio2WriteTimeout(), TimeUnit.MILLISECONDS, array, 
/*  675 */                 Nio2Endpoint.Nio2SocketWrapper.this.gatheringWriteCompletionHandler);
/*  676 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).decrementAndGet();
/*  677 */             } else if (attachment.hasRemaining())
/*      */             {
/*  679 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).incrementAndGet();
/*  680 */               ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).write(attachment, Nio2Endpoint.Nio2SocketWrapper.this.getNio2WriteTimeout(), TimeUnit.MILLISECONDS, attachment, 
/*  681 */                 Nio2Endpoint.Nio2SocketWrapper.this.writeCompletionHandler);
/*  682 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).decrementAndGet();
/*      */             }
/*      */             else {
/*  685 */               if (Nio2Endpoint.Nio2SocketWrapper.this.writeInterest) {
/*  686 */                 Nio2Endpoint.Nio2SocketWrapper.this.writeInterest = false;
/*  687 */                 Nio2Endpoint.Nio2SocketWrapper.this.writeNotify = true;
/*      */               }
/*  689 */               Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/*      */             }
/*      */           }
/*  692 */           if ((Nio2Endpoint.Nio2SocketWrapper.this.writeNotify) && (((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).get() == 0)) {
/*  693 */             endpoint.processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.OPEN_WRITE, Nio2Endpoint.isInline());
/*      */           }
/*      */         }
/*      */         
/*      */         public void failed(Throwable exc, ByteBuffer attachment) {
/*      */           IOException ioe;
/*      */           IOException ioe;
/*  700 */           if ((exc instanceof IOException)) {
/*  701 */             ioe = (IOException)exc;
/*      */           } else {
/*  703 */             ioe = new IOException(exc);
/*      */           }
/*  705 */           Nio2Endpoint.Nio2SocketWrapper.this.setError(ioe);
/*  706 */           Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/*  707 */           endpoint.processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.ERROR, true);
/*      */         }
/*      */         
/*  710 */       };
/*  711 */       this.gatheringWriteCompletionHandler = new CompletionHandler()
/*      */       {
/*      */         public void completed(Long nBytes, ByteBuffer[] attachment) {
/*  714 */           Nio2Endpoint.Nio2SocketWrapper.this.writeNotify = false;
/*  715 */           synchronized (Nio2Endpoint.Nio2SocketWrapper.this.writeCompletionHandler) {
/*  716 */             if (nBytes.longValue() < 0L) {
/*  717 */               failed(new EOFException(SocketWrapperBase.sm.getString("iob.failedwrite")), attachment);
/*  718 */             } else if ((Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites.size() > 0) || (Nio2Endpoint.Nio2SocketWrapper.arrayHasData(attachment)))
/*      */             {
/*  720 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).incrementAndGet();
/*  721 */               ArrayList<ByteBuffer> arrayList = new ArrayList();
/*  722 */               for (ByteBuffer buffer : attachment) {
/*  723 */                 if (buffer.hasRemaining()) {
/*  724 */                   arrayList.add(buffer);
/*      */                 }
/*      */               }
/*  727 */               for (??? = Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites.iterator(); ((Iterator)???).hasNext();) { ByteBufferHolder buffer = (ByteBufferHolder)((Iterator)???).next();
/*  728 */                 buffer.flip();
/*  729 */                 arrayList.add(buffer.getBuf());
/*      */               }
/*  731 */               Nio2Endpoint.Nio2SocketWrapper.this.bufferedWrites.clear();
/*  732 */               ByteBuffer[] array = (ByteBuffer[])arrayList.toArray(new ByteBuffer[arrayList.size()]);
/*  733 */               ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).write(array, 0, array.length, Nio2Endpoint.Nio2SocketWrapper.this
/*  734 */                 .getNio2WriteTimeout(), TimeUnit.MILLISECONDS, array, 
/*  735 */                 Nio2Endpoint.Nio2SocketWrapper.this.gatheringWriteCompletionHandler);
/*  736 */               ((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).decrementAndGet();
/*      */             }
/*      */             else {
/*  739 */               if (Nio2Endpoint.Nio2SocketWrapper.this.writeInterest) {
/*  740 */                 Nio2Endpoint.Nio2SocketWrapper.this.writeInterest = false;
/*  741 */                 Nio2Endpoint.Nio2SocketWrapper.this.writeNotify = true;
/*      */               }
/*  743 */               Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/*      */             }
/*      */           }
/*  746 */           if ((Nio2Endpoint.Nio2SocketWrapper.this.writeNotify) && (((AtomicInteger)Nio2Endpoint.Nio2SocketWrapper.nestedWriteCompletionCount.get()).get() == 0)) {
/*  747 */             endpoint.processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.OPEN_WRITE, Nio2Endpoint.isInline());
/*      */           }
/*      */         }
/*      */         
/*      */         public void failed(Throwable exc, ByteBuffer[] attachment) {
/*      */           IOException ioe;
/*      */           IOException ioe;
/*  754 */           if ((exc instanceof IOException)) {
/*  755 */             ioe = (IOException)exc;
/*      */           } else {
/*  757 */             ioe = new IOException(exc);
/*      */           }
/*  759 */           Nio2Endpoint.Nio2SocketWrapper.this.setError(ioe);
/*  760 */           Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/*  761 */           endpoint.processSocket(Nio2Endpoint.Nio2SocketWrapper.this, SocketEvent.ERROR, true);
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */     private static boolean arrayHasData(ByteBuffer[] byteBuffers)
/*      */     {
/*  768 */       for (ByteBuffer byteBuffer : byteBuffers) {
/*  769 */         if (byteBuffer.hasRemaining()) {
/*  770 */           return true;
/*      */         }
/*      */       }
/*  773 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  777 */     public void setSendfileData(Nio2Endpoint.SendfileData sf) { this.sendfileData = sf; }
/*  778 */     public Nio2Endpoint.SendfileData getSendfileData() { return this.sendfileData; }
/*      */     
/*      */     public boolean isReadyForRead() throws IOException
/*      */     {
/*  782 */       synchronized (this.readCompletionHandler) {
/*  783 */         if (!this.readPending.tryAcquire()) {
/*  784 */           this.readInterest = true;
/*  785 */           return false;
/*      */         }
/*      */         
/*  788 */         if (!this.socketBufferHandler.isReadBufferEmpty()) {
/*  789 */           this.readPending.release();
/*  790 */           return true;
/*      */         }
/*      */         
/*  793 */         int nRead = fillReadBuffer(false);
/*      */         
/*  795 */         boolean isReady = nRead > 0;
/*      */         
/*  797 */         if (!isReady) {
/*  798 */           this.readInterest = true;
/*      */         }
/*  800 */         return isReady;
/*      */       }
/*      */     }
/*      */     
/*      */     public int read(boolean block, byte[] b, int off, int len)
/*      */       throws IOException
/*      */     {
/*  807 */       checkError();
/*      */       
/*  809 */       if (Nio2Endpoint.log.isDebugEnabled()) {
/*  810 */         Nio2Endpoint.log.debug("Socket: [" + this + "], block: [" + block + "], length: [" + len + "]");
/*      */       }
/*      */       
/*  813 */       if (this.socketBufferHandler == null) {
/*  814 */         throw new IOException(sm.getString("socket.closed"));
/*      */       }
/*      */       
/*  817 */       if (block) {
/*      */         try {
/*  819 */           this.readPending.acquire();
/*      */         } catch (InterruptedException e) {
/*  821 */           throw new IOException(e);
/*      */         }
/*      */       }
/*  824 */       else if (!this.readPending.tryAcquire()) {
/*  825 */         if (Nio2Endpoint.log.isDebugEnabled()) {
/*  826 */           Nio2Endpoint.log.debug("Socket: [" + this + "], Read in progress. Returning [0]");
/*      */         }
/*  828 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  832 */       int nRead = populateReadBuffer(b, off, len);
/*  833 */       if (nRead > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  838 */         this.readPending.release();
/*  839 */         return nRead;
/*      */       }
/*      */       
/*  842 */       synchronized (this.readCompletionHandler)
/*      */       {
/*  844 */         nRead = fillReadBuffer(block);
/*      */         
/*      */ 
/*      */ 
/*  848 */         if (nRead > 0) {
/*  849 */           this.socketBufferHandler.configureReadBufferForRead();
/*  850 */           nRead = Math.min(nRead, len);
/*  851 */           this.socketBufferHandler.getReadBuffer().get(b, off, nRead);
/*  852 */         } else if ((nRead == 0) && (!block)) {
/*  853 */           this.readInterest = true;
/*      */         }
/*      */         
/*  856 */         if (Nio2Endpoint.log.isDebugEnabled()) {
/*  857 */           Nio2Endpoint.log.debug("Socket: [" + this + "], Read: [" + nRead + "]");
/*      */         }
/*  859 */         return nRead;
/*      */       }
/*      */     }
/*      */     
/*      */     public int read(boolean block, ByteBuffer to)
/*      */       throws IOException
/*      */     {
/*  866 */       checkError();
/*      */       
/*  868 */       if (this.socketBufferHandler == null) {
/*  869 */         throw new IOException(sm.getString("socket.closed"));
/*      */       }
/*      */       
/*  872 */       if (block) {
/*      */         try {
/*  874 */           this.readPending.acquire();
/*      */         } catch (InterruptedException e) {
/*  876 */           throw new IOException(e);
/*      */         }
/*      */       }
/*  879 */       else if (!this.readPending.tryAcquire()) {
/*  880 */         if (Nio2Endpoint.log.isDebugEnabled()) {
/*  881 */           Nio2Endpoint.log.debug("Socket: [" + this + "], Read in progress. Returning [0]");
/*      */         }
/*  883 */         return 0;
/*      */       }
/*      */       
/*      */ 
/*  887 */       int nRead = populateReadBuffer(to);
/*  888 */       if (nRead > 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  893 */         this.readPending.release();
/*  894 */         return nRead;
/*      */       }
/*      */       
/*  897 */       synchronized (this.readCompletionHandler)
/*      */       {
/*  899 */         int limit = this.socketBufferHandler.getReadBuffer().capacity();
/*  900 */         if ((block) && (to.remaining() >= limit)) {
/*  901 */           to.limit(to.position() + limit);
/*  902 */           nRead = fillReadBuffer(block, to);
/*      */         }
/*      */         else {
/*  905 */           nRead = fillReadBuffer(block);
/*      */           
/*      */ 
/*      */ 
/*  909 */           if (nRead > 0) {
/*  910 */             nRead = populateReadBuffer(to);
/*  911 */           } else if ((nRead == 0) && (!block)) {
/*  912 */             this.readInterest = true;
/*      */           }
/*      */         }
/*      */         
/*  916 */         return nRead;
/*      */       }
/*      */     }
/*      */     
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/*  923 */       ((Nio2Channel)getSocket()).close();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isClosed()
/*      */     {
/*  929 */       return !((Nio2Channel)getSocket()).isOpen();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean hasAsyncIO()
/*      */     {
/*  935 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     private static class OperationState<A>
/*      */     {
/*      */       private final ByteBuffer[] buffers;
/*      */       
/*      */       private final int offset;
/*      */       private final int length;
/*      */       private final A attachment;
/*      */       private final long timeout;
/*      */       private final TimeUnit unit;
/*      */       private final SocketWrapperBase.CompletionCheck check;
/*      */       private final CompletionHandler<Long, ? super A> handler;
/*      */       
/*      */       private OperationState(ByteBuffer[] buffers, int offset, int length, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler)
/*      */       {
/*  953 */         this.buffers = buffers;
/*  954 */         this.offset = offset;
/*  955 */         this.length = length;
/*  956 */         this.timeout = timeout;
/*  957 */         this.unit = unit;
/*  958 */         this.attachment = attachment;
/*  959 */         this.check = check;
/*  960 */         this.handler = handler; }
/*      */       
/*  962 */       private volatile long nBytes = 0L;
/*  963 */       private volatile SocketWrapperBase.CompletionState state = SocketWrapperBase.CompletionState.PENDING;
/*      */     }
/*      */     
/*      */     private class ScatterReadCompletionHandler<A> implements CompletionHandler<Long, Nio2Endpoint.Nio2SocketWrapper.OperationState<A>> {
/*      */       private ScatterReadCompletionHandler() {}
/*      */       
/*  969 */       public void completed(Long nBytes, Nio2Endpoint.Nio2SocketWrapper.OperationState<A> state) { if (nBytes.intValue() < 0) {
/*  970 */           failed(new EOFException(), state);
/*      */         } else {
/*  972 */           Nio2Endpoint.Nio2SocketWrapper.OperationState<A> localOperationState = state;Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1802(localOperationState, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1800(localOperationState) + nBytes.longValue());
/*  973 */           SocketWrapperBase.CompletionState currentState = Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.INLINE : SocketWrapperBase.CompletionState.DONE;
/*  974 */           boolean complete = true;
/*  975 */           boolean completion = true;
/*  976 */           if (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1900(state) != null) {
/*  977 */             switch (Nio2Endpoint.2.$SwitchMap$org$apache$tomcat$util$net$SocketWrapperBase$CompletionHandlerCall[Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1900(state).callHandler(currentState, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2000(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2100(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2200(state)).ordinal()]) {
/*      */             case 1: 
/*  979 */               complete = false;
/*  980 */               break;
/*      */             case 2: 
/*      */               break;
/*      */             case 3: 
/*  984 */               completion = false;
/*      */             }
/*      */             
/*      */           }
/*  988 */           if (complete) {
/*  989 */             Nio2Endpoint.Nio2SocketWrapper.this.readPending.release();
/*  990 */             Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2302(state, currentState);
/*  991 */             if ((completion) && (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state) != null)) {
/*  992 */               Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state).completed(Long.valueOf(Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1800(state)), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2500(state));
/*      */             }
/*      */           } else {
/*  995 */             ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).read(Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2000(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2100(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2200(state), 
/*  996 */               Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2600(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2700(state), state, this);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, Nio2Endpoint.Nio2SocketWrapper.OperationState<A> state) { IOException ioe;
/*      */         IOException ioe;
/* 1003 */         if ((exc instanceof IOException)) {
/* 1004 */           ioe = (IOException)exc;
/*      */         } else {
/* 1006 */           ioe = new IOException(exc);
/*      */         }
/* 1008 */         Nio2Endpoint.Nio2SocketWrapper.this.setError(ioe);
/* 1009 */         Nio2Endpoint.Nio2SocketWrapper.this.readPending.release();
/* 1010 */         if ((exc instanceof AsynchronousCloseException))
/*      */         {
/* 1012 */           return;
/*      */         }
/* 1014 */         Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2302(state, Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.ERROR : SocketWrapperBase.CompletionState.DONE);
/* 1015 */         if (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state) != null)
/* 1016 */           Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state).failed(ioe, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2500(state));
/*      */       }
/*      */     }
/*      */     
/*      */     private class GatherWriteCompletionHandler<A> implements CompletionHandler<Long, Nio2Endpoint.Nio2SocketWrapper.OperationState<A>> {
/*      */       private GatherWriteCompletionHandler() {}
/*      */       
/*      */       public void completed(Long nBytes, Nio2Endpoint.Nio2SocketWrapper.OperationState<A> state) {
/* 1024 */         if (nBytes.longValue() < 0L) {
/* 1025 */           failed(new EOFException(), state);
/*      */         } else {
/* 1027 */           Nio2Endpoint.Nio2SocketWrapper.OperationState<A> localOperationState = state;Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1802(localOperationState, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1800(localOperationState) + nBytes.longValue());
/* 1028 */           SocketWrapperBase.CompletionState currentState = Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.INLINE : SocketWrapperBase.CompletionState.DONE;
/* 1029 */           boolean complete = true;
/* 1030 */           boolean completion = true;
/* 1031 */           if (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1900(state) != null) {
/* 1032 */             switch (Nio2Endpoint.2.$SwitchMap$org$apache$tomcat$util$net$SocketWrapperBase$CompletionHandlerCall[Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1900(state).callHandler(currentState, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2000(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2100(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2200(state)).ordinal()]) {
/*      */             case 1: 
/* 1034 */               complete = false;
/* 1035 */               break;
/*      */             case 2: 
/*      */               break;
/*      */             case 3: 
/* 1039 */               completion = false;
/*      */             }
/*      */             
/*      */           }
/* 1043 */           if (complete) {
/* 1044 */             Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/* 1045 */             Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2302(state, currentState);
/* 1046 */             if ((completion) && (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state) != null)) {
/* 1047 */               Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state).completed(Long.valueOf(Nio2Endpoint.Nio2SocketWrapper.OperationState.access$1800(state)), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2500(state));
/*      */             }
/*      */           } else {
/* 1050 */             ((Nio2Channel)Nio2Endpoint.Nio2SocketWrapper.this.getSocket()).write(Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2000(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2100(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2200(state), 
/* 1051 */               Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2600(state), Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2700(state), state, this);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       public void failed(Throwable exc, Nio2Endpoint.Nio2SocketWrapper.OperationState<A> state) { IOException ioe;
/*      */         IOException ioe;
/* 1058 */         if ((exc instanceof IOException)) {
/* 1059 */           ioe = (IOException)exc;
/*      */         } else {
/* 1061 */           ioe = new IOException(exc);
/*      */         }
/* 1063 */         Nio2Endpoint.Nio2SocketWrapper.this.setError(ioe);
/* 1064 */         Nio2Endpoint.Nio2SocketWrapper.this.writePending.release();
/* 1065 */         Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2302(state, Nio2Endpoint.isInline() ? SocketWrapperBase.CompletionState.ERROR : SocketWrapperBase.CompletionState.DONE);
/* 1066 */         if (Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state) != null) {
/* 1067 */           Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2400(state).failed(ioe, Nio2Endpoint.Nio2SocketWrapper.OperationState.access$2500(state));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public <A> SocketWrapperBase.CompletionState read(ByteBuffer[] dsts, int offset, int length, boolean block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler)
/*      */     {
/* 1076 */       OperationState<A> state = new OperationState(dsts, offset, length, timeout, unit, attachment, check, handler, null);
/*      */       try {
/* 1078 */         if (((!block) && (this.readPending.tryAcquire())) || ((block) && (this.readPending.tryAcquire(timeout, unit)))) {
/* 1079 */           Nio2Endpoint.startInline();
/* 1080 */           ((Nio2Channel)getSocket()).read(dsts, offset, length, timeout, unit, state, new ScatterReadCompletionHandler(null));
/* 1081 */           Nio2Endpoint.endInline();
/*      */         } else {
/* 1083 */           throw new ReadPendingException();
/*      */         }
/* 1085 */         if ((block) && (state.state == SocketWrapperBase.CompletionState.PENDING) && (this.readPending.tryAcquire(timeout, unit))) {
/* 1086 */           this.readPending.release();
/*      */         }
/*      */       } catch (InterruptedException e) {
/* 1089 */         handler.failed(e, attachment);
/*      */       }
/* 1091 */       return state.state;
/*      */     }
/*      */     
/*      */     public boolean isWritePending()
/*      */     {
/* 1096 */       synchronized (this.writeCompletionHandler) {
/* 1097 */         return this.writePending.availablePermits() == 0;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public <A> SocketWrapperBase.CompletionState write(ByteBuffer[] srcs, int offset, int length, boolean block, long timeout, TimeUnit unit, A attachment, SocketWrapperBase.CompletionCheck check, CompletionHandler<Long, ? super A> handler)
/*      */     {
/* 1105 */       OperationState<A> state = new OperationState(srcs, offset, length, timeout, unit, attachment, check, handler, null);
/*      */       try {
/* 1107 */         if (((!block) && (this.writePending.tryAcquire())) || ((block) && (this.writePending.tryAcquire(timeout, unit)))) {
/* 1108 */           Nio2Endpoint.startInline();
/* 1109 */           ((Nio2Channel)getSocket()).write(srcs, offset, length, timeout, unit, state, new GatherWriteCompletionHandler(null));
/* 1110 */           Nio2Endpoint.endInline();
/*      */         } else {
/* 1112 */           throw new WritePendingException();
/*      */         }
/* 1114 */         if ((block) && (state.state == SocketWrapperBase.CompletionState.PENDING) && (this.writePending.tryAcquire(timeout, unit))) {
/* 1115 */           this.writePending.release();
/*      */         }
/*      */       } catch (InterruptedException e) {
/* 1118 */         handler.failed(e, attachment);
/*      */       }
/* 1120 */       return state.state;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int fillReadBuffer(boolean block)
/*      */       throws IOException
/*      */     {
/* 1131 */       this.socketBufferHandler.configureReadBufferForWrite();
/* 1132 */       return fillReadBuffer(block, this.socketBufferHandler.getReadBuffer());
/*      */     }
/*      */     
/*      */     private int fillReadBuffer(boolean block, ByteBuffer to) throws IOException {
/* 1136 */       int nRead = 0;
/* 1137 */       Future<Integer> integer = null;
/* 1138 */       if (block) {
/*      */         try {
/* 1140 */           integer = ((Nio2Channel)getSocket()).read(to);
/* 1141 */           nRead = ((Integer)integer.get(getNio2ReadTimeout(), TimeUnit.MILLISECONDS)).intValue();
/*      */         } catch (ExecutionException e) {
/* 1143 */           if ((e.getCause() instanceof IOException)) {
/* 1144 */             throw ((IOException)e.getCause());
/*      */           }
/* 1146 */           throw new IOException(e);
/*      */         }
/*      */         catch (InterruptedException e) {
/* 1149 */           throw new IOException(e);
/*      */         } catch (TimeoutException e) {
/* 1151 */           integer.cancel(true);
/* 1152 */           throw new SocketTimeoutException();
/*      */         }
/*      */         finally
/*      */         {
/* 1156 */           this.readPending.release();
/*      */         }
/*      */       } else {
/* 1159 */         Nio2Endpoint.startInline();
/* 1160 */         ((Nio2Channel)getSocket()).read(to, getNio2ReadTimeout(), TimeUnit.MILLISECONDS, this, this.readCompletionHandler);
/*      */         
/* 1162 */         Nio2Endpoint.endInline();
/* 1163 */         if (this.readPending.availablePermits() == 1) {
/* 1164 */           nRead = to.position();
/*      */         }
/*      */       }
/* 1167 */       return nRead;
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
/*      */ 
/*      */     protected void writeNonBlocking(byte[] buf, int off, int len)
/*      */       throws IOException
/*      */     {
/* 1188 */       synchronized (this.writeCompletionHandler) {
/* 1189 */         if (this.writePending.tryAcquire())
/*      */         {
/*      */ 
/* 1192 */           this.socketBufferHandler.configureWriteBufferForWrite();
/* 1193 */           int thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
/* 1194 */           len -= thisTime;
/* 1195 */           off += thisTime;
/* 1196 */           if (len > 0)
/*      */           {
/* 1198 */             addToBuffers(buf, off, len);
/*      */           }
/* 1200 */           flushNonBlocking(true);
/*      */         } else {
/* 1202 */           addToBuffers(buf, off, len);
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void writeNonBlocking(ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 1225 */       synchronized (this.writeCompletionHandler) {
/* 1226 */         if (this.writePending.tryAcquire())
/*      */         {
/*      */ 
/* 1229 */           this.socketBufferHandler.configureWriteBufferForWrite();
/* 1230 */           transfer(from, this.socketBufferHandler.getWriteBuffer());
/* 1231 */           if (from.remaining() > 0)
/*      */           {
/* 1233 */             addToBuffers(from);
/*      */           }
/* 1235 */           flushNonBlocking(true);
/*      */         } else {
/* 1237 */           addToBuffers(from);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void doWrite(boolean block, ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 1249 */       Future<Integer> integer = null;
/*      */       try {
/*      */         do {
/* 1252 */           integer = ((Nio2Channel)getSocket()).write(from);
/* 1253 */           if (((Integer)integer.get(getNio2WriteTimeout(), TimeUnit.MILLISECONDS)).intValue() < 0) {
/* 1254 */             throw new EOFException(sm.getString("iob.failedwrite"));
/*      */           }
/* 1256 */         } while (from.hasRemaining());
/*      */       } catch (ExecutionException e) {
/* 1258 */         if ((e.getCause() instanceof IOException)) {
/* 1259 */           throw ((IOException)e.getCause());
/*      */         }
/* 1261 */         throw new IOException(e);
/*      */       }
/*      */       catch (InterruptedException e) {
/* 1264 */         throw new IOException(e);
/*      */       } catch (TimeoutException e) {
/* 1266 */         integer.cancel(true);
/* 1267 */         throw new SocketTimeoutException();
/*      */       }
/*      */     }
/*      */     
/*      */     protected void flushBlocking()
/*      */       throws IOException
/*      */     {
/* 1274 */       checkError();
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 1279 */         if (this.writePending.tryAcquire(getNio2WriteTimeout(), TimeUnit.MILLISECONDS)) {
/* 1280 */           this.writePending.release();
/*      */         } else {
/* 1282 */           throw new SocketTimeoutException();
/*      */         }
/*      */       }
/*      */       catch (InterruptedException localInterruptedException) {}
/*      */       
/*      */ 
/* 1288 */       super.flushBlocking();
/*      */     }
/*      */     
/*      */     protected boolean flushNonBlocking() throws IOException
/*      */     {
/* 1293 */       return flushNonBlocking(false);
/*      */     }
/*      */     
/*      */     private boolean flushNonBlocking(boolean hasPermit) throws IOException {
/* 1297 */       checkError();
/* 1298 */       synchronized (this.writeCompletionHandler) {
/* 1299 */         if ((hasPermit) || (this.writePending.tryAcquire())) {
/* 1300 */           this.socketBufferHandler.configureWriteBufferForRead();
/* 1301 */           if (this.bufferedWrites.size() > 0)
/*      */           {
/* 1303 */             ArrayList<ByteBuffer> arrayList = new ArrayList();
/* 1304 */             if (this.socketBufferHandler.getWriteBuffer().hasRemaining()) {
/* 1305 */               arrayList.add(this.socketBufferHandler.getWriteBuffer());
/*      */             }
/* 1307 */             for (ByteBufferHolder buffer : this.bufferedWrites) {
/* 1308 */               buffer.flip();
/* 1309 */               arrayList.add(buffer.getBuf());
/*      */             }
/* 1311 */             this.bufferedWrites.clear();
/* 1312 */             ByteBuffer[] array = (ByteBuffer[])arrayList.toArray(new ByteBuffer[arrayList.size()]);
/* 1313 */             Nio2Endpoint.startInline();
/* 1314 */             ((Nio2Channel)getSocket()).write(array, 0, array.length, getNio2WriteTimeout(), TimeUnit.MILLISECONDS, array, this.gatheringWriteCompletionHandler);
/*      */             
/* 1316 */             Nio2Endpoint.endInline();
/* 1317 */           } else if (this.socketBufferHandler.getWriteBuffer().hasRemaining())
/*      */           {
/* 1319 */             Nio2Endpoint.startInline();
/* 1320 */             ((Nio2Channel)getSocket()).write(this.socketBufferHandler.getWriteBuffer(), getNio2WriteTimeout(), TimeUnit.MILLISECONDS, this.socketBufferHandler
/* 1321 */               .getWriteBuffer(), this.writeCompletionHandler);
/*      */             
/* 1323 */             Nio2Endpoint.endInline();
/*      */ 
/*      */           }
/* 1326 */           else if (!hasPermit) {
/* 1327 */             this.writePending.release();
/*      */           }
/*      */         }
/*      */         
/* 1331 */         return hasDataToWrite();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean hasDataToWrite()
/*      */     {
/* 1338 */       synchronized (this.writeCompletionHandler) {
/* 1339 */         return (!this.socketBufferHandler.isWriteBufferEmpty()) || 
/* 1340 */           (this.bufferedWrites.size() > 0) || (getError() != null);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isReadPending()
/*      */     {
/* 1347 */       synchronized (this.readCompletionHandler) {
/* 1348 */         return this.readPending.availablePermits() == 0;
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean awaitReadComplete(long timeout, TimeUnit unit)
/*      */     {
/*      */       try
/*      */       {
/* 1356 */         if (this.readPending.tryAcquire(timeout, unit)) {
/* 1357 */           this.readPending.release();
/*      */         }
/*      */       } catch (InterruptedException e) {
/* 1360 */         return false;
/*      */       }
/* 1362 */       return true;
/*      */     }
/*      */     
/*      */     public boolean awaitWriteComplete(long timeout, TimeUnit unit)
/*      */     {
/*      */       try
/*      */       {
/* 1369 */         if (this.writePending.tryAcquire(timeout, unit)) {
/* 1370 */           this.writePending.release();
/*      */         }
/*      */       } catch (InterruptedException e) {
/* 1373 */         return false;
/*      */       }
/* 1375 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     void releaseReadPending()
/*      */     {
/* 1384 */       synchronized (this.readCompletionHandler) {
/* 1385 */         if (this.readPending.availablePermits() == 0) {
/* 1386 */           this.readPending.release();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void registerReadInterest()
/*      */     {
/* 1394 */       synchronized (this.readCompletionHandler) {
/* 1395 */         if (this.readPending.availablePermits() == 0) {
/* 1396 */           this.readInterest = true;
/*      */         }
/*      */         else {
/* 1399 */           awaitBytes();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void registerWriteInterest()
/*      */     {
/* 1407 */       synchronized (this.writeCompletionHandler) {
/* 1408 */         if (this.writePending.availablePermits() == 0) {
/* 1409 */           this.writeInterest = true;
/*      */         }
/*      */         else {
/* 1412 */           getEndpoint().processSocket(this, SocketEvent.OPEN_WRITE, true);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void awaitBytes()
/*      */     {
/* 1420 */       if (this.readPending.tryAcquire()) {
/* 1421 */         ((Nio2Channel)getSocket()).getBufHandler().configureReadBufferForWrite();
/* 1422 */         Nio2Endpoint.startInline();
/* 1423 */         ((Nio2Channel)getSocket()).read(((Nio2Channel)getSocket()).getBufHandler().getReadBuffer(), 
/* 1424 */           getNio2ReadTimeout(), TimeUnit.MILLISECONDS, this, this.awaitBytesHandler);
/* 1425 */         Nio2Endpoint.endInline();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileDataBase createSendfileData(String filename, long pos, long length)
/*      */     {
/* 1432 */       return new Nio2Endpoint.SendfileData(filename, pos, length);
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileState processSendfile(SendfileDataBase sendfileData)
/*      */     {
/* 1438 */       Nio2Endpoint.SendfileData data = (Nio2Endpoint.SendfileData)sendfileData;
/* 1439 */       setSendfileData(data);
/*      */       
/* 1441 */       if ((data.fchannel == null) || (!data.fchannel.isOpen())) {
/* 1442 */         Path path = new File(sendfileData.fileName).toPath();
/*      */         try {
/* 1444 */           data.fchannel = 
/* 1445 */             FileChannel.open(path, new OpenOption[] { StandardOpenOption.READ }).position(sendfileData.pos);
/*      */         } catch (IOException e) {
/* 1447 */           return SendfileState.ERROR;
/*      */         }
/*      */       }
/* 1450 */       ((Nio2Channel)getSocket()).getBufHandler().configureWriteBufferForWrite();
/* 1451 */       ByteBuffer buffer = ((Nio2Channel)getSocket()).getBufHandler().getWriteBuffer();
/* 1452 */       int nRead = -1;
/*      */       try {
/* 1454 */         nRead = data.fchannel.read(buffer);
/*      */       } catch (IOException e1) {
/* 1456 */         return SendfileState.ERROR;
/*      */       }
/*      */       
/* 1459 */       if (nRead >= 0) {
/* 1460 */         data.length -= nRead;
/* 1461 */         ((Nio2Channel)getSocket()).getBufHandler().configureWriteBufferForRead();
/* 1462 */         Nio2Endpoint.startInline();
/* 1463 */         ((Nio2Channel)getSocket()).write(buffer, getNio2WriteTimeout(), TimeUnit.MILLISECONDS, data, this.sendfileHandler);
/*      */         
/* 1465 */         Nio2Endpoint.endInline();
/* 1466 */         if (data.doneInline) {
/* 1467 */           if (data.error) {
/* 1468 */             return SendfileState.ERROR;
/*      */           }
/* 1470 */           return SendfileState.DONE;
/*      */         }
/*      */         
/* 1473 */         return SendfileState.PENDING;
/*      */       }
/*      */       
/* 1476 */       return SendfileState.ERROR;
/*      */     }
/*      */     
/*      */ 
/*      */     private long getNio2ReadTimeout()
/*      */     {
/* 1482 */       long readTimeout = getReadTimeout();
/* 1483 */       if (readTimeout > 0L) {
/* 1484 */         return readTimeout;
/*      */       }
/*      */       
/* 1487 */       return Long.MAX_VALUE;
/*      */     }
/*      */     
/*      */     private long getNio2WriteTimeout()
/*      */     {
/* 1492 */       long writeTimeout = getWriteTimeout();
/* 1493 */       if (writeTimeout > 0L) {
/* 1494 */         return writeTimeout;
/*      */       }
/*      */       
/* 1497 */       return Long.MAX_VALUE;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteAddr()
/*      */     {
/* 1503 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1505 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getRemoteAddress();
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */       
/* 1509 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1510 */         this.remoteAddr = ((InetSocketAddress)socketAddress).getAddress().getHostAddress();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteHost()
/*      */     {
/* 1517 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1519 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getRemoteAddress();
/*      */       } catch (IOException e) {
/* 1521 */         Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noRemoteHost", new Object[] { getSocket() }), e);
/*      */       }
/* 1523 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1524 */         this.remoteHost = ((InetSocketAddress)socketAddress).getAddress().getHostName();
/* 1525 */         if (this.remoteAddr == null) {
/* 1526 */           this.remoteAddr = ((InetSocketAddress)socketAddress).getAddress().getHostAddress();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemotePort()
/*      */     {
/* 1534 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1536 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getRemoteAddress();
/*      */       } catch (IOException e) {
/* 1538 */         Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noRemotePort", new Object[] { getSocket() }), e);
/*      */       }
/* 1540 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1541 */         this.remotePort = ((InetSocketAddress)socketAddress).getPort();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalName()
/*      */     {
/* 1548 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1550 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getLocalAddress();
/*      */       } catch (IOException e) {
/* 1552 */         Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalName", new Object[] { getSocket() }), e);
/*      */       }
/* 1554 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1555 */         this.localName = ((InetSocketAddress)socketAddress).getHostName();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalAddr()
/*      */     {
/* 1562 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1564 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getLocalAddress();
/*      */       } catch (IOException e) {
/* 1566 */         Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalAddr", new Object[] { getSocket() }), e);
/*      */       }
/* 1568 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1569 */         this.localAddr = ((InetSocketAddress)socketAddress).getAddress().getHostAddress();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalPort()
/*      */     {
/* 1576 */       SocketAddress socketAddress = null;
/*      */       try {
/* 1578 */         socketAddress = ((Nio2Channel)getSocket()).getIOChannel().getLocalAddress();
/*      */       } catch (IOException e) {
/* 1580 */         Nio2Endpoint.log.warn(sm.getString("endpoint.warn.noLocalPort", new Object[] { getSocket() }), e);
/*      */       }
/* 1582 */       if ((socketAddress instanceof InetSocketAddress)) {
/* 1583 */         this.localPort = ((InetSocketAddress)socketAddress).getPort();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SSLSupport getSslSupport(String clientCertProvider)
/*      */     {
/* 1594 */       if ((getSocket() instanceof SecureNio2Channel)) {
/* 1595 */         SecureNio2Channel ch = (SecureNio2Channel)getSocket();
/* 1596 */         SSLSession session = ch.getSslEngine().getSession();
/* 1597 */         return ((Nio2Endpoint)getEndpoint()).getSslImplementation().getSSLSupport(session);
/*      */       }
/* 1599 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public void doClientAuth(SSLSupport sslSupport)
/*      */       throws IOException
/*      */     {
/* 1606 */       SecureNio2Channel sslChannel = (SecureNio2Channel)getSocket();
/* 1607 */       SSLEngine engine = sslChannel.getSslEngine();
/* 1608 */       if (!engine.getNeedClientAuth())
/*      */       {
/* 1610 */         engine.setNeedClientAuth(true);
/* 1611 */         sslChannel.rehandshake();
/* 1612 */         ((JSSESupport)sslSupport).setSession(engine.getSession());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void setAppReadBufHandler(ApplicationBufferHandler handler)
/*      */     {
/* 1619 */       ((Nio2Channel)getSocket()).setAppReadBufHandler(handler);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void startInline()
/*      */   {
/* 1625 */     inlineCompletion.set(Boolean.TRUE);
/*      */   }
/*      */   
/*      */   public static void endInline() {
/* 1629 */     inlineCompletion.set(Boolean.FALSE);
/*      */   }
/*      */   
/*      */   public static boolean isInline() {
/* 1633 */     Boolean flag = (Boolean)inlineCompletion.get();
/* 1634 */     if (flag == null) {
/* 1635 */       return false;
/*      */     }
/* 1637 */     return flag.booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class SocketProcessor
/*      */     extends SocketProcessorBase<Nio2Channel>
/*      */   {
/*      */     public SocketProcessor(SocketEvent socketWrapper)
/*      */     {
/* 1650 */       super(event);
/*      */     }
/*      */     
/*      */     protected void doRun()
/*      */     {
/* 1655 */       if (SocketEvent.OPEN_WRITE != this.event)
/*      */       {
/*      */ 
/* 1658 */         ((Nio2Endpoint.Nio2SocketWrapper)this.socketWrapper).releaseReadPending();
/*      */       }
/* 1660 */       boolean launch = false;
/*      */       try {
/* 1662 */         int handshake = -1;
/*      */         try
/*      */         {
/* 1665 */           if (((Nio2Channel)this.socketWrapper.getSocket()).isHandshakeComplete())
/*      */           {
/*      */ 
/* 1668 */             handshake = 0;
/* 1669 */           } else if ((this.event == SocketEvent.STOP) || (this.event == SocketEvent.DISCONNECT) || (this.event == SocketEvent.ERROR))
/*      */           {
/*      */ 
/*      */ 
/* 1673 */             handshake = -1;
/*      */           } else {
/* 1675 */             handshake = ((Nio2Channel)this.socketWrapper.getSocket()).handshake();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1683 */             this.event = SocketEvent.OPEN_READ;
/*      */           }
/*      */         } catch (IOException x) {
/* 1686 */           handshake = -1;
/* 1687 */           if (Nio2Endpoint.log.isDebugEnabled()) {
/* 1688 */             Nio2Endpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.handshake"), x);
/*      */           }
/*      */         }
/* 1691 */         if (handshake == 0) {
/* 1692 */           AbstractEndpoint.Handler.SocketState state = AbstractEndpoint.Handler.SocketState.OPEN;
/*      */           
/* 1694 */           if (this.event == null) {
/* 1695 */             state = Nio2Endpoint.this.getHandler().process(this.socketWrapper, SocketEvent.OPEN_READ);
/*      */           } else {
/* 1697 */             state = Nio2Endpoint.this.getHandler().process(this.socketWrapper, this.event);
/*      */           }
/* 1699 */           if (state == AbstractEndpoint.Handler.SocketState.CLOSED)
/*      */           {
/* 1701 */             Nio2Endpoint.this.closeSocket(this.socketWrapper);
/* 1702 */             if ((Nio2Endpoint.this.running) && (!Nio2Endpoint.this.paused) && 
/* 1703 */               (!Nio2Endpoint.this.nioChannels.push(this.socketWrapper.getSocket()))) {
/* 1704 */               ((Nio2Channel)this.socketWrapper.getSocket()).free();
/*      */             }
/*      */           }
/* 1707 */           else if (state == AbstractEndpoint.Handler.SocketState.UPGRADING) {
/* 1708 */             launch = true;
/*      */           }
/* 1710 */         } else if (handshake == -1) {
/* 1711 */           Nio2Endpoint.this.closeSocket(this.socketWrapper);
/* 1712 */           if ((Nio2Endpoint.this.running) && (!Nio2Endpoint.this.paused) && 
/* 1713 */             (!Nio2Endpoint.this.nioChannels.push(this.socketWrapper.getSocket()))) {
/* 1714 */             ((Nio2Channel)this.socketWrapper.getSocket()).free();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (VirtualMachineError vme) {
/* 1719 */         ExceptionUtils.handleThrowable(vme);
/*      */       } catch (Throwable t) {
/* 1721 */         Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.processing.fail"), t);
/* 1722 */         if (this.socketWrapper != null) {
/* 1723 */           Nio2Endpoint.this.closeSocket(this.socketWrapper);
/*      */         }
/*      */       } finally {
/* 1726 */         if (launch) {
/*      */           try {
/* 1728 */             Nio2Endpoint.this.getExecutor().execute(new SocketProcessor(Nio2Endpoint.this, this.socketWrapper, SocketEvent.OPEN_READ));
/*      */           } catch (NullPointerException npe) {
/* 1730 */             if (Nio2Endpoint.this.running) {
/* 1731 */               Nio2Endpoint.log.error(AbstractEndpoint.sm.getString("endpoint.launch.fail"), npe);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1736 */         this.socketWrapper = null;
/* 1737 */         this.event = null;
/*      */         
/* 1739 */         if ((Nio2Endpoint.this.running) && (!Nio2Endpoint.this.paused)) {
/* 1740 */           Nio2Endpoint.this.processorCache.push(this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class SendfileData
/*      */     extends SendfileDataBase
/*      */   {
/*      */     private FileChannel fchannel;
/*      */     
/* 1753 */     private boolean doneInline = false;
/* 1754 */     private boolean error = false;
/*      */     
/*      */     public SendfileData(String filename, long pos, long length) {
/* 1757 */       super(pos, length);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\Nio2Endpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */