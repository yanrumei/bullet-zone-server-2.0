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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeanIsNotAFactoryException
/*    */   extends BeanNotOfRequiredTypeException
/*    */ {
/*    */   public BeanIsNotAFactoryException(String name, Class<?> actualType)
/*    */   {
/* 38 */     super(name, FactoryBean.class, actualType);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanIsNotAFactoryException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */