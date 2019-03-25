package org.hibernate.validator.cfg.context;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;

public abstract interface ConstraintDefinitionContext<A extends Annotation>
  extends ConstraintMappingTarget
{
  public abstract ConstraintDefinitionContext<A> includeExistingValidators(boolean paramBoolean);
  
  public abstract ConstraintDefinitionContext<A> validatedBy(Class<? extends ConstraintValidator<A, ?>> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\ConstraintDefinitionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */