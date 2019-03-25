package javax.validation.metadata;

import java.util.Set;

public abstract interface BeanDescriptor
  extends ElementDescriptor
{
  public abstract boolean isBeanConstrained();
  
  public abstract PropertyDescriptor getConstraintsForProperty(String paramString);
  
  public abstract Set<PropertyDescriptor> getConstrainedProperties();
  
  public abstract MethodDescriptor getConstraintsForMethod(String paramString, Class<?>... paramVarArgs);
  
  public abstract Set<MethodDescriptor> getConstrainedMethods(MethodType paramMethodType, MethodType... paramVarArgs);
  
  public abstract ConstructorDescriptor getConstraintsForConstructor(Class<?>... paramVarArgs);
  
  public abstract Set<ConstructorDescriptor> getConstrainedConstructors();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\validation-api-1.1.0.Final.jar!\javax\validation\metadata\BeanDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */