/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.size;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class SizeValidatorForCollection
/*    */   implements ConstraintValidator<Size, Collection<?>>
/*    */ {
/* 24 */   private static final Log log = ;
/*    */   
/*    */   private int min;
/*    */   private int max;
/*    */   
/*    */   public void initialize(Size parameters)
/*    */   {
/* 31 */     this.min = parameters.min();
/* 32 */     this.max = parameters.max();
/* 33 */     validateParameters();
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
/*    */ 
/*    */   public boolean isValid(Collection<?> collection, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 48 */     if (collection == null) {
/* 49 */       return true;
/*    */     }
/* 51 */     int length = collection.size();
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


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\size\SizeValidatorForCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */