/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf={"N", "E"})
/*     */ @Beta
/*     */ public final class ImmutableNetwork<N, E>
/*     */   extends ConfigurableNetwork<N, E>
/*     */ {
/*     */   private ImmutableNetwork(Network<N, E> network)
/*     */   {
/*  50 */     super(
/*  51 */       NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
/*     */   }
/*     */   
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network)
/*     */   {
/*  56 */     return (network instanceof ImmutableNetwork) ? (ImmutableNetwork)network : new ImmutableNetwork(network);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network)
/*     */   {
/*  68 */     return (ImmutableNetwork)Preconditions.checkNotNull(network);
/*     */   }
/*     */   
/*     */   public ImmutableGraph<N> asGraph()
/*     */   {
/*  73 */     return new ImmutableGraph(super.asGraph());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network)
/*     */   {
/*  80 */     ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
/*  81 */     for (N node : network.nodes()) {
/*  82 */       nodeConnections.put(node, connectionsOf(network, node));
/*     */     }
/*  84 */     return nodeConnections.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network)
/*     */   {
/*  91 */     ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
/*  92 */     for (E edge : network.edges()) {
/*  93 */       edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
/*     */     }
/*  95 */     return edgeToReferenceNode.build();
/*     */   }
/*     */   
/*     */   private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
/*  99 */     if (network.isDirected()) {
/* 100 */       Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
/* 101 */       Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
/* 102 */       int selfLoopCount = network.edgesConnecting(node, node).size();
/* 103 */       return network.allowsParallelEdges() ? 
/* 104 */         DirectedMultiNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : 
/* 105 */         DirectedNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
/*     */     }
/*     */     
/* 108 */     Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
/* 109 */     return network.allowsParallelEdges() ? 
/* 110 */       UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap) : 
/* 111 */       UndirectedNetworkConnections.ofImmutable(incidentEdgeMap);
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> sourceNodeFn(Network<N, E> network)
/*     */   {
/* 116 */     new Function()
/*     */     {
/*     */       public N apply(E edge) {
/* 119 */         return (N)this.val$network.incidentNodes(edge).source();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> targetNodeFn(Network<N, E> network) {
/* 125 */     new Function()
/*     */     {
/*     */       public N apply(E edge) {
/* 128 */         return (N)this.val$network.incidentNodes(edge).target();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> adjacentNodeFn(Network<N, E> network, final N node) {
/* 134 */     new Function()
/*     */     {
/*     */       public N apply(E edge) {
/* 137 */         return (N)this.val$network.incidentNodes(edge).adjacentNode(node);
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ImmutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */