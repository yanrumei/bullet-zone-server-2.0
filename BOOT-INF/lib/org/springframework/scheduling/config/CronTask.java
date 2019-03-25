/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.scheduling.support.CronTrigger;
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
/*    */ public class CronTask
/*    */   extends TriggerTask
/*    */ {
/*    */   private final String expression;
/*    */   
/*    */   public CronTask(Runnable runnable, String expression)
/*    */   {
/* 43 */     this(runnable, new CronTrigger(expression));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CronTask(Runnable runnable, CronTrigger cronTrigger)
/*    */   {
/* 52 */     super(runnable, cronTrigger);
/* 53 */     this.expression = cronTrigger.getExpression();
/*    */   }
/*    */   
/*    */   public String getExpression()
/*    */   {
/* 58 */     return this.expression;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\config\CronTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */