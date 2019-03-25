/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ViewNameMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   private String[] redirectPatterns;
/*     */   
/*     */   public void setRedirectPatterns(String... redirectPatterns)
/*     */   {
/*  59 */     this.redirectPatterns = redirectPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getRedirectPatterns()
/*     */   {
/*  66 */     return this.redirectPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*  72 */     Class<?> paramType = returnType.getParameterType();
/*  73 */     return (Void.TYPE == paramType) || (CharSequence.class.isAssignableFrom(paramType));
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/*  80 */     if ((returnValue instanceof CharSequence)) {
/*  81 */       String viewName = returnValue.toString();
/*  82 */       mavContainer.setViewName(viewName);
/*  83 */       if (isRedirectViewName(viewName)) {
/*  84 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     }
/*  87 */     else if (returnValue != null)
/*     */     {
/*     */ 
/*  90 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
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
/*     */   protected boolean isRedirectViewName(String viewName)
/*     */   {
/* 103 */     return (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName)) || (viewName.startsWith("redirect:"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ViewNameMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */