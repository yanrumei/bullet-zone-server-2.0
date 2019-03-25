/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ public enum BoundType
/*    */ {
/* 31 */   OPEN, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 40 */   CLOSED;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private BoundType() {}
/*    */   
/*    */ 
/*    */ 
/*    */   static BoundType forBoolean(boolean inclusive)
/*    */   {
/* 51 */     return inclusive ? CLOSED : OPEN;
/*    */   }
/*    */   
/*    */   abstract BoundType flip();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\BoundType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */