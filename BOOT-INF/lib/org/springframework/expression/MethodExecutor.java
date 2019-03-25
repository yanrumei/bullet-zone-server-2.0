package org.springframework.expression;

public abstract interface MethodExecutor
{
  public abstract TypedValue execute(EvaluationContext paramEvaluationContext, Object paramObject, Object... paramVarArgs)
    throws AccessException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\MethodExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */