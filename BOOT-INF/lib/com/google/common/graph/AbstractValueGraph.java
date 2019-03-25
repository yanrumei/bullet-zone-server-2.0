/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ public abstract class AbstractValueGraph<N, V>
/*     */   extends AbstractBaseGraph<N>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   public Graph<N> asGraph()
/*     */   {
/*  46 */     new AbstractGraph()
/*     */     {
/*     */       public Set<N> nodes() {
/*  49 */         return AbstractValueGraph.this.nodes();
/*     */       }
/*     */       
/*     */       public Set<EndpointPair<N>> edges()
/*     */       {
/*  54 */         return AbstractValueGraph.this.edges();
/*     */       }
/*     */       
/*     */       public boolean isDirected()
/*     */       {
/*  59 */         return AbstractValueGraph.this.isDirected();
/*     */       }
/*     */       
/*     */       public boolean allowsSelfLoops()
/*     */       {
/*  64 */         return AbstractValueGraph.this.allowsSelfLoops();
/*     */       }
/*     */       
/*     */       public ElementOrder<N> nodeOrder()
/*     */       {
/*  69 */         return AbstractValueGraph.this.nodeOrder();
/*     */       }
/*     */       
/*     */       public Set<N> adjacentNodes(N node)
/*     */       {
/*  74 */         return AbstractValueGraph.this.adjacentNodes(node);
/*     */       }
/*     */       
/*     */       public Set<N> predecessors(N node)
/*     */       {
/*  79 */         return AbstractValueGraph.this.predecessors(node);
/*     */       }
/*     */       
/*     */       public Set<N> successors(N node)
/*     */       {
/*  84 */         return AbstractValueGraph.this.successors(node);
/*     */       }
/*     */       
/*     */       public int degree(N node)
/*     */       {
/*  89 */         return AbstractValueGraph.this.degree(node);
/*     */       }
/*     */       
/*     */       public int inDegree(N node)
/*     */       {
/*  94 */         return AbstractValueGraph.this.inDegree(node);
/*     */       }
/*     */       
/*     */       public int outDegree(N node)
/*     */       {
/*  99 */         return AbstractValueGraph.this.outDegree(node);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public V edgeValue(N nodeU, N nodeV)
/*     */   {
/* 106 */     V value = edgeValueOrDefault(nodeU, nodeV, null);
/* 107 */     if (value == null) {
/* 108 */       Preconditions.checkArgument(nodes().contains(nodeU), "Node %s is not an element of this graph.", nodeU);
/* 109 */       Preconditions.checkArgument(nodes().contains(nodeV), "Node %s is not an element of this graph.", nodeV);
/* 110 */       throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", new Object[] { nodeU, nodeV }));
/*     */     }
/* 112 */     return value;
/*     */   }
/*     */   
/*     */   public final boolean equals(@Nullable Object obj)
/*     */   {
/* 117 */     if (obj == this) {
/* 118 */       return true;
/*     */     }
/* 120 */     if (!(obj instanceof ValueGraph)) {
/* 121 */       return false;
/*     */     }
/* 123 */     ValueGraph<?, ?> other = (ValueGraph)obj;
/*     */     
/* 125 */     return (isDirected() == other.isDirected()) && 
/* 126 */       (nodes().equals(other.nodes())) && 
/* 127 */       (edgeValueMap(this).equals(edgeValueMap(other)));
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/* 132 */     return edgeValueMap(this).hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
/* 140 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edgeValueMap(this) });
/*     */   }
/*     */   
/*     */   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(ValueGraph<N, V> graph) {
/* 144 */     Function<EndpointPair<N>, V> edgeToValueFn = new Function()
/*     */     {
/*     */       public V apply(EndpointPair<N> edge)
/*     */       {
/* 148 */         return (V)this.val$graph.edgeValue(edge.nodeU(), edge.nodeV());
/*     */       }
/* 150 */     };
/* 151 */     return Maps.asMap(graph.edges(), edgeToValueFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */