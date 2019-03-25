/*    */ package org.springframework.boot.context.event;
/*    */ 
/*    */ import org.springframework.boot.SpringApplication;
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
/*    */ 
/*    */ @Deprecated
/*    */ public class ApplicationStartedEvent
/*    */   extends ApplicationStartingEvent
/*    */ {
/*    */   public ApplicationStartedEvent(SpringApplication application, String[] args)
/*    */   {
/* 44 */     super(application, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\ApplicationStartedEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */