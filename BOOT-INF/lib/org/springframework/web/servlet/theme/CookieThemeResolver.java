/*     */ package org.springframework.web.servlet.theme;
/*     */ 
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.ThemeResolver;
/*     */ import org.springframework.web.util.CookieGenerator;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public class CookieThemeResolver
/*     */   extends CookieGenerator
/*     */   implements ThemeResolver
/*     */ {
/*     */   public static final String ORIGINAL_DEFAULT_THEME_NAME = "theme";
/*  52 */   public static final String THEME_REQUEST_ATTRIBUTE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*     */   
/*  54 */   public static final String DEFAULT_COOKIE_NAME = CookieThemeResolver.class.getName() + ".THEME";
/*     */   
/*     */ 
/*  57 */   private String defaultThemeName = "theme";
/*     */   
/*     */   public CookieThemeResolver()
/*     */   {
/*  61 */     setCookieName(DEFAULT_COOKIE_NAME);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultThemeName(String defaultThemeName)
/*     */   {
/*  69 */     this.defaultThemeName = defaultThemeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDefaultThemeName()
/*     */   {
/*  76 */     return this.defaultThemeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String resolveThemeName(HttpServletRequest request)
/*     */   {
/*  83 */     String themeName = (String)request.getAttribute(THEME_REQUEST_ATTRIBUTE_NAME);
/*  84 */     if (themeName != null) {
/*  85 */       return themeName;
/*     */     }
/*     */     
/*     */ 
/*  89 */     Cookie cookie = WebUtils.getCookie(request, getCookieName());
/*  90 */     if (cookie != null) {
/*  91 */       String value = cookie.getValue();
/*  92 */       if (StringUtils.hasText(value)) {
/*  93 */         themeName = value;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  98 */     if (themeName == null) {
/*  99 */       themeName = getDefaultThemeName();
/*     */     }
/* 101 */     request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
/* 102 */     return themeName;
/*     */   }
/*     */   
/*     */   public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
/*     */   {
/* 107 */     if (StringUtils.hasText(themeName))
/*     */     {
/* 109 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, themeName);
/* 110 */       addCookie(response, themeName);
/*     */     }
/*     */     else
/*     */     {
/* 114 */       request.setAttribute(THEME_REQUEST_ATTRIBUTE_NAME, getDefaultThemeName());
/* 115 */       removeCookie(response);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\theme\CookieThemeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */