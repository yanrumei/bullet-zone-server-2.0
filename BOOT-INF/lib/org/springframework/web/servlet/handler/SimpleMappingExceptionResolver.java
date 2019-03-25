/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleMappingExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */ {
/*     */   public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
/*     */   private Properties exceptionMappings;
/*     */   private Class<?>[] excludedExceptions;
/*     */   private String defaultErrorView;
/*     */   private Integer defaultStatusCode;
/*  58 */   private Map<String, Integer> statusCodes = new HashMap();
/*     */   
/*  60 */   private String exceptionAttribute = "exception";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExceptionMappings(Properties mappings)
/*     */   {
/*  78 */     this.exceptionMappings = mappings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExcludedExceptions(Class<?>... excludedExceptions)
/*     */   {
/*  88 */     this.excludedExceptions = excludedExceptions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultErrorView(String defaultErrorView)
/*     */   {
/*  97 */     this.defaultErrorView = defaultErrorView;
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
/*     */   public void setStatusCodes(Properties statusCodes)
/*     */   {
/* 110 */     for (Enumeration<?> enumeration = statusCodes.propertyNames(); enumeration.hasMoreElements();) {
/* 111 */       String viewName = (String)enumeration.nextElement();
/* 112 */       Integer statusCode = Integer.valueOf(statusCodes.getProperty(viewName));
/* 113 */       this.statusCodes.put(viewName, statusCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addStatusCode(String viewName, int statusCode)
/*     */   {
/* 122 */     this.statusCodes.put(viewName, Integer.valueOf(statusCode));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Integer> getStatusCodesAsMap()
/*     */   {
/* 130 */     return Collections.unmodifiableMap(this.statusCodes);
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
/*     */   public void setDefaultStatusCode(int defaultStatusCode)
/*     */   {
/* 146 */     this.defaultStatusCode = Integer.valueOf(defaultStatusCode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExceptionAttribute(String exceptionAttribute)
/*     */   {
/* 157 */     this.exceptionAttribute = exceptionAttribute;
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
/*     */ 
/*     */ 
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/* 180 */     String viewName = determineViewName(ex, request);
/* 181 */     if (viewName != null)
/*     */     {
/*     */ 
/* 184 */       Integer statusCode = determineStatusCode(request, viewName);
/* 185 */       if (statusCode != null) {
/* 186 */         applyStatusCodeIfPossible(request, response, statusCode.intValue());
/*     */       }
/* 188 */       return getModelAndView(viewName, ex, request);
/*     */     }
/*     */     
/* 191 */     return null;
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
/*     */   protected String determineViewName(Exception ex, HttpServletRequest request)
/*     */   {
/* 205 */     String viewName = null;
/* 206 */     if (this.excludedExceptions != null) {
/* 207 */       for (Class<?> excludedEx : this.excludedExceptions) {
/* 208 */         if (excludedEx.equals(ex.getClass())) {
/* 209 */           return null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 214 */     if (this.exceptionMappings != null) {
/* 215 */       viewName = findMatchingViewName(this.exceptionMappings, ex);
/*     */     }
/*     */     
/* 218 */     if ((viewName == null) && (this.defaultErrorView != null)) {
/* 219 */       if (this.logger.isDebugEnabled()) {
/* 220 */         this.logger.debug("Resolving to default view '" + this.defaultErrorView + "' for exception of type [" + ex
/* 221 */           .getClass().getName() + "]");
/*     */       }
/* 223 */       viewName = this.defaultErrorView;
/*     */     }
/* 225 */     return viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String findMatchingViewName(Properties exceptionMappings, Exception ex)
/*     */   {
/* 236 */     String viewName = null;
/* 237 */     String dominantMapping = null;
/* 238 */     int deepest = Integer.MAX_VALUE;
/* 239 */     for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements();) {
/* 240 */       String exceptionMapping = (String)names.nextElement();
/* 241 */       int depth = getDepth(exceptionMapping, ex);
/* 242 */       if ((depth >= 0) && ((depth < deepest) || ((depth == deepest) && (dominantMapping != null) && 
/* 243 */         (exceptionMapping.length() > dominantMapping.length())))) {
/* 244 */         deepest = depth;
/* 245 */         dominantMapping = exceptionMapping;
/* 246 */         viewName = exceptionMappings.getProperty(exceptionMapping);
/*     */       }
/*     */     }
/* 249 */     if ((viewName != null) && (this.logger.isDebugEnabled())) {
/* 250 */       this.logger.debug("Resolving to view '" + viewName + "' for exception of type [" + ex.getClass().getName() + "], based on exception mapping [" + dominantMapping + "]");
/*     */     }
/*     */     
/* 253 */     return viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDepth(String exceptionMapping, Exception ex)
/*     */   {
/* 262 */     return getDepth(exceptionMapping, ex.getClass(), 0);
/*     */   }
/*     */   
/*     */   private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
/* 266 */     if (exceptionClass.getName().contains(exceptionMapping))
/*     */     {
/* 268 */       return depth;
/*     */     }
/*     */     
/* 271 */     if (exceptionClass == Throwable.class) {
/* 272 */       return -1;
/*     */     }
/* 274 */     return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
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
/*     */   protected Integer determineStatusCode(HttpServletRequest request, String viewName)
/*     */   {
/* 291 */     if (this.statusCodes.containsKey(viewName)) {
/* 292 */       return (Integer)this.statusCodes.get(viewName);
/*     */     }
/* 294 */     return this.defaultStatusCode;
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
/*     */   protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode)
/*     */   {
/* 308 */     if (!WebUtils.isIncludeRequest(request)) {
/* 309 */       if (this.logger.isDebugEnabled()) {
/* 310 */         this.logger.debug("Applying HTTP status code " + statusCode);
/*     */       }
/* 312 */       response.setStatus(statusCode);
/* 313 */       request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(statusCode));
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
/*     */   protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request)
/*     */   {
/* 326 */     return getModelAndView(viewName, ex);
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
/*     */   protected ModelAndView getModelAndView(String viewName, Exception ex)
/*     */   {
/* 339 */     ModelAndView mv = new ModelAndView(viewName);
/* 340 */     if (this.exceptionAttribute != null) {
/* 341 */       if (this.logger.isDebugEnabled()) {
/* 342 */         this.logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
/*     */       }
/* 344 */       mv.addObject(this.exceptionAttribute, ex);
/*     */     }
/* 346 */     return mv;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\SimpleMappingExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */