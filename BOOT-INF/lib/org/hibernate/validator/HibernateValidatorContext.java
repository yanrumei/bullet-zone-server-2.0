package org.hibernate.validator;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;
import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;

public abstract interface HibernateValidatorContext
  extends ValidatorContext
{
  public abstract HibernateValidatorContext messageInterpolator(MessageInterpolator paramMessageInterpolator);
  
  public abstract HibernateValidatorContext traversableResolver(TraversableResolver paramTraversableResolver);
  
  public abstract HibernateValidatorContext constraintValidatorFactory(ConstraintValidatorFactory paramConstraintValidatorFactory);
  
  public abstract HibernateValidatorContext parameterNameProvider(ParameterNameProvider paramParameterNameProvider);
  
  public abstract HibernateValidatorContext failFast(boolean paramBoolean);
  
  public abstract HibernateValidatorContext addValidationValueHandler(ValidatedValueUnwrapper<?> paramValidatedValueUnwrapper);
  
  public abstract HibernateValidatorContext timeProvider(TimeProvider paramTimeProvider);
  
  public abstract HibernateValidatorContext allowOverridingMethodAlterParameterConstraint(boolean paramBoolean);
  
  public abstract HibernateValidatorContext allowMultipleCascadedValidationOnReturnValues(boolean paramBoolean);
  
  public abstract HibernateValidatorContext allowParallelMethodsDefineParameterConstraints(boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\HibernateValidatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */