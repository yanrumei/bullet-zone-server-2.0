/*    */ package org.springframework.beans.factory;
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
/*    */ public class BeanIsAbstractException
/*    */   extends BeanCreationException
/*    */ {
/*    */   public BeanIsAbstractException(String beanName)
/*    */   {
/* 35 */     super(beanName, "Bean definition is abstract");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanIsAbstractException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */