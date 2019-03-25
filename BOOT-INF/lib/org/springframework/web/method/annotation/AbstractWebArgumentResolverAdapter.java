/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*     */ public abstract class AbstractWebArgumentResolverAdapter
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*  51 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */   private final WebArgumentResolver adaptee;
/*     */   
/*     */ 
/*     */ 
/*     */   public AbstractWebArgumentResolverAdapter(WebArgumentResolver adaptee)
/*     */   {
/*  60 */     Assert.notNull(adaptee, "'adaptee' must not be null");
/*  61 */     this.adaptee = adaptee;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsParameter(MethodParameter parameter)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       NativeWebRequest webRequest = getWebRequest();
/*  73 */       Object result = this.adaptee.resolveArgument(parameter, webRequest);
/*  74 */       if (result == WebArgumentResolver.UNRESOLVED) {
/*  75 */         return false;
/*     */       }
/*     */       
/*  78 */       return ClassUtils.isAssignableValue(parameter.getParameterType(), result);
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  83 */       this.logger.debug("Error in checking support for parameter [" + parameter + "], message: " + ex.getMessage()); }
/*  84 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*     */     throws Exception
/*     */   {
/*  97 */     Class<?> paramType = parameter.getParameterType();
/*  98 */     Object result = this.adaptee.resolveArgument(parameter, webRequest);
/*  99 */     if ((result == WebArgumentResolver.UNRESOLVED) || (!ClassUtils.isAssignableValue(paramType, result)))
/*     */     {
/*     */ 
/* 102 */       throw new IllegalStateException("Standard argument type [" + paramType.getName() + "] in method " + parameter.getMethod() + "resolved to incompatible value of type [" + (result != null ? result.getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
/*     */     }
/*     */     
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract NativeWebRequest getWebRequest();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\AbstractWebArgumentResolverAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */