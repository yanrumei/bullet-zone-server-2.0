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
/*    */ public final class MappingEndEvent
/*    */   extends CollectionEndEvent
/*    */ {
/*    */   public MappingEndEvent(Mark startMark, Mark endMark)
/*    */   {
/* 28 */     super(startMark, endMark);
/*    */   }
/*    */   
/*    */   public boolean is(Event.ID id)
/*    */   {
/* 33 */     return Event.ID.MappingEnd == id;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\events\MappingEndEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */