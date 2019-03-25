/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public abstract class ResourceTransformerSupport
/*    */   implements ResourceTransformer
/*    */ {
/*    */   private ResourceUrlProvider resourceUrlProvider;
/*    */   
/*    */   public void setResourceUrlProvider(ResourceUrlProvider resourceUrlProvider)
/*    */   {
/* 52 */     this.resourceUrlProvider = resourceUrlProvider;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ResourceUrlProvider getResourceUrlProvider()
/*    */   {
/* 59 */     return this.resourceUrlProvider;
/*    */   }
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
/*    */   protected String resolveUrlPath(String resourcePath, HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain)
/*    */   {
/* 77 */     if (resourcePath.startsWith("/"))
/*    */     {
/* 79 */       ResourceUrlProvider urlProvider = findResourceUrlProvider(request);
/* 80 */       return urlProvider != null ? urlProvider.getForRequestUrl(request, resourcePath) : null;
/*    */     }
/*    */     
/*    */ 
/* 84 */     return transformerChain.getResolverChain().resolveUrlPath(resourcePath, 
/* 85 */       Collections.singletonList(resource));
/*    */   }
/*    */   
/*    */   private ResourceUrlProvider findResourceUrlProvider(HttpServletRequest request)
/*    */   {
/* 90 */     if (this.resourceUrlProvider != null) {
/* 91 */       return this.resourceUrlProvider;
/*    */     }
/* 93 */     return (ResourceUrlProvider)request.getAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceTransformerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */