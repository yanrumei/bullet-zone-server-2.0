package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

public abstract interface PropertySourceFactory
{
  public abstract PropertySource<?> createPropertySource(String paramString, EncodedResource paramEncodedResource)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\support\PropertySourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */