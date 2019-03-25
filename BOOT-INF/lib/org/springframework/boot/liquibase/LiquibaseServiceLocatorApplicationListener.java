/*    */ package org.springframework.boot.liquibase;
/*    */ 
/*    */ import liquibase.servicelocator.CustomResolverServiceLocator;
/*    */ import liquibase.servicelocator.ServiceLocator;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.boot.context.event.ApplicationStartingEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class LiquibaseServiceLocatorApplicationListener
/*    */   implements ApplicationListener<ApplicationStartingEvent>
/*    */ {
/* 39 */   private static final Log logger = org.apache.commons.logging.LogFactory.getLog(LiquibaseServiceLocatorApplicationListener.class);
/*    */   
/*    */   public void onApplicationEvent(ApplicationStartingEvent event)
/*    */   {
/* 43 */     if (ClassUtils.isPresent("liquibase.servicelocator.CustomResolverServiceLocator", event
/* 44 */       .getSpringApplication().getClassLoader())) {
/* 45 */       new LiquibasePresent(null).replaceServiceLocator();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static class LiquibasePresent
/*    */   {
/*    */     public void replaceServiceLocator()
/*    */     {
/* 56 */       CustomResolverServiceLocator customResolverServiceLocator = new CustomResolverServiceLocator(new SpringPackageScanClassResolver(LiquibaseServiceLocatorApplicationListener.logger));
/* 57 */       customResolverServiceLocator.addPackageToScan(CommonsLoggingLiquibaseLogger.class
/* 58 */         .getPackage().getName());
/* 59 */       ServiceLocator.setInstance(customResolverServiceLocator);
/* 60 */       liquibase.logging.LogFactory.reset();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\liquibase\LiquibaseServiceLocatorApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */