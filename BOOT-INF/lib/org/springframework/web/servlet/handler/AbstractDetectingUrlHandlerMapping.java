/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextException;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public abstract class AbstractDetectingUrlHandlerMapping
/*    */   extends AbstractUrlHandlerMapping
/*    */ {
/* 35 */   private boolean detectHandlersInAncestorContexts = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts)
/*    */   {
/* 47 */     this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initApplicationContext()
/*    */     throws ApplicationContextException
/*    */   {
/* 57 */     super.initApplicationContext();
/* 58 */     detectHandlers();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void detectHandlers()
/*    */     throws BeansException
/*    */   {
/* 70 */     if (this.logger.isDebugEnabled()) {
/* 71 */       this.logger.debug("Looking for URL mappings in application context: " + getApplicationContext());
/*    */     }
/*    */     
/*    */ 
/* 75 */     String[] beanNames = this.detectHandlersInAncestorContexts ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) : getApplicationContext().getBeanNamesForType(Object.class);
/*    */     
/*    */ 
/* 78 */     for (String beanName : beanNames) {
/* 79 */       String[] urls = determineUrlsForHandler(beanName);
/* 80 */       if (!ObjectUtils.isEmpty(urls))
/*    */       {
/* 82 */         registerHandler(urls, beanName);
/*    */ 
/*    */       }
/* 85 */       else if (this.logger.isDebugEnabled()) {
/* 86 */         this.logger.debug("Rejected bean name '" + beanName + "': no URL paths identified");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract String[] determineUrlsForHandler(String paramString);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractDetectingUrlHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */