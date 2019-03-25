/*    */ package org.springframework.web.servlet.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.i18n.LocaleContext;
/*    */ import org.springframework.context.i18n.SimpleLocaleContext;
/*    */ import org.springframework.web.servlet.LocaleContextResolver;
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
/*    */ public abstract class AbstractLocaleContextResolver
/*    */   extends AbstractLocaleResolver
/*    */   implements LocaleContextResolver
/*    */ {
/*    */   private TimeZone defaultTimeZone;
/*    */   
/*    */   public void setDefaultTimeZone(TimeZone defaultTimeZone)
/*    */   {
/* 48 */     this.defaultTimeZone = defaultTimeZone;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public TimeZone getDefaultTimeZone()
/*    */   {
/* 55 */     return this.defaultTimeZone;
/*    */   }
/*    */   
/*    */ 
/*    */   public Locale resolveLocale(HttpServletRequest request)
/*    */   {
/* 61 */     return resolveLocaleContext(request).getLocale();
/*    */   }
/*    */   
/*    */   public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale)
/*    */   {
/* 66 */     setLocaleContext(request, response, locale != null ? new SimpleLocaleContext(locale) : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\AbstractLocaleContextResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */