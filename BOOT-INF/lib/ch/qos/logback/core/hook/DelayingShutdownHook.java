/*    */ package ch.qos.logback.core.hook;
/*    */ 
/*    */ import ch.qos.logback.core.util.Duration;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelayingShutdownHook
/*    */   extends ShutdownHookBase
/*    */ {
/* 28 */   public static final Duration DEFAULT_DELAY = Duration.buildByMilliseconds(0.0D);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 33 */   private Duration delay = DEFAULT_DELAY;
/*    */   
/*    */ 
/*    */ 
/*    */   public Duration getDelay()
/*    */   {
/* 39 */     return this.delay;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDelay(Duration delay)
/*    */   {
/* 48 */     this.delay = delay;
/*    */   }
/*    */   
/*    */   public void run() {
/* 52 */     addInfo("Sleeping for " + this.delay);
/*    */     try {
/* 54 */       Thread.sleep(this.delay.getMilliseconds());
/*    */     }
/*    */     catch (InterruptedException e) {}
/* 57 */     super.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\hook\DelayingShutdownHook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */