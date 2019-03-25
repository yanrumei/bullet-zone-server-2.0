/*    */ package org.springframework.boot.context.embedded.tomcat;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.servlet.ServletContainerInitializer;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.web.servlet.ServletContextInitializer;
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
/*    */ class TomcatStarter
/*    */   implements ServletContainerInitializer
/*    */ {
/* 40 */   private static final Log logger = LogFactory.getLog(TomcatStarter.class);
/*    */   
/*    */   private final ServletContextInitializer[] initializers;
/*    */   private volatile Exception startUpException;
/*    */   
/*    */   TomcatStarter(ServletContextInitializer[] initializers)
/*    */   {
/* 47 */     this.initializers = initializers;
/*    */   }
/*    */   
/*    */   public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException
/*    */   {
/*    */     try
/*    */     {
/* 54 */       for (ServletContextInitializer initializer : this.initializers) {
/* 55 */         initializer.onStartup(servletContext);
/*    */       }
/*    */     }
/*    */     catch (Exception ex) {
/* 59 */       this.startUpException = ex;
/*    */       
/*    */ 
/* 62 */       if (logger.isErrorEnabled()) {
/* 63 */         logger.error("Error starting Tomcat context. Exception: " + ex
/* 64 */           .getClass().getName() + ". Message: " + ex.getMessage());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Exception getStartUpException() {
/* 70 */     return this.startUpException;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatStarter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */