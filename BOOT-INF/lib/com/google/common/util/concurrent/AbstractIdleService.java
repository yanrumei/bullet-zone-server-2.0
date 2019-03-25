/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractIdleService
/*     */   implements Service
/*     */ {
/*  39 */   private final Supplier<String> threadNameSupplier = new ThreadNameSupplier(null);
/*     */   protected abstract void startUp() throws Exception;
/*     */   
/*     */   private final class ThreadNameSupplier implements Supplier<String> {
/*     */     private ThreadNameSupplier() {}
/*     */     
/*  45 */     public String get() { return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state(); }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  50 */   private final Service delegate = new DelegateService(null);
/*     */   
/*     */   private final class DelegateService extends AbstractService
/*     */   {
/*     */     private DelegateService() {}
/*     */     
/*     */     protected final void doStart() {
/*  57 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/*  62 */             AbstractIdleService.this.startUp();
/*  63 */             AbstractIdleService.DelegateService.this.notifyStarted();
/*     */           } catch (Throwable t) {
/*  65 */             AbstractIdleService.DelegateService.this.notifyFailed(t);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */ 
/*     */     protected final void doStop()
/*     */     {
/*  74 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/*  79 */             AbstractIdleService.this.shutDown();
/*  80 */             AbstractIdleService.DelegateService.this.notifyStopped();
/*     */           } catch (Throwable t) {
/*  82 */             AbstractIdleService.DelegateService.this.notifyFailed(t);
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  90 */       return AbstractIdleService.this.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void shutDown()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Executor executor()
/*     */   {
/* 111 */     new Executor()
/*     */     {
/*     */       public void execute(Runnable command) {
/* 114 */         MoreExecutors.newThread((String)AbstractIdleService.this.threadNameSupplier.get(), command).start();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 121 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */   
/*     */   public final boolean isRunning()
/*     */   {
/* 126 */     return this.delegate.isRunning();
/*     */   }
/*     */   
/*     */   public final Service.State state()
/*     */   {
/* 131 */     return this.delegate.state();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void addListener(Service.Listener listener, Executor executor)
/*     */   {
/* 139 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Throwable failureCause()
/*     */   {
/* 147 */     return this.delegate.failureCause();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync()
/*     */   {
/* 156 */     this.delegate.startAsync();
/* 157 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync()
/*     */   {
/* 166 */     this.delegate.stopAsync();
/* 167 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void awaitRunning()
/*     */   {
/* 175 */     this.delegate.awaitRunning();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void awaitRunning(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 183 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void awaitTerminated()
/*     */   {
/* 191 */     this.delegate.awaitTerminated();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 199 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String serviceName()
/*     */   {
/* 209 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractIdleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */