package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.Nullable;

abstract interface GraphConnections<N, V>
{
  public abstract Set<N> adjacentNodes();
  
  public abstract Set<N> predecessors();
  
  public abstract Set<N> successors();
  
  @Nullable
  public abstract V value(Object paramObject);
  
  public abstract void removePredecessor(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract V removeSuccessor(Object paramObject);
  
  public abstract void addPredecessor(N paramN, V paramV);
  
  @CanIgnoreReturnValue
  public abstract V addSuccessor(N paramN, V paramV);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\GraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */