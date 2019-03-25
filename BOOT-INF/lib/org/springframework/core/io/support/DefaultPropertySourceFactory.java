/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.env.PropertySource;
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
/*    */ public class DefaultPropertySourceFactory
/*    */   implements PropertySourceFactory
/*    */ {
/*    */   public PropertySource<?> createPropertySource(String name, EncodedResource resource)
/*    */     throws IOException
/*    */   {
/* 36 */     return name != null ? new ResourcePropertySource(name, resource) : new ResourcePropertySource(resource);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\support\DefaultPropertySourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */