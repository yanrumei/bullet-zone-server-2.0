/*    */ package org.apache.coyote.http2;
/*    */ 
/*    */ import org.apache.tomcat.util.net.SocketEvent;
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
/*    */ class StreamRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final StreamProcessor processor;
/*    */   private final SocketEvent event;
/*    */   
/*    */   public StreamRunnable(StreamProcessor processor, SocketEvent event)
/*    */   {
/* 28 */     this.processor = processor;
/* 29 */     this.event = event;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 35 */     this.processor.process(this.event);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\StreamRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */