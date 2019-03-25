package org.hibernate.validator.internal.metadata.core;

import java.lang.reflect.Member;

public abstract interface AnnotationProcessingOptions
{
  public abstract boolean areClassLevelConstraintsIgnoredFor(Class<?> paramClass);
  
  public abstract boolean areMemberConstraintsIgnoredFor(Member paramMember);
  
  public abstract boolean areReturnValueConstraintsIgnoredFor(Member paramMember);
  
  public abstract boolean areCrossParameterConstraintsIgnoredFor(Member paramMember);
  
  public abstract boolean areParameterConstraintsIgnoredFor(Member paramMember, int paramInt);
  
  public abstract void merge(AnnotationProcessingOptions paramAnnotationProcessingOptions);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\core\AnnotationProcessingOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */