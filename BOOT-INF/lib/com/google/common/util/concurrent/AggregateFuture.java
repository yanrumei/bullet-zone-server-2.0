/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */   private AggregateFuture<InputT, OutputT>.RunningState runningState;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void afterDone()
/*     */   {
/*  49 */     super.afterDone();
/*  50 */     AggregateFuture<InputT, OutputT>.RunningState localRunningState = this.runningState;
/*  51 */     boolean wasInterrupted; UnmodifiableIterator localUnmodifiableIterator; if (localRunningState != null)
/*     */     {
/*  53 */       this.runningState = null;
/*     */       
/*  55 */       ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
/*  56 */       wasInterrupted = wasInterrupted();
/*     */       
/*  58 */       if (wasInterrupted()) {
/*  59 */         localRunningState.interruptTask();
/*     */       }
/*     */       
/*  62 */       if ((isCancelled() & futures != null)) {
/*  63 */         for (localUnmodifiableIterator = futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<?> future = (ListenableFuture)localUnmodifiableIterator.next();
/*  64 */           future.cancel(wasInterrupted);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final void init(AggregateFuture<InputT, OutputT>.RunningState runningState)
/*     */   {
/*  74 */     this.runningState = runningState;
/*  75 */     runningState.init();
/*     */   }
/*     */   
/*     */   abstract class RunningState
/*     */     extends AggregateFutureState implements Runnable
/*     */   {
/*     */     private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */     private final boolean allMustSucceed;
/*     */     private final boolean collectsValues;
/*     */     
/*     */     RunningState(boolean futures, boolean allMustSucceed)
/*     */     {
/*  87 */       super();
/*  88 */       this.futures = ((ImmutableCollection)Preconditions.checkNotNull(futures));
/*  89 */       this.allMustSucceed = allMustSucceed;
/*  90 */       this.collectsValues = collectsValues;
/*     */     }
/*     */     
/*     */ 
/*     */     public final void run()
/*     */     {
/*  96 */       decrementCountAndMaybeComplete();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void init()
/*     */     {
/* 107 */       if (this.futures.isEmpty()) {
/* 108 */         handleAllCompleted(); return;
/*     */       }
/*     */       
/*     */       int i;
/*     */       
/*     */       UnmodifiableIterator localUnmodifiableIterator;
/*     */       
/* 115 */       if (this.allMustSucceed)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */         i = 0;
/* 126 */         for (localUnmodifiableIterator = this.futures.iterator(); localUnmodifiableIterator.hasNext();) { final ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
/* 127 */           final int index = i++;
/* 128 */           listenable.addListener(new Runnable()
/*     */           {
/*     */             /* Error */
/*     */             public void run()
/*     */             {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   4: aload_0
/*     */               //   5: getfield 2	com/google/common/util/concurrent/AggregateFuture$RunningState$1:val$index	I
/*     */               //   8: aload_0
/*     */               //   9: getfield 3	com/google/common/util/concurrent/AggregateFuture$RunningState$1:val$listenable	Lcom/google/common/util/concurrent/ListenableFuture;
/*     */               //   12: invokestatic 5	com/google/common/util/concurrent/AggregateFuture$RunningState:access$200	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;ILjava/util/concurrent/Future;)V
/*     */               //   15: aload_0
/*     */               //   16: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   19: invokestatic 6	com/google/common/util/concurrent/AggregateFuture$RunningState:access$300	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;)V
/*     */               //   22: goto +13 -> 35
/*     */               //   25: astore_1
/*     */               //   26: aload_0
/*     */               //   27: getfield 1	com/google/common/util/concurrent/AggregateFuture$RunningState$1:this$1	Lcom/google/common/util/concurrent/AggregateFuture$RunningState;
/*     */               //   30: invokestatic 6	com/google/common/util/concurrent/AggregateFuture$RunningState:access$300	(Lcom/google/common/util/concurrent/AggregateFuture$RunningState;)V
/*     */               //   33: aload_1
/*     */               //   34: athrow
/*     */               //   35: return
/*     */               // Line number table:
/*     */               //   Java source line #133	-> byte code offset #0
/*     */               //   Java source line #135	-> byte code offset #15
/*     */               //   Java source line #136	-> byte code offset #22
/*     */               //   Java source line #135	-> byte code offset #25
/*     */               //   Java source line #137	-> byte code offset #35
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	signature
/*     */               //   0	36	0	this	1
/*     */               //   25	9	1	localObject	Object
/*     */               // Exception table:
/*     */               //   from	to	target	type
/*     */               //   0	15	25	finally
/*     */             }
/* 139 */           }, MoreExecutors.directExecutor());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 144 */         for (i = this.futures.iterator(); i.hasNext();) { Object listenable = (ListenableFuture)i.next();
/* 145 */           ((ListenableFuture)listenable).addListener(this, MoreExecutors.directExecutor());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void handleException(Throwable throwable)
/*     */     {
/* 157 */       Preconditions.checkNotNull(throwable);
/*     */       
/* 159 */       boolean completedWithFailure = false;
/* 160 */       boolean firstTimeSeeingThisException = true;
/* 161 */       if (this.allMustSucceed)
/*     */       {
/*     */ 
/* 164 */         completedWithFailure = AggregateFuture.this.setException(throwable);
/* 165 */         if (completedWithFailure) {
/* 166 */           releaseResourcesAfterFailure();
/*     */         }
/*     */         else
/*     */         {
/* 170 */           firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 175 */       if ((throwable instanceof Error | this.allMustSucceed & !completedWithFailure & firstTimeSeeingThisException))
/*     */       {
/* 177 */         String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/*     */         
/*     */ 
/*     */ 
/* 181 */         AggregateFuture.logger.log(Level.SEVERE, message, throwable);
/*     */       }
/*     */     }
/*     */     
/*     */     final void addInitialException(Set<Throwable> seen)
/*     */     {
/* 187 */       if (!AggregateFuture.this.isCancelled())
/*     */       {
/* 189 */         boolean bool = AggregateFuture.addCausalChain(seen, AggregateFuture.this.trustedGetException());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void handleOneInputDone(int index, Future<? extends InputT> future)
/*     */     {
/* 199 */       Preconditions.checkState((this.allMustSucceed) || 
/* 200 */         (!AggregateFuture.this.isDone()) || (AggregateFuture.this.isCancelled()), "Future was done before all dependencies completed");
/*     */       
/*     */       try
/*     */       {
/* 204 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/* 205 */         if (this.allMustSucceed) {
/* 206 */           if (future.isCancelled())
/*     */           {
/*     */ 
/* 209 */             AggregateFuture.this.runningState = null;
/* 210 */             AggregateFuture.this.cancel(false);
/*     */           }
/*     */           else {
/* 213 */             InputT result = Futures.getDone(future);
/* 214 */             if (this.collectsValues) {
/* 215 */               collectOneValue(this.allMustSucceed, index, result);
/*     */             }
/*     */           }
/* 218 */         } else if ((this.collectsValues) && (!future.isCancelled())) {
/* 219 */           collectOneValue(this.allMustSucceed, index, Futures.getDone(future));
/*     */         }
/*     */       } catch (ExecutionException e) {
/* 222 */         handleException(e.getCause());
/*     */       } catch (Throwable t) {
/* 224 */         handleException(t);
/*     */       }
/*     */     }
/*     */     
/*     */     private void decrementCountAndMaybeComplete() {
/* 229 */       int newRemaining = decrementRemainingAndGet();
/* 230 */       Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
/* 231 */       if (newRemaining == 0) {
/* 232 */         processCompleted();
/*     */       }
/*     */     }
/*     */     
/*     */     private void processCompleted() {
/*     */       int i;
/*     */       UnmodifiableIterator localUnmodifiableIterator;
/* 239 */       if ((this.collectsValues & !this.allMustSucceed)) {
/* 240 */         i = 0;
/* 241 */         for (localUnmodifiableIterator = this.futures.iterator(); localUnmodifiableIterator.hasNext();) { ListenableFuture<? extends InputT> listenable = (ListenableFuture)localUnmodifiableIterator.next();
/* 242 */           handleOneInputDone(i++, listenable);
/*     */         }
/*     */       }
/* 245 */       handleAllCompleted();
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
/*     */     void releaseResourcesAfterFailure()
/*     */     {
/* 258 */       this.futures = null;
/*     */     }
/*     */     
/*     */     abstract void collectOneValue(boolean paramBoolean, int paramInt, @Nullable InputT paramInputT);
/*     */     
/*     */     abstract void handleAllCompleted();
/*     */     
/*     */     void interruptTask() {}
/*     */   }
/*     */   
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable t)
/*     */   {
/* 276 */     for (; 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 276 */         t != null; t = t.getCause()) {
/* 277 */       boolean firstTimeSeen = seen.add(t);
/* 278 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */         return false;
/*     */       }
/*     */     }
/* 288 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */