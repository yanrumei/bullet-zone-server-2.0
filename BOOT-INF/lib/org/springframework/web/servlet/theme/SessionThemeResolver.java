/*    */ package org.springframework.web.servlet.theme;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.util.WebUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionThemeResolver
/*    */   extends AbstractThemeResolver
/*    */ {
/* 49 */   public static final String THEME_SESSION_ATTRIBUTE_NAME = SessionThemeResolver.class.getName() + ".THEME";
/*    */   
/*    */ 
/*    */   public String resolveThemeName(HttpServletRequest request)
/*    */   {
/* 54 */     String themeName = (String)WebUtils.getSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME);
/*    */     
/* 56 */     return themeName != null ? themeName : getDefaultThemeName();
/*    */   }
/*    */   
/*    */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/*    */   {
/* 61 */     WebUtils.setSessionAttribute(request, THEME_SESSION_ATTRIBUTE_NAME, 
/* 62 */       StringUtils.hasText(themeName) ? themeName : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\theme\SessionThemeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */