/*    */ package org.springframework.boot.context.event;
/*    */ 
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
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
/*    */ public class ApplicationFailedEvent
/*    */   extends SpringApplicationEvent
/*    */ {
/*    */   private final ConfigurableApplicationContext context;
/*    */   private final Throwable exception;
/*    */   
/*    */   public ApplicationFailedEvent(SpringApplication application, String[] args, ConfigurableApplicationContext context, Throwable exception)
/*    */   {
/* 44 */     super(application, args);
/* 45 */     this.context = context;
/* 46 */     this.exception = exception;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConfigurableApplicationContext getApplicationContext()
/*    */   {
/* 54 */     return this.context;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Throwable getException()
/*    */   {
/* 62 */     return this.exception;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\ApplicationFailedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */