package org.hibernate.validator.cfg.context;

public abstract interface Unwrapable<U extends Unwrapable<U>>
{
  public abstract U unwrapValidatedValue(boolean paramBoolean);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\Unwrapable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */