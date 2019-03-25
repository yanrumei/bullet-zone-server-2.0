package org.springframework.expression.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;

public abstract class CompiledExpression
{
  public abstract Object getValue(Object paramObject, EvaluationContext paramEvaluationContext)
    throws EvaluationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\CompiledExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */