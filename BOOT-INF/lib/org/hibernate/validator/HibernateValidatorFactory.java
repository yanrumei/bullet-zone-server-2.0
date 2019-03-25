package org.hibernate.validator;

import javax.validation.ValidatorFactory;

public abstract interface HibernateValidatorFactory
  extends ValidatorFactory
{
  public abstract HibernateValidatorContext usingContext();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\HibernateValidatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */