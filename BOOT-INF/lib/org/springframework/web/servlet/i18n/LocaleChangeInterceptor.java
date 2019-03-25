/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.UsesJava7;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleChangeInterceptor
/*     */   extends HandlerInterceptorAdapter
/*     */ {
/*     */   public static final String DEFAULT_PARAM_NAME = "locale";
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  53 */   private String paramName = "locale";
/*     */   
/*     */   private String[] httpMethods;
/*     */   
/*  57 */   private boolean ignoreInvalidLocale = false;
/*     */   
/*  59 */   private boolean languageTagCompliant = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParamName(String paramName)
/*     */   {
/*  67 */     this.paramName = paramName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParamName()
/*     */   {
/*  75 */     return this.paramName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHttpMethods(String... httpMethods)
/*     */   {
/*  84 */     this.httpMethods = httpMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getHttpMethods()
/*     */   {
/*  92 */     return this.httpMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreInvalidLocale(boolean ignoreInvalidLocale)
/*     */   {
/* 100 */     this.ignoreInvalidLocale = ignoreInvalidLocale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isIgnoreInvalidLocale()
/*     */   {
/* 108 */     return this.ignoreInvalidLocale;
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
/*     */   public void setLanguageTagCompliant(boolean languageTagCompliant)
/*     */   {
/* 122 */     this.languageTagCompliant = languageTagCompliant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLanguageTagCompliant()
/*     */   {
/* 131 */     return this.languageTagCompliant;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */     throws ServletException
/*     */   {
/* 139 */     String newLocale = request.getParameter(getParamName());
/* 140 */     if ((newLocale != null) && 
/* 141 */       (checkHttpMethod(request.getMethod()))) {
/* 142 */       LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/* 143 */       if (localeResolver == null) {
/* 144 */         throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
/*     */       }
/*     */       try
/*     */       {
/* 148 */         localeResolver.setLocale(request, response, parseLocaleValue(newLocale));
/*     */       }
/*     */       catch (IllegalArgumentException ex) {
/* 151 */         if (isIgnoreInvalidLocale()) {
/* 152 */           this.logger.debug("Ignoring invalid locale value [" + newLocale + "]: " + ex.getMessage());
/*     */         }
/*     */         else {
/* 155 */           throw ex;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkHttpMethod(String currentMethod) {
/* 165 */     String[] configuredMethods = getHttpMethods();
/* 166 */     if (ObjectUtils.isEmpty(configuredMethods)) {
/* 167 */       return true;
/*     */     }
/* 169 */     for (String configuredMethod : configuredMethods) {
/* 170 */       if (configuredMethod.equalsIgnoreCase(currentMethod)) {
/* 171 */         return true;
/*     */       }
/*     */     }
/* 174 */     return false;
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
/*     */   protected Locale parseLocaleValue(String locale)
/*     */   {
/* 188 */     return isLanguageTagCompliant() ? Locale.forLanguageTag(locale) : StringUtils.parseLocaleString(locale);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\i18n\LocaleChangeInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */