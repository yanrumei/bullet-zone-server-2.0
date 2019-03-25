/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.servlet.HandlerExceptionResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Order(Integer.MIN_VALUE)
/*     */ public class DefaultErrorAttributes
/*     */   implements ErrorAttributes, HandlerExceptionResolver, Ordered
/*     */ {
/*  64 */   private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";
/*     */   
/*     */ 
/*     */   public int getOrder()
/*     */   {
/*  69 */     return Integer.MIN_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/*  75 */     storeErrorAttributes(request, ex);
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
/*  80 */     request.setAttribute(ERROR_ATTRIBUTE, ex);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace)
/*     */   {
/*  86 */     Map<String, Object> errorAttributes = new LinkedHashMap();
/*  87 */     errorAttributes.put("timestamp", new Date());
/*  88 */     addStatus(errorAttributes, requestAttributes);
/*  89 */     addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
/*  90 */     addPath(errorAttributes, requestAttributes);
/*  91 */     return errorAttributes;
/*     */   }
/*     */   
/*     */   private void addStatus(Map<String, Object> errorAttributes, RequestAttributes requestAttributes)
/*     */   {
/*  96 */     Integer status = (Integer)getAttribute(requestAttributes, "javax.servlet.error.status_code");
/*     */     
/*  98 */     if (status == null) {
/*  99 */       errorAttributes.put("status", Integer.valueOf(999));
/* 100 */       errorAttributes.put("error", "None");
/* 101 */       return;
/*     */     }
/* 103 */     errorAttributes.put("status", status);
/*     */     try {
/* 105 */       errorAttributes.put("error", HttpStatus.valueOf(status.intValue()).getReasonPhrase());
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 109 */       errorAttributes.put("error", "Http Status " + status);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addErrorDetails(Map<String, Object> errorAttributes, RequestAttributes requestAttributes, boolean includeStackTrace)
/*     */   {
/* 115 */     Throwable error = getError(requestAttributes);
/* 116 */     if (error != null) {
/* 117 */       while (((error instanceof ServletException)) && (error.getCause() != null)) {
/* 118 */         error = ((ServletException)error).getCause();
/*     */       }
/* 120 */       errorAttributes.put("exception", error.getClass().getName());
/* 121 */       addErrorMessage(errorAttributes, error);
/* 122 */       if (includeStackTrace) {
/* 123 */         addStackTrace(errorAttributes, error);
/*     */       }
/*     */     }
/* 126 */     Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
/* 127 */     if (((!StringUtils.isEmpty(message)) || (errorAttributes.get("message") == null)) && (!(error instanceof BindingResult)))
/*     */     {
/* 129 */       errorAttributes.put("message", 
/* 130 */         StringUtils.isEmpty(message) ? "No message available" : message);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
/* 135 */     BindingResult result = extractBindingResult(error);
/* 136 */     if (result == null) {
/* 137 */       errorAttributes.put("message", error.getMessage());
/* 138 */       return;
/*     */     }
/* 140 */     if (result.getErrorCount() > 0) {
/* 141 */       errorAttributes.put("errors", result.getAllErrors());
/* 142 */       errorAttributes.put("message", "Validation failed for object='" + result
/* 143 */         .getObjectName() + "'. Error count: " + result
/* 144 */         .getErrorCount());
/*     */     }
/*     */     else {
/* 147 */       errorAttributes.put("message", "No errors");
/*     */     }
/*     */   }
/*     */   
/*     */   private BindingResult extractBindingResult(Throwable error) {
/* 152 */     if ((error instanceof BindingResult)) {
/* 153 */       return (BindingResult)error;
/*     */     }
/* 155 */     if ((error instanceof MethodArgumentNotValidException)) {
/* 156 */       return ((MethodArgumentNotValidException)error).getBindingResult();
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
/* 162 */     StringWriter stackTrace = new StringWriter();
/* 163 */     error.printStackTrace(new PrintWriter(stackTrace));
/* 164 */     stackTrace.flush();
/* 165 */     errorAttributes.put("trace", stackTrace.toString());
/*     */   }
/*     */   
/*     */   private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes)
/*     */   {
/* 170 */     String path = (String)getAttribute(requestAttributes, "javax.servlet.error.request_uri");
/* 171 */     if (path != null) {
/* 172 */       errorAttributes.put("path", path);
/*     */     }
/*     */   }
/*     */   
/*     */   public Throwable getError(RequestAttributes requestAttributes)
/*     */   {
/* 178 */     Throwable exception = (Throwable)getAttribute(requestAttributes, ERROR_ATTRIBUTE);
/* 179 */     if (exception == null) {
/* 180 */       exception = (Throwable)getAttribute(requestAttributes, "javax.servlet.error.exception");
/*     */     }
/* 182 */     return exception;
/*     */   }
/*     */   
/*     */   private <T> T getAttribute(RequestAttributes requestAttributes, String name)
/*     */   {
/* 187 */     return (T)requestAttributes.getAttribute(name, 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\DefaultErrorAttributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */