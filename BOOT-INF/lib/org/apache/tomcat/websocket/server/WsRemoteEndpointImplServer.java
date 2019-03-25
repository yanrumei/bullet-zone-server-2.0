/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.websocket.SendHandler;
/*     */ import javax.websocket.SendResult;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.Transformation;
/*     */ import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;
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
/*     */ public class WsRemoteEndpointImplServer
/*     */   extends WsRemoteEndpointImplBase
/*     */ {
/*  43 */   private static final StringManager sm = StringManager.getManager(WsRemoteEndpointImplServer.class);
/*  44 */   private static final Log log = LogFactory.getLog(WsRemoteEndpointImplServer.class);
/*     */   
/*     */   private final SocketWrapperBase<?> socketWrapper;
/*     */   private final WsWriteTimeout wsWriteTimeout;
/*  48 */   private volatile SendHandler handler = null;
/*  49 */   private volatile ByteBuffer[] buffers = null;
/*     */   
/*  51 */   private volatile long timeoutExpiry = -1L;
/*     */   private volatile boolean close;
/*     */   
/*     */   public WsRemoteEndpointImplServer(SocketWrapperBase<?> socketWrapper, WsServerContainer serverContainer)
/*     */   {
/*  56 */     this.socketWrapper = socketWrapper;
/*  57 */     this.wsWriteTimeout = serverContainer.getTimeout();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final boolean isMasked()
/*     */   {
/*  63 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doWrite(SendHandler handler, long blockingWriteTimeoutExpiry, ByteBuffer... buffers)
/*     */   {
/*  70 */     if (blockingWriteTimeoutExpiry == -1L) {
/*  71 */       this.handler = handler;
/*  72 */       this.buffers = buffers;
/*     */       
/*     */ 
/*  75 */       onWritePossible(true);
/*     */     }
/*     */     else {
/*     */       try {
/*  79 */         for (ByteBuffer buffer : buffers) {
/*  80 */           long timeout = blockingWriteTimeoutExpiry - System.currentTimeMillis();
/*  81 */           if (timeout <= 0L) {
/*  82 */             SendResult sr = new SendResult(new SocketTimeoutException());
/*  83 */             handler.onResult(sr);
/*  84 */             return;
/*     */           }
/*  86 */           this.socketWrapper.setWriteTimeout(timeout);
/*  87 */           this.socketWrapper.write(true, buffer);
/*     */         }
/*  89 */         long timeout = blockingWriteTimeoutExpiry - System.currentTimeMillis();
/*  90 */         if (timeout <= 0L) {
/*  91 */           SendResult sr = new SendResult(new SocketTimeoutException());
/*  92 */           handler.onResult(sr);
/*  93 */           return;
/*     */         }
/*  95 */         this.socketWrapper.setWriteTimeout(timeout);
/*  96 */         this.socketWrapper.flush(true);
/*  97 */         handler.onResult(SENDRESULT_OK);
/*     */       } catch (IOException e) {
/*  99 */         SendResult sr = new SendResult(e);
/* 100 */         handler.onResult(sr);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onWritePossible(boolean useDispatch)
/*     */   {
/* 107 */     ByteBuffer[] buffers = this.buffers;
/* 108 */     if (buffers == null)
/*     */     {
/*     */ 
/* 111 */       return;
/*     */     }
/* 113 */     boolean complete = false;
/*     */     try {
/* 115 */       this.socketWrapper.flush(false);
/*     */       
/* 117 */       while (this.socketWrapper.isReadyForWrite()) {
/* 118 */         complete = true;
/* 119 */         for (ByteBuffer buffer : buffers) {
/* 120 */           if (buffer.hasRemaining()) {
/* 121 */             complete = false;
/* 122 */             this.socketWrapper.write(false, buffer);
/* 123 */             break;
/*     */           }
/*     */         }
/* 126 */         if (complete) {
/* 127 */           this.socketWrapper.flush(false);
/* 128 */           complete = this.socketWrapper.isReadyForWrite();
/* 129 */           if (complete) {
/* 130 */             this.wsWriteTimeout.unregister(this);
/* 131 */             clearHandler(null, useDispatch);
/* 132 */             if (this.close) {
/* 133 */               close();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException|IllegalStateException e) {
/* 140 */       this.wsWriteTimeout.unregister(this);
/* 141 */       clearHandler(e, useDispatch);
/* 142 */       close();
/*     */     }
/*     */     
/* 145 */     if (!complete)
/*     */     {
/* 147 */       long timeout = getSendTimeout();
/* 148 */       if (timeout > 0L)
/*     */       {
/* 150 */         this.timeoutExpiry = (timeout + System.currentTimeMillis());
/* 151 */         this.wsWriteTimeout.register(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doClose()
/*     */   {
/* 159 */     if (this.handler != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 164 */       clearHandler(new EOFException(), true);
/*     */     }
/*     */     try {
/* 167 */       this.socketWrapper.close();
/*     */     } catch (IOException e) {
/* 169 */       if (log.isInfoEnabled()) {
/* 170 */         log.info(sm.getString("wsRemoteEndpointServer.closeFailed"), e);
/*     */       }
/*     */     }
/* 173 */     this.wsWriteTimeout.unregister(this);
/*     */   }
/*     */   
/*     */   protected long getTimeoutExpiry()
/*     */   {
/* 178 */     return this.timeoutExpiry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onTimeout(boolean useDispatch)
/*     */   {
/* 190 */     if (this.handler != null) {
/* 191 */       clearHandler(new SocketTimeoutException(), useDispatch);
/*     */     }
/* 193 */     close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setTransformation(Transformation transformation)
/*     */   {
/* 200 */     super.setTransformation(transformation);
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
/*     */   private void clearHandler(Throwable t, boolean useDispatch)
/*     */   {
/* 219 */     SendHandler sh = this.handler;
/* 220 */     this.handler = null;
/* 221 */     this.buffers = null;
/* 222 */     if (sh != null) {
/* 223 */       if (useDispatch) {
/* 224 */         OnResultRunnable r = new OnResultRunnable(sh, t, null);
/* 225 */         AbstractEndpoint<?> endpoint = this.socketWrapper.getEndpoint();
/* 226 */         Executor containerExecutor = endpoint.getExecutor();
/* 227 */         if ((endpoint.isRunning()) && (containerExecutor != null)) {
/* 228 */           containerExecutor.execute(r);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/* 237 */           r.run();
/*     */         }
/*     */       }
/* 240 */       else if (t == null) {
/* 241 */         sh.onResult(new SendResult());
/*     */       } else {
/* 243 */         sh.onResult(new SendResult(t));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OnResultRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final SendHandler sh;
/*     */     private final Throwable t;
/*     */     
/*     */     private OnResultRunnable(SendHandler sh, Throwable t)
/*     */     {
/* 256 */       this.sh = sh;
/* 257 */       this.t = t;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 262 */       if (this.t == null) {
/* 263 */         this.sh.onResult(new SendResult());
/*     */       } else {
/* 265 */         this.sh.onResult(new SendResult(this.t));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsRemoteEndpointImplServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */