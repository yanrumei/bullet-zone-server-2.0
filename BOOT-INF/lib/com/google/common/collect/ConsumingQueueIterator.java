/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Collections;
/*    */ import java.util.Queue;
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
/*    */ @GwtCompatible
/*    */ class ConsumingQueueIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private final Queue<T> queue;
/*    */   
/*    */   ConsumingQueueIterator(T... elements)
/*    */   {
/* 33 */     this.queue = new ArrayDeque(elements.length);
/* 34 */     Collections.addAll(this.queue, elements);
/*    */   }
/*    */   
/*    */   ConsumingQueueIterator(Queue<T> queue) {
/* 38 */     this.queue = ((Queue)Preconditions.checkNotNull(queue));
/*    */   }
/*    */   
/*    */   public T computeNext()
/*    */   {
/* 43 */     return (T)(this.queue.isEmpty() ? endOfData() : this.queue.remove());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\ConsumingQueueIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */