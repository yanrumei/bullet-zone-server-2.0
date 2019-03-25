/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.past;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Past;
/*    */ import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
/*    */ import org.hibernate.validator.internal.util.IgnoreJava6Requirement;
/*    */ import org.hibernate.validator.spi.time.TimeProvider;
/*    */ import org.joda.time.ReadableInstant;
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
/*    */ @IgnoreJava6Requirement
/*    */ public class PastValidatorForReadableInstant
/*    */   implements ConstraintValidator<Past, ReadableInstant>
/*    */ {
/*    */   public void initialize(Past constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(ReadableInstant value, ConstraintValidatorContext context)
/*    */   {
/* 34 */     if (value == null) {
/* 35 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 39 */     TimeProvider timeProvider = ((HibernateConstraintValidatorContext)context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
/* 40 */     long now = timeProvider.getCurrentTime();
/*    */     
/* 42 */     return value.getMillis() < now;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\past\PastValidatorForReadableInstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */