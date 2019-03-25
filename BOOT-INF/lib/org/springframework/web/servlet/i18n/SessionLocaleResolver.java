/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
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
/*     */ public class SessionLocaleResolver
/*     */   extends AbstractLocaleContextResolver
/*     */ {
/*  69 */   public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   public static final String TIME_ZONE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".TIME_ZONE";
/*     */   
/*     */ 
/*  82 */   private String localeAttributeName = LOCALE_SESSION_ATTRIBUTE_NAME;
/*     */   
/*  84 */   private String timeZoneAttributeName = TIME_ZONE_SESSION_ATTRIBUTE_NAME;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocaleAttributeName(String localeAttributeName)
/*     */   {
/*  94 */     this.localeAttributeName = localeAttributeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeZoneAttributeName(String timeZoneAttributeName)
/*     */   {
/* 104 */     this.timeZoneAttributeName = timeZoneAttributeName;
/*     */   }
/*     */   
/*     */ 
/*     */   public Locale resolveLocale(HttpServletRequest request)
/*     */   {
/* 110 */     Locale locale = (Locale)WebUtils.getSessionAttribute(request, this.localeAttributeName);
/* 111 */     if (locale == null) {
/* 112 */       locale = determineDefaultLocale(request);
/*     */     }
/* 114 */     return locale;
/*     */   }
/*     */   
/*     */   public LocaleContext resolveLocaleContext(final HttpServletRequest request)
/*     */   {
/* 119 */     new TimeZoneAwareLocaleContext()
/*     */     {
/*     */       public Locale getLocale() {
/* 122 */         Locale locale = (Locale)WebUtils.getSessionAttribute(request, SessionLocaleResolver.this.localeAttributeName);
/* 123 */         if (locale == null) {
/* 124 */           locale = SessionLocaleResolver.this.determineDefaultLocale(request);
/*     */         }
/* 126 */         return locale;
/*     */       }
/*     */       
/*     */       public TimeZone getTimeZone() {
/* 130 */         TimeZone timeZone = (TimeZone)WebUtils.getSessionAttribute(request, SessionLocaleResolver.this.timeZoneAttributeName);
/* 131 */         if (timeZone == null) {
/* 132 */           timeZone = SessionLocaleResolver.this.determineDefaultTimeZone(request);
/*     */         }
/* 134 */         return timeZone;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext)
/*     */   {
/* 141 */     Locale locale = null;
/* 142 */     TimeZone timeZone = null;
/* 143 */     if (localeContext != null) {
/* 144 */       locale = localeContext.getLocale();
/* 145 */       if ((localeContext instanceof TimeZoneAwareLocaleContext)) {
/* 146 */         timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     }
/* 149 */     WebUtils.setSessionAttribute(request, this.localeAttributeName, locale);
/* 150 */     WebUtils.setSessionAttribute(request, this.timeZoneAttributeName, timeZone);
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
/*     */   protected Locale determineDefaultLocale(HttpServletRequest request)
/*     */   {
/* 165 */     Locale defaultLocale = getDefaultLocale();
/* 166 */     if (defaultLocale == null) {
/* 167 */       defaultLocale = request.getLocale();
/*     */     }
/* 169 */     return defaultLocale;
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
/*     */   protected TimeZone determineDefaultTimeZone(HttpServletRequest request)
/*     */   {
/* 182 */     return getDefaultTimeZone();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\SessionLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */