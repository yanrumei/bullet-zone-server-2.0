/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.io.Serializable;
/*     */ import java.util.function.BiPredicate;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ public abstract class Equivalence<T>
/*     */   implements BiPredicate<T, T>
/*     */ {
/*     */   public final boolean equivalent(@Nullable T a, @Nullable T b)
/*     */   {
/*  61 */     if (a == b) {
/*  62 */       return true;
/*     */     }
/*  64 */     if ((a == null) || (b == null)) {
/*  65 */       return false;
/*     */     }
/*  67 */     return doEquivalent(a, b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean test(@Nullable T t, @Nullable T u)
/*     */   {
/*  78 */     return equivalent(t, u);
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
/*     */   @ForOverride
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
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
/*     */   public final int hash(@Nullable T t)
/*     */   {
/* 110 */     if (t == null) {
/* 111 */       return 0;
/*     */     }
/* 113 */     return doHash(t);
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
/*     */ 
/*     */   @ForOverride
/*     */   protected abstract int doHash(T paramT);
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
/*     */   public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function)
/*     */   {
/* 151 */     return new FunctionalEquivalence(function, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <S extends T> Wrapper<S> wrap(@Nullable S reference)
/*     */   {
/* 162 */     return new Wrapper(this, reference, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Wrapper<T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */     
/*     */ 
/*     */ 
/*     */     @Nullable
/*     */     private final T reference;
/*     */     
/*     */ 
/*     */ 
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference)
/*     */     {
/* 188 */       this.equivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/* 189 */       this.reference = reference;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public T get()
/*     */     {
/* 195 */       return (T)this.reference;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/* 205 */       if (obj == this) {
/* 206 */         return true;
/*     */       }
/* 208 */       if ((obj instanceof Wrapper)) {
/* 209 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 211 */         if (this.equivalence.equals(that.equivalence))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */           Equivalence<Object> equivalence = this.equivalence;
/* 218 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         }
/*     */       }
/* 221 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 229 */       return this.equivalence.hash(this.reference);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 238 */       return this.equivalence + ".wrap(" + this.reference + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtCompatible(serializable=true)
/*     */   public final <S extends T> Equivalence<Iterable<S>> pairwise()
/*     */   {
/* 259 */     return new PairwiseEquivalence(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 269 */   public final Predicate<T> equivalentTo(@Nullable T target) { return new EquivalentToPredicate(this, target); }
/*     */   
/*     */   private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final Equivalence<T> equivalence;
/*     */     @Nullable
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
/* 278 */       this.equivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/* 279 */       this.target = target;
/*     */     }
/*     */     
/*     */     public boolean apply(@Nullable T input)
/*     */     {
/* 284 */       return this.equivalence.equivalent(input, this.target);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/* 289 */       if (this == obj) {
/* 290 */         return true;
/*     */       }
/* 292 */       if ((obj instanceof EquivalentToPredicate)) {
/* 293 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 294 */         return (this.equivalence.equals(that.equivalence)) && (Objects.equal(this.target, that.target));
/*     */       }
/* 296 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 301 */       return Objects.hashCode(new Object[] { this.equivalence, this.target });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 306 */       return this.equivalence + ".equivalentTo(" + this.target + ")";
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
/*     */   public static Equivalence<Object> equals()
/*     */   {
/* 323 */     return Equals.INSTANCE;
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
/*     */   public static Equivalence<Object> identity()
/*     */   {
/* 336 */     return Identity.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class Equals extends Equivalence<Object> implements Serializable
/*     */   {
/* 341 */     static final Equals INSTANCE = new Equals();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 345 */       return a.equals(b);
/*     */     }
/*     */     
/*     */     protected int doHash(Object o)
/*     */     {
/* 350 */       return o.hashCode();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 354 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 362 */     static final Identity INSTANCE = new Identity();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 366 */       return false;
/*     */     }
/*     */     
/*     */     protected int doHash(Object o)
/*     */     {
/* 371 */       return System.identityHashCode(o);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 375 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Equivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */