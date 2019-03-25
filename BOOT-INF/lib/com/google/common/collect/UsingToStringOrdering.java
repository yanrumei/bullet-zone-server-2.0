/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable=true)
/*    */ final class UsingToStringOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 28 */   static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public int compare(Object left, Object right) {
/* 32 */     return left.toString().compareTo(right.toString());
/*    */   }
/*    */   
/*    */   private Object readResolve()
/*    */   {
/* 37 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     return "Ordering.usingToString()";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\UsingToStringOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */