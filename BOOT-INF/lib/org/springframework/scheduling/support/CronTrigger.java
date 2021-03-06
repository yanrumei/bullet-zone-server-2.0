/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
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
/*     */ public class CronTrigger
/*     */   implements Trigger
/*     */ {
/*     */   private final CronSequenceGenerator sequenceGenerator;
/*     */   
/*     */   public CronTrigger(String expression)
/*     */   {
/*  44 */     this.sequenceGenerator = new CronSequenceGenerator(expression);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CronTrigger(String expression, TimeZone timeZone)
/*     */   {
/*  54 */     this.sequenceGenerator = new CronSequenceGenerator(expression, timeZone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExpression()
/*     */   {
/*  62 */     return this.sequenceGenerator.getExpression();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date nextExecutionTime(TriggerContext triggerContext)
/*     */   {
/*  74 */     Date date = triggerContext.lastCompletionTime();
/*  75 */     if (date != null) {
/*  76 */       Date scheduled = triggerContext.lastScheduledExecutionTime();
/*  77 */       if ((scheduled != null) && (date.before(scheduled)))
/*     */       {
/*     */ 
/*     */ 
/*  81 */         date = scheduled;
/*     */       }
/*     */     }
/*     */     else {
/*  85 */       date = new Date();
/*     */     }
/*  87 */     return this.sequenceGenerator.next(date);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  93 */     return (this == other) || (((other instanceof CronTrigger)) && 
/*  94 */       (this.sequenceGenerator.equals(((CronTrigger)other).sequenceGenerator)));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  99 */     return this.sequenceGenerator.hashCode();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 104 */     return this.sequenceGenerator.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\support\CronTrigger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */