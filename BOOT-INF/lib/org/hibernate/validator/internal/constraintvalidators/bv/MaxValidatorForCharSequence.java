/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Max;
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
/*    */ public class MaxValidatorForCharSequence
/*    */   implements ConstraintValidator<Max, CharSequence>
/*    */ {
/*    */   private BigDecimal maxValue;
/*    */   
/*    */   public void initialize(Max maxValue)
/*    */   {
/* 27 */     this.maxValue = BigDecimal.valueOf(maxValue.value());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 33 */     if (value == null) {
/* 34 */       return true;
/*    */     }
/*    */     try {
/* 37 */       return new BigDecimal(value.toString()).compareTo(this.maxValue) != 1;
/*    */     }
/*    */     catch (NumberFormatException nfe) {}
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\MaxValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */