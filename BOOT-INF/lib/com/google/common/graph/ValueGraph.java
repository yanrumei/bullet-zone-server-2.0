package com.google.common.graph;

import com.google.common.annotations.Beta;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract interface ValueGraph<N, V>
  extends BaseGraph<N>
{
  public abstract Set<N> nodes();
  
  public abstract Set<EndpointPair<N>> edges();
  
  public abstract Graph<N> asGraph();
  
  public abstract boolean isDirected();
  
  public abstract boolean allowsSelfLoops();
  
  public abstract ElementOrder<N> nodeOrder();
  
  public abstract Set<N> adjacentNodes(N paramN);
  
  public abstract Set<N> predecessors(N paramN);
  
  public abstract Set<N> successors(N paramN);
  
  public abstract int degree(N paramN);
  
  public abstract int inDegree(N paramN);
  
  public abstract int outDegree(N paramN);
  
  public abstract V edgeValue(N paramN1, N paramN2);
  
  public abstract V edgeValueOrDefault(N paramN1, N paramN2, @Nullable V paramV);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\ValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */