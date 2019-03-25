package org.hibernate.validator.cfg.context;

import java.lang.annotation.Annotation;

public abstract interface ConstraintDefinitionTarget
{
  public abstract <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\ConstraintDefinitionTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */