/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*     */ public abstract class AbstractUrlViewController
/*     */   extends AbstractController
/*     */ {
/*  42 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
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
/*  53 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
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
/*  65 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent)
/*     */   {
/*  73 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
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
/*  84 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  85 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected UrlPathHelper getUrlPathHelper()
/*     */   {
/*  92 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 103 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 104 */     String viewName = getViewNameForRequest(request);
/* 105 */     if (this.logger.isDebugEnabled()) {
/* 106 */       this.logger.debug("Returning view name '" + viewName + "' for lookup path [" + lookupPath + "]");
/*     */     }
/* 108 */     return new ModelAndView(viewName, RequestContextUtils.getInputFlashMap(request));
/*     */   }
/*     */   
/*     */   protected abstract String getViewNameForRequest(HttpServletRequest paramHttpServletRequest);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\AbstractUrlViewController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */