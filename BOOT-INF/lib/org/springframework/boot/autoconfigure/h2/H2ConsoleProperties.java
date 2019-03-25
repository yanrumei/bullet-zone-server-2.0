/*    */ package org.springframework.boot.autoconfigure.h2;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.util.Assert;
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
/*    */ @ConfigurationProperties(prefix="spring.h2.console")
/*    */ public class H2ConsoleProperties
/*    */ {
/* 36 */   private String path = "/h2-console";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 41 */   private boolean enabled = false;
/*    */   
/* 43 */   private final Settings settings = new Settings();
/*    */   
/*    */   public String getPath() {
/* 46 */     return this.path;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 50 */     Assert.notNull(path, "Path must not be null");
/* 51 */     Assert.isTrue((path.isEmpty()) || (path.startsWith("/")), "Path must start with / or be empty");
/*    */     
/* 53 */     this.path = path;
/*    */   }
/*    */   
/*    */   public boolean getEnabled() {
/* 57 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setEnabled(boolean enabled) {
/* 61 */     this.enabled = enabled;
/*    */   }
/*    */   
/*    */   public Settings getSettings() {
/* 65 */     return this.settings;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Settings
/*    */   {
/* 73 */     private boolean trace = false;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 78 */     private boolean webAllowOthers = false;
/*    */     
/*    */     public boolean isTrace() {
/* 81 */       return this.trace;
/*    */     }
/*    */     
/*    */     public void setTrace(boolean trace) {
/* 85 */       this.trace = trace;
/*    */     }
/*    */     
/*    */     public boolean isWebAllowOthers() {
/* 89 */       return this.webAllowOthers;
/*    */     }
/*    */     
/*    */     public void setWebAllowOthers(boolean webAllowOthers) {
/* 93 */       this.webAllowOthers = webAllowOthers;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\h2\H2ConsoleProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */