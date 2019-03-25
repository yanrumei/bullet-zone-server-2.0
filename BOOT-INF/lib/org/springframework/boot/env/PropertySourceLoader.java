package org.springframework.boot.env;

import java.io.IOException;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

public abstract interface PropertySourceLoader
{
  public abstract String[] getFileExtensions();
  
  public abstract PropertySource<?> load(String paramString1, Resource paramResource, String paramString2)
    throws IOException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\PropertySourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */