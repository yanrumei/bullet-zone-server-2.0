/*    */ package org.springframework.boot.logging.log4j2;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.springframework.boot.logging.LoggingSystem;
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
/*    */ @Plugin(name="SpringBootConfigurationFactory", category="ConfigurationFactory")
/*    */ @Order(0)
/*    */ public class SpringBootConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 44 */   private static final String[] TYPES = { ".springboot" };
/*    */   
/*    */   protected String[] getSupportedTypes()
/*    */   {
/* 48 */     return TYPES;
/*    */   }
/*    */   
/*    */ 
/*    */   public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source)
/*    */   {
/* 54 */     if ((source != null) && (source != ConfigurationSource.NULL_SOURCE) && 
/* 55 */       (LoggingSystem.get(loggerContext.getClass().getClassLoader()) != null)) {
/* 56 */       return new DefaultConfiguration();
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\log4j2\SpringBootConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */