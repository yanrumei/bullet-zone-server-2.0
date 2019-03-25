/*     */ package org.springframework.util.backoff;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExponentialBackOff
/*     */   implements BackOff
/*     */ {
/*     */   public static final long DEFAULT_INITIAL_INTERVAL = 2000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final double DEFAULT_MULTIPLIER = 1.5D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_MAX_INTERVAL = 30000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_MAX_ELAPSED_TIME = Long.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private long initialInterval = 2000L;
/*     */   
/*  78 */   private double multiplier = 1.5D;
/*     */   
/*  80 */   private long maxInterval = 30000L;
/*     */   
/*  82 */   private long maxElapsedTime = Long.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExponentialBackOff() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExponentialBackOff(long initialInterval, double multiplier)
/*     */   {
/* 101 */     checkMultiplier(multiplier);
/* 102 */     this.initialInterval = initialInterval;
/* 103 */     this.multiplier = multiplier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInitialInterval(long initialInterval)
/*     */   {
/* 111 */     this.initialInterval = initialInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getInitialInterval()
/*     */   {
/* 118 */     return this.initialInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMultiplier(double multiplier)
/*     */   {
/* 125 */     checkMultiplier(multiplier);
/* 126 */     this.multiplier = multiplier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getMultiplier()
/*     */   {
/* 133 */     return this.multiplier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaxInterval(long maxInterval)
/*     */   {
/* 140 */     this.maxInterval = maxInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxInterval()
/*     */   {
/* 147 */     return this.maxInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxElapsedTime(long maxElapsedTime)
/*     */   {
/* 155 */     this.maxElapsedTime = maxElapsedTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getMaxElapsedTime()
/*     */   {
/* 163 */     return this.maxElapsedTime;
/*     */   }
/*     */   
/*     */   public BackOffExecution start()
/*     */   {
/* 168 */     return new ExponentialBackOffExecution(null);
/*     */   }
/*     */   
/*     */   private void checkMultiplier(double multiplier) {
/* 172 */     if (multiplier < 1.0D) {
/* 173 */       throw new IllegalArgumentException("Invalid multiplier '" + multiplier + "'. Should be equalor higher than 1. A multiplier of 1 is equivalent to a fixed interval");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class ExponentialBackOffExecution
/*     */     implements BackOffExecution
/*     */   {
/* 181 */     private long currentInterval = -1L;
/*     */     
/* 183 */     private long currentElapsedTime = 0L;
/*     */     
/*     */     private ExponentialBackOffExecution() {}
/*     */     
/* 187 */     public long nextBackOff() { if (this.currentElapsedTime >= ExponentialBackOff.this.maxElapsedTime) {
/* 188 */         return -1L;
/*     */       }
/*     */       
/* 191 */       long nextInterval = computeNextInterval();
/* 192 */       this.currentElapsedTime += nextInterval;
/* 193 */       return nextInterval;
/*     */     }
/*     */     
/*     */     private long computeNextInterval() {
/* 197 */       long maxInterval = ExponentialBackOff.this.getMaxInterval();
/* 198 */       if (this.currentInterval >= maxInterval) {
/* 199 */         return maxInterval;
/*     */       }
/* 201 */       if (this.currentInterval < 0L) {
/* 202 */         long initialInterval = ExponentialBackOff.this.getInitialInterval();
/* 203 */         this.currentInterval = (initialInterval < maxInterval ? initialInterval : maxInterval);
/*     */       }
/*     */       else
/*     */       {
/* 207 */         this.currentInterval = multiplyInterval(maxInterval);
/*     */       }
/* 209 */       return this.currentInterval;
/*     */     }
/*     */     
/*     */     private long multiplyInterval(long maxInterval) {
/* 213 */       long i = this.currentInterval;
/* 214 */       i = (i * ExponentialBackOff.this.getMultiplier());
/* 215 */       return i > maxInterval ? maxInterval : i;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 221 */       StringBuilder sb = new StringBuilder("ExponentialBackOff{");
/* 222 */       sb.append("currentInterval=").append(this.currentInterval + "ms");
/* 223 */       sb.append(", multiplier=").append(ExponentialBackOff.this.getMultiplier());
/* 224 */       sb.append('}');
/* 225 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\backoff\ExponentialBackOff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */