/*    */ package javax.websocket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SendResult
/*    */ {
/*    */   private final Throwable exception;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final boolean ok;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SendResult(Throwable exception)
/*    */   {
/* 24 */     this.exception = exception;
/* 25 */     this.ok = (exception == null);
/*    */   }
/*    */   
/*    */   public SendResult() {
/* 29 */     this(null);
/*    */   }
/*    */   
/*    */   public Throwable getException() {
/* 33 */     return this.exception;
/*    */   }
/*    */   
/*    */   public boolean isOK() {
/* 37 */     return this.ok;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\SendResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */