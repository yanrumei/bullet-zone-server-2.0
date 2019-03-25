/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*     */   @Deprecated
/*     */   public SimpleTimeLimiter(ExecutorService executor)
/*     */   {
/*  67 */     this.executor = ((ExecutorService)Preconditions.checkNotNull(executor));
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
/*     */   @Deprecated
/*     */   public SimpleTimeLimiter()
/*     */   {
/*  83 */     this(Executors.newCachedThreadPool());
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
/*     */   public static SimpleTimeLimiter create(ExecutorService executor)
/*     */   {
/*  98 */     return new SimpleTimeLimiter(executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, TimeUnit timeoutUnit)
/*     */   {
/* 107 */     Preconditions.checkNotNull(target);
/* 108 */     Preconditions.checkNotNull(interfaceType);
/* 109 */     Preconditions.checkNotNull(timeoutUnit);
/* 110 */     checkPositiveTimeout(timeoutDuration);
/* 111 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */     
/* 113 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */     
/* 115 */     InvocationHandler handler = new InvocationHandler()
/*     */     {
/*     */       public Object invoke(Object obj, final Method method, final Object[] args)
/*     */         throws Throwable
/*     */       {
/* 120 */         Callable<Object> callable = new Callable()
/*     */         {
/*     */           public Object call() throws Exception
/*     */           {
/*     */             try {
/* 125 */               return method.invoke(SimpleTimeLimiter.1.this.val$target, args);
/*     */             } catch (InvocationTargetException e) {
/* 127 */               throw SimpleTimeLimiter.throwCause(e, false);
/*     */             }
/*     */           }
/* 130 */         };
/* 131 */         return SimpleTimeLimiter.this.callWithTimeout(callable, timeoutDuration, interruptibleMethods, this.val$interruptibleMethods
/* 132 */           .contains(method));
/*     */       }
/* 134 */     };
/* 135 */     return (T)newProxy(interfaceType, handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
/*     */     throws Exception
/*     */   {
/* 145 */     Preconditions.checkNotNull(callable);
/* 146 */     Preconditions.checkNotNull(timeoutUnit);
/* 147 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 149 */     Future<T> future = this.executor.submit(callable);
/*     */     try
/*     */     {
/* 152 */       if (amInterruptible) {
/*     */         try {
/* 154 */           return (T)future.get(timeoutDuration, timeoutUnit);
/*     */         } catch (InterruptedException e) {
/* 156 */           future.cancel(true);
/* 157 */           throw e;
/*     */         }
/*     */       }
/* 160 */       return (T)Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     }
/*     */     catch (ExecutionException e) {
/* 163 */       throw throwCause(e, true);
/*     */     } catch (TimeoutException e) {
/* 165 */       future.cancel(true);
/* 166 */       throw new UncheckedTimeoutException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws TimeoutException, InterruptedException, ExecutionException
/*     */   {
/* 173 */     Preconditions.checkNotNull(callable);
/* 174 */     Preconditions.checkNotNull(timeoutUnit);
/* 175 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 177 */     Future<T> future = this.executor.submit(callable);
/*     */     try
/*     */     {
/* 180 */       return (T)future.get(timeoutDuration, timeoutUnit);
/*     */     } catch (InterruptedException|TimeoutException e) {
/* 182 */       future.cancel(true);
/* 183 */       throw e;
/*     */     } catch (ExecutionException e) {
/* 185 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 186 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws TimeoutException, ExecutionException
/*     */   {
/* 194 */     Preconditions.checkNotNull(callable);
/* 195 */     Preconditions.checkNotNull(timeoutUnit);
/* 196 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 198 */     Future<T> future = this.executor.submit(callable);
/*     */     try
/*     */     {
/* 201 */       return (T)Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     } catch (TimeoutException e) {
/* 203 */       future.cancel(true);
/* 204 */       throw e;
/*     */     } catch (ExecutionException e) {
/* 206 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 207 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws TimeoutException, InterruptedException
/*     */   {
/* 214 */     Preconditions.checkNotNull(runnable);
/* 215 */     Preconditions.checkNotNull(timeoutUnit);
/* 216 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 218 */     Future<?> future = this.executor.submit(runnable);
/*     */     try
/*     */     {
/* 221 */       future.get(timeoutDuration, timeoutUnit);
/*     */     } catch (InterruptedException|TimeoutException e) {
/* 223 */       future.cancel(true);
/* 224 */       throw e;
/*     */     } catch (ExecutionException e) {
/* 226 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 227 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
/*     */     throws TimeoutException
/*     */   {
/* 234 */     Preconditions.checkNotNull(runnable);
/* 235 */     Preconditions.checkNotNull(timeoutUnit);
/* 236 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 238 */     Future<?> future = this.executor.submit(runnable);
/*     */     try
/*     */     {
/* 241 */       Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     } catch (TimeoutException e) {
/* 243 */       future.cancel(true);
/* 244 */       throw e;
/*     */     } catch (ExecutionException e) {
/* 246 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 247 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 252 */     Throwable cause = e.getCause();
/* 253 */     if (cause == null) {
/* 254 */       throw e;
/*     */     }
/* 256 */     if (combineStackTraces)
/*     */     {
/* 258 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat(cause.getStackTrace(), e.getStackTrace(), StackTraceElement.class);
/* 259 */       cause.setStackTrace(combined);
/*     */     }
/* 261 */     if ((cause instanceof Exception)) {
/* 262 */       throw ((Exception)cause);
/*     */     }
/* 264 */     if ((cause instanceof Error)) {
/* 265 */       throw ((Error)cause);
/*     */     }
/*     */     
/* 268 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 272 */     Set<Method> set = Sets.newHashSet();
/* 273 */     for (Method m : interfaceType.getMethods()) {
/* 274 */       if (declaresInterruptedEx(m)) {
/* 275 */         set.add(m);
/*     */       }
/*     */     }
/* 278 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 282 */     for (Class<?> exType : method.getExceptionTypes())
/*     */     {
/* 284 */       if (exType == InterruptedException.class) {
/* 285 */         return true;
/*     */       }
/*     */     }
/* 288 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler)
/*     */   {
/* 294 */     Object object = Proxy.newProxyInstance(interfaceType
/* 295 */       .getClassLoader(), new Class[] { interfaceType }, handler);
/* 296 */     return (T)interfaceType.cast(object);
/*     */   }
/*     */   
/*     */   private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
/* 300 */     if ((cause instanceof Error))
/* 301 */       throw new ExecutionError((Error)cause);
/* 302 */     if ((cause instanceof RuntimeException)) {
/* 303 */       throw new UncheckedExecutionException(cause);
/*     */     }
/* 305 */     throw new ExecutionException(cause);
/*     */   }
/*     */   
/*     */   private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause)
/*     */   {
/* 310 */     if ((cause instanceof Error)) {
/* 311 */       throw new ExecutionError((Error)cause);
/*     */     }
/* 313 */     throw new UncheckedExecutionException(cause);
/*     */   }
/*     */   
/*     */   private static void checkPositiveTimeout(long timeoutDuration)
/*     */   {
/* 318 */     Preconditions.checkArgument(timeoutDuration > 0L, "timeout must be positive: %s", timeoutDuration);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\SimpleTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */