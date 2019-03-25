package javax.validation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public abstract interface ParameterNameProvider
{
  public abstract List<String> getParameterNames(Constructor<?> paramConstructor);
  
  public abstract List<String> getParameterNames(Method paramMethod);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\ParameterNameProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */