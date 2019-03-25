package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

abstract interface NetworkConnections<N, E>
{
  public abstract Set<N> adjacentNodes();
  
  public abstract Set<N> predecessors();
  
  public abstract Set<N> successors();
  
  public abstract Set<E> incidentEdges();
  
  public abstract Set<E> inEdges();
  
  public abstract Set<E> outEdges();
  
  public abstract Set<E> edgesConnecting(N paramN);
  
  public abstract N oppositeNode(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract N removeInEdge(Object paramObject, boolean paramBoolean);
  
  @CanIgnoreReturnValue
  public abstract N removeOutEdge(Object paramObject);
  
  public abstract void addInEdge(E paramE, N paramN, boolean paramBoolean);
  
  public abstract void addOutEdge(E paramE, N paramN);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\NetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */