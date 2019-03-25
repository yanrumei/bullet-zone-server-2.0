package javax.validation;

import java.lang.annotation.Annotation;

public abstract interface ConstraintValidator<A extends Annotation, T>
{
  public abstract void initialize(A paramA);
  
  public abstract boolean isValid(T paramT, ConstraintValidatorContext paramConstraintValidatorContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ConstraintValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */