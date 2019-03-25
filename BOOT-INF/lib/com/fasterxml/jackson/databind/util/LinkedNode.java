/*    */ package com.fasterxml.jackson.databind.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LinkedNode<T>
/*    */ {
/*    */   private final T value;
/*    */   
/*    */ 
/*    */   private LinkedNode<T> next;
/*    */   
/*    */ 
/*    */ 
/*    */   public LinkedNode(T value, LinkedNode<T> next)
/*    */   {
/* 16 */     this.value = value;
/* 17 */     this.next = next;
/*    */   }
/*    */   
/*    */   public void linkNext(LinkedNode<T> n)
/*    */   {
/* 22 */     if (this.next != null) {
/* 23 */       throw new IllegalStateException();
/*    */     }
/* 25 */     this.next = n;
/*    */   }
/*    */   
/* 28 */   public LinkedNode<T> next() { return this.next; }
/*    */   
/* 30 */   public T value() { return (T)this.value; }
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
/*    */   public static <ST> boolean contains(LinkedNode<ST> node, ST value)
/*    */   {
/* 44 */     while (node != null) {
/* 45 */       if (node.value() == value) {
/* 46 */         return true;
/*    */       }
/* 48 */       node = node.next();
/*    */     }
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\LinkedNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */