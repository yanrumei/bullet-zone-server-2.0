/*    */ package org.hibernate.validator.internal.constraintvalidators.hv.br;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
/*    */ import org.hibernate.validator.constraints.br.CPF;
/*    */ import org.hibernate.validator.internal.constraintvalidators.hv.Mod11CheckValidator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CPFValidator
/*    */   implements ConstraintValidator<CPF, CharSequence>
/*    */ {
/* 21 */   private static final Pattern DIGITS_ONLY = Pattern.compile("\\d+");
/* 22 */   private static final Pattern SINGLE_DASH_SEPARATOR = Pattern.compile("\\d+-\\d\\d");
/*    */   
/* 24 */   private final Mod11CheckValidator withSeparatorMod11Validator1 = new Mod11CheckValidator();
/* 25 */   private final Mod11CheckValidator withSeparatorMod11Validator2 = new Mod11CheckValidator();
/*    */   
/* 27 */   private final Mod11CheckValidator withDashOnlySeparatorMod11Validator1 = new Mod11CheckValidator();
/* 28 */   private final Mod11CheckValidator withDashOnlySeparatorMod11Validator2 = new Mod11CheckValidator();
/*    */   
/* 30 */   private final Mod11CheckValidator withoutSeparatorMod11Validator1 = new Mod11CheckValidator();
/* 31 */   private final Mod11CheckValidator withoutSeparatorMod11Validator2 = new Mod11CheckValidator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialize(CPF constraintAnnotation)
/*    */   {
/* 39 */     this.withSeparatorMod11Validator1.initialize(0, 10, 12, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/* 42 */     this.withSeparatorMod11Validator2.initialize(0, 12, 13, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 47 */     this.withDashOnlySeparatorMod11Validator1.initialize(0, 8, 10, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/* 50 */     this.withDashOnlySeparatorMod11Validator2.initialize(0, 10, 11, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 56 */     this.withoutSeparatorMod11Validator1.initialize(0, 8, 9, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/* 59 */     this.withoutSeparatorMod11Validator2.initialize(0, 9, 10, true, Integer.MAX_VALUE, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*    */   {
/* 66 */     if (value == null) {
/* 67 */       return true;
/*    */     }
/*    */     
/* 70 */     if (DIGITS_ONLY.matcher(value).matches()) {
/* 71 */       return (this.withoutSeparatorMod11Validator1.isValid(value, context)) && 
/* 72 */         (this.withoutSeparatorMod11Validator2.isValid(value, context));
/*    */     }
/* 74 */     if (SINGLE_DASH_SEPARATOR.matcher(value).matches()) {
/* 75 */       return (this.withDashOnlySeparatorMod11Validator1.isValid(value, context)) && 
/* 76 */         (this.withDashOnlySeparatorMod11Validator2.isValid(value, context));
/*    */     }
/*    */     
/* 79 */     return (this.withSeparatorMod11Validator1.isValid(value, context)) && 
/* 80 */       (this.withSeparatorMod11Validator2.isValid(value, context));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\br\CPFValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */