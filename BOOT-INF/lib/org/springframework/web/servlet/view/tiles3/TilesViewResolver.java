/*    */ package org.springframework.web.servlet.view.tiles3;
/*    */ 
/*    */ import org.apache.tiles.request.render.Renderer;
/*    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
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
/*    */ public class TilesViewResolver
/*    */   extends UrlBasedViewResolver
/*    */ {
/*    */   private Renderer renderer;
/*    */   private Boolean alwaysInclude;
/*    */   
/*    */   public TilesViewResolver()
/*    */   {
/* 41 */     setViewClass(requiredViewClass());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Class<?> requiredViewClass()
/*    */   {
/* 50 */     return TilesView.class;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setRenderer(Renderer renderer)
/*    */   {
/* 59 */     this.renderer = renderer;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAlwaysInclude(Boolean alwaysInclude)
/*    */   {
/* 70 */     this.alwaysInclude = alwaysInclude;
/*    */   }
/*    */   
/*    */   protected TilesView buildView(String viewName)
/*    */     throws Exception
/*    */   {
/* 76 */     TilesView view = (TilesView)super.buildView(viewName);
/* 77 */     if (this.renderer != null) {
/* 78 */       view.setRenderer(this.renderer);
/*    */     }
/* 80 */     if (this.alwaysInclude != null) {
/* 81 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*    */     }
/* 83 */     return view;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles3\TilesViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */