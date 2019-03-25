/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ReadBufferOverflowException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final int minBufferSize;
/*    */   
/*    */   public ReadBufferOverflowException(int minBufferSize)
/*    */   {
/* 28 */     this.minBufferSize = minBufferSize;
/*    */   }
/*    */   
/*    */   public int getMinBufferSize() {
/* 32 */     return this.minBufferSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\ReadBufferOverflowException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */