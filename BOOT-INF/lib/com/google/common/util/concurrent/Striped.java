/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class Striped<L>
/*     */ {
/*     */   private static final int LARGE_LAZY_CUTOFF = 1024;
/*     */   
/*     */   public abstract L get(Object paramObject);
/*     */   
/*     */   public abstract L getAt(int paramInt);
/*     */   
/*     */   abstract int indexFor(Object paramObject);
/*     */   
/*     */   public abstract int size();
/*     */   
/*     */   public Iterable<L> bulkGet(Iterable<?> keys)
/*     */   {
/* 143 */     Object[] array = Iterables.toArray(keys, Object.class);
/* 144 */     if (array.length == 0) {
/* 145 */       return ImmutableList.of();
/*     */     }
/* 147 */     int[] stripes = new int[array.length];
/* 148 */     for (int i = 0; i < array.length; i++) {
/* 149 */       stripes[i] = indexFor(array[i]);
/*     */     }
/* 151 */     Arrays.sort(stripes);
/*     */     
/* 153 */     int previousStripe = stripes[0];
/* 154 */     array[0] = getAt(previousStripe);
/* 155 */     for (int i = 1; i < array.length; i++) {
/* 156 */       int currentStripe = stripes[i];
/* 157 */       if (currentStripe == previousStripe) {
/* 158 */         array[i] = array[(i - 1)];
/*     */       } else {
/* 160 */         array[i] = getAt(currentStripe);
/* 161 */         previousStripe = currentStripe;
/*     */       }
/*     */     }
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
/* 182 */     List<L> asList = Arrays.asList(array);
/* 183 */     return Collections.unmodifiableList(asList);
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
/*     */   public static Striped<Lock> lock(int stripes)
/*     */   {
/* 196 */     new CompactStriped(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */ 
/*     */       public Lock get() {
/* 201 */         return new Striped.PaddedLock(); } }, null);
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
/*     */   public static Striped<Lock> lazyWeakLock(int stripes)
/*     */   {
/* 214 */     lazy(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */       public Lock get()
/*     */       {
/* 219 */         return new ReentrantLock(false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static <L> Striped<L> lazy(int stripes, Supplier<L> supplier) {
/* 225 */     return stripes < 1024 ? new SmallLazyStriped(stripes, supplier) : new LargeLazyStriped(stripes, supplier);
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
/*     */   public static Striped<Semaphore> semaphore(int stripes, int permits)
/*     */   {
/* 239 */     new CompactStriped(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */ 
/*     */       public Semaphore get() {
/* 244 */         return new Striped.PaddedSemaphore(this.val$permits); } }, null);
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
/*     */   public static Striped<Semaphore> lazyWeakSemaphore(int stripes, int permits)
/*     */   {
/* 258 */     lazy(stripes, new Supplier()
/*     */     {
/*     */ 
/*     */       public Semaphore get()
/*     */       {
/* 263 */         return new Semaphore(this.val$permits, false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<ReadWriteLock> readWriteLock(int stripes)
/*     */   {
/* 276 */     return new CompactStriped(stripes, READ_WRITE_LOCK_SUPPLIER, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int stripes)
/*     */   {
/* 287 */     return lazy(stripes, READ_WRITE_LOCK_SUPPLIER);
/*     */   }
/*     */   
/*     */ 
/* 291 */   private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new Supplier()
/*     */   {
/*     */ 
/*     */     public ReadWriteLock get() {
/* 295 */       return new ReentrantReadWriteLock(); }
/*     */   };
/*     */   private static final int ALL_SET = -1;
/*     */   
/*     */   private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
/*     */     final int mask;
/*     */     
/*     */     PowerOfTwoStriped(int stripes) {
/* 303 */       super();
/* 304 */       Preconditions.checkArgument(stripes > 0, "Stripes must be positive");
/* 305 */       this.mask = (stripes > 1073741824 ? -1 : Striped.ceilToPowerOfTwo(stripes) - 1);
/*     */     }
/*     */     
/*     */     final int indexFor(Object key)
/*     */     {
/* 310 */       int hash = Striped.smear(key.hashCode());
/* 311 */       return hash & this.mask;
/*     */     }
/*     */     
/*     */     public final L get(Object key)
/*     */     {
/* 316 */       return (L)getAt(indexFor(key));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class CompactStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     private final Object[] array;
/*     */     
/*     */ 
/*     */     private CompactStriped(int stripes, Supplier<L> supplier)
/*     */     {
/* 329 */       super();
/* 330 */       Preconditions.checkArgument(stripes <= 1073741824, "Stripes must be <= 2^30)");
/*     */       
/* 332 */       this.array = new Object[this.mask + 1];
/* 333 */       for (int i = 0; i < this.array.length; i++) {
/* 334 */         this.array[i] = supplier.get();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public L getAt(int index)
/*     */     {
/* 341 */       return (L)this.array[index];
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 346 */       return this.array.length;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class SmallLazyStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     final AtomicReferenceArray<ArrayReference<? extends L>> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     
/*     */     final int size;
/* 360 */     final ReferenceQueue<L> queue = new ReferenceQueue();
/*     */     
/*     */     SmallLazyStriped(int stripes, Supplier<L> supplier) {
/* 363 */       super();
/* 364 */       this.size = (this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1);
/* 365 */       this.locks = new AtomicReferenceArray(this.size);
/* 366 */       this.supplier = supplier;
/*     */     }
/*     */     
/*     */     public L getAt(int index)
/*     */     {
/* 371 */       if (this.size != Integer.MAX_VALUE) {
/* 372 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 374 */       ArrayReference<? extends L> existingRef = (ArrayReference)this.locks.get(index);
/* 375 */       L existing = existingRef == null ? null : existingRef.get();
/* 376 */       if (existing != null) {
/* 377 */         return existing;
/*     */       }
/* 379 */       L created = this.supplier.get();
/* 380 */       ArrayReference<L> newRef = new ArrayReference(created, index, this.queue);
/* 381 */       while (!this.locks.compareAndSet(index, existingRef, newRef))
/*     */       {
/* 383 */         existingRef = (ArrayReference)this.locks.get(index);
/* 384 */         existing = existingRef == null ? null : existingRef.get();
/* 385 */         if (existing != null) {
/* 386 */           return existing;
/*     */         }
/*     */       }
/* 389 */       drainQueue();
/* 390 */       return created;
/*     */     }
/*     */     
/*     */ 
/*     */     private void drainQueue()
/*     */     {
/*     */       Reference<? extends L> ref;
/*     */       
/* 398 */       while ((ref = this.queue.poll()) != null)
/*     */       {
/* 400 */         ArrayReference<? extends L> arrayRef = (ArrayReference)ref;
/*     */         
/*     */ 
/* 403 */         this.locks.compareAndSet(arrayRef.index, arrayRef, null);
/*     */       }
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 409 */       return this.size;
/*     */     }
/*     */     
/*     */     private static final class ArrayReference<L> extends WeakReference<L> {
/*     */       final int index;
/*     */       
/*     */       ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
/* 416 */         super(queue);
/* 417 */         this.index = index;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class LargeLazyStriped<L>
/*     */     extends Striped.PowerOfTwoStriped<L>
/*     */   {
/*     */     final ConcurrentMap<Integer, L> locks;
/*     */     
/*     */     final Supplier<L> supplier;
/*     */     final int size;
/*     */     
/*     */     LargeLazyStriped(int stripes, Supplier<L> supplier)
/*     */     {
/* 434 */       super();
/* 435 */       this.size = (this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1);
/* 436 */       this.supplier = supplier;
/* 437 */       this.locks = new MapMaker().weakValues().makeMap();
/*     */     }
/*     */     
/*     */     public L getAt(int index)
/*     */     {
/* 442 */       if (this.size != Integer.MAX_VALUE) {
/* 443 */         Preconditions.checkElementIndex(index, size());
/*     */       }
/* 445 */       L existing = this.locks.get(Integer.valueOf(index));
/* 446 */       if (existing != null) {
/* 447 */         return existing;
/*     */       }
/* 449 */       L created = this.supplier.get();
/* 450 */       existing = this.locks.putIfAbsent(Integer.valueOf(index), created);
/* 451 */       return (L)MoreObjects.firstNonNull(existing, created);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 456 */       return this.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int ceilToPowerOfTwo(int x)
/*     */   {
/* 466 */     return 1 << IntMath.log2(x, RoundingMode.CEILING);
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
/*     */   private static int smear(int hashCode)
/*     */   {
/* 479 */     hashCode ^= hashCode >>> 20 ^ hashCode >>> 12;
/* 480 */     return hashCode ^ hashCode >>> 7 ^ hashCode >>> 4;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class PaddedLock
/*     */     extends ReentrantLock
/*     */   {
/*     */     long unused1;
/*     */     
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedLock()
/*     */     {
/* 494 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PaddedSemaphore extends Semaphore
/*     */   {
/*     */     long unused1;
/*     */     long unused2;
/*     */     long unused3;
/*     */     
/*     */     PaddedSemaphore(int permits) {
/* 505 */       super(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\Striped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */