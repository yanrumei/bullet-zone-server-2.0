/*    */ package ch.qos.logback.classic.servlet;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogbackServletContextListener
/*    */   implements ServletContextListener
/*    */ {
/* 21 */   ContextAwareBase contextAwareBase = new ContextAwareBase();
/*    */   
/*    */ 
/*    */ 
/*    */   public void contextInitialized(ServletContextEvent sce) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void contextDestroyed(ServletContextEvent sce)
/*    */   {
/* 31 */     ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
/* 32 */     if ((iLoggerFactory instanceof LoggerContext)) {
/* 33 */       LoggerContext loggerContext = (LoggerContext)iLoggerFactory;
/* 34 */       this.contextAwareBase.setContext(loggerContext);
/* 35 */       StatusViaSLF4JLoggerFactory.addInfo("About to stop " + loggerContext.getClass().getCanonicalName() + " [" + loggerContext.getName() + "]", this);
/* 36 */       loggerContext.stop();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\servlet\LogbackServletContextListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */