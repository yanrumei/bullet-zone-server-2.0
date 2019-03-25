/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import org.springframework.beans.NotWritablePropertyException;
/*    */ import org.springframework.core.env.PropertySource;
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
/*    */ public class RelaxedBindingNotWritablePropertyException
/*    */   extends NotWritablePropertyException
/*    */ {
/*    */   private final String message;
/*    */   private final PropertyOrigin propertyOrigin;
/*    */   
/*    */   RelaxedBindingNotWritablePropertyException(NotWritablePropertyException ex, PropertyOrigin propertyOrigin)
/*    */   {
/* 38 */     super(ex.getBeanClass(), ex.getPropertyName());
/* 39 */     this.propertyOrigin = propertyOrigin;
/*    */     
/*    */ 
/* 42 */     this.message = ("Failed to bind '" + propertyOrigin.getName() + "' from '" + propertyOrigin.getSource().getName() + "' to '" + ex.getPropertyName() + "' property on '" + ex.getBeanClass().getName() + "'");
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 47 */     return this.message;
/*    */   }
/*    */   
/*    */   public PropertyOrigin getPropertyOrigin() {
/* 51 */     return this.propertyOrigin;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\RelaxedBindingNotWritablePropertyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */