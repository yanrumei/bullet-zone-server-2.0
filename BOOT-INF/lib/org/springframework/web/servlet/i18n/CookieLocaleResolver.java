/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.SimpleLocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CookieLocaleResolver
/*     */   extends CookieGenerator
/*     */   implements LocaleContextResolver
/*     */ {
/*  66 */   public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
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
/*  77 */   public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".TIME_ZONE";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final String DEFAULT_COOKIE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
/*     */   
/*     */ 
/*  85 */   private boolean languageTagCompliant = false;
/*     */   
/*     */ 
/*     */   private Locale defaultLocale;
/*     */   
/*     */ 
/*     */   private TimeZone defaultTimeZone;
/*     */   
/*     */ 
/*     */ 
/*     */   public CookieLocaleResolver()
/*     */   {
/*  97 */     setCookieName(DEFAULT_COOKIE_NAME);
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
/*     */   public void setLanguageTagCompliant(boolean languageTagCompliant)
/*     */   {
/* 112 */     this.languageTagCompliant = languageTagCompliant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLanguageTagCompliant()
/*     */   {
/* 121 */     return this.languageTagCompliant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDefaultLocale(Locale defaultLocale)
/*     */   {
/* 128 */     this.defaultLocale = defaultLocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Locale getDefaultLocale()
/*     */   {
/* 136 */     return this.defaultLocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultTimeZone(TimeZone defaultTimeZone)
/*     */   {
/* 144 */     this.defaultTimeZone = defaultTimeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TimeZone getDefaultTimeZone()
/*     */   {
/* 153 */     return this.defaultTimeZone;
/*     */   }
/*     */   
/*     */ 
/*     */   public Locale resolveLocale(HttpServletRequest request)
/*     */   {
/* 159 */     parseLocaleCookieIfNecessary(request);
/* 160 */     return (Locale)request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
/*     */   }
/*     */   
/*     */   public LocaleContext resolveLocaleContext(final HttpServletRequest request)
/*     */   {
/* 165 */     parseLocaleCookieIfNecessary(request);
/* 166 */     new TimeZoneAwareLocaleContext()
/*     */     {
/*     */       public Locale getLocale() {
/* 169 */         return (Locale)request.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME);
/*     */       }
/*     */       
/*     */       public TimeZone getTimeZone() {
/* 173 */         return (TimeZone)request.getAttribute(CookieLocaleResolver.TIME_ZONE_REQUEST_ATTRIBUTE_NAME);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private void parseLocaleCookieIfNecessary(HttpServletRequest request) {
/* 179 */     if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) == null)
/*     */     {
/* 181 */       Cookie cookie = WebUtils.getCookie(request, getCookieName());
/* 182 */       Locale locale = null;
/* 183 */       TimeZone timeZone = null;
/* 184 */       if (cookie != null) {
/* 185 */         String value = cookie.getValue();
/* 186 */         String localePart = value;
/* 187 */         String timeZonePart = null;
/* 188 */         int spaceIndex = localePart.indexOf(' ');
/* 189 */         if (spaceIndex != -1) {
/* 190 */           localePart = value.substring(0, spaceIndex);
/* 191 */           timeZonePart = value.substring(spaceIndex + 1);
/*     */         }
/*     */         try {
/* 194 */           locale = !"-".equals(localePart) ? parseLocaleValue(localePart) : null;
/* 195 */           if (timeZonePart != null) {
/* 196 */             timeZone = StringUtils.parseTimeZoneString(timeZonePart);
/*     */           }
/*     */         }
/*     */         catch (IllegalArgumentException ex) {
/* 200 */           if (request.getAttribute("javax.servlet.error.exception") != null)
/*     */           {
/* 202 */             if (this.logger.isDebugEnabled()) {
/* 203 */               this.logger.debug("Ignoring invalid locale cookie '" + getCookieName() + "' with value [" + value + "] due to error dispatch: " + ex
/* 204 */                 .getMessage());
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 209 */             throw new IllegalStateException("Invalid locale cookie '" + getCookieName() + "' with value [" + value + "]: " + ex.getMessage());
/*     */           }
/*     */         }
/* 212 */         if (this.logger.isDebugEnabled()) {
/* 213 */           this.logger.debug("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale + "'" + (timeZone != null ? " and time zone '" + timeZone
/* 214 */             .getID() + "'" : ""));
/*     */         }
/*     */       }
/* 217 */       request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale != null ? locale : 
/* 218 */         determineDefaultLocale(request));
/* 219 */       request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone != null ? timeZone : 
/* 220 */         determineDefaultTimeZone(request));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/*     */   {
/* 226 */     setLocaleContext(request, response, locale != null ? new SimpleLocaleContext(locale) : null);
/*     */   }
/*     */   
/*     */   public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext)
/*     */   {
/* 231 */     Locale locale = null;
/* 232 */     TimeZone timeZone = null;
/* 233 */     if (localeContext != null) {
/* 234 */       locale = localeContext.getLocale();
/* 235 */       if ((localeContext instanceof TimeZoneAwareLocaleContext)) {
/* 236 */         timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/* 238 */       addCookie(response, (locale != null ? 
/* 239 */         toLocaleValue(locale) : "-") + (timeZone != null ? ' ' + timeZone.getID() : ""));
/*     */     }
/*     */     else {
/* 242 */       removeCookie(response);
/*     */     }
/* 244 */     request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale != null ? locale : 
/* 245 */       determineDefaultLocale(request));
/* 246 */     request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, timeZone != null ? timeZone : 
/* 247 */       determineDefaultTimeZone(request));
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
/*     */   @UsesJava7
/*     */   protected Locale parseLocaleValue(String locale)
/*     */   {
/* 262 */     return isLanguageTagCompliant() ? Locale.forLanguageTag(locale) : StringUtils.parseLocaleString(locale);
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
/*     */   @UsesJava7
/*     */   protected String toLocaleValue(Locale locale)
/*     */   {
/* 276 */     return isLanguageTagCompliant() ? locale.toLanguageTag() : locale.toString();
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
/*     */   protected Locale determineDefaultLocale(HttpServletRequest request)
/*     */   {
/* 290 */     Locale defaultLocale = getDefaultLocale();
/* 291 */     if (defaultLocale == null) {
/* 292 */       defaultLocale = request.getLocale();
/*     */     }
/* 294 */     return defaultLocale;
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
/* 307 */     return getDefaultTimeZone();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\CookieLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */