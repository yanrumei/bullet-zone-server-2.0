/*    */ package org.springframework.boot.web.servlet;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.MultipartConfigElement;
/*    */ import javax.servlet.annotation.MultipartConfig;
/*    */ import javax.servlet.annotation.WebServlet;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionBuilder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.context.annotation.ScannedGenericBeanDefinition;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ class WebServletHandler
/*    */   extends ServletComponentHandler
/*    */ {
/*    */   WebServletHandler()
/*    */   {
/* 39 */     super(WebServlet.class);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void doHandle(Map<String, Object> attributes, ScannedGenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry)
/*    */   {
/* 47 */     BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServletRegistrationBean.class);
/* 48 */     builder.addPropertyValue("asyncSupported", attributes.get("asyncSupported"));
/* 49 */     builder.addPropertyValue("initParameters", extractInitParameters(attributes));
/* 50 */     builder.addPropertyValue("loadOnStartup", attributes.get("loadOnStartup"));
/* 51 */     String name = determineName(attributes, beanDefinition);
/* 52 */     builder.addPropertyValue("name", name);
/* 53 */     builder.addPropertyValue("servlet", beanDefinition);
/* 54 */     builder.addPropertyValue("urlMappings", 
/* 55 */       extractUrlPatterns("urlPatterns", attributes));
/* 56 */     builder.addPropertyValue("multipartConfig", 
/* 57 */       determineMultipartConfig(beanDefinition));
/* 58 */     registry.registerBeanDefinition(name, builder.getBeanDefinition());
/*    */   }
/*    */   
/*    */   private String determineName(Map<String, Object> attributes, BeanDefinition beanDefinition)
/*    */   {
/* 63 */     return 
/* 64 */       (String)(StringUtils.hasText((String)attributes.get("name")) ? attributes.get("name") : beanDefinition.getBeanClassName());
/*    */   }
/*    */   
/*    */ 
/*    */   private MultipartConfigElement determineMultipartConfig(ScannedGenericBeanDefinition beanDefinition)
/*    */   {
/* 70 */     Map<String, Object> attributes = beanDefinition.getMetadata().getAnnotationAttributes(MultipartConfig.class.getName());
/* 71 */     if (attributes == null) {
/* 72 */       return null;
/*    */     }
/* 74 */     return new MultipartConfigElement((String)attributes.get("location"), 
/* 75 */       ((Long)attributes.get("maxFileSize")).longValue(), 
/* 76 */       ((Long)attributes.get("maxRequestSize")).longValue(), 
/* 77 */       ((Integer)attributes.get("fileSizeThreshold")).intValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\web\servlet\WebServletHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */