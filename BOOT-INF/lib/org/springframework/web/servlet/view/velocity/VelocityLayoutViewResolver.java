/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class VelocityLayoutViewResolver
/*     */   extends VelocityViewResolver
/*     */ {
/*     */   private String layoutUrl;
/*     */   private String layoutKey;
/*     */   private String screenContentKey;
/*     */   
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  52 */     return VelocityLayoutView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayoutUrl(String layoutUrl)
/*     */   {
/*  62 */     this.layoutUrl = layoutUrl;
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
/*     */   public void setLayoutKey(String layoutKey)
/*     */   {
/*  77 */     this.layoutKey = layoutKey;
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
/*     */   public void setScreenContentKey(String screenContentKey)
/*     */   {
/*  90 */     this.screenContentKey = screenContentKey;
/*     */   }
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/*  96 */     VelocityLayoutView view = (VelocityLayoutView)super.buildView(viewName);
/*     */     
/*  98 */     if (this.layoutUrl != null) {
/*  99 */       view.setLayoutUrl(this.layoutUrl);
/*     */     }
/* 101 */     if (this.layoutKey != null) {
/* 102 */       view.setLayoutKey(this.layoutKey);
/*     */     }
/* 104 */     if (this.screenContentKey != null) {
/* 105 */       view.setScreenContentKey(this.screenContentKey);
/*     */     }
/* 107 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityLayoutViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */