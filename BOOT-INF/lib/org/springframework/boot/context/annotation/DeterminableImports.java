package org.springframework.boot.context.annotation;

import java.util.Set;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface DeterminableImports
{
  public abstract Set<Object> determineImports(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\annotation\DeterminableImports.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */