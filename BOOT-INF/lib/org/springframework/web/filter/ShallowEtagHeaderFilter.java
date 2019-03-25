/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.web.util.ContentCachingResponseWrapper;
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
/*     */ public class ShallowEtagHeaderFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   private static final String HEADER_ETAG = "ETag";
/*     */   private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*     */   private static final String DIRECTIVE_NO_STORE = "no-store";
/*  62 */   private static final String STREAMING_ATTRIBUTE = ShallowEtagHeaderFilter.class.getName() + ".STREAMING";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private static final boolean servlet3Present = ClassUtils.hasMethod(HttpServletResponse.class, "getHeader", new Class[] { String.class });
/*     */   
/*  69 */   private boolean writeWeakETag = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWriteWeakETag(boolean writeWeakETag)
/*     */   {
/*  80 */     this.writeWeakETag = writeWeakETag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWriteWeakETag()
/*     */   {
/*  88 */     return this.writeWeakETag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldNotFilterAsyncDispatch()
/*     */   {
/*  98 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*     */     throws ServletException, IOException
/*     */   {
/* 105 */     HttpServletResponse responseToUse = response;
/* 106 */     if ((!isAsyncDispatch(request)) && (!(response instanceof ContentCachingResponseWrapper))) {
/* 107 */       responseToUse = new HttpStreamingAwareContentCachingResponseWrapper(response, request);
/*     */     }
/*     */     
/* 110 */     filterChain.doFilter(request, responseToUse);
/*     */     
/* 112 */     if ((!isAsyncStarted(request)) && (!isContentCachingDisabled(request))) {
/* 113 */       updateResponse(request, responseToUse);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
/*     */   {
/* 119 */     ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper)WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
/* 120 */     Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
/* 121 */     HttpServletResponse rawResponse = (HttpServletResponse)responseWrapper.getResponse();
/* 122 */     int statusCode = responseWrapper.getStatusCode();
/*     */     
/* 124 */     if (rawResponse.isCommitted()) {
/* 125 */       responseWrapper.copyBodyToResponse();
/*     */     }
/* 127 */     else if (isEligibleForEtag(request, responseWrapper, statusCode, responseWrapper.getContentInputStream())) {
/* 128 */       String responseETag = generateETagHeaderValue(responseWrapper.getContentInputStream(), this.writeWeakETag);
/* 129 */       rawResponse.setHeader("ETag", responseETag);
/* 130 */       String requestETag = request.getHeader("If-None-Match");
/* 131 */       if ((requestETag != null) && (
/* 132 */         (responseETag.equals(requestETag)) || 
/* 133 */         (responseETag.replaceFirst("^W/", "").equals(requestETag.replaceFirst("^W/", ""))) || 
/* 134 */         ("*".equals(requestETag)))) {
/* 135 */         if (this.logger.isTraceEnabled()) {
/* 136 */           this.logger.trace("ETag [" + responseETag + "] equal to If-None-Match, sending 304");
/*     */         }
/* 138 */         rawResponse.setStatus(304);
/*     */       }
/*     */       else {
/* 141 */         if (this.logger.isTraceEnabled()) {
/* 142 */           this.logger.trace("ETag [" + responseETag + "] not equal to If-None-Match [" + requestETag + "], sending normal response");
/*     */         }
/*     */         
/* 145 */         responseWrapper.copyBodyToResponse();
/*     */       }
/*     */     }
/*     */     else {
/* 149 */       if (this.logger.isTraceEnabled()) {
/* 150 */         this.logger.trace("Response with status code [" + statusCode + "] not eligible for ETag");
/*     */       }
/* 152 */       responseWrapper.copyBodyToResponse();
/*     */     }
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
/*     */   protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response, int responseStatusCode, InputStream inputStream)
/*     */   {
/* 173 */     String method = request.getMethod();
/* 174 */     if ((responseStatusCode >= 200) && (responseStatusCode < 300) && 
/* 175 */       (HttpMethod.GET.matches(method)))
/*     */     {
/* 177 */       String cacheControl = null;
/* 178 */       if (servlet3Present) {
/* 179 */         cacheControl = response.getHeader("Cache-Control");
/*     */       }
/* 181 */       if ((cacheControl == null) || (!cacheControl.contains("no-store"))) {
/* 182 */         return true;
/*     */       }
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak)
/*     */     throws IOException
/*     */   {
/* 198 */     StringBuilder builder = new StringBuilder(37);
/* 199 */     if (isWeak) {
/* 200 */       builder.append("W/");
/*     */     }
/* 202 */     builder.append("\"0");
/* 203 */     DigestUtils.appendMd5DigestAsHex(inputStream, builder);
/* 204 */     builder.append('"');
/* 205 */     return builder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void disableContentCaching(ServletRequest request)
/*     */   {
/* 217 */     Assert.notNull(request, "ServletRequest must not be null");
/* 218 */     request.setAttribute(STREAMING_ATTRIBUTE, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   private static boolean isContentCachingDisabled(HttpServletRequest request) {
/* 222 */     return request.getAttribute(STREAMING_ATTRIBUTE) != null;
/*     */   }
/*     */   
/*     */   private static class HttpStreamingAwareContentCachingResponseWrapper extends ContentCachingResponseWrapper
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     public HttpStreamingAwareContentCachingResponseWrapper(HttpServletResponse response, HttpServletRequest request)
/*     */     {
/* 231 */       super();
/* 232 */       this.request = request;
/*     */     }
/*     */     
/*     */     public ServletOutputStream getOutputStream() throws IOException
/*     */     {
/* 237 */       return useRawResponse() ? getResponse().getOutputStream() : super.getOutputStream();
/*     */     }
/*     */     
/*     */     public PrintWriter getWriter() throws IOException
/*     */     {
/* 242 */       return useRawResponse() ? getResponse().getWriter() : super.getWriter();
/*     */     }
/*     */     
/*     */     private boolean useRawResponse() {
/* 246 */       return ShallowEtagHeaderFilter.isContentCachingDisabled(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\ShallowEtagHeaderFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */