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
/*    */ @GwtCompatible(serializable=true)
/*    */ class EmptyImmutableSetMultimap
/*    */   extends ImmutableSetMultimap<Object, Object>
/*    */ {
/* 28 */   static final EmptyImmutableSetMultimap INSTANCE = new EmptyImmutableSetMultimap();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 31 */   private EmptyImmutableSetMultimap() { super(ImmutableMap.of(), 0, null); }
/*    */   
/*    */   private Object readResolve()
/*    */   {
/* 35 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\collect\EmptyImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */