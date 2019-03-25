package org.hibernate.validator.cfg.context;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

public abstract interface TypeConstraintMappingContext<C>
  extends Constrainable<TypeConstraintMappingContext<C>>, ConstraintMappingTarget, PropertyTarget, MethodTarget, ConstructorTarget, AnnotationProcessingOptions<TypeConstraintMappingContext<C>>, AnnotationIgnoreOptions<TypeConstraintMappingContext<C>>
{
  public abstract TypeConstraintMappingContext<C> ignoreAllAnnotations();
  
  public abstract TypeConstraintMappingContext<C> defaultGroupSequence(Class<?>... paramVarArgs);
  
  public abstract TypeConstraintMappingContext<C> defaultGroupSequenceProviderClass(Class<? extends DefaultGroupSequenceProvider<? super C>> paramClass);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\cfg\context\TypeConstraintMappingContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */