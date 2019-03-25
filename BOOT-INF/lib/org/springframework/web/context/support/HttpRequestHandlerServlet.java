/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.i18n.LocaleContextHolder;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*    */ import org.springframework.web.context.WebApplicationContext;
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
/*    */ public class HttpRequestHandlerServlet
/*    */   extends HttpServlet
/*    */ {
/*    */   private HttpRequestHandler target;
/*    */   
/*    */   public void init()
/*    */     throws ServletException
/*    */   {
/* 56 */     WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
/* 57 */     this.target = ((HttpRequestHandler)wac.getBean(getServletName(), HttpRequestHandler.class));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 65 */     LocaleContextHolder.setLocale(request.getLocale());
/*    */     try {
/* 67 */       this.target.handleRequest(request, response);
/*    */     }
/*    */     catch (HttpRequestMethodNotSupportedException ex) {
/* 70 */       String[] supportedMethods = ex.getSupportedMethods();
/* 71 */       if (supportedMethods != null) {
/* 72 */         response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
/*    */       }
/* 74 */       response.sendError(405, ex.getMessage());
/*    */     }
/*    */     finally {
/* 77 */       LocaleContextHolder.resetLocaleContext();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\HttpRequestHandlerServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */