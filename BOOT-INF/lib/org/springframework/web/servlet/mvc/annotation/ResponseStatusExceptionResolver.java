/*     */ package org.springframework.web.servlet.mvc.annotation;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseStatusExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */   implements MessageSourceAware
/*     */ {
/*     */   private MessageSource messageSource;
/*     */   
/*     */   public void setMessageSource(MessageSource messageSource)
/*     */   {
/*  57 */     this.messageSource = messageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/*  65 */     ResponseStatus responseStatus = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
/*  66 */     if (responseStatus != null) {
/*     */       try {
/*  68 */         return resolveResponseStatus(responseStatus, request, response, handler, ex);
/*     */       }
/*     */       catch (Exception resolveEx) {
/*  71 */         this.logger.warn("Handling of @ResponseStatus resulted in Exception", resolveEx);
/*     */       }
/*     */     }
/*  74 */     else if ((ex.getCause() instanceof Exception)) {
/*  75 */       ex = (Exception)ex.getCause();
/*  76 */       return doResolveException(request, response, handler, ex);
/*     */     }
/*  78 */     return null;
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
/*     */   protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus, HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */     throws Exception
/*     */   {
/* 100 */     int statusCode = responseStatus.code().value();
/* 101 */     String reason = responseStatus.reason();
/* 102 */     if (!StringUtils.hasLength(reason)) {
/* 103 */       response.sendError(statusCode);
/*     */     }
/*     */     else
/*     */     {
/* 107 */       String resolvedReason = this.messageSource != null ? this.messageSource.getMessage(reason, null, reason, LocaleContextHolder.getLocale()) : reason;
/*     */       
/* 109 */       response.sendError(statusCode, resolvedReason);
/*     */     }
/* 111 */     return new ModelAndView();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\ResponseStatusExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */