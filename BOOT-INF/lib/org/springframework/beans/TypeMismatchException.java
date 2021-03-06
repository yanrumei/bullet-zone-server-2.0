/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeMismatchException
/*     */   extends PropertyAccessException
/*     */ {
/*     */   public static final String ERROR_CODE = "typeMismatch";
/*     */   private transient Object value;
/*     */   private Class<?> requiredType;
/*     */   
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType)
/*     */   {
/*  49 */     this(propertyChangeEvent, requiredType, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType, Throwable cause)
/*     */   {
/*  59 */     super(propertyChangeEvent, "Failed to convert property value of type '" + 
/*     */     
/*  61 */       ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue()) + "'" + (requiredType != null ? " to required type '" + 
/*     */       
/*  63 */       ClassUtils.getQualifiedName(requiredType) + "'" : "") + (propertyChangeEvent
/*  64 */       .getPropertyName() != null ? " for property '" + propertyChangeEvent
/*  65 */       .getPropertyName() + "'" : ""), cause);
/*     */     
/*  67 */     this.value = propertyChangeEvent.getNewValue();
/*  68 */     this.requiredType = requiredType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeMismatchException(Object value, Class<?> requiredType)
/*     */   {
/*  77 */     this(value, requiredType, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause)
/*     */   {
/*  87 */     super("Failed to convert value of type '" + ClassUtils.getDescriptiveType(value) + "'" + (requiredType != null ? " to required type '" + 
/*  88 */       ClassUtils.getQualifiedName(requiredType) + "'" : ""), cause);
/*     */     
/*  90 */     this.value = value;
/*  91 */     this.requiredType = requiredType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 100 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Class<?> getRequiredType()
/*     */   {
/* 107 */     return this.requiredType;
/*     */   }
/*     */   
/*     */   public String getErrorCode()
/*     */   {
/* 112 */     return "typeMismatch";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\TypeMismatchException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */