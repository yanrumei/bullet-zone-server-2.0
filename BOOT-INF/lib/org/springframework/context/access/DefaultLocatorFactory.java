/*    */ package org.springframework.context.access;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
/*    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
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
/*    */ public class DefaultLocatorFactory
/*    */ {
/*    */   public static BeanFactoryLocator getInstance()
/*    */     throws FatalBeanException
/*    */   {
/* 36 */     return ContextSingletonBeanFactoryLocator.getInstance();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static BeanFactoryLocator getInstance(String selector)
/*    */     throws FatalBeanException
/*    */   {
/* 47 */     return ContextSingletonBeanFactoryLocator.getInstance(selector);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\access\DefaultLocatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */