/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Executor;
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
/*    */ @GwtIncompatible
/*    */ public final class RemovalListeners
/*    */ {
/*    */   public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, Executor executor)
/*    */   {
/* 42 */     Preconditions.checkNotNull(listener);
/* 43 */     Preconditions.checkNotNull(executor);
/* 44 */     new RemovalListener()
/*    */     {
/*    */       public void onRemoval(final RemovalNotification<K, V> notification) {
/* 47 */         this.val$executor.execute(new Runnable()
/*    */         {
/*    */           public void run()
/*    */           {
/* 51 */             RemovalListeners.1.this.val$listener.onRemoval(notification);
/*    */           }
/*    */         });
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\cache\RemovalListeners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */