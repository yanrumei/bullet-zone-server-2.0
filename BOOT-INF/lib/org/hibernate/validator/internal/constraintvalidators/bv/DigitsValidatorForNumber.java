/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Digits;
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
/*    */ 
/*    */ public class DigitsValidatorForNumber
/*    */   implements ConstraintValidator<Digits, Number>
/*    */ {
/* 26 */   private static final Log log = ;
/*    */   
/*    */   private int maxIntegerLength;
/*    */   private int maxFractionLength;
/*    */   
/*    */   public void initialize(Digits constraintAnnotation)
/*    */   {
/* 33 */     this.maxIntegerLength = constraintAnnotation.integer();
/* 34 */     this.maxFractionLength = constraintAnnotation.fraction();
/* 35 */     validateParameters();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(Number num, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 41 */     if (num == null) {
/* 42 */       return true;
/*    */     }
/*    */     BigDecimal bigNum;
/*    */     BigDecimal bigNum;
/* 46 */     if ((num instanceof BigDecimal)) {
/* 47 */       bigNum = (BigDecimal)num;
/*    */     }
/*    */     else {
/* 50 */       bigNum = new BigDecimal(num.toString()).stripTrailingZeros();
/*    */     }
/*    */     
/* 53 */     int integerPartLength = bigNum.precision() - bigNum.scale();
/* 54 */     int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();
/*    */     
/* 56 */     return (this.maxIntegerLength >= integerPartLength) && (this.maxFractionLength >= fractionPartLength);
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 60 */     if (this.maxIntegerLength < 0) {
/* 61 */       throw log.getInvalidLengthForIntegerPartException();
/*    */     }
/* 63 */     if (this.maxFractionLength < 0) {
/* 64 */       throw log.getInvalidLengthForFractionPartException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DigitsValidatorForNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */