/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.FutureTask;
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
/*    */ public class ListenableFutureTask<T>
/*    */   extends FutureTask<T>
/*    */   implements ListenableFuture<T>
/*    */ {
/* 31 */   private final ListenableFutureCallbackRegistry<T> callbacks = new ListenableFutureCallbackRegistry();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ListenableFutureTask(Callable<T> callable)
/*    */   {
/* 40 */     super(callable);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ListenableFutureTask(Runnable runnable, T result)
/*    */   {
/* 51 */     super(runnable, result);
/*    */   }
/*    */   
/*    */ 
/*    */   public void addCallback(ListenableFutureCallback<? super T> callback)
/*    */   {
/* 57 */     this.callbacks.addCallback(callback);
/*    */   }
/*    */   
/*    */   public void addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback)
/*    */   {
/* 62 */     this.callbacks.addSuccessCallback(successCallback);
/* 63 */     this.callbacks.addFailureCallback(failureCallback);
/*    */   }
/*    */   
/*    */   protected void done()
/*    */   {
/*    */     Throwable cause;
/*    */     try
/*    */     {
/* 71 */       T result = get();
/* 72 */       this.callbacks.success(result);
/* 73 */       return;
/*    */     }
/*    */     catch (InterruptedException ex) {
/* 76 */       Thread.currentThread().interrupt();
/* 77 */       return;
/*    */     }
/*    */     catch (ExecutionException ex) {
/* 80 */       Throwable cause = ex.getCause();
/* 81 */       if (cause == null) {
/* 82 */         cause = ex;
/*    */       }
/*    */     }
/*    */     catch (Throwable ex) {
/* 86 */       cause = ex;
/*    */     }
/* 88 */     this.callbacks.failure(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\concurrent\ListenableFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */