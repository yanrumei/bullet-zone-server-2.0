/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.io.Serializable;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ abstract class Cut<C extends Comparable>
/*     */   implements Comparable<Cut<C>>, Serializable
/*     */ {
/*     */   final C endpoint;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Cut(@Nullable C endpoint)
/*     */   {
/*  39 */     this.endpoint = endpoint;
/*     */   }
/*     */   
/*     */ 
/*     */   abstract boolean isLessThan(C paramC);
/*     */   
/*     */ 
/*     */   abstract BoundType typeAsLowerBound();
/*     */   
/*     */ 
/*     */   abstract BoundType typeAsUpperBound();
/*     */   
/*     */   abstract Cut<C> withLowerBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   
/*     */   abstract Cut<C> withUpperBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   
/*     */   abstract void describeAsLowerBound(StringBuilder paramStringBuilder);
/*     */   
/*     */   abstract void describeAsUpperBound(StringBuilder paramStringBuilder);
/*     */   
/*     */   abstract C leastValueAbove(DiscreteDomain<C> paramDiscreteDomain);
/*     */   
/*     */   abstract C greatestValueBelow(DiscreteDomain<C> paramDiscreteDomain);
/*     */   
/*     */   Cut<C> canonical(DiscreteDomain<C> domain)
/*     */   {
/*  65 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Cut<C> that)
/*     */   {
/*  71 */     if (that == belowAll()) {
/*  72 */       return 1;
/*     */     }
/*  74 */     if (that == aboveAll()) {
/*  75 */       return -1;
/*     */     }
/*  77 */     int result = Range.compareOrThrow(this.endpoint, that.endpoint);
/*  78 */     if (result != 0) {
/*  79 */       return result;
/*     */     }
/*     */     
/*  82 */     return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
/*     */   }
/*     */   
/*     */   C endpoint() {
/*  86 */     return this.endpoint;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  92 */     if ((obj instanceof Cut))
/*     */     {
/*  94 */       Cut<C> that = (Cut)obj;
/*     */       try {
/*  96 */         int compareResult = compareTo(that);
/*  97 */         return compareResult == 0;
/*     */       }
/*     */       catch (ClassCastException localClassCastException) {}
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int hashCode();
/*     */   
/*     */ 
/*     */ 
/*     */   static <C extends Comparable> Cut<C> belowAll()
/*     */   {
/* 113 */     return BelowAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class BelowAll
/*     */     extends Cut<Comparable<?>>
/*     */   {
/* 119 */     private static final BelowAll INSTANCE = new BelowAll();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 122 */     private BelowAll() { super(); }
/*     */     
/*     */ 
/*     */     Comparable<?> endpoint()
/*     */     {
/* 127 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */     
/*     */     boolean isLessThan(Comparable<?> value)
/*     */     {
/* 132 */       return true;
/*     */     }
/*     */     
/*     */     BoundType typeAsLowerBound()
/*     */     {
/* 137 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */     BoundType typeAsUpperBound()
/*     */     {
/* 142 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 148 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 154 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb)
/*     */     {
/* 159 */       sb.append("(-∞");
/*     */     }
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb)
/*     */     {
/* 164 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 169 */       return domain.minValue();
/*     */     }
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 174 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain)
/*     */     {
/*     */       try {
/* 180 */         return Cut.belowValue(domain.minValue());
/*     */       } catch (NoSuchElementException e) {}
/* 182 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(Cut<Comparable<?>> o)
/*     */     {
/* 188 */       return o == this ? 0 : -1;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 193 */       return System.identityHashCode(this);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 198 */       return "-∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 202 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <C extends Comparable> Cut<C> aboveAll()
/*     */   {
/* 214 */     return AboveAll.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class AboveAll extends Cut<Comparable<?>> {
/* 218 */     private static final AboveAll INSTANCE = new AboveAll();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 221 */     private AboveAll() { super(); }
/*     */     
/*     */ 
/*     */     Comparable<?> endpoint()
/*     */     {
/* 226 */       throw new IllegalStateException("range unbounded on this side");
/*     */     }
/*     */     
/*     */     boolean isLessThan(Comparable<?> value)
/*     */     {
/* 231 */       return false;
/*     */     }
/*     */     
/*     */     BoundType typeAsLowerBound()
/*     */     {
/* 236 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */     
/*     */     BoundType typeAsUpperBound()
/*     */     {
/* 241 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 247 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 253 */       throw new IllegalStateException();
/*     */     }
/*     */     
/*     */     void describeAsLowerBound(StringBuilder sb)
/*     */     {
/* 258 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb)
/*     */     {
/* 263 */       sb.append("+∞)");
/*     */     }
/*     */     
/*     */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 268 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain)
/*     */     {
/* 273 */       return domain.maxValue();
/*     */     }
/*     */     
/*     */     public int compareTo(Cut<Comparable<?>> o)
/*     */     {
/* 278 */       return o == this ? 0 : 1;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 283 */       return System.identityHashCode(this);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 288 */       return "+∞";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 292 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 299 */   static <C extends Comparable> Cut<C> belowValue(C endpoint) { return new BelowValue(endpoint); }
/*     */   
/*     */   private static final class BelowValue<C extends Comparable> extends Cut<C> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 304 */     BelowValue(C endpoint) { super(); }
/*     */     
/*     */ 
/*     */     boolean isLessThan(C value)
/*     */     {
/* 309 */       return Range.compareOrThrow(this.endpoint, value) <= 0;
/*     */     }
/*     */     
/*     */     BoundType typeAsLowerBound()
/*     */     {
/* 314 */       return BoundType.CLOSED;
/*     */     }
/*     */     
/*     */     BoundType typeAsUpperBound()
/*     */     {
/* 319 */       return BoundType.OPEN;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain)
/*     */     {
/* 324 */       switch (Cut.1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */       case 1: 
/* 326 */         return this;
/*     */       case 2: 
/* 328 */         C previous = domain.previous(this.endpoint);
/* 329 */         return previous == null ? Cut.belowAll() : new Cut.AboveValue(previous);
/*     */       }
/* 331 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain)
/*     */     {
/* 337 */       switch (Cut.1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */       case 1: 
/* 339 */         C previous = domain.previous(this.endpoint);
/* 340 */         return previous == null ? Cut.aboveAll() : new Cut.AboveValue(previous);
/*     */       case 2: 
/* 342 */         return this;
/*     */       }
/* 344 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */ 
/*     */     void describeAsLowerBound(StringBuilder sb)
/*     */     {
/* 350 */       sb.append('[').append(this.endpoint);
/*     */     }
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb)
/*     */     {
/* 355 */       sb.append(this.endpoint).append(')');
/*     */     }
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain)
/*     */     {
/* 360 */       return this.endpoint;
/*     */     }
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain)
/*     */     {
/* 365 */       return domain.previous(this.endpoint);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 370 */       return this.endpoint.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 375 */       return "\\" + this.endpoint + "/";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 382 */   static <C extends Comparable> Cut<C> aboveValue(C endpoint) { return new AboveValue(endpoint); }
/*     */   
/*     */   private static final class AboveValue<C extends Comparable> extends Cut<C> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 387 */     AboveValue(C endpoint) { super(); }
/*     */     
/*     */ 
/*     */     boolean isLessThan(C value)
/*     */     {
/* 392 */       return Range.compareOrThrow(this.endpoint, value) < 0;
/*     */     }
/*     */     
/*     */     BoundType typeAsLowerBound()
/*     */     {
/* 397 */       return BoundType.OPEN;
/*     */     }
/*     */     
/*     */     BoundType typeAsUpperBound()
/*     */     {
/* 402 */       return BoundType.CLOSED;
/*     */     }
/*     */     
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain)
/*     */     {
/* 407 */       switch (Cut.1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */       case 2: 
/* 409 */         return this;
/*     */       case 1: 
/* 411 */         C next = domain.next(this.endpoint);
/* 412 */         return next == null ? Cut.belowAll() : belowValue(next);
/*     */       }
/* 414 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */ 
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain)
/*     */     {
/* 420 */       switch (Cut.1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */       case 2: 
/* 422 */         C next = domain.next(this.endpoint);
/* 423 */         return next == null ? Cut.aboveAll() : belowValue(next);
/*     */       case 1: 
/* 425 */         return this;
/*     */       }
/* 427 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */ 
/*     */     void describeAsLowerBound(StringBuilder sb)
/*     */     {
/* 433 */       sb.append('(').append(this.endpoint);
/*     */     }
/*     */     
/*     */     void describeAsUpperBound(StringBuilder sb)
/*     */     {
/* 438 */       sb.append(this.endpoint).append(']');
/*     */     }
/*     */     
/*     */     C leastValueAbove(DiscreteDomain<C> domain)
/*     */     {
/* 443 */       return domain.next(this.endpoint);
/*     */     }
/*     */     
/*     */     C greatestValueBelow(DiscreteDomain<C> domain)
/*     */     {
/* 448 */       return this.endpoint;
/*     */     }
/*     */     
/*     */     Cut<C> canonical(DiscreteDomain<C> domain)
/*     */     {
/* 453 */       C next = leastValueAbove(domain);
/* 454 */       return next != null ? belowValue(next) : Cut.aboveAll();
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 459 */       return this.endpoint.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 464 */       return "/" + this.endpoint + "\\";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\Cut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */