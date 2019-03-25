/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import java.util.concurrent.ArrayBlockingQueue;
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
/*    */ class RemoteReceiverServerRunner
/*    */   extends ConcurrentServerRunner<RemoteReceiverClient>
/*    */ {
/*    */   private final int clientQueueSize;
/*    */   
/*    */   public RemoteReceiverServerRunner(ServerListener<RemoteReceiverClient> listener, Executor executor, int clientQueueSize)
/*    */   {
/* 40 */     super(listener, executor);
/* 41 */     this.clientQueueSize = clientQueueSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean configureClient(RemoteReceiverClient client)
/*    */   {
/* 49 */     client.setContext(getContext());
/* 50 */     client.setQueue(new ArrayBlockingQueue(this.clientQueueSize));
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\server\RemoteReceiverServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */