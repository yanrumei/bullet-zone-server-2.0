/*    */ package ch.qos.logback.classic.net.server;
/*    */ 
/*    */ import ch.qos.logback.core.net.server.ServerSocketListener;
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
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
/*    */ class RemoteAppenderServerListener
/*    */   extends ServerSocketListener<RemoteAppenderClient>
/*    */ {
/*    */   public RemoteAppenderServerListener(ServerSocket serverSocket)
/*    */   {
/* 36 */     super(serverSocket);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected RemoteAppenderClient createClient(String id, Socket socket)
/*    */     throws IOException
/*    */   {
/* 44 */     return new RemoteAppenderStreamClient(id, socket);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\server\RemoteAppenderServerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */