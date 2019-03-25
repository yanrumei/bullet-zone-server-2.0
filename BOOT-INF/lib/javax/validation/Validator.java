package javax.validation;

import java.util.Set;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;

public abstract interface Validator
{
  public abstract <T> Set<ConstraintViolation<T>> validate(T paramT, Class<?>... paramVarArgs);
  
  public abstract <T> Set<ConstraintViolation<T>> validateProperty(T paramT, String paramString, Class<?>... paramVarArgs);
  
  public abstract <T> Set<ConstraintViolation<T>> validateValue(Class<T> paramClass, String paramString, Object paramObject, Class<?>... paramVarArgs);
  
  public abstract BeanDescriptor getConstraintsForClass(Class<?> paramClass);
  
  public abstract <T> T unwrap(Class<T> paramClass);
  
  public abstract ExecutableValidator forExecutables();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\Validator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */