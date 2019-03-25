/*    */ package org.springframework.boot.context.embedded;
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
/*    */ public class EmbeddedServletContainerCustomizerBeanPostProcessor
/*    */   implements BeanPostProcessor, BeanFactoryAware
/*    */ {
/*    */   private ListableBeanFactory beanFactory;
/*    */   private List<EmbeddedServletContainerCustomizer> customizers;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */   {
/* 49 */     Assert.isInstanceOf(ListableBeanFactory.class, beanFactory, "EmbeddedServletContainerCustomizerBeanPostProcessor can only be used with a ListableBeanFactory");
/*    */     
/*    */ 
/* 52 */     this.beanFactory = ((ListableBeanFactory)beanFactory);
/*    */   }
/*    */   
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*    */     throws BeansException
/*    */   {
/* 58 */     if ((bean instanceof ConfigurableEmbeddedServletContainer)) {
/* 59 */       postProcessBeforeInitialization((ConfigurableEmbeddedServletContainer)bean);
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
/*    */   private void postProcessBeforeInitialization(ConfigurableEmbeddedServletContainer bean)
/*    */   {
/* 72 */     for (EmbeddedServletContainerCustomizer customizer : getCustomizers()) {
/* 73 */       customizer.customize(bean);
/*    */     }
/*    */   }
/*    */   
/*    */   private Collection<EmbeddedServletContainerCustomizer> getCustomizers() {
/* 78 */     if (this.customizers == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 84 */       this.customizers = new ArrayList(this.beanFactory.getBeansOfType(EmbeddedServletContainerCustomizer.class, false, false).values());
/* 85 */       Collections.sort(this.customizers, AnnotationAwareOrderComparator.INSTANCE);
/* 86 */       this.customizers = Collections.unmodifiableList(this.customizers);
/*    */     }
/* 88 */     return this.customizers;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\EmbeddedServletContainerCustomizerBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */