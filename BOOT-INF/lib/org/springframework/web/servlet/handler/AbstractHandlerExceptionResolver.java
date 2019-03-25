/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public abstract class AbstractHandlerExceptionResolver
/*     */   implements HandlerExceptionResolver, Ordered
/*     */ {
/*     */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*  49 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  51 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   private Set<?> mappedHandlers;
/*     */   
/*     */   private Class<?>[] mappedHandlerClasses;
/*     */   
/*     */   private Log warnLogger;
/*     */   
/*  59 */   private boolean preventResponseCaching = false;
/*     */   
/*     */   public void setOrder(int order)
/*     */   {
/*  63 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/*  68 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMappedHandlers(Set<?> mappedHandlers)
/*     */   {
/*  80 */     this.mappedHandlers = mappedHandlers;
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
/*     */   public void setMappedHandlerClasses(Class<?>... mappedHandlerClasses)
/*     */   {
/*  93 */     this.mappedHandlerClasses = mappedHandlerClasses;
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
/*     */   public void setWarnLogCategory(String loggerName)
/*     */   {
/* 107 */     this.warnLogger = LogFactory.getLog(loggerName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPreventResponseCaching(boolean preventResponseCaching)
/*     */   {
/* 117 */     this.preventResponseCaching = preventResponseCaching;
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
/*     */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/* 131 */     if (shouldApplyTo(request, handler)) {
/* 132 */       if (this.logger.isDebugEnabled()) {
/* 133 */         this.logger.debug("Resolving exception from handler [" + handler + "]: " + ex);
/*     */       }
/* 135 */       prepareResponse(ex, response);
/* 136 */       ModelAndView result = doResolveException(request, response, handler, ex);
/* 137 */       if (result != null) {
/* 138 */         logException(ex, request);
/*     */       }
/* 140 */       return result;
/*     */     }
/*     */     
/* 143 */     return null;
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
/*     */   protected boolean shouldApplyTo(HttpServletRequest request, Object handler)
/*     */   {
/* 161 */     if (handler != null) {
/* 162 */       if ((this.mappedHandlers != null) && (this.mappedHandlers.contains(handler))) {
/* 163 */         return true;
/*     */       }
/* 165 */       if (this.mappedHandlerClasses != null) {
/* 166 */         for (Class<?> handlerClass : this.mappedHandlerClasses) {
/* 167 */           if (handlerClass.isInstance(handler)) {
/* 168 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 174 */     return (this.mappedHandlers == null) && (this.mappedHandlerClasses == null);
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
/*     */   protected void logException(Exception ex, HttpServletRequest request)
/*     */   {
/* 188 */     if ((this.warnLogger != null) && (this.warnLogger.isWarnEnabled())) {
/* 189 */       this.warnLogger.warn(buildLogMessage(ex, request));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String buildLogMessage(Exception ex, HttpServletRequest request)
/*     */   {
/* 200 */     return "Resolved exception caused by Handler execution: " + ex;
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
/*     */   protected void prepareResponse(Exception ex, HttpServletResponse response)
/*     */   {
/* 213 */     if (this.preventResponseCaching) {
/* 214 */       preventCaching(response);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void preventCaching(HttpServletResponse response)
/*     */   {
/* 224 */     response.addHeader("Cache-Control", "no-store");
/*     */   }
/*     */   
/*     */   protected abstract ModelAndView doResolveException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractHandlerExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */