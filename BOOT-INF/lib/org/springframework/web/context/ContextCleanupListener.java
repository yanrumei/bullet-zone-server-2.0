/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.DisposableBean;
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
/*    */ public class ContextCleanupListener
/*    */   implements ServletContextListener
/*    */ {
/* 43 */   private static final Log logger = LogFactory.getLog(ContextCleanupListener.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public void contextInitialized(ServletContextEvent event) {}
/*    */   
/*    */ 
/*    */   public void contextDestroyed(ServletContextEvent event)
/*    */   {
/* 52 */     cleanupAttributes(event.getServletContext());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static void cleanupAttributes(ServletContext sc)
/*    */   {
/* 62 */     Enumeration<String> attrNames = sc.getAttributeNames();
/* 63 */     while (attrNames.hasMoreElements()) {
/* 64 */       String attrName = (String)attrNames.nextElement();
/* 65 */       if (attrName.startsWith("org.springframework.")) {
/* 66 */         Object attrValue = sc.getAttribute(attrName);
/* 67 */         if ((attrValue instanceof DisposableBean)) {
/*    */           try {
/* 69 */             ((DisposableBean)attrValue).destroy();
/*    */           }
/*    */           catch (Throwable ex) {
/* 72 */             logger.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", ex);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\ContextCleanupListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */