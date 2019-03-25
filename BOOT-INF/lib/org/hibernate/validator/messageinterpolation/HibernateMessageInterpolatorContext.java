package org.hibernate.validator.messageinterpolation;

import javax.validation.MessageInterpolator.Context;

public abstract interface HibernateMessageInterpolatorContext
  extends MessageInterpolator.Context
{
  public abstract Class<?> getRootBeanType();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\messageinterpolation\HibernateMessageInterpolatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */