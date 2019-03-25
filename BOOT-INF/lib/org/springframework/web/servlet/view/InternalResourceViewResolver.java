/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InternalResourceViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*  50 */   private static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class
/*  51 */     .getClassLoader());
/*     */   
/*     */ 
/*     */ 
/*     */   private Boolean alwaysInclude;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InternalResourceViewResolver()
/*     */   {
/*  62 */     Class<?> viewClass = requiredViewClass();
/*  63 */     if ((InternalResourceView.class == viewClass) && (jstlPresent)) {
/*  64 */       viewClass = JstlView.class;
/*     */     }
/*  66 */     setViewClass(viewClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InternalResourceViewResolver(String prefix, String suffix)
/*     */   {
/*  77 */     this();
/*  78 */     setPrefix(prefix);
/*  79 */     setSuffix(suffix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/*  88 */     return InternalResourceView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysInclude(boolean alwaysInclude)
/*     */   {
/*  98 */     this.alwaysInclude = Boolean.valueOf(alwaysInclude);
/*     */   }
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 104 */     InternalResourceView view = (InternalResourceView)super.buildView(viewName);
/* 105 */     if (this.alwaysInclude != null) {
/* 106 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*     */     }
/* 108 */     view.setPreventDispatchLoop(true);
/* 109 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\InternalResourceViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */