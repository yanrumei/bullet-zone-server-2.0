/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.validation.DefaultMessageCodesResolver.Format;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.mvc")
/*     */ public class WebMvcProperties
/*     */ {
/*     */   private DefaultMessageCodesResolver.Format messageCodesResolverFormat;
/*     */   private Locale locale;
/*  53 */   private LocaleResolver localeResolver = LocaleResolver.ACCEPT_HEADER;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String dateFormat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private boolean dispatchTraceRequest = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private boolean dispatchOptionsRequest = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private boolean ignoreDefaultModelOnRedirect = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private boolean throwExceptionIfNoHandlerFound = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private boolean logResolvedException = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private Map<String, MediaType> mediaTypes = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private String staticPathPattern = "/**";
/*     */   
/*  96 */   private final Async async = new Async();
/*     */   
/*  98 */   private final Servlet servlet = new Servlet();
/*     */   
/* 100 */   private final View view = new View();
/*     */   
/*     */   public DefaultMessageCodesResolver.Format getMessageCodesResolverFormat() {
/* 103 */     return this.messageCodesResolverFormat;
/*     */   }
/*     */   
/*     */   public void setMessageCodesResolverFormat(DefaultMessageCodesResolver.Format messageCodesResolverFormat)
/*     */   {
/* 108 */     this.messageCodesResolverFormat = messageCodesResolverFormat;
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 112 */     return this.locale;
/*     */   }
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 116 */     this.locale = locale;
/*     */   }
/*     */   
/*     */   public LocaleResolver getLocaleResolver() {
/* 120 */     return this.localeResolver;
/*     */   }
/*     */   
/*     */   public void setLocaleResolver(LocaleResolver localeResolver) {
/* 124 */     this.localeResolver = localeResolver;
/*     */   }
/*     */   
/*     */   public String getDateFormat() {
/* 128 */     return this.dateFormat;
/*     */   }
/*     */   
/*     */   public void setDateFormat(String dateFormat) {
/* 132 */     this.dateFormat = dateFormat;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreDefaultModelOnRedirect() {
/* 136 */     return this.ignoreDefaultModelOnRedirect;
/*     */   }
/*     */   
/*     */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
/* 140 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*     */   }
/*     */   
/*     */   public boolean isThrowExceptionIfNoHandlerFound() {
/* 144 */     return this.throwExceptionIfNoHandlerFound;
/*     */   }
/*     */   
/*     */   public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound)
/*     */   {
/* 149 */     this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
/*     */   }
/*     */   
/*     */   public boolean isLogResolvedException() {
/* 153 */     return this.logResolvedException;
/*     */   }
/*     */   
/*     */   public void setLogResolvedException(boolean logResolvedException) {
/* 157 */     this.logResolvedException = logResolvedException;
/*     */   }
/*     */   
/*     */   public Map<String, MediaType> getMediaTypes() {
/* 161 */     return this.mediaTypes;
/*     */   }
/*     */   
/*     */   public void setMediaTypes(Map<String, MediaType> mediaTypes) {
/* 165 */     this.mediaTypes = mediaTypes;
/*     */   }
/*     */   
/*     */   public boolean isDispatchOptionsRequest() {
/* 169 */     return this.dispatchOptionsRequest;
/*     */   }
/*     */   
/*     */   public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
/* 173 */     this.dispatchOptionsRequest = dispatchOptionsRequest;
/*     */   }
/*     */   
/*     */   public boolean isDispatchTraceRequest() {
/* 177 */     return this.dispatchTraceRequest;
/*     */   }
/*     */   
/*     */   public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
/* 181 */     this.dispatchTraceRequest = dispatchTraceRequest;
/*     */   }
/*     */   
/*     */   public String getStaticPathPattern() {
/* 185 */     return this.staticPathPattern;
/*     */   }
/*     */   
/*     */   public void setStaticPathPattern(String staticPathPattern) {
/* 189 */     this.staticPathPattern = staticPathPattern;
/*     */   }
/*     */   
/*     */   public Async getAsync() {
/* 193 */     return this.async;
/*     */   }
/*     */   
/*     */   public Servlet getServlet() {
/* 197 */     return this.servlet;
/*     */   }
/*     */   
/*     */   public View getView() {
/* 201 */     return this.view;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Async
/*     */   {
/*     */     private Long requestTimeout;
/*     */     
/*     */ 
/*     */ 
/*     */     public Long getRequestTimeout()
/*     */     {
/* 214 */       return this.requestTimeout;
/*     */     }
/*     */     
/*     */     public void setRequestTimeout(Long requestTimeout) {
/* 218 */       this.requestTimeout = requestTimeout;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Servlet
/*     */   {
/* 228 */     private int loadOnStartup = -1;
/*     */     
/*     */     public int getLoadOnStartup() {
/* 231 */       return this.loadOnStartup;
/*     */     }
/*     */     
/*     */     public void setLoadOnStartup(int loadOnStartup) {
/* 235 */       this.loadOnStartup = loadOnStartup;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class View
/*     */   {
/*     */     private String prefix;
/*     */     
/*     */ 
/*     */     private String suffix;
/*     */     
/*     */ 
/*     */ 
/*     */     public String getPrefix()
/*     */     {
/* 253 */       return this.prefix;
/*     */     }
/*     */     
/*     */     public void setPrefix(String prefix) {
/* 257 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     public String getSuffix() {
/* 261 */       return this.suffix;
/*     */     }
/*     */     
/*     */     public void setSuffix(String suffix) {
/* 265 */       this.suffix = suffix;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum LocaleResolver
/*     */   {
/* 275 */     FIXED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */     ACCEPT_HEADER;
/*     */     
/*     */     private LocaleResolver() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\WebMvcProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */