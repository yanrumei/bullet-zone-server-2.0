/*    */ package org.hibernate.validator.internal.constraintvalidators.hv.br;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.Mod11Check.ProcessingDirection;
/*    */ import org.hibernate.validator.constraints.br.CNPJ;
/*    */ import org.hibernate.validator.internal.constraintvalidators.hv.Mod11CheckValidator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CNPJValidator
/*    */   implements ConstraintValidator<CNPJ, CharSequence>
/*    */ {
/* 21 */   private static final Pattern DIGITS_ONLY = Pattern.compile("\\d+");
/*    */   
/* 23 */   private final Mod11CheckValidator withSeparatorMod11Validator1 = new Mod11CheckValidator();
/* 24 */   private final Mod11CheckValidator withSeparatorMod11Validator2 = new Mod11CheckValidator();
/*    */   
/* 26 */   private final Mod11CheckValidator withoutSeparatorMod11Validator1 = new Mod11CheckValidator();
/* 27 */   private final Mod11CheckValidator withoutSeparatorMod11Validator2 = new Mod11CheckValidator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialize(CNPJ constraintAnnotation)
/*    */   {
/* 35 */     this.withSeparatorMod11Validator1.initialize(0, 14, 16, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/* 38 */     this.withSeparatorMod11Validator2.initialize(0, 16, 17, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 44 */     this.withoutSeparatorMod11Validator1.initialize(0, 11, 12, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */     
/*    */ 
/* 47 */     this.withoutSeparatorMod11Validator2.initialize(0, 12, 13, true, 9, '0', '0', Mod11Check.ProcessingDirection.RIGHT_TO_LEFT);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*    */   {
/* 54 */     if (value == null) {
/* 55 */       return true;
/*    */     }
/*    */     
/* 58 */     if (DIGITS_ONLY.matcher(value).matches()) {
/* 59 */       return (this.withoutSeparatorMod11Validator1.isValid(value, context)) && 
/* 60 */         (this.withoutSeparatorMod11Validator2.isValid(value, context));
/*    */     }
/*    */     
/* 63 */     return (this.withSeparatorMod11Validator1.isValid(value, context)) && 
/* 64 */       (this.withSeparatorMod11Validator2.isValid(value, context));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\br\CNPJValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */