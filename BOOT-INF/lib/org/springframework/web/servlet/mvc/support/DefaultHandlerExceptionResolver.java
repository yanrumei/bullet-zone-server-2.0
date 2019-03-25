/*     */ package org.springframework.web.servlet.mvc.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.NoHandlerFoundException;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHandlerExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */ {
/*     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  92 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHandlerExceptionResolver()
/*     */   {
/*  99 */     setOrder(Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       if ((ex instanceof NoSuchRequestHandlingMethodException)) {
/* 110 */         return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException)ex, request, response, handler);
/*     */       }
/*     */       
/* 113 */       if ((ex instanceof HttpRequestMethodNotSupportedException)) {
/* 114 */         return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, request, response, handler);
/*     */       }
/*     */       
/* 117 */       if ((ex instanceof HttpMediaTypeNotSupportedException)) {
/* 118 */         return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, request, response, handler);
/*     */       }
/*     */       
/* 121 */       if ((ex instanceof HttpMediaTypeNotAcceptableException)) {
/* 122 */         return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 125 */       if ((ex instanceof MissingPathVariableException)) {
/* 126 */         return handleMissingPathVariable((MissingPathVariableException)ex, request, response, handler);
/*     */       }
/*     */       
/* 129 */       if ((ex instanceof MissingServletRequestParameterException)) {
/* 130 */         return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, request, response, handler);
/*     */       }
/*     */       
/* 133 */       if ((ex instanceof ServletRequestBindingException)) {
/* 134 */         return handleServletRequestBindingException((ServletRequestBindingException)ex, request, response, handler);
/*     */       }
/*     */       
/* 137 */       if ((ex instanceof ConversionNotSupportedException)) {
/* 138 */         return handleConversionNotSupported((ConversionNotSupportedException)ex, request, response, handler);
/*     */       }
/* 140 */       if ((ex instanceof TypeMismatchException)) {
/* 141 */         return handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
/*     */       }
/* 143 */       if ((ex instanceof HttpMessageNotReadableException)) {
/* 144 */         return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
/*     */       }
/* 146 */       if ((ex instanceof HttpMessageNotWritableException)) {
/* 147 */         return handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, request, response, handler);
/*     */       }
/* 149 */       if ((ex instanceof MethodArgumentNotValidException)) {
/* 150 */         return handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex, request, response, handler);
/*     */       }
/*     */       
/* 153 */       if ((ex instanceof MissingServletRequestPartException)) {
/* 154 */         return handleMissingServletRequestPartException((MissingServletRequestPartException)ex, request, response, handler);
/*     */       }
/*     */       
/* 157 */       if ((ex instanceof BindException)) {
/* 158 */         return handleBindException((BindException)ex, request, response, handler);
/*     */       }
/* 160 */       if ((ex instanceof NoHandlerFoundException)) {
/* 161 */         return handleNoHandlerFoundException((NoHandlerFoundException)ex, request, response, handler);
/*     */       }
/* 163 */       if ((ex instanceof AsyncRequestTimeoutException)) {
/* 164 */         return handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, request, response, handler);
/*     */       }
/*     */     }
/*     */     catch (Exception handlerException)
/*     */     {
/* 169 */       if (this.logger.isWarnEnabled()) {
/* 170 */         this.logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
/*     */       }
/*     */     }
/* 173 */     return null;
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
/*     */   @Deprecated
/*     */   protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 194 */     pageNotFoundLogger.warn(ex.getMessage());
/* 195 */     response.sendError(404);
/* 196 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 215 */     pageNotFoundLogger.warn(ex.getMessage());
/* 216 */     String[] supportedMethods = ex.getSupportedMethods();
/* 217 */     if (supportedMethods != null) {
/* 218 */       response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
/*     */     }
/* 220 */     response.sendError(405, ex.getMessage());
/* 221 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 240 */     response.sendError(415);
/* 241 */     List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
/* 242 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 243 */       response.setHeader("Accept", MediaType.toString(mediaTypes));
/*     */     }
/* 245 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 264 */     response.sendError(406);
/* 265 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 284 */     response.sendError(500, ex.getMessage());
/* 285 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 303 */     response.sendError(400, ex.getMessage());
/* 304 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 321 */     response.sendError(400, ex.getMessage());
/* 322 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 339 */     if (this.logger.isWarnEnabled()) {
/* 340 */       this.logger.warn("Failed to convert request element: " + ex);
/*     */     }
/* 342 */     sendServerError(ex, request, response);
/* 343 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 360 */     if (this.logger.isWarnEnabled()) {
/* 361 */       this.logger.warn("Failed to bind request element: " + ex);
/*     */     }
/* 363 */     response.sendError(400);
/* 364 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 383 */     if (this.logger.isWarnEnabled()) {
/* 384 */       this.logger.warn("Failed to read HTTP message: " + ex);
/*     */     }
/* 386 */     response.sendError(400);
/* 387 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 406 */     if (this.logger.isWarnEnabled()) {
/* 407 */       this.logger.warn("Failed to write HTTP message: " + ex);
/*     */     }
/* 409 */     sendServerError(ex, request, response);
/* 410 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 426 */     response.sendError(400);
/* 427 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 443 */     response.sendError(400, ex.getMessage());
/* 444 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 461 */     response.sendError(400);
/* 462 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 482 */     response.sendError(404);
/* 483 */     return new ModelAndView();
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
/*     */   protected ModelAndView handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws IOException
/*     */   {
/* 501 */     if (!response.isCommitted()) {
/* 502 */       response.sendError(503);
/*     */     }
/* 504 */     else if (this.logger.isErrorEnabled()) {
/* 505 */       this.logger.error("Async timeout for " + request.getMethod() + " [" + request.getRequestURI() + "]");
/*     */     }
/* 507 */     return new ModelAndView();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sendServerError(Exception ex, HttpServletRequest request, HttpServletResponse response)
/*     */     throws IOException
/*     */   {
/* 519 */     request.setAttribute("javax.servlet.error.exception", ex);
/* 520 */     response.sendError(500);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\DefaultHandlerExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */