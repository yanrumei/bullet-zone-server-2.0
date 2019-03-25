/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.ui.ExtendedModelMap;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelAndViewResolverMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   private final List<ModelAndViewResolver> mavResolvers;
/*  58 */   private final ModelAttributeMethodProcessor modelAttributeProcessor = new ModelAttributeMethodProcessor(true);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelAndViewResolverMethodReturnValueHandler(List<ModelAndViewResolver> mavResolvers)
/*     */   {
/*  65 */     this.mavResolvers = mavResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsReturnType(MethodParameter returnType)
/*     */   {
/*  74 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/*  81 */     if (this.mavResolvers != null) {
/*  82 */       for (ModelAndViewResolver mavResolver : this.mavResolvers) {
/*  83 */         Class<?> handlerType = returnType.getContainingClass();
/*  84 */         Method method = returnType.getMethod();
/*  85 */         ExtendedModelMap model = (ExtendedModelMap)mavContainer.getModel();
/*  86 */         ModelAndView mav = mavResolver.resolveModelAndView(method, handlerType, returnValue, model, webRequest);
/*  87 */         if (mav != ModelAndViewResolver.UNRESOLVED) {
/*  88 */           mavContainer.addAllAttributes(mav.getModel());
/*  89 */           mavContainer.setViewName(mav.getViewName());
/*  90 */           if (!mav.isReference()) {
/*  91 */             mavContainer.setView(mav.getView());
/*     */           }
/*  93 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     if (this.modelAttributeProcessor.supportsReturnType(returnType)) {
/* 100 */       this.modelAttributeProcessor.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
/*     */     }
/*     */     else
/*     */     {
/* 104 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ModelAndViewResolverMethodReturnValueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */