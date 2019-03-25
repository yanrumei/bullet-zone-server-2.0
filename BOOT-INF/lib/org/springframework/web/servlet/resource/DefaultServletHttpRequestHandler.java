/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultServletHttpRequestHandler
/*     */   implements HttpRequestHandler, ServletContextAware
/*     */ {
/*     */   private static final String COMMON_DEFAULT_SERVLET_NAME = "default";
/*     */   private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";
/*     */   private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";
/*     */   private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";
/*     */   private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";
/*     */   private String defaultServletName;
/*     */   private ServletContext servletContext;
/*     */   
/*     */   public void setDefaultServletName(String defaultServletName)
/*     */   {
/*  78 */     this.defaultServletName = defaultServletName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/*  88 */     this.servletContext = servletContext;
/*  89 */     if (!StringUtils.hasText(this.defaultServletName)) {
/*  90 */       if (this.servletContext.getNamedDispatcher("default") != null) {
/*  91 */         this.defaultServletName = "default";
/*     */       }
/*  93 */       else if (this.servletContext.getNamedDispatcher("_ah_default") != null) {
/*  94 */         this.defaultServletName = "_ah_default";
/*     */       }
/*  96 */       else if (this.servletContext.getNamedDispatcher("resin-file") != null) {
/*  97 */         this.defaultServletName = "resin-file";
/*     */       }
/*  99 */       else if (this.servletContext.getNamedDispatcher("FileServlet") != null) {
/* 100 */         this.defaultServletName = "FileServlet";
/*     */       }
/* 102 */       else if (this.servletContext.getNamedDispatcher("SimpleFileServlet") != null) {
/* 103 */         this.defaultServletName = "SimpleFileServlet";
/*     */       }
/*     */       else {
/* 106 */         throw new IllegalStateException("Unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/* 117 */     RequestDispatcher rd = this.servletContext.getNamedDispatcher(this.defaultServletName);
/* 118 */     if (rd == null) {
/* 119 */       throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" + this.defaultServletName + "'");
/*     */     }
/*     */     
/* 122 */     rd.forward(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\DefaultServletHttpRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */