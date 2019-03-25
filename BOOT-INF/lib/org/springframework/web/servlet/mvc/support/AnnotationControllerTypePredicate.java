/*    */ package org.springframework.web.servlet.mvc.support;
/*    */ 
/*    */ import org.springframework.core.annotation.AnnotationUtils;
/*    */ import org.springframework.stereotype.Controller;
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
/*    */ class AnnotationControllerTypePredicate
/*    */   extends ControllerTypePredicate
/*    */ {
/*    */   public boolean isControllerType(Class<?> beanClass)
/*    */   {
/* 35 */     return (super.isControllerType(beanClass)) || 
/* 36 */       (AnnotationUtils.findAnnotation(beanClass, Controller.class) != null);
/*    */   }
/*    */   
/*    */   public boolean isMultiActionControllerType(Class<?> beanClass)
/*    */   {
/* 41 */     return (super.isMultiActionControllerType(beanClass)) || 
/* 42 */       (AnnotationUtils.findAnnotation(beanClass, Controller.class) != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\support\AnnotationControllerTypePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */