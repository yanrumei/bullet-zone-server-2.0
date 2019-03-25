/*    */ package org.apache.coyote;
/*    */ 
/*    */ import javax.servlet.http.HttpUpgradeHandler;
/*    */ import org.apache.tomcat.ContextBind;
/*    */ import org.apache.tomcat.InstanceManager;
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
/*    */ public final class UpgradeToken
/*    */ {
/*    */   private final ContextBind contextBind;
/*    */   private final HttpUpgradeHandler httpUpgradeHandler;
/*    */   private final InstanceManager instanceManager;
/*    */   
/*    */   public UpgradeToken(HttpUpgradeHandler httpUpgradeHandler, ContextBind contextBind, InstanceManager instanceManager)
/*    */   {
/* 36 */     this.contextBind = contextBind;
/* 37 */     this.httpUpgradeHandler = httpUpgradeHandler;
/* 38 */     this.instanceManager = instanceManager;
/*    */   }
/*    */   
/*    */   public final ContextBind getContextBind() {
/* 42 */     return this.contextBind;
/*    */   }
/*    */   
/*    */   public final HttpUpgradeHandler getHttpUpgradeHandler() {
/* 46 */     return this.httpUpgradeHandler;
/*    */   }
/*    */   
/*    */   public final InstanceManager getInstanceManager() {
/* 50 */     return this.instanceManager;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\UpgradeToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */