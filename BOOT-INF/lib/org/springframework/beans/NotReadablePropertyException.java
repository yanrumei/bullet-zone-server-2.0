/*    */ package org.springframework.beans;
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
/*    */ public class NotReadablePropertyException
/*    */   extends InvalidPropertyException
/*    */ {
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName)
/*    */   {
/* 35 */     super(beanClass, propertyName, "Bean property '" + propertyName + "' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName, String msg)
/*    */   {
/* 47 */     super(beanClass, propertyName, msg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NotReadablePropertyException(Class<?> beanClass, String propertyName, String msg, Throwable cause)
/*    */   {
/* 59 */     super(beanClass, propertyName, msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\NotReadablePropertyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */