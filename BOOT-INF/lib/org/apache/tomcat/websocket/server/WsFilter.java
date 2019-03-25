/*    */ package org.apache.tomcat.websocket.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WsFilter
/*    */   implements Filter
/*    */ {
/*    */   private WsServerContainer sc;
/*    */   
/*    */   public void init(FilterConfig filterConfig)
/*    */     throws ServletException
/*    */   {
/* 40 */     this.sc = ((WsServerContainer)filterConfig.getServletContext().getAttribute("javax.websocket.server.ServerContainer"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
/*    */     throws IOException, ServletException
/*    */   {
/* 50 */     if ((!this.sc.areEndpointsRegistered()) || 
/* 51 */       (!UpgradeUtil.isWebSocketUpgradeRequest(request, response))) {
/* 52 */       chain.doFilter(request, response);
/* 53 */       return;
/*    */     }
/*    */     
/*    */ 
/* 57 */     HttpServletRequest req = (HttpServletRequest)request;
/* 58 */     HttpServletResponse resp = (HttpServletResponse)response;
/*    */     
/*    */ 
/*    */ 
/* 62 */     String pathInfo = req.getPathInfo();
/* 63 */     String path; String path; if (pathInfo == null) {
/* 64 */       path = req.getServletPath();
/*    */     } else {
/* 66 */       path = req.getServletPath() + pathInfo;
/*    */     }
/* 68 */     WsMappingResult mappingResult = this.sc.findMapping(path);
/*    */     
/* 70 */     if (mappingResult == null)
/*    */     {
/*    */ 
/* 73 */       chain.doFilter(request, response);
/* 74 */       return;
/*    */     }
/*    */     
/* 77 */     UpgradeUtil.doUpgrade(this.sc, req, resp, mappingResult.getConfig(), mappingResult
/* 78 */       .getPathParams());
/*    */   }
/*    */   
/*    */   public void destroy() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */