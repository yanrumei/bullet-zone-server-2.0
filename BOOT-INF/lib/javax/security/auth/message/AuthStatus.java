/*    */ package javax.security.auth.message;
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
/*    */ public class AuthStatus
/*    */ {
/* 21 */   public static final AuthStatus SUCCESS = new AuthStatus("SUCCESS");
/* 22 */   public static final AuthStatus FAILURE = new AuthStatus("FAILURE");
/* 23 */   public static final AuthStatus SEND_SUCCESS = new AuthStatus("SEND_SUCCESS");
/* 24 */   public static final AuthStatus SEND_FAILURE = new AuthStatus("SEND_FAILURE");
/* 25 */   public static final AuthStatus SEND_CONTINUE = new AuthStatus("SEND_CONTINUE");
/*    */   private final String name;
/*    */   
/*    */   private AuthStatus(String name)
/*    */   {
/* 30 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 35 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\AuthStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */