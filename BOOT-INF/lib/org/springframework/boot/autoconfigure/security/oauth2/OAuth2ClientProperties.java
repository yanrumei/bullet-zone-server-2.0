/*    */ package org.springframework.boot.autoconfigure.security.oauth2;
/*    */ 
/*    */ import java.util.UUID;
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
/*    */ @ConfigurationProperties(prefix="security.oauth2.client")
/*    */ public class OAuth2ClientProperties
/*    */ {
/*    */   private String clientId;
/* 41 */   private String clientSecret = UUID.randomUUID().toString();
/*    */   
/* 43 */   private boolean defaultSecret = true;
/*    */   
/*    */   public String getClientId() {
/* 46 */     return this.clientId;
/*    */   }
/*    */   
/*    */   public void setClientId(String clientId) {
/* 50 */     this.clientId = clientId;
/*    */   }
/*    */   
/*    */   public String getClientSecret() {
/* 54 */     return this.clientSecret;
/*    */   }
/*    */   
/*    */   public void setClientSecret(String clientSecret) {
/* 58 */     this.clientSecret = clientSecret;
/* 59 */     this.defaultSecret = false;
/*    */   }
/*    */   
/*    */   public boolean isDefaultSecret() {
/* 63 */     return this.defaultSecret;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\OAuth2ClientProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */