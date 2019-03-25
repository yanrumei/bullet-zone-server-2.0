/*     */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.Validator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="security.oauth2.resource")
/*     */ public class ResourceServerProperties
/*     */   implements Validator, BeanFactoryAware
/*     */ {
/*     */   @JsonIgnore
/*     */   private final String clientId;
/*     */   @JsonIgnore
/*     */   private final String clientSecret;
/*     */   @JsonIgnore
/*     */   private ListableBeanFactory beanFactory;
/*  53 */   private String serviceId = "resource";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String id;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String userInfoUri;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String tokenInfoUri;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private boolean preferTokenInfo = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   private String tokenType = "Bearer";
/*     */   
/*  80 */   private Jwt jwt = new Jwt();
/*     */   
/*  82 */   private Jwk jwk = new Jwk();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   private int filterOrder = 2147483639;
/*     */   
/*     */   public ResourceServerProperties() {
/*  91 */     this(null, null);
/*     */   }
/*     */   
/*     */   public ResourceServerProperties(String clientId, String clientSecret) {
/*  95 */     this.clientId = clientId;
/*  96 */     this.clientSecret = clientSecret;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 101 */     this.beanFactory = ((ListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */   public String getResourceId() {
/* 105 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getServiceId() {
/* 109 */     return this.serviceId;
/*     */   }
/*     */   
/*     */   public void setServiceId(String serviceId) {
/* 113 */     this.serviceId = serviceId;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 117 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 121 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getUserInfoUri() {
/* 125 */     return this.userInfoUri;
/*     */   }
/*     */   
/*     */   public void setUserInfoUri(String userInfoUri) {
/* 129 */     this.userInfoUri = userInfoUri;
/*     */   }
/*     */   
/*     */   public String getTokenInfoUri() {
/* 133 */     return this.tokenInfoUri;
/*     */   }
/*     */   
/*     */   public void setTokenInfoUri(String tokenInfoUri) {
/* 137 */     this.tokenInfoUri = tokenInfoUri;
/*     */   }
/*     */   
/*     */   public boolean isPreferTokenInfo() {
/* 141 */     return this.preferTokenInfo;
/*     */   }
/*     */   
/*     */   public void setPreferTokenInfo(boolean preferTokenInfo) {
/* 145 */     this.preferTokenInfo = preferTokenInfo;
/*     */   }
/*     */   
/*     */   public String getTokenType() {
/* 149 */     return this.tokenType;
/*     */   }
/*     */   
/*     */   public void setTokenType(String tokenType) {
/* 153 */     this.tokenType = tokenType;
/*     */   }
/*     */   
/*     */   public Jwt getJwt() {
/* 157 */     return this.jwt;
/*     */   }
/*     */   
/*     */   public void setJwt(Jwt jwt) {
/* 161 */     this.jwt = jwt;
/*     */   }
/*     */   
/*     */   public Jwk getJwk() {
/* 165 */     return this.jwk;
/*     */   }
/*     */   
/*     */   public void setJwk(Jwk jwk) {
/* 169 */     this.jwk = jwk;
/*     */   }
/*     */   
/*     */   public String getClientId() {
/* 173 */     return this.clientId;
/*     */   }
/*     */   
/*     */   public String getClientSecret() {
/* 177 */     return this.clientSecret;
/*     */   }
/*     */   
/*     */   public int getFilterOrder() {
/* 181 */     return this.filterOrder;
/*     */   }
/*     */   
/*     */   public void setFilterOrder(int filterOrder) {
/* 185 */     this.filterOrder = filterOrder;
/*     */   }
/*     */   
/*     */   public boolean supports(Class<?> clazz)
/*     */   {
/* 190 */     return ResourceServerProperties.class.isAssignableFrom(clazz);
/*     */   }
/*     */   
/*     */   public void validate(Object target, Errors errors)
/*     */   {
/* 195 */     if (countBeans(AuthorizationServerEndpointsConfiguration.class) > 0)
/*     */     {
/*     */ 
/* 198 */       return;
/*     */     }
/* 200 */     if (countBeans(ResourceServerTokenServicesConfiguration.class) == 0)
/*     */     {
/*     */ 
/* 203 */       return;
/*     */     }
/* 205 */     ResourceServerProperties resource = (ResourceServerProperties)target;
/* 206 */     validate(resource, errors);
/*     */   }
/*     */   
/*     */   private void validate(ResourceServerProperties target, Errors errors) {
/* 210 */     if (!StringUtils.hasText(this.clientId)) {
/* 211 */       return;
/*     */     }
/*     */     
/* 214 */     boolean jwtConfigPresent = (StringUtils.hasText(this.jwt.getKeyUri())) || (StringUtils.hasText(this.jwt.getKeyValue()));
/* 215 */     boolean jwkConfigPresent = StringUtils.hasText(this.jwk.getKeySetUri());
/*     */     
/* 217 */     if ((jwtConfigPresent) && (jwkConfigPresent)) {
/* 218 */       errors.reject("ambiguous.keyUri", "Only one of jwt.keyUri (or jwt.keyValue) and jwk.keySetUri should be configured.");
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 223 */       if ((jwtConfigPresent) || (jwkConfigPresent))
/*     */       {
/* 225 */         return;
/*     */       }
/* 227 */       if ((!StringUtils.hasText(target.getUserInfoUri())) && 
/* 228 */         (!StringUtils.hasText(target.getTokenInfoUri()))) {
/* 229 */         errors.rejectValue("tokenInfoUri", "missing.tokenInfoUri", "Missing tokenInfoUri and userInfoUri and there is no JWT verifier key");
/*     */       }
/*     */       
/*     */ 
/* 233 */       if ((StringUtils.hasText(target.getTokenInfoUri())) && (isPreferTokenInfo()) && 
/* 234 */         (!StringUtils.hasText(this.clientSecret))) {
/* 235 */         errors.rejectValue("clientSecret", "missing.clientSecret", "Missing client secret");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private int countBeans(Class<?> type)
/*     */   {
/* 243 */     return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, type, true, false).length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class Jwt
/*     */   {
/*     */     private String keyValue;
/*     */     
/*     */ 
/*     */     private String keyUri;
/*     */     
/*     */ 
/*     */ 
/*     */     public Jwt() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public String getKeyValue()
/*     */     {
/* 263 */       return this.keyValue;
/*     */     }
/*     */     
/*     */     public void setKeyValue(String keyValue) {
/* 267 */       this.keyValue = keyValue;
/*     */     }
/*     */     
/*     */     public void setKeyUri(String keyUri) {
/* 271 */       this.keyUri = keyUri;
/*     */     }
/*     */     
/*     */     public String getKeyUri() {
/* 275 */       return this.keyUri;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public class Jwk
/*     */   {
/*     */     private String keySetUri;
/*     */     
/*     */ 
/*     */     public Jwk() {}
/*     */     
/*     */     public String getKeySetUri()
/*     */     {
/* 289 */       return this.keySetUri;
/*     */     }
/*     */     
/*     */     public void setKeySetUri(String keySetUri) {
/* 293 */       this.keySetUri = keySetUri;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\ResourceServerProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */