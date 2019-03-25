/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import java.security.KeyStore;
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
/*    */ 
/*    */ public class TrustStoreCallback
/*    */   implements Callback
/*    */ {
/*    */   private KeyStore trustStore;
/*    */   
/*    */   public void setTrustStore(KeyStore trustStore)
/*    */   {
/* 32 */     this.trustStore = trustStore;
/*    */   }
/*    */   
/*    */   public KeyStore getTrustStore() {
/* 36 */     return this.trustStore;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\TrustStoreCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */