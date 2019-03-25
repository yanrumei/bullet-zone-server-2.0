/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.servlet.LocaleResolver;
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
/*     */ public class AcceptHeaderLocaleResolver
/*     */   implements LocaleResolver
/*     */ {
/*  43 */   private final List<Locale> supportedLocales = new ArrayList(4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Locale defaultLocale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSupportedLocales(List<Locale> locales)
/*     */   {
/*  56 */     this.supportedLocales.clear();
/*  57 */     if (locales != null) {
/*  58 */       this.supportedLocales.addAll(locales);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Locale> getSupportedLocales()
/*     */   {
/*  67 */     return this.supportedLocales;
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
/*     */   public void setDefaultLocale(Locale defaultLocale)
/*     */   {
/*  80 */     this.defaultLocale = defaultLocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Locale getDefaultLocale()
/*     */   {
/*  88 */     return this.defaultLocale;
/*     */   }
/*     */   
/*     */ 
/*     */   public Locale resolveLocale(HttpServletRequest request)
/*     */   {
/*  94 */     Locale defaultLocale = getDefaultLocale();
/*  95 */     if ((defaultLocale != null) && (request.getHeader("Accept-Language") == null)) {
/*  96 */       return defaultLocale;
/*     */     }
/*  98 */     Locale requestLocale = request.getLocale();
/*  99 */     if (isSupportedLocale(requestLocale)) {
/* 100 */       return requestLocale;
/*     */     }
/* 102 */     Locale supportedLocale = findSupportedLocale(request);
/* 103 */     if (supportedLocale != null) {
/* 104 */       return supportedLocale;
/*     */     }
/* 106 */     return defaultLocale != null ? defaultLocale : requestLocale;
/*     */   }
/*     */   
/*     */   private boolean isSupportedLocale(Locale locale) {
/* 110 */     List<Locale> supportedLocales = getSupportedLocales();
/* 111 */     return (supportedLocales.isEmpty()) || (supportedLocales.contains(locale));
/*     */   }
/*     */   
/*     */   private Locale findSupportedLocale(HttpServletRequest request) {
/* 115 */     Enumeration<Locale> requestLocales = request.getLocales();
/* 116 */     while (requestLocales.hasMoreElements()) {
/* 117 */       Locale locale = (Locale)requestLocales.nextElement();
/* 118 */       if (getSupportedLocales().contains(locale)) {
/* 119 */         return locale;
/*     */       }
/*     */     }
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/*     */   {
/* 127 */     throw new UnsupportedOperationException("Cannot change HTTP accept header - use a different locale resolution strategy");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\AcceptHeaderLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */