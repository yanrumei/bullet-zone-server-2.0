/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
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
/*     */ abstract class AbstractBaseGraph<N>
/*     */   implements BaseGraph<N>
/*     */ {
/*     */   protected long edgeCount()
/*     */   {
/*  42 */     long degreeSum = 0L;
/*  43 */     for (N node : nodes()) {
/*  44 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  47 */     Preconditions.checkState((degreeSum & 1L) == 0L);
/*  48 */     return degreeSum >>> 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<EndpointPair<N>> edges()
/*     */   {
/*  57 */     new AbstractSet()
/*     */     {
/*     */       public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  60 */         return EndpointPairIterator.of(AbstractBaseGraph.this);
/*     */       }
/*     */       
/*     */       public int size()
/*     */       {
/*  65 */         return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public boolean contains(@Nullable Object obj)
/*     */       {
/*  74 */         if (!(obj instanceof EndpointPair)) {
/*  75 */           return false;
/*     */         }
/*  77 */         EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  78 */         return (AbstractBaseGraph.this.isDirected() == endpointPair.isOrdered()) && 
/*  79 */           (AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU())) && 
/*  80 */           (AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public int degree(N node)
/*     */   {
/*  87 */     if (isDirected()) {
/*  88 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/*  90 */     Set<N> neighbors = adjacentNodes(node);
/*  91 */     int selfLoopCount = (allowsSelfLoops()) && (neighbors.contains(node)) ? 1 : 0;
/*  92 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */   
/*     */ 
/*     */   public int inDegree(N node)
/*     */   {
/*  98 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */   
/*     */   public int outDegree(N node)
/*     */   {
/* 103 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\AbstractBaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */