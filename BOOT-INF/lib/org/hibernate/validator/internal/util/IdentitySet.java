/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class IdentitySet
/*     */   implements Set<Object>
/*     */ {
/*     */   private final Map<Object, Object> map;
/*  22 */   private final Object CONTAINS = new Object();
/*     */   
/*     */   public IdentitySet() {
/*  25 */     this(10);
/*     */   }
/*     */   
/*     */   public IdentitySet(int size) {
/*  29 */     this.map = new IdentityHashMap(size);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  34 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  39 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean contains(Object o)
/*     */   {
/*  44 */     return this.map.containsKey(o);
/*     */   }
/*     */   
/*     */   public Iterator<Object> iterator()
/*     */   {
/*  49 */     return this.map.keySet().iterator();
/*     */   }
/*     */   
/*     */   public Object[] toArray()
/*     */   {
/*  54 */     return this.map.keySet().toArray();
/*     */   }
/*     */   
/*     */   public boolean add(Object o)
/*     */   {
/*  59 */     return this.map.put(o, this.CONTAINS) == null;
/*     */   }
/*     */   
/*     */   public boolean remove(Object o)
/*     */   {
/*  64 */     return this.map.remove(o) == this.CONTAINS;
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends Object> c)
/*     */   {
/*  69 */     boolean doThing = false;
/*  70 */     for (Object o : c) {
/*  71 */       doThing = (doThing) || (add(o));
/*     */     }
/*  73 */     return doThing;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  78 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<? extends Object> c)
/*     */   {
/*  83 */     boolean remove = false;
/*  84 */     for (Object o : c) {
/*  85 */       remove = (remove) || (remove(o));
/*     */     }
/*  87 */     return remove;
/*     */   }
/*     */   
/*     */   public boolean retainAll(Collection<? extends Object> c)
/*     */   {
/*  92 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection<? extends Object> c)
/*     */   {
/*  97 */     for (Object o : c) {
/*  98 */       if (!contains(o)) {
/*  99 */         return false;
/*     */       }
/*     */     }
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public Object[] toArray(Object[] a)
/*     */   {
/* 107 */     return this.map.keySet().toArray(a);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 112 */     return "IdentitySet{map=" + this.map + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\IdentitySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */