/*     */ package org.springframework.web.servlet.mvc.multiaction;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class AbstractUrlMethodNameResolver
/*     */   implements MethodNameResolver
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  45 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*     */   {
/*  56 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlDecode(boolean urlDecode)
/*     */   {
/*  68 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/*  79 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  80 */     this.urlPathHelper = urlPathHelper;
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
/*     */   public final String getHandlerMethodName(HttpServletRequest request)
/*     */     throws NoSuchRequestHandlingMethodException
/*     */   {
/*  94 */     String urlPath = this.urlPathHelper.getLookupPathForRequest(request);
/*  95 */     String name = getHandlerMethodNameForUrlPath(urlPath);
/*  96 */     if (name == null) {
/*  97 */       throw new NoSuchRequestHandlingMethodException(urlPath, request.getMethod(), request.getParameterMap());
/*     */     }
/*  99 */     if (this.logger.isDebugEnabled()) {
/* 100 */       this.logger.debug("Returning handler method name '" + name + "' for lookup path: " + urlPath);
/*     */     }
/* 102 */     return name;
/*     */   }
/*     */   
/*     */   protected abstract String getHandlerMethodNameForUrlPath(String paramString);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\AbstractUrlMethodNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */