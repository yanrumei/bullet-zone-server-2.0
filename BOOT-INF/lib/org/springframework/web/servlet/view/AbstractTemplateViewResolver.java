/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractTemplateViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*  35 */   private boolean exposeRequestAttributes = false;
/*     */   
/*  37 */   private boolean allowRequestOverride = false;
/*     */   
/*  39 */   private boolean exposeSessionAttributes = false;
/*     */   
/*  41 */   private boolean allowSessionOverride = false;
/*     */   
/*  43 */   private boolean exposeSpringMacroHelpers = true;
/*     */   
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  48 */     return AbstractTemplateView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeRequestAttributes(boolean exposeRequestAttributes)
/*     */   {
/*  57 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowRequestOverride(boolean allowRequestOverride)
/*     */   {
/*  68 */     this.allowRequestOverride = allowRequestOverride;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeSessionAttributes(boolean exposeSessionAttributes)
/*     */   {
/*  77 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowSessionOverride(boolean allowSessionOverride)
/*     */   {
/*  88 */     this.allowSessionOverride = allowSessionOverride;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers)
/*     */   {
/*  97 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*     */   }
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 103 */     AbstractTemplateView view = (AbstractTemplateView)super.buildView(viewName);
/* 104 */     view.setExposeRequestAttributes(this.exposeRequestAttributes);
/* 105 */     view.setAllowRequestOverride(this.allowRequestOverride);
/* 106 */     view.setExposeSessionAttributes(this.exposeSessionAttributes);
/* 107 */     view.setAllowSessionOverride(this.allowSessionOverride);
/* 108 */     view.setExposeSpringMacroHelpers(this.exposeSpringMacroHelpers);
/* 109 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\AbstractTemplateViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */