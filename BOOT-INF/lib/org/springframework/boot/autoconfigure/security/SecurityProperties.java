/*     */ package org.springframework.boot.autoconfigure.security;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.security.config.http.SessionCreationPolicy;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ @ConfigurationProperties(prefix="security")
/*     */ public class SecurityProperties
/*     */   implements SecurityPrerequisite
/*     */ {
/*     */   public static final int ACCESS_OVERRIDE_ORDER = 2147483640;
/*     */   public static final int BASIC_AUTH_ORDER = 2147483642;
/*     */   public static final int IGNORED_ORDER = Integer.MIN_VALUE;
/*     */   public static final int DEFAULT_FILTER_ORDER = -100;
/*     */   private boolean requireSsl;
/*  79 */   private boolean enableCsrf = false;
/*     */   
/*  81 */   private Basic basic = new Basic();
/*     */   
/*  83 */   private final Headers headers = new Headers();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  88 */   private SessionCreationPolicy sessions = SessionCreationPolicy.STATELESS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private List<String> ignored = new ArrayList();
/*     */   
/*  95 */   private final User user = new User();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private int filterOrder = -100;
/*     */   
/*     */ 
/*     */   private Set<String> filterDispatcherTypes;
/*     */   
/*     */ 
/*     */   public Headers getHeaders()
/*     */   {
/* 108 */     return this.headers;
/*     */   }
/*     */   
/*     */   public User getUser() {
/* 112 */     return this.user;
/*     */   }
/*     */   
/*     */   public SessionCreationPolicy getSessions() {
/* 116 */     return this.sessions;
/*     */   }
/*     */   
/*     */   public void setSessions(SessionCreationPolicy sessions) {
/* 120 */     this.sessions = sessions;
/*     */   }
/*     */   
/*     */   public Basic getBasic() {
/* 124 */     return this.basic;
/*     */   }
/*     */   
/*     */   public void setBasic(Basic basic) {
/* 128 */     this.basic = basic;
/*     */   }
/*     */   
/*     */   public boolean isRequireSsl() {
/* 132 */     return this.requireSsl;
/*     */   }
/*     */   
/*     */   public void setRequireSsl(boolean requireSsl) {
/* 136 */     this.requireSsl = requireSsl;
/*     */   }
/*     */   
/*     */   public boolean isEnableCsrf() {
/* 140 */     return this.enableCsrf;
/*     */   }
/*     */   
/*     */   public void setEnableCsrf(boolean enableCsrf) {
/* 144 */     this.enableCsrf = enableCsrf;
/*     */   }
/*     */   
/*     */   public void setIgnored(List<String> ignored) {
/* 148 */     this.ignored = new ArrayList(ignored);
/*     */   }
/*     */   
/*     */   public List<String> getIgnored() {
/* 152 */     return this.ignored;
/*     */   }
/*     */   
/*     */   public int getFilterOrder() {
/* 156 */     return this.filterOrder;
/*     */   }
/*     */   
/*     */   public void setFilterOrder(int filterOrder) {
/* 160 */     this.filterOrder = filterOrder;
/*     */   }
/*     */   
/*     */   public Set<String> getFilterDispatcherTypes() {
/* 164 */     return this.filterDispatcherTypes;
/*     */   }
/*     */   
/*     */   public void setFilterDispatcherTypes(Set<String> filterDispatcherTypes) {
/* 168 */     this.filterDispatcherTypes = filterDispatcherTypes;
/*     */   }
/*     */   
/*     */   public static class Headers
/*     */   {
/*     */     public static enum HSTS
/*     */     {
/* 175 */       NONE,  DOMAIN,  ALL;
/*     */       
/*     */ 
/*     */       private HSTS() {}
/*     */     }
/*     */     
/*     */ 
/*     */     public static enum ContentSecurityPolicyMode
/*     */     {
/* 184 */       DEFAULT, 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 189 */       REPORT_ONLY;
/*     */       
/*     */ 
/*     */       private ContentSecurityPolicyMode() {}
/*     */     }
/*     */     
/*     */ 
/* 196 */     private boolean xss = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 201 */     private boolean cache = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 206 */     private boolean frame = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 211 */     private boolean contentType = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private String contentSecurityPolicy;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 221 */     private ContentSecurityPolicyMode contentSecurityPolicyMode = ContentSecurityPolicyMode.DEFAULT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 226 */     private HSTS hsts = HSTS.ALL;
/*     */     
/*     */     public boolean isXss() {
/* 229 */       return this.xss;
/*     */     }
/*     */     
/*     */     public void setXss(boolean xss) {
/* 233 */       this.xss = xss;
/*     */     }
/*     */     
/*     */     public boolean isCache() {
/* 237 */       return this.cache;
/*     */     }
/*     */     
/*     */     public void setCache(boolean cache) {
/* 241 */       this.cache = cache;
/*     */     }
/*     */     
/*     */     public boolean isFrame() {
/* 245 */       return this.frame;
/*     */     }
/*     */     
/*     */     public void setFrame(boolean frame) {
/* 249 */       this.frame = frame;
/*     */     }
/*     */     
/*     */     public boolean isContentType() {
/* 253 */       return this.contentType;
/*     */     }
/*     */     
/*     */     public void setContentType(boolean contentType) {
/* 257 */       this.contentType = contentType;
/*     */     }
/*     */     
/*     */     public String getContentSecurityPolicy() {
/* 261 */       return this.contentSecurityPolicy;
/*     */     }
/*     */     
/*     */     public void setContentSecurityPolicy(String contentSecurityPolicy) {
/* 265 */       this.contentSecurityPolicy = contentSecurityPolicy;
/*     */     }
/*     */     
/*     */     public ContentSecurityPolicyMode getContentSecurityPolicyMode() {
/* 269 */       return this.contentSecurityPolicyMode;
/*     */     }
/*     */     
/*     */     public void setContentSecurityPolicyMode(ContentSecurityPolicyMode contentSecurityPolicyMode)
/*     */     {
/* 274 */       this.contentSecurityPolicyMode = contentSecurityPolicyMode;
/*     */     }
/*     */     
/*     */     public HSTS getHsts() {
/* 278 */       return this.hsts;
/*     */     }
/*     */     
/*     */     public void setHsts(HSTS hsts) {
/* 282 */       this.hsts = hsts;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Basic
/*     */   {
/* 292 */     private boolean enabled = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 297 */     private String realm = "Spring";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 302 */     private String[] path = { "/**" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 307 */     private SecurityAuthorizeMode authorizeMode = SecurityAuthorizeMode.ROLE;
/*     */     
/*     */     public boolean isEnabled() {
/* 310 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 314 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public String getRealm() {
/* 318 */       return this.realm;
/*     */     }
/*     */     
/*     */     public void setRealm(String realm) {
/* 322 */       this.realm = realm;
/*     */     }
/*     */     
/*     */     public String[] getPath() {
/* 326 */       return this.path;
/*     */     }
/*     */     
/*     */     public void setPath(String... paths) {
/* 330 */       this.path = paths;
/*     */     }
/*     */     
/*     */     public SecurityAuthorizeMode getAuthorizeMode() {
/* 334 */       return this.authorizeMode;
/*     */     }
/*     */     
/*     */     public void setAuthorizeMode(SecurityAuthorizeMode authorizeMode) {
/* 338 */       this.authorizeMode = authorizeMode;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class User
/*     */   {
/* 348 */     private String name = "user";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 353 */     private String password = UUID.randomUUID().toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 358 */     private List<String> role = new ArrayList(
/* 359 */       Collections.singletonList("USER"));
/*     */     
/* 361 */     private boolean defaultPassword = true;
/*     */     
/*     */     public String getName() {
/* 364 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 368 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getPassword() {
/* 372 */       return this.password;
/*     */     }
/*     */     
/*     */     public void setPassword(String password) {
/* 376 */       if (((password.startsWith("${")) && (password.endsWith("}"))) || 
/* 377 */         (!StringUtils.hasLength(password))) {
/* 378 */         return;
/*     */       }
/* 380 */       this.defaultPassword = false;
/* 381 */       this.password = password;
/*     */     }
/*     */     
/*     */     public List<String> getRole() {
/* 385 */       return this.role;
/*     */     }
/*     */     
/*     */     public void setRole(List<String> role) {
/* 389 */       this.role = new ArrayList(role);
/*     */     }
/*     */     
/*     */     public boolean isDefaultPassword() {
/* 393 */       return this.defaultPassword;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\SecurityProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */