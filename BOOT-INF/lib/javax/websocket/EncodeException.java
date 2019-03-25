/*    */ package javax.websocket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncodeException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Object object;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EncodeException(Object object, String message)
/*    */   {
/* 26 */     super(message);
/* 27 */     this.object = object;
/*    */   }
/*    */   
/*    */   public EncodeException(Object object, String message, Throwable cause) {
/* 31 */     super(message, cause);
/* 32 */     this.object = object;
/*    */   }
/*    */   
/*    */   public Object getObject() {
/* 36 */     return this.object;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\EncodeException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */