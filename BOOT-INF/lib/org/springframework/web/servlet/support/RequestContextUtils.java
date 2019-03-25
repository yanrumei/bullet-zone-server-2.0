/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.web.context.ContextLoader;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.FlashMapManager;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.ThemeResolver;
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
/*     */ 
/*     */ 
/*     */ public abstract class RequestContextUtils
/*     */ {
/*     */   public static final String REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME = "requestDataValueProcessor";
/*     */   
/*     */   @Deprecated
/*     */   public static WebApplicationContext getWebApplicationContext(ServletRequest request)
/*     */     throws IllegalStateException
/*     */   {
/*  75 */     return getWebApplicationContext(request, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static WebApplicationContext getWebApplicationContext(ServletRequest request, ServletContext servletContext)
/*     */     throws IllegalStateException
/*     */   {
/*  99 */     WebApplicationContext webApplicationContext = (WebApplicationContext)request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/* 101 */     if (webApplicationContext == null) {
/* 102 */       if (servletContext == null) {
/* 103 */         throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet request?");
/*     */       }
/* 105 */       webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
/*     */     }
/* 107 */     return webApplicationContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WebApplicationContext findWebApplicationContext(HttpServletRequest request, ServletContext servletContext)
/*     */   {
/* 129 */     WebApplicationContext webApplicationContext = (WebApplicationContext)request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*     */     
/* 131 */     if (webApplicationContext == null) {
/* 132 */       if (servletContext != null) {
/* 133 */         webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
/*     */       }
/* 135 */       if (webApplicationContext == null) {
/* 136 */         webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
/*     */       }
/*     */     }
/* 139 */     return webApplicationContext;
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
/*     */ 
/*     */ 
/*     */   public static WebApplicationContext findWebApplicationContext(HttpServletRequest request)
/*     */   {
/* 158 */     return findWebApplicationContext(request, request.getServletContext());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LocaleResolver getLocaleResolver(HttpServletRequest request)
/*     */   {
/* 168 */     return (LocaleResolver)request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
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
/*     */ 
/*     */ 
/*     */   public static Locale getLocale(HttpServletRequest request)
/*     */   {
/* 187 */     LocaleResolver localeResolver = getLocaleResolver(request);
/* 188 */     return localeResolver != null ? localeResolver.resolveLocale(request) : request.getLocale();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeZone getTimeZone(HttpServletRequest request)
/*     */   {
/* 209 */     LocaleResolver localeResolver = getLocaleResolver(request);
/* 210 */     if ((localeResolver instanceof LocaleContextResolver)) {
/* 211 */       LocaleContext localeContext = ((LocaleContextResolver)localeResolver).resolveLocaleContext(request);
/* 212 */       if ((localeContext instanceof TimeZoneAwareLocaleContext)) {
/* 213 */         return ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     }
/* 216 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ThemeResolver getThemeResolver(HttpServletRequest request)
/*     */   {
/* 226 */     return (ThemeResolver)request.getAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ThemeSource getThemeSource(HttpServletRequest request)
/*     */   {
/* 236 */     return (ThemeSource)request.getAttribute(DispatcherServlet.THEME_SOURCE_ATTRIBUTE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Theme getTheme(HttpServletRequest request)
/*     */   {
/* 247 */     ThemeResolver themeResolver = getThemeResolver(request);
/* 248 */     ThemeSource themeSource = getThemeSource(request);
/* 249 */     if ((themeResolver != null) && (themeSource != null)) {
/* 250 */       String themeName = themeResolver.resolveThemeName(request);
/* 251 */       return themeSource.getTheme(themeName);
/*     */     }
/*     */     
/* 254 */     return null;
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
/*     */   public static Map<String, ?> getInputFlashMap(HttpServletRequest request)
/*     */   {
/* 267 */     return (Map)request.getAttribute(DispatcherServlet.INPUT_FLASH_MAP_ATTRIBUTE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static FlashMap getOutputFlashMap(HttpServletRequest request)
/*     */   {
/* 277 */     return (FlashMap)request.getAttribute(DispatcherServlet.OUTPUT_FLASH_MAP_ATTRIBUTE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static FlashMapManager getFlashMapManager(HttpServletRequest request)
/*     */   {
/* 287 */     return (FlashMapManager)request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\RequestContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */