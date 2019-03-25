/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.CancelledKeyException;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.collections.SynchronizedQueue;
/*     */ import org.apache.tomcat.util.collections.SynchronizedStack;
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
/*     */ public class NioBlockingSelector
/*     */ {
/*  42 */   private static final Log log = LogFactory.getLog(NioBlockingSelector.class);
/*     */   
/*  44 */   private static int threadCounter = 0;
/*     */   
/*  46 */   private final SynchronizedStack<KeyReference> keyReferenceStack = new SynchronizedStack();
/*     */   
/*     */ 
/*     */   protected Selector sharedSelector;
/*     */   
/*     */ 
/*     */   protected BlockPoller poller;
/*     */   
/*     */ 
/*     */   public void open(Selector selector)
/*     */   {
/*  57 */     this.sharedSelector = selector;
/*  58 */     this.poller = new BlockPoller();
/*  59 */     this.poller.selector = this.sharedSelector;
/*  60 */     this.poller.setDaemon(true);
/*  61 */     this.poller.setName("NioBlockingSelector.BlockPoller-" + ++threadCounter);
/*  62 */     this.poller.start();
/*     */   }
/*     */   
/*     */   public void close() {
/*  66 */     if (this.poller != null) {
/*  67 */       this.poller.disable();
/*  68 */       this.poller.interrupt();
/*  69 */       this.poller = null;
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
/*     */   public int write(ByteBuffer buf, NioChannel socket, long writeTimeout)
/*     */     throws IOException
/*     */   {
/*  87 */     SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
/*  88 */     if (key == null) throw new IOException("Key no longer registered");
/*  89 */     KeyReference reference = (KeyReference)this.keyReferenceStack.pop();
/*  90 */     if (reference == null) {
/*  91 */       reference = new KeyReference();
/*     */     }
/*  93 */     NioEndpoint.NioSocketWrapper att = (NioEndpoint.NioSocketWrapper)key.attachment();
/*  94 */     int written = 0;
/*  95 */     boolean timedout = false;
/*  96 */     int keycount = 1;
/*  97 */     long time = System.currentTimeMillis();
/*     */     try {
/*  99 */       while ((!timedout) && (buf.hasRemaining())) {
/* 100 */         if (keycount > 0) {
/* 101 */           int cnt = socket.write(buf);
/* 102 */           if (cnt == -1)
/* 103 */             throw new EOFException();
/* 104 */           written += cnt;
/* 105 */           if (cnt > 0) {
/* 106 */             time = System.currentTimeMillis();
/* 107 */             continue;
/*     */           }
/*     */         }
/*     */         try {
/* 111 */           if ((att.getWriteLatch() == null) || (att.getWriteLatch().getCount() == 0L)) att.startWriteLatch(1);
/* 112 */           this.poller.add(att, 4, reference);
/* 113 */           if (writeTimeout < 0L) {
/* 114 */             att.awaitWriteLatch(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
/*     */           } else {
/* 116 */             att.awaitWriteLatch(writeTimeout, TimeUnit.MILLISECONDS);
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/* 121 */         if ((att.getWriteLatch() != null) && (att.getWriteLatch().getCount() > 0L))
/*     */         {
/* 123 */           keycount = 0;
/*     */         }
/*     */         else {
/* 126 */           keycount = 1;
/* 127 */           att.resetWriteLatch();
/*     */         }
/*     */         
/* 130 */         if ((writeTimeout > 0L) && (keycount == 0))
/* 131 */           timedout = System.currentTimeMillis() - time >= writeTimeout;
/*     */       }
/* 133 */       if (timedout)
/* 134 */         throw new SocketTimeoutException();
/*     */     } finally {
/* 136 */       this.poller.remove(att, 4);
/* 137 */       if ((timedout) && (reference.key != null)) {
/* 138 */         this.poller.cancelKey(reference.key);
/*     */       }
/* 140 */       reference.key = null;
/* 141 */       this.keyReferenceStack.push(reference);
/*     */     }
/* 143 */     return written;
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
/*     */   public int read(ByteBuffer buf, NioChannel socket, long readTimeout)
/*     */     throws IOException
/*     */   {
/* 159 */     SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
/* 160 */     if (key == null) throw new IOException("Key no longer registered");
/* 161 */     KeyReference reference = (KeyReference)this.keyReferenceStack.pop();
/* 162 */     if (reference == null) {
/* 163 */       reference = new KeyReference();
/*     */     }
/* 165 */     NioEndpoint.NioSocketWrapper att = (NioEndpoint.NioSocketWrapper)key.attachment();
/* 166 */     int read = 0;
/* 167 */     boolean timedout = false;
/* 168 */     int keycount = 1;
/* 169 */     long time = System.currentTimeMillis();
/*     */     try {
/* 171 */       while (!timedout) {
/* 172 */         if (keycount > 0) {
/* 173 */           read = socket.read(buf);
/* 174 */           if (read != 0) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         try {
/* 179 */           if ((att.getReadLatch() == null) || (att.getReadLatch().getCount() == 0L)) att.startReadLatch(1);
/* 180 */           this.poller.add(att, 1, reference);
/* 181 */           if (readTimeout < 0L) {
/* 182 */             att.awaitReadLatch(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
/*     */           } else {
/* 184 */             att.awaitReadLatch(readTimeout, TimeUnit.MILLISECONDS);
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/* 189 */         if ((att.getReadLatch() != null) && (att.getReadLatch().getCount() > 0L))
/*     */         {
/* 191 */           keycount = 0;
/*     */         }
/*     */         else {
/* 194 */           keycount = 1;
/* 195 */           att.resetReadLatch();
/*     */         }
/* 197 */         if ((readTimeout >= 0L) && (keycount == 0))
/* 198 */           timedout = System.currentTimeMillis() - time >= readTimeout;
/*     */       }
/* 200 */       if (timedout)
/* 201 */         throw new SocketTimeoutException();
/*     */     } finally {
/* 203 */       this.poller.remove(att, 1);
/* 204 */       if ((timedout) && (reference.key != null)) {
/* 205 */         this.poller.cancelKey(reference.key);
/*     */       }
/* 207 */       reference.key = null;
/* 208 */       this.keyReferenceStack.push(reference);
/*     */     }
/* 210 */     return read;
/*     */   }
/*     */   
/*     */   protected static class BlockPoller extends Thread
/*     */   {
/* 215 */     protected volatile boolean run = true;
/* 216 */     protected Selector selector = null;
/* 217 */     protected final SynchronizedQueue<Runnable> events = new SynchronizedQueue();
/* 218 */     public void disable() { this.run = false;this.selector.wakeup(); }
/* 219 */     protected final AtomicInteger wakeupCounter = new AtomicInteger(0);
/*     */     
/*     */     public void cancelKey(SelectionKey key) {
/* 222 */       Runnable r = new RunnableCancel(key);
/* 223 */       this.events.offer(r);
/* 224 */       wakeup();
/*     */     }
/*     */     
/*     */     public void wakeup() {
/* 228 */       if (this.wakeupCounter.addAndGet(1) == 0) this.selector.wakeup();
/*     */     }
/*     */     
/*     */     public void cancel(SelectionKey sk, NioEndpoint.NioSocketWrapper key, int ops) {
/* 232 */       if (sk != null) {
/* 233 */         sk.cancel();
/* 234 */         sk.attach(null);
/* 235 */         if (4 == (ops & 0x4)) countDown(key.getWriteLatch());
/* 236 */         if (1 == (ops & 0x1)) countDown(key.getReadLatch());
/*     */       }
/*     */     }
/*     */     
/*     */     public void add(NioEndpoint.NioSocketWrapper key, int ops, NioBlockingSelector.KeyReference ref) {
/* 241 */       if (key == null) return;
/* 242 */       NioChannel nch = (NioChannel)key.getSocket();
/* 243 */       SocketChannel ch = nch.getIOChannel();
/* 244 */       if (ch == null) { return;
/*     */       }
/* 246 */       Runnable r = new RunnableAdd(ch, key, ops, ref);
/* 247 */       this.events.offer(r);
/* 248 */       wakeup();
/*     */     }
/*     */     
/*     */     public void remove(NioEndpoint.NioSocketWrapper key, int ops) {
/* 252 */       if (key == null) return;
/* 253 */       NioChannel nch = (NioChannel)key.getSocket();
/* 254 */       SocketChannel ch = nch.getIOChannel();
/* 255 */       if (ch == null) { return;
/*     */       }
/* 257 */       Runnable r = new RunnableRemove(ch, key, ops);
/* 258 */       this.events.offer(r);
/* 259 */       wakeup();
/*     */     }
/*     */     
/*     */     public boolean events() {
/* 263 */       Runnable r = null;
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
/* 278 */       int size = this.events.size();
/* 279 */       for (int i = 0; (i < size) && ((r = (Runnable)this.events.poll()) != null); i++) {
/* 280 */         r.run();
/*     */       }
/*     */       
/* 283 */       return size > 0;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 288 */       while (this.run) {
/*     */         try {
/* 290 */           events();
/* 291 */           keyCount = 0;
/*     */           try {
/* 293 */             int i = this.wakeupCounter.get();
/* 294 */             if (i > 0) {
/* 295 */               keyCount = this.selector.selectNow();
/*     */             } else {
/* 297 */               this.wakeupCounter.set(-1);
/* 298 */               keyCount = this.selector.select(1000L);
/*     */             }
/* 300 */             this.wakeupCounter.set(0);
/* 301 */             if (!this.run)
/*     */               break;
/*     */           } catch (NullPointerException x) {
/* 304 */             if (this.selector == null) throw x;
/* 305 */             if (NioBlockingSelector.log.isDebugEnabled()) NioBlockingSelector.log.debug("Possibly encountered sun bug 5076772 on windows JDK 1.5", x);
/* 306 */             continue;
/*     */           }
/*     */           catch (CancelledKeyException x) {
/* 309 */             if (NioBlockingSelector.log.isDebugEnabled()) NioBlockingSelector.log.debug("Possibly encountered sun bug 5076772 on windows JDK 1.5", x);
/* 310 */             continue;
/*     */           } catch (Throwable x) {
/* 312 */             ExceptionUtils.handleThrowable(x);
/* 313 */             NioBlockingSelector.log.error("", x);
/*     */           }
/*     */         }
/*     */         catch (Throwable t)
/*     */         {
/*     */           int keyCount;
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
/*     */           Iterator<SelectionKey> iterator;
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
/* 340 */           NioBlockingSelector.log.error("", t);
/*     */         }
/* 317 */         iterator = keyCount > 0 ? this.selector.selectedKeys().iterator() : null;
/*     */         
/*     */ 
/*     */ 
/* 321 */         while ((this.run) && (iterator != null) && (iterator.hasNext())) {
/* 322 */           SelectionKey sk = (SelectionKey)iterator.next();
/* 323 */           NioEndpoint.NioSocketWrapper attachment = (NioEndpoint.NioSocketWrapper)sk.attachment();
/*     */           try {
/* 325 */             iterator.remove();
/* 326 */             sk.interestOps(sk.interestOps() & (sk.readyOps() ^ 0xFFFFFFFF));
/* 327 */             if (sk.isReadable()) {
/* 328 */               countDown(attachment.getReadLatch());
/*     */             }
/* 330 */             if (sk.isWritable()) {
/* 331 */               countDown(attachment.getWriteLatch());
/*     */             }
/*     */           } catch (CancelledKeyException ckx) {
/* 334 */             sk.cancel();
/* 335 */             countDown(attachment.getReadLatch());
/* 336 */             countDown(attachment.getWriteLatch());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 343 */       this.events.clear();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 348 */       if (this.selector.isOpen()) {
/*     */         try
/*     */         {
/* 351 */           this.selector.selectNow();
/*     */         } catch (Exception ignore) {
/* 353 */           if (NioBlockingSelector.log.isDebugEnabled()) NioBlockingSelector.log.debug("", ignore);
/*     */         }
/*     */       }
/*     */       try {
/* 357 */         this.selector.close();
/*     */       } catch (Exception ignore) {
/* 359 */         if (NioBlockingSelector.log.isDebugEnabled()) NioBlockingSelector.log.debug("", ignore);
/*     */       }
/*     */     }
/*     */     
/*     */     public void countDown(CountDownLatch latch) {
/* 364 */       if (latch == null) return;
/* 365 */       latch.countDown();
/*     */     }
/*     */     
/*     */     private class RunnableAdd implements Runnable
/*     */     {
/*     */       private final SocketChannel ch;
/*     */       private final NioEndpoint.NioSocketWrapper key;
/*     */       private final int ops;
/*     */       private final NioBlockingSelector.KeyReference ref;
/*     */       
/*     */       public RunnableAdd(SocketChannel ch, NioEndpoint.NioSocketWrapper key, int ops, NioBlockingSelector.KeyReference ref)
/*     */       {
/* 377 */         this.ch = ch;
/* 378 */         this.key = key;
/* 379 */         this.ops = ops;
/* 380 */         this.ref = ref;
/*     */       }
/*     */       
/*     */       public void run()
/*     */       {
/* 385 */         SelectionKey sk = this.ch.keyFor(NioBlockingSelector.BlockPoller.this.selector);
/*     */         try {
/* 387 */           if (sk == null) {
/* 388 */             sk = this.ch.register(NioBlockingSelector.BlockPoller.this.selector, this.ops, this.key);
/* 389 */             this.ref.key = sk;
/* 390 */           } else if (!sk.isValid()) {
/* 391 */             NioBlockingSelector.BlockPoller.this.cancel(sk, this.key, this.ops);
/*     */           } else {
/* 393 */             sk.interestOps(sk.interestOps() | this.ops);
/*     */           }
/*     */         } catch (CancelledKeyException cx) {
/* 396 */           NioBlockingSelector.BlockPoller.this.cancel(sk, this.key, this.ops);
/*     */         } catch (ClosedChannelException cx) {
/* 398 */           NioBlockingSelector.BlockPoller.this.cancel(null, this.key, this.ops);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private class RunnableRemove implements Runnable
/*     */     {
/*     */       private final SocketChannel ch;
/*     */       private final NioEndpoint.NioSocketWrapper key;
/*     */       private final int ops;
/*     */       
/*     */       public RunnableRemove(SocketChannel ch, NioEndpoint.NioSocketWrapper key, int ops)
/*     */       {
/* 411 */         this.ch = ch;
/* 412 */         this.key = key;
/* 413 */         this.ops = ops;
/*     */       }
/*     */       
/*     */       public void run()
/*     */       {
/* 418 */         SelectionKey sk = this.ch.keyFor(NioBlockingSelector.BlockPoller.this.selector);
/*     */         try {
/* 420 */           if (sk == null) {
/* 421 */             if (4 == (this.ops & 0x4)) NioBlockingSelector.BlockPoller.this.countDown(this.key.getWriteLatch());
/* 422 */             if (1 == (this.ops & 0x1)) NioBlockingSelector.BlockPoller.this.countDown(this.key.getReadLatch());
/*     */           }
/* 424 */           else if (sk.isValid()) {
/* 425 */             sk.interestOps(sk.interestOps() & (this.ops ^ 0xFFFFFFFF));
/* 426 */             if (4 == (this.ops & 0x4)) NioBlockingSelector.BlockPoller.this.countDown(this.key.getWriteLatch());
/* 427 */             if (1 == (this.ops & 0x1)) NioBlockingSelector.BlockPoller.this.countDown(this.key.getReadLatch());
/* 428 */             if (sk.interestOps() == 0) {
/* 429 */               sk.cancel();
/* 430 */               sk.attach(null);
/*     */             }
/*     */           } else {
/* 433 */             sk.cancel();
/* 434 */             sk.attach(null);
/*     */           }
/*     */         }
/*     */         catch (CancelledKeyException cx) {
/* 438 */           if (sk != null) {
/* 439 */             sk.cancel();
/* 440 */             sk.attach(null);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public static class RunnableCancel
/*     */       implements Runnable
/*     */     {
/*     */       private final SelectionKey key;
/*     */       
/*     */       public RunnableCancel(SelectionKey key)
/*     */       {
/* 453 */         this.key = key;
/*     */       }
/*     */       
/*     */       public void run()
/*     */       {
/* 458 */         this.key.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class KeyReference
/*     */   {
/* 465 */     SelectionKey key = null;
/*     */     
/*     */     public void finalize()
/*     */     {
/* 469 */       if ((this.key != null) && (this.key.isValid())) {
/* 470 */         NioBlockingSelector.log.warn("Possible key leak, cancelling key in the finalizer.");
/* 471 */         try { this.key.cancel();
/*     */         }
/*     */         catch (Exception localException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\NioBlockingSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */