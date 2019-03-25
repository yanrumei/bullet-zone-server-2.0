/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ 
/*     */ 
/*     */ class ConfigurableValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   protected long edgeCount;
/*     */   
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder)
/*     */   {
/*  57 */     this(builder, builder.nodeOrder
/*     */     
/*  59 */       .createMap(
/*  60 */       ((Integer)builder.expectedNodeCount.or(Integer.valueOf(10))).intValue()), 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount)
/*     */   {
/*  72 */     this.isDirected = builder.directed;
/*  73 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  74 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  76 */     this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections));
/*     */     
/*     */ 
/*     */ 
/*  80 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */   
/*     */   public Set<N> nodes()
/*     */   {
/*  85 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */   
/*     */   public boolean isDirected()
/*     */   {
/*  90 */     return this.isDirected;
/*     */   }
/*     */   
/*     */   public boolean allowsSelfLoops()
/*     */   {
/*  95 */     return this.allowsSelfLoops;
/*     */   }
/*     */   
/*     */   public ElementOrder<N> nodeOrder()
/*     */   {
/* 100 */     return this.nodeOrder;
/*     */   }
/*     */   
/*     */   public Set<N> adjacentNodes(N node)
/*     */   {
/* 105 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */   
/*     */   public Set<N> predecessors(N node)
/*     */   {
/* 110 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */   
/*     */   public Set<N> successors(N node)
/*     */   {
/* 115 */     return checkedConnections(node).successors();
/*     */   }
/*     */   
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, @Nullable V defaultValue)
/*     */   {
/* 120 */     GraphConnections<N, V> connectionsU = (GraphConnections)this.nodeConnections.get(nodeU);
/* 121 */     if (connectionsU == null) {
/* 122 */       return defaultValue;
/*     */     }
/* 124 */     V value = connectionsU.value(nodeV);
/* 125 */     if (value == null) {
/* 126 */       return defaultValue;
/*     */     }
/* 128 */     return value;
/*     */   }
/*     */   
/*     */   protected long edgeCount()
/*     */   {
/* 133 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   protected final GraphConnections<N, V> checkedConnections(N node) {
/* 137 */     GraphConnections<N, V> connections = (GraphConnections)this.nodeConnections.get(node);
/* 138 */     if (connections == null) {
/* 139 */       Preconditions.checkNotNull(node);
/* 140 */       throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
/*     */     }
/* 142 */     return connections;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(@Nullable N node) {
/* 146 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ConfigurableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */