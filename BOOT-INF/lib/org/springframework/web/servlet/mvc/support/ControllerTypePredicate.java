/*    */ package org.springframework.web.servlet.mvc.support;
/*    */ 
/*    */ import org.springframework.web.servlet.mvc.Controller;
/*    */ import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
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
/*    */ class ControllerTypePredicate
/*    */ {
/*    */   public boolean isControllerType(Class<?> beanClass)
/*    */   {
/* 32 */     return Controller.class.isAssignableFrom(beanClass);
/*    */   }
/*    */   
/*    */   public boolean isMultiActionControllerType(Class<?> beanClass)
/*    */   {
/* 37 */     return MultiActionController.class.isAssignableFrom(beanClass);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\ControllerTypePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */