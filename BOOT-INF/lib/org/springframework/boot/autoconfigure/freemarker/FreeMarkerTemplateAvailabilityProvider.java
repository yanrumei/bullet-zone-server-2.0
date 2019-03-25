/*    */ package org.springframework.boot.autoconfigure.freemarker;
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
/*    */ public class FreeMarkerTemplateAvailabilityProvider
/*    */   extends PathBasedTemplateAvailabilityProvider
/*    */ {
/*    */   public FreeMarkerTemplateAvailabilityProvider()
/*    */   {
/* 37 */     super("freemarker.template.Configuration", FreeMarkerTemplateAvailabilityProperties.class, "spring.freemarker");
/*    */   }
/*    */   
/*    */ 
/*    */   static final class FreeMarkerTemplateAvailabilityProperties
/*    */     extends PathBasedTemplateAvailabilityProvider.TemplateAvailabilityProperties
/*    */   {
/* 44 */     private List<String> templateLoaderPath = new ArrayList(
/* 45 */       Arrays.asList(new String[] { "classpath:/templates/" }));
/*    */     
/*    */     FreeMarkerTemplateAvailabilityProperties() {
/* 48 */       super(".ftl");
/*    */     }
/*    */     
/*    */ 
/*    */     protected List<String> getLoaderPath()
/*    */     {
/* 54 */       return this.templateLoaderPath;
/*    */     }
/*    */     
/*    */     public List<String> getTemplateLoaderPath() {
/* 58 */       return this.templateLoaderPath;
/*    */     }
/*    */     
/*    */     public void setTemplateLoaderPath(List<String> templateLoaderPath) {
/* 62 */       this.templateLoaderPath = templateLoaderPath;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\freemarker\FreeMarkerTemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */