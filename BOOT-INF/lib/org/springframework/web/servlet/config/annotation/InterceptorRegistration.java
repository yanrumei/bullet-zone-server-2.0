/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterceptorRegistration
/*     */ {
/*     */   private final HandlerInterceptor interceptor;
/*  40 */   private final List<String> includePatterns = new ArrayList();
/*     */   
/*  42 */   private final List<String> excludePatterns = new ArrayList();
/*     */   
/*     */ 
/*     */   private PathMatcher pathMatcher;
/*     */   
/*     */ 
/*     */ 
/*     */   public InterceptorRegistration(HandlerInterceptor interceptor)
/*     */   {
/*  51 */     Assert.notNull(interceptor, "Interceptor is required");
/*  52 */     this.interceptor = interceptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InterceptorRegistration addPathPatterns(String... patterns)
/*     */   {
/*  59 */     this.includePatterns.addAll(Arrays.asList(patterns));
/*  60 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InterceptorRegistration excludePathPatterns(String... patterns)
/*     */   {
/*  67 */     this.excludePatterns.addAll(Arrays.asList(patterns));
/*  68 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InterceptorRegistration pathMatcher(PathMatcher pathMatcher)
/*     */   {
/*  78 */     this.pathMatcher = pathMatcher;
/*  79 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getInterceptor()
/*     */   {
/*  87 */     if ((this.includePatterns.isEmpty()) && (this.excludePatterns.isEmpty())) {
/*  88 */       return this.interceptor;
/*     */     }
/*     */     
/*  91 */     String[] include = toArray(this.includePatterns);
/*  92 */     String[] exclude = toArray(this.excludePatterns);
/*  93 */     MappedInterceptor mappedInterceptor = new MappedInterceptor(include, exclude, this.interceptor);
/*     */     
/*  95 */     if (this.pathMatcher != null) {
/*  96 */       mappedInterceptor.setPathMatcher(this.pathMatcher);
/*     */     }
/*     */     
/*  99 */     return mappedInterceptor;
/*     */   }
/*     */   
/*     */   private static String[] toArray(List<String> list) {
/* 103 */     return CollectionUtils.isEmpty(list) ? null : (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\InterceptorRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */