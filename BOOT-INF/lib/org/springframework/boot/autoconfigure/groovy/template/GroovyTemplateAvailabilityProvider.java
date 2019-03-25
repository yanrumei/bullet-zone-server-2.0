/*    */ package org.springframework.boot.autoconfigure.groovy.template;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.springframework.boot.autoconfigure.template.PathBasedTemplateAvailabilityProvider;
/*    */ import org.springframework.boot.autoconfigure.template.PathBasedTemplateAvailabilityProvider.TemplateAvailabilityProperties;
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
/*    */ public class GroovyTemplateAvailabilityProvider
/*    */   extends PathBasedTemplateAvailabilityProvider
/*    */ {
/*    */   public GroovyTemplateAvailabilityProvider()
/*    */   {
/* 37 */     super("groovy.text.TemplateEngine", GroovyTemplateAvailabilityProperties.class, "spring.groovy.template");
/*    */   }
/*    */   
/*    */ 
/*    */   static final class GroovyTemplateAvailabilityProperties
/*    */     extends PathBasedTemplateAvailabilityProvider.TemplateAvailabilityProperties
/*    */   {
/* 44 */     private List<String> resourceLoaderPath = new ArrayList(
/* 45 */       Arrays.asList(new String[] { "classpath:/templates/" }));
/*    */     
/*    */     GroovyTemplateAvailabilityProperties() {
/* 48 */       super(".tpl");
/*    */     }
/*    */     
/*    */ 
/*    */     protected List<String> getLoaderPath()
/*    */     {
/* 54 */       return this.resourceLoaderPath;
/*    */     }
/*    */     
/*    */     public List<String> getResourceLoaderPath() {
/* 58 */       return this.resourceLoaderPath;
/*    */     }
/*    */     
/*    */     public void setResourceLoaderPath(List<String> resourceLoaderPath) {
/* 62 */       this.resourceLoaderPath = resourceLoaderPath;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\groovy\template\GroovyTemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */