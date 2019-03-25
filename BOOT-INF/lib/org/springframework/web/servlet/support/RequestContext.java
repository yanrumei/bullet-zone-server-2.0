/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.jsp.jstl.core.Config;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.ResourceBundleThemeSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.web.bind.EscapedErrors;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
/*     */ import org.springframework.web.servlet.LocaleResolver;
/*     */ import org.springframework.web.servlet.ThemeResolver;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ import org.springframework.web.util.UriTemplate;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class RequestContext
/*     */ {
/*     */   public static final String DEFAULT_THEME_NAME = "theme";
/*  90 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";
/*     */   
/*     */ 
/*  93 */   protected static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", RequestContext.class
/*  94 */     .getClassLoader());
/*     */   
/*     */ 
/*     */   private HttpServletRequest request;
/*     */   
/*     */ 
/*     */   private HttpServletResponse response;
/*     */   
/*     */ 
/*     */   private Map<String, Object> model;
/*     */   
/*     */ 
/*     */   private WebApplicationContext webApplicationContext;
/*     */   
/*     */ 
/*     */   private Locale locale;
/*     */   
/*     */ 
/*     */   private TimeZone timeZone;
/*     */   
/*     */ 
/*     */   private Theme theme;
/*     */   
/*     */ 
/*     */   private Boolean defaultHtmlEscape;
/*     */   
/*     */ 
/*     */   private Boolean responseEncodedHtmlEscape;
/*     */   
/*     */   private UrlPathHelper urlPathHelper;
/*     */   
/*     */   private RequestDataValueProcessor requestDataValueProcessor;
/*     */   
/*     */   private Map<String, Errors> errorsMap;
/*     */   
/*     */ 
/*     */   public RequestContext(HttpServletRequest request)
/*     */   {
/* 132 */     initContext(request, null, null, null);
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
/*     */   public RequestContext(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 147 */     initContext(request, response, null, null);
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
/*     */   public RequestContext(HttpServletRequest request, ServletContext servletContext)
/*     */   {
/* 163 */     initContext(request, null, servletContext, null);
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
/*     */   public RequestContext(HttpServletRequest request, Map<String, Object> model)
/*     */   {
/* 178 */     initContext(request, null, null, model);
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
/*     */   public RequestContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Map<String, Object> model)
/*     */   {
/* 198 */     initContext(request, response, servletContext, model);
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
/*     */   protected RequestContext() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Map<String, Object> model)
/*     */   {
/* 225 */     this.request = request;
/* 226 */     this.response = response;
/* 227 */     this.model = model;
/*     */     
/*     */ 
/*     */ 
/* 231 */     this.webApplicationContext = ((WebApplicationContext)request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE));
/* 232 */     if (this.webApplicationContext == null) {
/* 233 */       this.webApplicationContext = RequestContextUtils.findWebApplicationContext(request, servletContext);
/* 234 */       if (this.webApplicationContext == null) {
/* 235 */         throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet request and no ContextLoaderListener registered?");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 241 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
/* 242 */     if ((localeResolver instanceof LocaleContextResolver)) {
/* 243 */       LocaleContext localeContext = ((LocaleContextResolver)localeResolver).resolveLocaleContext(request);
/* 244 */       this.locale = localeContext.getLocale();
/* 245 */       if ((localeContext instanceof TimeZoneAwareLocaleContext)) {
/* 246 */         this.timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     }
/* 249 */     else if (localeResolver != null)
/*     */     {
/* 251 */       this.locale = localeResolver.resolveLocale(request);
/*     */     }
/*     */     
/*     */ 
/* 255 */     if (this.locale == null) {
/* 256 */       this.locale = getFallbackLocale();
/*     */     }
/* 258 */     if (this.timeZone == null) {
/* 259 */       this.timeZone = getFallbackTimeZone();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 264 */     this.defaultHtmlEscape = WebUtils.getDefaultHtmlEscape(this.webApplicationContext.getServletContext());
/*     */     
/*     */ 
/*     */ 
/* 268 */     this.responseEncodedHtmlEscape = WebUtils.getResponseEncodedHtmlEscape(this.webApplicationContext.getServletContext());
/*     */     
/* 270 */     this.urlPathHelper = new UrlPathHelper();
/*     */     
/* 272 */     if (this.webApplicationContext.containsBean("requestDataValueProcessor")) {
/* 273 */       this.requestDataValueProcessor = ((RequestDataValueProcessor)this.webApplicationContext.getBean("requestDataValueProcessor", RequestDataValueProcessor.class));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HttpServletRequest getRequest()
/*     */   {
/* 283 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final ServletContext getServletContext()
/*     */   {
/* 290 */     return this.webApplicationContext.getServletContext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final WebApplicationContext getWebApplicationContext()
/*     */   {
/* 297 */     return this.webApplicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final MessageSource getMessageSource()
/*     */   {
/* 304 */     return this.webApplicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Map<String, Object> getModel()
/*     */   {
/* 312 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Locale getLocale()
/*     */   {
/* 322 */     return this.locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZone getTimeZone()
/*     */   {
/* 332 */     return this.timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Locale getFallbackLocale()
/*     */   {
/* 343 */     if (jstlPresent) {
/* 344 */       Locale locale = JstlLocaleResolver.getJstlLocale(getRequest(), getServletContext());
/* 345 */       if (locale != null) {
/* 346 */         return locale;
/*     */       }
/*     */     }
/* 349 */     return getRequest().getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TimeZone getFallbackTimeZone()
/*     */   {
/* 359 */     if (jstlPresent) {
/* 360 */       TimeZone timeZone = JstlLocaleResolver.getJstlTimeZone(getRequest(), getServletContext());
/* 361 */       if (timeZone != null) {
/* 362 */         return timeZone;
/*     */       }
/*     */     }
/* 365 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeLocale(Locale locale)
/*     */   {
/* 376 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
/* 377 */     if (localeResolver == null) {
/* 378 */       throw new IllegalStateException("Cannot change locale if no LocaleResolver configured");
/*     */     }
/* 380 */     localeResolver.setLocale(this.request, this.response, locale);
/* 381 */     this.locale = locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeLocale(Locale locale, TimeZone timeZone)
/*     */   {
/* 393 */     LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
/* 394 */     if (!(localeResolver instanceof LocaleContextResolver)) {
/* 395 */       throw new IllegalStateException("Cannot change locale context if no LocaleContextResolver configured");
/*     */     }
/* 397 */     ((LocaleContextResolver)localeResolver).setLocaleContext(this.request, this.response, new SimpleTimeZoneAwareLocaleContext(locale, timeZone));
/*     */     
/* 399 */     this.locale = locale;
/* 400 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Theme getTheme()
/*     */   {
/* 408 */     if (this.theme == null)
/*     */     {
/* 410 */       this.theme = RequestContextUtils.getTheme(this.request);
/* 411 */       if (this.theme == null)
/*     */       {
/* 413 */         this.theme = getFallbackTheme();
/*     */       }
/*     */     }
/* 416 */     return this.theme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Theme getFallbackTheme()
/*     */   {
/* 425 */     ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
/* 426 */     if (themeSource == null) {
/* 427 */       themeSource = new ResourceBundleThemeSource();
/*     */     }
/* 429 */     Theme theme = themeSource.getTheme("theme");
/* 430 */     if (theme == null) {
/* 431 */       throw new IllegalStateException("No theme defined and no fallback theme found");
/*     */     }
/* 433 */     return theme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeTheme(Theme theme)
/*     */   {
/* 443 */     ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
/* 444 */     if (themeResolver == null) {
/* 445 */       throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
/*     */     }
/* 447 */     themeResolver.setThemeName(this.request, this.response, theme != null ? theme.getName() : null);
/* 448 */     this.theme = theme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void changeTheme(String themeName)
/*     */   {
/* 458 */     ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
/* 459 */     if (themeResolver == null) {
/* 460 */       throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
/*     */     }
/* 462 */     themeResolver.setThemeName(this.request, this.response, themeName);
/*     */     
/* 464 */     this.theme = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultHtmlEscape(boolean defaultHtmlEscape)
/*     */   {
/* 473 */     this.defaultHtmlEscape = Boolean.valueOf(defaultHtmlEscape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isDefaultHtmlEscape()
/*     */   {
/* 480 */     return (this.defaultHtmlEscape != null) && (this.defaultHtmlEscape.booleanValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getDefaultHtmlEscape()
/*     */   {
/* 488 */     return this.defaultHtmlEscape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isResponseEncodedHtmlEscape()
/*     */   {
/* 498 */     return (this.responseEncodedHtmlEscape == null) || (this.responseEncodedHtmlEscape.booleanValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getResponseEncodedHtmlEscape()
/*     */   {
/* 508 */     return this.responseEncodedHtmlEscape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 518 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 519 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/* 528 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RequestDataValueProcessor getRequestDataValueProcessor()
/*     */   {
/* 537 */     return this.requestDataValueProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextPath()
/*     */   {
/* 549 */     return this.urlPathHelper.getOriginatingContextPath(this.request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextUrl(String relativeUrl)
/*     */   {
/* 558 */     String url = getContextPath() + relativeUrl;
/* 559 */     if (this.response != null) {
/* 560 */       url = this.response.encodeURL(url);
/*     */     }
/* 562 */     return url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContextUrl(String relativeUrl, Map<String, ?> params)
/*     */   {
/* 574 */     String url = getContextPath() + relativeUrl;
/* 575 */     UriTemplate template = new UriTemplate(url);
/* 576 */     url = template.expand(params).toASCIIString();
/* 577 */     if (this.response != null) {
/* 578 */       url = this.response.encodeURL(url);
/*     */     }
/* 580 */     return url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathToServlet()
/*     */   {
/* 591 */     String path = this.urlPathHelper.getOriginatingContextPath(this.request);
/* 592 */     if (StringUtils.hasText(this.urlPathHelper.getPathWithinServletMapping(this.request))) {
/* 593 */       path = path + this.urlPathHelper.getOriginatingServletPath(this.request);
/*     */     }
/* 595 */     return path;
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
/*     */   public String getRequestUri()
/*     */   {
/* 608 */     return this.urlPathHelper.getOriginatingRequestUri(this.request);
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
/*     */   public String getQueryString()
/*     */   {
/* 621 */     return this.urlPathHelper.getOriginatingQueryString(this.request);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, String defaultMessage)
/*     */   {
/* 631 */     return getMessage(code, null, defaultMessage, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, Object[] args, String defaultMessage)
/*     */   {
/* 642 */     return getMessage(code, args, defaultMessage, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, List<?> args, String defaultMessage)
/*     */   {
/* 653 */     return getMessage(code, args != null ? args.toArray() : null, defaultMessage, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, Object[] args, String defaultMessage, boolean htmlEscape)
/*     */   {
/* 665 */     String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, this.locale);
/* 666 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code)
/*     */     throws NoSuchMessageException
/*     */   {
/* 676 */     return getMessage(code, null, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, Object[] args)
/*     */     throws NoSuchMessageException
/*     */   {
/* 687 */     return getMessage(code, args, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, List<?> args)
/*     */     throws NoSuchMessageException
/*     */   {
/* 698 */     return getMessage(code, args != null ? args.toArray() : null, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(String code, Object[] args, boolean htmlEscape)
/*     */     throws NoSuchMessageException
/*     */   {
/* 710 */     String msg = this.webApplicationContext.getMessage(code, args, this.locale);
/* 711 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(MessageSourceResolvable resolvable)
/*     */     throws NoSuchMessageException
/*     */   {
/* 721 */     return getMessage(resolvable, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape)
/*     */     throws NoSuchMessageException
/*     */   {
/* 732 */     String msg = this.webApplicationContext.getMessage(resolvable, this.locale);
/* 733 */     return htmlEscape ? HtmlUtils.htmlEscape(msg) : msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThemeMessage(String code, String defaultMessage)
/*     */   {
/* 745 */     return getTheme().getMessageSource().getMessage(code, null, defaultMessage, this.locale);
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
/*     */   public String getThemeMessage(String code, Object[] args, String defaultMessage)
/*     */   {
/* 758 */     return getTheme().getMessageSource().getMessage(code, args, defaultMessage, this.locale);
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
/*     */   public String getThemeMessage(String code, List<?> args, String defaultMessage)
/*     */   {
/* 771 */     return getTheme().getMessageSource().getMessage(code, args != null ? args.toArray() : null, defaultMessage, this.locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThemeMessage(String code)
/*     */     throws NoSuchMessageException
/*     */   {
/* 784 */     return getTheme().getMessageSource().getMessage(code, null, this.locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThemeMessage(String code, Object[] args)
/*     */     throws NoSuchMessageException
/*     */   {
/* 797 */     return getTheme().getMessageSource().getMessage(code, args, this.locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThemeMessage(String code, List<?> args)
/*     */     throws NoSuchMessageException
/*     */   {
/* 810 */     return getTheme().getMessageSource().getMessage(code, args != null ? args.toArray() : null, this.locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThemeMessage(MessageSourceResolvable resolvable)
/*     */     throws NoSuchMessageException
/*     */   {
/* 822 */     return getTheme().getMessageSource().getMessage(resolvable, this.locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Errors getErrors(String name)
/*     */   {
/* 831 */     return getErrors(name, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Errors getErrors(String name, boolean htmlEscape)
/*     */   {
/* 841 */     if (this.errorsMap == null) {
/* 842 */       this.errorsMap = new HashMap();
/*     */     }
/* 844 */     Errors errors = (Errors)this.errorsMap.get(name);
/* 845 */     boolean put = false;
/* 846 */     if (errors == null) {
/* 847 */       errors = (Errors)getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
/*     */       
/* 849 */       if ((errors instanceof BindException)) {
/* 850 */         errors = ((BindException)errors).getBindingResult();
/*     */       }
/* 852 */       if (errors == null) {
/* 853 */         return null;
/*     */       }
/* 855 */       put = true;
/*     */     }
/* 857 */     if ((htmlEscape) && (!(errors instanceof EscapedErrors))) {
/* 858 */       errors = new EscapedErrors(errors);
/* 859 */       put = true;
/*     */     }
/* 861 */     else if ((!htmlEscape) && ((errors instanceof EscapedErrors))) {
/* 862 */       errors = ((EscapedErrors)errors).getSource();
/* 863 */       put = true;
/*     */     }
/* 865 */     if (put) {
/* 866 */       this.errorsMap.put(name, errors);
/*     */     }
/* 868 */     return errors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getModelObject(String modelName)
/*     */   {
/* 878 */     if (this.model != null) {
/* 879 */       return this.model.get(modelName);
/*     */     }
/*     */     
/* 882 */     return this.request.getAttribute(modelName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BindStatus getBindStatus(String path)
/*     */     throws IllegalStateException
/*     */   {
/* 893 */     return new BindStatus(this, path, isDefaultHtmlEscape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BindStatus getBindStatus(String path, boolean htmlEscape)
/*     */     throws IllegalStateException
/*     */   {
/* 904 */     return new BindStatus(this, path, htmlEscape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class JstlLocaleResolver
/*     */   {
/*     */     public static Locale getJstlLocale(HttpServletRequest request, ServletContext servletContext)
/*     */     {
/* 915 */       Object localeObject = Config.get(request, "javax.servlet.jsp.jstl.fmt.locale");
/* 916 */       if (localeObject == null) {
/* 917 */         HttpSession session = request.getSession(false);
/* 918 */         if (session != null) {
/* 919 */           localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/*     */         }
/* 921 */         if ((localeObject == null) && (servletContext != null)) {
/* 922 */           localeObject = Config.get(servletContext, "javax.servlet.jsp.jstl.fmt.locale");
/*     */         }
/*     */       }
/* 925 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/*     */     }
/*     */     
/*     */     public static TimeZone getJstlTimeZone(HttpServletRequest request, ServletContext servletContext) {
/* 929 */       Object timeZoneObject = Config.get(request, "javax.servlet.jsp.jstl.fmt.timeZone");
/* 930 */       if (timeZoneObject == null) {
/* 931 */         HttpSession session = request.getSession(false);
/* 932 */         if (session != null) {
/* 933 */           timeZoneObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.timeZone");
/*     */         }
/* 935 */         if ((timeZoneObject == null) && (servletContext != null)) {
/* 936 */           timeZoneObject = Config.get(servletContext, "javax.servlet.jsp.jstl.fmt.timeZone");
/*     */         }
/*     */       }
/* 939 */       return (timeZoneObject instanceof TimeZone) ? (TimeZone)timeZoneObject : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\RequestContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */