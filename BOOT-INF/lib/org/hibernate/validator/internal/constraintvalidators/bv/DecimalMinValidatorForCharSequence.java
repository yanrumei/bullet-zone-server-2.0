/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.DecimalMin;
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
/*    */ public class DecimalMinValidatorForCharSequence
/*    */   implements ConstraintValidator<DecimalMin, CharSequence>
/*    */ {
/* 22 */   private static final Log log = ;
/*    */   private BigDecimal minValue;
/*    */   private boolean inclusive;
/*    */   
/*    */   public void initialize(DecimalMin minValue)
/*    */   {
/*    */     try
/*    */     {
/* 30 */       this.minValue = new BigDecimal(minValue.value());
/*    */     }
/*    */     catch (NumberFormatException nfe) {
/* 33 */       throw log.getInvalidBigDecimalFormatException(minValue.value(), nfe);
/*    */     }
/* 35 */     this.inclusive = minValue.inclusive();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 41 */     if (value == null) {
/* 42 */       return true;
/*    */     }
/*    */     try {
/* 45 */       int comparisonResult = new BigDecimal(value.toString()).compareTo(this.minValue);
/* 46 */       return comparisonResult >= 0;
/*    */     }
/*    */     catch (NumberFormatException nfe) {}
/* 49 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DecimalMinValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */