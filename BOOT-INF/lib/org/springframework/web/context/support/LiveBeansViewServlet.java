/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.support.LiveBeansView;
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
/*    */ public class LiveBeansViewServlet
/*    */   extends HttpServlet
/*    */ {
/*    */   private LiveBeansView liveBeansView;
/*    */   
/*    */   public void init()
/*    */     throws ServletException
/*    */   {
/* 45 */     this.liveBeansView = buildLiveBeansView();
/*    */   }
/*    */   
/*    */   protected LiveBeansView buildLiveBeansView() {
/* 49 */     return new ServletContextLiveBeansView(getServletContext());
/*    */   }
/*    */   
/*    */ 
/*    */   protected void doGet(HttpServletRequest request, HttpServletResponse response)
/*    */     throws ServletException, IOException
/*    */   {
/* 56 */     String content = this.liveBeansView.getSnapshotAsJson();
/* 57 */     response.setContentType("application/json");
/* 58 */     response.setContentLength(content.length());
/* 59 */     response.getWriter().write(content);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\LiveBeansViewServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */