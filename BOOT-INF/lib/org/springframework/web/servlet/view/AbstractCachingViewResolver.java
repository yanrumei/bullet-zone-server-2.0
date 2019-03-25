/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
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
/*     */ public abstract class AbstractCachingViewResolver
/*     */   extends WebApplicationObjectSupport
/*     */   implements ViewResolver
/*     */ {
/*     */   public static final int DEFAULT_CACHE_LIMIT = 1024;
/*  49 */   private static final View UNRESOLVED_VIEW = new View()
/*     */   {
/*     */     public String getContentType() {
/*  52 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {}
/*     */   };
/*     */   
/*     */ 
/*  61 */   private volatile int cacheLimit = 1024;
/*     */   
/*     */ 
/*  64 */   private boolean cacheUnresolved = true;
/*     */   
/*     */ 
/*  67 */   private final Map<Object, View> viewAccessCache = new ConcurrentHashMap(1024);
/*     */   
/*     */ 
/*  70 */   private final Map<Object, View> viewCreationCache = new LinkedHashMap(1024, 0.75F, true)
/*     */   {
/*     */ 
/*     */     protected boolean removeEldestEntry(Map.Entry<Object, View> eldest)
/*     */     {
/*  75 */       if (size() > AbstractCachingViewResolver.this.getCacheLimit()) {
/*  76 */         AbstractCachingViewResolver.this.viewAccessCache.remove(eldest.getKey());
/*  77 */         return true;
/*     */       }
/*     */       
/*  80 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheLimit(int cacheLimit)
/*     */   {
/*  91 */     this.cacheLimit = cacheLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getCacheLimit()
/*     */   {
/*  98 */     return this.cacheLimit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCache(boolean cache)
/*     */   {
/* 109 */     this.cacheLimit = (cache ? 1024 : 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCache()
/*     */   {
/* 116 */     return this.cacheLimit > 0;
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
/*     */   public void setCacheUnresolved(boolean cacheUnresolved)
/*     */   {
/* 132 */     this.cacheUnresolved = cacheUnresolved;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCacheUnresolved()
/*     */   {
/* 139 */     return this.cacheUnresolved;
/*     */   }
/*     */   
/*     */   public View resolveViewName(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 145 */     if (!isCache()) {
/* 146 */       return createView(viewName, locale);
/*     */     }
/*     */     
/* 149 */     Object cacheKey = getCacheKey(viewName, locale);
/* 150 */     View view = (View)this.viewAccessCache.get(cacheKey);
/* 151 */     if (view == null) {
/* 152 */       synchronized (this.viewCreationCache) {
/* 153 */         view = (View)this.viewCreationCache.get(cacheKey);
/* 154 */         if (view == null)
/*     */         {
/* 156 */           view = createView(viewName, locale);
/* 157 */           if ((view == null) && (this.cacheUnresolved)) {
/* 158 */             view = UNRESOLVED_VIEW;
/*     */           }
/* 160 */           if (view != null) {
/* 161 */             this.viewAccessCache.put(cacheKey, view);
/* 162 */             this.viewCreationCache.put(cacheKey, view);
/* 163 */             if (this.logger.isTraceEnabled()) {
/* 164 */               this.logger.trace("Cached view [" + cacheKey + "]");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 170 */     return view != UNRESOLVED_VIEW ? view : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getCacheKey(String viewName, Locale locale)
/*     */   {
/* 182 */     return viewName + '_' + locale;
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
/*     */   public void removeFromCache(String viewName, Locale locale)
/*     */   {
/* 195 */     if (!isCache()) {
/* 196 */       this.logger.warn("View caching is SWITCHED OFF -- removal not necessary");
/*     */     }
/*     */     else {
/* 199 */       Object cacheKey = getCacheKey(viewName, locale);
/*     */       Object cachedView;
/* 201 */       synchronized (this.viewCreationCache) {
/* 202 */         this.viewAccessCache.remove(cacheKey);
/* 203 */         cachedView = this.viewCreationCache.remove(cacheKey); }
/*     */       Object cachedView;
/* 205 */       if (this.logger.isDebugEnabled())
/*     */       {
/* 207 */         if (cachedView == null) {
/* 208 */           this.logger.debug("No cached instance for view '" + cacheKey + "' was found");
/*     */         }
/*     */         else {
/* 211 */           this.logger.debug("Cache for view " + cacheKey + " has been cleared");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearCache()
/*     */   {
/* 222 */     this.logger.debug("Clearing entire view cache");
/* 223 */     synchronized (this.viewCreationCache) {
/* 224 */       this.viewAccessCache.clear();
/* 225 */       this.viewCreationCache.clear();
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
/*     */   protected View createView(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 244 */     return loadView(viewName, locale);
/*     */   }
/*     */   
/*     */   protected abstract View loadView(String paramString, Locale paramLocale)
/*     */     throws Exception;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\AbstractCachingViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */