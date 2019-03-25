/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.support.ServletContextResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ResourceServlet
/*     */   extends HttpServletBean
/*     */ {
/*     */   public static final String RESOURCE_URL_DELIMITERS = ",; \t\n";
/*     */   public static final String RESOURCE_PARAM_NAME = "resource";
/*     */   private String defaultUrl;
/*     */   private String allowedResources;
/*     */   private String contentType;
/* 114 */   private boolean applyLastModified = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathMatcher pathMatcher;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long startupTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultUrl(String defaultUrl)
/*     */   {
/* 131 */     this.defaultUrl = defaultUrl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowedResources(String allowedResources)
/*     */   {
/* 140 */     this.allowedResources = allowedResources;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/* 152 */     this.contentType = contentType;
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
/*     */   public void setApplyLastModified(boolean applyLastModified)
/*     */   {
/* 167 */     this.applyLastModified = applyLastModified;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initServletBean()
/*     */   {
/* 176 */     this.pathMatcher = getPathMatcher();
/* 177 */     this.startupTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PathMatcher getPathMatcher()
/*     */   {
/* 187 */     return new AntPathMatcher();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/* 200 */     String resourceUrl = determineResourceUrl(request);
/*     */     
/* 202 */     if (resourceUrl != null) {
/*     */       try {
/* 204 */         doInclude(request, response, resourceUrl);
/*     */       }
/*     */       catch (ServletException ex) {
/* 207 */         if (this.logger.isWarnEnabled()) {
/* 208 */           this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex);
/*     */         }
/*     */         
/* 211 */         if (!includeDefaultUrl(request, response)) {
/* 212 */           throw ex;
/*     */         }
/*     */       }
/*     */       catch (IOException ex) {
/* 216 */         if (this.logger.isWarnEnabled()) {
/* 217 */           this.logger.warn("Failed to include content of resource [" + resourceUrl + "]", ex);
/*     */         }
/*     */         
/* 220 */         if (!includeDefaultUrl(request, response)) {
/* 221 */           throw ex;
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 227 */     else if (!includeDefaultUrl(request, response)) {
/* 228 */       throw new ServletException("No target resource URL found for request");
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
/*     */   protected String determineResourceUrl(HttpServletRequest request)
/*     */   {
/* 241 */     return request.getParameter("resource");
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
/*     */   private boolean includeDefaultUrl(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/* 255 */     if (this.defaultUrl == null) {
/* 256 */       return false;
/*     */     }
/* 258 */     doInclude(request, response, this.defaultUrl);
/* 259 */     return true;
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
/*     */   private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
/*     */     throws ServletException, IOException
/*     */   {
/* 273 */     if (this.contentType != null) {
/* 274 */       response.setContentType(this.contentType);
/*     */     }
/* 276 */     String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
/* 277 */     for (String url : resourceUrls) {
/* 278 */       String path = StringUtils.cleanPath(url);
/*     */       
/* 280 */       if ((this.allowedResources != null) && (!this.pathMatcher.match(this.allowedResources, path))) {
/* 281 */         throw new ServletException("Resource [" + path + "] does not match allowed pattern [" + this.allowedResources + "]");
/*     */       }
/*     */       
/* 284 */       if (this.logger.isDebugEnabled()) {
/* 285 */         this.logger.debug("Including resource [" + path + "]");
/*     */       }
/* 287 */       RequestDispatcher rd = request.getRequestDispatcher(path);
/* 288 */       rd.include(request, response);
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
/*     */   protected final long getLastModified(HttpServletRequest request)
/*     */   {
/* 307 */     if (this.applyLastModified) {
/* 308 */       String resourceUrl = determineResourceUrl(request);
/* 309 */       if (resourceUrl == null) {
/* 310 */         resourceUrl = this.defaultUrl;
/*     */       }
/* 312 */       if (resourceUrl != null) {
/* 313 */         String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, ",; \t\n");
/* 314 */         long latestTimestamp = -1L;
/* 315 */         for (String url : resourceUrls) {
/* 316 */           long timestamp = getFileTimestamp(url);
/* 317 */           if (timestamp > latestTimestamp) {
/* 318 */             latestTimestamp = timestamp;
/*     */           }
/*     */         }
/* 321 */         return latestTimestamp > this.startupTime ? latestTimestamp : this.startupTime;
/*     */       }
/*     */     }
/* 324 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long getFileTimestamp(String resourceUrl)
/*     */   {
/* 333 */     ServletContextResource resource = new ServletContextResource(getServletContext(), resourceUrl);
/*     */     try {
/* 335 */       long lastModifiedTime = resource.lastModified();
/* 336 */       if (this.logger.isDebugEnabled()) {
/* 337 */         this.logger.debug("Last-modified timestamp of " + resource + " is " + lastModifiedTime);
/*     */       }
/* 339 */       return lastModifiedTime;
/*     */     }
/*     */     catch (IOException ex) {
/* 342 */       if (this.logger.isWarnEnabled()) {
/* 343 */         this.logger.warn("Couldn't retrieve last-modified timestamp of " + resource + " - using ResourceServlet startup time");
/*     */       }
/*     */     }
/* 346 */     return -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\ResourceServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */