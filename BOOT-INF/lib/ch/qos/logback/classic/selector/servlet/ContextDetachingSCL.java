/*    */ package ch.qos.logback.classic.selector.servlet;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.selector.ContextSelector;
/*    */ import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
/*    */ import ch.qos.logback.classic.util.JNDIUtil;
/*    */ import java.io.PrintStream;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.slf4j.Logger;
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
/*    */ public class ContextDetachingSCL
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextInitialized(ServletContextEvent arg0) {}
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent servletContextEvent)
/*    */   {
/* 37 */     String loggerContextName = null;
/*    */     try
/*    */     {
/* 40 */       Context ctx = JNDIUtil.getInitialContext();
/* 41 */       loggerContextName = JNDIUtil.lookup(ctx, "java:comp/env/logback/context-name");
/*    */     }
/*    */     catch (NamingException localNamingException) {}
/*    */     
/* 45 */     if (loggerContextName != null) {
/* 46 */       System.out.println("About to detach context named " + loggerContextName);
/*    */       
/* 48 */       ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
/* 49 */       if (selector == null) {
/* 50 */         System.out.println("Selector is null, cannot detach context. Skipping.");
/* 51 */         return;
/*    */       }
/* 53 */       LoggerContext context = selector.getLoggerContext(loggerContextName);
/* 54 */       if (context != null) {
/* 55 */         Logger logger = context.getLogger("ROOT");
/* 56 */         logger.warn("Stopping logger context " + loggerContextName);
/* 57 */         selector.detachLoggerContext(loggerContextName);
/*    */         
/* 59 */         context.stop();
/*    */       } else {
/* 61 */         System.out.println("No context named " + loggerContextName + " was found.");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\selector\servlet\ContextDetachingSCL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */