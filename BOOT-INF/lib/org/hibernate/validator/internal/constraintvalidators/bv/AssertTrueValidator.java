/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.AssertTrue;
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
/*    */ public class AssertTrueValidator
/*    */   implements ConstraintValidator<AssertTrue, Boolean>
/*    */ {
/*    */   public void initialize(AssertTrue constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(Boolean bool, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 27 */     return (bool == null) || (bool.booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\AssertTrueValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */