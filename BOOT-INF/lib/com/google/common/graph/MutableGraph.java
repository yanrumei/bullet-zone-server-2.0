package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
public abstract interface MutableGraph<N>
  extends Graph<N>
{
  @CanIgnoreReturnValue
  public abstract boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean putEdge(N paramN1, N paramN2);
  
  @CanIgnoreReturnValue
  public abstract boolean removeNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean removeEdge(N paramN1, N paramN2);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\MutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */