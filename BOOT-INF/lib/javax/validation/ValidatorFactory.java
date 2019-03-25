package javax.validation;

public abstract interface ValidatorFactory
{
  public abstract Validator getValidator();
  
  public abstract ValidatorContext usingContext();
  
  public abstract MessageInterpolator getMessageInterpolator();
  
  public abstract TraversableResolver getTraversableResolver();
  
  public abstract ConstraintValidatorFactory getConstraintValidatorFactory();
  
  public abstract ParameterNameProvider getParameterNameProvider();
  
  public abstract <T> T unwrap(Class<T> paramClass);
  
  public abstract void close();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ValidatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */