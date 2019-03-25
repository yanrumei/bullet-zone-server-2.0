/*    */ package com.google.common.graph;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ abstract class ForwardingGraph<N>
/*    */   extends AbstractGraph<N>
/*    */ {
/*    */   protected abstract BaseGraph<N> delegate();
/*    */   
/*    */   public Set<N> nodes()
/*    */   {
/* 33 */     return delegate().nodes();
/*    */   }
/*    */   
/*    */   public Set<EndpointPair<N>> edges()
/*    */   {
/* 38 */     return delegate().edges();
/*    */   }
/*    */   
/*    */   public boolean isDirected()
/*    */   {
/* 43 */     return delegate().isDirected();
/*    */   }
/*    */   
/*    */   public boolean allowsSelfLoops()
/*    */   {
/* 48 */     return delegate().allowsSelfLoops();
/*    */   }
/*    */   
/*    */   public ElementOrder<N> nodeOrder()
/*    */   {
/* 53 */     return delegate().nodeOrder();
/*    */   }
/*    */   
/*    */   public Set<N> adjacentNodes(N node)
/*    */   {
/* 58 */     return delegate().adjacentNodes(node);
/*    */   }
/*    */   
/*    */   public Set<N> predecessors(N node)
/*    */   {
/* 63 */     return delegate().predecessors(node);
/*    */   }
/*    */   
/*    */   public Set<N> successors(N node)
/*    */   {
/* 68 */     return delegate().successors(node);
/*    */   }
/*    */   
/*    */   public int degree(N node)
/*    */   {
/* 73 */     return delegate().degree(node);
/*    */   }
/*    */   
/*    */   public int inDegree(N node)
/*    */   {
/* 78 */     return delegate().inDegree(node);
/*    */   }
/*    */   
/*    */   public int outDegree(N node)
/*    */   {
/* 83 */     return delegate().outDegree(node);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ForwardingGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */