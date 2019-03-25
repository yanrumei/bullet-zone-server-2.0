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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SequenceEndEvent
/*    */   extends CollectionEndEvent
/*    */ {
/*    */   public SequenceEndEvent(Mark startMark, Mark endMark)
/*    */   {
/* 28 */     super(startMark, endMark);
/*    */   }
/*    */   
/*    */   public boolean is(Event.ID id)
/*    */   {
/* 33 */     return Event.ID.SequenceEnd == id;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\events\SequenceEndEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */