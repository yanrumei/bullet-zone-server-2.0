/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Map;
/*    */ import javax.servlet.DispatcherType;
/*    */ import javax.servlet.annotation.WebFilter;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ScannedGenericBeanDefinition;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ class WebFilterHandler
/*    */   extends ServletComponentHandler
/*    */ {
/*    */   WebFilterHandler()
/*    */   {
/* 40 */     super(WebFilter.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doHandle(Map<String, Object> attributes, ScannedGenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*    */   {
/* 48 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(FilterRegistrationBean.class);
/* 49 */     builder.addPropertyValue("asyncSupported", attributes.get("asyncSupported"));
/* 50 */     builder.addPropertyValue("dispatcherTypes", extractDispatcherTypes(attributes));
/* 51 */     builder.addPropertyValue("filter", beanDefinition);
/* 52 */     builder.addPropertyValue("initParameters", extractInitParameters(attributes));
/* 53 */     String name = determineName(attributes, beanDefinition);
/* 54 */     builder.addPropertyValue("name", name);
/* 55 */     builder.addPropertyValue("servletNames", attributes.get("servletNames"));
/* 56 */     builder.addPropertyValue("urlPatterns", 
/* 57 */       extractUrlPatterns("urlPatterns", attributes));
/* 58 */     registry.registerBeanDefinition(name, builder.getBeanDefinition());
/*    */   }
/*    */   
/*    */ 
/*    */   private EnumSet<DispatcherType> extractDispatcherTypes(Map<String, Object> attributes)
/*    */   {
/* 64 */     DispatcherType[] dispatcherTypes = (DispatcherType[])attributes.get("dispatcherTypes");
/* 65 */     if (dispatcherTypes.length == 0) {
/* 66 */       return EnumSet.noneOf(DispatcherType.class);
/*    */     }
/* 68 */     if (dispatcherTypes.length == 1) {
/* 69 */       return EnumSet.of(dispatcherTypes[0]);
/*    */     }
/* 71 */     return EnumSet.of(dispatcherTypes[0], 
/* 72 */       (Enum[])Arrays.copyOfRange(dispatcherTypes, 1, dispatcherTypes.length));
/*    */   }
/*    */   
/*    */   private String determineName(Map<String, Object> attributes, BeanDefinition beanDefinition)
/*    */   {
/* 77 */     return 
/* 78 */       (String)(StringUtils.hasText((String)attributes.get("filterName")) ? attributes.get("filterName") : beanDefinition.getBeanClassName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\WebFilterHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */