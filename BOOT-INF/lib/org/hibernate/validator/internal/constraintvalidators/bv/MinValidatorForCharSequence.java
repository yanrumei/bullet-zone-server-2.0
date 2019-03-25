/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
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
/*    */ public class MinValidatorForCharSequence
/*    */   implements ConstraintValidator<Min, CharSequence>
/*    */ {
/*    */   private BigDecimal minValue;
/*    */   
/*    */   public void initialize(Min minValue)
/*    */   {
/* 27 */     this.minValue = BigDecimal.valueOf(minValue.value());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 33 */     if (value == null) {
/* 34 */       return true;
/*    */     }
/*    */     try {
/* 37 */       return new BigDecimal(value.toString()).compareTo(this.minValue) != -1;
/*    */     }
/*    */     catch (NumberFormatException nfe) {}
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\MinValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */