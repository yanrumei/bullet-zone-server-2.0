/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ 
/*    */ 
/*    */ public abstract class AbstractResourceResolver
/*    */   implements ResourceResolver
/*    */ {
/* 36 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*    */   {
/* 43 */     if (this.logger.isTraceEnabled()) {
/* 44 */       this.logger.trace("Resolving resource for request path \"" + requestPath + "\"");
/*    */     }
/* 46 */     return resolveResourceInternal(request, requestPath, locations, chain);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String resolveUrlPath(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*    */   {
/* 53 */     if (this.logger.isTraceEnabled()) {
/* 54 */       this.logger.trace("Resolving public URL for resource path \"" + resourceUrlPath + "\"");
/*    */     }
/*    */     
/* 57 */     return resolveUrlPathInternal(resourceUrlPath, locations, chain);
/*    */   }
/*    */   
/*    */   protected abstract Resource resolveResourceInternal(HttpServletRequest paramHttpServletRequest, String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
/*    */   
/*    */   protected abstract String resolveUrlPathInternal(String paramString, List<? extends Resource> paramList, ResourceResolverChain paramResourceResolverChain);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\AbstractResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */