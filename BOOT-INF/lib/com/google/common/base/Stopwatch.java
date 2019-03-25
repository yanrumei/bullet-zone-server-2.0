/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.time.Duration;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Stopwatch
/*     */ {
/*     */   private final Ticker ticker;
/*     */   private boolean isRunning;
/*     */   private long elapsedNanos;
/*     */   private long startTick;
/*     */   
/*     */   public static Stopwatch createUnstarted()
/*     */   {
/*  95 */     return new Stopwatch();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Stopwatch createUnstarted(Ticker ticker)
/*     */   {
/* 104 */     return new Stopwatch(ticker);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Stopwatch createStarted()
/*     */   {
/* 113 */     return new Stopwatch().start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Stopwatch createStarted(Ticker ticker)
/*     */   {
/* 122 */     return new Stopwatch(ticker).start();
/*     */   }
/*     */   
/*     */   Stopwatch() {
/* 126 */     this.ticker = Ticker.systemTicker();
/*     */   }
/*     */   
/*     */   Stopwatch(Ticker ticker) {
/* 130 */     this.ticker = ((Ticker)Preconditions.checkNotNull(ticker, "ticker"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/* 138 */     return this.isRunning;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch start()
/*     */   {
/* 149 */     Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
/* 150 */     this.isRunning = true;
/* 151 */     this.startTick = this.ticker.read();
/* 152 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch stop()
/*     */   {
/* 164 */     long tick = this.ticker.read();
/* 165 */     Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
/* 166 */     this.isRunning = false;
/* 167 */     this.elapsedNanos += tick - this.startTick;
/* 168 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch reset()
/*     */   {
/* 178 */     this.elapsedNanos = 0L;
/* 179 */     this.isRunning = false;
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   private long elapsedNanos() {
/* 184 */     return this.isRunning ? this.ticker.read() - this.startTick + this.elapsedNanos : this.elapsedNanos;
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
/*     */   public long elapsed(TimeUnit desiredUnit)
/*     */   {
/* 201 */     return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GwtIncompatible
/*     */   public Duration elapsed()
/*     */   {
/* 213 */     return Duration.ofNanos(elapsedNanos());
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 219 */     long nanos = elapsedNanos();
/*     */     
/* 221 */     TimeUnit unit = chooseUnit(nanos);
/* 222 */     double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
/*     */     
/*     */ 
/* 225 */     return Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
/*     */   }
/*     */   
/*     */   private static TimeUnit chooseUnit(long nanos) {
/* 229 */     if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 230 */       return TimeUnit.DAYS;
/*     */     }
/* 232 */     if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 233 */       return TimeUnit.HOURS;
/*     */     }
/* 235 */     if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 236 */       return TimeUnit.MINUTES;
/*     */     }
/* 238 */     if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 239 */       return TimeUnit.SECONDS;
/*     */     }
/* 241 */     if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 242 */       return TimeUnit.MILLISECONDS;
/*     */     }
/* 244 */     if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 245 */       return TimeUnit.MICROSECONDS;
/*     */     }
/* 247 */     return TimeUnit.NANOSECONDS;
/*     */   }
/*     */   
/*     */   private static String abbreviate(TimeUnit unit) {
/* 251 */     switch (unit) {
/*     */     case NANOSECONDS: 
/* 253 */       return "ns";
/*     */     case MICROSECONDS: 
/* 255 */       return "μs";
/*     */     case MILLISECONDS: 
/* 257 */       return "ms";
/*     */     case SECONDS: 
/* 259 */       return "s";
/*     */     case MINUTES: 
/* 261 */       return "min";
/*     */     case HOURS: 
/* 263 */       return "h";
/*     */     case DAYS: 
/* 265 */       return "d";
/*     */     }
/* 267 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\Stopwatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */