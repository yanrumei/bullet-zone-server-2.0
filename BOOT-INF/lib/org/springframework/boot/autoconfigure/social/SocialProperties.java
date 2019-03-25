/*    */ package org.springframework.boot.autoconfigure.social;
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
/*    */ public abstract class SocialProperties
/*    */ {
/*    */   private String appId;
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
/*    */   private String appSecret;
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
/*    */   public String getAppId()
/*    */   {
/* 40 */     return this.appId;
/*    */   }
/*    */   
/*    */   public void setAppId(String appId) {
/* 44 */     this.appId = appId;
/*    */   }
/*    */   
/*    */   public String getAppSecret() {
/* 48 */     return this.appSecret;
/*    */   }
/*    */   
/*    */   public void setAppSecret(String appSecret) {
/* 52 */     this.appSecret = appSecret;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\SocialProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */