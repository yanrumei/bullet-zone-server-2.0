package org.springframework.expression;

public abstract interface PropertyAccessor
{
  public abstract Class<?>[] getSpecificTargetClasses();
  
  public abstract boolean canRead(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract TypedValue read(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract boolean canWrite(EvaluationContext paramEvaluationContext, Object paramObject, String paramString)
    throws AccessException;
  
  public abstract void write(EvaluationContext paramEvaluationContext, Object paramObject1, String paramString, Object paramObject2)
    throws AccessException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\PropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */