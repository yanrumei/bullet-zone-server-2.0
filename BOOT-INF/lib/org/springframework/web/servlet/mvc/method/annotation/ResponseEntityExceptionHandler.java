/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConversionNotSupportedException;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.MissingPathVariableException;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.ServletRequestBindingException;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.servlet.NoHandlerFoundException;
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
/*     */ public abstract class ResponseEntityExceptionHandler
/*     */ {
/*     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  92 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  97 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @ExceptionHandler({NoSuchRequestHandlingMethodException.class, HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class, HttpMediaTypeNotAcceptableException.class, MissingPathVariableException.class, MissingServletRequestParameterException.class, ServletRequestBindingException.class, ConversionNotSupportedException.class, TypeMismatchException.class, HttpMessageNotReadableException.class, HttpMessageNotWritableException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class, BindException.class, NoHandlerFoundException.class, AsyncRequestTimeoutException.class})
/*     */   public final ResponseEntity<Object> handleException(Exception ex, WebRequest request)
/*     */   {
/* 125 */     HttpHeaders headers = new HttpHeaders();
/* 126 */     if ((ex instanceof NoSuchRequestHandlingMethodException)) {
/* 127 */       HttpStatus status = HttpStatus.NOT_FOUND;
/* 128 */       return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException)ex, headers, status, request);
/*     */     }
/* 130 */     if ((ex instanceof HttpRequestMethodNotSupportedException)) {
/* 131 */       HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
/* 132 */       return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, headers, status, request);
/*     */     }
/* 134 */     if ((ex instanceof HttpMediaTypeNotSupportedException)) {
/* 135 */       HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
/* 136 */       return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, headers, status, request);
/*     */     }
/* 138 */     if ((ex instanceof HttpMediaTypeNotAcceptableException)) {
/* 139 */       HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
/* 140 */       return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, headers, status, request);
/*     */     }
/* 142 */     if ((ex instanceof MissingPathVariableException)) {
/* 143 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 144 */       return handleMissingPathVariable((MissingPathVariableException)ex, headers, status, request);
/*     */     }
/* 146 */     if ((ex instanceof MissingServletRequestParameterException)) {
/* 147 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 148 */       return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, headers, status, request);
/*     */     }
/* 150 */     if ((ex instanceof ServletRequestBindingException)) {
/* 151 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 152 */       return handleServletRequestBindingException((ServletRequestBindingException)ex, headers, status, request);
/*     */     }
/* 154 */     if ((ex instanceof ConversionNotSupportedException)) {
/* 155 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 156 */       return handleConversionNotSupported((ConversionNotSupportedException)ex, headers, status, request);
/*     */     }
/* 158 */     if ((ex instanceof TypeMismatchException)) {
/* 159 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 160 */       return handleTypeMismatch((TypeMismatchException)ex, headers, status, request);
/*     */     }
/* 162 */     if ((ex instanceof HttpMessageNotReadableException)) {
/* 163 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 164 */       return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, headers, status, request);
/*     */     }
/* 166 */     if ((ex instanceof HttpMessageNotWritableException)) {
/* 167 */       HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 168 */       return handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, headers, status, request);
/*     */     }
/* 170 */     if ((ex instanceof MethodArgumentNotValidException)) {
/* 171 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 172 */       return handleMethodArgumentNotValid((MethodArgumentNotValidException)ex, headers, status, request);
/*     */     }
/* 174 */     if ((ex instanceof MissingServletRequestPartException)) {
/* 175 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 176 */       return handleMissingServletRequestPart((MissingServletRequestPartException)ex, headers, status, request);
/*     */     }
/* 178 */     if ((ex instanceof BindException)) {
/* 179 */       HttpStatus status = HttpStatus.BAD_REQUEST;
/* 180 */       return handleBindException((BindException)ex, headers, status, request);
/*     */     }
/* 182 */     if ((ex instanceof NoHandlerFoundException)) {
/* 183 */       HttpStatus status = HttpStatus.NOT_FOUND;
/* 184 */       return handleNoHandlerFoundException((NoHandlerFoundException)ex, headers, status, request);
/*     */     }
/* 186 */     if ((ex instanceof AsyncRequestTimeoutException)) {
/* 187 */       HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
/* 188 */       return handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, headers, status, request);
/*     */     }
/*     */     
/*     */ 
/* 192 */     if (this.logger.isWarnEnabled()) {
/* 193 */       this.logger.warn("Unknown exception type: " + ex.getClass().getName());
/*     */     }
/* 195 */     HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 196 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 214 */     if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
/* 215 */       request.setAttribute("javax.servlet.error.exception", ex, 0);
/*     */     }
/* 217 */     return new ResponseEntity(body, headers, status);
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
/*     */   @Deprecated
/*     */   protected ResponseEntity<Object> handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 234 */     pageNotFoundLogger.warn(ex.getMessage());
/*     */     
/* 236 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 252 */     pageNotFoundLogger.warn(ex.getMessage());
/*     */     
/* 254 */     Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
/* 255 */     if (!CollectionUtils.isEmpty(supportedMethods)) {
/* 256 */       headers.setAllow(supportedMethods);
/*     */     }
/* 258 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 274 */     List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
/* 275 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 276 */       headers.setAccept(mediaTypes);
/*     */     }
/*     */     
/* 279 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 294 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 310 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 325 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 340 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 355 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 370 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 385 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 400 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 415 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 430 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 445 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
/*     */   {
/* 461 */     return handleExceptionInternal(ex, null, headers, status, request);
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
/*     */   protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest)
/*     */   {
/* 477 */     if ((webRequest instanceof ServletWebRequest)) {
/* 478 */       ServletWebRequest servletRequest = (ServletWebRequest)webRequest;
/* 479 */       HttpServletRequest request = (HttpServletRequest)servletRequest.getNativeRequest(HttpServletRequest.class);
/* 480 */       HttpServletResponse response = (HttpServletResponse)servletRequest.getNativeResponse(HttpServletResponse.class);
/* 481 */       if (response.isCommitted()) {
/* 482 */         if (this.logger.isErrorEnabled()) {
/* 483 */           this.logger.error("Async timeout for " + request.getMethod() + " [" + request.getRequestURI() + "]");
/*     */         }
/* 485 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 489 */     return handleExceptionInternal(ex, null, headers, status, webRequest);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ResponseEntityExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */