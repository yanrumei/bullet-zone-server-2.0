/*    */ package ch.qos.logback.classic.net;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import java.util.concurrent.ScheduledExecutorService;
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
/*    */ public abstract class ReceiverBase
/*    */   extends ContextAwareBase
/*    */   implements LifeCycle
/*    */ {
/*    */   private boolean started;
/*    */   
/*    */   public final void start()
/*    */   {
/* 33 */     if (isStarted())
/* 34 */       return;
/* 35 */     if (getContext() == null) {
/* 36 */       throw new IllegalStateException("context not set");
/*    */     }
/* 38 */     if (shouldStart()) {
/* 39 */       getContext().getScheduledExecutorService().execute(getRunnableTask());
/* 40 */       this.started = true;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final void stop()
/*    */   {
/* 48 */     if (!isStarted())
/* 49 */       return;
/*    */     try {
/* 51 */       onStop();
/*    */     } catch (RuntimeException ex) {
/* 53 */       addError("on stop: " + ex, ex);
/*    */     }
/* 55 */     this.started = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean isStarted()
/*    */   {
/* 62 */     return this.started;
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldStart();
/*    */   
/*    */   protected abstract void onStop();
/*    */   
/*    */   protected abstract Runnable getRunnableTask();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\ReceiverBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */