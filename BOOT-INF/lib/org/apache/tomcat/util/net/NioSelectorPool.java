/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class NioSelectorPool
/*     */ {
/*  44 */   private static final Log log = LogFactory.getLog(NioSelectorPool.class);
/*     */   
/*     */ 
/*  47 */   protected static final boolean SHARED = Boolean.parseBoolean(System.getProperty("org.apache.tomcat.util.net.NioSelectorShared", "true"));
/*     */   
/*     */   protected NioBlockingSelector blockingSelector;
/*     */   
/*     */   protected volatile Selector SHARED_SELECTOR;
/*     */   
/*  53 */   protected int maxSelectors = 200;
/*  54 */   protected long sharedSelectorTimeout = 30000L;
/*  55 */   protected int maxSpareSelectors = -1;
/*  56 */   protected boolean enabled = true;
/*  57 */   protected AtomicInteger active = new AtomicInteger(0);
/*  58 */   protected AtomicInteger spare = new AtomicInteger(0);
/*  59 */   protected ConcurrentLinkedQueue<Selector> selectors = new ConcurrentLinkedQueue();
/*     */   
/*     */   protected Selector getSharedSelector() throws IOException
/*     */   {
/*  63 */     if ((SHARED) && (this.SHARED_SELECTOR == null)) {
/*  64 */       synchronized (NioSelectorPool.class) {
/*  65 */         if (this.SHARED_SELECTOR == null) {
/*  66 */           this.SHARED_SELECTOR = Selector.open();
/*  67 */           log.info("Using a shared selector for servlet write/read");
/*     */         }
/*     */       }
/*     */     }
/*  71 */     return this.SHARED_SELECTOR;
/*     */   }
/*     */   
/*     */   public Selector get() throws IOException {
/*  75 */     if (SHARED) {
/*  76 */       return getSharedSelector();
/*     */     }
/*  78 */     if ((!this.enabled) || (this.active.incrementAndGet() >= this.maxSelectors)) {
/*  79 */       if (this.enabled) this.active.decrementAndGet();
/*  80 */       return null;
/*     */     }
/*  82 */     Selector s = null;
/*     */     try {
/*  84 */       s = this.selectors.size() > 0 ? (Selector)this.selectors.poll() : null;
/*  85 */       if (s == null) {
/*  86 */         s = Selector.open();
/*     */       } else {
/*  88 */         this.spare.decrementAndGet();
/*     */       }
/*     */     } catch (NoSuchElementException x) {
/*     */       try {
/*  92 */         s = Selector.open();
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     } finally {
/*  96 */       if (s == null) this.active.decrementAndGet();
/*     */     }
/*  98 */     return s;
/*     */   }
/*     */   
/*     */   public void put(Selector s)
/*     */     throws IOException
/*     */   {
/* 104 */     if (SHARED) return;
/* 105 */     if (this.enabled) this.active.decrementAndGet();
/* 106 */     if ((this.enabled) && ((this.maxSpareSelectors == -1) || (this.spare.get() < Math.min(this.maxSpareSelectors, this.maxSelectors)))) {
/* 107 */       this.spare.incrementAndGet();
/* 108 */       this.selectors.offer(s);
/*     */     } else {
/* 110 */       s.close();
/*     */     }
/*     */   }
/*     */   
/* 114 */   public void close() throws IOException { this.enabled = false;
/*     */     Selector s;
/* 116 */     while ((s = (Selector)this.selectors.poll()) != null) s.close();
/* 117 */     this.spare.set(0);
/* 118 */     this.active.set(0);
/* 119 */     if (this.blockingSelector != null) {
/* 120 */       this.blockingSelector.close();
/*     */     }
/* 122 */     if ((SHARED) && (getSharedSelector() != null)) {
/* 123 */       getSharedSelector().close();
/* 124 */       this.SHARED_SELECTOR = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/* 129 */     this.enabled = true;
/* 130 */     getSharedSelector();
/* 131 */     if (SHARED) {
/* 132 */       this.blockingSelector = new NioBlockingSelector();
/* 133 */       this.blockingSelector.open(getSharedSelector());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int write(ByteBuffer buf, NioChannel socket, Selector selector, long writeTimeout, boolean block)
/*     */     throws IOException
/*     */   {
/* 156 */     if ((SHARED) && (block)) {
/* 157 */       return this.blockingSelector.write(buf, socket, writeTimeout);
/*     */     }
/* 159 */     SelectionKey key = null;
/* 160 */     int written = 0;
/* 161 */     boolean timedout = false;
/* 162 */     int keycount = 1;
/* 163 */     long time = System.currentTimeMillis();
/*     */     try {
/* 165 */       while ((!timedout) && (buf.hasRemaining())) {
/* 166 */         int cnt = 0;
/* 167 */         if (keycount > 0) {
/* 168 */           cnt = socket.write(buf);
/* 169 */           if (cnt == -1) { throw new EOFException();
/*     */           }
/* 171 */           written += cnt;
/* 172 */           if (cnt > 0) {
/* 173 */             time = System.currentTimeMillis();
/*     */           }
/*     */           else
/* 176 */             if ((cnt == 0) && (!block))
/*     */               break;
/* 178 */         } else { if (selector != null)
/*     */           {
/* 180 */             if (key == null) key = socket.getIOChannel().register(selector, 4); else
/* 181 */               key.interestOps(4);
/* 182 */             if (writeTimeout == 0L) {
/* 183 */               timedout = buf.hasRemaining();
/* 184 */             } else if (writeTimeout < 0L) {
/* 185 */               keycount = selector.select();
/*     */             } else {
/* 187 */               keycount = selector.select(writeTimeout);
/*     */             }
/*     */           }
/* 190 */           if ((writeTimeout > 0L) && ((selector == null) || (keycount == 0))) timedout = System.currentTimeMillis() - time >= writeTimeout;
/*     */         } }
/* 192 */       if (timedout) throw new SocketTimeoutException();
/*     */     } finally {
/* 194 */       if (key != null) {
/* 195 */         key.cancel();
/* 196 */         if (selector != null) selector.selectNow();
/*     */       }
/*     */     }
/* 199 */     return written;
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
/*     */   public int read(ByteBuffer buf, NioChannel socket, Selector selector, long readTimeout)
/*     */     throws IOException
/*     */   {
/* 216 */     return read(buf, socket, selector, readTimeout, true);
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
/*     */   public int read(ByteBuffer buf, NioChannel socket, Selector selector, long readTimeout, boolean block)
/*     */     throws IOException
/*     */   {
/* 234 */     if ((SHARED) && (block)) {
/* 235 */       return this.blockingSelector.read(buf, socket, readTimeout);
/*     */     }
/* 237 */     SelectionKey key = null;
/* 238 */     int read = 0;
/* 239 */     boolean timedout = false;
/* 240 */     int keycount = 1;
/* 241 */     long time = System.currentTimeMillis();
/*     */     try {
/* 243 */       while (!timedout) {
/* 244 */         int cnt = 0;
/* 245 */         if (keycount > 0) {
/* 246 */           cnt = socket.read(buf);
/* 247 */           if (cnt == -1) {
/* 248 */             if (read == 0) {
/* 249 */               read = -1;
/*     */             }
/*     */           }
/*     */           else {
/* 253 */             read += cnt;
/* 254 */             if (cnt > 0) continue;
/* 255 */             if ((cnt == 0) && ((read > 0) || (!block))) break;
/*     */           }
/* 257 */         } else { if (selector != null)
/*     */           {
/* 259 */             if (key == null) key = socket.getIOChannel().register(selector, 1); else
/* 260 */               key.interestOps(1);
/* 261 */             if (readTimeout == 0L) {
/* 262 */               timedout = read == 0;
/* 263 */             } else if (readTimeout < 0L) {
/* 264 */               keycount = selector.select();
/*     */             } else {
/* 266 */               keycount = selector.select(readTimeout);
/*     */             }
/*     */           }
/* 269 */           if ((readTimeout > 0L) && ((selector == null) || (keycount == 0))) timedout = System.currentTimeMillis() - time >= readTimeout;
/*     */         } }
/* 271 */       if (timedout) throw new SocketTimeoutException();
/*     */     } finally {
/* 273 */       if (key != null) {
/* 274 */         key.cancel();
/* 275 */         if (selector != null) selector.selectNow();
/*     */       }
/*     */     }
/* 278 */     return read;
/*     */   }
/*     */   
/*     */   public void setMaxSelectors(int maxSelectors) {
/* 282 */     this.maxSelectors = maxSelectors;
/*     */   }
/*     */   
/*     */   public void setMaxSpareSelectors(int maxSpareSelectors) {
/* 286 */     this.maxSpareSelectors = maxSpareSelectors;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 290 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public void setSharedSelectorTimeout(long sharedSelectorTimeout) {
/* 294 */     this.sharedSelectorTimeout = sharedSelectorTimeout;
/*     */   }
/*     */   
/*     */   public int getMaxSelectors() {
/* 298 */     return this.maxSelectors;
/*     */   }
/*     */   
/*     */   public int getMaxSpareSelectors() {
/* 302 */     return this.maxSpareSelectors;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 306 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public long getSharedSelectorTimeout() {
/* 310 */     return this.sharedSelectorTimeout;
/*     */   }
/*     */   
/*     */   public ConcurrentLinkedQueue<Selector> getSelectors() {
/* 314 */     return this.selectors;
/*     */   }
/*     */   
/*     */   public AtomicInteger getSpare() {
/* 318 */     return this.spare;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\NioSelectorPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */