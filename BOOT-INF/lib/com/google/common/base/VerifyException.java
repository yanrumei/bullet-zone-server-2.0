/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public class VerifyException
/*    */   extends RuntimeException
/*    */ {
/*    */   public VerifyException() {}
/*    */   
/*    */   public VerifyException(@Nullable String message)
/*    */   {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public VerifyException(@Nullable Throwable cause)
/*    */   {
/* 46 */     super(cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public VerifyException(@Nullable String message, @Nullable Throwable cause)
/*    */   {
/* 56 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\base\VerifyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */