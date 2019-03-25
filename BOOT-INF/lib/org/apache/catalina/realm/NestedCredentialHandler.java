/*    */ package org.apache.catalina.realm;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.catalina.CredentialHandler;
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
/*    */ public class NestedCredentialHandler
/*    */   implements CredentialHandler
/*    */ {
/* 26 */   private final List<CredentialHandler> credentialHandlers = new ArrayList();
/*    */   
/*    */ 
/*    */   public boolean matches(String inputCredentials, String storedCredentials)
/*    */   {
/* 31 */     for (CredentialHandler handler : this.credentialHandlers) {
/* 32 */       if (handler.matches(inputCredentials, storedCredentials)) {
/* 33 */         return true;
/*    */       }
/*    */     }
/* 36 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String mutate(String inputCredentials)
/*    */   {
/* 49 */     if (this.credentialHandlers.isEmpty()) {
/* 50 */       return null;
/*    */     }
/*    */     
/* 53 */     return ((CredentialHandler)this.credentialHandlers.get(0)).mutate(inputCredentials);
/*    */   }
/*    */   
/*    */   public void addCredentialHandler(CredentialHandler handler)
/*    */   {
/* 58 */     this.credentialHandlers.add(handler);
/*    */   }
/*    */   
/*    */   public CredentialHandler[] getCredentialHandlers() {
/* 62 */     return (CredentialHandler[])this.credentialHandlers.toArray(new CredentialHandler[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\NestedCredentialHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */