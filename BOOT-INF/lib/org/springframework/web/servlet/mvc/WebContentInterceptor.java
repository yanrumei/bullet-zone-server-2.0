/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.ModelAndView;
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
/*     */ public class WebContentInterceptor
/*     */   extends WebContentGenerator
/*     */   implements HandlerInterceptor
/*     */ {
/*  53 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  55 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*     */   
/*  57 */   private Map<String, Integer> cacheMappings = new HashMap();
/*     */   
/*  59 */   private Map<String, CacheControl> cacheControlMappings = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public WebContentInterceptor()
/*     */   {
/*  65 */     super(false);
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
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*     */   {
/*  79 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
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
/*     */   public void setUrlDecode(boolean urlDecode)
/*     */   {
/*  93 */     this.urlPathHelper.setUrlDecode(urlDecode);
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
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 106 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 107 */     this.urlPathHelper = urlPathHelper;
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
/*     */   public void setCacheMappings(Properties cacheMappings)
/*     */   {
/* 126 */     this.cacheMappings.clear();
/* 127 */     Enumeration<?> propNames = cacheMappings.propertyNames();
/* 128 */     while (propNames.hasMoreElements()) {
/* 129 */       String path = (String)propNames.nextElement();
/* 130 */       int cacheSeconds = Integer.valueOf(cacheMappings.getProperty(path)).intValue();
/* 131 */       this.cacheMappings.put(path, Integer.valueOf(cacheSeconds));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCacheMapping(CacheControl cacheControl, String... paths)
/*     */   {
/* 153 */     for (String path : paths) {
/* 154 */       this.cacheControlMappings.put(path, cacheControl);
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
/*     */   public void setPathMatcher(PathMatcher pathMatcher)
/*     */   {
/* 167 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 168 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws ServletException
/*     */   {
/* 176 */     checkRequest(request);
/*     */     
/* 178 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 179 */     if (this.logger.isDebugEnabled()) {
/* 180 */       this.logger.debug("Looking up cache seconds for [" + lookupPath + "]");
/*     */     }
/*     */     
/* 183 */     CacheControl cacheControl = lookupCacheControl(lookupPath);
/* 184 */     Integer cacheSeconds = lookupCacheSeconds(lookupPath);
/* 185 */     if (cacheControl != null) {
/* 186 */       if (this.logger.isDebugEnabled()) {
/* 187 */         this.logger.debug("Applying CacheControl to [" + lookupPath + "]");
/*     */       }
/* 189 */       applyCacheControl(response, cacheControl);
/*     */     }
/* 191 */     else if (cacheSeconds != null) {
/* 192 */       if (this.logger.isDebugEnabled()) {
/* 193 */         this.logger.debug("Applying CacheControl to [" + lookupPath + "]");
/*     */       }
/* 195 */       applyCacheSeconds(response, cacheSeconds.intValue());
/*     */     }
/*     */     else {
/* 198 */       if (this.logger.isDebugEnabled()) {
/* 199 */         this.logger.debug("Applying default cache seconds to [" + lookupPath + "]");
/*     */       }
/* 201 */       prepareResponse(response);
/*     */     }
/*     */     
/* 204 */     return true;
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
/*     */   protected CacheControl lookupCacheControl(String urlPath)
/*     */   {
/* 218 */     CacheControl cacheControl = (CacheControl)this.cacheControlMappings.get(urlPath);
/* 219 */     if (cacheControl != null) {
/* 220 */       return cacheControl;
/*     */     }
/*     */     
/* 223 */     for (String registeredPath : this.cacheControlMappings.keySet()) {
/* 224 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 225 */         return (CacheControl)this.cacheControlMappings.get(registeredPath);
/*     */       }
/*     */     }
/* 228 */     return null;
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
/*     */   protected Integer lookupCacheSeconds(String urlPath)
/*     */   {
/* 242 */     Integer cacheSeconds = (Integer)this.cacheMappings.get(urlPath);
/* 243 */     if (cacheSeconds != null) {
/* 244 */       return cacheSeconds;
/*     */     }
/*     */     
/* 247 */     for (String registeredPath : this.cacheMappings.keySet()) {
/* 248 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 249 */         return (Integer)this.cacheMappings.get(registeredPath);
/*     */       }
/*     */     }
/* 252 */     return null;
/*     */   }
/*     */   
/*     */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */     throws Exception
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\WebContentInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */