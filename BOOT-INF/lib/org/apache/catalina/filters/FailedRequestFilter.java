/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.http.Parameters.FailReason;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FailedRequestFilter
/*     */   extends FilterBase
/*     */ {
/*  46 */   private static final Log log = LogFactory.getLog(FailedRequestFilter.class);
/*     */   
/*     */   protected Log getLogger()
/*     */   {
/*  50 */     return log;
/*     */   }
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/*  56 */     if (!isGoodRequest(request)) {
/*  57 */       Parameters.FailReason reason = (Parameters.FailReason)request.getAttribute("org.apache.catalina.parameter_parse_failed_reason");
/*     */       
/*     */       int status;
/*     */       int status;
/*     */       int status;
/*  62 */       switch (reason)
/*     */       {
/*     */       case IO_ERROR: 
/*  65 */         status = 500;
/*  66 */         break;
/*     */       case POST_TOO_LARGE: 
/*  68 */         status = 413;
/*  69 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       case TOO_MANY_PARAMETERS: 
/*     */       case UNKNOWN: 
/*     */       case INVALID_CONTENT_TYPE: 
/*     */       case MULTIPART_CONFIG_INVALID: 
/*     */       case NO_NAME: 
/*     */       case REQUEST_BODY_INCOMPLETE: 
/*     */       case URL_DECODING: 
/*     */       case CLIENT_DISCONNECT: 
/*     */       default: 
/*  87 */         status = 400;
/*     */       }
/*     */       
/*     */       
/*  91 */       ((HttpServletResponse)response).sendError(status);
/*  92 */       return;
/*     */     }
/*  94 */     chain.doFilter(request, response);
/*     */   }
/*     */   
/*     */   private boolean isGoodRequest(ServletRequest request)
/*     */   {
/*  99 */     request.getParameter("none");
/*     */     
/* 101 */     if (request.getAttribute("org.apache.catalina.parameter_parse_failed") != null) {
/* 102 */       return false;
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isConfigProblemFatal()
/*     */   {
/* 109 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\FailedRequestFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */