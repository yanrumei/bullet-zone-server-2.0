/*    */ package org.hibernate.validator.internal.constraintvalidators.bv.future;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.constraints.Future;
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
/*    */ public class FutureValidatorForCalendar
/*    */   implements ConstraintValidator<Future, Calendar>
/*    */ {
/*    */   public void initialize(Future constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(Calendar cal, ConstraintValidatorContext context)
/*    */   {
/* 32 */     if (cal == null) {
/* 33 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 37 */     TimeProvider timeProvider = ((HibernateConstraintValidatorContext)context.unwrap(HibernateConstraintValidatorContext.class)).getTimeProvider();
/* 38 */     long now = timeProvider.getCurrentTime();
/*    */     
/* 40 */     return cal.getTimeInMillis() > now;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\bv\future\FutureValidatorForCalendar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */