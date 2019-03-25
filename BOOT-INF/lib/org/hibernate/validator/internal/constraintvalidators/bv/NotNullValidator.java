/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ public class NotNullValidator
/*    */   implements ConstraintValidator<NotNull, Object>
/*    */ {
/*    */   public void initialize(NotNull parameters) {}
/*    */   
/*    */   public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 26 */     return object != null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\NotNullValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */