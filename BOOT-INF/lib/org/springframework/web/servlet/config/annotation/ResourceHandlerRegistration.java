/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHandlerRegistration
/*     */ {
/*     */   private final String[] pathPatterns;
/*  41 */   private final List<String> locationValues = new ArrayList();
/*     */   
/*     */ 
/*     */   private Integer cachePeriod;
/*     */   
/*     */ 
/*     */   private CacheControl cacheControl;
/*     */   
/*     */ 
/*     */   private ResourceChainRegistration resourceChainRegistration;
/*     */   
/*     */ 
/*     */   public ResourceHandlerRegistration(String... pathPatterns)
/*     */   {
/*  55 */     Assert.notEmpty(pathPatterns, "At least one path pattern is required for resource handling.");
/*  56 */     this.pathPatterns = pathPatterns;
/*     */   }
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
/*     */   public ResourceHandlerRegistration addResourceLocations(String... resourceLocations)
/*     */   {
/*  79 */     this.locationValues.addAll(Arrays.asList(resourceLocations));
/*  80 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHandlerRegistration setCachePeriod(Integer cachePeriod)
/*     */   {
/*  91 */     this.cachePeriod = cachePeriod;
/*  92 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHandlerRegistration setCacheControl(CacheControl cacheControl)
/*     */   {
/* 104 */     this.cacheControl = cacheControl;
/* 105 */     return this;
/*     */   }
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
/*     */   public ResourceChainRegistration resourceChain(boolean cacheResources)
/*     */   {
/* 121 */     this.resourceChainRegistration = new ResourceChainRegistration(cacheResources);
/* 122 */     return this.resourceChainRegistration;
/*     */   }
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
/*     */   public ResourceChainRegistration resourceChain(boolean cacheResources, Cache cache)
/*     */   {
/* 143 */     this.resourceChainRegistration = new ResourceChainRegistration(cacheResources, cache);
/* 144 */     return this.resourceChainRegistration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] getPathPatterns()
/*     */   {
/* 152 */     return this.pathPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ResourceHttpRequestHandler getRequestHandler()
/*     */   {
/* 159 */     ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
/* 160 */     if (this.resourceChainRegistration != null) {
/* 161 */       handler.setResourceResolvers(this.resourceChainRegistration.getResourceResolvers());
/* 162 */       handler.setResourceTransformers(this.resourceChainRegistration.getResourceTransformers());
/*     */     }
/* 164 */     handler.setLocationValues(this.locationValues);
/* 165 */     if (this.cacheControl != null) {
/* 166 */       handler.setCacheControl(this.cacheControl);
/*     */     }
/* 168 */     else if (this.cachePeriod != null) {
/* 169 */       handler.setCacheSeconds(this.cachePeriod.intValue());
/*     */     }
/* 171 */     return handler;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ResourceHandlerRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */