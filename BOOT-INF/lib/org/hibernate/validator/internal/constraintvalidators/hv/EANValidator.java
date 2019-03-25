/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.EAN;
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
/*    */ public class EANValidator
/*    */   implements ConstraintValidator<EAN, CharSequence>
/*    */ {
/*    */   private int size;
/*    */   
/*    */   public void initialize(EAN constraintAnnotation)
/*    */   {
/* 25 */     switch (constraintAnnotation.type()) {
/*    */     case EAN8: 
/* 27 */       this.size = 8;
/* 28 */       break;
/*    */     
/*    */     case EAN13: 
/* 31 */       this.size = 13;
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*    */   {
/* 39 */     if (value == null) {
/* 40 */       return true;
/*    */     }
/* 42 */     int length = value.length();
/* 43 */     return length == this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\EANValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */