package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;

public abstract interface MethodFilter
{
  public abstract List<Method> filter(List<Method> paramList);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\MethodFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */