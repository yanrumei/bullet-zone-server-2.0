/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ public class InternalResourceView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*  65 */   private boolean alwaysInclude = false;
/*     */   
/*  67 */   private boolean preventDispatchLoop = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InternalResourceView() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InternalResourceView(String url)
/*     */   {
/*  84 */     super(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InternalResourceView(String url, boolean alwaysInclude)
/*     */   {
/*  93 */     super(url);
/*  94 */     this.alwaysInclude = alwaysInclude;
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
/*     */   public void setAlwaysInclude(boolean alwaysInclude)
/*     */   {
/* 107 */     this.alwaysInclude = alwaysInclude;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPreventDispatchLoop(boolean preventDispatchLoop)
/*     */   {
/* 118 */     this.preventDispatchLoop = preventDispatchLoop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isContextRequired()
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 139 */     exposeModelAsRequestAttributes(model, request);
/*     */     
/*     */ 
/* 142 */     exposeHelpers(request);
/*     */     
/*     */ 
/* 145 */     String dispatcherPath = prepareForRendering(request, response);
/*     */     
/*     */ 
/* 148 */     RequestDispatcher rd = getRequestDispatcher(request, dispatcherPath);
/* 149 */     if (rd == null) {
/* 150 */       throw new ServletException("Could not get RequestDispatcher for [" + getUrl() + "]: Check that the corresponding file exists within your web application archive!");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 155 */     if (useInclude(request, response)) {
/* 156 */       response.setContentType(getContentType());
/* 157 */       if (this.logger.isDebugEnabled()) {
/* 158 */         this.logger.debug("Including resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
/*     */       }
/* 160 */       rd.include(request, response);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 165 */       if (this.logger.isDebugEnabled()) {
/* 166 */         this.logger.debug("Forwarding to resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
/*     */       }
/* 168 */       rd.forward(request, response);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeHelpers(HttpServletRequest request)
/*     */     throws Exception
/*     */   {}
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
/*     */   protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 201 */     String path = getUrl();
/* 202 */     if (this.preventDispatchLoop) {
/* 203 */       String uri = request.getRequestURI();
/* 204 */       if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtils.applyRelativePath(uri, path))) {
/* 205 */         throw new ServletException("Circular view path [" + path + "]: would dispatch back to the current handler URL [" + uri + "] again. Check your ViewResolver setup! (Hint: This may be the result of an unspecified view, due to default view name generation.)");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 210 */     return path;
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
/*     */   protected RequestDispatcher getRequestDispatcher(HttpServletRequest request, String path)
/*     */   {
/* 223 */     return request.getRequestDispatcher(path);
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
/*     */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 241 */     return (this.alwaysInclude) || (WebUtils.isIncludeRequest(request)) || (response.isCommitted());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\InternalResourceView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */