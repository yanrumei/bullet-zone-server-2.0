/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.DecimalMax;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public class DecimalMaxValidatorForCharSequence
/*    */   implements ConstraintValidator<DecimalMax, CharSequence>
/*    */ {
/* 25 */   private static final Log log = ;
/*    */   private BigDecimal maxValue;
/*    */   private boolean inclusive;
/*    */   
/*    */   public void initialize(DecimalMax maxValue)
/*    */   {
/*    */     try
/*    */     {
/* 33 */       this.maxValue = new BigDecimal(maxValue.value());
/*    */     }
/*    */     catch (NumberFormatException nfe) {
/* 36 */       throw log.getInvalidBigDecimalFormatException(maxValue.value(), nfe);
/*    */     }
/* 38 */     this.inclusive = maxValue.inclusive();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 44 */     if (value == null) {
/* 45 */       return true;
/*    */     }
/*    */     try {
/* 48 */       int comparisonResult = new BigDecimal(value.toString()).compareTo(this.maxValue);
/* 49 */       return comparisonResult <= 0;
/*    */     }
/*    */     catch (NumberFormatException nfe) {}
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DecimalMaxValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */