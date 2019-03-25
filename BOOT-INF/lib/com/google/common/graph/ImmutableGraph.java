/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Functions;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableMap.Builder;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.errorprone.annotations.Immutable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable(containerOf={"N"})
/*    */ @Beta
/*    */ public class ImmutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */ {
/*    */   private final BaseGraph<N> backingGraph;
/*    */   
/*    */   ImmutableGraph(BaseGraph<N> backingGraph)
/*    */   {
/* 51 */     this.backingGraph = backingGraph;
/*    */   }
/*    */   
/*    */   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph)
/*    */   {
/* 56 */     return (graph instanceof ImmutableGraph) ? (ImmutableGraph)graph : new ImmutableGraph(new ConfigurableValueGraph(
/*    */     
/*    */ 
/* 59 */       GraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph)
/*    */   {
/* 69 */     return (ImmutableGraph)Preconditions.checkNotNull(graph);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph)
/*    */   {
/* 77 */     ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
/* 78 */     for (N node : graph.nodes()) {
/* 79 */       nodeConnections.put(node, connectionsOf(graph, node));
/*    */     }
/* 81 */     return nodeConnections.build();
/*    */   }
/*    */   
/*    */   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
/* 85 */     Function<Object, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
/* 86 */     return graph.isDirected() ? 
/* 87 */       DirectedGraphConnections.ofImmutable(graph
/* 88 */       .predecessors(node), Maps.asMap(graph.successors(node), edgeValueFn)) : 
/* 89 */       UndirectedGraphConnections.ofImmutable(
/* 90 */       Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
/*    */   }
/*    */   
/*    */   protected BaseGraph<N> delegate()
/*    */   {
/* 95 */     return this.backingGraph;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ImmutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */