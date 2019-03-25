/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.CancelledKeyException;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.NetworkChannel;
/*      */ import java.nio.channels.SelectableChannel;
/*      */ import java.nio.channels.SelectionKey;
/*      */ import java.nio.channels.Selector;
/*      */ import java.nio.channels.ServerSocketChannel;
/*      */ import java.nio.channels.SocketChannel;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.IntrospectionUtils;
/*      */ import org.apache.tomcat.util.collections.SynchronizedQueue;
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
/*      */ public class NioEndpoint
/*      */   extends AbstractJsseEndpoint<NioChannel>
/*      */ {
/*   75 */   private static final Log log = LogFactory.getLog(NioEndpoint.class);
/*      */   
/*      */ 
/*      */ 
/*      */   public static final int OP_REGISTER = 256;
/*      */   
/*      */ 
/*   82 */   private NioSelectorPool selectorPool = new NioSelectorPool();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   87 */   private ServerSocketChannel serverSock = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   92 */   private volatile CountDownLatch stopLatch = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private SynchronizedStack<PollerEvent> eventCache;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private SynchronizedStack<NioChannel> nioChannels;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean setProperty(String name, String value)
/*      */   {
/*  113 */     String selectorPoolName = "selectorPool.";
/*      */     try {
/*  115 */       if (name.startsWith("selectorPool.")) {
/*  116 */         return IntrospectionUtils.setProperty(this.selectorPool, name.substring("selectorPool.".length()), value);
/*      */       }
/*  118 */       return super.setProperty(name, value);
/*      */     }
/*      */     catch (Exception x) {
/*  121 */       log.error("Unable to set attribute \"" + name + "\" to \"" + value + "\"", x); }
/*  122 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  130 */   private int pollerThreadPriority = 5;
/*  131 */   public void setPollerThreadPriority(int pollerThreadPriority) { this.pollerThreadPriority = pollerThreadPriority; }
/*  132 */   public int getPollerThreadPriority() { return this.pollerThreadPriority; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  138 */   private int pollerThreadCount = Math.min(2, Runtime.getRuntime().availableProcessors());
/*  139 */   public void setPollerThreadCount(int pollerThreadCount) { this.pollerThreadCount = pollerThreadCount; }
/*  140 */   public int getPollerThreadCount() { return this.pollerThreadCount; }
/*      */   
/*  142 */   private long selectorTimeout = 1000L;
/*  143 */   public void setSelectorTimeout(long timeout) { this.selectorTimeout = timeout; }
/*  144 */   public long getSelectorTimeout() { return this.selectorTimeout; }
/*      */   
/*      */ 
/*      */ 
/*  148 */   private Poller[] pollers = null;
/*  149 */   private AtomicInteger pollerRotater = new AtomicInteger(0);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Poller getPoller0()
/*      */   {
/*  156 */     int idx = Math.abs(this.pollerRotater.incrementAndGet()) % this.pollers.length;
/*  157 */     return this.pollers[idx];
/*      */   }
/*      */   
/*      */   public void setSelectorPool(NioSelectorPool selectorPool)
/*      */   {
/*  162 */     this.selectorPool = selectorPool;
/*      */   }
/*      */   
/*      */   public void setSocketProperties(SocketProperties socketProperties) {
/*  166 */     this.socketProperties = socketProperties;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getDeferAccept()
/*      */   {
/*  175 */     return false;
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
/*  187 */     if (this.pollers == null) {
/*  188 */       return 0;
/*      */     }
/*  190 */     int sum = 0;
/*  191 */     for (int i = 0; i < this.pollers.length; i++) {
/*  192 */       sum += this.pollers[i].getKeyCount();
/*      */     }
/*  194 */     return sum;
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
/*  207 */     this.serverSock = ServerSocketChannel.open();
/*  208 */     this.socketProperties.setProperties(this.serverSock.socket());
/*  209 */     InetSocketAddress addr = getAddress() != null ? new InetSocketAddress(getAddress(), getPort()) : new InetSocketAddress(getPort());
/*  210 */     this.serverSock.socket().bind(addr, getAcceptCount());
/*  211 */     this.serverSock.configureBlocking(true);
/*      */     
/*      */ 
/*  214 */     if (this.acceptorThreadCount == 0)
/*      */     {
/*  216 */       this.acceptorThreadCount = 1;
/*      */     }
/*  218 */     if (this.pollerThreadCount <= 0)
/*      */     {
/*  220 */       this.pollerThreadCount = 1;
/*      */     }
/*  222 */     setStopLatch(new CountDownLatch(this.pollerThreadCount));
/*      */     
/*      */ 
/*  225 */     initialiseSsl();
/*      */     
/*  227 */     this.selectorPool.open();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void startInternal()
/*      */     throws Exception
/*      */   {
/*  236 */     if (!this.running) {
/*  237 */       this.running = true;
/*  238 */       this.paused = false;
/*      */       
/*      */ 
/*  241 */       this.processorCache = new SynchronizedStack(128, this.socketProperties.getProcessorCache());
/*      */       
/*  243 */       this.eventCache = new SynchronizedStack(128, this.socketProperties.getEventCache());
/*      */       
/*  245 */       this.nioChannels = new SynchronizedStack(128, this.socketProperties.getBufferPool());
/*      */       
/*      */ 
/*  248 */       if (getExecutor() == null) {
/*  249 */         createExecutor();
/*      */       }
/*      */       
/*  252 */       initializeConnectionLatch();
/*      */       
/*      */ 
/*  255 */       this.pollers = new Poller[getPollerThreadCount()];
/*  256 */       for (int i = 0; i < this.pollers.length; i++) {
/*  257 */         this.pollers[i] = new Poller();
/*  258 */         Thread pollerThread = new Thread(this.pollers[i], getName() + "-ClientPoller-" + i);
/*  259 */         pollerThread.setPriority(this.threadPriority);
/*  260 */         pollerThread.setDaemon(true);
/*  261 */         pollerThread.start();
/*      */       }
/*      */       
/*  264 */       startAcceptorThreads();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void stopInternal()
/*      */   {
/*  274 */     releaseConnectionLatch();
/*  275 */     if (!this.paused) {
/*  276 */       pause();
/*      */     }
/*  278 */     if (this.running) {
/*  279 */       this.running = false;
/*  280 */       unlockAccept();
/*  281 */       for (int i = 0; (this.pollers != null) && (i < this.pollers.length); i++)
/*  282 */         if (this.pollers[i] != null) {
/*  283 */           this.pollers[i].destroy();
/*  284 */           this.pollers[i] = null;
/*      */         }
/*      */       try {
/*  287 */         if (!getStopLatch().await(this.selectorTimeout + 100L, TimeUnit.MILLISECONDS)) {
/*  288 */           log.warn(sm.getString("endpoint.nio.stopLatchAwaitFail"));
/*      */         }
/*      */       } catch (InterruptedException e) {
/*  291 */         log.warn(sm.getString("endpoint.nio.stopLatchAwaitInterrupted"), e);
/*      */       }
/*  293 */       shutdownExecutor();
/*  294 */       this.eventCache.clear();
/*  295 */       this.nioChannels.clear();
/*  296 */       this.processorCache.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void unbind()
/*      */     throws Exception
/*      */   {
/*  306 */     if (log.isDebugEnabled()) {
/*  307 */       log.debug("Destroy initiated for " + new InetSocketAddress(getAddress(), getPort()));
/*      */     }
/*  309 */     if (this.running) {
/*  310 */       stop();
/*      */     }
/*      */     
/*  313 */     this.serverSock.socket().close();
/*  314 */     this.serverSock.close();
/*  315 */     this.serverSock = null;
/*  316 */     destroySsl();
/*  317 */     super.unbind();
/*  318 */     if (getHandler() != null) {
/*  319 */       getHandler().recycle();
/*      */     }
/*  321 */     this.selectorPool.close();
/*  322 */     if (log.isDebugEnabled()) {
/*  323 */       log.debug("Destroy completed for " + new InetSocketAddress(getAddress(), getPort()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getWriteBufSize()
/*      */   {
/*  332 */     return this.socketProperties.getTxBufSize();
/*      */   }
/*      */   
/*      */   public int getReadBufSize() {
/*  336 */     return this.socketProperties.getRxBufSize();
/*      */   }
/*      */   
/*      */   public NioSelectorPool getSelectorPool() {
/*  340 */     return this.selectorPool;
/*      */   }
/*      */   
/*      */ 
/*      */   protected AbstractEndpoint.Acceptor createAcceptor()
/*      */   {
/*  346 */     return new Acceptor();
/*      */   }
/*      */   
/*      */   protected CountDownLatch getStopLatch()
/*      */   {
/*  351 */     return this.stopLatch;
/*      */   }
/*      */   
/*      */   protected void setStopLatch(CountDownLatch stopLatch)
/*      */   {
/*  356 */     this.stopLatch = stopLatch;
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
/*      */   protected boolean setSocketOptions(SocketChannel socket)
/*      */   {
/*      */     try
/*      */     {
/*  371 */       socket.configureBlocking(false);
/*  372 */       Socket sock = socket.socket();
/*  373 */       this.socketProperties.setProperties(sock);
/*      */       
/*  375 */       NioChannel channel = (NioChannel)this.nioChannels.pop();
/*  376 */       if (channel == null)
/*      */       {
/*      */ 
/*      */ 
/*  380 */         SocketBufferHandler bufhandler = new SocketBufferHandler(this.socketProperties.getAppReadBufSize(), this.socketProperties.getAppWriteBufSize(), this.socketProperties.getDirectBuffer());
/*  381 */         if (isSSLEnabled()) {
/*  382 */           channel = new SecureNioChannel(socket, bufhandler, this.selectorPool, this);
/*      */         } else {
/*  384 */           channel = new NioChannel(socket, bufhandler);
/*      */         }
/*      */       } else {
/*  387 */         channel.setIOChannel(socket);
/*  388 */         channel.reset();
/*      */       }
/*  390 */       getPoller0().register(channel);
/*      */     } catch (Throwable t) {
/*  392 */       ExceptionUtils.handleThrowable(t);
/*      */       try {
/*  394 */         log.error("", t);
/*      */       } catch (Throwable tt) {
/*  396 */         ExceptionUtils.handleThrowable(tt);
/*      */       }
/*      */       
/*  399 */       return false;
/*      */     }
/*  401 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected Log getLog()
/*      */   {
/*  407 */     return log;
/*      */   }
/*      */   
/*      */ 
/*      */   protected NetworkChannel getServerSocket()
/*      */   {
/*  413 */     return this.serverSock;
/*      */   }
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
/*  427 */       int errorDelay = 0;
/*      */       
/*      */ 
/*  430 */       while (NioEndpoint.this.running)
/*      */       {
/*      */ 
/*  433 */         while ((NioEndpoint.this.paused) && (NioEndpoint.this.running)) {
/*  434 */           this.state = AbstractEndpoint.Acceptor.AcceptorState.PAUSED;
/*      */           try {
/*  436 */             Thread.sleep(50L);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {}
/*      */         }
/*      */         
/*      */ 
/*  442 */         if (!NioEndpoint.this.running) {
/*      */           break;
/*      */         }
/*  445 */         this.state = AbstractEndpoint.Acceptor.AcceptorState.RUNNING;
/*      */         
/*      */         try
/*      */         {
/*  449 */           NioEndpoint.this.countUpOrAwaitConnection();
/*      */           
/*  451 */           SocketChannel socket = null;
/*      */           
/*      */           try
/*      */           {
/*  455 */             socket = NioEndpoint.this.serverSock.accept();
/*      */           }
/*      */           catch (IOException ioe) {
/*  458 */             NioEndpoint.this.countDownConnection();
/*  459 */             if (NioEndpoint.this.running)
/*      */             {
/*  461 */               errorDelay = NioEndpoint.this.handleExceptionWithDelay(errorDelay);
/*      */               
/*  463 */               throw ioe;
/*      */             }
/*  465 */             break;
/*      */           }
/*      */           
/*      */ 
/*  469 */           errorDelay = 0;
/*      */           
/*      */ 
/*  472 */           if ((NioEndpoint.this.running) && (!NioEndpoint.this.paused))
/*      */           {
/*      */ 
/*  475 */             if (!NioEndpoint.this.setSocketOptions(socket)) {
/*  476 */               closeSocket(socket);
/*      */             }
/*      */           } else {
/*  479 */             closeSocket(socket);
/*      */           }
/*      */         } catch (Throwable t) {
/*  482 */           ExceptionUtils.handleThrowable(t);
/*  483 */           NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.accept.fail"), t);
/*      */         }
/*      */       }
/*  486 */       this.state = AbstractEndpoint.Acceptor.AcceptorState.ENDED;
/*      */     }
/*      */     
/*      */     private void closeSocket(SocketChannel socket)
/*      */     {
/*  491 */       NioEndpoint.this.countDownConnection();
/*      */       try {
/*  493 */         socket.socket().close();
/*      */       } catch (IOException ioe) {
/*  495 */         if (NioEndpoint.log.isDebugEnabled()) {
/*  496 */           NioEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.close"), ioe);
/*      */         }
/*      */       }
/*      */       try {
/*  500 */         socket.close();
/*      */       } catch (IOException ioe) {
/*  502 */         if (NioEndpoint.log.isDebugEnabled()) {
/*  503 */           NioEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.err.close"), ioe);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected SocketProcessorBase<NioChannel> createSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent event)
/*      */   {
/*  513 */     return new SocketProcessor(socketWrapper, event);
/*      */   }
/*      */   
/*      */   private void close(NioChannel socket, SelectionKey key)
/*      */   {
/*      */     try {
/*  519 */       if (socket.getPoller().cancelledKey(key) != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  526 */         if ((this.running) && (!this.paused) && 
/*  527 */           (!this.nioChannels.push(socket))) {
/*  528 */           socket.free();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception x) {
/*  533 */       log.error("", x);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class PollerEvent
/*      */     implements Runnable
/*      */   {
/*      */     private NioChannel socket;
/*      */     
/*      */     private int interestOps;
/*      */     
/*      */     private NioEndpoint.NioSocketWrapper socketWrapper;
/*      */     
/*      */ 
/*      */     public PollerEvent(NioChannel ch, NioEndpoint.NioSocketWrapper w, int intOps)
/*      */     {
/*  550 */       reset(ch, w, intOps);
/*      */     }
/*      */     
/*      */     public void reset(NioChannel ch, NioEndpoint.NioSocketWrapper w, int intOps) {
/*  554 */       this.socket = ch;
/*  555 */       this.interestOps = intOps;
/*  556 */       this.socketWrapper = w;
/*      */     }
/*      */     
/*      */     public void reset() {
/*  560 */       reset(null, null, 0);
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*  565 */       if (this.interestOps == 256) {
/*      */         try {
/*  567 */           this.socket.getIOChannel().register(this.socket
/*  568 */             .getPoller().getSelector(), 1, this.socketWrapper);
/*      */         } catch (Exception x) {
/*  570 */           NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.registerFail"), x);
/*      */         }
/*      */       } else {
/*  573 */         SelectionKey key = this.socket.getIOChannel().keyFor(this.socket.getPoller().getSelector());
/*      */         try {
/*  575 */           if (key == null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  581 */             this.socket.socketWrapper.getEndpoint().countDownConnection();
/*      */           } else {
/*  583 */             NioEndpoint.NioSocketWrapper socketWrapper = (NioEndpoint.NioSocketWrapper)key.attachment();
/*  584 */             if (socketWrapper != null)
/*      */             {
/*  586 */               int ops = key.interestOps() | this.interestOps;
/*  587 */               socketWrapper.interestOps(ops);
/*  588 */               key.interestOps(ops);
/*      */             } else {
/*  590 */               this.socket.getPoller().cancelledKey(key);
/*      */             }
/*      */           }
/*      */         } catch (CancelledKeyException ckx) {
/*      */           try {
/*  595 */             this.socket.getPoller().cancelledKey(key);
/*      */           }
/*      */           catch (Exception localException1) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public String toString() {
/*  603 */       return "Poller event: socket [" + this.socket + "], socketWrapper [" + this.socketWrapper + "], interestOps [" + this.interestOps + "]";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public class Poller
/*      */     implements Runnable
/*      */   {
/*      */     private Selector selector;
/*      */     
/*  614 */     private final SynchronizedQueue<NioEndpoint.PollerEvent> events = new SynchronizedQueue();
/*      */     
/*      */ 
/*  617 */     private volatile boolean close = false;
/*  618 */     private long nextExpiration = 0L;
/*      */     
/*  620 */     private AtomicLong wakeupCounter = new AtomicLong(0L);
/*      */     
/*  622 */     private volatile int keyCount = 0;
/*      */     
/*      */     public Poller() throws IOException {
/*  625 */       this.selector = Selector.open();
/*      */     }
/*      */     
/*  628 */     public int getKeyCount() { return this.keyCount; }
/*      */     
/*  630 */     public Selector getSelector() { return this.selector; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void destroy()
/*      */     {
/*  639 */       this.close = true;
/*  640 */       this.selector.wakeup();
/*      */     }
/*      */     
/*      */     private void addEvent(NioEndpoint.PollerEvent event) {
/*  644 */       this.events.offer(event);
/*  645 */       if (this.wakeupCounter.incrementAndGet() == 0L) { this.selector.wakeup();
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
/*      */     public void add(NioChannel socket, int interestOps)
/*      */     {
/*  659 */       NioEndpoint.PollerEvent r = (NioEndpoint.PollerEvent)NioEndpoint.this.eventCache.pop();
/*  660 */       if (r == null) r = new NioEndpoint.PollerEvent(socket, null, interestOps); else
/*  661 */         r.reset(socket, null, interestOps);
/*  662 */       addEvent(r);
/*  663 */       if (this.close) {
/*  664 */         NioEndpoint.NioSocketWrapper ka = (NioEndpoint.NioSocketWrapper)socket.getAttachment();
/*  665 */         NioEndpoint.this.processSocket(ka, SocketEvent.STOP, false);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean events()
/*      */     {
/*  676 */       boolean result = false;
/*      */       
/*  678 */       NioEndpoint.PollerEvent pe = null;
/*  679 */       int i = 0; for (int size = this.events.size(); (i < size) && ((pe = (NioEndpoint.PollerEvent)this.events.poll()) != null); i++) {
/*  680 */         result = true;
/*      */         try {
/*  682 */           pe.run();
/*  683 */           pe.reset();
/*  684 */           if ((NioEndpoint.this.running) && (!NioEndpoint.this.paused)) {
/*  685 */             NioEndpoint.this.eventCache.push(pe);
/*      */           }
/*      */         } catch (Throwable x) {
/*  688 */           NioEndpoint.log.error("", x);
/*      */         }
/*      */       }
/*      */       
/*  692 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void register(NioChannel socket)
/*      */     {
/*  701 */       socket.setPoller(this);
/*  702 */       NioEndpoint.NioSocketWrapper ka = new NioEndpoint.NioSocketWrapper(socket, NioEndpoint.this);
/*  703 */       socket.setSocketWrapper(ka);
/*  704 */       ka.setPoller(this);
/*  705 */       ka.setReadTimeout(NioEndpoint.this.getSocketProperties().getSoTimeout());
/*  706 */       ka.setWriteTimeout(NioEndpoint.this.getSocketProperties().getSoTimeout());
/*  707 */       ka.setKeepAliveLeft(NioEndpoint.this.getMaxKeepAliveRequests());
/*  708 */       ka.setSecure(NioEndpoint.this.isSSLEnabled());
/*  709 */       ka.setReadTimeout(NioEndpoint.this.getConnectionTimeout());
/*  710 */       ka.setWriteTimeout(NioEndpoint.this.getConnectionTimeout());
/*  711 */       NioEndpoint.PollerEvent r = (NioEndpoint.PollerEvent)NioEndpoint.this.eventCache.pop();
/*  712 */       ka.interestOps(1);
/*  713 */       if (r == null) r = new NioEndpoint.PollerEvent(socket, ka, 256); else
/*  714 */         r.reset(socket, ka, 256);
/*  715 */       addEvent(r);
/*      */     }
/*      */     
/*      */     public NioEndpoint.NioSocketWrapper cancelledKey(SelectionKey key) {
/*  719 */       NioEndpoint.NioSocketWrapper ka = null;
/*      */       try {
/*  721 */         if (key == null) return null;
/*  722 */         ka = (NioEndpoint.NioSocketWrapper)key.attach(null);
/*  723 */         if (ka != null)
/*      */         {
/*      */ 
/*  726 */           NioEndpoint.this.getHandler().release(ka);
/*      */         }
/*  728 */         if (key.isValid()) { key.cancel();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  733 */         if (ka != null) {
/*      */           try {
/*  735 */             ((NioChannel)ka.getSocket()).close(true);
/*      */           } catch (Exception e) {
/*  737 */             if (NioEndpoint.log.isDebugEnabled()) {
/*  738 */               NioEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.socketCloseFail"), e);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  745 */         if (key.channel().isOpen()) {
/*      */           try {
/*  747 */             key.channel().close();
/*      */           } catch (Exception e) {
/*  749 */             if (NioEndpoint.log.isDebugEnabled()) {
/*  750 */               NioEndpoint.log.debug(AbstractEndpoint.sm.getString("endpoint.debug.channelCloseFail"), e);
/*      */             }
/*      */           }
/*      */         }
/*      */         try
/*      */         {
/*  756 */           if ((ka != null) && (ka.getSendfileData() != null) && 
/*  757 */             (ka.getSendfileData().fchannel != null) && 
/*  758 */             (ka.getSendfileData().fchannel.isOpen())) {
/*  759 */             ka.getSendfileData().fchannel.close();
/*      */           }
/*      */         }
/*      */         catch (Exception localException1) {}
/*  763 */         if (ka != null) {
/*  764 */           NioEndpoint.this.countDownConnection();
/*      */         }
/*      */       } catch (Throwable e) {
/*  767 */         ExceptionUtils.handleThrowable(e);
/*  768 */         if (NioEndpoint.log.isDebugEnabled()) NioEndpoint.log.error("", e);
/*      */       }
/*  770 */       return ka;
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
/*      */       for (;;)
/*      */       {
/*  783 */         boolean hasEvents = false;
/*      */         try
/*      */         {
/*  786 */           if (!this.close) {
/*  787 */             hasEvents = events();
/*  788 */             if (this.wakeupCounter.getAndSet(-1L) > 0L)
/*      */             {
/*      */ 
/*  791 */               this.keyCount = this.selector.selectNow();
/*      */             } else {
/*  793 */               this.keyCount = this.selector.select(NioEndpoint.this.selectorTimeout);
/*      */             }
/*  795 */             this.wakeupCounter.set(0L);
/*      */           }
/*  797 */           if (this.close) {
/*  798 */             events();
/*  799 */             timeout(0, false);
/*      */             try {
/*  801 */               this.selector.close();
/*      */             } catch (IOException ioe) {
/*  803 */               NioEndpoint.log.error(AbstractEndpoint.sm.getString("endpoint.nio.selectorCloseFail"), ioe);
/*      */             }
/*  805 */             break;
/*      */           }
/*      */         } catch (Throwable x) {
/*  808 */           ExceptionUtils.handleThrowable(x);
/*  809 */           NioEndpoint.log.error("", x); }
/*  810 */         continue;
/*      */         
/*      */ 
/*  813 */         if (this.keyCount == 0) { hasEvents |= events();
/*      */         }
/*      */         
/*  816 */         Iterator<SelectionKey> iterator = this.keyCount > 0 ? this.selector.selectedKeys().iterator() : null;
/*      */         
/*      */ 
/*  819 */         while ((iterator != null) && (iterator.hasNext())) {
/*  820 */           SelectionKey sk = (SelectionKey)iterator.next();
/*  821 */           NioEndpoint.NioSocketWrapper attachment = (NioEndpoint.NioSocketWrapper)sk.attachment();
/*      */           
/*      */ 
/*  824 */           if (attachment == null) {
/*  825 */             iterator.remove();
/*      */           } else {
/*  827 */             iterator.remove();
/*  828 */             processKey(sk, attachment);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  833 */         timeout(this.keyCount, hasEvents);
/*      */       }
/*      */       
/*  836 */       NioEndpoint.this.getStopLatch().countDown();
/*      */     }
/*      */     
/*      */     protected void processKey(SelectionKey sk, NioEndpoint.NioSocketWrapper attachment) {
/*      */       try {
/*  841 */         if (this.close) {
/*  842 */           cancelledKey(sk);
/*  843 */         } else if ((sk.isValid()) && (attachment != null)) {
/*  844 */           if ((sk.isReadable()) || (sk.isWritable())) {
/*  845 */             if (attachment.getSendfileData() != null) {
/*  846 */               processSendfile(sk, attachment, false);
/*      */             } else {
/*  848 */               unreg(sk, attachment, sk.readyOps());
/*  849 */               boolean closeSocket = false;
/*      */               
/*  851 */               if ((sk.isReadable()) && 
/*  852 */                 (!NioEndpoint.this.processSocket(attachment, SocketEvent.OPEN_READ, true))) {
/*  853 */                 closeSocket = true;
/*      */               }
/*      */               
/*  856 */               if ((!closeSocket) && (sk.isWritable()) && 
/*  857 */                 (!NioEndpoint.this.processSocket(attachment, SocketEvent.OPEN_WRITE, true))) {
/*  858 */                 closeSocket = true;
/*      */               }
/*      */               
/*  861 */               if (closeSocket) {
/*  862 */                 cancelledKey(sk);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  868 */           cancelledKey(sk);
/*      */         }
/*      */       } catch (CancelledKeyException ckx) {
/*  871 */         cancelledKey(sk);
/*      */       } catch (Throwable t) {
/*  873 */         ExceptionUtils.handleThrowable(t);
/*  874 */         NioEndpoint.log.error("", t);
/*      */       }
/*      */     }
/*      */     
/*      */     public SendfileState processSendfile(SelectionKey sk, NioEndpoint.NioSocketWrapper socketWrapper, boolean calledByProcessor)
/*      */     {
/*  880 */       NioChannel sc = null;
/*      */       try {
/*  882 */         unreg(sk, socketWrapper, sk.readyOps());
/*  883 */         NioEndpoint.SendfileData sd = socketWrapper.getSendfileData();
/*      */         
/*  885 */         if (NioEndpoint.log.isTraceEnabled()) {
/*  886 */           NioEndpoint.log.trace("Processing send file for: " + sd.fileName);
/*      */         }
/*      */         
/*  889 */         if (sd.fchannel == null)
/*      */         {
/*  891 */           File f = new File(sd.fileName);
/*      */           
/*  893 */           FileInputStream fis = new FileInputStream(f);
/*  894 */           sd.fchannel = fis.getChannel();
/*      */         }
/*      */         
/*      */ 
/*  898 */         sc = (NioChannel)socketWrapper.getSocket();
/*      */         
/*  900 */         WritableByteChannel wc = (sc instanceof SecureNioChannel) ? sc : sc.getIOChannel();
/*      */         
/*      */ 
/*  903 */         if (sc.getOutboundRemaining() > 0) {
/*  904 */           if (sc.flushOutbound()) {
/*  905 */             socketWrapper.updateLastWrite();
/*      */           }
/*      */         } else {
/*  908 */           long written = sd.fchannel.transferTo(sd.pos, sd.length, wc);
/*  909 */           if (written > 0L) {
/*  910 */             sd.pos += written;
/*  911 */             sd.length -= written;
/*  912 */             socketWrapper.updateLastWrite();
/*      */ 
/*      */ 
/*      */           }
/*  916 */           else if (sd.fchannel.size() <= sd.pos) {
/*  917 */             throw new IOException("Sendfile configured to send more data than was available");
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  922 */         if ((sd.length <= 0L) && (sc.getOutboundRemaining() <= 0)) {
/*  923 */           if (NioEndpoint.log.isDebugEnabled()) {
/*  924 */             NioEndpoint.log.debug("Send file complete for: " + sd.fileName);
/*      */           }
/*  926 */           socketWrapper.setSendfileData(null);
/*      */           try {
/*  928 */             sd.fchannel.close();
/*      */           }
/*      */           catch (Exception localException) {}
/*      */           
/*      */ 
/*      */ 
/*  934 */           if (!calledByProcessor) {
/*  935 */             switch (NioEndpoint.1.$SwitchMap$org$apache$tomcat$util$net$SendfileKeepAliveState[sd.keepAliveState.ordinal()]) {
/*      */             case 1: 
/*  937 */               if (NioEndpoint.log.isDebugEnabled()) {
/*  938 */                 NioEndpoint.log.debug("Send file connection is being closed");
/*      */               }
/*  940 */               NioEndpoint.this.close(sc, sk);
/*  941 */               break;
/*      */             
/*      */             case 2: 
/*  944 */               if (NioEndpoint.log.isDebugEnabled()) {
/*  945 */                 NioEndpoint.log.debug("Connection is keep alive, processing pipe-lined data");
/*      */               }
/*  947 */               if (!NioEndpoint.this.processSocket(socketWrapper, SocketEvent.OPEN_READ, true)) {
/*  948 */                 NioEndpoint.this.close(sc, sk);
/*      */               }
/*      */               
/*      */               break;
/*      */             case 3: 
/*  953 */               if (NioEndpoint.log.isDebugEnabled()) {
/*  954 */                 NioEndpoint.log.debug("Connection is keep alive, registering back for OP_READ");
/*      */               }
/*  956 */               reg(sk, socketWrapper, 1);
/*      */             }
/*      */             
/*      */           }
/*      */           
/*  961 */           return SendfileState.DONE;
/*      */         }
/*  963 */         if (NioEndpoint.log.isDebugEnabled()) {
/*  964 */           NioEndpoint.log.debug("OP_WRITE for sendfile: " + sd.fileName);
/*      */         }
/*  966 */         if (calledByProcessor) {
/*  967 */           add((NioChannel)socketWrapper.getSocket(), 4);
/*      */         } else {
/*  969 */           reg(sk, socketWrapper, 4);
/*      */         }
/*  971 */         return SendfileState.PENDING;
/*      */       }
/*      */       catch (IOException x) {
/*  974 */         if (NioEndpoint.log.isDebugEnabled()) NioEndpoint.log.debug("Unable to complete sendfile request:", x);
/*  975 */         if ((!calledByProcessor) && (sc != null)) {
/*  976 */           NioEndpoint.this.close(sc, sk);
/*      */         }
/*  978 */         return SendfileState.ERROR;
/*      */       } catch (Throwable t) {
/*  980 */         NioEndpoint.log.error("", t);
/*  981 */         if ((!calledByProcessor) && (sc != null))
/*  982 */           NioEndpoint.this.close(sc, sk);
/*      */       }
/*  984 */       return SendfileState.ERROR;
/*      */     }
/*      */     
/*      */ 
/*      */     protected void unreg(SelectionKey sk, NioEndpoint.NioSocketWrapper attachment, int readyOps)
/*      */     {
/*  990 */       reg(sk, attachment, sk.interestOps() & (readyOps ^ 0xFFFFFFFF));
/*      */     }
/*      */     
/*      */     protected void reg(SelectionKey sk, NioEndpoint.NioSocketWrapper attachment, int intops) {
/*  994 */       sk.interestOps(intops);
/*  995 */       attachment.interestOps(intops);
/*      */     }
/*      */     
/*      */     protected void timeout(int keyCount, boolean hasEvents) {
/*  999 */       long now = System.currentTimeMillis();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1007 */       if ((this.nextExpiration > 0L) && ((keyCount > 0) || (hasEvents)) && (now < this.nextExpiration) && (!this.close)) {
/* 1008 */         return;
/*      */       }
/*      */       
/* 1011 */       int keycount = 0;
/*      */       try {
/* 1013 */         for (SelectionKey key : this.selector.keys()) {
/* 1014 */           keycount++;
/*      */           try {
/* 1016 */             NioEndpoint.NioSocketWrapper ka = (NioEndpoint.NioSocketWrapper)key.attachment();
/* 1017 */             if (ka == null) {
/* 1018 */               cancelledKey(key);
/* 1019 */             } else if (this.close) {
/* 1020 */               key.interestOps(0);
/* 1021 */               ka.interestOps(0);
/* 1022 */               processKey(key, ka);
/* 1023 */             } else if (((ka.interestOps() & 0x1) == 1) || 
/* 1024 */               ((ka.interestOps() & 0x4) == 4)) {
/* 1025 */               boolean isTimedOut = false;
/*      */               
/* 1027 */               if ((ka.interestOps() & 0x1) == 1) {
/* 1028 */                 long delta = now - ka.getLastRead();
/* 1029 */                 long timeout = ka.getReadTimeout();
/* 1030 */                 isTimedOut = (timeout > 0L) && (delta > timeout);
/*      */               }
/*      */               
/* 1033 */               if ((!isTimedOut) && ((ka.interestOps() & 0x4) == 4)) {
/* 1034 */                 long delta = now - ka.getLastWrite();
/* 1035 */                 long timeout = ka.getWriteTimeout();
/* 1036 */                 isTimedOut = (timeout > 0L) && (delta > timeout);
/*      */               }
/* 1038 */               if (isTimedOut) {
/* 1039 */                 key.interestOps(0);
/* 1040 */                 ka.interestOps(0);
/* 1041 */                 ka.setError(new SocketTimeoutException());
/* 1042 */                 if (!NioEndpoint.this.processSocket(ka, SocketEvent.ERROR, true)) {
/* 1043 */                   cancelledKey(key);
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (CancelledKeyException ckx) {
/* 1048 */             cancelledKey(key);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ConcurrentModificationException cme) {
/* 1053 */         NioEndpoint.log.warn(AbstractEndpoint.sm.getString("endpoint.nio.timeoutCme"), cme);
/*      */       }
/* 1055 */       long prevExp = this.nextExpiration;
/*      */       
/* 1057 */       this.nextExpiration = (System.currentTimeMillis() + NioEndpoint.this.socketProperties.getTimeoutInterval());
/* 1058 */       if (NioEndpoint.log.isTraceEnabled()) {
/* 1059 */         NioEndpoint.log.trace("timeout completed: keys processed=" + keycount + "; now=" + now + "; nextExpiration=" + prevExp + "; keyCount=" + keyCount + "; hasEvents=" + hasEvents + "; eval=" + ((now < prevExp) && ((keyCount > 0) || (hasEvents)) && (!this.close)));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class NioSocketWrapper
/*      */     extends SocketWrapperBase<NioChannel>
/*      */   {
/*      */     private final NioSelectorPool pool;
/*      */     
/*      */ 
/* 1073 */     private NioEndpoint.Poller poller = null;
/* 1074 */     private int interestOps = 0;
/* 1075 */     private CountDownLatch readLatch = null;
/* 1076 */     private CountDownLatch writeLatch = null;
/* 1077 */     private volatile NioEndpoint.SendfileData sendfileData = null;
/* 1078 */     private volatile long lastRead = System.currentTimeMillis();
/* 1079 */     private volatile long lastWrite = this.lastRead;
/*      */     
/*      */     public NioSocketWrapper(NioChannel channel, NioEndpoint endpoint) {
/* 1082 */       super(endpoint);
/* 1083 */       this.pool = endpoint.getSelectorPool();
/* 1084 */       this.socketBufferHandler = channel.getBufHandler();
/*      */     }
/*      */     
/* 1087 */     public NioEndpoint.Poller getPoller() { return this.poller; }
/* 1088 */     public void setPoller(NioEndpoint.Poller poller) { this.poller = poller; }
/* 1089 */     public int interestOps() { return this.interestOps; }
/* 1090 */     public int interestOps(int ops) { this.interestOps = ops;return ops; }
/* 1091 */     public CountDownLatch getReadLatch() { return this.readLatch; }
/* 1092 */     public CountDownLatch getWriteLatch() { return this.writeLatch; }
/*      */     
/* 1094 */     protected CountDownLatch resetLatch(CountDownLatch latch) { if ((latch == null) || (latch.getCount() == 0L)) return null;
/* 1095 */       throw new IllegalStateException("Latch must be at count 0"); }
/*      */     
/* 1097 */     public void resetReadLatch() { this.readLatch = resetLatch(this.readLatch); }
/* 1098 */     public void resetWriteLatch() { this.writeLatch = resetLatch(this.writeLatch); }
/*      */     
/*      */     protected CountDownLatch startLatch(CountDownLatch latch, int cnt) {
/* 1101 */       if ((latch == null) || (latch.getCount() == 0L)) {
/* 1102 */         return new CountDownLatch(cnt);
/*      */       }
/* 1104 */       throw new IllegalStateException("Latch must be at count 0 or null."); }
/*      */     
/* 1106 */     public void startReadLatch(int cnt) { this.readLatch = startLatch(this.readLatch, cnt); }
/* 1107 */     public void startWriteLatch(int cnt) { this.writeLatch = startLatch(this.writeLatch, cnt); }
/*      */     
/*      */     protected void awaitLatch(CountDownLatch latch, long timeout, TimeUnit unit) throws InterruptedException {
/* 1110 */       if (latch == null) { throw new IllegalStateException("Latch cannot be null");
/*      */       }
/*      */       
/*      */ 
/* 1114 */       latch.await(timeout, unit); }
/*      */     
/* 1116 */     public void awaitReadLatch(long timeout, TimeUnit unit) throws InterruptedException { awaitLatch(this.readLatch, timeout, unit); }
/* 1117 */     public void awaitWriteLatch(long timeout, TimeUnit unit) throws InterruptedException { awaitLatch(this.writeLatch, timeout, unit); }
/*      */     
/* 1119 */     public void setSendfileData(NioEndpoint.SendfileData sf) { this.sendfileData = sf; }
/* 1120 */     public NioEndpoint.SendfileData getSendfileData() { return this.sendfileData; }
/*      */     
/* 1122 */     public void updateLastWrite() { this.lastWrite = System.currentTimeMillis(); }
/* 1123 */     public long getLastWrite() { return this.lastWrite; }
/* 1124 */     public void updateLastRead() { this.lastRead = System.currentTimeMillis(); }
/* 1125 */     public long getLastRead() { return this.lastRead; }
/*      */     
/*      */     public boolean isReadyForRead()
/*      */       throws IOException
/*      */     {
/* 1130 */       this.socketBufferHandler.configureReadBufferForRead();
/*      */       
/* 1132 */       if (this.socketBufferHandler.getReadBuffer().remaining() > 0) {
/* 1133 */         return true;
/*      */       }
/*      */       
/* 1136 */       fillReadBuffer(false);
/*      */       
/* 1138 */       boolean isReady = this.socketBufferHandler.getReadBuffer().position() > 0;
/* 1139 */       return isReady;
/*      */     }
/*      */     
/*      */     public int read(boolean block, byte[] b, int off, int len)
/*      */       throws IOException
/*      */     {
/* 1145 */       int nRead = populateReadBuffer(b, off, len);
/* 1146 */       if (nRead > 0) {
/* 1147 */         return nRead;
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
/* 1158 */       nRead = fillReadBuffer(block);
/* 1159 */       updateLastRead();
/*      */       
/*      */ 
/*      */ 
/* 1163 */       if (nRead > 0) {
/* 1164 */         this.socketBufferHandler.configureReadBufferForRead();
/* 1165 */         nRead = Math.min(nRead, len);
/* 1166 */         this.socketBufferHandler.getReadBuffer().get(b, off, nRead);
/*      */       }
/* 1168 */       return nRead;
/*      */     }
/*      */     
/*      */     public int read(boolean block, ByteBuffer to)
/*      */       throws IOException
/*      */     {
/* 1174 */       int nRead = populateReadBuffer(to);
/* 1175 */       if (nRead > 0) {
/* 1176 */         return nRead;
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
/* 1187 */       int limit = this.socketBufferHandler.getReadBuffer().capacity();
/* 1188 */       if (to.remaining() >= limit) {
/* 1189 */         to.limit(to.position() + limit);
/* 1190 */         nRead = fillReadBuffer(block, to);
/* 1191 */         updateLastRead();
/*      */       }
/*      */       else {
/* 1194 */         nRead = fillReadBuffer(block);
/* 1195 */         updateLastRead();
/*      */         
/*      */ 
/*      */ 
/* 1199 */         if (nRead > 0) {
/* 1200 */           nRead = populateReadBuffer(to);
/*      */         }
/*      */       }
/* 1203 */       return nRead;
/*      */     }
/*      */     
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/* 1209 */       ((NioChannel)getSocket()).close();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isClosed()
/*      */     {
/* 1215 */       return !((NioChannel)getSocket()).isOpen();
/*      */     }
/*      */     
/*      */     private int fillReadBuffer(boolean block) throws IOException
/*      */     {
/* 1220 */       this.socketBufferHandler.configureReadBufferForWrite();
/* 1221 */       return fillReadBuffer(block, this.socketBufferHandler.getReadBuffer());
/*      */     }
/*      */     
/*      */     private int fillReadBuffer(boolean block, ByteBuffer to)
/*      */       throws IOException
/*      */     {
/* 1227 */       NioChannel channel = (NioChannel)getSocket();
/* 1228 */       int nRead; int nRead; if (block) {
/* 1229 */         Selector selector = null;
/*      */         try {
/* 1231 */           selector = this.pool.get();
/*      */         }
/*      */         catch (IOException localIOException) {}
/*      */         
/*      */         try
/*      */         {
/* 1237 */           NioSocketWrapper att = (NioSocketWrapper)channel.getAttachment();
/* 1238 */           if (att == null) {
/* 1239 */             throw new IOException("Key must be cancelled.");
/*      */           }
/* 1241 */           nRead = this.pool.read(to, channel, selector, att.getReadTimeout());
/*      */         } finally { int nRead;
/* 1243 */           if (selector != null) {
/* 1244 */             this.pool.put(selector);
/*      */           }
/*      */         }
/*      */       } else {
/* 1248 */         nRead = channel.read(to);
/* 1249 */         if (nRead == -1) {
/* 1250 */           throw new EOFException();
/*      */         }
/*      */       }
/* 1253 */       return nRead;
/*      */     }
/*      */     
/*      */     protected void doWrite(boolean block, ByteBuffer from)
/*      */       throws IOException
/*      */     {
/* 1259 */       long writeTimeout = getWriteTimeout();
/* 1260 */       Selector selector = null;
/*      */       try {
/* 1262 */         selector = this.pool.get();
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */       try
/*      */       {
/* 1267 */         this.pool.write(from, (NioChannel)getSocket(), selector, writeTimeout, block);
/* 1268 */         if (block) {
/*      */           for (;;)
/*      */           {
/* 1271 */             if (((NioChannel)getSocket()).flush(true, selector, writeTimeout)) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1276 */         updateLastWrite();
/*      */       } finally {
/* 1278 */         if (selector != null) {
/* 1279 */           this.pool.put(selector);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void registerReadInterest()
/*      */     {
/* 1291 */       getPoller().add((NioChannel)getSocket(), 1);
/*      */     }
/*      */     
/*      */ 
/*      */     public void registerWriteInterest()
/*      */     {
/* 1297 */       getPoller().add((NioChannel)getSocket(), 4);
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileDataBase createSendfileData(String filename, long pos, long length)
/*      */     {
/* 1303 */       return new NioEndpoint.SendfileData(filename, pos, length);
/*      */     }
/*      */     
/*      */ 
/*      */     public SendfileState processSendfile(SendfileDataBase sendfileData)
/*      */     {
/* 1309 */       setSendfileData((NioEndpoint.SendfileData)sendfileData);
/* 1310 */       SelectionKey key = ((NioChannel)getSocket()).getIOChannel().keyFor(
/* 1311 */         ((NioChannel)getSocket()).getPoller().getSelector());
/*      */       
/* 1313 */       return ((NioChannel)getSocket()).getPoller().processSendfile(key, this, true);
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteAddr()
/*      */     {
/* 1319 */       InetAddress inetAddr = ((NioChannel)getSocket()).getIOChannel().socket().getInetAddress();
/* 1320 */       if (inetAddr != null) {
/* 1321 */         this.remoteAddr = inetAddr.getHostAddress();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemoteHost()
/*      */     {
/* 1328 */       InetAddress inetAddr = ((NioChannel)getSocket()).getIOChannel().socket().getInetAddress();
/* 1329 */       if (inetAddr != null) {
/* 1330 */         this.remoteHost = inetAddr.getHostName();
/* 1331 */         if (this.remoteAddr == null) {
/* 1332 */           this.remoteAddr = inetAddr.getHostAddress();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateRemotePort()
/*      */     {
/* 1340 */       this.remotePort = ((NioChannel)getSocket()).getIOChannel().socket().getPort();
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalName()
/*      */     {
/* 1346 */       InetAddress inetAddr = ((NioChannel)getSocket()).getIOChannel().socket().getLocalAddress();
/* 1347 */       if (inetAddr != null) {
/* 1348 */         this.localName = inetAddr.getHostName();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalAddr()
/*      */     {
/* 1355 */       InetAddress inetAddr = ((NioChannel)getSocket()).getIOChannel().socket().getLocalAddress();
/* 1356 */       if (inetAddr != null) {
/* 1357 */         this.localAddr = inetAddr.getHostAddress();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     protected void populateLocalPort()
/*      */     {
/* 1364 */       this.localPort = ((NioChannel)getSocket()).getIOChannel().socket().getLocalPort();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public SSLSupport getSslSupport(String clientCertProvider)
/*      */     {
/* 1374 */       if ((getSocket() instanceof SecureNioChannel)) {
/* 1375 */         SecureNioChannel ch = (SecureNioChannel)getSocket();
/* 1376 */         SSLSession session = ch.getSslEngine().getSession();
/* 1377 */         return ((NioEndpoint)getEndpoint()).getSslImplementation().getSSLSupport(session);
/*      */       }
/* 1379 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public void doClientAuth(SSLSupport sslSupport)
/*      */       throws IOException
/*      */     {
/* 1386 */       SecureNioChannel sslChannel = (SecureNioChannel)getSocket();
/* 1387 */       SSLEngine engine = sslChannel.getSslEngine();
/* 1388 */       if (!engine.getNeedClientAuth())
/*      */       {
/* 1390 */         engine.setNeedClientAuth(true);
/* 1391 */         sslChannel.rehandshake(getEndpoint().getConnectionTimeout());
/* 1392 */         ((JSSESupport)sslSupport).setSession(engine.getSession());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void setAppReadBufHandler(ApplicationBufferHandler handler)
/*      */     {
/* 1399 */       ((NioChannel)getSocket()).setAppReadBufHandler(handler);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class SocketProcessor
/*      */     extends SocketProcessorBase<NioChannel>
/*      */   {
/*      */     public SocketProcessor(SocketEvent socketWrapper)
/*      */     {
/* 1413 */       super(event);
/*      */     }
/*      */     
/*      */     protected void doRun()
/*      */     {
/* 1418 */       NioChannel socket = (NioChannel)this.socketWrapper.getSocket();
/* 1419 */       SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
/*      */       try
/*      */       {
/* 1422 */         int handshake = -1;
/*      */         try
/*      */         {
/* 1425 */           if (key != null) {
/* 1426 */             if (socket.isHandshakeComplete())
/*      */             {
/*      */ 
/* 1429 */               handshake = 0;
/* 1430 */             } else if ((this.event == SocketEvent.STOP) || (this.event == SocketEvent.DISCONNECT) || (this.event == SocketEvent.ERROR))
/*      */             {
/*      */ 
/*      */ 
/* 1434 */               handshake = -1;
/*      */             } else {
/* 1436 */               handshake = socket.handshake(key.isReadable(), key.isWritable());
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1444 */               this.event = SocketEvent.OPEN_READ;
/*      */             }
/*      */           }
/*      */         } catch (IOException x) {
/* 1448 */           handshake = -1;
/* 1449 */           if (NioEndpoint.log.isDebugEnabled()) NioEndpoint.log.debug("Error during SSL handshake", x);
/*      */         } catch (CancelledKeyException ckx) {
/* 1451 */           handshake = -1;
/*      */         }
/* 1453 */         if (handshake == 0) {
/* 1454 */           AbstractEndpoint.Handler.SocketState state = AbstractEndpoint.Handler.SocketState.OPEN;
/*      */           
/* 1456 */           if (this.event == null) {
/* 1457 */             state = NioEndpoint.this.getHandler().process(this.socketWrapper, SocketEvent.OPEN_READ);
/*      */           } else {
/* 1459 */             state = NioEndpoint.this.getHandler().process(this.socketWrapper, this.event);
/*      */           }
/* 1461 */           if (state == AbstractEndpoint.Handler.SocketState.CLOSED) {
/* 1462 */             NioEndpoint.this.close(socket, key);
/*      */           }
/* 1464 */         } else if (handshake == -1) {
/* 1465 */           NioEndpoint.this.close(socket, key);
/* 1466 */         } else if (handshake == 1) {
/* 1467 */           this.socketWrapper.registerReadInterest();
/* 1468 */         } else if (handshake == 4) {
/* 1469 */           this.socketWrapper.registerWriteInterest();
/*      */         }
/*      */       } catch (CancelledKeyException cx) {
/* 1472 */         socket.getPoller().cancelledKey(key);
/*      */       } catch (VirtualMachineError vme) {
/* 1474 */         ExceptionUtils.handleThrowable(vme);
/*      */       } catch (Throwable t) {
/* 1476 */         NioEndpoint.log.error("", t);
/* 1477 */         socket.getPoller().cancelledKey(key);
/*      */       } finally {
/* 1479 */         this.socketWrapper = null;
/* 1480 */         this.event = null;
/*      */         
/* 1482 */         if ((NioEndpoint.this.running) && (!NioEndpoint.this.paused)) {
/* 1483 */           NioEndpoint.this.processorCache.push(this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SendfileData
/*      */     extends SendfileDataBase
/*      */   {
/*      */     protected volatile FileChannel fchannel;
/*      */     
/*      */     public SendfileData(String filename, long pos, long length)
/*      */     {
/* 1496 */       super(pos, length);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\NioEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */