/*    */ package org.springframework.web.servlet.view;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*    */ import org.springframework.web.servlet.View;
/*    */ import org.springframework.web.servlet.ViewResolver;
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
/*    */ public class BeanNameViewResolver
/*    */   extends WebApplicationObjectSupport
/*    */   implements ViewResolver, Ordered
/*    */ {
/* 56 */   private int order = Integer.MAX_VALUE;
/*    */   
/*    */   public void setOrder(int order)
/*    */   {
/* 60 */     this.order = order;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 65 */     return this.order;
/*    */   }
/*    */   
/*    */   public View resolveViewName(String viewName, Locale locale)
/*    */     throws BeansException
/*    */   {
/* 71 */     ApplicationContext context = getApplicationContext();
/* 72 */     if (!context.containsBean(viewName)) {
/* 73 */       if (this.logger.isDebugEnabled()) {
/* 74 */         this.logger.debug("No matching bean found for view name '" + viewName + "'");
/*    */       }
/*    */       
/* 77 */       return null;
/*    */     }
/* 79 */     if (!context.isTypeMatch(viewName, View.class)) {
/* 80 */       if (this.logger.isDebugEnabled()) {
/* 81 */         this.logger.debug("Found matching bean for view name '" + viewName + "' - to be ignored since it does not implement View");
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 86 */       return null;
/*    */     }
/* 88 */     return (View)context.getBean(viewName, View.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\BeanNameViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */