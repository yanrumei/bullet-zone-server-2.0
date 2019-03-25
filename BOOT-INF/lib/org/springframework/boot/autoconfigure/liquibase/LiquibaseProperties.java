/*     */ package org.springframework.boot.autoconfigure.liquibase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="liquibase", ignoreUnknownFields=false)
/*     */ public class LiquibaseProperties
/*     */ {
/*  39 */   private String changeLog = "classpath:/db/changelog/db.changelog-master.yaml";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private boolean checkChangeLogLocation = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String contexts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String defaultSchema;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean dropFirst;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String user;
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
/*     */   private String labels;
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<String, String> parameters;
/*     */   
/*     */ 
/*     */ 
/*     */   private File rollbackFile;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getChangeLog()
/*     */   {
/*  98 */     return this.changeLog;
/*     */   }
/*     */   
/*     */   public void setChangeLog(String changeLog) {
/* 102 */     Assert.notNull(changeLog, "ChangeLog must not be null");
/* 103 */     this.changeLog = changeLog;
/*     */   }
/*     */   
/*     */   public boolean isCheckChangeLogLocation() {
/* 107 */     return this.checkChangeLogLocation;
/*     */   }
/*     */   
/*     */   public void setCheckChangeLogLocation(boolean checkChangeLogLocation) {
/* 111 */     this.checkChangeLogLocation = checkChangeLogLocation;
/*     */   }
/*     */   
/*     */   public String getContexts() {
/* 115 */     return this.contexts;
/*     */   }
/*     */   
/*     */   public void setContexts(String contexts) {
/* 119 */     this.contexts = contexts;
/*     */   }
/*     */   
/*     */   public String getDefaultSchema() {
/* 123 */     return this.defaultSchema;
/*     */   }
/*     */   
/*     */   public void setDefaultSchema(String defaultSchema) {
/* 127 */     this.defaultSchema = defaultSchema;
/*     */   }
/*     */   
/*     */   public boolean isDropFirst() {
/* 131 */     return this.dropFirst;
/*     */   }
/*     */   
/*     */   public void setDropFirst(boolean dropFirst) {
/* 135 */     this.dropFirst = dropFirst;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 139 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 143 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */   public String getUser() {
/* 147 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/* 151 */     this.user = user;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 155 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 159 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getUrl() {
/* 163 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/* 167 */     this.url = url;
/*     */   }
/*     */   
/*     */   public String getLabels() {
/* 171 */     return this.labels;
/*     */   }
/*     */   
/*     */   public void setLabels(String labels) {
/* 175 */     this.labels = labels;
/*     */   }
/*     */   
/*     */   public Map<String, String> getParameters() {
/* 179 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setParameters(Map<String, String> parameters) {
/* 183 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public File getRollbackFile() {
/* 187 */     return this.rollbackFile;
/*     */   }
/*     */   
/*     */   public void setRollbackFile(File rollbackFile) {
/* 191 */     this.rollbackFile = rollbackFile;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\liquibase\LiquibaseProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */