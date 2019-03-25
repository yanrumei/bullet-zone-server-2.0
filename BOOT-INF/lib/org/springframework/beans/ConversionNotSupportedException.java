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
/*    */ public class ConversionNotSupportedException
/*    */   extends TypeMismatchException
/*    */ {
/*    */   public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause)
/*    */   {
/* 39 */     super(propertyChangeEvent, requiredType, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConversionNotSupportedException(Object value, Class<?> requiredType, Throwable cause)
/*    */   {
/* 49 */     super(value, requiredType, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\ConversionNotSupportedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */