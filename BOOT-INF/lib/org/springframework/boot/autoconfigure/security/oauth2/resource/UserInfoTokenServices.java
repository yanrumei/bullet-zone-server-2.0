/*     */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*     */ import org.springframework.security.core.AuthenticationException;
/*     */ import org.springframework.security.core.GrantedAuthority;
/*     */ import org.springframework.security.oauth2.client.OAuth2ClientContext;
/*     */ import org.springframework.security.oauth2.client.OAuth2RestOperations;
/*     */ import org.springframework.security.oauth2.client.OAuth2RestTemplate;
/*     */ import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
/*     */ import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
/*     */ import org.springframework.security.oauth2.common.OAuth2AccessToken;
/*     */ import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
/*     */ import org.springframework.security.oauth2.provider.OAuth2Authentication;
/*     */ import org.springframework.security.oauth2.provider.OAuth2Request;
/*     */ import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserInfoTokenServices
/*     */   implements ResourceServerTokenServices
/*     */ {
/*  48 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final String userInfoEndpointUrl;
/*     */   
/*     */   private final String clientId;
/*     */   
/*     */   private OAuth2RestOperations restTemplate;
/*     */   
/*  56 */   private String tokenType = "Bearer";
/*     */   
/*  58 */   private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();
/*     */   
/*  60 */   private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();
/*     */   
/*     */   public UserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
/*  63 */     this.userInfoEndpointUrl = userInfoEndpointUrl;
/*  64 */     this.clientId = clientId;
/*     */   }
/*     */   
/*     */   public void setTokenType(String tokenType) {
/*  68 */     this.tokenType = tokenType;
/*     */   }
/*     */   
/*     */   public void setRestTemplate(OAuth2RestOperations restTemplate) {
/*  72 */     this.restTemplate = restTemplate;
/*     */   }
/*     */   
/*     */   public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
/*  76 */     Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
/*  77 */     this.authoritiesExtractor = authoritiesExtractor;
/*     */   }
/*     */   
/*     */   public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
/*  81 */     Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");
/*  82 */     this.principalExtractor = principalExtractor;
/*     */   }
/*     */   
/*     */   public OAuth2Authentication loadAuthentication(String accessToken)
/*     */     throws AuthenticationException, InvalidTokenException
/*     */   {
/*  88 */     Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
/*  89 */     if (map.containsKey("error")) {
/*  90 */       if (this.logger.isDebugEnabled()) {
/*  91 */         this.logger.debug("userinfo returned error: " + map.get("error"));
/*     */       }
/*  93 */       throw new InvalidTokenException(accessToken);
/*     */     }
/*  95 */     return extractAuthentication(map);
/*     */   }
/*     */   
/*     */   private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
/*  99 */     Object principal = getPrincipal(map);
/*     */     
/* 101 */     List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
/* 102 */     OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null, null, null, null, null);
/*     */     
/* 104 */     UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
/*     */     
/* 106 */     token.setDetails(map);
/* 107 */     return new OAuth2Authentication(request, token);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getPrincipal(Map<String, Object> map)
/*     */   {
/* 117 */     Object principal = this.principalExtractor.extractPrincipal(map);
/* 118 */     return principal == null ? "unknown" : principal;
/*     */   }
/*     */   
/*     */   public OAuth2AccessToken readAccessToken(String accessToken)
/*     */   {
/* 123 */     throw new UnsupportedOperationException("Not supported: read access token");
/*     */   }
/*     */   
/*     */   private Map<String, Object> getMap(String path, String accessToken)
/*     */   {
/* 128 */     if (this.logger.isDebugEnabled()) {
/* 129 */       this.logger.debug("Getting user info from: " + path);
/*     */     }
/*     */     try {
/* 132 */       OAuth2RestOperations restTemplate = this.restTemplate;
/* 133 */       if (restTemplate == null) {
/* 134 */         BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
/* 135 */         resource.setClientId(this.clientId);
/* 136 */         restTemplate = new OAuth2RestTemplate(resource);
/*     */       }
/*     */       
/* 139 */       OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
/* 140 */       if ((existingToken == null) || (!accessToken.equals(existingToken.getValue()))) {
/* 141 */         DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
/*     */         
/* 143 */         token.setTokenType(this.tokenType);
/* 144 */         restTemplate.getOAuth2ClientContext().setAccessToken(token);
/*     */       }
/* 146 */       return (Map)restTemplate.getForEntity(path, Map.class, new Object[0]).getBody();
/*     */     }
/*     */     catch (Exception ex) {
/* 149 */       this.logger.warn("Could not fetch user details: " + ex.getClass() + ", " + ex
/* 150 */         .getMessage()); }
/* 151 */     return Collections.singletonMap("error", "Could not fetch user details");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\UserInfoTokenServices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */