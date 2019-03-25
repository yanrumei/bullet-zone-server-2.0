/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.size;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class SizeValidatorForMap
/*    */   implements ConstraintValidator<Size, Map<?, ?>>
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
/*    */   public boolean isValid(Map<?, ?> map, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 48 */     if (map == null) {
/* 49 */       return true;
/*    */     }
/* 51 */     int size = map.size();
/* 52 */     return (size >= this.min) && (size <= this.max);
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 56 */     if (this.min < 0) {
/* 57 */       throw log.getMaxCannotBeNegativeException();
/*    */     }
/* 59 */     if (this.max < 0) {
/* 60 */       throw log.getMaxCannotBeNegativeException();
/*    */     }
/* 62 */     if (this.max < this.min) {
/* 63 */       throw log.getLengthCannotBeNegativeException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\size\SizeValidatorForMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */