/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.Length;
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
/*    */ public class LengthValidator
/*    */   implements ConstraintValidator<Length, CharSequence>
/*    */ {
/* 24 */   private static final Log log = ;
/*    */   
/*    */   private int min;
/*    */   private int max;
/*    */   
/*    */   public void initialize(Length parameters)
/*    */   {
/* 31 */     this.min = parameters.min();
/* 32 */     this.max = parameters.max();
/* 33 */     validateParameters();
/*    */   }
/*    */   
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 38 */     if (value == null) {
/* 39 */       return true;
/*    */     }
/* 41 */     int length = value.length();
/* 42 */     return (length >= this.min) && (length <= this.max);
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 46 */     if (this.min < 0) {
/* 47 */       throw log.getMinCannotBeNegativeException();
/*    */     }
/* 49 */     if (this.max < 0) {
/* 50 */       throw log.getMaxCannotBeNegativeException();
/*    */     }
/* 52 */     if (this.max < this.min) {
/* 53 */       throw log.getLengthCannotBeNegativeException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\LengthValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */