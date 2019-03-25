/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NioChannel
/*     */   implements ByteChannel
/*     */ {
/*  38 */   protected static final StringManager sm = StringManager.getManager(NioChannel.class);
/*     */   
/*  40 */   protected static final ByteBuffer emptyBuf = ByteBuffer.allocate(0);
/*     */   
/*  42 */   protected SocketChannel sc = null;
/*  43 */   protected SocketWrapperBase<NioChannel> socketWrapper = null;
/*     */   protected final SocketBufferHandler bufHandler;
/*     */   protected NioEndpoint.Poller poller;
/*     */   private ApplicationBufferHandler appReadBufHandler;
/*     */   
/*     */   public NioChannel(SocketChannel channel, SocketBufferHandler bufHandler)
/*     */   {
/*  50 */     this.sc = channel;
/*  51 */     this.bufHandler = bufHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/*  60 */     this.bufHandler.reset();
/*     */   }
/*     */   
/*     */   void setSocketWrapper(SocketWrapperBase<NioChannel> socketWrapper)
/*     */   {
/*  65 */     this.socketWrapper = socketWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void free()
/*     */   {
/*  72 */     this.bufHandler.free();
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
/*     */   public boolean flush(boolean block, Selector s, long timeout)
/*     */     throws IOException
/*     */   {
/*  88 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  99 */     getIOChannel().socket().close();
/* 100 */     getIOChannel().close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close(boolean force)
/*     */     throws IOException
/*     */   {
/* 111 */     if ((isOpen()) || (force)) { close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpen()
/*     */   {
/* 121 */     return this.sc.isOpen();
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
/* 133 */     checkInterruptStatus();
/* 134 */     return this.sc.write(src);
/*     */   }
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
/* 147 */     return this.sc.read(dst);
/*     */   }
/*     */   
/*     */   public Object getAttachment() {
/* 151 */     NioEndpoint.Poller pol = getPoller();
/* 152 */     Selector sel = pol != null ? pol.getSelector() : null;
/* 153 */     SelectionKey key = sel != null ? getIOChannel().keyFor(sel) : null;
/* 154 */     Object att = key != null ? key.attachment() : null;
/* 155 */     return att;
/*     */   }
/*     */   
/*     */   public SocketBufferHandler getBufHandler() {
/* 159 */     return this.bufHandler;
/*     */   }
/*     */   
/*     */   public NioEndpoint.Poller getPoller() {
/* 163 */     return this.poller;
/*     */   }
/*     */   
/*     */   public SocketChannel getIOChannel() {
/* 167 */     return this.sc;
/*     */   }
/*     */   
/*     */   public boolean isClosing() {
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isHandshakeComplete() {
/* 175 */     return true;
/*     */   }
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
/* 188 */     return 0;
/*     */   }
/*     */   
/*     */   public void setPoller(NioEndpoint.Poller poller) {
/* 192 */     this.poller = poller;
/*     */   }
/*     */   
/*     */   public void setIOChannel(SocketChannel IOChannel) {
/* 196 */     this.sc = IOChannel;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 201 */     return super.toString() + ":" + this.sc.toString();
/*     */   }
/*     */   
/*     */   public int getOutboundRemaining() {
/* 205 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean flushOutbound()
/*     */     throws IOException
/*     */   {
/* 216 */     return false;
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
/*     */   protected void checkInterruptStatus()
/*     */     throws IOException
/*     */   {
/* 231 */     if (Thread.interrupted()) {
/* 232 */       throw new IOException(sm.getString("channel.nio.interrupted"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAppReadBufHandler(ApplicationBufferHandler handler)
/*     */   {
/* 239 */     this.appReadBufHandler = handler;
/*     */   }
/*     */   
/* 242 */   protected ApplicationBufferHandler getAppReadBufHandler() { return this.appReadBufHandler; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\NioChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */