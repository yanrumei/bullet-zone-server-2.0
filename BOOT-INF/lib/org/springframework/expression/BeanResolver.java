package org.springframework.expression;

public abstract interface BeanResolver
{
  public abstract Object resolve(EvaluationContext paramEvaluationContext, String paramString)
    throws AccessException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\BeanResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */