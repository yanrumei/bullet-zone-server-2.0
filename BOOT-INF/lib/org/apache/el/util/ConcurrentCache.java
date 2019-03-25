/*    */ package org.apache.el.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ConcurrentCache<K, V>
/*    */ {
/*    */   private final int size;
/*    */   private final Map<K, V> eden;
/*    */   private final Map<K, V> longterm;
/*    */   
/*    */   public ConcurrentCache(int size)
/*    */   {
/* 32 */     this.size = size;
/* 33 */     this.eden = new ConcurrentHashMap(size);
/* 34 */     this.longterm = new WeakHashMap(size);
/*    */   }
/*    */   
/*    */   public V get(K k) {
/* 38 */     V v = this.eden.get(k);
/* 39 */     if (v == null) {
/* 40 */       synchronized (this.longterm) {
/* 41 */         v = this.longterm.get(k);
/*    */       }
/* 43 */       if (v != null) {
/* 44 */         this.eden.put(k, v);
/*    */       }
/*    */     }
/* 47 */     return v;
/*    */   }
/*    */   
/*    */   public void put(K k, V v) {
/* 51 */     if (this.eden.size() >= this.size) {
/* 52 */       synchronized (this.longterm) {
/* 53 */         this.longterm.putAll(this.eden);
/*    */       }
/* 55 */       this.eden.clear();
/*    */     }
/* 57 */     this.eden.put(k, v);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\e\\util\ConcurrentCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */