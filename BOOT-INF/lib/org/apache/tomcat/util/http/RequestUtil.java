/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestUtil
/*     */ {
/*     */   public static String normalize(String path)
/*     */   {
/*  38 */     return normalize(path, true);
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
/*     */   public static String normalize(String path, boolean replaceBackSlash)
/*     */   {
/*  56 */     if (path == null) {
/*  57 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  61 */     String normalized = path;
/*     */     
/*  63 */     if ((replaceBackSlash) && (normalized.indexOf('\\') >= 0)) {
/*  64 */       normalized = normalized.replace('\\', '/');
/*     */     }
/*     */     
/*  67 */     if (!normalized.startsWith("/")) {
/*  68 */       normalized = "/" + normalized;
/*     */     }
/*  70 */     boolean addedTrailingSlash = false;
/*  71 */     if ((normalized.endsWith("/.")) || (normalized.endsWith("/.."))) {
/*  72 */       normalized = normalized + "/";
/*  73 */       addedTrailingSlash = true;
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/*  78 */       int index = normalized.indexOf("//");
/*  79 */       if (index < 0) {
/*     */         break;
/*     */       }
/*  82 */       normalized = normalized.substring(0, index) + normalized.substring(index + 1);
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/*  87 */       int index = normalized.indexOf("/./");
/*  88 */       if (index < 0) {
/*     */         break;
/*     */       }
/*  91 */       normalized = normalized.substring(0, index) + normalized.substring(index + 2);
/*     */     }
/*     */     
/*     */     for (;;)
/*     */     {
/*  96 */       int index = normalized.indexOf("/../");
/*  97 */       if (index < 0) {
/*     */         break;
/*     */       }
/* 100 */       if (index == 0) {
/* 101 */         return null;
/*     */       }
/* 103 */       int index2 = normalized.lastIndexOf('/', index - 1);
/* 104 */       normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
/*     */     }
/*     */     
/* 107 */     if ((normalized.length() > 1) && (addedTrailingSlash))
/*     */     {
/*     */ 
/* 110 */       normalized = normalized.substring(0, normalized.length() - 1);
/*     */     }
/*     */     
/*     */ 
/* 114 */     return normalized;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\RequestUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */