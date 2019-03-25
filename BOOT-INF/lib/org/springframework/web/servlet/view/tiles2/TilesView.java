/*     */ package org.springframework.web.servlet.view.tiles2;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.tiles.TilesApplicationContext;
/*     */ import org.apache.tiles.TilesContainer;
/*     */ import org.apache.tiles.context.TilesRequestContext;
/*     */ import org.apache.tiles.definition.DefinitionsFactory;
/*     */ import org.apache.tiles.impl.BasicTilesContainer;
/*     */ import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
/*     */ import org.apache.tiles.servlet.context.ServletTilesRequestContext;
/*     */ import org.apache.tiles.servlet.context.ServletUtil;
/*     */ import org.springframework.web.servlet.support.JstlUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
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
/*     */ @Deprecated
/*     */ public class TilesView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*  63 */   private boolean alwaysInclude = false;
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
/*  74 */     this.alwaysInclude = alwaysInclude;
/*     */   }
/*     */   
/*     */   public boolean checkResource(final Locale locale)
/*     */     throws Exception
/*     */   {
/*  80 */     TilesContainer container = ServletUtil.getContainer(getServletContext());
/*  81 */     if (!(container instanceof BasicTilesContainer))
/*     */     {
/*  83 */       return true;
/*     */     }
/*  85 */     BasicTilesContainer basicContainer = (BasicTilesContainer)container;
/*  86 */     TilesApplicationContext appContext = new ServletTilesApplicationContext(getServletContext());
/*  87 */     TilesRequestContext requestContext = new ServletTilesRequestContext(appContext, null, null)
/*     */     {
/*     */       public Locale getRequestLocale() {
/*  90 */         return locale;
/*     */       }
/*  92 */     };
/*  93 */     return basicContainer.getDefinitionsFactory().getDefinition(getUrl(), requestContext) != null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 100 */     ServletContext servletContext = getServletContext();
/* 101 */     TilesContainer container = ServletUtil.getContainer(servletContext);
/* 102 */     if (container == null) {
/* 103 */       throw new ServletException("Tiles container is not initialized. Have you added a TilesConfigurer to your web application context?");
/*     */     }
/*     */     
/*     */ 
/* 107 */     exposeModelAsRequestAttributes(model, request);
/* 108 */     JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));
/* 109 */     if (this.alwaysInclude) {
/* 110 */       ServletUtil.setForceInclude(request, true);
/*     */     }
/* 112 */     container.render(getUrl(), new Object[] { request, response });
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\TilesView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */