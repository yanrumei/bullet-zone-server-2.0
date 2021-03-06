/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
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
/*    */ public class MethodInvocationException
/*    */   extends PropertyAccessException
/*    */ {
/*    */   public static final String ERROR_CODE = "methodInvocation";
/*    */   
/*    */   public MethodInvocationException(PropertyChangeEvent propertyChangeEvent, Throwable cause)
/*    */   {
/* 42 */     super(propertyChangeEvent, "Property '" + propertyChangeEvent.getPropertyName() + "' threw exception", cause);
/*    */   }
/*    */   
/*    */   public String getErrorCode()
/*    */   {
/* 47 */     return "methodInvocation";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\MethodInvocationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */