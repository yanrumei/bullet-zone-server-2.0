package org.hibernate.validator.cfg;

import java.lang.annotation.Annotation;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;

public abstract interface ConstraintMapping
{
  public abstract <C> TypeConstraintMappingContext<C> type(Class<C> paramClass);
  
  public abstract <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\ConstraintMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */