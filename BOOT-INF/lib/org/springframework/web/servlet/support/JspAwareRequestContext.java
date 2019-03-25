/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.jstl.core.Config;
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
/*     */ public class JspAwareRequestContext
/*     */   extends RequestContext
/*     */ {
/*     */   private PageContext pageContext;
/*     */   
/*     */   public JspAwareRequestContext(PageContext pageContext)
/*     */   {
/*  49 */     initContext(pageContext, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JspAwareRequestContext(PageContext pageContext, Map<String, Object> model)
/*     */   {
/*  60 */     initContext(pageContext, model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initContext(PageContext pageContext, Map<String, Object> model)
/*     */   {
/*  71 */     if (!(pageContext.getRequest() instanceof HttpServletRequest)) {
/*  72 */       throw new IllegalArgumentException("RequestContext only supports HTTP requests");
/*     */     }
/*  74 */     this.pageContext = pageContext;
/*  75 */     initContext((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse(), pageContext
/*  76 */       .getServletContext(), model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PageContext getPageContext()
/*     */   {
/*  85 */     return this.pageContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Locale getFallbackLocale()
/*     */   {
/*  95 */     if (jstlPresent) {
/*  96 */       Locale locale = JstlPageLocaleResolver.getJstlLocale(getPageContext());
/*  97 */       if (locale != null) {
/*  98 */         return locale;
/*     */       }
/*     */     }
/* 101 */     return getRequest().getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TimeZone getFallbackTimeZone()
/*     */   {
/* 110 */     if (jstlPresent) {
/* 111 */       TimeZone timeZone = JstlPageLocaleResolver.getJstlTimeZone(getPageContext());
/* 112 */       if (timeZone != null) {
/* 113 */         return timeZone;
/*     */       }
/*     */     }
/* 116 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class JstlPageLocaleResolver
/*     */   {
/*     */     public static Locale getJstlLocale(PageContext pageContext)
/*     */     {
/* 127 */       Object localeObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.locale");
/* 128 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/*     */     }
/*     */     
/*     */     public static TimeZone getJstlTimeZone(PageContext pageContext) {
/* 132 */       Object timeZoneObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.timeZone");
/* 133 */       return (timeZoneObject instanceof TimeZone) ? (TimeZone)timeZoneObject : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\JspAwareRequestContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */