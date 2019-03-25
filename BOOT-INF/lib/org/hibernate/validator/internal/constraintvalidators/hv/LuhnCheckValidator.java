/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import org.hibernate.validator.constraints.LuhnCheck;
/*    */ import org.hibernate.validator.internal.util.ModUtil;
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
/*    */ public class LuhnCheckValidator
/*    */   extends ModCheckBase
/*    */   implements ConstraintValidator<LuhnCheck, CharSequence>
/*    */ {
/*    */   public void initialize(LuhnCheck constraintAnnotation)
/*    */   {
/* 29 */     super.initialize(constraintAnnotation
/* 30 */       .startIndex(), constraintAnnotation
/* 31 */       .endIndex(), constraintAnnotation
/* 32 */       .checkDigitIndex(), constraintAnnotation
/* 33 */       .ignoreNonDigitCharacters());
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
/*    */ 
/*    */   public boolean isCheckDigitValid(List<Integer> digits, char checkDigit)
/*    */   {
/* 47 */     int modResult = ModUtil.calculateLuhnMod10Check(digits);
/*    */     
/* 49 */     if (!Character.isDigit(checkDigit)) {
/* 50 */       return false;
/*    */     }
/*    */     
/* 53 */     int checkValue = extractDigit(checkDigit);
/* 54 */     return checkValue == modResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\LuhnCheckValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */