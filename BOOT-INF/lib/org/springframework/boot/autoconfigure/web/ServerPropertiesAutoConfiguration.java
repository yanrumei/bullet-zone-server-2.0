/*    */ package org.springframework.boot.autoconfigure.web;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*    */ import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextAware;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @EnableConfigurationProperties
/*    */ @ConditionalOnWebApplication
/*    */ public class ServerPropertiesAutoConfiguration
/*    */ {
/*    */   @Bean
/*    */   @ConditionalOnMissingBean(search=SearchStrategy.CURRENT)
/*    */   public ServerProperties serverProperties()
/*    */   {
/* 50 */     return new ServerProperties();
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public DuplicateServerPropertiesDetector duplicateServerPropertiesDetector() {
/* 55 */     return new DuplicateServerPropertiesDetector(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static class DuplicateServerPropertiesDetector
/*    */     implements EmbeddedServletContainerCustomizer, Ordered, ApplicationContextAware
/*    */   {
/*    */     private ApplicationContext applicationContext;
/*    */     
/*    */ 
/*    */ 
/*    */     public int getOrder()
/*    */     {
/* 69 */       return 0;
/*    */     }
/*    */     
/*    */     public void setApplicationContext(ApplicationContext applicationContext)
/*    */       throws BeansException
/*    */     {
/* 75 */       this.applicationContext = applicationContext;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public void customize(ConfigurableEmbeddedServletContainer container)
/*    */     {
/* 83 */       String[] serverPropertiesBeans = this.applicationContext.getBeanNamesForType(ServerProperties.class);
/* 84 */       Assert.state(serverPropertiesBeans.length != 0, "No ServerProperties registered");
/*    */       
/* 86 */       Assert.state(serverPropertiesBeans.length == 1, "Multiple ServerProperties registered " + 
/*    */       
/* 88 */         StringUtils.arrayToCommaDelimitedString(serverPropertiesBeans));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\ServerPropertiesAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */