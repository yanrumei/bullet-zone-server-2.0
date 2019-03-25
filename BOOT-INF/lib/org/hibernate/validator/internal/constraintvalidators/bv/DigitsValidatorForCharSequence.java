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
/*    */ public class DigitsValidatorForCharSequence
/*    */   implements ConstraintValidator<Digits, CharSequence>
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
/*    */   public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 41 */     if (charSequence == null) {
/* 42 */       return true;
/*    */     }
/*    */     
/* 45 */     BigDecimal bigNum = getBigDecimalValue(charSequence);
/* 46 */     if (bigNum == null) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     int integerPartLength = bigNum.precision() - bigNum.scale();
/* 51 */     int fractionPartLength = bigNum.scale() < 0 ? 0 : bigNum.scale();
/*    */     
/* 53 */     return (this.maxIntegerLength >= integerPartLength) && (this.maxFractionLength >= fractionPartLength);
/*    */   }
/*    */   
/*    */   private BigDecimal getBigDecimalValue(CharSequence charSequence)
/*    */   {
/*    */     try {
/* 59 */       bd = new BigDecimal(charSequence.toString());
/*    */     } catch (NumberFormatException nfe) {
/*    */       BigDecimal bd;
/* 62 */       return null; }
/*    */     BigDecimal bd;
/* 64 */     return bd;
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 68 */     if (this.maxIntegerLength < 0) {
/* 69 */       throw log.getInvalidLengthForIntegerPartException();
/*    */     }
/* 71 */     if (this.maxFractionLength < 0) {
/* 72 */       throw log.getInvalidLengthForFractionPartException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DigitsValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */