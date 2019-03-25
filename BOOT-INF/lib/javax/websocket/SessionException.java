/*    */ package javax.websocket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Session session;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SessionException(String message, Throwable cause, Session session)
/*    */   {
/* 27 */     super(message, cause);
/* 28 */     this.session = session;
/*    */   }
/*    */   
/*    */   public Session getSession()
/*    */   {
/* 33 */     return this.session;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\SessionException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */