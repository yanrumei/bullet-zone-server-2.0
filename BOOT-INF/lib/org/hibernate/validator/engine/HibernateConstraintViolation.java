package org.hibernate.validator.engine;

import javax.validation.ConstraintViolation;

public abstract interface HibernateConstraintViolation<T>
  extends ConstraintViolation<T>
{
  public abstract <C> C getDynamicPayload(Class<C> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\engine\HibernateConstraintViolation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */