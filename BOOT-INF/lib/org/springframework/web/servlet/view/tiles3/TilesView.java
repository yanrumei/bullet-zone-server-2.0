/*     */ package org.springframework.web.servlet.view.tiles3;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.tiles.TilesContainer;
/*     */ import org.apache.tiles.access.TilesAccess;
/*     */ import org.apache.tiles.renderer.DefinitionRenderer;
/*     */ import org.apache.tiles.request.AbstractRequest;
/*     */ import org.apache.tiles.request.ApplicationContext;
/*     */ import org.apache.tiles.request.Request;
/*     */ import org.apache.tiles.request.render.Renderer;
/*     */ import org.apache.tiles.request.servlet.ServletRequest;
/*     */ import org.apache.tiles.request.servlet.ServletUtil;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.servlet.support.JstlUtils;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*     */ public class TilesView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   private Renderer renderer;
/*  57 */   private boolean exposeJstlAttributes = true;
/*     */   
/*  59 */   private boolean alwaysInclude = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRenderer(Renderer renderer)
/*     */   {
/*  69 */     this.renderer = renderer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setExposeJstlAttributes(boolean exposeJstlAttributes)
/*     */   {
/*  77 */     this.exposeJstlAttributes = exposeJstlAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysInclude(boolean alwaysInclude)
/*     */   {
/*  88 */     this.alwaysInclude = alwaysInclude;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  93 */     super.afterPropertiesSet();
/*     */     
/*  95 */     this.applicationContext = ServletUtil.getApplicationContext(getServletContext());
/*  96 */     if (this.renderer == null) {
/*  97 */       TilesContainer container = TilesAccess.getContainer(this.applicationContext);
/*  98 */       this.renderer = new DefinitionRenderer(container);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean checkResource(final Locale locale)
/*     */     throws Exception
/*     */   {
/* 105 */     HttpServletRequest servletRequest = null;
/* 106 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 107 */     if ((requestAttributes instanceof ServletRequestAttributes)) {
/* 108 */       servletRequest = ((ServletRequestAttributes)requestAttributes).getRequest();
/*     */     }
/* 110 */     Request request = new ServletRequest(this.applicationContext, servletRequest, null)
/*     */     {
/*     */       public Locale getRequestLocale() {
/* 113 */         return locale;
/*     */       }
/* 115 */     };
/* 116 */     return this.renderer.isRenderable(getUrl(), request);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 123 */     exposeModelAsRequestAttributes(model, request);
/* 124 */     if (this.exposeJstlAttributes) {
/* 125 */       JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
/*     */     }
/* 127 */     if (this.alwaysInclude) {
/* 128 */       request.setAttribute(AbstractRequest.FORCE_INCLUDE_ATTRIBUTE_NAME, Boolean.valueOf(true));
/*     */     }
/*     */     
/* 131 */     Request tilesRequest = createTilesRequest(request, response);
/* 132 */     this.renderer.render(getUrl(), tilesRequest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Request createTilesRequest(final HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 143 */     new ServletRequest(this.applicationContext, request, response)
/*     */     {
/*     */       public Locale getRequestLocale() {
/* 146 */         return RequestContextUtils.getLocale(request);
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\TilesView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */