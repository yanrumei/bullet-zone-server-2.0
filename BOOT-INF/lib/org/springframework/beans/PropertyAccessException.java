/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import org.springframework.core.ErrorCoded;
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
/*    */ public abstract class PropertyAccessException
/*    */   extends BeansException
/*    */   implements ErrorCoded
/*    */ {
/*    */   private transient PropertyChangeEvent propertyChangeEvent;
/*    */   
/*    */   public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause)
/*    */   {
/* 41 */     super(msg, cause);
/* 42 */     this.propertyChangeEvent = propertyChangeEvent;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyAccessException(String msg, Throwable cause)
/*    */   {
/* 51 */     super(msg, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PropertyChangeEvent getPropertyChangeEvent()
/*    */   {
/* 61 */     return this.propertyChangeEvent;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 68 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 75 */     return this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null;
/*    */   }
/*    */   
/*    */   public abstract String getErrorCode();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\PropertyAccessException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */