/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import org.springframework.web.servlet.ThemeResolver;
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
/*    */ public abstract class AbstractThemeResolver
/*    */   implements ThemeResolver
/*    */ {
/*    */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/* 36 */   private String defaultThemeName = "theme";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDefaultThemeName(String defaultThemeName)
/*    */   {
/* 44 */     this.defaultThemeName = defaultThemeName;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getDefaultThemeName()
/*    */   {
/* 51 */     return this.defaultThemeName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\theme\AbstractThemeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */