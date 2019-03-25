/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRange;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.accept.ServletPathExtensionContentNegotiationStrategy;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class ResourceHttpRequestHandler
/*     */   extends WebContentGenerator
/*     */   implements HttpRequestHandler, EmbeddedValueResolverAware, InitializingBean, CorsConfigurationSource
/*     */ {
/* 103 */   private static final boolean contentLengthLongAvailable = ClassUtils.hasMethod(ServletResponse.class, "setContentLengthLong", new Class[] { Long.TYPE });
/*     */   
/* 105 */   private static final Log logger = LogFactory.getLog(ResourceHttpRequestHandler.class);
/*     */   
/*     */ 
/*     */   private static final String URL_RESOURCE_CHARSET_PREFIX = "[charset=";
/*     */   
/* 110 */   private final List<String> locationValues = new ArrayList(4);
/*     */   
/* 112 */   private final List<Resource> locations = new ArrayList(4);
/*     */   
/* 114 */   private final Map<Resource, Charset> locationCharsets = new HashMap(4);
/*     */   
/* 116 */   private final List<ResourceResolver> resourceResolvers = new ArrayList(4);
/*     */   
/* 118 */   private final List<ResourceTransformer> resourceTransformers = new ArrayList(4);
/*     */   
/*     */   private ResourceHttpMessageConverter resourceHttpMessageConverter;
/*     */   
/*     */   private ResourceRegionHttpMessageConverter resourceRegionHttpMessageConverter;
/*     */   
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   
/*     */   private PathExtensionContentNegotiationStrategy contentNegotiationStrategy;
/*     */   
/*     */   private CorsConfiguration corsConfiguration;
/*     */   
/*     */   private UrlPathHelper urlPathHelper;
/*     */   
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   public ResourceHttpRequestHandler()
/*     */   {
/* 136 */     super(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name() });
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
/*     */   public void setLocationValues(List<String> locationValues)
/*     */   {
/* 149 */     Assert.notNull(locationValues, "Location values list must not be null");
/* 150 */     this.locationValues.clear();
/* 151 */     this.locationValues.addAll(locationValues);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocations(List<Resource> locations)
/*     */   {
/* 160 */     Assert.notNull(locations, "Locations list must not be null");
/* 161 */     this.locations.clear();
/* 162 */     this.locations.addAll(locations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Resource> getLocations()
/*     */   {
/* 174 */     return this.locations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceResolvers(List<ResourceResolver> resourceResolvers)
/*     */   {
/* 183 */     this.resourceResolvers.clear();
/* 184 */     if (resourceResolvers != null) {
/* 185 */       this.resourceResolvers.addAll(resourceResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ResourceResolver> getResourceResolvers()
/*     */   {
/* 193 */     return this.resourceResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceTransformers(List<ResourceTransformer> resourceTransformers)
/*     */   {
/* 201 */     this.resourceTransformers.clear();
/* 202 */     if (resourceTransformers != null) {
/* 203 */       this.resourceTransformers.addAll(resourceTransformers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ResourceTransformer> getResourceTransformers()
/*     */   {
/* 211 */     return this.resourceTransformers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceHttpMessageConverter(ResourceHttpMessageConverter messageConverter)
/*     */   {
/* 220 */     this.resourceHttpMessageConverter = messageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHttpMessageConverter getResourceHttpMessageConverter()
/*     */   {
/* 228 */     return this.resourceHttpMessageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceRegionHttpMessageConverter(ResourceRegionHttpMessageConverter messageConverter)
/*     */   {
/* 237 */     this.resourceRegionHttpMessageConverter = messageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceRegionHttpMessageConverter getResourceRegionHttpMessageConverter()
/*     */   {
/* 245 */     return this.resourceRegionHttpMessageConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 255 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationManager getContentNegotiationManager()
/*     */   {
/* 263 */     return this.contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCorsConfiguration(CorsConfiguration corsConfiguration)
/*     */   {
/* 271 */     this.corsConfiguration = corsConfiguration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
/*     */   {
/* 279 */     return this.corsConfiguration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 289 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/* 297 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*     */   {
/* 302 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 308 */     resolveResourceLocations();
/*     */     
/* 310 */     if ((logger.isWarnEnabled()) && (CollectionUtils.isEmpty(this.locations))) {
/* 311 */       logger.warn("Locations list is empty. No resources will be served unless a custom ResourceResolver is configured as an alternative to PathResourceResolver.");
/*     */     }
/*     */     
/*     */ 
/* 315 */     if (this.resourceResolvers.isEmpty()) {
/* 316 */       this.resourceResolvers.add(new PathResourceResolver());
/*     */     }
/*     */     
/* 319 */     initAllowedLocations();
/*     */     
/* 321 */     if (this.resourceHttpMessageConverter == null) {
/* 322 */       this.resourceHttpMessageConverter = new ResourceHttpMessageConverter();
/*     */     }
/* 324 */     if (this.resourceRegionHttpMessageConverter == null) {
/* 325 */       this.resourceRegionHttpMessageConverter = new ResourceRegionHttpMessageConverter();
/*     */     }
/*     */     
/* 328 */     this.contentNegotiationStrategy = initContentNegotiationStrategy();
/*     */   }
/*     */   
/*     */   private void resolveResourceLocations() {
/* 332 */     if (CollectionUtils.isEmpty(this.locationValues)) {
/* 333 */       return;
/*     */     }
/* 335 */     if (!CollectionUtils.isEmpty(this.locations)) {
/* 336 */       throw new IllegalArgumentException("Please set either Resource-based \"locations\" or String-based \"locationValues\", but not both.");
/*     */     }
/*     */     
/*     */ 
/* 340 */     ApplicationContext applicationContext = getApplicationContext();
/* 341 */     for (String location : this.locationValues) {
/* 342 */       if (this.embeddedValueResolver != null) {
/* 343 */         String resolvedLocation = this.embeddedValueResolver.resolveStringValue(location);
/* 344 */         if (resolvedLocation == null) {
/* 345 */           throw new IllegalArgumentException("Location resolved to null: " + location);
/*     */         }
/* 347 */         location = resolvedLocation;
/*     */       }
/* 349 */       Charset charset = null;
/* 350 */       location = location.trim();
/* 351 */       if (location.startsWith("[charset=")) {
/* 352 */         int endIndex = location.indexOf("]", "[charset=".length());
/* 353 */         if (endIndex == -1) {
/* 354 */           throw new IllegalArgumentException("Invalid charset syntax in location: " + location);
/*     */         }
/* 356 */         String value = location.substring("[charset=".length(), endIndex);
/* 357 */         charset = Charset.forName(value);
/* 358 */         location = location.substring(endIndex + 1);
/*     */       }
/* 360 */       Resource resource = applicationContext.getResource(location);
/* 361 */       this.locations.add(resource);
/* 362 */       if (charset != null) {
/* 363 */         if (!(resource instanceof UrlResource)) {
/* 364 */           throw new IllegalArgumentException("Unexpected charset for non-UrlResource: " + resource);
/*     */         }
/* 366 */         this.locationCharsets.put(resource, charset);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initAllowedLocations()
/*     */   {
/* 377 */     if (CollectionUtils.isEmpty(this.locations)) {
/* 378 */       return;
/*     */     }
/* 380 */     for (int i = getResourceResolvers().size() - 1; i >= 0; i--) {
/* 381 */       if ((getResourceResolvers().get(i) instanceof PathResourceResolver)) {
/* 382 */         PathResourceResolver pathResolver = (PathResourceResolver)getResourceResolvers().get(i);
/* 383 */         if (ObjectUtils.isEmpty(pathResolver.getAllowedLocations())) {
/* 384 */           pathResolver.setAllowedLocations((Resource[])getLocations().toArray(new Resource[getLocations().size()]));
/*     */         }
/* 386 */         if (this.urlPathHelper == null) break;
/* 387 */         pathResolver.setLocationCharsets(this.locationCharsets);
/* 388 */         pathResolver.setUrlPathHelper(this.urlPathHelper); break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PathExtensionContentNegotiationStrategy initContentNegotiationStrategy()
/*     */   {
/* 402 */     Map<String, MediaType> mediaTypes = null;
/* 403 */     if (getContentNegotiationManager() != null)
/*     */     {
/* 405 */       PathExtensionContentNegotiationStrategy strategy = (PathExtensionContentNegotiationStrategy)getContentNegotiationManager().getStrategy(PathExtensionContentNegotiationStrategy.class);
/* 406 */       if (strategy != null) {
/* 407 */         mediaTypes = new HashMap(strategy.getMediaTypes());
/*     */       }
/*     */     }
/* 410 */     return getServletContext() != null ? new ServletPathExtensionContentNegotiationStrategy(
/* 411 */       getServletContext(), mediaTypes) : new PathExtensionContentNegotiationStrategy(mediaTypes);
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
/*     */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/* 433 */     Resource resource = getResource(request);
/* 434 */     if (resource == null) {
/* 435 */       logger.trace("No matching resource found - returning 404");
/* 436 */       response.sendError(404);
/* 437 */       return;
/*     */     }
/*     */     
/* 440 */     if (HttpMethod.OPTIONS.matches(request.getMethod())) {
/* 441 */       response.setHeader("Allow", getAllowHeader());
/* 442 */       return;
/*     */     }
/*     */     
/*     */ 
/* 446 */     checkRequest(request);
/*     */     
/*     */ 
/* 449 */     if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified())) {
/* 450 */       logger.trace("Resource not modified - returning 304");
/* 451 */       return;
/*     */     }
/*     */     
/*     */ 
/* 455 */     prepareResponse(response);
/*     */     
/*     */ 
/* 458 */     MediaType mediaType = getMediaType(request, resource);
/* 459 */     if (mediaType != null) {
/* 460 */       if (logger.isTraceEnabled()) {
/* 461 */         logger.trace("Determined media type '" + mediaType + "' for " + resource);
/*     */       }
/*     */       
/*     */     }
/* 465 */     else if (logger.isTraceEnabled()) {
/* 466 */       logger.trace("No media type found for " + resource + " - not sending a content-type header");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 471 */     if ("HEAD".equals(request.getMethod())) {
/* 472 */       setHeaders(response, resource, mediaType);
/* 473 */       logger.trace("HEAD request - skipping content");
/* 474 */       return;
/*     */     }
/*     */     
/* 477 */     ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
/* 478 */     if (request.getHeader("Range") == null) {
/* 479 */       setHeaders(response, resource, mediaType);
/* 480 */       this.resourceHttpMessageConverter.write(resource, mediaType, outputMessage);
/*     */     }
/*     */     else {
/* 483 */       response.setHeader("Accept-Ranges", "bytes");
/* 484 */       ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
/*     */       try {
/* 486 */         List<HttpRange> httpRanges = inputMessage.getHeaders().getRange();
/* 487 */         response.setStatus(206);
/* 488 */         if (httpRanges.size() == 1) {
/* 489 */           ResourceRegion resourceRegion = ((HttpRange)httpRanges.get(0)).toResourceRegion(resource);
/* 490 */           this.resourceRegionHttpMessageConverter.write(resourceRegion, mediaType, outputMessage);
/*     */         }
/*     */         else {
/* 493 */           this.resourceRegionHttpMessageConverter.write(
/* 494 */             HttpRange.toResourceRegions(httpRanges, resource), mediaType, outputMessage);
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException ex) {
/* 498 */         response.setHeader("Content-Range", "bytes */" + resource.contentLength());
/* 499 */         response.sendError(416);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Resource getResource(HttpServletRequest request) throws IOException {
/* 505 */     String path = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/* 506 */     if (path == null) {
/* 507 */       throw new IllegalStateException("Required request attribute '" + HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE + "' is not set");
/*     */     }
/*     */     
/* 510 */     path = processPath(path);
/* 511 */     if ((!StringUtils.hasText(path)) || (isInvalidPath(path))) {
/* 512 */       if (logger.isTraceEnabled()) {
/* 513 */         logger.trace("Ignoring invalid resource path [" + path + "]");
/*     */       }
/* 515 */       return null;
/*     */     }
/* 517 */     if (path.contains("%")) {
/*     */       try
/*     */       {
/* 520 */         if (isInvalidPath(URLDecoder.decode(path, "UTF-8"))) {
/* 521 */           if (logger.isTraceEnabled()) {
/* 522 */             logger.trace("Ignoring invalid resource path with escape sequences [" + path + "].");
/*     */           }
/* 524 */           return null;
/*     */         }
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */     }
/*     */     
/*     */ 
/* 531 */     ResourceResolverChain resolveChain = new DefaultResourceResolverChain(getResourceResolvers());
/* 532 */     Resource resource = resolveChain.resolveResource(request, path, getLocations());
/* 533 */     if ((resource == null) || (getResourceTransformers().isEmpty())) {
/* 534 */       return resource;
/*     */     }
/*     */     
/* 537 */     ResourceTransformerChain transformChain = new DefaultResourceTransformerChain(resolveChain, getResourceTransformers());
/* 538 */     resource = transformChain.transform(request, resource);
/* 539 */     return resource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String processPath(String path)
/*     */   {
/* 550 */     boolean slash = false;
/* 551 */     for (int i = 0; i < path.length(); i++) {
/* 552 */       if (path.charAt(i) == '/') {
/* 553 */         slash = true;
/*     */       }
/* 555 */       else if ((path.charAt(i) > ' ') && (path.charAt(i) != '')) {
/* 556 */         if ((i == 0) || ((i == 1) && (slash))) {
/* 557 */           return path;
/*     */         }
/* 559 */         path = slash ? "/" + path.substring(i) : path.substring(i);
/* 560 */         if (logger.isTraceEnabled()) {
/* 561 */           logger.trace("Path after trimming leading '/' and control characters: " + path);
/*     */         }
/* 563 */         return path;
/*     */       }
/*     */     }
/* 566 */     return slash ? "/" : "";
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
/*     */   protected boolean isInvalidPath(String path)
/*     */   {
/* 585 */     if (logger.isTraceEnabled()) {
/* 586 */       logger.trace("Applying \"invalid path\" checks to path: " + path);
/*     */     }
/* 588 */     if ((path.contains("WEB-INF")) || (path.contains("META-INF"))) {
/* 589 */       if (logger.isTraceEnabled()) {
/* 590 */         logger.trace("Path contains \"WEB-INF\" or \"META-INF\".");
/*     */       }
/* 592 */       return true;
/*     */     }
/* 594 */     if (path.contains(":/")) {
/* 595 */       String relativePath = path.charAt(0) == '/' ? path.substring(1) : path;
/* 596 */       if ((ResourceUtils.isUrl(relativePath)) || (relativePath.startsWith("url:"))) {
/* 597 */         if (logger.isTraceEnabled()) {
/* 598 */           logger.trace("Path represents URL or has \"url:\" prefix.");
/*     */         }
/* 600 */         return true;
/*     */       }
/*     */     }
/* 603 */     if (path.contains("..")) {
/* 604 */       path = StringUtils.cleanPath(path);
/* 605 */       if (path.contains("../")) {
/* 606 */         if (logger.isTraceEnabled()) {
/* 607 */           logger.trace("Path contains \"../\" after call to StringUtils#cleanPath.");
/*     */         }
/* 609 */         return true;
/*     */       }
/*     */     }
/* 612 */     return false;
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
/*     */   protected MediaType getMediaType(HttpServletRequest request, Resource resource)
/*     */   {
/* 627 */     MediaType mediaType = getMediaType(resource);
/* 628 */     if (mediaType != null) {
/* 629 */       return mediaType;
/*     */     }
/* 631 */     return this.contentNegotiationStrategy.getMediaTypeForResource(resource);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected MediaType getMediaType(Resource resource)
/*     */   {
/* 643 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setHeaders(HttpServletResponse response, Resource resource, MediaType mediaType)
/*     */     throws IOException
/*     */   {
/* 655 */     long length = resource.contentLength();
/* 656 */     if (length > 2147483647L) {
/* 657 */       if (contentLengthLongAvailable) {
/* 658 */         response.setContentLengthLong(length);
/*     */       }
/*     */       else {
/* 661 */         response.setHeader("Content-Length", Long.toString(length));
/*     */       }
/*     */     }
/*     */     else {
/* 665 */       response.setContentLength((int)length);
/*     */     }
/*     */     
/* 668 */     if (mediaType != null) {
/* 669 */       response.setContentType(mediaType.toString());
/*     */     }
/* 671 */     if ((resource instanceof EncodedResource)) {
/* 672 */       response.setHeader("Content-Encoding", ((EncodedResource)resource).getContentEncoding());
/*     */     }
/* 674 */     if ((resource instanceof VersionedResource)) {
/* 675 */       response.setHeader("ETag", "\"" + ((VersionedResource)resource).getVersion() + "\"");
/*     */     }
/* 677 */     response.setHeader("Accept-Ranges", "bytes");
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 683 */     return "ResourceHttpRequestHandler [locations=" + getLocations() + ", resolvers=" + getResourceResolvers() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceHttpRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */