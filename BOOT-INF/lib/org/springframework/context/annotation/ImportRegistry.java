package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

abstract interface ImportRegistry
{
  public abstract AnnotationMetadata getImportingClassFor(String paramString);
  
  public abstract void removeImportingClass(String paramString);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ImportRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */