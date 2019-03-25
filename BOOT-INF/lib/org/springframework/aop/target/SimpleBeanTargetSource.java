/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
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
/*    */ public class SimpleBeanTargetSource
/*    */   extends AbstractBeanFactoryBasedTargetSource
/*    */ {
/*    */   public Object getTarget()
/*    */     throws Exception
/*    */   {
/* 35 */     return getBeanFactory().getBean(getTargetBeanName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\SimpleBeanTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */