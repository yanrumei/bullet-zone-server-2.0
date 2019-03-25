/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
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
/*    */ public class ErrorPageRegistrarBeanPostProcessor
/*    */   implements BeanPostProcessor, BeanFactoryAware
/*    */ {
/*    */   private ListableBeanFactory beanFactory;
/*    */   private List<ErrorPageRegistrar> registrars;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */   {
/* 49 */     Assert.isInstanceOf(ListableBeanFactory.class, beanFactory, "ErrorPageRegistrarBeanPostProcessor can only be used with a ListableBeanFactory");
/*    */     
/*    */ 
/* 52 */     this.beanFactory = ((ListableBeanFactory)beanFactory);
/*    */   }
/*    */   
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 58 */     if ((bean instanceof ErrorPageRegistry)) {
/* 59 */       postProcessBeforeInitialization((ErrorPageRegistry)bean);
/*    */     }
/* 61 */     return bean;
/*    */   }
/*    */   
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 67 */     return bean;
/*    */   }
/*    */   
/*    */   private void postProcessBeforeInitialization(ErrorPageRegistry registry) {
/* 71 */     for (ErrorPageRegistrar registrar : getRegistrars()) {
/* 72 */       registrar.registerErrorPages(registry);
/*    */     }
/*    */   }
/*    */   
/*    */   private Collection<ErrorPageRegistrar> getRegistrars() {
/* 77 */     if (this.registrars == null)
/*    */     {
/*    */ 
/* 80 */       this.registrars = new ArrayList(this.beanFactory.getBeansOfType(ErrorPageRegistrar.class, false, false).values());
/* 81 */       Collections.sort(this.registrars, AnnotationAwareOrderComparator.INSTANCE);
/* 82 */       this.registrars = Collections.unmodifiableList(this.registrars);
/*    */     }
/* 84 */     return this.registrars;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\ErrorPageRegistrarBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */