/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.List;
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
/*     */ public final class ObjectBuffer
/*     */ {
/*     */   private static final int SMALL_CHUNK = 16384;
/*     */   private static final int MAX_CHUNK = 262144;
/*     */   private LinkedNode<Object[]> _head;
/*     */   private LinkedNode<Object[]> _tail;
/*     */   private int _size;
/*     */   private Object[] _freeBuffer;
/*     */   
/*     */   public Object[] resetAndStart()
/*     */   {
/*  68 */     _reset();
/*  69 */     if (this._freeBuffer == null) {
/*  70 */       return new Object[12];
/*     */     }
/*  72 */     return this._freeBuffer;
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
/*     */   public Object[] appendCompletedChunk(Object[] fullChunk)
/*     */   {
/*  91 */     LinkedNode<Object[]> next = new LinkedNode(fullChunk, null);
/*  92 */     if (this._head == null) {
/*  93 */       this._head = (this._tail = next);
/*     */     } else {
/*  95 */       this._tail.linkNext(next);
/*  96 */       this._tail = next;
/*     */     }
/*  98 */     int len = fullChunk.length;
/*  99 */     this._size += len;
/*     */     
/* 101 */     if (len < 16384) {
/* 102 */       len += len;
/* 103 */     } else if (len < 262144) {
/* 104 */       len += (len >> 2);
/*     */     }
/* 106 */     return new Object[len];
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
/*     */   public Object[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries)
/*     */   {
/* 121 */     int totalSize = lastChunkEntries + this._size;
/* 122 */     Object[] result = new Object[totalSize];
/* 123 */     _copyTo(result, totalSize, lastChunk, lastChunkEntries);
/* 124 */     return result;
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
/*     */   public <T> T[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, Class<T> componentType)
/*     */   {
/* 137 */     int totalSize = lastChunkEntries + this._size;
/*     */     
/* 139 */     T[] result = (Object[])Array.newInstance(componentType, totalSize);
/* 140 */     _copyTo(result, totalSize, lastChunk, lastChunkEntries);
/* 141 */     _reset();
/* 142 */     return result;
/*     */   }
/*     */   
/*     */   public void completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, List<Object> resultList)
/*     */   {
/* 147 */     for (LinkedNode<Object[]> n = this._head; n != null; n = n.next()) {
/* 148 */       Object[] curr = (Object[])n.value();
/* 149 */       int i = 0; for (int len = curr.length; i < len; i++) {
/* 150 */         resultList.add(curr[i]);
/*     */       }
/*     */     }
/*     */     
/* 154 */     for (int i = 0; i < lastChunkEntries; i++) {
/* 155 */       resultList.add(lastChunk[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int initialCapacity()
/*     */   {
/* 166 */     return this._freeBuffer == null ? 0 : this._freeBuffer.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int bufferedSize()
/*     */   {
/* 173 */     return this._size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _reset()
/*     */   {
/* 184 */     if (this._tail != null) {
/* 185 */       this._freeBuffer = ((Object[])this._tail.value());
/*     */     }
/*     */     
/* 188 */     this._head = (this._tail = null);
/* 189 */     this._size = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void _copyTo(Object resultArray, int totalSize, Object[] lastChunk, int lastChunkEntries)
/*     */   {
/* 195 */     int ptr = 0;
/*     */     
/* 197 */     for (LinkedNode<Object[]> n = this._head; n != null; n = n.next()) {
/* 198 */       Object[] curr = (Object[])n.value();
/* 199 */       int len = curr.length;
/* 200 */       System.arraycopy(curr, 0, resultArray, ptr, len);
/* 201 */       ptr += len;
/*     */     }
/* 203 */     System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
/* 204 */     ptr += lastChunkEntries;
/*     */     
/*     */ 
/* 207 */     if (ptr != totalSize) {
/* 208 */       throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ObjectBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */