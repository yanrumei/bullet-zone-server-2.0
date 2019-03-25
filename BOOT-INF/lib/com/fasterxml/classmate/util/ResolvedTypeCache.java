/*     */ package com.fasterxml.classmate.util;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.types.TypePlaceHolder;
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResolvedTypeCache
/*     */   implements Serializable
/*     */ {
/*     */   protected final CacheMap _map;
/*     */   
/*     */   public ResolvedTypeCache(int maxEntries)
/*     */   {
/*  21 */     this._map = new CacheMap(maxEntries);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Key key(Class<?> simpleType)
/*     */   {
/*  28 */     return new Key(simpleType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Key key(Class<?> simpleType, ResolvedType[] tp)
/*     */   {
/*  35 */     int len = tp == null ? 0 : tp.length;
/*  36 */     if (len == 0) {
/*  37 */       return new Key(simpleType);
/*     */     }
/*     */     
/*     */ 
/*  41 */     for (int i = 0; i < len; i++) {
/*  42 */       if ((tp[i] instanceof TypePlaceHolder)) {
/*  43 */         return null;
/*     */       }
/*     */     }
/*  46 */     return new Key(simpleType, tp);
/*     */   }
/*     */   
/*     */   public synchronized ResolvedType find(Key key) {
/*  50 */     if (key == null) {
/*  51 */       throw new IllegalArgumentException("Null key not allowed");
/*     */     }
/*  53 */     return (ResolvedType)this._map.get(key);
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/*  57 */     return this._map.size();
/*     */   }
/*     */   
/*     */   public synchronized void put(Key key, ResolvedType type) {
/*  61 */     if (key == null) {
/*  62 */       throw new IllegalArgumentException("Null key not allowed");
/*     */     }
/*  64 */     this._map.put(key, type);
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
/*     */   protected void addForTest(ResolvedType type)
/*     */   {
/*  78 */     List<ResolvedType> tp = type.getTypeParameters();
/*  79 */     ResolvedType[] tpa = (ResolvedType[])tp.toArray(new ResolvedType[tp.size()]);
/*  80 */     put(key(type.getErasedType(), tpa), type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Key
/*     */   {
/*     */     private final Class<?> _erasedType;
/*     */     
/*     */ 
/*     */     private final ResolvedType[] _typeParameters;
/*     */     
/*     */ 
/*     */     private final int _hashCode;
/*     */     
/*     */ 
/*     */ 
/*     */     public Key(Class<?> simpleType)
/*     */     {
/*  99 */       this(simpleType, null);
/*     */     }
/*     */     
/*     */ 
/*     */     public Key(Class<?> erasedType, ResolvedType[] tp)
/*     */     {
/* 105 */       if ((tp != null) && (tp.length == 0)) {
/* 106 */         tp = null;
/*     */       }
/* 108 */       this._erasedType = erasedType;
/* 109 */       this._typeParameters = tp;
/* 110 */       int h = erasedType.getName().hashCode();
/* 111 */       if (tp != null) {
/* 112 */         h += tp.length;
/*     */       }
/* 114 */       this._hashCode = h;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 119 */       StringBuilder sb = new StringBuilder();
/* 120 */       sb.append("[CacheKey: ");
/* 121 */       sb.append(this._erasedType.getName()).append('(');
/*     */       
/* 123 */       if (this._typeParameters != null) {
/* 124 */         for (int i = 0; i < this._typeParameters.length; i++) {
/* 125 */           if (i > 0) {
/* 126 */             sb.append(',');
/*     */           }
/* 128 */           sb.append(this._typeParameters[i]);
/*     */         }
/*     */       }
/* 131 */       sb.append(")]");
/* 132 */       return sb.toString();
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 136 */       return this._hashCode;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 141 */       if (o == this) return true;
/* 142 */       if ((o == null) || (o.getClass() != getClass())) return false;
/* 143 */       Key other = (Key)o;
/* 144 */       if (other._erasedType != this._erasedType) return false;
/* 145 */       ResolvedType[] otherTP = other._typeParameters;
/* 146 */       if (this._typeParameters == null) {
/* 147 */         return otherTP == null;
/*     */       }
/* 149 */       if ((otherTP == null) || (otherTP.length != this._typeParameters.length)) {
/* 150 */         return false;
/*     */       }
/* 152 */       int i = 0; for (int len = this._typeParameters.length; i < len; i++) {
/* 153 */         if (!this._typeParameters[i].equals(otherTP[i])) {
/* 154 */           return false;
/*     */         }
/*     */       }
/* 157 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class CacheMap
/*     */     extends LinkedHashMap<ResolvedTypeCache.Key, ResolvedType>
/*     */   {
/*     */     protected final int _maxEntries;
/*     */     
/*     */ 
/*     */     public CacheMap(int maxEntries)
/*     */     {
/* 170 */       this._maxEntries = maxEntries;
/*     */     }
/*     */     
/*     */     protected boolean removeEldestEntry(Map.Entry<ResolvedTypeCache.Key, ResolvedType> eldest)
/*     */     {
/* 175 */       return size() > this._maxEntries;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmat\\util\ResolvedTypeCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */