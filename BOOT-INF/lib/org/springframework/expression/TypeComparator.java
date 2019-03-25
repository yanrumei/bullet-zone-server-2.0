package org.springframework.expression;

public abstract interface TypeComparator
{
  public abstract boolean canCompare(Object paramObject1, Object paramObject2);
  
  public abstract int compare(Object paramObject1, Object paramObject2)
    throws EvaluationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\TypeComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */