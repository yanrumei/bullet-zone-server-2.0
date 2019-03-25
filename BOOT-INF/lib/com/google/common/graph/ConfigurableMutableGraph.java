/*    */ package com.google.common.graph;
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
/*    */ final class ConfigurableMutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */   implements MutableGraph<N>
/*    */ {
/*    */   private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
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
/*    */   ConfigurableMutableGraph(AbstractGraphBuilder<? super N> builder)
/*    */   {
/* 36 */     this.backingValueGraph = new ConfigurableMutableValueGraph(builder);
/*    */   }
/*    */   
/*    */   protected BaseGraph<N> delegate()
/*    */   {
/* 41 */     return this.backingValueGraph;
/*    */   }
/*    */   
/*    */   public boolean addNode(N node)
/*    */   {
/* 46 */     return this.backingValueGraph.addNode(node);
/*    */   }
/*    */   
/*    */   public boolean putEdge(N nodeU, N nodeV)
/*    */   {
/* 51 */     return this.backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null;
/*    */   }
/*    */   
/*    */   public boolean removeNode(N node)
/*    */   {
/* 56 */     return this.backingValueGraph.removeNode(node);
/*    */   }
/*    */   
/*    */   public boolean removeEdge(N nodeU, N nodeV)
/*    */   {
/* 61 */     return this.backingValueGraph.removeEdge(nodeU, nodeV) != null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ConfigurableMutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */