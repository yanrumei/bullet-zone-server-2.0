/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.web.filter.GenericFilterBean;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceUrlEncodingFilter
/*     */   extends GenericFilterBean
/*     */ {
/*  48 */   private static final Log logger = LogFactory.getLog(ResourceUrlEncodingFilter.class);
/*     */   
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
/*     */     throws IOException, ServletException
/*     */   {
/*  54 */     if ((!(request instanceof HttpServletRequest)) || (!(response instanceof HttpServletResponse))) {
/*  55 */       throw new ServletException("ResourceUrlEncodingFilter just supports HTTP requests");
/*     */     }
/*  57 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*  58 */     HttpServletResponse httpResponse = (HttpServletResponse)response;
/*  59 */     filterChain.doFilter(httpRequest, new ResourceUrlEncodingResponseWrapper(httpRequest, httpResponse));
/*     */   }
/*     */   
/*     */ 
/*     */   private static class ResourceUrlEncodingResponseWrapper
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     private Integer indexLookupPath;
/*     */     private String prefixLookupPath;
/*     */     
/*     */     public ResourceUrlEncodingResponseWrapper(HttpServletRequest request, HttpServletResponse wrapped)
/*     */     {
/*  73 */       super();
/*  74 */       this.request = request;
/*     */     }
/*     */     
/*     */     public String encodeURL(String url)
/*     */     {
/*  79 */       ResourceUrlProvider resourceUrlProvider = getResourceUrlProvider();
/*  80 */       if (resourceUrlProvider == null) {
/*  81 */         ResourceUrlEncodingFilter.logger.debug("Request attribute exposing ResourceUrlProvider not found");
/*  82 */         return super.encodeURL(url);
/*     */       }
/*     */       
/*  85 */       initLookupPath(resourceUrlProvider);
/*  86 */       if (url.startsWith(this.prefixLookupPath)) {
/*  87 */         int suffixIndex = getQueryParamsIndex(url);
/*  88 */         String suffix = url.substring(suffixIndex);
/*  89 */         String lookupPath = url.substring(this.indexLookupPath.intValue(), suffixIndex);
/*  90 */         lookupPath = resourceUrlProvider.getForLookupPath(lookupPath);
/*  91 */         if (lookupPath != null) {
/*  92 */           return super.encodeURL(this.prefixLookupPath + lookupPath + suffix);
/*     */         }
/*     */       }
/*     */       
/*  96 */       return super.encodeURL(url);
/*     */     }
/*     */     
/*     */     private ResourceUrlProvider getResourceUrlProvider() {
/* 100 */       return (ResourceUrlProvider)this.request.getAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
/*     */     }
/*     */     
/*     */     private void initLookupPath(ResourceUrlProvider urlProvider)
/*     */     {
/* 105 */       if (this.indexLookupPath == null) {
/* 106 */         UrlPathHelper pathHelper = urlProvider.getUrlPathHelper();
/* 107 */         String requestUri = pathHelper.getRequestUri(this.request);
/* 108 */         String lookupPath = pathHelper.getLookupPathForRequest(this.request);
/* 109 */         this.indexLookupPath = Integer.valueOf(requestUri.lastIndexOf(lookupPath));
/* 110 */         this.prefixLookupPath = requestUri.substring(0, this.indexLookupPath.intValue());
/*     */         
/* 112 */         if (("/".equals(lookupPath)) && (!"/".equals(requestUri))) {
/* 113 */           String contextPath = pathHelper.getContextPath(this.request);
/* 114 */           if (requestUri.equals(contextPath)) {
/* 115 */             this.indexLookupPath = Integer.valueOf(requestUri.length());
/* 116 */             this.prefixLookupPath = requestUri;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private int getQueryParamsIndex(String url) {
/* 123 */       int index = url.indexOf("?");
/* 124 */       return index > 0 ? index : url.length();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\ResourceUrlEncodingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */