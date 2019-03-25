package org.springframework.expression.spel;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;

public abstract interface SpelNode
{
  public abstract Object getValue(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract TypedValue getTypedValue(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract boolean isWritable(ExpressionState paramExpressionState)
    throws EvaluationException;
  
  public abstract void setValue(ExpressionState paramExpressionState, Object paramObject)
    throws EvaluationException;
  
  public abstract String toStringAST();
  
  public abstract int getChildCount();
  
  public abstract SpelNode getChild(int paramInt);
  
  public abstract Class<?> getObjectClass(Object paramObject);
  
  public abstract int getStartPosition();
  
  public abstract int getEndPosition();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\SpelNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */