/*     */ package org.apache.tomcat.util.collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchronizedQueue<T>
/*     */ {
/*     */   public static final int DEFAULT_SIZE = 128;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object[] queue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int size;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  34 */   private int insert = 0;
/*  35 */   private int remove = 0;
/*     */   
/*     */   public SynchronizedQueue() {
/*  38 */     this(128);
/*     */   }
/*     */   
/*     */   public SynchronizedQueue(int initialSize) {
/*  42 */     this.queue = new Object[initialSize];
/*  43 */     this.size = initialSize;
/*     */   }
/*     */   
/*     */   public synchronized boolean offer(T t) {
/*  47 */     this.queue[(this.insert++)] = t;
/*     */     
/*     */ 
/*  50 */     if (this.insert == this.size) {
/*  51 */       this.insert = 0;
/*     */     }
/*     */     
/*  54 */     if (this.insert == this.remove) {
/*  55 */       expand();
/*     */     }
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized T poll() {
/*  61 */     if (this.insert == this.remove)
/*     */     {
/*  63 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  67 */     T result = this.queue[this.remove];
/*  68 */     this.queue[this.remove] = null;
/*  69 */     this.remove += 1;
/*     */     
/*     */ 
/*  72 */     if (this.remove == this.size) {
/*  73 */       this.remove = 0;
/*     */     }
/*     */     
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   private void expand() {
/*  80 */     int newSize = this.size * 2;
/*  81 */     Object[] newQueue = new Object[newSize];
/*     */     
/*  83 */     System.arraycopy(this.queue, this.insert, newQueue, 0, this.size - this.insert);
/*  84 */     System.arraycopy(this.queue, 0, newQueue, this.size - this.insert, this.insert);
/*     */     
/*  86 */     this.insert = this.size;
/*  87 */     this.remove = 0;
/*  88 */     this.queue = newQueue;
/*  89 */     this.size = newSize;
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/*  93 */     int result = this.insert - this.remove;
/*  94 */     if (result < 0) {
/*  95 */       result += this.size;
/*     */     }
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized void clear() {
/* 101 */     this.queue = new Object[this.size];
/* 102 */     this.insert = 0;
/* 103 */     this.remove = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\collections\SynchronizedQueue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */