/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.webjars.WebJarAssetLocator;
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
/*     */ public class WebJarsResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   private static final String WEBJARS_LOCATION = "META-INF/resources/webjars/";
/*  50 */   private static final int WEBJARS_LOCATION_LENGTH = "META-INF/resources/webjars/".length();
/*     */   
/*     */ 
/*     */ 
/*     */   private final WebJarAssetLocator webJarAssetLocator;
/*     */   
/*     */ 
/*     */ 
/*     */   public WebJarsResourceResolver()
/*     */   {
/*  60 */     this(new WebJarAssetLocator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebJarsResourceResolver(WebJarAssetLocator webJarAssetLocator)
/*     */   {
/*  69 */     this.webJarAssetLocator = webJarAssetLocator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/*  77 */     Resource resolved = chain.resolveResource(request, requestPath, locations);
/*  78 */     if (resolved == null) {
/*  79 */       String webJarResourcePath = findWebJarResourcePath(requestPath);
/*  80 */       if (webJarResourcePath != null) {
/*  81 */         return chain.resolveResource(request, webJarResourcePath, locations);
/*     */       }
/*     */     }
/*  84 */     return resolved;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain)
/*     */   {
/*  91 */     String path = chain.resolveUrlPath(resourceUrlPath, locations);
/*  92 */     if (path == null) {
/*  93 */       String webJarResourcePath = findWebJarResourcePath(resourceUrlPath);
/*  94 */       if (webJarResourcePath != null) {
/*  95 */         return chain.resolveUrlPath(webJarResourcePath, locations);
/*     */       }
/*     */     }
/*  98 */     return path;
/*     */   }
/*     */   
/*     */   protected String findWebJarResourcePath(String path) {
/* 102 */     int startOffset = path.startsWith("/") ? 1 : 0;
/* 103 */     int endOffset = path.indexOf("/", 1);
/* 104 */     if (endOffset != -1) {
/* 105 */       String webjar = path.substring(startOffset, endOffset);
/* 106 */       String partialPath = path.substring(endOffset + 1);
/* 107 */       String webJarPath = this.webJarAssetLocator.getFullPathExact(webjar, partialPath);
/* 108 */       if (webJarPath != null) {
/* 109 */         return webJarPath.substring(WEBJARS_LOCATION_LENGTH);
/*     */       }
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\WebJarsResourceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */