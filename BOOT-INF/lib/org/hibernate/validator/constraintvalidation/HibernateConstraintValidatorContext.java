package org.hibernate.validator.constraintvalidation;

import javax.validation.ConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

public abstract interface HibernateConstraintValidatorContext
  extends ConstraintValidatorContext
{
  public abstract HibernateConstraintValidatorContext addExpressionVariable(String paramString, Object paramObject);
  
  public abstract TimeProvider getTimeProvider();
  
  public abstract HibernateConstraintValidatorContext withDynamicPayload(Object paramObject);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\constraintvalidation\HibernateConstraintValidatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */