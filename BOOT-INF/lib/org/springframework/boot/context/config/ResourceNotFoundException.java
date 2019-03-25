/*    */ package org.springframework.boot.context.config;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class ResourceNotFoundException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final String propertyName;
/*    */   private final Resource resource;
/*    */   
/*    */   public ResourceNotFoundException(String propertyName, Resource resource)
/*    */   {
/* 35 */     super(String.format("%s defined by '%s' does not exist", new Object[] { resource, propertyName }));
/* 36 */     this.propertyName = propertyName;
/* 37 */     this.resource = resource;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 45 */     return this.propertyName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Resource getResource()
/*    */   {
/* 53 */     return this.resource;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\ResourceNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */