/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import javax.crypto.SecretKey;
/*    */ import javax.security.auth.callback.Callback;
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
/*    */ public class SecretKeyCallback
/*    */   implements Callback
/*    */ {
/*    */   private final Request request;
/*    */   private SecretKey key;
/*    */   
/*    */   public SecretKeyCallback(Request request)
/*    */   {
/* 32 */     this.request = request;
/*    */   }
/*    */   
/*    */   public Request getRequest() {
/* 36 */     return this.request;
/*    */   }
/*    */   
/*    */   public void setKey(SecretKey key) {
/* 40 */     this.key = key;
/*    */   }
/*    */   
/*    */   public SecretKey getKey() {
/* 44 */     return this.key;
/*    */   }
/*    */   
/*    */ 
/*    */   public static class AliasRequest
/*    */     implements SecretKeyCallback.Request
/*    */   {
/*    */     private final String alias;
/*    */     
/*    */     public AliasRequest(String alias)
/*    */     {
/* 55 */       this.alias = alias;
/*    */     }
/*    */     
/*    */     public String getAlias() {
/* 59 */       return this.alias;
/*    */     }
/*    */   }
/*    */   
/*    */   public static abstract interface Request {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\SecretKeyCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */