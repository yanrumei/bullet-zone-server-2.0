/*    */ package org.springframework.boot.autoconfigure.batch;
/*    */ 
/*    */ import org.springframework.batch.core.JobExecution;
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ public class JobExecutionEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   private final JobExecution execution;
/*    */   
/*    */   public JobExecutionEvent(JobExecution execution)
/*    */   {
/* 37 */     super(execution);
/* 38 */     this.execution = execution;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JobExecution getJobExecution()
/*    */   {
/* 46 */     return this.execution;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\JobExecutionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */