/*    */ package javax.security.auth.message.callback;
/*    */ 
/*    */ import java.security.cert.CertStore;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CertStoreCallback
/*    */   implements Callback
/*    */ {
/*    */   private CertStore certStore;
/*    */   
/*    */   public void setCertStore(CertStore certStore)
/*    */   {
/* 35 */     this.certStore = certStore;
/*    */   }
/*    */   
/*    */   public CertStore getCertStore() {
/* 39 */     return this.certStore;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\security\auth\message\callback\CertStoreCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */