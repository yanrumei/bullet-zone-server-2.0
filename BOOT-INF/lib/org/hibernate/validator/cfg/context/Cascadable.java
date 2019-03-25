package org.hibernate.validator.cfg.context;

public abstract interface Cascadable<C extends Cascadable<C>>
{
  public abstract C valid();
  
  public abstract GroupConversionTargetContext<C> convertGroup(Class<?> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\Cascadable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */