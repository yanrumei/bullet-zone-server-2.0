/*     */ package org.apache.tomcat.util.collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchronizedStack<T>
/*     */ {
/*     */   public static final int DEFAULT_SIZE = 128;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_LIMIT = -1;
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
/*     */   private final int limit;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   private int index = -1;
/*     */   
/*     */   private Object[] stack;
/*     */   
/*     */   public SynchronizedStack()
/*     */   {
/*  45 */     this(128, -1);
/*     */   }
/*     */   
/*     */   public SynchronizedStack(int size, int limit) {
/*  49 */     if ((limit > -1) && (size > limit)) {
/*  50 */       this.size = limit;
/*     */     } else {
/*  52 */       this.size = size;
/*     */     }
/*  54 */     this.limit = limit;
/*  55 */     this.stack = new Object[size];
/*     */   }
/*     */   
/*     */   public synchronized boolean push(T obj)
/*     */   {
/*  60 */     this.index += 1;
/*  61 */     if (this.index == this.size) {
/*  62 */       if ((this.limit == -1) || (this.size < this.limit)) {
/*  63 */         expand();
/*     */       } else {
/*  65 */         this.index -= 1;
/*  66 */         return false;
/*     */       }
/*     */     }
/*  69 */     this.stack[this.index] = obj;
/*  70 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized T pop()
/*     */   {
/*  75 */     if (this.index == -1) {
/*  76 */       return null;
/*     */     }
/*  78 */     T result = this.stack[this.index];
/*  79 */     this.stack[(this.index--)] = null;
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized void clear() {
/*  84 */     if (this.index > -1) {
/*  85 */       for (int i = 0; i < this.index + 1; i++) {
/*  86 */         this.stack[i] = null;
/*     */       }
/*     */     }
/*  89 */     this.index = -1;
/*     */   }
/*     */   
/*     */   private void expand() {
/*  93 */     int newSize = this.size * 2;
/*  94 */     if ((this.limit != -1) && (newSize > this.limit)) {
/*  95 */       newSize = this.limit;
/*     */     }
/*  97 */     Object[] newStack = new Object[newSize];
/*  98 */     System.arraycopy(this.stack, 0, newStack, 0, this.size);
/*     */     
/*     */ 
/*     */ 
/* 102 */     this.stack = newStack;
/* 103 */     this.size = newSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\collections\SynchronizedStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */