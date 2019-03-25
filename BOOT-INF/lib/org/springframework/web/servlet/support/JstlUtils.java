/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.jsp.jstl.core.Config;
/*     */ import javax.servlet.jsp.jstl.fmt.LocalizationContext;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.support.MessageSourceResourceBundle;
/*     */ import org.springframework.context.support.ResourceBundleMessageSource;
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
/*     */ public abstract class JstlUtils
/*     */ {
/*     */   public static MessageSource getJstlAwareMessageSource(ServletContext servletContext, MessageSource messageSource)
/*     */   {
/*  56 */     if (servletContext != null) {
/*  57 */       String jstlInitParam = servletContext.getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
/*  58 */       if (jstlInitParam != null)
/*     */       {
/*     */ 
/*     */ 
/*  62 */         ResourceBundleMessageSource jstlBundleWrapper = new ResourceBundleMessageSource();
/*  63 */         jstlBundleWrapper.setBasename(jstlInitParam);
/*  64 */         jstlBundleWrapper.setParentMessageSource(messageSource);
/*  65 */         return jstlBundleWrapper;
/*     */       }
/*     */     }
/*  68 */     return messageSource;
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
/*     */   public static void exposeLocalizationContext(HttpServletRequest request, MessageSource messageSource)
/*     */   {
/*  81 */     Locale jstlLocale = RequestContextUtils.getLocale(request);
/*  82 */     Config.set(request, "javax.servlet.jsp.jstl.fmt.locale", jstlLocale);
/*  83 */     TimeZone timeZone = RequestContextUtils.getTimeZone(request);
/*  84 */     if (timeZone != null) {
/*  85 */       Config.set(request, "javax.servlet.jsp.jstl.fmt.timeZone", timeZone);
/*     */     }
/*  87 */     if (messageSource != null) {
/*  88 */       LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, request);
/*  89 */       Config.set(request, "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void exposeLocalizationContext(RequestContext requestContext)
/*     */   {
/* 101 */     Config.set(requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.locale", requestContext.getLocale());
/* 102 */     TimeZone timeZone = requestContext.getTimeZone();
/* 103 */     if (timeZone != null) {
/* 104 */       Config.set(requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.timeZone", timeZone);
/*     */     }
/* 106 */     MessageSource messageSource = getJstlAwareMessageSource(requestContext
/* 107 */       .getServletContext(), requestContext.getMessageSource());
/* 108 */     LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, requestContext.getRequest());
/* 109 */     Config.set(requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class SpringLocalizationContext
/*     */     extends LocalizationContext
/*     */   {
/*     */     private final MessageSource messageSource;
/*     */     
/*     */     private final HttpServletRequest request;
/*     */     
/*     */ 
/*     */     public SpringLocalizationContext(MessageSource messageSource, HttpServletRequest request)
/*     */     {
/* 124 */       this.messageSource = messageSource;
/* 125 */       this.request = request;
/*     */     }
/*     */     
/*     */     public ResourceBundle getResourceBundle()
/*     */     {
/* 130 */       HttpSession session = this.request.getSession(false);
/* 131 */       if (session != null) {
/* 132 */         Object lcObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.localizationContext");
/* 133 */         if ((lcObject instanceof LocalizationContext)) {
/* 134 */           ResourceBundle lcBundle = ((LocalizationContext)lcObject).getResourceBundle();
/* 135 */           return new MessageSourceResourceBundle(this.messageSource, getLocale(), lcBundle);
/*     */         }
/*     */       }
/* 138 */       return new MessageSourceResourceBundle(this.messageSource, getLocale());
/*     */     }
/*     */     
/*     */     public Locale getLocale()
/*     */     {
/* 143 */       HttpSession session = this.request.getSession(false);
/* 144 */       if (session != null) {
/* 145 */         Object localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/* 146 */         if ((localeObject instanceof Locale)) {
/* 147 */           return (Locale)localeObject;
/*     */         }
/*     */       }
/* 150 */       return RequestContextUtils.getLocale(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\JstlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */