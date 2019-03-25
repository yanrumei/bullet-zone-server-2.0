/*    */ package org.springframework.boot.autoconfigure.groovy.template;
/*    */ 
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
/*    */ @ConfigurationProperties(prefix="spring.groovy.template", ignoreUnknownFields=true)
/*    */ public class GroovyTemplateProperties
/*    */   extends AbstractTemplateViewResolverProperties
/*    */ {
/*    */   public static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:/templates/";
/*    */   public static final String DEFAULT_PREFIX = "";
/*    */   public static final String DEFAULT_SUFFIX = ".tpl";
/*    */   public static final String DEFAULT_REQUEST_CONTEXT_ATTRIBUTE = "spring";
/* 43 */   private String resourceLoaderPath = "classpath:/templates/";
/*    */   
/*    */   public GroovyTemplateProperties() {
/* 46 */     super("", ".tpl");
/* 47 */     setRequestContextAttribute("spring");
/*    */   }
/*    */   
/*    */   public String getResourceLoaderPath() {
/* 51 */     return this.resourceLoaderPath;
/*    */   }
/*    */   
/*    */   public void setResourceLoaderPath(String resourceLoaderPath) {
/* 55 */     this.resourceLoaderPath = resourceLoaderPath;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\groovy\template\GroovyTemplateProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */