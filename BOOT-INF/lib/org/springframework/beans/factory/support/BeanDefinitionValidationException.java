/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
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
/*    */ public class BeanDefinitionValidationException
/*    */   extends FatalBeanException
/*    */ {
/*    */   public BeanDefinitionValidationException(String msg)
/*    */   {
/* 36 */     super(msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BeanDefinitionValidationException(String msg, Throwable cause)
/*    */   {
/* 46 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\BeanDefinitionValidationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */