/*    */ package org.springframework.web.servlet.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.i18n.LocaleContext;
/*    */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
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
/*    */ public class FixedLocaleResolver
/*    */   extends AbstractLocaleContextResolver
/*    */ {
/*    */   public FixedLocaleResolver()
/*    */   {
/* 49 */     setDefaultLocale(Locale.getDefault());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FixedLocaleResolver(Locale locale)
/*    */   {
/* 57 */     setDefaultLocale(locale);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FixedLocaleResolver(Locale locale, TimeZone timeZone)
/*    */   {
/* 66 */     setDefaultLocale(locale);
/* 67 */     setDefaultTimeZone(timeZone);
/*    */   }
/*    */   
/*    */ 
/*    */   public Locale resolveLocale(HttpServletRequest request)
/*    */   {
/* 73 */     Locale locale = getDefaultLocale();
/* 74 */     if (locale == null) {
/* 75 */       locale = Locale.getDefault();
/*    */     }
/* 77 */     return locale;
/*    */   }
/*    */   
/*    */   public LocaleContext resolveLocaleContext(HttpServletRequest request)
/*    */   {
/* 82 */     new TimeZoneAwareLocaleContext()
/*    */     {
/*    */       public Locale getLocale() {
/* 85 */         return FixedLocaleResolver.this.getDefaultLocale();
/*    */       }
/*    */       
/*    */       public TimeZone getTimeZone() {
/* 89 */         return FixedLocaleResolver.this.getDefaultTimeZone();
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext)
/*    */   {
/* 96 */     throw new UnsupportedOperationException("Cannot change fixed locale - use a different locale resolution strategy");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\FixedLocaleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */