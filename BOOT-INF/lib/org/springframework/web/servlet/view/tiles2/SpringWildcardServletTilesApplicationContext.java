/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.context.support.ServletContextResourcePatternResolver;
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
/*    */ public class SpringWildcardServletTilesApplicationContext
/*    */   extends ServletTilesApplicationContext
/*    */ {
/*    */   private final ResourcePatternResolver resolver;
/*    */   
/*    */   public SpringWildcardServletTilesApplicationContext(ServletContext servletContext)
/*    */   {
/* 50 */     super(servletContext);
/* 51 */     this.resolver = new ServletContextResourcePatternResolver(servletContext);
/*    */   }
/*    */   
/*    */   public URL getResource(String path)
/*    */     throws IOException
/*    */   {
/* 57 */     Set<URL> urlSet = getResources(path);
/* 58 */     if (!CollectionUtils.isEmpty(urlSet)) {
/* 59 */       return (URL)urlSet.iterator().next();
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public Set<URL> getResources(String path) throws IOException
/*    */   {
/* 66 */     Set<URL> urlSet = null;
/* 67 */     Resource[] resources = this.resolver.getResources(path);
/* 68 */     if (!ObjectUtils.isEmpty(resources)) {
/* 69 */       urlSet = new LinkedHashSet(resources.length);
/* 70 */       for (Resource resource : resources) {
/* 71 */         urlSet.add(resource.getURL());
/*    */       }
/*    */     }
/* 74 */     return urlSet;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\SpringWildcardServletTilesApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */