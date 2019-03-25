/*    */ package com.google.common.escape;
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
/*    */ @GwtCompatible(emulated=true)
/*    */ final class Platform
/*    */ {
/*    */   static char[] charBufferFromThreadLocal()
/*    */   {
/* 30 */     return (char[])DEST_TL.get();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 38 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal()
/*    */   {
/*    */     protected char[] initialValue()
/*    */     {
/* 42 */       return new char['Ѐ'];
/*    */     }
/*    */   };
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\escape\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */