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
/*    */ 
/*    */ 
/*    */ public class Task
/*    */ {
/*    */   private final Runnable runnable;
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
/*    */   public Task(Runnable runnable)
/*    */   {
/* 36 */     this.runnable = runnable;
/*    */   }
/*    */   
/*    */   public Runnable getRunnable()
/*    */   {
/* 41 */     return this.runnable;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\Task.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */