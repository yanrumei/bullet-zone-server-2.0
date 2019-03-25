/*    */ package org.apache.catalina.authenticator.jaspic;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.security.auth.Subject;
/*    */ import javax.security.auth.message.AuthException;
/*    */ import javax.security.auth.message.AuthStatus;
/*    */ import javax.security.auth.message.MessageInfo;
/*    */ import javax.security.auth.message.config.ServerAuthContext;
/*    */ import javax.security.auth.message.module.ServerAuthModule;
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
/*    */ public class SimpleServerAuthContext
/*    */   implements ServerAuthContext
/*    */ {
/*    */   private final List<ServerAuthModule> modules;
/*    */   
/*    */   public SimpleServerAuthContext(List<ServerAuthModule> modules)
/*    */   {
/* 40 */     this.modules = modules;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
/*    */     throws AuthException
/*    */   {
/* 48 */     for (int moduleIndex = 0; moduleIndex < this.modules.size(); moduleIndex++) {
/* 49 */       ServerAuthModule module = (ServerAuthModule)this.modules.get(moduleIndex);
/* 50 */       AuthStatus result = module.validateRequest(messageInfo, clientSubject, serviceSubject);
/* 51 */       if (result != AuthStatus.SEND_FAILURE) {
/* 52 */         messageInfo.getMap().put("moduleIndex", Integer.valueOf(moduleIndex));
/* 53 */         return result;
/*    */       }
/*    */     }
/* 56 */     return AuthStatus.SEND_FAILURE;
/*    */   }
/*    */   
/*    */ 
/*    */   public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject)
/*    */     throws AuthException
/*    */   {
/* 63 */     ServerAuthModule module = (ServerAuthModule)this.modules.get(((Integer)messageInfo.getMap().get("moduleIndex")).intValue());
/* 64 */     return module.secureResponse(messageInfo, serviceSubject);
/*    */   }
/*    */   
/*    */   public void cleanSubject(MessageInfo messageInfo, Subject subject)
/*    */     throws AuthException
/*    */   {
/* 70 */     for (ServerAuthModule module : this.modules) {
/* 71 */       module.cleanSubject(messageInfo, subject);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\SimpleServerAuthContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */