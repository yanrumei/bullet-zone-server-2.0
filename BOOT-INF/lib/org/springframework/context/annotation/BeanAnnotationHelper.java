/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*    */ class BeanAnnotationHelper
/*    */ {
/*    */   public static boolean isBeanAnnotated(Method method)
/*    */   {
/* 33 */     return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
/*    */   }
/*    */   
/*    */   public static String determineBeanNameFor(Method beanMethod)
/*    */   {
/* 38 */     String beanName = beanMethod.getName();
/*    */     
/*    */ 
/* 41 */     Bean bean = (Bean)AnnotatedElementUtils.findMergedAnnotation(beanMethod, Bean.class);
/* 42 */     if ((bean != null) && (bean.name().length > 0)) {
/* 43 */       beanName = bean.name()[0];
/*    */     }
/*    */     
/* 46 */     return beanName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\BeanAnnotationHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */