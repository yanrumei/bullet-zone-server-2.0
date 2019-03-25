/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.Immutable;
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
/*     */ @Immutable(containerOf={"N"})
/*     */ @Beta
/*     */ public abstract class EndpointPair<N>
/*     */   implements Iterable<N>
/*     */ {
/*     */   private final N nodeU;
/*     */   private final N nodeV;
/*     */   
/*     */   private EndpointPair(N nodeU, N nodeV)
/*     */   {
/*  47 */     this.nodeU = Preconditions.checkNotNull(nodeU);
/*  48 */     this.nodeV = Preconditions.checkNotNull(nodeV);
/*     */   }
/*     */   
/*     */   public static <N> EndpointPair<N> ordered(N source, N target)
/*     */   {
/*  53 */     return new Ordered(source, target, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <N> EndpointPair<N> unordered(N nodeU, N nodeV)
/*     */   {
/*  59 */     return new Unordered(nodeV, nodeU, null);
/*     */   }
/*     */   
/*     */   static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV)
/*     */   {
/*  64 */     return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
/*     */   }
/*     */   
/*     */   static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV)
/*     */   {
/*  69 */     return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract N source();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract N target();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final N nodeU()
/*     */   {
/*  91 */     return (N)this.nodeU;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final N nodeV()
/*     */   {
/*  99 */     return (N)this.nodeV;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final N adjacentNode(Object node)
/*     */   {
/* 108 */     if (node.equals(this.nodeU))
/* 109 */       return (N)this.nodeV;
/* 110 */     if (node.equals(this.nodeV)) {
/* 111 */       return (N)this.nodeU;
/*     */     }
/*     */     
/* 114 */     throw new IllegalArgumentException(String.format("EndpointPair %s does not contain node %s", new Object[] { this, node }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isOrdered();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final UnmodifiableIterator<N> iterator()
/*     */   {
/* 127 */     return Iterators.forArray(new Object[] { this.nodeU, this.nodeV });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean equals(@Nullable Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int hashCode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class Ordered<N>
/*     */     extends EndpointPair<N>
/*     */   {
/*     */     private Ordered(N source, N target)
/*     */     {
/* 148 */       super(target, null);
/*     */     }
/*     */     
/*     */     public N source()
/*     */     {
/* 153 */       return (N)nodeU();
/*     */     }
/*     */     
/*     */     public N target()
/*     */     {
/* 158 */       return (N)nodeV();
/*     */     }
/*     */     
/*     */     public boolean isOrdered()
/*     */     {
/* 163 */       return true;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/* 168 */       if (obj == this) {
/* 169 */         return true;
/*     */       }
/* 171 */       if (!(obj instanceof EndpointPair)) {
/* 172 */         return false;
/*     */       }
/*     */       
/* 175 */       EndpointPair<?> other = (EndpointPair)obj;
/* 176 */       if (isOrdered() != other.isOrdered()) {
/* 177 */         return false;
/*     */       }
/*     */       
/* 180 */       return (source().equals(other.source())) && (target().equals(other.target()));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 185 */       return Objects.hashCode(new Object[] { source(), target() });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 190 */       return String.format("<%s -> %s>", new Object[] { source(), target() });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Unordered<N> extends EndpointPair<N> {
/*     */     private Unordered(N nodeU, N nodeV) {
/* 196 */       super(nodeV, null);
/*     */     }
/*     */     
/*     */     public N source()
/*     */     {
/* 201 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */     
/*     */     public N target()
/*     */     {
/* 206 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */     
/*     */     public boolean isOrdered()
/*     */     {
/* 211 */       return false;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj)
/*     */     {
/* 216 */       if (obj == this) {
/* 217 */         return true;
/*     */       }
/* 219 */       if (!(obj instanceof EndpointPair)) {
/* 220 */         return false;
/*     */       }
/*     */       
/* 223 */       EndpointPair<?> other = (EndpointPair)obj;
/* 224 */       if (isOrdered() != other.isOrdered()) {
/* 225 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 232 */       if (nodeU().equals(other.nodeU()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */         return nodeV().equals(other.nodeV());
/*     */       }
/* 240 */       return (nodeU().equals(other.nodeV())) && (nodeV().equals(other.nodeU()));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 245 */       return nodeU().hashCode() + nodeV().hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 250 */       return String.format("[%s, %s]", new Object[] { nodeU(), nodeV() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\EndpointPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */