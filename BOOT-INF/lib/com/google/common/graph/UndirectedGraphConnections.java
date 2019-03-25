/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
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
/*    */ final class UndirectedGraphConnections<N, V>
/*    */   implements GraphConnections<N, V>
/*    */ {
/*    */   private final Map<N, V> adjacentNodeValues;
/*    */   
/*    */   private UndirectedGraphConnections(Map<N, V> adjacentNodeValues)
/*    */   {
/* 40 */     this.adjacentNodeValues = ((Map)Preconditions.checkNotNull(adjacentNodeValues));
/*    */   }
/*    */   
/*    */   static <N, V> UndirectedGraphConnections<N, V> of() {
/* 44 */     return new UndirectedGraphConnections(new HashMap(2, 1.0F));
/*    */   }
/*    */   
/*    */   static <N, V> UndirectedGraphConnections<N, V> ofImmutable(Map<N, V> adjacentNodeValues)
/*    */   {
/* 49 */     return new UndirectedGraphConnections(ImmutableMap.copyOf(adjacentNodeValues));
/*    */   }
/*    */   
/*    */   public Set<N> adjacentNodes()
/*    */   {
/* 54 */     return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*    */   }
/*    */   
/*    */   public Set<N> predecessors()
/*    */   {
/* 59 */     return adjacentNodes();
/*    */   }
/*    */   
/*    */   public Set<N> successors()
/*    */   {
/* 64 */     return adjacentNodes();
/*    */   }
/*    */   
/*    */   public V value(Object node)
/*    */   {
/* 69 */     return (V)this.adjacentNodeValues.get(node);
/*    */   }
/*    */   
/*    */ 
/*    */   public void removePredecessor(Object node)
/*    */   {
/* 75 */     V unused = removeSuccessor(node);
/*    */   }
/*    */   
/*    */   public V removeSuccessor(Object node)
/*    */   {
/* 80 */     return (V)this.adjacentNodeValues.remove(node);
/*    */   }
/*    */   
/*    */ 
/*    */   public void addPredecessor(N node, V value)
/*    */   {
/* 86 */     V unused = addSuccessor(node, value);
/*    */   }
/*    */   
/*    */   public V addSuccessor(N node, V value)
/*    */   {
/* 91 */     return (V)this.adjacentNodeValues.put(node, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\UndirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */