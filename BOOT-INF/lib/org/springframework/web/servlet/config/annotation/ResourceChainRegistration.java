/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.concurrent.ConcurrentMapCache;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.servlet.resource.CachingResourceResolver;
/*     */ import org.springframework.web.servlet.resource.CachingResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.PathResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.VersionResourceResolver;
/*     */ import org.springframework.web.servlet.resource.WebJarsResourceResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceChainRegistration
/*     */ {
/*     */   private static final String DEFAULT_CACHE_NAME = "spring-resource-chain-cache";
/*  45 */   private static final boolean isWebJarsAssetLocatorPresent = ClassUtils.isPresent("org.webjars.WebJarAssetLocator", ResourceChainRegistration.class
/*  46 */     .getClassLoader());
/*     */   
/*     */ 
/*  49 */   private final List<ResourceResolver> resolvers = new ArrayList(4);
/*     */   
/*  51 */   private final List<ResourceTransformer> transformers = new ArrayList(4);
/*     */   
/*     */   private boolean hasVersionResolver;
/*     */   
/*     */   private boolean hasPathResolver;
/*     */   
/*     */   private boolean hasCssLinkTransformer;
/*     */   
/*     */   private boolean hasWebjarsResolver;
/*     */   
/*     */   public ResourceChainRegistration(boolean cacheResources)
/*     */   {
/*  63 */     this(cacheResources, cacheResources ? new ConcurrentMapCache("spring-resource-chain-cache") : null);
/*     */   }
/*     */   
/*     */   public ResourceChainRegistration(boolean cacheResources, Cache cache) {
/*  67 */     Assert.isTrue((!cacheResources) || (cache != null), "'cache' is required when cacheResources=true");
/*  68 */     if (cacheResources) {
/*  69 */       this.resolvers.add(new CachingResourceResolver(cache));
/*  70 */       this.transformers.add(new CachingResourceTransformer(cache));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceChainRegistration addResolver(ResourceResolver resolver)
/*     */   {
/*  81 */     Assert.notNull(resolver, "The provided ResourceResolver should not be null");
/*  82 */     this.resolvers.add(resolver);
/*  83 */     if ((resolver instanceof VersionResourceResolver)) {
/*  84 */       this.hasVersionResolver = true;
/*     */     }
/*  86 */     else if ((resolver instanceof PathResourceResolver)) {
/*  87 */       this.hasPathResolver = true;
/*     */     }
/*  89 */     else if ((resolver instanceof WebJarsResourceResolver)) {
/*  90 */       this.hasWebjarsResolver = true;
/*     */     }
/*  92 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceChainRegistration addTransformer(ResourceTransformer transformer)
/*     */   {
/* 101 */     Assert.notNull(transformer, "The provided ResourceTransformer should not be null");
/* 102 */     this.transformers.add(transformer);
/* 103 */     if ((transformer instanceof CssLinkResourceTransformer)) {
/* 104 */       this.hasCssLinkTransformer = true;
/*     */     }
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   protected List<ResourceResolver> getResourceResolvers() {
/* 110 */     if (!this.hasPathResolver) {
/* 111 */       List<ResourceResolver> result = new ArrayList(this.resolvers);
/* 112 */       if ((isWebJarsAssetLocatorPresent) && (!this.hasWebjarsResolver)) {
/* 113 */         result.add(new WebJarsResourceResolver());
/*     */       }
/* 115 */       result.add(new PathResourceResolver());
/* 116 */       return result;
/*     */     }
/* 118 */     return this.resolvers;
/*     */   }
/*     */   
/*     */   protected List<ResourceTransformer> getResourceTransformers() {
/* 122 */     if ((this.hasVersionResolver) && (!this.hasCssLinkTransformer)) {
/* 123 */       List<ResourceTransformer> result = new ArrayList(this.transformers);
/* 124 */       boolean hasTransformers = !this.transformers.isEmpty();
/* 125 */       boolean hasCaching = (hasTransformers) && ((this.transformers.get(0) instanceof CachingResourceTransformer));
/* 126 */       result.add(hasCaching ? 1 : 0, new CssLinkResourceTransformer());
/* 127 */       return result;
/*     */     }
/* 129 */     return this.transformers;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ResourceChainRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */