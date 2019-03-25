/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import javax.annotation.concurrent.GuardedBy;
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
/*      */ @Beta
/*      */ @GwtIncompatible
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   
/*      */   @Beta
/*      */   public static abstract class Guard
/*      */   {
/*      */     @Weak
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*  308 */     int waiterCount = 0;
/*      */     
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */     
/*      */ 
/*      */     protected Guard(Monitor monitor)
/*      */     {
/*  316 */       this.monitor = ((Monitor)Preconditions.checkNotNull(monitor, "monitor"));
/*  317 */       this.condition = monitor.lock.newCondition();
/*      */     }
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
/*      */     public abstract boolean isSatisfied();
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
/*      */   @GuardedBy("lock")
/*  342 */   private Guard activeGuards = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Monitor()
/*      */   {
/*  350 */     this(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Monitor(boolean fair)
/*      */   {
/*  360 */     this.fair = fair;
/*  361 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Guard newGuard(final BooleanSupplier isSatisfied)
/*      */   {
/*  370 */     Preconditions.checkNotNull(isSatisfied, "isSatisfied");
/*  371 */     new Guard(this)
/*      */     {
/*      */       public boolean isSatisfied() {
/*  374 */         return isSatisfied.getAsBoolean();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void enter()
/*      */   {
/*  383 */     this.lock.lock();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enterInterruptibly()
/*      */     throws InterruptedException
/*      */   {
/*  392 */     this.lock.lockInterruptibly();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean enter(long time, TimeUnit unit)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: lload_1
/*      */     //   1: aload_3
/*      */     //   2: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
/*      */     //   5: lstore 4
/*      */     //   7: aload_0
/*      */     //   8: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   11: astore 6
/*      */     //   13: aload_0
/*      */     //   14: getfield 5	com/google/common/util/concurrent/Monitor:fair	Z
/*      */     //   17: ifne +13 -> 30
/*      */     //   20: aload 6
/*      */     //   22: invokevirtual 15	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*      */     //   25: ifeq +5 -> 30
/*      */     //   28: iconst_1
/*      */     //   29: ireturn
/*      */     //   30: invokestatic 16	java/lang/Thread:interrupted	()Z
/*      */     //   33: istore 7
/*      */     //   35: invokestatic 17	java/lang/System:nanoTime	()J
/*      */     //   38: lstore 8
/*      */     //   40: lload 4
/*      */     //   42: lstore 10
/*      */     //   44: aload 6
/*      */     //   46: lload 10
/*      */     //   48: getstatic 18	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
/*      */     //   51: invokevirtual 19	java/util/concurrent/locks/ReentrantLock:tryLock	(JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   54: istore 12
/*      */     //   56: iload 7
/*      */     //   58: ifeq +9 -> 67
/*      */     //   61: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   64: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   67: iload 12
/*      */     //   69: ireturn
/*      */     //   70: astore 12
/*      */     //   72: iconst_1
/*      */     //   73: istore 7
/*      */     //   75: lload 8
/*      */     //   77: lload 4
/*      */     //   79: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
/*      */     //   82: lstore 10
/*      */     //   84: goto -40 -> 44
/*      */     //   87: astore 13
/*      */     //   89: iload 7
/*      */     //   91: ifeq +9 -> 100
/*      */     //   94: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   97: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   100: aload 13
/*      */     //   102: athrow
/*      */     // Line number table:
/*      */     //   Java source line #401	-> byte code offset #0
/*      */     //   Java source line #402	-> byte code offset #7
/*      */     //   Java source line #403	-> byte code offset #13
/*      */     //   Java source line #404	-> byte code offset #28
/*      */     //   Java source line #406	-> byte code offset #30
/*      */     //   Java source line #408	-> byte code offset #35
/*      */     //   Java source line #409	-> byte code offset #40
/*      */     //   Java source line #411	-> byte code offset #44
/*      */     //   Java source line #418	-> byte code offset #56
/*      */     //   Java source line #419	-> byte code offset #61
/*      */     //   Java source line #411	-> byte code offset #67
/*      */     //   Java source line #412	-> byte code offset #70
/*      */     //   Java source line #413	-> byte code offset #72
/*      */     //   Java source line #414	-> byte code offset #75
/*      */     //   Java source line #415	-> byte code offset #84
/*      */     //   Java source line #418	-> byte code offset #87
/*      */     //   Java source line #419	-> byte code offset #94
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	103	0	this	Monitor
/*      */     //   0	103	1	time	long
/*      */     //   0	103	3	unit	TimeUnit
/*      */     //   5	73	4	timeoutNanos	long
/*      */     //   11	34	6	lock	ReentrantLock
/*      */     //   33	57	7	interrupted	boolean
/*      */     //   38	38	8	startTime	long
/*      */     //   42	41	10	remainingNanos	long
/*      */     //   54	14	12	bool1	boolean
/*      */     //   70	3	12	interrupt	InterruptedException
/*      */     //   87	14	13	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   44	56	70	java/lang/InterruptedException
/*      */     //   35	56	87	finally
/*      */     //   70	89	87	finally
/*      */   }
/*      */   
/*      */   public boolean enterInterruptibly(long time, TimeUnit unit)
/*      */     throws InterruptedException
/*      */   {
/*  431 */     return this.lock.tryLock(time, unit);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean tryEnter()
/*      */   {
/*  442 */     return this.lock.tryLock();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enterWhen(Guard guard)
/*      */     throws InterruptedException
/*      */   {
/*  451 */     if (guard.monitor != this) {
/*  452 */       throw new IllegalMonitorStateException();
/*      */     }
/*  454 */     ReentrantLock lock = this.lock;
/*  455 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  456 */     lock.lockInterruptibly();
/*      */     
/*  458 */     boolean satisfied = false;
/*      */     try {
/*  460 */       if (!guard.isSatisfied()) {
/*  461 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  463 */       satisfied = true;
/*      */     } finally {
/*  465 */       if (!satisfied) {
/*  466 */         leave();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void enterWhenUninterruptibly(Guard guard)
/*      */   {
/*  475 */     if (guard.monitor != this) {
/*  476 */       throw new IllegalMonitorStateException();
/*      */     }
/*  478 */     ReentrantLock lock = this.lock;
/*  479 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  480 */     lock.lock();
/*      */     
/*  482 */     boolean satisfied = false;
/*      */     try {
/*  484 */       if (!guard.isSatisfied()) {
/*  485 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  487 */       satisfied = true;
/*      */     } finally {
/*  489 */       if (!satisfied) {
/*  490 */         leave();
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
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit)
/*      */     throws InterruptedException
/*      */   {
/*  504 */     long timeoutNanos = toSafeNanos(time, unit);
/*  505 */     if (guard.monitor != this) {
/*  506 */       throw new IllegalMonitorStateException();
/*      */     }
/*  508 */     ReentrantLock lock = this.lock;
/*  509 */     boolean reentrant = lock.isHeldByCurrentThread();
/*  510 */     long startTime = 0L;
/*      */     
/*      */ 
/*      */ 
/*  514 */     if (!this.fair)
/*      */     {
/*  516 */       if (Thread.interrupted()) {
/*  517 */         throw new InterruptedException();
/*      */       }
/*  519 */       if (lock.tryLock()) {}
/*      */     }
/*      */     else
/*      */     {
/*  523 */       startTime = initNanoTime(timeoutNanos);
/*  524 */       if (!lock.tryLock(time, unit)) {
/*  525 */         return false;
/*      */       }
/*      */     }
/*      */     
/*  529 */     boolean satisfied = false;
/*  530 */     boolean threw = true;
/*      */     try
/*      */     {
/*  533 */       if (!guard.isSatisfied()) {}
/*  534 */       satisfied = awaitNanos(guard, startTime == 0L ? timeoutNanos : 
/*      */       
/*  536 */         remainingNanos(startTime, timeoutNanos), reentrant);
/*      */       
/*  538 */       threw = false;
/*  539 */       return satisfied;
/*      */     } finally {
/*  541 */       if (!satisfied) {
/*      */         try
/*      */         {
/*  544 */           if ((threw) && (!reentrant)) {
/*  545 */             signalNextWaiter();
/*      */           }
/*      */         } finally {
/*  548 */           lock.unlock();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload 4
/*      */     //   3: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore 5
/*      */     //   8: aload_1
/*      */     //   9: getfield 24	com/google/common/util/concurrent/Monitor$Guard:monitor	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpeq +11 -> 24
/*      */     //   16: new 25	java/lang/IllegalMonitorStateException
/*      */     //   19: dup
/*      */     //   20: invokespecial 26	java/lang/IllegalMonitorStateException:<init>	()V
/*      */     //   23: athrow
/*      */     //   24: aload_0
/*      */     //   25: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   28: astore 7
/*      */     //   30: lconst_0
/*      */     //   31: lstore 8
/*      */     //   33: aload 7
/*      */     //   35: invokevirtual 27	java/util/concurrent/locks/ReentrantLock:isHeldByCurrentThread	()Z
/*      */     //   38: istore 10
/*      */     //   40: invokestatic 16	java/lang/Thread:interrupted	()Z
/*      */     //   43: istore 11
/*      */     //   45: aload_0
/*      */     //   46: getfield 5	com/google/common/util/concurrent/Monitor:fair	Z
/*      */     //   49: ifne +11 -> 60
/*      */     //   52: aload 7
/*      */     //   54: invokevirtual 15	java/util/concurrent/locks/ReentrantLock:tryLock	()Z
/*      */     //   57: ifne +64 -> 121
/*      */     //   60: lload 5
/*      */     //   62: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
/*      */     //   65: lstore 8
/*      */     //   67: lload 5
/*      */     //   69: lstore 12
/*      */     //   71: aload 7
/*      */     //   73: lload 12
/*      */     //   75: getstatic 18	java/util/concurrent/TimeUnit:NANOSECONDS	Ljava/util/concurrent/TimeUnit;
/*      */     //   78: invokevirtual 19	java/util/concurrent/locks/ReentrantLock:tryLock	(JLjava/util/concurrent/TimeUnit;)Z
/*      */     //   81: ifeq +6 -> 87
/*      */     //   84: goto +37 -> 121
/*      */     //   87: iconst_0
/*      */     //   88: istore 14
/*      */     //   90: iload 11
/*      */     //   92: ifeq +9 -> 101
/*      */     //   95: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   98: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   101: iload 14
/*      */     //   103: ireturn
/*      */     //   104: astore 14
/*      */     //   106: iconst_1
/*      */     //   107: istore 11
/*      */     //   109: lload 8
/*      */     //   111: lload 5
/*      */     //   113: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
/*      */     //   116: lstore 12
/*      */     //   118: goto -47 -> 71
/*      */     //   121: iconst_0
/*      */     //   122: istore 12
/*      */     //   124: aload_1
/*      */     //   125: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
/*      */     //   128: ifeq +9 -> 137
/*      */     //   131: iconst_1
/*      */     //   132: istore 12
/*      */     //   134: goto +44 -> 178
/*      */     //   137: lload 8
/*      */     //   139: lconst_0
/*      */     //   140: lcmp
/*      */     //   141: ifne +17 -> 158
/*      */     //   144: lload 5
/*      */     //   146: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
/*      */     //   149: lstore 8
/*      */     //   151: lload 5
/*      */     //   153: lstore 13
/*      */     //   155: goto +12 -> 167
/*      */     //   158: lload 8
/*      */     //   160: lload 5
/*      */     //   162: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
/*      */     //   165: lstore 13
/*      */     //   167: aload_0
/*      */     //   168: aload_1
/*      */     //   169: lload 13
/*      */     //   171: iload 10
/*      */     //   173: invokespecial 34	com/google/common/util/concurrent/Monitor:awaitNanos	(Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   176: istore 12
/*      */     //   178: iload 12
/*      */     //   180: istore 13
/*      */     //   182: iload 12
/*      */     //   184: ifne +8 -> 192
/*      */     //   187: aload 7
/*      */     //   189: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*      */     //   192: iload 11
/*      */     //   194: ifeq +9 -> 203
/*      */     //   197: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   200: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   203: iload 13
/*      */     //   205: ireturn
/*      */     //   206: astore 13
/*      */     //   208: iconst_1
/*      */     //   209: istore 11
/*      */     //   211: iconst_0
/*      */     //   212: istore 10
/*      */     //   214: goto -90 -> 124
/*      */     //   217: astore 15
/*      */     //   219: iload 12
/*      */     //   221: ifne +8 -> 229
/*      */     //   224: aload 7
/*      */     //   226: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*      */     //   229: aload 15
/*      */     //   231: athrow
/*      */     //   232: astore 16
/*      */     //   234: iload 11
/*      */     //   236: ifeq +9 -> 245
/*      */     //   239: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   242: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   245: aload 16
/*      */     //   247: athrow
/*      */     // Line number table:
/*      */     //   Java source line #561	-> byte code offset #0
/*      */     //   Java source line #562	-> byte code offset #8
/*      */     //   Java source line #563	-> byte code offset #16
/*      */     //   Java source line #565	-> byte code offset #24
/*      */     //   Java source line #566	-> byte code offset #30
/*      */     //   Java source line #567	-> byte code offset #33
/*      */     //   Java source line #568	-> byte code offset #40
/*      */     //   Java source line #570	-> byte code offset #45
/*      */     //   Java source line #571	-> byte code offset #60
/*      */     //   Java source line #572	-> byte code offset #67
/*      */     //   Java source line #574	-> byte code offset #71
/*      */     //   Java source line #575	-> byte code offset #84
/*      */     //   Java source line #577	-> byte code offset #87
/*      */     //   Java source line #614	-> byte code offset #90
/*      */     //   Java source line #615	-> byte code offset #95
/*      */     //   Java source line #577	-> byte code offset #101
/*      */     //   Java source line #579	-> byte code offset #104
/*      */     //   Java source line #580	-> byte code offset #106
/*      */     //   Java source line #581	-> byte code offset #109
/*      */     //   Java source line #582	-> byte code offset #118
/*      */     //   Java source line #586	-> byte code offset #121
/*      */     //   Java source line #590	-> byte code offset #124
/*      */     //   Java source line #591	-> byte code offset #131
/*      */     //   Java source line #594	-> byte code offset #137
/*      */     //   Java source line #595	-> byte code offset #144
/*      */     //   Java source line #596	-> byte code offset #151
/*      */     //   Java source line #598	-> byte code offset #158
/*      */     //   Java source line #600	-> byte code offset #167
/*      */     //   Java source line #602	-> byte code offset #178
/*      */     //   Java source line #609	-> byte code offset #182
/*      */     //   Java source line #610	-> byte code offset #187
/*      */     //   Java source line #614	-> byte code offset #192
/*      */     //   Java source line #615	-> byte code offset #197
/*      */     //   Java source line #602	-> byte code offset #203
/*      */     //   Java source line #603	-> byte code offset #206
/*      */     //   Java source line #604	-> byte code offset #208
/*      */     //   Java source line #605	-> byte code offset #211
/*      */     //   Java source line #606	-> byte code offset #214
/*      */     //   Java source line #609	-> byte code offset #217
/*      */     //   Java source line #610	-> byte code offset #224
/*      */     //   Java source line #614	-> byte code offset #232
/*      */     //   Java source line #615	-> byte code offset #239
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	248	0	this	Monitor
/*      */     //   0	248	1	guard	Guard
/*      */     //   0	248	2	time	long
/*      */     //   0	248	4	unit	TimeUnit
/*      */     //   6	155	5	timeoutNanos	long
/*      */     //   28	197	7	lock	ReentrantLock
/*      */     //   31	128	8	startTime	long
/*      */     //   38	175	10	signalBeforeWaiting	boolean
/*      */     //   43	192	11	interrupted	boolean
/*      */     //   69	48	12	remainingNanos	long
/*      */     //   122	98	12	satisfied	boolean
/*      */     //   153	3	13	remainingNanos	long
/*      */     //   165	39	13	remainingNanos	long
/*      */     //   206	3	13	interrupt	InterruptedException
/*      */     //   88	14	14	bool1	boolean
/*      */     //   104	3	14	interrupt	InterruptedException
/*      */     //   217	13	15	localObject1	Object
/*      */     //   232	14	16	localObject2	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   71	84	104	java/lang/InterruptedException
/*      */     //   87	90	104	java/lang/InterruptedException
/*      */     //   124	182	206	java/lang/InterruptedException
/*      */     //   124	182	217	finally
/*      */     //   206	219	217	finally
/*      */     //   45	90	232	finally
/*      */     //   104	192	232	finally
/*      */     //   206	234	232	finally
/*      */   }
/*      */   
/*      */   public boolean enterIf(Guard guard)
/*      */   {
/*  627 */     if (guard.monitor != this) {
/*  628 */       throw new IllegalMonitorStateException();
/*      */     }
/*  630 */     ReentrantLock lock = this.lock;
/*  631 */     lock.lock();
/*      */     
/*  633 */     boolean satisfied = false;
/*      */     try {
/*  635 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  637 */       if (!satisfied) {
/*  638 */         lock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean enterIfInterruptibly(Guard guard)
/*      */     throws InterruptedException
/*      */   {
/*  651 */     if (guard.monitor != this) {
/*  652 */       throw new IllegalMonitorStateException();
/*      */     }
/*  654 */     ReentrantLock lock = this.lock;
/*  655 */     lock.lockInterruptibly();
/*      */     
/*  657 */     boolean satisfied = false;
/*      */     try {
/*  659 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  661 */       if (!satisfied) {
/*  662 */         lock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit)
/*      */   {
/*  674 */     if (guard.monitor != this) {
/*  675 */       throw new IllegalMonitorStateException();
/*      */     }
/*  677 */     if (!enter(time, unit)) {
/*  678 */       return false;
/*      */     }
/*      */     
/*  681 */     boolean satisfied = false;
/*      */     try {
/*  683 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  685 */       if (!satisfied) {
/*  686 */         this.lock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit)
/*      */     throws InterruptedException
/*      */   {
/*  699 */     if (guard.monitor != this) {
/*  700 */       throw new IllegalMonitorStateException();
/*      */     }
/*  702 */     ReentrantLock lock = this.lock;
/*  703 */     if (!lock.tryLock(time, unit)) {
/*  704 */       return false;
/*      */     }
/*      */     
/*  707 */     boolean satisfied = false;
/*      */     try {
/*  709 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  711 */       if (!satisfied) {
/*  712 */         lock.unlock();
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
/*      */   public boolean tryEnterIf(Guard guard)
/*      */   {
/*  726 */     if (guard.monitor != this) {
/*  727 */       throw new IllegalMonitorStateException();
/*      */     }
/*  729 */     ReentrantLock lock = this.lock;
/*  730 */     if (!lock.tryLock()) {
/*  731 */       return false;
/*      */     }
/*      */     
/*  734 */     boolean satisfied = false;
/*      */     try {
/*  736 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  738 */       if (!satisfied) {
/*  739 */         lock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void waitFor(Guard guard)
/*      */     throws InterruptedException
/*      */   {
/*  751 */     if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
/*  752 */       throw new IllegalMonitorStateException();
/*      */     }
/*  754 */     if (!guard.isSatisfied()) {
/*  755 */       await(guard, true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void waitForUninterruptibly(Guard guard)
/*      */   {
/*  764 */     if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
/*  765 */       throw new IllegalMonitorStateException();
/*      */     }
/*  767 */     if (!guard.isSatisfied()) {
/*  768 */       awaitUninterruptibly(guard, true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit)
/*      */     throws InterruptedException
/*      */   {
/*  780 */     long timeoutNanos = toSafeNanos(time, unit);
/*  781 */     if (!(guard.monitor == this & this.lock.isHeldByCurrentThread())) {
/*  782 */       throw new IllegalMonitorStateException();
/*      */     }
/*  784 */     if (guard.isSatisfied()) {
/*  785 */       return true;
/*      */     }
/*  787 */     if (Thread.interrupted()) {
/*  788 */       throw new InterruptedException();
/*      */     }
/*  790 */     return awaitNanos(guard, timeoutNanos, true);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: lload_2
/*      */     //   1: aload 4
/*      */     //   3: invokestatic 14	com/google/common/util/concurrent/Monitor:toSafeNanos	(JLjava/util/concurrent/TimeUnit;)J
/*      */     //   6: lstore 5
/*      */     //   8: aload_1
/*      */     //   9: getfield 24	com/google/common/util/concurrent/Monitor$Guard:monitor	Lcom/google/common/util/concurrent/Monitor;
/*      */     //   12: aload_0
/*      */     //   13: if_acmpne +7 -> 20
/*      */     //   16: iconst_1
/*      */     //   17: goto +4 -> 21
/*      */     //   20: iconst_0
/*      */     //   21: aload_0
/*      */     //   22: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   25: invokevirtual 27	java/util/concurrent/locks/ReentrantLock:isHeldByCurrentThread	()Z
/*      */     //   28: iand
/*      */     //   29: ifne +11 -> 40
/*      */     //   32: new 25	java/lang/IllegalMonitorStateException
/*      */     //   35: dup
/*      */     //   36: invokespecial 26	java/lang/IllegalMonitorStateException:<init>	()V
/*      */     //   39: athrow
/*      */     //   40: aload_1
/*      */     //   41: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
/*      */     //   44: ifeq +5 -> 49
/*      */     //   47: iconst_1
/*      */     //   48: ireturn
/*      */     //   49: iconst_1
/*      */     //   50: istore 7
/*      */     //   52: lload 5
/*      */     //   54: invokestatic 33	com/google/common/util/concurrent/Monitor:initNanoTime	(J)J
/*      */     //   57: lstore 8
/*      */     //   59: invokestatic 16	java/lang/Thread:interrupted	()Z
/*      */     //   62: istore 10
/*      */     //   64: lload 5
/*      */     //   66: lstore 11
/*      */     //   68: aload_0
/*      */     //   69: aload_1
/*      */     //   70: lload 11
/*      */     //   72: iload 7
/*      */     //   74: invokespecial 34	com/google/common/util/concurrent/Monitor:awaitNanos	(Lcom/google/common/util/concurrent/Monitor$Guard;JZ)Z
/*      */     //   77: istore 13
/*      */     //   79: iload 10
/*      */     //   81: ifeq +9 -> 90
/*      */     //   84: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   87: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   90: iload 13
/*      */     //   92: ireturn
/*      */     //   93: astore 13
/*      */     //   95: iconst_1
/*      */     //   96: istore 10
/*      */     //   98: aload_1
/*      */     //   99: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
/*      */     //   102: ifeq +20 -> 122
/*      */     //   105: iconst_1
/*      */     //   106: istore 14
/*      */     //   108: iload 10
/*      */     //   110: ifeq +9 -> 119
/*      */     //   113: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   116: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   119: iload 14
/*      */     //   121: ireturn
/*      */     //   122: iconst_0
/*      */     //   123: istore 7
/*      */     //   125: lload 8
/*      */     //   127: lload 5
/*      */     //   129: invokestatic 23	com/google/common/util/concurrent/Monitor:remainingNanos	(JJ)J
/*      */     //   132: lstore 11
/*      */     //   134: goto -66 -> 68
/*      */     //   137: astore 15
/*      */     //   139: iload 10
/*      */     //   141: ifeq +9 -> 150
/*      */     //   144: invokestatic 20	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*      */     //   147: invokevirtual 21	java/lang/Thread:interrupt	()V
/*      */     //   150: aload 15
/*      */     //   152: athrow
/*      */     // Line number table:
/*      */     //   Java source line #800	-> byte code offset #0
/*      */     //   Java source line #801	-> byte code offset #8
/*      */     //   Java source line #802	-> byte code offset #32
/*      */     //   Java source line #804	-> byte code offset #40
/*      */     //   Java source line #805	-> byte code offset #47
/*      */     //   Java source line #807	-> byte code offset #49
/*      */     //   Java source line #808	-> byte code offset #52
/*      */     //   Java source line #809	-> byte code offset #59
/*      */     //   Java source line #811	-> byte code offset #64
/*      */     //   Java source line #813	-> byte code offset #68
/*      */     //   Java source line #824	-> byte code offset #79
/*      */     //   Java source line #825	-> byte code offset #84
/*      */     //   Java source line #813	-> byte code offset #90
/*      */     //   Java source line #814	-> byte code offset #93
/*      */     //   Java source line #815	-> byte code offset #95
/*      */     //   Java source line #816	-> byte code offset #98
/*      */     //   Java source line #817	-> byte code offset #105
/*      */     //   Java source line #824	-> byte code offset #108
/*      */     //   Java source line #825	-> byte code offset #113
/*      */     //   Java source line #817	-> byte code offset #119
/*      */     //   Java source line #819	-> byte code offset #122
/*      */     //   Java source line #820	-> byte code offset #125
/*      */     //   Java source line #821	-> byte code offset #134
/*      */     //   Java source line #824	-> byte code offset #137
/*      */     //   Java source line #825	-> byte code offset #144
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	153	0	this	Monitor
/*      */     //   0	153	1	guard	Guard
/*      */     //   0	153	2	time	long
/*      */     //   0	153	4	unit	TimeUnit
/*      */     //   6	122	5	timeoutNanos	long
/*      */     //   50	74	7	signalBeforeWaiting	boolean
/*      */     //   57	69	8	startTime	long
/*      */     //   62	78	10	interrupted	boolean
/*      */     //   66	67	11	remainingNanos	long
/*      */     //   77	14	13	bool1	boolean
/*      */     //   93	3	13	interrupt	InterruptedException
/*      */     //   106	14	14	bool2	boolean
/*      */     //   137	14	15	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   68	79	93	java/lang/InterruptedException
/*      */     //   64	79	137	finally
/*      */     //   93	108	137	finally
/*      */     //   122	139	137	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public void leave()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 1	com/google/common/util/concurrent/Monitor:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*      */     //   4: astore_1
/*      */     //   5: aload_1
/*      */     //   6: invokevirtual 38	java/util/concurrent/locks/ReentrantLock:getHoldCount	()I
/*      */     //   9: iconst_1
/*      */     //   10: if_icmpne +7 -> 17
/*      */     //   13: aload_0
/*      */     //   14: invokespecial 35	com/google/common/util/concurrent/Monitor:signalNextWaiter	()V
/*      */     //   17: aload_1
/*      */     //   18: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*      */     //   21: goto +10 -> 31
/*      */     //   24: astore_2
/*      */     //   25: aload_1
/*      */     //   26: invokevirtual 36	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     //   31: return
/*      */     // Line number table:
/*      */     //   Java source line #834	-> byte code offset #0
/*      */     //   Java source line #837	-> byte code offset #5
/*      */     //   Java source line #838	-> byte code offset #13
/*      */     //   Java source line #841	-> byte code offset #17
/*      */     //   Java source line #842	-> byte code offset #21
/*      */     //   Java source line #841	-> byte code offset #24
/*      */     //   Java source line #843	-> byte code offset #31
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	32	0	this	Monitor
/*      */     //   4	22	1	lock	ReentrantLock
/*      */     //   24	6	2	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   5	17	24	finally
/*      */   }
/*      */   
/*      */   public boolean isFair()
/*      */   {
/*  849 */     return this.fair;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOccupied()
/*      */   {
/*  857 */     return this.lock.isLocked();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOccupiedByCurrentThread()
/*      */   {
/*  865 */     return this.lock.isHeldByCurrentThread();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getOccupiedDepth()
/*      */   {
/*  873 */     return this.lock.getHoldCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getQueueLength()
/*      */   {
/*  883 */     return this.lock.getQueueLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasQueuedThreads()
/*      */   {
/*  893 */     return this.lock.hasQueuedThreads();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasQueuedThread(Thread thread)
/*      */   {
/*  903 */     return this.lock.hasQueuedThread(thread);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasWaiters(Guard guard)
/*      */   {
/*  913 */     return getWaitQueueLength(guard) > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getWaitQueueLength(Guard guard)
/*      */   {
/*  923 */     if (guard.monitor != this) {
/*  924 */       throw new IllegalMonitorStateException();
/*      */     }
/*  926 */     this.lock.lock();
/*      */     try {
/*  928 */       return guard.waiterCount;
/*      */     } finally {
/*  930 */       this.lock.unlock();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long toSafeNanos(long time, TimeUnit unit)
/*      */   {
/*  940 */     long timeoutNanos = unit.toNanos(time);
/*  941 */     return timeoutNanos > 6917529027641081853L ? 6917529027641081853L : timeoutNanos <= 0L ? 0L : timeoutNanos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static long initNanoTime(long timeoutNanos)
/*      */   {
/*  951 */     if (timeoutNanos <= 0L) {
/*  952 */       return 0L;
/*      */     }
/*  954 */     long startTime = System.nanoTime();
/*  955 */     return startTime == 0L ? 1L : startTime;
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
/*      */   private static long remainingNanos(long startTime, long timeoutNanos)
/*      */   {
/*  971 */     return timeoutNanos <= 0L ? 0L : timeoutNanos - (System.nanoTime() - startTime);
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
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter()
/*      */   {
/* 1000 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1001 */       if (isSatisfied(guard)) {
/* 1002 */         guard.condition.signal();
/* 1003 */         break;
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
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard)
/*      */   {
/*      */     try
/*      */     {
/* 1033 */       return guard.isSatisfied();
/*      */     } catch (Throwable throwable) {
/* 1035 */       signalAllWaiters();
/* 1036 */       throw Throwables.propagate(throwable);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters()
/*      */   {
/* 1045 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/* 1046 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard)
/*      */   {
/* 1055 */     int waiters = guard.waiterCount++;
/* 1056 */     if (waiters == 0)
/*      */     {
/* 1058 */       guard.next = this.activeGuards;
/* 1059 */       this.activeGuards = guard;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard)
/*      */   {
/* 1068 */     int waiters = --guard.waiterCount;
/* 1069 */     if (waiters == 0)
/*      */     {
/* 1071 */       Guard p = this.activeGuards; for (Guard pred = null;; p = p.next) {
/* 1072 */         if (p == guard) {
/* 1073 */           if (pred == null) {
/* 1074 */             this.activeGuards = p.next;
/*      */           } else {
/* 1076 */             pred.next = p.next;
/*      */           }
/* 1078 */           p.next = null;
/* 1079 */           break;
/*      */         }
/* 1071 */         pred = p;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting)
/*      */     throws InterruptedException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iload_2
/*      */     //   1: ifeq +7 -> 8
/*      */     //   4: aload_0
/*      */     //   5: invokespecial 35	com/google/common/util/concurrent/Monitor:signalNextWaiter	()V
/*      */     //   8: aload_0
/*      */     //   9: aload_1
/*      */     //   10: invokespecial 57	com/google/common/util/concurrent/Monitor:beginWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   13: aload_1
/*      */     //   14: getfield 50	com/google/common/util/concurrent/Monitor$Guard:condition	Ljava/util/concurrent/locks/Condition;
/*      */     //   17: invokeinterface 58 1 0
/*      */     //   22: aload_1
/*      */     //   23: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
/*      */     //   26: ifeq -13 -> 13
/*      */     //   29: aload_0
/*      */     //   30: aload_1
/*      */     //   31: invokespecial 59	com/google/common/util/concurrent/Monitor:endWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   34: goto +11 -> 45
/*      */     //   37: astore_3
/*      */     //   38: aload_0
/*      */     //   39: aload_1
/*      */     //   40: invokespecial 59	com/google/common/util/concurrent/Monitor:endWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   43: aload_3
/*      */     //   44: athrow
/*      */     //   45: return
/*      */     // Line number table:
/*      */     //   Java source line #1093	-> byte code offset #0
/*      */     //   Java source line #1094	-> byte code offset #4
/*      */     //   Java source line #1096	-> byte code offset #8
/*      */     //   Java source line #1099	-> byte code offset #13
/*      */     //   Java source line #1100	-> byte code offset #22
/*      */     //   Java source line #1102	-> byte code offset #29
/*      */     //   Java source line #1103	-> byte code offset #34
/*      */     //   Java source line #1102	-> byte code offset #37
/*      */     //   Java source line #1104	-> byte code offset #45
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	46	0	this	Monitor
/*      */     //   0	46	1	guard	Guard
/*      */     //   0	46	2	signalBeforeWaiting	boolean
/*      */     //   37	7	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   13	29	37	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iload_2
/*      */     //   1: ifeq +7 -> 8
/*      */     //   4: aload_0
/*      */     //   5: invokespecial 35	com/google/common/util/concurrent/Monitor:signalNextWaiter	()V
/*      */     //   8: aload_0
/*      */     //   9: aload_1
/*      */     //   10: invokespecial 57	com/google/common/util/concurrent/Monitor:beginWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   13: aload_1
/*      */     //   14: getfield 50	com/google/common/util/concurrent/Monitor$Guard:condition	Ljava/util/concurrent/locks/Condition;
/*      */     //   17: invokeinterface 60 1 0
/*      */     //   22: aload_1
/*      */     //   23: invokevirtual 28	com/google/common/util/concurrent/Monitor$Guard:isSatisfied	()Z
/*      */     //   26: ifeq -13 -> 13
/*      */     //   29: aload_0
/*      */     //   30: aload_1
/*      */     //   31: invokespecial 59	com/google/common/util/concurrent/Monitor:endWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   34: goto +11 -> 45
/*      */     //   37: astore_3
/*      */     //   38: aload_0
/*      */     //   39: aload_1
/*      */     //   40: invokespecial 59	com/google/common/util/concurrent/Monitor:endWaitingFor	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*      */     //   43: aload_3
/*      */     //   44: athrow
/*      */     //   45: return
/*      */     // Line number table:
/*      */     //   Java source line #1108	-> byte code offset #0
/*      */     //   Java source line #1109	-> byte code offset #4
/*      */     //   Java source line #1111	-> byte code offset #8
/*      */     //   Java source line #1114	-> byte code offset #13
/*      */     //   Java source line #1115	-> byte code offset #22
/*      */     //   Java source line #1117	-> byte code offset #29
/*      */     //   Java source line #1118	-> byte code offset #34
/*      */     //   Java source line #1117	-> byte code offset #37
/*      */     //   Java source line #1119	-> byte code offset #45
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	46	0	this	Monitor
/*      */     //   0	46	1	guard	Guard
/*      */     //   0	46	2	signalBeforeWaiting	boolean
/*      */     //   37	7	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   13	29	37	finally
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting)
/*      */     throws InterruptedException
/*      */   {
/* 1127 */     boolean firstTime = true;
/*      */     try {
/*      */       boolean bool1;
/* 1130 */       do { if (nanos <= 0L) {
/* 1131 */           return false;
/*      */         }
/* 1133 */         if (firstTime) {
/* 1134 */           if (signalBeforeWaiting) {
/* 1135 */             signalNextWaiter();
/*      */           }
/* 1137 */           beginWaitingFor(guard);
/* 1138 */           firstTime = false;
/*      */         }
/* 1140 */         nanos = guard.condition.awaitNanos(nanos);
/* 1141 */       } while (!guard.isSatisfied());
/* 1142 */       return true;
/*      */     } finally {
/* 1144 */       if (!firstTime) {
/* 1145 */         endWaitingFor(guard);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */