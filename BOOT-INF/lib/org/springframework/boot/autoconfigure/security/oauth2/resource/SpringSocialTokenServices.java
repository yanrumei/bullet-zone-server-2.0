/*    */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*    */ import org.springframework.security.core.AuthenticationException;
/*    */ import org.springframework.security.core.GrantedAuthority;
/*    */ import org.springframework.security.core.authority.AuthorityUtils;
/*    */ import org.springframework.security.oauth2.common.OAuth2AccessToken;
/*    */ import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
/*    */ import org.springframework.security.oauth2.provider.OAuth2Authentication;
/*    */ import org.springframework.security.oauth2.provider.OAuth2Request;
/*    */ import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
/*    */ import org.springframework.social.connect.Connection;
/*    */ import org.springframework.social.connect.UserProfile;
/*    */ import org.springframework.social.connect.support.OAuth2ConnectionFactory;
/*    */ import org.springframework.social.oauth2.AccessGrant;
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
/*    */ public class SpringSocialTokenServices
/*    */   implements ResourceServerTokenServices
/*    */ {
/*    */   private final OAuth2ConnectionFactory<?> connectionFactory;
/*    */   private final String clientId;
/*    */   
/*    */   public SpringSocialTokenServices(OAuth2ConnectionFactory<?> connectionFactory, String clientId)
/*    */   {
/* 49 */     this.connectionFactory = connectionFactory;
/* 50 */     this.clientId = clientId;
/*    */   }
/*    */   
/*    */   public OAuth2Authentication loadAuthentication(String accessToken)
/*    */     throws AuthenticationException, InvalidTokenException
/*    */   {
/* 56 */     AccessGrant accessGrant = new AccessGrant(accessToken);
/* 57 */     Connection<?> connection = this.connectionFactory.createConnection(accessGrant);
/* 58 */     UserProfile user = connection.fetchUserProfile();
/* 59 */     return extractAuthentication(user);
/*    */   }
/*    */   
/*    */   private OAuth2Authentication extractAuthentication(UserProfile user) {
/* 63 */     String principal = user.getUsername();
/*    */     
/* 65 */     List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
/* 66 */     OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null, null, null, null, null);
/*    */     
/* 68 */     return new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken(principal, "N/A", authorities));
/*    */   }
/*    */   
/*    */ 
/*    */   public OAuth2AccessToken readAccessToken(String accessToken)
/*    */   {
/* 74 */     throw new UnsupportedOperationException("Not supported: read access token");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\SpringSocialTokenServices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */