/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Min;
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
/*    */ public class MinValidatorForNumber
/*    */   implements ConstraintValidator<Min, Number>
/*    */ {
/*    */   private long minValue;
/*    */   
/*    */   public void initialize(Min minValue)
/*    */   {
/* 29 */     this.minValue = minValue.value();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 35 */     if (value == null) {
/* 36 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 40 */     if ((value instanceof Double)) {
/* 41 */       if (((Double)value).doubleValue() == Double.POSITIVE_INFINITY) {
/* 42 */         return true;
/*    */       }
/* 44 */       if ((Double.isNaN(((Double)value).doubleValue())) || (((Double)value).doubleValue() == Double.NEGATIVE_INFINITY)) {
/* 45 */         return false;
/*    */       }
/*    */     }
/* 48 */     else if ((value instanceof Float)) {
/* 49 */       if (((Float)value).floatValue() == Float.POSITIVE_INFINITY) {
/* 50 */         return true;
/*    */       }
/* 52 */       if ((Float.isNaN(((Float)value).floatValue())) || (((Float)value).floatValue() == Float.NEGATIVE_INFINITY)) {
/* 53 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 57 */     if ((value instanceof BigDecimal)) {
/* 58 */       return ((BigDecimal)value).compareTo(BigDecimal.valueOf(this.minValue)) != -1;
/*    */     }
/* 60 */     if ((value instanceof BigInteger)) {
/* 61 */       return ((BigInteger)value).compareTo(BigInteger.valueOf(this.minValue)) != -1;
/*    */     }
/*    */     
/* 64 */     long longValue = value.longValue();
/* 65 */     return longValue >= this.minValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\MinValidatorForNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */