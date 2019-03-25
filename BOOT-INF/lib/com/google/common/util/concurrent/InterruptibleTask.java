/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ abstract class InterruptibleTask
/*     */   implements Runnable
/*     */ {
/*     */   private volatile Thread runner;
/*     */   private volatile boolean doneInterrupting;
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*  34 */   private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
/*     */   
/*     */   static
/*     */   {
/*     */     AtomicHelper helper;
/*     */     try {
/*  40 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
/*     */     }
/*     */     catch (Throwable reflectionFailure)
/*     */     {
/*     */       AtomicHelper helper;
/*     */       
/*  46 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
/*  47 */       helper = new SynchronizedAtomicHelper(null);
/*     */     }
/*  49 */     ATOMIC_HELPER = helper;
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
/*     */   final void interruptTask()
/*     */   {
/*  82 */     Thread currentRunner = this.runner;
/*  83 */     if (currentRunner != null) {
/*  84 */       currentRunner.interrupt();
/*     */     }
/*  86 */     this.doneInterrupting = true;
/*     */   }
/*     */   
/*     */   abstract boolean wasInterrupted();
/*     */   
/*     */   abstract void runInterruptibly();
/*     */   
/*     */   /* Error */
/*     */   public final void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 3	com/google/common/util/concurrent/InterruptibleTask:ATOMIC_HELPER	Lcom/google/common/util/concurrent/InterruptibleTask$AtomicHelper;
/*     */     //   3: aload_0
/*     */     //   4: aconst_null
/*     */     //   5: invokestatic 4	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   8: invokevirtual 5	com/google/common/util/concurrent/InterruptibleTask$AtomicHelper:compareAndSetRunner	(Lcom/google/common/util/concurrent/InterruptibleTask;Ljava/lang/Thread;Ljava/lang/Thread;)Z
/*     */     //   11: ifne +4 -> 15
/*     */     //   14: return
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual 6	com/google/common/util/concurrent/InterruptibleTask:runInterruptibly	()V
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 7	com/google/common/util/concurrent/InterruptibleTask:wasInterrupted	()Z
/*     */     //   23: ifeq +39 -> 62
/*     */     //   26: aload_0
/*     */     //   27: getfield 8	com/google/common/util/concurrent/InterruptibleTask:doneInterrupting	Z
/*     */     //   30: ifne +32 -> 62
/*     */     //   33: invokestatic 9	java/lang/Thread:yield	()V
/*     */     //   36: goto -10 -> 26
/*     */     //   39: astore_1
/*     */     //   40: aload_0
/*     */     //   41: invokevirtual 7	com/google/common/util/concurrent/InterruptibleTask:wasInterrupted	()Z
/*     */     //   44: ifeq +16 -> 60
/*     */     //   47: aload_0
/*     */     //   48: getfield 8	com/google/common/util/concurrent/InterruptibleTask:doneInterrupting	Z
/*     */     //   51: ifne +9 -> 60
/*     */     //   54: invokestatic 9	java/lang/Thread:yield	()V
/*     */     //   57: goto -10 -> 47
/*     */     //   60: aload_1
/*     */     //   61: athrow
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #54	-> byte code offset #0
/*     */     //   Java source line #55	-> byte code offset #14
/*     */     //   Java source line #58	-> byte code offset #15
/*     */     //   Java source line #60	-> byte code offset #19
/*     */     //   Java source line #67	-> byte code offset #26
/*     */     //   Java source line #68	-> byte code offset #33
/*     */     //   Java source line #60	-> byte code offset #39
/*     */     //   Java source line #67	-> byte code offset #47
/*     */     //   Java source line #68	-> byte code offset #54
/*     */     //   Java source line #72	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	InterruptibleTask
/*     */     //   39	22	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   15	19	39	finally
/*     */   }
/*     */   
/*     */   private static abstract class AtomicHelper
/*     */   {
/*     */     abstract boolean compareAndSetRunner(InterruptibleTask paramInterruptibleTask, Thread paramThread1, Thread paramThread2);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper
/*     */     extends InterruptibleTask.AtomicHelper
/*     */   {
/*     */     final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;
/*     */     
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater runnerUpdater)
/*     */     {
/* 100 */       super();
/* 101 */       this.runnerUpdater = runnerUpdater;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 106 */     boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) { return this.runnerUpdater.compareAndSet(task, expect, update); }
/*     */   }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends InterruptibleTask.AtomicHelper {
/* 110 */     private SynchronizedAtomicHelper() { super(); }
/*     */     
/*     */     boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
/* 113 */       synchronized (task) {
/* 114 */         if (task.runner == expect) {
/* 115 */           task.runner = update;
/*     */         }
/*     */       }
/* 118 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\InterruptibleTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */