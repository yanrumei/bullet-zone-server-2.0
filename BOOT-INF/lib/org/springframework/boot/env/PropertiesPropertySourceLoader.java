/*    */ package org.springframework.boot.env;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ import org.springframework.core.env.PropertiesPropertySource;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
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
/*    */ public class PropertiesPropertySourceLoader
/*    */   implements PropertySourceLoader
/*    */ {
/*    */   public String[] getFileExtensions()
/*    */   {
/* 37 */     return new String[] { "properties", "xml" };
/*    */   }
/*    */   
/*    */   public PropertySource<?> load(String name, Resource resource, String profile)
/*    */     throws IOException
/*    */   {
/* 43 */     if (profile == null) {
/* 44 */       Properties properties = PropertiesLoaderUtils.loadProperties(resource);
/* 45 */       if (!properties.isEmpty()) {
/* 46 */         return new PropertiesPropertySource(name, properties);
/*    */       }
/*    */     }
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\PropertiesPropertySourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */