/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiddenHttpMethodFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   public static final String DEFAULT_METHOD_PARAM = "_method";
/*  56 */   private String methodParam = "_method";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMethodParam(String methodParam)
/*     */   {
/*  64 */     Assert.hasText(methodParam, "'methodParam' must not be empty");
/*  65 */     this.methodParam = methodParam;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*     */     throws ServletException, IOException
/*     */   {
/*  72 */     HttpServletRequest requestToUse = request;
/*     */     
/*  74 */     if (("POST".equals(request.getMethod())) && (request.getAttribute("javax.servlet.error.exception") == null)) {
/*  75 */       String paramValue = request.getParameter(this.methodParam);
/*  76 */       if (StringUtils.hasLength(paramValue)) {
/*  77 */         requestToUse = new HttpMethodRequestWrapper(request, paramValue);
/*     */       }
/*     */     }
/*     */     
/*  81 */     filterChain.doFilter(requestToUse, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class HttpMethodRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private final String method;
/*     */     
/*     */ 
/*     */     public HttpMethodRequestWrapper(HttpServletRequest request, String method)
/*     */     {
/*  94 */       super();
/*  95 */       this.method = method.toUpperCase(Locale.ENGLISH);
/*     */     }
/*     */     
/*     */     public String getMethod()
/*     */     {
/* 100 */       return this.method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\HiddenHttpMethodFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */