/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  54 */   private static final ListenerCallQueue.Event<Service.Listener> STARTING_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(Service.Listener listener)
/*     */     {
/*  58 */       listener.starting();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  63 */       return "starting()";
/*     */     }
/*     */   };
/*  66 */   private static final ListenerCallQueue.Event<Service.Listener> RUNNING_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(Service.Listener listener)
/*     */     {
/*  70 */       listener.running();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  75 */       return "running()";
/*     */     }
/*     */   };
/*     */   
/*  79 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_STARTING_EVENT = stoppingEvent(Service.State.STARTING);
/*     */   
/*  81 */   private static final ListenerCallQueue.Event<Service.Listener> STOPPING_FROM_RUNNING_EVENT = stoppingEvent(Service.State.RUNNING);
/*     */   
/*     */ 
/*  84 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_NEW_EVENT = terminatedEvent(Service.State.NEW);
/*     */   
/*  86 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_RUNNING_EVENT = terminatedEvent(Service.State.RUNNING);
/*     */   
/*  88 */   private static final ListenerCallQueue.Event<Service.Listener> TERMINATED_FROM_STOPPING_EVENT = terminatedEvent(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> terminatedEvent(Service.State from) {
/*  91 */     new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener) {
/*  94 */         listener.terminated(this.val$from);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/*  99 */         return "terminated({from = " + this.val$from + "})";
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Event<Service.Listener> stoppingEvent(Service.State from) {
/* 105 */     new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener) {
/* 108 */         listener.stopping(this.val$from);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 113 */         return "stopping({from = " + this.val$from + "})";
/*     */       }
/*     */     };
/*     */   }
/*     */   
/* 118 */   private final Monitor monitor = new Monitor();
/*     */   
/* 120 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */   @ForOverride
/*     */   protected abstract void doStart();
/*     */   
/*     */   private final class IsStartableGuard extends Monitor.Guard {
/* 125 */     IsStartableGuard() { super(); }
/*     */     
/*     */ 
/*     */     public boolean isSatisfied()
/*     */     {
/* 130 */       return AbstractService.this.state() == Service.State.NEW;
/*     */     }
/*     */   }
/*     */   
/* 134 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */   
/*     */   private final class IsStoppableGuard extends Monitor.Guard
/*     */   {
/*     */     IsStoppableGuard() {
/* 139 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 144 */       return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
/*     */     }
/*     */   }
/*     */   
/* 148 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */   
/*     */   private final class HasReachedRunningGuard extends Monitor.Guard
/*     */   {
/*     */     HasReachedRunningGuard() {
/* 153 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 158 */       return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
/*     */     }
/*     */   }
/*     */   
/* 162 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */   
/*     */   private final class IsStoppedGuard extends Monitor.Guard
/*     */   {
/*     */     IsStoppedGuard() {
/* 167 */       super();
/*     */     }
/*     */     
/*     */     public boolean isSatisfied()
/*     */     {
/* 172 */       return AbstractService.this.state().isTerminal();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 177 */   private final ListenerCallQueue<Service.Listener> listeners = new ListenerCallQueue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   protected abstract void doStop();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public final Service startAsync()
/*     */   {
/* 222 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 224 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 225 */         enqueueStartingEvent();
/* 226 */         doStart();
/*     */       } catch (Throwable startupFailure) {
/* 228 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 230 */         this.monitor.leave();
/* 231 */         dispatchListenerEvents();
/*     */       }
/*     */     } else {
/* 234 */       throw new IllegalStateException("Service " + this + " has already been started");
/*     */     }
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync()
/*     */   {
/* 242 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 244 */         Service.State previous = state();
/* 245 */         switch (previous) {
/*     */         case NEW: 
/* 247 */           this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 248 */           enqueueTerminatedEvent(Service.State.NEW);
/* 249 */           break;
/*     */         case STARTING: 
/* 251 */           this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 252 */           enqueueStoppingEvent(Service.State.STARTING);
/* 253 */           break;
/*     */         case RUNNING: 
/* 255 */           this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 256 */           enqueueStoppingEvent(Service.State.RUNNING);
/* 257 */           doStop();
/* 258 */           break;
/*     */         
/*     */         case STOPPING: 
/*     */         case TERMINATED: 
/*     */         case FAILED: 
/* 263 */           throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
/*     */         default: 
/* 265 */           throw new AssertionError("Unexpected state: " + previous);
/*     */         }
/*     */       } catch (Throwable shutdownFailure) {
/* 268 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 270 */         this.monitor.leave();
/* 271 */         dispatchListenerEvents();
/*     */       }
/*     */     }
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public final void awaitRunning()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: aload_0
/*     */     //   5: getfield 17	com/google/common/util/concurrent/AbstractService:hasReachedRunning	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */     //   8: invokevirtual 59	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */     //   11: aload_0
/*     */     //   12: getstatic 53	com/google/common/util/concurrent/Service$State:RUNNING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   15: invokespecial 60	com/google/common/util/concurrent/AbstractService:checkCurrentState	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   18: aload_0
/*     */     //   19: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   22: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   25: goto +13 -> 38
/*     */     //   28: astore_1
/*     */     //   29: aload_0
/*     */     //   30: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   33: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   36: aload_1
/*     */     //   37: athrow
/*     */     //   38: return
/*     */     // Line number table:
/*     */     //   Java source line #279	-> byte code offset #0
/*     */     //   Java source line #281	-> byte code offset #11
/*     */     //   Java source line #283	-> byte code offset #18
/*     */     //   Java source line #284	-> byte code offset #25
/*     */     //   Java source line #283	-> byte code offset #28
/*     */     //   Java source line #285	-> byte code offset #38
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	AbstractService
/*     */     //   28	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	18	28	finally
/*     */   }
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 289 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 291 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 293 */         this.monitor.leave();
/*     */ 
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 300 */       throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public final void awaitTerminated()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: aload_0
/*     */     //   5: getfield 20	com/google/common/util/concurrent/AbstractService:isStopped	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */     //   8: invokevirtual 59	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */     //   11: aload_0
/*     */     //   12: getstatic 48	com/google/common/util/concurrent/Service$State:TERMINATED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   15: invokespecial 60	com/google/common/util/concurrent/AbstractService:checkCurrentState	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   18: aload_0
/*     */     //   19: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   22: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   25: goto +13 -> 38
/*     */     //   28: astore_1
/*     */     //   29: aload_0
/*     */     //   30: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   33: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   36: aload_1
/*     */     //   37: athrow
/*     */     //   38: return
/*     */     // Line number table:
/*     */     //   Java source line #306	-> byte code offset #0
/*     */     //   Java source line #308	-> byte code offset #11
/*     */     //   Java source line #310	-> byte code offset #18
/*     */     //   Java source line #311	-> byte code offset #25
/*     */     //   Java source line #310	-> byte code offset #28
/*     */     //   Java source line #312	-> byte code offset #38
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	AbstractService
/*     */     //   28	9	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	18	28	finally
/*     */   }
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 316 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 318 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 320 */         this.monitor.leave();
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 332 */       throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + state());
/*     */     }
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected)
/*     */   {
/* 339 */     Service.State actual = state();
/* 340 */     if (actual != expected) {
/* 341 */       if (actual == Service.State.FAILED)
/*     */       {
/*     */ 
/*     */ 
/* 345 */         throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", failureCause());
/*     */       }
/* 347 */       throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyStarted()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   11: getfield 75	com/google/common/util/concurrent/AbstractService$StateSnapshot:state	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   14: getstatic 29	com/google/common/util/concurrent/Service$State:STARTING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   17: if_acmpeq +43 -> 60
/*     */     //   20: new 36	java/lang/IllegalStateException
/*     */     //   23: dup
/*     */     //   24: new 37	java/lang/StringBuilder
/*     */     //   27: dup
/*     */     //   28: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   31: ldc 76
/*     */     //   33: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   36: aload_0
/*     */     //   37: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   40: getfield 75	com/google/common/util/concurrent/AbstractService$StateSnapshot:state	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   43: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   46: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   49: invokespecial 44	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   52: astore_1
/*     */     //   53: aload_0
/*     */     //   54: aload_1
/*     */     //   55: invokevirtual 35	com/google/common/util/concurrent/AbstractService:notifyFailed	(Ljava/lang/Throwable;)V
/*     */     //   58: aload_1
/*     */     //   59: athrow
/*     */     //   60: aload_0
/*     */     //   61: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   64: getfield 77	com/google/common/util/concurrent/AbstractService$StateSnapshot:shutdownWhenStartupFinishes	Z
/*     */     //   67: ifeq +24 -> 91
/*     */     //   70: aload_0
/*     */     //   71: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   74: dup
/*     */     //   75: getstatic 52	com/google/common/util/concurrent/Service$State:STOPPING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   78: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   81: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   84: aload_0
/*     */     //   85: invokevirtual 54	com/google/common/util/concurrent/AbstractService:doStop	()V
/*     */     //   88: goto +21 -> 109
/*     */     //   91: aload_0
/*     */     //   92: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   95: dup
/*     */     //   96: getstatic 53	com/google/common/util/concurrent/Service$State:RUNNING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   99: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   102: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   105: aload_0
/*     */     //   106: invokespecial 78	com/google/common/util/concurrent/AbstractService:enqueueRunningEvent	()V
/*     */     //   109: aload_0
/*     */     //   110: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   113: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   116: aload_0
/*     */     //   117: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   120: goto +17 -> 137
/*     */     //   123: astore_2
/*     */     //   124: aload_0
/*     */     //   125: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   128: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   131: aload_0
/*     */     //   132: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   135: aload_2
/*     */     //   136: athrow
/*     */     //   137: return
/*     */     // Line number table:
/*     */     //   Java source line #359	-> byte code offset #0
/*     */     //   Java source line #363	-> byte code offset #7
/*     */     //   Java source line #364	-> byte code offset #20
/*     */     //   Java source line #367	-> byte code offset #53
/*     */     //   Java source line #368	-> byte code offset #58
/*     */     //   Java source line #371	-> byte code offset #60
/*     */     //   Java source line #372	-> byte code offset #70
/*     */     //   Java source line #375	-> byte code offset #84
/*     */     //   Java source line #377	-> byte code offset #91
/*     */     //   Java source line #378	-> byte code offset #105
/*     */     //   Java source line #381	-> byte code offset #109
/*     */     //   Java source line #382	-> byte code offset #116
/*     */     //   Java source line #383	-> byte code offset #120
/*     */     //   Java source line #381	-> byte code offset #123
/*     */     //   Java source line #382	-> byte code offset #131
/*     */     //   Java source line #384	-> byte code offset #137
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	138	0	this	AbstractService
/*     */     //   52	7	1	failure	IllegalStateException
/*     */     //   123	13	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	109	123	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyStopped()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   4: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   7: aload_0
/*     */     //   8: getfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   11: getfield 75	com/google/common/util/concurrent/AbstractService$StateSnapshot:state	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   14: astore_1
/*     */     //   15: aload_1
/*     */     //   16: getstatic 52	com/google/common/util/concurrent/Service$State:STOPPING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   19: if_acmpeq +44 -> 63
/*     */     //   22: aload_1
/*     */     //   23: getstatic 53	com/google/common/util/concurrent/Service$State:RUNNING	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   26: if_acmpeq +37 -> 63
/*     */     //   29: new 36	java/lang/IllegalStateException
/*     */     //   32: dup
/*     */     //   33: new 37	java/lang/StringBuilder
/*     */     //   36: dup
/*     */     //   37: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   40: ldc 79
/*     */     //   42: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   45: aload_1
/*     */     //   46: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   49: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   52: invokespecial 44	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   55: astore_2
/*     */     //   56: aload_0
/*     */     //   57: aload_2
/*     */     //   58: invokevirtual 35	com/google/common/util/concurrent/AbstractService:notifyFailed	(Ljava/lang/Throwable;)V
/*     */     //   61: aload_2
/*     */     //   62: athrow
/*     */     //   63: aload_0
/*     */     //   64: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   67: dup
/*     */     //   68: getstatic 48	com/google/common/util/concurrent/Service$State:TERMINATED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   71: invokespecial 26	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   74: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   77: aload_0
/*     */     //   78: aload_1
/*     */     //   79: invokespecial 49	com/google/common/util/concurrent/AbstractService:enqueueTerminatedEvent	(Lcom/google/common/util/concurrent/Service$State;)V
/*     */     //   82: aload_0
/*     */     //   83: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   86: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   89: aload_0
/*     */     //   90: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   93: goto +17 -> 110
/*     */     //   96: astore_3
/*     */     //   97: aload_0
/*     */     //   98: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   101: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   104: aload_0
/*     */     //   105: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   108: aload_3
/*     */     //   109: athrow
/*     */     //   110: return
/*     */     // Line number table:
/*     */     //   Java source line #394	-> byte code offset #0
/*     */     //   Java source line #398	-> byte code offset #7
/*     */     //   Java source line #399	-> byte code offset #15
/*     */     //   Java source line #400	-> byte code offset #29
/*     */     //   Java source line #402	-> byte code offset #56
/*     */     //   Java source line #403	-> byte code offset #61
/*     */     //   Java source line #405	-> byte code offset #63
/*     */     //   Java source line #406	-> byte code offset #77
/*     */     //   Java source line #408	-> byte code offset #82
/*     */     //   Java source line #409	-> byte code offset #89
/*     */     //   Java source line #410	-> byte code offset #93
/*     */     //   Java source line #408	-> byte code offset #96
/*     */     //   Java source line #409	-> byte code offset #104
/*     */     //   Java source line #411	-> byte code offset #110
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	111	0	this	AbstractService
/*     */     //   14	65	1	previous	Service.State
/*     */     //   55	7	2	failure	IllegalStateException
/*     */     //   96	13	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	82	96	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected final void notifyFailed(Throwable cause)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic 80	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: aload_0
/*     */     //   6: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   9: invokevirtual 74	com/google/common/util/concurrent/Monitor:enter	()V
/*     */     //   12: aload_0
/*     */     //   13: invokevirtual 45	com/google/common/util/concurrent/AbstractService:state	()Lcom/google/common/util/concurrent/Service$State;
/*     */     //   16: astore_2
/*     */     //   17: getstatic 46	com/google/common/util/concurrent/AbstractService$6:$SwitchMap$com$google$common$util$concurrent$Service$State	[I
/*     */     //   20: aload_2
/*     */     //   21: invokevirtual 47	com/google/common/util/concurrent/Service$State:ordinal	()I
/*     */     //   24: iaload
/*     */     //   25: tableswitch	default:+95->120, 1:+39->64, 2:+67->92, 3:+67->92, 4:+67->92, 5:+39->64, 6:+92->117
/*     */     //   64: new 36	java/lang/IllegalStateException
/*     */     //   67: dup
/*     */     //   68: new 37	java/lang/StringBuilder
/*     */     //   71: dup
/*     */     //   72: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   75: ldc 81
/*     */     //   77: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   80: aload_2
/*     */     //   81: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   84: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   87: aload_1
/*     */     //   88: invokespecial 72	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   91: athrow
/*     */     //   92: aload_0
/*     */     //   93: new 24	com/google/common/util/concurrent/AbstractService$StateSnapshot
/*     */     //   96: dup
/*     */     //   97: getstatic 67	com/google/common/util/concurrent/Service$State:FAILED	Lcom/google/common/util/concurrent/Service$State;
/*     */     //   100: iconst_0
/*     */     //   101: aload_1
/*     */     //   102: invokespecial 50	com/google/common/util/concurrent/AbstractService$StateSnapshot:<init>	(Lcom/google/common/util/concurrent/Service$State;ZLjava/lang/Throwable;)V
/*     */     //   105: putfield 27	com/google/common/util/concurrent/AbstractService:snapshot	Lcom/google/common/util/concurrent/AbstractService$StateSnapshot;
/*     */     //   108: aload_0
/*     */     //   109: aload_2
/*     */     //   110: aload_1
/*     */     //   111: invokespecial 82	com/google/common/util/concurrent/AbstractService:enqueueFailedEvent	(Lcom/google/common/util/concurrent/Service$State;Ljava/lang/Throwable;)V
/*     */     //   114: goto +33 -> 147
/*     */     //   117: goto +30 -> 147
/*     */     //   120: new 55	java/lang/AssertionError
/*     */     //   123: dup
/*     */     //   124: new 37	java/lang/StringBuilder
/*     */     //   127: dup
/*     */     //   128: invokespecial 38	java/lang/StringBuilder:<init>	()V
/*     */     //   131: ldc 58
/*     */     //   133: invokevirtual 40	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   136: aload_2
/*     */     //   137: invokevirtual 41	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   140: invokevirtual 43	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   143: invokespecial 57	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
/*     */     //   146: athrow
/*     */     //   147: aload_0
/*     */     //   148: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   151: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   154: aload_0
/*     */     //   155: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   158: goto +17 -> 175
/*     */     //   161: astore_3
/*     */     //   162: aload_0
/*     */     //   163: getfield 1	com/google/common/util/concurrent/AbstractService:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */     //   166: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */     //   169: aload_0
/*     */     //   170: invokespecial 33	com/google/common/util/concurrent/AbstractService:dispatchListenerEvents	()V
/*     */     //   173: aload_3
/*     */     //   174: athrow
/*     */     //   175: return
/*     */     // Line number table:
/*     */     //   Java source line #419	-> byte code offset #0
/*     */     //   Java source line #421	-> byte code offset #5
/*     */     //   Java source line #423	-> byte code offset #12
/*     */     //   Java source line #424	-> byte code offset #17
/*     */     //   Java source line #427	-> byte code offset #64
/*     */     //   Java source line #431	-> byte code offset #92
/*     */     //   Java source line #432	-> byte code offset #108
/*     */     //   Java source line #433	-> byte code offset #114
/*     */     //   Java source line #436	-> byte code offset #117
/*     */     //   Java source line #438	-> byte code offset #120
/*     */     //   Java source line #441	-> byte code offset #147
/*     */     //   Java source line #442	-> byte code offset #154
/*     */     //   Java source line #443	-> byte code offset #158
/*     */     //   Java source line #441	-> byte code offset #161
/*     */     //   Java source line #442	-> byte code offset #169
/*     */     //   Java source line #444	-> byte code offset #175
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	176	0	this	AbstractService
/*     */     //   0	176	1	cause	Throwable
/*     */     //   16	121	2	previous	Service.State
/*     */     //   161	13	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   12	147	161	finally
/*     */   }
/*     */   
/*     */   public final boolean isRunning()
/*     */   {
/* 448 */     return state() == Service.State.RUNNING;
/*     */   }
/*     */   
/*     */   public final Service.State state()
/*     */   {
/* 453 */     return this.snapshot.externalState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Throwable failureCause()
/*     */   {
/* 461 */     return this.snapshot.failureCause();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void addListener(Service.Listener listener, Executor executor)
/*     */   {
/* 469 */     this.listeners.addListener(listener, executor);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 474 */     return getClass().getSimpleName() + " [" + state() + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void dispatchListenerEvents()
/*     */   {
/* 482 */     if (!this.monitor.isOccupiedByCurrentThread()) {
/* 483 */       this.listeners.dispatch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueStartingEvent() {
/* 488 */     this.listeners.enqueue(STARTING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueRunningEvent() {
/* 492 */     this.listeners.enqueue(RUNNING_EVENT);
/*     */   }
/*     */   
/*     */   private void enqueueStoppingEvent(Service.State from) {
/* 496 */     if (from == Service.State.STARTING) {
/* 497 */       this.listeners.enqueue(STOPPING_FROM_STARTING_EVENT);
/* 498 */     } else if (from == Service.State.RUNNING) {
/* 499 */       this.listeners.enqueue(STOPPING_FROM_RUNNING_EVENT);
/*     */     } else {
/* 501 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueTerminatedEvent(Service.State from) {
/* 506 */     switch (from) {
/*     */     case NEW: 
/* 508 */       this.listeners.enqueue(TERMINATED_FROM_NEW_EVENT);
/* 509 */       break;
/*     */     case RUNNING: 
/* 511 */       this.listeners.enqueue(TERMINATED_FROM_RUNNING_EVENT);
/* 512 */       break;
/*     */     case STOPPING: 
/* 514 */       this.listeners.enqueue(TERMINATED_FROM_STOPPING_EVENT);
/* 515 */       break;
/*     */     case STARTING: 
/*     */     case TERMINATED: 
/*     */     case FAILED: 
/*     */     default: 
/* 520 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */   private void enqueueFailedEvent(final Service.State from, final Throwable cause)
/*     */   {
/* 526 */     this.listeners.enqueue(new ListenerCallQueue.Event()
/*     */     {
/*     */       public void call(Service.Listener listener)
/*     */       {
/* 530 */         listener.failed(from, cause);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 535 */         return "failed({from = " + from + ", cause = " + cause + "})";
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Immutable
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */     
/*     */ 
/*     */ 
/*     */     @Nullable
/*     */     final Throwable failure;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     StateSnapshot(Service.State internalState)
/*     */     {
/* 564 */       this(internalState, false, null);
/*     */     }
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure)
/*     */     {
/* 569 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes) || (internalState == Service.State.STARTING), "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */       
/*     */ 
/*     */ 
/* 573 */       Preconditions.checkArgument(((failure != null ? 1 : 0) ^ (internalState == Service.State.FAILED ? 1 : 0)) == 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 579 */       this.state = internalState;
/* 580 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 581 */       this.failure = failure;
/*     */     }
/*     */     
/*     */     Service.State externalState()
/*     */     {
/* 586 */       if ((this.shutdownWhenStartupFinishes) && (this.state == Service.State.STARTING)) {
/* 587 */         return Service.State.STOPPING;
/*     */       }
/* 589 */       return this.state;
/*     */     }
/*     */     
/*     */ 
/*     */     Throwable failureCause()
/*     */     {
/* 595 */       Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */       
/*     */ 
/*     */ 
/* 599 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */