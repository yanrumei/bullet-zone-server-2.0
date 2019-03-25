/*     */ package org.springframework.boot.autoconfigure.flyway;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ @ConfigurationProperties(prefix="flyway", ignoreUnknownFields=true)
/*     */ public class FlywayProperties
/*     */ {
/*  43 */   private List<String> locations = new ArrayList(
/*  44 */     Collections.singletonList("db/migration"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private boolean checkLocation = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String user;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String password;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String url;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private List<String> initSqls = new ArrayList();
/*     */   
/*     */   public void setLocations(List<String> locations) {
/*  79 */     this.locations = locations;
/*     */   }
/*     */   
/*     */   public List<String> getLocations() {
/*  83 */     return this.locations;
/*     */   }
/*     */   
/*     */   public void setCheckLocation(boolean checkLocation) {
/*  87 */     this.checkLocation = checkLocation;
/*     */   }
/*     */   
/*     */   public boolean isCheckLocation() {
/*  91 */     return this.checkLocation;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  95 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/*  99 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public String getUser() {
/* 103 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/* 107 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 111 */     return this.password == null ? "" : this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 115 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getUrl() {
/* 119 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/* 123 */     this.url = url;
/*     */   }
/*     */   
/*     */   public List<String> getInitSqls() {
/* 127 */     return this.initSqls;
/*     */   }
/*     */   
/*     */   public void setInitSqls(List<String> initSqls) {
/* 131 */     this.initSqls = initSqls;
/*     */   }
/*     */   
/*     */   public boolean isCreateDataSource() {
/* 135 */     return (this.url != null) && (this.user != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\flyway\FlywayProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */