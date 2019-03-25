package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
public abstract interface MutableValueGraph<N, V>
  extends ValueGraph<N, V>
{
  @CanIgnoreReturnValue
  public abstract boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract V putEdgeValue(N paramN1, N paramN2, V paramV);
  
  @CanIgnoreReturnValue
  public abstract boolean removeNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract V removeEdge(N paramN1, N paramN2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\MutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */