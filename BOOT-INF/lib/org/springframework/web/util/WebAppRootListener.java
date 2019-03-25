/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebAppRootListener
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextInitialized(ServletContextEvent event)
/*    */   {
/* 57 */     WebUtils.setWebAppRootSystemProperty(event.getServletContext());
/*    */   }
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent event)
/*    */   {
/* 62 */     WebUtils.removeWebAppRootSystemProperty(event.getServletContext());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\WebAppRootListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */