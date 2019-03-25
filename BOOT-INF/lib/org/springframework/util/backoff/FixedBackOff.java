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
/*     */ public class FixedBackOff
/*     */   implements BackOff
/*     */ {
/*     */   public static final long DEFAULT_INTERVAL = 5000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final long UNLIMITED_ATTEMPTS = Long.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private long interval = 5000L;
/*     */   
/*  40 */   private long maxAttempts = Long.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FixedBackOff() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FixedBackOff(long interval, long maxAttempts)
/*     */   {
/*  56 */     this.interval = interval;
/*  57 */     this.maxAttempts = maxAttempts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInterval(long interval)
/*     */   {
/*  65 */     this.interval = interval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getInterval()
/*     */   {
/*  72 */     return this.interval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaxAttempts(long maxAttempts)
/*     */   {
/*  79 */     this.maxAttempts = maxAttempts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getMaxAttempts()
/*     */   {
/*  86 */     return this.maxAttempts;
/*     */   }
/*     */   
/*     */   public BackOffExecution start()
/*     */   {
/*  91 */     return new FixedBackOffExecution(null);
/*     */   }
/*     */   
/*     */   private class FixedBackOffExecution
/*     */     implements BackOffExecution
/*     */   {
/*  97 */     private long currentAttempts = 0L;
/*     */     
/*     */     private FixedBackOffExecution() {}
/*     */     
/* 101 */     public long nextBackOff() { this.currentAttempts += 1L;
/* 102 */       if (this.currentAttempts <= FixedBackOff.this.getMaxAttempts()) {
/* 103 */         return FixedBackOff.this.getInterval();
/*     */       }
/*     */       
/* 106 */       return -1L;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 112 */       StringBuilder sb = new StringBuilder("FixedBackOff{");
/* 113 */       sb.append("interval=").append(FixedBackOff.this.interval);
/*     */       
/* 115 */       String attemptValue = FixedBackOff.this.maxAttempts == Long.MAX_VALUE ? "unlimited" : String.valueOf(FixedBackOff.this.maxAttempts);
/* 116 */       sb.append(", currentAttempts=").append(this.currentAttempts);
/* 117 */       sb.append(", maxAttempts=").append(attemptValue);
/* 118 */       sb.append('}');
/* 119 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\backoff\FixedBackOff.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */