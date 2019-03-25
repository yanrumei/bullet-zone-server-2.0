/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.websocket.Transformation;
/*     */ import org.apache.tomcat.websocket.WsFrameBase;
/*     */ import org.apache.tomcat.websocket.WsFrameBase.ReadState;
/*     */ import org.apache.tomcat.websocket.WsIOException;
/*     */ import org.apache.tomcat.websocket.WsSession;
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
/*     */ public class WsFrameServer
/*     */   extends WsFrameBase
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(WsFrameServer.class);
/*  37 */   private static final StringManager sm = StringManager.getManager(WsFrameServer.class);
/*     */   
/*     */   private final SocketWrapperBase<?> socketWrapper;
/*     */   
/*     */   private final ClassLoader applicationClassLoader;
/*     */   
/*     */   public WsFrameServer(SocketWrapperBase<?> socketWrapper, WsSession wsSession, Transformation transformation, ClassLoader applicationClassLoader)
/*     */   {
/*  45 */     super(wsSession, transformation);
/*  46 */     this.socketWrapper = socketWrapper;
/*  47 */     this.applicationClassLoader = applicationClassLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void onDataAvailable()
/*     */     throws IOException
/*     */   {
/*  58 */     if (log.isDebugEnabled()) {
/*  59 */       log.debug("wsFrameServer.onDataAvailable");
/*     */     }
/*  61 */     if ((isOpen()) && (this.inputBuffer.hasRemaining()) && (!isSuspended()))
/*     */     {
/*     */ 
/*     */ 
/*  65 */       processInputBuffer();
/*     */     }
/*     */     
/*  68 */     while ((isOpen()) && (!isSuspended()))
/*     */     {
/*  70 */       this.inputBuffer.mark();
/*  71 */       this.inputBuffer.position(this.inputBuffer.limit()).limit(this.inputBuffer.capacity());
/*  72 */       int read = this.socketWrapper.read(false, this.inputBuffer);
/*  73 */       this.inputBuffer.limit(this.inputBuffer.position()).reset();
/*  74 */       if (read < 0)
/*  75 */         throw new EOFException();
/*  76 */       if (read == 0) {
/*  77 */         return;
/*     */       }
/*  79 */       if (log.isDebugEnabled()) {
/*  80 */         log.debug(sm.getString("wsFrameServer.bytesRead", new Object[] { Integer.toString(read) }));
/*     */       }
/*  82 */       processInputBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isMasked()
/*     */   {
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Transformation getTransformation()
/*     */   {
/*  97 */     return super.getTransformation();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isOpen()
/*     */   {
/* 104 */     return super.isOpen();
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 110 */     return log;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void sendMessageText(boolean last)
/*     */     throws WsIOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 31	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 32	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   6: astore_2
/*     */     //   7: invokestatic 31	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   10: aload_0
/*     */     //   11: getfield 3	org/apache/tomcat/websocket/server/WsFrameServer:applicationClassLoader	Ljava/lang/ClassLoader;
/*     */     //   14: invokevirtual 33	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   17: aload_0
/*     */     //   18: iload_1
/*     */     //   19: invokespecial 34	org/apache/tomcat/websocket/WsFrameBase:sendMessageText	(Z)V
/*     */     //   22: invokestatic 31	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   25: aload_2
/*     */     //   26: invokevirtual 33	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   29: goto +13 -> 42
/*     */     //   32: astore_3
/*     */     //   33: invokestatic 31	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   36: aload_2
/*     */     //   37: invokevirtual 33	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   40: aload_3
/*     */     //   41: athrow
/*     */     //   42: return
/*     */     // Line number table:
/*     */     //   Java source line #116	-> byte code offset #0
/*     */     //   Java source line #118	-> byte code offset #7
/*     */     //   Java source line #119	-> byte code offset #17
/*     */     //   Java source line #121	-> byte code offset #22
/*     */     //   Java source line #122	-> byte code offset #29
/*     */     //   Java source line #121	-> byte code offset #32
/*     */     //   Java source line #123	-> byte code offset #42
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	43	0	this	WsFrameServer
/*     */     //   0	43	1	last	boolean
/*     */     //   6	31	2	cl	ClassLoader
/*     */     //   32	9	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	22	32	finally
/*     */   }
/*     */   
/*     */   protected void sendMessageBinary(ByteBuffer msg, boolean last)
/*     */     throws WsIOException
/*     */   {
/* 128 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 130 */       Thread.currentThread().setContextClassLoader(this.applicationClassLoader);
/* 131 */       super.sendMessageBinary(msg, last);
/*     */     } finally {
/* 133 */       Thread.currentThread().setContextClassLoader(cl);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void resumeProcessing()
/*     */   {
/* 140 */     this.socketWrapper.processSocket(SocketEvent.OPEN_READ, true);
/*     */   }
/*     */   
/*     */   AbstractEndpoint.Handler.SocketState notifyDataAvailable() throws IOException {
/* 144 */     while (isOpen()) {
/* 145 */       switch (getReadState()) {
/*     */       case WAITING: 
/* 147 */         if (changeReadState(WsFrameBase.ReadState.WAITING, WsFrameBase.ReadState.PROCESSING))
/*     */         {
/*     */           try
/*     */           {
/* 151 */             return doOnDataAvailable();
/*     */           } catch (IOException e) {
/* 153 */             changeReadState(WsFrameBase.ReadState.CLOSING);
/* 154 */             throw e;
/*     */           } }
/*     */         break;
/* 157 */       case SUSPENDING_WAIT:  if (changeReadState(WsFrameBase.ReadState.SUSPENDING_WAIT, WsFrameBase.ReadState.SUSPENDED))
/*     */         {
/*     */ 
/* 160 */           return AbstractEndpoint.Handler.SocketState.SUSPENDED; }
/*     */         break;
/*     */       default: 
/* 163 */         throw new IllegalStateException(sm.getString("wsFrameServer.illegalReadState", new Object[] {getReadState() }));
/*     */       }
/*     */       
/*     */     }
/* 167 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */   }
/*     */   
/*     */   private AbstractEndpoint.Handler.SocketState doOnDataAvailable() throws IOException {
/* 171 */     onDataAvailable();
/* 172 */     while (isOpen()) {
/* 173 */       switch (getReadState()) {
/*     */       case PROCESSING: 
/* 175 */         if (changeReadState(WsFrameBase.ReadState.PROCESSING, WsFrameBase.ReadState.WAITING))
/*     */         {
/*     */ 
/* 178 */           return AbstractEndpoint.Handler.SocketState.UPGRADED; }
/*     */         break;
/* 180 */       case SUSPENDING_PROCESS:  if (changeReadState(WsFrameBase.ReadState.SUSPENDING_PROCESS, WsFrameBase.ReadState.SUSPENDED))
/*     */         {
/*     */ 
/* 183 */           return AbstractEndpoint.Handler.SocketState.SUSPENDED; }
/*     */         break;
/*     */       default: 
/* 186 */         throw new IllegalStateException(sm.getString("wsFrameServer.illegalReadState", new Object[] {getReadState() }));
/*     */       }
/*     */       
/*     */     }
/* 190 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsFrameServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */