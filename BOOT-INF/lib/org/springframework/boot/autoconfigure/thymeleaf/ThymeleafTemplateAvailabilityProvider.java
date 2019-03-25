/*    */ package org.springframework.boot.autoconfigure.thymeleaf;
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
/*    */ public class ThymeleafTemplateAvailabilityProvider
/*    */   implements TemplateAvailabilityProvider
/*    */ {
/*    */   public boolean isTemplateAvailable(String view, Environment environment, ClassLoader classLoader, ResourceLoader resourceLoader)
/*    */   {
/* 39 */     if (ClassUtils.isPresent("org.thymeleaf.spring4.SpringTemplateEngine", classLoader))
/*    */     {
/* 41 */       PropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
/*    */       
/* 43 */       String prefix = resolver.getProperty("prefix", "classpath:/templates/");
/*    */       
/* 45 */       String suffix = resolver.getProperty("suffix", ".html");
/*    */       
/* 47 */       return resourceLoader.getResource(prefix + view + suffix).exists();
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\thymeleaf\ThymeleafTemplateAvailabilityProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */