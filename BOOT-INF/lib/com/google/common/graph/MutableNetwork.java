package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
public abstract interface MutableNetwork<N, E>
  extends Network<N, E>
{
  @CanIgnoreReturnValue
  public abstract boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean addEdge(N paramN1, N paramN2, E paramE);
  
  @CanIgnoreReturnValue
  public abstract boolean removeNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean removeEdge(E paramE);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\MutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */