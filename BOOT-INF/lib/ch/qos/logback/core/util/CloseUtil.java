/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.io.Closeable;
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
/*    */ public class CloseUtil
/*    */ {
/*    */   public static void closeQuietly(Closeable closeable)
/*    */   {
/* 33 */     if (closeable == null)
/* 34 */       return;
/*    */     try {
/* 36 */       closeable.close();
/*    */     }
/*    */     catch (IOException ex) {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void closeQuietly(Socket socket)
/*    */   {
/* 47 */     if (socket == null)
/* 48 */       return;
/*    */     try {
/* 50 */       socket.close();
/*    */     }
/*    */     catch (IOException ex) {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void closeQuietly(ServerSocket serverSocket)
/*    */   {
/* 62 */     if (serverSocket == null)
/* 63 */       return;
/*    */     try {
/* 65 */       serverSocket.close();
/*    */     }
/*    */     catch (IOException ex) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\cor\\util\CloseUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */