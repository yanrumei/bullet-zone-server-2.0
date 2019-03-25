/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
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
/*     */ public class ResourceUrlProvider
/*     */   implements ApplicationListener<ContextRefreshedEvent>
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  56 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*     */   
/*  58 */   private final Map<String, ResourceHttpRequestHandler> handlerMap = new LinkedHashMap();
/*     */   
/*  60 */   private boolean autodetect = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/*  69 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/*  77 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public UrlPathHelper getPathHelper()
/*     */   {
/*  85 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathMatcher(PathMatcher pathMatcher)
/*     */   {
/*  93 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathMatcher getPathMatcher()
/*     */   {
/* 100 */     return this.pathMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHandlerMap(Map<String, ResourceHttpRequestHandler> handlerMap)
/*     */   {
/* 110 */     if (handlerMap != null) {
/* 111 */       this.handlerMap.clear();
/* 112 */       this.handlerMap.putAll(handlerMap);
/* 113 */       this.autodetect = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, ResourceHttpRequestHandler> getHandlerMap()
/*     */   {
/* 122 */     return this.handlerMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAutodetect()
/*     */   {
/* 130 */     return this.autodetect;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event)
/*     */   {
/* 135 */     if (isAutodetect()) {
/* 136 */       this.handlerMap.clear();
/* 137 */       detectResourceHandlers(event.getApplicationContext());
/* 138 */       if ((this.handlerMap.isEmpty()) && (this.logger.isDebugEnabled())) {
/* 139 */         this.logger.debug("No resource handling mappings found");
/*     */       }
/* 141 */       if (!this.handlerMap.isEmpty()) {
/* 142 */         this.autodetect = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void detectResourceHandlers(ApplicationContext appContext)
/*     */   {
/* 149 */     this.logger.debug("Looking for resource handler mappings");
/*     */     
/* 151 */     Map<String, SimpleUrlHandlerMapping> beans = appContext.getBeansOfType(SimpleUrlHandlerMapping.class);
/* 152 */     List<SimpleUrlHandlerMapping> mappings = new ArrayList(beans.values());
/* 153 */     AnnotationAwareOrderComparator.sort(mappings);
/*     */     
/* 155 */     for (Iterator localIterator1 = mappings.iterator(); localIterator1.hasNext();) { mapping = (SimpleUrlHandlerMapping)localIterator1.next();
/* 156 */       for (String pattern : mapping.getHandlerMap().keySet()) {
/* 157 */         Object handler = mapping.getHandlerMap().get(pattern);
/* 158 */         if ((handler instanceof ResourceHttpRequestHandler)) {
/* 159 */           ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler)handler;
/* 160 */           if (this.logger.isDebugEnabled()) {
/* 161 */             this.logger.debug("Found resource handler mapping: URL pattern=\"" + pattern + "\", locations=" + resourceHandler
/* 162 */               .getLocations() + ", resolvers=" + resourceHandler
/* 163 */               .getResourceResolvers());
/*     */           }
/* 165 */           this.handlerMap.put(pattern, resourceHandler);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     SimpleUrlHandlerMapping mapping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getForRequestUrl(HttpServletRequest request, String requestUrl)
/*     */   {
/* 180 */     if (this.logger.isTraceEnabled()) {
/* 181 */       this.logger.trace("Getting resource URL for request URL \"" + requestUrl + "\"");
/*     */     }
/* 183 */     int prefixIndex = getLookupPathIndex(request);
/* 184 */     int suffixIndex = getEndPathIndex(requestUrl);
/* 185 */     String prefix = requestUrl.substring(0, prefixIndex);
/* 186 */     String suffix = requestUrl.substring(suffixIndex);
/* 187 */     String lookupPath = requestUrl.substring(prefixIndex, suffixIndex);
/* 188 */     String resolvedLookupPath = getForLookupPath(lookupPath);
/* 189 */     return resolvedLookupPath != null ? prefix + resolvedLookupPath + suffix : null;
/*     */   }
/*     */   
/*     */   private int getLookupPathIndex(HttpServletRequest request) {
/* 193 */     UrlPathHelper pathHelper = getUrlPathHelper();
/* 194 */     String requestUri = pathHelper.getRequestUri(request);
/* 195 */     String lookupPath = pathHelper.getLookupPathForRequest(request);
/* 196 */     return requestUri.indexOf(lookupPath);
/*     */   }
/*     */   
/*     */   private int getEndPathIndex(String lookupPath) {
/* 200 */     int suffixIndex = lookupPath.length();
/* 201 */     int queryIndex = lookupPath.indexOf("?");
/* 202 */     if (queryIndex > 0) {
/* 203 */       suffixIndex = queryIndex;
/*     */     }
/* 205 */     int hashIndex = lookupPath.indexOf("#");
/* 206 */     if (hashIndex > 0) {
/* 207 */       suffixIndex = Math.min(suffixIndex, hashIndex);
/*     */     }
/* 209 */     return suffixIndex;
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
/*     */   public final String getForLookupPath(String lookupPath)
/*     */   {
/* 225 */     if (this.logger.isTraceEnabled()) {
/* 226 */       this.logger.trace("Getting resource URL for lookup path \"" + lookupPath + "\"");
/*     */     }
/*     */     
/* 229 */     List<String> matchingPatterns = new ArrayList();
/* 230 */     for (Iterator localIterator = this.handlerMap.keySet().iterator(); localIterator.hasNext();) { pattern = (String)localIterator.next();
/* 231 */       if (getPathMatcher().match(pattern, lookupPath)) {
/* 232 */         matchingPatterns.add(pattern);
/*     */       }
/*     */     }
/*     */     String pattern;
/* 236 */     if (!matchingPatterns.isEmpty()) {
/* 237 */       Object patternComparator = getPathMatcher().getPatternComparator(lookupPath);
/* 238 */       Collections.sort(matchingPatterns, (Comparator)patternComparator);
/* 239 */       for (String pattern : matchingPatterns) {
/* 240 */         String pathWithinMapping = getPathMatcher().extractPathWithinPattern(pattern, lookupPath);
/* 241 */         String pathMapping = lookupPath.substring(0, lookupPath.indexOf(pathWithinMapping));
/* 242 */         if (this.logger.isTraceEnabled()) {
/* 243 */           this.logger.trace("Invoking ResourceResolverChain for URL pattern \"" + pattern + "\"");
/*     */         }
/* 245 */         ResourceHttpRequestHandler handler = (ResourceHttpRequestHandler)this.handlerMap.get(pattern);
/* 246 */         ResourceResolverChain chain = new DefaultResourceResolverChain(handler.getResourceResolvers());
/* 247 */         String resolved = chain.resolveUrlPath(pathWithinMapping, handler.getLocations());
/* 248 */         if (resolved != null)
/*     */         {
/*     */ 
/* 251 */           if (this.logger.isTraceEnabled()) {
/* 252 */             this.logger.trace("Resolved public resource URL path \"" + resolved + "\"");
/*     */           }
/* 254 */           return pathMapping + resolved;
/*     */         }
/*     */       }
/*     */     }
/* 258 */     if (this.logger.isDebugEnabled()) {
/* 259 */       this.logger.debug("No matching resource mapping for lookup path \"" + lookupPath + "\"");
/*     */     }
/* 261 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceUrlProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */