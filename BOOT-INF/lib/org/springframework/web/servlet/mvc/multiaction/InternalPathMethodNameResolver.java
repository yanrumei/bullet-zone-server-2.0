/*     */ package org.springframework.web.servlet.mvc.multiaction;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ @Deprecated
/*     */ public class InternalPathMethodNameResolver
/*     */   extends AbstractUrlMethodNameResolver
/*     */ {
/*  43 */   private String prefix = "";
/*     */   
/*  45 */   private String suffix = "";
/*     */   
/*     */ 
/*  48 */   private final Map<String, String> methodNameCache = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/*  57 */     this.prefix = (prefix != null ? prefix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getPrefix()
/*     */   {
/*  64 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSuffix(String suffix)
/*     */   {
/*  73 */     this.suffix = (suffix != null ? suffix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getSuffix()
/*     */   {
/*  80 */     return this.suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getHandlerMethodNameForUrlPath(String urlPath)
/*     */   {
/*  91 */     String methodName = (String)this.methodNameCache.get(urlPath);
/*  92 */     if (methodName == null) {
/*  93 */       methodName = extractHandlerMethodNameFromUrlPath(urlPath);
/*  94 */       methodName = postProcessHandlerMethodName(methodName);
/*  95 */       this.methodNameCache.put(urlPath, methodName);
/*     */     }
/*  97 */     return methodName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String extractHandlerMethodNameFromUrlPath(String uri)
/*     */   {
/* 108 */     return WebUtils.extractFilenameFromUrlPath(uri);
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
/*     */   protected String postProcessHandlerMethodName(String methodName)
/*     */   {
/* 123 */     return getPrefix() + methodName + getSuffix();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\InternalPathMethodNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */