/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.size;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Size;
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
/*    */ public class SizeValidatorForArraysOfDouble
/*    */   extends SizeValidatorForArraysOfPrimitives
/*    */   implements ConstraintValidator<Size, double[]>
/*    */ {
/*    */   public boolean isValid(double[] array, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 32 */     if (array == null) {
/* 33 */       return true;
/*    */     }
/* 35 */     int length = Array.getLength(array);
/* 36 */     return (length >= this.min) && (length <= this.max);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\size\SizeValidatorForArraysOfDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */