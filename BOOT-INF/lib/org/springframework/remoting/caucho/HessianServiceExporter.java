/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*    */ import org.springframework.web.util.NestedServletException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HessianServiceExporter
/*    */   extends HessianExporter
/*    */   implements HttpRequestHandler
/*    */ {
/*    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 59 */     if (!"POST".equals(request.getMethod())) {
/* 60 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" }, "HessianServiceExporter only supports POST requests");
/*    */     }
/*    */     
/*    */ 
/* 64 */     response.setContentType("application/x-hessian");
/*    */     try {
/* 66 */       invoke(request.getInputStream(), response.getOutputStream());
/*    */     }
/*    */     catch (Throwable ex) {
/* 69 */       throw new NestedServletException("Hessian skeleton invocation failed", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\HessianServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */