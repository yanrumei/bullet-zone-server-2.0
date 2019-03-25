/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class ValueGraphBuilder<N, V>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private ValueGraphBuilder(boolean directed)
/*     */   {
/*  54 */     super(directed);
/*     */   }
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> directed()
/*     */   {
/*  59 */     return new ValueGraphBuilder(true);
/*     */   }
/*     */   
/*     */   public static ValueGraphBuilder<Object, Object> undirected()
/*     */   {
/*  64 */     return new ValueGraphBuilder(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <N> ValueGraphBuilder<N, Object> from(ValueGraph<N, ?> graph)
/*     */   {
/*  76 */     return 
/*     */     
/*  78 */       new ValueGraphBuilder(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueGraphBuilder<N, V> allowsSelfLoops(boolean allowsSelfLoops)
/*     */   {
/*  87 */     this.allowsSelfLoops = allowsSelfLoops;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueGraphBuilder<N, V> expectedNodeCount(int expectedNodeCount)
/*     */   {
/*  97 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public <N1 extends N> ValueGraphBuilder<N1, V> nodeOrder(ElementOrder<N1> nodeOrder)
/*     */   {
/* 103 */     ValueGraphBuilder<N1, V> newBuilder = cast();
/* 104 */     newBuilder.nodeOrder = ((ElementOrder)Preconditions.checkNotNull(nodeOrder));
/* 105 */     return newBuilder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <N1 extends N, V1 extends V> MutableValueGraph<N1, V1> build()
/*     */   {
/* 113 */     return new ConfigurableMutableValueGraph(this);
/*     */   }
/*     */   
/*     */   private <N1 extends N, V1 extends V> ValueGraphBuilder<N1, V1> cast()
/*     */   {
/* 118 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ValueGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */