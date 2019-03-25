/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import javax.servlet.http.HttpSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CsrfPreventionFilter
/*     */   extends CsrfPreventionFilterBase
/*     */ {
/*  47 */   private final Set<String> entryPoints = new HashSet();
/*     */   
/*  49 */   private int nonceCacheSize = 5;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEntryPoints(String entryPoints)
/*     */   {
/*  62 */     String[] values = entryPoints.split(",");
/*  63 */     for (String value : values) {
/*  64 */       this.entryPoints.add(value.trim());
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
/*     */   public void setNonceCacheSize(int nonceCacheSize)
/*     */   {
/*  78 */     this.nonceCacheSize = nonceCacheSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  85 */     ServletResponse wResponse = null;
/*     */     
/*  87 */     if (((request instanceof HttpServletRequest)) && ((response instanceof HttpServletResponse)))
/*     */     {
/*     */ 
/*  90 */       HttpServletRequest req = (HttpServletRequest)request;
/*  91 */       HttpServletResponse res = (HttpServletResponse)response;
/*     */       
/*  93 */       boolean skipNonceCheck = false;
/*     */       
/*  95 */       if (("GET".equals(req.getMethod())) && 
/*  96 */         (this.entryPoints.contains(getRequestedPath(req)))) {
/*  97 */         skipNonceCheck = true;
/*     */       }
/*     */       
/* 100 */       HttpSession session = req.getSession(false);
/*     */       
/*     */ 
/*     */ 
/* 104 */       LruCache<String> nonceCache = session == null ? null : (LruCache)session.getAttribute("org.apache.catalina.filters.CSRF_NONCE");
/*     */       
/*     */ 
/* 107 */       if (!skipNonceCheck)
/*     */       {
/* 109 */         String previousNonce = req.getParameter("org.apache.catalina.filters.CSRF_NONCE");
/*     */         
/* 111 */         if ((nonceCache == null) || (previousNonce == null) || 
/* 112 */           (!nonceCache.contains(previousNonce))) {
/* 113 */           res.sendError(getDenyStatus());
/* 114 */           return;
/*     */         }
/*     */       }
/*     */       
/* 118 */       if (nonceCache == null) {
/* 119 */         nonceCache = new LruCache(this.nonceCacheSize);
/* 120 */         if (session == null) {
/* 121 */           session = req.getSession(true);
/*     */         }
/* 123 */         session.setAttribute("org.apache.catalina.filters.CSRF_NONCE", nonceCache);
/*     */       }
/*     */       
/*     */ 
/* 127 */       String newNonce = generateNonce();
/*     */       
/* 129 */       nonceCache.add(newNonce);
/*     */       
/* 131 */       wResponse = new CsrfResponseWrapper(res, newNonce);
/*     */     } else {
/* 133 */       wResponse = response;
/*     */     }
/*     */     
/* 136 */     chain.doFilter(request, wResponse);
/*     */   }
/*     */   
/*     */   protected static class CsrfResponseWrapper
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private final String nonce;
/*     */     
/*     */     public CsrfResponseWrapper(HttpServletResponse response, String nonce)
/*     */     {
/* 146 */       super();
/* 147 */       this.nonce = nonce;
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public String encodeRedirectUrl(String url)
/*     */     {
/* 153 */       return encodeRedirectURL(url);
/*     */     }
/*     */     
/*     */     public String encodeRedirectURL(String url)
/*     */     {
/* 158 */       return addNonce(super.encodeRedirectURL(url));
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public String encodeUrl(String url)
/*     */     {
/* 164 */       return encodeURL(url);
/*     */     }
/*     */     
/*     */     public String encodeURL(String url)
/*     */     {
/* 169 */       return addNonce(super.encodeURL(url));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private String addNonce(String url)
/*     */     {
/* 180 */       if ((url == null) || (this.nonce == null)) {
/* 181 */         return url;
/*     */       }
/*     */       
/* 184 */       String path = url;
/* 185 */       String query = "";
/* 186 */       String anchor = "";
/* 187 */       int pound = path.indexOf('#');
/* 188 */       if (pound >= 0) {
/* 189 */         anchor = path.substring(pound);
/* 190 */         path = path.substring(0, pound);
/*     */       }
/* 192 */       int question = path.indexOf('?');
/* 193 */       if (question >= 0) {
/* 194 */         query = path.substring(question);
/* 195 */         path = path.substring(0, question);
/*     */       }
/* 197 */       StringBuilder sb = new StringBuilder(path);
/* 198 */       if (query.length() > 0) {
/* 199 */         sb.append(query);
/* 200 */         sb.append('&');
/*     */       } else {
/* 202 */         sb.append('?');
/*     */       }
/* 204 */       sb.append("org.apache.catalina.filters.CSRF_NONCE");
/* 205 */       sb.append('=');
/* 206 */       sb.append(this.nonce);
/* 207 */       sb.append(anchor);
/* 208 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class LruCache<T>
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final Map<T, T> cache;
/*     */     
/*     */     public LruCache(final int cacheSize)
/*     */     {
/* 221 */       this.cache = new LinkedHashMap() {
/*     */         private static final long serialVersionUID = 1L;
/*     */         
/*     */         protected boolean removeEldestEntry(Map.Entry<T, T> eldest) {
/* 225 */           if (size() > cacheSize) {
/* 226 */             return true;
/*     */           }
/* 228 */           return false;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     public void add(T key) {
/* 234 */       synchronized (this.cache) {
/* 235 */         this.cache.put(key, null);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public boolean contains(T key)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	org/apache/catalina/filters/CsrfPreventionFilter$LruCache:cache	Ljava/util/Map;
/*     */       //   4: dup
/*     */       //   5: astore_2
/*     */       //   6: monitorenter
/*     */       //   7: aload_0
/*     */       //   8: getfield 4	org/apache/catalina/filters/CsrfPreventionFilter$LruCache:cache	Ljava/util/Map;
/*     */       //   11: aload_1
/*     */       //   12: invokeinterface 6 2 0
/*     */       //   17: aload_2
/*     */       //   18: monitorexit
/*     */       //   19: ireturn
/*     */       //   20: astore_3
/*     */       //   21: aload_2
/*     */       //   22: monitorexit
/*     */       //   23: aload_3
/*     */       //   24: athrow
/*     */       // Line number table:
/*     */       //   Java source line #240	-> byte code offset #0
/*     */       //   Java source line #241	-> byte code offset #7
/*     */       //   Java source line #242	-> byte code offset #20
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	LruCache<T>
/*     */       //   0	25	1	key	T
/*     */       //   5	17	2	Ljava/lang/Object;	Object
/*     */       //   20	4	3	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	19	20	finally
/*     */       //   20	23	20	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\CsrfPreventionFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */