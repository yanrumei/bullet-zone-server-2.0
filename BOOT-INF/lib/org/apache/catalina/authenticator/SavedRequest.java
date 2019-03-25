/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.Cookie;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SavedRequest
/*     */ {
/*  49 */   private final ArrayList<Cookie> cookies = new ArrayList();
/*     */   
/*     */   public void addCookie(Cookie cookie) {
/*  52 */     this.cookies.add(cookie);
/*     */   }
/*     */   
/*     */   public Iterator<Cookie> getCookies() {
/*  56 */     return this.cookies.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private final HashMap<String, ArrayList<String>> headers = new HashMap();
/*     */   
/*     */   public void addHeader(String name, String value) {
/*  69 */     ArrayList<String> values = (ArrayList)this.headers.get(name);
/*  70 */     if (values == null) {
/*  71 */       values = new ArrayList();
/*  72 */       this.headers.put(name, values);
/*     */     }
/*  74 */     values.add(value);
/*     */   }
/*     */   
/*     */   public Iterator<String> getHeaderNames() {
/*  78 */     return this.headers.keySet().iterator();
/*     */   }
/*     */   
/*     */   public Iterator<String> getHeaderValues(String name) {
/*  82 */     ArrayList<String> values = (ArrayList)this.headers.get(name);
/*  83 */     if (values == null) {
/*  84 */       return new ArrayList().iterator();
/*     */     }
/*  86 */     return values.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private final ArrayList<Locale> locales = new ArrayList();
/*     */   
/*     */   public void addLocale(Locale locale) {
/*  96 */     this.locales.add(locale);
/*     */   }
/*     */   
/*     */   public Iterator<Locale> getLocales() {
/* 100 */     return this.locales.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */   private String method = null;
/*     */   
/*     */   public String getMethod() {
/* 110 */     return this.method;
/*     */   }
/*     */   
/*     */   public void setMethod(String method) {
/* 114 */     this.method = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   private String queryString = null;
/*     */   
/*     */   public String getQueryString() {
/* 124 */     return this.queryString;
/*     */   }
/*     */   
/*     */   public void setQueryString(String queryString) {
/* 128 */     this.queryString = queryString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */   private String requestURI = null;
/*     */   
/*     */   public String getRequestURI() {
/* 138 */     return this.requestURI;
/*     */   }
/*     */   
/*     */   public void setRequestURI(String requestURI) {
/* 142 */     this.requestURI = requestURI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */   private String decodedRequestURI = null;
/*     */   
/*     */   public String getDecodedRequestURI() {
/* 153 */     return this.decodedRequestURI;
/*     */   }
/*     */   
/*     */   public void setDecodedRequestURI(String decodedRequestURI) {
/* 157 */     this.decodedRequestURI = decodedRequestURI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */   private ByteChunk body = null;
/*     */   
/*     */   public ByteChunk getBody() {
/* 167 */     return this.body;
/*     */   }
/*     */   
/*     */   public void setBody(ByteChunk body) {
/* 171 */     this.body = body;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 177 */   private String contentType = null;
/*     */   
/*     */   public String getContentType() {
/* 180 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType) {
/* 184 */     this.contentType = contentType;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SavedRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */