/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.net.DefaultSocketConnector;
/*     */ import ch.qos.logback.core.net.SocketConnector;
/*     */ import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
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
/*     */ public class SocketReceiver
/*     */   extends ReceiverBase
/*     */   implements Runnable, SocketConnector.ExceptionHandler
/*     */ {
/*     */   private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
/*     */   private String remoteHost;
/*     */   private InetAddress address;
/*     */   private int port;
/*     */   private int reconnectionDelay;
/*  51 */   private int acceptConnectionTimeout = 5000;
/*     */   
/*     */   private String receiverId;
/*     */   
/*     */   private volatile Socket socket;
/*     */   
/*     */   private Future<Socket> connectorTask;
/*     */   
/*     */   protected boolean shouldStart()
/*     */   {
/*  61 */     int errorCount = 0;
/*  62 */     if (this.port == 0) {
/*  63 */       errorCount++;
/*  64 */       addError("No port was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_port");
/*     */     }
/*     */     
/*  67 */     if (this.remoteHost == null) {
/*  68 */       errorCount++;
/*  69 */       addError("No host name or address was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_host");
/*     */     }
/*     */     
/*     */ 
/*  73 */     if (this.reconnectionDelay == 0) {
/*  74 */       this.reconnectionDelay = 30000;
/*     */     }
/*     */     
/*  77 */     if (errorCount == 0) {
/*     */       try {
/*  79 */         this.address = InetAddress.getByName(this.remoteHost);
/*     */       } catch (UnknownHostException localUnknownHostException) {
/*  81 */         addError("unknown host: " + this.remoteHost);
/*  82 */         errorCount++;
/*     */       }
/*     */     }
/*     */     
/*  86 */     if (errorCount == 0) {
/*  87 */       this.receiverId = ("receiver " + this.remoteHost + ":" + this.port + ": ");
/*     */     }
/*     */     
/*  90 */     return errorCount == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void onStop()
/*     */   {
/*  97 */     if (this.socket != null) {
/*  98 */       CloseUtil.closeQuietly(this.socket);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Runnable getRunnableTask()
/*     */   {
/* 104 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 112 */       LoggerContext lc = (LoggerContext)getContext();
/* 113 */       while (!Thread.currentThread().isInterrupted()) {
/* 114 */         SocketConnector connector = createConnector(this.address, this.port, 0, this.reconnectionDelay);
/* 115 */         this.connectorTask = activateConnector(connector);
/* 116 */         if (this.connectorTask == null) {
/*     */           break;
/*     */         }
/* 119 */         this.socket = waitForConnectorToReturnASocket();
/* 120 */         if (this.socket == null)
/*     */           break;
/* 122 */         dispatchEvents(lc);
/*     */       }
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */     
/* 127 */     addInfo("shutting down");
/*     */   }
/*     */   
/*     */   private SocketConnector createConnector(InetAddress address, int port, int initialDelay, int retryDelay) {
/* 131 */     SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);
/* 132 */     connector.setExceptionHandler(this);
/* 133 */     connector.setSocketFactory(getSocketFactory());
/* 134 */     return connector;
/*     */   }
/*     */   
/*     */   private Future<Socket> activateConnector(SocketConnector connector) {
/*     */     try {
/* 139 */       return getContext().getScheduledExecutorService().submit(connector);
/*     */     } catch (RejectedExecutionException localRejectedExecutionException) {}
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   private Socket waitForConnectorToReturnASocket() throws InterruptedException
/*     */   {
/*     */     try {
/* 147 */       Socket s = (Socket)this.connectorTask.get();
/* 148 */       this.connectorTask = null;
/* 149 */       return s;
/*     */     } catch (ExecutionException localExecutionException) {}
/* 151 */     return null;
/*     */   }
/*     */   
/*     */   private void dispatchEvents(LoggerContext lc)
/*     */   {
/*     */     try {
/* 157 */       this.socket.setSoTimeout(this.acceptConnectionTimeout);
/* 158 */       ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
/* 159 */       this.socket.setSoTimeout(0);
/* 160 */       addInfo(this.receiverId + "connection established");
/*     */       for (;;) {
/* 162 */         ILoggingEvent event = (ILoggingEvent)ois.readObject();
/* 163 */         Logger remoteLogger = lc.getLogger(event.getLoggerName());
/* 164 */         if (remoteLogger.isEnabledFor(event.getLevel())) {
/* 165 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */       }
/*     */     } catch (EOFException localEOFException) {
/* 169 */       addInfo(this.receiverId + "end-of-stream detected");
/*     */     } catch (IOException ex) {
/* 171 */       addInfo(this.receiverId + "connection failed: " + ex);
/*     */     } catch (ClassNotFoundException ex) {
/* 173 */       addInfo(this.receiverId + "unknown event class: " + ex);
/*     */     } finally {
/* 175 */       CloseUtil.closeQuietly(this.socket);
/* 176 */       this.socket = null;
/* 177 */       addInfo(this.receiverId + "connection closed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void connectionFailed(SocketConnector connector, Exception ex)
/*     */   {
/* 185 */     if ((ex instanceof InterruptedException)) {
/* 186 */       addInfo("connector interrupted");
/* 187 */     } else if ((ex instanceof ConnectException)) {
/* 188 */       addInfo(this.receiverId + "connection refused");
/*     */     } else {
/* 190 */       addInfo(this.receiverId + ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected SocketConnector newConnector(InetAddress address, int port, int initialDelay, int retryDelay) {
/* 195 */     return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
/*     */   }
/*     */   
/*     */   protected SocketFactory getSocketFactory() {
/* 199 */     return SocketFactory.getDefault();
/*     */   }
/*     */   
/*     */   public void setRemoteHost(String remoteHost) {
/* 203 */     this.remoteHost = remoteHost;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/* 207 */     this.port = port;
/*     */   }
/*     */   
/*     */   public void setReconnectionDelay(int reconnectionDelay) {
/* 211 */     this.reconnectionDelay = reconnectionDelay;
/*     */   }
/*     */   
/*     */   public void setAcceptConnectionTimeout(int acceptConnectionTimeout) {
/* 215 */     this.acceptConnectionTimeout = acceptConnectionTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\SocketReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */