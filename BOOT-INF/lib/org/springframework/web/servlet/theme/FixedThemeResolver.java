/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class FixedThemeResolver
/*    */   extends AbstractThemeResolver
/*    */ {
/*    */   public String resolveThemeName(HttpServletRequest request)
/*    */   {
/* 39 */     return getDefaultThemeName();
/*    */   }
/*    */   
/*    */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/*    */   {
/* 44 */     throw new UnsupportedOperationException("Cannot change theme - use a different theme resolution strategy");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\theme\FixedThemeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */