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
/*    */ @GwtCompatible
/*    */ public class ExecutionError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected ExecutionError() {}
/*    */   
/*    */   protected ExecutionError(@Nullable String message)
/*    */   {
/* 41 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ExecutionError(@Nullable String message, @Nullable Error cause)
/*    */   {
/* 48 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ExecutionError(@Nullable Error cause)
/*    */   {
/* 55 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ExecutionError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */