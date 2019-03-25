/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndViewMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   private String[] redirectPatterns;
/*     */   
/*     */   public void setRedirectPatterns(String... redirectPatterns)
/*     */   {
/*  57 */     this.redirectPatterns = redirectPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRedirectPatterns()
/*     */   {
/*  65 */     return this.redirectPatterns;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*  71 */     return ModelAndView.class.isAssignableFrom(returnType.getParameterType());
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/*  78 */     if (returnValue == null) {
/*  79 */       mavContainer.setRequestHandled(true);
/*  80 */       return;
/*     */     }
/*     */     
/*  83 */     ModelAndView mav = (ModelAndView)returnValue;
/*  84 */     if (mav.isReference()) {
/*  85 */       String viewName = mav.getViewName();
/*  86 */       mavContainer.setViewName(viewName);
/*  87 */       if ((viewName != null) && (isRedirectViewName(viewName))) {
/*  88 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     }
/*     */     else {
/*  92 */       View view = mav.getView();
/*  93 */       mavContainer.setView(view);
/*  94 */       if (((view instanceof SmartView)) && 
/*  95 */         (((SmartView)view).isRedirectView())) {
/*  96 */         mavContainer.setRedirectModelScenario(true);
/*     */       }
/*     */     }
/*     */     
/* 100 */     mavContainer.setStatus(mav.getStatus());
/* 101 */     mavContainer.addAllAttributes(mav.getModel());
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
/* 113 */     if (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName)) {
/* 114 */       return true;
/*     */     }
/* 116 */     return viewName.startsWith("redirect:");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ModelAndViewMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */