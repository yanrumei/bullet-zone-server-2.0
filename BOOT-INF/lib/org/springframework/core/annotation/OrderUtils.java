/*    */ package org.springframework.core.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class OrderUtils
/*    */ {
/* 36 */   private static Class<? extends Annotation> priorityAnnotationType = null;
/*    */   
/*    */   static
/*    */   {
/*    */     try {
/* 41 */       priorityAnnotationType = ClassUtils.forName("javax.annotation.Priority", OrderUtils.class.getClassLoader());
/*    */     }
/*    */     catch (Throwable localThrowable) {}
/*    */   }
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
/*    */   public static Integer getOrder(Class<?> type)
/*    */   {
/* 57 */     return getOrder(type, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Integer getOrder(Class<?> type, Integer defaultOrder)
/*    */   {
/* 69 */     Order order = (Order)AnnotationUtils.findAnnotation(type, Order.class);
/* 70 */     if (order != null) {
/* 71 */       return Integer.valueOf(order.value());
/*    */     }
/* 73 */     Integer priorityOrder = getPriority(type);
/* 74 */     if (priorityOrder != null) {
/* 75 */       return priorityOrder;
/*    */     }
/* 77 */     return defaultOrder;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Integer getPriority(Class<?> type)
/*    */   {
/* 87 */     if (priorityAnnotationType != null) {
/* 88 */       Annotation priority = AnnotationUtils.findAnnotation(type, priorityAnnotationType);
/* 89 */       if (priority != null) {
/* 90 */         return (Integer)AnnotationUtils.getValue(priority);
/*    */       }
/*    */     }
/* 93 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\annotation\OrderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */