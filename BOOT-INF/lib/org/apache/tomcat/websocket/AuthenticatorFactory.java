/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AuthenticatorFactory
/*    */ {
/*    */   public static Authenticator getAuthenticator(String authScheme)
/*    */   {
/* 35 */     Authenticator auth = null;
/* 36 */     switch (authScheme.toLowerCase())
/*    */     {
/*    */     case "basic": 
/* 39 */       auth = new BasicAuthenticator();
/* 40 */       break;
/*    */     
/*    */     case "digest": 
/* 43 */       auth = new DigestAuthenticator();
/* 44 */       break;
/*    */     
/*    */     default: 
/* 47 */       auth = loadAuthenticators(authScheme);
/*    */     }
/*    */     
/*    */     
/* 51 */     return auth;
/*    */   }
/*    */   
/*    */   private static Authenticator loadAuthenticators(String authScheme)
/*    */   {
/* 56 */     ServiceLoader<Authenticator> serviceLoader = ServiceLoader.load(Authenticator.class);
/* 57 */     Iterator<Authenticator> auths = serviceLoader.iterator();
/*    */     
/* 59 */     while (auths.hasNext()) {
/* 60 */       Authenticator auth = (Authenticator)auths.next();
/* 61 */       if (auth.getSchemeName().equalsIgnoreCase(authScheme)) {
/* 62 */         return auth;
/*    */       }
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\AuthenticatorFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */