/*    */ package org.springframework.web.servlet;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ModelAndViewDefiningException
/*    */   extends ServletException
/*    */ {
/*    */   private ModelAndView modelAndView;
/*    */   
/*    */   public ModelAndViewDefiningException(ModelAndView modelAndView)
/*    */   {
/* 47 */     Assert.notNull(modelAndView, "ModelAndView must not be null in ModelAndViewDefiningException");
/* 48 */     this.modelAndView = modelAndView;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ModelAndView getModelAndView()
/*    */   {
/* 55 */     return this.modelAndView;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\ModelAndViewDefiningException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */