/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import org.hibernate.validator.constraints.Mod10Check;
/*    */ import org.hibernate.validator.internal.util.ModUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mod10CheckValidator
/*    */   extends ModCheckBase
/*    */   implements ConstraintValidator<Mod10Check, CharSequence>
/*    */ {
/* 30 */   private static final Log log = ;
/*    */   
/*    */ 
/*    */ 
/*    */   private int multiplier;
/*    */   
/*    */ 
/*    */ 
/*    */   private int weight;
/*    */   
/*    */ 
/*    */ 
/*    */   public void initialize(Mod10Check constraintAnnotation)
/*    */   {
/* 44 */     super.initialize(constraintAnnotation
/* 45 */       .startIndex(), constraintAnnotation
/* 46 */       .endIndex(), constraintAnnotation
/* 47 */       .checkDigitIndex(), constraintAnnotation
/* 48 */       .ignoreNonDigitCharacters());
/*    */     
/* 50 */     this.multiplier = constraintAnnotation.multiplier();
/* 51 */     this.weight = constraintAnnotation.weight();
/*    */     
/* 53 */     if (this.multiplier < 0) {
/* 54 */       throw log.getMultiplierCannotBeNegativeException(this.multiplier);
/*    */     }
/* 56 */     if (this.weight < 0) {
/* 57 */       throw log.getWeightCannotBeNegativeException(this.weight);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isCheckDigitValid(List<Integer> digits, char checkDigit)
/*    */   {
/* 71 */     int modResult = ModUtil.calculateMod10Check(digits, this.multiplier, this.weight);
/*    */     
/* 73 */     if (!Character.isDigit(checkDigit)) {
/* 74 */       return false;
/*    */     }
/*    */     
/* 77 */     int checkValue = extractDigit(checkDigit);
/* 78 */     return checkValue == modResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\Mod10CheckValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */