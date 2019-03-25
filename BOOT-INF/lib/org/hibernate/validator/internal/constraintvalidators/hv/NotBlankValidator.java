/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.NotBlank;
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
/*    */ 
/*    */ public class NotBlankValidator
/*    */   implements ConstraintValidator<NotBlank, CharSequence>
/*    */ {
/*    */   public void initialize(NotBlank annotation) {}
/*    */   
/*    */   public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 36 */     if (charSequence == null) {
/* 37 */       return true;
/*    */     }
/*    */     
/* 40 */     return charSequence.toString().trim().length() > 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\NotBlankValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */