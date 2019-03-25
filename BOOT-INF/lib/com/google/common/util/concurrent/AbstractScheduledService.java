/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractScheduledService
/*     */   implements Service
/*     */ {
/*  96 */   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
/*     */   
/*     */ 
/*     */   protected abstract void runOneIteration()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void startUp()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void shutDown()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */   protected abstract Scheduler scheduler();
/*     */   
/*     */ 
/*     */   public static abstract class Scheduler
/*     */   {
/*     */     public static Scheduler newFixedDelaySchedule(long initialDelay, long delay, final TimeUnit unit)
/*     */     {
/* 122 */       Preconditions.checkNotNull(unit);
/* 123 */       Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
/* 124 */       new Scheduler(initialDelay)
/*     */       {
/*     */         public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */         {
/* 128 */           return executor.scheduleWithFixedDelay(task, this.val$initialDelay, unit, this.val$unit);
/*     */         }
/*     */       };
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
/*     */     public static Scheduler newFixedRateSchedule(long initialDelay, long period, final TimeUnit unit)
/*     */     {
/* 143 */       Preconditions.checkNotNull(unit);
/* 144 */       Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
/* 145 */       new Scheduler(initialDelay)
/*     */       {
/*     */         public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */         {
/* 149 */           return executor.scheduleAtFixedRate(task, this.val$initialDelay, unit, this.val$unit);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract Future<?> schedule(AbstractService paramAbstractService, ScheduledExecutorService paramScheduledExecutorService, Runnable paramRunnable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 162 */   private final AbstractService delegate = new ServiceDelegate(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class ServiceDelegate
/*     */     extends AbstractService
/*     */   {
/*     */     private volatile Future<?> runningTask;
/*     */     
/*     */ 
/*     */     private volatile ScheduledExecutorService executorService;
/*     */     
/*     */ 
/* 176 */     private final ReentrantLock lock = new ReentrantLock();
/*     */     private ServiceDelegate() {}
/*     */     
/*     */     class Task implements Runnable {
/*     */       Task() {}
/*     */       
/* 182 */       public void run() { AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */         try {
/* 184 */           if (AbstractScheduledService.ServiceDelegate.this.runningTask.isCancelled())
/*     */           {
/* 186 */             return;
/*     */           }
/* 188 */           AbstractScheduledService.this.runOneIteration();
/*     */         } catch (Throwable t) {
/*     */           try {
/* 191 */             AbstractScheduledService.this.shutDown();
/*     */           } catch (Exception ignored) {
/* 193 */             AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 198 */           AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/* 199 */           AbstractScheduledService.ServiceDelegate.this.runningTask.cancel(false);
/*     */         } finally {
/* 201 */           AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 206 */     private final Runnable task = new Task();
/*     */     
/*     */ 
/*     */     protected final void doStart()
/*     */     {
/* 211 */       this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this
/* 212 */         .executor(), new Supplier()
/*     */         {
/*     */           public String get()
/*     */           {
/* 216 */             return AbstractScheduledService.this.serviceName() + " " + AbstractScheduledService.ServiceDelegate.this.state();
/*     */           }
/* 218 */         });
/* 219 */       this.executorService.execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 223 */           AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */           try {
/* 225 */             AbstractScheduledService.this.startUp();
/* 226 */             AbstractScheduledService.ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, AbstractScheduledService.ServiceDelegate.this.executorService, AbstractScheduledService.ServiceDelegate.this.task);
/* 227 */             AbstractScheduledService.ServiceDelegate.this.notifyStarted();
/*     */           } catch (Throwable t) {
/* 229 */             AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/* 230 */             if (AbstractScheduledService.ServiceDelegate.this.runningTask != null)
/*     */             {
/* 232 */               AbstractScheduledService.ServiceDelegate.this.runningTask.cancel(false);
/*     */             }
/*     */           } finally {
/* 235 */             AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     protected final void doStop()
/*     */     {
/* 243 */       this.runningTask.cancel(false);
/* 244 */       this.executorService.execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/* 249 */             AbstractScheduledService.ServiceDelegate.this.lock.lock();
/*     */             try {
/* 251 */               if (AbstractScheduledService.ServiceDelegate.this.state() != Service.State.STOPPING)
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 256 */                 return;
/*     */               }
/* 258 */               AbstractScheduledService.this.shutDown();
/*     */             } finally {
/* 260 */               AbstractScheduledService.ServiceDelegate.this.lock.unlock();
/*     */             }
/* 262 */             AbstractScheduledService.ServiceDelegate.this.notifyStopped();
/*     */           } catch (Throwable t) {
/* 264 */             AbstractScheduledService.ServiceDelegate.this.notifyFailed(t);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 272 */       return AbstractScheduledService.this.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ScheduledExecutorService executor()
/*     */   {
/* 330 */     final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
/*     */     {
/*     */       public Thread newThread(Runnable runnable)
/*     */       {
/* 326 */         return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 335 */     });
/* 336 */     addListener(new Service.Listener()
/*     */     {
/*     */       public void terminated(Service.State from)
/*     */       {
/* 340 */         executor.shutdown();
/*     */       }
/*     */       
/*     */       public void failed(Service.State from, Throwable failure)
/*     */       {
/* 345 */         executor.shutdown();
/*     */       }
/*     */       
/* 348 */     }, MoreExecutors.directExecutor());
/* 349 */     return executor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String serviceName()
/*     */   {
/* 359 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 364 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */   
/*     */   public final boolean isRunning()
/*     */   {
/* 369 */     return this.delegate.isRunning();
/*     */   }
/*     */   
/*     */   public final Service.State state()
/*     */   {
/* 374 */     return this.delegate.state();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void addListener(Service.Listener listener, Executor executor)
/*     */   {
/* 382 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Throwable failureCause()
/*     */   {
/* 390 */     return this.delegate.failureCause();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync()
/*     */   {
/* 399 */     this.delegate.startAsync();
/* 400 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync()
/*     */   {
/* 409 */     this.delegate.stopAsync();
/* 410 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void awaitRunning()
/*     */   {
/* 418 */     this.delegate.awaitRunning();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void awaitRunning(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 426 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void awaitTerminated()
/*     */   {
/* 434 */     this.delegate.awaitTerminated();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 442 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static abstract class CustomScheduler
/*     */     extends AbstractScheduledService.Scheduler
/*     */   {
/*     */     public CustomScheduler()
/*     */     {
/* 454 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private class ReschedulableCallable
/*     */       extends ForwardingFuture<Void>
/*     */       implements Callable<Void>
/*     */     {
/*     */       private final Runnable wrappedRunnable;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       private final ScheduledExecutorService executor;
/*     */       
/*     */ 
/*     */ 
/*     */       private final AbstractService service;
/*     */       
/*     */ 
/*     */ 
/* 478 */       private final ReentrantLock lock = new ReentrantLock();
/*     */       
/*     */       @GuardedBy("lock")
/*     */       private Future<Void> currentFuture;
/*     */       
/*     */ 
/*     */       ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
/*     */       {
/* 486 */         this.wrappedRunnable = runnable;
/* 487 */         this.executor = executor;
/* 488 */         this.service = service;
/*     */       }
/*     */       
/*     */       public Void call() throws Exception
/*     */       {
/* 493 */         this.wrappedRunnable.run();
/* 494 */         reschedule();
/* 495 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public void reschedule()
/*     */       {
/*     */         try
/*     */         {
/* 505 */           schedule = AbstractScheduledService.CustomScheduler.this.getNextSchedule();
/*     */         } catch (Throwable t) { AbstractScheduledService.CustomScheduler.Schedule schedule;
/* 507 */           this.service.notifyFailed(t); return;
/*     */         }
/*     */         
/*     */ 
/*     */         AbstractScheduledService.CustomScheduler.Schedule schedule;
/*     */         
/*     */ 
/* 514 */         Throwable scheduleFailure = null;
/* 515 */         this.lock.lock();
/*     */         try {
/* 517 */           if ((this.currentFuture == null) || (!this.currentFuture.isCancelled())) {
/* 518 */             this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (Throwable e)
/*     */         {
/*     */ 
/*     */ 
/* 529 */           scheduleFailure = e;
/*     */         } finally {
/* 531 */           this.lock.unlock();
/*     */         }
/*     */         
/* 534 */         if (scheduleFailure != null) {
/* 535 */           this.service.notifyFailed(scheduleFailure);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public boolean cancel(boolean mayInterruptIfRunning)
/*     */       {
/* 544 */         this.lock.lock();
/*     */         try {
/* 546 */           return this.currentFuture.cancel(mayInterruptIfRunning);
/*     */         } finally {
/* 548 */           this.lock.unlock();
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean isCancelled()
/*     */       {
/* 554 */         this.lock.lock();
/*     */         try {
/* 556 */           return this.currentFuture.isCancelled();
/*     */         } finally {
/* 558 */           this.lock.unlock();
/*     */         }
/*     */       }
/*     */       
/*     */       protected Future<Void> delegate()
/*     */       {
/* 564 */         throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
/*     */     {
/* 572 */       ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
/* 573 */       task.reschedule();
/* 574 */       return task;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected abstract Schedule getNextSchedule()
/*     */       throws Exception;
/*     */     
/*     */ 
/*     */ 
/*     */     @Beta
/*     */     protected static final class Schedule
/*     */     {
/*     */       private final long delay;
/*     */       
/*     */       private final TimeUnit unit;
/*     */       
/*     */ 
/*     */       public Schedule(long delay, TimeUnit unit)
/*     */       {
/* 594 */         this.delay = delay;
/* 595 */         this.unit = ((TimeUnit)Preconditions.checkNotNull(unit));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractScheduledService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */