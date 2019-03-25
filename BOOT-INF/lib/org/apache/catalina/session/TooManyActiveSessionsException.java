/*    */ package org.apache.catalina.session;
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
/*    */ public class TooManyActiveSessionsException
/*    */   extends IllegalStateException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
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
/*    */   private final int maxActiveSessions;
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
/*    */   public TooManyActiveSessionsException(String message, int maxActive)
/*    */   {
/* 40 */     super(message);
/* 41 */     this.maxActiveSessions = maxActive;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMaxActiveSessions()
/*    */   {
/* 50 */     return this.maxActiveSessions;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\TooManyActiveSessionsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */