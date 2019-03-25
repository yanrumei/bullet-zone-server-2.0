/*    */ package org.hibernate.validator.internal.constraintvalidators.bv;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.DecimalMin;
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
/*    */ public class DecimalMinValidatorForNumber
/*    */   implements ConstraintValidator<DecimalMin, Number>
/*    */ {
/* 27 */   private static final Log log = ;
/*    */   private BigDecimal minValue;
/*    */   private boolean inclusive;
/*    */   
/*    */   public void initialize(DecimalMin minValue)
/*    */   {
/*    */     try
/*    */     {
/* 35 */       this.minValue = new BigDecimal(minValue.value());
/*    */     }
/*    */     catch (NumberFormatException nfe) {
/* 38 */       throw log.getInvalidBigDecimalFormatException(minValue.value(), nfe);
/*    */     }
/* 40 */     this.inclusive = minValue.inclusive();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 47 */     if (value == null) {
/* 48 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 52 */     if ((value instanceof Double)) {
/* 53 */       if (((Double)value).doubleValue() == Double.POSITIVE_INFINITY) {
/* 54 */         return true;
/*    */       }
/* 56 */       if ((Double.isNaN(((Double)value).doubleValue())) || (((Double)value).doubleValue() == Double.NEGATIVE_INFINITY)) {
/* 57 */         return false;
/*    */       }
/*    */     }
/* 60 */     else if ((value instanceof Float)) {
/* 61 */       if (((Float)value).floatValue() == Float.POSITIVE_INFINITY) {
/* 62 */         return true;
/*    */       }
/* 64 */       if ((Float.isNaN(((Float)value).floatValue())) || (((Float)value).floatValue() == Float.NEGATIVE_INFINITY)) {
/* 65 */         return false;
/*    */       }
/*    */     }
/*    */     int comparisonResult;
/*    */     int comparisonResult;
/* 70 */     if ((value instanceof BigDecimal)) {
/* 71 */       comparisonResult = ((BigDecimal)value).compareTo(this.minValue);
/*    */     } else { int comparisonResult;
/* 73 */       if ((value instanceof BigInteger)) {
/* 74 */         comparisonResult = new BigDecimal((BigInteger)value).compareTo(this.minValue);
/*    */       } else { int comparisonResult;
/* 76 */         if ((value instanceof Long)) {
/* 77 */           comparisonResult = BigDecimal.valueOf(value.longValue()).compareTo(this.minValue);
/*    */         }
/*    */         else
/* 80 */           comparisonResult = BigDecimal.valueOf(value.doubleValue()).compareTo(this.minValue);
/*    */       } }
/* 82 */     return comparisonResult >= 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\DecimalMinValidatorForNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */