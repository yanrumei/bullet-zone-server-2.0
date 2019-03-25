/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.PreSerializationTransformer;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import ch.qos.logback.core.util.Duration;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.SocketFactory;
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
/*     */ public abstract class AbstractSocketAppender<E>
/*     */   extends AppenderBase<E>
/*     */   implements SocketConnector.ExceptionHandler
/*     */ {
/*     */   public static final int DEFAULT_PORT = 4560;
/*     */   public static final int DEFAULT_RECONNECTION_DELAY = 30000;
/*     */   public static final int DEFAULT_QUEUE_SIZE = 128;
/*     */   private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
/*     */   private static final int DEFAULT_EVENT_DELAY_TIMEOUT = 100;
/*     */   private final ObjectWriterFactory objectWriterFactory;
/*     */   private final QueueFactory queueFactory;
/*     */   private String remoteHost;
/*  78 */   private int port = 4560;
/*     */   private InetAddress address;
/*  80 */   private Duration reconnectionDelay = new Duration(30000L);
/*  81 */   private int queueSize = 128;
/*  82 */   private int acceptConnectionTimeout = 5000;
/*  83 */   private Duration eventDelayLimit = new Duration(100L);
/*     */   
/*     */   private BlockingDeque<E> deque;
/*     */   
/*     */   private String peerId;
/*     */   
/*     */   private SocketConnector connector;
/*     */   
/*     */   private Future<?> task;
/*     */   private volatile Socket socket;
/*     */   
/*     */   protected AbstractSocketAppender()
/*     */   {
/*  96 */     this(new QueueFactory(), new ObjectWriterFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   AbstractSocketAppender(QueueFactory queueFactory, ObjectWriterFactory objectWriterFactory)
/*     */   {
/* 103 */     this.objectWriterFactory = objectWriterFactory;
/* 104 */     this.queueFactory = queueFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/* 111 */     if (isStarted())
/* 112 */       return;
/* 113 */     int errorCount = 0;
/* 114 */     if (this.port <= 0) {
/* 115 */       errorCount++;
/* 116 */       addError("No port was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_port");
/*     */     }
/*     */     
/* 119 */     if (this.remoteHost == null) {
/* 120 */       errorCount++;
/* 121 */       addError("No remote host was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_host");
/*     */     }
/*     */     
/*     */ 
/* 125 */     if (this.queueSize == 0) {
/* 126 */       addWarn("Queue size of zero is deprecated, use a size of one to indicate synchronous processing");
/*     */     }
/*     */     
/* 129 */     if (this.queueSize < 0) {
/* 130 */       errorCount++;
/* 131 */       addError("Queue size must be greater than zero");
/*     */     }
/*     */     
/* 134 */     if (errorCount == 0) {
/*     */       try {
/* 136 */         this.address = InetAddress.getByName(this.remoteHost);
/*     */       } catch (UnknownHostException ex) {
/* 138 */         addError("unknown host: " + this.remoteHost);
/* 139 */         errorCount++;
/*     */       }
/*     */     }
/*     */     
/* 143 */     if (errorCount == 0) {
/* 144 */       this.deque = this.queueFactory.newLinkedBlockingDeque(this.queueSize);
/* 145 */       this.peerId = ("remote peer " + this.remoteHost + ":" + this.port + ": ");
/* 146 */       this.connector = createConnector(this.address, this.port, 0, this.reconnectionDelay.getMilliseconds());
/* 147 */       this.task = getContext().getExecutorService().submit(new Runnable()
/*     */       {
/*     */         public void run() {
/* 150 */           AbstractSocketAppender.this.connectSocketAndDispatchEvents();
/*     */         }
/* 152 */       });
/* 153 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 162 */     if (!isStarted())
/* 163 */       return;
/* 164 */     CloseUtil.closeQuietly(this.socket);
/* 165 */     this.task.cancel(true);
/* 166 */     super.stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void append(E event)
/*     */   {
/* 174 */     if ((event == null) || (!isStarted())) {
/* 175 */       return;
/*     */     }
/*     */     try {
/* 178 */       boolean inserted = this.deque.offer(event, this.eventDelayLimit.getMilliseconds(), TimeUnit.MILLISECONDS);
/* 179 */       if (!inserted) {
/* 180 */         addInfo("Dropping event due to timeout limit of [" + this.eventDelayLimit + "] being exceeded");
/*     */       }
/*     */     } catch (InterruptedException e) {
/* 183 */       addError("Interrupted while appending event to SocketAppender", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void connectSocketAndDispatchEvents() {
/*     */     try {
/* 189 */       while (socketConnectionCouldBeEstablished()) {
/*     */         try {
/* 191 */           ObjectWriter objectWriter = createObjectWriterForSocket();
/* 192 */           addInfo(this.peerId + "connection established");
/* 193 */           dispatchEvents(objectWriter);
/*     */         } catch (IOException ex) {
/* 195 */           addInfo(this.peerId + "connection failed: " + ex);
/*     */         } finally {
/* 197 */           CloseUtil.closeQuietly(this.socket);
/* 198 */           this.socket = null;
/* 199 */           addInfo(this.peerId + "connection closed");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InterruptedException ex) {}
/*     */     
/* 205 */     addInfo("shutting down");
/*     */   }
/*     */   
/*     */   private boolean socketConnectionCouldBeEstablished() throws InterruptedException {
/* 209 */     return (this.socket = this.connector.call()) != null;
/*     */   }
/*     */   
/*     */   private ObjectWriter createObjectWriterForSocket() throws IOException {
/* 213 */     this.socket.setSoTimeout(this.acceptConnectionTimeout);
/* 214 */     ObjectWriter objectWriter = this.objectWriterFactory.newAutoFlushingObjectWriter(this.socket.getOutputStream());
/* 215 */     this.socket.setSoTimeout(0);
/* 216 */     return objectWriter;
/*     */   }
/*     */   
/*     */   private SocketConnector createConnector(InetAddress address, int port, int initialDelay, long retryDelay) {
/* 220 */     SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);
/* 221 */     connector.setExceptionHandler(this);
/* 222 */     connector.setSocketFactory(getSocketFactory());
/* 223 */     return connector;
/*     */   }
/*     */   
/*     */   private void dispatchEvents(ObjectWriter objectWriter) throws InterruptedException, IOException {
/*     */     for (;;) {
/* 228 */       E event = this.deque.takeFirst();
/* 229 */       postProcessEvent(event);
/* 230 */       Serializable serializableEvent = getPST().transform(event);
/*     */       try {
/* 232 */         objectWriter.write(serializableEvent);
/*     */       } catch (IOException e) {
/* 234 */         tryReAddingEventToFrontOfQueue(event);
/* 235 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryReAddingEventToFrontOfQueue(E event) {
/* 241 */     boolean wasInserted = this.deque.offerFirst(event);
/* 242 */     if (!wasInserted) {
/* 243 */       addInfo("Dropping event due to socket connection error and maxed out deque capacity");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void connectionFailed(SocketConnector connector, Exception ex)
/*     */   {
/* 251 */     if ((ex instanceof InterruptedException)) {
/* 252 */       addInfo("connector interrupted");
/* 253 */     } else if ((ex instanceof ConnectException)) {
/* 254 */       addInfo(this.peerId + "connection refused");
/*     */     } else {
/* 256 */       addInfo(this.peerId + ex);
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
/*     */   protected SocketConnector newConnector(InetAddress address, int port, long initialDelay, long retryDelay)
/*     */   {
/* 274 */     return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SocketFactory getSocketFactory()
/*     */   {
/* 283 */     return SocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void postProcessEvent(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract PreSerializationTransformer<E> getPST();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoteHost(String host)
/*     */   {
/* 305 */     this.remoteHost = host;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRemoteHost()
/*     */   {
/* 312 */     return this.remoteHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int port)
/*     */   {
/* 320 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 327 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReconnectionDelay(Duration delay)
/*     */   {
/* 339 */     this.reconnectionDelay = delay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Duration getReconnectionDelay()
/*     */   {
/* 346 */     return this.reconnectionDelay;
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
/*     */   public void setQueueSize(int queueSize)
/*     */   {
/* 362 */     this.queueSize = queueSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getQueueSize()
/*     */   {
/* 369 */     return this.queueSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEventDelayLimit(Duration eventDelayLimit)
/*     */   {
/* 380 */     this.eventDelayLimit = eventDelayLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Duration getEventDelayLimit()
/*     */   {
/* 387 */     return this.eventDelayLimit;
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
/*     */   void setAcceptConnectionTimeout(int acceptConnectionTimeout)
/*     */   {
/* 400 */     this.acceptConnectionTimeout = acceptConnectionTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\AbstractSocketAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */