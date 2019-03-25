/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.websocket.CloseReason;
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
/*    */ public class WsIOException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final CloseReason closeReason;
/*    */   
/*    */   public WsIOException(CloseReason closeReason)
/*    */   {
/* 35 */     this.closeReason = closeReason;
/*    */   }
/*    */   
/*    */   public CloseReason getCloseReason() {
/* 39 */     return this.closeReason;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsIOException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */