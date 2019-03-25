package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

public abstract interface MetadataReaderFactory
{
  public abstract MetadataReader getMetadataReader(String paramString)
    throws IOException;
  
  public abstract MetadataReader getMetadataReader(Resource paramResource)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\type\classreading\MetadataReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */