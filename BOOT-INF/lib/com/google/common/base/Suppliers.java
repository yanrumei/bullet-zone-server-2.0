/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @GwtCompatible
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier)
/*     */   {
/*  43 */     Preconditions.checkNotNull(function);
/*  44 */     Preconditions.checkNotNull(supplier);
/*  45 */     return new SupplierComposition(function, supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     final Supplier<F> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*  53 */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) { this.function = function;
/*  54 */       this.supplier = supplier;
/*     */     }
/*     */     
/*     */     public T get()
/*     */     {
/*  59 */       return (T)this.function.apply(this.supplier.get());
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/*  64 */       if ((obj instanceof SupplierComposition)) {
/*  65 */         SupplierComposition<?, ?> that = (SupplierComposition)obj;
/*  66 */         return (this.function.equals(that.function)) && (this.supplier.equals(that.supplier));
/*     */       }
/*  68 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  73 */       return Objects.hashCode(new Object[] { this.function, this.supplier });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  78 */       return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
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
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate)
/*     */   {
/*  97 */     if (((delegate instanceof NonSerializableMemoizingSupplier)) || ((delegate instanceof MemoizingSupplier)))
/*     */     {
/*  99 */       return delegate;
/*     */     }
/* 101 */     return (delegate instanceof Serializable) ? new MemoizingSupplier(delegate) : new NonSerializableMemoizingSupplier(delegate);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     volatile transient boolean initialized;
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate)
/*     */     {
/* 115 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/*     */     }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 121 */       if (!this.initialized) {
/* 122 */         synchronized (this) {
/* 123 */           if (!this.initialized) {
/* 124 */             T t = this.delegate.get();
/* 125 */             this.value = t;
/* 126 */             this.initialized = true;
/* 127 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 131 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 136 */       return "Suppliers.memoize(" + this.delegate + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class NonSerializableMemoizingSupplier<T>
/*     */     implements Supplier<T>
/*     */   {
/*     */     volatile Supplier<T> delegate;
/*     */     volatile boolean initialized;
/*     */     T value;
/*     */     
/*     */     NonSerializableMemoizingSupplier(Supplier<T> delegate)
/*     */     {
/* 151 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/*     */     }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 157 */       if (!this.initialized) {
/* 158 */         synchronized (this) {
/* 159 */           if (!this.initialized) {
/* 160 */             T t = this.delegate.get();
/* 161 */             this.value = t;
/* 162 */             this.initialized = true;
/*     */             
/* 164 */             this.delegate = null;
/* 165 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 169 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 174 */       return "Suppliers.memoize(" + this.delegate + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit)
/*     */   {
/* 197 */     return new ExpiringMemoizingSupplier(delegate, duration, unit);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     volatile transient T value;
/*     */     volatile transient long expirationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 209 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/* 210 */       this.durationNanos = unit.toNanos(duration);
/* 211 */       Preconditions.checkArgument(duration > 0L);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public T get()
/*     */     {
/* 222 */       long nanos = this.expirationNanos;
/* 223 */       long now = Platform.systemNanoTime();
/* 224 */       if ((nanos == 0L) || (now - nanos >= 0L)) {
/* 225 */         synchronized (this) {
/* 226 */           if (nanos == this.expirationNanos) {
/* 227 */             T t = this.delegate.get();
/* 228 */             this.value = t;
/* 229 */             nanos = now + this.durationNanos;
/*     */             
/*     */ 
/* 232 */             this.expirationNanos = (nanos == 0L ? 1L : nanos);
/* 233 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 237 */       return (T)this.value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 244 */       return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> ofInstance(@Nullable T instance)
/*     */   {
/* 254 */     return new SupplierOfInstance(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
/*     */     final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 261 */     SupplierOfInstance(@Nullable T instance) { this.instance = instance; }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 266 */       return (T)this.instance;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/* 271 */       if ((obj instanceof SupplierOfInstance)) {
/* 272 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 273 */         return Objects.equal(this.instance, that.instance);
/*     */       }
/* 275 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 280 */       return Objects.hashCode(new Object[] { this.instance });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 285 */       return "Suppliers.ofInstance(" + this.instance + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate)
/*     */   {
/* 296 */     return new ThreadSafeSupplier((Supplier)Preconditions.checkNotNull(delegate));
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 303 */     ThreadSafeSupplier(Supplier<T> delegate) { this.delegate = delegate; }
/*     */     
/*     */     /* Error */
/*     */     public T get()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/base/Suppliers$ThreadSafeSupplier:delegate	Lcom/google/common/base/Supplier;
/*     */       //   4: dup
/*     */       //   5: astore_1
/*     */       //   6: monitorenter
/*     */       //   7: aload_0
/*     */       //   8: getfield 2	com/google/common/base/Suppliers$ThreadSafeSupplier:delegate	Lcom/google/common/base/Supplier;
/*     */       //   11: invokeinterface 3 1 0
/*     */       //   16: aload_1
/*     */       //   17: monitorexit
/*     */       //   18: areturn
/*     */       //   19: astore_2
/*     */       //   20: aload_1
/*     */       //   21: monitorexit
/*     */       //   22: aload_2
/*     */       //   23: athrow
/*     */       // Line number table:
/*     */       //   Java source line #308	-> byte code offset #0
/*     */       //   Java source line #309	-> byte code offset #7
/*     */       //   Java source line #310	-> byte code offset #19
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	24	0	this	ThreadSafeSupplier<T>
/*     */       //   5	16	1	Ljava/lang/Object;	Object
/*     */       //   19	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	18	19	finally
/*     */       //   19	22	19	finally
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 315 */       return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
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
/*     */   public static <T> Function<Supplier<T>, T> supplierFunction()
/*     */   {
/* 331 */     SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
/* 332 */     return sf;
/*     */   }
/*     */   
/*     */   private static abstract interface SupplierFunction<T> extends Function<Supplier<T>, T>
/*     */   {}
/*     */   
/* 338 */   private static enum SupplierFunctionImpl implements Suppliers.SupplierFunction<Object> { INSTANCE;
/*     */     
/*     */     private SupplierFunctionImpl() {}
/*     */     
/*     */     public Object apply(Supplier<Object> input) {
/* 343 */       return input.get();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 348 */       return "Suppliers.supplierFunction()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Suppliers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */