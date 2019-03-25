/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
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
/*    */ abstract class MultiEdgesConnecting<E>
/*    */   extends AbstractSet<E>
/*    */ {
/*    */   private final Map<E, ?> outEdgeToNode;
/*    */   private final Object targetNode;
/*    */   
/*    */   MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode)
/*    */   {
/* 44 */     this.outEdgeToNode = ((Map)Preconditions.checkNotNull(outEdgeToNode));
/* 45 */     this.targetNode = Preconditions.checkNotNull(targetNode);
/*    */   }
/*    */   
/*    */   public UnmodifiableIterator<E> iterator()
/*    */   {
/* 50 */     final Iterator<? extends Map.Entry<E, ?>> entries = this.outEdgeToNode.entrySet().iterator();
/* 51 */     new AbstractIterator()
/*    */     {
/*    */       protected E computeNext() {
/* 54 */         while (entries.hasNext()) {
/* 55 */           Map.Entry<E, ?> entry = (Map.Entry)entries.next();
/* 56 */           if (MultiEdgesConnecting.this.targetNode.equals(entry.getValue())) {
/* 57 */             return (E)entry.getKey();
/*    */           }
/*    */         }
/* 60 */         return (E)endOfData();
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   public boolean contains(@Nullable Object edge)
/*    */   {
/* 67 */     return this.targetNode.equals(this.outEdgeToNode.get(edge));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\graph\MultiEdgesConnecting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */