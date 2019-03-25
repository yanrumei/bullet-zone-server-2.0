package org.springframework.core.type.classreading;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

public abstract interface MetadataReader
{
  public abstract Resource getResource();
  
  public abstract ClassMetadata getClassMetadata();
  
  public abstract AnnotationMetadata getAnnotationMetadata();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\MetadataReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */