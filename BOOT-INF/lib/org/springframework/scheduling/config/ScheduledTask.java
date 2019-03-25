/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import java.util.concurrent.ScheduledFuture;
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
/*    */ public final class ScheduledTask
/*    */ {
/*    */   volatile ScheduledFuture<?> future;
/*    */   
/*    */   public void cancel()
/*    */   {
/* 43 */     ScheduledFuture<?> future = this.future;
/* 44 */     if (future != null) {
/* 45 */       future.cancel(true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\ScheduledTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */