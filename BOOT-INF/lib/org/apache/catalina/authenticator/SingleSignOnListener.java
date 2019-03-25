/*    */ package org.apache.catalina.authenticator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.catalina.Authenticator;
/*    */ import org.apache.catalina.Context;
/*    */ import org.apache.catalina.Manager;
/*    */ import org.apache.catalina.Session;
/*    */ import org.apache.catalina.SessionEvent;
/*    */ import org.apache.catalina.SessionListener;
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
/*    */ public class SingleSignOnListener
/*    */   implements SessionListener, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String ssoId;
/*    */   
/*    */   public SingleSignOnListener(String ssoId)
/*    */   {
/* 35 */     this.ssoId = ssoId;
/*    */   }
/*    */   
/*    */ 
/*    */   public void sessionEvent(SessionEvent event)
/*    */   {
/* 41 */     if (!"destroySession".equals(event.getType())) {
/* 42 */       return;
/*    */     }
/*    */     
/* 45 */     Session session = event.getSession();
/* 46 */     Manager manager = session.getManager();
/* 47 */     if (manager == null) {
/* 48 */       return;
/*    */     }
/* 50 */     Context context = manager.getContext();
/* 51 */     Authenticator authenticator = context.getAuthenticator();
/* 52 */     if (!(authenticator instanceof AuthenticatorBase)) {
/* 53 */       return;
/*    */     }
/* 55 */     SingleSignOn sso = ((AuthenticatorBase)authenticator).sso;
/* 56 */     if (sso == null) {
/* 57 */       return;
/*    */     }
/* 59 */     sso.sessionDestroyed(this.ssoId, session);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SingleSignOnListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */