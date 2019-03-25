/*     */ package org.springframework.validation;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ public class FieldError
/*     */   extends ObjectError
/*     */ {
/*     */   private final String field;
/*     */   private final Object rejectedValue;
/*     */   private final boolean bindingFailure;
/*     */   
/*     */   public FieldError(String objectName, String field, String defaultMessage)
/*     */   {
/*  51 */     this(objectName, field, null, false, null, null, defaultMessage);
/*     */   }
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
/*     */   public FieldError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage)
/*     */   {
/*  68 */     super(objectName, codes, arguments, defaultMessage);
/*  69 */     Assert.notNull(field, "Field must not be null");
/*  70 */     this.field = field;
/*  71 */     this.rejectedValue = rejectedValue;
/*  72 */     this.bindingFailure = bindingFailure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getField()
/*     */   {
/*  80 */     return this.field;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getRejectedValue()
/*     */   {
/*  87 */     return this.rejectedValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBindingFailure()
/*     */   {
/*  95 */     return this.bindingFailure;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 101 */     return 
/* 102 */       "Field error in object '" + getObjectName() + "' on field '" + this.field + "': rejected value [" + this.rejectedValue + "]; " + resolvableToString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 107 */     if (this == other) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (!super.equals(other)) {
/* 111 */       return false;
/*     */     }
/* 113 */     FieldError otherError = (FieldError)other;
/* 114 */     return (getField().equals(otherError.getField())) && 
/* 115 */       (ObjectUtils.nullSafeEquals(getRejectedValue(), otherError.getRejectedValue())) && 
/* 116 */       (isBindingFailure() == otherError.isBindingFailure());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 121 */     int hashCode = super.hashCode();
/* 122 */     hashCode = 29 * hashCode + getField().hashCode();
/* 123 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getRejectedValue());
/* 124 */     hashCode = 29 * hashCode + (isBindingFailure() ? 1 : 0);
/* 125 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\FieldError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */