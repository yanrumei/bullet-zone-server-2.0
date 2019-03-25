/*    */ package org.springframework.ui.context.support;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.ui.context.HierarchicalThemeSource;
/*    */ import org.springframework.ui.context.ThemeSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UiApplicationContextUtils
/*    */ {
/*    */   public static final String THEME_SOURCE_BEAN_NAME = "themeSource";
/* 45 */   private static final Log logger = LogFactory.getLog(UiApplicationContextUtils.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ThemeSource initThemeSource(ApplicationContext context)
/*    */   {
/* 57 */     if (context.containsLocalBean("themeSource")) {
/* 58 */       ThemeSource themeSource = (ThemeSource)context.getBean("themeSource", ThemeSource.class);
/*    */       
/* 60 */       if (((context.getParent() instanceof ThemeSource)) && ((themeSource instanceof HierarchicalThemeSource))) {
/* 61 */         HierarchicalThemeSource hts = (HierarchicalThemeSource)themeSource;
/* 62 */         if (hts.getParentThemeSource() == null)
/*    */         {
/*    */ 
/* 65 */           hts.setParentThemeSource((ThemeSource)context.getParent());
/*    */         }
/*    */       }
/* 68 */       if (logger.isDebugEnabled()) {
/* 69 */         logger.debug("Using ThemeSource [" + themeSource + "]");
/*    */       }
/* 71 */       return themeSource;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 76 */     HierarchicalThemeSource themeSource = null;
/* 77 */     if ((context.getParent() instanceof ThemeSource)) {
/* 78 */       themeSource = new DelegatingThemeSource();
/* 79 */       themeSource.setParentThemeSource((ThemeSource)context.getParent());
/*    */     }
/*    */     else {
/* 82 */       themeSource = new ResourceBundleThemeSource();
/*    */     }
/* 84 */     if (logger.isDebugEnabled()) {
/* 85 */       logger.debug("Unable to locate ThemeSource with name 'themeSource': using default [" + themeSource + "]");
/*    */     }
/*    */     
/* 88 */     return themeSource;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframewor\\ui\context\support\UiApplicationContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */