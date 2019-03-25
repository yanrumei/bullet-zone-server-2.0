package javax.validation.executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;

public abstract interface ExecutableValidator
{
  public abstract <T> Set<ConstraintViolation<T>> validateParameters(T paramT, Method paramMethod, Object[] paramArrayOfObject, Class<?>... paramVarArgs);
  
  public abstract <T> Set<ConstraintViolation<T>> validateReturnValue(T paramT, Method paramMethod, Object paramObject, Class<?>... paramVarArgs);
  
  public abstract <T> Set<ConstraintViolation<T>> validateConstructorParameters(Constructor<? extends T> paramConstructor, Object[] paramArrayOfObject, Class<?>... paramVarArgs);
  
  public abstract <T> Set<ConstraintViolation<T>> validateConstructorReturnValue(Constructor<? extends T> paramConstructor, T paramT, Class<?>... paramVarArgs);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\executable\ExecutableValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */