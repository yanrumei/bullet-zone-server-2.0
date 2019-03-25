/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.env.PropertyResolver;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class JspTemplateAvailabilityProvider
/*    */   implements TemplateAvailabilityProvider
/*    */ {
/*    */   public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*    */   {
/* 39 */     if (ClassUtils.isPresent("org.apache.jasper.compiler.JspConfig", classLoader)) {
/* 40 */       String resourceName = getResourceName(view, environment);
/* 41 */       return resourceLoader.getResource(resourceName).exists();
/*    */     }
/* 43 */     return false;
/*    */   }
/*    */   
/*    */   private String getResourceName(String view, Environment environment) {
/* 47 */     PropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.mvc.view.");
/*    */     
/* 49 */     String prefix = resolver.getProperty("prefix", "");
/*    */     
/* 51 */     String suffix = resolver.getProperty("suffix", "");
/*    */     
/* 53 */     return prefix + view + suffix;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\JspTemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */