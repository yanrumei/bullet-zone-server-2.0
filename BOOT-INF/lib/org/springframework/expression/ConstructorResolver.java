package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;

public abstract interface ConstructorResolver
{
  public abstract ConstructorExecutor resolve(EvaluationContext paramEvaluationContext, String paramString, List<TypeDescriptor> paramList)
    throws AccessException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\ConstructorResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */