/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class EdgesConnecting<E>
/*    */   extends AbstractSet<E>
/*    */ {
/*    */   private final Map<?, E> nodeToOutEdge;
/*    */   private final Object targetNode;
/*    */   
/*    */   EdgesConnecting(Map<?, E> nodeToEdgeMap, Object targetNode)
/*    */   {
/* 43 */     this.nodeToOutEdge = ((Map)Preconditions.checkNotNull(nodeToEdgeMap));
/* 44 */     this.targetNode = Preconditions.checkNotNull(targetNode);
/*    */   }
/*    */   
/*    */   public UnmodifiableIterator<E> iterator()
/*    */   {
/* 49 */     E connectingEdge = getConnectingEdge();
/* 50 */     return connectingEdge == null ? 
/* 51 */       ImmutableSet.of().iterator() : 
/* 52 */       Iterators.singletonIterator(connectingEdge);
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 57 */     return getConnectingEdge() == null ? 0 : 1;
/*    */   }
/*    */   
/*    */   public boolean contains(@Nullable Object edge)
/*    */   {
/* 62 */     E connectingEdge = getConnectingEdge();
/* 63 */     return (connectingEdge != null) && (connectingEdge.equals(edge));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private E getConnectingEdge() {
/* 68 */     return (E)this.nodeToOutEdge.get(this.targetNode);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\EdgesConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */