/*    */ package com.google.common.util.concurrent;
/*    */ 
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
/*    */ public class UncheckedExecutionException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected UncheckedExecutionException() {}
/*    */   
/*    */   protected UncheckedExecutionException(@Nullable String message)
/*    */   {
/* 47 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UncheckedExecutionException(@Nullable String message, @Nullable Throwable cause)
/*    */   {
/* 54 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UncheckedExecutionException(@Nullable Throwable cause)
/*    */   {
/* 61 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\UncheckedExecutionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */