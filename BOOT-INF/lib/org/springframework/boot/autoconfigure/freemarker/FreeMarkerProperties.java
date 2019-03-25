/*    */ package org.springframework.boot.autoconfigure.freemarker;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
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
/*    */ 
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.freemarker")
/*    */ public class FreeMarkerProperties
/*    */   extends AbstractTemplateViewResolverProperties
/*    */ {
/*    */   public static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";
/*    */   public static final String DEFAULT_PREFIX = "";
/*    */   public static final String DEFAULT_SUFFIX = ".ftl";
/* 44 */   private Map<String, String> settings = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 49 */   private String[] templateLoaderPath = { "classpath:/templates/" };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */   private boolean preferFileSystemAccess = true;
/*    */   
/*    */   public FreeMarkerProperties() {
/* 58 */     super("", ".ftl");
/*    */   }
/*    */   
/*    */   public Map<String, String> getSettings() {
/* 62 */     return this.settings;
/*    */   }
/*    */   
/*    */   public void setSettings(Map<String, String> settings) {
/* 66 */     this.settings = settings;
/*    */   }
/*    */   
/*    */   public String[] getTemplateLoaderPath() {
/* 70 */     return this.templateLoaderPath;
/*    */   }
/*    */   
/*    */   public boolean isPreferFileSystemAccess() {
/* 74 */     return this.preferFileSystemAccess;
/*    */   }
/*    */   
/*    */   public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
/* 78 */     this.preferFileSystemAccess = preferFileSystemAccess;
/*    */   }
/*    */   
/*    */   public void setTemplateLoaderPath(String... templateLoaderPaths) {
/* 82 */     this.templateLoaderPath = templateLoaderPaths;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\freemarker\FreeMarkerProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */