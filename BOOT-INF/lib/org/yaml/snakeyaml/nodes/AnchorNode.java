/*    */ package org.yaml.snakeyaml.nodes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnchorNode
/*    */   extends Node
/*    */ {
/*    */   private Node realNode;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AnchorNode(Node realNode)
/*    */   {
/* 23 */     super(realNode.getTag(), realNode.getStartMark(), realNode.getEndMark());
/* 24 */     this.realNode = realNode;
/*    */   }
/*    */   
/*    */   public NodeId getNodeId()
/*    */   {
/* 29 */     return NodeId.anchor;
/*    */   }
/*    */   
/*    */   public Node getRealNode() {
/* 33 */     return this.realNode;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\nodes\AnchorNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */