/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.annotation.WebListener;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ScannedGenericBeanDefinition;
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
/*    */ class WebListenerHandler
/*    */   extends ServletComponentHandler
/*    */ {
/*    */   WebListenerHandler()
/*    */   {
/* 35 */     super(WebListener.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void doHandle(Map<String, Object> attributes, ScannedGenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*    */   {
/* 43 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServletListenerRegistrationBean.class);
/* 44 */     builder.addPropertyValue("listener", beanDefinition);
/* 45 */     registry.registerBeanDefinition(beanDefinition.getBeanClassName(), builder
/* 46 */       .getBeanDefinition());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\WebListenerHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */