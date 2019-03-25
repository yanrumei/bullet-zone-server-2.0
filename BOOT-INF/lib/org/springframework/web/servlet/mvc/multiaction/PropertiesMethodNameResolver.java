/*    */ package org.springframework.web.servlet.mvc.multiaction;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Properties;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.util.AntPathMatcher;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.PathMatcher;
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
/*    */ @Deprecated
/*    */ public class PropertiesMethodNameResolver
/*    */   extends AbstractUrlMethodNameResolver
/*    */   implements InitializingBean
/*    */ {
/*    */   private Properties mappings;
/* 56 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMappings(Properties mappings)
/*    */   {
/* 64 */     this.mappings = mappings;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPathMatcher(PathMatcher pathMatcher)
/*    */   {
/* 73 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 74 */     this.pathMatcher = pathMatcher;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 79 */     if ((this.mappings == null) || (this.mappings.isEmpty())) {
/* 80 */       throw new IllegalArgumentException("'mappings' property is required");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected String getHandlerMethodNameForUrlPath(String urlPath)
/*    */   {
/* 87 */     String methodName = this.mappings.getProperty(urlPath);
/* 88 */     if (methodName != null) {
/* 89 */       return methodName;
/*    */     }
/* 91 */     Enumeration<?> propNames = this.mappings.propertyNames();
/* 92 */     while (propNames.hasMoreElements()) {
/* 93 */       String registeredPath = (String)propNames.nextElement();
/* 94 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 95 */         return (String)this.mappings.get(registeredPath);
/*    */       }
/*    */     }
/* 98 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\PropertiesMethodNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */