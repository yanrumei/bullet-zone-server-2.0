/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
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
/*    */ public abstract class ListenableFutureAdapter<T, S>
/*    */   extends FutureAdapter<T, S>
/*    */   implements ListenableFuture<T>
/*    */ {
/*    */   protected ListenableFutureAdapter(ListenableFuture<S> adaptee)
/*    */   {
/* 40 */     super(adaptee);
/*    */   }
/*    */   
/*    */ 
/*    */   public void addCallback(ListenableFutureCallback<? super T> callback)
/*    */   {
/* 46 */     addCallback(callback, callback);
/*    */   }
/*    */   
/*    */   public void addCallback(final SuccessCallback<? super T> successCallback, final FailureCallback failureCallback)
/*    */   {
/* 51 */     ListenableFuture<S> listenableAdaptee = (ListenableFuture)getAdaptee();
/* 52 */     listenableAdaptee.addCallback(new ListenableFutureCallback()
/*    */     {
/*    */       public void onSuccess(S result)
/*    */       {
/*    */         try {
/* 57 */           adapted = ListenableFutureAdapter.this.adaptInternal(result);
/*    */         } catch (ExecutionException ex) {
/*    */           T adapted;
/* 60 */           Throwable cause = ex.getCause();
/* 61 */           onFailure(cause != null ? cause : ex);
/* 62 */           return;
/*    */         }
/*    */         catch (Throwable ex) {
/* 65 */           onFailure(ex); return;
/*    */         }
/*    */         T adapted;
/* 68 */         successCallback.onSuccess(adapted);
/*    */       }
/*    */       
/*    */       public void onFailure(Throwable ex) {
/* 72 */         failureCallback.onFailure(ex);
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\concurrent\ListenableFutureAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */