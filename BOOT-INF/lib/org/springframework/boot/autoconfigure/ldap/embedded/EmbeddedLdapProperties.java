/*     */ package org.springframework.boot.autoconfigure.ldap.embedded;
/*     */ 
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ @ConfigurationProperties(prefix="spring.ldap.embedded")
/*     */ public class EmbeddedLdapProperties
/*     */ {
/*  35 */   private int port = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private Credential credential = new Credential();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String baseDn;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private String ldif = "classpath:schema.ldif";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private Validation validation = new Validation();
/*     */   
/*     */   public int getPort() {
/*  58 */     return this.port;
/*     */   }
/*     */   
/*     */   public void setPort(int port) {
/*  62 */     this.port = port;
/*     */   }
/*     */   
/*     */   public Credential getCredential() {
/*  66 */     return this.credential;
/*     */   }
/*     */   
/*     */   public void setCredential(Credential credential) {
/*  70 */     this.credential = credential;
/*     */   }
/*     */   
/*     */   public String getBaseDn() {
/*  74 */     return this.baseDn;
/*     */   }
/*     */   
/*     */   public void setBaseDn(String baseDn) {
/*  78 */     this.baseDn = baseDn;
/*     */   }
/*     */   
/*     */   public String getLdif() {
/*  82 */     return this.ldif;
/*     */   }
/*     */   
/*     */   public void setLdif(String ldif) {
/*  86 */     this.ldif = ldif;
/*     */   }
/*     */   
/*     */   public Validation getValidation() {
/*  90 */     return this.validation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Credential
/*     */   {
/*     */     private String username;
/*     */     
/*     */ 
/*     */     private String password;
/*     */     
/*     */ 
/*     */ 
/*     */     public String getUsername()
/*     */     {
/* 106 */       return this.username;
/*     */     }
/*     */     
/*     */     public void setUsername(String username) {
/* 110 */       this.username = username;
/*     */     }
/*     */     
/*     */     public String getPassword() {
/* 114 */       return this.password;
/*     */     }
/*     */     
/*     */     public void setPassword(String password) {
/* 118 */       this.password = password;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Validation
/*     */   {
/* 128 */     private boolean enabled = true;
/*     */     
/*     */ 
/*     */     private Resource schema;
/*     */     
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 136 */       return this.enabled;
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 140 */       this.enabled = enabled;
/*     */     }
/*     */     
/*     */     public Resource getSchema() {
/* 144 */       return this.schema;
/*     */     }
/*     */     
/*     */     public void setSchema(Resource schema) {
/* 148 */       this.schema = schema;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\ldap\embedded\EmbeddedLdapProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */