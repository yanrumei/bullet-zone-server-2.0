/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.size;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Size;
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
/*    */ public class SizeValidatorForCharSequence
/*    */   implements ConstraintValidator<Size, CharSequence>
/*    */ {
/* 25 */   private static final Log log = ;
/*    */   
/*    */   private int min;
/*    */   private int max;
/*    */   
/*    */   public void initialize(Size parameters)
/*    */   {
/* 32 */     this.min = parameters.min();
/* 33 */     this.max = parameters.max();
/* 34 */     validateParameters();
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
/*    */   public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 48 */     if (charSequence == null) {
/* 49 */       return true;
/*    */     }
/* 51 */     int length = charSequence.length();
/* 52 */     return (length >= this.min) && (length <= this.max);
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 56 */     if (this.min < 0) {
/* 57 */       throw log.getMinCannotBeNegativeException();
/*    */     }
/* 59 */     if (this.max < 0) {
/* 60 */       throw log.getMaxCannotBeNegativeException();
/*    */     }
/* 62 */     if (this.max < this.min) {
/* 63 */       throw log.getLengthCannotBeNegativeException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\size\SizeValidatorForCharSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */