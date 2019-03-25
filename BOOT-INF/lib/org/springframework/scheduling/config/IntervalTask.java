/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntervalTask
/*    */   extends Task
/*    */ {
/*    */   private final long interval;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final long initialDelay;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IntervalTask(Runnable runnable, long interval, long initialDelay)
/*    */   {
/* 46 */     super(runnable);
/* 47 */     this.interval = interval;
/* 48 */     this.initialDelay = initialDelay;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IntervalTask(Runnable runnable, long interval)
/*    */   {
/* 57 */     this(runnable, interval, 0L);
/*    */   }
/*    */   
/*    */   public long getInterval()
/*    */   {
/* 62 */     return this.interval;
/*    */   }
/*    */   
/*    */   public long getInitialDelay() {
/* 66 */     return this.initialDelay;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\IntervalTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */