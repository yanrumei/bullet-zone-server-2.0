/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import javax.websocket.CloseReason;
/*     */ import javax.websocket.CloseReason.CloseCodes;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class WsFrameClient
/*     */   extends WsFrameBase
/*     */ {
/*  33 */   private final Log log = LogFactory.getLog(WsFrameClient.class);
/*  34 */   private static final StringManager sm = StringManager.getManager(WsFrameClient.class);
/*     */   
/*     */   private final AsyncChannelWrapper channel;
/*     */   
/*     */   private final CompletionHandler<Integer, Void> handler;
/*     */   private volatile ByteBuffer response;
/*     */   
/*     */   public WsFrameClient(ByteBuffer response, AsyncChannelWrapper channel, WsSession wsSession, Transformation transformation)
/*     */   {
/*  43 */     super(wsSession, transformation);
/*  44 */     this.response = response;
/*  45 */     this.channel = channel;
/*  46 */     this.handler = new WsFrameClientCompletionHandler(null);
/*     */   }
/*     */   
/*     */   void startInputProcessing()
/*     */   {
/*     */     try {
/*  52 */       processSocketRead();
/*     */     } catch (IOException e) {
/*  54 */       close(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processSocketRead() throws IOException {
/*     */     do {
/*     */       do {
/*  61 */         switch (getReadState()) {
/*     */         }
/*  63 */       } while (!changeReadState(WsFrameBase.ReadState.WAITING, WsFrameBase.ReadState.PROCESSING));
/*     */       
/*     */ 
/*  66 */       while (this.response.hasRemaining())
/*  67 */         if (isSuspended()) {
/*  68 */           if (!changeReadState(WsFrameBase.ReadState.SUSPENDING_PROCESS, WsFrameBase.ReadState.SUSPENDED)) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  79 */           this.inputBuffer.mark();
/*  80 */           this.inputBuffer.position(this.inputBuffer.limit()).limit(this.inputBuffer.capacity());
/*     */           
/*  82 */           int toCopy = Math.min(this.response.remaining(), this.inputBuffer.remaining());
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*  87 */           int orgLimit = this.response.limit();
/*  88 */           this.response.limit(this.response.position() + toCopy);
/*  89 */           this.inputBuffer.put(this.response);
/*  90 */           this.response.limit(orgLimit);
/*     */           
/*  92 */           this.inputBuffer.limit(this.inputBuffer.position()).reset();
/*     */           
/*     */ 
/*  95 */           processInputBuffer();
/*     */         }
/*  97 */       this.response.clear();
/*     */       
/*     */ 
/* 100 */       if (isOpen()) {
/* 101 */         this.channel.read(this.response, null, this.handler);
/*     */       } else {
/* 103 */         changeReadState(WsFrameBase.ReadState.CLOSING);
/*     */       }
/* 105 */       return;
/*     */     }
/* 107 */     while (!changeReadState(WsFrameBase.ReadState.SUSPENDING_WAIT, WsFrameBase.ReadState.SUSPENDED));
/*     */     
/*     */ 
/* 110 */     return;
/*     */     
/*     */ 
/* 113 */     throw new IllegalStateException(sm.getString("wsFrameServer.illegalReadState", new Object[] {getReadState() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final void close(Throwable t)
/*     */   {
/* 120 */     changeReadState(WsFrameBase.ReadState.CLOSING);
/*     */     CloseReason cr;
/* 122 */     CloseReason cr; if ((t instanceof WsIOException)) {
/* 123 */       cr = ((WsIOException)t).getCloseReason();
/*     */     } else {
/* 125 */       cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, t.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 129 */       this.wsSession.close(cr);
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isMasked()
/*     */   {
/* 139 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 145 */     return this.log;
/*     */   }
/*     */   
/*     */   private class WsFrameClientCompletionHandler implements CompletionHandler<Integer, Void> {
/*     */     private WsFrameClientCompletionHandler() {}
/*     */     
/*     */     public void completed(Integer result, Void attachment) {
/* 152 */       if (result.intValue() == -1)
/*     */       {
/*     */ 
/* 155 */         if (WsFrameClient.this.isOpen())
/*     */         {
/* 157 */           WsFrameClient.this.close(new EOFException());
/*     */         }
/*     */         
/* 160 */         return;
/*     */       }
/* 162 */       WsFrameClient.this.response.flip();
/* 163 */       doResumeProcessing(true);
/*     */     }
/*     */     
/*     */     public void failed(Throwable exc, Void attachment)
/*     */     {
/* 168 */       if ((exc instanceof ReadBufferOverflowException))
/*     */       {
/* 170 */         WsFrameClient.this.response = 
/* 171 */           ByteBuffer.allocate(((ReadBufferOverflowException)exc).getMinBufferSize());
/* 172 */         WsFrameClient.this.response.flip();
/* 173 */         doResumeProcessing(false);
/*     */       } else {
/* 175 */         WsFrameClient.this.close(exc);
/*     */       }
/*     */     }
/*     */     
/*     */     private void doResumeProcessing(boolean checkOpenOnError) {
/*     */       do {
/* 181 */         do { switch (WsFrameClient.1.$SwitchMap$org$apache$tomcat$websocket$WsFrameBase$ReadState[WsFrameClient.this.getReadState().ordinal()]) {
/*     */           }
/* 183 */         } while (!WsFrameClient.this.changeReadState(WsFrameBase.ReadState.PROCESSING, WsFrameBase.ReadState.WAITING));
/*     */         
/*     */ 
/* 186 */         WsFrameClient.this.resumeProcessing(checkOpenOnError);
/* 187 */         return;
/*     */       }
/* 189 */       while (!WsFrameClient.this.changeReadState(WsFrameBase.ReadState.SUSPENDING_PROCESS, WsFrameBase.ReadState.SUSPENDED));
/*     */       
/*     */ 
/* 192 */       return;
/*     */       
/*     */ 
/* 195 */       throw new IllegalStateException(WsFrameClient.sm.getString("wsFrame.illegalReadState", new Object[] { WsFrameClient.this.getReadState() }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void resumeProcessing()
/*     */   {
/* 204 */     resumeProcessing(true);
/*     */   }
/*     */   
/*     */   private void resumeProcessing(boolean checkOpenOnError) {
/*     */     try {
/* 209 */       processSocketRead();
/*     */     } catch (IOException e) {
/* 211 */       if (checkOpenOnError)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */         if (isOpen()) {
/* 218 */           if (this.log.isDebugEnabled()) {
/* 219 */             this.log.debug(sm.getString("wsFrameClient.ioe"), e);
/*     */           }
/* 221 */           close(e);
/*     */         }
/*     */       } else {
/* 224 */         close(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsFrameClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */