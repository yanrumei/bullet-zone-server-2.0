/*    */ package org.apache.catalina.authenticator.jaspic;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.auth.message.AuthException;
/*    */ import javax.security.auth.message.config.AuthConfigFactory;
/*    */ import javax.security.auth.message.config.AuthConfigProvider;
/*    */ import javax.security.auth.message.config.ClientAuthConfig;
/*    */ import javax.security.auth.message.config.ServerAuthConfig;
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
/*    */ public class SimpleAuthConfigProvider
/*    */   implements AuthConfigProvider
/*    */ {
/*    */   private final Map<String, String> properties;
/*    */   private volatile ServerAuthConfig serverAuthConfig;
/*    */   
/*    */   public SimpleAuthConfigProvider(Map<String, String> properties, AuthConfigFactory factory)
/*    */   {
/* 40 */     this.properties = properties;
/* 41 */     if (factory != null) {
/* 42 */       factory.registerConfigProvider(this, null, null, "Automatic registration");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler)
/*    */     throws AuthException
/*    */   {
/* 56 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler)
/*    */     throws AuthException
/*    */   {
/* 63 */     ServerAuthConfig serverAuthConfig = this.serverAuthConfig;
/* 64 */     if (serverAuthConfig == null) {
/* 65 */       synchronized (this) {
/* 66 */         if (this.serverAuthConfig == null) {
/* 67 */           this.serverAuthConfig = createServerAuthConfig(layer, appContext, handler, this.properties);
/*    */         }
/* 69 */         serverAuthConfig = this.serverAuthConfig;
/*    */       }
/*    */     }
/* 72 */     return serverAuthConfig;
/*    */   }
/*    */   
/*    */ 
/*    */   protected ServerAuthConfig createServerAuthConfig(String layer, String appContext, CallbackHandler handler, Map<String, String> properties)
/*    */   {
/* 78 */     return new SimpleServerAuthConfig(layer, appContext, handler, properties);
/*    */   }
/*    */   
/*    */ 
/*    */   public void refresh()
/*    */   {
/* 84 */     ServerAuthConfig serverAuthConfig = this.serverAuthConfig;
/* 85 */     if (serverAuthConfig != null) {
/* 86 */       serverAuthConfig.refresh();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\SimpleAuthConfigProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */