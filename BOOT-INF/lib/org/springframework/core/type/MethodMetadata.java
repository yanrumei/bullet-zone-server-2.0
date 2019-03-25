package org.springframework.core.type;

public abstract interface MethodMetadata
  extends AnnotatedTypeMetadata
{
  public abstract String getMethodName();
  
  public abstract String getDeclaringClassName();
  
  public abstract String getReturnTypeName();
  
  public abstract boolean isAbstract();
  
  public abstract boolean isStatic();
  
  public abstract boolean isFinal();
  
  public abstract boolean isOverridable();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\MethodMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */