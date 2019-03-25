/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.PropertyValues;
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
/*    */ public abstract class InstantiationAwareBeanPostProcessorAdapter
/*    */   implements SmartInstantiationAwareBeanPostProcessor
/*    */ {
/*    */   public Class<?> predictBeanType(Class<?> beanClass, String beanName)
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException
/*    */   {
/* 54 */     return bean;
/*    */   }
/*    */   
/*    */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */   
/*    */   public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException
/*    */   {
/* 64 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 71 */     return pvs;
/*    */   }
/*    */   
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
/*    */   {
/* 76 */     return bean;
/*    */   }
/*    */   
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
/*    */   {
/* 81 */     return bean;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\InstantiationAwareBeanPostProcessorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */