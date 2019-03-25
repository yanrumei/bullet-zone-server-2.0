/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
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
/*     */ public abstract class AbstractErrorController
/*     */   implements ErrorController
/*     */ {
/*     */   private final ErrorAttributes errorAttributes;
/*     */   private final List<ErrorViewResolver> errorViewResolvers;
/*     */   
/*     */   public AbstractErrorController(ErrorAttributes errorAttributes)
/*     */   {
/*  49 */     this(errorAttributes, null);
/*     */   }
/*     */   
/*     */   public AbstractErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers)
/*     */   {
/*  54 */     Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
/*  55 */     this.errorAttributes = errorAttributes;
/*  56 */     this.errorViewResolvers = sortErrorViewResolvers(errorViewResolvers);
/*     */   }
/*     */   
/*     */   private List<ErrorViewResolver> sortErrorViewResolvers(List<ErrorViewResolver> resolvers)
/*     */   {
/*  61 */     List<ErrorViewResolver> sorted = new ArrayList();
/*  62 */     if (resolvers != null) {
/*  63 */       sorted.addAll(resolvers);
/*  64 */       AnnotationAwareOrderComparator.sortIfNecessary(sorted);
/*     */     }
/*  66 */     return sorted;
/*     */   }
/*     */   
/*     */   protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace)
/*     */   {
/*  71 */     RequestAttributes requestAttributes = new ServletRequestAttributes(request);
/*  72 */     return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
/*     */   }
/*     */   
/*     */   protected boolean getTraceParameter(HttpServletRequest request)
/*     */   {
/*  77 */     String parameter = request.getParameter("trace");
/*  78 */     if (parameter == null) {
/*  79 */       return false;
/*     */     }
/*  81 */     return !"false".equals(parameter.toLowerCase());
/*     */   }
/*     */   
/*     */   protected HttpStatus getStatus(HttpServletRequest request)
/*     */   {
/*  86 */     Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
/*  87 */     if (statusCode == null) {
/*  88 */       return HttpStatus.INTERNAL_SERVER_ERROR;
/*     */     }
/*     */     try {
/*  91 */       return HttpStatus.valueOf(statusCode.intValue());
/*     */     }
/*     */     catch (Exception ex) {}
/*  94 */     return HttpStatus.INTERNAL_SERVER_ERROR;
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
/*     */   protected ModelAndView resolveErrorView(HttpServletRequest request, HttpServletResponse response, HttpStatus status, Map<String, Object> model)
/*     */   {
/* 111 */     for (ErrorViewResolver resolver : this.errorViewResolvers) {
/* 112 */       ModelAndView modelAndView = resolver.resolveErrorView(request, status, model);
/* 113 */       if (modelAndView != null) {
/* 114 */         return modelAndView;
/*     */       }
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\AbstractErrorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */