/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond)
/*     */   {
/* 122 */     return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond)
/*     */   {
/* 131 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 132 */     rateLimiter.setRate(permitsPerSecond);
/* 133 */     return rateLimiter;
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
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)
/*     */   {
/* 161 */     Preconditions.checkArgument(warmupPeriod >= 0L, "warmupPeriod must not be negative: %s", warmupPeriod);
/* 162 */     return create(
/* 163 */       SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit, 3.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor)
/*     */   {
/* 173 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
/* 174 */     rateLimiter.setRate(permitsPerSecond);
/* 175 */     return rateLimiter;
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
/*     */   private Object mutex()
/*     */   {
/* 188 */     Object mutex = this.mutexDoNotUseDirectly;
/* 189 */     if (mutex == null) {
/* 190 */       synchronized (this) {
/* 191 */         mutex = this.mutexDoNotUseDirectly;
/* 192 */         if (mutex == null) {
/* 193 */           this.mutexDoNotUseDirectly = (mutex = new Object());
/*     */         }
/*     */       }
/*     */     }
/* 197 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 201 */     this.stopwatch = ((SleepingStopwatch)Preconditions.checkNotNull(stopwatch));
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
/*     */   public final void setRate(double permitsPerSecond)
/*     */   {
/* 223 */     Preconditions.checkArgument((permitsPerSecond > 0.0D) && 
/* 224 */       (!Double.isNaN(permitsPerSecond)), "rate must be positive");
/* 225 */     synchronized (mutex()) {
/* 226 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */   
/*     */   /* Error */
/*     */   public final double getRate()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 22	com/google/common/util/concurrent/RateLimiter:mutex	()Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 25	com/google/common/util/concurrent/RateLimiter:doGetRate	()D
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: dreturn
/*     */     //   14: astore_2
/*     */     //   15: aload_1
/*     */     //   16: monitorexit
/*     */     //   17: aload_2
/*     */     //   18: athrow
/*     */     // Line number table:
/*     */     //   Java source line #239	-> byte code offset #0
/*     */     //   Java source line #240	-> byte code offset #7
/*     */     //   Java source line #241	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	19	0	this	RateLimiter
/*     */     //   5	11	1	Ljava/lang/Object;	Object
/*     */     //   14	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	17	14	finally
/*     */   }
/*     */   
/*     */   abstract double doGetRate();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire()
/*     */   {
/* 257 */     return acquire(1);
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
/*     */   public double acquire(int permits)
/*     */   {
/* 271 */     long microsToWait = reserve(permits);
/* 272 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 273 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   final long reserve(int permits)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: invokestatic 31	com/google/common/util/concurrent/RateLimiter:checkPermits	(I)V
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 22	com/google/common/util/concurrent/RateLimiter:mutex	()Ljava/lang/Object;
/*     */     //   8: dup
/*     */     //   9: astore_2
/*     */     //   10: monitorenter
/*     */     //   11: aload_0
/*     */     //   12: iload_1
/*     */     //   13: aload_0
/*     */     //   14: getfield 18	com/google/common/util/concurrent/RateLimiter:stopwatch	Lcom/google/common/util/concurrent/RateLimiter$SleepingStopwatch;
/*     */     //   17: invokevirtual 23	com/google/common/util/concurrent/RateLimiter$SleepingStopwatch:readMicros	()J
/*     */     //   20: invokevirtual 32	com/google/common/util/concurrent/RateLimiter:reserveAndGetWaitLength	(IJ)J
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: lreturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #283	-> byte code offset #0
/*     */     //   Java source line #284	-> byte code offset #4
/*     */     //   Java source line #285	-> byte code offset #11
/*     */     //   Java source line #286	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	RateLimiter
/*     */     //   0	31	1	permits	int
/*     */     //   9	19	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   11	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit)
/*     */   {
/* 302 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits)
/*     */   {
/* 316 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire()
/*     */   {
/* 329 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit)
/*     */   {
/* 344 */     long timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 345 */     checkPermits(permits);
/*     */     long microsToWait;
/* 347 */     synchronized (mutex()) {
/* 348 */       long nowMicros = this.stopwatch.readMicros();
/* 349 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 350 */         return false;
/*     */       }
/* 352 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     }
/*     */     long microsToWait;
/* 355 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 356 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 360 */     return queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros)
/*     */   {
/* 369 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 370 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 392 */     return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     protected abstract long readMicros();
/*     */     
/*     */ 
/*     */ 
/*     */     protected abstract void sleepMicrosUninterruptibly(long paramLong);
/*     */     
/*     */ 
/*     */ 
/*     */     public static final SleepingStopwatch createFromSystemTimer()
/*     */     {
/* 409 */       new SleepingStopwatch() {
/* 410 */         final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */         
/*     */         protected long readMicros()
/*     */         {
/* 414 */           return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */         }
/*     */         
/*     */         protected void sleepMicrosUninterruptibly(long micros)
/*     */         {
/* 419 */           if (micros > 0L) {
/* 420 */             Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkPermits(int permits) {
/* 428 */     Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */