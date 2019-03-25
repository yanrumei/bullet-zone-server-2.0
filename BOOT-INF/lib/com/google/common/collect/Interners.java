/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ public final class Interners
/*     */ {
/*     */   public static class InternerBuilder
/*     */   {
/*  44 */     private final MapMaker mapMaker = new MapMaker();
/*  45 */     private boolean strong = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public InternerBuilder strong()
/*     */     {
/*  56 */       this.strong = true;
/*  57 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @GwtIncompatible("java.lang.ref.WeakReference")
/*     */     public InternerBuilder weak()
/*     */     {
/*  67 */       this.strong = false;
/*  68 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public InternerBuilder concurrencyLevel(int concurrencyLevel)
/*     */     {
/*  77 */       this.mapMaker.concurrencyLevel(concurrencyLevel);
/*  78 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Interner<E> build() {
/*  82 */       if (!this.strong) {
/*  83 */         this.mapMaker.weakKeys();
/*     */       }
/*  85 */       return new Interners.InternerImpl(this.mapMaker, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static InternerBuilder newBuilder()
/*     */   {
/*  91 */     return new InternerBuilder(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Interner<E> newStrongInterner()
/*     */   {
/* 100 */     return newBuilder().strong().build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public static <E> Interner<E> newWeakInterner()
/*     */   {
/* 111 */     return newBuilder().weak().build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class InternerImpl<E> implements Interner<E>
/*     */   {
/*     */     @VisibleForTesting
/*     */     final MapMakerInternalMap<E, MapMaker.Dummy, ?, ?> map;
/*     */     
/*     */     private InternerImpl(MapMaker mapMaker) {
/* 121 */       this.map = MapMakerInternalMap.createWithDummyValues(mapMaker
/* 122 */         .keyEquivalence(Equivalence.equals()));
/*     */     }
/*     */     
/*     */     public E intern(E sample)
/*     */     {
/*     */       for (;;)
/*     */       {
/* 129 */         MapMakerInternalMap.InternalEntry<E, MapMaker.Dummy, ?> entry = this.map.getEntry(sample);
/* 130 */         if (entry != null) {
/* 131 */           E canonical = entry.getKey();
/* 132 */           if (canonical != null) {
/* 133 */             return canonical;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 138 */         MapMaker.Dummy sneaky = (MapMaker.Dummy)this.map.putIfAbsent(sample, MapMaker.Dummy.VALUE);
/* 139 */         if (sneaky == null) {
/* 140 */           return sample;
/*     */         }
/*     */       }
/*     */     }
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
/*     */   public static <E> Function<E, E> asFunction(Interner<E> interner)
/*     */   {
/* 159 */     return new InternerFunction((Interner)Preconditions.checkNotNull(interner));
/*     */   }
/*     */   
/*     */   private static class InternerFunction<E> implements Function<E, E>
/*     */   {
/*     */     private final Interner<E> interner;
/*     */     
/*     */     public InternerFunction(Interner<E> interner) {
/* 167 */       this.interner = interner;
/*     */     }
/*     */     
/*     */     public E apply(E input)
/*     */     {
/* 172 */       return (E)this.interner.intern(input);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 177 */       return this.interner.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 182 */       if ((other instanceof InternerFunction)) {
/* 183 */         InternerFunction<?> that = (InternerFunction)other;
/* 184 */         return this.interner.equals(that.interner);
/*     */       }
/*     */       
/* 187 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Interners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */