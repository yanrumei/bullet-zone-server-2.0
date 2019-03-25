/*    */ package org.springframework.boot.autoconfigure.webservices;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.boot.web.servlet.ServletRegistrationBean;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.ws.config.annotation.EnableWs;
/*    */ import org.springframework.ws.config.annotation.WsConfigurationSupport;
/*    */ import org.springframework.ws.transport.http.MessageDispatcherServlet;
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
/*    */ @Configuration
/*    */ @ConditionalOnWebApplication
/*    */ @ConditionalOnClass({MessageDispatcherServlet.class})
/*    */ @ConditionalOnMissingBean({WsConfigurationSupport.class})
/*    */ @EnableConfigurationProperties({WebServicesProperties.class})
/*    */ @AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
/*    */ public class WebServicesAutoConfiguration
/*    */ {
/*    */   private final WebServicesProperties properties;
/*    */   
/*    */   public WebServicesAutoConfiguration(WebServicesProperties properties)
/*    */   {
/* 54 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext)
/*    */   {
/* 60 */     MessageDispatcherServlet servlet = new MessageDispatcherServlet();
/* 61 */     servlet.setApplicationContext(applicationContext);
/* 62 */     String path = this.properties.getPath();
/* 63 */     String urlMapping = path + "/*";
/* 64 */     ServletRegistrationBean registration = new ServletRegistrationBean(servlet, new String[] { urlMapping });
/*    */     
/* 66 */     WebServicesProperties.Servlet servletProperties = this.properties.getServlet();
/* 67 */     registration.setLoadOnStartup(servletProperties.getLoadOnStartup());
/* 68 */     for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
/* 69 */       registration.addInitParameter((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/* 71 */     return registration;
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @EnableWs
/*    */   protected static class WsConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\webservices\WebServicesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */