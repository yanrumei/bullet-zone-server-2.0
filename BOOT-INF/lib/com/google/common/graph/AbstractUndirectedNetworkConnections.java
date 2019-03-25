/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ abstract class AbstractUndirectedNetworkConnections<N, E>
/*    */   implements NetworkConnections<N, E>
/*    */ {
/*    */   protected final Map<E, N> incidentEdgeMap;
/*    */   
/*    */   protected AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap)
/*    */   {
/* 38 */     this.incidentEdgeMap = ((Map)Preconditions.checkNotNull(incidentEdgeMap));
/*    */   }
/*    */   
/*    */   public Set<N> predecessors()
/*    */   {
/* 43 */     return adjacentNodes();
/*    */   }
/*    */   
/*    */   public Set<N> successors()
/*    */   {
/* 48 */     return adjacentNodes();
/*    */   }
/*    */   
/*    */   public Set<E> incidentEdges()
/*    */   {
/* 53 */     return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
/*    */   }
/*    */   
/*    */   public Set<E> inEdges()
/*    */   {
/* 58 */     return incidentEdges();
/*    */   }
/*    */   
/*    */   public Set<E> outEdges()
/*    */   {
/* 63 */     return incidentEdges();
/*    */   }
/*    */   
/*    */   public N oppositeNode(Object edge)
/*    */   {
/* 68 */     return (N)Preconditions.checkNotNull(this.incidentEdgeMap.get(edge));
/*    */   }
/*    */   
/*    */   public N removeInEdge(Object edge, boolean isSelfLoop)
/*    */   {
/* 73 */     if (!isSelfLoop) {
/* 74 */       return (N)removeOutEdge(edge);
/*    */     }
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public N removeOutEdge(Object edge)
/*    */   {
/* 81 */     N previousNode = this.incidentEdgeMap.remove(edge);
/* 82 */     return (N)Preconditions.checkNotNull(previousNode);
/*    */   }
/*    */   
/*    */   public void addInEdge(E edge, N node, boolean isSelfLoop)
/*    */   {
/* 87 */     if (!isSelfLoop) {
/* 88 */       addOutEdge(edge, node);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addOutEdge(E edge, N node)
/*    */   {
/* 94 */     N previousNode = this.incidentEdgeMap.put(edge, node);
/* 95 */     Preconditions.checkState(previousNode == null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\AbstractUndirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */