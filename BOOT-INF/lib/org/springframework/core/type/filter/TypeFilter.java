package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public abstract interface TypeFilter
{
  public abstract boolean match(MetadataReader paramMetadataReader, MetadataReaderFactory paramMetadataReaderFactory)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\filter\TypeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */