/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import org.hibernate.validator.constraints.ModCheck;
/*    */ import org.hibernate.validator.constraints.ModCheck.ModType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ModCheckValidator
/*    */   extends ModCheckBase
/*    */   implements ConstraintValidator<ModCheck, CharSequence>
/*    */ {
/*    */   private int multiplier;
/*    */   private ModCheck.ModType modType;
/*    */   
/*    */   public void initialize(ModCheck constraintAnnotation)
/*    */   {
/* 40 */     super.initialize(constraintAnnotation
/* 41 */       .startIndex(), constraintAnnotation
/* 42 */       .endIndex(), constraintAnnotation
/* 43 */       .checkDigitPosition(), constraintAnnotation
/* 44 */       .ignoreNonDigitCharacters());
/*    */     
/*    */ 
/* 47 */     this.modType = constraintAnnotation.modType();
/* 48 */     this.multiplier = constraintAnnotation.multiplier();
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
/* 61 */     int modResult = -1;
/* 62 */     int checkValue = extractDigit(checkDigit);
/*    */     
/* 64 */     if (this.modType.equals(ModCheck.ModType.MOD11)) {
/* 65 */       modResult = ModUtil.calculateMod11Check(digits, this.multiplier);
/*    */       
/* 67 */       if ((modResult == 10) || (modResult == 11)) {
/* 68 */         modResult = 0;
/*    */       }
/*    */     }
/*    */     else {
/* 72 */       modResult = ModUtil.calculateLuhnMod10Check(digits);
/*    */     }
/*    */     
/* 75 */     return checkValue == modResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\ModCheckValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */