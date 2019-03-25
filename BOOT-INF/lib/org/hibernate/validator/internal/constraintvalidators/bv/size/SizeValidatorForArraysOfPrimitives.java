/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.size;
/*    */ 
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
/*    */ public abstract class SizeValidatorForArraysOfPrimitives
/*    */ {
/* 21 */   private static final Log log = ;
/*    */   protected int min;
/*    */   protected int max;
/*    */   
/*    */   public void initialize(Size parameters)
/*    */   {
/* 27 */     this.min = parameters.min();
/* 28 */     this.max = parameters.max();
/* 29 */     validateParameters();
/*    */   }
/*    */   
/*    */   private void validateParameters() {
/* 33 */     if (this.min < 0) {
/* 34 */       throw log.getMinCannotBeNegativeException();
/*    */     }
/* 36 */     if (this.max < 0) {
/* 37 */       throw log.getMaxCannotBeNegativeException();
/*    */     }
/* 39 */     if (this.max < this.min) {
/* 40 */       throw log.getLengthCannotBeNegativeException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\size\SizeValidatorForArraysOfPrimitives.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */