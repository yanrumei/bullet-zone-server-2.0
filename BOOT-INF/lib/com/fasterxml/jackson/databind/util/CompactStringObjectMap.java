/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public final class CompactStringObjectMap
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  25 */   private static final CompactStringObjectMap EMPTY = new CompactStringObjectMap(1, 0, new Object[4]);
/*     */   
/*     */   private final int _hashMask;
/*     */   
/*     */   private final int _spillCount;
/*     */   private final Object[] _hashArea;
/*     */   
/*     */   private CompactStringObjectMap(int hashMask, int spillCount, Object[] hashArea)
/*     */   {
/*  34 */     this._hashMask = hashMask;
/*  35 */     this._spillCount = spillCount;
/*  36 */     this._hashArea = hashArea;
/*     */   }
/*     */   
/*     */   public static <T> CompactStringObjectMap construct(Map<String, T> all)
/*     */   {
/*  41 */     if (all.isEmpty()) {
/*  42 */       return EMPTY;
/*     */     }
/*     */     
/*     */ 
/*  46 */     int size = findSize(all.size());
/*  47 */     int mask = size - 1;
/*     */     
/*  49 */     int alloc = (size + (size >> 1)) * 2;
/*  50 */     Object[] hashArea = new Object[alloc];
/*  51 */     int spillCount = 0;
/*     */     
/*  53 */     for (Map.Entry<String, T> entry : all.entrySet()) {
/*  54 */       String key = (String)entry.getKey();
/*     */       
/*  56 */       int slot = key.hashCode() & mask;
/*  57 */       int ix = slot + slot;
/*     */       
/*     */ 
/*  60 */       if (hashArea[ix] != null)
/*     */       {
/*  62 */         ix = size + (slot >> 1) << 1;
/*  63 */         if (hashArea[ix] != null)
/*     */         {
/*  65 */           ix = (size + (size >> 1) << 1) + spillCount;
/*  66 */           spillCount += 2;
/*  67 */           if (ix >= hashArea.length) {
/*  68 */             hashArea = Arrays.copyOf(hashArea, hashArea.length + 4);
/*     */           }
/*     */         }
/*     */       }
/*  72 */       hashArea[ix] = key;
/*  73 */       hashArea[(ix + 1)] = entry.getValue();
/*     */     }
/*  75 */     return new CompactStringObjectMap(mask, spillCount, hashArea);
/*     */   }
/*     */   
/*     */   private static final int findSize(int size)
/*     */   {
/*  80 */     if (size <= 5) {
/*  81 */       return 8;
/*     */     }
/*  83 */     if (size <= 12) {
/*  84 */       return 16;
/*     */     }
/*  86 */     int needed = size + (size >> 2);
/*  87 */     int result = 32;
/*  88 */     while (result < needed) {
/*  89 */       result += result;
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   public Object find(String key) {
/*  95 */     int slot = key.hashCode() & this._hashMask;
/*  96 */     int ix = slot << 1;
/*  97 */     Object match = this._hashArea[ix];
/*  98 */     if ((match == key) || (key.equals(match))) {
/*  99 */       return this._hashArea[(ix + 1)];
/*     */     }
/* 101 */     return _find2(key, slot, match);
/*     */   }
/*     */   
/*     */   private final Object _find2(String key, int slot, Object match)
/*     */   {
/* 106 */     if (match == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     int hashSize = this._hashMask + 1;
/* 110 */     int ix = hashSize + (slot >> 1) << 1;
/* 111 */     match = this._hashArea[ix];
/* 112 */     if (key.equals(match)) {
/* 113 */       return this._hashArea[(ix + 1)];
/*     */     }
/* 115 */     if (match != null) {
/* 116 */       int i = hashSize + (hashSize >> 1) << 1;
/* 117 */       for (int end = i + this._spillCount; i < end; i += 2) {
/* 118 */         match = this._hashArea[i];
/* 119 */         if ((match == key) || (key.equals(match))) {
/* 120 */           return this._hashArea[(i + 1)];
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */   
/*     */   public List<String> keys() {
/* 128 */     int end = this._hashArea.length;
/* 129 */     List<String> keys = new ArrayList(end >> 2);
/* 130 */     for (int i = 0; i < end; i += 2) {
/* 131 */       Object key = this._hashArea[i];
/* 132 */       if (key != null) {
/* 133 */         keys.add((String)key);
/*     */       }
/*     */     }
/* 136 */     return keys;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\CompactStringObjectMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */