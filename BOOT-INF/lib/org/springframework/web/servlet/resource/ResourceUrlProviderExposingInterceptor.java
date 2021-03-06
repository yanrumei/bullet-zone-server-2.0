/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
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
/*    */ public class ResourceUrlProviderExposingInterceptor
/*    */   extends HandlerInterceptorAdapter
/*    */ {
/* 37 */   public static final String RESOURCE_URL_PROVIDER_ATTR = ResourceUrlProvider.class.getName();
/*    */   
/*    */   private final ResourceUrlProvider resourceUrlProvider;
/*    */   
/*    */   public ResourceUrlProviderExposingInterceptor(ResourceUrlProvider resourceUrlProvider)
/*    */   {
/* 43 */     Assert.notNull(resourceUrlProvider, "ResourceUrlProvider is required");
/* 44 */     this.resourceUrlProvider = resourceUrlProvider;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*    */     throws Exception
/*    */   {
/* 51 */     request.setAttribute(RESOURCE_URL_PROVIDER_ATTR, this.resourceUrlProvider);
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceUrlProviderExposingInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */