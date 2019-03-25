package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract interface ResourcePatternResolver
  extends ResourceLoader
{
  public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
  
  public abstract Resource[] getResources(String paramString)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\io\support\ResourcePatternResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */