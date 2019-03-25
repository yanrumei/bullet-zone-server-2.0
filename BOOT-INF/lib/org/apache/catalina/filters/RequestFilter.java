/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RequestFilter
/*     */   extends FilterBase
/*     */ {
/*  63 */   protected Pattern allow = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   protected Pattern deny = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   protected int denyStatus = 403;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PLAIN_TEXT_MIME_TYPE = "text/plain";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAllow()
/*     */   {
/*  90 */     if (this.allow == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     return this.allow.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllow(String allow)
/*     */   {
/* 104 */     if ((allow == null) || (allow.length() == 0)) {
/* 105 */       this.allow = null;
/*     */     } else {
/* 107 */       this.allow = Pattern.compile(allow);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeny()
/*     */   {
/* 117 */     if (this.deny == null) {
/* 118 */       return null;
/*     */     }
/* 120 */     return this.deny.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeny(String deny)
/*     */   {
/* 131 */     if ((deny == null) || (deny.length() == 0)) {
/* 132 */       this.deny = null;
/*     */     } else {
/* 134 */       this.deny = Pattern.compile(deny);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDenyStatus()
/*     */   {
/* 143 */     return this.denyStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDenyStatus(int denyStatus)
/*     */   {
/* 153 */     this.denyStatus = denyStatus;
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
/*     */   public abstract void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
/*     */     throws IOException, ServletException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isConfigProblemFatal()
/*     */   {
/* 184 */     return true;
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
/*     */   protected void process(String property, ServletRequest request, ServletResponse response, FilterChain chain)
/*     */     throws IOException, ServletException
/*     */   {
/* 204 */     if (isAllowed(property)) {
/* 205 */       chain.doFilter(request, response);
/*     */     }
/* 207 */     else if ((response instanceof HttpServletResponse)) {
/* 208 */       if (getLogger().isDebugEnabled()) {
/* 209 */         getLogger().debug(sm.getString("requestFilter.deny", new Object[] {((HttpServletRequest)request)
/* 210 */           .getRequestURI(), property }));
/*     */       }
/* 212 */       ((HttpServletResponse)response).sendError(this.denyStatus);
/*     */     } else {
/* 214 */       sendErrorWhenNotHttp(response);
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
/*     */   private boolean isAllowed(String property)
/*     */   {
/* 228 */     if ((this.deny != null) && (this.deny.matcher(property).matches())) {
/* 229 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 233 */     if ((this.allow != null) && (this.allow.matcher(property).matches())) {
/* 234 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 238 */     if ((this.deny != null) && (this.allow == null)) {
/* 239 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 243 */     return false;
/*     */   }
/*     */   
/*     */   private void sendErrorWhenNotHttp(ServletResponse response) throws IOException
/*     */   {
/* 248 */     response.setContentType("text/plain");
/* 249 */     response.getWriter().write(sm.getString("http.403"));
/* 250 */     response.getWriter().flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\RequestFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */