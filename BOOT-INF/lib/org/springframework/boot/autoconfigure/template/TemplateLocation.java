/*    */ package org.springframework.boot.autoconfigure.template;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.support.ResourcePatternResolver;
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
/*    */ public class TemplateLocation
/*    */ {
/*    */   private final String path;
/*    */   
/*    */   public TemplateLocation(String path)
/*    */   {
/* 37 */     Assert.notNull(path, "Path must not be null");
/* 38 */     this.path = path;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean exists(ResourcePatternResolver resolver)
/*    */   {
/* 48 */     Assert.notNull(resolver, "Resolver must not be null");
/* 49 */     if (resolver.getResource(this.path).exists()) {
/* 50 */       return true;
/*    */     }
/*    */     try {
/* 53 */       return anyExists(resolver);
/*    */     }
/*    */     catch (IOException ex) {}
/* 56 */     return false;
/*    */   }
/*    */   
/*    */   private boolean anyExists(ResourcePatternResolver resolver) throws IOException
/*    */   {
/* 61 */     String searchPath = this.path;
/* 62 */     if (searchPath.startsWith("classpath:"))
/*    */     {
/* 64 */       searchPath = "classpath*:" + searchPath.substring("classpath:".length());
/*    */     }
/* 66 */     if (searchPath.startsWith("classpath*:")) {
/* 67 */       Resource[] resources = resolver.getResources(searchPath);
/* 68 */       for (Resource resource : resources) {
/* 69 */         if (resource.exists()) {
/* 70 */           return true;
/*    */         }
/*    */       }
/*    */     }
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 79 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\template\TemplateLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */