/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph()
/*     */   {
/*  48 */     new AbstractGraph()
/*     */     {
/*     */       public Set<N> nodes() {
/*  51 */         return AbstractNetwork.this.nodes();
/*     */       }
/*     */       
/*     */       public Set<EndpointPair<N>> edges()
/*     */       {
/*  56 */         if (AbstractNetwork.this.allowsParallelEdges()) {
/*  57 */           return super.edges();
/*     */         }
/*     */         
/*     */ 
/*  61 */         new AbstractSet()
/*     */         {
/*     */           public Iterator<EndpointPair<N>> iterator() {
/*  64 */             Iterators.transform(AbstractNetwork.this
/*  65 */               .edges().iterator(), new Function()
/*     */               {
/*     */                 public EndpointPair<N> apply(E edge)
/*     */                 {
/*  69 */                   return AbstractNetwork.this.incidentNodes(edge);
/*     */                 }
/*     */               });
/*     */           }
/*     */           
/*     */           public int size()
/*     */           {
/*  76 */             return AbstractNetwork.this.edges().size();
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           public boolean contains(@Nullable Object obj)
/*     */           {
/*  85 */             if (!(obj instanceof EndpointPair)) {
/*  86 */               return false;
/*     */             }
/*  88 */             EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  89 */             return (AbstractNetwork.1.this.isDirected() == endpointPair.isOrdered()) && 
/*  90 */               (AbstractNetwork.1.this.nodes().contains(endpointPair.nodeU())) && 
/*  91 */               (AbstractNetwork.1.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */           }
/*     */         };
/*     */       }
/*     */       
/*     */       public ElementOrder<N> nodeOrder()
/*     */       {
/*  98 */         return AbstractNetwork.this.nodeOrder();
/*     */       }
/*     */       
/*     */       public boolean isDirected()
/*     */       {
/* 103 */         return AbstractNetwork.this.isDirected();
/*     */       }
/*     */       
/*     */       public boolean allowsSelfLoops()
/*     */       {
/* 108 */         return AbstractNetwork.this.allowsSelfLoops();
/*     */       }
/*     */       
/*     */       public Set<N> adjacentNodes(N node)
/*     */       {
/* 113 */         return AbstractNetwork.this.adjacentNodes(node);
/*     */       }
/*     */       
/*     */       public Set<N> predecessors(N node)
/*     */       {
/* 118 */         return AbstractNetwork.this.predecessors(node);
/*     */       }
/*     */       
/*     */       public Set<N> successors(N node)
/*     */       {
/* 123 */         return AbstractNetwork.this.successors(node);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int degree(N node)
/*     */   {
/* 132 */     if (isDirected()) {
/* 133 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 135 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */   
/*     */ 
/*     */   public int inDegree(N node)
/*     */   {
/* 141 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public int outDegree(N node)
/*     */   {
/* 146 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public Set<E> adjacentEdges(E edge)
/*     */   {
/* 151 */     EndpointPair<N> endpointPair = incidentNodes(edge);
/*     */     
/* 153 */     Set<E> endpointPairIncidentEdges = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 154 */     return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
/*     */   }
/*     */   
/*     */   public final boolean equals(@Nullable Object obj)
/*     */   {
/* 159 */     if (obj == this) {
/* 160 */       return true;
/*     */     }
/* 162 */     if (!(obj instanceof Network)) {
/* 163 */       return false;
/*     */     }
/* 165 */     Network<?, ?> other = (Network)obj;
/*     */     
/* 167 */     return (isDirected() == other.isDirected()) && 
/* 168 */       (nodes().equals(other.nodes())) && 
/* 169 */       (edgeIncidentNodesMap(this).equals(edgeIncidentNodesMap(other)));
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/* 174 */     return edgeIncidentNodesMap(this).hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 181 */     String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", new Object[] {
/*     */     
/* 183 */       Boolean.valueOf(isDirected()), Boolean.valueOf(allowsParallelEdges()), Boolean.valueOf(allowsSelfLoops()) });
/* 184 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, 
/* 185 */       nodes(), edgeIncidentNodesMap(this) });
/*     */   }
/*     */   
/*     */   private static <N, E> Map<E, EndpointPair<N>> edgeIncidentNodesMap(Network<N, E> network) {
/* 189 */     Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function()
/*     */     {
/*     */       public EndpointPair<N> apply(E edge)
/*     */       {
/* 193 */         return this.val$network.incidentNodes(edge);
/*     */       }
/* 195 */     };
/* 196 */     return Maps.asMap(network.edges(), edgeToIncidentNodesFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */