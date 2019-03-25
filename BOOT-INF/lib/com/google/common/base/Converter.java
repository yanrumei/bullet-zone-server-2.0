/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Converter<A, B>
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   @LazyInit
/*     */   private transient Converter<B, A> reverse;
/*     */   
/*     */   protected Converter()
/*     */   {
/* 124 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Converter(boolean handleNullAutomatically)
/*     */   {
/* 131 */     this.handleNullAutomatically = handleNullAutomatically;
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
/*     */   protected abstract B doForward(A paramA);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract A doBackward(B paramB);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   @CanIgnoreReturnValue
/*     */   public final B convert(@Nullable A a)
/*     */   {
/* 169 */     return (B)correctedDoForward(a);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   B correctedDoForward(@Nullable A a) {
/* 174 */     if (this.handleNullAutomatically)
/*     */     {
/* 176 */       return a == null ? null : Preconditions.checkNotNull(doForward(a));
/*     */     }
/* 178 */     return (B)doForward(a);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   A correctedDoBackward(@Nullable B b)
/*     */   {
/* 184 */     if (this.handleNullAutomatically)
/*     */     {
/* 186 */       return b == null ? null : Preconditions.checkNotNull(doBackward(b));
/*     */     }
/* 188 */     return (A)doBackward(b);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable)
/*     */   {
/* 202 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 203 */     new Iterable()
/*     */     {
/*     */       public Iterator<B> iterator() {
/* 206 */         new Iterator() {
/* 207 */           private final Iterator<? extends A> fromIterator = Converter.1.this.val$fromIterable.iterator();
/*     */           
/*     */           public boolean hasNext()
/*     */           {
/* 211 */             return this.fromIterator.hasNext();
/*     */           }
/*     */           
/*     */           public B next()
/*     */           {
/* 216 */             return (B)Converter.this.convert(this.fromIterator.next());
/*     */           }
/*     */           
/*     */           public void remove()
/*     */           {
/* 221 */             this.fromIterator.remove();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public Converter<B, A> reverse()
/*     */   {
/* 238 */     Converter<B, A> result = this.reverse;
/* 239 */     return result == null ? (this.reverse = new ReverseConverter(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ReverseConverter(Converter<A, B> original) {
/* 247 */       this.original = original;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected A doForward(B b)
/*     */     {
/* 259 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     protected B doBackward(A a)
/*     */     {
/* 264 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     A correctedDoForward(@Nullable B b)
/*     */     {
/* 270 */       return (A)this.original.correctedDoBackward(b);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     B correctedDoBackward(@Nullable A a)
/*     */     {
/* 276 */       return (B)this.original.correctedDoForward(a);
/*     */     }
/*     */     
/*     */     public Converter<A, B> reverse()
/*     */     {
/* 281 */       return this.original;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object)
/*     */     {
/* 286 */       if ((object instanceof ReverseConverter)) {
/* 287 */         ReverseConverter<?, ?> that = (ReverseConverter)object;
/* 288 */         return this.original.equals(that.original);
/*     */       }
/* 290 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 295 */       return this.original.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 300 */       return this.original + ".reverse()";
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
/*     */   public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter)
/*     */   {
/* 314 */     return doAndThen(secondConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter)
/*     */   {
/* 321 */     return new ConverterComposition(this, (Converter)Preconditions.checkNotNull(secondConverter));
/*     */   }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 330 */       this.first = first;
/* 331 */       this.second = second;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected C doForward(A a)
/*     */     {
/* 343 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     protected A doBackward(C c)
/*     */     {
/* 348 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     C correctedDoForward(@Nullable A a)
/*     */     {
/* 354 */       return (C)this.second.correctedDoForward(this.first.correctedDoForward(a));
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     A correctedDoBackward(@Nullable C c)
/*     */     {
/* 360 */       return (A)this.first.correctedDoBackward(this.second.correctedDoBackward(c));
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object)
/*     */     {
/* 365 */       if ((object instanceof ConverterComposition)) {
/* 366 */         ConverterComposition<?, ?, ?> that = (ConverterComposition)object;
/* 367 */         return (this.first.equals(that.first)) && (this.second.equals(that.second));
/*     */       }
/* 369 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 374 */       return 31 * this.first.hashCode() + this.second.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 379 */       return this.first + ".andThen(" + this.second + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @Nullable
/*     */   @CanIgnoreReturnValue
/*     */   public final B apply(@Nullable A a)
/*     */   {
/* 393 */     return (B)convert(a);
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
/*     */   public boolean equals(@Nullable Object object)
/*     */   {
/* 409 */     return super.equals(object);
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
/*     */ 
/*     */ 
/*     */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
/*     */   {
/* 431 */     return new FunctionBasedConverter(forwardFunction, backwardFunction, null);
/*     */   }
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B> implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
/*     */     {
/* 442 */       this.forwardFunction = ((Function)Preconditions.checkNotNull(forwardFunction));
/* 443 */       this.backwardFunction = ((Function)Preconditions.checkNotNull(backwardFunction));
/*     */     }
/*     */     
/*     */     protected B doForward(A a)
/*     */     {
/* 448 */       return (B)this.forwardFunction.apply(a);
/*     */     }
/*     */     
/*     */     protected A doBackward(B b)
/*     */     {
/* 453 */       return (A)this.backwardFunction.apply(b);
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object object)
/*     */     {
/* 458 */       if ((object instanceof FunctionBasedConverter)) {
/* 459 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter)object;
/* 460 */         return (this.forwardFunction.equals(that.forwardFunction)) && 
/* 461 */           (this.backwardFunction.equals(that.backwardFunction));
/*     */       }
/* 463 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 468 */       return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 473 */       return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Converter<T, T> identity()
/*     */   {
/* 482 */     return IdentityConverter.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 490 */     static final IdentityConverter INSTANCE = new IdentityConverter();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected T doForward(T t) {
/* 494 */       return t;
/*     */     }
/*     */     
/*     */     protected T doBackward(T t)
/*     */     {
/* 499 */       return t;
/*     */     }
/*     */     
/*     */     public IdentityConverter<T> reverse()
/*     */     {
/* 504 */       return this;
/*     */     }
/*     */     
/*     */     <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter)
/*     */     {
/* 509 */       return (Converter)Preconditions.checkNotNull(otherConverter, "otherConverter");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 519 */       return "Converter.identity()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 523 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */