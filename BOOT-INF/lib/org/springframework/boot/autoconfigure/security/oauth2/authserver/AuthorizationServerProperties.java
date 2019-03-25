/*    */ package org.springframework.boot.autoconfigure.security.oauth2.authserver;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*    */ @ConfigurationProperties(prefix="security.oauth2.authorization")
/*    */ public class AuthorizationServerProperties
/*    */ {
/*    */   private String checkTokenAccess;
/*    */   private String tokenKeyAccess;
/*    */   private String realm;
/*    */   
/*    */   public String getCheckTokenAccess()
/*    */   {
/* 51 */     return this.checkTokenAccess;
/*    */   }
/*    */   
/*    */   public void setCheckTokenAccess(String checkTokenAccess) {
/* 55 */     this.checkTokenAccess = checkTokenAccess;
/*    */   }
/*    */   
/*    */   public String getTokenKeyAccess() {
/* 59 */     return this.tokenKeyAccess;
/*    */   }
/*    */   
/*    */   public void setTokenKeyAccess(String tokenKeyAccess) {
/* 63 */     this.tokenKeyAccess = tokenKeyAccess;
/*    */   }
/*    */   
/*    */   public String getRealm() {
/* 67 */     return this.realm;
/*    */   }
/*    */   
/*    */   public void setRealm(String realm) {
/* 71 */     this.realm = realm;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\authserver\AuthorizationServerProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */