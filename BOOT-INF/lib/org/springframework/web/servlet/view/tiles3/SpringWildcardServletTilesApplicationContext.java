/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Locale;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.tiles.request.ApplicationResource;
/*    */ import org.apache.tiles.request.locale.URLApplicationResource;
/*    */ import org.apache.tiles.request.servlet.ServletApplicationContext;
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
/*    */ public class SpringWildcardServletTilesApplicationContext
/*    */   extends ServletApplicationContext
/*    */ {
/*    */   private final ResourcePatternResolver resolver;
/*    */   
/*    */   public SpringWildcardServletTilesApplicationContext(ServletContext servletContext)
/*    */   {
/* 50 */     super(servletContext);
/* 51 */     this.resolver = new ServletContextResourcePatternResolver(servletContext);
/*    */   }
/*    */   
/*    */ 
/*    */   public ApplicationResource getResource(String localePath)
/*    */   {
/* 57 */     Collection<ApplicationResource> urlSet = getResources(localePath);
/* 58 */     if (!CollectionUtils.isEmpty(urlSet)) {
/* 59 */       return (ApplicationResource)urlSet.iterator().next();
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public ApplicationResource getResource(ApplicationResource base, Locale locale)
/*    */   {
/* 66 */     Collection<ApplicationResource> urlSet = getResources(base.getLocalePath(locale));
/* 67 */     if (!CollectionUtils.isEmpty(urlSet)) {
/* 68 */       return (ApplicationResource)urlSet.iterator().next();
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */   
/*    */   public Collection<ApplicationResource> getResources(String path)
/*    */   {
/*    */     try
/*    */     {
/* 77 */       resources = this.resolver.getResources(path);
/*    */     } catch (IOException ex) {
/*    */       Resource[] resources;
/* 80 */       ((ServletContext)getContext()).log("Resource retrieval failed for path: " + path, ex);
/* 81 */       return Collections.emptyList(); }
/*    */     Resource[] resources;
/* 83 */     if (ObjectUtils.isEmpty(resources)) {
/* 84 */       ((ServletContext)getContext()).log("No resources found for path pattern: " + path);
/* 85 */       return Collections.emptyList();
/*    */     }
/*    */     
/* 88 */     Collection<ApplicationResource> resourceList = new ArrayList(resources.length);
/* 89 */     for (Resource resource : resources) {
/*    */       try {
/* 91 */         URL url = resource.getURL();
/* 92 */         resourceList.add(new URLApplicationResource(url.toExternalForm(), url));
/*    */       }
/*    */       catch (IOException ex)
/*    */       {
/* 96 */         throw new IllegalArgumentException("No URL for " + resource, ex);
/*    */       }
/*    */     }
/* 99 */     return resourceList;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\SpringWildcardServletTilesApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */