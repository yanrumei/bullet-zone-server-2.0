package javax.validation;

public abstract interface ConstraintValidatorFactory
{
  public abstract <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> paramClass);
  
  public abstract void releaseInstance(ConstraintValidator<?, ?> paramConstraintValidator);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ConstraintValidatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */