/*     */ package org.springframework.boot.autoconfigure.ldap;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ @ConfigurationProperties(prefix="spring.ldap")
/*     */ public class LdapProperties
/*     */ {
/*     */   private static final int DEFAULT_PORT = 389;
/*     */   private String[] urls;
/*     */   private String base;
/*     */   private String username;
/*     */   private String password;
/*  61 */   private Map<String, String> baseEnvironment = new HashMap();
/*     */   
/*     */   public String[] getUrls() {
/*  64 */     return this.urls;
/*     */   }
/*     */   
/*     */   public void setUrls(String[] urls) {
/*  68 */     this.urls = urls;
/*     */   }
/*     */   
/*     */   public String getBase() {
/*  72 */     return this.base;
/*     */   }
/*     */   
/*     */   public void setBase(String base) {
/*  76 */     this.base = base;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  80 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  84 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  88 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  92 */     this.password = password;
/*     */   }
/*     */   
/*     */   public Map<String, String> getBaseEnvironment() {
/*  96 */     return this.baseEnvironment;
/*     */   }
/*     */   
/*     */   public void setBaseEnvironment(Map<String, String> baseEnvironment) {
/* 100 */     this.baseEnvironment = baseEnvironment;
/*     */   }
/*     */   
/*     */   public String[] determineUrls(Environment environment) {
/* 104 */     if (ObjectUtils.isEmpty(this.urls)) {
/* 105 */       return new String[] { "ldap://localhost:" + determinePort(environment) };
/*     */     }
/* 107 */     return this.urls;
/*     */   }
/*     */   
/*     */   private int determinePort(Environment environment) {
/* 111 */     Assert.notNull(environment, "Environment must not be null");
/* 112 */     String localPort = environment.getProperty("local.ldap.port");
/* 113 */     if (localPort != null) {
/* 114 */       return Integer.valueOf(localPort).intValue();
/*     */     }
/* 116 */     return 389;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\ldap\LdapProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */