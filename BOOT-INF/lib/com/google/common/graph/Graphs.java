/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static <N> boolean hasCycle(Graph<N> graph)
/*     */   {
/*  58 */     int numEdges = graph.edges().size();
/*  59 */     if (numEdges == 0) {
/*  60 */       return false;
/*     */     }
/*  62 */     if ((!graph.isDirected()) && (numEdges >= graph.nodes().size())) {
/*  63 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  67 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  68 */     for (N node : graph.nodes()) {
/*  69 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  70 */         return true;
/*     */       }
/*     */     }
/*  73 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasCycle(Network<?, ?> network)
/*     */   {
/*  86 */     if ((!network.isDirected()) && 
/*  87 */       (network.allowsParallelEdges()) && 
/*  88 */       (network.edges().size() > network.asGraph().edges().size())) {
/*  89 */       return true;
/*     */     }
/*  91 */     return hasCycle(network.asGraph());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N node, @Nullable N previousNode)
/*     */   {
/* 101 */     NodeVisitState state = (NodeVisitState)visitedNodes.get(node);
/* 102 */     if (state == NodeVisitState.COMPLETE) {
/* 103 */       return false;
/*     */     }
/* 105 */     if (state == NodeVisitState.PENDING) {
/* 106 */       return true;
/*     */     }
/*     */     
/* 109 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 110 */     for (N nextNode : graph.successors(node)) {
/* 111 */       if ((canTraverseWithoutReusingEdge(graph, nextNode, previousNode)) && 
/* 112 */         (subgraphHasCycle(graph, visitedNodes, nextNode, node))) {
/* 113 */         return true;
/*     */       }
/*     */     }
/* 116 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 117 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @Nullable Object previousNode)
/*     */   {
/* 128 */     if ((graph.isDirected()) || (!Objects.equal(previousNode, nextNode))) {
/* 129 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 133 */     return false;
/*     */   }
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
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph)
/*     */   {
/* 147 */     MutableGraph<N> transitiveClosure = GraphBuilder.from(graph).allowsSelfLoops(true).build();
/*     */     Iterator localIterator1;
/*     */     N node;
/*     */     Object visitedNodes;
/* 151 */     if (graph.isDirected())
/*     */     {
/* 153 */       for (localIterator1 = graph.nodes().iterator(); localIterator1.hasNext();) { node = localIterator1.next();
/* 154 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 155 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 161 */       visitedNodes = new HashSet();
/* 162 */       for (Object node : graph.nodes())
/* 163 */         if (!((Set)visitedNodes).contains(node)) {
/* 164 */           reachableNodes = reachableNodes(graph, node);
/* 165 */           ((Set)visitedNodes).addAll(reachableNodes);
/* 166 */           pairwiseMatch = 1;
/* 167 */           for (localIterator3 = reachableNodes.iterator(); localIterator3.hasNext();) { nodeU = localIterator3.next();
/* 168 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++))
/* 169 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */           }
/*     */         } }
/*     */     Set<N> reachableNodes;
/*     */     int pairwiseMatch;
/*     */     Iterator localIterator3;
/*     */     N nodeU;
/* 176 */     return transitiveClosure;
/*     */   }
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
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, N node)
/*     */   {
/* 191 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 192 */     Set<N> visitedNodes = new LinkedHashSet();
/* 193 */     Queue<N> queuedNodes = new ArrayDeque();
/* 194 */     visitedNodes.add(node);
/* 195 */     queuedNodes.add(node);
/*     */     
/* 197 */     while (!queuedNodes.isEmpty()) {
/* 198 */       N currentNode = queuedNodes.remove();
/* 199 */       for (N successor : graph.successors(currentNode)) {
/* 200 */         if (visitedNodes.add(successor)) {
/* 201 */           queuedNodes.add(successor);
/*     */         }
/*     */       }
/*     */     }
/* 205 */     return Collections.unmodifiableSet(visitedNodes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean equivalent(@Nullable Graph<?> graphA, @Nullable Graph<?> graphB)
/*     */   {
/* 214 */     return Objects.equal(graphA, graphB);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean equivalent(@Nullable ValueGraph<?, ?> graphA, @Nullable ValueGraph<?, ?> graphB)
/*     */   {
/* 225 */     return Objects.equal(graphA, graphB);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean equivalent(@Nullable Network<?, ?> networkA, @Nullable Network<?, ?> networkB)
/*     */   {
/* 236 */     return Objects.equal(networkA, networkB);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> Graph<N> transpose(Graph<N> graph)
/*     */   {
/* 248 */     if (!graph.isDirected()) {
/* 249 */       return graph;
/*     */     }
/*     */     
/* 252 */     if ((graph instanceof TransposedGraph)) {
/* 253 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 256 */     return new TransposedGraph(graph);
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N> extends AbstractGraph<N> {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph) {
/* 263 */       this.graph = graph;
/*     */     }
/*     */     
/*     */     public Set<N> nodes()
/*     */     {
/* 268 */       return this.graph.nodes();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected long edgeCount()
/*     */     {
/* 277 */       return this.graph.edges().size();
/*     */     }
/*     */     
/*     */     public boolean isDirected()
/*     */     {
/* 282 */       return this.graph.isDirected();
/*     */     }
/*     */     
/*     */     public boolean allowsSelfLoops()
/*     */     {
/* 287 */       return this.graph.allowsSelfLoops();
/*     */     }
/*     */     
/*     */     public ElementOrder<N> nodeOrder()
/*     */     {
/* 292 */       return this.graph.nodeOrder();
/*     */     }
/*     */     
/*     */     public Set<N> adjacentNodes(N node)
/*     */     {
/* 297 */       return this.graph.adjacentNodes(node);
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 302 */       return this.graph.successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 307 */       return this.graph.predecessors(node);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph)
/*     */   {
/* 316 */     if (!graph.isDirected()) {
/* 317 */       return graph;
/*     */     }
/*     */     
/* 320 */     if ((graph instanceof TransposedValueGraph)) {
/* 321 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 324 */     return new TransposedValueGraph(graph);
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V> extends AbstractValueGraph<N, V> {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph) {
/* 331 */       this.graph = graph;
/*     */     }
/*     */     
/*     */     public Set<N> nodes()
/*     */     {
/* 336 */       return this.graph.nodes();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected long edgeCount()
/*     */     {
/* 345 */       return this.graph.edges().size();
/*     */     }
/*     */     
/*     */     public boolean isDirected()
/*     */     {
/* 350 */       return this.graph.isDirected();
/*     */     }
/*     */     
/*     */     public boolean allowsSelfLoops()
/*     */     {
/* 355 */       return this.graph.allowsSelfLoops();
/*     */     }
/*     */     
/*     */     public ElementOrder<N> nodeOrder()
/*     */     {
/* 360 */       return this.graph.nodeOrder();
/*     */     }
/*     */     
/*     */     public Set<N> adjacentNodes(N node)
/*     */     {
/* 365 */       return this.graph.adjacentNodes(node);
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 370 */       return this.graph.successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 375 */       return this.graph.predecessors(node);
/*     */     }
/*     */     
/*     */     public V edgeValue(N nodeU, N nodeV)
/*     */     {
/* 380 */       return (V)this.graph.edgeValue(nodeV, nodeU);
/*     */     }
/*     */     
/*     */     public V edgeValueOrDefault(N nodeU, N nodeV, @Nullable V defaultValue)
/*     */     {
/* 385 */       return (V)this.graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network)
/*     */   {
/* 394 */     if (!network.isDirected()) {
/* 395 */       return network;
/*     */     }
/*     */     
/* 398 */     if ((network instanceof TransposedNetwork)) {
/* 399 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 402 */     return new TransposedNetwork(network);
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends AbstractNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 409 */       this.network = network;
/*     */     }
/*     */     
/*     */     public Set<N> nodes()
/*     */     {
/* 414 */       return this.network.nodes();
/*     */     }
/*     */     
/*     */     public Set<E> edges()
/*     */     {
/* 419 */       return this.network.edges();
/*     */     }
/*     */     
/*     */     public boolean isDirected()
/*     */     {
/* 424 */       return this.network.isDirected();
/*     */     }
/*     */     
/*     */     public boolean allowsParallelEdges()
/*     */     {
/* 429 */       return this.network.allowsParallelEdges();
/*     */     }
/*     */     
/*     */     public boolean allowsSelfLoops()
/*     */     {
/* 434 */       return this.network.allowsSelfLoops();
/*     */     }
/*     */     
/*     */     public ElementOrder<N> nodeOrder()
/*     */     {
/* 439 */       return this.network.nodeOrder();
/*     */     }
/*     */     
/*     */     public ElementOrder<E> edgeOrder()
/*     */     {
/* 444 */       return this.network.edgeOrder();
/*     */     }
/*     */     
/*     */     public Set<N> adjacentNodes(N node)
/*     */     {
/* 449 */       return this.network.adjacentNodes(node);
/*     */     }
/*     */     
/*     */     public Set<N> predecessors(N node)
/*     */     {
/* 454 */       return this.network.successors(node);
/*     */     }
/*     */     
/*     */     public Set<N> successors(N node)
/*     */     {
/* 459 */       return this.network.predecessors(node);
/*     */     }
/*     */     
/*     */     public Set<E> incidentEdges(N node)
/*     */     {
/* 464 */       return this.network.incidentEdges(node);
/*     */     }
/*     */     
/*     */     public Set<E> inEdges(N node)
/*     */     {
/* 469 */       return this.network.outEdges(node);
/*     */     }
/*     */     
/*     */     public Set<E> outEdges(N node)
/*     */     {
/* 474 */       return this.network.inEdges(node);
/*     */     }
/*     */     
/*     */     public EndpointPair<N> incidentNodes(E edge)
/*     */     {
/* 479 */       EndpointPair<N> endpointPair = this.network.incidentNodes(edge);
/* 480 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */     
/*     */     public Set<E> adjacentEdges(E edge)
/*     */     {
/* 485 */       return this.network.adjacentEdges(edge);
/*     */     }
/*     */     
/*     */     public Set<E> edgesConnecting(N nodeU, N nodeV)
/*     */     {
/* 490 */       return this.network.edgesConnecting(nodeV, nodeU);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes)
/*     */   {
/* 504 */     MutableGraph<N> subgraph = GraphBuilder.from(graph).build();
/* 505 */     for (N node : nodes) {
/* 506 */       subgraph.addNode(node);
/*     */     }
/* 508 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 509 */       for (N successorNode : graph.successors(node)) {
/* 510 */         if (subgraph.nodes().contains(successorNode))
/* 511 */           subgraph.putEdge(node, successorNode);
/*     */       }
/*     */     }
/*     */     N node;
/* 515 */     return subgraph;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes)
/*     */   {
/* 528 */     MutableValueGraph<N, V> subgraph = ValueGraphBuilder.from(graph).build();
/* 529 */     for (N node : nodes) {
/* 530 */       subgraph.addNode(node);
/*     */     }
/* 532 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 533 */       for (N successorNode : graph.successors(node)) {
/* 534 */         if (subgraph.nodes().contains(successorNode))
/* 535 */           subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
/*     */       }
/*     */     }
/*     */     N node;
/* 539 */     return subgraph;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes)
/*     */   {
/* 552 */     MutableNetwork<N, E> subgraph = NetworkBuilder.from(network).build();
/* 553 */     for (N node : nodes) {
/* 554 */       subgraph.addNode(node);
/*     */     }
/* 556 */     for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
/* 557 */       for (E edge : network.outEdges(node)) {
/* 558 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 559 */         if (subgraph.nodes().contains(successorNode))
/* 560 */           subgraph.addEdge(node, successorNode, edge);
/*     */       }
/*     */     }
/*     */     N node;
/* 564 */     return subgraph;
/*     */   }
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph)
/*     */   {
/* 569 */     MutableGraph<N> copy = GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 570 */     for (N node : graph.nodes()) {
/* 571 */       copy.addNode(node);
/*     */     }
/* 573 */     for (EndpointPair<N> edge : graph.edges()) {
/* 574 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 576 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph)
/*     */   {
/* 582 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 583 */     for (N node : graph.nodes()) {
/* 584 */       copy.addNode(node);
/*     */     }
/* 586 */     for (EndpointPair<N> edge : graph.edges()) {
/* 587 */       copy.putEdgeValue(edge.nodeU(), edge.nodeV(), graph.edgeValue(edge.nodeU(), edge.nodeV()));
/*     */     }
/* 589 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network)
/*     */   {
/* 598 */     MutableNetwork<N, E> copy = NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 599 */     for (N node : network.nodes()) {
/* 600 */       copy.addNode(node);
/*     */     }
/* 602 */     for (E edge : network.edges()) {
/* 603 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 604 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     }
/* 606 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 611 */     Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
/* 612 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 617 */     Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
/* 618 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 623 */     Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
/* 624 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 629 */     Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
/* 630 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum NodeVisitState
/*     */   {
/* 639 */     PENDING, 
/* 640 */     COMPLETE;
/*     */     
/*     */     private NodeVisitState() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */