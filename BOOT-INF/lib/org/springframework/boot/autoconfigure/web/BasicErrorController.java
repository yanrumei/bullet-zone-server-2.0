/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
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
/*     */ @Controller
/*     */ @RequestMapping({"${server.error.path:${error.path:/error}}"})
/*     */ public class BasicErrorController
/*     */   extends AbstractErrorController
/*     */ {
/*     */   private final ErrorProperties errorProperties;
/*     */   
/*     */   public BasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties)
/*     */   {
/*  63 */     this(errorAttributes, errorProperties, 
/*  64 */       Collections.emptyList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers)
/*     */   {
/*  75 */     super(errorAttributes, errorViewResolvers);
/*  76 */     Assert.notNull(errorProperties, "ErrorProperties must not be null");
/*  77 */     this.errorProperties = errorProperties;
/*     */   }
/*     */   
/*     */   public String getErrorPath()
/*     */   {
/*  82 */     return this.errorProperties.getPath();
/*     */   }
/*     */   
/*     */   @RequestMapping(produces={"text/html"})
/*     */   public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/*  88 */     HttpStatus status = getStatus(request);
/*  89 */     Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, 
/*  90 */       isIncludeStackTrace(request, MediaType.TEXT_HTML)));
/*  91 */     response.setStatus(status.value());
/*  92 */     ModelAndView modelAndView = resolveErrorView(request, response, status, model);
/*  93 */     return modelAndView == null ? new ModelAndView("error", model) : modelAndView;
/*     */   }
/*     */   
/*     */   @RequestMapping
/*     */   @ResponseBody
/*     */   public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
/*  99 */     Map<String, Object> body = getErrorAttributes(request, 
/* 100 */       isIncludeStackTrace(request, MediaType.ALL));
/* 101 */     HttpStatus status = getStatus(request);
/* 102 */     return new ResponseEntity(body, status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces)
/*     */   {
/* 113 */     ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
/* 114 */     if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
/* 115 */       return true;
/*     */     }
/* 117 */     if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
/* 118 */       return getTraceParameter(request);
/*     */     }
/* 120 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ErrorProperties getErrorProperties()
/*     */   {
/* 128 */     return this.errorProperties;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\BasicErrorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */