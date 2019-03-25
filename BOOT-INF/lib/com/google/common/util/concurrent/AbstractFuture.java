/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.DoNotMock;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @DoNotMock("Use Futures.immediate*Future or SettableFuture")
/*      */ @GwtCompatible(emulated=true)
/*      */ public abstract class AbstractFuture<V>
/*      */   implements ListenableFuture<V>
/*      */ {
/*   68 */   private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(
/*   69 */     System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
/*      */   
/*      */ 
/*      */   static abstract class TrustedFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     @CanIgnoreReturnValue
/*      */     public final V get()
/*      */       throws InterruptedException, ExecutionException
/*      */     {
/*   79 */       return (V)super.get();
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final V get(long timeout, TimeUnit unit)
/*      */       throws InterruptedException, ExecutionException, TimeoutException
/*      */     {
/*   86 */       return (V)super.get(timeout, unit);
/*      */     }
/*      */     
/*      */     public final boolean isDone()
/*      */     {
/*   91 */       return super.isDone();
/*      */     }
/*      */     
/*      */     public final boolean isCancelled()
/*      */     {
/*   96 */       return super.isCancelled();
/*      */     }
/*      */     
/*      */     public final void addListener(Runnable listener, Executor executor)
/*      */     {
/*  101 */       super.addListener(listener, executor);
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean cancel(boolean mayInterruptIfRunning)
/*      */     {
/*  107 */       return super.cancel(mayInterruptIfRunning);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  112 */   private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
/*      */   
/*      */   private static final long SPIN_THRESHOLD_NANOS = 1000L;
/*      */   
/*      */   private static final AtomicHelper ATOMIC_HELPER;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*      */     AtomicHelper helper;
/*      */     try
/*      */     {
/*  124 */       helper = new UnsafeAtomicHelper(null);
/*      */     }
/*      */     catch (Throwable unsafeFailure)
/*      */     {
/*      */       try
/*      */       {
/*      */         AtomicHelper helper;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  136 */         helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
/*      */       }
/*      */       catch (Throwable atomicReferenceFieldUpdaterFailure)
/*      */       {
/*      */         AtomicHelper helper;
/*      */         
/*  142 */         log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", unsafeFailure);
/*  143 */         log.log(Level.SEVERE, "SafeAtomicHelper is broken!", atomicReferenceFieldUpdaterFailure);
/*  144 */         helper = new SynchronizedHelper(null);
/*      */       }
/*      */     }
/*  147 */     ATOMIC_HELPER = helper;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  152 */     unsafeFailure = LockSupport.class;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class Waiter
/*      */   {
/*  159 */     static final Waiter TOMBSTONE = new Waiter(false);
/*      */     
/*      */     @Nullable
/*      */     volatile Thread thread;
/*      */     
/*      */     @Nullable
/*      */     volatile Waiter next;
/*      */     
/*      */ 
/*      */     Waiter(boolean unused) {}
/*      */     
/*      */     Waiter()
/*      */     {
/*  172 */       AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
/*      */     }
/*      */     
/*      */ 
/*      */     void setNext(Waiter next)
/*      */     {
/*  178 */       AbstractFuture.ATOMIC_HELPER.putNext(this, next);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void unpark()
/*      */     {
/*  185 */       Thread w = this.thread;
/*  186 */       if (w != null) {
/*  187 */         this.thread = null;
/*  188 */         LockSupport.unpark(w);
/*      */       }
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
/*      */   private void removeWaiter(Waiter node)
/*      */   {
/*  204 */     node.thread = null;
/*      */     
/*      */ 
/*  207 */     Waiter pred = null;
/*  208 */     Waiter curr = this.waiters;
/*  209 */     if (curr == Waiter.TOMBSTONE) {
/*      */       return;
/*      */     }
/*      */     for (;;) {
/*  213 */       if (curr == null) return;
/*  214 */       Waiter succ = curr.next;
/*  215 */       if (curr.thread != null) {
/*  216 */         pred = curr;
/*  217 */       } else { if (pred != null) {
/*  218 */           pred.next = succ;
/*  219 */           if (pred.thread != null) break label78;
/*  220 */           break;
/*      */         }
/*  222 */         if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) break;
/*      */       }
/*      */       label78:
/*  225 */       curr = succ;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class Listener
/*      */   {
/*  233 */     static final Listener TOMBSTONE = new Listener(null, null);
/*      */     final Runnable task;
/*      */     final Executor executor;
/*      */     @Nullable
/*      */     Listener next;
/*      */     
/*      */     Listener(Runnable task, Executor executor)
/*      */     {
/*  241 */       this.task = task;
/*  242 */       this.executor = executor;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  247 */   private static final Object NULL = new Object();
/*      */   private volatile Object value;
/*      */   
/*      */   private static final class Failure {
/*  251 */     static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
/*      */     {
/*      */ 
/*      */       public synchronized Throwable fillInStackTrace()
/*      */       {
/*  256 */         return this;
/*      */       }
/*  251 */     });
/*      */     
/*      */ 
/*      */ 
/*      */     final Throwable exception;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     Failure(Throwable exception)
/*      */     {
/*  262 */       this.exception = ((Throwable)Preconditions.checkNotNull(exception));
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Cancellation {
/*      */     final boolean wasInterrupted;
/*      */     @Nullable
/*      */     final Throwable cause;
/*      */     
/*      */     Cancellation(boolean wasInterrupted, @Nullable Throwable cause) {
/*  272 */       this.wasInterrupted = wasInterrupted;
/*  273 */       this.cause = cause;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class SetFuture<V> implements Runnable
/*      */   {
/*      */     final AbstractFuture<V> owner;
/*      */     final ListenableFuture<? extends V> future;
/*      */     
/*      */     SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
/*  283 */       this.owner = owner;
/*  284 */       this.future = future;
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*  289 */       if (this.owner.value != this)
/*      */       {
/*  291 */         return;
/*      */       }
/*  293 */       Object valueToSet = AbstractFuture.getFutureValue(this.future);
/*  294 */       if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
/*  295 */         AbstractFuture.complete(this.owner);
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private volatile Listener listeners;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private volatile Waiter waiters;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public V get(long timeout, TimeUnit unit)
/*      */     throws InterruptedException, TimeoutException, ExecutionException
/*      */   {
/*  374 */     long remainingNanos = unit.toNanos(timeout);
/*  375 */     if (Thread.interrupted()) {
/*  376 */       throw new InterruptedException();
/*      */     }
/*  378 */     Object localValue = this.value;
/*  379 */     if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  380 */       return (V)getDoneValue(localValue);
/*      */     }
/*      */     
/*  383 */     long endNanos = remainingNanos > 0L ? System.nanoTime() + remainingNanos : 0L;
/*      */     
/*  385 */     if (remainingNanos >= 1000L) {
/*  386 */       Waiter oldHead = this.waiters;
/*  387 */       if (oldHead != Waiter.TOMBSTONE) {
/*  388 */         Waiter node = new Waiter();
/*      */         do {
/*  390 */           node.setNext(oldHead);
/*  391 */           if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */             do {
/*  393 */               LockSupport.parkNanos(this, remainingNanos);
/*      */               
/*  395 */               if (Thread.interrupted()) {
/*  396 */                 removeWaiter(node);
/*  397 */                 throw new InterruptedException();
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  402 */               localValue = this.value;
/*  403 */               if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  404 */                 return (V)getDoneValue(localValue);
/*      */               }
/*      */               
/*      */ 
/*  408 */               remainingNanos = endNanos - System.nanoTime();
/*  409 */             } while (remainingNanos >= 1000L);
/*      */             
/*  411 */             removeWaiter(node);
/*  412 */             break;
/*      */           }
/*      */           
/*      */ 
/*  416 */           oldHead = this.waiters;
/*  417 */         } while (oldHead != Waiter.TOMBSTONE);
/*      */       }
/*      */       
/*      */ 
/*  421 */       return (V)getDoneValue(this.value);
/*      */     }
/*      */     
/*      */ 
/*  425 */     while (remainingNanos > 0L) {
/*  426 */       localValue = this.value;
/*  427 */       if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  428 */         return (V)getDoneValue(localValue);
/*      */       }
/*  430 */       if (Thread.interrupted()) {
/*  431 */         throw new InterruptedException();
/*      */       }
/*  433 */       remainingNanos = endNanos - System.nanoTime();
/*      */     }
/*  435 */     throw new TimeoutException();
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
/*      */   @CanIgnoreReturnValue
/*      */   public V get()
/*      */     throws InterruptedException, ExecutionException
/*      */   {
/*  457 */     if (Thread.interrupted()) {
/*  458 */       throw new InterruptedException();
/*      */     }
/*  460 */     Object localValue = this.value;
/*  461 */     if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  462 */       return (V)getDoneValue(localValue);
/*      */     }
/*  464 */     Waiter oldHead = this.waiters;
/*  465 */     if (oldHead != Waiter.TOMBSTONE) {
/*  466 */       Waiter node = new Waiter();
/*      */       do {
/*  468 */         node.setNext(oldHead);
/*  469 */         if (ATOMIC_HELPER.casWaiters(this, oldHead, node))
/*      */         {
/*      */           do {
/*  472 */             LockSupport.park(this);
/*      */             
/*  474 */             if (Thread.interrupted()) {
/*  475 */               removeWaiter(node);
/*  476 */               throw new InterruptedException();
/*      */             }
/*      */             
/*      */ 
/*  480 */             localValue = this.value;
/*  481 */           } while (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) == 0);
/*  482 */           return (V)getDoneValue(localValue);
/*      */         }
/*      */         
/*      */ 
/*  486 */         oldHead = this.waiters;
/*  487 */       } while (oldHead != Waiter.TOMBSTONE);
/*      */     }
/*      */     
/*      */ 
/*  491 */     return (V)getDoneValue(this.value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private V getDoneValue(Object obj)
/*      */     throws ExecutionException
/*      */   {
/*  500 */     if ((obj instanceof Cancellation))
/*  501 */       throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause);
/*  502 */     if ((obj instanceof Failure))
/*  503 */       throw new ExecutionException(((Failure)obj).exception);
/*  504 */     if (obj == NULL) {
/*  505 */       return null;
/*      */     }
/*      */     
/*  508 */     V asV = (V)obj;
/*  509 */     return asV;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isDone()
/*      */   {
/*  515 */     Object localValue = this.value;
/*  516 */     return (localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0);
/*      */   }
/*      */   
/*      */   public boolean isCancelled()
/*      */   {
/*  521 */     Object localValue = this.value;
/*  522 */     return localValue instanceof Cancellation;
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
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning)
/*      */   {
/*  541 */     Object localValue = this.value;
/*  542 */     boolean rValue = false;
/*  543 */     if ((localValue == null | localValue instanceof SetFuture))
/*      */     {
/*      */ 
/*  546 */       Throwable cause = GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null;
/*      */       
/*      */ 
/*      */ 
/*  550 */       Object valueToSet = new Cancellation(mayInterruptIfRunning, cause);
/*  551 */       AbstractFuture<?> abstractFuture = this;
/*      */       for (;;) {
/*  553 */         if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
/*  554 */           rValue = true;
/*      */           
/*      */ 
/*  557 */           if (mayInterruptIfRunning) {
/*  558 */             abstractFuture.interruptTask();
/*      */           }
/*  560 */           complete(abstractFuture);
/*  561 */           if ((localValue instanceof SetFuture))
/*      */           {
/*      */ 
/*  564 */             ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
/*  565 */             if ((futureToPropagateTo instanceof TrustedFuture))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  573 */               AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
/*  574 */               localValue = trusted.value;
/*  575 */               if ((localValue == null | localValue instanceof SetFuture)) {
/*  576 */                 abstractFuture = trusted;
/*  577 */                 continue;
/*      */               }
/*      */             }
/*      */             else {
/*  581 */               futureToPropagateTo.cancel(mayInterruptIfRunning);
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  587 */           localValue = abstractFuture.value;
/*  588 */           if (!(localValue instanceof SetFuture)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  596 */     return rValue;
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
/*      */ 
/*      */ 
/*      */   protected final boolean wasInterrupted()
/*      */   {
/*  619 */     Object localValue = this.value;
/*  620 */     return ((localValue instanceof Cancellation)) && (((Cancellation)localValue).wasInterrupted);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addListener(Runnable listener, Executor executor)
/*      */   {
/*  630 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  631 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*  632 */     Listener oldHead = this.listeners;
/*  633 */     if (oldHead != Listener.TOMBSTONE) {
/*  634 */       Listener newNode = new Listener(listener, executor);
/*      */       do {
/*  636 */         newNode.next = oldHead;
/*  637 */         if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
/*  638 */           return;
/*      */         }
/*  640 */         oldHead = this.listeners;
/*  641 */       } while (oldHead != Listener.TOMBSTONE);
/*      */     }
/*      */     
/*      */ 
/*  645 */     executeListener(listener, executor);
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean set(@Nullable V value)
/*      */   {
/*  662 */     Object valueToSet = value == null ? NULL : value;
/*  663 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  664 */       complete(this);
/*  665 */       return true;
/*      */     }
/*  667 */     return false;
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
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setException(Throwable throwable)
/*      */   {
/*  684 */     Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
/*  685 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  686 */       complete(this);
/*  687 */       return true;
/*      */     }
/*  689 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setFuture(ListenableFuture<? extends V> future)
/*      */   {
/*  716 */     Preconditions.checkNotNull(future);
/*  717 */     Object localValue = this.value;
/*  718 */     if (localValue == null) {
/*  719 */       if (future.isDone()) {
/*  720 */         Object value = getFutureValue(future);
/*  721 */         if (ATOMIC_HELPER.casValue(this, null, value)) {
/*  722 */           complete(this);
/*  723 */           return true;
/*      */         }
/*  725 */         return false;
/*      */       }
/*  727 */       SetFuture valueToSet = new SetFuture(this, future);
/*  728 */       if (ATOMIC_HELPER.casValue(this, null, valueToSet))
/*      */       {
/*      */         try
/*      */         {
/*  732 */           future.addListener(valueToSet, MoreExecutors.directExecutor());
/*      */         }
/*      */         catch (Throwable t)
/*      */         {
/*      */           Failure failure;
/*      */           try
/*      */           {
/*  739 */             failure = new Failure(t);
/*      */           } catch (Throwable oomMostLikely) { Failure failure;
/*  741 */             failure = Failure.FALLBACK_INSTANCE;
/*      */           }
/*      */           
/*  744 */           oomMostLikely = ATOMIC_HELPER.casValue(this, valueToSet, failure);
/*      */         }
/*  746 */         return true;
/*      */       }
/*  748 */       localValue = this.value;
/*      */     }
/*      */     
/*      */ 
/*  752 */     if ((localValue instanceof Cancellation))
/*      */     {
/*  754 */       future.cancel(((Cancellation)localValue).wasInterrupted);
/*      */     }
/*  756 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Object getFutureValue(ListenableFuture<?> future)
/*      */   {
/*  767 */     if ((future instanceof TrustedFuture))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  772 */       return ((AbstractFuture)future).value;
/*      */     }
/*      */     Object valueToSet;
/*      */     try {
/*  776 */       Object v = Futures.getDone(future);
/*  777 */       valueToSet = v == null ? NULL : v;
/*      */     } catch (ExecutionException exception) { Object valueToSet;
/*  779 */       valueToSet = new Failure(exception.getCause());
/*      */     } catch (CancellationException cancellation) { Object valueToSet;
/*  781 */       valueToSet = new Cancellation(false, cancellation);
/*      */     } catch (Throwable t) { Object valueToSet;
/*  783 */       valueToSet = new Failure(t);
/*      */     }
/*      */     
/*  786 */     return valueToSet;
/*      */   }
/*      */   
/*      */   private static void complete(AbstractFuture<?> future)
/*      */   {
/*  791 */     Listener next = null;
/*      */     
/*  793 */     future.releaseWaiters();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  798 */     future.afterDone();
/*      */     
/*  800 */     next = future.clearListeners(next);
/*  801 */     future = null;
/*  802 */     label100: for (;;) { if (next == null) return;
/*  803 */       Listener curr = next;
/*  804 */       next = next.next;
/*  805 */       Runnable task = curr.task;
/*  806 */       if ((task instanceof SetFuture)) {
/*  807 */         SetFuture<?> setFuture = (SetFuture)task;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  813 */         future = setFuture.owner;
/*  814 */         if (future.value == setFuture) {
/*  815 */           Object valueToSet = getFutureValue(setFuture.future);
/*  816 */           if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         break label100;
/*      */       }
/*  822 */       executeListener(task, curr.executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final Throwable trustedGetException()
/*      */   {
/*  851 */     return ((Failure)this.value).exception;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void maybePropagateCancellation(@Nullable Future<?> related)
/*      */   {
/*  862 */     if ((related != null & isCancelled())) {
/*  863 */       related.cancel(wasInterrupted());
/*      */     }
/*      */   }
/*      */   
/*      */   private void releaseWaiters()
/*      */   {
/*      */     Waiter head;
/*      */     do {
/*  871 */       head = this.waiters;
/*  872 */     } while (!ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
/*  873 */     for (Waiter currentWaiter = head; 
/*  874 */         currentWaiter != null; 
/*  875 */         currentWaiter = currentWaiter.next) {
/*  876 */       currentWaiter.unpark();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Listener clearListeners(Listener onto)
/*      */   {
/*      */     Listener head;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     do
/*      */     {
/*  893 */       head = this.listeners;
/*  894 */     } while (!ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
/*  895 */     Listener reversedList = onto;
/*  896 */     while (head != null) {
/*  897 */       Listener tmp = head;
/*  898 */       head = head.next;
/*  899 */       tmp.next = reversedList;
/*  900 */       reversedList = tmp;
/*      */     }
/*  902 */     return reversedList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void executeListener(Runnable runnable, Executor executor)
/*      */   {
/*      */     try
/*      */     {
/*  911 */       executor.execute(runnable);
/*      */ 
/*      */     }
/*      */     catch (RuntimeException e)
/*      */     {
/*  916 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static abstract class AtomicHelper
/*      */   {
/*      */     abstract void putThread(AbstractFuture.Waiter paramWaiter, Thread paramThread);
/*      */     
/*      */     abstract void putNext(AbstractFuture.Waiter paramWaiter1, AbstractFuture.Waiter paramWaiter2);
/*      */     
/*      */     abstract boolean casWaiters(AbstractFuture<?> paramAbstractFuture, AbstractFuture.Waiter paramWaiter1, AbstractFuture.Waiter paramWaiter2);
/*      */     
/*      */     abstract boolean casListeners(AbstractFuture<?> paramAbstractFuture, AbstractFuture.Listener paramListener1, AbstractFuture.Listener paramListener2);
/*      */     
/*      */     abstract boolean casValue(AbstractFuture<?> paramAbstractFuture, Object paramObject1, Object paramObject2);
/*      */   }
/*      */   
/*      */   private static final class UnsafeAtomicHelper
/*      */     extends AbstractFuture.AtomicHelper
/*      */   {
/*      */     static final Unsafe UNSAFE;
/*      */     static final long LISTENERS_OFFSET;
/*      */     static final long WAITERS_OFFSET;
/*      */     static final long VALUE_OFFSET;
/*      */     static final long WAITER_THREAD_OFFSET;
/*      */     static final long WAITER_NEXT_OFFSET;
/*      */     
/*      */     private UnsafeAtomicHelper()
/*      */     {
/*  946 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     static
/*      */     {
/*  955 */       Unsafe unsafe = null;
/*      */       try {
/*  957 */         unsafe = Unsafe.getUnsafe();
/*      */       }
/*      */       catch (SecurityException tryReflectionInstead) {
/*      */         try {
/*  961 */           unsafe = (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Unsafe run() throws Exception
/*      */             {
/*  965 */               Class<Unsafe> k = Unsafe.class;
/*  966 */               for (Field f : k.getDeclaredFields()) {
/*  967 */                 f.setAccessible(true);
/*  968 */                 Object x = f.get(null);
/*  969 */                 if (k.isInstance(x)) {
/*  970 */                   return (Unsafe)k.cast(x);
/*      */                 }
/*      */               }
/*  973 */               throw new NoSuchFieldError("the Unsafe");
/*      */             }
/*      */           });
/*      */         } catch (PrivilegedActionException e) {
/*  977 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */         }
/*      */       }
/*      */       try {
/*  981 */         Class<?> abstractFuture = AbstractFuture.class;
/*  982 */         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
/*  983 */         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
/*  984 */         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
/*  985 */         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
/*  986 */         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
/*  987 */         UNSAFE = unsafe;
/*      */       } catch (Exception e) {
/*  989 */         Throwables.throwIfUnchecked(e);
/*  990 */         throw new RuntimeException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue)
/*      */     {
/*  996 */       UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
/*      */     }
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
/*      */     {
/* 1001 */       UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
/*      */     }
/*      */     
/*      */ 
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
/*      */     {
/* 1007 */       return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
/*      */     }
/*      */     
/*      */ 
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
/*      */     {
/* 1013 */       return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
/*      */     }
/*      */     
/*      */ 
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update)
/*      */     {
/* 1019 */       return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class SafeAtomicHelper
/*      */     extends AbstractFuture.AtomicHelper
/*      */   {
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
/*      */     
/*      */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater)
/*      */     {
/* 1036 */       super();
/* 1037 */       this.waiterThreadUpdater = waiterThreadUpdater;
/* 1038 */       this.waiterNextUpdater = waiterNextUpdater;
/* 1039 */       this.waitersUpdater = waitersUpdater;
/* 1040 */       this.listenersUpdater = listenersUpdater;
/* 1041 */       this.valueUpdater = valueUpdater;
/*      */     }
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue)
/*      */     {
/* 1046 */       this.waiterThreadUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
/*      */     {
/* 1051 */       this.waiterNextUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
/*      */     {
/* 1056 */       return this.waitersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
/*      */     {
/* 1061 */       return this.listenersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update)
/*      */     {
/* 1066 */       return this.valueUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class SynchronizedHelper
/*      */     extends AbstractFuture.AtomicHelper
/*      */   {
/*      */     private SynchronizedHelper()
/*      */     {
/* 1076 */       super();
/*      */     }
/*      */     
/* 1079 */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) { waiter.thread = newValue; }
/*      */     
/*      */ 
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
/*      */     {
/* 1084 */       waiter.next = newValue;
/*      */     }
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
/*      */     {
/* 1089 */       synchronized (future) {
/* 1090 */         if (future.waiters == expect) {
/* 1091 */           future.waiters = update;
/* 1092 */           return true;
/*      */         }
/* 1094 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
/*      */     {
/* 1100 */       synchronized (future) {
/* 1101 */         if (future.listeners == expect) {
/* 1102 */           future.listeners = update;
/* 1103 */           return true;
/*      */         }
/* 1105 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update)
/*      */     {
/* 1111 */       synchronized (future) {
/* 1112 */         if (future.value == expect) {
/* 1113 */           future.value = update;
/* 1114 */           return true;
/*      */         }
/* 1116 */         return false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause)
/*      */   {
/* 1123 */     CancellationException exception = new CancellationException(message);
/* 1124 */     exception.initCause(cause);
/* 1125 */     return exception;
/*      */   }
/*      */   
/*      */   protected void interruptTask() {}
/*      */   
/*      */   @Beta
/*      */   protected void afterDone() {}
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */