package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

public abstract interface Expression
{
  public abstract String getExpressionString();
  
  public abstract Object getValue()
    throws EvaluationException;
  
  public abstract <T> T getValue(Class<T> paramClass)
    throws EvaluationException;
  
  public abstract Object getValue(Object paramObject)
    throws EvaluationException;
  
  public abstract <T> T getValue(Object paramObject, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract Object getValue(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract Object getValue(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract <T> T getValue(EvaluationContext paramEvaluationContext, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract <T> T getValue(EvaluationContext paramEvaluationContext, Object paramObject, Class<T> paramClass)
    throws EvaluationException;
  
  public abstract Class<?> getValueType()
    throws EvaluationException;
  
  public abstract Class<?> getValueType(Object paramObject)
    throws EvaluationException;
  
  public abstract Class<?> getValueType(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract Class<?> getValueType(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor()
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(Object paramObject)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract TypeDescriptor getValueTypeDescriptor(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract boolean isWritable(Object paramObject)
    throws EvaluationException;
  
  public abstract boolean isWritable(EvaluationContext paramEvaluationContext)
    throws EvaluationException;
  
  public abstract boolean isWritable(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract void setValue(Object paramObject1, Object paramObject2)
    throws EvaluationException;
  
  public abstract void setValue(EvaluationContext paramEvaluationContext, Object paramObject)
    throws EvaluationException;
  
  public abstract void setValue(EvaluationContext paramEvaluationContext, Object paramObject1, Object paramObject2)
    throws EvaluationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\Expression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */