/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.GuardedBy;
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
/*      */ @GwtIncompatible
/*      */ class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
/*      */   final transient int segmentMask;
/*      */   final transient int segmentShift;
/*      */   final transient Segment<K, V, E, S>[] segments;
/*      */   final int concurrencyLevel;
/*      */   final Equivalence<Object> keyEquivalence;
/*      */   final transient InternalEntryHelper<K, V, E, S> entryHelper;
/*      */   
/*      */   private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper)
/*      */   {
/*  161 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  163 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  164 */     this.entryHelper = entryHelper;
/*      */     
/*  166 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*      */     
/*      */ 
/*      */ 
/*  170 */     int segmentShift = 0;
/*  171 */     int segmentCount = 1;
/*  172 */     while (segmentCount < this.concurrencyLevel) {
/*  173 */       segmentShift++;
/*  174 */       segmentCount <<= 1;
/*      */     }
/*  176 */     this.segmentShift = (32 - segmentShift);
/*  177 */     this.segmentMask = (segmentCount - 1);
/*      */     
/*  179 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  181 */     int segmentCapacity = initialCapacity / segmentCount;
/*  182 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  183 */       segmentCapacity++;
/*      */     }
/*      */     
/*  186 */     int segmentSize = 1;
/*  187 */     while (segmentSize < segmentCapacity) {
/*  188 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  191 */     for (int i = 0; i < this.segments.length; i++) {
/*  192 */       this.segments[i] = createSegment(segmentSize, -1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder)
/*      */   {
/*  199 */     if ((builder.getKeyStrength() == Strength.STRONG) && 
/*  200 */       (builder.getValueStrength() == Strength.STRONG)) {
/*  201 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  203 */         MapMakerInternalMap.StrongKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  205 */     if ((builder.getKeyStrength() == Strength.STRONG) && 
/*  206 */       (builder.getValueStrength() == Strength.WEAK)) {
/*  207 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  209 */         MapMakerInternalMap.StrongKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  211 */     if ((builder.getKeyStrength() == Strength.WEAK) && 
/*  212 */       (builder.getValueStrength() == Strength.STRONG)) {
/*  213 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  215 */         MapMakerInternalMap.WeakKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  217 */     if ((builder.getKeyStrength() == Strength.WEAK) && (builder.getValueStrength() == Strength.WEAK)) {
/*  218 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  220 */         MapMakerInternalMap.WeakKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  222 */     throw new AssertionError();
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
/*      */   static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker builder)
/*      */   {
/*  238 */     if ((builder.getKeyStrength() == Strength.STRONG) && 
/*  239 */       (builder.getValueStrength() == Strength.STRONG)) {
/*  240 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  242 */         MapMakerInternalMap.StrongKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  244 */     if ((builder.getKeyStrength() == Strength.WEAK) && 
/*  245 */       (builder.getValueStrength() == Strength.STRONG)) {
/*  246 */       return new MapMakerInternalMap(builder, 
/*      */       
/*  248 */         MapMakerInternalMap.WeakKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  250 */     if (builder.getValueStrength() == Strength.WEAK) {
/*  251 */       throw new IllegalArgumentException("Map cannot have both weak and dummy values");
/*      */     }
/*  253 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   static abstract enum Strength {
/*  257 */     STRONG, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */     WEAK;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Strength() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract Equivalence<Object> defaultEquivalence();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface InternalEntryHelper<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
/*      */   {
/*      */     public abstract MapMakerInternalMap.Strength keyStrength();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract MapMakerInternalMap.Strength valueStrength();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract S newSegment(MapMakerInternalMap<K, V, E, S> paramMapMakerInternalMap, int paramInt1, int paramInt2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract E newEntry(S paramS, K paramK, int paramInt, @Nullable E paramE);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract E copy(S paramS, E paramE1, @Nullable E paramE2);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract void setValue(S paramS, E paramE, V paramV);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface InternalEntry<K, V, E extends InternalEntry<K, V, E>>
/*      */   {
/*      */     public abstract E getNext();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract int getHash();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract K getKey();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract V getValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract class AbstractStrongKeyEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
/*      */     implements MapMakerInternalMap.InternalEntry<K, V, E>
/*      */   {
/*      */     final K key;
/*      */     
/*      */ 
/*      */ 
/*      */     final int hash;
/*      */     
/*      */ 
/*      */ 
/*      */     final E next;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     AbstractStrongKeyEntry(K key, int hash, @Nullable E next)
/*      */     {
/*  361 */       this.key = key;
/*  362 */       this.hash = hash;
/*  363 */       this.next = next;
/*      */     }
/*      */     
/*      */     public K getKey()
/*      */     {
/*  368 */       return (K)this.key;
/*      */     }
/*      */     
/*      */     public int getHash()
/*      */     {
/*  373 */       return this.hash;
/*      */     }
/*      */     
/*      */     public E getNext()
/*      */     {
/*  378 */       return this.next;
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
/*      */   static abstract interface StrongValueEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
/*      */     extends MapMakerInternalMap.InternalEntry<K, V, E>
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  401 */   static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() { return UNSET_WEAK_VALUE_REFERENCE; }
/*      */   
/*      */   static abstract interface WeakValueEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends MapMakerInternalMap.InternalEntry<K, V, E> { public abstract MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
/*      */     
/*      */     public abstract void clearValue(); }
/*      */   
/*      */   static final class StrongKeyStrongValueEntry<K, V> extends MapMakerInternalMap.AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements MapMakerInternalMap.StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> { @Nullable
/*  408 */     private volatile V value = null;
/*      */     
/*      */     StrongKeyStrongValueEntry(K key, int hash, @Nullable StrongKeyStrongValueEntry<K, V> next) {
/*  411 */       super(hash, next);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public V getValue()
/*      */     {
/*  417 */       return (V)this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  421 */       this.value = value;
/*      */     }
/*      */     
/*      */     StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
/*  425 */       StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(this.key, this.hash, newNext);
/*      */       
/*  427 */       newEntry.value = this.value;
/*  428 */       return newEntry;
/*      */     }
/*      */     
/*      */ 
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
/*      */     {
/*  435 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */       
/*      */       static <K, V> Helper<K, V> instance()
/*      */       {
/*  439 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  444 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  449 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  459 */         return new MapMakerInternalMap.StrongKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext)
/*      */       {
/*  467 */         return entry.copy(newNext);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value)
/*      */       {
/*  475 */         entry.setValue(value);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
/*      */       {
/*  484 */         return new MapMakerInternalMap.StrongKeyStrongValueEntry(key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static final class StrongKeyWeakValueEntry<K, V>
/*      */     extends MapMakerInternalMap.AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>>
/*      */     implements MapMakerInternalMap.WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>>
/*      */   {
/*  494 */     private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     StrongKeyWeakValueEntry(K key, int hash, @Nullable StrongKeyWeakValueEntry<K, V> next) {
/*  497 */       super(hash, next);
/*      */     }
/*      */     
/*      */     public V getValue()
/*      */     {
/*  502 */       return (V)this.valueReference.get();
/*      */     }
/*      */     
/*      */     public void clearValue()
/*      */     {
/*  507 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  511 */       MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  512 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, this);
/*      */       
/*      */ 
/*  515 */       previous.clear();
/*      */     }
/*      */     
/*      */     StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext)
/*      */     {
/*  520 */       StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(this.key, this.hash, newNext);
/*      */       
/*  522 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  523 */       return newEntry;
/*      */     }
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference()
/*      */     {
/*  528 */       return this.valueReference;
/*      */     }
/*      */     
/*      */ 
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
/*      */     {
/*  535 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */       
/*      */       static <K, V> Helper<K, V> instance()
/*      */       {
/*  539 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  544 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  549 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  558 */         return new MapMakerInternalMap.StrongKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext)
/*      */       {
/*  566 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  567 */           return null;
/*      */         }
/*  569 */         return entry.copy(segment.queueForValues, newNext);
/*      */       }
/*      */       
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value)
/*      */       {
/*  575 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
/*      */       {
/*  584 */         return new MapMakerInternalMap.StrongKeyWeakValueEntry(key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyDummyValueEntry<K>
/*      */     extends MapMakerInternalMap.AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements MapMakerInternalMap.StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>>
/*      */   {
/*      */     StrongKeyDummyValueEntry(K key, int hash, @Nullable StrongKeyDummyValueEntry<K> next)
/*      */     {
/*  594 */       super(hash, next);
/*      */     }
/*      */     
/*      */     public MapMaker.Dummy getValue()
/*      */     {
/*  599 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueEntry<K> newNext) {
/*  605 */       return new StrongKeyDummyValueEntry(this.key, this.hash, newNext);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>>
/*      */     {
/*  615 */       private static final Helper<?> INSTANCE = new Helper();
/*      */       
/*      */       static <K> Helper<K> instance()
/*      */       {
/*  619 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  624 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  629 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  638 */         return new MapMakerInternalMap.StrongKeyDummyValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, @Nullable MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext)
/*      */       {
/*  646 */         return entry.copy(newNext);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyDummyValueEntry<K> next)
/*      */       {
/*  659 */         return new MapMakerInternalMap.StrongKeyDummyValueEntry(key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class AbstractWeakKeyEntry<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends WeakReference<K> implements MapMakerInternalMap.InternalEntry<K, V, E>
/*      */   {
/*      */     final int hash;
/*      */     final E next;
/*      */     
/*      */     AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable E next)
/*      */     {
/*  671 */       super(queue);
/*  672 */       this.hash = hash;
/*  673 */       this.next = next;
/*      */     }
/*      */     
/*      */     public K getKey()
/*      */     {
/*  678 */       return (K)get();
/*      */     }
/*      */     
/*      */     public int getHash()
/*      */     {
/*  683 */       return this.hash;
/*      */     }
/*      */     
/*      */     public E getNext()
/*      */     {
/*  688 */       return this.next;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyDummyValueEntry<K>
/*      */     extends MapMakerInternalMap.AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */     implements MapMakerInternalMap.StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */   {
/*      */     WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyDummyValueEntry<K> next)
/*      */     {
/*  698 */       super(key, hash, next);
/*      */     }
/*      */     
/*      */     public MapMaker.Dummy getValue()
/*      */     {
/*  703 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     WeakKeyDummyValueEntry<K> copy(ReferenceQueue<K> queueForKeys, WeakKeyDummyValueEntry<K> newNext)
/*      */     {
/*  710 */       return new WeakKeyDummyValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>>
/*      */     {
/*  720 */       private static final Helper<?> INSTANCE = new Helper();
/*      */       
/*      */       static <K> Helper<K> instance()
/*      */       {
/*  724 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  729 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  734 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  742 */         return new MapMakerInternalMap.WeakKeyDummyValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, @Nullable MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext)
/*      */       {
/*  750 */         if (entry.getKey() == null)
/*      */         {
/*  752 */           return null;
/*      */         }
/*  754 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyDummyValueEntry<K> next)
/*      */       {
/*  767 */         return new MapMakerInternalMap.WeakKeyDummyValueEntry(segment.queueForKeys, key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueEntry<K, V>
/*      */     extends MapMakerInternalMap.AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements MapMakerInternalMap.StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>>
/*      */   {
/*      */     @Nullable
/*  776 */     private volatile V value = null;
/*      */     
/*      */     WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyStrongValueEntry<K, V> next)
/*      */     {
/*  780 */       super(key, hash, next);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public V getValue()
/*      */     {
/*  786 */       return (V)this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  790 */       this.value = value;
/*      */     }
/*      */     
/*      */ 
/*      */     WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext)
/*      */     {
/*  796 */       WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  797 */       newEntry.setValue(this.value);
/*  798 */       return newEntry;
/*      */     }
/*      */     
/*      */ 
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
/*      */     {
/*  805 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */       
/*      */       static <K, V> Helper<K, V> instance()
/*      */       {
/*  809 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  814 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  819 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  828 */         return new MapMakerInternalMap.WeakKeyStrongValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext)
/*      */       {
/*  836 */         if (entry.getKey() == null)
/*      */         {
/*  838 */           return null;
/*      */         }
/*  840 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */       
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value)
/*      */       {
/*  846 */         entry.setValue(value);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
/*      */       {
/*  855 */         return new MapMakerInternalMap.WeakKeyStrongValueEntry(segment.queueForKeys, key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static final class WeakKeyWeakValueEntry<K, V>
/*      */     extends MapMakerInternalMap.AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>>
/*      */     implements MapMakerInternalMap.WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>>
/*      */   {
/*  865 */     private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyWeakValueEntry<K, V> next)
/*      */     {
/*  869 */       super(key, hash, next);
/*      */     }
/*      */     
/*      */     public V getValue()
/*      */     {
/*  874 */       return (V)this.valueReference.get();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext)
/*      */     {
/*  882 */       WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  883 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  884 */       return newEntry;
/*      */     }
/*      */     
/*      */     public void clearValue()
/*      */     {
/*  889 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  893 */       MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  894 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl(queueForValues, value, this);
/*      */       
/*      */ 
/*  897 */       previous.clear();
/*      */     }
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference()
/*      */     {
/*  902 */       return this.valueReference;
/*      */     }
/*      */     
/*      */ 
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
/*      */     {
/*  909 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */       
/*      */       static <K, V> Helper<K, V> instance()
/*      */       {
/*  913 */         return INSTANCE;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength()
/*      */       {
/*  918 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength()
/*      */       {
/*  923 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */       {
/*  931 */         return new MapMakerInternalMap.WeakKeyWeakValueSegment(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext)
/*      */       {
/*  939 */         if (entry.getKey() == null)
/*      */         {
/*  941 */           return null;
/*      */         }
/*  943 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  944 */           return null;
/*      */         }
/*  946 */         return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */       }
/*      */       
/*      */ 
/*      */       public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value)
/*      */       {
/*  952 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
/*      */       {
/*  961 */         return new MapMakerInternalMap.WeakKeyWeakValueEntry(segment.queueForKeys, key, hash, next);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static abstract interface WeakValueReference<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>>
/*      */   {
/*      */     @Nullable
/*      */     public abstract V get();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public abstract E getEntry();
/*      */     
/*      */ 
/*      */ 
/*      */     public abstract void clear();
/*      */     
/*      */ 
/*      */ 
/*      */     public abstract WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> paramReferenceQueue, E paramE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static final class DummyInternalEntry
/*      */     implements MapMakerInternalMap.InternalEntry<Object, Object, DummyInternalEntry>
/*      */   {
/*      */     private DummyInternalEntry()
/*      */     {
/*  995 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     public DummyInternalEntry getNext()
/*      */     {
/* 1000 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     public int getHash()
/*      */     {
/* 1005 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     public Object getKey()
/*      */     {
/* 1010 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     public Object getValue()
/*      */     {
/* 1015 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1023 */   static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference()
/*      */   {
/*      */     public MapMakerInternalMap.DummyInternalEntry getEntry()
/*      */     {
/* 1027 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public void clear() {}
/*      */     
/*      */     public Object get()
/*      */     {
/* 1035 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1041 */     public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) { return this; }
/*      */   };
/*      */   transient Set<K> keySet;
/*      */   transient Collection<V> values;
/*      */   transient Set<Map.Entry<K, V>> entrySet;
/*      */   private static final long serialVersionUID = 5L;
/*      */   
/*      */   static final class WeakValueReferenceImpl<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> extends WeakReference<V> implements MapMakerInternalMap.WeakValueReference<K, V, E> { @Weak
/*      */     final E entry;
/*      */     
/* 1051 */     WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) { super(queue);
/* 1052 */       this.entry = entry;
/*      */     }
/*      */     
/*      */     public E getEntry()
/*      */     {
/* 1057 */       return this.entry;
/*      */     }
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry)
/*      */     {
/* 1062 */       return new WeakValueReferenceImpl(queue, get(), entry);
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
/*      */   static int rehash(int h)
/*      */   {
/* 1078 */     h += (h << 15 ^ 0xCD7D);
/* 1079 */     h ^= h >>> 10;
/* 1080 */     h += (h << 3);
/* 1081 */     h ^= h >>> 6;
/* 1082 */     h += (h << 2) + (h << 14);
/* 1083 */     return h ^ h >>> 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @VisibleForTesting
/*      */   E copyEntry(E original, E newNext)
/*      */   {
/* 1092 */     int hash = original.getHash();
/* 1093 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1097 */     int h = this.keyEquivalence.hash(key);
/* 1098 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(WeakValueReference<K, V, E> valueReference) {
/* 1102 */     E entry = valueReference.getEntry();
/* 1103 */     int hash = entry.getHash();
/* 1104 */     segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(E entry) {
/* 1108 */     int hash = entry.getHash();
/* 1109 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @VisibleForTesting
/*      */   boolean isLiveForTesting(InternalEntry<K, V, ?> entry)
/*      */   {
/* 1118 */     return segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Segment<K, V, E, S> segmentFor(int hash)
/*      */   {
/* 1129 */     return this.segments[(hash >>> this.segmentShift & this.segmentMask)];
/*      */   }
/*      */   
/*      */   Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
/* 1133 */     return this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   V getLiveValue(E entry)
/*      */   {
/* 1141 */     if (entry.getKey() == null) {
/* 1142 */       return null;
/*      */     }
/* 1144 */     V value = entry.getValue();
/* 1145 */     if (value == null) {
/* 1146 */       return null;
/*      */     }
/* 1148 */     return value;
/*      */   }
/*      */   
/*      */   final Segment<K, V, E, S>[] newSegmentArray(int ssize)
/*      */   {
/* 1153 */     return new Segment[ssize];
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
/*      */   static abstract class Segment<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final MapMakerInternalMap<K, V, E, S> map;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     volatile int count;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int modCount;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     int threshold;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     volatile AtomicReferenceArray<E> table;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     final int maxSegmentSize;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1229 */     final AtomicInteger readCount = new AtomicInteger();
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
/* 1232 */       this.map = map;
/* 1233 */       this.maxSegmentSize = maxSegmentSize;
/* 1234 */       initTable(newEntryArray(initialCapacity));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     abstract S self();
/*      */     
/*      */ 
/*      */ 
/*      */     @GuardedBy("this")
/*      */     void maybeDrainReferenceQueues() {}
/*      */     
/*      */ 
/*      */ 
/*      */     void maybeClearReferenceQueues() {}
/*      */     
/*      */ 
/*      */ 
/*      */     void setValue(E entry, V value)
/*      */     {
/* 1254 */       this.map.entryHelper.setValue(self(), entry, value);
/*      */     }
/*      */     
/*      */     E copyEntry(E original, E newNext)
/*      */     {
/* 1259 */       return this.map.entryHelper.copy(self(), original, newNext);
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<E> newEntryArray(int size) {
/* 1263 */       return new AtomicReferenceArray(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<E> newTable) {
/* 1267 */       this.threshold = (newTable.length() * 3 / 4);
/* 1268 */       if (this.threshold == this.maxSegmentSize)
/*      */       {
/* 1270 */         this.threshold += 1;
/*      */       }
/* 1272 */       this.table = newTable;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> paramInternalEntry);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting()
/*      */     {
/* 1288 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting()
/*      */     {
/* 1293 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 1298 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value)
/*      */     {
/* 1307 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
/*      */     {
/* 1317 */       throw new AssertionError();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 1324 */       this.table.set(i, castForTesting(entry));
/*      */     }
/*      */     
/*      */     E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> newNext)
/*      */     {
/* 1329 */       return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
/*      */     }
/*      */     
/*      */     void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value)
/*      */     {
/* 1334 */       this.map.entryHelper.setValue(self(), castForTesting(entry), value);
/*      */     }
/*      */     
/*      */     E newEntryForTesting(K key, int hash, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> next)
/*      */     {
/* 1339 */       return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 1345 */       return removeEntryForTesting(castForTesting(entry));
/*      */     }
/*      */     
/*      */     E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 1350 */       return removeFromChain(castForTesting(first), castForTesting(entry));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @Nullable
/*      */     V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 1358 */       return (V)getLiveValue(castForTesting(entry));
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     void tryDrainReferenceQueues()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual 27	com/google/common/collect/MapMakerInternalMap$Segment:tryLock	()Z
/*      */       //   4: ifeq +21 -> 25
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 28	com/google/common/collect/MapMakerInternalMap$Segment:maybeDrainReferenceQueues	()V
/*      */       //   11: aload_0
/*      */       //   12: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   15: goto +10 -> 25
/*      */       //   18: astore_1
/*      */       //   19: aload_0
/*      */       //   20: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   23: aload_1
/*      */       //   24: athrow
/*      */       //   25: return
/*      */       // Line number table:
/*      */       //   Java source line #1367	-> byte code offset #0
/*      */       //   Java source line #1369	-> byte code offset #7
/*      */       //   Java source line #1371	-> byte code offset #11
/*      */       //   Java source line #1372	-> byte code offset #15
/*      */       //   Java source line #1371	-> byte code offset #18
/*      */       //   Java source line #1374	-> byte code offset #25
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	26	0	this	Segment<K, V, E, S>
/*      */       //   18	6	1	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	11	18	finally
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue)
/*      */     {
/* 1379 */       int i = 0;
/* 1380 */       Reference<? extends K> ref; for (; (ref = keyReferenceQueue.poll()) != null; 
/*      */           
/*      */ 
/*      */ 
/* 1384 */           i == 16)
/*      */       {
/* 1382 */         E entry = (MapMakerInternalMap.InternalEntry)ref;
/* 1383 */         this.map.reclaimKey(entry);
/* 1384 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue)
/*      */     {
/* 1393 */       int i = 0;
/* 1394 */       Reference<? extends V> ref; for (; (ref = valueReferenceQueue.poll()) != null; 
/*      */           
/*      */ 
/*      */ 
/* 1398 */           i == 16)
/*      */       {
/* 1396 */         MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
/* 1397 */         this.map.reclaimValue(valueReference);
/* 1398 */         i++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue)
/*      */     {
/* 1405 */       while (referenceQueue.poll() != null) {}
/*      */     }
/*      */     
/*      */ 
/*      */     E getFirst(int hash)
/*      */     {
/* 1411 */       AtomicReferenceArray<E> table = this.table;
/* 1412 */       return (MapMakerInternalMap.InternalEntry)table.get(hash & table.length() - 1);
/*      */     }
/*      */     
/*      */ 
/*      */     E getEntry(Object key, int hash)
/*      */     {
/* 1418 */       if (this.count != 0) {
/* 1419 */         for (E e = getFirst(hash); e != null; e = e.getNext()) {
/* 1420 */           if (e.getHash() == hash)
/*      */           {
/*      */ 
/*      */ 
/* 1424 */             K entryKey = e.getKey();
/* 1425 */             if (entryKey == null) {
/* 1426 */               tryDrainReferenceQueues();
/*      */ 
/*      */ 
/*      */             }
/* 1430 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1431 */               return e;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1436 */       return null;
/*      */     }
/*      */     
/*      */     E getLiveEntry(Object key, int hash) {
/* 1440 */       return getEntry(key, hash);
/*      */     }
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 1445 */         E e = getLiveEntry(key, hash);
/* 1446 */         if (e == null) {
/* 1447 */           return null;
/*      */         }
/*      */         
/* 1450 */         Object value = e.getValue();
/* 1451 */         if (value == null) {
/* 1452 */           tryDrainReferenceQueues();
/*      */         }
/* 1454 */         return (V)value;
/*      */       } finally {
/* 1456 */         postReadCleanup();
/*      */       }
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try { E e;
/* 1462 */         if (this.count != 0) {
/* 1463 */           e = getLiveEntry(key, hash);
/* 1464 */           return (e != null) && (e.getValue() != null);
/*      */         }
/*      */         
/* 1467 */         return 0;
/*      */       } finally {
/* 1469 */         postReadCleanup();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value)
/*      */     {
/*      */       try
/*      */       {
/*      */         AtomicReferenceArray<E> table;
/* 1480 */         if (this.count != 0) {
/* 1481 */           table = this.table;
/* 1482 */           int length = table.length();
/* 1483 */           for (int i = 0; i < length; i++) {
/* 1484 */             for (E e = (MapMakerInternalMap.InternalEntry)table.get(i); e != null; e = e.getNext()) {
/* 1485 */               V entryValue = getLiveValue(e);
/* 1486 */               if (entryValue != null)
/*      */               {
/*      */ 
/* 1489 */                 if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1490 */                   return true;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/* 1496 */         return 0;
/*      */       } finally {
/* 1498 */         postReadCleanup();
/*      */       }
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 1503 */       lock();
/*      */       try {
/* 1505 */         preWriteCleanup();
/*      */         
/* 1507 */         int newCount = this.count + 1;
/* 1508 */         if (newCount > this.threshold) {
/* 1509 */           expand();
/* 1510 */           newCount = this.count + 1;
/*      */         }
/*      */         
/* 1513 */         AtomicReferenceArray<E> table = this.table;
/* 1514 */         int index = hash & table.length() - 1;
/* 1515 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/*      */         K entryKey;
/* 1518 */         for (E e = first; e != null; e = e.getNext()) {
/* 1519 */           entryKey = e.getKey();
/* 1520 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1522 */             if (this.map.keyEquivalence.equivalent(key, entryKey))
/*      */             {
/*      */ 
/* 1525 */               V entryValue = e.getValue();
/*      */               V ?;
/* 1527 */               if (entryValue == null) {
/* 1528 */                 this.modCount += 1;
/* 1529 */                 setValue(e, value);
/* 1530 */                 newCount = this.count;
/* 1531 */                 this.count = newCount;
/* 1532 */                 return null; }
/* 1533 */               if (onlyIfAbsent)
/*      */               {
/*      */ 
/*      */ 
/* 1537 */                 return entryValue;
/*      */               }
/*      */               
/* 1540 */               this.modCount += 1;
/* 1541 */               setValue(e, value);
/* 1542 */               return entryValue;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1548 */         this.modCount += 1;
/* 1549 */         E newEntry = this.map.entryHelper.newEntry(self(), key, hash, first);
/* 1550 */         setValue(newEntry, value);
/* 1551 */         table.set(index, newEntry);
/* 1552 */         this.count = newCount;
/* 1553 */         return null;
/*      */       } finally {
/* 1555 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     @GuardedBy("this")
/*      */     void expand()
/*      */     {
/* 1564 */       AtomicReferenceArray<E> oldTable = this.table;
/* 1565 */       int oldCapacity = oldTable.length();
/* 1566 */       if (oldCapacity >= 1073741824) {
/* 1567 */         return;
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
/* 1580 */       int newCount = this.count;
/* 1581 */       AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
/* 1582 */       this.threshold = (newTable.length() * 3 / 4);
/* 1583 */       int newMask = newTable.length() - 1;
/* 1584 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++)
/*      */       {
/*      */ 
/* 1587 */         E head = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
/*      */         
/* 1589 */         if (head != null) {
/* 1590 */           E next = head.getNext();
/* 1591 */           int headIndex = head.getHash() & newMask;
/*      */           
/*      */ 
/* 1594 */           if (next == null) {
/* 1595 */             newTable.set(headIndex, head);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1600 */             E tail = head;
/* 1601 */             int tailIndex = headIndex;
/* 1602 */             for (E e = next; e != null; e = e.getNext()) {
/* 1603 */               int newIndex = e.getHash() & newMask;
/* 1604 */               if (newIndex != tailIndex)
/*      */               {
/* 1606 */                 tailIndex = newIndex;
/* 1607 */                 tail = e;
/*      */               }
/*      */             }
/* 1610 */             newTable.set(tailIndex, tail);
/*      */             
/*      */ 
/* 1613 */             for (E e = head; e != tail; e = e.getNext()) {
/* 1614 */               int newIndex = e.getHash() & newMask;
/* 1615 */               E newNext = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
/* 1616 */               E newFirst = copyEntry(e, newNext);
/* 1617 */               if (newFirst != null) {
/* 1618 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 1620 */                 newCount--;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1626 */       this.table = newTable;
/* 1627 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 1631 */       lock();
/*      */       try {
/* 1633 */         preWriteCleanup();
/*      */         
/* 1635 */         AtomicReferenceArray<E> table = this.table;
/* 1636 */         int index = hash & table.length() - 1;
/* 1637 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1639 */         for (E e = first; e != null; e = e.getNext()) {
/* 1640 */           K entryKey = e.getKey();
/* 1641 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1643 */             if (this.map.keyEquivalence.equivalent(key, entryKey))
/*      */             {
/*      */ 
/* 1646 */               V entryValue = e.getValue();
/* 1647 */               int newCount; if (entryValue == null) {
/* 1648 */                 if (isCollected(e)) {
/* 1649 */                   newCount = this.count - 1;
/* 1650 */                   this.modCount += 1;
/* 1651 */                   E newFirst = removeFromChain(first, e);
/* 1652 */                   newCount = this.count - 1;
/* 1653 */                   table.set(index, newFirst);
/* 1654 */                   this.count = newCount;
/*      */                 }
/* 1656 */                 return 0;
/*      */               }
/*      */               
/* 1659 */               if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
/* 1660 */                 this.modCount += 1;
/* 1661 */                 setValue(e, newValue);
/* 1662 */                 return 1;
/*      */               }
/*      */               
/*      */ 
/* 1666 */               return 0;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1671 */         return 0;
/*      */       } finally {
/* 1673 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 1678 */       lock();
/*      */       try {
/* 1680 */         preWriteCleanup();
/*      */         
/* 1682 */         AtomicReferenceArray<E> table = this.table;
/* 1683 */         int index = hash & table.length() - 1;
/* 1684 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1686 */         for (E e = first; e != null; e = e.getNext()) {
/* 1687 */           K entryKey = e.getKey();
/* 1688 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1690 */             if (this.map.keyEquivalence.equivalent(key, entryKey))
/*      */             {
/*      */ 
/* 1693 */               V entryValue = e.getValue();
/* 1694 */               int newCount; if (entryValue == null) {
/* 1695 */                 if (isCollected(e)) {
/* 1696 */                   newCount = this.count - 1;
/* 1697 */                   this.modCount += 1;
/* 1698 */                   E newFirst = removeFromChain(first, e);
/* 1699 */                   newCount = this.count - 1;
/* 1700 */                   table.set(index, newFirst);
/* 1701 */                   this.count = newCount;
/*      */                 }
/* 1703 */                 return null;
/*      */               }
/*      */               
/* 1706 */               this.modCount += 1;
/* 1707 */               setValue(e, newValue);
/* 1708 */               return entryValue;
/*      */             }
/*      */           }
/*      */         }
/* 1712 */         return null;
/*      */       } finally {
/* 1714 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V remove(Object key, int hash) {
/* 1720 */       lock();
/*      */       try {
/* 1722 */         preWriteCleanup();
/*      */         
/* 1724 */         int newCount = this.count - 1;
/* 1725 */         AtomicReferenceArray<E> table = this.table;
/* 1726 */         int index = hash & table.length() - 1;
/* 1727 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1729 */         for (E e = first; e != null; e = e.getNext()) {
/* 1730 */           K entryKey = e.getKey();
/* 1731 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1733 */             if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1734 */               V entryValue = e.getValue();
/*      */               
/* 1736 */               if (entryValue == null)
/*      */               {
/* 1738 */                 if (!isCollected(e))
/*      */                 {
/*      */ 
/* 1741 */                   return null;
/*      */                 }
/*      */               }
/* 1744 */               this.modCount += 1;
/* 1745 */               Object newFirst = removeFromChain(first, e);
/* 1746 */               newCount = this.count - 1;
/* 1747 */               table.set(index, newFirst);
/* 1748 */               this.count = newCount;
/* 1749 */               return entryValue;
/*      */             }
/*      */           }
/*      */         }
/* 1753 */         return null;
/*      */       } finally {
/* 1755 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 1760 */       lock();
/*      */       try {
/* 1762 */         preWriteCleanup();
/*      */         
/* 1764 */         int newCount = this.count - 1;
/* 1765 */         AtomicReferenceArray<E> table = this.table;
/* 1766 */         int index = hash & table.length() - 1;
/* 1767 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1769 */         for (E e = first; e != null; e = e.getNext()) {
/* 1770 */           K entryKey = e.getKey();
/* 1771 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1773 */             if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1774 */               V entryValue = e.getValue();
/*      */               
/* 1776 */               boolean explicitRemoval = false;
/* 1777 */               if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1778 */                 explicitRemoval = true;
/* 1779 */               } else if (!isCollected(e))
/*      */               {
/*      */ 
/* 1782 */                 return false;
/*      */               }
/*      */               
/* 1785 */               this.modCount += 1;
/* 1786 */               Object newFirst = removeFromChain(first, e);
/* 1787 */               newCount = this.count - 1;
/* 1788 */               table.set(index, newFirst);
/* 1789 */               this.count = newCount;
/* 1790 */               return explicitRemoval;
/*      */             }
/*      */           }
/*      */         }
/* 1794 */         return 0;
/*      */       } finally {
/* 1796 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     void clear()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: getfield 36	com/google/common/collect/MapMakerInternalMap$Segment:count	I
/*      */       //   4: ifeq +75 -> 79
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 49	com/google/common/collect/MapMakerInternalMap$Segment:lock	()V
/*      */       //   11: aload_0
/*      */       //   12: getfield 18	com/google/common/collect/MapMakerInternalMap$Segment:table	Ljava/util/concurrent/atomic/AtomicReferenceArray;
/*      */       //   15: astore_1
/*      */       //   16: iconst_0
/*      */       //   17: istore_2
/*      */       //   18: iload_2
/*      */       //   19: aload_1
/*      */       //   20: invokevirtual 16	java/util/concurrent/atomic/AtomicReferenceArray:length	()I
/*      */       //   23: if_icmpge +15 -> 38
/*      */       //   26: aload_1
/*      */       //   27: iload_2
/*      */       //   28: aconst_null
/*      */       //   29: invokevirtual 22	java/util/concurrent/atomic/AtomicReferenceArray:set	(ILjava/lang/Object;)V
/*      */       //   32: iinc 2 1
/*      */       //   35: goto -17 -> 18
/*      */       //   38: aload_0
/*      */       //   39: invokevirtual 57	com/google/common/collect/MapMakerInternalMap$Segment:maybeClearReferenceQueues	()V
/*      */       //   42: aload_0
/*      */       //   43: getfield 5	com/google/common/collect/MapMakerInternalMap$Segment:readCount	Ljava/util/concurrent/atomic/AtomicInteger;
/*      */       //   46: iconst_0
/*      */       //   47: invokevirtual 58	java/util/concurrent/atomic/AtomicInteger:set	(I)V
/*      */       //   50: aload_0
/*      */       //   51: dup
/*      */       //   52: getfield 52	com/google/common/collect/MapMakerInternalMap$Segment:modCount	I
/*      */       //   55: iconst_1
/*      */       //   56: iadd
/*      */       //   57: putfield 52	com/google/common/collect/MapMakerInternalMap$Segment:modCount	I
/*      */       //   60: aload_0
/*      */       //   61: iconst_0
/*      */       //   62: putfield 36	com/google/common/collect/MapMakerInternalMap$Segment:count	I
/*      */       //   65: aload_0
/*      */       //   66: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   69: goto +10 -> 79
/*      */       //   72: astore_3
/*      */       //   73: aload_0
/*      */       //   74: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   77: aload_3
/*      */       //   78: athrow
/*      */       //   79: return
/*      */       // Line number table:
/*      */       //   Java source line #1801	-> byte code offset #0
/*      */       //   Java source line #1802	-> byte code offset #7
/*      */       //   Java source line #1804	-> byte code offset #11
/*      */       //   Java source line #1805	-> byte code offset #16
/*      */       //   Java source line #1806	-> byte code offset #26
/*      */       //   Java source line #1805	-> byte code offset #32
/*      */       //   Java source line #1808	-> byte code offset #38
/*      */       //   Java source line #1809	-> byte code offset #42
/*      */       //   Java source line #1811	-> byte code offset #50
/*      */       //   Java source line #1812	-> byte code offset #60
/*      */       //   Java source line #1814	-> byte code offset #65
/*      */       //   Java source line #1815	-> byte code offset #69
/*      */       //   Java source line #1814	-> byte code offset #72
/*      */       //   Java source line #1817	-> byte code offset #79
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	80	0	this	Segment<K, V, E, S>
/*      */       //   15	12	1	table	AtomicReferenceArray<E>
/*      */       //   17	16	2	i	int
/*      */       //   72	6	3	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   11	65	72	finally
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     E removeFromChain(E first, E entry)
/*      */     {
/* 1833 */       int newCount = this.count;
/* 1834 */       E newFirst = entry.getNext();
/* 1835 */       for (E e = first; e != entry; e = e.getNext()) {
/* 1836 */         E next = copyEntry(e, newFirst);
/* 1837 */         if (next != null) {
/* 1838 */           newFirst = next;
/*      */         } else {
/* 1840 */           newCount--;
/*      */         }
/*      */       }
/* 1843 */       this.count = newCount;
/* 1844 */       return newFirst;
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(E entry, int hash)
/*      */     {
/* 1850 */       lock();
/*      */       try {
/* 1852 */         int newCount = this.count - 1;
/* 1853 */         AtomicReferenceArray<E> table = this.table;
/* 1854 */         int index = hash & table.length() - 1;
/* 1855 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1857 */         for (E e = first; e != null; e = e.getNext()) {
/* 1858 */           if (e == entry) {
/* 1859 */             this.modCount += 1;
/* 1860 */             E newFirst = removeFromChain(first, e);
/* 1861 */             newCount = this.count - 1;
/* 1862 */             table.set(index, newFirst);
/* 1863 */             this.count = newCount;
/* 1864 */             return true;
/*      */           }
/*      */         }
/*      */         
/* 1868 */         return 0;
/*      */       } finally {
/* 1870 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference)
/*      */     {
/* 1877 */       lock();
/*      */       try {
/* 1879 */         int newCount = this.count - 1;
/* 1880 */         AtomicReferenceArray<E> table = this.table;
/* 1881 */         int index = hash & table.length() - 1;
/* 1882 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1884 */         for (E e = first; e != null; e = e.getNext()) {
/* 1885 */           K entryKey = e.getKey();
/* 1886 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1888 */             if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1889 */               MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry)e).getValueReference();
/* 1890 */               E newFirst; if (v == valueReference) {
/* 1891 */                 this.modCount += 1;
/* 1892 */                 newFirst = removeFromChain(first, e);
/* 1893 */                 newCount = this.count - 1;
/* 1894 */                 table.set(index, newFirst);
/* 1895 */                 this.count = newCount;
/* 1896 */                 return true;
/*      */               }
/* 1898 */               return 0;
/*      */             }
/*      */           }
/*      */         }
/* 1902 */         return 0;
/*      */       } finally {
/* 1904 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     @CanIgnoreReturnValue
/*      */     boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
/*      */     {
/* 1914 */       lock();
/*      */       try {
/* 1916 */         AtomicReferenceArray<E> table = this.table;
/* 1917 */         int index = hash & table.length() - 1;
/* 1918 */         E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1920 */         for (E e = first; e != null; e = e.getNext()) {
/* 1921 */           K entryKey = e.getKey();
/* 1922 */           if ((e.getHash() == hash) && (entryKey != null))
/*      */           {
/* 1924 */             if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1925 */               MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry)e).getValueReference();
/* 1926 */               E newFirst; if (v == valueReference) {
/* 1927 */                 newFirst = removeFromChain(first, e);
/* 1928 */                 table.set(index, newFirst);
/* 1929 */                 return true;
/*      */               }
/* 1931 */               return 0;
/*      */             }
/*      */           }
/*      */         }
/* 1935 */         return 0;
/*      */       } finally {
/* 1937 */         unlock();
/*      */       }
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     boolean removeEntryForTesting(E entry) {
/* 1943 */       int hash = entry.getHash();
/* 1944 */       int newCount = this.count - 1;
/* 1945 */       AtomicReferenceArray<E> table = this.table;
/* 1946 */       int index = hash & table.length() - 1;
/* 1947 */       E first = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */       
/* 1949 */       for (E e = first; e != null; e = e.getNext()) {
/* 1950 */         if (e == entry) {
/* 1951 */           this.modCount += 1;
/* 1952 */           E newFirst = removeFromChain(first, e);
/* 1953 */           newCount = this.count - 1;
/* 1954 */           table.set(index, newFirst);
/* 1955 */           this.count = newCount;
/* 1956 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 1960 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry)
/*      */     {
/* 1968 */       return entry.getValue() == null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     @Nullable
/*      */     V getLiveValue(E entry)
/*      */     {
/* 1977 */       if (entry.getKey() == null) {
/* 1978 */         tryDrainReferenceQueues();
/* 1979 */         return null;
/*      */       }
/* 1981 */       V value = entry.getValue();
/* 1982 */       if (value == null) {
/* 1983 */         tryDrainReferenceQueues();
/* 1984 */         return null;
/*      */       }
/*      */       
/* 1987 */       return value;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     void postReadCleanup()
/*      */     {
/* 1996 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 1997 */         runCleanup();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup()
/*      */     {
/* 2007 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runCleanup() {
/* 2011 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     /* Error */
/*      */     void runLockedCleanup()
/*      */     {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual 27	com/google/common/collect/MapMakerInternalMap$Segment:tryLock	()Z
/*      */       //   4: ifeq +29 -> 33
/*      */       //   7: aload_0
/*      */       //   8: invokevirtual 28	com/google/common/collect/MapMakerInternalMap$Segment:maybeDrainReferenceQueues	()V
/*      */       //   11: aload_0
/*      */       //   12: getfield 5	com/google/common/collect/MapMakerInternalMap$Segment:readCount	Ljava/util/concurrent/atomic/AtomicInteger;
/*      */       //   15: iconst_0
/*      */       //   16: invokevirtual 58	java/util/concurrent/atomic/AtomicInteger:set	(I)V
/*      */       //   19: aload_0
/*      */       //   20: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   23: goto +10 -> 33
/*      */       //   26: astore_1
/*      */       //   27: aload_0
/*      */       //   28: invokevirtual 29	com/google/common/collect/MapMakerInternalMap$Segment:unlock	()V
/*      */       //   31: aload_1
/*      */       //   32: athrow
/*      */       //   33: return
/*      */       // Line number table:
/*      */       //   Java source line #2015	-> byte code offset #0
/*      */       //   Java source line #2017	-> byte code offset #7
/*      */       //   Java source line #2018	-> byte code offset #11
/*      */       //   Java source line #2020	-> byte code offset #19
/*      */       //   Java source line #2021	-> byte code offset #23
/*      */       //   Java source line #2020	-> byte code offset #26
/*      */       //   Java source line #2023	-> byte code offset #33
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	signature
/*      */       //   0	34	0	this	Segment<K, V, E, S>
/*      */       //   26	6	1	localObject	Object
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   7	19	26	finally
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyStrongValueSegment<K, V>
/*      */     extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
/*      */   {
/*      */     StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2035 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     StrongKeyStrongValueSegment<K, V> self()
/*      */     {
/* 2040 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 2046 */       return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyWeakValueSegment<K, V>
/*      */     extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
/*      */   {
/* 2053 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2060 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     StrongKeyWeakValueSegment<K, V> self()
/*      */     {
/* 2065 */       return this;
/*      */     }
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting()
/*      */     {
/* 2070 */       return this.queueForValues;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 2076 */       return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e)
/*      */     {
/* 2082 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value)
/*      */     {
/* 2088 */       return new MapMakerInternalMap.WeakValueReferenceImpl(this.queueForValues, value, 
/* 2089 */         castForTesting(e));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
/*      */     {
/* 2096 */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2098 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newValueReference = valueReference;
/*      */       
/* 2100 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap.StrongKeyWeakValueEntry.access$600(entry);
/* 2101 */       MapMakerInternalMap.StrongKeyWeakValueEntry.access$602(entry, newValueReference);
/* 2102 */       previous.clear();
/*      */     }
/*      */     
/*      */     void maybeDrainReferenceQueues()
/*      */     {
/* 2107 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */     
/*      */     void maybeClearReferenceQueues()
/*      */     {
/* 2112 */       clearReferenceQueue(this.queueForValues);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static final class StrongKeyDummyValueSegment<K>
/*      */     extends MapMakerInternalMap.Segment<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
/*      */   {
/*      */     StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2124 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     StrongKeyDummyValueSegment<K> self()
/*      */     {
/* 2129 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry)
/*      */     {
/* 2135 */       return (MapMakerInternalMap.StrongKeyDummyValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueSegment<K, V>
/*      */     extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
/*      */   {
/* 2142 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2149 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     WeakKeyStrongValueSegment<K, V> self()
/*      */     {
/* 2154 */       return this;
/*      */     }
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting()
/*      */     {
/* 2159 */       return this.queueForKeys;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 2165 */       return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
/*      */     }
/*      */     
/*      */     void maybeDrainReferenceQueues()
/*      */     {
/* 2170 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */     
/*      */     void maybeClearReferenceQueues()
/*      */     {
/* 2175 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyWeakValueSegment<K, V>
/*      */     extends MapMakerInternalMap.Segment<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
/*      */   {
/* 2182 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
/* 2183 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue();
/*      */     
/*      */ 
/*      */ 
/*      */     WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2189 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     WeakKeyWeakValueSegment<K, V> self()
/*      */     {
/* 2194 */       return this;
/*      */     }
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting()
/*      */     {
/* 2199 */       return this.queueForKeys;
/*      */     }
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting()
/*      */     {
/* 2204 */       return this.queueForValues;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry)
/*      */     {
/* 2210 */       return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e)
/*      */     {
/* 2216 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value)
/*      */     {
/* 2222 */       return new MapMakerInternalMap.WeakValueReferenceImpl(this.queueForValues, value, 
/* 2223 */         castForTesting(e));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference)
/*      */     {
/* 2230 */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2232 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newValueReference = valueReference;
/*      */       
/* 2234 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = MapMakerInternalMap.WeakKeyWeakValueEntry.access$700(entry);
/* 2235 */       MapMakerInternalMap.WeakKeyWeakValueEntry.access$702(entry, newValueReference);
/* 2236 */       previous.clear();
/*      */     }
/*      */     
/*      */     void maybeDrainReferenceQueues()
/*      */     {
/* 2241 */       drainKeyReferenceQueue(this.queueForKeys);
/* 2242 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */     
/*      */     void maybeClearReferenceQueues()
/*      */     {
/* 2247 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyDummyValueSegment<K>
/*      */     extends MapMakerInternalMap.Segment<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
/*      */   {
/* 2254 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue();
/*      */     
/*      */ 
/*      */ 
/*      */     WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize)
/*      */     {
/* 2260 */       super(initialCapacity, maxSegmentSize);
/*      */     }
/*      */     
/*      */     WeakKeyDummyValueSegment<K> self()
/*      */     {
/* 2265 */       return this;
/*      */     }
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting()
/*      */     {
/* 2270 */       return this.queueForKeys;
/*      */     }
/*      */     
/*      */ 
/*      */     public MapMakerInternalMap.WeakKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry)
/*      */     {
/* 2276 */       return (MapMakerInternalMap.WeakKeyDummyValueEntry)entry;
/*      */     }
/*      */     
/*      */     void maybeDrainReferenceQueues()
/*      */     {
/* 2281 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */     
/*      */     void maybeClearReferenceQueues()
/*      */     {
/* 2286 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
/*      */     
/*      */     public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
/* 2294 */       this.mapReference = new WeakReference(map);
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/* 2299 */       MapMakerInternalMap<?, ?, ?, ?> map = (MapMakerInternalMap)this.mapReference.get();
/* 2300 */       if (map == null) {
/* 2301 */         throw new CancellationException();
/*      */       }
/*      */       
/* 2304 */       for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : map.segments) {
/* 2305 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength keyStrength() {
/* 2312 */     return this.entryHelper.keyStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength valueStrength() {
/* 2317 */     return this.entryHelper.valueStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Equivalence<Object> valueEquivalence() {
/* 2322 */     return this.entryHelper.valueStrength().defaultEquivalence();
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
/*      */   public boolean isEmpty()
/*      */   {
/* 2336 */     long sum = 0L;
/* 2337 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2338 */     for (int i = 0; i < segments.length; i++) {
/* 2339 */       if (segments[i].count != 0) {
/* 2340 */         return false;
/*      */       }
/* 2342 */       sum += segments[i].modCount;
/*      */     }
/*      */     
/* 2345 */     if (sum != 0L) {
/* 2346 */       for (int i = 0; i < segments.length; i++) {
/* 2347 */         if (segments[i].count != 0) {
/* 2348 */           return false;
/*      */         }
/* 2350 */         sum -= segments[i].modCount;
/*      */       }
/* 2352 */       if (sum != 0L) {
/* 2353 */         return false;
/*      */       }
/*      */     }
/* 2356 */     return true;
/*      */   }
/*      */   
/*      */   public int size()
/*      */   {
/* 2361 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2362 */     long sum = 0L;
/* 2363 */     for (int i = 0; i < segments.length; i++) {
/* 2364 */       sum += segments[i].count;
/*      */     }
/* 2366 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */   
/*      */   public V get(@Nullable Object key)
/*      */   {
/* 2371 */     if (key == null) {
/* 2372 */       return null;
/*      */     }
/* 2374 */     int hash = hash(key);
/* 2375 */     return (V)segmentFor(hash).get(key, hash);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   E getEntry(@Nullable Object key)
/*      */   {
/* 2383 */     if (key == null) {
/* 2384 */       return null;
/*      */     }
/* 2386 */     int hash = hash(key);
/* 2387 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */   
/*      */   public boolean containsKey(@Nullable Object key)
/*      */   {
/* 2392 */     if (key == null) {
/* 2393 */       return false;
/*      */     }
/* 2395 */     int hash = hash(key);
/* 2396 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */   
/*      */   public boolean containsValue(@Nullable Object value)
/*      */   {
/* 2401 */     if (value == null) {
/* 2402 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2410 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2411 */     long last = -1L;
/* 2412 */     for (int i = 0; i < 3; i++) {
/* 2413 */       long sum = 0L;
/* 2414 */       for (Segment<K, V, E, S> segment : segments)
/*      */       {
/* 2416 */         int unused = segment.count;
/*      */         
/* 2418 */         AtomicReferenceArray<E> table = segment.table;
/* 2419 */         for (int j = 0; j < table.length(); j++) {
/* 2420 */           for (E e = (InternalEntry)table.get(j); e != null; e = e.getNext()) {
/* 2421 */             V v = segment.getLiveValue(e);
/* 2422 */             if ((v != null) && (valueEquivalence().equivalent(value, v))) {
/* 2423 */               return true;
/*      */             }
/*      */           }
/*      */         }
/* 2427 */         sum += segment.modCount;
/*      */       }
/* 2429 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 2432 */       last = sum;
/*      */     }
/* 2434 */     return false;
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value)
/*      */   {
/* 2440 */     Preconditions.checkNotNull(key);
/* 2441 */     Preconditions.checkNotNull(value);
/* 2442 */     int hash = hash(key);
/* 2443 */     return (V)segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V putIfAbsent(K key, V value)
/*      */   {
/* 2449 */     Preconditions.checkNotNull(key);
/* 2450 */     Preconditions.checkNotNull(value);
/* 2451 */     int hash = hash(key);
/* 2452 */     return (V)segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m)
/*      */   {
/* 2457 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 2458 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@Nullable Object key)
/*      */   {
/* 2465 */     if (key == null) {
/* 2466 */       return null;
/*      */     }
/* 2468 */     int hash = hash(key);
/* 2469 */     return (V)segmentFor(hash).remove(key, hash);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value)
/*      */   {
/* 2475 */     if ((key == null) || (value == null)) {
/* 2476 */       return false;
/*      */     }
/* 2478 */     int hash = hash(key);
/* 2479 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, @Nullable V oldValue, V newValue)
/*      */   {
/* 2485 */     Preconditions.checkNotNull(key);
/* 2486 */     Preconditions.checkNotNull(newValue);
/* 2487 */     if (oldValue == null) {
/* 2488 */       return false;
/*      */     }
/* 2490 */     int hash = hash(key);
/* 2491 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value)
/*      */   {
/* 2497 */     Preconditions.checkNotNull(key);
/* 2498 */     Preconditions.checkNotNull(value);
/* 2499 */     int hash = hash(key);
/* 2500 */     return (V)segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */   
/*      */   public void clear()
/*      */   {
/* 2505 */     for (Segment<K, V, E, S> segment : this.segments) {
/* 2506 */       segment.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Set<K> keySet()
/*      */   {
/* 2514 */     Set<K> ks = this.keySet;
/* 2515 */     return ks != null ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Collection<V> values()
/*      */   {
/* 2522 */     Collection<V> vs = this.values;
/* 2523 */     return vs != null ? vs : (this.values = new Values());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Set<Map.Entry<K, V>> entrySet()
/*      */   {
/* 2530 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 2531 */     return es != null ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
/*      */     AtomicReferenceArray<E> currentTable;
/*      */     E nextEntry;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator()
/*      */     {
/* 2547 */       this.nextSegmentIndex = (MapMakerInternalMap.this.segments.length - 1);
/* 2548 */       this.nextTableIndex = -1;
/* 2549 */       advance();
/*      */     }
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance()
/*      */     {
/* 2556 */       this.nextExternal = null;
/*      */       
/* 2558 */       if (nextInChain()) {
/* 2559 */         return;
/*      */       }
/*      */       
/* 2562 */       if (nextInTable()) {
/* 2563 */         return;
/*      */       }
/*      */       
/* 2566 */       while (this.nextSegmentIndex >= 0) {
/* 2567 */         this.currentSegment = MapMakerInternalMap.this.segments[(this.nextSegmentIndex--)];
/* 2568 */         if (this.currentSegment.count != 0) {
/* 2569 */           this.currentTable = this.currentSegment.table;
/* 2570 */           this.nextTableIndex = (this.currentTable.length() - 1);
/* 2571 */           if (nextInTable()) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     boolean nextInChain()
/*      */     {
/* 2582 */       if (this.nextEntry != null) {
/* 2583 */         for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
/* 2584 */           if (advanceTo(this.nextEntry)) {
/* 2585 */             return true;
/*      */           }
/*      */         }
/*      */       }
/* 2589 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean nextInTable()
/*      */     {
/* 2596 */       while (this.nextTableIndex >= 0) {
/* 2597 */         if (((this.nextEntry = (MapMakerInternalMap.InternalEntry)this.currentTable.get(this.nextTableIndex--)) != null) && (
/* 2598 */           (advanceTo(this.nextEntry)) || (nextInChain()))) {
/* 2599 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 2603 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     boolean advanceTo(E entry)
/*      */     {
/*      */       try
/*      */       {
/* 2612 */         K key = entry.getKey();
/* 2613 */         V value = MapMakerInternalMap.this.getLiveValue(entry);
/* 2614 */         boolean bool; if (value != null) {
/* 2615 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(MapMakerInternalMap.this, key, value);
/* 2616 */           return true;
/*      */         }
/*      */         
/* 2619 */         return false;
/*      */       }
/*      */       finally {
/* 2622 */         this.currentSegment.postReadCleanup();
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 2628 */       return this.nextExternal != null;
/*      */     }
/*      */     
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
/* 2632 */       if (this.nextExternal == null) {
/* 2633 */         throw new NoSuchElementException();
/*      */       }
/* 2635 */       this.lastReturned = this.nextExternal;
/* 2636 */       advance();
/* 2637 */       return this.lastReturned;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 2642 */       CollectPreconditions.checkRemove(this.lastReturned != null);
/* 2643 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 2644 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/* 2648 */   final class KeyIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<K> { KeyIterator() { super(); }
/*      */     
/*      */ 
/*      */ 
/* 2652 */     public K next() { return (K)nextEntry().getKey(); }
/*      */   }
/*      */   
/*      */   final class ValueIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<V> {
/* 2656 */     ValueIterator() { super(); }
/*      */     
/*      */     public V next()
/*      */     {
/* 2660 */       return (V)nextEntry().getValue();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(V key)
/*      */     {
/* 2673 */       this.key = key;
/* 2674 */       this.value = value;
/*      */     }
/*      */     
/*      */     public K getKey()
/*      */     {
/* 2679 */       return (K)this.key;
/*      */     }
/*      */     
/*      */     public V getValue()
/*      */     {
/* 2684 */       return (V)this.value;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean equals(@Nullable Object object)
/*      */     {
/* 2690 */       if ((object instanceof Map.Entry)) {
/* 2691 */         Map.Entry<?, ?> that = (Map.Entry)object;
/* 2692 */         return (this.key.equals(that.getKey())) && (this.value.equals(that.getValue()));
/*      */       }
/* 2694 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2700 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */     
/*      */     public V setValue(V newValue)
/*      */     {
/* 2705 */       V oldValue = MapMakerInternalMap.this.put(this.key, newValue);
/* 2706 */       this.value = newValue;
/* 2707 */       return oldValue;
/*      */     }
/*      */   }
/*      */   
/* 2711 */   final class EntryIterator extends MapMakerInternalMap<K, V, E, S>.HashIterator<Map.Entry<K, V>> { EntryIterator() { super(); }
/*      */     
/*      */     public Map.Entry<K, V> next()
/*      */     {
/* 2715 */       return nextEntry();
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeySet extends MapMakerInternalMap.SafeToArraySet<K> {
/* 2720 */     KeySet() { super(); }
/*      */     
/*      */     public Iterator<K> iterator()
/*      */     {
/* 2724 */       return new MapMakerInternalMap.KeyIterator(MapMakerInternalMap.this);
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/* 2729 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty()
/*      */     {
/* 2734 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public boolean contains(Object o)
/*      */     {
/* 2739 */       return MapMakerInternalMap.this.containsKey(o);
/*      */     }
/*      */     
/*      */     public boolean remove(Object o)
/*      */     {
/* 2744 */       return MapMakerInternalMap.this.remove(o) != null;
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/* 2749 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   final class Values extends AbstractCollection<V>
/*      */   {
/*      */     Values() {}
/*      */     
/*      */     public Iterator<V> iterator() {
/* 2758 */       return new MapMakerInternalMap.ValueIterator(MapMakerInternalMap.this);
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/* 2763 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty()
/*      */     {
/* 2768 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public boolean contains(Object o)
/*      */     {
/* 2773 */       return MapMakerInternalMap.this.containsValue(o);
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/* 2778 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object[] toArray()
/*      */     {
/* 2786 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */     
/*      */     public <E> E[] toArray(E[] a)
/*      */     {
/* 2791 */       return MapMakerInternalMap.toArrayList(this).toArray(a);
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntrySet extends MapMakerInternalMap.SafeToArraySet<Map.Entry<K, V>> {
/* 2796 */     EntrySet() { super(); }
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator()
/*      */     {
/* 2800 */       return new MapMakerInternalMap.EntryIterator(MapMakerInternalMap.this);
/*      */     }
/*      */     
/*      */     public boolean contains(Object o)
/*      */     {
/* 2805 */       if (!(o instanceof Map.Entry)) {
/* 2806 */         return false;
/*      */       }
/* 2808 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 2809 */       Object key = e.getKey();
/* 2810 */       if (key == null) {
/* 2811 */         return false;
/*      */       }
/* 2813 */       V v = MapMakerInternalMap.this.get(key);
/*      */       
/* 2815 */       return (v != null) && (MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v));
/*      */     }
/*      */     
/*      */     public boolean remove(Object o)
/*      */     {
/* 2820 */       if (!(o instanceof Map.Entry)) {
/* 2821 */         return false;
/*      */       }
/* 2823 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 2824 */       Object key = e.getKey();
/* 2825 */       return (key != null) && (MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/* 2830 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */     
/*      */     public boolean isEmpty()
/*      */     {
/* 2835 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/* 2840 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static abstract class SafeToArraySet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     public Object[] toArray()
/*      */     {
/* 2850 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */     
/*      */     public <E> E[] toArray(E[] a)
/*      */     {
/* 2855 */       return MapMakerInternalMap.toArrayList(this).toArray(a);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c)
/*      */   {
/* 2861 */     ArrayList<E> result = new ArrayList(c.size());
/* 2862 */     Iterators.addAll(result, c.iterator());
/* 2863 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   Object writeReplace()
/*      */   {
/* 2871 */     return new SerializationProxy(this.entryHelper
/* 2872 */       .keyStrength(), this.entryHelper
/* 2873 */       .valueStrength(), this.keyEquivalence, this.entryHelper
/*      */       
/* 2875 */       .valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */     
/*      */ 
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */     
/*      */ 
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     transient ConcurrentMap<K, V> delegate;
/*      */     
/*      */ 
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate)
/*      */     {
/* 2903 */       this.keyStrength = keyStrength;
/* 2904 */       this.valueStrength = valueStrength;
/* 2905 */       this.keyEquivalence = keyEquivalence;
/* 2906 */       this.valueEquivalence = valueEquivalence;
/* 2907 */       this.concurrencyLevel = concurrencyLevel;
/* 2908 */       this.delegate = delegate;
/*      */     }
/*      */     
/*      */     protected ConcurrentMap<K, V> delegate()
/*      */     {
/* 2913 */       return this.delegate;
/*      */     }
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 2917 */       out.writeInt(this.delegate.size());
/* 2918 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 2919 */         out.writeObject(entry.getKey());
/* 2920 */         out.writeObject(entry.getValue());
/*      */       }
/* 2922 */       out.writeObject(null);
/*      */     }
/*      */     
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException
/*      */     {
/* 2927 */       int size = in.readInt();
/* 2928 */       return new MapMaker()
/* 2929 */         .initialCapacity(size)
/* 2930 */         .setKeyStrength(this.keyStrength)
/* 2931 */         .setValueStrength(this.valueStrength)
/* 2932 */         .keyEquivalence(this.keyEquivalence)
/* 2933 */         .concurrencyLevel(this.concurrencyLevel);
/*      */     }
/*      */     
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException
/*      */     {
/*      */       for (;;) {
/* 2939 */         K key = in.readObject();
/* 2940 */         if (key == null) {
/*      */           break;
/*      */         }
/* 2943 */         V value = in.readObject();
/* 2944 */         this.delegate.put(key, value);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate)
/*      */     {
/* 2963 */       super(valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void writeObject(ObjectOutputStream out)
/*      */       throws IOException
/*      */     {
/* 2973 */       out.defaultWriteObject();
/* 2974 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 2978 */       in.defaultReadObject();
/* 2979 */       MapMaker mapMaker = readMapMaker(in);
/* 2980 */       this.delegate = mapMaker.makeMap();
/* 2981 */       readEntries(in);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 2985 */       return this.delegate;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\MapMakerInternalMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */