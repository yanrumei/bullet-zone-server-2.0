/*    */ package org.springframework.web.servlet.view.tiles2;
/*    */ 
/*    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
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
/*    */ @Deprecated
/*    */ public class TilesViewResolver
/*    */   extends UrlBasedViewResolver
/*    */ {
/*    */   private Boolean alwaysInclude;
/*    */   
/*    */   public TilesViewResolver()
/*    */   {
/* 53 */     setViewClass(requiredViewClass());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Class<?> requiredViewClass()
/*    */   {
/* 62 */     return TilesView.class;
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
/* 73 */     this.alwaysInclude = alwaysInclude;
/*    */   }
/*    */   
/*    */   protected AbstractUrlBasedView buildView(String viewName)
/*    */     throws Exception
/*    */   {
/* 79 */     TilesView view = (TilesView)super.buildView(viewName);
/* 80 */     if (this.alwaysInclude != null) {
/* 81 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*    */     }
/* 83 */     return view;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\tiles2\TilesViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */