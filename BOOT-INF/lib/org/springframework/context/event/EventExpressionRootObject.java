/*    */ package org.springframework.context.event;
/*    */ 
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
/*    */ class EventExpressionRootObject
/*    */ {
/*    */   private final ApplicationEvent event;
/*    */   private final Object[] args;
/*    */   
/*    */   public EventExpressionRootObject(ApplicationEvent event, Object[] args)
/*    */   {
/* 34 */     this.event = event;
/* 35 */     this.args = args;
/*    */   }
/*    */   
/*    */   public ApplicationEvent getEvent() {
/* 39 */     return this.event;
/*    */   }
/*    */   
/*    */   public Object[] getArgs() {
/* 43 */     return this.args;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\EventExpressionRootObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */