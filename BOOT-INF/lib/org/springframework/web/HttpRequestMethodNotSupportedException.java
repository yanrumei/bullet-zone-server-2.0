/*     */ package org.springframework.web;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpRequestMethodNotSupportedException
/*     */   extends ServletException
/*     */ {
/*     */   private String method;
/*     */   private String[] supportedMethods;
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method)
/*     */   {
/*  49 */     this(method, (String[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestMethodNotSupportedException(String method, String msg)
/*     */   {
/*  58 */     this(method, null, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestMethodNotSupportedException(String method, Collection<String> supportedMethods)
/*     */   {
/*  67 */     this(method, StringUtils.toStringArray(supportedMethods));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods)
/*     */   {
/*  76 */     this(method, supportedMethods, "Request method '" + method + "' not supported");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestMethodNotSupportedException(String method, String[] supportedMethods, String msg)
/*     */   {
/*  86 */     super(msg);
/*  87 */     this.method = method;
/*  88 */     this.supportedMethods = supportedMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMethod()
/*     */   {
/*  96 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getSupportedMethods()
/*     */   {
/* 103 */     return this.supportedMethods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<HttpMethod> getSupportedHttpMethods()
/*     */   {
/* 112 */     if (this.supportedMethods == null) {
/* 113 */       return null;
/*     */     }
/* 115 */     List<HttpMethod> supportedMethods = new LinkedList();
/* 116 */     for (String value : this.supportedMethods) {
/* 117 */       HttpMethod resolved = HttpMethod.resolve(value);
/* 118 */       if (resolved != null) {
/* 119 */         supportedMethods.add(resolved);
/*     */       }
/*     */     }
/* 122 */     return EnumSet.copyOf(supportedMethods);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\HttpRequestMethodNotSupportedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */