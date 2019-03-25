/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ class DefaultResourceTransformerChain
/*    */   implements ResourceTransformerChain
/*    */ {
/*    */   private final ResourceResolverChain resolverChain;
/* 38 */   private final List<ResourceTransformer> transformers = new ArrayList();
/*    */   
/* 40 */   private int index = -1;
/*    */   
/*    */ 
/*    */ 
/*    */   public DefaultResourceTransformerChain(ResourceResolverChain resolverChain, List<ResourceTransformer> transformers)
/*    */   {
/* 46 */     Assert.notNull(resolverChain, "ResourceResolverChain is required");
/* 47 */     this.resolverChain = resolverChain;
/* 48 */     if (transformers != null) {
/* 49 */       this.transformers.addAll(transformers);
/*    */     }
/*    */   }
/*    */   
/*    */   public ResourceResolverChain getResolverChain()
/*    */   {
/* 55 */     return this.resolverChain;
/*    */   }
/*    */   
/*    */   public Resource transform(HttpServletRequest request, Resource resource)
/*    */     throws IOException
/*    */   {
/* 61 */     ResourceTransformer transformer = getNext();
/* 62 */     if (transformer == null) {
/* 63 */       return resource;
/*    */     }
/*    */     try
/*    */     {
/* 67 */       return transformer.transform(request, resource, this);
/*    */     }
/*    */     finally {
/* 70 */       this.index -= 1;
/*    */     }
/*    */   }
/*    */   
/*    */   private ResourceTransformer getNext() {
/* 75 */     Assert.state(this.index <= this.transformers.size(), "Current index exceeds the number of configured ResourceTransformer's");
/*    */     
/*    */ 
/* 78 */     if (this.index == this.transformers.size() - 1) {
/* 79 */       return null;
/*    */     }
/*    */     
/* 82 */     this.index += 1;
/* 83 */     return (ResourceTransformer)this.transformers.get(this.index);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\DefaultResourceTransformerChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */