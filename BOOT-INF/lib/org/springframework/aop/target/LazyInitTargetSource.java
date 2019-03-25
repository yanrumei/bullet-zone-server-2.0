/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
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
/*    */ public class LazyInitTargetSource
/*    */   extends AbstractBeanFactoryBasedTargetSource
/*    */ {
/*    */   private Object target;
/*    */   
/*    */   public synchronized Object getTarget()
/*    */     throws BeansException
/*    */   {
/* 67 */     if (this.target == null) {
/* 68 */       this.target = getBeanFactory().getBean(getTargetBeanName());
/* 69 */       postProcessTargetObject(this.target);
/*    */     }
/* 71 */     return this.target;
/*    */   }
/*    */   
/*    */   protected void postProcessTargetObject(Object targetObject) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\LazyInitTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */