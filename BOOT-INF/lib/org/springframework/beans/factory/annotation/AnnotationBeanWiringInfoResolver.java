/*    */ package org.springframework.beans.factory.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.wiring.BeanWiringInfo;
/*    */ import org.springframework.beans.factory.wiring.BeanWiringInfoResolver;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class AnnotationBeanWiringInfoResolver
/*    */   implements BeanWiringInfoResolver
/*    */ {
/*    */   public BeanWiringInfo resolveWiringInfo(Object beanInstance)
/*    */   {
/* 41 */     Assert.notNull(beanInstance, "Bean instance must not be null");
/* 42 */     Configurable annotation = (Configurable)beanInstance.getClass().getAnnotation(Configurable.class);
/* 43 */     return annotation != null ? buildWiringInfo(beanInstance, annotation) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected BeanWiringInfo buildWiringInfo(Object beanInstance, Configurable annotation)
/*    */   {
/* 53 */     if (!Autowire.NO.equals(annotation.autowire())) {
/* 54 */       return new BeanWiringInfo(annotation.autowire().value(), annotation.dependencyCheck());
/*    */     }
/*    */     
/* 57 */     if (!"".equals(annotation.value()))
/*    */     {
/* 59 */       return new BeanWiringInfo(annotation.value(), false);
/*    */     }
/*    */     
/*    */ 
/* 63 */     return new BeanWiringInfo(getDefaultBeanName(beanInstance), true);
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
/*    */   protected String getDefaultBeanName(Object beanInstance)
/*    */   {
/* 77 */     return ClassUtils.getUserClass(beanInstance).getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\annotation\AnnotationBeanWiringInfoResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */