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
/*    */ 
/*    */ public class ApplicationStartingEvent
/*    */   extends SpringApplicationEvent
/*    */ {
/*    */   public ApplicationStartingEvent(SpringApplication application, String[] args)
/*    */   {
/* 44 */     super(application, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\event\ApplicationStartingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */