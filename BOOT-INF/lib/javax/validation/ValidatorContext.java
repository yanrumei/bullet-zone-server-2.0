package javax.validation;

public abstract interface ValidatorContext
{
  public abstract ValidatorContext messageInterpolator(MessageInterpolator paramMessageInterpolator);
  
  public abstract ValidatorContext traversableResolver(TraversableResolver paramTraversableResolver);
  
  public abstract ValidatorContext constraintValidatorFactory(ConstraintValidatorFactory paramConstraintValidatorFactory);
  
  public abstract ValidatorContext parameterNameProvider(ParameterNameProvider paramParameterNameProvider);
  
  public abstract Validator getValidator();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ValidatorContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */