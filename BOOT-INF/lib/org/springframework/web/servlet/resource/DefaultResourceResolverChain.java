/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ 
/*    */ 
/*    */ class DefaultResourceResolverChain
/*    */   implements ResourceResolverChain
/*    */ {
/* 37 */   private final List<ResourceResolver> resolvers = new ArrayList();
/*    */   
/* 39 */   private int index = -1;
/*    */   
/*    */   public DefaultResourceResolverChain(List<? extends ResourceResolver> resolvers)
/*    */   {
/* 43 */     if (resolvers != null) {
/* 44 */       this.resolvers.addAll(resolvers);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations)
/*    */   {
/* 51 */     ResourceResolver resolver = getNext();
/* 52 */     if (resolver == null) {
/* 53 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 57 */       return resolver.resolveResource(request, requestPath, locations, this);
/*    */     }
/*    */     finally {
/* 60 */       this.index -= 1;
/*    */     }
/*    */   }
/*    */   
/*    */   public String resolveUrlPath(String resourcePath, List<? extends Resource> locations)
/*    */   {
/* 66 */     ResourceResolver resolver = getNext();
/* 67 */     if (resolver == null) {
/* 68 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 72 */       return resolver.resolveUrlPath(resourcePath, locations, this);
/*    */     }
/*    */     finally {
/* 75 */       this.index -= 1;
/*    */     }
/*    */   }
/*    */   
/*    */   private ResourceResolver getNext() {
/* 80 */     Assert.state(this.index <= this.resolvers.size(), "Current index exceeds the number of configured ResourceResolvers");
/*    */     
/*    */ 
/* 83 */     if (this.index == this.resolvers.size() - 1) {
/* 84 */       return null;
/*    */     }
/* 86 */     this.index += 1;
/* 87 */     return (ResourceResolver)this.resolvers.get(this.index);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\DefaultResourceResolverChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */