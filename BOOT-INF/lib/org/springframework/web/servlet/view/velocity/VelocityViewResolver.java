/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class VelocityViewResolver
/*     */   extends AbstractTemplateViewResolver
/*     */ {
/*     */   private String dateToolAttribute;
/*     */   private String numberToolAttribute;
/*     */   private String toolboxConfigLocation;
/*     */   
/*     */   public VelocityViewResolver()
/*     */   {
/*  56 */     setViewClass(requiredViewClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  64 */     return VelocityView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateToolAttribute(String dateToolAttribute)
/*     */   {
/*  75 */     this.dateToolAttribute = dateToolAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNumberToolAttribute(String numberToolAttribute)
/*     */   {
/*  85 */     this.numberToolAttribute = numberToolAttribute;
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
/*     */   public void setToolboxConfigLocation(String toolboxConfigLocation)
/*     */   {
/* 102 */     this.toolboxConfigLocation = toolboxConfigLocation;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initApplicationContext()
/*     */   {
/* 108 */     super.initApplicationContext();
/*     */     
/* 110 */     if (this.toolboxConfigLocation != null) {
/* 111 */       if (VelocityView.class == getViewClass()) {
/* 112 */         this.logger.info("Using VelocityToolboxView instead of default VelocityView due to specified toolboxConfigLocation");
/*     */         
/* 114 */         setViewClass(VelocityToolboxView.class);
/*     */       }
/* 116 */       else if (!VelocityToolboxView.class.isAssignableFrom(getViewClass()))
/*     */       {
/*     */ 
/* 119 */         throw new IllegalArgumentException("Given view class [" + getViewClass().getName() + "] is not of type [" + VelocityToolboxView.class.getName() + "], which it needs to be in case of a specified toolboxConfigLocation");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 128 */     VelocityView view = (VelocityView)super.buildView(viewName);
/* 129 */     view.setDateToolAttribute(this.dateToolAttribute);
/* 130 */     view.setNumberToolAttribute(this.numberToolAttribute);
/* 131 */     if (this.toolboxConfigLocation != null) {
/* 132 */       ((VelocityToolboxView)view).setToolboxConfigLocation(this.toolboxConfigLocation);
/*     */     }
/* 134 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */