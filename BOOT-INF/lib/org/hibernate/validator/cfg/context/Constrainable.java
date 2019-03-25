package org.hibernate.validator.cfg.context;

import org.hibernate.validator.cfg.ConstraintDef;

public abstract interface Constrainable<C extends Constrainable<C>>
{
  public abstract C constraint(ConstraintDef<?, ?> paramConstraintDef);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\Constrainable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */