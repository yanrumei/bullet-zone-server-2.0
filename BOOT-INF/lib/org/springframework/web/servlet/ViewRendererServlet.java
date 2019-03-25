/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.util.NestedServletException;
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
/*     */ public class ViewRendererServlet
/*     */   extends HttpServlet
/*     */ {
/*  53 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
/*     */   
/*     */ 
/*  56 */   public static final String VIEW_ATTRIBUTE = ViewRendererServlet.class.getName() + ".VIEW";
/*     */   
/*     */ 
/*  59 */   public static final String MODEL_ATTRIBUTE = ViewRendererServlet.class.getName() + ".MODEL";
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/*  66 */     processRequest(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void doPost(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/*  73 */     processRequest(request, response);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
/*     */     throws ServletException, IOException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       renderView(request, response);
/*     */     }
/*     */     catch (ServletException ex) {
/*  89 */       throw ex;
/*     */     }
/*     */     catch (IOException ex) {
/*  92 */       throw ex;
/*     */     }
/*     */     catch (Exception ex) {
/*  95 */       throw new NestedServletException("View rendering failed", ex);
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
/*     */   protected void renderView(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 109 */     View view = (View)request.getAttribute(VIEW_ATTRIBUTE);
/* 110 */     if (view == null) {
/* 111 */       throw new ServletException("Could not complete render request: View is null");
/*     */     }
/* 113 */     Map<String, Object> model = (Map)request.getAttribute(MODEL_ATTRIBUTE);
/* 114 */     view.render(model, request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\ViewRendererServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */