/*    */ package org.yaml.snakeyaml.events;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class AliasEvent
/*    */   extends NodeEvent
/*    */ {
/*    */   public AliasEvent(String anchor, Mark startMark, Mark endMark)
/*    */   {
/* 25 */     super(anchor, startMark, endMark);
/*    */   }
/*    */   
/*    */   public boolean is(Event.ID id)
/*    */   {
/* 30 */     return Event.ID.Alias == id;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\events\AliasEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */