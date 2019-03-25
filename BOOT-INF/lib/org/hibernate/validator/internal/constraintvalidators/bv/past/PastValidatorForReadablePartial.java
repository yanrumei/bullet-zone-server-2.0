/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.past;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Past;
/*    */ import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
/*    */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
/*    */ import org.hibernate.validator.spi.time.TimeProvider;
/*    */ import org.joda.time.DateTime;
/*    */ import org.joda.time.Instant;
/*    */ import org.joda.time.ReadablePartial;
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
/*    */ @IgnoreJava6Requirement
/*    */ public class PastValidatorForReadablePartial
/*    */   implements ConstraintValidator<Past, ReadablePartial>
/*    */ {
/*    */   public void initialize(Past constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(ReadablePartial value, ConstraintValidatorContext context)
/*    */   {
/* 35 */     if (value == null) {
/* 36 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 40 */     TimeProvider timeProvider = ((HibernateConstraintValidatorContext)context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
/* 41 */     long now = timeProvider.getCurrentTime();
/*    */     
/* 43 */     return value.toDateTime(new Instant(now)).isBefore(now);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\past\PastValidatorForReadablePartial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */