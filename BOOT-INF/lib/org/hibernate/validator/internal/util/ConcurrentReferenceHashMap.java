/*      */ package org.hibernate.validator.internal.util;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.locks.ReentrantLock;
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
/*      */ 
/*      */ public final class ConcurrentReferenceHashMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 7249069246763182397L;
/*      */   
/*      */   public static enum ReferenceType
/*      */   {
/*  149 */     STRONG, 
/*      */     
/*  151 */     WEAK, 
/*      */     
/*  153 */     SOFT;
/*      */     
/*      */     private ReferenceType() {}
/*      */   }
/*      */   
/*      */   public static enum Option
/*      */   {
/*  160 */     IDENTITY_COMPARISONS;
/*      */     
/*      */     private Option() {}
/*      */   }
/*      */   
/*  165 */   static final ReferenceType DEFAULT_KEY_TYPE = ReferenceType.WEAK;
/*      */   
/*  167 */   static final ReferenceType DEFAULT_VALUE_TYPE = ReferenceType.STRONG;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int RETRIES_BEFORE_LOCK = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final int segmentMask;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final int segmentShift;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final Segment<K, V>[] segments;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean identityComparisons;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   transient Set<K> keySet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   transient Set<Map.Entry<K, V>> entrySet;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   transient Collection<V> values;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int hash(int h)
/*      */   {
/*  246 */     h += (h << 15 ^ 0xCD7D);
/*  247 */     h ^= h >>> 10;
/*  248 */     h += (h << 3);
/*  249 */     h ^= h >>> 6;
/*  250 */     h += (h << 2) + (h << 14);
/*  251 */     return h ^ h >>> 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final Segment<K, V> segmentFor(int hash)
/*      */   {
/*  260 */     return this.segments[(hash >>> this.segmentShift & this.segmentMask)];
/*      */   }
/*      */   
/*      */   private int hashOf(Object key) {
/*  264 */     return hash(this.identityComparisons ? 
/*  265 */       System.identityHashCode(key) : key.hashCode());
/*      */   }
/*      */   
/*      */   static abstract interface KeyReference
/*      */   {
/*      */     public abstract int keyHash();
/*      */     
/*      */     public abstract Object keyRef();
/*      */   }
/*      */   
/*      */   static final class WeakKeyReference<K> extends WeakReference<K> implements ConcurrentReferenceHashMap.KeyReference
/*      */   {
/*      */     final int hash;
/*      */     
/*      */     WeakKeyReference(K key, int hash, ReferenceQueue<Object> refQueue)
/*      */     {
/*  281 */       super(refQueue);
/*  282 */       this.hash = hash;
/*      */     }
/*      */     
/*      */     public final int keyHash() {
/*  286 */       return this.hash;
/*      */     }
/*      */     
/*      */     public final Object keyRef()
/*      */     {
/*  291 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class SoftKeyReference<K> extends SoftReference<K> implements ConcurrentReferenceHashMap.KeyReference
/*      */   {
/*      */     final int hash;
/*      */     
/*      */     SoftKeyReference(K key, int hash, ReferenceQueue<Object> refQueue)
/*      */     {
/*  301 */       super(refQueue);
/*  302 */       this.hash = hash;
/*      */     }
/*      */     
/*      */     public final int keyHash() {
/*  306 */       return this.hash;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  311 */     public final Object keyRef() { return this; }
/*      */   }
/*      */   
/*      */   static final class WeakValueReference<V> extends WeakReference<V> implements ConcurrentReferenceHashMap.KeyReference {
/*      */     final Object keyRef;
/*      */     final int hash;
/*      */     
/*      */     WeakValueReference(V value, Object keyRef, int hash, ReferenceQueue<Object> refQueue) {
/*  319 */       super(refQueue);
/*  320 */       this.keyRef = keyRef;
/*  321 */       this.hash = hash;
/*      */     }
/*      */     
/*      */     public final int keyHash()
/*      */     {
/*  326 */       return this.hash;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  331 */     public final Object keyRef() { return this.keyRef; }
/*      */   }
/*      */   
/*      */   static final class SoftValueReference<V> extends SoftReference<V> implements ConcurrentReferenceHashMap.KeyReference {
/*      */     final Object keyRef;
/*      */     final int hash;
/*      */     
/*      */     SoftValueReference(V value, Object keyRef, int hash, ReferenceQueue<Object> refQueue) {
/*  339 */       super(refQueue);
/*  340 */       this.keyRef = keyRef;
/*  341 */       this.hash = hash;
/*      */     }
/*      */     
/*      */     public final int keyHash() {
/*  345 */       return this.hash;
/*      */     }
/*      */     
/*      */     public final Object keyRef()
/*      */     {
/*  350 */       return this.keyRef;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final class HashEntry<K, V>
/*      */   {
/*      */     final Object keyRef;
/*      */     
/*      */ 
/*      */ 
/*      */     final int hash;
/*      */     
/*      */ 
/*      */     volatile Object valueRef;
/*      */     
/*      */ 
/*      */     final HashEntry<K, V> next;
/*      */     
/*      */ 
/*      */ 
/*      */     HashEntry(K key, int hash, HashEntry<K, V> next, V value, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue)
/*      */     {
/*  375 */       this.hash = hash;
/*  376 */       this.next = next;
/*  377 */       this.keyRef = newKeyReference(key, keyType, refQueue);
/*  378 */       this.valueRef = newValueReference(value, valueType, refQueue);
/*      */     }
/*      */     
/*      */     final Object newKeyReference(K key, ConcurrentReferenceHashMap.ReferenceType keyType, ReferenceQueue<Object> refQueue)
/*      */     {
/*  383 */       if (keyType == ConcurrentReferenceHashMap.ReferenceType.WEAK)
/*  384 */         return new ConcurrentReferenceHashMap.WeakKeyReference(key, this.hash, refQueue);
/*  385 */       if (keyType == ConcurrentReferenceHashMap.ReferenceType.SOFT) {
/*  386 */         return new ConcurrentReferenceHashMap.SoftKeyReference(key, this.hash, refQueue);
/*      */       }
/*  388 */       return key;
/*      */     }
/*      */     
/*      */     final Object newValueReference(V value, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue)
/*      */     {
/*  393 */       if (valueType == ConcurrentReferenceHashMap.ReferenceType.WEAK)
/*  394 */         return new ConcurrentReferenceHashMap.WeakValueReference(value, this.keyRef, this.hash, refQueue);
/*  395 */       if (valueType == ConcurrentReferenceHashMap.ReferenceType.SOFT) {
/*  396 */         return new ConcurrentReferenceHashMap.SoftValueReference(value, this.keyRef, this.hash, refQueue);
/*      */       }
/*  398 */       return value;
/*      */     }
/*      */     
/*      */     final K key()
/*      */     {
/*  403 */       if ((this.keyRef instanceof ConcurrentReferenceHashMap.KeyReference)) {
/*  404 */         return (K)((Reference)this.keyRef).get();
/*      */       }
/*  406 */       return (K)this.keyRef;
/*      */     }
/*      */     
/*      */     final V value() {
/*  410 */       return (V)dereferenceValue(this.valueRef);
/*      */     }
/*      */     
/*      */     final V dereferenceValue(Object value)
/*      */     {
/*  415 */       if ((value instanceof ConcurrentReferenceHashMap.KeyReference)) {
/*  416 */         return (V)((Reference)value).get();
/*      */       }
/*  418 */       return (V)value;
/*      */     }
/*      */     
/*      */     final void setValue(V value, ConcurrentReferenceHashMap.ReferenceType valueType, ReferenceQueue<Object> refQueue) {
/*  422 */       this.valueRef = newValueReference(value, valueType, refQueue);
/*      */     }
/*      */     
/*      */     static final <K, V> HashEntry<K, V>[] newArray(int i)
/*      */     {
/*  427 */       return new HashEntry[i];
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
/*      */   static final class Segment<K, V>
/*      */     extends ReentrantLock
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 2249069246763182397L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     volatile transient int count;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     transient int modCount;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     transient int threshold;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     volatile transient ConcurrentReferenceHashMap.HashEntry<K, V>[] table;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final float loadFactor;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     volatile transient ReferenceQueue<Object> refQueue;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final ConcurrentReferenceHashMap.ReferenceType keyType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final ConcurrentReferenceHashMap.ReferenceType valueType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final boolean identityComparisons;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     Segment(int initialCapacity, float lf, ConcurrentReferenceHashMap.ReferenceType keyType, ConcurrentReferenceHashMap.ReferenceType valueType, boolean identityComparisons)
/*      */     {
/*  525 */       this.loadFactor = lf;
/*  526 */       this.keyType = keyType;
/*  527 */       this.valueType = valueType;
/*  528 */       this.identityComparisons = identityComparisons;
/*  529 */       setTable(ConcurrentReferenceHashMap.HashEntry.newArray(initialCapacity));
/*      */     }
/*      */     
/*      */     static final <K, V> Segment<K, V>[] newArray(int i)
/*      */     {
/*  534 */       return new Segment[i];
/*      */     }
/*      */     
/*      */     private boolean keyEq(Object src, Object dest) {
/*  538 */       return this.identityComparisons ? false : src == dest ? true : src.equals(dest);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     void setTable(ConcurrentReferenceHashMap.HashEntry<K, V>[] newTable)
/*      */     {
/*  546 */       this.threshold = ((int)(newTable.length * this.loadFactor));
/*  547 */       this.table = newTable;
/*  548 */       this.refQueue = new ReferenceQueue();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V> getFirst(int hash)
/*      */     {
/*  555 */       ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
/*  556 */       return tab[(hash & tab.length - 1)];
/*      */     }
/*      */     
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V> newHashEntry(K key, int hash, ConcurrentReferenceHashMap.HashEntry<K, V> next, V value) {
/*  560 */       return new ConcurrentReferenceHashMap.HashEntry(key, hash, next, value, this.keyType, this.valueType, this.refQueue);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     V readValueUnderLock(ConcurrentReferenceHashMap.HashEntry<K, V> e)
/*      */     {
/*  571 */       lock();
/*      */       try {
/*  573 */         removeStale();
/*  574 */         return (V)e.value();
/*      */       } finally {
/*  576 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     V get(Object key, int hash)
/*      */     {
/*  583 */       if (this.count != 0) {
/*  584 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = getFirst(hash);
/*  585 */         while (e != null) {
/*  586 */           if ((e.hash == hash) && (keyEq(key, e.key()))) {
/*  587 */             Object opaque = e.valueRef;
/*  588 */             if (opaque != null) {
/*  589 */               return (V)e.dereferenceValue(opaque);
/*      */             }
/*  591 */             return (V)readValueUnderLock(e);
/*      */           }
/*  593 */           e = e.next;
/*      */         }
/*      */       }
/*  596 */       return null;
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*  600 */       if (this.count != 0) {
/*  601 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = getFirst(hash);
/*  602 */         while (e != null) {
/*  603 */           if ((e.hash == hash) && (keyEq(key, e.key())))
/*  604 */             return true;
/*  605 */           e = e.next;
/*      */         }
/*      */       }
/*  608 */       return false;
/*      */     }
/*      */     
/*      */     boolean containsValue(Object value) {
/*  612 */       if (this.count != 0) {
/*  613 */         ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
/*  614 */         int len = tab.length;
/*  615 */         for (int i = 0; i < len; i++) {
/*  616 */           for (ConcurrentReferenceHashMap.HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
/*  617 */             Object opaque = e.valueRef;
/*      */             V v;
/*      */             V v;
/*  620 */             if (opaque == null) {
/*  621 */               v = readValueUnderLock(e);
/*      */             } else {
/*  623 */               v = e.dereferenceValue(opaque);
/*      */             }
/*  625 */             if (value.equals(v))
/*  626 */               return true;
/*      */           }
/*      */         }
/*      */       }
/*  630 */       return false;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/*  634 */       lock();
/*      */       try {
/*  636 */         removeStale();
/*  637 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = getFirst(hash);
/*  638 */         while ((e != null) && ((e.hash != hash) || (!keyEq(key, e.key())))) {
/*  639 */           e = e.next;
/*      */         }
/*  641 */         boolean replaced = false;
/*  642 */         if ((e != null) && (oldValue.equals(e.value()))) {
/*  643 */           replaced = true;
/*  644 */           e.setValue(newValue, this.valueType, this.refQueue);
/*      */         }
/*  646 */         return replaced;
/*      */       } finally {
/*  648 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/*  653 */       lock();
/*      */       try {
/*  655 */         removeStale();
/*  656 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = getFirst(hash);
/*  657 */         while ((e != null) && ((e.hash != hash) || (!keyEq(key, e.key())))) {
/*  658 */           e = e.next;
/*      */         }
/*  660 */         V oldValue = null;
/*  661 */         if (e != null) {
/*  662 */           oldValue = e.value();
/*  663 */           e.setValue(newValue, this.valueType, this.refQueue);
/*      */         }
/*  665 */         return oldValue;
/*      */       } finally {
/*  667 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent)
/*      */     {
/*  673 */       lock();
/*      */       try {
/*  675 */         removeStale();
/*  676 */         int c = this.count;
/*  677 */         if (c++ > this.threshold) {
/*  678 */           int reduced = rehash();
/*  679 */           if (reduced > 0) {
/*  680 */             this.count = (c -= reduced - 1);
/*      */           }
/*      */         }
/*  683 */         ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
/*  684 */         int index = hash & tab.length - 1;
/*  685 */         ConcurrentReferenceHashMap.HashEntry<K, V> first = tab[index];
/*  686 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = first;
/*  687 */         while ((e != null) && ((e.hash != hash) || (!keyEq(key, e.key())))) {
/*  688 */           e = e.next;
/*      */         }
/*      */         V oldValue;
/*  691 */         if (e != null) {
/*  692 */           V oldValue = e.value();
/*  693 */           if ((!onlyIfAbsent) || (oldValue == null)) {
/*  694 */             e.setValue(value, this.valueType, this.refQueue);
/*      */           }
/*      */         } else {
/*  697 */           oldValue = null;
/*  698 */           this.modCount += 1;
/*  699 */           tab[index] = newHashEntry(key, hash, first, value);
/*  700 */           this.count = c;
/*      */         }
/*  702 */         return oldValue;
/*      */       } finally {
/*  704 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     int rehash() {
/*  709 */       ConcurrentReferenceHashMap.HashEntry<K, V>[] oldTable = this.table;
/*  710 */       int oldCapacity = oldTable.length;
/*  711 */       if (oldCapacity >= 1073741824) {
/*  712 */         return 0;
/*      */       }
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
/*  728 */       ConcurrentReferenceHashMap.HashEntry<K, V>[] newTable = ConcurrentReferenceHashMap.HashEntry.newArray(oldCapacity << 1);
/*  729 */       this.threshold = ((int)(newTable.length * this.loadFactor));
/*  730 */       int sizeMask = newTable.length - 1;
/*  731 */       int reduce = 0;
/*  732 */       for (int i = 0; i < oldCapacity; i++)
/*      */       {
/*      */ 
/*  735 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = oldTable[i];
/*      */         
/*  737 */         if (e != null) {
/*  738 */           ConcurrentReferenceHashMap.HashEntry<K, V> next = e.next;
/*  739 */           int idx = e.hash & sizeMask;
/*      */           
/*      */ 
/*  742 */           if (next == null) {
/*  743 */             newTable[idx] = e;
/*      */           }
/*      */           else
/*      */           {
/*  747 */             ConcurrentReferenceHashMap.HashEntry<K, V> lastRun = e;
/*  748 */             int lastIdx = idx;
/*  749 */             for (ConcurrentReferenceHashMap.HashEntry<K, V> last = next; 
/*  750 */                 last != null; 
/*  751 */                 last = last.next) {
/*  752 */               int k = last.hash & sizeMask;
/*  753 */               if (k != lastIdx) {
/*  754 */                 lastIdx = k;
/*  755 */                 lastRun = last;
/*      */               }
/*      */             }
/*  758 */             newTable[lastIdx] = lastRun;
/*      */             
/*  760 */             for (ConcurrentReferenceHashMap.HashEntry<K, V> p = e; p != lastRun; p = p.next)
/*      */             {
/*  762 */               K key = p.key();
/*  763 */               if (key == null) {
/*  764 */                 reduce++;
/*      */               }
/*      */               else {
/*  767 */                 int k = p.hash & sizeMask;
/*  768 */                 ConcurrentReferenceHashMap.HashEntry<K, V> n = newTable[k];
/*  769 */                 newTable[k] = newHashEntry(key, p.hash, n, p.value());
/*      */               }
/*      */             }
/*      */           }
/*      */         } }
/*  774 */       this.table = newTable;
/*  775 */       return reduce;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     V remove(Object key, int hash, Object value, boolean refRemove)
/*      */     {
/*  782 */       lock();
/*      */       try {
/*  784 */         if (!refRemove)
/*  785 */           removeStale();
/*  786 */         int c = this.count - 1;
/*  787 */         ConcurrentReferenceHashMap.HashEntry<K, V>[] tab = this.table;
/*  788 */         int index = hash & tab.length - 1;
/*  789 */         ConcurrentReferenceHashMap.HashEntry<K, V> first = tab[index];
/*  790 */         ConcurrentReferenceHashMap.HashEntry<K, V> e = first;
/*      */         
/*  792 */         while ((e != null) && (key != e.keyRef) && ((refRemove) || (hash != e.hash) || 
/*  793 */           (!keyEq(key, e.key())))) {
/*  794 */           e = e.next;
/*      */         }
/*  796 */         V oldValue = null;
/*  797 */         V v; if (e != null) {
/*  798 */           v = e.value();
/*  799 */           if ((value == null) || (value.equals(v))) {
/*  800 */             oldValue = v;
/*      */             
/*      */ 
/*      */ 
/*  804 */             this.modCount += 1;
/*  805 */             ConcurrentReferenceHashMap.HashEntry<K, V> newFirst = e.next;
/*  806 */             for (ConcurrentReferenceHashMap.HashEntry<K, V> p = first; p != e; p = p.next) {
/*  807 */               K pKey = p.key();
/*  808 */               if (pKey == null) {
/*  809 */                 c--;
/*      */               }
/*      */               else
/*      */               {
/*  813 */                 newFirst = newHashEntry(pKey, p.hash, newFirst, p.value()); }
/*      */             }
/*  815 */             tab[index] = newFirst;
/*  816 */             this.count = c;
/*      */           }
/*      */         }
/*  819 */         return oldValue;
/*      */       } finally {
/*  821 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     final void removeStale() {
/*      */       ConcurrentReferenceHashMap.KeyReference ref;
/*  827 */       while ((ref = (ConcurrentReferenceHashMap.KeyReference)this.refQueue.poll()) != null) {
/*  828 */         remove(ref.keyRef(), ref.keyHash(), null, true);
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     void clear()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 22	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:count	I
/*      */       //   4: ifeq +70 -> 74
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 18	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:lock	()V
/*      */       //   11: aload_0
/*      */       //   12: getfield 12	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:table	[Lorg/hibernate/validator/internal/util/ConcurrentReferenceHashMap$HashEntry;
/*      */       //   15: astore_1
/*      */       //   16: iconst_0
/*      */       //   17: istore_2
/*      */       //   18: iload_2
/*      */       //   19: aload_1
/*      */       //   20: arraylength
/*      */       //   21: if_icmpge +13 -> 34
/*      */       //   24: aload_1
/*      */       //   25: iload_2
/*      */       //   26: aconst_null
/*      */       //   27: aastore
/*      */       //   28: iinc 2 1
/*      */       //   31: goto -13 -> 18
/*      */       //   34: aload_0
/*      */       //   35: dup
/*      */       //   36: getfield 33	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:modCount	I
/*      */       //   39: iconst_1
/*      */       //   40: iadd
/*      */       //   41: putfield 33	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:modCount	I
/*      */       //   44: aload_0
/*      */       //   45: new 13	java/lang/ref/ReferenceQueue
/*      */       //   48: dup
/*      */       //   49: invokespecial 14	java/lang/ref/ReferenceQueue:<init>	()V
/*      */       //   52: putfield 15	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:refQueue	Ljava/lang/ref/ReferenceQueue;
/*      */       //   55: aload_0
/*      */       //   56: iconst_0
/*      */       //   57: putfield 22	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:count	I
/*      */       //   60: aload_0
/*      */       //   61: invokevirtual 21	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:unlock	()V
/*      */       //   64: goto +10 -> 74
/*      */       //   67: astore_3
/*      */       //   68: aload_0
/*      */       //   69: invokevirtual 21	org/hibernate/validator/internal/util/ConcurrentReferenceHashMap$Segment:unlock	()V
/*      */       //   72: aload_3
/*      */       //   73: athrow
/*      */       //   74: return
/*      */       // Line number table:
/*      */       //   Java source line #833	-> byte code offset #0
/*      */       //   Java source line #834	-> byte code offset #7
/*      */       //   Java source line #836	-> byte code offset #11
/*      */       //   Java source line #837	-> byte code offset #16
/*      */       //   Java source line #838	-> byte code offset #24
/*      */       //   Java source line #837	-> byte code offset #28
/*      */       //   Java source line #839	-> byte code offset #34
/*      */       //   Java source line #841	-> byte code offset #44
/*      */       //   Java source line #842	-> byte code offset #55
/*      */       //   Java source line #844	-> byte code offset #60
/*      */       //   Java source line #845	-> byte code offset #64
/*      */       //   Java source line #844	-> byte code offset #67
/*      */       //   Java source line #847	-> byte code offset #74
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	75	0	this	Segment<K, V>
/*      */       //   15	10	1	tab	ConcurrentReferenceHashMap.HashEntry<K, V>[]
/*      */       //   17	12	2	i	int
/*      */       //   67	6	3	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   11	60	67	finally
/*      */     }
/*      */   }
/*      */   
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType keyType, ReferenceType valueType, EnumSet<Option> options)
/*      */   {
/*  880 */     if ((loadFactor <= 0.0F) || (initialCapacity < 0) || (concurrencyLevel <= 0)) {
/*  881 */       throw new IllegalArgumentException();
/*      */     }
/*  883 */     if (concurrencyLevel > 65536) {
/*  884 */       concurrencyLevel = 65536;
/*      */     }
/*      */     
/*  887 */     int sshift = 0;
/*  888 */     int ssize = 1;
/*  889 */     while (ssize < concurrencyLevel) {
/*  890 */       sshift++;
/*  891 */       ssize <<= 1;
/*      */     }
/*  893 */     this.segmentShift = (32 - sshift);
/*  894 */     this.segmentMask = (ssize - 1);
/*  895 */     this.segments = Segment.newArray(ssize);
/*      */     
/*  897 */     if (initialCapacity > 1073741824)
/*  898 */       initialCapacity = 1073741824;
/*  899 */     int c = initialCapacity / ssize;
/*  900 */     if (c * ssize < initialCapacity)
/*  901 */       c++;
/*  902 */     int cap = 1;
/*  903 */     while (cap < c) {
/*  904 */       cap <<= 1;
/*      */     }
/*  906 */     this.identityComparisons = ((options != null) && (options.contains(Option.IDENTITY_COMPARISONS)));
/*      */     
/*  908 */     for (int i = 0; i < this.segments.length; i++) {
/*  909 */       this.segments[i] = new Segment(cap, loadFactor, keyType, valueType, this.identityComparisons);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
/*      */   {
/*  931 */     this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_KEY_TYPE, DEFAULT_VALUE_TYPE, null);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor)
/*      */   {
/*  951 */     this(initialCapacity, loadFactor, 16);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType keyType, ReferenceType valueType)
/*      */   {
/*  968 */     this(initialCapacity, 0.75F, 16, keyType, valueType, null);
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
/*      */   public ConcurrentReferenceHashMap(int initialCapacity)
/*      */   {
/*  983 */     this(initialCapacity, 0.75F, 16);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ConcurrentReferenceHashMap()
/*      */   {
/*  992 */     this(16, 0.75F, 16);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ConcurrentReferenceHashMap(Map<? extends K, ? extends V> m)
/*      */   {
/* 1004 */     this(Math.max((int)(m.size() / 0.75F) + 1, 16), 0.75F, 16);
/*      */     
/*      */ 
/* 1007 */     putAll(m);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1017 */     Segment<K, V>[] segments = this.segments;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1027 */     int[] mc = new int[segments.length];
/* 1028 */     int mcsum = 0;
/* 1029 */     for (int i = 0; i < segments.length; i++) {
/* 1030 */       if (segments[i].count != 0) {
/* 1031 */         return false;
/*      */       }
/* 1033 */       mcsum += (mc[i] = segments[i].modCount);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1038 */     if (mcsum != 0) {
/* 1039 */       for (int i = 0; i < segments.length; i++) {
/* 1040 */         if ((segments[i].count != 0) || (mc[i] != segments[i].modCount))
/*      */         {
/* 1042 */           return false; }
/*      */       }
/*      */     }
/* 1045 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/* 1057 */     Segment<K, V>[] segments = this.segments;
/* 1058 */     long sum = 0L;
/* 1059 */     long check = 0L;
/* 1060 */     int[] mc = new int[segments.length];
/*      */     
/*      */ 
/* 1063 */     for (int k = 0; k < 2; k++) {
/* 1064 */       check = 0L;
/* 1065 */       sum = 0L;
/* 1066 */       int mcsum = 0;
/* 1067 */       for (int i = 0; i < segments.length; i++) {
/* 1068 */         sum += segments[i].count;
/* 1069 */         mcsum += (mc[i] = segments[i].modCount);
/*      */       }
/* 1071 */       if (mcsum != 0) {
/* 1072 */         for (int i = 0; i < segments.length; i++) {
/* 1073 */           check += segments[i].count;
/* 1074 */           if (mc[i] != segments[i].modCount) {
/* 1075 */             check = -1L;
/* 1076 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1080 */       if (check == sum)
/*      */         break;
/*      */     }
/* 1083 */     if (check != sum) {
/* 1084 */       sum = 0L;
/* 1085 */       for (int i = 0; i < segments.length; i++)
/* 1086 */         segments[i].lock();
/* 1087 */       for (int i = 0; i < segments.length; i++)
/* 1088 */         sum += segments[i].count;
/* 1089 */       for (int i = 0; i < segments.length; i++)
/* 1090 */         segments[i].unlock();
/*      */     }
/* 1092 */     if (sum > 2147483647L) {
/* 1093 */       return Integer.MAX_VALUE;
/*      */     }
/* 1095 */     return (int)sum;
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
/*      */   public V get(Object key)
/*      */   {
/* 1111 */     int hash = hashOf(key);
/* 1112 */     return (V)segmentFor(hash).get(key, hash);
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
/*      */   public boolean containsKey(Object key)
/*      */   {
/* 1126 */     int hash = hashOf(key);
/* 1127 */     return segmentFor(hash).containsKey(key, hash);
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
/*      */   public boolean containsValue(Object value)
/*      */   {
/* 1143 */     if (value == null) {
/* 1144 */       throw new NullPointerException();
/*      */     }
/*      */     
/*      */ 
/* 1148 */     Segment<K, V>[] segments = this.segments;
/* 1149 */     int[] mc = new int[segments.length];
/*      */     
/*      */ 
/* 1152 */     for (int k = 0; k < 2; k++) {
/* 1153 */       int mcsum = 0;
/* 1154 */       for (int i = 0; i < segments.length; i++) {
/* 1155 */         mcsum += (mc[i] = segments[i].modCount);
/* 1156 */         if (segments[i].containsValue(value))
/* 1157 */           return true;
/*      */       }
/* 1159 */       boolean cleanSweep = true;
/* 1160 */       if (mcsum != 0) {
/* 1161 */         for (int i = 0; i < segments.length; i++) {
/* 1162 */           if (mc[i] != segments[i].modCount) {
/* 1163 */             cleanSweep = false;
/* 1164 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1168 */       if (cleanSweep) {
/* 1169 */         return false;
/*      */       }
/*      */     }
/* 1172 */     for (int i = 0; i < segments.length; i++)
/* 1173 */       segments[i].lock();
/* 1174 */     boolean found = false;
/*      */     try {
/* 1176 */       for (int i = 0; i < segments.length; i++)
/* 1177 */         if (segments[i].containsValue(value)) {
/* 1178 */           found = true;
/* 1179 */           break;
/*      */         }
/*      */     } finally {
/*      */       int i;
/* 1183 */       for (int i = 0; i < segments.length; i++)
/* 1184 */         segments[i].unlock();
/*      */     }
/* 1186 */     return found;
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
/*      */   public boolean contains(Object value)
/*      */   {
/* 1205 */     return containsValue(value);
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
/*      */   public V put(K key, V value)
/*      */   {
/* 1223 */     if (value == null)
/* 1224 */       throw new NullPointerException();
/* 1225 */     int hash = hashOf(key);
/* 1226 */     return (V)segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public V putIfAbsent(K key, V value)
/*      */   {
/* 1238 */     if (value == null)
/* 1239 */       throw new NullPointerException();
/* 1240 */     int hash = hashOf(key);
/* 1241 */     return (V)segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void putAll(Map<? extends K, ? extends V> m)
/*      */   {
/* 1253 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 1254 */       put(e.getKey(), e.getValue());
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
/*      */   public V remove(Object key)
/*      */   {
/* 1268 */     int hash = hashOf(key);
/* 1269 */     return (V)segmentFor(hash).remove(key, hash, null, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean remove(Object key, Object value)
/*      */   {
/* 1279 */     int hash = hashOf(key);
/* 1280 */     if (value == null)
/* 1281 */       return false;
/* 1282 */     return segmentFor(hash).remove(key, hash, value, false) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean replace(K key, V oldValue, V newValue)
/*      */   {
/* 1292 */     if ((oldValue == null) || (newValue == null))
/* 1293 */       throw new NullPointerException();
/* 1294 */     int hash = hashOf(key);
/* 1295 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public V replace(K key, V value)
/*      */   {
/* 1307 */     if (value == null)
/* 1308 */       throw new NullPointerException();
/* 1309 */     int hash = hashOf(key);
/* 1310 */     return (V)segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */   {
/* 1318 */     for (int i = 0; i < this.segments.length; i++) {
/* 1319 */       this.segments[i].clear();
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
/*      */   public void purgeStaleEntries()
/*      */   {
/* 1334 */     for (int i = 0; i < this.segments.length; i++) {
/* 1335 */       this.segments[i].removeStale();
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
/*      */   public Set<K> keySet()
/*      */   {
/* 1357 */     Set<K> ks = this.keySet;
/* 1358 */     return ks != null ? ks : (this.keySet = new KeySet());
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
/*      */   public Collection<V> values()
/*      */   {
/* 1379 */     Collection<V> vs = this.values;
/* 1380 */     return vs != null ? vs : (this.values = new Values());
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
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 1401 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 1402 */     return es != null ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<K> keys()
/*      */   {
/* 1412 */     return new KeyIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<V> elements()
/*      */   {
/* 1422 */     return new ValueIterator();
/*      */   }
/*      */   
/*      */   abstract class HashIterator
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V>[] currentTable;
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V> nextEntry;
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V> lastReturned;
/*      */     K currentKey;
/*      */     
/*      */     HashIterator()
/*      */     {
/* 1436 */       this.nextSegmentIndex = (ConcurrentReferenceHashMap.this.segments.length - 1);
/* 1437 */       this.nextTableIndex = -1;
/* 1438 */       advance();
/*      */     }
/*      */     
/* 1441 */     public boolean hasMoreElements() { return hasNext(); }
/*      */     
/*      */     final void advance() {
/* 1444 */       if ((this.nextEntry != null) && ((this.nextEntry = this.nextEntry.next) != null)) {
/* 1445 */         return;
/*      */       }
/* 1447 */       while (this.nextTableIndex >= 0) {
/* 1448 */         if ((this.nextEntry = this.currentTable[(this.nextTableIndex--)]) != null) {
/* 1449 */           return;
/*      */         }
/*      */       }
/* 1452 */       while (this.nextSegmentIndex >= 0) {
/* 1453 */         ConcurrentReferenceHashMap.Segment<K, V> seg = ConcurrentReferenceHashMap.this.segments[(this.nextSegmentIndex--)];
/* 1454 */         if (seg.count != 0) {
/* 1455 */           this.currentTable = seg.table;
/* 1456 */           for (int j = this.currentTable.length - 1; j >= 0; j--) {
/* 1457 */             if ((this.nextEntry = this.currentTable[j]) != null) {
/* 1458 */               this.nextTableIndex = (j - 1);
/* 1459 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1467 */       while (this.nextEntry != null) {
/* 1468 */         if (this.nextEntry.key() != null)
/* 1469 */           return true;
/* 1470 */         advance();
/*      */       }
/*      */       
/* 1473 */       return false;
/*      */     }
/*      */     
/*      */     ConcurrentReferenceHashMap.HashEntry<K, V> nextEntry() {
/*      */       do {
/* 1478 */         if (this.nextEntry == null) {
/* 1479 */           throw new NoSuchElementException();
/*      */         }
/* 1481 */         this.lastReturned = this.nextEntry;
/* 1482 */         this.currentKey = this.lastReturned.key();
/* 1483 */         advance();
/* 1484 */       } while (this.currentKey == null);
/*      */       
/* 1486 */       return this.lastReturned;
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1490 */       if (this.lastReturned == null)
/* 1491 */         throw new IllegalStateException();
/* 1492 */       ConcurrentReferenceHashMap.this.remove(this.currentKey);
/* 1493 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/* 1497 */   final class KeyIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<K>, Enumeration<K> { KeyIterator() { super(); }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1502 */     public K next() { return (K)super.nextEntry().key(); }
/*      */     
/* 1504 */     public K nextElement() { return (K)super.nextEntry().key(); }
/*      */   }
/*      */   
/* 1507 */   final class ValueIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<V>, Enumeration<V> { ValueIterator() { super(); }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1512 */     public V next() { return (V)super.nextEntry().value(); }
/*      */     
/* 1514 */     public V nextElement() { return (V)super.nextEntry().value(); }
/*      */   }
/*      */   
/*      */ 
/*      */   static class SimpleEntry<K, V>
/*      */     implements Map.Entry<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -8499721149061103585L;
/*      */     
/*      */     private final K key;
/*      */     private V value;
/*      */     
/*      */     public SimpleEntry(K key, V value)
/*      */     {
/* 1528 */       this.key = key;
/* 1529 */       this.value = value;
/*      */     }
/*      */     
/*      */     public SimpleEntry(Map.Entry<? extends K, ? extends V> entry) {
/* 1533 */       this.key = entry.getKey();
/* 1534 */       this.value = entry.getValue();
/*      */     }
/*      */     
/*      */     public K getKey()
/*      */     {
/* 1539 */       return (K)this.key;
/*      */     }
/*      */     
/*      */     public V getValue()
/*      */     {
/* 1544 */       return (V)this.value;
/*      */     }
/*      */     
/*      */     public V setValue(V value)
/*      */     {
/* 1549 */       V oldValue = this.value;
/* 1550 */       this.value = value;
/* 1551 */       return oldValue;
/*      */     }
/*      */     
/*      */     public boolean equals(Object o)
/*      */     {
/* 1556 */       if (!(o instanceof Map.Entry))
/* 1557 */         return false;
/* 1558 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 1559 */       return (eq(this.key, e.getKey())) && (eq(this.value, e.getValue()));
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/* 1564 */       return 
/* 1565 */         (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1570 */       return this.key + "=" + this.value;
/*      */     }
/*      */     
/*      */     private static boolean eq(Object o1, Object o2) {
/* 1574 */       return o1 == null ? false : o2 == null ? true : o1.equals(o2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   final class WriteThroughEntry
/*      */     extends ConcurrentReferenceHashMap.SimpleEntry<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = -7900634345345313646L;
/*      */     
/*      */ 
/*      */     WriteThroughEntry(V k)
/*      */     {
/* 1588 */       super(v);
/*      */     }
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
/*      */     public V setValue(V value)
/*      */     {
/* 1602 */       if (value == null) throw new NullPointerException();
/* 1603 */       V v = super.setValue(value);
/* 1604 */       ConcurrentReferenceHashMap.this.put(getKey(), value);
/* 1605 */       return v;
/*      */     }
/*      */   }
/*      */   
/* 1609 */   final class EntryIterator extends ConcurrentReferenceHashMap<K, V>.HashIterator implements Iterator<Map.Entry<K, V>> { EntryIterator() { super(); }
/*      */     
/*      */ 
/*      */ 
/*      */     public Map.Entry<K, V> next()
/*      */     {
/* 1615 */       ConcurrentReferenceHashMap.HashEntry<K, V> e = super.nextEntry();
/* 1616 */       return new ConcurrentReferenceHashMap.WriteThroughEntry(ConcurrentReferenceHashMap.this, e.key(), e.value());
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeySet extends AbstractSet<K> {
/*      */     KeySet() {}
/*      */     
/* 1623 */     public Iterator<K> iterator() { return new ConcurrentReferenceHashMap.KeyIterator(ConcurrentReferenceHashMap.this); }
/*      */     
/*      */     public int size()
/*      */     {
/* 1627 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1631 */       return ConcurrentReferenceHashMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/* 1635 */       return ConcurrentReferenceHashMap.this.containsKey(o);
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1639 */       return ConcurrentReferenceHashMap.this.remove(o) != null;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1643 */       ConcurrentReferenceHashMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   final class Values extends AbstractCollection<V> {
/*      */     Values() {}
/*      */     
/* 1650 */     public Iterator<V> iterator() { return new ConcurrentReferenceHashMap.ValueIterator(ConcurrentReferenceHashMap.this); }
/*      */     
/*      */     public int size()
/*      */     {
/* 1654 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1658 */       return ConcurrentReferenceHashMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/* 1662 */       return ConcurrentReferenceHashMap.this.containsValue(o);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1666 */       ConcurrentReferenceHashMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
/*      */     EntrySet() {}
/*      */     
/* 1673 */     public Iterator<Map.Entry<K, V>> iterator() { return new ConcurrentReferenceHashMap.EntryIterator(ConcurrentReferenceHashMap.this); }
/*      */     
/*      */     public boolean contains(Object o)
/*      */     {
/* 1677 */       if (!(o instanceof Map.Entry))
/* 1678 */         return false;
/* 1679 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 1680 */       V v = ConcurrentReferenceHashMap.this.get(e.getKey());
/* 1681 */       return (v != null) && (v.equals(e.getValue()));
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1685 */       if (!(o instanceof Map.Entry))
/* 1686 */         return false;
/* 1687 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 1688 */       return ConcurrentReferenceHashMap.this.remove(e.getKey(), e.getValue());
/*      */     }
/*      */     
/*      */     public int size() {
/* 1692 */       return ConcurrentReferenceHashMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/* 1696 */       return ConcurrentReferenceHashMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1700 */       ConcurrentReferenceHashMap.this.clear();
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
/*      */   private void writeObject(ObjectOutputStream s)
/*      */     throws IOException
/*      */   {
/* 1718 */     s.defaultWriteObject();
/*      */     
/* 1720 */     for (int k = 0; k < this.segments.length; k++) {
/* 1721 */       Segment<K, V> seg = this.segments[k];
/* 1722 */       seg.lock();
/*      */       try {
/* 1724 */         HashEntry<K, V>[] tab = seg.table;
/* 1725 */         for (int i = 0; i < tab.length; i++)
/* 1726 */           for (HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
/* 1727 */             K key = e.key();
/* 1728 */             if (key != null)
/*      */             {
/*      */ 
/* 1731 */               s.writeObject(key);
/* 1732 */               s.writeObject(e.value());
/*      */             }
/*      */           }
/*      */       } finally {
/* 1736 */         seg.unlock();
/*      */       }
/*      */     }
/* 1739 */     s.writeObject(null);
/* 1740 */     s.writeObject(null);
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
/*      */   private void readObject(ObjectInputStream s)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1754 */     s.defaultReadObject();
/*      */     
/*      */ 
/* 1757 */     for (int i = 0; i < this.segments.length; i++) {
/* 1758 */       this.segments[i].setTable(new HashEntry[1]);
/*      */     }
/*      */     
/*      */     for (;;)
/*      */     {
/* 1763 */       K key = s.readObject();
/* 1764 */       V value = s.readObject();
/* 1765 */       if (key == null)
/*      */         break;
/* 1767 */       put(key, value);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\ConcurrentReferenceHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */