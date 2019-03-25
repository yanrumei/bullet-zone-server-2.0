package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

public abstract interface ImportAware
  extends Aware
{
  public abstract void setImportMetadata(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\ImportAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */