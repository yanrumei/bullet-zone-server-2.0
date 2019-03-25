/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.RequestToViewNameTranslator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultRequestToViewNameTranslator
/*     */   implements RequestToViewNameTranslator
/*     */ {
/*     */   private static final String SLASH = "/";
/*  61 */   private String prefix = "";
/*     */   
/*  63 */   private String suffix = "";
/*     */   
/*  65 */   private String separator = "/";
/*     */   
/*  67 */   private boolean stripLeadingSlash = true;
/*     */   
/*  69 */   private boolean stripTrailingSlash = true;
/*     */   
/*  71 */   private boolean stripExtension = true;
/*     */   
/*  73 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/*  81 */     this.prefix = (prefix != null ? prefix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuffix(String suffix)
/*     */   {
/*  89 */     this.suffix = (suffix != null ? suffix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSeparator(String separator)
/*     */   {
/*  98 */     this.separator = separator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStripLeadingSlash(boolean stripLeadingSlash)
/*     */   {
/* 106 */     this.stripLeadingSlash = stripLeadingSlash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStripTrailingSlash(boolean stripTrailingSlash)
/*     */   {
/* 114 */     this.stripTrailingSlash = stripTrailingSlash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStripExtension(boolean stripExtension)
/*     */   {
/* 122 */     this.stripExtension = stripExtension;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*     */   {
/* 133 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlDecode(boolean urlDecode)
/*     */   {
/* 145 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent)
/*     */   {
/* 153 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 163 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 164 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getViewName(HttpServletRequest request)
/*     */   {
/* 176 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 177 */     return this.prefix + transformPath(lookupPath) + this.suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String transformPath(String lookupPath)
/*     */   {
/* 189 */     String path = lookupPath;
/* 190 */     if ((this.stripLeadingSlash) && (path.startsWith("/"))) {
/* 191 */       path = path.substring(1);
/*     */     }
/* 193 */     if ((this.stripTrailingSlash) && (path.endsWith("/"))) {
/* 194 */       path = path.substring(0, path.length() - 1);
/*     */     }
/* 196 */     if (this.stripExtension) {
/* 197 */       path = StringUtils.stripFilenameExtension(path);
/*     */     }
/* 199 */     if (!"/".equals(this.separator)) {
/* 200 */       path = StringUtils.replace(path, "/", this.separator);
/*     */     }
/* 202 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\DefaultRequestToViewNameTranslator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */