/*    */ package org.springframework.boot.context;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileEncodingApplicationListener
/*    */   implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered
/*    */ {
/* 50 */   private static final Log logger = LogFactory.getLog(FileEncodingApplicationListener.class);
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 54 */     return Integer.MAX_VALUE;
/*    */   }
/*    */   
/*    */ 
/*    */   public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event)
/*    */   {
/* 60 */     RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(event.getEnvironment(), "spring.");
/* 61 */     if (resolver.containsProperty("mandatoryFileEncoding")) {
/* 62 */       String encoding = System.getProperty("file.encoding");
/* 63 */       String desired = resolver.getProperty("mandatoryFileEncoding");
/* 64 */       if ((encoding != null) && (!desired.equalsIgnoreCase(encoding))) {
/* 65 */         logger.error("System property 'file.encoding' is currently '" + encoding + "'. It should be '" + desired + "' (as defined in 'spring.mandatoryFileEncoding').");
/*    */         
/*    */ 
/* 68 */         logger.error("Environment variable LANG is '" + System.getenv("LANG") + "'. You could use a locale setting that matches encoding='" + desired + "'.");
/*    */         
/*    */ 
/* 71 */         logger.error("Environment variable LC_ALL is '" + System.getenv("LC_ALL") + "'. You could use a locale setting that matches encoding='" + desired + "'.");
/*    */         
/*    */ 
/* 74 */         throw new IllegalStateException("The Java Virtual Machine has not been configured to use the desired default character encoding (" + desired + ").");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\FileEncodingApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */