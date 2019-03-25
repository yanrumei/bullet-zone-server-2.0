package org.springframework.core.type;

public abstract interface ClassMetadata
{
  public abstract String getClassName();
  
  public abstract boolean isInterface();
  
  public abstract boolean isAnnotation();
  
  public abstract boolean isAbstract();
  
  public abstract boolean isConcrete();
  
  public abstract boolean isFinal();
  
  public abstract boolean isIndependent();
  
  public abstract boolean hasEnclosingClass();
  
  public abstract String getEnclosingClassName();
  
  public abstract boolean hasSuperClass();
  
  public abstract String getSuperClassName();
  
  public abstract String[] getInterfaceNames();
  
  public abstract String[] getMemberClassNames();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\ClassMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */