/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
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
/*    */ 
/*    */ 
/*    */ public abstract class ApplicationContextEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   public ApplicationContextEvent(ApplicationContext source)
/*    */   {
/* 37 */     super(source);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final ApplicationContext getApplicationContext()
/*    */   {
/* 44 */     return (ApplicationContext)getSource();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\ApplicationContextEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */