package org.springframework.expression;

public abstract interface OperatorOverloader
{
  public abstract boolean overridesOperation(Operation paramOperation, Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract Object operate(Operation paramOperation, Object paramObject1, Object paramObject2)
    throws EvaluationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\OperatorOverloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */