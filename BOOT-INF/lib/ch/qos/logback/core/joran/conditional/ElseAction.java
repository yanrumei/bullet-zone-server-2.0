/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import java.util.List;
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
/*    */ public class ElseAction
/*    */   extends ThenOrElseActionBase
/*    */ {
/*    */   void registerEventList(IfAction ifAction, List<SaxEvent> eventList)
/*    */   {
/* 24 */     ifAction.setElseSaxEventList(eventList);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\conditional\ElseAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */