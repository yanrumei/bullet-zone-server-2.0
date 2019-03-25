/*    */ package org.apache.tomcat.util.net;
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
/*    */ public enum DispatchType
/*    */ {
/* 26 */   NON_BLOCKING_READ(SocketEvent.OPEN_READ), 
/* 27 */   NON_BLOCKING_WRITE(SocketEvent.OPEN_WRITE);
/*    */   
/*    */   private final SocketEvent status;
/*    */   
/*    */   private DispatchType(SocketEvent status) {
/* 32 */     this.status = status;
/*    */   }
/*    */   
/*    */   public SocketEvent getSocketStatus() {
/* 36 */     return this.status;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\DispatchType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */