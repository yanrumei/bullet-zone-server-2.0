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
/*    */ public abstract class NodeEvent
/*    */   extends Event
/*    */ {
/*    */   private final String anchor;
/*    */   
/*    */   public NodeEvent(String anchor, Mark startMark, Mark endMark)
/*    */   {
/* 28 */     super(startMark, endMark);
/* 29 */     this.anchor = anchor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAnchor()
/*    */   {
/* 42 */     return this.anchor;
/*    */   }
/*    */   
/*    */   protected String getArguments()
/*    */   {
/* 47 */     return "anchor=" + this.anchor;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\events\NodeEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */