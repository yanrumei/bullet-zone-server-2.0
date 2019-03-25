/*     */ package org.apache.catalina.servlet4preview.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.catalina.servlet4preview.GenericFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class HttpFilter
/*     */   extends GenericFilter
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  57 */     if (!(request instanceof HttpServletRequest)) {
/*  58 */       throw new ServletException(request + " not HttpServletRequest");
/*     */     }
/*  60 */     if (!(response instanceof HttpServletResponse)) {
/*  61 */       throw new ServletException(request + " not HttpServletResponse");
/*     */     }
/*  63 */     doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/* 103 */     chain.doFilter(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\http\HttpFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */