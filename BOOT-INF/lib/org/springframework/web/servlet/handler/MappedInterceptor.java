/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.context.request.WebRequestInterceptor;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.ModelAndView;
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
/*     */ public final class MappedInterceptor
/*     */   implements HandlerInterceptor
/*     */ {
/*     */   private final String[] includePatterns;
/*     */   private final String[] excludePatterns;
/*     */   private final HandlerInterceptor interceptor;
/*     */   private PathMatcher pathMatcher;
/*     */   
/*     */   public MappedInterceptor(String[] includePatterns, HandlerInterceptor interceptor)
/*     */   {
/*  62 */     this(includePatterns, null, interceptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappedInterceptor(String[] includePatterns, String[] excludePatterns, HandlerInterceptor interceptor)
/*     */   {
/*  72 */     this.includePatterns = includePatterns;
/*  73 */     this.excludePatterns = excludePatterns;
/*  74 */     this.interceptor = interceptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappedInterceptor(String[] includePatterns, WebRequestInterceptor interceptor)
/*     */   {
/*  84 */     this(includePatterns, null, interceptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappedInterceptor(String[] includePatterns, String[] excludePatterns, WebRequestInterceptor interceptor)
/*     */   {
/*  93 */     this(includePatterns, excludePatterns, new WebRequestHandlerInterceptorAdapter(interceptor));
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
/* 105 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathMatcher getPathMatcher()
/*     */   {
/* 112 */     return this.pathMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getPathPatterns()
/*     */   {
/* 119 */     return this.includePatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HandlerInterceptor getInterceptor()
/*     */   {
/* 126 */     return this.interceptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matches(String lookupPath, PathMatcher pathMatcher)
/*     */   {
/* 135 */     PathMatcher pathMatcherToUse = this.pathMatcher != null ? this.pathMatcher : pathMatcher;
/* 136 */     if (this.excludePatterns != null) {
/* 137 */       for (String pattern : this.excludePatterns) {
/* 138 */         if (pathMatcherToUse.match(pattern, lookupPath)) {
/* 139 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 143 */     if (ObjectUtils.isEmpty(this.includePatterns)) {
/* 144 */       return true;
/*     */     }
/*     */     
/* 147 */     for (String pattern : this.includePatterns) {
/* 148 */       if (pathMatcherToUse.match(pattern, lookupPath)) {
/* 149 */         return true;
/*     */       }
/*     */     }
/* 152 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws Exception
/*     */   {
/* 160 */     return this.interceptor.preHandle(request, response, handler);
/*     */   }
/*     */   
/*     */ 
/*     */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/*     */     throws Exception
/*     */   {
/* 167 */     this.interceptor.postHandle(request, response, handler, modelAndView);
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */     throws Exception
/*     */   {
/* 174 */     this.interceptor.afterCompletion(request, response, handler, ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\MappedInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */