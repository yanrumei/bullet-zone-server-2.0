/*    */ package org.apache.tomcat.util.net;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public abstract class SocketProcessorBase<S>
/*    */   implements Runnable
/*    */ {
/*    */   protected SocketWrapperBase<S> socketWrapper;
/*    */   protected SocketEvent event;
/*    */   
/*    */   public SocketProcessorBase(SocketWrapperBase<S> socketWrapper, SocketEvent event)
/*    */   {
/* 27 */     reset(socketWrapper, event);
/*    */   }
/*    */   
/*    */   public void reset(SocketWrapperBase<S> socketWrapper, SocketEvent event)
/*    */   {
/* 32 */     Objects.requireNonNull(event);
/* 33 */     this.socketWrapper = socketWrapper;
/* 34 */     this.event = event;
/*    */   }
/*    */   
/*    */ 
/*    */   public final void run()
/*    */   {
/* 40 */     synchronized (this.socketWrapper)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */       if (this.socketWrapper.isClosed()) {
/* 47 */         return;
/*    */       }
/* 49 */       doRun();
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void doRun();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SocketProcessorBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */