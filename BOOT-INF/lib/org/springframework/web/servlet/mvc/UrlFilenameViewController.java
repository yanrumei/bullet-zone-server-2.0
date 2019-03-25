/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlFilenameViewController
/*     */   extends AbstractUrlViewController
/*     */ {
/*  51 */   private String prefix = "";
/*     */   
/*  53 */   private String suffix = "";
/*     */   
/*     */ 
/*  56 */   private final Map<String, String> viewNameCache = new ConcurrentHashMap(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/*  64 */     this.prefix = (prefix != null ? prefix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getPrefix()
/*     */   {
/*  71 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuffix(String suffix)
/*     */   {
/*  79 */     this.suffix = (suffix != null ? suffix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getSuffix()
/*     */   {
/*  86 */     return this.suffix;
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
/*     */   protected String getViewNameForRequest(HttpServletRequest request)
/*     */   {
/*  99 */     String uri = extractOperableUrl(request);
/* 100 */     return getViewNameForUrlPath(uri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String extractOperableUrl(HttpServletRequest request)
/*     */   {
/* 110 */     String urlPath = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/* 111 */     if (!StringUtils.hasText(urlPath)) {
/* 112 */       urlPath = getUrlPathHelper().getLookupPathForRequest(request);
/*     */     }
/* 114 */     return urlPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getViewNameForUrlPath(String uri)
/*     */   {
/* 126 */     String viewName = (String)this.viewNameCache.get(uri);
/* 127 */     if (viewName == null) {
/* 128 */       viewName = extractViewNameFromUrlPath(uri);
/* 129 */       viewName = postProcessViewName(viewName);
/* 130 */       this.viewNameCache.put(uri, viewName);
/*     */     }
/* 132 */     return viewName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String extractViewNameFromUrlPath(String uri)
/*     */   {
/* 141 */     int start = uri.charAt(0) == '/' ? 1 : 0;
/* 142 */     int lastIndex = uri.lastIndexOf(".");
/* 143 */     int end = lastIndex < 0 ? uri.length() : lastIndex;
/* 144 */     return uri.substring(start, end);
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
/*     */   protected String postProcessViewName(String viewName)
/*     */   {
/* 159 */     return getPrefix() + viewName + getSuffix();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\UrlFilenameViewController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */