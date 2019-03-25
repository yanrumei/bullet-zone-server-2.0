package org.springframework.core.type;

import java.util.Set;

public abstract interface AnnotationMetadata
  extends ClassMetadata, AnnotatedTypeMetadata
{
  public abstract Set<String> getAnnotationTypes();
  
  public abstract Set<String> getMetaAnnotationTypes(String paramString);
  
  public abstract boolean hasAnnotation(String paramString);
  
  public abstract boolean hasMetaAnnotation(String paramString);
  
  public abstract boolean hasAnnotatedMethods(String paramString);
  
  public abstract Set<MethodMetadata> getAnnotatedMethods(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\AnnotationMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */