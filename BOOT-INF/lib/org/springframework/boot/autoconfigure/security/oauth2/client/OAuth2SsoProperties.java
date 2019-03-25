/*    */ package org.springframework.boot.autoconfigure.security.oauth2.client;
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
/*    */ @ConfigurationProperties(prefix="security.oauth2.sso")
/*    */ public class OAuth2SsoProperties
/*    */ {
/*    */   public static final String DEFAULT_LOGIN_PATH = "/login";
/* 36 */   private String loginPath = "/login";
/*    */   
/*    */ 
/*    */   private Integer filterOrder;
/*    */   
/*    */ 
/*    */ 
/*    */   public String getLoginPath()
/*    */   {
/* 45 */     return this.loginPath;
/*    */   }
/*    */   
/*    */   public void setLoginPath(String loginPath) {
/* 49 */     this.loginPath = loginPath;
/*    */   }
/*    */   
/*    */   public Integer getFilterOrder() {
/* 53 */     return this.filterOrder;
/*    */   }
/*    */   
/*    */   public void setFilterOrder(Integer filterOrder) {
/* 57 */     this.filterOrder = filterOrder;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\OAuth2SsoProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */