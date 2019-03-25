/*     */ package org.springframework.util.concurrent;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListenableFutureCallbackRegistry<T>
/*     */ {
/*  37 */   private final Queue<SuccessCallback<? super T>> successCallbacks = new LinkedList();
/*     */   
/*  39 */   private final Queue<FailureCallback> failureCallbacks = new LinkedList();
/*     */   
/*  41 */   private State state = State.NEW;
/*     */   
/*  43 */   private Object result = null;
/*     */   
/*  45 */   private final Object mutex = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCallback(ListenableFutureCallback<? super T> callback)
/*     */   {
/*  53 */     Assert.notNull(callback, "'callback' must not be null");
/*  54 */     synchronized (this.mutex) {
/*  55 */       switch (this.state) {
/*     */       case NEW: 
/*  57 */         this.successCallbacks.add(callback);
/*  58 */         this.failureCallbacks.add(callback);
/*  59 */         break;
/*     */       case SUCCESS: 
/*  61 */         notifySuccess(callback);
/*  62 */         break;
/*     */       case FAILURE: 
/*  64 */         notifyFailure(callback);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifySuccess(SuccessCallback<? super T> callback)
/*     */   {
/*     */     try
/*     */     {
/*  73 */       callback.onSuccess(this.result);
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */   }
/*     */   
/*     */   private void notifyFailure(FailureCallback callback)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       callback.onFailure((Throwable)this.result);
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addSuccessCallback(SuccessCallback<? super T> callback)
/*     */   {
/*  96 */     Assert.notNull(callback, "'callback' must not be null");
/*  97 */     synchronized (this.mutex) {
/*  98 */       switch (this.state) {
/*     */       case NEW: 
/* 100 */         this.successCallbacks.add(callback);
/* 101 */         break;
/*     */       case SUCCESS: 
/* 103 */         notifySuccess(callback);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addFailureCallback(FailureCallback callback)
/*     */   {
/* 115 */     Assert.notNull(callback, "'callback' must not be null");
/* 116 */     synchronized (this.mutex) {
/* 117 */       switch (this.state) {
/*     */       case NEW: 
/* 119 */         this.failureCallbacks.add(callback);
/* 120 */         break;
/*     */       case FAILURE: 
/* 122 */         notifyFailure(callback);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void success(T result)
/*     */   {
/* 134 */     synchronized (this.mutex) {
/* 135 */       this.state = State.SUCCESS;
/* 136 */       this.result = result;
/* 137 */       while (!this.successCallbacks.isEmpty()) {
/* 138 */         notifySuccess((SuccessCallback)this.successCallbacks.poll());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void failure(Throwable ex)
/*     */   {
/* 149 */     synchronized (this.mutex) {
/* 150 */       this.state = State.FAILURE;
/* 151 */       this.result = ex;
/* 152 */       while (!this.failureCallbacks.isEmpty()) {
/* 153 */         notifyFailure((FailureCallback)this.failureCallbacks.poll());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum State {
/* 159 */     NEW,  SUCCESS,  FAILURE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\concurrent\ListenableFutureCallbackRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */