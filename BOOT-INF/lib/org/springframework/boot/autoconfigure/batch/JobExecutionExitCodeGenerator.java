/*    */ package org.springframework.boot.autoconfigure.batch;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.batch.core.BatchStatus;
/*    */ import org.springframework.batch.core.JobExecution;
/*    */ import org.springframework.boot.ExitCodeGenerator;
/*    */ import org.springframework.context.ApplicationListener;
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
/*    */ public class JobExecutionExitCodeGenerator
/*    */   implements ApplicationListener<JobExecutionEvent>, ExitCodeGenerator
/*    */ {
/* 34 */   private final List<JobExecution> executions = new ArrayList();
/*    */   
/*    */   public void onApplicationEvent(JobExecutionEvent event)
/*    */   {
/* 38 */     this.executions.add(event.getJobExecution());
/*    */   }
/*    */   
/*    */   public int getExitCode()
/*    */   {
/* 43 */     for (JobExecution execution : this.executions) {
/* 44 */       if (execution.getStatus().ordinal() > 0) {
/* 45 */         return execution.getStatus().ordinal();
/*    */       }
/*    */     }
/* 48 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\JobExecutionExitCodeGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */