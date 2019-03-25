/*    */ package ch.qos.logback.classic.servlet;
/*    */ 
/*    */ import ch.qos.logback.classic.util.StatusViaSLF4JLoggerFactory;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.util.Set;
/*    */ import javax.servlet.ServletContainerInitializer;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
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
/*    */ public class LogbackServletContainerInitializer
/*    */   implements ServletContainerInitializer
/*    */ {
/*    */   public void onStartup(Set<Class<?>> c, ServletContext ctx)
/*    */     throws ServletException
/*    */   {
/* 25 */     if (isDisabledByConfiguration(ctx)) {
/* 26 */       StatusViaSLF4JLoggerFactory.addInfo(
/* 27 */         "Due to deployment instructions will NOT register an instance of " + LogbackServletContextListener.class + " to the current web-app", this);
/*    */       
/* 29 */       return;
/*    */     }
/*    */     
/* 32 */     StatusViaSLF4JLoggerFactory.addInfo("Adding an instance of  " + LogbackServletContextListener.class + " to the current web-app", this);
/* 33 */     LogbackServletContextListener lscl = new LogbackServletContextListener();
/* 34 */     ctx.addListener(lscl);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean isDisabledByConfiguration(ServletContext ctx)
/*    */   {
/* 45 */     String disableAttributeStr = null;
/* 46 */     Object disableAttribute = ctx.getInitParameter("logbackDisableServletContainerInitializer");
/* 47 */     if ((disableAttribute instanceof String)) {
/* 48 */       disableAttributeStr = (String)disableAttribute;
/*    */     }
/*    */     
/* 51 */     if (OptionHelper.isEmpty(disableAttributeStr)) {
/* 52 */       disableAttributeStr = OptionHelper.getSystemProperty("logbackDisableServletContainerInitializer");
/*    */     }
/*    */     
/* 55 */     if (OptionHelper.isEmpty(disableAttributeStr)) {
/* 56 */       disableAttributeStr = OptionHelper.getEnv("logbackDisableServletContainerInitializer");
/*    */     }
/*    */     
/* 59 */     if (OptionHelper.isEmpty(disableAttributeStr)) {
/* 60 */       return false;
/*    */     }
/* 62 */     return disableAttributeStr.equalsIgnoreCase("true");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\servlet\LogbackServletContainerInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */