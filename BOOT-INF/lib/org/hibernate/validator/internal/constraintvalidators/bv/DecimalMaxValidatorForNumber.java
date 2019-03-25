/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.DecimalMax;
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
/*    */ public class DecimalMaxValidatorForNumber
/*    */   implements ConstraintValidator<DecimalMax, Number>
/*    */ {
/* 27 */   private static final Log log = ;
/*    */   private BigDecimal maxValue;
/*    */   private boolean inclusive;
/*    */   
/*    */   public void initialize(DecimalMax maxValue)
/*    */   {
/*    */     try
/*    */     {
/* 35 */       this.maxValue = new BigDecimal(maxValue.value());
/*    */     }
/*    */     catch (NumberFormatException nfe) {
/* 38 */       throw log.getInvalidBigDecimalFormatException(maxValue.value(), nfe);
/*    */     }
/* 40 */     this.inclusive = maxValue.inclusive();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 46 */     if (value == null) {
/* 47 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 51 */     if ((value instanceof Double)) {
/* 52 */       if (((Double)value).doubleValue() == Double.NEGATIVE_INFINITY) {
/* 53 */         return true;
/*    */       }
/* 55 */       if ((Double.isNaN(((Double)value).doubleValue())) || (((Double)value).doubleValue() == Double.POSITIVE_INFINITY)) {
/* 56 */         return false;
/*    */       }
/*    */     }
/* 59 */     else if ((value instanceof Float)) {
/* 60 */       if (((Float)value).floatValue() == Float.NEGATIVE_INFINITY) {
/* 61 */         return true;
/*    */       }
/* 63 */       if ((Float.isNaN(((Float)value).floatValue())) || (((Float)value).floatValue() == Float.POSITIVE_INFINITY)) {
/* 64 */         return false;
/*    */       }
/*    */     }
/*    */     int comparisonResult;
/*    */     int comparisonResult;
/* 69 */     if ((value instanceof BigDecimal)) {
/* 70 */       comparisonResult = ((BigDecimal)value).compareTo(this.maxValue);
/*    */     } else { int comparisonResult;
/* 72 */       if ((value instanceof BigInteger)) {
/* 73 */         comparisonResult = new BigDecimal((BigInteger)value).compareTo(this.maxValue);
/*    */       } else { int comparisonResult;
/* 75 */         if ((value instanceof Long)) {
/* 76 */           comparisonResult = BigDecimal.valueOf(value.longValue()).compareTo(this.maxValue);
/*    */         }
/*    */         else
/* 79 */           comparisonResult = BigDecimal.valueOf(value.doubleValue()).compareTo(this.maxValue);
/*    */       } }
/* 81 */     return comparisonResult <= 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DecimalMaxValidatorForNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */