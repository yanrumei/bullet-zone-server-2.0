/*      */ package org.apache.tomcat.util.net;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.CompletionHandler;
/*      */ import java.util.Iterator;
/*      */ import java.util.concurrent.LinkedBlockingDeque;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.ByteBufferHolder;
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
/*      */ public abstract class SocketWrapperBase<E>
/*      */ {
/*   36 */   private static final Log log = LogFactory.getLog(SocketWrapperBase.class);
/*      */   
/*   38 */   protected static final StringManager sm = StringManager.getManager(SocketWrapperBase.class);
/*      */   
/*      */ 
/*      */   private final E socket;
/*      */   
/*      */   private final AbstractEndpoint<E> endpoint;
/*      */   
/*   45 */   private volatile long readTimeout = -1L;
/*   46 */   private volatile long writeTimeout = -1L;
/*      */   
/*   48 */   private volatile int keepAliveLeft = 100;
/*   49 */   private volatile boolean upgraded = false;
/*   50 */   private boolean secure = false;
/*   51 */   private String negotiatedProtocol = null;
/*      */   
/*      */ 
/*      */ 
/*   55 */   protected String localAddr = null;
/*   56 */   protected String localName = null;
/*   57 */   protected int localPort = -1;
/*   58 */   protected String remoteAddr = null;
/*   59 */   protected String remoteHost = null;
/*   60 */   protected int remotePort = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   65 */   private volatile boolean blockingStatus = true;
/*      */   
/*      */ 
/*      */   private final Lock blockingStatusReadLock;
/*      */   
/*      */ 
/*      */   private final ReentrantReadWriteLock.WriteLock blockingStatusWriteLock;
/*      */   
/*   73 */   private volatile IOException error = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   78 */   protected volatile SocketBufferHandler socketBufferHandler = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   86 */   protected final LinkedBlockingDeque<ByteBufferHolder> bufferedWrites = new LinkedBlockingDeque();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   91 */   protected int bufferedWriteSize = 65536;
/*      */   
/*      */   public SocketWrapperBase(E socket, AbstractEndpoint<E> endpoint) {
/*   94 */     this.socket = socket;
/*   95 */     this.endpoint = endpoint;
/*   96 */     ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*   97 */     this.blockingStatusReadLock = lock.readLock();
/*   98 */     this.blockingStatusWriteLock = lock.writeLock();
/*      */   }
/*      */   
/*      */   public E getSocket() {
/*  102 */     return (E)this.socket;
/*      */   }
/*      */   
/*      */   public AbstractEndpoint<E> getEndpoint() {
/*  106 */     return this.endpoint;
/*      */   }
/*      */   
/*  109 */   public IOException getError() { return this.error; }
/*      */   
/*      */   public void setError(IOException error)
/*      */   {
/*  113 */     if (this.error != null) {
/*  114 */       return;
/*      */     }
/*  116 */     this.error = error;
/*      */   }
/*      */   
/*  119 */   public void checkError() throws IOException { if (this.error != null) {
/*  120 */       throw this.error;
/*      */     }
/*      */   }
/*      */   
/*  124 */   public boolean isUpgraded() { return this.upgraded; }
/*  125 */   public void setUpgraded(boolean upgraded) { this.upgraded = upgraded; }
/*  126 */   public boolean isSecure() { return this.secure; }
/*  127 */   public void setSecure(boolean secure) { this.secure = secure; }
/*  128 */   public String getNegotiatedProtocol() { return this.negotiatedProtocol; }
/*      */   
/*  130 */   public void setNegotiatedProtocol(String negotiatedProtocol) { this.negotiatedProtocol = negotiatedProtocol; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadTimeout(long readTimeout)
/*      */   {
/*  141 */     if (readTimeout > 0L) {
/*  142 */       this.readTimeout = readTimeout;
/*      */     } else {
/*  144 */       this.readTimeout = -1L;
/*      */     }
/*      */   }
/*      */   
/*      */   public long getReadTimeout() {
/*  149 */     return this.readTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWriteTimeout(long writeTimeout)
/*      */   {
/*  160 */     if (writeTimeout > 0L) {
/*  161 */       this.writeTimeout = writeTimeout;
/*      */     } else {
/*  163 */       this.writeTimeout = -1L;
/*      */     }
/*      */   }
/*      */   
/*      */   public long getWriteTimeout() {
/*  168 */     return this.writeTimeout;
/*      */   }
/*      */   
/*      */ 
/*  172 */   public void setKeepAliveLeft(int keepAliveLeft) { this.keepAliveLeft = keepAliveLeft; }
/*  173 */   public int decrementKeepAlive() { return --this.keepAliveLeft; }
/*      */   
/*      */   public String getRemoteHost() {
/*  176 */     if (this.remoteHost == null) {
/*  177 */       populateRemoteHost();
/*      */     }
/*  179 */     return this.remoteHost;
/*      */   }
/*      */   
/*      */   protected abstract void populateRemoteHost();
/*      */   
/*  184 */   public String getRemoteAddr() { if (this.remoteAddr == null) {
/*  185 */       populateRemoteAddr();
/*      */     }
/*  187 */     return this.remoteAddr;
/*      */   }
/*      */   
/*      */   protected abstract void populateRemoteAddr();
/*      */   
/*  192 */   public int getRemotePort() { if (this.remotePort == -1) {
/*  193 */       populateRemotePort();
/*      */     }
/*  195 */     return this.remotePort;
/*      */   }
/*      */   
/*      */   protected abstract void populateRemotePort();
/*      */   
/*  200 */   public String getLocalName() { if (this.localName == null) {
/*  201 */       populateLocalName();
/*      */     }
/*  203 */     return this.localName;
/*      */   }
/*      */   
/*      */   protected abstract void populateLocalName();
/*      */   
/*  208 */   public String getLocalAddr() { if (this.localAddr == null) {
/*  209 */       populateLocalAddr();
/*      */     }
/*  211 */     return this.localAddr;
/*      */   }
/*      */   
/*      */   protected abstract void populateLocalAddr();
/*      */   
/*  216 */   public int getLocalPort() { if (this.localPort == -1) {
/*  217 */       populateLocalPort();
/*      */     }
/*  219 */     return this.localPort; }
/*      */   
/*      */   protected abstract void populateLocalPort();
/*      */   
/*  223 */   public boolean getBlockingStatus() { return this.blockingStatus; }
/*      */   
/*  225 */   public void setBlockingStatus(boolean blockingStatus) { this.blockingStatus = blockingStatus; }
/*      */   
/*  227 */   public Lock getBlockingStatusReadLock() { return this.blockingStatusReadLock; }
/*      */   
/*  229 */   public ReentrantReadWriteLock.WriteLock getBlockingStatusWriteLock() { return this.blockingStatusWriteLock; }
/*      */   
/*  231 */   public SocketBufferHandler getSocketBufferHandler() { return this.socketBufferHandler; }
/*      */   
/*      */   public boolean hasDataToWrite() {
/*  234 */     return (!this.socketBufferHandler.isWriteBufferEmpty()) || (this.bufferedWrites.size() > 0);
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
/*      */   public boolean isReadyForWrite()
/*      */   {
/*  252 */     boolean result = canWrite();
/*  253 */     if (!result) {
/*  254 */       registerWriteInterest();
/*      */     }
/*  256 */     return result;
/*      */   }
/*      */   
/*      */   public boolean canWrite()
/*      */   {
/*  261 */     if (this.socketBufferHandler == null) {
/*  262 */       throw new IllegalStateException(sm.getString("socket.closed"));
/*      */     }
/*  264 */     return (this.socketBufferHandler.isWriteBufferWritable()) && (this.bufferedWrites.size() == 0);
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
/*  276 */   public String toString() { return super.toString() + ":" + String.valueOf(this.socket); }
/*      */   
/*      */   public abstract int read(boolean paramBoolean, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
/*      */   
/*      */   public abstract int read(boolean paramBoolean, ByteBuffer paramByteBuffer) throws IOException;
/*      */   
/*      */   public abstract boolean isReadyForRead() throws IOException;
/*      */   
/*      */   public abstract void setAppReadBufHandler(ApplicationBufferHandler paramApplicationBufferHandler);
/*      */   
/*  286 */   protected int populateReadBuffer(byte[] b, int off, int len) { this.socketBufferHandler.configureReadBufferForRead();
/*  287 */     ByteBuffer readBuffer = this.socketBufferHandler.getReadBuffer();
/*  288 */     int remaining = readBuffer.remaining();
/*      */     
/*      */ 
/*      */ 
/*  292 */     if (remaining > 0) {
/*  293 */       remaining = Math.min(remaining, len);
/*  294 */       readBuffer.get(b, off, remaining);
/*      */       
/*  296 */       if (log.isDebugEnabled()) {
/*  297 */         log.debug("Socket: [" + this + "], Read from buffer: [" + remaining + "]");
/*      */       }
/*      */     }
/*  300 */     return remaining;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int populateReadBuffer(ByteBuffer to)
/*      */   {
/*  307 */     this.socketBufferHandler.configureReadBufferForRead();
/*  308 */     int nRead = transfer(this.socketBufferHandler.getReadBuffer(), to);
/*      */     
/*  310 */     if (log.isDebugEnabled()) {
/*  311 */       log.debug("Socket: [" + this + "], Read from buffer: [" + nRead + "]");
/*      */     }
/*  313 */     return nRead;
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
/*      */   public void unRead(ByteBuffer returnedInput)
/*      */   {
/*  329 */     if (returnedInput != null) {
/*  330 */       this.socketBufferHandler.configureReadBufferForWrite();
/*  331 */       this.socketBufferHandler.getReadBuffer().put(returnedInput);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void close()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean isClosed();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void write(boolean block, byte[] buf, int off, int len)
/*      */     throws IOException
/*      */   {
/*  352 */     if ((len == 0) || (buf == null)) {
/*  353 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  360 */     if (block) {
/*  361 */       writeBlocking(buf, off, len);
/*      */     } else {
/*  363 */       writeNonBlocking(buf, off, len);
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
/*      */ 
/*      */ 
/*      */   public final void write(boolean block, ByteBuffer from)
/*      */     throws IOException
/*      */   {
/*  379 */     if ((from == null) || (from.remaining() == 0)) {
/*  380 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  387 */     if (block) {
/*  388 */       writeBlocking(from);
/*      */     } else {
/*  390 */       writeNonBlocking(from);
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
/*      */   protected void writeBlocking(byte[] buf, int off, int len)
/*      */     throws IOException
/*      */   {
/*  414 */     this.socketBufferHandler.configureWriteBufferForWrite();
/*  415 */     int thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
/*  416 */     while (this.socketBufferHandler.getWriteBuffer().remaining() == 0) {
/*  417 */       len -= thisTime;
/*  418 */       off += thisTime;
/*  419 */       doWrite(true);
/*  420 */       this.socketBufferHandler.configureWriteBufferForWrite();
/*  421 */       thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeBlocking(ByteBuffer from)
/*      */     throws IOException
/*      */   {
/*  444 */     if (this.socketBufferHandler.isWriteBufferEmpty()) {
/*  445 */       writeByteBufferBlocking(from);
/*      */     } else {
/*  447 */       this.socketBufferHandler.configureWriteBufferForWrite();
/*  448 */       transfer(from, this.socketBufferHandler.getWriteBuffer());
/*  449 */       if (!this.socketBufferHandler.isWriteBufferWritable()) {
/*  450 */         doWrite(true);
/*  451 */         writeByteBufferBlocking(from);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void writeByteBufferBlocking(ByteBuffer from)
/*      */     throws IOException
/*      */   {
/*  459 */     int limit = this.socketBufferHandler.getWriteBuffer().capacity();
/*  460 */     int fromLimit = from.limit();
/*  461 */     while (from.remaining() >= limit) {
/*  462 */       from.limit(from.position() + limit);
/*  463 */       doWrite(true, from);
/*  464 */       from.limit(fromLimit);
/*      */     }
/*      */     
/*  467 */     if (from.remaining() > 0) {
/*  468 */       this.socketBufferHandler.configureWriteBufferForWrite();
/*  469 */       transfer(from, this.socketBufferHandler.getWriteBuffer());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeNonBlocking(byte[] buf, int off, int len)
/*      */     throws IOException
/*      */   {
/*  487 */     if ((this.bufferedWrites.size() == 0) && (this.socketBufferHandler.isWriteBufferWritable())) {
/*  488 */       this.socketBufferHandler.configureWriteBufferForWrite();
/*  489 */       int thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
/*  490 */       len -= thisTime;
/*  491 */       while (!this.socketBufferHandler.isWriteBufferWritable()) {
/*  492 */         off += thisTime;
/*  493 */         doWrite(false);
/*  494 */         if ((len <= 0) || (!this.socketBufferHandler.isWriteBufferWritable())) break;
/*  495 */         this.socketBufferHandler.configureWriteBufferForWrite();
/*  496 */         thisTime = transfer(buf, off, len, this.socketBufferHandler.getWriteBuffer());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  503 */         len -= thisTime;
/*      */       }
/*      */     }
/*      */     
/*  507 */     if (len > 0)
/*      */     {
/*  509 */       addToBuffers(buf, off, len);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void writeNonBlocking(ByteBuffer from)
/*      */     throws IOException
/*      */   {
/*  526 */     if ((this.bufferedWrites.size() == 0) && (this.socketBufferHandler.isWriteBufferWritable())) {
/*  527 */       writeNonBlockingInternal(from);
/*      */     }
/*      */     
/*  530 */     if (from.remaining() > 0)
/*      */     {
/*  532 */       addToBuffers(from);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean writeNonBlockingInternal(ByteBuffer from) throws IOException
/*      */   {
/*  538 */     if (this.socketBufferHandler.isWriteBufferEmpty()) {
/*  539 */       return writeByteBufferNonBlocking(from);
/*      */     }
/*  541 */     this.socketBufferHandler.configureWriteBufferForWrite();
/*  542 */     transfer(from, this.socketBufferHandler.getWriteBuffer());
/*  543 */     if (!this.socketBufferHandler.isWriteBufferWritable()) {
/*  544 */       doWrite(false);
/*  545 */       if (this.socketBufferHandler.isWriteBufferWritable()) {
/*  546 */         return writeByteBufferNonBlocking(from);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  551 */     return !this.socketBufferHandler.isWriteBufferWritable();
/*      */   }
/*      */   
/*      */   protected boolean writeByteBufferNonBlocking(ByteBuffer from)
/*      */     throws IOException
/*      */   {
/*  557 */     int limit = this.socketBufferHandler.getWriteBuffer().capacity();
/*  558 */     int fromLimit = from.limit();
/*  559 */     while (from.remaining() >= limit) {
/*  560 */       int newLimit = from.position() + limit;
/*  561 */       from.limit(newLimit);
/*  562 */       doWrite(false, from);
/*  563 */       from.limit(fromLimit);
/*  564 */       if (from.position() != newLimit)
/*      */       {
/*      */ 
/*      */ 
/*  568 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  572 */     if (from.remaining() > 0) {
/*  573 */       this.socketBufferHandler.configureWriteBufferForWrite();
/*  574 */       transfer(from, this.socketBufferHandler.getWriteBuffer());
/*      */     }
/*      */     
/*  577 */     return false;
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
/*      */   public boolean flush(boolean block)
/*      */     throws IOException
/*      */   {
/*  594 */     boolean result = false;
/*  595 */     if (block)
/*      */     {
/*  597 */       flushBlocking();
/*      */     } else {
/*  599 */       result = flushNonBlocking();
/*      */     }
/*      */     
/*  602 */     return result;
/*      */   }
/*      */   
/*      */   protected void flushBlocking() throws IOException
/*      */   {
/*  607 */     doWrite(true);
/*      */     
/*  609 */     if (this.bufferedWrites.size() > 0) {
/*  610 */       Iterator<ByteBufferHolder> bufIter = this.bufferedWrites.iterator();
/*  611 */       while (bufIter.hasNext()) {
/*  612 */         ByteBufferHolder buffer = (ByteBufferHolder)bufIter.next();
/*  613 */         buffer.flip();
/*  614 */         writeBlocking(buffer.getBuf());
/*  615 */         if (buffer.getBuf().remaining() == 0) {
/*  616 */           bufIter.remove();
/*      */         }
/*      */       }
/*      */       
/*  620 */       if (!this.socketBufferHandler.isWriteBufferEmpty()) {
/*  621 */         doWrite(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean flushNonBlocking()
/*      */     throws IOException
/*      */   {
/*  629 */     boolean dataLeft = !this.socketBufferHandler.isWriteBufferEmpty();
/*      */     
/*      */ 
/*  632 */     if (dataLeft) {
/*  633 */       doWrite(false);
/*  634 */       dataLeft = !this.socketBufferHandler.isWriteBufferEmpty();
/*      */     }
/*      */     
/*  637 */     if ((!dataLeft) && (this.bufferedWrites.size() > 0)) {
/*  638 */       Iterator<ByteBufferHolder> bufIter = this.bufferedWrites.iterator();
/*  639 */       while ((!dataLeft) && (bufIter.hasNext())) {
/*  640 */         ByteBufferHolder buffer = (ByteBufferHolder)bufIter.next();
/*  641 */         buffer.flip();
/*  642 */         dataLeft = writeNonBlockingInternal(buffer.getBuf());
/*  643 */         if (buffer.getBuf().remaining() == 0) {
/*  644 */           bufIter.remove();
/*      */         }
/*      */       }
/*      */       
/*  648 */       if ((!dataLeft) && (!this.socketBufferHandler.isWriteBufferEmpty())) {
/*  649 */         doWrite(false);
/*  650 */         dataLeft = !this.socketBufferHandler.isWriteBufferEmpty();
/*      */       }
/*      */     }
/*      */     
/*  654 */     return dataLeft;
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
/*      */   protected void doWrite(boolean block)
/*      */     throws IOException
/*      */   {
/*  669 */     this.socketBufferHandler.configureWriteBufferForRead();
/*  670 */     doWrite(block, this.socketBufferHandler.getWriteBuffer());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void doWrite(boolean paramBoolean, ByteBuffer paramByteBuffer)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addToBuffers(byte[] buf, int offset, int length)
/*      */   {
/*  689 */     ByteBufferHolder holder = getByteBufferHolder(length);
/*  690 */     holder.getBuf().put(buf, offset, length);
/*      */   }
/*      */   
/*      */   protected void addToBuffers(ByteBuffer from)
/*      */   {
/*  695 */     ByteBufferHolder holder = getByteBufferHolder(from.remaining());
/*  696 */     holder.getBuf().put(from);
/*      */   }
/*      */   
/*      */   private ByteBufferHolder getByteBufferHolder(int capacity)
/*      */   {
/*  701 */     ByteBufferHolder holder = (ByteBufferHolder)this.bufferedWrites.peekLast();
/*  702 */     if ((holder == null) || (holder.isFlipped()) || (holder.getBuf().remaining() < capacity)) {
/*  703 */       ByteBuffer buffer = ByteBuffer.allocate(Math.max(this.bufferedWriteSize, capacity));
/*  704 */       holder = new ByteBufferHolder(buffer, false);
/*  705 */       this.bufferedWrites.add(holder);
/*      */     }
/*  707 */     return holder;
/*      */   }
/*      */   
/*      */   public void processSocket(SocketEvent socketStatus, boolean dispatch)
/*      */   {
/*  712 */     this.endpoint.processSocket(this, socketStatus, dispatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void registerReadInterest();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void registerWriteInterest();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract SendfileDataBase createSendfileData(String paramString, long paramLong1, long paramLong2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract SendfileState processSendfile(SendfileDataBase paramSendfileDataBase);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void doClientAuth(SSLSupport paramSSLSupport)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract SSLSupport getSslSupport(String paramString);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum CompletionState
/*      */   {
/*  758 */     PENDING, 
/*      */     
/*      */ 
/*      */ 
/*  762 */     INLINE, 
/*      */     
/*      */ 
/*      */ 
/*  766 */     ERROR, 
/*      */     
/*      */ 
/*      */ 
/*  770 */     DONE;
/*      */     
/*      */ 
/*      */     private CompletionState() {}
/*      */   }
/*      */   
/*      */   public static enum CompletionHandlerCall
/*      */   {
/*  778 */     CONTINUE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  783 */     NONE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  788 */     DONE;
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
/*      */     private CompletionHandlerCall() {}
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
/*  814 */   public static final CompletionCheck COMPLETE_WRITE = new CompletionCheck()
/*      */   {
/*      */     public SocketWrapperBase.CompletionHandlerCall callHandler(SocketWrapperBase.CompletionState state, ByteBuffer[] buffers, int offset, int length)
/*      */     {
/*  818 */       for (int i = 0; i < offset; i++) {
/*  819 */         if (buffers[i].remaining() > 0) {
/*  820 */           return SocketWrapperBase.CompletionHandlerCall.CONTINUE;
/*      */         }
/*      */       }
/*  823 */       return state == SocketWrapperBase.CompletionState.DONE ? SocketWrapperBase.CompletionHandlerCall.DONE : SocketWrapperBase.CompletionHandlerCall.NONE;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  833 */   public static final CompletionCheck READ_DATA = new CompletionCheck()
/*      */   {
/*      */     public SocketWrapperBase.CompletionHandlerCall callHandler(SocketWrapperBase.CompletionState state, ByteBuffer[] buffers, int offset, int length)
/*      */     {
/*  837 */       return state == SocketWrapperBase.CompletionState.DONE ? SocketWrapperBase.CompletionHandlerCall.DONE : SocketWrapperBase.CompletionHandlerCall.NONE;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasAsyncIO()
/*      */   {
/*  849 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReadPending()
/*      */   {
/*  858 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isWritePending()
/*      */   {
/*  867 */     return false;
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
/*      */   public boolean awaitReadComplete(long timeout, TimeUnit unit)
/*      */   {
/*  881 */     return true;
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
/*      */   public boolean awaitWriteComplete(long timeout, TimeUnit unit)
/*      */   {
/*  895 */     return true;
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
/*      */   public final <A> CompletionState read(boolean block, long timeout, TimeUnit unit, A attachment, CompletionCheck check, CompletionHandler<Long, ? super A> handler, ByteBuffer... dsts)
/*      */   {
/*  924 */     if (dsts == null) {
/*  925 */       throw new IllegalArgumentException();
/*      */     }
/*  927 */     return read(dsts, 0, dsts.length, block, timeout, unit, attachment, check, handler);
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
/*      */   public <A> CompletionState read(ByteBuffer[] dsts, int offset, int length, boolean block, long timeout, TimeUnit unit, A attachment, CompletionCheck check, CompletionHandler<Long, ? super A> handler)
/*      */   {
/*  959 */     throw new UnsupportedOperationException();
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
/*      */   public final <A> CompletionState write(boolean block, long timeout, TimeUnit unit, A attachment, CompletionCheck check, CompletionHandler<Long, ? super A> handler, ByteBuffer... srcs)
/*      */   {
/*  989 */     if (srcs == null) {
/*  990 */       throw new IllegalArgumentException();
/*      */     }
/*  992 */     return write(srcs, 0, srcs.length, block, timeout, unit, attachment, check, handler);
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
/*      */   public <A> CompletionState write(ByteBuffer[] srcs, int offset, int length, boolean block, long timeout, TimeUnit unit, A attachment, CompletionCheck check, CompletionHandler<Long, ? super A> handler)
/*      */   {
/* 1025 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static int transfer(byte[] from, int offset, int length, ByteBuffer to)
/*      */   {
/* 1032 */     int max = Math.min(length, to.remaining());
/* 1033 */     if (max > 0) {
/* 1034 */       to.put(from, offset, max);
/*      */     }
/* 1036 */     return max;
/*      */   }
/*      */   
/*      */   protected static int transfer(ByteBuffer from, ByteBuffer to) {
/* 1040 */     int max = Math.min(from.remaining(), to.remaining());
/* 1041 */     if (max > 0) {
/* 1042 */       int fromLimit = from.limit();
/* 1043 */       from.limit(from.position() + max);
/* 1044 */       to.put(from);
/* 1045 */       from.limit(fromLimit);
/*      */     }
/* 1047 */     return max;
/*      */   }
/*      */   
/*      */   public static abstract interface CompletionCheck
/*      */   {
/*      */     public abstract SocketWrapperBase.CompletionHandlerCall callHandler(SocketWrapperBase.CompletionState paramCompletionState, ByteBuffer[] paramArrayOfByteBuffer, int paramInt1, int paramInt2);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SocketWrapperBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */