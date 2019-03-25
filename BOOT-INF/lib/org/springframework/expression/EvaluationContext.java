package org.springframework.expression;

import java.util.List;

public abstract interface EvaluationContext
{
  public abstract TypedValue getRootObject();
  
  public abstract List<ConstructorResolver> getConstructorResolvers();
  
  public abstract List<MethodResolver> getMethodResolvers();
  
  public abstract List<PropertyAccessor> getPropertyAccessors();
  
  public abstract TypeLocator getTypeLocator();
  
  public abstract TypeConverter getTypeConverter();
  
  public abstract TypeComparator getTypeComparator();
  
  public abstract OperatorOverloader getOperatorOverloader();
  
  public abstract BeanResolver getBeanResolver();
  
  public abstract void setVariable(String paramString, Object paramObject);
  
  public abstract Object lookupVariable(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\EvaluationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */