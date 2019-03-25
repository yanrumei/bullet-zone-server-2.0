/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebdavFixFilter
/*     */   implements Filter
/*     */ {
/*     */   private static final String LOG_MESSAGE_PREAMBLE = "WebdavFixFilter: Detected client problem: ";
/*     */   private static final String UA_MINIDIR_START = "Microsoft-WebDAV-MiniRedir";
/*     */   private static final String UA_MINIDIR_5_1_2600 = "Microsoft-WebDAV-MiniRedir/5.1.2600";
/*     */   private static final String UA_MINIDIR_5_2_3790 = "Microsoft-WebDAV-MiniRedir/5.2.3790";
/*     */   
/*     */   public void init(FilterConfig filterConfig)
/*     */     throws ServletException
/*     */   {}
/*     */   
/*     */   public void destroy() {}
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  93 */     if ((!(request instanceof HttpServletRequest)) || (!(response instanceof HttpServletResponse)))
/*     */     {
/*  95 */       chain.doFilter(request, response);
/*  96 */       return;
/*     */     }
/*  98 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*  99 */     HttpServletResponse httpResponse = (HttpServletResponse)response;
/* 100 */     String ua = httpRequest.getHeader("User-Agent");
/*     */     
/* 102 */     if ((ua == null) || (ua.length() == 0) || 
/* 103 */       (!ua.startsWith("Microsoft-WebDAV-MiniRedir")))
/*     */     {
/*     */ 
/* 106 */       chain.doFilter(request, response);
/* 107 */     } else if (ua.startsWith("Microsoft-WebDAV-MiniRedir/5.1.2600"))
/*     */     {
/* 109 */       httpResponse.sendRedirect(buildRedirect(httpRequest));
/* 110 */     } else if (ua.startsWith("Microsoft-WebDAV-MiniRedir/5.2.3790"))
/*     */     {
/* 112 */       if (!"".equals(httpRequest.getContextPath())) {
/* 113 */         log(httpRequest, "XP-x64-SP2 clients only work with the root context");
/*     */       }
/*     */       
/*     */ 
/* 117 */       log(httpRequest, "XP-x64-SP2 is known not to work with WebDAV Servlet");
/*     */       
/* 119 */       chain.doFilter(request, response);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 124 */       httpResponse.sendRedirect(buildRedirect(httpRequest));
/*     */     }
/*     */   }
/*     */   
/*     */   private String buildRedirect(HttpServletRequest request)
/*     */   {
/* 130 */     StringBuilder location = new StringBuilder(request.getRequestURL().length());
/* 131 */     location.append(request.getScheme());
/* 132 */     location.append("://");
/* 133 */     location.append(request.getServerName());
/* 134 */     location.append(':');
/*     */     
/*     */ 
/*     */ 
/* 138 */     location.append(request.getServerPort());
/* 139 */     location.append(request.getRequestURI());
/* 140 */     return location.toString();
/*     */   }
/*     */   
/*     */   private void log(ServletRequest request, String msg) {
/* 144 */     StringBuilder builder = new StringBuilder("WebdavFixFilter: Detected client problem: ");
/* 145 */     builder.append(msg);
/* 146 */     request.getServletContext().log(builder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\WebdavFixFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */