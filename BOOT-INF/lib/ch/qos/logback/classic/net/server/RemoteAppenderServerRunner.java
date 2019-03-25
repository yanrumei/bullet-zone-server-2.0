/*    */ package ch.qos.logback.classic.net.server;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.core.net.server.ConcurrentServerRunner;
/*    */ import ch.qos.logback.core.net.server.ServerListener;
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
/*    */ class RemoteAppenderServerRunner
/*    */   extends ConcurrentServerRunner<RemoteAppenderClient>
/*    */ {
/*    */   public RemoteAppenderServerRunner(ServerListener<RemoteAppenderClient> listener, Executor executor)
/*    */   {
/* 39 */     super(listener, executor);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean configureClient(RemoteAppenderClient client)
/*    */   {
/* 47 */     client.setLoggerContext((LoggerContext)getContext());
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\server\RemoteAppenderServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */