/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.past;
/*    */ 
/*    */ import java.util.Date;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Past;
/*    */ import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
/*    */ import org.hibernate.validator.spi.time.TimeProvider;
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
/*    */ public class PastValidatorForDate
/*    */   implements ConstraintValidator<Past, Date>
/*    */ {
/*    */   public void initialize(Past constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext)
/*    */   {
/* 32 */     if (date == null) {
/* 33 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 37 */     TimeProvider timeProvider = ((HibernateConstraintValidatorContext)constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
/* 38 */     long now = timeProvider.getCurrentTime();
/*    */     
/* 40 */     return date.getTime() < now;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\past\PastValidatorForDate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */